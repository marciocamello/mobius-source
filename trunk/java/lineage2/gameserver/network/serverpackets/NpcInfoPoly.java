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

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NpcInfoPoly extends L2GameServerPacket
{
	/**
	 * Field _obj.
	 */
	private final Creature _obj;
	/**
	 * Field _heading. Field _z. Field _y. Field _x.
	 */
	private final int _x, _y, _z, _heading;
	/**
	 * Field _npcId.
	 */
	private final int _npcId;
	/**
	 * Field _isAlikeDead. Field _isInCombat. Field _isRunning. Field _isSummoned.
	 */
	private final boolean _isSummoned, _isRunning, _isInCombat, _isAlikeDead;
	/**
	 * Field _pAtkSpd. Field _mAtkSpd.
	 */
	private final int _mAtkSpd, _pAtkSpd;
	/**
	 * Field _swimWalkSpd. Field _swimRunSpd. Field _walkSpd. Field _runSpd.
	 */
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	/**
	 * Field _flRunSpd.
	 */
	private int _flRunSpd;
	/**
	 * Field _flWalkSpd.
	 */
	private int _flWalkSpd;
	/**
	 * Field _flyRunSpd.
	 */
	private int _flyRunSpd;
	/**
	 * Field _flyWalkSpd.
	 */
	private int _flyWalkSpd;
	/**
	 * Field _lhand. Field _rhand.
	 */
	private int _rhand, _lhand;
	/**
	 * Field _title. Field _name.
	 */
	private final String _name, _title;
	/**
	 * Field colHeight. Field colRadius.
	 */
	private final double colRadius, colHeight;
	/**
	 * Field _team.
	 */
	private final TeamType _team;
	
	/**
	 * Constructor for NpcInfoPoly.
	 * @param cha Player
	 */
	public NpcInfoPoly(Player cha)
	{
		_obj = cha;
		_npcId = cha.getPolyId();
		NpcTemplate template = NpcHolder.getInstance().getTemplate(_npcId);
		_rhand = 0;
		_lhand = 0;
		_isSummoned = false;
		colRadius = template.getCollisionRadius();
		colHeight = template.getCollisionHeight();
		_x = _obj.getX();
		_y = _obj.getY();
		_z = _obj.getZ();
		_rhand = template.rhand;
		_lhand = template.lhand;
		_heading = cha.getHeading();
		_mAtkSpd = cha.getMAtkSpd();
		_pAtkSpd = cha.getPAtkSpd();
		_runSpd = cha.getRunSpeed();
		_walkSpd = cha.getWalkSpeed();
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_isRunning = cha.isRunning();
		_isInCombat = cha.isInCombat();
		_isAlikeDead = cha.isAlikeDead();
		_name = cha.getName();
		_title = cha.getTitle();
		cha.getAbnormalEffect();
		_team = cha.getTeam();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		writeC(0x0c);
		writeD(_obj.getObjectId());
		writeD(_npcId + 1000000);
		writeD(0x00);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_heading);
		writeD(0x00);
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_swimRunSpd);
		writeD(_swimWalkSpd);
		writeD(_flRunSpd);
		writeD(_flWalkSpd);
		writeD(_flyRunSpd);
		writeD(_flyWalkSpd);
		writeF(1);
		writeF(1);
		writeF(colRadius);
		writeF(colHeight);
		writeD(_rhand);
		writeD(0);
		writeD(_lhand);
		writeC(1);
		writeC(_isRunning ? 1 : 0);
		writeC(_isInCombat ? 1 : 0);
		writeC(_isAlikeDead ? 1 : 0);
		writeC(_isSummoned ? 2 : 0);
		writeS(_name);
		writeS(_title);
		writeD(0);
		writeD(0);
		writeD(0000);
		writeD(0000);
		writeD(0000);
		writeD(0000);
		writeD(0000);
		writeC(0000);
		writeC(_team.ordinal());
		writeF(colRadius);
		writeF(colHeight);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeC(1);
		writeC(1);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeH(0x00);
		writeH(0x00);
		writeC(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}
}
