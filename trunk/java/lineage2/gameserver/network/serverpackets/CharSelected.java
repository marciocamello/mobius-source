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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharSelected extends L2GameServerPacket
{
	/**
	 * Field class_id. Field race. Field sex. Field clan_id. Field char_id. Field _sessionId.
	 */
	private final int _sessionId, char_id, clan_id, sex, race, class_id;
	/**
	 * Field _title. Field _name.
	 */
	private final String _name, _title;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field curMp. Field curHp.
	 */
	private final double curHp, curMp;
	/**
	 * Field _pk. Field _wit. Field _dex. Field _men. Field _con. Field _str. Field _int. Field karma. Field level. Field _sp.
	 */
	private final int _sp, level, karma, _int, _str, _con, _men, _dex, _wit, _pk;
	/**
	 * Field _exp.
	 */
	private final long _exp;
	
	/**
	 * Constructor for CharSelected.
	 * @param cha Player
	 * @param sessionId int
	 */
	public CharSelected(final Player cha, final int sessionId)
	{
		_sessionId = sessionId;
		_name = cha.getName();
		char_id = cha.getObjectId();
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
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x0B);
		writeS(_name);
		writeD(char_id);
		writeS(_title);
		writeD(_sessionId);
		writeD(clan_id);
		writeD(0x00);
		writeD(sex);
		writeD(race);
		writeD(class_id);
		writeD(0x01);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeF(curHp);
		writeF(curMp);
		writeD(_sp);
		writeQ(_exp);
		writeD(level);
		writeD(karma);
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
