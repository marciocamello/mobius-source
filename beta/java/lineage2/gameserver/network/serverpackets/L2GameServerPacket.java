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
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.MultiSellIngredient;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class L2GameServerPacket extends SendablePacket<GameClient> implements IStaticPacket
{
	private static final Logger _log = LoggerFactory.getLogger(L2GameServerPacket.class);
	
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
			_log.error("Client: " + getClient() + " - Failed writing: " + getType(), e);
		}
		
		return false;
	}
	
	protected abstract void writeImpl();
	
	protected void writeEx(int value)
	{
		writeC(0xFE);
		writeH(value);
	}
	
	protected void writeD(boolean b)
	{
		writeD(b ? 1 : 0);
	}
	
	protected void writeC(boolean b)
	{
		writeC(b ? 1 : 0);
	}
	
	/**
	 * @param values
	 * @param sendCount
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
	
	protected void writeDD(int[] values)
	{
		writeDD(values, false);
	}
	
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
				writeH(e.getId()); // attack element (-1 - none)
				writeH(item.getItemAttributes().getValue(e) + i.getBaseAttributeValue(e)); // attack element value
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
				writeH(0);
			}
			else if (i.isArmor())
			{
				writeH(-1); // attack element (-1 - none)
				writeH(0); // attack element value
				
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
	
	/*
	 * protected void writeInfo(final MultiSellIngredient ingr, final boolean product) { final int itemId = ingr.getItemId(); final ItemTemplate template = (itemId > 0) ? ItemHolder.getInstance().getTemplate(ingr.getItemId()) : null; writeD(itemId); if(product) { writeD((itemId > 0) ?
	 * template.getBodyPart() : 0); } writeH((itemId > 0) ? template.getType2ForPackets() : 0); writeQ(ingr.getItemCount()); writeH(ingr.getItemEnchant()); if(product) { writeD(ingr.getChance(true)); } writeAugmentationInfo(ingr); writeItemElements(ingr); } protected void writeAugmentationInfo(final
	 * MultiSellIngredient ingr) { if(ingr.getAugmentationId() != 0) { final int augm = ingr.getAugmentationId(); writeD(augm & 0x0000FFFF); writeD(augm >> 16); } else { writeD(0x00); writeD(0x00); } }
	 */
	protected void writeItemElements()
	{
		writeH(-1); // attack element (-1 - none)
		writeH(0x00); // attack element value
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
	}
	
	/**
	 * Writes 3 D (int32) with current location x, y, z
	 * @param loc
	 */
	protected void writeLoc(Location loc)
	{
		writeD(loc.getX());
		writeD(loc.getY());
		writeD(loc.getZ());
	}
	
	public String getType()
	{
		return "[S] " + getClass().getSimpleName();
	}
	
	@Override
	public L2GameServerPacket packet(Player player)
	{
		return this;
	}
}