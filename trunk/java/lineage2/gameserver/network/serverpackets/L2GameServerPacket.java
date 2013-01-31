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

import lineage2.commons.net.nio.impl.SendablePacket;
import lineage2.gameserver.GameServer;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.MultiSellIngredient;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class L2GameServerPacket extends SendablePacket<GameClient> implements IStaticPacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(L2GameServerPacket.class);
	
	/**
	 * Method write.
	 * @return boolean
	 */
	@Override
	public final boolean write()
	{
		try
		{
			writeImpl();
			return true;
		}
		catch (Exception e)
		{
			_log.error("Client: " + getClient() + " - Failed writing: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
		}
		return false;
	}
	
	/**
	 * Method writeImpl.
	 */
	protected abstract void writeImpl();
	
	/**
	 * Method writeEx.
	 * @param value int
	 */
	protected void writeEx(int value)
	{
		writeC(0xFE);
		writeH(value);
	}
	
	/**
	 * Method writeD.
	 * @param b boolean
	 */
	protected void writeD(boolean b)
	{
		writeD(b ? 1 : 0);
	}
	
	/**
	 * Method writeC.
	 * @param b boolean
	 */
	protected void writeC(boolean b)
	{
		writeC(b ? 1 : 0);
	}
	
	/**
	 * Method writeDD.
	 * @param values int[]
	 * @param sendCount boolean
	 */
	protected void writeDD(int[] values, boolean sendCount)
	{
		if (sendCount)
		{
			getByteBuffer().putInt(values.length);
		}
		for (int value : values)
		{
			getByteBuffer().putInt(value);
		}
	}
	
	/**
	 * Method writeItemInfo.
	 * @param item ItemInstance
	 */
	protected void writeItemInfo(ItemInstance item)
	{
		writeItemInfo(item, item.getCount());
	}
	
	/**
	 * Method writeItemInfo.
	 * @param item ItemInstance
	 * @param count long
	 */
	protected void writeItemInfo(ItemInstance item, long count)
	{
		writeD(item.getObjectId());
		writeD(item.getItemId());
		writeD(item.getEquipSlot());
		writeQ(count);
		writeH(item.getTemplate().getType2ForPackets());
		writeH(item.getCustomType1());
		writeH(item.isEquipped() ? 1 : 0);
		writeD(item.getBodyPart());
		writeH(item.getEnchantLevel());
		writeH(item.getCustomType2());
		writeD(item.getAugmentationId());
		writeD(item.getShadowLifeTime());
		writeD(item.getTemporalLifeTime());
		writeH(0x01);
		writeH(item.getAttackElement().getId());
		writeH(item.getAttackElementValue());
		writeH(item.getDefenceFire());
		writeH(item.getDefenceWater());
		writeH(item.getDefenceWind());
		writeH(item.getDefenceEarth());
		writeH(item.getDefenceHoly());
		writeH(item.getDefenceUnholy());
		writeH(item.getEnchantOptions()[0]);
		writeH(item.getEnchantOptions()[1]);
		writeH(item.getEnchantOptions()[2]);
		writeD(0x00);
	}
	
	/**
	 * Method writeItemInfo.
	 * @param item ItemInfo
	 */
	protected void writeItemInfo(ItemInfo item)
	{
		writeItemInfo(item, item.getCount());
	}
	
	/**
	 * Method writeItemInfo.
	 * @param item ItemInfo
	 * @param count long
	 */
	protected void writeItemInfo(ItemInfo item, long count)
	{
		writeD(item.getObjectId());
		writeD(item.getItemId());
		writeD(item.getEquipSlot());
		writeQ(count);
		writeH(item.getItem().getType2ForPackets());
		writeH(item.getCustomType1());
		writeH(item.isEquipped() ? 1 : 0);
		writeD(item.getItem().getBodyPart());
		writeH(item.getEnchantLevel());
		writeH(item.getCustomType2());
		writeD(item.getAugmentationId());
		writeD(item.getShadowLifeTime());
		writeD(item.getTemporalLifeTime());
		writeH(0x01);
		writeH(item.getAttackElement());
		writeH(item.getAttackElementValue());
		writeH(item.getDefenceFire());
		writeH(item.getDefenceWater());
		writeH(item.getDefenceWind());
		writeH(item.getDefenceEarth());
		writeH(item.getDefenceHoly());
		writeH(item.getDefenceUnholy());
		writeH(item.getEnchantOptions()[0]);
		writeH(item.getEnchantOptions()[1]);
		writeH(item.getEnchantOptions()[2]);
		writeD(0x00);
	}
	
	/**
	 * Method writeItemElements.
	 * @param item MultiSellIngredient
	 */
	protected void writeItemElements(MultiSellIngredient item)
	{
		if (item.getItemId() <= 0)
		{
			writeItemElements();
			return;
		}
		ItemTemplate i = ItemHolder.getInstance().getTemplate(item.getItemId());
		if (item.getItemAttributes().getValue() > 0)
		{
			if (i.isWeapon())
			{
				Element e = item.getItemAttributes().getElement();
				writeH(e.getId());
				writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e));
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
			}
			else if (i.isArmor())
			{
				writeH(-1);
				writeH(0);
				for (Element e : Element.VALUES)
				{
					writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e));
				}
			}
			else
			{
				writeItemElements();
			}
		}
		else
		{
			writeItemElements();
		}
	}
	
	/**
	 * Method writeItemElements.
	 */
	protected void writeItemElements()
	{
		writeH(-1);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
	}
	
	/**
	 * Method getType.
	 * @return String
	 */
	public String getType()
	{
		return "[S] " + getClass().getSimpleName();
	}
	
	/**
	 * Method packet.
	 * @param player Player
	 * @return L2GameServerPacket * @see lineage2.gameserver.network.serverpackets.components.IStaticPacket#packet(Player)
	 */
	@Override
	public L2GameServerPacket packet(Player player)
	{
		return this;
	}
}
