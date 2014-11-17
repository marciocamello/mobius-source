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

/**
 * @author Iqman
 */
public class Q10378_WeedingWork extends Quest implements ScriptFile
{
	// Npc
	private static final int DADFENA = 33697;
	// Monsters
	private static final int MAN_OF_JOY = 23210;
	private static final int MAN_OR_PRAYER = 23211;
	// Items
	private static final int STEBEL = 34974;
	private static final int KOREN = 34975;
	// Rewards
	private static final int SCROLL = 35292;
	
	public Q10378_WeedingWork()
	{
		super(false);
		addTalkId(DADFENA);
		addQuestItem(STEBEL);
		addQuestItem(KOREN);
		addKillId(MAN_OF_JOY, MAN_OR_PRAYER);
		addLevelCheck(95, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "accepted.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "endquest.htm":
				qs.takeAllItems(STEBEL);
				qs.takeAllItems(KOREN);
				qs.getPlayer().addExpAndSp(845059770, 378445230);
				qs.giveItems(SCROLL, 1);
				qs.giveItems(57, 3000000);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
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
				htmltext = "start.htm";
				break;
			
			case 1:
				htmltext = "notcollected.htm";
				break;
			
			case 2:
				htmltext = "collected.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs == null)
		{
			return null;
		}
		
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		if (qs.getCond() == 1)
		{
			if ((qs.getQuestItemsCount(STEBEL) < 5) && Rnd.chance(7))
			{
				qs.giveItems(STEBEL, 1);
			}
			
			if ((qs.getQuestItemsCount(KOREN) < 5) && Rnd.chance(7))
			{
				qs.giveItems(KOREN, 1);
			}
			
			if ((qs.getQuestItemsCount(KOREN) >= 5) && (qs.getQuestItemsCount(STEBEL) >= 5))
			{
				qs.setCond(2);
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