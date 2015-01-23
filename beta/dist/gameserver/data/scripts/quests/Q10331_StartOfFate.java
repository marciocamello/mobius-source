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

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class Q10331_StartOfFate extends Quest implements ScriptFile
{
	private static final int INFILTRATION_OFFICER = 19155;
	private static final int VALFAR = 32146;
	private static final int RIVIAN = 32147;
	private static final int TOOK = 32150;
	private static final int FRANCO = 32153;
	private static final int MOKA = 32157;
	private static final int DEVON = 32160;
	private static final int PANTHEON = 32972;
	private static final int LAKCIS = 32977;
	private static final int SEBION = 32978;
	private static final int BELIS_VERIFICATION_SYSTEM = 33215;
	private static final int ELECTRICITY_GENERATOR = 33216;
	private static final int NEMERTESS = 22984;
	private static final int HANDYMAN = 22997;
	private static final int OPERATIVE = 22998;
	private static final int SARILS_NECKLACE = 17580;
	private static final int BELIS_MARK = 17615;
	private static final int PROOF_OF_COURAGE = 17821;
	private static final Location ESAGIRA_5_AREA = new Location(-111774, 231933, -3160);
	private static final int INSTANCED_ZONE_ID = 178;
	private static final int NEED_BELIS_MARKS_COUNT = 3;
	private static final int NEED_OPERATIVES_KILLS_COUNT = 6;
	private static final int NEED_DEFENDERS_KILLS_COUNT = 3;
	private static final int BELISE_MARK_DROP_CHANCE = 45;
	private static final int PROOF_OF_COURAGE_MULTISELL_ID = 717;
	
	public Q10331_StartOfFate()
	{
		super(false);
		addStartNpc(FRANCO, RIVIAN, DEVON, TOOK, MOKA, VALFAR);
		addTalkId(FRANCO, RIVIAN, DEVON, TOOK, MOKA, VALFAR, LAKCIS, SEBION, INFILTRATION_OFFICER, PANTHEON);
		addFirstTalkId(INFILTRATION_OFFICER, BELIS_VERIFICATION_SYSTEM, ELECTRICITY_GENERATOR);
		addKillId(HANDYMAN, OPERATIVE, NEMERTESS);
		addQuestItem(BELIS_MARK, SARILS_NECKLACE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if ((npc != null) && event.equals(getStartNpcPrefix(npc.getId()) + "_q10331_3.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("lakcis_q10331_2.htm"))
		{
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("teleport_to_5_area"))
		{
			qs.getPlayer().teleToLocation(ESAGIRA_5_AREA);
			return null;
		}
		else if (event.equals("sebion_q10331_3.htm"))
		{
			qs.setCond(3);
			qs.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("enter_to_labyrinth"))
		{
			enterInstance(qs, INSTANCED_ZONE_ID);
			return null;
		}
		else if (event.equals("start_stage_1"))
		{
			Player p = qs.getPlayer();
			Reflection reflect = p.getReflection();
			NpcInstance officer = getNpcFromReflection(INFILTRATION_OFFICER, reflect);
			
			if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
			{
				qs.set("stage", 1);
				reflect.openDoor(16240002);
				officer.setRunning();
				officer.setFollowTarget(qs.getPlayer());
				officer.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, qs.getPlayer(), 150);
			}
			
			return null;
		}
		else if (event.equals("start_stage_3"))
		{
			Player p = qs.getPlayer();
			Reflection reflect = p.getReflection();
			NpcInstance officer = getNpcFromReflection(INFILTRATION_OFFICER, reflect);
			
			if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
			{
				qs.set("stage", 3);
				reflect.openDoor(16240004);
				officer.setRunning();
				officer.setFollowTarget(qs.getPlayer());
				officer.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, qs.getPlayer(), 150);
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcStringId.MARK_OF_BELIS_CAN_BE_ACQUIRED_FROM_ENEMIES_NUSE_THEM_IN_THE_BELIS_VERIFICATION_SYSTEM, 7000, ScreenMessageAlign.TOP_CENTER));
				qs.startQuestTimer("belise_mark_msg_timer", 10000);
			}
			
			return null;
		}
		else if (event.equals("drop_belise_mark") && (npc != null))
		{
			npc.dropItem(qs.getPlayer(), BELIS_MARK, 1);
			return null;
		}
		else if (event.equals("belise_mark_msg_timer"))
		{
			Player player = qs.getPlayer();
			Reflection reflection = player.getActiveReflection();
			
			if ((reflection != null) && (reflection.getInstancedZoneId() == INSTANCED_ZONE_ID) && (qs.getInt("stage") == 3))
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.MARK_OF_BELIS_CAN_BE_ACQUIRED_FROM_ENEMIES_NUSE_THEM_IN_THE_BELIS_VERIFICATION_SYSTEM, 7000, ScreenMessageAlign.TOP_CENTER));
				qs.startQuestTimer("belise_mark_msg_timer", 10000);
			}
			
			return null;
		}
		else if (event.equals("use_belise_mark"))
		{
			if (qs.getInt("stage") == 3)
			{
				if (qs.takeItems(BELIS_MARK, 1) == 1)
				{
					int marksLeft = NEED_BELIS_MARKS_COUNT - qs.getInt("belise_marks_left") - 1;
					
					if (marksLeft > 0)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10331_StartOfFate/belis_verification_system_q10331_2.htm", qs.getPlayer());
						htmltext = htmltext.replace("<?BELISE_MARKS_LEFT?>", String.valueOf(marksLeft));
					}
					else
					{
						Player player = qs.getPlayer();
						Reflection reflect = player.getActiveReflection();
						
						if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
						{
							qs.set("stage", 4);
							reflect.openDoor(16240005);
							NpcInstance officer = getNpcFromReflection(INFILTRATION_OFFICER, reflect);
							
							if (officer != null)
							{
								officerMoveToLocation(officer, new Location(-117896, 214248, -8617, 49151));
							}
						}
						
						htmltext = "belis_verification_system_q10331_3.htm";
					}
					
					qs.set("belise_marks_left", NEED_BELIS_MARKS_COUNT - marksLeft);
				}
				else
				{
					htmltext = "belis_verification_system_q10331_no.htm";
				}
			}
			else
			{
				htmltext = "belis_verification_system_q10331_4.htm";
			}
		}
		else if (event.equals("start_stage_5"))
		{
			Player p = qs.getPlayer();
			Reflection reflect = p.getReflection();
			NpcInstance officer = getNpcFromReflection(INFILTRATION_OFFICER, reflect);
			
			if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
			{
				qs.set("stage", 5);
				reflect.openDoor(16240006);
				officer.setRunning();
				NpcInstance generator = getNpcFromReflection(ELECTRICITY_GENERATOR, reflect);
				
				if (generator != null)
				{
					generator.setNpcState(1);
					officer.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, generator, 1000);
					Functions.npcSay(officer, NpcStringId.DON_T_COME_BACK_HERE);
					qs.startQuestTimer("stage_5_phrases_timer", 5000, officer);
					qs.startQuestTimer("stage_5_spawn_timer", 5000, officer);
				}
			}
			
			return null;
		}
		else if (event.equals("stage_5_phrases_timer"))
		{
			if (qs.getInt("stage") == 5)
			{
				Functions.npcSay(npc, NpcStringId.DON_T_COME_BACK_HERE);
				Player player = qs.getPlayer();
				Reflection reflection = player.getActiveReflection();
				
				if ((reflection != null) && (reflection.getInstancedZoneId() == INSTANCED_ZONE_ID))
				{
					NpcStringId screenMsg = NpcStringId.BEHIND_YOU_THE_ENEMY_IS_AMBUSHING_YOU;
					
					if (Rnd.chance(50))
					{
						screenMsg = NpcStringId.IF_TERAIN_DIES_THE_MISSION_WILL_FAIL;
					}
					
					player.sendPacket(new ExShowScreenMessage(screenMsg, 7000, ScreenMessageAlign.TOP_CENTER, true, true));
				}
				
				qs.startQuestTimer("stage_5_phrases_timer", 10000, npc);
			}
			
			return null;
		}
		else if (event.equals("stage_5_spawn_timer"))
		{
			if (qs.getInt("stage") == 5)
			{
				int defendersCount = qs.getInt("spawned_defenders");
				
				if ((defendersCount < NEED_DEFENDERS_KILLS_COUNT) && (npc != null))
				{
					int defenderNpcId = ((defendersCount == 0) || ((defendersCount % 2) == 0)) ? HANDYMAN : OPERATIVE;
					Reflection reflect = npc.getReflection();
					NpcInstance defender = addSpawnToInstance(defenderNpcId, new Location(-116600, 213080, -8615, 21220), 0, reflect.getId());
					/*
					 * NpcStringId defenderPhrase = NpcStringId.FOCUS_ON_ATTACKING_THE_GUY_IN_THE_ROOM; if(Rnd.chance(50)) defenderPhrase = NpcStringId.KILL_THE_GUY_MESSING_WITH_THE_ELECTRIC_DEVICE; Functions.npcSay(defender, defenderPhrase);
					 */
					NpcInstance officer = getNpcFromReflection(INFILTRATION_OFFICER, reflect);
					
					if (officer != null)
					{
						defender.setSpawnedLoc(officer.getLoc());
						defender.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, officer, 10);
					}
					
					qs.set("spawned_defenders", defendersCount + 1);
					String defendersIds = qs.get("defenders_ids");
					
					if (defendersIds == null)
					{
						defendersIds = "";
					}
					
					qs.set("defenders_ids", qs.get("defenders_ids") + "-" + defender.getObjectId() + "-");
				}
				
				qs.startQuestTimer("stage_5_spawn_timer", 20000, npc);
			}
			
			return null;
		}
		else if (event.equals("process_stage_5"))
		{
			Player player = qs.getPlayer();
			Reflection reflect = player.getActiveReflection();
			
			if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
			{
				int defenderKills = qs.getInt("defender_kills");
				
				if (defenderKills >= NEED_DEFENDERS_KILLS_COUNT)
				{
					qs.set("stage", 6);
					qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcStringId.ELECTRONIC_DEVICE_HAS_BEEN_DESTROYED, 5000, ScreenMessageAlign.TOP_CENTER));
					reflect.openDoor(16240007);
					reflect.getZone("[belise_labyrinth_03_1]").setActive(true);
					reflect.getZone("[belise_labyrinth_03_2]").setActive(false);
					NpcInstance officer = getNpcFromReflection(INFILTRATION_OFFICER, reflect);
					
					if (officer != null)
					{
						officerMoveToLocation(officer, new Location(-119112, 213672, -8617, 8191));
					}
					
					NpcInstance generator = getNpcFromReflection(ELECTRICITY_GENERATOR, reflect);
					
					if (generator != null)
					{
						generator.deleteMe();
					}
				}
			}
			
			return null;
		}
		else if (event.equals("start_stage_7"))
		{
			Player player = qs.getPlayer();
			Reflection reflect = player.getActiveReflection();
			
			if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
			{
				qs.set("stage", 7);
				reflect.openDoor(16240008);
				SceneMovie scene = SceneMovie.sc_talking_island_boss_opening;
				qs.getPlayer().showQuestMovie(scene);
				qs.startQuestTimer("spawn_nemertess", scene.getDuration(), npc);
			}
			
			return null;
		}
		else if (event.equals("spawn_nemertess"))
		{
			if (qs.getInt("stage") == 7)
			{
				Player player = qs.getPlayer();
				Reflection reflect = player.getActiveReflection();
				
				if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
				{
					addSpawnToInstance(NEMERTESS, new Location(-118328, 212968, -8705, 24575), 0, reflect.getId());
				}
			}
			
			return null;
		}
		else if (event.equals("kill_nemertess"))
		{
			if (qs.getInt("stage") == 7)
			{
				Player player = qs.getPlayer();
				Reflection reflect = player.getActiveReflection();
				
				if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
				{
					clearInstanceVariables(qs);
					qs.setCond(4);
					qs.giveItems(SARILS_NECKLACE, 1);
					NpcInstance officer = getNpcFromReflection(INFILTRATION_OFFICER, reflect);
					
					if (officer != null)
					{
						officerMoveToLocation(officer, new Location(-118328, 212968, -8705, 24575));
					}
				}
			}
			
			return null;
		}
		else if (event.equals("pantheon_q10331_2.htm"))
		{
			int cond = qs.getPlayer().getRace().ordinal() + 6;
			qs.setCond(cond);
		}
		else if (event.startsWith("class_transfer") && (npc != null))
		{
			String[] params = event.split(" ");
			
			if (params.length < 3)
			{
				return null;
			}
			
			int classId = Integer.parseInt(params[1]);
			String html = params[2];
			htmltext = HtmCache.getInstance().getNotNull("quests/Q10331_StartOfFate/" + html, qs.getPlayer());
			htmltext = htmltext.replace("<?CLASS_NAME?>", HtmlUtils.htmlClassName(classId));
			Player player = qs.getPlayer();
			player.setClassId(classId, false, false);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CONGRATULATIONS_YOU_VE_COMPLETED_A_CLASS_TRANSFER));
			player.broadcastPacket(new MagicSkillUse(player, player, 5103, 1, 1000, 0));
			MultiSellHolder.getInstance().SeparateAndSend(PROOF_OF_COURAGE_MULTISELL_ID, qs.getPlayer(), 0, npc.getId());
			qs.showTutorialHTML(TutorialShowHtml.QT_009, TutorialShowHtml.TYPE_WINDOW);
			qs.giveItems(ADENA_ID, 789);
			qs.giveItems(PROOF_OF_COURAGE, 40);
			qs.addExpAndSp(200000, 4000);
			qs.setState(COMPLETED);
			qs.exitCurrentQuest(false);
			qs.playSound(SOUND_FANFARE2);
			// st.playSound(SOUND_FINISH);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case FRANCO:
			case RIVIAN:
			case DEVON:
			case TOOK:
			case MOKA:
			case VALFAR:
				String prefix = getStartNpcPrefix(npcId);
				if (cond >= 6)
				{
					htmltext = getAvailableClassList(qs.getPlayer(), prefix + "_q10331_5.htm", prefix + "_q10331_6.htm");
				}
				else if (checkSC(qs.getPlayer()))
				{
					htmltext = prefix + "_q10331_1.htm";
				}
				else if (!checkSC(qs.getPlayer()))
				{
					htmltext = prefix + "_q10331_0.htm";
				}
				else if (qs.getPlayer().getLevel() >= 18)
				{
					htmltext = prefix + "_q10331_3.htm";
				}
				else if (cond == 1)
				{
					htmltext = prefix + "_q10331_4.htm";
				}
				break;
			
			case LAKCIS:
				if (cond == 1)
				{
					htmltext = "lakcis_q10331_1.htm";
				}
				else if (cond == 2)
				{
					htmltext = "lakcis_q10331_3.htm";
				}
				break;
			
			case SEBION:
				if (cond == 2)
				{
					htmltext = "sebion_q10331_1.htm";
				}
				else if (cond == 3)
				{
					htmltext = "sebion_q10331_4.htm";
				}
				else if (cond == 4)
				{
					htmltext = "sebion_q10331_5.htm";
					qs.setCond(5);
				}
				else if (cond == 5)
				{
					htmltext = "sebion_q10331_6.htm";
				}
				break;
			
			case PANTHEON:
				if (cond == 5)
				{
					htmltext = "pantheon_q10331_1.htm";
				}
				else if (cond >= 6)
				{
					htmltext = "pantheon_q10331_3.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(getClass());
		
		if (qs == null)
		{
			return "";
		}
		
		final Reflection reflect = npc.getReflection();
		
		if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
		{
			final int npcId = npc.getId();
			
			if (npcId == INFILTRATION_OFFICER)
			{
				final int cond = qs.getCond();
				
				if (cond == 3)
				{
					if (npc.isMoving || npc.isFollow || (npc.getAI().getIntention() != CtrlIntention.AI_INTENTION_ACTIVE))
					{
						return "infiltration_officer_q10331_no.htm";
					}
					
					final int stage = qs.getInt("stage");
					player.sendMessage("npc:" + npc.getId());
					
					if (stage == 0)
					{
						return "infiltration_officer_q10331_1.htm";
					}
					else if (stage == 2)
					{
						return "infiltration_officer_q10331_2.htm";
					}
					else if (stage == 4)
					{
						return "infiltration_officer_q10331_3.htm";
					}
					else if (stage == 6)
					{
						return "infiltration_officer_q10331_4.htm";
					}
					
					return "infiltration_officer_q10331_no.htm";
				}
				else if (cond == 4)
				{
					if (npc.isMoving || npc.isFollow || (npc.getAI().getIntention() != CtrlIntention.AI_INTENTION_ACTIVE))
					{
						return "infiltration_officer_q10331_no.htm";
					}
					
					return "infiltration_officer_q10331_5.htm";
				}
			}
			else if (npcId == BELIS_VERIFICATION_SYSTEM)
			{
				int cond = qs.getCond();
				
				if ((cond == 3) || (cond == 4))
				{
					String htmltext = HtmCache.getInstance().getNotNull("quests/Q10331_StartOfFate/belis_verification_system_q10331_1.htm", qs.getPlayer());
					htmltext = htmltext.replace("<?BELISE_MARK_COUNT?>", String.valueOf(NEED_BELIS_MARKS_COUNT));
					return htmltext;
				}
			}
			else if (npcId == ELECTRICITY_GENERATOR)
			{
				int cond = qs.getCond();
				
				if ((cond == 3) || (cond == 4))
				{
					return "electricity_generator_q10331_1.htm";
				}
			}
		}
		
		return null;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Reflection reflect = npc.getReflection();
		
		if (reflect.getInstancedZoneId() == INSTANCED_ZONE_ID)
		{
			switch (npc.getId())
			{
				case OPERATIVE:
					if (qs.getCond() == 3)
					{
						if (qs.getInt("stage") == 1)
						{
							int killsCount = qs.getInt("operative_kills") + 1;
							
							if (killsCount >= NEED_OPERATIVES_KILLS_COUNT)
							{
								qs.set("stage", 2);
								reflect.openDoor(16240003);
								NpcInstance officer = getNpcFromReflection(INFILTRATION_OFFICER, reflect);
								
								if (officer != null)
								{
									officerMoveToLocation(officer, new Location(-117037, 212504, -8592, 39479));
								}
							}
							else
							{
								qs.set("operative_kills", killsCount);
							}
						}
						else if (qs.getInt("stage") == 5)
						{
							qs.set("defender_kills", qs.getInt("defender_kills") + 1);
							qs.startQuestTimer("process_stage_5", 3000, npc);
						}
					}
					break;
				
				case HANDYMAN:
					if (qs.getCond() == 3)
					{
						if (qs.getInt("stage") == 3)
						{
							if (Rnd.chance(BELISE_MARK_DROP_CHANCE))
							{
								qs.startQuestTimer("drop_belise_mark", 3000, npc);
							}
						}
						else if (qs.getInt("stage") == 5)
						{
							String defendersIds = qs.get("defenders_ids");
							
							if ((defendersIds != null) && defendersIds.contains("-" + npc.getObjectId() + "-"))
							{
								qs.set("defender_kills", qs.getInt("defender_kills") + 1);
								qs.startQuestTimer("process_stage_5", 3000, npc);
							}
						}
					}
					break;
				
				case NEMERTESS:
					if (qs.getCond() == 3)
					{
						if (qs.getInt("stage") == 7)
						{
							npc.deleteMe();
							SceneMovie scene = SceneMovie.sc_talking_island_boss_ending;
							qs.getPlayer().showQuestMovie(scene);
							qs.startQuestTimer("kill_nemertess", scene.getDuration(), npc);
						}
					}
					break;
			}
		}
		
		return null;
	}
	
	@Override
	public void onEnterInstance(QuestState qs, Reflection reflection)
	{
		clearInstanceVariables(qs);
	}
	
	public boolean checkSC(Player player)
	{
		return (player.getLevel() >= 18) && (player.getClassLevel() <= 1);
	}
	
	public boolean checkStartNpc(NpcInstance npc, Player player)
	{
		final Race race = player.getRace();
		
		switch (npc.getId())
		{
			case FRANCO:
				if (race == Race.human)
				{
					return true;
				}
				return false;
				
			case RIVIAN:
				if (race == Race.elf)
				{
					return true;
				}
				return false;
				
			case DEVON:
				if (race == Race.darkelf)
				{
					return true;
				}
				return false;
				
			case TOOK:
				if (race == Race.orc)
				{
					return true;
				}
				return false;
				
			case MOKA:
				if (race == Race.dwarf)
				{
					return true;
				}
				return false;
				
			case VALFAR:
				if (race == Race.kamael)
				{
					return true;
				}
				return false;
		}
		
		return true;
	}
	
	public boolean checkTalkNpc(NpcInstance npc, QuestState qs)
	{
		return checkStartNpc(npc, qs.getPlayer());
	}
	
	private static String getStartNpcPrefix(int npcId)
	{
		String prefix = "high_priest_franco";
		
		switch (npcId)
		{
			case RIVIAN:
				prefix = "grand_master_rivian";
				break;
			
			case DEVON:
				prefix = "grand_magister_devon";
				break;
			
			case TOOK:
				prefix = "high_prefect_took";
				break;
			
			case MOKA:
				prefix = "head_blacksmith_moka";
				break;
			
			case VALFAR:
				prefix = "grand_master_valfar";
				break;
		}
		
		return prefix;
	}
	
	private static void clearInstanceVariables(QuestState qs)
	{
		qs.unset("belise_marks_left");
		qs.unset("operative_kills");
		qs.unset("stage");
		qs.unset("defender_kills");
		qs.unset("spawned_defenders");
		qs.unset("defenders_ids");
	}
	
	private static NpcInstance getNpcFromReflection(int npcId, Reflection reflect)
	{
		List<NpcInstance> npc = reflect.getAllByNpcId(npcId, true);
		
		if (!npc.isEmpty())
		{
			return npc.get(0);
		}
		
		return null;
	}
	
	private static void officerMoveToLocation(NpcInstance officer, Location loc)
	{
		officer.abortAttack(true, true);
		officer.abortCast(true, true);
		officer.setTarget(null);
		officer.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		officer.setRunning();
		officer.setSpawnedLoc(loc);
		
		if (!officer.moveToLocation(loc, 0, true))
		{
			officer.getAI().clientStopMoving();
			officer.teleToLocation(loc);
		}
		
		officer.setHeading(loc.getHeading());
	}
	
	private static String getAvailableClassList(Player player, String html, String html2)
	{
		String htmltext = HtmCache.getInstance().getNotNull("quests/Q10331_StartOfFate/" + html, player);
		ClassId classId = player.getClassId();
		StringBuilder classList = new StringBuilder();
		
		for (ClassId firstClassId : ClassId.VALUES)
		{
			if (!firstClassId.isOfLevel(ClassLevel.First))
			{
				continue;
			}
			
			if (!firstClassId.childOf(classId))
			{
				continue;
			}
			
			int firstClsId = firstClassId.getId();
			classList.append("<a action=\"bypass -h Quest Q10331_StartOfFate class_transfer " + firstClsId + " " + html2 + "\">" + HtmlUtils.htmlClassName(firstClsId) + "</a><br>");
		}
		
		htmltext = htmltext.replace("<?AVAILABLE_CLASS_LIST?>", classList.toString());
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