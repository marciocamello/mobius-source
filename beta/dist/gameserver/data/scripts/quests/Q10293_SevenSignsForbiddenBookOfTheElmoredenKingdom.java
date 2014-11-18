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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

/**
 * @author pchayka
 */
public class Q10293_SevenSignsForbiddenBookOfTheElmoredenKingdom extends Quest implements ScriptFile
{
	// Npcs
	private static final int Elcardia = 32784;
	private static final int Sophia = 32596;
	private static final int SophiaInzone1 = 32861;
	private static final int ElcardiaInzone1 = 32785;
	private static final int SophiaInzone2 = 32863;
	private static final int[] books =
	{
		32809,
		32810,
		32811,
		32812,
		32813
	};
	// Item
	private static final int SolinasBiography = 17213;
	
	public Q10293_SevenSignsForbiddenBookOfTheElmoredenKingdom()
	{
		super(false);
		addStartNpc(Elcardia);
		addTalkId(Sophia, SophiaInzone1, ElcardiaInzone1, SophiaInzone2);
		addTalkId(books);
		addQuestItem(SolinasBiography);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		Player player = qs.getPlayer();
		String htmltext = event;
		
		switch (event)
		{
			case "elcardia_q10293_3.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "enter_library":
				enterInstance(qs, 156);
				return null;
				
			case "sophia2_q10293_4.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "sophia2_q10293_8.htm":
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "elcardia2_q10293_4.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "sophia2_q10293_10.htm":
				qs.setCond(6);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "teleport_in":
				qs.getPlayer().teleToLocation(new Location(37348, -50383, -1168));
				teleportElcardia(player);
				return null;
				
			case "teleport_out":
				qs.getPlayer().teleToLocation(new Location(37205, -49753, -1128));
				teleportElcardia(player);
				return null;
				
			case "book_q10293_3a.htm":
				qs.giveItems(SolinasBiography, 1);
				qs.setCond(7);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "elcardia_q10293_7.htm":
				qs.addExpAndSp(15000000, 1500000);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		if (!player.isBaseClassActive())
		{
			return "no_subclass_allowed.htm";
		}
		
		switch (npc.getId())
		{
			case Elcardia:
				if (cond == 0)
				{
					final QuestState state = player.getQuestState(Q10292_SevenSignsMysteriousGirl.class);
					
					if ((player.getLevel() >= 81) && (state != null) && state.isCompleted())
					{
						htmltext = "elcardia_q10293_1.htm";
					}
					else
					{
						htmltext = "elcardia_q10293_0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if ((cond >= 1) && (cond < 8))
				{
					htmltext = "elcardia_q10293_4.htm";
				}
				else if (cond == 8)
				{
					htmltext = "elcardia_q10293_5.htm";
				}
				break;
			
			case Sophia:
				if ((cond >= 1) && (cond <= 7))
				{
					htmltext = "sophia_q10293_1.htm";
				}
				break;
			
			case SophiaInzone1:
				switch (cond)
				{
					case 1:
						htmltext = "sophia2_q10293_1.htm";
						break;
					
					case 2:
					case 4:
					case 7:
					case 8:
						htmltext = "sophia2_q10293_5.htm";
						break;
					
					case 3:
						htmltext = "sophia2_q10293_6.htm";
						break;
					
					case 5:
						htmltext = "sophia2_q10293_9.htm";
						break;
					
					case 6:
						htmltext = "sophia2_q10293_11.htm";
						break;
				}
				break;
			
			case ElcardiaInzone1:
				switch (cond)
				{
					case 1:
					case 3:
					case 5:
					case 6:
						htmltext = "elcardia2_q10293_1.htm";
						break;
					
					case 2:
						qs.setCond(3);
						htmltext = "elcardia2_q10293_2.htm";
						break;
					
					case 4:
						htmltext = "elcardia2_q10293_3.htm";
						break;
					
					case 7:
						qs.setCond(8);
						htmltext = "elcardia2_q10293_5.htm";
						break;
					
					case 8:
						htmltext = "elcardia2_q10293_5.htm";
						break;
				}
				break;
			
			case SophiaInzone2:
				if ((cond == 6) || (cond == 7))
				{
					htmltext = "sophia3_q10293_1.htm";
				}
				else if (cond == 8)
				{
					htmltext = "sophia3_q10293_4.htm";
				}
				break;
			
			// Books
			case 32809:
				htmltext = "book_q10293_3.htm";
				break;
			
			case 32811:
				htmltext = "book_q10293_1.htm";
				break;
			
			case 32812:
				htmltext = "book_q10293_2.htm";
				break;
			
			case 32810:
				htmltext = "book_q10293_4.htm";
				break;
			
			case 32813:
				htmltext = "book_q10293_5.htm";
				break;
		}
		
		return htmltext;
	}
	
	private void teleportElcardia(Player player)
	{
		for (NpcInstance n : player.getReflection().getNpcs())
		{
			if (n.getId() == ElcardiaInzone1)
			{
				n.teleToLocation(Location.findPointToStay(player, 60));
			}
		}
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