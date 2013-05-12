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
package ai;

import instances.SpezionNormal;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author cruel
 */
public class SpezionMonster extends Mystic
{
	
	public SpezionMonster(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		if (actor.getNpcId() == 22985)
		{
			Reflection r = actor.getReflection();
			if (r instanceof SpezionNormal)
			{
				SpezionNormal spezion = (SpezionNormal) r;
				spezion.openGate(26190004);
			}
		}
		
		super.onEvtDead(killer);
	}
	
	@Override
	protected void thinkAttack()
	{
		NpcInstance actor = getActor();
		Creature randomHated = actor.getAggroList().getRandomHated();
		if (((randomHated != null) && (actor.getNpcId() == 22971)) || (actor.getNpcId() == 22972))
		{
			actor.doCast(SkillTable.getInstance().getInfo(14139, 1), randomHated, true);
		}
		super.thinkAttack();
	}
}
