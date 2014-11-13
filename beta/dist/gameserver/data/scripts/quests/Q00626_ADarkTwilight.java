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

public class Q00626_ADarkTwilight extends Quest implements ScriptFile
{
	// Npc
	private static final int Hierarch = 31517;
	// Item
	private static final int BloodOfSaint = 7169;
	
	public Q00626_ADarkTwilight()
	{
		super(true);
		addStartNpc(Hierarch);
		addQuestItem(BloodOfSaint);
		
		for (int npcId = 21520; npcId <= 21542; npcId++)
		{
			addKillId(npcId);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "dark_presbyter_q0626_0104.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "dark_presbyter_q0626_0201.htm":
				if (qs.getQuestItemsCount(BloodOfSaint) < 300)
				{
					htmltext = "dark_presbyter_q0626_0203.htm";
				}
				break;
			
			case "rew_exp":
				qs.takeItems(BloodOfSaint, -1);
				qs.addExpAndSp(162773, 12500);
				htmltext = "dark_presbyter_q0626_0202.htm";
				qs.exitCurrentQuest(true);
				break;
			
			case "rew_adena":
				qs.takeItems(BloodOfSaint, -1);
				qs.giveItems(ADENA_ID, 100000, true);
				htmltext = "dark_presbyter_q0626_0202.htm";
				qs.exitCurrentQuest(true);
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
				if (qs.getPlayer().getLevel() < 60)
				{
					htmltext = "dark_presbyter_q0626_0103.htm";
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "dark_presbyter_q0626_0101.htm";
				}
				break;
			
			case 1:
				htmltext = "dark_presbyter_q0626_0106.htm";
				break;
			
			case 2:
				htmltext = "dark_presbyter_q0626_0105.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Rnd.chance(70))
		{
			qs.giveItems(BloodOfSaint, 1);
			
			if (qs.getQuestItemsCount(BloodOfSaint) == 300)
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
