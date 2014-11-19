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
package ai.Tauti;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Nache
 */
public class Zahak extends Fighter
{
	// Npcs
	private static final int TAUTI_NORMAL = 29233;
	private static final int TAUTI_EXTREME = 29234;
	private static final int ZAHAK = 19287;
	// Skill
	private static final Skill HEAL_TO_TAUTI = SkillTable.getInstance().getInfo(14625, 1);
	
	public Zahak(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return false;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		final NpcInstance npc = getActor();
		if ((npc.getId() == TAUTI_NORMAL) || (npc.getId() == TAUTI_EXTREME))
		{
			for (NpcInstance zahak : npc.getReflection().getAllByNpcId(ZAHAK, true))
			{
				zahak.doCast(HEAL_TO_TAUTI, npc, true);
				zahak.setRandomWalk(false);
			}
		}
		super.onEvtSpawn();
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		final NpcInstance npc = getActor();
		for (NpcInstance zahak : npc.getReflection().getAllByNpcId(ZAHAK, true))
		{
			zahak.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, target, 1000);
		}
		super.onEvtAggression(target, aggro);
	}
}