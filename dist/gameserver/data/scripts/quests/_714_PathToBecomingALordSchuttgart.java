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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class _714_PathToBecomingALordSchuttgart extends Quest implements ScriptFile
{
	private static final int August = 35555;
	private static final int Newyear = 31961;
	private static final int Yasheni = 31958;
	private static final int GolemShard = 17162;
	private static final int ShuttgartCastle = 9;
	
	public _714_PathToBecomingALordSchuttgart()
	{
		super(false);
		addStartNpc(August);
		addTalkId(Newyear, Yasheni);
		for (int i = 22801; i < 22812; i++)
		{
			addKillId(i);
		}
		addQuestItem(GolemShard);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Castle castle = ResidenceHolder.getInstance().getResidence(ShuttgartCastle);
		if (castle.getOwner() == null)
		{
			return "Castle has no lord";
		}
		Player castleOwner = castle.getOwner().getLeader().getPlayer();
		if (event.equals("august_q714_03.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("august_q714_05.htm"))
		{
			st.setCond(2);
		}
		else if (event.equals("newyear_q714_03.htm"))
		{
			st.setCond(3);
		}
		else if (event.equals("yasheni_q714_02.htm"))
		{
			st.setCond(5);
		}
		else if (event.equals("august_q714_08.htm"))
		{
			Functions.npcSay(npc, NpcString.S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_SCHUTTGART, st.getPlayer().getName());
			castle.getDominion().changeOwner(castleOwner.getClan());
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		st.getState();
		int cond = st.getCond();
		Castle castle = ResidenceHolder.getInstance().getResidence(ShuttgartCastle);
		if (castle.getOwner() == null)
		{
			return "Castle has no lord";
		}
		Player castleOwner = castle.getOwner().getLeader().getPlayer();
		if (npcId == August)
		{
			if (cond == 0)
			{
				if (castleOwner == st.getPlayer())
				{
					if (castle.getDominion().getLordObjectId() != st.getPlayer().getObjectId())
					{
						htmltext = "august_q714_01.htm";
					}
					else
					{
						htmltext = "august_q714_00.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "august_q714_00a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "august_q714_04.htm";
			}
			else if (cond == 2)
			{
				htmltext = "august_q714_06.htm";
			}
			else if (cond == 7)
			{
				htmltext = "august_q714_07.htm";
			}
		}
		else if (npcId == Newyear)
		{
			if (cond == 2)
			{
				htmltext = "newyear_q714_01.htm";
			}
			else if (cond == 3)
			{
				QuestState q1 = st.getPlayer().getQuestState(_114_ResurrectionOfAnOldManager.class);
				QuestState q2 = st.getPlayer().getQuestState(_120_PavelsResearch.class);
				QuestState q3 = st.getPlayer().getQuestState(_121_PavelTheGiants.class);
				if ((q3 != null) && q3.isCompleted())
				{
					if ((q1 != null) && q1.isCompleted())
					{
						if ((q2 != null) && q2.isCompleted())
						{
							st.setCond(4);
							htmltext = "newyear_q714_04.htm";
						}
						else
						{
							htmltext = "newyear_q714_04a.htm";
						}
					}
					else
					{
						htmltext = "newyear_q714_04b.htm";
					}
				}
				else
				{
					htmltext = "newyear_q714_04c.htm";
				}
			}
		}
		else if (npcId == Yasheni)
		{
			if (cond == 4)
			{
				htmltext = "yasheni_q714_01.htm";
			}
			else if (cond == 5)
			{
				htmltext = "yasheni_q714_03.htm";
			}
			else if (cond == 6)
			{
				st.takeAllItems(GolemShard);
				st.setCond(7);
				htmltext = "yasheni_q714_04.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() == 5)
		{
			if (st.getQuestItemsCount(GolemShard) < 300)
			{
				st.giveItems(GolemShard, 1);
			}
			if (st.getQuestItemsCount(GolemShard) >= 300)
			{
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
