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

import java.util.List;

import lineage2.gameserver.model.Player;

/**
 * @author blacksmoke
 */
public class ExUserInfoAbnormalVisualEffect extends L2GameServerPacket
{
	private final Player _activeChar;
	private final List<Integer> _aveList;
	
	public ExUserInfoAbnormalVisualEffect(Player activeChar)
	{
		_activeChar = activeChar;
		_aveList = activeChar.getAveList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x158);
		writeD(_activeChar.getObjectId());
		writeD(_activeChar.getTransformation());
		if (_aveList != null)
		{
			writeD(_aveList.size());
			
			for (int i : _aveList)
			{
				writeH(i);
			}
		}
	}
}