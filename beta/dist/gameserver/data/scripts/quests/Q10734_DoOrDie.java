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
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author blacksmoke
 */
public class Q10734_DoOrDie extends Quest implements ScriptFile
{
	private static final int Katalin = 33943;
	private static final int Ayanthe = 33942;
	private static final int Adventurers_Guide = 33950;
	private static final int Training_Dummy = 19546;
	
	private final static int[] _warriorBuff =
	{
		15642, // Horn Melody (Adventurer)
		15643, // Drum Melody (Adventurer)
		15644, // Pipe Organ Melody (Adventurer)
		15645, // Guitar Melody (Adventurer)
		15646, // Harp Melody (Adventurer)
		15647, // Lute Melody (Adventurer)
		15651, // Prevailing Sonata (Adventurer)
		15652, // Daring Sonata (Adventurer)
		15653, // Refreshing Sonata (Adventurer)
		15649, // Warrior's Harmony (Adventurer)
		5182, // Blessing of Protection
	};
	private final static int[] _wizardBuff =
	{
		15642, // Horn Melody (Adventurer)
		15643, // Drum Melody (Adventurer)
		15644, // Pipe Organ Melody (Adventurer)
		15645, // Guitar Melody (Adventurer)
		15646, // Harp Melody (Adventurer)
		15647, // Lute Melody (Adventurer)
		15651, // Prevailing Sonata (Adventurer)
		15652, // Daring Sonata (Adventurer)
		15653, // Refreshing Sonata (Adventurer)
		15650, // Wizard's Harmony (Adventurer)
		5182, // Blessing of Protection
		
	};
	
	public Q10734_DoOrDie()
	{
		super(false);
		addStartNpc(Katalin, Ayanthe);
		addTalkId(Katalin, Ayanthe, Adventurers_Guide);
		addKillId(Training_Dummy);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10733_TheTestForSurvival.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		Player player = qs.getPlayer();
		List<Creature> target = new ArrayList<>();
		target.add(player);
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				if (qs.getPlayer().getClassId().getId() == 182) // Ertheia Fighter
				{
					htmltext = "33943-3.htm";
				}
				if (qs.getPlayer().getClassId().getId() == 183) // Ertheia Wizard
				{
					htmltext = "33942-3.htm";
				}
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ATTACK_THE_TRAINING_DUMMY, 4500, ScreenMessageAlign.TOP_CENTER));
				break;
			
			case "buffs_info":
				qs.showTutorialHTML(TutorialShowHtml.QT_002, TutorialShowHtml.TYPE_WINDOW);
				if (qs.getPlayer().getClassId().getId() == 182) // Ertheia Fighter
				{
					htmltext = "33950-3.htm";
				}
				else if (qs.getPlayer().getClassId().getId() == 183) // Ertheia Wizard
				{
					htmltext = "33950-5.htm";
				}
				break;
			
			case "buff":
				doSupportMagic(npc, player);
				qs.playSound(SOUND_MIDDLE);
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ATTACK_THE_TRAINING_DUMMY, 4500, ScreenMessageAlign.TOP_CENTER));
				qs.setCond(6);
				if (qs.getPlayer().getClassId().getId() == 182) // Ertheia Fighter
				{
					htmltext = "33950-4.htm";
				}
				else if (qs.getPlayer().getClassId().getId() == 183) // Ertheia Wizard
				{
					htmltext = "33950-6.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		int cond = qs.getCond();
		int npcId = npc.getId();
		boolean e_warrior = qs.getPlayer().getClassId().getId() == 182;
		boolean e_wizard = qs.getPlayer().getClassId().getId() == 183;
		
		String htmltext = null;
		if (qs.isCompleted())
		{
			return "quest_completed.htm";
		}
		
		switch (npcId)
		{
			case Katalin:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							if (e_warrior)
							{
								htmltext = "33943-1.htm";
							}
							else if (e_wizard)
							{
								htmltext = "Go To Ayanthe 33943-8.htm";
							}
						}
						break;
					
					case 1:
						if (e_warrior)
						{
							htmltext = "33943-4.htm";
						}
						break;
					
					case 3:
						htmltext = "33943-5.htm";
						qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.TALK_TO_THE_APPRENTICE_ADVENTURERS_GUIDE, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.setCond(5);
						qs.playSound(SOUND_MIDDLE);
						break;
					
					case 5:
						htmltext = "33943-6.htm";
						break;
					
					case 8:
						qs.giveItems(57, 7000);
						qs.getPlayer().addExpAndSp(805, 2);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(false);
						htmltext = "33943-7.htm";
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
			
			case Ayanthe:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							if (e_warrior)
							{
								htmltext = "Go To Katalin 33942-8.htm";
							}
							else if (e_wizard)
							{
								htmltext = "33942-1.htm";
							}
						}
						break;
					
					case 1:
						if (e_wizard)
						{
							htmltext = "33942-4.htm";
						}
						break;
					
					case 2:
						htmltext = "33942-5.htm";
						qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.TALK_TO_THE_APPRENTICE_ADVENTURERS_GUIDE, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.setCond(4);
						qs.playSound(SOUND_MIDDLE);
						break;
					
					case 4:
						htmltext = "33942-6.htm";
						break;
					
					case 7:
						qs.giveItems(57, 7000);
						qs.getPlayer().addExpAndSp(805, 2);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(false);
						htmltext = "33942-7.htm";
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
			
			case Adventurers_Guide:
				switch (cond)
				{
					case 0:
						htmltext = "33950-nc.htm";
						break;
					
					case 4:
					case 5:
						htmltext = "33950-1.htm";
						break;
					
					case 6:
						htmltext = "33950-4.htm";
						doSupportMagic(npc, qs.getPlayer());
						break;
					
					case 7:
						htmltext = "33950-7.htm";
						doSupportMagic(npc, qs.getPlayer());
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if (cond == 1)
		{
			qs.playSound(SOUND_MIDDLE);
			if (qs.getPlayer().getClassId().getId() == 182)
			{
				qs.setCond(3);
			}
			else if (qs.getPlayer().getClassId().getId() == 183)
			{
				qs.setCond(2);
			}
		}
		else if (cond == 6)
		{
			qs.playSound(SOUND_MIDDLE);
			if (qs.getPlayer().getClassId().getId() == 182)
			{
				qs.setCond(8);
			}
			else if (qs.getPlayer().getClassId().getId() == 183)
			{
				qs.setCond(7);
			}
		}
		
		return null;
	}
	
	private void doSupportMagic(NpcInstance npc, Player player)
	{
		List<Creature> target = new ArrayList<>();
		target.add(player);
		if (player.getClassId().getId() == 182) // Ertheia Fighter
		{
			for (int buff : _warriorBuff)
			{
				npc.broadcastPacket(new MagicSkillUse(npc, player, buff, 1, 0, 0));
				npc.callSkill(SkillTable.getInstance().getInfo(buff, 1), target, true);
			}
		}
		else if (player.getClassId().getId() == 183) // Ertheia Wizard
		{
			for (int buff : _wizardBuff)
			{
				npc.broadcastPacket(new MagicSkillUse(npc, player, buff, 1, 0, 0));
				npc.callSkill(SkillTable.getInstance().getInfo(buff, 1), target, true);
			}
		}
	}
	
	// Need to use this (quest need to be available only at one npc for warrior/wizard)
	public boolean checkStartNpc(NpcInstance npc, Player player)
	{
		int npcId = npc.getId();
		int classId = player.getClassId().getId();
		
		switch (npcId)
		{
			case Katalin:
				if (classId == 182)
				{
					return true;
				}
				return false;
				
			case Ayanthe:
				if (classId == 183)
				{
					return true;
				}
				return false;
		}
		
		return true;
	}
	
	public boolean checkTalkNpc(NpcInstance npc, QuestState st)
	{
		return checkStartNpc(npc, st.getPlayer());
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
