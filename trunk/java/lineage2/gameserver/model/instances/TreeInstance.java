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

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TreeInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is -3990686488577795700)
	 */
	private static final long serialVersionUID = -3990686488577795700L;
	/**
	 * Field _owner.
	 */
	private final Player _owner;
	/**
	 * Field _skill.
	 */
	final Skill _skill;
	/**
	 * Field _lifetimeCountdown.
	 */
	private final int _lifetimeCountdown;
	/**
	 * Field _targetTask.
	 */
	private ScheduledFuture<?> _targetTask;
	/**
	 * Field _destroyTask.
	 */
	private ScheduledFuture<?> _destroyTask;
	
	/**
	 * Constructor for TreeInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param lifetime int
	 * @param skill Skill
	 */
	public TreeInstance(int objectId, NpcTemplate template, Player owner, int lifetime, Skill skill)
	{
		this(objectId, template, owner, lifetime, skill, owner.getLoc());
	}
	
	/**
	 * Constructor for TreeInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param lifetime int
	 * @param skill Skill
	 * @param loc Location
	 */
	public TreeInstance(int objectId, NpcTemplate template, Player owner, int lifetime, Skill skill, Location loc)
	{
		super(objectId, template);
		_owner = owner;
		_skill = skill;
		_lifetimeCountdown = lifetime;
		setLevel(owner.getLevel());
		setTitle(owner.getName());
		setLoc(loc);
		setHeading(owner.getHeading());
	}
	
	/**
	 * Method getOwner.
	 * @return Player
	 */
	public Player getOwner()
	{
		return _owner;
	}
	
	/**
	 * @author Mobius
	 */
	private static class CastTask extends RunnableImpl
	{
		/**
		 * Field _trapRef.
		 */
		private final HardReference<NpcInstance> _trapRef;
		
		/**
		 * Constructor for CastTask.
		 * @param trap TreeInstance
		 */
		public CastTask(TreeInstance trap)
		{
			_trapRef = trap.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			TreeInstance tree = (TreeInstance) _trapRef.get();
			if (tree == null)
			{
				return;
			}
			Player owner = tree.getOwner();
			if (owner == null)
			{
				return;
			}
			List<Creature> targets = new ArrayList<>(10);
			for (Player target : World.getAroundPlayers(tree, 600, 200))
			{
				if (targets.size() > 10)
				{
					break;
				}
				if (target == owner)
				{
					targets.add(target);
					tree.broadcastPacket(new MagicSkillUse(tree, target, tree._skill.getId(), tree._skill.getLevel(), 0, 0));
				}
				if ((target.getParty() != null) && (owner.getParty() == target.getParty()))
				{
					targets.add(target);
					tree.broadcastPacket(new MagicSkillUse(tree, target, tree._skill.getId(), tree._skill.getLevel(), 0, 0));
				}
			}
			tree.callSkill(tree._skill, targets, true);
		}
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_destroyTask = ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(this), _lifetimeCountdown);
		_targetTask = EffectTaskManager.getInstance().scheduleAtFixedRate(new CastTask(this), 1000L, 5000L);
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	protected void onDelete()
	{
		Player owner = getOwner();
		if (owner != null)
		{
			owner.setTree(false);
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
	
	/**
	 * Method hasRandomAnimation.
	 * @return boolean
	 */
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return false;
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return false;
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return true;
	}
	
	/**
	 * Method isFearImmune.
	 * @return boolean
	 */
	@Override
	public boolean isFearImmune()
	{
		return true;
	}
	
	/**
	 * Method isParalyzeImmune.
	 * @return boolean
	 */
	@Override
	public boolean isParalyzeImmune()
	{
		return true;
	}
	
	/**
	 * Method isLethalImmune.
	 * @return boolean
	 */
	@Override
	public boolean isLethalImmune()
	{
		return true;
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param filename String
	 * @param replace Object[]
	 */
	@Override
	public void showChatWindow(Player player, String filename, Object... replace)
	{
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
	}
	
	/**
	 * Method onAction.
	 * @param player Player
	 * @param shift boolean
	 */
	@Override
	public void onAction(Player player, boolean shift)
	{
		player.sendActionFailed();
	}
}
