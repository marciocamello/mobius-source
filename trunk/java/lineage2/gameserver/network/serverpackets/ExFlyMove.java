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

import lineage2.gameserver.templates.jump.JumpPoint;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExFlyMove extends L2GameServerPacket
{
	/**
	 * Field MANY_WAY_TYPE. (value is 0)
	 */
	public static final int MANY_WAY_TYPE = 0;
	/**
	 * Field ONE_WAY_TYPE. (value is 2)
	 */
	public static final int ONE_WAY_TYPE = 2;
	/**
	 * Field _objId.
	 */
	private final int _objId;
	/**
	 * Field _points.
	 */
	private final JumpPoint[] _points;
	/**
	 * Field _type.
	 */
	private int _type;
	/**
	 * Field _trackId.
	 */
	private final int _trackId;
	
	/**
	 * Constructor for ExFlyMove.
	 * @param objId int
	 * @param points JumpPoint[]
	 * @param trackId int
	 */
	public ExFlyMove(int objId, JumpPoint[] points, int trackId)
	{
		_objId = objId;
		_points = points;
		if (_points.length > 1)
		{
			_type = MANY_WAY_TYPE;
		}
		else
		{
			_type = ONE_WAY_TYPE;
		}
		_trackId = trackId;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xE7);
		writeD(_objId);
		writeD(_type);
		writeD(0x00);
		writeD(_trackId);
		writeD(_points.length);
		for (JumpPoint point : _points)
		{
			writeD(point.getNextWayId());
			writeD(0x00);
			writeD(point.getLocation().getX());
			writeD(point.getLocation().getY());
			writeD(point.getLocation().getZ());
		}
	}
}
