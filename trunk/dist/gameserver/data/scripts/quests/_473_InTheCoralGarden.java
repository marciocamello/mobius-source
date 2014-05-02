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

public class _473_InTheCoralGarden extends Quest implements ScriptFile
{
	// npc
	public static final int FIOREN = 33044;
	
	// mobs
	public static final int MICHAEL = 25799;
	
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
	
	public _473_InTheCoralGarden()
	{
		super(true);
		addStartNpc(FIOREN);
		addKillId(MICHAEL);
		addLevelCheck(97, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("33044-3.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		if (event.equalsIgnoreCase("33044-6.htm"))
		{
			st.giveItems(30387, 10); // hell proof
			st.unset("cond");
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if (npcId == FIOREN)
		{
			if (state == 1)
			{
				if (player.getLevel() < 97)
				{
					return "33044-lvl.htm";
				}
				if (!st.isNowAvailable())
				{
					return "33044-comp.htm";
				}
				
				if (player.getLevel() < 97)
				{
					return "33044-lvl.htm";
				}
				
				return "33044.htm";
			}
			if (state == 2)
			{
				if (cond == 1)
				{
					return "33044-4.htm";
				}
				if (cond == 2)
				{
					return "33044-5.htm";
				}
			}
		}
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if ((cond != 1) || (npc == null))
		{
			return null;
		}
		st.setCond(2);
		return null;
	}
}