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

public class _10388_ConspiracyBehindDoor extends Quest implements ScriptFile
{
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
	
	private static final int ELIA = 31329;
	private static final int KARGOS = 33821;
	private static final int HICHEN = 33820;
	private static final int RAZDEN = 33803;
	
	public _10388_ConspiracyBehindDoor()
	{
		super(true);
		addStartNpc(ELIA);
		addTalkId(KARGOS);
		addTalkId(HICHEN);
		addTalkId(RAZDEN);
		addLevelCheck(97, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("go.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("toCond2.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("toCond3.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("final.htm"))
		{
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			st.giveItems(ADENA_ID, 65136);
			st.addExpAndSp(29638350, 2963835);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		int id = st.getState();
		
		if (id == CREATED)
		{
			if (st.getPlayer().getLevel() >= 97)
			{
				htmltext = "start.htm";
			}
			else
			{
				htmltext = "nolvl.htm";
			}
		}
		else if (npcId == KARGOS)
		{
			if (cond == 1)
			{
				return "cond1.htm";
			}
		}
		else if (npcId == 33820)
		{
			if (cond == 2)
			{
				return "cond2.htm";
			}
		}
		else if (npcId == RAZDEN)
		{
			if (cond == 3)
			{
				return "cond3.htm";
			}
		}
		
		return htmltext;
	}
}