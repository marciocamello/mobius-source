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
import lineage2.gameserver.utils.Util;

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
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accpted":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "sofa_sizraku_q10383_03.htm";
				break;
			
			case "quest_next":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "maestro_ferguson_q10383_04.htm";
				break;
			
			case "quest_done":
				qs.giveItems(ADENA_ID, 3256740);
				qs.addExpAndSp(951127800, 435041400);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				htmltext = "sofa_aku_q10383_03.htm";
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
			case SIZRAK:
				if (qs.isCompleted())
				{
					htmltext = "sofa_sizraku_q10383_05.htm";
				}
				else if (!isAvailableFor(qs.getPlayer()))
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
				break;
			
			case FERGASON:
				if (cond == 1)
				{
					htmltext = "maestro_ferguson_q10383_01.htm";
				}
				else if (cond == 2)
				{
					htmltext = "maestro_ferguson_q10383_05.htm";
				}
				break;
			
			case AKU:
				if (cond == 2)
				{
					htmltext = "sofa_aku_q10383_01.htm";
				}
				else if (cond == 3)
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
		if ((qs.getCond() == 2) && (Util.contains(COUCH, npc.getId()) & (qs.getQuestItemsCount(UNSTABLE_PETRA) <= 20)))
		{
			qs.rollAndGive(UNSTABLE_PETRA, 1, 30);
			
			if (qs.getQuestItemsCount(UNSTABLE_PETRA) == 20)
			{
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
			}
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