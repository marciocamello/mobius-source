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
package npc.model;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.HtmlUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProclaimerInstance extends NpcInstance
{
	public static final Logger _log = LoggerFactory.getLogger(ProclaimerInstance.class);
	private final List<Creature> target = new ArrayList<>();
	
	public ProclaimerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!(canBypassCheck(player, this)))
		{
			return;
		}
		
		if (command.equals("gift"))
		{
			target.add(player);
			Skill skill = SkillTable.getInstance().getInfo(19036, 1);
			
			if (skill != null)
			{
				callSkill(skill, target, false);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		
		Residence castle = getCastle();
		if ((castle != null) && (castle.getId() > 0))
		{
			Clan clan = ClanTable.getInstance().getClan(castle.getOwnerId());
			if (clan != null)
			{
				html.setFile("custom/Proclaimer/index.htm");
				html.replace("%castle%", HtmlUtils.htmlResidenceName(castle.getId()));
				html.replace("%clanName%", clan.getName());
				html.replace("%leaderName%", clan.getLeaderName());
				// return;
			}
			else
			{
				html.setFile("custom/Proclaimer/index_no_clan.htm");
			}
			player.sendPacket(html);
			player.sendPacket(new NpcSay(this, ChatType.TELL, NpcStringId.WHEN_THE_WORLD_PLUNGES_INTO_CHAOS_WE_WILL_NEED_YOUR_HELP_WE_HOPE_YOU_JOIN_US_WHEN_THE_TIME_COMES, new String[0]));
		}
	}
}