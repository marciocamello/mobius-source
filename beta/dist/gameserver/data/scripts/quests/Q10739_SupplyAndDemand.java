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
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author: Krash
 */
public class Q10739_SupplyAndDemand extends Quest implements ScriptFile
{
	// Npcs
	private static final int Evna = 33935;
	private static final int Denya = 33934;
	private static final int Pelu = 33936;
	private static final int Ceri = 33937;
	private static final int Sivanthe = 33951;
	// Items
	private static final int Weapon_Supply_Box = 39522;
	private static final int Armor_Supply_Box = 39523;
	private static final int Grocery_Supply_Box = 39524;
	private static final int Accessory_Supply_Box = 39525;
	private static final int Leather_Shirt = 21;
	private static final int Leather_Pants = 29;
	private static final int Apprentice_Earring = 112;
	private static final int Necklace_of_Knowledge = 906;
	
	public Q10739_SupplyAndDemand()
	{
		super(false);
		addStartNpc(Evna);
		addTalkId(Evna, Denya, Pelu, Ceri, Sivanthe);
		addQuestItem(Weapon_Supply_Box);
		addLevelCheck(6, 20);
		addClassCheck(182, 183);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "evna_cont":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				qs.giveItems(Weapon_Supply_Box, 1);
				htmltext = "33935-3.htm";
				break;
			
			case "denya_cont":
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
				qs.giveItems(Armor_Supply_Box, 1);
				htmltext = "33934-2.htm";
				break;
			
			case "pelu_cont":
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(3);
				qs.giveItems(Grocery_Supply_Box, 1);
				htmltext = "33936-2.htm";
				break;
			
			case "ceri_cont":
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(4);
				qs.giveItems(Accessory_Supply_Box, 1);
				htmltext = "33937-2.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Evna:
				if (cond == 0)
				{
					if (isAvailableFor(qs.getPlayer()))
					{
						htmltext = "33935-1.htm";
					}
				}
				else if (cond == 1)
				{
					htmltext = "33935-4.htm";
				}
				break;
			
			case Denya:
				if (cond == 1)
				{
					htmltext = "33934-1.htm";
				}
				else if (cond == 2)
				{
					htmltext = "33934-3.htm";
				}
				break;
			
			case Pelu:
				if (cond == 2)
				{
					htmltext = "33936-1.htm";
				}
				else if (cond == 3)
				{
					htmltext = "33936-3.htm";
				}
				break;
			
			case Ceri:
				if (cond == 3)
				{
					htmltext = "33937-1.htm";
				}
				else if (cond == 4)
				{
					htmltext = "33937-3.htm";
				}
				break;
			
			case Sivanthe:
				if (cond == 4)
				{
					qs.giveItems(Leather_Shirt, 1);
					qs.giveItems(Leather_Pants, 1);
					qs.giveItems(57, 1400);
					qs.giveItems(Apprentice_Earring, 1);
					qs.giveItems(Apprentice_Earring, 1);
					qs.giveItems(Necklace_of_Knowledge, 1);
					qs.takeItems(Weapon_Supply_Box, 1);
					qs.takeItems(Armor_Supply_Box, 1);
					qs.takeItems(Grocery_Supply_Box, 1);
					qs.takeItems(Accessory_Supply_Box, 1);
					qs.getPlayer().addExpAndSp(8136, 0);
					htmltext = "33951-1.htm";
					qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.CHECK_YOUR_EQUIPMENT_IN_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
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