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
 * Created by IntelliJ IDEA. User: Darvin Date: 24.02.12 Time: 18:05
 */
public class FriendAdd extends L2GameServerPacket
{
	
	private final String _unk;
	
	public FriendAdd(String unk)
	{
		_unk = unk;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x56);
		writeS(_unk);
		writeD(0);
		writeD(0);
		writeS(_unk);
		writeC(0);
		writeD(0);
	}
}
