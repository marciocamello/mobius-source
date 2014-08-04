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

import lineage2.gameserver.model.Player;

/**
 * Seven Signs Record Update
 * <p/>
 * packet type id 0xf5 format:
 * <p/>
 * c cc (Page Num = 1 -> 4, period)
 * <p/>
 * 1: [ddd cc dd ddd c ddd c] 2: [hc [cd (dc (S))] 3: [ccc (cccc)] 4: [(cchh)]
 */
public class SSQStatus extends L2GameServerPacket
{
	private final int _page;
	private int period;
	
	public SSQStatus(Player player, int recordPage)
	{
		_page = recordPage;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfb);
		writeC(_page);
		writeC(period); // current period?
	}
}