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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

public class Q10273_GoodDayToFly extends Quest implements ScriptFile
{
	// Npc
	private final static int Lekon = 32557;
	// Monsters
	private final static int VultureRider1 = 22614;
	private final static int VultureRider2 = 22615;
	// Item
	private final static int Mark = 13856;
	
	public Q10273_GoodDayToFly()
	{
		super(false);
		addStartNpc(Lekon);
		addQuestItem(Mark);
		addKillId(VultureRider1, VultureRider2);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "32557-06.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32557-09.htm":
				if (player.getTransformation() != 0)
				{
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				qs.set("transform", "1");
				SkillTable.getInstance().getInfo(5982, 1).getEffects(player, player, false, false);
				break;
			
			case "32557-10.htm":
				if (player.getTransformation() != 0)
				{
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				SkillTable.getInstance().getInfo(5983, 1).getEffects(player, player, false, false);
				break;
			
			case "32557-13.htm":
				if (player.getTransformation() != 0)
				{
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				if (qs.getInt("transform") == 1)
				{
					SkillTable.getInstance().getInfo(5982, 1).getEffects(player, player, false, false);
				}
				else if (qs.getInt("transform") == 2)
				{
					SkillTable.getInstance().getInfo(5983, 1).getEffects(player, player, false, false);
				}
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int id = qs.getState();
		final int transform = qs.getInt("transform");
		
		if (id == COMPLETED)
		{
			htmltext = "32557-0a.htm";
		}
		else if (id == CREATED)
		{
			if (qs.getPlayer().getLevel() < 75)
			{
				htmltext = "32557-00.htm";
			}
			else
			{
				htmltext = "32557-01.htm";
			}
		}
		else if (qs.getQuestItemsCount(Mark) >= 5)
		{
			htmltext = "32557-14.htm";
			
			if (transform == 1)
			{
				qs.giveItems(13553, 1);
			}
			else if (transform == 2)
			{
				qs.giveItems(13554, 1);
			}
			
			qs.takeAllItems(Mark);
			qs.giveItems(13857, 1);
			qs.addExpAndSp(6660000, 7375000);
			qs.exitCurrentQuest(false);
			qs.playSound(SOUND_FINISH);
		}
		else if (transform < 1)
		{
			htmltext = "32557-07.htm";
		}
		else
		{
			htmltext = "32557-11.htm";
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
		final long count = qs.getQuestItemsCount(Mark);
		
		if ((cond == 1) && (count < 5))
		{
			qs.giveItems(Mark, 1);
			
			if (count == 4)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
			}
			else
			{
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
