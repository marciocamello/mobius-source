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

public class Q00122_OminousNews extends Quest implements ScriptFile
{
	// Npcs
	private static final int MOIRA = 31979;
	private static final int KARUDA = 32017;
	
	public Q00122_OminousNews()
	{
		super(false);
		addStartNpc(MOIRA);
		addTalkId(KARUDA);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "seer_moirase_q0122_0104.htm":
				if (qs.getCond() == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "karuda_q0122_0201.htm":
				if (qs.getCond() == 1)
				{
					qs.giveItems(ADENA_ID, 8923);
					qs.addExpAndSp(45151, 2310);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
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
			case MOIRA:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 20)
					{
						htmltext = "seer_moirase_q0122_0101.htm";
					}
					else
					{
						htmltext = "seer_moirase_q0122_0103.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "seer_moirase_q0122_0104.htm";
				}
				break;
			
			case KARUDA:
				if (cond == 1)
				{
					htmltext = "karuda_q0122_0101.htm";
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
