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

import lineage2.gameserver.model.items.ItemInfo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TradeOtherAdd extends L2GameServerPacket
{
	/**
	 * Field _temp.
	 */
	private final ItemInfo _temp;
	/**
	 * Field _amount.
	 */
	private final long _amount;
	
	/**
	 * Constructor for TradeOtherAdd.
	 * @param item ItemInfo
	 * @param amount long
	 */
	public TradeOtherAdd(ItemInfo item, long amount)
	{
		_temp = item;
		_amount = amount;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x1b);
		writeH(1);
		writeH(_temp.getItem().getType1());
		writeD(_temp.getObjectId());
		writeD(_temp.getItemId());
		writeQ(_amount);
		writeH(_temp.getItem().getType2ForPackets());
		writeH(_temp.getCustomType1());
		writeD(_temp.getItem().getBodyPart());
		writeH(_temp.getEnchantLevel());
		writeH(0x00);
		writeH(_temp.getCustomType2());
		writeH(_temp.getAttackElement());
		writeH(_temp.getAttackElementValue());
		writeH(_temp.getDefenceFire());
		writeH(_temp.getDefenceWater());
		writeH(_temp.getDefenceWind());
		writeH(_temp.getDefenceEarth());
		writeH(_temp.getDefenceHoly());
		writeH(_temp.getDefenceUnholy());
		writeH(0);
		writeH(0);
		writeH(0);
		writeD(0x00);
	}
}
