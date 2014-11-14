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

public class Q00904_DragonTrophyAntharas extends Quest implements ScriptFile
{
	private static final int Theodric = 30755;
	private static final int AntharasMax = 29068;
	private static final int MedalofGlory = 21874;
	
	public Q00904_DragonTrophyAntharas()
	{
		super(PARTY_ALL);
		addStartNpc(Theodric);
		addKillId(AntharasMax);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "theodric_q904_04.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "theodric_q904_07.htm":
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
					if (qs.getQuestItemsCount(3865) > 0)
					{
						htmltext = "theodric_q904_01.htm";
					}
					else
					{
						htmltext = "theodric_q904_00b.htm";
					}
				}
				else
				{
					htmltext = "theodric_q904_00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case STARTED:
				if (cond == 1)
				{
					htmltext = "theodric_q904_05.htm";
				}
				else if (cond == 2)
				{
					htmltext = "theodric_q904_06.htm";
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
			if (npc.getId() == AntharasMax)
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
