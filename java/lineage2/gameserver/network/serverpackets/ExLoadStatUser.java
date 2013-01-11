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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.instancemanager.MuseumManager;
import lineage2.gameserver.model.Player;

public class ExLoadStatUser extends L2GameServerPacket
{
	private static final String _S__FE_101_EXLOADSTATUSER = "[S] FE:101 ExLoadStatUser";
	Player player;
	List<String[]> stats;
	
	public ExLoadStatUser(Player _player)
	{
		stats = new ArrayList<>();
		player = _player;
		for (String[] information : MuseumManager.getInstance().getLoadingInfo())
		{
			int category = Integer.parseInt(information[2]);
			if (category != 2)
			{
				if ((player.getAcquiredItem(category, false) > 0) || (player.getAcquiredItem(category, true) > 0))
				{
					stats.add((category + " " + player.getAcquiredItem(category, false) + " " + player.getAcquiredItem(category, true)).split(" "));
				}
			}
			else
			{
				if ((player.getOnlineTime(false) > 0) || (player.getOnlineTime(true) > 0))
				{
					stats.add((category + " " + player.getOnlineTime(false) + " " + player.getOnlineTime(true)).split(" "));
				}
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x101);
		writeD(stats.size());
		for (String[] information : stats)
		{
			writeQ(Long.parseLong(information[0]));
			writeQ(Long.parseLong(information[1]));
			writeQ(Long.parseLong(information[2]));
		}
	}
	
	@Override
	public String getType()
	{
		return _S__FE_101_EXLOADSTATUSER;
	}
}
