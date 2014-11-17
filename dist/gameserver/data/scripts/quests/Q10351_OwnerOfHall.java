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

public class Q10351_OwnerOfHall extends Quest implements ScriptFile
{
	// Npc
	private static final int TIPIA_NORMAL = 32892;
	// Monster
	private static final int OCTAVIUS = 29212;
	
	public Q10351_OwnerOfHall()
	{
		super(true);
		addStartNpc(TIPIA_NORMAL);
		addTalkId(TIPIA_NORMAL);
		addKillId(OCTAVIUS);
		addQuestCompletedCheck(Q10318_DecayingDarkness.class);
		addLevelCheck(95, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "32892-7.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32892-10.htm":
				qs.giveItems(57, 23655000);
				qs.giveItems(19461, 1);
				qs.addExpAndSp(897850000, 416175000);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if (player.getLevel() < 95)
		{
			return "32892-lvl.htm";
		}
		
		final QuestState state = qs.getPlayer().getQuestState(Q10318_DecayingDarkness.class);
		
		if ((state == null) || !state.isCompleted())
		{
			return "32892-lvl.htm";
		}
		
		switch (qs.getCond())
		{
			case 0:
				return "32892.htm";
				
			case 1:
				return "32892-8.htm";
				
			case 2:
				return "32892-9.htm";
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() != 1)
		{
			return null;
		}
		
		qs.setCond(2);
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