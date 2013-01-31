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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExChangeAttributeInfo extends L2GameServerPacket
{
	/**
	 * Field _attribute.
	 */
	private int _attribute = -1;
	/**
	 * Field _ObjectIdStone.
	 */
	private final int _ObjectIdStone;
	
	/**
	 * Constructor for ExChangeAttributeInfo.
	 * @param att int
	 * @param ObjectIdStone int
	 */
	public ExChangeAttributeInfo(int att, int ObjectIdStone)
	{
		switch (att)
		{
			case 0:
				_attribute = -2;
				break;
			case 1:
				_attribute = -3;
				break;
			case 2:
				_attribute = -5;
				break;
			case 3:
				_attribute = -9;
				break;
			case 4:
				_attribute = -17;
				break;
			case 5:
				_attribute = -33;
		}
		_ObjectIdStone = ObjectIdStone;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x118);
		writeD(_ObjectIdStone);
		writeD(_attribute);
	}
}
