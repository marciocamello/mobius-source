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
package handlers.admincommands;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminEnchant implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_seteh",
		"admin_setec",
		"admin_seteg",
		"admin_setel",
		"admin_seteb",
		"admin_setew",
		"admin_setes",
		"admin_setle",
		"admin_setre",
		"admin_setlf",
		"admin_setrf",
		"admin_seten",
		"admin_setun",
		"admin_setba",
		"admin_setha",
		"admin_setdha",
		"admin_setlbr",
		"admin_setrbr",
		"admin_setbe",
		"admin_enchant",
		"admin_setlh", // elements
		"admin_setlc",
		"admin_setll",
		"admin_setlg",
		"admin_setlb",
		"admin_setlw",
		"admin_setls"
	};
	
	/**
	 * Method useAdminCommand.
	 * @param command String
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IAdminCommandHandler#useAdminCommand(String, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(String command, String[] wordList, String fullString, Player activeChar)
	{
		if (!activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}
		
		int armorType = -1;
		boolean isElement = false;
		
		switch (command)
		{
			case "admin_enchant":
				showMainPage(activeChar);
				return true;
				
			case "admin_seteh":
				armorType = Inventory.PAPERDOLL_HEAD;
				break;
			
			case "admin_setec":
				armorType = Inventory.PAPERDOLL_CHEST;
				break;
			
			case "admin_seteg":
				armorType = Inventory.PAPERDOLL_GLOVES;
				break;
			
			case "admin_seteb":
				armorType = Inventory.PAPERDOLL_FEET;
				break;
			
			case "admin_setel":
				armorType = Inventory.PAPERDOLL_LEGS;
				break;
			
			case "admin_setew":
				armorType = Inventory.PAPERDOLL_RHAND;
				break;
			
			case "admin_setes":
				armorType = Inventory.PAPERDOLL_LHAND;
				break;
			
			case "admin_setle":
				armorType = Inventory.PAPERDOLL_LEAR;
				break;
			
			case "admin_setre":
				armorType = Inventory.PAPERDOLL_REAR;
				break;
			
			case "admin_setlf":
				armorType = Inventory.PAPERDOLL_LFINGER;
				break;
			
			case "admin_setrf":
				armorType = Inventory.PAPERDOLL_RFINGER;
				break;
			
			case "admin_seten":
				armorType = Inventory.PAPERDOLL_NECK;
				break;
			
			case "admin_setun":
				armorType = Inventory.PAPERDOLL_UNDER;
				break;
			
			case "admin_setba":
				armorType = Inventory.PAPERDOLL_BACK;
				break;
			
			case "admin_setha":
				armorType = Inventory.PAPERDOLL_HAIR;
				break;
			
			case "admin_setdha":
				armorType = Inventory.PAPERDOLL_HAIR;
				break;
			
			case "admin_setlbr":
				armorType = Inventory.PAPERDOLL_LBRACELET;
				break;
			
			case "admin_setrbr":
				armorType = Inventory.PAPERDOLL_RBRACELET;
				break;
			
			case "admin_setbe":
				armorType = Inventory.PAPERDOLL_BELT;
				break;
			
			case "admin_setlh":
				isElement = true;
				armorType = Inventory.PAPERDOLL_HEAD;
				break;
			
			case "admin_setlc":
				isElement = true;
				armorType = Inventory.PAPERDOLL_CHEST;
				break;
			
			case "admin_setlg":
				isElement = true;
				armorType = Inventory.PAPERDOLL_GLOVES;
				break;
			
			case "admin_setlb":
				isElement = true;
				armorType = Inventory.PAPERDOLL_FEET;
				break;
			
			case "admin_setll":
				isElement = true;
				armorType = Inventory.PAPERDOLL_LEGS;
				break;
			
			case "admin_setlw":
				isElement = true;
				armorType = Inventory.PAPERDOLL_RHAND;
				break;
			
			case "admin_setls":
				isElement = true;
				armorType = Inventory.PAPERDOLL_LHAND;
				break;
		}
		
		if ((armorType == -1) || (wordList.length < 2))
		{
			showMainPage(activeChar);
			return true;
		}
		
		if (!isElement)
		{
			try
			{
				int ench = Integer.parseInt(wordList[1]);
				
				if ((ench < 0) || (ench > 65535))
				{
					activeChar.sendMessage("You must set the enchant level to be between 0-65535.");
				}
				else
				{
					setEnchant(activeChar, ench, armorType);
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Please specify a new enchant value.");
			}
			catch (NumberFormatException e)
			{
				activeChar.sendMessage("Please specify a valid new enchant value.");
			}
		}
		else
		{
			try
			{
				Element element = Element.getElementByName(wordList[1]);
				int value = Integer.parseInt(wordList[2]);
				if ((element == null) || (value < 0) || (value > 450))
				{
					activeChar.sendMessage("Usage: //setlh/setlc/setlg/setlb/setll/setlw/setls <element> <value>[0-450]");
					return false;
				}
				
				setElement(activeChar, element, value, armorType);
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //setlh/setlc/setlg/setlb/setll/setlw/setls <element>[0-5] <value>[0-450]");
				return false;
			}
		}
		
		showMainPage(activeChar);
		return true;
	}
	
	/**
	 * Method setEnchant.
	 * @param activeChar Player
	 * @param ench int
	 * @param armorType int
	 */
	private void setEnchant(Player activeChar, int ench, int armorType)
	{
		GameObject target = activeChar.getTarget();
		
		if (target == null)
		{
			target = activeChar;
		}
		
		if (!target.isPlayer())
		{
			activeChar.sendMessage("Wrong target type.");
			return;
		}
		
		Player player = (Player) target;
		int curEnchant = 0;
		ItemInstance itemInstance = player.getInventory().getPaperdollItem(armorType);
		
		if (itemInstance != null)
		{
			curEnchant = itemInstance.getEnchantLevel();
			player.getInventory().unEquipItem(itemInstance);
			itemInstance.setEnchantLevel(ench);
			player.getInventory().equipItem(itemInstance);
			// send packets
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(itemInstance);
			player.sendPacket(iu);
			
			player.broadcastCharInfo();
			activeChar.sendMessage("Changed enchantment of " + player.getName() + "'s " + itemInstance.getName() + " from " + curEnchant + " to " + ench + ".");
			player.sendMessage("Admin has changed the enchantment of your " + itemInstance.getName() + " from " + curEnchant + " to " + ench + ".");
		}
	}
	
	/**
	 * Method setElement.
	 * @param activeChar
	 * @param element
	 * @param value
	 * @param armorType
	 */
	private void setElement(Player activeChar, Element element, int value, int armorType)
	{
		// get the target
		GameObject target = activeChar.getTarget();
		
		if (target == null)
		{
			target = activeChar;
		}
		
		if (!target.isPlayer())
		{
			activeChar.sendMessage("Wrong target type.");
			return;
		}
		
		Player player = (Player) target;
		ItemInstance itemInstance = null;
		
		// only attempt to enchant if there is a weapon equipped
		ItemInstance parmorInstance = player.getInventory().getPaperdollItem(armorType);
		if ((parmorInstance != null) && (parmorInstance.isArmor() || parmorInstance.isWeapon()))
		{
			itemInstance = parmorInstance;
		}
		
		if (itemInstance != null)
		{
			// set enchant value
			player.getInventory().unEquipItem(itemInstance);
			if (element.name() == "NONE")
			{
				itemInstance.setAttributeElement(element, 0);
			}
			else
			{
				itemInstance.setAttributeElement(element, value);
			}
			player.getInventory().equipItem(itemInstance);
			
			// send packets
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(itemInstance);
			player.sendPacket(iu);
		}
	}
	
	/**
	 * Method showMainPage.
	 * @param activeChar Player
	 */
	private void showMainPage(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		
		if (target == null)
		{
			target = activeChar;
		}
		
		Player player = activeChar;
		
		if (target.isPlayer())
		{
			player = (Player) target;
		}
		
		String dialog = HtmCache.getInstance().getNotNull("admin/enchant.htm", player);
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setHtml(dialog);
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IAdminCommandHandler#getAdminCommandList()
	 */
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}
