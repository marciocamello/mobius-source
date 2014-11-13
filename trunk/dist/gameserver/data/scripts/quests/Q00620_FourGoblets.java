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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;
import bosses.FourSepulchersManager;

public class Q00620_FourGoblets extends Quest implements ScriptFile
{
	private static final int NAMELESS_SPIRIT = 31453;
	private static final int GHOST_OF_WIGOTH_1 = 31452;
	private static final int GHOST_OF_WIGOTH_2 = 31454;
	private static final int CONQ_SM = 31921;
	private static final int EMPER_SM = 31922;
	private static final int SAGES_SM = 31923;
	private static final int JUDGE_SM = 31924;
	private static final int GHOST_CHAMBERLAIN_1 = 31919;
	private static final int GHOST_CHAMBERLAIN_2 = 31920;
	private static final int GRAVE_PASS = 7261;
	private static final int[] GOBLETS = new int[]
	{
		7256,
		7257,
		7258,
		7259
	};
	private static final int RELIC = 7254;
	public final static int Sealed_Box = 7255;
	private static final int ANTIQUE_BROOCH = 7262;
	private static final int[] RCP_REWARDS = new int[]
	{
		6881,
		6883,
		6885,
		6887,
		6891,
		6893,
		6895,
		6897,
		6899,
		7580
	};
	
	public Q00620_FourGoblets()
	{
		super(false);
		addStartNpc(NAMELESS_SPIRIT, CONQ_SM, EMPER_SM, SAGES_SM, JUDGE_SM, GHOST_CHAMBERLAIN_1, GHOST_CHAMBERLAIN_2);
		addTalkId(GHOST_OF_WIGOTH_1, GHOST_OF_WIGOTH_2);
		addQuestItem(Sealed_Box, GRAVE_PASS);
		addQuestItem(GOBLETS);
		
		for (int id = 18120; id <= 18256; id++)
		{
			addKillId(id);
		}
	}
	
	private static String onOpenBoxes(QuestState qs, String count)
	{
		try
		{
			return new OpenSealedBox(qs, Integer.parseInt(count)).apply();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "Don't try to cheat with me!";
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		
		if (event.equals("Enter"))
		{
			FourSepulchersManager.tryEntry(npc, player);
			return null;
		}
		else if (event.equals("accept"))
		{
			if (cond == 0)
			{
				if (qs.getPlayer().getLevel() >= 74)
				{
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
					qs.setCond(1);
					return "31453-13.htm";
				}
				
				qs.exitCurrentQuest(true);
				return "31453-12.htm";
			}
		}
		else if (event.startsWith("openBoxes "))
		{
			return onOpenBoxes(qs, event.replace("openBoxes ", "").trim());
		}
		else if (event.equals("12"))
		{
			if (!qs.checkQuestItemsCount(GOBLETS))
			{
				return "31453-14.htm";
			}
			
			qs.takeAllItems(GOBLETS);
			qs.giveItems(ANTIQUE_BROOCH, 1);
			qs.setCond(2);
			qs.playSound(SOUND_FINISH);
			return "31453-16.htm";
		}
		else if (event.equals("13"))
		{
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(true);
			return "31453-18.htm";
		}
		else if (event.equals("14"))
		{
			if (cond == 2)
			{
				return "31453-19.htm";
			}
			
			return "31453-13.htm";
		}
		else if (event.equals("15"))
		{
			if (qs.getQuestItemsCount(ANTIQUE_BROOCH) >= 1)
			{
				qs.getPlayer().teleToLocation(178298, -84574, -7216);
				return null;
			}
			
			if (qs.getQuestItemsCount(GRAVE_PASS) >= 1)
			{
				qs.takeItems(GRAVE_PASS, 1);
				qs.getPlayer().teleToLocation(178298, -84574, -7216);
				return null;
			}
			
			return "" + str(npc.getId()) + "-0.htm";
		}
		else if (event.equals("16"))
		{
			if (qs.getQuestItemsCount(ANTIQUE_BROOCH) >= 1)
			{
				qs.getPlayer().teleToLocation(186942, -75602, -2834);
				return null;
			}
			
			if (qs.getQuestItemsCount(GRAVE_PASS) >= 1)
			{
				qs.takeItems(GRAVE_PASS, 1);
				qs.getPlayer().teleToLocation(186942, -75602, -2834);
				return null;
			}
			
			return "" + str(npc.getId()) + "-0.htm";
		}
		else if (event.equals("17"))
		{
			if (qs.getQuestItemsCount(ANTIQUE_BROOCH) >= 1)
			{
				qs.getPlayer().teleToLocation(169590, -90218, -2914);
			}
			else
			{
				qs.takeItems(GRAVE_PASS, 1);
				qs.getPlayer().teleToLocation(169590, -90218, -2914);
			}
			
			return "31452-6.htm";
		}
		else if (event.equals("18"))
		{
			if (qs.getSumQuestItemsCount(GOBLETS) < 3)
			{
				return "31452-3.htm";
			}
			
			if (qs.getSumQuestItemsCount(GOBLETS) == 3)
			{
				return "31452-4.htm";
			}
			
			if (qs.getSumQuestItemsCount(GOBLETS) >= 4)
			{
				return "31452-5.htm";
			}
		}
		else if (event.equals("19"))
		{
			return new OpenSealedBox(qs, 1).apply();
		}
		else if (event.startsWith("19 "))
		{
			return onOpenBoxes(qs, event.replaceFirst("19 ", ""));
		}
		else if (event.equals("11"))
		{
			return "<html><body><a action=\"bypass -h Quest Q00620_FourGoblets 19\">\"Please open a box.\"</a><br><a action=\"bypass -h Quest Q00620_FourGoblets 19 5\">\"Please open 5 boxes.\"</a><br><a action=\"bypass -h Quest Q00620_FourGoblets 19 10\">\"Please open 10 boxes.\"</a><br><a action=\"bypass -h Quest Q00620_FourGoblets 19 50\">\"Please open 50 boxes.\"</a><br></body></html>";
		}
		else
		{
			int id = 0;
			
			try
			{
				id = Integer.parseInt(event);
			}
			catch (Exception e)
			{
				// empty catch clause
			}
			
			if (Util.contains(RCP_REWARDS, id))
			{
				qs.takeItems(RELIC, 1000);
				qs.giveItems(id, 1);
				return "31454-17.htm";
			}
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int id = qs.getState();
		final int cond = qs.getCond();
		
		if (id == CREATED)
		{
			qs.setCond(0);
		}
		
		switch (npcId)
		{
			case NAMELESS_SPIRIT:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 74)
					{
						htmltext = "31453-1.htm";
					}
					else
					{
						htmltext = "31453-12.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					if (qs.checkQuestItemsCount(GOBLETS))
					{
						htmltext = "31453-15.htm";
					}
					else
					{
						htmltext = "31453-14.htm";
					}
				}
				else if (cond == 2)
				{
					htmltext = "31453-17.htm";
				}
				break;
			
			case GHOST_OF_WIGOTH_1:
				if (cond == 2)
				{
					htmltext = "31452-2.htm";
				}
				else if (cond == 1)
				{
					if (qs.getSumQuestItemsCount(GOBLETS) == 1)
					{
						htmltext = "31452-1.htm";
					}
					else if (qs.getSumQuestItemsCount(GOBLETS) > 1)
					{
						htmltext = "31452-2.htm";
					}
				}
				break;
			
			case GHOST_OF_WIGOTH_2:
				if (qs.getQuestItemsCount(RELIC) >= 1000)
				{
					if (qs.getQuestItemsCount(Sealed_Box) >= 1)
					{
						if (qs.checkQuestItemsCount(GOBLETS))
						{
							htmltext = "31454-4.htm";
						}
						else if (qs.checkQuestItemsCount(GOBLETS))
						{
							htmltext = "31454-8.htm";
						}
						else
						{
							htmltext = "31454-12.htm";
						}
					}
					else if (qs.checkQuestItemsCount(GOBLETS))
					{
						htmltext = "31454-3.htm";
					}
					else if (qs.getSumQuestItemsCount(GOBLETS) > 1)
					{
						htmltext = "31454-7.htm";
					}
					else
					{
						htmltext = "31454-11.htm";
					}
				}
				else if (qs.getQuestItemsCount(Sealed_Box) >= 1)
				{
					if (qs.checkQuestItemsCount(GOBLETS))
					{
						htmltext = "31454-2.htm";
					}
					else if (qs.getSumQuestItemsCount(GOBLETS) > 1)
					{
						htmltext = "31454-6.htm";
					}
					else
					{
						htmltext = "31454-10.htm";
					}
				}
				else if (qs.checkQuestItemsCount(GOBLETS))
				{
					htmltext = "31454-1.htm";
				}
				else if (qs.getSumQuestItemsCount(GOBLETS) > 1)
				{
					htmltext = "31454-5.htm";
				}
				else
				{
					htmltext = "31454-9.htm";
				}
				break;
			
			case CONQ_SM:
				htmltext = "31921-E.htm";
				break;
			
			case EMPER_SM:
				htmltext = "31922-E.htm";
				break;
			
			case SAGES_SM:
				htmltext = "31923-E.htm";
				break;
			
			case JUDGE_SM:
				htmltext = "31924-E.htm";
				break;
			
			case GHOST_CHAMBERLAIN_1:
				htmltext = "31919-1.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		if (((cond == 1) || (cond == 2)) && (npcId >= 18120) && (npcId <= 18256) && Rnd.chance(30))
		{
			qs.giveItems(Sealed_Box, 1);
			qs.playSound(SOUND_ITEMGET);
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
