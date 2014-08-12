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

public class Q00150_ExtremeChallengePrimalMotherResurrected extends Quest implements ScriptFile
{
	private static final int LIMIER = 33293;
	private static final int ISXINA_EXTRIM = 29196;
	private static final int SIGN_OF_SHILEN = 17589;
	
	public Q00150_ExtremeChallengePrimalMotherResurrected()
	{
		super(false);
		addStartNpc(LIMIER);
		addTalkId(LIMIER);
		addKillId(ISXINA_EXTRIM);
		addQuestItem(SIGN_OF_SHILEN);
		addLevelCheck(97, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("33293-5.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		QuestState questState = player.getQuestState(Q00149_PrimalMotherIstina.class);
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		int id = st.getState();
		
		if (id == COMPLETED)
		{
			return "33293-comp.htm";
		}
		
		if ((questState == null) || !questState.isCompleted())
		{
			return "33293-nocomp.htm";
		}
		
		if (player.getLevel() < 97)
		{
			return "33293-lvl.htm";
		}
		
		if (npcId == LIMIER)
		{
			if (cond == 0)
			{
				return "33293.htm";
			}
			
			if (cond == 1)
			{
				return "33293-7.htm";
			}
			
			if (cond == 2)
			{
				st.takeItems(SIGN_OF_SHILEN, -1);
				st.giveItems(34883, 1); // isxina soul bottle GOD: harmony //maybe trade for blessed?
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
				return "33293-9.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getCond() == 1) && (st.getQuestItemsCount(SIGN_OF_SHILEN) == 0))
		{
			st.giveItems(SIGN_OF_SHILEN, 1);
			st.setCond(2);
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
