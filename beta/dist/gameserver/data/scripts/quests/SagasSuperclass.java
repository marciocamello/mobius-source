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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;
import ai.NpcArcherAI;
import ai.NpcHealerAI;
import ai.NpcMageAI;
import ai.NpcWarriorAI;

public abstract class SagasSuperclass extends Quest
{
	SagasSuperclass(boolean party)
	{
		super(party);
	}
	
	protected int StartNPC = 0;
	protected Race StartRace;
	// massives
	static final List<NpcInstance> _npcWaves = new ArrayList<>();
	private static final int Vanguard_aden = 33407;
	private static final int Vanguard_corpse1 = 33166;
	private static final int Vanguard_corpse2 = 33167;
	private static final int Vanguard_corpse3 = 33168;
	private static final int Vanguard_corpse4 = 33169;
	private static final int Vanguard_member = 33165;
	// instance npc
	private static final int Vanguard_camptain = 33170;
	private static final int Vanguard_Ellis = 33171;
	private static final int Vanguard_Barton = 33172;
	private static final int Vanguard_Xaok = 33173;
	private static final int Vanguard_Ellia = 33174;
	// npc helpers
	private static final int Van_Archer = 33414;
	private static final int Van_Infantry = 33415;
	// monsters
	private static final int Shaman = 27430;
	private static final int Slayer = 27431;
	private static final int Pursuer = 27432;
	private static final int Priest_Darkness = 27433;
	private static final int Guard_Darkness = 27434;
	// boss
	private static final int Death_wound = 27425;
	// items
	private static final int DeadSoldierOrbs = 17748;
	private static final int Ring_Shout = 17484;
	// onKill won't work here because mobs also killing mobs
	private final DeathListener deathListener = new DeathListener();
	private static final Map<Integer, Class<?>> Quests = new HashMap<>();
	static
	{
		Quests.put(10341, Q10341_DayOfDestinyHumansFate.class);
		Quests.put(10342, Q10342_DayOfDestinyElvenFate.class);
		Quests.put(10343, Q10343_DayOfDestinyDarkElfsFate.class);
		Quests.put(10344, Q10344_DayOfDestinyOrcsFate.class);
		Quests.put(10345, Q10345_DayOfDestinyDwarfsFate.class);
		Quests.put(10346, Q10346_DayOfDestinyKamaelsFate.class);
	}
	protected static final int[][] QuestRace = new int[][]
	{
		{
			0
		},
		{
			1
		},
		{
			2
		},
		{
			3
		},
		{
			4
		},
		{
			5
		}
	};
	
	protected void init()
	{
		addStartNpc(StartNPC);
		addTalkId(StartNPC, Vanguard_aden, Vanguard_corpse1, Vanguard_corpse2, Vanguard_corpse3, Vanguard_corpse4, Vanguard_member, Vanguard_camptain, Vanguard_Ellis, Vanguard_Barton, Vanguard_Xaok, Vanguard_Ellia);
		addQuestItem(DeadSoldierOrbs, Ring_Shout);
		addLevelCheck(76, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		if (event.equals(StartNPC + "-5.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("33407-1.htm"))
		{
			qs.setCond(2);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("33407-4.htm"))
		{
			qs.takeItems(DeadSoldierOrbs, -1);
			qs.setCond(4);
		}
		else if (event.equals("33166-1.htm"))
		{
			if (player.getVar("orb1") != null)
			{
				return "33166-got.htm";
			}
			
			player.setVar("orb1", "1", -1);
			qs.giveItems(DeadSoldierOrbs, 1);
			qs.playSound(SOUND_MIDDLE);
			checkOrbs(player, qs);
		}
		else if (event.equals("33167-1.htm"))
		{
			if (player.getVar("orb2") != null)
			{
				return "33167-got.htm";
			}
			
			player.setVar("orb2", "1", -1);
			qs.giveItems(DeadSoldierOrbs, 1);
			qs.playSound(SOUND_MIDDLE);
			checkOrbs(player, qs);
		}
		else if (event.equals("33168-1.htm"))
		{
			if (player.getVar("orb3") != null)
			{
				return "33168-got.htm";
			}
			
			player.setVar("orb3", "1", -1);
			qs.giveItems(DeadSoldierOrbs, 1);
			qs.playSound(SOUND_MIDDLE);
			checkOrbs(player, qs);
		}
		else if (event.equals("33169-1.htm"))
		{
			if (player.getVar("orb4") != null)
			{
				return "33168-got.htm";
			}
			
			player.setVar("orb4", "1", -1);
			qs.giveItems(DeadSoldierOrbs, 1);
			qs.playSound(SOUND_MIDDLE);
			checkOrbs(player, qs);
		}
		else if (event.equals("33170-2.htm"))
		{
			qs.setCond(6);
			qs.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("33170-6.htm"))
		{
			qs.setCond(10);
			
			if (qs.getQuestItemsCount(Ring_Shout) == 0)
			{
				qs.giveItems(Ring_Shout, 1); // ring
			}
			
			Functions.npcSay(npc, NpcString.THE_CRY_OF_FATE_PENDANT_WILL_BE_HELPFUL_TO_YOU_PLEASE_EQUIP_IT_AND_BRING_OUT_THE_POWER_OF_THE_PENDANT_TO_PREPARE_FOR_THE_NEXT_FIGHT);
		}
		else if (event.equals("selection"))
		{
			if (player.getVar("sel1") == null)
			{
				player.setVar("sel1", npc.getId(), -1);
				npc.deleteMe();
				return null;
			}
			
			if (player.getVar("sel2") == null)
			{
				player.setVar("sel2", npc.getId(), -1);
				npc.deleteMe();
				qs.setCond(7);
				return null;
			}
		}
		else if (event.equals("enterinstance"))
		{
			if (!_npcWaves.isEmpty())
			{
				_npcWaves.clear();
			}
			
			player.unsetVar("wave");
			player.unsetVar("sel1");
			player.unsetVar("sel2");
			// maybe take some other quest items?
			qs.setCond(5);
			enterInstance(qs, 185);
			return null;
		}
		else if (event.equals("battleField"))
		{
			// missing parts of the instance:
			// init npcs
			initFriendNpc(player);
			// init waves
			qs.startQuestTimer("wave1", 2000);
			player.teleToLocation(56168, -175576, -7974, player.getReflection().getId());
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			qs.setCond(8);
			return null;
		}
		else if (event.equals("wave1"))
		{
			initWave1(player);
			return null;
		}
		else if (event.equals("2"))
		{
			initWave2(player);
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			return null;
		}
		else if (event.equals("3"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave3(player);
			return null;
		}
		else if (event.equals("4"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave4(player);
			return null;
		}
		else if (event.equals("5"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave5(player);
			return null;
		}
		else if (event.equals("6"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave6(player);
			return null;
		}
		else if (event.equals("8"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave8(player);
			qs.startQuestTimer("9", 30000);
			return null;
		}
		else if (event.equals("9"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave9(player);
			qs.startQuestTimer("10", 30000);
			return null;
		}
		else if (event.equals("10"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave10(player);
			qs.startQuestTimer("11", 30000);
			return null;
		}
		else if (event.equals("11"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave11(player);
			qs.startQuestTimer("12", 30000);
			return null;
		}
		else if (event.equals("12"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave12(player);
			// boss is comming after we killed all the waves.
			player.unsetVar("wave");
			player.setVar("wave", 12, -1);
			return null;
		}
		else if (event.equals("13"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.I_DEATH_WOUND_CHAMPION_OF_SHILEN_SHALL_END_YOUR_WORLD, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave13(player);
			// boss is comming after we killed all the waves.
			player.unsetVar("wave");
			player.setVar("wave", 13, -1);
			return null;
		}
		else if (event.equals("firstStandCompleted"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_HAVE_STOPPED_THEIR_ATTACK_REST_AND_THEN_SPEAK_WITH_ADOLPH, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			qs.setCond(9);
			return null;
		}
		else if (event.equals("engagesecondstand"))
		{
			// init second stand
			// init waves
			qs.startQuestTimer("8", 30000);
			qs.setCond(11);
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave7(player);
			return null;
		}
		else if (event.equals("secondStandCompleted"))
		{
			player.unsetVar("wave");
			qs.setCond(12);
			return null;
		}
		else if (event.startsWith("giveme"))
		{
			if (event.equals("givemered"))
			{
				qs.giveItems(9570, 1);
			}
			else if (event.equals("givemeblue"))
			{
				qs.giveItems(9571, 1);
			}
			else if (event.equals("givemegreen"))
			{
				qs.giveItems(9572, 1);
			}
			
			int _reqClass = -1;
			
			for (ClassId cid : ClassId.VALUES)
			{
				if (cid.childOf(player.getClassId()) && (cid.getClassLevel().ordinal() == (player.getClassId().getClassLevel().ordinal() + 1)))
				{
					_reqClass = cid.getId();
				}
			}
			
			if (_reqClass == -1)
			{
				player.sendMessage("Something gone wrong, please contact administrator!");
			}
			
			player.setClassId(_reqClass, false, false);
			player.broadcastPacket(new MagicSkillUse(player, player, 5103, 1, 1000, 0));
			qs.giveItems(ADENA_ID, 5000000);
			qs.addExpAndSp(2050000, 0);
			qs.giveItems(9627, 1);
			qs.takeItems(DeadSoldierOrbs, -1);
			qs.setState(COMPLETED);
			qs.exitCurrentQuest(false);
			qs.playSound(SOUND_FINISH);
			player.broadcastUserInfo();
			player.sendPacket(new ExShowScreenMessage(NpcString.CONGRATULATIONS_YOU_WILL_NOW_GRADUATE_FROM_THE_CLAN_ACADEMY_AND_LEAVE_YOUR_CURRENT_CLAN_AS_A_GRADUATE_OF_THE_ACADEMY_YOU_CAN_IMMEDIATELY_JOIN_A_CLAN_AS_A_REGULAR_MEMBER_WITHOUT_BEING_SUBJECT_TO_ANY_PENALTIES, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			return StartNPC + "-7.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int wave = Integer.parseInt(player.getVar("wave"));
		
		if (npc.getId() == Death_wound)
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.AGH_HUMANS_HA_IT_DOES_NOT_MATTER_YOUR_WORLD_WILL_END_ANYWAYS, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			qs.startQuestTimer("secondStandCompleted", 1000);
			return null;
		}
		
		if (checkWave(player, npc, wave, qs))
		{
			return null;
		}
		
		return null;
	}
	
	private void initWave13(Player player)
	{
		// _npcWaves
		player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55976, -177544, -7980, 16383), 0);
		player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(55864, -177544, -8320, 16383), 0);
		player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55768, -177544, -8320, 16383), 0);
		player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56376, -177544, -8320, 16383), 0);
		player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56336, -177544, -8320, 16383), 0);
		NpcInstance boss = player.getReflection().addSpawnWithoutRespawn(Death_wound, new Location(56168, -177544, -7974, 16383), 0);
		boss.broadcastPacket(new Earthquake(boss.getLoc(), 40, 10));
		boss.addListener(deathListener);
	}
	
	private void initWave12(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56872, -176648, -7975, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56904, -176744, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(56824, -176728, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(56728, -176664, -7974, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56680, -176776, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56664, -176712, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56696, -176632, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
	}
	
	private void initWave11(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55512, -176648, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55512, -176712, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(55576, -176696, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(55544, -176776, -7974, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55432, -176808, -7980, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55432, -176680, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55592, -176632, -7974, 16383), 0);
		NpcInstance npc8 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55640, -176712, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		_npcWaves.add(npc8);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
		npc8.addListener(deathListener);
	}
	
	private void initWave10(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56184, -177672, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56088, -177704, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(56152, -177592, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(56264, -177496, -7978, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56184, -177464, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56104, -177448, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56024, -177448, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
	}
	
	private void initWave9(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56696, -176744, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56712, -176664, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56776, -176808, -7980, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56696, -176808, -7980, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56616, -176728, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56600, -176648, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56584, -176584, -7980, 16383), 0);
		NpcInstance npc8 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56712, -176552, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		_npcWaves.add(npc8);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
		npc8.addListener(deathListener);
	}
	
	private void initWave8(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55432, -176680, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55432, -176744, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55432, -176648, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55496, -176792, -7976, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55464, -176680, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55576, -176584, -7980, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55416, -176776, -7974, 16383), 0);
		NpcInstance npc8 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55368, -176664, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		_npcWaves.add(npc8);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
		npc8.addListener(deathListener);
	}
	
	private void initWave7(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55432, -176680, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55432, -176744, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55432, -176648, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55464, -176680, -7974, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55416, -176776, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55368, -176664, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
	}
	
	private void initWave6(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56840, -176712, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56824, -176648, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56824, -176584, -7980, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56872, -176632, -7974, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56904, -176696, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56904, -176792, -7976, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
	}
	
	private void initWave5(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55448, -176760, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55464, -176664, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55560, -176744, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55512, -176824, -7980, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55448, -176808, -7980, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55400, -176776, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55384, -176696, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
	}
	
	private void initWave4(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56216, -177624, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56088, -177624, -7975, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56168, -177544, -7975, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56296, -177512, -7980, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56376, -177512, -7980, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55944, -177512, -7980, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56296, -177448, -7979, 16383), 0);
		NpcInstance npc8 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55992, -177384, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		_npcWaves.add(npc8);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
		npc8.addListener(deathListener);
	}
	
	private void initWave3(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56808, -176680, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56824, -176792, -7979, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56760, -176712, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56824, -176584, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
	}
	
	private void initWave2(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56808, -176680, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56824, -176792, -7979, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56760, -176712, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56824, -176584, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
	}
	
	private void initWave1(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56168, -177592, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56248, -177576, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56072, -177544, -7977, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56312, -177576, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		player.setVar("wave", 1, -1);
	}
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc())
			{
				NpcInstance npc = (NpcInstance) self;
				
				if (_npcWaves.contains(npc) || (npc.getId() == Death_wound))
				{
					// we need to find our player in this instance, let's search
					for (Player p : npc.getReflection().getPlayers())
					{
						// the only player inside is ours
						if (p == null)
						{
							continue;
						}
						
						QuestState st = findQuest(p);
						onKill(npc, st);
					}
				}
			}
		}
	}
	
	private static boolean checkWave(Player player, NpcInstance npc, int waveId, QuestState st)
	{
		if (_npcWaves.contains(npc))
		{
			_npcWaves.remove(npc);
		}
		
		if (waveId < 7) // after the first stand we go on timers anyway
		{
			if (_npcWaves.isEmpty())
			{
				int _nextWave = waveId + 1;
				player.setVar("wave", _nextWave, -1);
				
				if (_nextWave == 7)
				{
					st.startQuestTimer("firstStandCompleted", 5000);
				}
				else
				{
					st.startQuestTimer("" + _nextWave + "", 7000);
				}
			}
		}
		
		if ((waveId == 12) && _npcWaves.isEmpty())
		{
			st.startQuestTimer("13", 2000);
		}
		
		return true;
	}
	
	private static void checkOrbs(Player player, QuestState qs)
	{
		if (qs.getQuestItemsCount(DeadSoldierOrbs) == 4)
		{
			qs.playSound(SOUND_MIDDLE);
			qs.setCond(3);
			player.unsetVar("orb1");
			player.unsetVar("orb2");
			player.unsetVar("orb3");
			player.unsetVar("orb4");
		}
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		int cond = qs.getCond();
		int id = qs.getState();
		int npcId = npc.getId();
		Player player = qs.getPlayer();
		String htmltext = "noquest";
		
		if (!canTakeQuest(player))
		{
			return StartNPC + ".htm";
		}
		
		if (id == COMPLETED)
		{
			// QUEST COMPLETED BUT PLAYER WANT TO TRICLASS WITH ANOTHER SUBCLASS
			qs.exitCurrentQuest(true);
		}
		
		if (npcId == StartNPC)
		{
			if (cond == 0)
			{
				return StartNPC + "-1.htm";
			}
			else if (cond == 1)
			{
				return StartNPC + "-got.htm";
			}
			else if (cond == 13)
			{
				return StartNPC + "-6.htm";
			}
		}
		else if (npcId == Vanguard_aden)
		{
			if (cond == 1)
			{
				return "33407.htm";
			}
			else if (cond == 2)
			{
				return "33407-2.htm";
			}
			else if (cond == 3)
			{
				return "33407-3.htm";
			}
		}
		else if (npcId == Vanguard_corpse1)
		{
			if (cond == 2)
			{
				return "33166.htm";
			}
		}
		else if (npcId == Vanguard_corpse2)
		{
			if (cond == 2)
			{
				return "33167.htm";
			}
		}
		else if (npcId == Vanguard_corpse3)
		{
			if (cond == 2)
			{
				return "33168.htm";
			}
		}
		else if (npcId == Vanguard_corpse4)
		{
			if (cond == 2)
			{
				return "33169.htm";
			}
		}
		else if (npcId == Vanguard_member)
		{
			if (cond >= 4)
			{
				return "33165.htm";
			}
		}
		else if (npcId == Vanguard_camptain)
		{
			if (cond == 5)
			{
				return "33170-1.htm";
			}
			else if (cond == 7)
			{
				return "33170-3.htm";
			}
			else if (cond == 9)
			{
				return "33170-5.htm";
			}
			else if (cond == 10)
			{
				return "33170-7.htm";
			}
			else if (cond == 12)
			{
				qs.setCond(13);
				qs.giveItems(736, 1); // SOE
				npc.broadcastPacket(new SocialAction(npc.getObjectId(), 3));
				return "33170-8.htm";
			}
		}
		else if (npcId == Vanguard_Ellis)
		{
			if (cond == 6)
			{
				return "33171-1.htm";
			}
		}
		else if (npcId == Vanguard_Barton)
		{
			if (cond == 6)
			{
				return "33172-1.htm";
			}
		}
		else if (npcId == Vanguard_Xaok)
		{
			if (cond == 6)
			{
				return "33173-1.htm";
			}
		}
		else if (npcId == Vanguard_Ellia)
		{
			if (cond == 6)
			{
				return "33174-1.htm";
			}
		}
		
		return htmltext;
	}
	
	private static void initFriendNpc(Player player)
	{
		int npcId1 = Integer.parseInt(player.getVar("sel1")); // first chosen
		int npcId2 = Integer.parseInt(player.getVar("sel2")); // second chosen
		int npcId3 = Vanguard_camptain; // adolf
		int npcId4 = Van_Archer; // 3 archers
		int npcId5 = Van_Infantry; // 3 infantry soldiers
		// spawn npc helpers
		NpcInstance sel1 = player.getReflection().addSpawnWithoutRespawn(npcId1, new Location(55976, -175672, -7980, 49151), 0);
		NpcInstance sel2 = player.getReflection().addSpawnWithoutRespawn(npcId2, new Location(56328, -175672, -7980, 49151), 0);
		NpcInstance adolf = player.getReflection().addSpawnWithoutRespawn(npcId3, new Location(56168, -175576, -7974, 49151), 0);
		// archers
		NpcInstance archer1 = player.getReflection().addSpawnWithoutRespawn(npcId4, new Location(56392, -176232, -7980, 49151), 0);
		NpcInstance archer2 = player.getReflection().addSpawnWithoutRespawn(npcId4, new Location(56184, -176168, -7974, 49151), 0);
		NpcInstance archer3 = player.getReflection().addSpawnWithoutRespawn(npcId4, new Location(55976, -176136, -7980, 49151), 0);
		// infantry
		NpcInstance infantry1 = player.getReflection().addSpawnWithoutRespawn(npcId5, new Location(56168, -176712, -7973, 49151), 0);
		NpcInstance infantry2 = player.getReflection().addSpawnWithoutRespawn(npcId5, new Location(55960, -176696, -7973, 49151), 0);
		NpcInstance infantry3 = player.getReflection().addSpawnWithoutRespawn(npcId5, new Location(56376, -176712, -7973, 49151), 0);
		
		switch (npcId1)
		{
			case Vanguard_Ellis:
				sel1.setAI(new NpcHealerAI(sel1));
				break;
			
			case Vanguard_Barton:
				sel1.setAI(new NpcWarriorAI(sel1));
				break;
			
			case Vanguard_Xaok:
				sel1.setAI(new NpcArcherAI(sel1));
				break;
			
			case Vanguard_Ellia:
				sel1.setAI(new NpcMageAI(sel1));
				break;
			
			default:
				break;
		}
		
		switch (npcId2)
		{
			case Vanguard_Ellis:
				sel2.setAI(new NpcHealerAI(sel2));
				break;
			
			case Vanguard_Barton:
				sel2.setAI(new NpcWarriorAI(sel2));
				break;
			
			case Vanguard_Xaok:
				sel2.setAI(new NpcArcherAI(sel2));
				break;
			
			case Vanguard_Ellia:
				sel2.setAI(new NpcMageAI(sel2));
				break;
			
			default:
				break;
		}
		
		adolf.setAI(new NpcWarriorAI(adolf));
		archer1.setAI(new NpcArcherAI(archer1));
		archer2.setAI(new NpcArcherAI(archer2));
		archer3.setAI(new NpcArcherAI(archer3));
		infantry1.setAI(new NpcWarriorAI(infantry1));
		infantry2.setAI(new NpcWarriorAI(infantry2));
		infantry3.setAI(new NpcWarriorAI(infantry3));
		player.unsetVar("sel1");
		player.unsetVar("sel2");
	}
	
	private boolean canTakeQuest(Player player)
	{
		if (player == null)
		{
			return false;
		}
		
		if (player.getLevel() < 76)
		{
			return false;
		}
		
		if (!player.getClassId().isOfLevel(ClassLevel.Second))
		{
			return false;
		}
		
		if (player.getClassId().getRace() != StartRace)
		{
			return false;
		}
		
		return true;
	}
	
	QuestState findQuest(Player player)
	{
		QuestState st = player.getQuestState(Quests.get(questId()));
		
		if (st != null)
		{
			return st;
		}
		
		return null;
	}
	
	public int questId()
	{
		return 0;
	}
}
