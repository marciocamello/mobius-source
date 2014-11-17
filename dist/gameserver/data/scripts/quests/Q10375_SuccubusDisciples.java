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

public class Q10375_SuccubusDisciples extends Quest implements ScriptFile
{
	private static final int NPC_ZENYA = 32140;
	private static final int NightmareDeath = 23191;
	private static final int NightmareofDarkness = 23192;
	private static final int NightmareofMadness = 23197;
	private static final int NightmareofSilence = 23198;
	private static final String Nightmare_Death = "NightmareDeath";
	private static final String Nightmare_of_Darkness = "NightmareofDarkness";
	private static final String Nightmare_of_Madness = "NightmareofMadness";
	private static final String Nightmare_of_Silence = "NightmareofSilence";
	
	public Q10375_SuccubusDisciples()
	{
		super(false);
		addStartNpc(NPC_ZENYA);
		addKillNpcWithLog(1, Nightmare_Death, 5, NightmareDeath);
		addKillNpcWithLog(1, Nightmare_of_Darkness, 5, NightmareofDarkness);
		addKillNpcWithLog(3, Nightmare_of_Madness, 5, NightmareofMadness);
		addKillNpcWithLog(3, Nightmare_of_Silence, 5, NightmareofSilence);
		addLevelCheck(80, 99);
		addQuestCompletedCheck(Q10374_ThatPlaceSuccubus.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "32140-06.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32140-09.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getState())
		{
			case COMPLETED:
				htmltext = "32140-05.htm";
				break;
			
			case CREATED:
				if (qs.getPlayer().getLevel() >= 80)
				{
					final QuestState state = qs.getPlayer().getQuestState(Q10374_ThatPlaceSuccubus.class);
					
					if ((qs.getPlayer().getClassId().level() == 4) && (state != null) && state.isCompleted())
					{
						htmltext = "32140-01.htm";
					}
					else
					{
						htmltext = "32140-03.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "32140-04.htm";
				}
				break;
			
			case STARTED:
				if (qs.getCond() == 1)
				{
					htmltext = "32140-07.htm";
				}
				else if (qs.getCond() == 2)
				{
					htmltext = "32140-08.htm";
				}
				else if (qs.getCond() == 3)
				{
					htmltext = "32140-10.htm";
				}
				else if (qs.getCond() == 4)
				{
					htmltext = "32140-11.htm";
					qs.giveItems(57, 498700L);
					qs.addExpAndSp(24782300, 28102300);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (((qs.getCond() == 1) || (qs.getCond() == 3)) && updateKill(npc, qs))
		{
			if (qs.getCond() == 1)
			{
				qs.unset(Nightmare_Death);
				qs.unset(Nightmare_of_Darkness);
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
			}
			else if (qs.getCond() == 3)
			{
				qs.unset(Nightmare_of_Madness);
				qs.unset(Nightmare_of_Silence);
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
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
