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
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TreeInstance extends Summon
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
		super(objectId, template, owner);
		_owner = owner;
		_skill = skill;
		_lifetimeCountdown = lifetime;
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
	
	@Override
	public String getName()
	{
		return this.getTemplate().getName();
	}
	
	/**
	 * @author Mobius
	 */
	private static class CastTask extends RunnableImpl
	{
		/**
		 * Field _trapRef.
		 */
		private final HardReference<? extends Playable> _trapRef;
		
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
			
			if (tree._skill.isOffensive())
			{
				for (Creature target : World.getAroundCharacters(tree, 600, 200))
				{
					if (targets.size() > 10)
					{
						break;
					}
					if (target.isAutoAttackable(tree))
					{
						targets.add(target);
						tree.broadcastPacket(new MagicSkillUse(tree, target, tree._skill.getId(), tree._skill.getLevel(), tree._skill.getHitTime(), tree._skill.getReuseDelay()));
					}
				}
				tree.callSkill(tree._skill, targets, true);
			}
			else
			{
				for (Creature target : World.getAroundCharacters(tree, 600, 200))
				{
					if (targets.size() > 10)
					{
						break;
					}
					if (target == owner)
					{
						targets.add(target);
						tree.broadcastPacket(new MagicSkillUse(tree, target, tree._skill.getId(), tree._skill.getLevel(), tree._skill.getHitTime(), tree._skill.getReuseDelay()));
					}
					if ((target instanceof Player) && (((Player) target).getParty() != null) && (owner.getParty() == ((Player) target).getParty()))
					{
						targets.add(target);
						tree.broadcastPacket(new MagicSkillUse(tree, target, tree._skill.getId(), tree._skill.getLevel(), tree._skill.getHitTime(), tree._skill.getReuseDelay()));
					}
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
		getPlayer().getSummonList().removeSummon(this);
		super.onDelete();
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		saveEffects();
		if (_destroyTask != null)
		{
			_destroyTask.cancel(false);
			_destroyTask = null;
		}
		if (_targetTask != null)
		{
			_targetTask.cancel(false);
		}
		getPlayer().getSummonList().removeSummon(this);
	}
	
	/**
	 * Method stopDisappear.
	 */
	protected synchronized void stopDisappear()
	{
		if (_destroyTask != null)
		{
			_destroyTask.cancel(false);
			_destroyTask = null;
		}
		if (_targetTask != null)
		{
			_targetTask.cancel(false);
		}
		getPlayer().getSummonList().removeSummon(this);
	}
	
	@Override
	public Player getPlayer()
	{
		return _owner;
	}
	
	/**
	 * Method unSummon.
	 */
	@Override
	public void unSummon()
	{
		stopDisappear();
		super.unSummon();
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
	
	@Override
	public int getSummonType()
	{
		return 0;
	}
	
	@Override
	public int getSummonSkillId()
	{
		return 0;
	}
	
	@Override
	public int getSummonSkillLvl()
	{
		return 0;
	}
	
	@Override
	public int getCurrentFed()
	{
		return 0;
	}
	
	@Override
	public int getMaxFed()
	{
		return 0;
	}
	
	@Override
	public int getSummonPoint()
	{
		return 1;
	}
	
	@Override
	public void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic)
	{
		
	}
	
	/**
	 * Method displayReceiveDamageMessage.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	public void displayReceiveDamageMessage(Creature attacker, int damage)
	{
		Player owner = getPlayer();
		owner.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RECEIVED_DAMAGE_OF_S3_FROM_C2).addName(this).addName(attacker).addNumber((long) damage));
	}
	
	@Override
	public double getExpPenalty()
	{
		return 0;
	}
	
	@Override
	public long getWearedMask()
	{
		return 0;
	}
	
	@Override
	public int getLevel()
	{
		return getTemplate().level;
	}
	
	/**
	 * Method isServitor.
	 * @return boolean
	 */
	@Override
	public boolean isServitor()
	{
		return true;
	}
	
	/**
	 * Method onAction.
	 * @param player Player
	 * @param shift boolean
	 */
	@Override
	public void onAction(Player player, boolean shift)
	{
		super.onAction(player, shift);
		if (shift)
		{
			if (!player.getPlayerAccess().CanViewChar)
			{
				return;
			}
			String dialog;
			dialog = HtmCache.getInstance().getNotNull("scripts/actions/admin.L2SummonInstance.onActionShift.htm", player);
			dialog = dialog.replaceFirst("%name%", String.valueOf(getName()));
			dialog = dialog.replaceFirst("%level%", String.valueOf(getLevel()));
			dialog = dialog.replaceFirst("%class%", String.valueOf(getClass().getSimpleName().replaceFirst("L2", "").replaceFirst("Instance", "")));
			dialog = dialog.replaceFirst("%xyz%", getLoc().x + " " + getLoc().y + " " + getLoc().z);
			dialog = dialog.replaceFirst("%heading%", String.valueOf(getLoc().h));
			dialog = dialog.replaceFirst("%owner%", String.valueOf(getPlayer().getName()));
			dialog = dialog.replaceFirst("%ownerId%", String.valueOf(getPlayer().getObjectId()));
			dialog = dialog.replaceFirst("%npcId%", String.valueOf(getNpcId()));
			dialog = dialog.replaceFirst("%expPenalty%", String.valueOf(getExpPenalty()));
			dialog = dialog.replaceFirst("%maxHp%", String.valueOf(getMaxHp()));
			dialog = dialog.replaceFirst("%maxMp%", String.valueOf(getMaxMp()));
			dialog = dialog.replaceFirst("%currHp%", String.valueOf((int) getCurrentHp()));
			dialog = dialog.replaceFirst("%currMp%", String.valueOf((int) getCurrentMp()));
			dialog = dialog.replaceFirst("%pDef%", String.valueOf(getPDef(null)));
			dialog = dialog.replaceFirst("%mDef%", String.valueOf(getMDef(null, null)));
			dialog = dialog.replaceFirst("%pAtk%", String.valueOf(getPAtk(null)));
			dialog = dialog.replaceFirst("%mAtk%", String.valueOf(getMAtk(null, null)));
			dialog = dialog.replaceFirst("%accuracy%", String.valueOf(getAccuracy()));
			dialog = dialog.replaceFirst("%evasionRate%", String.valueOf(getEvasionRate(null)));
			dialog = dialog.replaceFirst("%crt%", String.valueOf(getCriticalHit(null, null)));
			dialog = dialog.replaceFirst("%runSpeed%", String.valueOf(getRunSpeed()));
			dialog = dialog.replaceFirst("%walkSpeed%", String.valueOf(getWalkSpeed()));
			dialog = dialog.replaceFirst("%pAtkSpd%", String.valueOf(getPAtkSpd()));
			dialog = dialog.replaceFirst("%mAtkSpd%", String.valueOf(getMAtkSpd()));
			dialog = dialog.replaceFirst("%dist%", String.valueOf((int) getRealDistance(player)));
			dialog = dialog.replaceFirst("%STR%", String.valueOf(getSTR()));
			dialog = dialog.replaceFirst("%DEX%", String.valueOf(getDEX()));
			dialog = dialog.replaceFirst("%CON%", String.valueOf(getCON()));
			dialog = dialog.replaceFirst("%INT%", String.valueOf(getINT()));
			dialog = dialog.replaceFirst("%WIT%", String.valueOf(getWIT()));
			dialog = dialog.replaceFirst("%MEN%", String.valueOf(getMEN()));
			NpcHtmlMessage msg = new NpcHtmlMessage(5);
			msg.setHtml(dialog);
			player.sendPacket(msg);
		}
	}
}
