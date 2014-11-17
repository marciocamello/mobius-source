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

//By Evil_dnk
public class Q10358_DividedSakumPoslof extends Quest implements ScriptFile
{
	private static final int Guild = 31795;
	private static final int Lef = 33510;
	private static final int Vilan = 20402;
	private static final int Zombi = 20458;
	private static final int Poslov = 27452;
	private static final String vilan_item = "vilan";
	private static final String zombi_item = "zombi";
	private int killedposlov;
	
	public Q10358_DividedSakumPoslof()
	{
		super(false);
		addStartNpc(Lef);
		addTalkId(Lef, Guild);
		addKillId(Poslov);
		addKillNpcWithLog(1, vilan_item, 23, Vilan);
		addKillNpcWithLog(1, zombi_item, 20, Zombi);
		addLevelCheck(32, 40);
		addQuestCompletedCheck(Q10337_SakumsImpact.class);
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
				htmltext = "0-3.htm";
				break;
			
			case "qet_rev":
				htmltext = "1-3.htm";
				qs.takeAllItems(17585);
				qs.getPlayer().addExpAndSp(450000, 180000);
				qs.giveItems(57, 105000);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "1-3.htm":
				htmltext = "1-3.htm";
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Lef:
				if (qs.isCompleted())
				{
					return htmltext;
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "0-1.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-4.htm";
				}
				else if (cond == 2)
				{
					htmltext = "0-5.htm";
					qs.giveItems(17585, 1, false);
					qs.setCond(3);
				}
				else if (cond == 3)
				{
					return htmltext;
				}
				else if (cond == 4)
				{
					return htmltext;
				}
				else
				{
					return htmltext;
				}
				break;
			
			case Guild:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if ((cond == 0) || (cond == 1) || (cond == 2) || (cond == 3))
				{
					return htmltext;
				}
				else if (cond == 4)
				{
					htmltext = "1-1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (updateKill(npc, qs))
		{
			qs.unset(vilan_item);
			qs.unset(zombi_item);
			qs.setCond(2);
		}
		
		if ((npc.getId() == Poslov) && (qs.getCond() == 3))
		{
			++killedposlov;
			
			if (killedposlov >= 1)
			{
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				killedposlov = 0;
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