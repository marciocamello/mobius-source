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

import lineage2.gameserver.GameTimeController;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Location;

public class CharSelected extends L2GameServerPacket
{
	private final int _sessionId;
	private final int char_id;
	private final int clan_id;
	private final int sex;
	private final int race;
	private final int class_id;
	private final String _name;
	private final String _title;
	private final Location _loc;
	private final double curHp;
	private final double curMp;
	private final int _sp;
	private final int level;
	private final int karma;
	private final int _int;
	private final int _str;
	private final int _con;
	private final int _men;
	private final int _dex;
	private final int _wit;
	private final int _pk;
	private final long _exp;
	
	public CharSelected(final Player cha, final int sessionId)
	{
		_sessionId = sessionId;
		_name = cha.getName();
		char_id = cha.getObjectId(); // FIXME 0x00030b7a ??
		_title = cha.getTitle();
		clan_id = cha.getClanId();
		sex = cha.getSex();
		race = cha.getRace().ordinal();
		class_id = cha.getClassId().getId();
		_loc = cha.getLoc();
		curHp = cha.getCurrentHp();
		curMp = cha.getCurrentMp();
		_sp = cha.getIntSp();
		_exp = cha.getExp();
		level = cha.getLevel();
		karma = cha.getKarma();
		_pk = cha.getPkKills();
		_int = cha.getINT();
		_str = cha.getSTR();
		_con = cha.getCON();
		_men = cha.getMEN();
		_dex = cha.getDEX();
		_wit = cha.getWIT();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x0B);
		writeS(_name);
		writeD(char_id);
		writeS(_title);
		writeD(_sessionId);
		writeD(clan_id);
		writeD(0x00); // ??
		writeD(sex);
		writeD(race);
		writeD(class_id);
		writeD(0x01); // active ??
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeF(curHp);
		writeF(curMp);
		writeD(_sp);
		writeQ(_exp);
		writeD(level);
		writeD(karma); // ?
		writeD(_pk);
		writeD(_int);
		writeD(_str);
		writeD(_con);
		writeD(_men);
		writeD(_dex);
		writeD(_wit);
		writeD(GameTimeController.getInstance().getGameTime());
		writeD(0x00);
		writeD(class_id);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeB(new byte[64]);
		writeD(0x00);
	}
}