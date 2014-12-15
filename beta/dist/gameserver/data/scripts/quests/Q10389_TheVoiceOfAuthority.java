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
 * @author Iqman
 */
public class Q10389_TheVoiceOfAuthority extends Quest implements ScriptFile
{
	// Npc
	private static final int RADZEN = 33803;
	// Item
	private static final int SIGN = 36229;
	// Other
	private static final String KILL = "kill";
	
	public Q10389_TheVoiceOfAuthority()
	{
		super(false);
		addTalkId(RADZEN);
		addKillNpcWithLog(1, KILL, 30, 22139, 22140, 22141, 22147, 22154, 22144, 22145, 22148, 22142, 22155);
		addLevelCheck(97, 100);
		addQuestCompletedCheck(Q10388_ConspiracyBehindDoors.class);
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
				qs.getPlayer().addExpAndSp(592767000, 142264);
				qs.giveItems(SIGN, 1);
				qs.giveItems(57, 1302720);
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
		final QuestState Cb = qs.getPlayer().getQuestState(Q10388_ConspiracyBehindDoors.class);
		
		if ((Cb == null) || !Cb.isCompleted())
		{
			return "you cannot procceed with this quest until you have completed the Conspiracy Behind Door quest";
		}
		
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
		
		if ((qs.getCond() == 1) && updateKill(npc, qs))
		{
			qs.unset(KILL);
			qs.setCond(2);
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