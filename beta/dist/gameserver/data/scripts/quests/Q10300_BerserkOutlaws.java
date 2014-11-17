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
 * @author Evil_dnk
 */
public class Q10300_BerserkOutlaws extends Quest implements ScriptFile
{
	private static final int[] Adventurequid =
	{
		33463
	};
	private static final int mouen = 30196;
	private static final int[] Basilisk =
	{
		20573,
		20574
	};
	private static final int[] gnols =
	{
		21261,
		21262,
		21263,
		21264,
		20241
	};
	private static final int[] OelMahum =
	{
		20575,
		35428,
		20576,
		20161
	};
	private static final int markofbandit = 19484;
	private static final int markofshaman = 19485;
	private static final int proofmonstr = 19486;
	
	public Q10300_BerserkOutlaws()
	{
		super(false);
		addStartNpc(Adventurequid);
		addTalkId(Adventurequid);
		addTalkId(mouen);
		addKillId(Basilisk);
		addKillId(gnols);
		addKillId(OelMahum);
		addQuestItem(markofbandit, markofshaman, proofmonstr);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "0-3.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "1-2.htm":
				qs.setCond(1);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (Util.contains(Adventurequid, npcId))
		{
			if ((qs.getPlayer().getLevel() >= 50) && (qs.getPlayer().getLevel() <= 54) && (cond == 0))
			{
				htmltext = "start.htm";
			}
			else if ((cond == 1) && Util.contains(Adventurequid, npcId))
			{
				htmltext = "0-4.htm";
			}
			else
			{
				htmltext = "noquest";
			}
		}
		else if (npcId == mouen)
		{
			if ((cond == 2) && (qs.getQuestItemsCount(markofshaman) >= 30))
			{
				htmltext = "1-3.htm";
				qs.takeAllItems(markofbandit);
				qs.takeAllItems(markofshaman);
				qs.takeAllItems(proofmonstr);
				qs.getPlayer().addExpAndSp(2046093, 1618470);
				qs.giveItems(57, 329556);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
			}
			else if (cond == 1)
			{
				htmltext = "1-2.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		
		if (qs.getCond() == 1)
		{
			if (Util.contains(Basilisk, npcId) && (qs.getQuestItemsCount(markofbandit) < 30))
			{
				qs.giveItems(markofbandit, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			else if (Util.contains(gnols, npcId) && (qs.getQuestItemsCount(markofshaman) < 30))
			{
				qs.giveItems(markofshaman, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			else if (Util.contains(OelMahum, npcId) && (qs.getQuestItemsCount(proofmonstr) < 30))
			{
				qs.giveItems(proofmonstr, 1);
				qs.playSound(SOUND_ITEMGET);
			}
		}
		
		if ((qs.getQuestItemsCount(markofbandit) >= 30) && (qs.getQuestItemsCount(markofshaman) >= 30) && (qs.getQuestItemsCount(proofmonstr) >= 30))
		{
			qs.setCond(2);
		}
		
		qs.playSound(SOUND_MIDDLE);
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