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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExResponseCommissionList extends L2GameServerPacket
{
	/**
	 * Field EMPTY_LIST. (value is -2)
	 */
	public static final int EMPTY_LIST = -2;
	/**
	 * Field PLAYER_REGISTERED_ITEMS. (value is 2)
	 */
	public static final int PLAYER_REGISTERED_ITEMS = 2;
	/**
	 * Field ALL_ITEMS. (value is 3)
	 */
	public static final int ALL_ITEMS = 3;
	/**
	 * Field type.
	 */
	private final int type;
	/**
	 * Field currentTime.
	 */
	private int currentTime;
	/**
	 * Field part.
	 */
	private int part;
	/**
	 * Field items.
	 */
	private List<CommissionItemInfo> items;
	
	/**
	 * Constructor for ExResponseCommissionList.
	 * @param type int
	 */
	public ExResponseCommissionList(int type)
	{
		this.type = type;
	}
	
	/**
	 * Constructor for ExResponseCommissionList.
	 * @param type int
	 * @param part int
	 * @param items List<CommissionItemInfo>
	 */
	public ExResponseCommissionList(int type, int part, List<CommissionItemInfo> items)
	{
		this.type = type;
		this.part = part;
		this.items = items;
		currentTime = (int) (System.currentTimeMillis() / 1000);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xF6);
		writeD(type);
		if (type == EMPTY_LIST)
		{
			return;
		}
		writeD(currentTime);
		writeD(part);
		writeD(items.size());
		for (CommissionItemInfo itemInfo : items)
		{
			writeQ(itemInfo.getAuctionId());
			writeQ(itemInfo.getRegisteredPrice());
			writeD(itemInfo.getExItemType().ordinal());
			writeD(itemInfo.getSaleDays());
			writeD((int) (itemInfo.getSaleEndTime() / 1000));
			writeS(itemInfo.getSellerName());
			writeD(0);
			writeD(itemInfo.getItem().getItemId());
			writeQ(itemInfo.getItem().getCount());
			writeH(itemInfo.getItem().getTemplate().getType2ForPackets());
			writeD(itemInfo.getItem().getBodyPart());
			writeH(itemInfo.getItem().getEnchantLevel());
			writeH(itemInfo.getItem().getCustomType2());
			writeD(0);
			writeH(itemInfo.getItem().getAttackElement().getId());
			writeH(itemInfo.getItem().getAttackElementValue());
			writeH(itemInfo.getItem().getDefenceFire());
			writeH(itemInfo.getItem().getDefenceWater());
			writeH(itemInfo.getItem().getDefenceWind());
			writeH(itemInfo.getItem().getDefenceEarth());
			writeH(itemInfo.getItem().getDefenceHoly());
			writeH(itemInfo.getItem().getDefenceUnholy());
			writeH(itemInfo.getItem().getEnchantOptions()[0]);
			writeH(itemInfo.getItem().getEnchantOptions()[1]);
			writeH(itemInfo.getItem().getEnchantOptions()[2]);
		}
	}
}
