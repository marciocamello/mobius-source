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
 * @author Smo
 */
public class ExBlockAddResult extends L2GameServerPacket
{
	private final String _name;
	private final int _blocked;
	
	public ExBlockAddResult(Player cha, boolean blocked)
	{
		_blocked = blocked ? 1 : 0;
		_name = cha.getName();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xED);
		writeD(_blocked);
		if (_blocked > 0)
		{
			writeS(_name);
		}
	}
}
