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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00038_DragonFangs extends Quest implements ScriptFile
{
	// Npcs
	public final int ROHMER = 30344;
	public final int LUIS = 30386;
	public final int IRIS = 30034;
	// Monsters
	public final int LANGK_LIZARDMAN_LIEUTENANT = 20357;
	public final int LANGK_LIZARDMAN_SENTINEL = 21100;
	public final int LANGK_LIZARDMAN_LEADER = 20356;
	public final int LANGK_LIZARDMAN_SHAMAN = 21101;
	// Items
	public final int FEATHER_ORNAMENT = 7173;
	public final int TOOTH_OF_TOTEM = 7174;
	public final int LETTER_OF_IRIS = 7176;
	public final int LETTER_OF_ROHMER = 7177;
	public final int TOOTH_OF_DRAGON = 7175;
	public final int BONE_HELMET = 45;
	public final int ASSAULT_BOOTS = 1125;
	public final int BLUE_BUCKSKIN_BOOTS = 1123;
	// Other
	public final int CHANCE_FOR_QUEST_ITEMS = 100;
	
	public Q00038_DragonFangs()
	{
		super(false);
		addStartNpc(LUIS);
		addTalkId(IRIS, ROHMER);
		addKillId(LANGK_LIZARDMAN_LEADER, LANGK_LIZARDMAN_SHAMAN, LANGK_LIZARDMAN_SENTINEL, LANGK_LIZARDMAN_LIEUTENANT);
		addQuestItem(TOOTH_OF_TOTEM, LETTER_OF_IRIS, LETTER_OF_ROHMER, TOOTH_OF_DRAGON, FEATHER_ORNAMENT);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "guard_luis_q0038_0104.htm":
				if (cond == 0)
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "guard_luis_q0038_0201.htm":
				if (cond == 2)
				{
					qs.setCond(3);
					qs.takeItems(FEATHER_ORNAMENT, 100);
					qs.giveItems(TOOTH_OF_TOTEM, 1);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "iris_q0038_0301.htm":
				if (cond == 3)
				{
					qs.setCond(4);
					qs.takeItems(TOOTH_OF_TOTEM, 1);
					qs.giveItems(LETTER_OF_IRIS, 1);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "magister_roh_q0038_0401.htm":
				if (cond == 4)
				{
					qs.setCond(5);
					qs.takeItems(LETTER_OF_IRIS, 1);
					qs.giveItems(LETTER_OF_ROHMER, 1);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "iris_q0038_0501.htm":
				if (cond == 5)
				{
					qs.setCond(6);
					qs.takeItems(LETTER_OF_ROHMER, 1);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "iris_q0038_0601.htm":
				if (cond == 7)
				{
					qs.takeItems(TOOTH_OF_DRAGON, 50);
					final int luck = Rnd.get(3);
					
					switch (luck)
					{
						case 0:
							qs.giveItems(BLUE_BUCKSKIN_BOOTS, 1);
							qs.giveItems(ADENA_ID, 1500);
							break;
						
						case 1:
							qs.giveItems(BONE_HELMET, 1);
							qs.giveItems(ADENA_ID, 5200);
							break;
						
						case 2:
							qs.giveItems(ASSAULT_BOOTS, 1);
							qs.giveItems(ADENA_ID, 1500);
							break;
					}
					
					qs.addExpAndSp(435117, 23977);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
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
			case LUIS:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() < 19)
						{
							htmltext = "guard_luis_q0038_0102.htm";
							qs.exitCurrentQuest(true);
						}
						else if (qs.getPlayer().getLevel() >= 19)
						{
							htmltext = "guard_luis_q0038_0101.htm";
						}
						break;
					
					case 1:
						htmltext = "guard_luis_q0038_0202.htm";
						break;
					
					case 2:
						if (qs.getQuestItemsCount(FEATHER_ORNAMENT) == 100)
						{
							htmltext = "guard_luis_q0038_0105.htm";
						}
						break;
					
					case 3:
						htmltext = "guard_luis_q0038_0203.htm";
						break;
				}
				break;
			
			case IRIS:
				switch (cond)
				{
					case 3:
						if (qs.getQuestItemsCount(TOOTH_OF_TOTEM) == 1)
						{
							htmltext = "iris_q0038_0201.htm";
						}
						break;
					
					case 4:
						htmltext = "iris_q0038_0303.htm";
						break;
					
					case 5:
						if (qs.getQuestItemsCount(LETTER_OF_ROHMER) == 1)
						{
							htmltext = "iris_q0038_0401.htm";
						}
						break;
					
					case 6:
						htmltext = "iris_q0038_0602.htm";
						break;
					
					case 7:
						if (qs.getQuestItemsCount(TOOTH_OF_DRAGON) == 50)
						{
							htmltext = "iris_q0038_0503.htm";
						}
						break;
				}
				break;
			
			case ROHMER:
				if ((cond == 4) && (qs.getQuestItemsCount(LETTER_OF_IRIS) == 1))
				{
					htmltext = "magister_roh_q0038_0301.htm";
				}
				else if (cond == 5)
				{
					htmltext = "magister_roh_q0038_0403.htm";
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final boolean chance = Rnd.chance(CHANCE_FOR_QUEST_ITEMS);
		
		switch (npcId)
		{
			case LANGK_LIZARDMAN_LIEUTENANT:
			case LANGK_LIZARDMAN_SENTINEL:
				if ((cond == 1) && chance && (qs.getQuestItemsCount(FEATHER_ORNAMENT) < 100))
				{
					qs.giveItems(FEATHER_ORNAMENT, 1);
					
					if (qs.getQuestItemsCount(FEATHER_ORNAMENT) == 100)
					{
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(2);
					}
					else
					{
						qs.playSound(SOUND_ITEMGET);
					}
				}
				break;
			
			case LANGK_LIZARDMAN_LEADER:
			case LANGK_LIZARDMAN_SHAMAN:
				if ((cond == 6) && chance && (qs.getQuestItemsCount(TOOTH_OF_DRAGON) < 50))
				{
					qs.giveItems(TOOTH_OF_DRAGON, 1);
					
					if (qs.getQuestItemsCount(TOOTH_OF_DRAGON) == 50)
					{
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(7);
					}
					else
					{
						qs.playSound(SOUND_ITEMGET);
					}
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
