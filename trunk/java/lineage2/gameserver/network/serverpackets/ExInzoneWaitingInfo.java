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

import java.util.Map;

import javolution.util.FastMap;
import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.model.Player;

/**
 * @author KilRoy
 */
public class ExInzoneWaitingInfo extends L2GameServerPacket
{
	private int instanceZoneId = -1;
	private final Map<Integer, Integer> collapseInstanceTime;
	
	public ExInzoneWaitingInfo(Player player)
	{
		collapseInstanceTime = new FastMap<>();
		if (player.getActiveReflection() != null)
		{
			instanceZoneId = player.getActiveReflection().getInstancedZoneId();
		}
		for (int i : player.getInstanceReuses().keySet())
		{
			int timeToCollapse = InstantZoneHolder.getInstance().getMinutesToNextEntrance(i, player);
			if (timeToCollapse > 0)
			{
				collapseInstanceTime.put(i, timeToCollapse * 60);
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x124);
		writeD(instanceZoneId);
		writeD(collapseInstanceTime.size());
		for (Integer integer : collapseInstanceTime.keySet())
		{
			int currentInstanceId = integer.intValue();
			writeD(currentInstanceId);
			writeD((collapseInstanceTime.get(currentInstanceId)));
		}
	}
}