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

public class Q00239_WontYouJoinUs extends Quest implements ScriptFile
{
	// Npc
	private static final int Athenia = 32643;
	// Monsters
	private static final int WasteLandfillMachine = 18805;
	private static final int Suppressor = 22656;
	private static final int Exterminator = 22657;
	// Items
	private static final int CertificateOfSupport = 14866;
	private static final int DestroyedMachinePiece = 14869;
	private static final int EnchantedGolemFragment = 14870;
	
	public Q00239_WontYouJoinUs()
	{
		super(false);
		addStartNpc(Athenia);
		addKillId(WasteLandfillMachine, Suppressor, Exterminator);
		addQuestItem(DestroyedMachinePiece, EnchantedGolemFragment);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "32643-03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				break;
			
			case "32643-07.htm":
				qs.takeAllItems(DestroyedMachinePiece);
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
				return "32643-00.htm";
			}
			
			if (qs.getQuestItemsCount(CertificateOfSupport) == 0)
			{
				return "32643-12.htm";
			}
			
			return "32643-01.htm";
		}
		else if (id == COMPLETED)
		{
			return "32643-11.htm";
		}
		else if (cond == 1)
		{
			return "32643-04.htm";
		}
		else if (cond == 2)
		{
			return "32643-06.htm";
		}
		else if (cond == 3)
		{
			return "32643-08.htm";
		}
		else if (cond == 4)
		{
			qs.takeAllItems(CertificateOfSupport);
			qs.takeAllItems(EnchantedGolemFragment);
			qs.giveItems(ADENA_ID, 4498920);
			qs.addExpAndSp(21843270, 25080120);
			qs.setState(COMPLETED);
			qs.exitCurrentQuest(false);
			return "32643-10.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if ((cond == 1) && (npc.getId() == WasteLandfillMachine))
		{
			qs.giveItems(DestroyedMachinePiece, 1);
			
			if (qs.getQuestItemsCount(DestroyedMachinePiece) >= 10)
			{
				qs.setCond(2);
			}
		}
		else if ((cond == 3) && ((npc.getId() == Suppressor) || (npc.getId() == Exterminator)))
		{
			qs.giveItems(EnchantedGolemFragment, 1);
			
			if (qs.getQuestItemsCount(EnchantedGolemFragment) >= 20)
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
