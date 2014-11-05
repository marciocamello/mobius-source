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

import lineage2.gameserver.data.xml.holder.BeautyShopHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.beautyshop.BeautyShopSet;
import lineage2.gameserver.network.serverpackets.ExResponseBeautyRegistResetPacket;

/**
 * @author Smo
 */
public class RequestShowResetShopList extends L2GameClientPacket
{
	private int _hairStyle;
	private int _face;
	@SuppressWarnings("unused")
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
		boolean reset = false;
		BeautyShopSet set = BeautyShopHolder.getInstance().getSet(activeChar);
		
		if (set == null)
		{
			activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 0));
			return;
		}
		
		if (_hairStyle > 0)
		{
			if (set.getHairStyle(_hairStyle) == null)
			{
				activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 0));
				return;
			}
			
			reqAdena += set.getHairStyle(_hairStyle).getResetPrice();
			reset = true;
		}
		
		if (_face > 0)
		{
			if (set.getFace(_face) == null)
			{
				activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 0));
				return;
			}
			
			reqAdena += set.getFace(_face).getResetPrice();
			reset = true;
		}
		
		if ((!reset) || (activeChar.getAdena() < reqAdena))
		{
			activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 0));
			return;
		}
		
		activeChar.getInventory().destroyItemByItemId(57, reqAdena);
		
		if (activeChar.getNewHairStyle() > 0)
		{
			activeChar.setNewHairStyle(0);
			activeChar.setNewHairColor(0);
		}
		
		if (activeChar.getNewFace() > 0)
		{
			activeChar.setNewFace(0);
		}
		
		activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 1));
	}
}