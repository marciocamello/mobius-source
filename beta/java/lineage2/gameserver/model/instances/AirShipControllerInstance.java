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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AirShipControllerInstance extends NpcInstance
{
	
	/**
	 * Constructor for AirShipControllerInstance.
	 * @param objectID int
	 * @param template NpcTemplate
	 */
	public AirShipControllerInstance(int objectID, NpcTemplate template)
	{
		super(objectID, template);
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
		
		if (command.equals("board"))
		{
			SystemMessage msg = canBoard(player);
			
			if (msg != null)
			{
				player.sendPacket(msg);
				return;
			}
			
			Boat boat = getDockedAirShip();
			
			if (boat == null)
			{
				player.sendActionFailed();
				return;
			}
			
			if ((player.getBoat() != null) && (player.getBoat().getObjectId() != boat.getObjectId()))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_ALREADY_BOARDED_ANOTHER_AIRSHIP));
				return;
			}
			
			player._stablePoint = player.getLoc().setHeading(0);
			boat.addPlayer(player, new Location());
		}
		else if (command.equals("hellfireenter"))
		{
			if (player.getLevel() < 97)
			{
				showChatWindow(player, "default/hellfire-notp.htm");
				return;
			}
			
			player.teleToLocation(-147711, 152768, -14056);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getDockedAirShip.
	 * @return Boat
	 */
	protected Boat getDockedAirShip()
	{
		for (Creature cha : World.getAroundCharacters(this, 1000, 500))
		{
			if (cha.isAirShip() && ((Boat) cha).isDocked())
			{
				return (Boat) cha;
			}
		}
		
		return null;
	}
	
	/**
	 * Method canBoard.
	 * @param player Player
	 * @return SystemMessage
	 */
	private static SystemMessage canBoard(Player player)
	{
		if (player.getTransformation() != 0)
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_TRANSFORMED);
		}
		
		if (player.isParalyzed())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_PETRIFIED);
		}
		
		if (player.isDead() || player.isFakeDeath())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_DEAD);
		}
		
		if (player.isFishing())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_FISHING);
		}
		
		if (player.isInCombat())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_BATTLE);
		}
		
		if (player.isInDuel())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_A_DUEL);
		}
		
		if (player.isSitting())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_SITTING);
		}
		
		if (player.isCastingNow())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_CASTING);
		}
		
		if (player.isCursedWeaponEquipped())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHEN_A_CURSED_WEAPON_IS_EQUIPPED);
		}
		
		if (player.getActiveWeaponFlagAttachment() != null)
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_HOLDING_A_FLAG);
		}
		
		if ((player.getSummonList().size() > 0) || player.isMounted())
		{
			return SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_A_PET_OR_A_SERVITOR_IS_SUMMONED);
		}
		
		return null;
	}
}
