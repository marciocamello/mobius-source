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

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author KilRoy
 * @name 10383 - Fergasons Offer
 * @category One quest. Party
 */
public class Q10383_FergasonsOffer extends Quest implements ScriptFile
{
	private final static int UNSTABLE_PETRA = 34958;
	private final static int SIZRAK = 33669;
	private final static int FERGASON = 33681;
	private final static int AKU = 33671;
	private final static int[] COUCH =
	{
		23233,
		23234,
		23237,
		23219,
		23217,
		23218,
		23231,
		23232,
		23213,
		23214,
		23227,
		23228
	};
	
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
	
	public Q10383_FergasonsOffer()
	{
		super(2);
		addStartNpc(SIZRAK);
		addTalkId(FERGASON);
		addTalkId(AKU);
		addKillId(COUCH);
		addQuestItem(UNSTABLE_PETRA);
		addLevelCheck(97, 99);
		addQuestCompletedCheck(Q10381_ToTheSeedOfHellfire.class);
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
			htmltext = "sofa_sizraku_q10383_03.htm";
		}
		
		if (event.equalsIgnoreCase("quest_next"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			htmltext = "maestro_ferguson_q10383_04.htm";
		}
		
		if (event.equalsIgnoreCase("quest_done"))
		{
			st.giveItems(ADENA_ID, 3256740);
			st.addExpAndSp(951127800, 435041400);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			htmltext = "sofa_aku_q10383_03.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getId();
		String htmltext = "noquest";
		
		if (npcId == SIZRAK)
		{
			if (st.isCompleted())
			{
				htmltext = "sofa_sizraku_q10383_05.htm";
			}
			else if (!isAvailableFor(st.getPlayer()))
			{
				htmltext = "sofa_sizraku_q10383_07.htm";
			}
			else if (cond == 0)
			{
				htmltext = "sofa_sizraku_q10383_01.htm";
			}
			else if (cond > 0)
			{
				htmltext = "sofa_sizraku_q10383_06.htm";
			}
			else
			{
				htmltext = "sofa_sizraku_q10383_04.htm";
			}
		}
		
		if (npcId == FERGASON)
		{
			if (cond == 1)
			{
				htmltext = "maestro_ferguson_q10383_01.htm";
			}
			else if (cond == 2)
			{
				htmltext = "maestro_ferguson_q10383_05.htm";
			}
		}
		
		if (npcId == AKU)
		{
			if (cond == 2)
			{
				htmltext = "sofa_aku_q10383_01.htm";
			}
			else if (cond == 3)
			{
				htmltext = "sofa_aku_q10383_02.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getId();
		int cond = st.getCond();
		
		if ((cond == 2) && (ArrayUtils.contains(COUCH, npcId) & (st.getQuestItemsCount(UNSTABLE_PETRA) <= 20)))
		{
			st.rollAndGive(UNSTABLE_PETRA, 1, 30);
			
			if (st.getQuestItemsCount(UNSTABLE_PETRA) == 20)
			{
				st.setCond(3);
				st.playSound(SOUND_MIDDLE);
			}
		}
		
		return null;
	}
}