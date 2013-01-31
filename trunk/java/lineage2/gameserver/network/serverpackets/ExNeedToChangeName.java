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
public class ExNeedToChangeName extends L2GameServerPacket
{
	/**
	 * Field _reason. Field _type.
	 */
	private final int _type, _reason;
	/**
	 * Field _origName.
	 */
	private final String _origName;
	
	/**
	 * Constructor for ExNeedToChangeName.
	 * @param type int
	 * @param reason int
	 * @param origName String
	 */
	public ExNeedToChangeName(int type, int reason, String origName)
	{
		_type = type;
		_reason = reason;
		_origName = origName;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x69);
		writeD(_type);
		writeD(_reason);
		writeS(_origName);
	}
}
