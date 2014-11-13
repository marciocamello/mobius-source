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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00114_ResurrectionOfAnOldManager extends Quest implements ScriptFile
{
	// Npcs
	private static final int NEWYEAR = 31961;
	private static final int YUMI = 32041;
	private static final int STONES = 32046;
	private static final int WENDY = 32047;
	private static final int BOX = 32050;
	// Monster
	private static final int GUARDIAN = 27318;
	private NpcInstance GUARDIAN_SPAWN = null;
	// Items
	private static final int DETECTOR = 8090;
	private static final int DETECTOR2 = 8091;
	private static final int STARSTONE = 8287;
	private static final int LETTER = 8288;
	private static final int STARSTONE2 = 8289;
	
	public Q00114_ResurrectionOfAnOldManager()
	{
		super(false);
		addStartNpc(YUMI);
		addTalkId(WENDY, BOX, STONES, NEWYEAR);
		addFirstTalkId(STONES);
		addKillId(GUARDIAN);
		addQuestItem(DETECTOR, DETECTOR2, STARSTONE, LETTER, STARSTONE2);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		int choice;
		
		switch (event)
		{
			case "head_blacksmith_newyear_q0114_02.htm":
				qs.setCond(22);
				qs.takeItems(LETTER, 1);
				qs.giveItems(STARSTONE2, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "collecter_yumi_q0114_04.htm":
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				qs.set("talk", "0");
				break;
			
			case "collecter_yumi_q0114_08.htm":
				qs.set("talk", "1");
				break;
			
			case "collecter_yumi_q0114_09.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				qs.set("talk", "0");
				break;
			
			case "collecter_yumi_q0114_12.htm":
				choice = qs.getInt("choice");
				if (choice == 1)
				{
					htmltext = "collecter_yumi_q0114_12.htm";
				}
				else if (choice == 2)
				{
					htmltext = "collecter_yumi_q0114_13.htm";
				}
				else if (choice == 3)
				{
					htmltext = "collecter_yumi_q0114_14.htm";
				}
				break;
			
			case "collecter_yumi_q0114_15.htm":
				qs.set("talk", "1");
				break;
			
			case "collecter_yumi_q0114_23.htm":
				qs.set("talk", "2");
				break;
			
			case "collecter_yumi_q0114_26.htm":
				qs.setCond(6);
				qs.playSound(SOUND_MIDDLE);
				qs.set("talk", "0");
				break;
			
			case "collecter_yumi_q0114_31.htm":
				qs.setCond(17);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(DETECTOR, 1);
				break;
			
			case "collecter_yumi_q0114_34.htm":
				qs.takeItems(DETECTOR2, 1);
				qs.set("talk", "1");
				break;
			
			case "collecter_yumi_q0114_38.htm":
				choice = qs.getInt("choice");
				if (choice > 1)
				{
					htmltext = "collecter_yumi_q0114_37.htm";
				}
				break;
			
			case "collecter_yumi_q0114_40.htm":
				qs.setCond(21);
				qs.giveItems(LETTER, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "collecter_yumi_q0114_39.htm":
				qs.setCond(20);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "pavel_atlanta_q0114_03.htm":
				qs.setCond(19);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "pavel_atlanta_q0114_07.htm":
				qs.playSound(SOUND_FINISH);
				qs.addExpAndSp(2939190, 3059910);
				qs.exitCurrentQuest(false);
				break;
			
			case "chaos_secretary_wendy_q0114_01.htm":
				if ((qs.getInt("talk") + qs.getInt("talk1")) == 2)
				{
					htmltext = "chaos_secretary_wendy_q0114_05.htm";
				}
				else if ((qs.getInt("talk") + qs.getInt("talk1") + qs.getInt("talk2")) == 6)
				{
					htmltext = "chaos_secretary_wendy_q0114_06a.htm";
				}
				break;
			
			case "chaos_secretary_wendy_q0114_02.htm":
				if (qs.getInt("talk") == 0)
				{
					qs.set("talk", "1");
				}
				break;
			
			case "chaos_secretary_wendy_q0114_03.htm":
				if (qs.getInt("talk1") == 0)
				{
					qs.set("talk1", "1");
				}
				break;
			
			case "chaos_secretary_wendy_q0114_06.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				qs.set("talk", "0");
				qs.set("choice", "1");
				qs.unset("talk1");
				break;
			
			case "chaos_secretary_wendy_q0114_07.htm":
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				qs.set("talk", "0");
				qs.set("choice", "2");
				qs.unset("talk1");
				break;
			
			case "chaos_secretary_wendy_q0114_09.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				qs.set("talk", "0");
				qs.set("choice", "3");
				qs.unset("talk1");
				break;
			
			case "chaos_secretary_wendy_q0114_14ab.htm":
				qs.setCond(7);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "chaos_secretary_wendy_q0114_14b.htm":
				qs.setCond(10);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "chaos_secretary_wendy_q0114_12c.htm":
				if (qs.getInt("talk") == 0)
				{
					qs.set("talk", "1");
				}
				break;
			
			case "chaos_secretary_wendy_q0114_15b.htm":
				if ((GUARDIAN_SPAWN == null) || !qs.getPlayer().knowsObject(GUARDIAN_SPAWN) || !GUARDIAN_SPAWN.isVisible())
				{
					GUARDIAN_SPAWN = qs.addSpawn(GUARDIAN, 96977, -110625, -3280, 900000);
					Functions.npcSay(GUARDIAN_SPAWN, "You, " + qs.getPlayer().getName() + ", you attacked Wendy. Prepare to die!");
					GUARDIAN_SPAWN.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, qs.getPlayer(), 999);
				}
				else
				{
					htmltext = "chaos_secretary_wendy_q0114_17b.htm";
				}
				break;
			
			case "chaos_secretary_wendy_q0114_20b.htm":
				qs.setCond(12);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "chaos_secretary_wendy_q0114_17c.htm":
				qs.set("talk", "2");
				break;
			
			case "chaos_secretary_wendy_q0114_20c.htm":
				qs.setCond(13);
				qs.playSound(SOUND_MIDDLE);
				qs.set("talk", "0");
				break;
			
			case "chaos_secretary_wendy_q0114_23c.htm":
				qs.setCond(15);
				qs.playSound(SOUND_MIDDLE);
				qs.takeItems(STARSTONE, 1);
				break;
			
			case "chaos_secretary_wendy_q0114_16a.htm":
				qs.set("talk", "2");
				break;
			
			case "chaos_secretary_wendy_q0114_20a.htm":
				if (qs.getCond() == 7)
				{
					qs.setCond(8);
					qs.set("talk", "0");
					qs.playSound(SOUND_MIDDLE);
				}
				else if (qs.getCond() == 8)
				{
					qs.setCond(9);
					qs.playSound(SOUND_MIDDLE);
					htmltext = "chaos_secretary_wendy_q0114_21a.htm";
				}
				break;
			
			case "chaos_secretary_wendy_q0114_21a.htm":
				qs.setCond(9);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "chaos_secretary_wendy_q0114_29c.htm":
				qs.giveItems(STARSTONE2, 1);
				qs.takeItems(ADENA_ID, 3000);
				qs.setCond(26);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "chaos_box2_q0114_01r.htm":
				qs.playSound(SOUND_ARMOR_WOOD_3);
				qs.set("talk", "1");
				break;
			
			case "chaos_box2_q0114_03.htm":
				qs.setCond(14);
				qs.giveItems(STARSTONE, 1);
				qs.playSound(SOUND_MIDDLE);
				qs.set("talk", "0");
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(getName());
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (cond == 17)
		{
			qs.playSound(SOUND_MIDDLE);
			qs.takeItems(DETECTOR, 1);
			qs.giveItems(DETECTOR2, 1);
			qs.setCond(18);
			player.sendPacket(new ExShowScreenMessage(NpcString.THE_RADIO_SIGNAL_DETECTOR_IS_RESPONDING_A_SUSPICIOUS_PILE_OF_STONES_CATCHES_YOUR_EYE, 4500, ScreenMessageAlign.TOP_CENTER));
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		final int talk = qs.getInt("talk");
		final int talk1 = qs.getInt("talk1");
		
		switch (npcId)
		{
			case YUMI:
				if (qs.getState() == CREATED)
				{
					QuestState Pavel = qs.getPlayer().getQuestState(Q00121_PavelTheGiant.class);
					
					if (Pavel == null)
					{
						return "collecter_yumi_q0114_01.htm";
					}
					
					if ((qs.getPlayer().getLevel() >= 70) && (Pavel.getState() == COMPLETED))
					{
						return "collecter_yumi_q0114_02.htm";
					}
					qs.exitCurrentQuest(true);
					return "collecter_yumi_q0114_01.htm";
				}
				switch (cond)
				{
					case 1:
						if (talk == 0)
						{
							htmltext = "collecter_yumi_q0114_04.htm";
						}
						else
						{
							htmltext = "collecter_yumi_q0114_08.htm";
						}
						break;
					
					case 2:
						htmltext = "collecter_yumi_q0114_10.htm";
						break;
					
					case 3:
					case 4:
					case 5:
						if (talk == 0)
						{
							htmltext = "collecter_yumi_q0114_11.htm";
						}
						else if (talk == 1)
						{
							htmltext = "collecter_yumi_q0114_15.htm";
						}
						else
						{
							htmltext = "collecter_yumi_q0114_23.htm";
						}
						break;
					
					case 6:
						htmltext = "collecter_yumi_q0114_27.htm";
						break;
					
					case 9:
					case 12:
					case 16:
						htmltext = "collecter_yumi_q0114_28.htm";
						break;
					
					case 17:
						htmltext = "collecter_yumi_q0114_32.htm";
						break;
					
					case 19:
						if (talk == 0)
						{
							htmltext = "collecter_yumi_q0114_33.htm";
						}
						else
						{
							htmltext = "collecter_yumi_q0114_34.htm";
						}
						break;
					
					case 20:
						htmltext = "collecter_yumi_q0114_39.htm";
						break;
					
					case 21:
						htmltext = "collecter_yumi_q0114_40z.htm";
						break;
					
					case 22:
					case 26:
						htmltext = "collecter_yumi_q0114_41.htm";
						qs.setCond(27);
						qs.playSound(SOUND_MIDDLE);
						break;
					
					case 27:
						htmltext = "collecter_yumi_q0114_42.htm";
						break;
				}
				break;
			
			case WENDY:
				switch (cond)
				{
					case 2:
						if ((talk + talk1) < 2)
						{
							htmltext = "chaos_secretary_wendy_q0114_01.htm";
						}
						else if ((talk + talk1) == 2)
						{
							htmltext = "chaos_secretary_wendy_q0114_05.htm";
						}
						break;
					
					case 3:
						htmltext = "chaos_secretary_wendy_q0114_06b.htm";
						break;
					
					case 4:
					case 5:
						htmltext = "chaos_secretary_wendy_q0114_08.htm";
						break;
					
					case 6:
						int choice = qs.getInt("choice");
						if (choice == 1)
						{
							if (talk == 0)
							{
								htmltext = "chaos_secretary_wendy_q0114_11a.htm";
							}
							else if (talk == 1)
							{
								htmltext = "chaos_secretary_wendy_q0114_17c.htm";
							}
							else
							{
								htmltext = "chaos_secretary_wendy_q0114_16a.htm";
							}
						}
						else if (choice == 2)
						{
							htmltext = "chaos_secretary_wendy_q0114_11b.htm";
						}
						else if (choice == 3)
						{
							if (talk == 0)
							{
								htmltext = "chaos_secretary_wendy_q0114_11c.htm";
							}
							else if (talk == 1)
							{
								htmltext = "chaos_secretary_wendy_q0114_12c.htm";
							}
							else
							{
								htmltext = "chaos_secretary_wendy_q0114_17c.htm";
							}
						}
						break;
					
					case 7:
						if (talk == 0)
						{
							htmltext = "chaos_secretary_wendy_q0114_11c.htm";
						}
						else if (talk == 1)
						{
							htmltext = "chaos_secretary_wendy_q0114_12c.htm";
						}
						else
						{
							htmltext = "chaos_secretary_wendy_q0114_17c.htm";
						}
						break;
					
					case 8:
						htmltext = "chaos_secretary_wendy_q0114_16a.htm";
						break;
					
					case 9:
						htmltext = "chaos_secretary_wendy_q0114_25c.htm";
						break;
					
					case 10:
						htmltext = "chaos_secretary_wendy_q0114_18b.htm";
						break;
					
					case 11:
						htmltext = "chaos_secretary_wendy_q0114_19b.htm";
						break;
					
					case 12:
						htmltext = "chaos_secretary_wendy_q0114_25c.htm";
						break;
					
					case 13:
						htmltext = "chaos_secretary_wendy_q0114_20c.htm";
						break;
					
					case 14:
						htmltext = "chaos_secretary_wendy_q0114_22c.htm";
						break;
					
					case 15:
						htmltext = "chaos_secretary_wendy_q0114_24c.htm";
						qs.setCond(16);
						qs.playSound(SOUND_MIDDLE);
						break;
					
					case 16:
						htmltext = "chaos_secretary_wendy_q0114_25c.htm";
						break;
					
					case 20:
						htmltext = "chaos_secretary_wendy_q0114_26c.htm";
						break;
					
					case 26:
						htmltext = "chaos_secretary_wendy_q0114_32c.htm";
						break;
				}
				break;
			
			case BOX:
				if (cond == 13)
				{
					if (talk == 0)
					{
						htmltext = "chaos_box2_q0114_01.htm";
					}
					else
					{
						htmltext = "chaos_box2_q0114_02.htm";
					}
				}
				else if (cond == 14)
				{
					htmltext = "chaos_box2_q0114_04.htm";
				}
				break;
			
			case STONES:
				if (cond == 18)
				{
					htmltext = "pavel_atlanta_q0114_02.htm";
				}
				else if (cond == 19)
				{
					htmltext = "pavel_atlanta_q0114_03.htm";
				}
				else if (cond == 27)
				{
					htmltext = "pavel_atlanta_q0114_04.htm";
				}
				break;
			
			case NEWYEAR:
				if (cond == 21)
				{
					htmltext = "head_blacksmith_newyear_q0114_01.htm";
				}
				else if (cond == 22)
				{
					htmltext = "head_blacksmith_newyear_q0114_03.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 10)
		{
			Functions.npcSay(npc, "This enemy is far too powerful for me to fight. I must withdraw");
			qs.setCond(11);
			qs.playSound(SOUND_MIDDLE);
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
