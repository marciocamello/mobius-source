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

public class ExCleftList extends L2GameServerPacket
{
	public static final int CleftType_Close = -1;
	public static final int CleftType_Total = 0;
	public static final int CleftType_Add = 1;
	public static final int CleftType_Remove = 2;
	public static final int CleftType_TeamChange = 3;
	private final int CleftType = 0; // TODO
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x95);
		writeD(CleftType);
		
		switch (CleftType)
		{
			case CleftType_Total:
				// dd (MinMemberCount:%d bBalancedMatch:%d)
				// BlueTeam: d[dS] (PlayerID:%d PlayerName:%s)
				// RedTeam: d[dS] (PlayerID:%d PlayerName:%s)
				break;
			
			case CleftType_Add:
				// ddS - TeamID:%d PlayerID:%d PlayerName:%s
				break;
			
			case CleftType_Remove:
				// dd - TeamID:%d PlayerID:%d
				break;
			
			case CleftType_TeamChange:
				// ddd - PlayerID:%d From:%d To:%d
				break;
			
			case CleftType_Close:
				break;
		}
	}
}