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

public class _713_PathToBecomingALordAden extends Quest implements ScriptFile
{
	private static final int Logan = 35274;
	private static final int Orven = 30857;
	private static final int[] Orcs =
	{
		20669,
		20665
	};
	private static final int AdenCastle = 5;
	private int _mobs = 0;
	
	public _713_PathToBecomingALordAden()
	{
		super(false);
		addStartNpc(Logan);
		addTalkId(Orven);
		addKillId(Orcs);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Castle castle = ResidenceHolder.getInstance().getResidence(AdenCastle);
		if (castle.getOwner() == null)
		{
			return "Castle has no lord";
		}
		Player castleOwner = castle.getOwner().getLeader().getPlayer();
		if (event.equals("logan_q713_02.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("orven_q713_03.htm"))
		{
			st.setCond(2);
		}
		else if (event.equals("logan_q713_05.htm"))
		{
			Functions.npcSay(npc, NpcString.S1_HAS_BECOME_THE_LORD_OF_THE_TOWN_OF_ADEN, st.getPlayer().getName());
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
		Castle castle = ResidenceHolder.getInstance().getResidence(AdenCastle);
		if (castle.getOwner() == null)
		{
			return "Castle has no lord";
		}
		Player castleOwner = castle.getOwner().getLeader().getPlayer();
		if (npcId == Logan)
		{
			if (cond == 0)
			{
				if (castleOwner == st.getPlayer())
				{
					if (castle.getDominion().getLordObjectId() != st.getPlayer().getObjectId())
					{
						htmltext = "logan_q713_01.htm";
					}
					else
					{
						htmltext = "logan_q713_00.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "logan_q713_00a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "logan_q713_03.htm";
			}
			else if (cond == 7)
			{
				htmltext = "logan_q713_04.htm";
			}
		}
		else if (npcId == Orven)
		{
			if (cond == 1)
			{
				htmltext = "orven_q713_01.htm";
			}
			else if (cond == 2)
			{
				htmltext = "orven_q713_04.htm";
			}
			else if (cond == 4)
			{
				htmltext = "orven_q713_05.htm";
			}
			else if (cond == 5)
			{
				st.setCond(7);
				htmltext = "orven_q713_06.htm";
			}
			else if (cond == 7)
			{
				htmltext = "orven_q713_06.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() == 4)
		{
			if (_mobs < 100)
			{
				_mobs++;
			}
			else
			{
				st.setCond(5);
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
