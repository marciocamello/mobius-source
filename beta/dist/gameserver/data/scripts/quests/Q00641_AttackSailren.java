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
package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00641_AttackSailren extends Quest implements ScriptFile
{
	// Npc
	private static final int STATUE = 32109;
	// Monsters
	private static final int VEL1 = 22196;
	private static final int VEL2 = 22197;
	private static final int VEL3 = 22198;
	private static final int VEL4 = 22218;
	private static final int VEL5 = 22223;
	private static final int PTE = 22199;
	// Items
	private static final int FRAGMENTS = 8782;
	private static final int GAZKH = 8784;
	
	public Q00641_AttackSailren()
	{
		super(true);
		addStartNpc(STATUE);
		addKillId(VEL1, VEL2, VEL3, VEL4, VEL5, PTE);
		addQuestItem(FRAGMENTS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "statue_of_shilen_q0641_05.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "statue_of_shilen_q0641_08.htm":
				qs.playSound(SOUND_FINISH);
				qs.takeItems(FRAGMENTS, -1);
				qs.giveItems(GAZKH, 1);
				qs.addExpAndSp(1500000, 1650000);
				qs.exitCurrentQuest(true);
				qs.unset("cond");
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				final QuestState state = qs.getPlayer().getQuestState(Q00126_TheNameOfEvilPart2.class);
				if ((state == null) || !state.isCompleted())
				{
					htmltext = "statue_of_shilen_q0641_02.htm";
				}
				else if (qs.getPlayer().getLevel() >= 77)
				{
					htmltext = "statue_of_shilen_q0641_01.htm";
				}
				else
				{
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "statue_of_shilen_q0641_05.htm";
				break;
			
			case 2:
				htmltext = "statue_of_shilen_q0641_07.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getQuestItemsCount(FRAGMENTS) < 30)
		{
			qs.giveItems(FRAGMENTS, 1);
			
			if (qs.getQuestItemsCount(FRAGMENTS) == 30)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
				qs.setState(STARTED);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
			}
		}
		
		return null;
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}
