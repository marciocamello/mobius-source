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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q00196_SevenSignsSealOfTheEmperor extends Quest implements ScriptFile
{
	// Npcs
	private static final int IasonHeine = 30969;
	private static final int MerchantofMammon = 32584;
	private static final int PromiseofMammon = 32585;
	private static final int Shunaiman = 32586;
	private static final int Leon = 32587;
	private static final int DisciplesGatekeeper = 32657;
	private static final int CourtMagician = 32598;
	private static final int Wood = 32593;
	// Items
	private static final int ElmoredenHolyWater = 13808;
	private static final int CourtMagiciansMagicStaff = 13809;
	private static final int SealOfBinding = 13846;
	private static final int SacredSwordofEinhasad = 15310;
	// Others
	private NpcInstance MerchantofMammonSpawn;
	private static final int door11 = 17240111;
	private static final int InstanceId = 112;
	
	public Q00196_SevenSignsSealOfTheEmperor()
	{
		super(false);
		addStartNpc(IasonHeine);
		addTalkId(IasonHeine, MerchantofMammon, PromiseofMammon, Shunaiman, Leon, DisciplesGatekeeper, CourtMagician, Wood);
		addQuestItem(ElmoredenHolyWater, CourtMagiciansMagicStaff, SealOfBinding, SacredSwordofEinhasad);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		final Reflection ref = player.getReflection();
		
		switch (event)
		{
			case "iasonheine_q196_1d.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "iasonheine_q196_2.htm":
				if (GameObjectsStorage.getAllByNpcId(MerchantofMammon, false).isEmpty())
				{
					MerchantofMammonSpawn = qs.addSpawn(MerchantofMammon, 109763, 219944, -3512, 16384, 0, 120 * 1000);
					Functions.npcSay(MerchantofMammonSpawn, "Who dares summon the Merchant of Mammon?!");
				}
				break;
			
			case "merchantofmammon_q196_2.htm":
				if (MerchantofMammonSpawn != null)
				{
					MerchantofMammonSpawn.deleteMe();
					MerchantofMammonSpawn = null;
				}
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "teleport_instance":
				if (((qs.getCond() == 3) || (qs.getCond() == 4)))
				{
					enterInstance(player);
				}
				else
				{
					player.sendMessage("You can only access the Necropolis of Dawn while carrying Seal of the Emperor quest.");
				}
				return null;
				
			case "collapse_instance":
				ref.collapse();
				htmltext = "leon_q196_1.htm";
				break;
			
			case "shunaiman_q196_2.htm":
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				player.sendPacket(new SystemMessage(SystemMessage.BY_USING_THE_SKILL_OF_EINHASAD_S_HOLY_SWORD_DEFEAT_THE_EVIL_LILIMS));
				player.sendPacket(new SystemMessage(SystemMessage.BY_USING_THE_HOLY_WATER_OF_EINHASAD_OPEN_THE_DOOR_POSSESSED_BY_THE_CURSE_OF_FLAMES));
				qs.giveItems(SacredSwordofEinhasad, 1);
				qs.giveItems(ElmoredenHolyWater, 1);
				break;
			
			case "courtmagician_q196_2.htm":
				qs.playSound(SOUND_ITEMGET);
				qs.giveItems(CourtMagiciansMagicStaff, 1);
				player.sendPacket(new SystemMessage(SystemMessage.BY_USING_THE_COURT_MAGICIAN_S_MAGIC_STAFF_OPEN_THE_DOOR_ON_WHICH_THE_MAGICIAN_S_BARRIER_IS));
				break;
			
			case "free_anakim":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ_SEALING_EMPEROR_1ST);
				player.sendPacket(new SystemMessage(SystemMessage.IN_ORDER_TO_HELP_ANAKIM_ACTIVATE_THE_SEALING_DEVICE_OF_THE_EMPEROR_WHO_IS_POSSESED_BY_THE_EVIL));
				ref.openDoor(door11);
				ThreadPoolManager.getInstance().schedule(new SpawnLilithRoom(ref), 17000);
				return null;
				
			case "shunaiman_q196_4.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				qs.takeItems(SealOfBinding, -1);
				qs.takeItems(ElmoredenHolyWater, -1);
				qs.takeItems(CourtMagiciansMagicStaff, -1);
				qs.takeItems(SacredSwordofEinhasad, -1);
				break;
			
			case "leon_q196_2.htm":
				player.getReflection().collapse();
				break;
			
			case "iasonheine_q196_6.htm":
				qs.setCond(6);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "wood_q196_2.htm":
				if (player.getBaseClassId() == player.getActiveClassId())
				{
					qs.addExpAndSp(10000000, 2500000);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				else
				{
					return "subclass_forbidden.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		switch (npcId)
		{
			case IasonHeine:
				final QuestState state = player.getQuestState(Q00195_SevenSignsSecretRitualOfThePriests.class);
				switch (cond)
				{
					case 0:
						if ((player.getLevel() >= 79) && (state != null) && state.isCompleted())
						{
							htmltext = "iasonheine_q196_1.htm";
						}
						else
						{
							htmltext = "iasonheine_q196_0.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 1:
						htmltext = "iasonheine_q196_1a.htm";
						break;
					
					case 2:
						qs.setCond(3);
						qs.playSound(SOUND_MIDDLE);
						htmltext = "iasonheine_q196_3.htm";
						break;
					
					case 3:
					case 4:
						htmltext = "iasonheine_q196_4.htm";
						break;
					
					case 5:
						htmltext = "iasonheine_q196_5.htm";
						break;
					
					case 6:
						htmltext = "iasonheine_q196_6a.htm";
						break;
				}
				break;
			
			case MerchantofMammon:
				if ((cond == 1) && (MerchantofMammonSpawn != null))
				{
					htmltext = "merchantofmammon_q196_1.htm";
				}
				else
				{
					htmltext = "merchantofmammon_q196_0.htm";
				}
				break;
			
			case Shunaiman:
				switch (cond)
				{
					case 3:
						htmltext = "shunaiman_q196_1.htm";
						break;
					
					case 4:
						if (qs.getQuestItemsCount(SealOfBinding) >= 4)
						{
							htmltext = "shunaiman_q196_3.htm";
						}
						else if (qs.getQuestItemsCount(SealOfBinding) < 4)
						{
							htmltext = "shunaiman_q196_3a.htm";
						}
						break;
					
					case 5:
						htmltext = "shunaiman_q196_4a.htm";
						break;
				}
				break;
			
			case CourtMagician:
				if ((cond == 4) && (qs.getQuestItemsCount(CourtMagiciansMagicStaff) < 1))
				{
					htmltext = "courtmagician_q196_1.htm";
				}
				else
				{
					htmltext = "courtmagician_q196_1a.htm";
				}
				break;
			
			case DisciplesGatekeeper:
				if (cond == 4)
				{
					htmltext = "disciplesgatekeeper_q196_1.htm";
				}
				break;
			
			case Leon:
				if (cond == 5)
				{
					htmltext = "leon_q196_1.htm";
				}
				else
				{
					htmltext = "leon_q196_1a.htm";
				}
				break;
			
			case Wood:
				if (cond == 6)
				{
					htmltext = "wood_q196_1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	private void enterInstance(Player player)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(InstanceId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(InstanceId))
		{
			ReflectionUtils.enterReflection(player, InstanceId);
		}
	}
	
	private class SpawnLilithRoom extends RunnableImpl
	{
		Reflection _r;
		
		public SpawnLilithRoom(Reflection r)
		{
			_r = r;
		}
		
		@Override
		public void runImpl()
		{
			if (_r != null)
			{
				_r.addSpawnWithoutRespawn(32715, new Location(-83175, 217021, -7504, 49151), 0);
				_r.addSpawnWithoutRespawn(32718, new Location(-83179, 216479, -7504, 16384), 0);
				_r.addSpawnWithoutRespawn(32717, new Location(-83222, 217055, -7504, 49151), 0);
				_r.addSpawnWithoutRespawn(32716, new Location(-83127, 217056, -7504, 49151), 0);
				_r.addSpawnWithoutRespawn(32719, new Location(-83227, 216443, -7504, 16384), 0);
				_r.addSpawnWithoutRespawn(32721, new Location(-83179, 216432, -7504, 16384), 0);
				_r.addSpawnWithoutRespawn(32720, new Location(-83134, 216443, -7504, 16384), 0);
			}
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
