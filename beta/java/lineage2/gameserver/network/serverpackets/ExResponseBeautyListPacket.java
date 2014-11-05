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

import lineage2.gameserver.data.xml.holder.BeautyShopHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.beautyshop.BeautyShopFace;
import lineage2.gameserver.model.beautyshop.BeautyShopHairStyle;
import lineage2.gameserver.model.beautyshop.BeautyShopSet;

/**
 * @author Smo
 */
public class ExResponseBeautyListPacket extends L2GameServerPacket
{
	private int _type;
	private long _adena;
	private long _coins;
	private int[][] _data;
	private boolean _send = false;
	
	public ExResponseBeautyListPacket(Player activeChar, int type)
	{
		BeautyShopSet set = BeautyShopHolder.getInstance().getSet(activeChar);
		
		if (set == null)
		{
			return;
		}
		
		int i = 0;
		
		if (type == 0)
		{
			_data = new int[set.getHairStyles().size()][2];
			
			for (BeautyShopHairStyle style : set.getHairStyles().valueCollection())
			{
				_data[i][0] = style.getId();
				_data[i][1] = 0;
				++i;
			}
		}
		else
		{
			_data = new int[set.getFaces().size()][2];
			
			for (BeautyShopFace face : set.getFaces().valueCollection())
			{
				_data[i][0] = face.getId();
				_data[i][1] = 0;
				++i;
			}
		}
		
		_type = type;
		_adena = activeChar.getAdena();
		_coins = activeChar.getBeautyShopCoin();
		_send = true;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x135);
		
		if (!(_send))
		{
			return;
		}
		
		writeQ(_adena);
		writeQ(_coins);
		writeD(_type);
		writeD(_data.length);
		
		for (int[] element : _data)
		{
			writeD(element[0]);
			writeD(element[1]);
		}
	}
}
