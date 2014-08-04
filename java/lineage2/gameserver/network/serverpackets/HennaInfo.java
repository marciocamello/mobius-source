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

public class HennaInfo extends L2GameServerPacket
{
	private final Henna[] _hennas = new Henna[3];
	private final int _str, _con, _dex, _int, _wit, _men;
	private int _count;
	
	public HennaInfo(Player player)
	{
		_count = 0;
		lineage2.gameserver.templates.Henna h;
		
		for (int i = 0; i < 3; i++)
		{
			if ((h = player.getHenna(i + 1)) != null)
			{
				_hennas[_count++] = new Henna(h.getSymbolId(), h.isForThisClass(player));
			}
		}
		
		_str = player.getHennaStatSTR();
		_con = player.getHennaStatCON();
		_dex = player.getHennaStatDEX();
		_int = player.getHennaStatINT();
		_wit = player.getHennaStatWIT();
		_men = player.getHennaStatMEN();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe5);
		writeC(_int); // equip INT
		writeC(_str); // equip STR
		writeC(_con); // equip CON
		writeC(_men); // equip MEM
		writeC(_dex); // equip DEX
		writeC(_wit); // equip WIT
		writeD(3); // interlude, slots?
		writeD(_count);
		
		for (int i = 0; i < _count; i++)
		{
			writeD(_hennas[i]._symbolId);
			writeD(_hennas[i]._valid ? _hennas[i]._symbolId : 0);
		}
	}
	
	private static class Henna
	{
		final int _symbolId;
		final boolean _valid;
		
		public Henna(int sy, boolean valid)
		{
			_symbolId = sy;
			_valid = valid;
		}
	}
}