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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

public class Q10275_ContainingTheAttributePower extends Quest implements ScriptFile
{
	// Npcs
	private final static int Holly = 30839;
	private final static int Weber = 31307;
	private final static int Yin = 32325;
	private final static int Yang = 32326;
	// Monsters
	private final static int Water = 27380;
	private final static int Air = 27381;
	// Items
	private final static int YinSword = 13845;
	private final static int YangSword = 13881;
	private final static int SoulPieceWater = 13861;
	private final static int SoulPieceAir = 13862;
	
	public Q10275_ContainingTheAttributePower()
	{
		super(false);
		addStartNpc(Holly, Weber);
		addTalkId(Yin, Yang);
		addKillId(Air, Water);
		addQuestItem(YinSword, YangSword, SoulPieceWater, SoulPieceAir);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "30839-02.htm":
			case "31307-02.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30839-05.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "31307-05.htm":
				qs.setCond(7);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32325-03.htm":
				qs.setCond(3);
				qs.giveItems(YinSword, 1, Element.FIRE, 10);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32326-03.htm":
				qs.setCond(8);
				qs.giveItems(YangSword, 1, Element.EARTH, 10);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32325-06.htm":
				if (qs.getQuestItemsCount(YinSword) > 0)
				{
					qs.takeItems(YinSword, 1);
					htmltext = "32325-07.htm";
				}
				qs.giveItems(YinSword, 1, Element.FIRE, 10);
				break;
			
			case "32326-06.htm":
				if (qs.getQuestItemsCount(YangSword) > 0)
				{
					qs.takeItems(YangSword, 1);
					htmltext = "32326-07.htm";
				}
				qs.giveItems(YangSword, 1, Element.EARTH, 10);
				break;
			
			case "32325-09.htm":
				qs.setCond(5);
				SkillTable.getInstance().getInfo(2635, 1).getEffects(player, player, false, false);
				qs.giveItems(YinSword, 1, Element.FIRE, 10);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32326-09.htm":
				qs.setCond(10);
				SkillTable.getInstance().getInfo(2636, 1).getEffects(player, player, false, false);
				qs.giveItems(YangSword, 1, Element.EARTH, 10);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			default:
				int item = 0;
				switch (event)
				{
					case "1":
						item = 10521;
						break;
					
					case "2":
						item = 10522;
						break;
					
					case "3":
						item = 10523;
						break;
					
					case "4":
						item = 10524;
						break;
					
					case "5":
						item = 10525;
						break;
					
					case "6":
						item = 10526;
						break;
				}
				if (item > 0)
				{
					qs.giveItems(item, 2, true);
					qs.addExpAndSp(10000000, 11200000);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
					
					if (npc != null)
					{
						htmltext = str(npc.getId()) + "-1" + event + ".htm";
					}
					else
					{
						htmltext = null;
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
		final int id = qs.getState();
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (id == COMPLETED)
		{
			if (npcId == Holly)
			{
				htmltext = "30839-0a.htm";
			}
			else if (npcId == Weber)
			{
				htmltext = "31307-0a.htm";
			}
		}
		else if (id == CREATED)
		{
			if (qs.getPlayer().getLevel() >= 76)
			{
				if (npcId == Holly)
				{
					htmltext = "30839-01.htm";
				}
				else
				{
					htmltext = "31307-01.htm";
				}
			}
			else if (npcId == Holly)
			{
				htmltext = "30839-00.htm";
			}
			else
			{
				htmltext = "31307-00.htm";
			}
		}
		else if (npcId == Holly)
		{
			if (cond == 1)
			{
				htmltext = "30839-03.htm";
			}
			else if (cond == 2)
			{
				htmltext = "30839-05.htm";
			}
		}
		else if (npcId == Weber)
		{
			if (cond == 1)
			{
				htmltext = "31307-03.htm";
			}
			else if (cond == 7)
			{
				htmltext = "31307-05.htm";
			}
		}
		else if (npcId == Yin)
		{
			if (cond == 2)
			{
				htmltext = "32325-01.htm";
			}
			else if ((cond == 3) || (cond == 5))
			{
				htmltext = "32325-04.htm";
			}
			else if (cond == 4)
			{
				htmltext = "32325-08.htm";
				qs.takeItems(YinSword, 1);
				qs.takeItems(SoulPieceWater, -1);
			}
			else if (cond == 6)
			{
				htmltext = "32325-10.htm";
			}
		}
		else if (npcId == Yang)
		{
			if (cond == 7)
			{
				htmltext = "32326-01.htm";
			}
			else if ((cond == 8) || (cond == 10))
			{
				htmltext = "32326-04.htm";
			}
			else if (cond == 9)
			{
				htmltext = "32326-08.htm";
				qs.takeItems(YangSword, 1);
				qs.takeItems(SoulPieceAir, -1);
			}
			else if (cond == 11)
			{
				htmltext = "32326-10.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Air:
				if ((qs.getItemEquipped(Inventory.PAPERDOLL_RHAND) == YangSword) && ((cond == 8) || (cond == 10)) && (qs.getQuestItemsCount(SoulPieceAir) < 6) && Rnd.chance(30))
				{
					qs.giveItems(SoulPieceAir, 1, false);
					
					if (qs.getQuestItemsCount(SoulPieceAir) >= 6)
					{
						qs.setCond(cond + 1);
						qs.playSound(SOUND_MIDDLE);
					}
				}
				break;
			
			case Water:
				if ((qs.getItemEquipped(Inventory.PAPERDOLL_RHAND) == YinSword) && ((cond == 3) || (cond == 5)) && (qs.getQuestItemsCount(SoulPieceWater) < 6) && Rnd.chance(30))
				{
					qs.giveItems(SoulPieceWater, 1, false);
					
					if (qs.getQuestItemsCount(SoulPieceWater) >= 6)
					{
						qs.setCond(cond + 1);
						qs.playSound(SOUND_MIDDLE);
					}
				}
				break;
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
