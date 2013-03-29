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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

import org.apache.commons.lang3.ArrayUtils;

public class _10346_DayOfDestinyKamaelsFate extends Quest implements ScriptFile
{
	private static final int Winonin = 32221;
	private static final int Quartermaster = 33407;
	private static final int Vanguardmember = 33165;
	private static final int Adolph = 33170;
	private static final int VanguardCorpse1 = 33168;
	private static final int VanguardCorpse2 = 33166;
	private static final int VanguardCorpse3 = 33167;
	private static final int VanguardCorpse4 = 33169;
	private static final int Alice = 33171;
	private static final int Barton = 33172;
	private static final int Hayuk = 33173;
	private static final int Eliyah = 33174;
	private static TIntObjectHashMap<World> worlds = new TIntObjectHashMap<>();
	private static final int INSTANCE_ID = 185;
	private static final int dogtag = 17749;
	private static final int[] helpers =
	{
		Alice,
		Barton,
		Hayuk,
		Eliyah
	};
	private static final int[][][] SPAWNLIST_MONSTERS_1_WAVE_TYPE_1 =
	{
		{
			{
				27431,
				56205,
				-177550,
				-7944,
				63
			},
			{
				27434,
				56095,
				-177550,
				-7944,
				233
			},
			{
				27434,
				56245,
				-177550,
				-7944,
				255
			},
			{
				27431,
				56125,
				-177550,
				-7944,
				97
			},
			{
				27431,
				56165,
				-177550,
				-7944,
				33
			}
		},
		{
			{
				27431,
				55645,
				-176695,
				-7944,
				15
			},
			{
				27430,
				55645,
				-176765,
				-7944,
				97
			},
			{
				27434,
				55645,
				-176735,
				-7944,
				209
			},
			{
				27434,
				55645,
				-176735,
				-7944,
				47
			}
		},
		{
			{
				27431,
				56590,
				-176744,
				-7944,
				141
			},
			{
				27431,
				56595,
				-176695,
				-7944,
				269
			},
			{
				27430,
				56642,
				-176560,
				-7952,
				263
			},
			{
				27434,
				56023,
				-177087,
				-7952,
				16026
			},
			{
				27434,
				56212,
				-176074,
				-7944,
				37403
			}
		}
	};
	
	public class World
	{
		public int instanceId;
		public int status;
		public Player player;
		public Map<NpcInstance, Boolean> monsters;
		public Map<Integer, NpcInstance> helpers;
	}
	
	public _10346_DayOfDestinyKamaelsFate()
	{
		super(false);
		addStartNpc(Winonin);
		addTalkId(Quartermaster);
		addTalkId(Vanguardmember);
		addTalkId(Adolph);
		addTalkId(VanguardCorpse1);
		addTalkId(VanguardCorpse2);
		addTalkId(VanguardCorpse3);
		addTalkId(VanguardCorpse4);
		addTalkId(Alice);
		addTalkId(Barton);
		addTalkId(Hayuk);
		addTalkId(Eliyah);
		addTalkId(Adolph);
		addKillId(27431);
		addKillId(27434);
		addKillId(27430);
		addLevelCheck(75, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		if (event.equalsIgnoreCase("quest_accept"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-5.htm";
		}
		if (event.equalsIgnoreCase("red"))
		{
			htmltext = "0-7.htm";
			st.getPlayer().addExpAndSp(2050000, 0);
			st.giveItems(33771, 1);
			st.giveItems(57, 5000000);
			st.exitCurrentQuest(false);
			int Class = Util.getThirdClassForId(player.getClassId().getId());
			player.setClassId(Class, false, false);
			st.getPlayer().broadcastPacket(new MagicSkillUse(st.getPlayer(), 4339, 1, 6000, 1));
			st.getPlayer().broadcastPacket(new MagicSkillUse(npc, 4339, 1, 6000, 1));
			st.playSound(SOUND_FINISH);
		}
		if (event.equalsIgnoreCase("blue"))
		{
			htmltext = "0-7.htm";
			st.getPlayer().addExpAndSp(2050000, 0);
			st.giveItems(33772, 1);
			st.giveItems(57, 5000000);
			st.exitCurrentQuest(false);
			int Class = Util.getThirdClassForId(player.getClassId().getId());
			player.setClassId(Class, false, false);
			st.getPlayer().broadcastPacket(new MagicSkillUse(st.getPlayer(), 4339, 1, 6000, 1));
			st.getPlayer().broadcastPacket(new MagicSkillUse(npc, 4339, 1, 6000, 1));
			st.playSound(SOUND_FINISH);
		}
		if (event.equalsIgnoreCase("green"))
		{
			htmltext = "0-7.htm";
			st.getPlayer().addExpAndSp(2050000, 0);
			st.giveItems(33773, 1);
			st.giveItems(57, 5000000);
			st.exitCurrentQuest(false);
			int Class = Util.getThirdClassForId(player.getClassId().getId());
			player.setClassId(Class, false, false);
			st.getPlayer().broadcastPacket(new MagicSkillUse(st.getPlayer(), 4339, 1, 6000, 1));
			st.getPlayer().broadcastPacket(new MagicSkillUse(npc, 4339, 1, 6000, 1));
			st.playSound(SOUND_FINISH);
		}
		if (event.equalsIgnoreCase("corps"))
		{
			htmltext = "1-2.htm";
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		if (event.equalsIgnoreCase("corps1"))
		{
			htmltext = "2-2.htm";
			st.set("Corp1", 1);
			st.giveItems(dogtag, 1, false);
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			if (st.getQuestItemsCount(dogtag) >= 4)
			{
				st.setCond(3);
				htmltext = "2-4.htm";
			}
		}
		if (event.equalsIgnoreCase("corps2"))
		{
			htmltext = "2-2.htm";
			st.set("Corp2", 1);
			st.giveItems(dogtag, 1, false);
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			if (st.getQuestItemsCount(dogtag) >= 4)
			{
				st.setCond(3);
				htmltext = "2-4.htm";
			}
		}
		if (event.equalsIgnoreCase("corps3"))
		{
			htmltext = "2-2.htm";
			st.set("Corp3", 1);
			st.giveItems(dogtag, 1, false);
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			if (st.getQuestItemsCount(dogtag) >= 4)
			{
				st.setCond(3);
				htmltext = "2-4.htm";
			}
		}
		if (event.equalsIgnoreCase("corps4"))
		{
			htmltext = "2-2.htm";
			st.set("Corp4", 1);
			st.giveItems(dogtag, 1, false);
			st.playSound(SOUND_MIDDLE);
			if (st.getQuestItemsCount(dogtag) >= 4)
			{
				st.setCond(3);
				htmltext = "2-4.htm";
			}
		}
		if (event.equalsIgnoreCase("give_dogtags"))
		{
			htmltext = "1-5.htm";
			st.takeAllItems(dogtag);
			st.playSound(SOUND_MIDDLE);
			st.setCond(4);
		}
		if (event.equalsIgnoreCase("enter_instance"))
		{
			enterInstance(st.getPlayer(), 1);
			st.playSound(SOUND_MIDDLE);
			st.setCond(5);
			return null;
		}
		if (event.equalsIgnoreCase("select_team"))
		{
			htmltext = "8-2.htm";
			st.playSound(SOUND_MIDDLE);
			st.setCond(6);
		}
		if (event.equalsIgnoreCase("select_helper"))
		{
			if (st.get("1") == null)
			{
				st.set("1", String.valueOf(npc.getNpcId()));
				npc.deleteMe();
			}
			else if (st.get("2") == null)
			{
				st.set("2", String.valueOf(npc.getNpcId()));
				npc.deleteMe();
			}
			if ((st.get("1") != null) && (st.get("2") != null))
			{
				Reflection reflection = st.getPlayer().getActiveReflection();
				for (NpcInstance mob : reflection.getNpcs())
				{
					if (ArrayUtils.contains(helpers, mob.getNpcId()))
					{
						mob.deleteMe();
					}
				}
				st.setCond(7);
				st.playSound(SOUND_MIDDLE);
			}
			return null;
		}
		if (event.equalsIgnoreCase("start"))
		{
			npc.deleteMe();
			enterInstance(st.getPlayer(), 2);
			st.playSound(SOUND_MIDDLE);
			st.setCond(8);
			return null;
		}
		if (event.equalsIgnoreCase("ring"))
		{
			htmltext = "8-6.htm";
			st.giveItems(Util.getThirdClassForId(st.getPlayer().getActiveClassId()) + 17396, 1);
			st.setCond(10);
			st.playSound("SOUND_MIDDLE");
		}
		if (event.equalsIgnoreCase("stage2"))
		{
			World world = worlds.get(npc.getReflectionId());
			st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURRECTED_DEFEND_YOURSELF, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
			runNextStage(world);
			return null;
		}
		return htmltext;
	}
	
	private void enterInstance(Player player, int type)
	{
		Reflection reflection = player.getActiveReflection();
		if ((reflection != null) && (type == 1))
		{
			if (player.canReenterInstance(INSTANCE_ID))
			{
				player.teleToLocation(reflection.getTeleportLoc(), reflection);
			}
		}
		else if ((reflection != null) && (type == 2))
		{
			World world = worlds.get(player.getReflectionId());
			runStartStage(world);
		}
		else if (player.canEnterInstance(INSTANCE_ID))
		{
			Reflection newInstance = ReflectionUtils.enterReflection(player, INSTANCE_ID);
			World world = new World();
			world.instanceId = newInstance.getId();
			world.player = player;
			worlds.put(newInstance.getId(), world);
		}
	}
	
	private void runStartStage(World world)
	{
		world.status = 0;
		world.monsters = new HashMap<>();
		world.helpers = new HashMap<>();
		QuestState st = world.player.getQuestState("_10346_DayOfDestinyKamaelsFate");
		int helper1 = 0;
		int helper2 = 0;
		helper1 = st.getInt("1");
		helper2 = st.getInt("2");
		if (st != null)
		{
			world.helpers.put(helper1, addSpawnToInstance(helper1, new Location(56325, -175536, -7952, 49820), 0, world.instanceId));
			world.helpers.put(helper2, addSpawnToInstance(helper2, new Location(56005, -175536, -7952, 49044), 0, world.instanceId));
			st.unset("1");
			st.unset("2");
		}
		world.player.teleToLocation(56167, -175615, -7944, world.instanceId);
		world.helpers.put(Integer.valueOf(Adolph), addSpawnToInstance(Adolph, new Location(56167, -175615, -7944, 49180), 0, world.instanceId));
		if (world.helpers.containsKey(Integer.valueOf(Alice)))
		{
			world.helpers.get(Integer.valueOf(Alice)).getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, world.player);
		}
		world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURRECTED_NONE_WAVE, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
		runNextStage(world);
	}
	
	private void runNextStage(World world)
	{
		world.status++;
		switch (world.status)
		{
			case 1:
				world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURRECTED_FIRST_WAVE, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				break;
			case 2:
				world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURRECTED_SECOND_WAVE, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				break;
			case 3:
				world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURRECTED_THIRD_WAVE, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				break;
			case 4:
				world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURRECTED_FOURTH_WAVE, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				break;
			case 5:
				world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURRECTED_FIFTH_WAVE, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
				break;
		}
		world.monsters.clear();
		world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURRECTED_DEATH_WOUND_HAS_BEEN_SUMMONED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
		for (int[] spawn : SPAWNLIST_MONSTERS_1_WAVE_TYPE_1[Rnd.get(SPAWNLIST_MONSTERS_1_WAVE_TYPE_1.length)])
		{
			NpcInstance mob = addSpawnToInstance(spawn[0], new Location(spawn[1], spawn[2], spawn[3], spawn[4]), 0, world.instanceId);
			world.monsters.put(mob, Boolean.valueOf(false));
		}
		for (NpcInstance helper : world.helpers.values())
		{
			if (helper.getNpcId() == 33171)
			{
				continue;
			}
			Object[] monsters = world.monsters.keySet().toArray();
			NpcInstance randomMonster = (NpcInstance) monsters[Rnd.get(monsters.length)];
			helper.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, randomMonster, 300);
		}
	}
	
	private boolean checkKillProgress(NpcInstance npc, World world)
	{
		if (world.monsters.containsKey(npc))
		{
			world.monsters.put(npc, true);
		}
		for (boolean value : world.monsters.values())
		{
			if (!value)
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		Player player = st.getPlayer();
		String htmltext = "noquest";
		int corp1 = st.getInt("Corp1");
		int corp2 = st.getInt("Corp2");
		int corp3 = st.getInt("Corp3");
		int corp4 = st.getInt("Corp4");
		if (npcId == Winonin)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-5.htm";
			}
			else if (cond == 11)
			{
				htmltext = "0-6.htm";
			}
			else
			{
				htmltext = "noquest";
			}
		}
		else if (npcId == Quartermaster)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if (cond == 1)
			{
				htmltext = "1-1.htm";
				st.set("Corp1", 0);
				st.set("Corp2", 0);
				st.set("Corp3", 0);
				st.set("Corp4", 0);
			}
			else if (cond == 2)
			{
				htmltext = "1-3.htm";
				st.set("Corp1", 0);
				st.set("Corp2", 0);
				st.set("Corp3", 0);
				st.set("Corp4", 0);
			}
			else if (cond == 3)
			{
				htmltext = "1-4.htm";
			}
			else if (cond == 4)
			{
				htmltext = "1-5.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == VanguardCorpse1)
		{
			if ((cond == 2) && (corp1 == 0))
			{
				htmltext = "2-11.htm";
			}
			else if ((cond == 2) && (corp1 == 1))
			{
				htmltext = "2-3.htm";
			}
			else if (cond == 3)
			{
				htmltext = "2-4.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == VanguardCorpse2)
		{
			if ((cond == 2) && (corp2 == 0))
			{
				htmltext = "2-12.htm";
			}
			else if ((cond == 2) && (corp2 == 1))
			{
				htmltext = "2-3.htm";
			}
			else if (cond == 3)
			{
				htmltext = "2-4.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == VanguardCorpse3)
		{
			if ((cond == 2) && (corp3 == 0))
			{
				htmltext = "2-13.htm";
			}
			else if ((cond == 2) && (corp3 == 1))
			{
				htmltext = "2-3.htm";
			}
			else if (cond == 3)
			{
				htmltext = "2-4.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == VanguardCorpse4)
		{
			if ((cond == 2) && (corp4 == 0))
			{
				htmltext = "2-14.htm";
			}
			else if ((cond == 2) && (corp4 == 1))
			{
				htmltext = "2-3.htm";
			}
			else if (cond == 3)
			{
				htmltext = "2-4.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		else if (npcId == Vanguardmember)
		{
			if ((cond == 4) || (cond == 5) || (cond == 6) || (cond == 7) || (cond == 8) || (cond == 9) || (cond == 10) || (cond == 11))
			{
				htmltext = "3-1.htm";
			}
		}
		else if (npcId == Barton)
		{
			if (cond == 5)
			{
				htmltext = "4-1.htm";
			}
			else if (cond == 6)
			{
				htmltext = "4-2.htm";
			}
		}
		else if (npcId == Alice)
		{
			if (cond == 5)
			{
				htmltext = "5-1.htm";
			}
			else if (cond == 6)
			{
				htmltext = "5-2.htm";
			}
		}
		else if (npcId == Hayuk)
		{
			if (cond == 5)
			{
				htmltext = "6-1.htm";
			}
			else if (cond == 6)
			{
				htmltext = "6-2.htm";
			}
		}
		else if (npcId == Eliyah)
		{
			if (cond == 5)
			{
				htmltext = "7-1.htm";
			}
			else if (cond == 6)
			{
				htmltext = "7-2.htm";
			}
		}
		else if (npcId == Adolph)
		{
			if (cond == 5)
			{
				htmltext = "8-1.htm";
			}
			else if (cond == 6)
			{
				htmltext = "8-2.htm";
			}
			else if (cond == 7)
			{
				htmltext = "8-3.htm";
			}
			else if (cond == 9)
			{
				htmltext = "8-5.htm";
			}
			else if (cond == 10)
			{
				htmltext = "8-7.htm";
			}
			else if (cond == 11)
			{
				htmltext = "8-8.htm";
				ItemFunctions.addItem(player, 736, 1, true);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		World world = worlds.get(npc.getReflectionId());
		if (world == null)
		{
			return null;
		}
		switch (world.status)
		{
			case 1:
				if (checkKillProgress(npc, world))
				{
					runNextStage(world);
				}
				break;
			case 2:
				if (checkKillProgress(npc, world))
				{
					runNextStage(world);
				}
				break;
			case 3:
				if (checkKillProgress(npc, world))
				{
					runNextStage(world);
				}
				break;
			case 4:
				if (checkKillProgress(npc, world))
				{
					runNextStage(world);
				}
				break;
			case 5:
				if (checkKillProgress(npc, world))
				{
					runNextStage(world);
				}
				break;
			case 6:
				if (checkKillProgress(npc, world) && (st != null))
				{
					world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_HAVE_STOPPED_THEIR_ATTACK_REST_AND_THEN_SPEAK_WITH_ADOLPH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
					st.setCond(9);
					st.playSound("SOUND_MIDDLE");
				}
				break;
			case 7:
				if (checkKillProgress(npc, world))
				{
					world.player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_HAVE_STOPPED_THEIR_ATTACK_REST_AND_THEN_SPEAK_WITH_ADOLPH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER));
					st.setCond(11);
					st.playSound("SOUND_MIDDLE");
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
