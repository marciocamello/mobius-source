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

public class Q00040_ASpecialOrder extends Quest implements ScriptFile
{
	// Npcs
	private static final int Helvetia = 30081;
	private static final int OFulle = 31572;
	private static final int Gesto = 30511;
	// Items
	private static final int FatOrangeFish = 6452;
	private static final int NimbleOrangeFish = 6450;
	private static final int OrangeUglyFish = 6451;
	private static final int GoldenCobol = 5079;
	private static final int ThornCobol = 5082;
	private static final int GreatCobol = 5084;
	private static final int FishChest = 12764;
	private static final int SeedJar = 12765;
	private static final int WondrousCubic = 10632;
	
	public Q00040_ASpecialOrder()
	{
		super(false);
		addStartNpc(Helvetia);
		addTalkId(Helvetia, OFulle, Gesto);
		addQuestItem(FishChest, SeedJar);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "take":
				if (Rnd.get(1, 2) == 1)
				{
					qs.setCond(2);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
					htmltext = "Helvetia-gave-ofulle.htm";
				}
				else
				{
					qs.setCond(5);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
					htmltext = "Helvetia-gave-gesto.htm";
				}
				break;
			
			case "6":
				qs.setCond(6);
				htmltext = "Gesto-3.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Helvetia:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() >= 40)
						{
							htmltext = "Helvetia-1.htm";
						}
						else
						{
							htmltext = "Helvetia-level.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 2:
					case 3:
					case 5:
					case 6:
						htmltext = "Helvetia-whereismyfish.htm";
						break;
					
					case 4:
						qs.takeAllItems(FishChest);
						qs.giveItems(WondrousCubic, 1, false);
						qs.exitCurrentQuest(false);
						htmltext = "Helvetia-finish.htm";
						break;
					
					case 7:
						qs.takeAllItems(SeedJar);
						qs.giveItems(WondrousCubic, 1, false);
						qs.exitCurrentQuest(false);
						htmltext = "Helvetia-finish.htm";
						break;
				}
				break;
			
			case OFulle:
				switch (cond)
				{
					case 2:
						htmltext = "OFulle-1.htm";
						qs.setCond(3);
						break;
					
					case 3:
						if ((qs.getQuestItemsCount(FatOrangeFish) >= 10) && (qs.getQuestItemsCount(NimbleOrangeFish) >= 10) && (qs.getQuestItemsCount(OrangeUglyFish) >= 10))
						{
							qs.takeItems(FatOrangeFish, 10);
							qs.takeItems(NimbleOrangeFish, 10);
							qs.takeItems(OrangeUglyFish, 10);
							qs.giveItems(FishChest, 1, false);
							qs.setCond(4);
							htmltext = "OFulle-2.htm";
						}
						else
						{
							htmltext = "OFulle-1.htm";
						}
						break;
					
					case 5:
					case 6:
						htmltext = "OFulle-3.htm";
						break;
				}
				break;
			
			case Gesto:
				switch (cond)
				{
					case 5:
						htmltext = "Gesto-1.htm";
						break;
					
					case 6:
						if ((qs.getQuestItemsCount(GoldenCobol) >= 40) && (qs.getQuestItemsCount(ThornCobol) >= 40) && (qs.getQuestItemsCount(GreatCobol) >= 40))
						{
							qs.takeItems(GoldenCobol, 40);
							qs.takeItems(ThornCobol, 40);
							qs.takeItems(GreatCobol, 40);
							qs.giveItems(SeedJar, 1, false);
							qs.setCond(7);
							htmltext = "Gesto-4.htm";
						}
						else
						{
							htmltext = "Gesto-5.htm";
						}
						break;
					
					case 7:
						htmltext = "Gesto-6.htm";
						break;
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
