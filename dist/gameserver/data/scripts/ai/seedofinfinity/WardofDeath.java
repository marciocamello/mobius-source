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
package ai.seedofinfinity;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

public class WardofDeath extends DefaultAI
{
	private static final int[] mobs =
	{
		22516,
		22520,
		22522,
		22524
	};
	
	public WardofDeath(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	@Override
	protected boolean checkAggression(Creature target)
	{
		NpcInstance actor = getActor();
		if (target.isInRange(actor, actor.getAggroRange()) && target.isPlayable() && !target.isDead() && !target.isInvisible())
		{
			if (actor.getNpcId() == 18667)
			{
				actor.doCast(SkillTable.getInstance().getInfo(Rnd.get(5423, 5424), 9), actor, false);
				actor.doDie(null);
			}
			else if (actor.getNpcId() == 18668)
			{
				for (int i = 0; i < Rnd.get(1, 4); i++)
				{
					actor.getReflection().addSpawnWithoutRespawn(mobs[Rnd.get(mobs.length)], actor.getLoc(), 100);
				}
				actor.doDie(null);
			}
		}
		return true;
	}
}
