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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.PremiumItem;
import lineage2.gameserver.network.serverpackets.ExGoodsInventoryInfo;
import lineage2.gameserver.network.serverpackets.ExGoodsInventoryResult;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Smo
 */
public class RequestUseGoodsInventoryItem extends L2GameClientPacket
{
	private long _itemNum;
	private int _unk1;
	private long _itemcount;
	
	@Override
	protected void readImpl()
	{
		_unk1 = readC();
		_itemNum = readQ();
		if (_unk1 != 1)
		{
			_itemcount = readQ();
		}
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.getPrivateStoreType() != 0)
		{
			activeChar.sendPacket(new ExGoodsInventoryResult(-5));
			return;
		}
		
		if (activeChar.getPremiumItemList().isEmpty())
		{
			activeChar.sendPacket(new ExGoodsInventoryResult(-6));
			return;
		}
		
		if (activeChar.getInventory().getSize() >= (activeChar.getInventoryLimit() * 0.8))
		{
			activeChar.sendPacket(new ExGoodsInventoryResult(-3));
			return;
		}
		
		PremiumItem _item = activeChar.getPremiumItemList().get((int) _itemNum);
		
		if (_item == null)
		{
			return;
		}
		if ((_itemcount != 0L) && (_item.getCount() < _itemcount))
		{
			return;
		}
		
		ItemFunctions.addItem(activeChar, _item.getItemId(), _itemcount, true);
		
		long itemsLeft = _item.getCount() - _itemcount;
		
		if (_itemcount < _item.getCount())
		{
			_item.updateCount(itemsLeft);
			activeChar.updatePremiumItem((int) _itemNum, _item.getCount() - _itemcount);
		}
		else
		{
			activeChar.deletePremiumItem((int) _itemNum);
		}
		
		activeChar.sendPacket(new ExGoodsInventoryInfo(activeChar.getPremiumItemList()));
	}
	
	@Override
	public String getType()
	{
		return "[C] D0:B2 RequestUseGoodsInventoryItem";
	}
}
