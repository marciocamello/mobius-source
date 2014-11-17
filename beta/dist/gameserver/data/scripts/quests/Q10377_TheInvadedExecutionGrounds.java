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
public class Q10377_TheInvadedExecutionGrounds extends Quest implements ScriptFile
{
	// Npcs
	private static final int SYLVAIN = 30070;
	private static final int HARLAN = 30074;
	private static final int RODERIK = 30631;
	private static final int ENDRIGO = 30632;
	// Monsters
	private static final int HOUPON_THE_WARDEN_OVERSEER = 25886;
	private static final int CROOK_THE_MAD = 25887;
	private static final int EXECUTION_GROUNDS_WATCHMAN_GUILLOTINE = 25888;
	// Items
	private static final int SOE_GUILLOTINE_FORTRESS = 35292;
	private static final int HARLANS_ORDERS = 34972;
	private static final int ENDRIGOS_REPORT = 34973;
	
	public Q10377_TheInvadedExecutionGrounds()
	{
		super(true);
		addStartNpc(SYLVAIN);
		addTalkId(SYLVAIN, HARLAN, RODERIK, ENDRIGO);
		addKillId(HOUPON_THE_WARDEN_OVERSEER, CROOK_THE_MAD, EXECUTION_GROUNDS_WATCHMAN_GUILLOTINE);
		addQuestItem(HARLANS_ORDERS, ENDRIGOS_REPORT);
		addLevelCheck(95, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "sylvain_q10377_06.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "hitsran_q10377_03.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(HARLANS_ORDERS, 1);
				break;
			
			case "warden_roderik_q10377_02.htm":
				qs.takeItems(HARLANS_ORDERS, -1L);
				break;
			
			case "warden_roderik_q10377_03.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(ENDRIGOS_REPORT, 1);
				break;
			
			case "warden_endrigo_q10377_02.htm":
				qs.addExpAndSp(756106110, 338608890);
				qs.giveItems(ADENA_ID, 2970560, true);
				qs.giveItems(SOE_GUILLOTINE_FORTRESS, 2);
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
		
		switch (npc.getId())
		{
			case SYLVAIN:
				if (qs.isCompleted())
				{
					htmltext = "sylvain_q10377_03.htm";
				}
				else if (qs.isStarted())
				{
					htmltext = "sylvain_q10377_07.htm";
				}
				else
				{
					if (isAvailableFor(qs.getPlayer()))
					{
						htmltext = "sylvain_q10377_01.htm";
					}
					else
					{
						htmltext = "sylvain_q10377_02.htm";
					}
				}
				break;
			
			case HARLAN:
				if (cond == 1)
				{
					htmltext = "hitsran_q10377_01.htm";
				}
				else if (cond == 2)
				{
					htmltext = "hitsran_q10377_04.htm";
				}
				break;
			
			case RODERIK:
				if (cond == 2)
				{
					htmltext = "warden_roderik_q10377_01.htm";
				}
				else if (cond == 3)
				{
					htmltext = "warden_roderik_q10377_04.htm";
				}
				else if (cond == 6)
				{
					htmltext = "warden_roderik_q10377_05.htm";
				}
				break;
			
			case ENDRIGO:
				if (cond == 6)
				{
					qs.takeItems(ENDRIGOS_REPORT, -1);
					htmltext = "warden_endrigo_q10377_01.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case HOUPON_THE_WARDEN_OVERSEER:
				if (cond == 3)
				{
					qs.setCond(4);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case CROOK_THE_MAD:
				if (cond == 4)
				{
					qs.setCond(5);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case EXECUTION_GROUNDS_WATCHMAN_GUILLOTINE:
				if (cond == 5)
				{
					qs.setCond(6);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
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