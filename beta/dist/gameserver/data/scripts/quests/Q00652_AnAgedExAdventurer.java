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

public class Q00652_AnAgedExAdventurer extends Quest implements ScriptFile
{
	// Npcs
	private static final int Tantan = 32012;
	private static final int Sara = 30180;
	// Items
	private static final int SoulshotCgrade = 1464;
	private static final int ElixirOfMentalStrC = 8630;
	private static final int EnchantArmorC = 952;
	private static final int ElixirOfLife = 8624;
	
	public Q00652_AnAgedExAdventurer()
	{
		super(false);
		addStartNpc(Tantan);
		addTalkId(Sara);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("retired_oldman_tantan_q0652_03.htm") && (qs.getQuestItemsCount(SoulshotCgrade) >= 100))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.takeItems(SoulshotCgrade, 100);
			qs.playSound(SOUND_ACCEPT);
			htmltext = "retired_oldman_tantan_q0652_04.htm";
		}
		else
		{
			htmltext = "retired_oldman_tantan_q0652_03.htm";
			qs.exitCurrentQuest(true);
			qs.playSound(SOUND_GIVEUP);
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
			case Tantan:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() < 46)
					{
						htmltext = "retired_oldman_tantan_q0652_01a.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "retired_oldman_tantan_q0652_01.htm";
					}
				}
				break;
			
			case Sara:
				if (cond == 1)
				{
					htmltext = "sara_q0652_01.htm";
					qs.giveItems(ElixirOfMentalStrC, 2);
					qs.giveItems(EnchantArmorC, 1);
					qs.giveItems(ElixirOfLife, 1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				break;
		}
		
		return htmltext;
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
