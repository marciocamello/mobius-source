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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00727_HopeWithinTheDarkness extends Quest implements ScriptFile
{
	// Item
	private static final int KnightsEpaulette = 9912;
	// Monster
	private static final int KanadisGuide3 = 25661;
	
	public Q00727_HopeWithinTheDarkness()
	{
		super(true);
		addStartNpc(36403, 36404, 36405, 36406, 36407, 36408, 36409, 36410, 36411);
		addKillId(KanadisGuide3);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "dcw_q727_4.htm":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "reward":
				if ((cond == 1) && player.getVar("q727").equals("done"))
				{
					player.unsetVar("q727");
					qs.giveItems(KnightsEpaulette, 300);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
					return null;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		final QuestState qs726 = player.getQuestState(Q00726_LightWithinTheDarkness.class);
		
		if (!check(qs.getPlayer()))
		{
			qs.exitCurrentQuest(true);
			return "dcw_q727_1a.htm";
		}
		
		if (qs726 != null)
		{
			qs.exitCurrentQuest(true);
			return "dcw_q727_1b.htm";
		}
		else if (cond == 0)
		{
			if (qs.getPlayer().getLevel() >= 70)
			{
				htmltext = "dcw_q727_1.htm";
			}
			else
			{
				htmltext = "dcw_q727_0.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			if ((player.getVar("q727") != null) && player.getVar("q727").equals("done"))
			{
				htmltext = "dcw_q727_6.htm";
			}
			else
			{
				htmltext = "dcw_q727_5.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if ((qs.getCond() == 1) && (npc.getId() == KanadisGuide3) && checkAllDestroyed(KanadisGuide3, player.getReflectionId()))
		{
			if (player.isInParty())
			{
				for (Player member : player.getParty().getPartyMembers())
				{
					if (!member.isDead() && member.getParty().isInReflection())
					{
						member.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTE_S_YOU_WILL_BE_FORCED_OUT_OF_THE_DUNGEON_WHEN_THE_TIME_EXPIRES).addInt(5));
						member.setVar("q727", "done", -1);
					}
				}
			}
			
			player.getReflection().startCollapseTimer(5 * 60 * 1000L);
		}
		
		return null;
	}
	
	private static boolean checkAllDestroyed(int mobId, int refId)
	{
		for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(mobId, true))
		{
			if (npc.getReflectionId() == refId)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean check(Player player)
	{
		final Castle castle = ResidenceHolder.getInstance().getResidenceByObject(Castle.class, player);
		
		if (castle == null)
		{
			return false;
		}
		
		final Clan clan = player.getClan();
		
		if (clan == null)
		{
			return false;
		}
		
		if (clan.getClanId() != castle.getOwnerId())
		{
			return false;
		}
		
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
