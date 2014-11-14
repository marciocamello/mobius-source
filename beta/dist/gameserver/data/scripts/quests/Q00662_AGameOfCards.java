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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00662_AGameOfCards extends Quest implements ScriptFile
{
	private final static int KLUMP = 30845;
	private final static int[] MONSTERS =
	{
		20677,
		21109,
		21112,
		21116,
		21114,
		21004,
		21002,
		21006,
		21008,
		21010,
		18001,
		20672,
		20673,
		20674,
		20955,
		20962,
		20961,
		20959,
		20958,
		20966,
		20965,
		20968,
		20973,
		20972,
		21278,
		21279,
		21280,
		21286,
		21287,
		21288,
		21520,
		21526,
		21530,
		21535,
		21508,
		21510,
		21513,
		21515
	};
	private final static int RED_GEM = 8765;
	private final static int Enchant_Weapon_S = 959;
	private final static int Enchant_Weapon_A = 729;
	private final static int Enchant_Weapon_B = 947;
	private final static int Enchant_Weapon_C = 951;
	private final static int Enchant_Weapon_D = 955;
	private final static int Enchant_Armor_D = 956;
	private final static int ZIGGOS_GEMSTONE = 8868;
	private final static int DROP_CHANCE = 35;
	final static Map<Integer, CardGame> Games = new ConcurrentHashMap<>();
	
	public Q00662_AGameOfCards()
	{
		super(true);
		addStartNpc(KLUMP);
		addKillId(MONSTERS);
		addQuestItem(RED_GEM);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int _state = qs.getState();
		
		switch (event)
		{
			case "30845_02.htm":
				if (_state == CREATED)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "30845_07.htm":
				if (_state == STARTED)
				{
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "30845_03.htm":
				if ((_state == STARTED) && (qs.getQuestItemsCount(RED_GEM) >= 50))
				{
					return "30845_04.htm";
				}
				break;
			
			case "30845_10.htm":
				if (_state == STARTED)
				{
					if (qs.getQuestItemsCount(RED_GEM) < 50)
					{
						return "30845_10a.htm";
					}
					
					qs.takeItems(RED_GEM, 50);
					final int player_id = qs.getPlayer().getObjectId();
					
					if (Games.containsKey(player_id))
					{
						Games.remove(player_id);
					}
					
					Games.put(player_id, new CardGame(player_id));
				}
				break;
			
			case "play":
				if (_state == STARTED)
				{
					final int player_id = qs.getPlayer().getObjectId();
					
					if (!Games.containsKey(player_id))
					{
						return null;
					}
					
					return Games.get(player_id).playField();
				}
				break;
		}
		
		if (event.startsWith("card"))
		{
			if (_state == STARTED)
			{
				final int player_id = qs.getPlayer().getObjectId();
				
				if (!Games.containsKey(player_id))
				{
					return null;
				}
				
				try
				{
					int cardn = Integer.valueOf(event.replaceAll("card", ""));
					return Games.get(player_id).next(cardn, qs);
				}
				catch (Exception E)
				{
					return null;
				}
			}
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		switch (qs.getState())
		{
			case CREATED:
				if (qs.getPlayer().getLevel() < 61)
				{
					qs.exitCurrentQuest(true);
					return "30845_00.htm";
				}
				qs.setCond(0);
				return "30845_01.htm";
				
			case STARTED:
				return qs.getQuestItemsCount(RED_GEM) < 50 ? "30845_03.htm" : "30845_04.htm";
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() == STARTED)
		{
			qs.rollAndGive(RED_GEM, 1, DROP_CHANCE);
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
	
	private static class CardGame
	{
		private final String[] cards = new String[5];
		private final int player_id;
		private final static String[] card_chars = new String[]
		{
			"A",
			"1",
			"2",
			"3",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9",
			"10",
			"J",
			"Q",
			"K"
		};
		private final static String html_header = "<html><body>";
		private final static String html_footer = "</body></html>";
		private final static String table_header = "<table border=\"1\" cellpadding=\"3\"><tr>";
		private final static String table_footer = "</tr></table><br><br>";
		private final static String td_begin = "<center><td width=\"50\" align=\"center\"><br><br><br> ";
		private final static String td_end = " <br><br><br><br></td></center>";
		
		public CardGame(int _player_id)
		{
			player_id = _player_id;
			
			for (int i = 0; i < cards.length; i++)
			{
				cards[i] = "<a action=\"bypass -h Quest Q00662_AGameOfCards card" + i + "\">?</a>";
			}
		}
		
		public String next(int cardn, QuestState qs)
		{
			if ((cardn >= cards.length) || !cards[cardn].startsWith("<a"))
			{
				return null;
			}
			
			cards[cardn] = card_chars[Rnd.get(card_chars.length)];
			
			for (String card : cards)
			{
				if (card.startsWith("<a"))
				{
					return playField();
				}
			}
			
			return finish(qs);
		}
		
		private String finish(QuestState qs)
		{
			String result = html_header + table_header;
			Map<String, Integer> matches = new HashMap<>();
			
			for (String card : cards)
			{
				int count = matches.containsKey(card) ? matches.remove(card) : 0;
				count++;
				matches.put(card, count);
			}
			
			for (String card : cards)
			{
				if (matches.get(card) < 2)
				{
					matches.remove(card);
				}
			}
			
			String[] smatches = matches.keySet().toArray(new String[matches.size()]);
			Integer[] cmatches = matches.values().toArray(new Integer[matches.size()]);
			String txt = "Hmmm...? This is... No pair? Tough luck, my friend! Want to try again? Perhaps your luck will take a turn for the better...";
			
			switch (cmatches.length)
			{
				case 1:
					switch (cmatches[0])
					{
						case 5:
							txt = "Hmmm...? This is... Five of a kind!!!! What luck! The goddess of victory must be with you! Here is your prize! Well earned, well played!";
							qs.giveItems(ZIGGOS_GEMSTONE, 43);
							qs.giveItems(Enchant_Weapon_S, 3);
							qs.giveItems(Enchant_Weapon_A, 1);
							break;
						
						case 4:
							txt = "Hmmm...? This is... Four of a kind! Well done, my young friend! That sort of hand doesn't come up very often, that's for sure. Here's your prize.";
							qs.giveItems(Enchant_Weapon_S, 2);
							qs.giveItems(Enchant_Weapon_C, 2);
							break;
						
						case 3:
							txt = "Hmmm...? This is... Three of a kind? Very good, you are very lucky. Here's your prize.";
							qs.giveItems(Enchant_Weapon_C, 2);
							break;
						
						case 2:
							txt = "Hmmm...? This is... One pair? You got lucky this time, but I wonder if it'll last. Here's your prize.";
							qs.giveItems(Enchant_Armor_D, 2);
							break;
					}
					break;
				
				case 2:
					if ((cmatches[0] == 3) || (cmatches[1] == 3))
					{
						txt = "Hmmm...? This is... A full house? Excellent! you're better than I thought. Here's your prize.";
						qs.giveItems(Enchant_Weapon_A, 1);
						qs.giveItems(Enchant_Weapon_B, 2);
						qs.giveItems(Enchant_Weapon_D, 1);
					}
					else
					{
						txt = "Hmmm...? This is... Two pairs? You got lucky this time, but I wonder if it'll last. Here's your prize.";
						qs.giveItems(Enchant_Weapon_C, 1);
					}
					break;
			}
			
			for (String card : cards)
			{
				if ((smatches.length > 0) && smatches[0].equals(card))
				{
					result += td_begin + "<font color=\"55FD44\">" + card + "</font>" + td_end;
				}
				else if ((smatches.length == 2) && smatches[1].equals(card))
				{
					result += td_begin + "<font color=\"FE6666\">" + card + "</font>" + td_end;
				}
				else
				{
					result += td_begin + card + td_end;
				}
			}
			
			result += table_footer + txt;
			
			if (qs.getQuestItemsCount(RED_GEM) >= 50)
			{
				result += "<br><br><a action=\"bypass -h Quest Q00662_AGameOfCards 30845_10.htm\">Play Again!</a>";
			}
			
			result += html_footer;
			Games.remove(player_id);
			return result;
		}
		
		public String playField()
		{
			String result = html_header + table_header;
			
			for (String card : cards)
			{
				result += td_begin + card + td_end;
			}
			
			result += table_footer + "Check your next card." + html_footer;
			return result;
		}
	}
}
