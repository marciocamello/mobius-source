/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.entity.boat;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExAirShipInfo;
import lineage2.gameserver.network.serverpackets.ExGetOffAirShip;
import lineage2.gameserver.network.serverpackets.ExGetOnAirShip;
import lineage2.gameserver.network.serverpackets.ExMoveToLocationAirShip;
import lineage2.gameserver.network.serverpackets.ExMoveToLocationInAirShip;
import lineage2.gameserver.network.serverpackets.ExStopMoveAirShip;
import lineage2.gameserver.network.serverpackets.ExStopMoveInAirShip;
import lineage2.gameserver.network.serverpackets.ExValidateLocationInAirShip;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.utils.Location;

public class AirShip extends Boat
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AirShip(int objectId, CharTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public L2GameServerPacket infoPacket()
	{
		return new ExAirShipInfo(this);
	}
	
	@Override
	public L2GameServerPacket movePacket()
	{
		return new ExMoveToLocationAirShip(this);
	}
	
	@Override
	public L2GameServerPacket inMovePacket(Player player, Location src, Location desc)
	{
		return new ExMoveToLocationInAirShip(player, this, src, desc);
	}
	
	@Override
	public L2GameServerPacket stopMovePacket()
	{
		return new ExStopMoveAirShip(this);
	}
	
	@Override
	public L2GameServerPacket inStopMovePacket(Player player)
	{
		return new ExStopMoveInAirShip(player);
	}
	
	@Override
	public L2GameServerPacket startPacket()
	{
		return null;
	}
	
	@Override
	public L2GameServerPacket checkLocationPacket()
	{
		return null;
	}
	
	@Override
	public L2GameServerPacket validateLocationPacket(Player player)
	{
		return new ExValidateLocationInAirShip(player);
	}
	
	@Override
	public L2GameServerPacket getOnPacket(Playable playable, Location location)
	{
		return new ExGetOnAirShip(playable, this, location);
	}
	
	@Override
	public L2GameServerPacket getOffPacket(Playable playable, Location location)
	{
		return new ExGetOffAirShip(playable, this, location);
	}
	
	@Override
	public boolean isAirShip()
	{
		return true;
	}
	
	@Override
	public void oustPlayers()
	{
		for (Player player : _players)
		{
			oustPlayer(player, getReturnLoc(), true);
		}
	}
}
