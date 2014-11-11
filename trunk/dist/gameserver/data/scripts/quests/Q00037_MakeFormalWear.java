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
import lineage2.gameserver.templates.item.ItemTemplate;

public class Q00037_MakeFormalWear extends Quest implements ScriptFile
{
	// Npcs
	private static final int ALEXIS = 30842;
	private static final int LEIKAR = 31520;
	private static final int JEREMY = 31521;
	private static final int MIST = 31627;
	// Items
	private static final int MYSTERIOUS_CLOTH = 7076;
	private static final int JEWEL_BOX = 7077;
	private static final int SEWING_KIT = 7078;
	private static final int DRESS_SHOES_BOX = 7113;
	private static final int SIGNET_RING = 7164;
	private static final int ICE_WINE = 7160;
	private static final int BOX_OF_COOKIES = 7159;
	
	public Q00037_MakeFormalWear()
	{
		super(false);
		addStartNpc(ALEXIS);
		addTalkId(ALEXIS, LEIKAR, JEREMY, MIST);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30842-1.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "31520-1.htm":
				qs.giveItems(SIGNET_RING, 1);
				qs.setCond(2);
				break;
			
			case "31521-1.htm":
				qs.takeItems(SIGNET_RING, 1);
				qs.giveItems(ICE_WINE, 1);
				qs.setCond(3);
				break;
			
			case "31627-1.htm":
				if (qs.getQuestItemsCount(ICE_WINE) > 0)
				{
					qs.takeItems(ICE_WINE, 1);
					qs.setCond(4);
				}
				else
				{
					htmltext = "You don't have enough materials";
				}
				break;
			
			case "31521-3.htm":
				qs.giveItems(BOX_OF_COOKIES, 1);
				qs.setCond(5);
				break;
			
			case "31520-3.htm":
				qs.takeItems(BOX_OF_COOKIES, 1);
				qs.setCond(6);
				break;
			
			case "31520-5.htm":
				qs.takeItems(MYSTERIOUS_CLOTH, 1);
				qs.takeItems(JEWEL_BOX, 1);
				qs.takeItems(SEWING_KIT, 1);
				qs.setCond(7);
				break;
			
			case "31520-7.htm":
				if (qs.getQuestItemsCount(DRESS_SHOES_BOX) > 0)
				{
					qs.takeItems(DRESS_SHOES_BOX, 1);
					qs.giveItems(ItemTemplate.ITEM_ID_FORMAL_WEAR, 1);
					qs.unset("cond");
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "You don't have enough materials";
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
		
		switch (npcId)
		{
			case ALEXIS:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 60)
					{
						htmltext = "30842-0.htm";
					}
					else
					{
						htmltext = "30842-2.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "30842-1.htm";
				}
				break;
			
			case LEIKAR:
				switch (cond)
				{
					case 1:
						htmltext = "31520-0.htm";
						break;
					
					case 2:
						htmltext = "31520-1.htm";
						break;
					
					case 5:
					case 6:
						if ((qs.getQuestItemsCount(MYSTERIOUS_CLOTH) > 0) && (qs.getQuestItemsCount(JEWEL_BOX) > 0) && (qs.getQuestItemsCount(SEWING_KIT) > 0))
						{
							htmltext = "31520-4.htm";
						}
						else if (qs.getQuestItemsCount(BOX_OF_COOKIES) > 0)
						{
							htmltext = "31520-2.htm";
						}
						else
						{
							htmltext = "31520-3.htm";
						}
						break;
					
					case 7:
						if (qs.getQuestItemsCount(DRESS_SHOES_BOX) > 0)
						{
							htmltext = "31520-6.htm";
						}
						else
						{
							htmltext = "31520-5.htm";
						}
						break;
				}
				break;
			
			case JEREMY:
				switch (cond)
				{
					case 3:
						htmltext = "31521-1.htm";
						break;
					
					case 4:
						htmltext = "31521-2.htm";
						break;
					
					case 5:
						htmltext = "31521-3.htm";
						break;
				}
				
				if (qs.getQuestItemsCount(SIGNET_RING) > 0)
				{
					htmltext = "31521-0.htm";
				}
				break;
			
			case MIST:
				htmltext = "31627-0.htm";
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
