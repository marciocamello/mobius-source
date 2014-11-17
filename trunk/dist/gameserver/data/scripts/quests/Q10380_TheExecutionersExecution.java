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
public class Q10380_TheExecutionersExecution extends Quest implements ScriptFile
{
	// Npc
	private static final int ENDRIGO = 30632;
	// Monster
	private static final int GUILLOTINE_OF_DEATH = 25892;
	// Item
	private static final int GLORIOUS_T_SHIRT = 35291;
	
	public Q10380_TheExecutionersExecution()
	{
		super(PARTY_ALL);
		addStartNpc(ENDRIGO);
		addTalkId(ENDRIGO);
		addKillId(GUILLOTINE_OF_DEATH);
		addKillNpcWithLog(1, "GUILLOTINE_OF_DEATH", 1, GUILLOTINE_OF_DEATH);
		addLevelCheck(95, 100);
		addQuestCompletedCheck(Q10379_AnUninvitedGuest.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "warden_endrigo_q10380_06.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "warden_endrigo_q10380_10.htm":
				qs.addExpAndSp(0, 458117910);
				qs.giveItems(GLORIOUS_T_SHIRT, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (qs.isCompleted())
		{
			htmltext = "warden_endrigo_q10380_03.htm";
		}
		else if (qs.isStarted())
		{
			if (cond == 1)
			{
				htmltext = "warden_endrigo_q10380_07.htm";
			}
			else if (cond == 2)
			{
				htmltext = "warden_endrigo_q10380_08.htm";
			}
		}
		else
		{
			if (isAvailableFor(qs.getPlayer()))
			{
				htmltext = "warden_endrigo_q10380_01.htm";
			}
			else
			{
				htmltext = "warden_endrigo_q10380_02.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
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