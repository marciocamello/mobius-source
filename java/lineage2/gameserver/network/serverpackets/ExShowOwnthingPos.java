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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.entity.events.objects.TerritoryWardObject;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowOwnthingPos extends L2GameServerPacket
{
	/**
	 * Field _wardList.
	 */
	private final List<WardInfo> _wardList = new ArrayList<>(9);
	
	/**
	 * Constructor for ExShowOwnthingPos.
	 */
	public ExShowOwnthingPos()
	{
		for (Dominion dominion : ResidenceHolder.getInstance().getResidenceList(Dominion.class))
		{
			if (dominion.getSiegeDate().getTimeInMillis() == 0)
			{
				continue;
			}
			int[] flags = dominion.getFlags();
			for (int dominionId : flags)
			{
				TerritoryWardObject wardObject = dominion.getSiegeEvent().getFirstObject("ward_" + dominionId);
				Location loc = wardObject.getWardLocation();
				if (loc != null)
				{
					_wardList.add(new WardInfo(dominionId, loc.x, loc.y, loc.z));
				}
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x93);
		writeD(_wardList.size());
		for (WardInfo wardInfo : _wardList)
		{
			writeD(wardInfo.dominionId);
			writeD(wardInfo._x);
			writeD(wardInfo._y);
			writeD(wardInfo._z);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class WardInfo
	{
		/**
		 * Field dominionId.
		 */
		final int dominionId;
		/**
		 * Field _x.
		 */
		final int _x;
		/**
		 * Field _y.
		 */
		final int _y;
		/**
		 * Field _z.
		 */
		final int _z;
		
		/**
		 * Constructor for WardInfo.
		 * @param territoryId int
		 * @param x int
		 * @param y int
		 * @param z int
		 */
		public WardInfo(int territoryId, int x, int y, int z)
		{
			dominionId = territoryId;
			_x = x;
			_y = y;
			_z = z;
		}
	}
}
