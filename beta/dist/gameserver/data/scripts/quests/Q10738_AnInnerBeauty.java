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
 * @author blacksmoke
 */
public class Q10738_AnInnerBeauty extends Quest implements ScriptFile
{
	private static final int Grakon = 33947;
	private static final int Evna = 33935;
	private static final int GrakonsNote = 39521;
	
	public Q10738_AnInnerBeauty()
	{
		super(false);
		addStartNpc(Grakon);
		addTalkId(Grakon, Evna);
		addQuestItem(GrakonsNote);
		addLevelCheck(5, 20);
		addClassCheck(182, 183);
		addQuestCompletedCheck(Q10737_GrakonsWarehouse.class);
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
				qs.giveItems(GrakonsNote, 1);
				htmltext = "33947-4.htm";
				break;
			
			case "qet_rev":
				qs.takeItems(GrakonsNote, 1);
				htmltext = "33935-3.htm";
				qs.giveItems(57, 12000);
				qs.getPlayer().addExpAndSp(2625, 1);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		
		switch (npcId)
		{
			case Grakon:
				if (isAvailableFor(qs.getPlayer()))
				{
					if (cond == 0)
					{
						htmltext = "33947-1.htm";
					}
					else if (cond == 1)
					{
						htmltext = "33947-4.htm";
					}
					else
					{
						htmltext = "noqu.htm";
					}
				}
				break;
			
			case Evna:
				if (isAvailableFor(qs.getPlayer()) && (cond == 1))
				{
					htmltext = "33935-1.htm";
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