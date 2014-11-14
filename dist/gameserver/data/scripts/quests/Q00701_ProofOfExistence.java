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

public class Q00701_ProofOfExistence extends Quest implements ScriptFile
{
	// Npc
	private static final int Artius = 32559;
	// Items
	private static final int DeadmansRemains = 13875;
	private static final int BansheeQueensEye = 13876;
	// Monsters
	private static final int Enira = 25625;
	private static final int FloatingSkull1 = 22606;
	private static final int FloatingSkull2 = 22607;
	private static final int FloatingZombie1 = 22608;
	private static final int FloatingZombie2 = 22609;
	
	public Q00701_ProofOfExistence()
	{
		super(false);
		addStartNpc(Artius);
		addTalkId(Artius);
		addKillId(Enira, FloatingSkull1, FloatingSkull2, FloatingZombie1, FloatingZombie2);
		addQuestItem(DeadmansRemains, BansheeQueensEye);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "artius_q701_2.htm":
				if (qs.getCond() == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "ex_mons":
				if (qs.getCond() == 1)
				{
					if (qs.getQuestItemsCount(DeadmansRemains) >= 1)
					{
						qs.giveItems(ADENA_ID, qs.getQuestItemsCount(DeadmansRemains) * 2500);
						qs.takeItems(DeadmansRemains, -1);
						htmltext = "artius_q701_4.htm";
					}
					else
					{
						htmltext = "artius_q701_3a.htm";
					}
				}
				break;
			
			case "ex_boss":
				if (qs.getCond() == 1)
				{
					if (qs.getQuestItemsCount(BansheeQueensEye) >= 1)
					{
						qs.giveItems(ADENA_ID, qs.getQuestItemsCount(BansheeQueensEye) * 1000000);
						qs.takeItems(BansheeQueensEye, -1);
						htmltext = "artius_q701_4.htm";
					}
					else
					{
						htmltext = "artius_q701_3a.htm";
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
				if ((qs.getPlayer().getLevel() >= 78) && (GoodDayToFly != null) && GoodDayToFly.isCompleted())
				{
					htmltext = "artius_q701_1.htm";
				}
				else
				{
					htmltext = "artius_q701_0.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if ((qs.getQuestItemsCount(DeadmansRemains) >= 1) || (qs.getQuestItemsCount(BansheeQueensEye) >= 1))
				{
					htmltext = "artius_q701_3.htm";
				}
				else
				{
					htmltext = "artius_q701_3a.htm";
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
			if (npc.getId() == Enira)
			{
				qs.giveItems(BansheeQueensEye, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			else
			{
				qs.giveItems(DeadmansRemains, 1);
				qs.playSound(SOUND_ITEMGET);
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
