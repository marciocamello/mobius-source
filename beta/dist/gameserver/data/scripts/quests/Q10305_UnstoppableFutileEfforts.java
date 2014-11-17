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

public class Q10305_UnstoppableFutileEfforts extends Quest implements ScriptFile
{
	// Npc
	private static final int NOETI = 32895;
	// Other
	public static final String A_LIST = "A_LIST";
	
	public Q10305_UnstoppableFutileEfforts()
	{
		super(false);
		addStartNpc(NOETI);
		addTalkId(NOETI);
		addLevelCheck(90, 99);
		addQuestCompletedCheck(Q10302_UnsettlingShadowAndRumors.class);
		addKillNpcWithLog(1, A_LIST, 5, 22866, 22882, 22890, 22898, 22874, 22870, 22886, 22910, 22902, 22894, 22878);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("32895-6.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmlText = "noquest";
		
		if (qs.getState() == COMPLETED)
		{
			return "32895-comp.htm";
		}
		
		if (qs.getPlayer().getLevel() < 90)
		{
			return "32895-lvl.htm";
		}
		
		final QuestState state = qs.getPlayer().getQuestState(Q10302_UnsettlingShadowAndRumors.class);
		if ((state == null) || !state.isCompleted())
		{
			return "32895-lvl.htm";
		}
		
		switch (qs.getCond())
		{
			case 0:
				return "32895.htm";
				
			case 1:
				return "32895-7.htm";
				
			case 2:
				qs.addExpAndSp(34971975, 12142200);
				qs.giveItems(57, 1007735);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				return "32895-8.htm";
		}
		
		return htmlText;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() != 1)
		{
			return null;
		}
		
		if (updateKill(npc, qs))
		{
			qs.unset(A_LIST);
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