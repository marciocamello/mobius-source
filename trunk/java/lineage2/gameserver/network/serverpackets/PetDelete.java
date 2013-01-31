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
public class PetDelete extends L2GameServerPacket
{
	/**
	 * Field _summonType.
	 */
	private final int _summonType;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	
	/**
	 * Constructor for PetDelete.
	 * @param summonType int
	 * @param objectId int
	 */
	public PetDelete(int summonType, int objectId)
	{
		_summonType = summonType;
		_objectId = objectId;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xb7);
		writeD(_summonType);
		writeD(_objectId);
	}
}
