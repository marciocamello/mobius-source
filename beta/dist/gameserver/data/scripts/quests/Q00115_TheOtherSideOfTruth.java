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

public class Q00115_TheOtherSideOfTruth extends Quest implements ScriptFile
{
	// Npcs
	private static final int Rafforty = 32020;
	private static final int Misa = 32018;
	private static final int Kierre = 32022;
	private static final int Ice_Sculpture1 = 32021;
	private static final int Ice_Sculpture2 = 32077;
	private static final int Ice_Sculpture3 = 32078;
	private static final int Ice_Sculpture4 = 32079;
	// Items
	private static final int Misas_Letter = 8079;
	private static final int Raffortys_Letter = 8080;
	private static final int Piece_of_Tablet = 8081;
	private static final int Report_Piece = 8082;
	
	public Q00115_TheOtherSideOfTruth()
	{
		super(false);
		addStartNpc(Rafforty);
		addTalkId(Misa, Kierre, Ice_Sculpture1, Ice_Sculpture2, Ice_Sculpture3, Ice_Sculpture4);
		addQuestItem(Misas_Letter, Raffortys_Letter, Piece_of_Tablet, Report_Piece);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("32020-02.htm") && (qs.getState() == CREATED))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		
		if (qs.getState() != STARTED)
		{
			return event;
		}
		
		switch (event)
		{
			case "32020-06.htm":
			case "32020-08a.htm":
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
			
			case "32020-05.htm":
				qs.setCond(3);
				qs.takeItems(Misas_Letter, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32020-08.htm":
			case "32020-07a.htm":
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32020-12.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32018-04.htm":
				qs.setCond(7);
				qs.takeItems(Raffortys_Letter, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "Sculpture-04a.htm":
				qs.setCond(8);
				qs.playSound(SOUND_MIDDLE);
				if ((qs.getInt("32021") == 0) && (qs.getInt("32077") == 0))
				{
					qs.giveItems(Piece_of_Tablet, 1);
				}
				return "Sculpture-04.htm";
				
			case "32022-02.htm":
				qs.setCond(9);
				qs.giveItems(Report_Piece, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32020-16.htm":
				qs.setCond(10);
				qs.takeItems(Report_Piece, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32020-18.htm":
				if (qs.getQuestItemsCount(Piece_of_Tablet) > 0)
				{
					qs.giveItems(ADENA_ID, 60044);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				else
				{
					qs.setCond(11);
					qs.playSound(SOUND_MIDDLE);
					return "32020-19.htm";
				}
				break;
			
			case "32020-19.htm":
				qs.setCond(11);
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		if (event.startsWith("32021") || event.startsWith("32077"))
		{
			if (event.contains("-pick"))
			{
				qs.set("talk", "1");
				event = event.replace("-pick", "");
			}
			
			qs.set(event, "1");
			return "Sculpture-05.htm";
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() == COMPLETED)
		{
			return "completed";
		}
		
		final int npcId = npc.getId();
		
		if (qs.getState() == CREATED)
		{
			if (npcId != Rafforty)
			{
				return "noquest";
			}
			
			if (qs.getPlayer().getLevel() >= 53)
			{
				qs.setCond(0);
				return "32020-01.htm";
			}
			
			qs.exitCurrentQuest(true);
			return "32020-00.htm";
		}
		
		final int cond = qs.getCond();
		
		if (qs.getState() != STARTED)
		{
			return "noquest";
		}
		
		switch (npcId)
		{
			case Rafforty:
				switch (cond)
				{
					case 1:
						return "32020-03.htm";
						
					case 2:
						return "32020-04.htm";
						
					case 3:
						return "32020-05.htm";
						
					case 4:
						return "32020-11.htm";
						
					case 5:
						qs.setCond(6);
						qs.giveItems(Raffortys_Letter, 1);
						qs.playSound(SOUND_MIDDLE);
						return "32020-13.htm";
						
					case 6:
						return "32020-14.htm";
						
					case 9:
						return "32020-15.htm";
						
					case 10:
						return "32020-17.htm";
						
					case 11:
						return "32020-20.htm";
						
					case 12:
						qs.giveItems(ADENA_ID, 193350);
						qs.addExpAndSp(956590, 789630);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(false);
						return "32020-18.htm";
				}
				break;
			
			case Misa:
				switch (cond)
				{
					case 1:
						qs.setCond(2);
						qs.giveItems(Misas_Letter, 1);
						qs.playSound(SOUND_MIDDLE);
						return "32018-01.htm";
						
					case 2:
						return "32018-02.htm";
						
					case 6:
						return "32018-03.htm";
						
					case 7:
						return "32018-05.htm";
				}
				break;
			
			case Kierre:
				if (cond == 8)
				{
					return "32022-01.htm";
				}
				else if (cond == 9)
				{
					return "32022-03.htm";
				}
				break;
			
			case Ice_Sculpture1:
			case Ice_Sculpture2:
			case Ice_Sculpture3:
			case Ice_Sculpture4:
				switch (cond)
				{
					case 7:
						String _npcId = String.valueOf(npcId);
						int npcId_flag = qs.getInt(_npcId);
						if ((npcId == Ice_Sculpture1) || (npcId == Ice_Sculpture2))
						{
							int talk_flag = qs.getInt("talk");
							return npcId_flag == 1 ? "Sculpture-02.htm" : talk_flag == 1 ? "Sculpture-06.htm" : "Sculpture-03-" + _npcId + ".htm";
						}
						else if (npcId_flag == 1)
						{
							return "Sculpture-02.htm";
						}
						else
						{
							qs.set(_npcId, "1");
							return "Sculpture-01.htm";
						}
						
					case 8:
						return "Sculpture-04.htm";
						
					case 11:
						qs.setCond(12);
						qs.giveItems(Piece_of_Tablet, 1);
						qs.playSound(SOUND_MIDDLE);
						return "Sculpture-07.htm";
						
					case 12:
						return "Sculpture-08.htm";
				}
				break;
		}
		
		return "noquest";
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
