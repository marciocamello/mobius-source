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
 * @author : Darvin
 */
public class ExResponseCommissionBuyItem extends L2GameServerPacket
{
	public static final ExResponseCommissionBuyItem FAILED = new ExResponseCommissionBuyItem(0);
	private final int _code;
	private int _itemId;
	private long _count;
	
	private ExResponseCommissionBuyItem(int code)
	{
		_code = code;
	}
	
	public ExResponseCommissionBuyItem(int code, int itemId, long count)
	{
		_code = code;
		_itemId = itemId;
		_count = count;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xF9);
		writeD(_code);
		
		if (_code == 0)
		{
			return;
		}
		
		writeD(0x00); // unk, maybe item object Id
		writeD(_itemId);
		writeQ(_count);
	}
}
