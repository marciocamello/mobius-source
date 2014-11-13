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

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import ai.GuardofDawn;
import ai.GuardofDawnFemale;
import ai.GuardofDawnStat;

public class Q00195_SevenSignsSecretRitualOfThePriests extends Quest implements ScriptFile
{
	// Npcs
	private static final int ClaudiaAthebaldt = 31001;
	private static final int John = 32576;
	private static final int Raymond = 30289;
	private static final int LightofDawn = 32575;
	private static final int IdentityConfirmDevice = 32578;
	private static final int PasswordEntryDevice = 32577;
	private static final int IasonHeine = 30969;
	private static final int BookShelf = 32580;
	// Items
	private static final int GuardsoftheDawnIdentityCard = 13822;
	private static final int EmperorShunaimansContract = 13823;
	// Others
	private static final int door1 = 17240001;
	private static final int door2 = 17240002;
	private static final int door3 = 17240003;
	private static final int door4 = 17240004;
	private static final int door5 = 17240005;
	private static final int door6 = 17240006;
	private static final int instanceId = 111;
	private static final Location[][] guardsOfDawn1 =
	{
		{
			new Location(-75208, 212600, -7320),
			new Location(-75208, 212248, -7320),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-74616, 212600, -7320),
			new Location(-74616, 212248, -7320),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-75064, 212136, -7320),
			new Location(-74824, 212136, -7320),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-74696, 211480, -7320),
			new Location(-74696, 211144, -7320),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-75272, 211144, -7320),
			new Location(-75272, 211480, -7320),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-78088, 208504, -7704),
			new Location(-77304, 208504, -7704),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-77944, 207800, -7704),
			new Location(-77448, 207800, -7704),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-77688, 207544, -7704),
			new Location(-77688, 208024, -7704),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-77304, 207928, -7704),
			new Location(-76968, 207928, -7704),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78104, 207928, -7704),
			new Location(-78424, 207928, -7704),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-77032, 207128, -7704),
			new Location(-77320, 207416, -7704),
			new Location(-77688, 209112, -7608)
		},
	};
	private static final Location[][] guardsOfDawn1stationary =
	{
		{
			new Location(-74952, 211848, -7320, 16384),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-77208, 208312, -7704, 32768),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78184, 208264, -7704, 32768),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-77240, 207528, -7704, 32768),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78168, 207528, -7704, 32768),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-77544, 207080, -7704, 16384),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78040, 207400, -7704, 49152),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78408, 207144, -7704, 0),
			new Location(-77688, 209112, -7608)
		},
	};
	private static final Location[][] guardsOfDawnFemale =
	{
		{
			new Location(-75272, 209976, -7416, 0),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-74616, 209976, -7416, 32768),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-74280, 208792, -7512, 32768),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-74552, 206616, -7512, 49152),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-75448, 206712, -7512, 32768),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-77688, 208376, -7696, 16384),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-77688, 207224, -7696, 49152),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-76936, 207800, -7696, 32768),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78488, 207800, -7696, 0),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78936, 205416, -7918, 16384),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-79784, 205432, -7918, 49152),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-79784, 206280, -7918, 49152),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78952, 206280, -7918, 49152),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-81544, 205464, -7984, 16384),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-81544, 206168, -7984, 49152),
			new Location(-77688, 209112, -7608)
		},
	};
	private static final Location[][] guardsOfDawn2 =
	{
		{
			new Location(-75464, 210216, -7416),
			new Location(-74456, 210216, -7416),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-74456, 209752, -7416),
			new Location(-75464, 209752, -7416),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-74472, 208344, -7512),
			new Location(-74216, 208344, -7512),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-74488, 207016, -7512),
			new Location(-74216, 207016, -7512),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-74216, 206472, -7512),
			new Location(-75672, 206456, -7512),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-74968, 206680, -7512),
			new Location(-74968, 206344, -7512),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-75688, 206968, -7512),
			new Location(-75416, 206968, -7512),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-75544, 208712, -7512),
			new Location(-75544, 208152, -7512),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-76392, 207912, -7608),
			new Location(-76616, 207912, -7608),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-76616, 208248, -7608),
			new Location(-76392, 208248, -7608),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-76616, 208920, -7608),
			new Location(-76392, 208920, -7608),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-76952, 209432, -7608),
			new Location(-76952, 209208, -7608),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-77272, 209208, -7608),
			new Location(-77272, 209432, -7608),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-78936, 205576, -7888),
			new Location(-78728, 205416, -7888),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78904, 205256, -7888),
			new Location(-79096, 205416, -7888),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-79368, 205000, -7888),
			new Location(-79368, 205368, -7888),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-79784, 205592, -7888),
			new Location(-80008, 205448, -7888),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78904, 206088, -7888),
			new Location(-78696, 206264, -7888),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-78936, 206488, -7888),
			new Location(-79128, 206312, -7888),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-79368, 206728, -7888),
			new Location(-79368, 206344, -7888),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-79992, 206280, -7888),
			new Location(-79800, 206104, -7888),
			new Location(-77688, 209112, -7608)
		},
		{
			new Location(-81896, 205848, -7984),
			new Location(-81128, 205848, -7984),
			new Location(-77688, 209112, -7608)
		},
	};
	private static final Location[][] guardsOfDawn2stationary =
	{
		{
			new Location(-74856, 213496, -7224, 16384),
			new Location(-75992, 213416, -7148)
		},
		{
			new Location(-75624, 208792, -7512, 0),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-75432, 208040, -7512, 16384),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-75672, 208040, -7512, 16384),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-74952, 207528, -7512, 16384),
			new Location(-74952, 209320, -7480)
		},
		{
			new Location(-74968, 207704, -7512, 16384),
			new Location(-74952, 209320, -7480)
		},
	};
	
	public Q00195_SevenSignsSecretRitualOfThePriests()
	{
		super(false);
		addStartNpc(ClaudiaAthebaldt);
		addTalkId(John, Raymond, LightofDawn, IdentityConfirmDevice, ClaudiaAthebaldt, PasswordEntryDevice, IasonHeine, BookShelf);
		addQuestItem(GuardsoftheDawnIdentityCard, EmperorShunaimansContract);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		final Reflection ref = player.getReflection();
		
		switch (event)
		{
			case "claudiaathebaldt_q195_2.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "john_q195_2.htm":
				qs.setCond(2);
				qs.giveItems(GuardsoftheDawnIdentityCard, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "raymond_q195_3.htm":
				if ((player.getTransformation() != 0) || player.isMounted())
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				qs.playSound(SOUND_MIDDLE);
				negateSpeedBuffs(player);
				SkillTable.getInstance().getInfo(6204, 1).getEffects(player, player, false, false);
				qs.setCond(3);
				break;
			
			case "transformagain":
				if ((player.getTransformation() != 0) || player.isMounted())
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				negateSpeedBuffs(player);
				SkillTable.getInstance().getInfo(6204, 1).getEffects(player, player, false, false);
				htmltext = "raymond_q195_4c.htm";
				break;
			
			case "dispel":
				if (player.getTransformation() == 113)
				{
					player.setTransformation(0);
					htmltext = "raymond_q195_4d.htm";
				}
				else
				{
					htmltext = "raymond_q195_4b.htm";
				}
				break;
			
			case "teleout":
				if (qs.getQuestItemsCount(GuardsoftheDawnIdentityCard) > 0)
				{
					htmltext = "darknessofdawn_q195_1.htm";
				}
				else
				{
					htmltext = "darknessofdawn_q195_2.htm";
				}
				if (ref != null)
				{
					ref.collapse();
				}
				break;
			
			case "telelater":
				return null;
				
			case "open_door":
				if ((ref != null) && (player.getTransformation() == 113) && (qs.getQuestItemsCount(GuardsoftheDawnIdentityCard) >= 1))
				{
					if (npc.getLoc().equals(new Location(-75695, 213537, -7128, 0)))
					{
						ref.openDoor(door1);
						ref.openDoor(door2);
						player.sendPacket(new SystemMessage(SystemMessage.BY_USING_THE_INVISIBLE_SKILL_SNEAK_INTO_THE_DAWN_S_DOCUMENT_STORAGE));
						player.sendPacket(new SystemMessage(SystemMessage.MALE_GUARDS_CAN_DETECT_THE_CONCEALMENT_BUT_THE_FEMALE_GUARDS_CANNOT));
						player.sendPacket(new SystemMessage(SystemMessage.FEMALE_GUARDS_NOTICE_THE_DISGUISES_FROM_FAR_AWAY_BETTER_THAN_THE_MALE_GUARDS_DO_SO_BEWARE));
						htmltext = "identityconfirmdevice_q195_1.htm";
					}
					else
					{
						ref.openDoor(door3);
						ref.openDoor(door4);
						player.sendPacket(new SystemMessage(SystemMessage.THE_DOOR_IN_FRONT_OF_US_IS_THE_ENTRANCE_TO_THE_DAWN_S_DOCUMENT_STORAGE_APPROACH_TO_THE_CODE));
						player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ_RITUAL_OF_PRIEST);
						htmltext = "identityconfirmdevice_q195_1.htm";
					}
				}
				else
				{
					return "identityconfirmdevice_q195_2.htm";
				}
				break;
			
			case "false_code":
				htmltext = "passwordentrydevice_q195_2.htm";
				break;
			
			case "correct_code":
				if (ref != null)
				{
					ref.openDoor(door5);
					ref.openDoor(door6);
				}
				htmltext = "passwordentrydevice_q195_1.htm";
				break;
			
			case "bookshelf_q195_2.htm":
				qs.giveItems(EmperorShunaimansContract, 1);
				qs.playSound(SOUND_ITEMGET);
				break;
			
			case "bookshelf_q195_3.htm":
				if ((ref != null) && !ref.isDefault())
				{
					ref.collapse();
				}
				break;
			
			case "raymond_q195_5.htm":
				player.setTransformation(0);
				qs.takeItems(GuardsoftheDawnIdentityCard, -1);
				qs.playSound(SOUND_ITEMGET);
				qs.setCond(4);
				break;
			
			case "iasonheine_q195_2.htm":
				if (player.getBaseClassId() == player.getActiveClassId())
				{
					qs.takeItems(EmperorShunaimansContract, -1);
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
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		if (player.getBaseClassId() != player.getActiveClassId())
		{
			return "subclass_forbidden.htm";
		}
		
		switch (npcId)
		{
			case ClaudiaAthebaldt:
				final QuestState state = player.getQuestState(Q00194_SevenSignsMammonsContract.class);
				if (cond == 0)
				{
					if ((player.getLevel() >= 79) && (state != null) && state.isCompleted())
					{
						htmltext = "claudiaathebaldt_q195_1.htm";
					}
					else
					{
						htmltext = "claudiaathebaldt_q195_0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "claudiaathebaldt_q195_3.htm";
				}
				break;
			
			case John:
				if (cond == 1)
				{
					htmltext = "john_q195_1.htm";
				}
				else
				{
					htmltext = "john_q195_3.htm";
				}
				break;
			
			case Raymond:
				switch (cond)
				{
					case 2:
						htmltext = "raymond_q195_1.htm";
						break;
					
					case 3:
						if (qs.getQuestItemsCount(EmperorShunaimansContract) >= 1)
						{
							htmltext = "raymond_q195_4.htm";
						}
						else
						{
							htmltext = "raymond_q195_4a.htm";
						}
						break;
					
					case 4:
						htmltext = "raymond_q195_5b.htm";
						break;
				}
				break;
			
			case LightofDawn:
				if ((cond == 3) && (player.getTransformation() == 113))
				{
					enterInstance(player);
					htmltext = "lightofdawn_q195_1.htm";
				}
				else
				{
					htmltext = "lightofdawn_q195_2.htm";
				}
				break;
			
			case BookShelf:
				if ((cond == 3) && (player.getTransformation() == 113))
				{
					htmltext = "bookshelf_q195_1.htm";
				}
				break;
			
			case IasonHeine:
				if ((cond == 4) && (qs.getQuestItemsCount(EmperorShunaimansContract) > 0))
				{
					htmltext = "iasonheine_q195_1.htm";
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
			if (player.canReenterInstance(instanceId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(instanceId))
		{
			Reflection newInstance = ReflectionUtils.enterReflection(player, instanceId);
			synchronized (guardsOfDawn1)
			{
				for (Location spawn[] : guardsOfDawn1)
				{
					NpcInstance guard = newInstance.addSpawnWithoutRespawn(18835, spawn[0], 0);
					guard.setAI(new GuardofDawn(guard, spawn[1], spawn[2]));
				}
				
				for (Location spawn[] : guardsOfDawn1stationary)
				{
					NpcInstance guard = newInstance.addSpawnWithoutRespawn(18835, spawn[0], 0);
					guard.setAI(new GuardofDawnStat(guard, spawn[1]));
				}
				
				for (Location spawn[] : guardsOfDawnFemale)
				{
					NpcInstance guard = newInstance.addSpawnWithoutRespawn(27352, spawn[0], 0);
					guard.setAI(new GuardofDawnFemale(guard, spawn[1]));
				}
				
				for (Location spawn[] : guardsOfDawn2)
				{
					NpcInstance guard = newInstance.addSpawnWithoutRespawn(18834, spawn[0], 0);
					guard.setAI(new GuardofDawn(guard, spawn[1], spawn[2]));
				}
				
				for (Location spawn[] : guardsOfDawn2stationary)
				{
					NpcInstance guard = newInstance.addSpawnWithoutRespawn(18834, spawn[0], 0);
					guard.setAI(new GuardofDawnStat(guard, spawn[1]));
				}
			}
		}
	}
	
	private void negateSpeedBuffs(Player p)
	{
		for (Effect e : p.getEffectList().getAllEffects())
		{
			if (e.getStackType().equals("SpeedUp") && !e.isOffensive())
			{
				e.exit();
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
