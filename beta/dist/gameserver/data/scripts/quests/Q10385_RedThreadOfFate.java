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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q10385_RedThreadOfFate extends Quest implements ScriptFile, OnMagicUseListener
{
	public static final int INSTANCE_ID = 241;
	private static final int Rean = 33491;
	private static final int Morelin = 30925;
	private static final int Lania = 33783;
	private static final int HeineWaterSource = 33784;
	private static final int Ladyofthelike = 31745;
	private static final int Nerupa = 30370;
	private static final int Innocentin = 31328;
	private static final int Enfeux = 31519;
	private static final int Vulkan = 31539;
	private static final int Urn = 31149;
	private static final int Wesley = 30166;
	private static final int DesertedDwarvenHouse = 33788;
	private static final int PaagrioTemple = 33787;
	private static final int AltarofShilen = 33785;
	private static final int ShilensMessenger = 27492; // spawned by scripts mob
	private static final int CaveofSouls = 33789;
	private static final int MotherTree = 33786;
	private static final int Darin = 33748; // instance
	private static final int Roxxy = 33749; // instance
	private static final int BiotinHighPriest = 30031; // instance
	private static final int MysteriousDarkKnight = 33751; // spawned by script npc
	// Items
	private static final int MysteriosLetter = 36072;
	private static final int Waterfromthegardenofeva = 36066;
	private static final int Clearestwater = 36067;
	private static final int Brightestlight = 36068;
	private static final int Purestsoul = 36069;
	private static final int Vulkangold = 36113;
	private static final int Vulkansilver = 36114;
	private static final int Vulkanfire = 36115;
	private static final int Fiercestflame = 36070;
	private static final int Fondestheart = 36071;
	private static final int SOEDwarvenvillage = 20376;
	private static final int DimensionalDiamond = 7562;
	// Skills
	private static final int water = 9579;
	private static final int light = 9580;
	private static final int soul = 9581;
	private static final int flame = 9582;
	private static final int love = 9583;
	private final ZoneListener _zoneListener;
	
	public Q10385_RedThreadOfFate()
	{
		super(false);
		addStartNpc(Rean);
		addTalkId(Morelin, Lania, HeineWaterSource, Ladyofthelike, Nerupa, Innocentin, Enfeux, Vulkan, Urn, Wesley, DesertedDwarvenHouse, PaagrioTemple, AltarofShilen, ShilensMessenger, CaveofSouls, MotherTree, Darin, Roxxy, BiotinHighPriest, MysteriousDarkKnight);
		addQuestItem(MysteriosLetter, Waterfromthegardenofeva, Clearestwater, Brightestlight, Purestsoul, Vulkangold, Vulkansilver, Vulkanfire, Fiercestflame, Fondestheart, SOEDwarvenvillage, DimensionalDiamond);
		addKillId(ShilensMessenger);
		addLevelCheck(85, 100);
		// addQuestCompletedCheck(Q10338_SeizeYourDestiny.class);
		_zoneListener = new ZoneListener();
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "Rean_q10385_03.htm":
				if (cond == 0)
				{
					qs.setState(2);
					qs.setCond(1);
					qs.giveItems(MysteriosLetter, 1L);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "Morelin_q10385_02.htm":
				if (cond == 1)
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Lania_q10385_02.htm":
				if (cond == 2)
				{
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Ladyofthelike_q10385_03.htm":
				if (cond == 6)
				{
					qs.takeItems(Waterfromthegardenofeva, 1L);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Ladyofthelike_q10385_06.htm":
				if (cond == 6)
				{
					qs.giveItems(Clearestwater, 1L);
					qs.setCond(7);
					qs.getPlayer().teleToLocation(new Location(172440, 90312, -2011));
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Nerupa_q10385_04.htm":
				if (cond == 7)
				{
					qs.setCond(8);
					qs.giveItems(Brightestlight, 1L);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Enfeux_q10385_02.htm":
				if (cond == 8)
				{
					qs.giveItems(Purestsoul, 1L);
					qs.setCond(9);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Innocentin_q10385_02.htm":
				if (cond == 9)
				{
					qs.setCond(10);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Vulkan_q10385_04.htm":
				if (cond == 10)
				{
					qs.giveItems(Vulkangold, 1L);
					qs.giveItems(Vulkansilver, 1L);
					qs.giveItems(Vulkanfire, 1L);
					qs.setCond(11);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Wesley_q10385_03.htm":
				if (cond == 13)
				{
					qs.getPlayer().teleToLocation(new Location(180168, -111720, -5856));
				}
				break;
			
			case "Vulkan_q10385_08.htm":
				if (cond == 13)
				{
					qs.giveItems(Fiercestflame, 1L);
					qs.giveItems(Fondestheart, 1L);
					qs.giveItems(SOEDwarvenvillage, 1L);
					qs.setCond(14);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Urn_q10385_02.htm":
				if (cond == 11)
				{
					qs.takeItems(Vulkangold, 1L);
					qs.takeItems(Vulkansilver, 1L);
					qs.takeItems(Vulkanfire, 1L);
					qs.setCond(12);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Darin_q10385_03.htm":
				if (cond == 19)
				{
					qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcStringId.SPEAK_WITH_ROXXY, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, new String[0]));
				}
				break;
			
			case "Roxxy_q10385_02.htm":
				if (cond == 19)
				{
					qs.setCond(20);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "Biotin_q10385_03.htm":
				if (cond == 20)
				{
					qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcStringId.GO_OUTSIDE_THE_TEMPLE, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, new String[0]));
					qs.setCond(21);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "showMovie":
				if (cond == 21)
				{
					qs.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_SC_SUB_QUEST);
					qs.startQuestTimer("timer1", 25000L);
					htmltext = null;
				}
				break;
			
			case "timer1":
				qs.setCond(22);
				qs.cancelQuestTimer("timer1");
				qs.getPlayer().teleToLocation(-113656, 246040, -3724, 0);
				htmltext = null;
				break;
			
			case "Rean_q10385_05.htm":
				if (cond == 22)
				{
					qs.giveItems(DimensionalDiamond, 40L);
					qs.setState(3);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (qs.getPlayer().getVar("q10338") == null)
		{
			return "<html><head><body>You didn't complete quest Seize Your Destiny1...</body></html>";
		}
		
		switch (npc.getId())
		{
			case Rean:
				if (cond == 22)
				{
					htmltext = "Rean_q10385_04.htm";
				}
				else if (cond == 0)
				{
					htmltext = "Rean_q10385_01.htm";
				}
				else if (cond > 22)
				{
					htmltext = "Rean_q10385_0.htm";
				}
				break;
			
			case Morelin:
				if (cond == 1)
				{
					htmltext = "Morelin_q10385_01.htm";
				}
				break;
			
			case Lania:
				if (cond == 4)
				{
					htmltext = "Lania_q10385_03.htm";
					qs.setCond(5);
					qs.playSound(SOUND_MIDDLE);
				}
				else if (cond == 2)
				{
					htmltext = "Lania_q10385_01.htm";
				}
				else if (cond == 3)
				{
					htmltext = "Lania_q10385_02.htm";
				}
				break;
			
			case HeineWaterSource:
				if (cond == 5)
				{
					htmltext = "HeineWaterSource_q10385_01.htm";
					qs.giveItems(Waterfromthegardenofeva, 1L);
					qs.setCond(6);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case Ladyofthelike:
				if (cond == 6)
				{
					htmltext = "Ladyofthelike_q10385_01.htm";
				}
				break;
			
			case Nerupa:
				if (cond == 7)
				{
					htmltext = "Nerupa_q10385_01.htm";
				}
				break;
			
			case Enfeux:
				if (cond == 8)
				{
					htmltext = "Enfeux_q10385_01.htm";
				}
				break;
			
			case Innocentin:
				if (cond == 9)
				{
					htmltext = "Innocentin_q10385_01.htm";
				}
				break;
			
			case Vulkan:
				if (cond == 10)
				{
					htmltext = "Vulkan_q10385_01.htm";
				}
				else if (cond == 13)
				{
					htmltext = "Vulkan_q10385_05.htm";
				}
				break;
			
			case Urn:
				if (cond == 11)
				{
					htmltext = "Urn_q10385_01.htm";
				}
				break;
			
			case Wesley:
				if (cond == 12)
				{
					htmltext = "Wesley_q10385_01.htm";
					qs.setCond(13);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case Darin:
				if (cond == 19)
				{
					htmltext = "Darin_q10385_01.htm";
				}
				break;
			
			case Roxxy:
				if (cond == 19)
				{
					htmltext = "Roxxy_q10385_01.htm";
				}
				break;
			
			case BiotinHighPriest:
				if (cond == 20)
				{
					htmltext = "Biotin_q10385_01.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
	{
		if ((actor == null) || !actor.isPlayer() || (target == null) || !target.isNpc())
		{
			return;
		}
		
		final QuestState qs = actor.getPlayer().getQuestState(Q10385_RedThreadOfFate.class);
		
		if (qs == null)
		{
			return;
		}
		
		final NpcInstance npc = (NpcInstance) target;
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (skill.getId())
		{
			case water:
				if ((npcId == MotherTree) && (cond == 18))
				{
					ItemFunctions.removeItem(qs.getPlayer(), Clearestwater, 1L, true);
					enterInstance(qs.getPlayer());
					qs.setCond(19);
				}
				break;
			
			case light:
				if ((npcId == AltarofShilen) && (cond == 16))
				{
					ItemFunctions.removeItem(qs.getPlayer(), Brightestlight, 1L, true);
					player.sendPacket(new ExShowScreenMessage(NpcStringId.YOU_MUST_DEFEAT_SHILEN_S_MESSENGER, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, new String[0]));
					player.sendPacket(new ExShowScreenMessage(NpcStringId.THE_ONLY_GOOD_SHILEN_CREATURE_IS_A_DEAD_ONE, 4500, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, new String[0]));
					NpcInstance mob = qs.addSpawn(ShilensMessenger, 28760, 11032, -4252);
					mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 100000);
				}
				break;
			
			case soul:
				if ((npcId == CaveofSouls) && (cond == 17))
				{
					ItemFunctions.removeItem(qs.getPlayer(), Purestsoul, 1L, true);
					qs.setCond(18);
				}
				break;
			
			case flame:
				if ((npcId == PaagrioTemple) && (cond == 15))
				{
					ItemFunctions.removeItem(qs.getPlayer(), Fiercestflame, 1L, true);
					qs.setCond(16);
				}
				break;
			
			case love:
				if ((npcId == DesertedDwarvenHouse) && (cond == 14))
				{
					ItemFunctions.removeItem(qs.getPlayer(), Fondestheart, 1L, true);
					qs.setCond(15);
				}
				break;
		}
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getPlayer() == null)
		{
			return null;
		}
		
		if (qs.getCond() == 16)
		{
			qs.setCond(17);
			qs.playSound(SOUND_ITEMGET);
		}
		
		return null;
	}
	
	private void enterInstance(Player player)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(INSTANCE_ID))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(INSTANCE_ID))
		{
			Reflection newInstance = ReflectionUtils.enterReflection(player, INSTANCE_ID);
			newInstance.getZone("[Q10385_EXIT_1]").addListener(_zoneListener);
			newInstance.getZone("[Q10385_EXIT_2]").addListener(_zoneListener);
		}
	}
	
	@Override
	public void onSocialActionUse(QuestState qs, int actionId)
	{
		if ((qs.getPlayer().getTarget() == null) || !qs.getPlayer().getTarget().isNpc())
		{
			return;
		}
		
		final GameObject npc1 = qs.getPlayer().getTarget();
		final int npcId = ((NpcInstance) npc1).getId();
		
		if ((qs.getCond() == 3) && (npcId == Lania) && (actionId == SocialAction.BOW))
		{
			qs.setCond(4);
			qs.playSound(SOUND_MIDDLE);
		}
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature actor)
		{
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
			if (!actor.isPlayer())
			{
				return;
			}
			
			final Player player = actor.getPlayer();
			final QuestState qs = player.getQuestState(Q10385_RedThreadOfFate.class);
			
			if (qs == null)
			{
				return;
			}
			
			if (qs.getCond() == 21)
			{
				Location loc = zone.getName().contains("Q10385_EXIT_1") ? new Location(210632, 15576, -3754) : new Location(210632, 15576, -3754);
				
				if (player.getReflection().getAllByNpcId(MysteriousDarkKnight, true).isEmpty())
				{
					NpcUtils.spawnSingle(MysteriousDarkKnight, loc, player.getReflection());
				}
			}
		}
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
	}
	
	@Override
	public void onReload()
	{
	}
}
