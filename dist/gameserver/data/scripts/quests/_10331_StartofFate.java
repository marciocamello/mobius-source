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

import instances.LabyrinthOfBelis;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import ai.InfiltrationOfficer.State;

public class _10331_StartofFate extends Quest implements ScriptFile
{
	private static final int FRANCO = 32153;
	private static final int RIVIAN = 32147;
	private static final int DEVON = 32160;
	private static final int TOOK = 32150;
	private static final int MOKA = 32157;
	private static final int VALFAR = 32146;
	private static final int LAKCIS = 32977;
	private static final int SEBION = 32978;
	private static final int PANTHEON = 32972;
	private static final int OFFICER = 19155;
	private static final int VERIFICATOR = 33215;
	private static final int OPERATIVE = 22998;
	private static final int NEMERTESS = 22984;
	private static final int BELIS_MARK = 17615;
	private static final int SARIL_NECKLACE = 17580;
	private static final int PROOF_OF_COURAGE = 17821;
	private static final int SECOND_DOOR = 16240002;
	private static final int THIRD_DOOR = 16240003;
	private static final int FOURTH_DOOR = 16240004;
	private static final int FIFTH_DOOR = 16240005;
	private static final int SIXTH_DOOR = 16240006;
	private static final int SEVENTH_DOOR = 16240007;
	private static final int EIGHTH_DOOR = 16240008;
	private static final int LABYRINTH_OF_BELIS = 178;
	
	public _10331_StartofFate()
	{
		super(false);
		addStartNpc(FRANCO, RIVIAN, DEVON, TOOK, MOKA, VALFAR);
		addTalkId(OFFICER, LAKCIS, SEBION, VERIFICATOR, PANTHEON);
		addFirstTalkId(OFFICER, VERIFICATOR);
		addAttackId(OPERATIVE, NEMERTESS);
		addKillId(OPERATIVE, NEMERTESS, OFFICER);
		addQuestItem(SARIL_NECKLACE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		LabyrinthOfBelis lob = (LabyrinthOfBelis) qs.getPlayer().getActiveReflection();
		int cond = qs.getCond();
		if (event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "master_q10331_3.htm";
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
			qs.setCond(1);
		}
		else if ((cond == 1) && event.equalsIgnoreCase("telep"))
		{
			htmltext = "";
			qs.getPlayer().teleToLocation(new Location(-111784, 231864, -3195));
			qs.setCond(2);
		}
		else if ((cond == 2) && event.equalsIgnoreCase("preinstance_talk"))
		{
			htmltext = "2-3.htm";
			qs.setCond(3);
		}
		else if ((cond == 3) && event.equalsIgnoreCase("enter_instance"))
		{
			htmltext = "";
			enterInstance(qs.getPlayer());
		}
		else if ((cond == 3) && event.equalsIgnoreCase("first_door"))
		{
			htmltext = "";
			lob.makeOnEvent(State.AI_FOLLOW, SECOND_DOOR);
		}
		else if ((cond == 3) && (lob.getInstanceCond() == 2) && event.equalsIgnoreCase("fourth_door"))
		{
			htmltext = "";
			onEvent("belis_mark_message", qs, null);
			lob.makeOnEvent(State.AI_FOLLOW, FOURTH_DOOR);
		}
		else if ((cond == 3) && (lob.getInstanceCond() == 4) && event.equalsIgnoreCase("sixth_door"))
		{
			htmltext = "";
			lob.activateGenerator(qs.getPlayer());
			lob.makeOnEvent(State.AI_ATTACK_GENERATOR, SIXTH_DOOR);
			onEvent("attack_officer", qs, null);
		}
		else if ((cond == 3) && event.equalsIgnoreCase("attack_officer"))
		{
			htmltext = "";
			if ((lob.getInstanceCond() > 4) && (lob.getInstanceCond() < 12))
			{
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.BEHIND_YOU_THE_ENEMY_IS_AMBUSING_YOU, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, true));
				lob.spawnAttackers();
				lob.makeOnEvent(State.AI_ATTACK_GENERATOR, 0);
				qs.startQuestTimer("attack_officer", 10000);
			}
			else if (lob.getInstanceCond() == 12)
			{
				lob.deleteGenerator();
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ELECTRONIC_DEVICE_HAS_BEEN_DESTROYED, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false));
				lob.makeOnEvent(State.AI_NEXT_STEP, SEVENTH_DOOR);
			}
		}
		else if ((cond == 3) && (lob.getInstanceCond() == 13) && event.equalsIgnoreCase("eighth_door"))
		{
			qs.startQuestTimer("spawn", 60000);
			lob.makeOnEvent(State.AI_NEXT_STEP, EIGHTH_DOOR);
			qs.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_TALKING_ISLAND_BOSS_OPENING);
		}
		else if ((cond == 3) && event.equalsIgnoreCase("belis_mark_message"))
		{
			htmltext = "";
			if (lob.getInstanceCond() != 3)
			{
				qs.stopQuestTimers();
				return htmltext;
			}
			qs.startQuestTimer("belis_mark_message", 8000);
			qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.MARK_OF_BELIS_CAN_BE_ACQUIRED_FROM_ENEMIES, 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
		}
		else if ((cond == 3) && event.equalsIgnoreCase("verificate"))
		{
			long marksCount = qs.getQuestItemsCount(BELIS_MARK);
			if (marksCount <= 0)
			{
				return "You have not enough items to do that";
			}
			qs.takeItems(BELIS_MARK, 1);
			lob.reduceMarksRequiered();
			int marksRequiered = lob.getMarksRequieredCount();
			if (marksRequiered <= 0)
			{
				htmltext = "4-5.htm";
				lob.makeOnEvent(State.AI_NEXT_STEP, FIFTH_DOOR);
			}
			else
			{
				if (marksRequiered == 1)
				{
					htmltext = "4-4.htm";
				}
				else if (marksRequiered == 2)
				{
					htmltext = "4-2.htm";
				}
				else
				{
					htmltext = "4-5.htm";
				}
			}
		}
		else if ((cond == 5) && event.equalsIgnoreCase("give_sarils_necklace"))
		{
			Race race = qs.getPlayer().getRace();
			qs.takeAllItems(SARIL_NECKLACE);
			if (race == Race.human)
			{
				htmltext = "5-2h.htm";
				qs.setCond(6);
			}
			else if (race == Race.elf)
			{
				htmltext = "5-2e.htm";
				qs.setCond(7);
			}
			else if (race == Race.darkelf)
			{
				htmltext = "5-2de.htm";
				qs.setCond(8);
			}
			else if (race == Race.orc)
			{
				qs.setCond(9);
				htmltext = "5-2o.htm";
			}
			else if (race == Race.dwarf)
			{
				qs.setCond(10);
				htmltext = "5-2d.htm";
			}
			else if (race == Race.kamael)
			{
				htmltext = "5-2k.htm";
				qs.setCond(11);
			}
		}
		else if ((cond >= 6) && (cond <= 11) && event.startsWith("change_prof"))
		{
			htmltext = "";
			Player player = qs.getPlayer();
			String prof = event.substring("change_prof:".length());
			if (player.isMageClass() && (player.getRace() != Race.kamael))
			{
				if (player.getClassId() == ClassId.HUMAN_MAGE)
				{
					if (prof.equals("human_wizard"))
					{
						player.setClassId(ClassId.WIZARD.ordinal(), false, true);
					}
					else if (prof.equals("cleric"))
					{
						player.setClassId(ClassId.CLERIC.ordinal(), false, true);
					}
				}
				else if (player.getClassId() == ClassId.ELVEN_MAGE)
				{
					if (prof.equals("elven_wizard"))
					{
						player.setClassId(ClassId.ELVEN_WIZARD.ordinal(), false, true);
					}
					else if (prof.equals("oracle"))
					{
						player.setClassId(ClassId.ORACLE.ordinal(), false, true);
					}
				}
				else if (player.getClassId() == ClassId.DARK_MAGE)
				{
					if (prof.equals("dark_wizard"))
					{
						player.setClassId(ClassId.DARK_WIZARD.ordinal(), false, true);
					}
					else if (prof.equals("shillien_oracle"))
					{
						player.setClassId(ClassId.SHILLEN_ORACLE.ordinal(), false, true);
					}
				}
				else if (player.getClassId() == ClassId.ORC_MAGE)
				{
					if (prof.equals("shaman"))
					{
						player.setClassId(ClassId.ORC_SHAMAN.ordinal(), false, true);
					}
				}
			}
			else if (player.getRace() != Race.kamael)
			{
				if (player.getClassId() == ClassId.HUMAN_FIGHTER)
				{
					if (prof.equals("warrior"))
					{
						player.setClassId(ClassId.WARRIOR.ordinal(), false, true);
					}
					else if (prof.equals("human_knight"))
					{
						player.setClassId(ClassId.KNIGHT.ordinal(), false, true);
					}
					else if (prof.equals("rogue"))
					{
						player.setClassId(ClassId.ROGUE.ordinal(), false, true);
					}
				}
				else if (player.getClassId() == ClassId.ELVEN_FIGHTER)
				{
					if (prof.equals("elven_knight"))
					{
						player.setClassId(ClassId.ELVEN_KNIGHT.ordinal(), false, true);
					}
					else if (prof.equals("elven_scout"))
					{
						player.setClassId(ClassId.ELVEN_SCOUT.ordinal(), false, true);
					}
				}
				else if (player.getClassId() == ClassId.DARK_FIGHTER)
				{
					if (prof.equals("palus_knight"))
					{
						player.setClassId(ClassId.PALUS_KNIGHT.ordinal(), false, true);
					}
					else if (prof.equals("assassin"))
					{
						player.setClassId(ClassId.ASSASIN.ordinal(), false, true);
					}
				}
				else if (player.getClassId() == ClassId.ORC_FIGHTER)
				{
					if (prof.equals("raider"))
					{
						player.setClassId(ClassId.ORC_RAIDER.ordinal(), false, true);
					}
					else if (prof.equals("monk"))
					{
						player.setClassId(ClassId.ORC_MONK.ordinal(), false, true);
					}
				}
				else if (player.getClassId() == ClassId.DWARVEN_FIGHTER)
				{
					if (prof.equals("scavenger"))
					{
						player.setClassId(ClassId.SCAVENGER.ordinal(), false, true);
					}
					else if (prof.equals("artisan"))
					{
						player.setClassId(ClassId.ARTISAN.ordinal(), false, true);
					}
				}
			}
			else if (player.getSex() == 0)
			{
				if (prof.equals("trooper"))
				{
					player.setClassId(ClassId.TROOPER.ordinal(), false, true);
				}
			}
			else
			{
				if (prof.equals("warder"))
				{
					player.setClassId(ClassId.WARDER.ordinal(), false, true);
				}
			}
			qs.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.QT_009, TutorialShowHtml.TYPE_WINDOW));
			qs.getPlayer().addExpAndSp(200000, 40000);
			qs.giveItems(ADENA_ID, 80000);
			qs.giveItems(PROOF_OF_COURAGE, 40);
			qs.exitCurrentQuest(false);
			MultiSellHolder.getInstance().SeparateAndSend(85555, qs.getPlayer(), 0);
			qs.playSound(SOUND_FINISH);
		}
		else if (event.equalsIgnoreCase("move"))
		{
			htmltext = "";
		}
		else if (event.equalsIgnoreCase("spawn"))
		{
			htmltext = "";
			qs.getPlayer().getActiveReflection().addSpawnWithoutRespawn(NEMERTESS, new Location(-118328, 212968, -8704), 0);
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		LabyrinthOfBelis lob = (LabyrinthOfBelis) qs.getPlayer().getActiveReflection();
		if ((qs.getCond() == 3) && (lob.getInstanceCond() == 1) && (npc.getNpcId() == OPERATIVE))
		{
			lob.incOperativesKilled();
			if (lob.getOperativesKilledCount() >= 6)
			{
				lob.makeOnEvent(State.AI_NEXT_STEP, THIRD_DOOR);
			}
		}
		else if ((qs.getCond() == 3) && (lob.getInstanceCond() == 14) && (npc.getNpcId() == NEMERTESS))
		{
			qs.startQuestTimer("move", 30000);
			qs.takeAllItems(SARIL_NECKLACE);
			qs.giveItems(SARIL_NECKLACE, 1, false);
			qs.setCond(4);
			qs.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_TALKING_ISLAND_BOSS_ENDING);
		}
		else if ((qs.getCond() > 2) && (npc.getNpcId() == OFFICER))
		{
			if (qs.getPlayer().getReflection() != null)
			{
				qs.getPlayer().getReflection().collapse();
			}
		}
		return "";
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = qs.getCond();
		Player player = qs.getPlayer();
		if (player.getClassLevel() > 1)
		{
			return "You cannot pass this quest because you already have profession.";
		}
		switch (npcId)
		{
			case FRANCO:
			case RIVIAN:
			case DEVON:
			case TOOK:
			case MOKA:
			case VALFAR:
				if (cond == 0)
				{
					Race race = player.getRace();
					if (((race == Race.human) && (npcId != FRANCO)) || ((race == Race.elf) && (npcId != RIVIAN)) || ((race == Race.darkelf) && (npcId != DEVON)) || ((race == Race.orc) && (npcId != TOOK)) || ((race == Race.dwarf) && (npcId != MOKA)) || ((race == Race.kamael) && (npcId != VALFAR)))
					{
						break;
					}
					htmltext = "master_q10331_1.htm";
				}
				else if ((cond >= 6) && (cond <= 11))
				{
					Race race = player.getRace();
					if ((cond == 0) && (qs.getPlayer().getLevel() < 85)) 		
					{ 		
						htmltext = "Only characters under level 85 can accept this quest"; 		
					}
					else if (player.isMageClass())
					{
						if (race == Race.human)
						{
							htmltext = "master_q10331_human_mage";
						}
						else if (race == Race.elf)
						{
							htmltext = "master_q10331_elf_mage";
						}
						else if (race == Race.darkelf)
						{
							htmltext = "master_q10331_darkelf_mage";
						}
						else if (race == Race.orc)
						{
							htmltext = "master_q10331_orc_mage";
						}
					}
					else
					{
						if (race == Race.human)
						{
							htmltext = "master_q10331_human_fighter";
						}
						else if (race == Race.elf)
						{
							htmltext = "master_q10331_elf_fighter";
						}
						else if (race == Race.darkelf)
						{
							htmltext = "master_q10331_darkelf_fighter";
						}
						else if (race == Race.orc)
						{
							htmltext = "master_q10331_orc_fighter";
						}
					}
					if (race == Race.dwarf)
					{
						htmltext = "master_q10331_dwarf";
					}
					if (race == Race.kamael)
					{
						if (player.getSex() == 0)
						{
							htmltext = "master_q10331_kamael_male";
						}
						else
						{
							htmltext = "master_q10331_kamael_female";
						}
					}
					htmltext += ".htm";
				}
				else
				{
					htmltext = "You already taken this quest";
				}
				break;
			case LAKCIS:
				if (cond == 1)
				{
					htmltext = "1-1.htm";
				}
				break;
			case SEBION:
				if (cond == 2)
				{
					htmltext = "2-1.htm";
				}
				else if (cond == 3)
				{
					htmltext = "2-4.htm";
				}
				else if ((cond == 4) && qs.haveQuestItem(SARIL_NECKLACE))
				{
					qs.setCond(5);
					htmltext = "2-5.htm";
				}
				break;
			case PANTHEON:
				if ((qs.getCond() == 5) && qs.haveQuestItem(SARIL_NECKLACE))
				{
					htmltext = "5-1.htm";
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = player.getQuestState(_10331_StartofFate.class).getCond();
		Reflection reflection = player.getActiveReflection();
		LabyrinthOfBelis lob = (LabyrinthOfBelis) reflection;
		int marksRequiered = ((LabyrinthOfBelis) reflection).getMarksRequieredCount();
		switch (npcId)
		{
			case OFFICER:
				htmltext = "";
				if (cond == 3)
				{
					if (lob.getInstanceCond() == 0)
					{
						htmltext = "3-1.htm";
					}
					else if (lob.getInstanceCond() == 2)
					{
						htmltext = "3-2.htm";
					}
					else if (lob.getInstanceCond() == 4)
					{
						htmltext = "3-3.htm";
					}
					else if (lob.getInstanceCond() == 13)
					{
						htmltext = "3-4.htm";
					}
				}
				else if ((cond == 4) && (lob.getInstanceCond() == 14))
				{
					htmltext = "3-5.htm";
				}
				break;
			case VERIFICATOR:
				if (cond == 3)
				{
					if (marksRequiered == 1)
					{
						htmltext = "4-7.htm";
					}
					else if (marksRequiered == 2)
					{
						htmltext = "4-3.htm";
					}
					else if (marksRequiered <= 0)
					{
						htmltext = "4-5.htm";
					}
					else
					{
						htmltext = "4-1.htm";
					}
				}
				break;
		}
		return htmltext;
	}
	
	private void enterInstance(Player player)
	{
		Reflection reflection = player.getActiveReflection();
		if (reflection != null)
		{
			if (player.canReenterInstance(LABYRINTH_OF_BELIS))
			{
				player.teleToLocation(reflection.getTeleportLoc(), reflection);
			}
		}
		else if (player.canEnterInstance(LABYRINTH_OF_BELIS))
		{
			ReflectionUtils.enterReflection(player, new LabyrinthOfBelis(player), LABYRINTH_OF_BELIS);
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
