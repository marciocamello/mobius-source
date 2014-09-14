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
 * @author Smo
 */
public class ExShowOwnthingPos extends L2GameServerPacket
{
	private final List<WardInfo> _wardList = new ArrayList<>(9);
	
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
					_wardList.add(new WardInfo(dominionId, loc.getX(), loc.getY(), loc.getZ()));
				}
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x94);
		writeD(_wardList.size());
		for (WardInfo wardInfo : _wardList)
		{
			writeD(wardInfo.dominionId);
			writeD(wardInfo._x);
			writeD(wardInfo._y);
			writeD(wardInfo._z);
		}
	}
	
	private static class WardInfo
	{
		public final int dominionId, _x, _y, _z;
		
		public WardInfo(int territoryId, int x, int y, int z)
		{
			dominionId = territoryId;
			_x = x;
			_y = y;
			_z = z;
		}
	}
}
