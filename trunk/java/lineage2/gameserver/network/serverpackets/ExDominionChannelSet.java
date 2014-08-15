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
 * @author Smo
 */
public class ExDominionChannelSet extends L2GameServerPacket
{
	public static final L2GameServerPacket ACTIVE = new ExDominionChannelSet(1);
	public static final L2GameServerPacket DEACTIVE = new ExDominionChannelSet(0);
	
	private final int _active;
	
	public ExDominionChannelSet(int active)
	{
		_active = active;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x97);
		writeD(_active);
	}
}
