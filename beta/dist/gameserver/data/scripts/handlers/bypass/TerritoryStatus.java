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
package handlers.bypass;

import lineage2.gameserver.handlers.BypassHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.interfaces.IBypassHandler;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.utils.HtmlUtils;

/**
 * @author Mobius
 */
public final class TerritoryStatus implements IBypassHandler, ScriptFile
{
	/**
	 * Method getBypasses.
	 * @return String[]
	 * @see lineage2.gameserver.model.interfaces.IBypassHandler#getBypasses()
	 */
	@Override
	public String[] getBypasses()
	{
		return new String[]
		{
			"TerritoryStatus"
		};
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param command String
	 * @see lineage2.gameserver.model.interfaces.IBypassHandler#onBypassFeedback(NpcInstance, Player, String)
	 */
	@Override
	public void onBypassFeedback(NpcInstance npc, Player player, String command)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, npc);
		html.setFile("merchant/territorystatus.htm");
		html.replace("%npcname%", npc.getName());
		Castle castle = npc.getCastle(player);
		
		if ((castle != null) && (castle.getId() > 0))
		{
			html.replace("%castlename%", HtmlUtils.htmlResidenceName(castle.getId()));
			html.replace("%taxpercent%", String.valueOf(castle.getTaxPercent()));
			
			if (castle.getOwnerId() > 0)
			{
				Clan clan = ClanTable.getInstance().getClan(castle.getOwnerId());
				
				if (clan != null)
				{
					html.replace("%clanname%", clan.getName());
					html.replace("%clanleadername%", clan.getLeaderName());
				}
				else
				{
					html.replace("%clanname%", "unexistant clan");
					html.replace("%clanleadername%", "None");
				}
			}
			else
			{
				html.replace("%clanname%", "NPC");
				html.replace("%clanleadername%", "None");
			}
		}
		else
		{
			html.replace("%castlename%", "Open");
			html.replace("%taxpercent%", "0");
			html.replace("%clanname%", "No");
			html.replace("%clanleadername%", npc.getName());
		}
		
		player.sendPacket(html);
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		BypassHandler.getInstance().registerBypass(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}