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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.Henna;

//ccccccdd[dd]
public class GMHennaInfo extends L2GameServerPacket
{
	private final int _count;
	private final int _str;
	private final int _con;
	private final int _dex;
	private final int _int;
	private final int _wit;
	private final int _men;
	private final int _luc;
	private final int _cha;
	private final Henna[] _hennas = new Henna[3];
	
	public GMHennaInfo(final Player cha)
	{
		_str = cha.getHennaStatSTR();
		_con = cha.getHennaStatCON();
		_dex = cha.getHennaStatDEX();
		_int = cha.getHennaStatINT();
		_wit = cha.getHennaStatWIT();
		_men = cha.getHennaStatMEN();
		_luc = cha.getHennaStatLUC();
		_cha = cha.getHennaStatCHA();
		
		int j = 0;
		
		for (int i = 0; i < 3; i++)
		{
			Henna h = cha.getHenna(i + 1);
			
			if (h != null)
			{
				_hennas[j++] = h;
			}
		}
		
		_count = j;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xf0);
		
		writeC(_int);
		writeC(_str);
		writeC(_con);
		writeC(_men);
		writeC(_dex);
		writeC(_wit);
		writeC(_luc);
		writeC(_cha);
		writeD(0x03);
		writeD(_count);
		
		for (int i = 0; i < _count; i++)
		{
			writeD(_hennas[i].getSymbolId());
			writeD(_hennas[i].getSymbolId());
		}
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}
}