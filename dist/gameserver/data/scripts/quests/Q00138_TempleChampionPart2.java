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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00138_TempleChampionPart2 extends Quest implements ScriptFile
{
	// Npcs
	private static final int SYLVAIN = 30070;
	private static final int PUPINA = 30118;
	private static final int ANGUS = 30474;
	private static final int SLA = 30666;
	// Items
	private static final int MANIFESTO = 10341;
	private static final int RELIC = 10342;
	private static final int ANGUS_REC = 10343;
	private static final int PUPINA_REC = 10344;
	// Monsters
	private final static int Wyrm = 20176;
	private final static int GuardianBasilisk = 20550;
	private final static int RoadScavenger = 20551;
	private final static int FetteredSoul = 20552;
	
	public Q00138_TempleChampionPart2()
	{
		super(false);
		addFirstTalkId(SYLVAIN);
		addTalkId(SYLVAIN, PUPINA, ANGUS, SLA);
		addKillId(Wyrm, GuardianBasilisk, RoadScavenger, FetteredSoul);
		addQuestItem(MANIFESTO, RELIC, ANGUS_REC, PUPINA_REC);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "sylvain_q0138_04.htm":
				st.setCond(1);
				st.playSound(SOUND_ACCEPT);
				st.giveItems(MANIFESTO, 1);
				break;
			
			case "sylvain_q0138_09.htm":
				st.giveItems(ADENA_ID, 84593);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
				break;
			
			case "sylvain_q0138_06.htm":
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
				break;
			
			case "pupina_q0138_08.htm":
				st.setCond(3);
				st.playSound(SOUND_MIDDLE);
				break;
			
			case "pupina_q0138_11.htm":
				st.setCond(6);
				st.playSound(SOUND_MIDDLE);
				st.set("talk", "0");
				st.giveItems(PUPINA_REC, 1);
				break;
			
			case "grandmaster_angus_q0138_03.htm":
				st.setCond(4);
				st.playSound(SOUND_MIDDLE);
				break;
			
			case "preacher_sla_q0138_03.htm":
				st.set("talk", "1");
				st.takeItems(PUPINA_REC, -1);
				break;
			
			case "preacher_sla_q0138_05.htm":
				st.set("talk", "2");
				st.takeItems(MANIFESTO, -1);
				break;
			
			case "preacher_sla_q0138_12.htm":
				st.setCond(7);
				st.playSound(SOUND_MIDDLE);
				st.unset("talk");
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case SYLVAIN:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() >= 36)
						{
							htmltext = "sylvain_q0138_01.htm";
						}
						else
						{
							htmltext = "sylvain_q0138_03.htm";
						}
						break;
					
					case 1:
						htmltext = "sylvain_q0138_04.htm";
						break;
					
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						htmltext = "sylvain_q0138_06.htm";
						break;
					
					case 7:
						htmltext = "sylvain_q0138_08.htm";
						break;
				}
				break;
			
			case PUPINA:
				switch (cond)
				{
					case 2:
						htmltext = "pupina_q0138_02.htm";
						break;
					
					case 3:
					case 4:
						htmltext = "pupina_q0138_09.htm";
						break;
					
					case 5:
						htmltext = "pupina_q0138_10.htm";
						qs.takeItems(ANGUS_REC, -1);
						break;
					
					case 6:
						htmltext = "pupina_q0138_13.htm";
						break;
				}
				break;
			
			case ANGUS:
				switch (cond)
				{
					case 3:
						htmltext = "grandmaster_angus_q0138_02.htm";
						break;
					
					case 4:
						if (qs.getQuestItemsCount(RELIC) >= 10)
						{
							htmltext = "grandmaster_angus_q0138_05.htm";
							qs.takeItems(RELIC, -1);
							qs.giveItems(ANGUS_REC, 1);
							qs.setCond(5);
							qs.playSound(SOUND_MIDDLE);
						}
						else
						{
							htmltext = "grandmaster_angus_q0138_04.htm";
						}
						break;
					
					case 5:
						htmltext = "grandmaster_angus_q0138_06.htm";
						break;
				}
				break;
			
			case SLA:
				if (cond == 6)
				{
					switch (qs.getInt("talk"))
					{
						case 0:
							htmltext = "preacher_sla_q0138_02.htm";
							break;
						
						case 1:
							htmltext = "preacher_sla_q0138_03.htm";
							break;
						
						case 2:
							htmltext = "preacher_sla_q0138_05.htm";
							break;
					}
				}
				else if (cond == 7)
				{
					htmltext = "preacher_sla_q0138_13.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(Q00137_TempleChampionPart1.class);
		
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		
		return "";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 4)
		{
			if (qs.getQuestItemsCount(RELIC) < 10)
			{
				qs.giveItems(RELIC, 1);
				
				if (qs.getQuestItemsCount(RELIC) >= 10)
				{
					qs.playSound(SOUND_MIDDLE);
				}
				else
				{
					qs.playSound(SOUND_ITEMGET);
				}
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
