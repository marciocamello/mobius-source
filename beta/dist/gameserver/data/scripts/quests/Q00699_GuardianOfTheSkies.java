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

public class Q00699_GuardianOfTheSkies extends Quest implements ScriptFile
{
	// Npc
	private static final int Lekon = 32557;
	// Monsters
	private static final int VultureRider1 = 22614;
	private static final int VultureRider2 = 22615;
	private static final int EliteRider = 25633;
	private static final int Valdstone = 25623;
	// Item
	private static final int VulturesGoldenFeather = 13871;
	// Other
	private static final int FeatherPrice = 5000;
	
	public Q00699_GuardianOfTheSkies()
	{
		super(false);
		addStartNpc(Lekon);
		addTalkId(Lekon);
		addKillId(VultureRider1, VultureRider2, EliteRider, Valdstone);
		addQuestItem(VulturesGoldenFeather);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "lekon_q699_2.htm":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "ex_feathers":
				if (cond == 1)
				{
					if (qs.getQuestItemsCount(VulturesGoldenFeather) >= 1)
					{
						qs.giveItems(ADENA_ID, qs.getQuestItemsCount(VulturesGoldenFeather) * FeatherPrice);
						qs.takeItems(VulturesGoldenFeather, -1);
						htmltext = "lekon_q699_4.htm";
					}
					else
					{
						htmltext = "lekon_q699_3a.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		switch (qs.getCond())
		{
			case 0:
				final QuestState GoodDayToFly = qs.getPlayer().getQuestState(Q10273_GoodDayToFly.class);
				
				if ((qs.getPlayer().getLevel() >= 75) && (GoodDayToFly != null) && GoodDayToFly.isCompleted())
				{
					htmltext = "lekon_q699_1.htm";
				}
				else
				{
					htmltext = "lekon_q699_0.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if (qs.getQuestItemsCount(VulturesGoldenFeather) >= 1)
				{
					htmltext = "lekon_q699_3.htm";
				}
				else
				{
					htmltext = "lekon_q699_3a.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			switch (npc.getId())
			{
				case VultureRider1:
				case VultureRider2:
				case EliteRider:
					qs.giveItems(VulturesGoldenFeather, 1);
					qs.playSound(SOUND_ITEMGET);
					break;
				
				case Valdstone:
					qs.giveItems(VulturesGoldenFeather, 50);
					qs.playSound(SOUND_ITEMGET);
					break;
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
