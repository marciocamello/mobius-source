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

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.etcitems.EnchantScrollInfo;
import lineage2.gameserver.model.items.etcitems.EnchantScrollManager;
import lineage2.gameserver.model.items.etcitems.EnchantScrollType;
import lineage2.gameserver.network.serverpackets.EnchantResult;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 */
public class RequestEnchantItem extends AbstractEnchantPacket
{
	private int _objectId;
	private int _catalystObjId;
	private int _EnchantAmount = 1;
	
	/**
	 * Method readImpl.
	 */
	@Override
	public void readImpl()
	{
		_objectId = readD();
		_catalystObjId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		if (!isValidPlayer(player))
		{
			player.setEnchantScroll(null);
			player.sendPacket(EnchantResult.CANCEL);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITIONS));
			player.sendActionFailed();
		}
		
		PcInventory inventory = player.getInventory();
		inventory.writeLock();
		
		try
		{
			ItemInstance item = inventory.getItemByObjectId(_objectId);
			ItemInstance scroll = player.getEnchantScroll();
			ItemInstance catalyst = _catalystObjId > 0 ? inventory.getItemByObjectId(_catalystObjId) : null;
			
			if (!ItemFunctions.checkCatalyst(item, catalyst))
			{
				catalyst = null;
			}
			
			if ((item == null) || (scroll == null))
			{
				player.sendActionFailed();
				return;
			}
			
			EnchantScrollInfo esi = EnchantScrollManager.getScrollInfo(scroll.getId());
			
			if (esi == null)
			{
				player.sendActionFailed();
				return;
			}
			
			if ((item.getEnchantLevel() >= esi.getMax()) || (item.getEnchantLevel() < esi.getMin()))
			{
				player.sendPacket(EnchantResult.CANCEL);
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITIONS));
				player.sendActionFailed();
				return;
			}
			
			if (esi.getType() != EnchantScrollType.SPECIAL)
			{
				if (!checkItem(item, esi))
				{
					player.sendPacket(EnchantResult.CANCEL);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL));
					player.sendActionFailed();
					return;
				}
			}
			
			if ((!inventory.destroyItem(scroll, 1)) || ((catalyst != null) && (!inventory.destroyItem(catalyst, 1))))
			{
				player.sendPacket(EnchantResult.CANCEL);
				player.sendActionFailed();
				return;
			}
			
			boolean equipped = item.isEquipped();
			
			if (equipped)
			{
				inventory.isRefresh = true;
				inventory.unEquipItem(item);
			}
			
			int safeEnchantLevel = item.getTemplate().getBodyPart() == 32768 ? (esi.getSafe() + 1) : esi.getSafe();
			int chance = esi.getChance();
			
			switch (esi.getType())
			{
				case NORMAL:
					if (item.isWeapon())
					{
						chance = Config.ENCHANT_CHANCE_WEAPON;
					}
					else if (item.isArmor())
					{
						chance = Config.ENCHANT_CHANCE_ARMOR;
					}
					else
					{
						chance = Config.ENCHANT_CHANCE_ACCESSORY;
					}
					break;
				
				case BLESSED:
					if (item.isWeapon())
					{
						chance = Config.ENCHANT_CHANCE_BLESSED_WEAPON;
					}
					else if (item.isArmor())
					{
						chance = Config.ENCHANT_CHANCE_BLESSED_ARMOR;
					}
					else
					{
						chance = Config.ENCHANT_CHANCE_BLESSED_ACCESSORY;
					}
					break;
				
				case CRYSTALL:
					if (item.isWeapon())
					{
						chance = Config.ENCHANT_CHANCE_CRYSTAL_WEAPON;
					}
					else if (item.isArmor())
					{
						chance = Config.ENCHANT_CHANCE_CRYSTAL_ARMOR;
					}
					else
					{
						chance = Config.ENCHANT_CHANCE_CRYSTAL_ACCESSORY;
					}
					break;
				case GIANT:
					_EnchantAmount = Rnd.get(1, 3);
					if (item.isWeapon())
					{
						chance = Config.ENCHANT_CHANCE_GIANT_WEAPON;
					}
					else if (item.isArmor())
					{
						chance = Config.ENCHANT_CHANCE_GIANT_ARMOR;
					}
					else
					{
						chance = Config.ENCHANT_CHANCE_GIANT_ACCESSORY;
					}
			}
			if (catalyst != null)
			{
				chance += ItemFunctions.getCatalystPower(catalyst.getId());
			}
			
			if ((esi.getType() == EnchantScrollType.ANCIENT) || (esi.getType() == EnchantScrollType.ITEM_MALL))
			{
				chance += 10;
			}
			
			if (esi.getType() == EnchantScrollType.DIVINE)
			{
				chance = 100;
			}
			
			if (item.getEnchantLevel() <= safeEnchantLevel)
			{
				chance = 100;
			}
			
			chance = Math.min(chance, 100);
			
			if (Rnd.chance(chance))
			{
				item.setEnchantLevel(item.getEnchantLevel() + _EnchantAmount);
				item.setJdbcState(JdbcEntityState.UPDATED);
				item.update();
				
				if (equipped)
				{
					inventory.equipItem(item);
					inventory.isRefresh = false;
				}
				
				// send packets
				InventoryUpdate iu = new InventoryUpdate();
				iu.addModifiedItem(item);
				player.sendPacket(iu);
				
				player.sendPacket(new EnchantResult(0, 0, 0, item.getEnchantLevel()));
				
				if (Config.SHOW_ENCHANT_EFFECT_RESULT)
				{
					player.broadcastPacket(new L2GameServerPacket[]
					{
						SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_SUCCESSFULLY_ENCHANTED_A_S2_S3).addPcName(player).addInt(item.getEnchantLevel()).addItemName(item.getId())
					});
					player.broadcastPacket(new L2GameServerPacket[]
					{
						new MagicSkillUse(player, player, 5965, 1, 500, 1500L)
					});
				}
			}
			else
			{
				switch (esi.getType())
				{
					case NORMAL:
						if (item.isEquipped())
						{
							player.sendDisarmMessage(item);
						}
						
						if (!inventory.destroyItem(item, 1L))
						{
							player.sendActionFailed();
							return;
						}
						
						int crystalId = item.getCrystalType().cry;
						
						if ((crystalId > 0) && (item.getTemplate().getCrystalCount() > 0))
						{
							int crystalAmount = (int) (item.getTemplate().getCrystalCount() * 0.87D);
							
							if (item.getEnchantLevel() > 3)
							{
								crystalAmount = (int) (crystalAmount + (item.getTemplate().getCrystalCount() * 0.25D * (item.getEnchantLevel() - 3)));
							}
							
							if (crystalAmount < 1)
							{
								crystalAmount = 1;
							}
							
							player.sendPacket(new EnchantResult(1, crystalId, crystalAmount));
							ItemFunctions.addItem(player, crystalId, crystalAmount, true);
						}
						else
						{
							player.sendPacket(EnchantResult.FAILED_NO_CRYSTALS);
						}
						break;
					case GIANT:
						if (item.isEquipped())
						{
							player.sendDisarmMessage(item);
						}
						
						if (!inventory.destroyItem(item, 1L))
						{
							player.sendActionFailed();
							return;
						}
						
						crystalId = item.getCrystalType().cry;
						
						if ((crystalId > 0) && (item.getTemplate().getCrystalCount() > 0))
						{
							int crystalAmount = (int) (item.getTemplate().getCrystalCount() * 0.87D);
							
							if (item.getEnchantLevel() > 3)
							{
								crystalAmount = (int) (crystalAmount + (item.getTemplate().getCrystalCount() * 0.25D * (item.getEnchantLevel() - 3)));
							}
							
							if (crystalAmount < 1)
							{
								crystalAmount = 1;
							}
							
							player.sendPacket(new EnchantResult(1, crystalId, crystalAmount));
							ItemFunctions.addItem(player, crystalId, crystalAmount, true);
						}
						else
						{
							player.sendPacket(EnchantResult.FAILED_NO_CRYSTALS);
						}
						break;
					case DESTRUCTION:
						item.setEnchantLevel(item.getEnchantLevel());
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();
						
						if (equipped)
						{
							inventory.equipItem(item);
							inventory.isRefresh = false;
						}
						
						// send packets
						InventoryUpdate iu = new InventoryUpdate();
						iu.addModifiedItem(item);
						player.sendPacket(iu);
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_BLESSED_ENCHANT_FAILED_THE_ENCHANT_VALUE_OF_THE_ITEM_BECAME_0));
						player.sendPacket(new EnchantResult(5, 0, 0, item.getEnchantLevel()));
						break;
					
					case BLESSED:
					case ITEM_MALL:
					case CRYSTALL:
						item.setEnchantLevel(0);
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();
						
						if (equipped)
						{
							inventory.equipItem(item);
							inventory.isRefresh = false;
						}
						
						// send packets
						InventoryUpdate iuc = new InventoryUpdate();
						iuc.addModifiedItem(item);
						player.sendPacket(iuc);
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_BLESSED_ENCHANT_FAILED_THE_ENCHANT_VALUE_OF_THE_ITEM_BECAME_0));
						player.sendPacket(EnchantResult.BLESSED_FAILED);
						break;
					
					case ANCIENT:
						player.sendPacket(new EnchantResult(5, 0, 0, item.getEnchantLevel()));
						break;
					
					default:
						break;
				}
			}
		}
		finally
		{
			inventory.writeUnlock();
			player.setEnchantScroll(null);
			player.updateStats();
		}
	}
}
