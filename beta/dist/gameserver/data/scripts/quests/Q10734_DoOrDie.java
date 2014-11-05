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
	
	public Q10734_DoOrDie()
	{
		super(false);
		addStartNpc(Katalin);
		addTalkId(Katalin, Ayanthe, Adventurers_Guide);
		addKillId(Training_Dummy);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10733_TheTestForSurvival.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		List<Creature> target = new ArrayList<>();
		target.add(player);
		
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "33943-3.htm";
			st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ATTACK_THE_TRAINING_DUMMY, 4500, ScreenMessageAlign.TOP_CENTER));
			
		}
		if (event.equalsIgnoreCase("buffs_info"))
		{
			// TODO Adventurers' Guide
			st.showTutorialHTML(TutorialShowHtml.QT_002, TutorialShowHtml.TYPE_WINDOW);
			htmltext = "33950-3.htm";
		}
		if (event.equalsIgnoreCase("buff"))
		{
			doSupportMagic(npc, player);
			htmltext = "33950-4.htm";
			st.setCond(6);
			st.playSound(SOUND_MIDDLE);
			st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ATTACK_THE_TRAINING_DUMMY, 4500, ScreenMessageAlign.TOP_CENTER));
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getId();
		String htmltext = "noquest";
		
		if (npcId == Katalin)
		{
			if (st.isCompleted())
			{
				htmltext = "quest_completed.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "33943-1.htm";
			}
			else if (cond == 1)
			{
				htmltext = "33943-4.htm";
			}
			else if (cond == 3)
			{
				htmltext = "33943-5.htm";
				st.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.TALK_TO_THE_APPRENTICE_ADVENTURERS_GUIDE, 4500, ScreenMessageAlign.TOP_CENTER));
				st.setCond(5);
				st.playSound(SOUND_MIDDLE);
			}
			else if (cond == 5)
			{
				htmltext = "33943-6.htm";
			}
			else if (cond == 8)
			{
				st.giveItems(57, 7000);
				st.getPlayer().addExpAndSp(805, 2);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
				htmltext = "33943-7.htm";
			}
			else
			{
				htmltext = "noqu.htm";
			}
		}
		else if (npcId == Ayanthe)
		{
			// TODO
		}
		else if (npcId == Adventurers_Guide)
		{
			if (st.isCompleted())
			{
				htmltext = "quest_completed.htm";
			}
			else if (cond == 0)
			{
				htmltext = "33950-nc.htm";
			}
			else if (cond == 5)
			{
				htmltext = "33950-1.htm";
			}
			else if (cond == 6)
			{
				htmltext = "33950-4.htm";
				doSupportMagic(npc, st.getPlayer());
			}
			else
			{
				htmltext = "noqu.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getId();
		
		if ((st.getCond() == 1) && (npcId == Training_Dummy))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(3);
		}
		else if ((st.getCond() == 6) && (npcId == Training_Dummy))
		{
			st.setCond(8);
			st.playSound(SOUND_MIDDLE);
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
}
