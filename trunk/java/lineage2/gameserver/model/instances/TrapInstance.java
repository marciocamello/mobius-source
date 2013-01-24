/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Skill.SkillTargetType;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MyTargetSelected;
import lineage2.gameserver.network.serverpackets.NpcInfo;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

public final class TrapInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static class CastTask extends RunnableImpl
	{
		private final HardReference<NpcInstance> _trapRef;
		
		public CastTask(TrapInstance trap)
		{
			_trapRef = trap.getRef();
		}
		
		@Override
		public void runImpl()
		{
			TrapInstance trap = (TrapInstance) _trapRef.get();
			if (trap == null)
			{
				return;
			}
			Creature owner = trap.getOwner();
			if (owner == null)
			{
				return;
			}
			if (trap._skill == null)
			{
				System.out.println("ERROR IN TRAP SKILL");
				trap.deleteMe();
				return;
			}
			for (Creature target : trap.getAroundCharacters(200, 200))
			{
				if (target != owner)
				{
					if (trap._skill.checkTarget(owner, target, null, false, false) == null)
					{
						List<Creature> targets = new ArrayList<>();
						if (trap._skill.getTargetType() != SkillTargetType.TARGET_AREA)
						{
							targets.add(target);
						}
						else
						{
							for (Creature t : trap.getAroundCharacters(trap._skill.getSkillRadius(), 128))
							{
								if (trap._skill.checkTarget(owner, t, null, false, false) == null)
								{
									targets.add(target);
								}
							}
						}
						trap._skill.useSkill(trap, targets);
						if (target.isPlayer())
						{
							target.sendMessage(new CustomMessage("common.Trap", target.getPlayer()));
						}
						trap.deleteMe();
						break;
					}
				}
			}
		}
	}
	
	private final HardReference<? extends Creature> _ownerRef;
	final Skill _skill;
	private ScheduledFuture<?> _targetTask;
	private ScheduledFuture<?> _destroyTask;
	private boolean _detected;
	
	public TrapInstance(int objectId, NpcTemplate template, Creature owner, Skill skill)
	{
		this(objectId, template, owner, skill, owner.getLoc());
	}
	
	public TrapInstance(int objectId, NpcTemplate template, Creature owner, Skill skill, Location loc)
	{
		super(objectId, template);
		_ownerRef = owner.getRef();
		_skill = skill;
		setReflection(owner.getReflection());
		setLevel(owner.getLevel());
		setTitle(owner.getName());
		setLoc(loc);
	}
	
	@Override
	public boolean isTrap()
	{
		return true;
	}
	
	public Creature getOwner()
	{
		return _ownerRef.get();
	}
	
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), 120000L);
		_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new CastTask(this), 250L, 250L);
	}
	
	@Override
	public void broadcastCharInfo()
	{
		if (!isDetected())
		{
			return;
		}
		super.broadcastCharInfo();
	}
	
	@Override
	protected void onDelete()
	{
		Creature owner = getOwner();
		if ((owner != null) && owner.isPlayer())
		{
			((Player) owner).removeTrap(this);
		}
		if (_destroyTask != null)
		{
			_destroyTask.cancel(false);
		}
		_destroyTask = null;
		if (_targetTask != null)
		{
			_targetTask.cancel(false);
		}
		_targetTask = null;
		super.onDelete();
	}
	
	public boolean isDetected()
	{
		return _detected;
	}
	
	public void setDetected(boolean detected)
	{
		_detected = detected;
	}
	
	@Override
	public int getPAtk(Creature target)
	{
		Creature owner = getOwner();
		return owner == null ? 0 : owner.getPAtk(target);
	}
	
	@Override
	public int getMAtk(Creature target, Skill skill)
	{
		Creature owner = getOwner();
		return owner == null ? 0 : owner.getMAtk(target, skill);
	}
	
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
	
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return false;
	}
	
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return false;
	}
	
	@Override
	public boolean isInvul()
	{
		return true;
	}
	
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	@Override
	public boolean isLethalImmune()
	{
		return true;
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
	}
	
	@Override
	public void showChatWindow(Player player, String filename, Object... replace)
	{
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
	}
	
	@Override
	public void onAction(Player player, boolean shift)
	{
		if (player.getTarget() != this)
		{
			player.setTarget(this);
			if (player.getTarget() == this)
			{
				player.sendPacket(new MyTargetSelected(getObjectId(), player.getLevel()));
			}
		}
		player.sendActionFailed();
	}
	
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		if (!isDetected() && (getOwner() != forPlayer))
		{
			return Collections.emptyList();
		}
		return Collections.<L2GameServerPacket> singletonList(new NpcInfo(this, forPlayer));
	}
}
