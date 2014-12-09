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
 * @author GM-Exid
 */
public class Q10391_SuspiciousHelper extends Quest implements ScriptFile
{
	// Npcs
	private static final int Eli = 33858;
	private static final int Chel = 33861;
	private static final int Iason = 33859;
	// Items
	private static final int Forged_Card = 36707;
	private static final int Exp_Material = 36708;
	private static final int STEEL_COINS = 37045;
	
	public Q10391_SuspiciousHelper()
	{
		super(false);
		addStartNpc(Eli);
		addTalkId(Chel, Iason);
		addQuestItem(Forged_Card, Exp_Material);
		addLevelCheck(40, 46);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "33858-04.htm":
				qs.setCond(1);
				qs.giveItems(36707, 1);
				qs.setState(STARTED);
				qs.playSound("ItemSound.quests_accept");
				break;
			
			case "33861-03.htm":
				qs.setCond(2);
				qs.takeItems(36707, 1);
				qs.giveItems(36708, 1);
				break;
			
			case "33859-04.htm":
				qs.takeItems(36708, 1);
				qs.giveItems(STEEL_COINS, 1, true);
				qs.addExpAndSp(388290, 3882);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
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
			case Eli:
				if ((cond == 0) && (qs.getPlayer().getLevel() >= 40) && (qs.getPlayer().getLevel() <= 46))
				{
					htmltext = "33858-01.htm";
				}
				else if (cond == 1)
				{
					htmltext = "33858-05.htm";
				}
				break;
			
			case Chel:
				if (cond == 1)
				{
					htmltext = "33861-01.htm";
				}
				else if (cond == 2)
				{
					htmltext = "33861-04.htm";
				}
				break;
			
			case Iason:
				if (cond == 2)
				{
					htmltext = "33859-01.htm";
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
