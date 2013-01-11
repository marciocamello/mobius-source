/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package npc.model;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.templates.npc.MinionData;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.ArrayUtils;

public class SeedOfAnnihilationInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int[] BISTAKON_MOBS = new int[]
	{
		22750,
		22751,
		22752,
		22753
	};
	private static final int[] COKRAKON_MOBS = new int[]
	{
		22763,
		22764,
		22765
	};
	private static final int[][] BISTAKON_MINIONS = new int[][]
	{
		{
			22746,
			22746,
			22746
		},
		{
			22747,
			22747,
			22747
		},
		{
			22748,
			22748,
			22748
		},
		{
			22749,
			22749,
			22749
		}
	};
	private static final int[][] COKRAKON_MINIONS = new int[][]
	{
		{
			22760,
			22760,
			22761
		},
		{
			22760,
			22760,
			22762
		},
		{
			22761,
			22761,
			22760
		},
		{
			22761,
			22761,
			22762
		},
		{
			22762,
			22762,
			22760
		},
		{
			22762,
			22762,
			22761
		}
	};
	
	public SeedOfAnnihilationInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		if (ArrayUtils.contains(BISTAKON_MOBS, template.getNpcId()))
		{
			addMinions(BISTAKON_MINIONS[Rnd.get(BISTAKON_MINIONS.length)], template);
		}
		else if (ArrayUtils.contains(COKRAKON_MOBS, template.getNpcId()))
		{
			addMinions(COKRAKON_MINIONS[Rnd.get(COKRAKON_MINIONS.length)], template);
		}
	}
	
	private static void addMinions(int[] minions, NpcTemplate template)
	{
		if ((minions != null) && (minions.length > 0))
		{
			for (int id : minions)
			{
				template.addMinion(new MinionData(id, 1));
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
