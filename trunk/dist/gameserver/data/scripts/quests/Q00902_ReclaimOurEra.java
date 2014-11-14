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

public class Q00902_ReclaimOurEra extends Quest implements ScriptFile
{
	private static final int Mathias = 31340;
	private static final int[] OrcsSilenos =
	{
		25309,
		25312,
		25315,
		25299,
		25302,
		25305
	};
	private static final int[] CannibalisticStakatoChief =
	{
		25667,
		25668,
		25669,
		25670
	};
	private static final int Anais = 25701;
	private static final int ShatteredBones = 21997;
	private static final int CannibalisticStakatoLeaderClaw = 21998;
	private static final int AnaisScroll = 21999;
	
	public Q00902_ReclaimOurEra()
	{
		super(PARTY_ALL);
		addStartNpc(Mathias);
		addKillId(OrcsSilenos);
		addKillId(CannibalisticStakatoChief);
		addKillId(Anais);
		addQuestItem(ShatteredBones, CannibalisticStakatoLeaderClaw, AnaisScroll);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "mathias_q902_04.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "mathias_q902_05.htm":
				qs.setCond(2);
				break;
			
			case "mathias_q902_06.htm":
				qs.setCond(3);
				break;
			
			case "mathias_q902_07.htm":
				qs.setCond(4);
				break;
			
			case "mathias_q902_09.htm":
				if (qs.takeAllItems(ShatteredBones) > 0)
				{
					qs.giveItems(21750, 1);
					qs.giveItems(ADENA_ID, 134038);
				}
				else if (qs.takeAllItems(CannibalisticStakatoLeaderClaw) > 0)
				{
					qs.giveItems(21750, 3);
					qs.giveItems(ADENA_ID, 210119);
				}
				else if (qs.takeAllItems(AnaisScroll) > 0)
				{
					qs.giveItems(21750, 3);
					qs.giveItems(ADENA_ID, 348155);
				}
				qs.setState(COMPLETED);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getState())
		{
			case CREATED:
				if (qs.isNowAvailableByTime())
				{
					if (qs.getPlayer().getLevel() >= 80)
					{
						htmltext = "mathias_q902_01.htm";
					}
					else
					{
						htmltext = "mathias_q902_00.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "mathias_q902_00a.htm";
				}
				break;
			
			case STARTED:
				switch (qs.getCond())
				{
					case 1:
						htmltext = "mathias_q902_04.htm";
						break;
					
					case 2:
						htmltext = "mathias_q902_05.htm";
						break;
					
					case 3:
						htmltext = "mathias_q902_06.htm";
						break;
					
					case 4:
						htmltext = "mathias_q902_07.htm";
						break;
					
					case 5:
						htmltext = "mathias_q902_08.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		switch (qs.getCond())
		{
			case 2:
				if (Util.contains(OrcsSilenos, npc.getId()))
				{
					qs.giveItems(ShatteredBones, 1);
					qs.setCond(5);
				}
				break;
			
			case 3:
				if (Util.contains(CannibalisticStakatoChief, npc.getId()))
				{
					qs.giveItems(CannibalisticStakatoLeaderClaw, 1);
					qs.setCond(5);
				}
				break;
			
			case 4:
				if (npc.getId() == Anais)
				{
					qs.giveItems(AnaisScroll, 1);
					qs.setCond(5);
				}
				break;
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
