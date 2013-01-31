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

import lineage2.gameserver.model.Creature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MagicSkillUse extends L2GameServerPacket
{
	/**
	 * Field _classChange.
	 */
	private int _classChange = -1;
	/**
	 * Field _targetId.
	 */
	private final int _targetId;
	/**
	 * Field _skillId.
	 */
	private final int _skillId;
	/**
	 * Field _skillLevel.
	 */
	private final int _skillLevel;
	/**
	 * Field _hitTime.
	 */
	private final int _hitTime;
	/**
	 * Field _reuseDelay.
	 */
	private final int _reuseDelay;
	/**
	 * Field _tz. Field _ty. Field _tx. Field _z. Field _y. Field _x. Field _chaId.
	 */
	private final int _chaId, _x, _y, _z, _tx, _ty, _tz;
	/**
	 * Field _isDoubleCasting.
	 */
	private final boolean _isDoubleCasting;
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(MagicSkillUse.class);
	
	/**
	 * Constructor for MagicSkillUse.
	 * @param cha Creature
	 * @param target Creature
	 * @param skillId int
	 * @param skillLevel int
	 * @param hitTime int
	 * @param reuseDelay long
	 * @param isDoubleCastingNow boolean
	 */
	public MagicSkillUse(Creature cha, Creature target, int skillId, int skillLevel, int hitTime, long reuseDelay, boolean isDoubleCastingNow)
	{
		_chaId = cha.getObjectId();
		_targetId = target.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = (int) reuseDelay;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
		if ((skillId >= 1566) && (skillId <= 1569))
		{
			_classChange = skillId;
		}
		_isDoubleCasting = isDoubleCastingNow;
	}
	
	/**
	 * Constructor for MagicSkillUse.
	 * @param cha Creature
	 * @param target Creature
	 * @param skillId int
	 * @param skillLevel int
	 * @param hitTime int
	 * @param reuseDelay long
	 */
	public MagicSkillUse(Creature cha, Creature target, int skillId, int skillLevel, int hitTime, long reuseDelay)
	{
		_chaId = cha.getObjectId();
		_targetId = target.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = (int) reuseDelay;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
		if ((skillId >= 1566) && (skillId <= 1569))
		{
			_classChange = skillId;
		}
		_isDoubleCasting = false;
	}
	
	/**
	 * Constructor for MagicSkillUse.
	 * @param cha Creature
	 * @param skillId int
	 * @param skillLevel int
	 * @param hitTime int
	 * @param reuseDelay long
	 */
	public MagicSkillUse(Creature cha, int skillId, int skillLevel, int hitTime, long reuseDelay)
	{
		_chaId = cha.getObjectId();
		_targetId = cha.getTargetId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = (int) reuseDelay;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_tx = cha.getX();
		_ty = cha.getY();
		_tz = cha.getZ();
		if ((skillId >= 1566) && (skillId <= 1569))
		{
			_classChange = skillId;
		}
		_isDoubleCasting = false;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x48);
		writeD(_isDoubleCasting ? 1 : 0);
		writeD(_chaId);
		writeD(_targetId);
		writeC(0x00);
		writeD(_skillId);
		writeD(_skillLevel);
		writeD(_hitTime);
		writeD(_classChange);
		writeD(_reuseDelay);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(0x00);
		writeD(_tx);
		writeD(_ty);
		writeD(_tz);
	}
}
