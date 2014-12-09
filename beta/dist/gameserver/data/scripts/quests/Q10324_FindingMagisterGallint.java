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
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10324_FindingMagisterGallint extends Quest implements ScriptFile
{
	// Npcs
	private static final int Shenon = 32974;
	private static final int Galint = 32980;
	
	public Q10324_FindingMagisterGallint()
	{
		super(false);
		addStartNpc(Shenon);
		addTalkId(Shenon);
		addTalkId(Galint);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10323_GoingIntoARealWarLetsGoToTheTrainingGround.class);
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
				htmltext = "1-2.htm";
				qs.showTutorialHTML(TutorialShowHtml.QT_004, TutorialShowHtml.TYPE_WINDOW);
				qs.getPlayer().addExpAndSp(1700, 200);
				qs.giveItems(57, 109);
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
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Shenon:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-4.htm";
				}
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case Galint:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = "1-nc.htm";
				}
				else if (cond == 1)
				{
					htmltext = "1-1.htm";
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
