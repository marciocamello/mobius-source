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

public class Q00188_SealRemoval extends Quest implements ScriptFile
{
	// Npcs
	private static final int Dorothy = 30970;
	private static final int Lorain = 30673;
	private static final int Nikola = 30621;
	// Item
	private static final int BrokenMetal = 10369;
	
	public Q00188_SealRemoval()
	{
		super(false);
		addTalkId(Dorothy, Nikola, Lorain);
		addFirstTalkId(Lorain);
		addQuestItem(BrokenMetal);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "researcher_lorain_q0188_03.htm":
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				qs.giveItems(BrokenMetal, 1);
				break;
			
			case "maestro_nikola_q0188_03.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "dorothy_the_locksmith_q0188_03.htm":
				qs.giveItems(ADENA_ID, 110336);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (qs.getState() == STARTED)
		{
			switch (npcId)
			{
				case Lorain:
					if (cond == 0)
					{
						if (qs.getPlayer().getLevel() < 41)
						{
							htmltext = "researcher_lorain_q0188_02.htm";
						}
						else
						{
							htmltext = "researcher_lorain_q0188_01.htm";
						}
					}
					else if (cond == 1)
					{
						htmltext = "researcher_lorain_q0188_04.htm";
					}
					break;
				
				case Nikola:
					if (cond == 1)
					{
						htmltext = "maestro_nikola_q0188_01.htm";
					}
					else if (cond == 2)
					{
						htmltext = "maestro_nikola_q0188_05.htm";
					}
					break;
				
				case Dorothy:
					if (cond == 2)
					{
						htmltext = "dorothy_the_locksmith_q0188_01.htm";
					}
					break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs1 = player.getQuestState(Q00186_ContractExecution.class);
		final QuestState qs2 = player.getQuestState(Q00187_NikolasHeart.class);
		
		if ((((qs1 != null) && qs1.isCompleted()) || ((qs2 != null) && qs2.isCompleted())) && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		
		return "";
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
