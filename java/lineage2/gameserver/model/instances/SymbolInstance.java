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
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class SymbolInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Creature _owner;
	final Skill _skill;
	private ScheduledFuture<?> _targetTask;
	private ScheduledFuture<?> _destroyTask;
	
	public SymbolInstance(int objectId, NpcTemplate template, Creature owner, Skill skill)
	{
		super(objectId, template);
		_owner = owner;
		_skill = skill;
		setReflection(owner.getReflection());
		setLevel(owner.getLevel());
		setTitle(owner.getName());
	}
	
	public Creature getOwner()
	{
		return _owner;
	}
	
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), 120000L);
		_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				for (Creature target : getAroundCharacters(200, 200))
				{
					if (_skill.checkTarget(_owner, target, null, false, false) == null)
					{
						List<Creature> targets = new ArrayList<>();
						if (!_skill.isAoE())
						{
							targets.add(target);
						}
						else
						{
							for (Creature t : getAroundCharacters(_skill.getSkillRadius(), 128))
							{
								if (_skill.checkTarget(_owner, t, null, false, false) == null)
								{
									targets.add(target);
								}
							}
						}
						_skill.useSkill(SymbolInstance.this, targets);
					}
				}
			}
		}, 1000L, Rnd.get(4000L, 7000L));
	}
	
	@Override
	protected void onDelete()
	{
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
		player.sendActionFailed();
	}
	
	@Override
	public Clan getClan()
	{
		return null;
	}
}
