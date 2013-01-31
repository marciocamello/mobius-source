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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RecipeShopItemInfo extends L2GameServerPacket
{
	/**
	 * Field _maxMp. Field _curMp. Field _shopId. Field _recipeId.
	 */
	private final int _recipeId, _shopId, _curMp, _maxMp;
	/**
	 * Field _success.
	 */
	private int _success = 0xFFFFFFFF;
	/**
	 * Field _price.
	 */
	private final long _price;
	
	/**
	 * Constructor for RecipeShopItemInfo.
	 * @param activeChar Player
	 * @param manufacturer Player
	 * @param recipeId int
	 * @param price long
	 * @param success int
	 */
	public RecipeShopItemInfo(Player activeChar, Player manufacturer, int recipeId, long price, int success)
	{
		_recipeId = recipeId;
		_shopId = manufacturer.getObjectId();
		_price = price;
		_success = success;
		_curMp = (int) manufacturer.getCurrentMp();
		_maxMp = manufacturer.getMaxMp();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xe0);
		writeD(_shopId);
		writeD(_recipeId);
		writeD(_curMp);
		writeD(_maxMp);
		writeD(_success);
		writeQ(_price);
	}
}
