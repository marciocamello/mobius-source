/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.handler.items;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.templates.item.ItemTemplate;

public class ItemHandler extends AbstractHolder
{
	private static final ItemHandler _instance = new ItemHandler();
	
	public static ItemHandler getInstance()
	{
		return _instance;
	}
	
	private ItemHandler()
	{
	}
	
	public void registerItemHandler(IItemHandler handler)
	{
		int[] ids = handler.getItemIds();
		for (int itemId : ids)
		{
			ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
			if (template == null)
			{
				warn("Item not found: " + itemId + " handler: " + handler.getClass().getSimpleName());
			}
			else if (template.getHandler() != IItemHandler.NULL)
			{
				warn("Duplicate handler for item: " + itemId + "(" + template.getHandler().getClass().getSimpleName() + "," + handler.getClass().getSimpleName() + ")");
			}
			else
			{
				template.setHandler(handler);
			}
		}
	}
	
	public void unregisterItemHandler(IItemHandler handler)
	{
		for (int itemId : handler.getItemIds())
		{
			ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
			template.setHandler(IItemHandler.NULL);
		}
	}
	
	@Override
	public int size()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
	}
}
