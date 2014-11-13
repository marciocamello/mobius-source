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

public class Q00238_SuccessFailureOfBusiness extends Quest implements ScriptFile
{
	// Npc
	private static final int Helvetica = 32641;
	// Monsters
	private static final int BrazierOfPurity = 18806;
	private static final int EvilSpirit = 22658;
	private static final int GuardianSpirit = 22659;
	// Items
	private static final int VicinityOfTheFieldOfSilenceResearchCenter = 14865;
	private static final int BrokenPieveOfMagicForce = 14867;
	private static final int GuardianSpiritFragment = 14868;
	
	public Q00238_SuccessFailureOfBusiness()
	{
		super(false);
		addStartNpc(Helvetica);
		addKillId(BrazierOfPurity, EvilSpirit, GuardianSpirit);
		addQuestItem(BrokenPieveOfMagicForce, GuardianSpiritFragment);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "32641-03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				break;
			
			case "32641-06.htm":
				qs.takeAllItems(BrokenPieveOfMagicForce);
				qs.setCond(3);
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
		final int cond = qs.getCond();
		final int id = qs.getState();
		
		if (id == CREATED)
		{
			if ((qs.getPlayer().getLevel() < 82) || !qs.getPlayer().isQuestCompleted(Q00237_WindsOfChange.class))
			{
				qs.exitCurrentQuest(true);
				htmltext = "32641-00.htm";
			}
			else if (qs.getQuestItemsCount(VicinityOfTheFieldOfSilenceResearchCenter) == 0)
			{
				htmltext = "32641-10.htm";
			}
			else
			{
				htmltext = "32641-01.htm";
			}
		}
		else if (id == COMPLETED)
		{
			htmltext = "32641-09.htm";
		}
		else if (cond == 1)
		{
			htmltext = "32641-04.htm";
		}
		else if (cond == 2)
		{
			htmltext = "32641-05.htm";
		}
		else if (cond == 3)
		{
			htmltext = "32641-07.htm";
		}
		else if (cond == 4)
		{
			qs.takeAllItems(VicinityOfTheFieldOfSilenceResearchCenter);
			qs.takeAllItems(GuardianSpiritFragment);
			qs.giveItems(ADENA_ID, 4498920);
			qs.addExpAndSp(21843270, 25080120);
			qs.setState(COMPLETED);
			qs.exitCurrentQuest(false);
			htmltext = "32641-08.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if ((cond == 1) && (npc.getId() == BrazierOfPurity))
		{
			qs.giveItems(BrokenPieveOfMagicForce, 1);
			
			if (qs.getQuestItemsCount(BrokenPieveOfMagicForce) >= 10)
			{
				qs.setCond(2);
			}
		}
		else if ((cond == 3) && ((npc.getId() == EvilSpirit) || (npc.getId() == GuardianSpirit)))
		{
			qs.giveItems(GuardianSpiritFragment, 1);
			
			if (qs.getQuestItemsCount(GuardianSpiritFragment) >= 20)
			{
				qs.setCond(4);
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
