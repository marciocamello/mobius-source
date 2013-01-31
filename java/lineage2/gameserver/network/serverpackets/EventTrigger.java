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
public class EventTrigger extends L2GameServerPacket
{
	/**
	 * Field _trapId.
	 */
	private final int _trapId;
	/**
	 * Field _active.
	 */
	private final boolean _active;
	
	/**
	 * Constructor for EventTrigger.
	 * @param trapId int
	 * @param active boolean
	 */
	public EventTrigger(int trapId, boolean active)
	{
		_trapId = trapId;
		_active = active;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xCF);
		writeD(_trapId);
		writeC(_active ? 1 : 0);
	}
}
