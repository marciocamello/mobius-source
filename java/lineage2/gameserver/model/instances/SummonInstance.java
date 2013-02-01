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

import java.util.concurrent.Future;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SetSummonRemainTime;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SummonInstance extends Summon
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field CYCLE.
	 */
	public final int CYCLE = 5000;
	/**
	 * Field _summonSkillId.
	 */
	private final int _summonSkillId;
	/**
	 * Field _summonSkillLvl.
	 */
	private final int _summonSkillLvl;
	/**
	 * Field _expPenalty.
	 */
	private double _expPenalty = 0;
	/**
	 * Field _disappearTask.
	 */
	Future<?> _disappearTask;
	/**
	 * Field _lifetimeCountdown.
	 */
	int _lifetimeCountdown;
	/**
	 * Field _maxLifetime.
	 */
	private int _maxLifetime;
	/**
	 * Field _summonPoint.
	 */
	private final int _summonPoint;
	
	/**
	 * Constructor for SummonInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param owner Player
	 * @param lifetime int
	 * @param summonPoint int
	 * @param skill Skill
	 */
	public SummonInstance(int objectId, NpcTemplate template, Player owner, int lifetime, int summonPoint, Skill skill)
	{
		super(objectId, template, owner);
		setName(template.name);
		_lifetimeCountdown = _maxLifetime = lifetime;
		_summonSkillId = skill.getDisplayId();
		_summonSkillLvl = skill.getLevel();
		_disappearTask = ThreadPoolManager.getInstance().schedule(new Lifetime(), CYCLE);
		_summonPoint = summonPoint;
	}
	
	/**
	 * Method getRef.
	 * @return HardReference<SummonInstance>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HardReference<SummonInstance> getRef()
	{
		return (HardReference<SummonInstance>) super.getRef();
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	@Override
	public final int getLevel()
	{
		return getTemplate() != null ? getTemplate().level : 0;
	}
	
	/**
	 * Method getSummonType.
	 * @return int
	 */
	@Override
	public int getSummonType()
	{
		return 1;
	}
	
	/**
	 * Method getCurrentFed.
	 * @return int
	 */
	@Override
	public int getCurrentFed()
	{
		return _lifetimeCountdown;
	}
	
	/**
	 * Method getMaxFed.
	 * @return int
	 */
	@Override
	public int getMaxFed()
	{
		return _maxLifetime;
	}
	
	/**
	 * Method setExpPenalty.
	 * @param expPenalty double
	 */
	public void setExpPenalty(double expPenalty)
	{
		_expPenalty = expPenalty;
	}
	
	/**
	 * Method getExpPenalty.
	 * @return double
	 */
	@Override
	public double getExpPenalty()
	{
		return _expPenalty;
	}
	
	/**
	 * @author Mobius
	 */
	class Lifetime extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player owner = getPlayer();
			if (owner == null)
			{
				_disappearTask = null;
				unSummon();
				return;
			}
			int usedtime = isInCombat() ? CYCLE : CYCLE / 4;
			_lifetimeCountdown -= usedtime;
			if (_lifetimeCountdown <= 0)
			{
				owner.sendPacket(Msg.SERVITOR_DISAPPEASR_BECAUSE_THE_SUMMONING_TIME_IS_OVER);
				_disappearTask = null;
				unSummon();
				return;
			}
			owner.sendPacket(new SetSummonRemainTime(SummonInstance.this));
			_disappearTask = ThreadPoolManager.getInstance().schedule(this, CYCLE);
		}
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
		if (_disappearTask != null)
		{
			_disappearTask.cancel(false);
			_disappearTask = null;
		}
		getPlayer().getSummonList().removeSummon(this);
	}
	
	/**
	 * Method stopDisappear.
	 */
	protected synchronized void stopDisappear()
	{
		if (_disappearTask != null)
		{
			_disappearTask.cancel(false);
			_disappearTask = null;
		}
		getPlayer().getSummonList().removeSummon(this);
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
	 * Method getSummonPoint.
	 * @return int
	 */
	@Override
	public int getSummonPoint()
	{
		return _summonPoint;
	}
	
	/**
	 * Method displayGiveDamageMessage.
	 * @param target Creature
	 * @param damage int
	 * @param crit boolean
	 * @param miss boolean
	 * @param shld boolean
	 * @param magic boolean
	 */
	@Override
	public void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic)
	{
		Player owner = getPlayer();
		if (owner == null)
		{
			return;
		}
		if (crit)
		{
			owner.sendPacket(SystemMsg.SUMMONED_MONSTERS_CRITICAL_HIT);
		}
		if (miss)
		{
			owner.sendPacket(new SystemMessage(SystemMessage.C1S_ATTACK_WENT_ASTRAY).addName(this));
		}
		else if (!target.isInvul())
		{
			owner.sendPacket(new SystemMessage(SystemMessage.C1_HAS_GIVEN_C2_DAMAGE_OF_S3).addName(this).addName(target).addNumber(damage));
		}
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
	
	/**
	 * Method getSummonSkillId.
	 * @return int
	 */
	@Override
	public int getSummonSkillId()
	{
		return _summonSkillId;
	}
	
	/**
	 * Method getSummonSkillLvl.
	 * @return int
	 */
	@Override
	public int getSummonSkillLvl()
	{
		return _summonSkillLvl;
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
	
	/**
	 * Method getWearedMask.
	 * @return long
	 */
	@Override
	public long getWearedMask()
	{
		return WeaponType.SWORD.mask();
	}
}
