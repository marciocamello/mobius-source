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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00149_PrimalMotherIstina extends Quest implements ScriptFile
{
	// Npc
	private static final int Rumiese = 33293;
	// Monster
	private static final int Istina = 29195;
	// Items
	private static final int ShilensMark = 17589;
	private static final int IstinasBracelet = 19455;
	private static final int EnchantScrollArmor = 17527;
	
	public Q00149_PrimalMotherIstina()
	{
		super(false);
		addStartNpc(Rumiese);
		addTalkId(Rumiese);
		addKillId(Istina);
		addQuestItem(ShilensMark);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("33293-06.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		if (cond == 0)
		{
			if (player.getLevel() < 90)
			{
				htmltext = "33293-02.htm";
				qs.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "33293-01.htm";
			}
		}
		else if (cond == 1)
		{
			htmltext = "33293-07.htm";
		}
		else if ((cond == 2) || (qs.getQuestItemsCount(ShilensMark) >= 1))
		{
			htmltext = "33293-08.htm";
			qs.addExpAndSp(833065000, 368800464);
			qs.giveItems(IstinasBracelet, 1);
			qs.giveItems(EnchantScrollArmor, 10);
			qs.setState(COMPLETED);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Party party = qs.getPlayer().getParty();
		
		if (qs.getCond() == 1)
		{
			if (party == null)
			{
				qs.setCond(2);
				qs.giveItems(ShilensMark, 1);
				qs.playSound(SOUND_MIDDLE);
			}
			else
			{
				for (Player pmember : party.getPartyMembers())
				{
					final QuestState pst = pmember.getQuestState(Q00149_PrimalMotherIstina.class);
					if ((pst != null) && (pst.getCond() == 1))
					{
						pst.setCond(2);
						pst.giveItems(ShilensMark, 1);
						pst.playSound("SOUND_MIDDLE");
					}
				}
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
