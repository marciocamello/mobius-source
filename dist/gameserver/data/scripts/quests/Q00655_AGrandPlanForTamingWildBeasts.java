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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.TimeUtils;

public class Q00655_AGrandPlanForTamingWildBeasts extends Quest implements ScriptFile
{
	// Npc
	private static final int MESSENGER = 35627;
	// Items
	private final static int STONE = 8084;
	private final static int TRAINER_LICENSE = 8293;
	
	public Q00655_AGrandPlanForTamingWildBeasts()
	{
		super(PARTY_NONE);
		addStartNpc(MESSENGER);
		addTalkId(MESSENGER);
		addQuestItem(STONE, TRAINER_LICENSE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmlText = event;
		
		if (event.equals("farm_messenger_q0655_06.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmlText;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmlText = "noquest";
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		final Clan clan = player.getClan();
		final ClanHall clanhall = ResidenceHolder.getInstance().getResidence(63);
		
		if (clanhall.getSiegeEvent().isRegistrationOver())
		{
			htmlText = null;
			showHtmlFile(player, "farm_messenger_q0655_02.htm", false, "%siege_time%", TimeUtils.toSimpleFormat(clanhall.getSiegeDate()));
		}
		else if ((clan == null) || (player.getObjectId() != clan.getLeaderId()))
		{
			htmlText = "farm_messenger_q0655_03.htm";
		}
		else if ((player.getObjectId() == clan.getLeaderId()) && (clan.getLevel() < 4))
		{
			htmlText = "farm_messenger_q0655_05.htm";
		}
		else if (clanhall.getSiegeEvent().getSiegeClan(SiegeEvent.ATTACKERS, player.getClan()) != null)
		{
			htmlText = "farm_messenger_q0655_07.htm";
		}
		else if (clan.getHasHideout() > 0)
		{
			htmlText = "farm_messenger_q0655_04.htm";
		}
		else if (cond == 0)
		{
			htmlText = "farm_messenger_q0655_01.htm";
		}
		else if ((cond == 1) && (qs.getQuestItemsCount(STONE) < 10))
		{
			htmlText = "farm_messenger_q0655_08.htm";
		}
		else if ((cond == 1) && (qs.getQuestItemsCount(STONE) == 10))
		{
			qs.setCond(-1);
			qs.takeItems(STONE, -1);
			qs.giveItems(TRAINER_LICENSE, 1);
			htmlText = "farm_messenger_q0655_10.htm";
		}
		else if (qs.getQuestItemsCount(TRAINER_LICENSE) == 1)
		{
			htmlText = "farm_messenger_q0655_09.htm";
		}
		
		return htmlText;
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
