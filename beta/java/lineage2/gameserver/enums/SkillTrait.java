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
package lineage2.gameserver.enums;

import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum SkillTrait
{
	NONE,
	BLEED
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.BLEED_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.BLEED_POWER, env.target, env.skill);
		}
	},
	BOSS,
	DEATH,
	DERANGEMENT
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.MENTAL_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return Math.min(40., env.character.calcStat(Stats.MENTAL_POWER, env.target, env.skill) + calcEnchantMod(env));
		}
	},
	ETC,
	GUST,
	HOLD
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.ROOT_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.ROOT_POWER, env.target, env.skill);
		}
	},
	PARALYZE
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.PARALYZE_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.PARALYZE_POWER, env.target, env.skill);
		}
	},
	PHYSICAL_BLOCKADE,
	AIRJOKE
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.AIRJOKE_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.AIRJOKE_POWER, env.target, env.skill);
		}
	},
	MUTATE
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.MUTATE_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.MUTATE_POWER, env.target, env.skill);
		}
	},
	DISARM
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.DISARM_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.DISARM_POWER, env.target, env.skill);
		}
	},
	PULL
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.PULL_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.PULL_POWER, env.target, env.skill);
		}
	},
	POISON
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.POISON_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.POISON_POWER, env.target, env.skill);
		}
	},
	SHOCK
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.STUN_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return Math.min(40., env.character.calcStat(Stats.STUN_POWER, env.target, env.skill) + calcEnchantMod(env));
		}
	},
	SLEEP
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.SLEEP_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.SLEEP_POWER, env.target, env.skill);
		}
	},
	VALAKAS,
	KNOCKBACK
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.KNOCKBACK_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.KNOCKBACK_POWER, env.target, env.skill);
		}
	},
	KNOCKDOWN
	{
		@Override
		public final double calcVuln(Env env)
		{
			return env.target.calcStat(Stats.KNOCKDOWN_RESIST, env.character, env.skill);
		}
		
		@Override
		public final double calcProf(Env env)
		{
			return env.character.calcStat(Stats.KNOCKDOWN_POWER, env.target, env.skill);
		}
	};
	/**
	 * Method calcVuln.
	 * @param env Env
	 * @return double
	 */
	public double calcVuln(Env env)
	{
		return 0;
	}
	
	/**
	 * Method calcProf.
	 * @param env Env
	 * @return double
	 */
	public double calcProf(Env env)
	{
		return 0;
	}
	
	/**
	 * Method calcEnchantMod.
	 * @param env Env
	 * @return double
	 */
	public static double calcEnchantMod(Env env)
	{
		int enchantLevel = env.skill.getDisplayLevel();
		
		if (enchantLevel <= 100)
		{
			return 0;
		}
		
		enchantLevel = enchantLevel % 100;
		return env.skill.getEnchantLevelCount() == 15 ? enchantLevel * 2 : enchantLevel;
	}
}
