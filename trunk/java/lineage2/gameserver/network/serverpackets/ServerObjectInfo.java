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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.StatueInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ServerObjectInfo extends L2GameServerPacket
{
	/**
	 * Field _activeChar.
	 */
	private final NpcInstance _activeChar;
	/**
	 * Field _heading. Field _z. Field _y. Field _x.
	 */
	private final int _x, _y, _z, _heading;
	/**
	 * Field _idTemplate.
	 */
	private int _idTemplate;
	/**
	 * Field _isAttackable.
	 */
	private boolean _isAttackable;
	/**
	 * Field _collisionRadius. Field _collisionHeight.
	 */
	private double _collisionHeight, _collisionRadius;
	/**
	 * Field _name.
	 */
	private String _name;
	/**
	 * Field _type.
	 */
	private int _type;
	
	/**
	 * Constructor for ServerObjectInfo.
	 * @param activeChar NpcInstance
	 * @param actor Creature
	 */
	public ServerObjectInfo(NpcInstance activeChar, Creature actor)
	{
		_activeChar = activeChar;
		_idTemplate = _activeChar.getTemplate().npcId;
		_isAttackable = _activeChar.isAutoAttackable(actor);
		_collisionHeight = _activeChar.getCollisionHeight();
		_collisionRadius = _activeChar.getCollisionRadius();
		_x = _activeChar.getX();
		_y = _activeChar.getY();
		_z = _activeChar.getZ();
		_heading = _activeChar.getHeading();
		_name = _activeChar.getTemplate().name;
		_type = 4;
		if (_activeChar instanceof StatueInstance)
		{
			_idTemplate = 0;
			_name = _activeChar.getName();
			_isAttackable = false;
			_collisionHeight = 30;
			_collisionRadius = 40;
			_type = 7;
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x92);
		writeD(_activeChar.getObjectId());
		writeD(_idTemplate + 1000000);
		writeS(_name);
		writeD(_isAttackable ? 1 : 0);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_heading);
		writeF(1.0);
		writeF(1.0);
		writeF(_collisionRadius);
		writeF(_collisionHeight);
		writeD((int) (_isAttackable ? _activeChar.getCurrentHp() : 0));
		writeD(_isAttackable ? _activeChar.getMaxHp() : 0);
		writeD(_type);
		writeD(0x00);
		if (_type == 7)
		{
			StatueInstance statue = (StatueInstance) _activeChar;
			writeD(statue.getRecordId());
			writeD(0x00);
			writeD(statue.getSocialId());
			writeD(statue.getSocialFrame());
			writeD(statue.getClassId());
			writeD(statue.getRace());
			writeD(statue.getSex());
			writeD(statue.getHairStyle());
			writeD(statue.getHairColor());
			writeD(statue.getFace());
			writeD(statue.getNecklace());
			writeD(statue.getHead());
			writeD(statue.getRHand());
			writeD(statue.getLHand());
			writeD(statue.getGloves());
			writeD(statue.getChest());
			writeD(statue.getPants());
			writeD(statue.getBoots());
			writeD(statue.getCloak());
			writeD(statue.getHair1());
			writeD(statue.getHair2());
		}
	}
}
