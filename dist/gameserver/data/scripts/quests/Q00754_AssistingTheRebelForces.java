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
		addTalkId(SIZRAK, COMMUNICATION);
		addKillNpcWithLog(1, KUNDA_GUARDIAN_KILL, 5, KUNDA_GUARDIAN);
		addKillNpcWithLog(1, KUNDA_BERSERKER_KILL, 5, KUNDA_BERSERKER);
		addKillNpcWithLog(1, KUNDA_EXECUTOR_KILL, 5, KUNDA_EXECUTOR);
		addLevelCheck(97, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("quest_accpted"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
			htmltext = "sofa_sizraku_q0754_04.htm";
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
			case SIZRAK:
				if (qs.isCreated() && !qs.isNowAvailable())
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
					qs.getPlayer().addExpAndSp(570676680, 261024840);
					qs.giveItems(REBEL_SUPPLY_BOX, 1);
					qs.giveItems(MARK_OF_RESISTANCE, 1);
					qs.exitCurrentQuest(this);
					qs.playSound(SOUND_FINISH);
					htmltext = "sofa_sizraku_q0754_08.htm";
				}
				else
				{
					htmltext = "sofa_sizraku_q0754_05.htm";
				}
				break;
			
			case COMMUNICATION:
				if (cond == 2)
				{
					qs.getPlayer().addExpAndSp(570676680, 261024840);
					qs.giveItems(REBEL_SUPPLY_BOX, 1);
					qs.giveItems(MARK_OF_RESISTANCE, 1);
					qs.exitCurrentQuest(this);
					qs.playSound(SOUND_FINISH);
					htmltext = "sofa_sizraku_q0754_08.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (updateKill(npc, qs))
		{
			qs.unset(KUNDA_GUARDIAN_KILL);
			qs.unset(KUNDA_BERSERKER_KILL);
			qs.unset(KUNDA_EXECUTOR_KILL);
			qs.playSound(SOUND_MIDDLE);
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