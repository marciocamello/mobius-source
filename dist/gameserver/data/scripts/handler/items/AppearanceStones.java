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
package handler.items;

import lineage2.gameserver.data.xml.holder.EnchantItemHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExChooseShapeShiftingItem;

public final class AppearanceStones extends ScriptItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if ((playable == null) || !playable.isPlayer())
		{
			return false;
		}
		
		Player player = (Player) playable;
		
		if (player.getAppearanceStone() != null)
		{
			return false;
		}
		
		player.setAppearanceStone(item);
		player.sendPacket(new ExChooseShapeShiftingItem(EnchantItemHolder.getInstance().getAppearanceStone(item.getItemId())));
		return true;
	}
	
	@Override
	public int[] getItemIds()
	{
		return EnchantItemHolder.getInstance().getAppearanceStones();
	}
}
