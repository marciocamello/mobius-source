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
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.StaticObjectInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StaticObject extends L2GameServerPacket
{
	/**
	 * Field _staticObjectId.
	 */
	private final int _staticObjectId;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _isTargetable.
	 */
	private final int _isTargetable;
	/**
	 * Field _meshIndex.
	 */
	private final int _meshIndex;
	/**
	 * Field _isClosed.
	 */
	private final int _isClosed;
	/**
	 * Field _isEnemy.
	 */
	private final int _isEnemy;
	/**
	 * Field _maxHp.
	 */
	private final int _maxHp;
	/**
	 * Field _currentHp.
	 */
	private final int _currentHp;
	/**
	 * Field _showHp.
	 */
	private final int _showHp;
	/**
	 * Field _damageGrade.
	 */
	private final int _damageGrade;
	
	/**
	 * Constructor for StaticObject.
	 * @param obj StaticObjectInstance
	 */
	public StaticObject(StaticObjectInstance obj)
	{
		_staticObjectId = obj.getUId();
		_objectId = obj.getObjectId();
		_type = 0;
		_isTargetable = 1;
		_meshIndex = obj.getMeshIndex();
		_isClosed = 0;
		_isEnemy = 0;
		_maxHp = 0;
		_currentHp = 0;
		_showHp = 0;
		_damageGrade = 0;
	}
	
	/**
	 * Constructor for StaticObject.
	 * @param door DoorInstance
	 * @param player Player
	 */
	public StaticObject(DoorInstance door, Player player)
	{
		_staticObjectId = door.getDoorId();
		_objectId = door.getObjectId();
		_type = 1;
		_isTargetable = door.getTemplate().isTargetable() ? 1 : 0;
		_meshIndex = 1;
		_isClosed = door.isOpen() ? 0 : 1;
		_isEnemy = door.isAutoAttackable(player) ? 1 : 0;
		_currentHp = (int) door.getCurrentHp();
		_maxHp = door.getMaxHp();
		_showHp = door.isHPVisible() ? 1 : 0;
		_damageGrade = door.getDamage();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x9f);
		writeD(_staticObjectId);
		writeD(_objectId);
		writeD(_type);
		writeD(_isTargetable);
		writeD(_meshIndex);
		writeD(_isClosed);
		writeD(_isEnemy);
		writeD(_currentHp);
		writeD(_maxHp);
		writeD(_showHp);
		writeD(_damageGrade);
	}
}
