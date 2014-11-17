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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

public class Q10292_SevenSignsMysteriousGirl extends Quest implements ScriptFile
{
	// Npcs
	private static final int WOOD = 32593;
	private static final int FRANZ = 32597;
	private static final int ELCARDIA = 32784;
	private static final int HARDIN = 30832;
	// Monsters
	private static final int[] MONSTERS =
	{
		22801,
		22802,
		22803,
		22804,
		22805,
		22806
	};
	private static final int CREATURE_OF_THE_DUSK_1 = 27422;
	private static final int CREATURE_OF_THE_DUSK_2 = 27424;
	// Item
	private static final int ELCARDIAS_MARK = 17226;
	
	public Q10292_SevenSignsMysteriousGirl()
	{
		super(false);
		addStartNpc(WOOD);
		addTalkId(WOOD, FRANZ, ELCARDIA, HARDIN);
		addKillId(MONSTERS);
		addKillId(CREATURE_OF_THE_DUSK_1, CREATURE_OF_THE_DUSK_2);
		addQuestItem(ELCARDIAS_MARK);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "priest_wood_q10292_1.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "priest_wood_q10292_4.htm":
				enterInstance(player, 145);
				break;
			
			case "witness_of_dawn_q10292_2.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "elcadia_abyssal_saintess_q10292_2.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "elcadia_abyssal_saintess_q10292_9.htm":
				qs.setCond(7);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "hardin_q10292_1.htm":
				qs.setCond(8);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "spawnTestMobs":
				int reflectId = player.getReflectionId();
				qs.set("CreatureOfTheDusk1", 1);
				qs.set("CreatureOfTheDusk2", 1);
				addSpawnToInstance(CREATURE_OF_THE_DUSK_1, 89416, -237992, -9632, 0, 0, reflectId);
				addSpawnToInstance(CREATURE_OF_THE_DUSK_2, 89416, -238136, -9632, 0, 0, reflectId);
				return null;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		if (player.getBaseClassId() != player.getActiveClassId())
		{
			return "no_subclass_allowed.htm";
		}
		
		switch (npc.getId())
		{
			case WOOD:
				if (cond == 0)
				{
					final QuestState state = player.getQuestState(Q00198_SevenSignsEmbryo.class);
					
					if ((player.getLevel() >= 81) && (state != null) && state.isCompleted())
					{
						htmltext = "priest_wood_q10292_0.htm";
					}
					else
					{
						htmltext = "priest_wood_q10292_0n.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "priest_wood_q10292_3.htm";
				}
				else if ((cond > 1) && !qs.isCompleted())
				{
					htmltext = "priest_wood_q10292_5.htm";
				}
				else if (qs.isCompleted())
				{
					htmltext = "priest_wood_q10292_6.htm";
				}
				break;
			
			case FRANZ:
				if (cond == 1)
				{
					htmltext = "witness_of_dawn_q10292_0.htm";
				}
				else if (cond == 2)
				{
					htmltext = "witness_of_dawn_q10292_4.htm";
				}
				break;
			
			case ELCARDIA:
				switch (cond)
				{
					case 2:
						htmltext = "elcadia_abyssal_saintess_q10292_0.htm";
						break;
					
					case 3:
						htmltext = "elcadia_abyssal_saintess_q10292_2.htm";
						break;
					
					case 4:
						htmltext = "elcadia_abyssal_saintess_q10292_3.htm";
						qs.takeItems(ELCARDIAS_MARK, -1);
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(5);
						break;
					
					case 5:
						htmltext = "elcadia_abyssal_saintess_q10292_5.htm";
						break;
					
					case 6:
						htmltext = "elcadia_abyssal_saintess_q10292_6.htm";
						break;
					
					case 7:
						htmltext = "elcadia_abyssal_saintess_q10292_9.htm";
						break;
					
					case 8:
						htmltext = "elcadia_abyssal_saintess_q10292_10.htm";
						qs.addExpAndSp(10000000, 1000000);
						qs.setState(COMPLETED);
						qs.exitCurrentQuest(false);
						qs.playSound(SOUND_FINISH);
						break;
				}
				break;
			
			case HARDIN:
				if (cond == 7)
				{
					htmltext = "hardin_q10292_0.htm";
				}
				else if (cond == 8)
				{
					htmltext = "hardin_q10292_2.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		
		if ((qs.getCond() == 3) && Util.contains(MONSTERS, npcId) && Rnd.chance(70))
		{
			qs.giveItems(ELCARDIAS_MARK, 1);
			
			if (qs.getQuestItemsCount(ELCARDIAS_MARK) < 10)
			{
				qs.playSound(SOUND_ITEMGET);
			}
			else
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(4);
			}
		}
		else if (npcId == CREATURE_OF_THE_DUSK_1)
		{
			qs.set("CreatureOfTheDusk1", 2);
			
			if ((qs.get("CreatureOfTheDusk2") != null) && (Integer.parseInt(qs.get("CreatureOfTheDusk2")) == 2))
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(6);
			}
		}
		else if (npcId == CREATURE_OF_THE_DUSK_2)
		{
			qs.set("CreatureOfTheDusk2", 2);
			
			if ((qs.get("CreatureOfTheDusk1") != null) && (Integer.parseInt(qs.get("CreatureOfTheDusk1")) == 2))
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(6);
			}
		}
		
		return null;
	}
	
	private void enterInstance(Player player, int instancedZoneId)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(instancedZoneId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(instancedZoneId))
		{
			ReflectionUtils.enterReflection(player, instancedZoneId);
		}
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
