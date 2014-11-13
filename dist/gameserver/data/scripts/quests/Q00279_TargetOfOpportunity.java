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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00279_TargetOfOpportunity extends Quest implements ScriptFile
{
	// Npc
	private static final int Jerian = 32302;
	// Monsters
	private static final int CosmicScout = 22373;
	private static final int CosmicWatcher = 22374;
	private static final int CosmicPriest = 22375;
	private static final int CosmicLord = 22376;
	// Items
	private static final int SealComponentsPart1 = 15517;
	private static final int SealComponentsPart2 = 15518;
	private static final int SealComponentsPart3 = 15519;
	private static final int SealComponentsPart4 = 15520;
	
	public Q00279_TargetOfOpportunity()
	{
		super(PARTY_ALL);
		addStartNpc(Jerian);
		addKillId(CosmicScout, CosmicWatcher, CosmicPriest, CosmicLord);
		addQuestItem(SealComponentsPart1, SealComponentsPart2, SealComponentsPart3, SealComponentsPart4);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "jerian_q279_04.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "jerian_q279_07.htm":
				qs.takeAllItems(SealComponentsPart1, SealComponentsPart2, SealComponentsPart3, SealComponentsPart4);
				qs.giveItems(15515, 1);
				qs.giveItems(15516, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
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
		
		switch (qs.getCond())
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 82)
				{
					htmltext = "jerian_q279_01.htm";
				}
				else
				{
					htmltext = "jerian_q279_00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "jerian_q279_05.htm";
				break;
			
			case 2:
				htmltext = "jerian_q279_06.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Rnd.chance(15))
		{
			switch (npc.getId())
			{
				case CosmicScout:
					if (qs.getQuestItemsCount(SealComponentsPart1) < 1)
					{
						qs.giveItems(SealComponentsPart1, 1);
					}
					break;
				
				case CosmicWatcher:
					if (qs.getQuestItemsCount(SealComponentsPart2) < 1)
					{
						qs.giveItems(SealComponentsPart2, 1);
					}
					break;
				
				case CosmicPriest:
					if (qs.getQuestItemsCount(SealComponentsPart3) < 1)
					{
						qs.giveItems(SealComponentsPart3, 1);
					}
					break;
				
				case CosmicLord:
					if (qs.getQuestItemsCount(SealComponentsPart4) < 1)
					{
						qs.giveItems(SealComponentsPart4, 1);
					}
					break;
			}
			
			if ((qs.getQuestItemsCount(SealComponentsPart1) >= 1) && (qs.getQuestItemsCount(SealComponentsPart2) >= 1) && (qs.getQuestItemsCount(SealComponentsPart3) >= 1) && (qs.getQuestItemsCount(SealComponentsPart4) >= 1))
			{
				qs.setCond(2);
			}
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
