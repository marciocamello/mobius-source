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

/**
 * @author Krash
 */
public class Q10744_StrongerThanSteel extends Quest implements ScriptFile
{
	// Npcs
	private static final int Leira = 33952;
	private static final int Dolkin = 33954;
	// Monsters
	private static final int Treant = 23457;
	private static final int Leafie = 23458;
	// Drops
	private static final int Treant_leaf = 39532;
	private static final int Leafie_leaf = 39531;
	
	public Q10744_StrongerThanSteel()
	{
		super(false);
		addStartNpc(Leira);
		addTalkId(Leira, Dolkin);
		addQuestItem(Treant_leaf, Leafie_leaf);
		addKillId(Treant, Leafie);
		addLevelCheck(15, 20);
		addClassCheck(182, 183);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "33952-3.htm";
				break;
			
			case "quest_middle":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "33954-2.htm";
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
		
		switch (npc.getId())
		{
			case Leira:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							htmltext = "33952-1.htm";
						}
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
			
			case Dolkin:
				switch (cond)
				{
					case 1:
						htmltext = "33954-1.htm";
						break;
					
					case 3:
						htmltext = "33954-3.htm";
						qs.takeItems(Treant_leaf, 20);
						qs.takeItems(Leafie_leaf, 15);
						qs.giveItems(57, 34000);
						qs.getPlayer().addExpAndSp(112001, 5);
						qs.exitCurrentQuest(false);
						break;
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 2)
		{
			switch (npc.getId())
			{
				case Treant:
					if (qs.getQuestItemsCount(Treant_leaf) < 20)
					{
						qs.giveItems(Treant_leaf, 1);
						qs.playSound(SOUND_ITEMGET);
					}
					break;
				
				case Leafie:
					if (qs.getQuestItemsCount(Leafie_leaf) < 15)
					{
						qs.giveItems(Leafie_leaf, 1);
						qs.playSound(SOUND_ITEMGET);
					}
					break;
			}
			
			if ((qs.getQuestItemsCount(Treant_leaf) >= 20) && (qs.getQuestItemsCount(Leafie_leaf) >= 15))
			{
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
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