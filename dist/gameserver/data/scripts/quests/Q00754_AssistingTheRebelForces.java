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
 * @author KilRoy & Mangol
 * @name 754 - Assisting the Rebel Forces
 * @category Daily quest. Party
 */
public class Q00754_AssistingTheRebelForces extends Quest implements ScriptFile
{
	private final int REBEL_SUPPLY_BOX = 35549;
	private final int MARK_OF_RESISTANCE = 34909;
	private final int SIZRAK = 33669;
	private final int COMMUNICATION = 33676;
	private final int KUNDA_GUARDIAN = 23224;
	private final int KUNDA_BERSERKER = 23225;
	private final int KUNDA_EXECUTOR = 23226;
	private static final String KUNDA_GUARDIAN_KILL = "guardian";
	private static final String KUNDA_BERSERKER_KILL = "berserker";
	private static final String KUNDA_EXECUTOR_KILL = "executor";
	
	public Q00754_AssistingTheRebelForces()
	{
		super(2);
		addStartNpc(SIZRAK);
		addTalkId(SIZRAK);
		addTalkId(COMMUNICATION);
		addKillNpcWithLog(1, KUNDA_GUARDIAN_KILL, 5, KUNDA_GUARDIAN);
		addKillNpcWithLog(1, KUNDA_BERSERKER_KILL, 5, KUNDA_BERSERKER);
		addKillNpcWithLog(1, KUNDA_EXECUTOR_KILL, 5, KUNDA_EXECUTOR);
		addLevelCheck(97, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("quest_accpted"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "sofa_sizraku_q0754_04.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		
		if (npcId == SIZRAK)
		{
			if (st.isCreated() && !st.isNowAvailable())
			{
				htmltext = "sofa_sizraku_q0754_06.htm";
			}
			else if (cond == 0)
			{
				htmltext = "sofa_sizraku_q0754_01.htm";
			}
			else if (cond == 1)
			{
				htmltext = "sofa_sizraku_q0754_07.htm";
			}
			else if (cond == 2)
			{
				st.getPlayer().addExpAndSp(570676680, 261024840);
				st.giveItems(REBEL_SUPPLY_BOX, 1);
				st.giveItems(MARK_OF_RESISTANCE, 1);
				st.exitCurrentQuest(this);
				st.playSound(SOUND_FINISH);
				htmltext = "sofa_sizraku_q0754_08.htm";
			}
			else
			{
				htmltext = "sofa_sizraku_q0754_05.htm";
			}
		}
		
		if (npcId == COMMUNICATION)
		{
			if (cond == 2)
			{
				st.getPlayer().addExpAndSp(570676680, 261024840);
				st.giveItems(REBEL_SUPPLY_BOX, 1);
				st.giveItems(MARK_OF_RESISTANCE, 1);
				st.exitCurrentQuest(this);
				st.playSound(SOUND_FINISH);
				htmltext = "sofa_sizraku_q0754_08.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		boolean doneKill = updateKill(npc, st);
		
		if (doneKill)
		{
			st.unset(KUNDA_GUARDIAN_KILL);
			st.unset(KUNDA_BERSERKER_KILL);
			st.unset(KUNDA_EXECUTOR_KILL);
			st.playSound(SOUND_MIDDLE);
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