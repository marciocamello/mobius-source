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
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharMoveToLocation extends L2GameServerPacket
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _client_z_shift.
	 */
	private int _client_z_shift;
	/**
	 * Field _current.
	 */
	private final Location _current;
	/**
	 * Field _destination.
	 */
	private Location _destination;
	
	/**
	 * Constructor for CharMoveToLocation.
	 * @param cha Creature
	 */
	public CharMoveToLocation(Creature cha)
	{
		_objectId = cha.getObjectId();
		_current = cha.getLoc();
		_destination = cha.getDestination();
		if (!cha.isFlying())
		{
			_client_z_shift = Config.CLIENT_Z_SHIFT;
		}
		if (cha.isInWater())
		{
			_client_z_shift += Config.CLIENT_Z_SHIFT;
		}
		if (_destination == null)
		{
			Log.debug("CharMoveToLocation: desc is null, but moving. L2Character: " + cha.getObjectId() + ":" + cha.getName() + "; Loc: " + _current);
			_destination = _current;
		}
	}
	
	/**
	 * Constructor for CharMoveToLocation.
	 * @param objectId int
	 * @param from Location
	 * @param to Location
	 */
	public CharMoveToLocation(int objectId, Location from, Location to)
	{
		_objectId = objectId;
		_current = from;
		_destination = to;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x2F);
		writeD(_objectId);
		writeD(_destination.x);
		writeD(_destination.y);
		writeD(_destination.z + _client_z_shift);
		writeD(_current.x);
		writeD(_current.y);
		writeD(_current.z + _client_z_shift);
	}
}
