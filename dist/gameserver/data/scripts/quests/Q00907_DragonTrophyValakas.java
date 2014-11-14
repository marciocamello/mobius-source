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

public class Q00907_DragonTrophyValakas extends Quest implements ScriptFile
{
	// Npc
	private static final int Klein = 31540;
	// Monster
	private static final int Valakas = 29028;
	// Item
	private static final int MedalofGlory = 21874;
	
	public Q00907_DragonTrophyValakas()
	{
		super(PARTY_ALL);
		addStartNpc(Klein);
		addKillId(Valakas);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "klein_q907_04.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "klein_q907_07.htm":
				qs.giveItems(MedalofGlory, 30);
				qs.setState(COMPLETED);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (qs.getState())
		{
			case CREATED:
				if (qs.getPlayer().getLevel() >= 84)
				{
					if (qs.getQuestItemsCount(7267) > 0)
					{
						htmltext = "klein_q907_01.htm";
					}
					else
					{
						htmltext = "klein_q907_00b.htm";
					}
				}
				else
				{
					htmltext = "klein_q907_00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case STARTED:
				if (cond == 1)
				{
					htmltext = "klein_q907_05.htm";
				}
				else if (cond == 2)
				{
					htmltext = "klein_q907_06.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			if (npc.getId() == Valakas)
			{
				qs.setCond(2);
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
