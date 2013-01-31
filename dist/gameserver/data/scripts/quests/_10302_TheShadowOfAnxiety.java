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

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;

public class _10302_TheShadowOfAnxiety extends Quest implements ScriptFile
{
	private static final int _kantarubis = 32898;
	private static final int _izshael = 32894;
	private static final int _kes = 32901;
	private static final int _masterKei = 32903;
	private static final int _kotKik = 32902;
	
	public _10302_TheShadowOfAnxiety()
	{
		super(false);
		addStartNpc(_kantarubis);
		addTalkId(_kantarubis, _izshael, _kes, _masterKei, _kotKik);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (st == null)
		{
			return event;
		}
		if (htmltext.equals("32898-02.htm"))
		{
			st.getQuest();
		}
		else if (htmltext.equals("32894-01.htm"))
		{
			st.setCond(2);
		}
		else if (htmltext.equals("32901-01.htm"))
		{
			st.setCond(3);
		}
		else if (htmltext.equals("32903-01.htm"))
		{
			st.setCond(4);
		}
		else if (htmltext.equals("32902-01.htm"))
		{
			st.setCond(5);
		}
		else if (htmltext.equals("32894-05.htm"))
		{
			st.setCond(6);
		}
		else if (htmltext.equals("32898-06.htm"))
		{
			st.showQuestionMark(10304);
			st.playSound("ItemSound.quest_tutorial");
			st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.The_job_can_start_with_the_old_scroll_of_items, 600, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false));
			st.addExpAndSp(6728850, 755280);
			st.giveItems(57, 2177190);
			st.giveItems(34033, 1);
			st.playSound("ItemSound.quest_finish");
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		QuestState prevst = st.getPlayer().getQuestState("10301_TheShadowOfFear");
		if (npc.getNpcId() == 32898)
		{
			switch (st.getState())
			{
				case 2:
					htmltext = "32898-completed.htm";
					break;
				case 0:
					if (st.getPlayer().getLevel() >= 90)
					{
						if ((prevst != null) && (prevst.isCompleted()))
						{
							htmltext = "32898-00.htm";
						}
						else
						{
							htmltext = "32898-nolvl.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "32898-nolvl.htm";
						st.exitCurrentQuest(true);
					}
					break;
				case 1:
					if (st.getCond() == 1)
					{
						htmltext = "32898-03.htm";
					}
					else
					{
						if (st.getCond() != 6)
						{
							break;
						}
						htmltext = "32898-04.htm";
					}
			}
		}
		else if (npc.getNpcId() == 32894)
		{
			switch (st.getState())
			{
				case 2:
					htmltext = HtmCache.getInstance().getNotNull("data/html/completed-quest.htm", st.getPlayer());
					break;
				case 1:
					switch (st.getCond())
					{
						case 1:
							htmltext = "32894-00.htm";
							break;
						case 2:
							htmltext = "32894-02.htm";
							break;
						case 5:
							htmltext = "32894-03.htm";
							break;
						case 6:
							htmltext = "32894-06.htm";
						case 3:
						case 4:
					}
			}
		}
		else if (npc.getNpcId() == 32901)
		{
			switch (st.getState())
			{
				case 2:
					htmltext = HtmCache.getInstance().getNotNull("data/html/completed-quest.htm", st.getPlayer());
					break;
				case 1:
					if (st.getCond() != 2)
					{
						break;
					}
					htmltext = "32901-00.htm";
			}
		}
		else if (npc.getNpcId() == 32903)
		{
			switch (st.getState())
			{
				case 2:
					htmltext = HtmCache.getInstance().getNotNull("data/html/completed-quest.htm", st.getPlayer());
					break;
				case 1:
					if (st.getCond() != 3)
					{
						break;
					}
					htmltext = "32903-00.htm";
			}
		}
		else if (npc.getNpcId() == 32902)
		{
			switch (st.getState())
			{
				case 2:
					htmltext = HtmCache.getInstance().getNotNull("data/html/completed-quest.htm", st.getPlayer());
					break;
				case 1:
					if (st.getCond() != 4)
					{
						break;
					}
					htmltext = "32902-00.htm";
			}
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
