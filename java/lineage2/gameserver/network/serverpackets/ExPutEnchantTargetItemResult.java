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
public class ExPutEnchantTargetItemResult extends L2GameServerPacket
{
	/**
	 * Field FAIL.
	 */
	public static final L2GameServerPacket FAIL = new ExPutEnchantTargetItemResult(0);
	/**
	 * Field SUCCESS.
	 */
	public static final L2GameServerPacket SUCCESS = new ExPutEnchantTargetItemResult(1);
	/**
	 * Field _result.
	 */
	private final int _result;
	
	/**
	 * Constructor for ExPutEnchantTargetItemResult.
	 * @param result int
	 */
	public ExPutEnchantTargetItemResult(int result)
	{
		_result = result;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x81);
		writeD(_result);
	}
}
