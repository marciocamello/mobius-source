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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.network.serverpackets.SetSummonRemainTime;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SummonInstance extends Summon
{
	public final int CYCLE = 5000;
	private final int _summonSkillId;
	private final int _summonSkillLvl;
	private double _expPenalty = 0;
	Future<?> _disappearTask;
	int _lifetimeCountdown;
	private int _maxLifetime;
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
				owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_SERVITOR_HAS_VANISHED_YOU_LL_NEED_TO_SUMMON_A_NEW_ONE));
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
			owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SUMMONED_MONSTER_S_CRITICAL_HIT));
		}
		
		if (miss)
		{
			owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_S_ATTACK_WENT_ASTRAY).addCharName(this));
		}
		else if (!target.isInvul())
		{
			owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_INFLICTED_S3_DAMAGE_ON_C2_S4).addCharName(this).addCharName(target).addInt(damage).addPopup(target.getId(), target.getId(), -damage));
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
		owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_RECEIVED_S3_DAMAGE_FROM_C2).addCharName(this).addCharName(attacker).addInt(damage).addPopup(getId(), attacker.getId(), -damage));
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
	public boolean isSummon()
	{
		return true;
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
