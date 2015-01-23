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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.RecipeHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ManufactureItem;
import lineage2.gameserver.network.serverpackets.RecipeShopItemInfo;
import lineage2.gameserver.network.serverpackets.StatusUpdate.StatusUpdateField;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.item.RecipeTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.TradeHelper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRecipeShopMakeDo extends L2GameClientPacket
{
	private int _manufacturerId;
	private int _recipeId;
	private long _price;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_manufacturerId = readD();
		_recipeId = readD();
		_price = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player buyer = getClient().getActiveChar();
		
		if (buyer == null)
		{
			return;
		}
		
		if (buyer.isActionsDisabled())
		{
			buyer.sendActionFailed();
			return;
		}
		
		if (buyer.isInStoreMode())
		{
			buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM));
			return;
		}
		
		if (buyer.isInTrade())
		{
			buyer.sendActionFailed();
			return;
		}
		
		if (buyer.isFishing())
		{
			buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_DO_THAT_WHILE_FISHING3));
			return;
		}
		
		if (!buyer.getPlayerAccess().UseTrade)
		{
			buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_AREN_T_ALLOWED_BUY_ITEMS_FROM_PRIVATE_STORES_TO_UNLOCK_ALL_OF_THE_FEATURES_OF_LINEAGE_II_PURCHASE_THE_FULL_VERSION_TODAY));
			return;
		}
		
		Player manufacturer = (Player) buyer.getVisibleObject(_manufacturerId);
		
		if ((manufacturer == null) || (manufacturer.getPrivateStoreType() != Player.STORE_PRIVATE_MANUFACTURE) || !manufacturer.isInRangeZ(buyer, Creature.INTERACTION_DISTANCE))
		{
			buyer.sendActionFailed();
			return;
		}
		
		RecipeTemplate recipe = null;
		
		for (ManufactureItem mi : manufacturer.getCreateList())
		{
			if (mi.getRecipeId() == _recipeId)
			{
				if (_price == mi.getCost())
				{
					recipe = RecipeHolder.getInstance().getRecipeByRecipeId(_recipeId);
					break;
				}
			}
		}
		
		if (recipe == null)
		{
			buyer.sendActionFailed();
			return;
		}
		
		if (recipe.getMaterials().length == 0)
		{
			manufacturer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_RECIPE_IS_INCORRECT));
			buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_RECIPE_IS_INCORRECT));
			return;
		}
		
		if (!manufacturer.findRecipe(_recipeId))
		{
			buyer.sendActionFailed();
			return;
		}
		
		int success = 0;
		
		if (manufacturer.getCurrentMp() < recipe.getMpConsume())
		{
			manufacturer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_MP));
			buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_MP), new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
			return;
		}
		
		buyer.getInventory().writeLock();
		
		try
		{
			if (buyer.getAdena() < _price)
			{
				buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA), new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
				return;
			}
			
			RecipeTemplate.RecipeComponent[] materials = recipe.getMaterials();
			
			for (RecipeTemplate.RecipeComponent material : materials)
			{
				if (material.getCount() == 0)
				{
					continue;
				}
				
				ItemInstance item = buyer.getInventory().getItemByItemId(material.getId());
				
				if ((item == null) || (material.getCount() > item.getCount()))
				{
					buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_MATERIALS_TO_PERFORM_THAT_ACTION), new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
					return;
				}
			}
			
			if (!buyer.reduceAdena(_price, false))
			{
				buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA), new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
				return;
			}
			
			for (RecipeTemplate.RecipeComponent material : materials)
			{
				if (material.getCount() == 0)
				{
					continue;
				}
				
				buyer.getInventory().destroyItemByItemId(material.getId(), material.getCount());
				if (material.getCount() > 1)
				{
					buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_S1_S_DISAPPEARED).addItemName(material.getId()).addLong(material.getCount()));
				}
				else
				{
					buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_DISAPPEARED).addItemName(material.getId()));
				}
			}
			
			long tax = TradeHelper.getTax(manufacturer, _price);
			
			if (tax > 0)
			{
				_price -= tax;
			}
			
			manufacturer.addAdena(_price);
		}
		finally
		{
			buyer.getInventory().writeUnlock();
		}
		manufacturer.reduceCurrentMp(recipe.getMpConsume(), null);
		manufacturer.sendStatusUpdate(false, false, StatusUpdateField.CUR_MP);
		RecipeTemplate.RecipeComponent product = recipe.getRandomProduct();
		int itemId = product.getId();
		long itemsCount = product.getCount();
		
		if (Rnd.chance(recipe.getSuccessRate()))
		{
			ItemFunctions.addItem(buyer, itemId, itemsCount, true);
			
			if (itemsCount > 1)
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_CREATED_S3_S2_S_AT_THE_PRICE_OF_S4_ADENA);
				sm.addString(manufacturer.getName());
				sm.addItemName(itemId);
				sm.addLong(itemsCount);
				sm.addLong(_price);
				buyer.sendPacket(sm);
				sm = SystemMessage.getSystemMessage(SystemMessageId.S3_S2_S_HAVE_BEEN_SOLD_TO_C1_FOR_S4_ADENA);
				sm.addString(buyer.getName());
				sm.addItemName(itemId);
				sm.addLong(itemsCount);
				sm.addLong(_price);
				manufacturer.sendPacket(sm);
			}
			else
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_CREATED_S2_AFTER_RECEIVING_S3_ADENA);
				sm.addString(manufacturer.getName());
				sm.addItemName(itemId);
				sm.addLong(_price);
				buyer.sendPacket(sm);
				sm = SystemMessage.getSystemMessage(SystemMessageId.S2_IS_SOLD_TO_C1_FOR_THE_PRICE_OF_S3_ADENA);
				sm.addString(buyer.getName());
				sm.addItemName(itemId);
				sm.addLong(_price);
				manufacturer.sendPacket(sm);
			}
			
			success = 1;
		}
		else
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_FAILED_TO_CREATE_S2_AT_THE_PRICE_OF_S3_ADENA);
			sm.addString(manufacturer.getName());
			sm.addItemName(itemId);
			sm.addLong(_price);
			buyer.sendPacket(sm);
			sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_FAILED_TO_CREATE_S2_FOR_C1_AT_THE_PRICE_OF_S3_ADENA);
			sm.addString(buyer.getName());
			sm.addItemName(itemId);
			sm.addLong(_price);
			manufacturer.sendPacket(sm);
		}
		
		buyer.sendChanges();
		buyer.sendPacket(new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
	}
}
