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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HennaUnequipInfo extends L2GameServerPacket
{
	/**
	 * Field _men. Field _wit. Field _int. Field _dex. Field _con. Field _str.
	 */
	private final int _str, _con, _dex, _int, _wit, _men;
	/**
	 * Field _adena.
	 */
	private final long _adena;
	/**
	 * Field _henna.
	 */
	private final Henna _henna;
	
	/**
	 * Constructor for HennaUnequipInfo.
	 * @param henna Henna
	 * @param player Player
	 */
	public HennaUnequipInfo(Henna henna, Player player)
	{
		_henna = henna;
		_adena = player.getAdena();
		_str = player.getSTR();
		_dex = player.getDEX();
		_con = player.getCON();
		_int = player.getINT();
		_wit = player.getWIT();
		_men = player.getMEN();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xE7);
		writeD(_henna.getSymbolId());
		writeD(_henna.getDyeId());
		writeQ(_henna.getDrawCount());
		writeQ(_henna.getPrice());
		writeD(1);
		writeQ(_adena);
		writeD(_int);
		writeC(_int + _henna.getStatINT());
		writeD(_str);
		writeC(_str + _henna.getStatSTR());
		writeD(_con);
		writeC(_con + _henna.getStatCON());
		writeD(_men);
		writeC(_men + _henna.getStatMEN());
		writeD(_dex);
		writeC(_dex + _henna.getStatDEX());
		writeD(_wit);
		writeC(_wit + _henna.getStatWIT());
	}
}
