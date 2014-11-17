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
 * @author KilRoy
 * @name 10384 - An Audience With Tauti
 * @category One quest. Party
 */
public class Q10384_AnAudienceWithTauti extends Quest implements ScriptFile
{
	// Npcs
	private final static int FERGASON = 33681;
	private final static int AKU = 33671;
	// Items
	private final static int BOTTLE_OF_TAUTI_SOUL = 35295;
	private final static int TAUTI_FRAGMENT = 34960;
	// Monster
	private final static int TAUTI_EXTREME = 29234;
	
	public Q10384_AnAudienceWithTauti()
	{
		super(2);
		addStartNpc(FERGASON);
		addTalkId(FERGASON, AKU);
		addKillId(TAUTI_EXTREME);
		addQuestItem(TAUTI_FRAGMENT);
		addLevelCheck(97, 99);
		addQuestCompletedCheck(Q10383_FergasonsOffer.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accpted":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "maestro_ferguson_q10384_04.htm";
				break;
			
			case "sofa_aku_q10384_02.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "sofa_aku_q10384_02.htm";
				break;
			
			case "quest_done":
				qs.giveItems(ADENA_ID, 3256740);
				qs.giveItems(BOTTLE_OF_TAUTI_SOUL, 1);
				qs.addExpAndSp(951127800, 435041400);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				htmltext = "maestro_ferguson_q10384_11.htm";
				break;
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
			case FERGASON:
				if (qs.isCompleted())
				{
					htmltext = "maestro_ferguson_q10384_07.htm";
				}
				else if (!isAvailableFor(qs.getPlayer()))
				{
					htmltext = "maestro_ferguson_q10384_06.htm";
				}
				else if (cond == 0)
				{
					htmltext = "maestro_ferguson_q10384_01.htm";
				}
				else if ((cond == 1) | (cond == 2))
				{
					htmltext = "maestro_ferguson_q10384_08.htm";
				}
				else if (cond == 3)
				{
					htmltext = "maestro_ferguson_q10384_09.htm";
				}
				else
				{
					htmltext = "maestro_ferguson_q10384_05.htm";
				}
				break;
			
			case AKU:
				if (cond == 1)
				{
					htmltext = "sofa_aku_q10384_01.htm";
				}
				else if (cond == 2)
				{
					htmltext = "sofa_aku_q10383_02.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 2)
		{
			qs.giveItems(TAUTI_FRAGMENT, 1);
			qs.setCond(3);
			qs.playSound(SOUND_MIDDLE);
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