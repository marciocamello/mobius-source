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
package lineage2.gameserver.skills.effects;

import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

public class EffectDamageHealToEffectorAndPets extends EffectDamOverTime
{
	private DamageListener damageListener;
	
	private class DamageListener implements OnCurrentHpDamageListener
	{
		public DamageListener()
		{
		}
		
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			int effectorCount = _effector.getPlayer().getPets().size() + 1;
			double forEachHeal = (damage * _effected.calcStat(Stats.DAMAGE_HEAL_TO_EFFECTOR, 0.0D)) / 100.0D / effectorCount;
			double forEachHealMp = (damage * _effected.calcStat(Stats.DAMAGE_HEAL_MP_TO_EFFECTOR, 0.0D)) / 100.0D / effectorCount;
			for (Summon pet : _effector.getPlayer().getPets())
			{
				if (forEachHeal > 0.0D)
				{
					pet.setCurrentHp(pet.getCurrentHp() + forEachHeal, false);
				}
				if (forEachHealMp > 0.0D)
				{
					pet.setCurrentMp(pet.getCurrentMp() + forEachHealMp);
				}
			}
			if (forEachHeal > 0.0D)
			{
				_effector.setCurrentHp(_effector.getCurrentHp() + forEachHeal, false);
				_effector.sendPacket(new SystemMessage2(SystemMsg.S1_HP_HAS_BEEN_RESTORED).addInteger(Math.round(forEachHeal)));
			}
			if (forEachHealMp > 0.0D)
			{
				_effector.setCurrentMp(_effector.getCurrentMp() + forEachHealMp);
				_effector.sendPacket(new SystemMessage2(SystemMsg.S1_MP_HAS_BEEN_RESTORED).addInteger(Math.round(forEachHealMp)));
			}
		}
	}
	
	public EffectDamageHealToEffectorAndPets(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public void onStart()
	{
		_effected.addListener(damageListener = new DamageListener());
		super.onStart();
	}
	
	@Override
	public boolean onActionTime()
	{
		if (_effected.isDead())
		{
			if (damageListener != null)
			{
				_effected.removeListener(damageListener);
			}
			return false;
		}
		
		return super.onActionTime();
	}
	
	@Override
	public void onExit()
	{
		if (damageListener != null)
		{
			_effected.removeListener(damageListener);
		}
		super.onExit();
	}
}
