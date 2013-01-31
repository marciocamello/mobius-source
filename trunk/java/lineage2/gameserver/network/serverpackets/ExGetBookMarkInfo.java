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
import lineage2.gameserver.model.actor.instances.player.BookMark;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExGetBookMarkInfo extends L2GameServerPacket
{
	/**
	 * Field bookmarksCapacity.
	 */
	private final int bookmarksCapacity;
	/**
	 * Field bookmarks.
	 */
	private final BookMark[] bookmarks;
	
	/**
	 * Constructor for ExGetBookMarkInfo.
	 * @param player Player
	 */
	public ExGetBookMarkInfo(Player player)
	{
		bookmarksCapacity = player.bookmarks.getCapacity();
		bookmarks = player.bookmarks.toArray();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x84);
		writeD(0x00);
		writeD(bookmarksCapacity);
		writeD(bookmarks.length);
		int slotId = 0;
		for (BookMark bookmark : bookmarks)
		{
			writeD(++slotId);
			writeD(bookmark.x);
			writeD(bookmark.y);
			writeD(bookmark.z);
			writeS(bookmark.getName());
			writeD(bookmark.getIcon());
			writeS(bookmark.getAcronym());
		}
	}
}
