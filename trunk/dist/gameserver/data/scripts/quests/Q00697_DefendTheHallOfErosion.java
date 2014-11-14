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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00697_DefendTheHallOfErosion extends Quest implements ScriptFile
{
	// Npc
	private static final int TEPIOS = 32603;
	// Item
	private static final int VesperNobleEnhanceStone = 14052;
	
	public Q00697_DefendTheHallOfErosion()
	{
		super(PARTY_ALL);
		addStartNpc(TEPIOS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		qs.getPlayer();
		String htmltext = event;
		
		if (event.equals("tepios_q697_3.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		
		switch (qs.getCond())
		{
			case 0:
				if (player.getLevel() < 75)
				{
					qs.exitCurrentQuest(true);
					return "tepios_q697_0.htm";
				}
				if (SoIManager.getCurrentStage() != 4)
				{
					qs.exitCurrentQuest(true);
					return "tepios_q697_0a.htm";
				}
				htmltext = "tepios_q697_1.htm";
				break;
			
			case 1:
				if (qs.getInt("defenceDone") == 0)
				{
					htmltext = "tepios_q697_4.htm";
				}
				else
				{
					qs.giveItems(VesperNobleEnhanceStone, Rnd.get(12, 20));
					htmltext = "tepios_q697_5.htm";
					qs.playSound(SOUND_FINISH);
					qs.unset("defenceDone");
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
