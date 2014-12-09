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
 * @author GM-Exid
 */
public class Q10392_FailureAndItsConsequences extends Quest implements ScriptFile
{
	// Npcs
	private static final int Eli = 33858;
	private static final int Iason = 33859;
	// Items
	private static final int Sus_frag = 36709;
	private static final int STEEL_COINS = 37045;
	// Mobs
	private static final int[] Swamp_Tribe =
	{
		20991,
		20992,
		20993
	};
	
	public Q10392_FailureAndItsConsequences()
	{
		super(false);
		addStartNpc(Iason);
		addTalkId(Eli);
		addKillId(Swamp_Tribe);
		addQuestItem(Sus_frag);
		addLevelCheck(40, 46);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "33859-04.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "33859-06.htm":
				qs.setCond(3);
				qs.takeItems(Sus_frag, 30);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "33858-04.htm":
				qs.giveItems(STEEL_COINS, 17, true);
				qs.addExpAndSp(2329740, 23297);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Iason:
				if ((qs.getPlayer().getLevel() >= 40) && (qs.getPlayer().getLevel() <= 46))
				{
					switch (cond)
					{
						case 0:
							htmltext = "33859-01.htm";
							break;
						
						case 1:
							htmltext = "33859-04a.htm";
							break;
						
						case 2:
							htmltext = "33859-05.htm";
							break;
					}
				}
				else
				{
					htmltext = "noquest";
				}
			case Eli:
				if (cond == 3)
				{
					htmltext = "33858-01.htm";
				}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Util.contains(Swamp_Tribe, npc.getId()) && (qs.getQuestItemsCount(Sus_frag) < 30))
		{
			qs.giveItems(Sus_frag, 1);
			qs.playSound(SOUND_ITEMGET);
		}
		
		if (qs.getQuestItemsCount(Sus_frag) == 30)
		{
			qs.setCond(2);
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