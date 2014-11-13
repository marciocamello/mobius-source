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
import lineage2.gameserver.scripts.ScriptFile;

public class Q00191_VainConclusion extends Quest implements ScriptFile
{
	// Npcs
	private static final int Kusto = 30512;
	private static final int Lorain = 30673;
	private static final int Dorothy = 30970;
	private static final int Shegfield = 30068;
	// Item
	private static final int Metal = 10371;
	
	public Q00191_VainConclusion()
	{
		super(false);
		addTalkId(Kusto, Dorothy, Lorain, Shegfield);
		addFirstTalkId(Dorothy);
		addQuestItem(Metal);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30970-03.htm":
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				qs.giveItems(Metal, 1);
				break;
			
			case "30673-02.htm":
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
				qs.takeItems(Metal, -1);
				break;
			
			case "30068-03.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30512-02.htm":
				qs.giveItems(ADENA_ID, 134292);
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
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (qs.getState() == STARTED)
		{
			switch (npcId)
			{
				case Dorothy:
					if (cond == 0)
					{
						if (qs.getPlayer().getLevel() < 42)
						{
							htmltext = "30970-00.htm";
						}
						else
						{
							htmltext = "30970-01.htm";
						}
					}
					else if (cond == 1)
					{
						htmltext = "30970-04.htm";
					}
					break;
				
				case Lorain:
					switch (cond)
					{
						case 1:
							htmltext = "30673-01.htm";
							break;
						
						case 2:
							htmltext = "30673-03.htm";
							break;
						
						case 3:
							htmltext = "30673-04.htm";
							qs.setCond(4);
							qs.playSound(SOUND_MIDDLE);
							break;
						
						case 4:
							htmltext = "30673-05.htm";
							break;
					}
					break;
				
				case Shegfield:
					if (cond == 2)
					{
						htmltext = "30068-01.htm";
					}
					else if (cond == 3)
					{
						htmltext = "30068-04.htm";
					}
					break;
				
				case Kusto:
					if (cond == 4)
					{
						htmltext = "30512-01.htm";
					}
					break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(Q00188_SealRemoval.class);
		
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		
		return "";
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
