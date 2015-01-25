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
package lineage2.gameserver.model.items;

import java.util.Comparator;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.listener.inventory.OnEquipListener;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.model.items.listeners.StatsListener;
import lineage2.gameserver.templates.item.EtcItemTemplate.EtcItemType;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 */
public abstract class Inventory extends ItemContainer
{
	private static final Logger _log = LoggerFactory.getLogger(Inventory.class);
	
	public static final int PAPERDOLL_UNDER = 0;
	public static final int PAPERDOLL_REAR = 1;
	public static final int PAPERDOLL_LEAR = 2;
	public static final int PAPERDOLL_NECK = 3;
	public static final int PAPERDOLL_RFINGER = 4;
	public static final int PAPERDOLL_LFINGER = 5;
	public static final int PAPERDOLL_HEAD = 6;
	public static final int PAPERDOLL_RHAND = 7;
	public static final int PAPERDOLL_LHAND = 8;
	public static final int PAPERDOLL_GLOVES = 9;
	public static final int PAPERDOLL_CHEST = 10;
	public static final int PAPERDOLL_LEGS = 11;
	public static final int PAPERDOLL_FEET = 12;
	public static final int PAPERDOLL_BACK = 13;
	public static final int PAPERDOLL_LRHAND = 14;
	public static final int PAPERDOLL_HAIR = 15;
	public static final int PAPERDOLL_DHAIR = 16;
	public static final int PAPERDOLL_RBRACELET = 17;
	public static final int PAPERDOLL_LBRACELET = 18;
	public static final int PAPERDOLL_DECO1 = 19;
	public static final int PAPERDOLL_DECO2 = 20;
	public static final int PAPERDOLL_DECO3 = 21;
	public static final int PAPERDOLL_DECO4 = 22;
	public static final int PAPERDOLL_DECO5 = 23;
	public static final int PAPERDOLL_DECO6 = 24;
	public static final int PAPERDOLL_BELT = 25;
	public static final int PAPERDOLL_BROOCH = 26;
	public static final int PAPERDOLL_JEWEL1 = 27;
	public static final int PAPERDOLL_JEWEL2 = 28;
	public static final int PAPERDOLL_JEWEL3 = 29;
	public static final int PAPERDOLL_JEWEL4 = 30;
	public static final int PAPERDOLL_JEWEL5 = 31;
	public static final int PAPERDOLL_JEWEL6 = 32;
	public static final int PAPERDOLL_MAX = 33;
	
	public static final int[] PAPERDOLL_ORDER =
	{
		Inventory.PAPERDOLL_UNDER,
		Inventory.PAPERDOLL_REAR,
		Inventory.PAPERDOLL_LEAR,
		Inventory.PAPERDOLL_NECK,
		Inventory.PAPERDOLL_RFINGER,
		Inventory.PAPERDOLL_LFINGER,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_BACK,
		Inventory.PAPERDOLL_LRHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_DHAIR,
		Inventory.PAPERDOLL_RBRACELET,
		Inventory.PAPERDOLL_LBRACELET,
		Inventory.PAPERDOLL_DECO1,
		Inventory.PAPERDOLL_DECO2,
		Inventory.PAPERDOLL_DECO3,
		Inventory.PAPERDOLL_DECO4,
		Inventory.PAPERDOLL_DECO5,
		Inventory.PAPERDOLL_DECO6,
		Inventory.PAPERDOLL_BELT,
		Inventory.PAPERDOLL_BROOCH,
		Inventory.PAPERDOLL_JEWEL1,
		Inventory.PAPERDOLL_JEWEL2,
		Inventory.PAPERDOLL_JEWEL3,
		Inventory.PAPERDOLL_JEWEL4,
		Inventory.PAPERDOLL_JEWEL5,
		Inventory.PAPERDOLL_JEWEL6
	};
	
	public static final int ADENA_ID = 57;
	
	/**
	 * @author Mobius
	 */
	class InventoryListenerList extends ListenerList<Playable>
	{
		public InventoryListenerList()
		{
		}
		
		/**
		 * Method onEquip.
		 * @param slot int
		 * @param item ItemInstance
		 */
		public void onEquip(int slot, ItemInstance item)
		{
			for (Listener<Playable> listener : getListeners())
			{
				((OnEquipListener) listener).onEquip(slot, item, getActor());
			}
		}
		
		/**
		 * Method onUnequip.
		 * @param slot int
		 * @param item ItemInstance
		 */
		public void onUnequip(int slot, ItemInstance item)
		{
			for (Listener<Playable> listener : getListeners())
			{
				((OnEquipListener) listener).onUnequip(slot, item, getActor());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class ItemOrderComparator implements Comparator<ItemInstance>
	{
		private static final Comparator<ItemInstance> instance = new ItemOrderComparator();
		
		/**
		 * Method getInstance.
		 * @return Comparator<ItemInstance>
		 */
		static final Comparator<ItemInstance> getInstance()
		{
			return instance;
		}
		
		/**
		 * Method compare.
		 * @param o1 ItemInstance
		 * @param o2 ItemInstance
		 * @return int
		 */
		@Override
		public int compare(ItemInstance o1, ItemInstance o2)
		{
			if ((o1 == null) || (o2 == null))
			{
				return 0;
			}
			
			return o1.getLocData() - o2.getLocData();
		}
	}
	
	private final int _ownerId;
	protected final ItemInstance[] _paperdoll = new ItemInstance[PAPERDOLL_MAX];
	protected final InventoryListenerList _listeners = new InventoryListenerList();
	private int _totalWeight;
	private long _wearedMask;
	
	/**
	 * Constructor for Inventory.
	 * @param ownerId int
	 */
	protected Inventory(int ownerId)
	{
		_ownerId = ownerId;
		addListener(StatsListener.getInstance());
	}
	
	/**
	 * Method getActor.
	 * @return Playable
	 */
	public abstract Playable getActor();
	
	/**
	 * Method getBaseLocation.
	 * @return ItemLocation
	 */
	protected abstract ItemLocation getBaseLocation();
	
	/**
	 * Method getEquipLocation.
	 * @return ItemLocation
	 */
	protected abstract ItemLocation getEquipLocation();
	
	/**
	 * Method getOwnerId.
	 * @return int
	 */
	public int getOwnerId()
	{
		return _ownerId;
	}
	
	/**
	 * Method onRestoreItem.
	 * @param item ItemInstance
	 */
	protected void onRestoreItem(ItemInstance item)
	{
		_totalWeight += item.getTemplate().getWeight() * item.getCount();
	}
	
	/**
	 * Method onAddItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onAddItem(ItemInstance item)
	{
		item.setOwnerId(getOwnerId());
		item.setLocation(getBaseLocation());
		item.setLocData(findSlot());
		
		if (item.getJdbcState().isSavable())
		{
			item.save();
		}
		else
		{
			item.setJdbcState(JdbcEntityState.UPDATED);
			item.update();
		}
		
		sendAddItem(item);
		refreshWeight();
	}
	
	/**
	 * Method onModifyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onModifyItem(ItemInstance item)
	{
		item.setJdbcState(JdbcEntityState.UPDATED);
		item.update();
		sendModifyItem(item);
		refreshWeight();
	}
	
	/**
	 * Method onRemoveItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onRemoveItem(ItemInstance item)
	{
		if (item.isEquipped())
		{
			unEquipItem(item);
		}
		
		sendRemoveItem(item);
		item.setLocData(-1);
		refreshWeight();
	}
	
	/**
	 * Method onDestroyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onDestroyItem(ItemInstance item)
	{
		item.setCount(0L);
		item.delete();
	}
	
	/**
	 * Method onEquip.
	 * @param slot int
	 * @param item ItemInstance
	 */
	protected void onEquip(int slot, ItemInstance item)
	{
		_listeners.onEquip(slot, item);
		item.setLocation(getEquipLocation());
		item.setLocData(slot);
		item.setEquipped(true);
		item.setJdbcState(JdbcEntityState.UPDATED);
		sendModifyItem(item);
		_wearedMask |= item.getTemplate().getItemMask();
	}
	
	/**
	 * Method onUnequip.
	 * @param slot int
	 * @param item ItemInstance
	 */
	protected void onUnequip(int slot, ItemInstance item)
	{
		item.setLocation(getBaseLocation());
		item.setLocData(findSlot());
		item.setEquipped(false);
		item.setJdbcState(JdbcEntityState.UPDATED);
		item.setChargedSpiritshot(ItemInstance.CHARGED_NONE);
		item.setChargedSoulshot(ItemInstance.CHARGED_NONE);
		sendModifyItem(item);
		_wearedMask &= ~item.getTemplate().getItemMask();
		_listeners.onUnequip(slot, item);
	}
	
	/**
	 * Method findSlot.
	 * @return int
	 */
	private int findSlot()
	{
		ItemInstance item;
		int slot = 0;
		loop:
		
		for (slot = 0; slot < _items.size(); slot++)
		{
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				
				if (item.isEquipped() || item.getTemplate().isQuest())
				{
					continue;
				}
				
				if (item.getEquipSlot() == slot)
				{
					continue loop;
				}
			}
			break;
		}
		
		return slot;
	}
	
	/**
	 * Method getPaperdollItem.
	 * @param slot int
	 * @return ItemInstance
	 */
	public ItemInstance getPaperdollItem(int slot)
	{
		return _paperdoll[slot];
	}
	
	/**
	 * Method getPaperdollItems.
	 * @return ItemInstance[]
	 */
	public ItemInstance[] getPaperdollItems()
	{
		return _paperdoll;
	}
	
	/**
	 * Method getPaperdollItemId.
	 * @param slot int
	 * @return int
	 */
	public int getPaperdollItemId(int slot)
	{
		ItemInstance item = getPaperdollItem(slot);
		
		if (item != null)
		{
			return item.getId();
		}
		else if (slot == PAPERDOLL_HAIR)
		{
			item = _paperdoll[PAPERDOLL_DHAIR];
			
			if (item != null)
			{
				return item.getId();
			}
		}
		
		return 0;
	}
	
	public int getVisualItemId(int slot)
	{
		ItemInstance item = getPaperdollItem(slot);
		
		if (item != null)
		{
			return item.getVisualId();
		}
		else if (slot == PAPERDOLL_HAIR)
		{
			item = _paperdoll[PAPERDOLL_DHAIR];
			
			if (item != null)
			{
				return item.getVisualId();
			}
		}
		
		return 0;
	}
	
	/**
	 * Method getPaperdollObjectId.
	 * @param slot int
	 * @return int
	 */
	public int getPaperdollObjectId(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		
		if (item != null)
		{
			return item.getObjectId();
		}
		else if (slot == PAPERDOLL_HAIR)
		{
			item = _paperdoll[PAPERDOLL_DHAIR];
			
			if (item != null)
			{
				return item.getObjectId();
			}
		}
		
		return 0;
	}
	
	/**
	 * Method addListener.
	 * @param listener OnEquipListener
	 */
	void addListener(OnEquipListener listener)
	{
		_listeners.add(listener);
	}
	
	/**
	 * Method removeListener.
	 * @param listener OnEquipListener
	 */
	public void removeListener(OnEquipListener listener)
	{
		_listeners.remove(listener);
	}
	
	/**
	 * Method setPaperdollItem.
	 * @param slot int
	 * @param item ItemInstance
	 * @return ItemInstance
	 */
	public ItemInstance setPaperdollItem(int slot, ItemInstance item)
	{
		ItemInstance old;
		writeLock();
		
		try
		{
			old = _paperdoll[slot];
			
			if (old != item)
			{
				if (old != null)
				{
					_paperdoll[slot] = null;
					onUnequip(slot, old);
				}
				
				if (item != null)
				{
					_paperdoll[slot] = item;
					onEquip(slot, item);
				}
			}
		}
		finally
		{
			writeUnlock();
		}
		return old;
	}
	
	/**
	 * Method getWearedMask.
	 * @return long
	 */
	public long getWearedMask()
	{
		return _wearedMask;
	}
	
	/**
	 * Method unEquipItem.
	 * @param item ItemInstance
	 */
	public void unEquipItem(ItemInstance item)
	{
		if (item.isEquipped())
		{
			unEquipItemInBodySlot(item.getBodyPart(), item);
		}
	}
	
	/**
	 * Method unEquipItemInBodySlot.
	 * @param bodySlot int
	 */
	public void unEquipItemInBodySlot(int bodySlot)
	{
		unEquipItemInBodySlot(bodySlot, null);
	}
	
	/**
	 * Method unEquipItemInBodySlot.
	 * @param bodySlot int
	 * @param item ItemInstance
	 */
	private void unEquipItemInBodySlot(int bodySlot, ItemInstance item)
	{
		int pdollSlot = -1;
		
		switch (bodySlot)
		{
			case ItemTemplate.SLOT_NECK:
				pdollSlot = PAPERDOLL_NECK;
				break;
			
			case ItemTemplate.SLOT_L_EAR:
				pdollSlot = PAPERDOLL_LEAR;
				break;
			
			case ItemTemplate.SLOT_R_EAR:
				pdollSlot = PAPERDOLL_REAR;
				break;
			
			case ItemTemplate.SLOT_L_EAR | ItemTemplate.SLOT_R_EAR:
				if (item == null)
				{
					return;
				}
				
				if (getPaperdollItem(PAPERDOLL_LEAR) == item)
				{
					pdollSlot = PAPERDOLL_LEAR;
				}
				
				if (getPaperdollItem(PAPERDOLL_REAR) == item)
				{
					pdollSlot = PAPERDOLL_REAR;
				}
				break;
			
			case ItemTemplate.SLOT_L_FINGER:
				pdollSlot = PAPERDOLL_LFINGER;
				break;
			
			case ItemTemplate.SLOT_R_FINGER:
				pdollSlot = PAPERDOLL_RFINGER;
				break;
			
			case ItemTemplate.SLOT_L_FINGER | ItemTemplate.SLOT_R_FINGER:
				if (item == null)
				{
					return;
				}
				
				if (getPaperdollItem(PAPERDOLL_LFINGER) == item)
				{
					pdollSlot = PAPERDOLL_LFINGER;
				}
				
				if (getPaperdollItem(PAPERDOLL_RFINGER) == item)
				{
					pdollSlot = PAPERDOLL_RFINGER;
				}
				break;
			
			case ItemTemplate.SLOT_HAIR:
				pdollSlot = PAPERDOLL_HAIR;
				break;
			
			case ItemTemplate.SLOT_DHAIR:
				pdollSlot = PAPERDOLL_DHAIR;
				break;
			
			case ItemTemplate.SLOT_HAIRALL:
				setPaperdollItem(PAPERDOLL_DHAIR, null);
				pdollSlot = PAPERDOLL_HAIR;
				break;
			
			case ItemTemplate.SLOT_HEAD:
				pdollSlot = PAPERDOLL_HEAD;
				break;
			
			case ItemTemplate.SLOT_R_HAND:
				pdollSlot = PAPERDOLL_RHAND;
				break;
			
			case ItemTemplate.SLOT_L_HAND:
				pdollSlot = PAPERDOLL_LHAND;
				break;
			
			case ItemTemplate.SLOT_GLOVES:
				pdollSlot = PAPERDOLL_GLOVES;
				break;
			
			case ItemTemplate.SLOT_LEGS:
				pdollSlot = PAPERDOLL_LEGS;
				break;
			
			case ItemTemplate.SLOT_CHEST:
			case ItemTemplate.SLOT_FULL_ARMOR:
			case ItemTemplate.SLOT_FORMAL_WEAR:
				pdollSlot = PAPERDOLL_CHEST;
				break;
			
			case ItemTemplate.SLOT_BACK:
				pdollSlot = PAPERDOLL_BACK;
				break;
			
			case ItemTemplate.SLOT_FEET:
				pdollSlot = PAPERDOLL_FEET;
				break;
			
			case ItemTemplate.SLOT_UNDERWEAR:
				pdollSlot = PAPERDOLL_UNDER;
				break;
			
			case ItemTemplate.SLOT_BELT:
				pdollSlot = PAPERDOLL_BELT;
				break;
			
			case ItemTemplate.SLOT_LR_HAND:
				setPaperdollItem(PAPERDOLL_LHAND, null);
				pdollSlot = PAPERDOLL_RHAND;
				break;
			
			case ItemTemplate.SLOT_L_BRACELET:
				pdollSlot = PAPERDOLL_LBRACELET;
				break;
			
			case ItemTemplate.SLOT_R_BRACELET:
				pdollSlot = PAPERDOLL_RBRACELET;
				setPaperdollItem(PAPERDOLL_DECO1, null);
				setPaperdollItem(PAPERDOLL_DECO2, null);
				setPaperdollItem(PAPERDOLL_DECO3, null);
				setPaperdollItem(PAPERDOLL_DECO4, null);
				setPaperdollItem(PAPERDOLL_DECO5, null);
				setPaperdollItem(PAPERDOLL_DECO6, null);
				break;
			
			case ItemTemplate.SLOT_DECO:
				if (item == null)
				{
					return;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO1) == item)
				{
					pdollSlot = PAPERDOLL_DECO1;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO2) == item)
				{
					pdollSlot = PAPERDOLL_DECO2;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO3) == item)
				{
					pdollSlot = PAPERDOLL_DECO3;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO4) == item)
				{
					pdollSlot = PAPERDOLL_DECO4;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO5) == item)
				{
					pdollSlot = PAPERDOLL_DECO5;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO6) == item)
				{
					pdollSlot = PAPERDOLL_DECO6;
				}
				break;
			case ItemTemplate.SLOT_BROOCH:
				pdollSlot = PAPERDOLL_BROOCH;
				setPaperdollItem(PAPERDOLL_JEWEL1, null);
				setPaperdollItem(PAPERDOLL_JEWEL2, null);
				setPaperdollItem(PAPERDOLL_JEWEL3, null);
				setPaperdollItem(PAPERDOLL_JEWEL4, null);
				setPaperdollItem(PAPERDOLL_JEWEL5, null);
				setPaperdollItem(PAPERDOLL_JEWEL6, null);
				break;
			case ItemTemplate.SLOT_BROOCH_JEWEL:
				if (item == null)
				{
					return;
				}
				else if (getPaperdollItem(PAPERDOLL_JEWEL1) == item)
				{
					pdollSlot = PAPERDOLL_JEWEL1;
				}
				else if (getPaperdollItem(PAPERDOLL_JEWEL2) == item)
				{
					pdollSlot = PAPERDOLL_JEWEL2;
				}
				else if (getPaperdollItem(PAPERDOLL_JEWEL3) == item)
				{
					pdollSlot = PAPERDOLL_JEWEL3;
				}
				else if (getPaperdollItem(PAPERDOLL_JEWEL4) == item)
				{
					pdollSlot = PAPERDOLL_JEWEL4;
				}
				else if (getPaperdollItem(PAPERDOLL_JEWEL5) == item)
				{
					pdollSlot = PAPERDOLL_JEWEL5;
				}
				else if (getPaperdollItem(PAPERDOLL_JEWEL6) == item)
				{
					pdollSlot = PAPERDOLL_JEWEL6;
				}
				break;
			
			default:
				_log.warn("Requested invalid body slot: " + bodySlot + ", Item: " + item + ", ownerId: '" + getOwnerId() + "'");
				return;
		}
		
		if (pdollSlot >= 0)
		{
			setPaperdollItem(pdollSlot, null);
		}
	}
	
	/**
	 * Method equipItem.
	 * @param item ItemInstance
	 */
	public void equipItem(ItemInstance item)
	{
		int bodySlot = item.getBodyPart();
		double hp = getActor().getCurrentHp();
		double mp = getActor().getCurrentMp();
		double cp = getActor().getCurrentCp();
		
		switch (bodySlot)
		{
			case ItemTemplate.SLOT_LR_HAND:
			{
				setPaperdollItem(PAPERDOLL_LHAND, null);
				setPaperdollItem(PAPERDOLL_RHAND, item);
				break;
			}
			
			case ItemTemplate.SLOT_L_HAND:
			{
				final ItemInstance rHandItem = getPaperdollItem(PAPERDOLL_RHAND);
				final ItemTemplate rHandItemTemplate = rHandItem == null ? null : rHandItem.getTemplate();
				final ItemTemplate newItem = item.getTemplate();
				
				if ((newItem.getItemType() == EtcItemType.ARROW) || (newItem.getItemType() == EtcItemType.UNLIMITED_ARROW))
				{
					if (rHandItemTemplate == null)
					{
						return;
					}
					
					if (rHandItemTemplate.getItemType() != WeaponType.BOW)
					{
						return;
					}
					
					if (rHandItemTemplate.getCrystalType() != newItem.getCrystalType())
					{
						return;
					}
				}
				else if (newItem.getItemType() == EtcItemType.BOLT)
				{
					if (rHandItemTemplate == null)
					{
						return;
					}
					
					if (rHandItemTemplate.getItemType() != WeaponType.CROSSBOW)
					{
						return;
					}
					
					if (rHandItemTemplate.getCrystalType() != newItem.getCrystalType())
					{
						return;
					}
				}
				else if (newItem.getItemType() == EtcItemType.BAIT)
				{
					if (rHandItemTemplate == null)
					{
						return;
					}
					
					if (rHandItemTemplate.getItemType() != WeaponType.ROD)
					{
						return;
					}
					
					if (!getActor().isPlayer())
					{
						return;
					}
					
					Player owner = (Player) getActor();
					owner.setVar("LastLure", String.valueOf(item.getObjectId()), -1);
				}
				else
				{
					if ((rHandItemTemplate != null) && (rHandItemTemplate.getBodyPart() == ItemTemplate.SLOT_LR_HAND))
					{
						setPaperdollItem(PAPERDOLL_RHAND, null);
					}
				}
				
				setPaperdollItem(PAPERDOLL_LHAND, item);
				break;
			}
			
			case ItemTemplate.SLOT_R_HAND:
			{
				setPaperdollItem(PAPERDOLL_RHAND, item);
				break;
			}
			
			case ItemTemplate.SLOT_L_EAR:
			case ItemTemplate.SLOT_R_EAR:
			case ItemTemplate.SLOT_L_EAR | ItemTemplate.SLOT_R_EAR:
			{
				if (_paperdoll[PAPERDOLL_LEAR] == null)
				{
					setPaperdollItem(PAPERDOLL_LEAR, item);
				}
				else if (_paperdoll[PAPERDOLL_REAR] == null)
				{
					setPaperdollItem(PAPERDOLL_REAR, item);
				}
				else
				{
					setPaperdollItem(PAPERDOLL_LEAR, item);
				}
				break;
			}
			
			case ItemTemplate.SLOT_L_FINGER:
			case ItemTemplate.SLOT_R_FINGER:
			case ItemTemplate.SLOT_L_FINGER | ItemTemplate.SLOT_R_FINGER:
			{
				if (_paperdoll[PAPERDOLL_LFINGER] == null)
				{
					setPaperdollItem(PAPERDOLL_LFINGER, item);
				}
				else if (_paperdoll[PAPERDOLL_RFINGER] == null)
				{
					setPaperdollItem(PAPERDOLL_RFINGER, item);
				}
				else
				{
					setPaperdollItem(PAPERDOLL_LFINGER, item);
				}
				break;
			}
			
			case ItemTemplate.SLOT_NECK:
				setPaperdollItem(PAPERDOLL_NECK, item);
				break;
			
			case ItemTemplate.SLOT_FULL_ARMOR:
				setPaperdollItem(PAPERDOLL_LEGS, null);
				setPaperdollItem(PAPERDOLL_CHEST, item);
				break;
			
			case ItemTemplate.SLOT_CHEST:
				setPaperdollItem(PAPERDOLL_CHEST, item);
				break;
			
			case ItemTemplate.SLOT_LEGS:
			{
				ItemInstance chest = getPaperdollItem(PAPERDOLL_CHEST);
				
				if ((chest != null) && (chest.getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR))
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				else if (getPaperdollItemId(PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				
				setPaperdollItem(PAPERDOLL_LEGS, item);
				break;
			}
			
			case ItemTemplate.SLOT_FEET:
				if (getPaperdollItemId(PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				
				setPaperdollItem(PAPERDOLL_FEET, item);
				break;
			
			case ItemTemplate.SLOT_GLOVES:
				if (getPaperdollItemId(PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				
				setPaperdollItem(PAPERDOLL_GLOVES, item);
				break;
			
			case ItemTemplate.SLOT_HEAD:
				if (getPaperdollItemId(PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				
				setPaperdollItem(PAPERDOLL_HEAD, item);
				break;
			
			case ItemTemplate.SLOT_HAIR:
				ItemInstance old = getPaperdollItem(PAPERDOLL_DHAIR);
				
				if ((old != null) && (old.getBodyPart() == ItemTemplate.SLOT_HAIRALL))
				{
					setPaperdollItem(PAPERDOLL_DHAIR, null);
				}
				
				setPaperdollItem(PAPERDOLL_HAIR, item);
				break;
			
			case ItemTemplate.SLOT_DHAIR:
				ItemInstance slot2 = getPaperdollItem(PAPERDOLL_DHAIR);
				
				if ((slot2 != null) && (slot2.getBodyPart() == ItemTemplate.SLOT_HAIRALL))
				{
					setPaperdollItem(PAPERDOLL_HAIR, null);
				}
				
				setPaperdollItem(PAPERDOLL_DHAIR, item);
				break;
			
			case ItemTemplate.SLOT_HAIRALL:
				setPaperdollItem(PAPERDOLL_HAIR, null);
				setPaperdollItem(PAPERDOLL_DHAIR, item);
				break;
			
			case ItemTemplate.SLOT_R_BRACELET:
				setPaperdollItem(PAPERDOLL_RBRACELET, item);
				break;
			
			case ItemTemplate.SLOT_L_BRACELET:
				setPaperdollItem(PAPERDOLL_LBRACELET, item);
				break;
			
			case ItemTemplate.SLOT_UNDERWEAR:
				setPaperdollItem(PAPERDOLL_UNDER, item);
				break;
			
			case ItemTemplate.SLOT_BACK:
				setPaperdollItem(PAPERDOLL_BACK, item);
				break;
			
			case ItemTemplate.SLOT_BELT:
				setPaperdollItem(PAPERDOLL_BELT, item);
				break;
			
			case ItemTemplate.SLOT_DECO:
				if (_paperdoll[PAPERDOLL_DECO1] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO1, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO2] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO2, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO3] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO3, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO4] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO4, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO5] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO5, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO6] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO6, item);
				}
				else
				{
					setPaperdollItem(PAPERDOLL_DECO1, item);
				}
				break;
			
			case ItemTemplate.SLOT_FORMAL_WEAR:
				setPaperdollItem(PAPERDOLL_LEGS, null);
				setPaperdollItem(PAPERDOLL_HEAD, null);
				setPaperdollItem(PAPERDOLL_FEET, null);
				setPaperdollItem(PAPERDOLL_GLOVES, null);
				setPaperdollItem(PAPERDOLL_CHEST, item);
				break;
			
			case ItemTemplate.SLOT_BROOCH:
				setPaperdollItem(PAPERDOLL_BROOCH, item);
				break;
			
			case ItemTemplate.SLOT_BROOCH_JEWEL:
				equipBroochJewel(item);
				break;
			
			default:
				_log.warn("unknown body slot:" + bodySlot + " for item id: " + item.getId());
				return;
		}
		
		getActor().setCurrentHp(hp, false);
		getActor().setCurrentMp(mp);
		getActor().setCurrentCp(cp);
		
		if (getActor().isPlayer())
		{
			((Player) getActor()).autoShot();
		}
	}
	
	/**
	 * Method sendAddItem.
	 * @param item ItemInstance
	 */
	protected abstract void sendAddItem(ItemInstance item);
	
	/**
	 * Method sendModifyItem.
	 * @param item ItemInstance
	 */
	protected abstract void sendModifyItem(ItemInstance item);
	
	/**
	 * Method sendRemoveItem.
	 * @param item ItemInstance
	 */
	protected abstract void sendRemoveItem(ItemInstance item);
	
	/**
	 * Method refreshWeight.
	 */
	protected void refreshWeight()
	{
		int weight = 0;
		readLock();
		
		try
		{
			ItemInstance item;
			
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				weight += item.getTemplate().getWeight() * item.getCount();
			}
		}
		finally
		{
			readUnlock();
		}
		
		if (_totalWeight == weight)
		{
			return;
		}
		
		_totalWeight = weight;
		onRefreshWeight();
	}
	
	/**
	 * Method onRefreshWeight.
	 */
	protected abstract void onRefreshWeight();
	
	/**
	 * Method getTotalWeight.
	 * @return int
	 */
	public int getTotalWeight()
	{
		return _totalWeight;
	}
	
	/**
	 * Method validateCapacity.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public boolean validateCapacity(ItemInstance item)
	{
		long slots = 0;
		
		if (!item.isStackable() || (getItemByItemId(item.getId()) == null))
		{
			slots++;
		}
		
		return validateCapacity(slots);
	}
	
	/**
	 * Method validateCapacity.
	 * @param itemId int
	 * @param count long
	 * @return boolean
	 */
	public boolean validateCapacity(int itemId, long count)
	{
		ItemTemplate item = ItemHolder.getInstance().getTemplate(itemId);
		return validateCapacity(item, count);
	}
	
	/**
	 * Method validateCapacity.
	 * @param item ItemTemplate
	 * @param count long
	 * @return boolean
	 */
	private boolean validateCapacity(ItemTemplate item, long count)
	{
		long slots = 0;
		
		if (!item.isStackable() || (getItemByItemId(item.getId()) == null))
		{
			slots = count;
		}
		
		return validateCapacity(slots);
	}
	
	/**
	 * Method validateCapacity.
	 * @param slots long
	 * @return boolean
	 */
	public boolean validateCapacity(long slots)
	{
		if (slots == 0)
		{
			return true;
		}
		
		if ((slots < Integer.MIN_VALUE) || (slots > Integer.MAX_VALUE))
		{
			return false;
		}
		
		if ((getSize() + (int) slots) < 0)
		{
			return false;
		}
		
		return (getSize() + slots) <= getActor().getInventoryLimit();
	}
	
	/**
	 * Method validateWeight.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public boolean validateWeight(ItemInstance item)
	{
		long weight = item.getTemplate().getWeight() * item.getCount();
		return validateWeight(weight);
	}
	
	/**
	 * Method validateWeight.
	 * @param itemId int
	 * @param count long
	 * @return boolean
	 */
	public boolean validateWeight(int itemId, long count)
	{
		ItemTemplate item = ItemHolder.getInstance().getTemplate(itemId);
		return validateWeight(item, count);
	}
	
	/**
	 * Method validateWeight.
	 * @param item ItemTemplate
	 * @param count long
	 * @return boolean
	 */
	private boolean validateWeight(ItemTemplate item, long count)
	{
		long weight = item.getWeight() * count;
		return validateWeight(weight);
	}
	
	/**
	 * Method validateWeight.
	 * @param weight long
	 * @return boolean
	 */
	public boolean validateWeight(long weight)
	{
		if (weight == 0L)
		{
			return true;
		}
		
		if ((weight < Integer.MIN_VALUE) || (weight > Integer.MAX_VALUE))
		{
			return false;
		}
		
		if ((getTotalWeight() + (int) weight) < 0)
		{
			return false;
		}
		
		return (getTotalWeight() + weight) <= getActor().getMaxLoad();
	}
	
	/**
	 * Method restore.
	 */
	public abstract void restore();
	
	/**
	 * Method store.
	 */
	public abstract void store();
	
	/**
	 * Method getPaperdollIndex.
	 * @param slot int
	 * @return int
	 */
	public static int getPaperdollIndex(int slot)
	{
		switch (slot)
		{
			case ItemTemplate.SLOT_UNDERWEAR:
				return PAPERDOLL_UNDER;
				
			case ItemTemplate.SLOT_R_EAR:
				return PAPERDOLL_REAR;
				
			case ItemTemplate.SLOT_L_EAR:
				return PAPERDOLL_LEAR;
				
			case ItemTemplate.SLOT_NECK:
				return PAPERDOLL_NECK;
				
			case ItemTemplate.SLOT_R_FINGER:
				return PAPERDOLL_RFINGER;
				
			case ItemTemplate.SLOT_L_FINGER:
				return PAPERDOLL_LFINGER;
				
			case ItemTemplate.SLOT_HEAD:
				return PAPERDOLL_HEAD;
				
			case ItemTemplate.SLOT_R_HAND:
				return PAPERDOLL_RHAND;
				
			case ItemTemplate.SLOT_L_HAND:
				return PAPERDOLL_LHAND;
				
			case ItemTemplate.SLOT_LR_HAND:
				return PAPERDOLL_LRHAND;
				
			case ItemTemplate.SLOT_GLOVES:
				return PAPERDOLL_GLOVES;
				
			case ItemTemplate.SLOT_CHEST:
			case ItemTemplate.SLOT_FULL_ARMOR:
			case ItemTemplate.SLOT_FORMAL_WEAR:
				return PAPERDOLL_CHEST;
				
			case ItemTemplate.SLOT_LEGS:
				return PAPERDOLL_LEGS;
				
			case ItemTemplate.SLOT_FEET:
				return PAPERDOLL_FEET;
				
			case ItemTemplate.SLOT_BACK:
				return PAPERDOLL_BACK;
				
			case ItemTemplate.SLOT_HAIR:
			case ItemTemplate.SLOT_HAIRALL:
				return PAPERDOLL_HAIR;
				
			case ItemTemplate.SLOT_DHAIR:
				return PAPERDOLL_DHAIR;
				
			case ItemTemplate.SLOT_R_BRACELET:
				return PAPERDOLL_RBRACELET;
				
			case ItemTemplate.SLOT_L_BRACELET:
				return PAPERDOLL_LBRACELET;
				
			case ItemTemplate.SLOT_DECO:
				return PAPERDOLL_DECO1;
				
			case ItemTemplate.SLOT_BELT:
				return PAPERDOLL_BELT;
				
			case ItemTemplate.SLOT_BROOCH:
				return PAPERDOLL_BROOCH;
				
			case ItemTemplate.SLOT_BROOCH_JEWEL:
				return PAPERDOLL_JEWEL1;
		}
		
		return -1;
	}
	
	public int getBroochJewelSlots()
	{
		return getActor().getPlayer().getJewelsLimit();
	}
	
	private void equipBroochJewel(ItemInstance item)
	{
		
		// find same (or incompatible) brooch jewel type
		for (int i = PAPERDOLL_JEWEL1; i < (PAPERDOLL_JEWEL1 + getBroochJewelSlots()); i++)
		{
			if (_paperdoll[i] != null)
			{
				if (getPaperdollItemId(i) == item.getId())
				{
					// overwtite
					setPaperdollItem(i, item);
					return;
				}
			}
		}
		
		// no free slot found - put on first free
		for (int i = PAPERDOLL_JEWEL1; i < (PAPERDOLL_JEWEL1 + getBroochJewelSlots()); i++)
		{
			if (_paperdoll[i] == null)
			{
				setPaperdollItem(i, item);
				return;
			}
		}
		
		// no free slots - put on first
		setPaperdollItem(PAPERDOLL_JEWEL1, item);
	}
	
	/**
	 * Method getSize.
	 * @return int
	 */
	@Override
	public int getSize()
	{
		return super.getSize() - getQuestSize();
	}
	
	/**
	 * Method getAllSize.
	 * @return int
	 */
	public int getAllSize()
	{
		return super.getSize();
	}
	
	/**
	 * Method getQuestSize.
	 * @return int
	 */
	public int getQuestSize()
	{
		int size = 0;
		
		for (ItemInstance item : getItems())
		{
			if (item.getTemplate().isQuest())
			{
				size++;
			}
		}
		
		return size;
	}
}
