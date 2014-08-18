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

public class HennaUnequipInfo extends L2GameServerPacket
{
	private final int _str;
	private final int _con;
	private final int _dex;
	private final int _int;
	private final int _wit;
	private final int _men;
	private final long _adena;
	private final Henna _henna;
	
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
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xE7);
		writeD(_henna.getSymbolId()); // symbol Id
		writeD(_henna.getDyeId()); // item id of dye
		writeQ(_henna.getDrawCount());
		writeQ(_henna.getPrice());
		writeD(1); // able to draw or not 0 is false and 1 is true
		writeQ(_adena);
		writeD(_int); // current INT
		writeC(_int + _henna.getStatINT()); // equip INT
		writeD(_str); // current STR
		writeC(_str + _henna.getStatSTR()); // equip STR
		writeD(_con); // current CON
		writeC(_con + _henna.getStatCON()); // equip CON
		writeD(_men); // current MEM
		writeC(_men + _henna.getStatMEN()); // equip MEM
		writeD(_dex); // current DEX
		writeC(_dex + _henna.getStatDEX()); // equip DEX
		writeD(_wit); // current WIT
		writeC(_wit + _henna.getStatWIT()); // equip WIT
	}
}