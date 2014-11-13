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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author GM-Exid
 */
public class Q10390_KekropusLetter extends Quest implements ScriptFile
{
	// Npcs
	private static final int RAYMOND = 30289;
	private static final int BATHIS = 30332;
	private static final int GOSTA = 30916;
	private static final int ELI = 33858;
	// Items
	private static final int LETTER_OF_KEKROPUS = 36706;
	private static final int TELEPORT_HIENE = 37112;
	private static final int TELEPORT_ALLIGATOR_ISLAND = 37025;
	private static final int WEAPON_ENCHANT_C = 951;
	private static final int STEEL_COINS = 37045;
	
	public Q10390_KekropusLetter()
	{
		super(false);
		addStartNpc(RAYMOND);
		addTalkId(BATHIS);
		addTalkId(GOSTA);
		addTalkId(ELI);
		addQuestItem(LETTER_OF_KEKROPUS);
		addLevelCheck(40, 45);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30289-03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound("ItemSound.quest_accept");
				break;
			
			case "30332-02.htm":
				qs.setCond(2);
				qs.giveItems(LETTER_OF_KEKROPUS, 1, true);
				htmltext = "30332-03.htm";
				break;
			
			case "30332-05.htm":
				qs.setCond(3);
				qs.takeItems(LETTER_OF_KEKROPUS, -1);
				qs.giveItems(TELEPORT_HIENE, 1, true);
				break;
			
			case "30916-03.htm":
				qs.setCond(4);
				qs.giveItems(TELEPORT_ALLIGATOR_ISLAND, 1, true);
				break;
			
			case "33858-02.htm":
				qs.giveItems(WEAPON_ENCHANT_C, 3, true);
				qs.giveItems(STEEL_COINS, 10, true);
				qs.addExpAndSp(370440, 88);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		
		switch (npcId)
		{
			case RAYMOND:
				if ((cond == 0) && (qs.getPlayer().getLevel() >= 40) && (qs.getPlayer().getLevel() <= 45))
				{
					htmltext = "30289-01.htm";
				}
				else if (cond == 1)
				{
					htmltext = "30289-03.htm";
				}
				break;
			
			case BATHIS:
				if (cond == 1)
				{
					htmltext = "30332-01.htm";
				}
				else if ((cond == 2) || (cond == 3))
				{
					htmltext = "30332-05.htm";
				}
				break;
			
			case GOSTA:
				if (cond == 3)
				{
					htmltext = "30916-01.htm";
				}
				else if (cond == 4)
				{
					htmltext = "30916-03.htm";
				}
				break;
			
			case ELI:
				if (cond == 4)
				{
					htmltext = "33858-01.htm";
				}
				break;
		}
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