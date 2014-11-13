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

public class Q00481_ShadowHelper extends Quest implements ScriptFile
{
	// Npc
	public static final int RINOBERG = 33302;
	// Other
	public static final String A_LIST = "a_list";
	
	public Q00481_ShadowHelper()
	{
		super(true);
		addStartNpc(RINOBERG);
		addTalkId(RINOBERG);
		addKillNpcWithLog(1, A_LIST, 20, 20213, 20214, 20215, 20216, 21035, 21039, 20217, 20751, 21036, 20218, 20219, 20220, 21037, 29012, 20754, 20222, 21040, 21038, 20753, 29007);
		addLevelCheck(38, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "33302-6.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "33302-10.htm":
				qs.unset("cond");
				qs.addExpAndSp(240000, 156000);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		
		switch (qs.getState())
		{
			case 1:
				if (player.getLevel() < 38)
				{
					return "33302-lvl.htm";
				}
				else if (!qs.isNowAvailable())
				{
					return "33302-comp.htm";
				}
				return "33302.htm";
				
			case 2:
				if (cond == 1)
				{
					return "33302-8.htm";
				}
				else if (cond == 2)
				{
					return "33302-9.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && (updateKill(npc, qs)))
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