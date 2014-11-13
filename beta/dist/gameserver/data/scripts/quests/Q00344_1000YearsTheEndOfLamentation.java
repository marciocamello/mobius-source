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

public class Q00344_1000YearsTheEndOfLamentation extends Quest implements ScriptFile
{
	private static final int ARTICLES_DEAD_HEROES = 4269;
	private static final int OLD_KEY = 4270;
	private static final int OLD_HILT = 4271;
	private static final int OLD_TOTEM = 4272;
	private static final int CRUCIFIX = 4273;
	private static final int CHANCE = 36;
	private static final int SPECIAL = 1000;
	private static final int GILMORE = 30754;
	private static final int RODEMAI = 30756;
	private static final int ORVEN = 30857;
	private static final int KAIEN = 30623;
	private static final int GARVARENTZ = 30704;
	
	public Q00344_1000YearsTheEndOfLamentation()
	{
		super(true);
		addStartNpc(GILMORE);
		addTalkId(RODEMAI, ORVEN, GARVARENTZ, KAIEN);
		addQuestItem(ARTICLES_DEAD_HEROES, OLD_KEY, OLD_HILT, OLD_TOTEM, CRUCIFIX);
		for (int mob = 20236; mob < 20241; mob++)
		{
			addKillId(mob);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final long amount = qs.getQuestItemsCount(ARTICLES_DEAD_HEROES);
		final int cond = qs.getCond();
		final int level = qs.getPlayer().getLevel();
		
		switch (event)
		{
			case "30754-04.htm":
				if ((level >= 48) && (cond == 0))
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.playSound(SOUND_ACCEPT);
				}
				else
				{
					htmltext = "noquest";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "30754-08.htm":
				qs.exitCurrentQuest(true);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "30754-06.htm":
				if (cond == 1)
				{
					if (amount == 0)
					{
						htmltext = "30754-06a.htm";
					}
					else
					{
						if (Rnd.get((int) (SPECIAL / qs.getRateQuestsReward())) >= amount)
						{
							qs.giveItems(ADENA_ID, amount * 60);
						}
						else
						{
							htmltext = "30754-10.htm";
							qs.set("ok", "1");
							qs.set("amount", str(amount));
						}
						
						qs.takeItems(ARTICLES_DEAD_HEROES, -1);
					}
				}
				break;
			
			case "30754-11.htm":
				if (cond == 1)
				{
					if (qs.getInt("ok") != 1)
					{
						htmltext = "noquest";
					}
					else
					{
						int random = Rnd.get(100);
						qs.setCond(2);
						qs.unset("ok");
						
						if (random < 25)
						{
							htmltext = "30754-12.htm";
							qs.giveItems(OLD_KEY, 1);
						}
						else if (random < 50)
						{
							htmltext = "30754-13.htm";
							qs.giveItems(OLD_HILT, 1);
						}
						else if (random < 75)
						{
							htmltext = "30754-14.htm";
							qs.giveItems(OLD_TOTEM, 1);
						}
						else
						{
							qs.giveItems(CRUCIFIX, 1);
						}
					}
				}
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
		final int id = qs.getState();
		final long amount = qs.getQuestItemsCount(ARTICLES_DEAD_HEROES);
		
		if (id == CREATED)
		{
			if (qs.getPlayer().getLevel() >= 48)
			{
				htmltext = "30754-02.htm";
			}
			else
			{
				htmltext = "30754-01.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if ((npcId == GILMORE) && (cond == 1))
		{
			if (amount > 0)
			{
				htmltext = "30754-05.htm";
			}
			else
			{
				htmltext = "30754-09.htm";
			}
		}
		else if (cond == 2)
		{
			if (npcId == GILMORE)
			{
				htmltext = "30754-15.htm";
			}
			else if (rewards(qs, npcId))
			{
				htmltext = str(npcId) + "-01.htm";
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
			}
		}
		else if (cond == 3)
		{
			if (npcId == GILMORE)
			{
				int amt = qs.getInt("amount");
				int mission = qs.getInt("mission");
				int bonus = 0;
				
				if (mission == 1)
				{
					bonus = 1500;
				}
				else if (mission == 2)
				{
					qs.giveItems(4044, 1);
				}
				else if (mission == 3)
				{
					qs.giveItems(4043, 1);
				}
				else if (mission == 4)
				{
					qs.giveItems(4042, 1);
				}
				
				if (amt > 0)
				{
					qs.unset("amount");
					qs.giveItems(ADENA_ID, (amt * 50) + bonus, true);
				}
				
				htmltext = "30754-16.htm";
				qs.setCond(1);
				qs.unset("mission");
			}
			else
			{
				htmltext = str(npcId) + "-02.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.rollAndGive(ARTICLES_DEAD_HEROES, 1, CHANCE + ((npc.getId() - 20234) * 2));
		}
		
		return null;
	}
	
	private boolean rewards(QuestState qs, int npcId)
	{
		boolean state = false;
		final int chance = Rnd.get(100);
		
		switch (npcId)
		{
			case ORVEN:
				if (qs.getQuestItemsCount(CRUCIFIX) > 0)
				{
					qs.set("mission", "1");
					qs.takeItems(CRUCIFIX, -1);
					state = true;
					
					if (chance < 50)
					{
						qs.giveItems(1875, 19);
					}
					else if (chance < 70)
					{
						qs.giveItems(952, 5);
					}
					else
					{
						qs.giveItems(2437, 1);
					}
				}
				break;
			
			case GARVARENTZ:
				if (qs.getQuestItemsCount(OLD_TOTEM) > 0)
				{
					qs.set("mission", "2");
					qs.takeItems(OLD_TOTEM, -1);
					state = true;
					
					if (chance < 45)
					{
						qs.giveItems(1882, 70);
					}
					else if (chance < 95)
					{
						qs.giveItems(1881, 50);
					}
					else
					{
						qs.giveItems(191, 1);
					}
				}
				break;
			
			case KAIEN:
				if (qs.getQuestItemsCount(OLD_HILT) > 0)
				{
					qs.set("mission", "3");
					qs.takeItems(OLD_HILT, -1);
					state = true;
					
					if (chance < 50)
					{
						qs.giveItems(1874, 25);
					}
					else if (chance < 75)
					{
						qs.giveItems(1887, 10);
					}
					else if (chance < 99)
					{
						qs.giveItems(951, 1);
					}
					else
					{
						qs.giveItems(133, 1);
					}
				}
				break;
			
			case RODEMAI:
				if (qs.getQuestItemsCount(OLD_KEY) > 0)
				{
					qs.set("mission", "4");
					qs.takeItems(OLD_KEY, -1);
					state = true;
					
					if (chance < 40)
					{
						qs.giveItems(1879, 55);
					}
					else if (chance < 90)
					{
						qs.giveItems(951, 1);
					}
					else
					{
						qs.giveItems(885, 1);
					}
				}
				break;
		}
		
		return state;
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
