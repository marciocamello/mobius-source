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
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.boat.AirShip;
import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.utils.Location;

public class ExAirShipInfo extends L2GameServerPacket
{
	private final int _objId, _speed1, _speed2;
	private int _fuel;
	private int _maxFuel;
	private int _driverObjId;
	private int _controlKey;
	private final Location _loc;
	
	public ExAirShipInfo(AirShip ship)
	{
		_objId = ship.getObjectId();
		_loc = ship.getLoc();
		_speed1 = ship.getRunSpeed();
		_speed2 = ship.getRotationSpeed();
		if (ship.isClanAirShip())
		{
			_fuel = ((ClanAirShip) ship).getCurrentFuel();
			_maxFuel = ((ClanAirShip) ship).getMaxFuel();
			Player driver = ((ClanAirShip) ship).getDriver();
			_driverObjId = driver == null ? 0 : driver.getObjectId();
			_controlKey = ((ClanAirShip) ship).getControlKey().getObjectId();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x60);
		writeD(_objId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
		writeD(_driverObjId);
		writeD(_speed1);
		writeD(_speed2);
		writeD(_controlKey);
		if (_controlKey != 0)
		{
			writeD(0x16e);
			writeD(0x00);
			writeD(0x6b);
			writeD(0x15c);
			writeD(0x00);
			writeD(0x69);
		}
		else
		{
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
		}
		writeD(_fuel);
		writeD(_maxFuel);
	}
}
