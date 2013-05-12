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

import lineage2.gameserver.instancemanager.commission.CommissionItemInfo;

/**
 * @author : Darvin
 */
public class ExResponseCommissionBuyInfo extends L2GameServerPacket
{
	private final CommissionItemInfo _itemInfo;
	
	public ExResponseCommissionBuyInfo(CommissionItemInfo itemInfo)
	{
		_itemInfo = itemInfo;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xF8);
		writeD(1); // listSize
		writeQ(_itemInfo.getRegisteredPrice());
		writeQ(_itemInfo.getAuctionId());
		writeD(_itemInfo.getExItemType().ordinal());
		writeD(0x00); // unk maybe objId?
		writeD(_itemInfo.getItem().getItemId());
		writeD(_itemInfo.getItem().getEquipSlot());
		writeQ(_itemInfo.getItem().getCount());
		writeH(_itemInfo.getItem().getTemplate().getType2ForPackets());
		writeH(_itemInfo.getItem().getCustomType1());
		writeH(_itemInfo.getItem().isEquipped() ? 1 : 0);
		writeD(_itemInfo.getItem().getBodyPart());
		writeH(_itemInfo.getItem().getEnchantLevel());
		writeH(_itemInfo.getItem().getCustomType2());
		writeD(_itemInfo.getItem().getAugmentationId()); // L2WT TEST!!! D =
															// [HH] [00 00] [00 00]
		writeD(_itemInfo.getItem().getShadowLifeTime());
		writeD(_itemInfo.getItem().getTemporalLifeTime());
		writeH(0x01); // L2WT GOD
		writeH(_itemInfo.getItem().getAttackElement().getId());
		writeH(_itemInfo.getItem().getAttackElementValue());
		writeH(_itemInfo.getItem().getDefenceFire());
		writeH(_itemInfo.getItem().getDefenceWater());
		writeH(_itemInfo.getItem().getDefenceWind());
		writeH(_itemInfo.getItem().getDefenceEarth());
		writeH(_itemInfo.getItem().getDefenceHoly());
		writeH(_itemInfo.getItem().getDefenceUnholy());
		writeH(_itemInfo.getItem().getEnchantOptions()[0]);
		writeH(_itemInfo.getItem().getEnchantOptions()[1]);
		writeH(_itemInfo.getItem().getEnchantOptions()[2]);
	}
}
