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

import java.util.List;

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class NpcInfoPoly extends L2GameServerPacket
{
	private final Creature _obj;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _heading;
	private final int _npcId;
	final boolean _isSummoned;
	private final boolean _isRunning;
	private final boolean _isInCombat;
	private final boolean _isAlikeDead;
	private final boolean _canTarget;
	private final int _mAtkSpd;
	private final int _pAtkSpd;
	private final int _runSpd;
	private final int _walkSpd;
	final int _swimRunSpd;
	final int _swimWalkSpd;
	int _flRunSpd;
	int _flWalkSpd;
	int _flyRunSpd;
	int _flyWalkSpd;
	private int _rhand;
	private int _lhand;
	private final int _chest = 0;
	final String _name;
	private final String _title;
	final double _colRadius;
	final double _colHeight;
	final TeamType _team;
	private final List<Integer> _aveList;
	protected double _moveMultiplier;
	protected double _spdMultiplier;
	
	public NpcInfoPoly(Player cha)
	{
		_obj = cha;
		_npcId = cha.getPolyId();
		NpcTemplate template = NpcHolder.getInstance().getTemplate(_npcId);
		_rhand = 0;
		_lhand = 0;
		_isSummoned = false;
		_colRadius = template.getCollisionRadius();
		_colHeight = template.getCollisionHeight();
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
		_canTarget = cha.isTargetable();
		_name = cha.getName();
		_title = cha.getTitle();
		_team = cha.getTeam();
		_aveList = cha.getAveList();
		_moveMultiplier = cha.getMovementSpeedMultiplier();
		_spdMultiplier = cha.getAttackSpeedMultiplier();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x0C);
		writeD(_obj.getObjectId());
		writeC(0x00);
		writeC(0x25);
		writeC(0x00);
		writeC(0xED);
		if ((_rhand > 0) || (_lhand > 0))
		{
			writeC(0xFE);
		}
		else
		{
			writeC(0xBE);
		}
		writeC(0x4E);
		writeC(0xA2);
		writeC(0x0C);
		int len_poly_title = 0;
		if (_title != null)
		{
			len_poly_title = _title.length();
		}
		writeC(7 + (len_poly_title * 2));
		writeC(_obj.getKarma() < 0 ? 1 : 0);
		writeH(0);
		writeH(0);
		writeS(_title);
		if ((_rhand > 0) || (_lhand > 0))
		{
			writeH(68);
		}
		else
		{
			writeH(56);
		}
		writeD(_npcId + 1000000); // npctype id
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_heading);
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		
		putFloat(1.100000023841858f);
		putFloat(_pAtkSpd / 277.478340719f);
		
		if ((_rhand > 0) || (_lhand > 0))
		{
			writeD(_rhand); // right hand weapon
			writeD(_chest); // chest
			writeD(_lhand); // left hand weapon
		}
		
		writeC(1);
		writeC(_isRunning ? 1 : 0);
		writeC(_obj.isFlying() ? 2 : 0); // C2
		writeD(_obj.isFlying() ? 1 : 0);
		writeC(0);
		writeC(0);
		writeH(0);
		writeD((int) _obj.getCurrentHp());
		writeD(_obj.getMaxHp());
		
		writeC((_isInCombat ? 1 : 0) + (_isAlikeDead ? 2 : 0) + (_canTarget ? 4 : 0) + 8);
		
		if (_aveList != null)
		{
			writeH(_aveList.size());
			
			for (int i : _aveList)
			{
				writeH(i);
			}
		}
		else
		{
			writeH(0x00);
		}
	}
}