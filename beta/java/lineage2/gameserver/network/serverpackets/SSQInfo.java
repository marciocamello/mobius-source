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
 * Seven Signs Info
 * <p/>
 * packet id 0x73 format: cc
 * <p/>
 * Example package offs (828 minutes): 73 01 01   *
 * <p/>
 * Possible uses of this package 0 0 - Normal sky??? January 1 - Dusk Sky February 2 - Dawn Sky??? March 3 - The sky gradually turns red (for 10 seconds)   *
 * <p/>
 * Perhaps other variations, the effect is not well understood. 1 0 0 1
 * @author SYS
 */
public class SSQInfo extends L2GameServerPacket
{
	private int _state = 0;
	
	public SSQInfo()
	{
	}
	
	public SSQInfo(int state)
	{
		_state = state;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x73);
		
		switch (_state)
		{
			case 1:
				writeH(257);
				break;
			
			case 2:
				writeH(258);
				break;
			
			default:
				writeH(256);
				break;
		}
	}
}