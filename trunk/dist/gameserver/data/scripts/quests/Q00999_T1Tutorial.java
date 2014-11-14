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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00999_T1Tutorial extends Quest implements ScriptFile
{
	private static final int RECOMMENDATION_01 = 1067;
	private static final int RECOMMENDATION_02 = 1068;
	private static final int LEAF_OF_MOTHERTREE = 1069;
	private static final int BLOOD_OF_JUNDIN = 1070;
	private static final int LICENSE_OF_MINER = 1498;
	private static final int VOUCHER_OF_FLAME = 1496;
	private static final int SOULSHOT_NOVICE = 5789;
	private static final int SPIRITSHOT_NOVICE = 5790;
	private static final int BLUE_GEM = 6353;
	private static final int DIPLOMA = 9881;
	
	private static class Event
	{
		public String _htm;
		public int _radarX;
		public int _radarY;
		public int _radarZ;
		public int _item;
		public int _classId1;
		public int _gift1;
		public int _count1;
		public int _classId2;
		public int _gift2;
		public int _count2;
		
		public Event(String htm, int radarX, int radarY, int radarZ, int item, int classId1, int gift1, int count1, int classId2, int gift2, int count2)
		{
			this._htm = htm;
			this._radarX = radarX;
			this._radarY = radarY;
			this._radarZ = radarZ;
			this._item = item;
			this._classId1 = classId1;
			this._gift1 = gift1;
			this._count1 = count1;
			this._classId2 = classId2;
			this._gift2 = gift2;
			this._count2 = count2;
		}
	}
	
	private static class Talk
	{
		public int _raceId;
		public String[] _htmlfiles;
		public int _npcTyp;
		public int _item;
		
		public Talk(int raceId, String[] htmlfiles, int npcTyp, int item)
		{
			this._raceId = raceId;
			this._htmlfiles = htmlfiles;
			this._npcTyp = npcTyp;
			this._item = item;
		}
	}
	
	private static final Map<String, Event> events = new HashMap<>();
	static
	{
		events.put("32133_02", new Event("32133-03.htm", -119692, 44504, 380, DIPLOMA, 0x7b, SOULSHOT_NOVICE, 200, 0x7c, SOULSHOT_NOVICE, 200));
		events.put("30008_02", new Event("30008-03.htm", 0, 0, 0, RECOMMENDATION_01, 0x00, SOULSHOT_NOVICE, 200, 0x00, 0, 0));
		events.put("30008_04", new Event("30008-04.htm", -84058, 243239, -3730, 0, 0x00, 0, 0, 0, 0, 0));
		events.put("30017_02", new Event("30017-03.htm", 0, 0, 0, RECOMMENDATION_02, 0x0a, SPIRITSHOT_NOVICE, 100, 0x00, 0, 0));
		events.put("30017_04", new Event("30017-04.htm", -84058, 243239, -3730, 0, 0x0a, 0, 0, 0x00, 0, 0));
		events.put("30370_02", new Event("30370-03.htm", 0, 0, 0, LEAF_OF_MOTHERTREE, 0x19, SPIRITSHOT_NOVICE, 100, 0x12, SOULSHOT_NOVICE, 200));
		events.put("30370_04", new Event("30370-04.htm", 45491, 48359, -3086, 0, 0x19, 0, 0, 0x12, 0, 0));
		events.put("30129_02", new Event("30129-03.htm", 0, 0, 0, BLOOD_OF_JUNDIN, 0x26, SPIRITSHOT_NOVICE, 100, 0x1f, SOULSHOT_NOVICE, 200));
		events.put("30129_04", new Event("30129-04.htm", 12116, 16666, -4610, 0, 0x26, 0, 0, 0x1f, 0, 0));
		events.put("30528_02", new Event("30528-03.htm", 0, 0, 0, LICENSE_OF_MINER, 0x35, SOULSHOT_NOVICE, 200, 0x00, 0, 0));
		events.put("30528_04", new Event("30528-04.htm", 115642, -178046, -941, 0, 0x35, 0, 0, 0x00, 0, 0));
		events.put("30573_02", new Event("30573-03.htm", 0, 0, 0, VOUCHER_OF_FLAME, 0x31, SPIRITSHOT_NOVICE, 100, 0x2c, SOULSHOT_NOVICE, 200));
		events.put("30573_04", new Event("30573-04.htm", -45067, -113549, -235, 0, 0x31, 0, 0, 0x2c, 0, 0));
	}
	private static final Map<Integer, Talk> talks = new HashMap<>();
	static
	{
		talks.put(30017, new Talk(0, new String[]
		{
			"30017-01.htm",
			"30017-02.htm",
			"30017-04.htm"
		}, 0, 0));
		talks.put(30008, new Talk(0, new String[]
		{
			"30008-01.htm",
			"30008-02.htm",
			"30008-04.htm"
		}, 0, 0));
		talks.put(30370, new Talk(1, new String[]
		{
			"30370-01.htm",
			"30370-02.htm",
			"30370-04.htm"
		}, 0, 0));
		talks.put(30129, new Talk(2, new String[]
		{
			"30129-01.htm",
			"30129-02.htm",
			"30129-04.htm"
		}, 0, 0));
		talks.put(30573, new Talk(3, new String[]
		{
			"30573-01.htm",
			"30573-02.htm",
			"30573-04.htm"
		}, 0, 0));
		talks.put(30528, new Talk(4, new String[]
		{
			"30528-01.htm",
			"30528-02.htm",
			"30528-04.htm"
		}, 0, 0));
		talks.put(30018, new Talk(0, new String[]
		{
			"30131-01.htm",
			"",
			"30019-03a.htm",
			"30019-04.htm",
		}, 1, RECOMMENDATION_02));
		talks.put(30019, new Talk(0, new String[]
		{
			"30131-01.htm",
			"",
			"30019-03a.htm",
			"30019-04.htm",
		}, 1, RECOMMENDATION_02));
		talks.put(30020, new Talk(0, new String[]
		{
			"30131-01.htm",
			"",
			"30019-03a.htm",
			"30019-04.htm",
		}, 1, RECOMMENDATION_02));
		talks.put(30021, new Talk(0, new String[]
		{
			"30131-01.htm",
			"",
			"30019-03a.htm",
			"30019-04.htm",
		}, 1, RECOMMENDATION_02));
		talks.put(30009, new Talk(0, new String[]
		{
			"30530-01.htm",
			"30009-03.htm",
			"",
			"30009-04.htm",
		}, 1, RECOMMENDATION_01));
		talks.put(30011, new Talk(0, new String[]
		{
			"30530-01.htm",
			"30009-03.htm",
			"",
			"30009-04.htm",
		}, 1, RECOMMENDATION_01));
		talks.put(30012, new Talk(0, new String[]
		{
			"30530-01.htm",
			"30009-03.htm",
			"",
			"30009-04.htm",
		}, 1, RECOMMENDATION_01));
		talks.put(30056, new Talk(0, new String[]
		{
			"30530-01.htm",
			"30009-03.htm",
			"",
			"30009-04.htm",
		}, 1, RECOMMENDATION_01));
		talks.put(30400, new Talk(1, new String[]
		{
			"30131-01.htm",
			"30400-03.htm",
			"30400-03a.htm",
			"30400-04.htm",
		}, 1, LEAF_OF_MOTHERTREE));
		talks.put(30401, new Talk(1, new String[]
		{
			"30131-01.htm",
			"30400-03.htm",
			"30400-03a.htm",
			"30400-04.htm",
		}, 1, LEAF_OF_MOTHERTREE));
		talks.put(30402, new Talk(1, new String[]
		{
			"30131-01.htm",
			"30400-03.htm",
			"30400-03a.htm",
			"30400-04.htm",
		}, 1, LEAF_OF_MOTHERTREE));
		talks.put(30403, new Talk(1, new String[]
		{
			"30131-01.htm",
			"30400-03.htm",
			"30400-03a.htm",
			"30400-04.htm",
		}, 1, LEAF_OF_MOTHERTREE));
		talks.put(30131, new Talk(2, new String[]
		{
			"30131-01.htm",
			"30131-03.htm",
			"30131-03a.htm",
			"30131-04.htm",
		}, 1, BLOOD_OF_JUNDIN));
		talks.put(30404, new Talk(2, new String[]
		{
			"30131-01.htm",
			"30131-03.htm",
			"30131-03a.htm",
			"30131-04.htm",
		}, 1, BLOOD_OF_JUNDIN));
		talks.put(30574, new Talk(3, new String[]
		{
			"30575-01.htm",
			"30575-03.htm",
			"30575-03a.htm",
			"30575-04.htm",
		}, 1, VOUCHER_OF_FLAME));
		talks.put(30575, new Talk(3, new String[]
		{
			"30575-01.htm",
			"30575-03.htm",
			"30575-03a.htm",
			"30575-04.htm",
		}, 1, VOUCHER_OF_FLAME));
		talks.put(30530, new Talk(4, new String[]
		{
			"30530-01.htm",
			"30530-03.htm",
			"",
			"30530-04.htm",
		}, 1, LICENSE_OF_MINER));
		talks.put(32133, new Talk(5, new String[]
		{
			"32133-01.htm",
			"32133-02.htm",
			"32133-04.htm"
		}, 0, 0));
		talks.put(32134, new Talk(5, new String[]
		{
			"32134-01.htm",
			"32134-03.htm",
			"",
			"32134-04.htm",
		}, 1, DIPLOMA));
	}
	
	public Q00999_T1Tutorial()
	{
		super(false);
		addStartNpc(30008, 30009, 30017, 30019, 30129, 30131, 30573, 30575, 30370, 30528, 30530, 30400, 30401, 30402, 30403, 30404, 32133, 32134);
		addTalkId(30008, 30009, 30017, 30019, 30129, 30131, 30573, 30575, 30370, 30528, 30530, 30400, 30401, 30402, 30403, 30404, 32133, 32134);
		addFirstTalkId(30008, 30009, 30017, 30019, 30129, 30131, 30573, 30575, 30370, 30528, 30530, 30400, 30401, 30402, 30403, 30404, 32133, 32134);
		addKillId(18342, 20001);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final QuestState state = qs.getPlayer().getQuestState(Q00255_Tutorial.class);
		
		if (state == null)
		{
			return null;
		}
		
		final Player player = qs.getPlayer();
		
		if (player == null)
		{
			return null;
		}
		
		String htmltext = event;
		final int Ex = state.getInt("Ex");
		final int classId = player.getClassId().getId();
		final boolean isMage = (player.getRace() != Race.orc) && player.getClassId().isMage();
		
		switch (event)
		{
			case "TimerEx_NewbieHelper":
				if (Ex == 0)
				{
					if (isMage)
					{
						qs.playTutorialVoice("tutorial_voice_009b");
					}
					else
					{
						qs.playTutorialVoice("tutorial_voice_009a");
					}
					
					state.set("Ex", "1");
				}
				else if (Ex == 3)
				{
					qs.playTutorialVoice("tutorial_voice_010a");
					state.set("Ex", "4");
				}
				return null;
				
			case "TimerEx_GrandMaster":
				if (Ex >= 4)
				{
					qs.showQuestionMark(7);
					qs.playSound(SOUND_TUTORIAL);
					qs.playTutorialVoice("tutorial_voice_025");
				}
				return null;
				
			case "isle":
				qs.addRadar(-119692, 44504, 380);
				player.teleToLocation(-120050, 44500, 360);
				String title = npc == null ? "" : npc.getTitle() + " " + npc.getName();
				htmltext = "<html><body>" + title + "<br>Go to the <font color=\"LEVEL\">Isle of Souls</font> and meet the <font color=\"LEVEL\">Newbie Guide</font> there to learn a number of important tips. He will also give you an item to assist your development.<br>Follow the direction arrow above your head and it will lead you to the Newbie Guide. Good luck!</body></html>";
				break;
			
			default:
				Event e = events.get(event);
				if (e._radarX != 0)
				{
					qs.addRadar(e._radarX, e._radarY, e._radarZ);
				}
				htmltext = e._htm;
				if ((qs.getQuestItemsCount(e._item) > 0) && (qs.getInt("onlyone") == 0))
				{
					qs.addExpAndSp(0, 50);
					qs.startQuestTimer("TimerEx_GrandMaster", 60000);
					qs.takeItems(e._item, 1);
					
					if (Ex <= 3)
					{
						state.set("Ex", "4");
					}
					
					if (classId == e._classId1)
					{
						qs.giveItems(e._gift1, e._count1);
						
						if (e._gift1 == SPIRITSHOT_NOVICE)
						{
							qs.playTutorialVoice("tutorial_voice_027");
						}
						else
						{
							qs.playTutorialVoice("tutorial_voice_026");
						}
					}
					else if (classId == e._classId2)
					{
						if (e._gift2 != 0)
						{
							qs.giveItems(e._gift2, e._count2);
							qs.playTutorialVoice("tutorial_voice_026");
						}
					}
					
					qs.set("step", "3");
					qs.set("onlyone", "1");
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		String htmltext = "";
		final QuestState qs = player.getQuestState(Q00255_Tutorial.class);
		
		if (qs == null)
		{
			return htmltext;
		}
		
		QuestState st = player.getQuestState(getClass());
		
		if (st == null)
		{
			newQuestState(player, CREATED);
			st = player.getQuestState(getClass());
		}
		
		final int Ex = qs.getInt("Ex");
		final int npcId = npc.getId();
		final int step = st.getInt("step");
		final int onlyone = st.getInt("onlyone");
		final int level = player.getLevel();
		final boolean isMage = (player.getRace() != Race.orc) && player.getClassId().isMage();
		Talk t = talks.get(npcId);
		
		if (t == null)
		{
			return "";
		}
		
		if (((level >= 10) || (onlyone == 1)) && (t._npcTyp == 1))
		{
			htmltext = "30575-05.htm";
		}
		else if ((onlyone == 0) && (level < 10))
		{
			if (player.getRace().ordinal() == t._raceId)
			{
				htmltext = t._htmlfiles[0];
			}
			
			if (t._npcTyp == 1)
			{
				if ((step == 0) && (Ex < 0))
				{
					qs.set("Ex", "0");
					st.startQuestTimer("TimerEx_NewbieHelper", 30000);
					
					if (isMage)
					{
						st.set("step", "1");
						st.setState(STARTED);
					}
					else
					{
						htmltext = "30530-01.htm";
						st.set("step", "1");
						st.setState(STARTED);
					}
				}
				else if ((step == 1) && (st.getQuestItemsCount(t._item) == 0) && (Ex <= 2))
				{
					if (st.getQuestItemsCount(BLUE_GEM) > 0)
					{
						st.takeItems(BLUE_GEM, st.getQuestItemsCount(BLUE_GEM));
						st.giveItems(t._item, 1);
						st.set("step", "2");
						qs.set("Ex", "3");
						st.startQuestTimer("TimerEx_NewbieHelper", 30000);
						qs.set("ucMemo", "3");
						
						if (isMage)
						{
							st.playTutorialVoice("tutorial_voice_027");
							st.giveItems(SPIRITSHOT_NOVICE, 100);
							htmltext = t._htmlfiles[2];
							
							if (htmltext.isEmpty())
							{
								htmltext = "<html><body>" + (npc.getTitle().isEmpty() ? "" : npc.getTitle() + " ") + npc.getName() + "<br>I am sorry. I only help warriors. Please go to another Newbie Helper who may assist you.</body></html>";
							}
						}
						else
						{
							st.playTutorialVoice("tutorial_voice_026");
							st.giveItems(SOULSHOT_NOVICE, 200);
							htmltext = t._htmlfiles[1];
							
							if (htmltext.isEmpty())
							{
								htmltext = "<html><body>" + (npc.getTitle().isEmpty() ? "" : npc.getTitle() + " ") + npc.getName() + "<br>I am sorry. I only help mystics. Please go to another Newbie Helper who may assist you.</body></html>";
							}
						}
					}
					else
					{
						if (isMage)
						{
							htmltext = "30131-02.htm";
							
							if (player.getRace().ordinal() == 3)
							{
								htmltext = "30575-02.htm";
							}
						}
						else
						{
							htmltext = "30530-02.htm";
						}
					}
				}
				else if (step == 2)
				{
					htmltext = t._htmlfiles[3];
				}
			}
			else if (t._npcTyp == 0)
			{
				if (step == 1)
				{
					htmltext = t._htmlfiles[0];
				}
				else if (step == 2)
				{
					htmltext = t._htmlfiles[1];
				}
				else if (step == 3)
				{
					htmltext = t._htmlfiles[2];
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final QuestState state = qs.getPlayer().getQuestState(Q00255_Tutorial.class);
		
		if (state == null)
		{
			return null;
		}
		
		final int Ex = state.getInt("Ex");
		
		if (Ex <= 1)
		{
			qs.playTutorialVoice("tutorial_voice_011");
			qs.showQuestionMark(3);
			state.set("Ex", "2");
		}
		
		if ((Ex <= 2) && (qs.getQuestItemsCount(BLUE_GEM) < 1))
		{
			ThreadPoolManager.getInstance().schedule(new DropGem(npc, qs), 3000);
		}
		
		return null;
	}
	
	private static class DropGem extends RunnableImpl
	{
		private final NpcInstance _npc;
		private final QuestState _st;
		
		DropGem(NpcInstance npc, QuestState st)
		{
			_npc = npc;
			_st = st;
		}
		
		@Override
		public void runImpl()
		{
			if ((_st != null) && (_npc != null))
			{
				_npc.dropItem(_st.getPlayer(), BLUE_GEM, 1);
				_st.playSound(SOUND_TUTORIAL);
			}
		}
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		return false;
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
