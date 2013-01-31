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
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.SubClassType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExSubjobInfo;
import lineage2.gameserver.scripts.ScriptFile;

public class _177_StartTheNewDestiny extends Quest implements ScriptFile
{
	private static final int Hadel = 33344;
	private static final int Ishuma = 32615;
	private static final int questitem_01 = 17718;
	private static final int questitem_02 = 17719;
	private static final int questitem_03 = 17720;
	private static final int questitem_04 = 17721;
	private static final int[] MOBS_1 =
	{
		21549,
		21550,
		21547,
		21548,
		21587
	};
	private static final int[] MOBS_2 =
	{
		22257,
		22258,
		22259,
		22260
	};
	
	public _177_StartTheNewDestiny()
	{
		super(false);
		addStartNpc(Hadel);
		addTalkId(Hadel);
		addTalkId(Ishuma);
		addQuestItem(questitem_01, questitem_02, questitem_03, questitem_04);
		addKillId(MOBS_1);
		addKillId(MOBS_2);
		addLevelCheck(85, 99);
		addClassLevelCheck(4);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("33344_03.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("33344_07.htm"))
		{
			st.takeAllItems(questitem_03);
			st.setCond(4);
		}
		else if (event.equalsIgnoreCase("33344_10.htm"))
		{
			st.giveItems(questitem_03, 10);
			st.setCond(7);
		}
		else if (event.equalsIgnoreCase("32615_03.htm"))
		{
			st.takeAllItems(questitem_03);
			st.takeAllItems(questitem_04);
			st.setCond(8);
		}
		else if (event.equalsIgnoreCase("33344_13.htm"))
		{
			st.takeAllItems(questitem_01);
			st.takeAllItems(questitem_02);
		}
		else if (event.equalsIgnoreCase("33344_16.htm"))
		{
			st.giveItems(10480, 1);
			st.giveItems(18168, 1);
			st.addExpAndSp(175739575, 2886300);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			st.getPlayer().getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			AwakingManager.getInstance().onPlayerEnter(st.getPlayer());
			st.getPlayer().sendPacket(new ExSubjobInfo(st.getPlayer(), true));
		}
		else if (event.equalsIgnoreCase("33344_17.htm"))
		{
			st.giveItems(10481, 1);
			st.giveItems(18168, 1);
			st.addExpAndSp(175739575, 2886300);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			st.getPlayer().getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			AwakingManager.getInstance().onPlayerEnter(st.getPlayer());
			st.getPlayer().sendPacket(new ExSubjobInfo(st.getPlayer(), true));
		}
		else if (event.equalsIgnoreCase("33344_18.htm"))
		{
			st.giveItems(10482, 1);
			st.giveItems(18168, 1);
			st.addExpAndSp(175739575, 2886300);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			st.getPlayer().getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			AwakingManager.getInstance().onPlayerEnter(st.getPlayer());
			st.getPlayer().sendPacket(new ExSubjobInfo(st.getPlayer(), true));
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Hadel)
		{
			if (st.getInt("subClassId") == player.getActiveClassId())
			{
				if (cond == 0)
				{
					if (player.getLevel() >= 80)
					{
						htmltext = "33344_01.htm";
					}
					else
					{
						htmltext = "33344_00.htm";
					}
				}
				else if (cond == 1)
				{
					htmltext = "33344_04.htm";
				}
				else if (cond == 2)
				{
					htmltext = "33344_04.htm";
				}
				else if (cond == 3)
				{
					if (st.getQuestItemsCount(questitem_03) >= 10)
					{
						htmltext = "33344_05.htm";
					}
				}
				else if (cond == 4)
				{
					htmltext = "33344_08.htm";
				}
				else if (cond == 5)
				{
					htmltext = "33344_08.htm";
				}
				else if (cond == 6)
				{
					if (st.getQuestItemsCount(questitem_04) >= 10)
					{
						htmltext = "33344_09.htm";
					}
				}
				else if (cond == 7)
				{
					htmltext = "33344_11.htm";
				}
				else if (cond == 9)
				{
					if ((st.getQuestItemsCount(questitem_01) >= 2) && (st.getQuestItemsCount(questitem_02) >= 2))
					{
						htmltext = "33344_12.htm";
					}
					else
					{
						htmltext = "33344_14.htm";
					}
				}
			}
			else
			{
				htmltext = "no_subclass.htm";
			}
		}
		else if (npcId == Ishuma)
		{
			if (st.getInt("subClassId") == player.getActiveClassId())
			{
				if (cond == 7)
				{
					if ((st.getQuestItemsCount(questitem_03) >= 10) && (st.getQuestItemsCount(questitem_04) >= 10))
					{
						htmltext = "32615_01.htm";
						st.setCond(8);
					}
				}
				else if (cond == 8)
				{
					st.giveItems(questitem_01, 2);
					st.giveItems(questitem_02, 2);
					htmltext = "32615_04.htm";
					st.setCond(9);
				}
				else if (cond == 9)
				{
					htmltext = "32615_05.htm";
				}
			}
			else
			{
				htmltext = "no_subclass.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 1) && MOBS_1.equals(npcId) && Rnd.chance(30))
		{
			if (st.getQuestItemsCount(questitem_03) < 1)
			{
				st.giveItems(questitem_03, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (st.getQuestItemsCount(questitem_03) >= 1)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
		}
		else if ((cond == 2) && MOBS_1.equals(npcId) && Rnd.chance(30))
		{
			if (st.getQuestItemsCount(questitem_03) < 10)
			{
				st.giveItems(questitem_03, 1);
				st.playSound(SOUND_ITEMGET);
			}
			if (st.getQuestItemsCount(questitem_03) >= 10)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(3);
			}
		}
		else if ((cond == 4) && MOBS_2.equals(npcId) && Rnd.chance(30))
		{
			if (st.getQuestItemsCount(questitem_04) < 1)
			{
				st.giveItems(questitem_04, 1);
				st.playSound(SOUND_ITEMGET);
			}
			else if (st.getQuestItemsCount(questitem_04) >= 1)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(5);
			}
		}
		else if ((cond == 5) && MOBS_2.equals(npcId) && Rnd.chance(30))
		{
			if (st.getQuestItemsCount(questitem_04) < 10)
			{
				st.giveItems(questitem_04, 1);
				st.playSound(SOUND_ITEMGET);
			}
			if (st.getQuestItemsCount(questitem_04) >= 10)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(6);
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
