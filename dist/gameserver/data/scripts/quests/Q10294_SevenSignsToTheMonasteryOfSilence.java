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

import java.util.StringTokenizer;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Util;

/**
 * @author pchayka
 */
public class Q10294_SevenSignsToTheMonasteryOfSilence extends Quest implements ScriptFile
{
	private static final int Elcardia = 32784;
	private static final int ErisEvilThoughts = 32792;
	private static final int ElcardiaInzone1 = 32787;
	private static final int RelicGuard = 32803;
	private static final int[] RelicWatcher = ArrayUtils.createAscendingArray(32804, 32807);
	private static final int YellowRelicWatcher = RelicWatcher[0];
	private static final int GreenRelicWatcher = RelicWatcher[1];
	private static final int BlueRelicWatcher = RelicWatcher[2];
	private static final int RedRelicWatcher = RelicWatcher[3];
	private static final int JudevanEtinasEvilThoughts = 32888;
	private static final int SolinaLayrother = 27407;
	private static final int JudevanEtinasEvilThoughts2 = 32797;
	private static final int SolinasEvilThoughts = 32793;
	// reading desks
	private static final int[] ReadingDesk = ArrayUtils.createAscendingArray(32821, 32836);
	private static final int[] YellowRoomDesks =
	{
		ReadingDesk[0],
		ReadingDesk[1],
		ReadingDesk[2],
		ReadingDesk[3]
	};
	private static final int YellowTrueReadingDesk = YellowRoomDesks[2];
	private static final int[] GreenRoomDesks =
	{
		ReadingDesk[4],
		ReadingDesk[5],
		ReadingDesk[6],
		ReadingDesk[7]
	};
	private static final int GreenTrueReadingDesk = GreenRoomDesks[3];
	private static final int[] BlueRoomDesks =
	{
		ReadingDesk[8],
		ReadingDesk[9],
		ReadingDesk[10],
		ReadingDesk[11]
	};
	private static final int BlueTrueReadingDesk = BlueRoomDesks[1];
	private static final int[] RedRoomDesks =
	{
		ReadingDesk[12],
		ReadingDesk[13],
		ReadingDesk[14],
		ReadingDesk[15]
	};
	private static final int RedTrueReadingDesk = RedRoomDesks[0];
	
	public Q10294_SevenSignsToTheMonasteryOfSilence()
	{
		super(false);
		addStartNpc(Elcardia);
		addTalkId(ErisEvilThoughts, ElcardiaInzone1, RelicGuard, JudevanEtinasEvilThoughts2, SolinasEvilThoughts);
		addTalkId(ReadingDesk);
		addTalkId(RelicWatcher);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "elcardia_q10294_4.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "eris_q10294_3.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "teleport_deeper":
				player.teleToLocation(new Location(85937, -249618, -8320));
				teleportElcardia(player);
				return null;
				
			case "teleport_deeper_out":
				player.teleToLocation(new Location(120600, -86952, -3392));
				teleportElcardia(player);
				return null;
				
			case "readingdesk_q10294_yellowtrue2.htm":
				if (qs.getInt("yellow") == 0)
				{
					npc.setNpcState(1);
					qs.set("yellow", 1);
					proccessComplete(qs);
				}
				else
				{
					htmltext = "readingdesk_q10294_0.htm";
				}
				break;
			
			case "readingdesk_q10294_greentrue2.htm":
				if (qs.getInt("green") == 0)
				{
					npc.setNpcState(1);
					qs.set("green", 1);
					qs.getPlayer().getReflection().addSpawnWithoutRespawn(JudevanEtinasEvilThoughts, new Location(87704, -249496, -8320, 49152), 0);
					
					for (int i = 0; i < 3; i++)
					{
						qs.getPlayer().getReflection().addSpawnWithoutRespawn(SolinaLayrother, Location.findPointToStay(qs.getPlayer(), 300), 0);
					}
					
					proccessComplete(qs);
				}
				else
				{
					htmltext = "readingdesk_q10294_0.htm";
				}
				break;
			
			case "readingdesk_q10294_bluetrue2.htm":
				if (qs.getInt("blue") == 0)
				{
					npc.setNpcState(1);
					qs.set("blue", 1);
					qs.getPlayer().getReflection().addSpawnWithoutRespawn(SolinasEvilThoughts, new Location(86680, -246728, -8320, 0), 0);
					proccessComplete(qs);
				}
				else
				{
					htmltext = "readingdesk_q10294_0.htm";
				}
				break;
			
			case "readingdesk_q10294_redtrue2.htm":
				if (qs.getInt("red") == 0)
				{
					npc.setNpcState(1);
					qs.set("red", 1);
					qs.getPlayer().getReflection().addSpawnWithoutRespawn(JudevanEtinasEvilThoughts2, new Location(84840, -252392, -8320, 49152), 0);
					proccessComplete(qs);
				}
				else
				{
					htmltext = "readingdesk_q10294_0.htm";
				}
				break;
			
			case "teleport_to_guardian":
				if (((npc.getId() == YellowRelicWatcher) && (qs.getInt("yellow") != 0)) || ((npc.getId() == GreenRelicWatcher) && (qs.getInt("green") != 0)) || ((npc.getId() == BlueRelicWatcher) && (qs.getInt("blue") != 0)) || ((npc.getId() == RedRelicWatcher) && (qs.getInt("red") != 0)))
				{
					htmltext = "relicwatcher_q10294_4.htm";
				}
				else
				{
					player.teleToLocation(new Location(85937, -249618, -8320));
					teleportElcardia(player);
					return null;
				}
				break;
			
			case "take_test":
				if (npc.getId() == YellowRelicWatcher)
				{
					if (qs.getInt("yellow") != 0)
					{
						htmltext = "relicwatcher_q10294_yellowtest.htm";
					}
				}
				else if (npc.getId() == GreenRelicWatcher)
				{
					if (qs.getInt("green") != 0)
					{
						htmltext = "relicwatcher_q10294_greentest.htm";
					}
				}
				else if (npc.getId() == BlueRelicWatcher)
				{
					if (qs.getInt("blue") != 0)
					{
						htmltext = "relicwatcher_q10294_bluetest.htm";
					}
				}
				else if (npc.getId() == RedRelicWatcher)
				{
					if (qs.getInt("red") != 0)
					{
						htmltext = "relicwatcher_q10294_redtest.htm";
					}
				}
				break;
			
			case "false_answer":
				htmltext = "relicwatcher_q10294_falseanswer.htm";
				break;
			
			case "true_answer":
				player.teleToLocation(new Location(85937, -249618, -8320));
				teleportElcardia(player);
				return null;
				
			case "eris_q10294_9.htm":
				qs.addExpAndSp(25000000, 2500000);
				qs.setState(COMPLETED);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		if (event.startsWith("watcher_teleport"))
		{
			StringTokenizer tokenizer = new StringTokenizer(event);
			tokenizer.nextToken();
			Location loc = null;
			
			switch (Integer.parseInt(tokenizer.nextToken()))
			{
				case 1: // yellow
					if (qs.getInt("yellow") == 0)
					{
						loc = new Location(82434, -249546, -8320);
					}
					break;
				
				case 2: // green
					if (qs.getInt("green") == 0)
					{
						loc = new Location(88536, -249784, -8320);
					}
					break;
				
				case 3: // blue
					if (qs.getInt("blue") == 0)
					{
						loc = new Location(85672, -246872, -8320);
					}
					break;
				
				case 4: // red
					if (qs.getInt("red") == 0)
					{
						loc = new Location(85896, -252664, -8320);
					}
					break;
				
				default:
					break;
			}
			
			if (loc != null)
			{
				player.teleToLocation(loc);
				teleportElcardia(player);
				return null;
			}
			
			htmltext = "movingdevice_q10294_0.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		if (!player.isBaseClassActive())
		{
			return "no_subclass_allowed.htm";
		}
		
		if (npcId == Elcardia)
		{
			if (cond == 0)
			{
				final QuestState state = player.getQuestState(Q10293_SevenSignsForbiddenBookOfTheElmoredenKingdom.class);
				
				if ((player.getLevel() >= 81) && (state != null) && state.isCompleted())
				{
					htmltext = "elcardia_q10294_1.htm";
				}
				else
				{
					htmltext = "elcardia_q10294_0.htm";
					qs.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "elcardia_q10294_5.htm";
			}
		}
		else if (npcId == ErisEvilThoughts)
		{
			if (cond == 1)
			{
				htmltext = "eris_q10294_1.htm";
			}
			else if (cond == 2)
			{
				htmltext = "eris_q10294_4.htm";
			}
			else if (cond == 3)
			{
				htmltext = "eris_q10294_8.htm";
			}
		}
		else if (npcId == ElcardiaInzone1)
		{
			if ((cond == 1) || (cond == 2))
			{
				htmltext = "elcardia2_q10294_1.htm";
			}
		}
		else if (npcId == RelicGuard)
		{
			if ((cond == 2) && checkComplete(qs))
			{
				qs.setCond(3);
				htmltext = "relicguard_q10294_4.htm";
			}
			else if ((cond == 1) || (cond == 2))
			{
				htmltext = "relicguard_q10294_1.htm";
			}
			else if (cond == 3)
			{
				htmltext = "relicguard_q10294_5.htm";
			}
		}
		else if (Util.contains(RelicWatcher, npcId))
		{
			if (cond == 2)
			{
				htmltext = "relicwatcher_q10294_1.htm";
			}
		}
		else if (Util.contains(ReadingDesk, npcId))
		{
			if (cond == 2)
			{
				if (Util.contains(YellowRoomDesks, npcId))
				{
					if (npcId == YellowTrueReadingDesk)
					{
						htmltext = "readingdesk_q10294_yellowtrue.htm";
					}
					else
					{
						htmltext = "readingdesk_q10294_false.htm";
					}
				}
				else if (Util.contains(GreenRoomDesks, npcId))
				{
					if (npcId == GreenTrueReadingDesk)
					{
						htmltext = "readingdesk_q10294_greentrue.htm";
					}
					else
					{
						htmltext = "readingdesk_q10294_false.htm";
					}
				}
				else if (Util.contains(BlueRoomDesks, npcId))
				{
					if (npcId == BlueTrueReadingDesk)
					{
						htmltext = "readingdesk_q10294_bluetrue.htm";
					}
					else
					{
						htmltext = "readingdesk_q10294_false.htm";
					}
				}
				else if (Util.contains(RedRoomDesks, npcId))
				{
					if (npcId == RedTrueReadingDesk)
					{
						htmltext = "readingdesk_q10294_redtrue.htm";
					}
					else
					{
						htmltext = "readingdesk_q10294_false.htm";
					}
				}
			}
		}
		else if (npcId == JudevanEtinasEvilThoughts2)
		{
			htmltext = "jude_q10294_1.htm";
		}
		else if (npcId == SolinasEvilThoughts)
		{
			htmltext = "solina_q10294_1.htm";
		}
		
		return htmltext;
	}
	
	private void teleportElcardia(Player player)
	{
		for (NpcInstance n : player.getReflection().getNpcs())
		{
			if (n.getId() == ElcardiaInzone1)
			{
				n.teleToLocation(Location.findPointToStay(player, 100));
			}
		}
	}
	
	private boolean checkComplete(QuestState qs)
	{
		return (qs.getInt("yellow") != 0) && (qs.getInt("green") != 0) && (qs.getInt("blue") != 0) && (qs.getInt("red") != 0);
	}
	
	private void proccessComplete(QuestState qs)
	{
		if (checkComplete(qs))
		{
			qs.getPlayer().showQuestMovie(ExStartScenePlayer.SCENE_SSQ2_HOLY_BURIAL_GROUND_CLOSING);
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