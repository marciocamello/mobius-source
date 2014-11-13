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

public class Q00126_TheNameOfEvilPart2 extends Quest implements ScriptFile
{
	// Npcs
	private final int Mushika = 32114;
	private final int Asamah = 32115;
	private final int UluKaimu = 32119;
	private final int BaluKaimu = 32120;
	private final int ChutaKaimu = 32121;
	private final int WarriorGrave = 32122;
	private final int ShilenStoneStatue = 32109;
	// Items
	private final int BONEPOWDER = 8783;
	private final int EPITAPH = 8781;
	private final int EWA = 729;
	
	public Q00126_TheNameOfEvilPart2()
	{
		super(false);
		addStartNpc(Asamah);
		addTalkId(Mushika, UluKaimu, BaluKaimu, ChutaKaimu, WarriorGrave, ShilenStoneStatue);
		addQuestItem(BONEPOWDER, EPITAPH);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "asamah_q126_4.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				qs.takeAllItems(EPITAPH);
				break;
			
			case "asamah_q126_7.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "ulukaimu_q126_2.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "ulukaimu_q126_8.htm":
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "ulukaimu_q126_10.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "balukaimu_q126_2.htm":
				qs.setCond(6);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "balukaimu_q126_7.htm":
				qs.setCond(7);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "balukaimu_q126_9.htm":
				qs.setCond(8);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "chutakaimu_q126_2.htm":
				qs.setCond(9);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "chutakaimu_q126_9.htm":
				qs.setCond(10);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "chutakaimu_q126_14.htm":
				qs.setCond(11);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "warriorgrave_q126_2.htm":
				qs.setCond(12);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "warriorgrave_q126_10.htm":
				qs.setCond(13);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "warriorgrave_q126_19.htm":
				qs.setCond(14);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "warriorgrave_q126_20.htm":
				qs.setCond(15);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "warriorgrave_q126_23.htm":
				qs.setCond(16);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "warriorgrave_q126_25.htm":
				qs.setCond(17);
				qs.giveItems(BONEPOWDER, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "warriorgrave_q126_27.htm":
				qs.setCond(18);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "shilenstatue_q126_2.htm":
				qs.setCond(19);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "shilenstatue_q126_13.htm":
				qs.setCond(20);
				qs.takeAllItems(BONEPOWDER);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "asamah_q126_10.htm":
				qs.setCond(21);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "asamah_q126_17.htm":
				qs.setCond(22);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "mushika_q126_3.htm":
				qs.setCond(23);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "mushika_q126_4.htm":
				qs.giveItems(EWA, 1);
				qs.giveItems(ADENA_ID, 484990);
				qs.addExpAndSp(2264190, 2572950);
				qs.playSound(SOUND_FINISH);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Asamah:
				switch (cond)
				{
					case 0:
						QuestState qst = qs.getPlayer().getQuestState(Q00125_TheNameOfEvilPart1.class);
						if ((qs.getPlayer().getLevel() >= 77) && (qst != null) && qst.isCompleted())
						{
							htmltext = "asamah_q126_1.htm";
						}
						else
						{
							htmltext = "asamah_q126_0.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 1:
						htmltext = "asamah_q126_4.htm";
						break;
					
					case 20:
						htmltext = "asamah_q126_8.htm";
						break;
					
					case 21:
						htmltext = "asamah_q126_10.htm";
						break;
					
					case 22:
						htmltext = "asamah_q126_17.htm";
						break;
					
					default:
						htmltext = "asamah_q126_0a.htm";
						break;
				}
				break;
			
			case UluKaimu:
				switch (cond)
				{
					case 2:
						htmltext = "ulukaimu_q126_1.htm";
						break;
					
					case 3:
						htmltext = "ulukaimu_q126_2.htm";
						break;
					
					case 4:
						htmltext = "ulukaimu_q126_8.htm";
						break;
					
					case 5:
						htmltext = "ulukaimu_q126_10.htm";
						break;
					
					default:
						htmltext = "ulukaimu_q126_0.htm";
						break;
				}
				break;
			
			case BaluKaimu:
				switch (cond)
				{
					case 5:
						htmltext = "balukaimu_q126_1.htm";
						break;
					
					case 6:
						htmltext = "balukaimu_q126_2.htm";
						break;
					
					case 7:
						htmltext = "balukaimu_q126_7.htm";
						break;
					
					case 8:
						htmltext = "balukaimu_q126_9.htm";
						break;
					
					default:
						htmltext = "balukaimu_q126_0.htm";
						break;
				}
				break;
			
			case ChutaKaimu:
				switch (cond)
				{
					case 8:
						htmltext = "chutakaimu_q126_1.htm";
						break;
					
					case 9:
						htmltext = "chutakaimu_q126_2.htm";
						break;
					
					case 10:
						htmltext = "chutakaimu_q126_9.htm";
						break;
					
					case 11:
						htmltext = "chutakaimu_q126_14.htm";
						break;
					
					default:
						htmltext = "chutakaimu_q126_0.htm";
						break;
				}
				break;
			
			case WarriorGrave:
				switch (cond)
				{
					case 11:
						htmltext = "warriorgrave_q126_1.htm";
						break;
					
					case 12:
						htmltext = "warriorgrave_q126_2.htm";
						break;
					
					case 13:
						htmltext = "warriorgrave_q126_10.htm";
						break;
					
					case 14:
						htmltext = "warriorgrave_q126_19.htm";
						break;
					
					case 15:
						htmltext = "warriorgrave_q126_20.htm";
						break;
					
					case 16:
						htmltext = "warriorgrave_q126_23.htm";
						break;
					
					case 17:
						htmltext = "warriorgrave_q126_25.htm";
						break;
					
					case 18:
						htmltext = "warriorgrave_q126_27.htm";
						break;
					
					default:
						htmltext = "warriorgrave_q126_0.htm";
						break;
				}
				break;
			
			case ShilenStoneStatue:
				switch (cond)
				{
					case 18:
						htmltext = "shilenstatue_q126_1.htm";
						break;
					
					case 19:
						htmltext = "shilenstatue_q126_2.htm";
						break;
					
					case 20:
						htmltext = "shilenstatue_q126_13.htm";
						break;
					
					default:
						htmltext = "shilenstatue_q126_0.htm";
						break;
				}
				break;
			
			case Mushika:
				if (cond == 22)
				{
					htmltext = "mushika_q126_1.htm";
				}
				else if (cond == 23)
				{
					htmltext = "mushika_q126_3.htm";
				}
				else
				{
					htmltext = "mushika_q126_0.htm";
				}
				break;
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
