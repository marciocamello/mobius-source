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
package npc.model.residences.dominion;

import java.util.StringTokenizer;

import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowDominionRegistry;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MercenaryCaptainInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for MercenaryCaptainInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MercenaryCaptainInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		Dominion dominion = getDominion();
		if (command.equalsIgnoreCase("territory_register"))
		{
			player.sendPacket(new ExShowDominionRegistry(player, dominion));
		}
		else if (command.startsWith("certificate_multisell"))
		{
			StringTokenizer tokenizer = new StringTokenizer(command);
			tokenizer.nextToken();
			int certification = Integer.parseInt(tokenizer.nextToken());
			int multisell = Integer.parseInt(tokenizer.nextToken());
			if (player.getInventory().getCountOf(certification) > 0)
			{
				MultiSellHolder.getInstance().SeparateAndSend(multisell, player, getCastle().getTaxRate());
			}
			else
			{
				showChatWindow(player, 25);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		if ((player.getLevel() < 40) || (player.getClassLevel() <= 2))
		{
			val = 26;
		}
		else
		{
			Castle castle = getCastle();
			Dominion dominion = getDominion();
			if (((castle.getOwner() != null) && (player.getClan() == castle.getOwner())) || (dominion.getLordObjectId() == player.getObjectId()))
			{
				if (castle.getSiegeEvent().isInProgress() || dominion.getSiegeEvent().isInProgress())
				{
					val = 21;
				}
				else
				{
					val = 7;
				}
			}
			else if (castle.getSiegeEvent().isInProgress() || dominion.getSiegeEvent().isInProgress())
			{
				val = 22;
			}
		}
		if (val == 0)
		{
			val = 1;
		}
		return val > 9 ? "residence2/dominion/gludio_merc_captain0" + val + ".htm" : "residence2/dominion/gludio_merc_captain00" + val + ".htm";
	}
}
