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
import lineage2.gameserver.network.serverpackets.ExQuestNpcLogList;
import lineage2.gameserver.scripts.ScriptFile;
import gnu.trove.map.hash.TIntIntHashMap;

public class Q00472_ChallengeSteamCorridor extends Quest implements ScriptFile
{
	// Npc
	private static final int FIOREN = 33044;
	// Monster
	private static final int KECHI = 25797;
	// Item
	private static final int CERTIFICATE_OF_HELL = 30387;
	
	public Q00472_ChallengeSteamCorridor()
	{
		super(false);
		addStartNpc(FIOREN);
		addTalkId(FIOREN);
		addKillId(KECHI);
		addLevelCheck(97, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (htmltext)
		{
			case "33044-04.htm":
				qs.setState(STARTED);
				break;
			
			case "33044-07.htm":
				qs.playSound(SOUND_FINISH);
				qs.giveItems(CERTIFICATE_OF_HELL, 10);
				qs.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		
		if (player.getLevel() < 97)
		{
			qs.exitCurrentQuest(true);
			return "33044-02.htm";
		}
		else if (qs.getState() == CREATED)
		{
			htmltext = "33044-01.htm";
		}
		else if (qs.getState() == STARTED)
		{
			if (cond == 1)
			{
				htmltext = "33044-05.htm";
			}
			else
			{
				htmltext = "33044-06.htm";
			}
		}
		else if (qs.getState() == COMPLETED)
		{
			htmltext = "33044-08.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if ((npc.getId() == KECHI) && (qs.getCond() == 1))
		{
			TIntIntHashMap moblist = new TIntIntHashMap();
			moblist.put(KECHI, 1);
			
			if (player.getParty() != null)
			{
				for (Player partyMember : player.getParty().getPartyMembers())
				{
					QuestState pst = partyMember.getQuestState("Q00472_ChallengeSteamCorridor");
					
					if ((pst != null) && (pst.isStarted()))
					{
						pst.setCond(2);
						pst.playSound(SOUND_MIDDLE);
						partyMember.sendPacket(new ExQuestNpcLogList(qs));
					}
				}
			}
			else
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				player.sendPacket(new ExQuestNpcLogList(qs));
			}
		}
		
		return null;
	}
	
	public boolean isDailyQuest()
	{
		return true;
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
