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
import lineage2.gameserver.model.items.ManufactureItem;

public class RecipeShopSellList extends L2GameServerPacket
{
	private final int objId, curMp, maxMp;
	private final long adena;
	private final List<ManufactureItem> createList;
	
	public RecipeShopSellList(Player buyer, Player manufacturer)
	{
		objId = manufacturer.getObjectId();
		curMp = (int) manufacturer.getCurrentMp();
		maxMp = manufacturer.getMaxMp();
		adena = buyer.getAdena();
		createList = manufacturer.getCreateList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xdf);
		writeD(objId);
		writeD(curMp);// Creator's MP
		writeD(maxMp);// Creator's MP
		writeQ(adena);
		writeD(createList.size());
		for (ManufactureItem mi : createList)
		{
			writeD(mi.getRecipeId());
			writeD(0x00); // unknown
			writeQ(mi.getCost());
		}
	}
}