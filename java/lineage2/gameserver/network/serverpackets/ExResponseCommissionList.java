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

import lineage2.gameserver.instancemanager.commission.CommissionItemInfo;

/**
 * @author : Darvin
 */
public class ExResponseCommissionList extends L2GameServerPacket
{
	public static final int EMPTY_LIST = -2;
	public static final int PLAYER_REGISTERED_ITEMS = 2;
	public static final int ALL_ITEMS = 3;
	private final int type;
	private int currentTime;
	private int part;
	private List<CommissionItemInfo> items;
	
	public ExResponseCommissionList(int type)
	{
		this.type = type;
	}
	
	public ExResponseCommissionList(int type, int part, List<CommissionItemInfo> items)
	{
		this.type = type;
		this.part = part;
		this.items = items;
		currentTime = (int) (System.currentTimeMillis() / 1000);
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xF7);
		writeD(type); // List type.
		
		if (type == EMPTY_LIST)
		{
			return;
		}
		
		writeD(currentTime); // current time
		writeD(part); // part
		writeD(items.size()); // items count
		
		for (CommissionItemInfo itemInfo : items)
		{
			writeQ(itemInfo.getAuctionId()); // auctionId
			writeQ(itemInfo.getRegisteredPrice()); // item price
			writeD(itemInfo.getExItemType().ordinal());
			writeD(itemInfo.getSaleDays());
			writeD((int) (itemInfo.getSaleEndTime() / 1000)); // Sale end time
			writeS(itemInfo.getSellerName()); // seller name
			writeD(0);
			writeD(itemInfo.getItem().getItemId()); // item_id
			writeQ(itemInfo.getItem().getCount()); // count
			writeH(itemInfo.getItem().getTemplate().getType2ForPackets()); // itemType2
			writeD(itemInfo.getItem().getBodyPart()); // bodypart
			writeH(itemInfo.getItem().getEnchantLevel()); // enchant_lvl
			writeH(itemInfo.getItem().getCustomType2()); // custom_type2
			writeD(0x00); // unk
			writeH(itemInfo.getItem().getAttackElement().getId()); // atk_element_id
			writeH(itemInfo.getItem().getAttackElementValue()); // atk_element_val
			writeH(itemInfo.getItem().getDefenceFire()); // fire_defence
			writeH(itemInfo.getItem().getDefenceWater()); // water_defence
			writeH(itemInfo.getItem().getDefenceWind()); // wind_defence
			writeH(itemInfo.getItem().getDefenceEarth()); // earth_defence
			writeH(itemInfo.getItem().getDefenceHoly()); // holy_defence
			writeH(itemInfo.getItem().getDefenceUnholy()); // unholy_defence
			writeH(itemInfo.getItem().getEnchantOptions()[0]); // enchant_opt1
			writeH(itemInfo.getItem().getEnchantOptions()[1]); // enchant_opt2
			writeH(itemInfo.getItem().getEnchantOptions()[2]); // enchant_opt3
		}
	}
}
