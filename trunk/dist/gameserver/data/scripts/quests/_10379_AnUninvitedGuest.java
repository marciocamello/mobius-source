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

/**
 * @author GodWorld & Bonux
 */
public class _10379_AnUninvitedGuest extends Quest implements ScriptFile
{
	// NPC's
	private static final int ENDRIGO = 30632;
	private static final int SCALDISECT_THE_FURIOUS = 23212;
	
	// Item's
	private static final int SOE_GUILLOTINE_FORTRESS = 35292;
	
	public _10379_AnUninvitedGuest()
	{
		super(true);
		addStartNpc(ENDRIGO);
		addTalkId(ENDRIGO);
		addKillId(SCALDISECT_THE_FURIOUS);
		addKillNpcWithLog(1, "SCALDISECT_THE_FURIOUS", 1, SCALDISECT_THE_FURIOUS);
		addLevelCheck(95, 100);
		addQuestCompletedCheck(_10377_TheInvadedExecutionGrounds.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("warden_endrigo_q10379_06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("warden_endrigo_q10379_09.htm"))
		{
			st.addExpAndSp(934013430, 418281570);
			st.giveItems(ADENA_ID, 3441680, true);
			st.giveItems(SOE_GUILLOTINE_FORTRESS, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		String htmltext = "noquest";
		if (npcId == ENDRIGO)
		{
			if (st.isCompleted())
			{
				htmltext = "warden_endrigo_q10379_03.htm";
			}
			else if (st.isStarted())
			{
				if (cond == 1)
				{
					htmltext = "warden_endrigo_q10379_07.htm";
				}
				else if (cond == 2)
				{
					htmltext = "warden_endrigo_q10379_08.htm";
				}
			}
			else
			{
				if (isAvailableFor(st.getPlayer()))
				{
					htmltext = "warden_endrigo_q10379_01.htm";
				}
				else
				{
					htmltext = "warden_endrigo_q10379_02.htm";
				}
			}
			
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == SCALDISECT_THE_FURIOUS)
		{
			if (cond == 1)
			{
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
			}
		}
		return null;
	}
	
	@Override
	public void onLoad()
	{
		//
	}
	
	@Override
	public void onReload()
	{
		//
	}
	
	@Override
	public void onShutdown()
	{
		//
	}
}