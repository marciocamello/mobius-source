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
public class PledgeReceiveUpdatePower extends L2GameServerPacket
{
	/**
	 * Field _privs.
	 */
	private final int _privs;
	
	/**
	 * Constructor for PledgeReceiveUpdatePower.
	 * @param privs int
	 */
	public PledgeReceiveUpdatePower(int privs)
	{
		_privs = privs;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x42);
		writeD(_privs);
	}
}
