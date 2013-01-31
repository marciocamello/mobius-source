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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HennaInfo extends L2GameServerPacket
{
	/**
	 * Field _hennas.
	 */
	private final Henna[] _hennas = new Henna[3];
	/**
	 * Field _men. Field _wit. Field _int. Field _dex. Field _con. Field _str.
	 */
	private final int _str, _con, _dex, _int, _wit, _men;
	/**
	 * Field _count.
	 */
	private int _count;
	
	/**
	 * Constructor for HennaInfo.
	 * @param player Player
	 */
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
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xe5);
		writeC(_int);
		writeC(_str);
		writeC(_con);
		writeC(_men);
		writeC(_dex);
		writeC(_wit);
		writeD(3);
		writeD(_count);
		for (int i = 0; i < _count; i++)
		{
			writeD(_hennas[i]._symbolId);
			writeD(_hennas[i]._valid ? _hennas[i]._symbolId : 0);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class Henna
	{
		/**
		 * Field _symbolId.
		 */
		final int _symbolId;
		/**
		 * Field _valid.
		 */
		final boolean _valid;
		
		/**
		 * Constructor for Henna.
		 * @param sy int
		 * @param valid boolean
		 */
		public Henna(int sy, boolean valid)
		{
			_symbolId = sy;
			_valid = valid;
		}
	}
}
