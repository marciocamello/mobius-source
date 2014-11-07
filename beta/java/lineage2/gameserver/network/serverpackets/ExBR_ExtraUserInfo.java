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

public class ExBR_ExtraUserInfo extends L2GameServerPacket
{
	@SuppressWarnings("unused")
	private final int _objectId;
	@SuppressWarnings("unused")
	private final List<Integer> _effect;
	
	public ExBR_ExtraUserInfo(Player cha)
	{
		_objectId = cha.getObjectId();
		_effect = cha.getAveList();
	}
	
	@Override
	protected void writeImpl()
	{
		/*
		 * writeEx(0xDB); writeD(_objectId); // object id of player if (_effect != null) { writeD(_effect.size()); for (int i : _effect) { writeD(i); } } else { writeD(0x00); } writeC(0);
		 */
	}
}