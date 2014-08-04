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

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.BeautyShopHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.beautyshop.BeautyShopSet;
import lineage2.gameserver.network.serverpackets.ExResponseBeautyListPacket;
import lineage2.gameserver.network.serverpackets.ExResponseBeautyRegistResetPacket;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;

/**
 * @author Smo
 */
public class RequestRegistBeauty extends L2GameClientPacket
{
	private int _hairStyle;
	private int _face;
	private int _hairColor;
	
	@Override
	protected void readImpl()
	{
		_hairStyle = readD();
		_face = readD();
		_hairColor = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		long reqAdena = 0L;
		long reqCoins = 0L;
		boolean change = false;
		BeautyShopSet set = BeautyShopHolder.getInstance().getSet(activeChar);
		
		if (set == null)
		{
			activeChar.sendPacket(new IStaticPacket[]
			{
				new ExResponseBeautyRegistResetPacket(activeChar, 0, 0),
				new ExResponseBeautyListPacket(activeChar, 0)
			});
			return;
		}
		
		if ((_hairStyle > 0) && (_hairColor > 0) && (((_hairStyle != activeChar.getNewHairStyle()) || (_hairColor != activeChar.getNewHairColor()))))
		{
			if ((set.getHairStyle(_hairStyle) == null) || (set.getHairStyle(_hairStyle).getHairColor(_hairColor) == null))
			{
				activeChar.sendPacket(new IStaticPacket[]
				{
					new ExResponseBeautyRegistResetPacket(activeChar, 0, 0),
					new ExResponseBeautyListPacket(activeChar, 0)
				});
				return;
			}
			
			if (_hairStyle != activeChar.getNewHairStyle())
			{
				reqAdena += set.getHairStyle(_hairStyle).getAdena() + set.getHairStyle(_hairStyle).getHairColor(_hairColor).getAdena();
				reqCoins += set.getHairStyle(_hairStyle).getCoins() + set.getHairStyle(_hairStyle).getHairColor(_hairColor).getCoins();
			}
			else
			{
				reqAdena += set.getHairStyle(_hairStyle).getHairColor(_hairColor).getAdena();
				reqCoins += set.getHairStyle(_hairStyle).getHairColor(_hairColor).getCoins();
			}
			
			change = true;
		}
		
		if ((_face > 0) && (_face != activeChar.getNewFace()))
		{
			if (set.getFace(_face) == null)
			{
				activeChar.sendPacket(new IStaticPacket[]
				{
					new ExResponseBeautyRegistResetPacket(activeChar, 0, 0),
					new ExResponseBeautyListPacket(activeChar, 0)
				});
				return;
			}
			
			reqAdena += set.getFace(_face).getAdena();
			reqCoins += set.getFace(_face).getCoins();
			change = true;
		}
		
		if ((!(change)) || (activeChar.getAdena() < reqAdena) || (activeChar.getBeautyShopCoin() < reqCoins))
		{
			activeChar.sendPacket(new IStaticPacket[]
			{
				new ExResponseBeautyRegistResetPacket(activeChar, 0, 0),
				new ExResponseBeautyListPacket(activeChar, 0)
			});
			return;
		}
		
		if (reqAdena > 0L)
		{
			activeChar.getInventory().destroyItemByItemId(57, reqAdena);
		}
		
		if (reqCoins > 0L)
		{
			activeChar.getInventory().destroyItemByItemId(Config.BEAUTY_SHOP_COIN_ITEM_ID, reqCoins);
		}
		
		if (_hairStyle > 0)
		{
			activeChar.setNewHairStyle(_hairStyle);
			activeChar.setNewHairColor(_hairColor);
		}
		
		if (_face > 0)
		{
			activeChar.setNewFace(_face);
		}
		
		activeChar.sendPacket(new IStaticPacket[]
		{
			new ExResponseBeautyRegistResetPacket(activeChar, 0, 1),
			new ExResponseBeautyListPacket(activeChar, 0)
		});
	}
}