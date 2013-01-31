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
public class ExVariationResult extends L2GameServerPacket
{
	/**
	 * Field _stat12.
	 */
	private final int _stat12;
	/**
	 * Field _stat34.
	 */
	private final int _stat34;
	/**
	 * Field _unk3.
	 */
	private final int _unk3;
	
	/**
	 * Constructor for ExVariationResult.
	 * @param unk1 int
	 * @param unk2 int
	 * @param unk3 int
	 */
	public ExVariationResult(int unk1, int unk2, int unk3)
	{
		_stat12 = unk1;
		_stat34 = unk2;
		_unk3 = unk3;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x56);
		writeD(_stat12);
		writeD(_stat34);
		writeD(_unk3);
	}
}
