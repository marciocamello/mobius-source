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
package npc.model;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.templates.npc.MinionData;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Awakeninger + Nache
 */
public class CrystallHallCannonInstance extends MonsterInstance
{
	private static final int[] CANNON_CUSTOM = new int[]
	{
		19008
	};
	private static final int[] CANNON_DOOR = new int[]
	{
		19009
	};
	private static final int[][] MINIONS = new int[][]
	{
		{
			23012,
			23012,
			23012
		},
		{
			23011,
			23011,
			23011
		},
		{
			23010,
			23010,
			23010
		}
	};
	
	public CrystallHallCannonInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		if (ArrayUtils.contains(CANNON_CUSTOM, template.getId()))
		{
			addMinions(MINIONS[Rnd.get(MINIONS.length)], template);
		}
		else if (ArrayUtils.contains(CANNON_DOOR, template.getId()))
		{
			addMinions(MINIONS[Rnd.get(MINIONS.length)], template);
		}
	}
	
	private static void addMinions(int[] minions, NpcTemplate template)
	{
		if ((minions != null) && (minions.length > 0))
		{
			for (int id : minions)
			{
				template.addMinion(new MinionData(id, 3));
			}
		}
	}
	
	@Override
	protected void onDeath(Creature killer)
	{
		getMinionList().unspawnMinions();
		super.onDeath(killer);
		
	}
	
	@Override
	public boolean canChampion()
	{
		return false;
	}
}