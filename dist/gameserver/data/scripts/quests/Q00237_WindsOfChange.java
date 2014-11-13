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

public class Q00237_WindsOfChange extends Quest implements ScriptFile
{
	// Npcs
	private static final int Flauen = 30899;
	private static final int Iason = 30969;
	private static final int Roman = 30897;
	private static final int Morelyn = 30925;
	private static final int Helvetica = 32641;
	private static final int Athenia = 32643;
	// Items
	private static final int FlauensLetter = 14862;
	private static final int LetterToHelvetica = 14863;
	private static final int LetterToAthenia = 14864;
	private static final int VicinityOfTheFieldOfSilenceResearchCenter = 14865;
	private static final int CertificateOfSupport = 14866;
	
	public Q00237_WindsOfChange()
	{
		super(false);
		addStartNpc(Flauen);
		addTalkId(Iason, Roman, Morelyn, Helvetica, Athenia);
		addQuestItem(FlauensLetter, LetterToHelvetica, LetterToAthenia);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30899-06.htm":
				qs.giveItems(FlauensLetter, 1);
				qs.setCond(1);
				qs.setState(STARTED);
				break;
			
			case "30969-05.htm":
				qs.setCond(2);
				break;
			
			case "30897-03.htm":
				qs.setCond(3);
				break;
			
			case "30925-03.htm":
				qs.setCond(4);
				break;
			
			case "30969-09.htm":
				qs.giveItems(LetterToHelvetica, 1);
				qs.setCond(5);
				break;
			
			case "30969-10.htm":
				qs.giveItems(LetterToAthenia, 1);
				qs.setCond(6);
				break;
			
			case "32641-02.htm":
				qs.takeItems(LetterToHelvetica, -1);
				qs.giveItems(ADENA_ID, 499880);
				qs.addExpAndSp(2427030, 2786680);
				qs.giveItems(VicinityOfTheFieldOfSilenceResearchCenter, 1);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(false);
				break;
			
			case "32643-02.htm":
				qs.takeItems(LetterToAthenia, -1);
				qs.giveItems(ADENA_ID, 499880);
				qs.addExpAndSp(2427030, 2786680);
				qs.giveItems(CertificateOfSupport, 1);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		final int id = qs.getState();
		
		switch (npcId)
		{
			case Flauen:
				if (id == CREATED)
				{
					if (qs.getPlayer().getLevel() < 82)
					{
						qs.exitCurrentQuest(true);
						htmltext = "30899-00.htm";
					}
					else
					{
						htmltext = "30899-01.htm";
					}
				}
				else if (id == COMPLETED)
				{
					htmltext = "30899-09.htm";
				}
				else if (cond < 5)
				{
					htmltext = "30899-07.htm";
				}
				else
				{
					htmltext = "30899-08.htm";
				}
				break;
			
			case Iason:
				if (cond == 1)
				{
					qs.takeItems(FlauensLetter, -1);
					htmltext = "30969-01.htm";
				}
				else if ((cond > 1) && (cond < 4))
				{
					htmltext = "30969-06.htm";
				}
				else if (cond == 4)
				{
					htmltext = "30969-07.htm";
				}
				else if (cond > 4)
				{
					htmltext = "30969-11.htm";
				}
				break;
			
			case Roman:
				if (cond == 2)
				{
					htmltext = "30897-01.htm";
				}
				else if (cond > 2)
				{
					htmltext = "30897-04.htm";
				}
				break;
			
			case Morelyn:
				if (cond == 3)
				{
					htmltext = "30925-01.htm";
				}
				else if (cond > 3)
				{
					htmltext = "30925-04.htm";
				}
				break;
			
			case Helvetica:
				if (cond == 5)
				{
					htmltext = "32641-01.htm";
				}
				else if (id == COMPLETED)
				{
					htmltext = "32641-03.htm";
				}
				break;
			
			case Athenia:
				if (cond == 6)
				{
					htmltext = "32643-01.htm";
				}
				else if (id == COMPLETED)
				{
					htmltext = "32643-03.htm";
				}
				break;
		}
		
		return htmltext;
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
