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
 * @author SYS
 * @date 10/9/2007
 */
public class EventTrigger extends L2GameServerPacket
{
	private final int _trapId;
	private final boolean _active;
	
	public EventTrigger(int trapId, boolean active)
	{
		_trapId = trapId;
		_active = active;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xCF);
		writeD(_trapId); // trap object id
		writeC(_active ? 1 : 0); // trap activity 1 or 0
	}
}