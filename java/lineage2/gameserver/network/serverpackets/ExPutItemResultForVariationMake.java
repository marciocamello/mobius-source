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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExPutItemResultForVariationMake extends L2GameServerPacket
{
	/**
	 * Field _itemObjId.
	 */
	private final int _itemObjId;
	/**
	 * Field _unk1.
	 */
	private final int _unk1;
	/**
	 * Field _unk2.
	 */
	private final int _unk2;
	
	/**
	 * Constructor for ExPutItemResultForVariationMake.
	 * @param itemObjId int
	 */
	public ExPutItemResultForVariationMake(int itemObjId)
	{
		_itemObjId = itemObjId;
		_unk1 = 1;
		_unk2 = 1;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x53);
		writeD(_itemObjId);
		writeD(_unk1);
		writeD(_unk2);
	}
}
