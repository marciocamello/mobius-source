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
package lineage2.gameserver.model.base;

import lineage2.gameserver.Config;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Experience
{
	public final static long LEVEL[] =
	{
		-1L,
		0L,
		68L,
		363L,
		1168L,
		2884L,
		6038L,
		11287L,
		19423L,
		31378L,
		48229L,
		71202L,
		101677L,
		141193L,
		191454L,
		254330L,
		331867L,
		426288L,
		540000L,
		675596L,
		835862L,
		920357L,
		1015431L,
		1123336L,
		1246808L,
		1389235L,
		1554904L,
		1749413L,
		1980499L,
		2260321L,
		2634751L,
		2844287L,
		3093068L,
		3389496L,
		3744042L,
		4169902L,
		4683988L,
		5308556L,
		6074376L,
		7029248L,
		8342182L,
		8718976L,
		12842357L,
		14751932L,
		17009030L,
		19686117L,
		22875008L,
		26695470L,
		31312332L,
		36982854L,
		44659561L,
		48128727L,
		52277875L,
		57248635L,
		63216221L,
		70399827L,
		79078300L,
		89616178L,
		102514871L,
		118552044L,
		140517709L,
		153064754L,
		168231664L,
		186587702L,
		208840245L,
		235877658L,
		268833561L,
		309192920L,
		358998712L,
		421408669L,
		493177635L,
		555112374L,
		630494192L,
		722326994L,
		834354722L,
		971291524L,
		1139165674L,
		1345884863L,
		1602331019L,
		1902355477L,
		2288742870L,
		2703488268L,
		3174205601L,
		3708727539L,
		4316300702L,
		5008025097L,
		10985069426L,
		19192594397L,
		33533938399L,
		44373087147L,
		63751938490L,
		88688523458L,
		120224273113L,
		157133602347L,
		208513860393L,
		266769078393L,
		377839508352L,
		592791113370L,
		1016243369039L,
		1956916677389L,
		6178380725000L
	};
	
	/**
	 * Method penaltyModifier.
	 * @param count long
	 * @param percents double
	 * @return double
	 */
	public static double penaltyModifier(long count, double percents)
	{
		return Math.max(1. - ((count * percents) / 100), 0);
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public static int getMaxLevel()
	{
		return Config.ALT_MAX_LEVEL;
	}
	
	/**
	 * Method getMaxSubLevel.
	 * @return int
	 */
	public static int getMaxSubLevel()
	{
		return Config.ALT_MAX_SUB_LEVEL;
	}
	
	/**
	 * Method getLevel.
	 * @param thisExp long
	 * @return int
	 */
	public static int getLevel(long thisExp)
	{
		int level = 0;
		
		for (int i = 0; i < LEVEL.length; i++)
		{
			long exp = LEVEL[i];
			
			if (thisExp >= exp)
			{
				level = i;
			}
		}
		
		return level;
	}
	
	/**
	 * Method getExpForLevel.
	 * @param lvl int
	 * @return long
	 */
	public static long getExpForLevel(int lvl)
	{
		if (lvl >= Experience.LEVEL.length)
		{
			return 0;
		}
		
		return Experience.LEVEL[lvl];
	}
	
	/**
	 * Method getExpPercent.
	 * @param level int
	 * @param exp long
	 * @return double
	 */
	public static double getExpPercent(int level, long exp)
	{
		return ((exp - getExpForLevel(level)) / ((getExpForLevel(level + 1) - getExpForLevel(level)) / 100.0D)) * 0.01D;
	}
	
	/**
	 * Method getMaxDualLevel.
	 * @return int
	 */
	public static int getMaxDualLevel()
	{
		return Config.ALT_MAX_DUAL_SUB_LEVEL;
	}
}
