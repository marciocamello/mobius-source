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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExFishingEnd extends L2GameServerPacket
{
	/**
	 * Field _charId.
	 */
	private final int _charId;
	/**
	 * Field _win.
	 */
	private final boolean _win;
	
	/**
	 * Constructor for ExFishingEnd.
	 * @param character Player
	 * @param win boolean
	 */
	public ExFishingEnd(Player character, boolean win)
	{
		_charId = character.getObjectId();
		_win = win;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x1f);
		writeD(_charId);
		writeC(_win ? 1 : 0);
	}
}
