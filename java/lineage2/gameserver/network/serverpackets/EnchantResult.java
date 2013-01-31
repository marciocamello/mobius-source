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
public class EnchantResult extends L2GameServerPacket
{
	/**
	 * Field _crystalId. Field _resultId.
	 */
	private final int _resultId, _crystalId;
	/**
	 * Field _count.
	 */
	private final long _count;
	/**
	 * Field _enchantValue.
	 */
	private final int _enchantValue;
	/**
	 * Field SUCESS.
	 */
	public static final EnchantResult SUCESS = new EnchantResult(0, 0, 0);
	/**
	 * Field CANCEL.
	 */
	public static final EnchantResult CANCEL = new EnchantResult(2, 0, 0);
	/**
	 * Field BLESSED_FAILED.
	 */
	public static final EnchantResult BLESSED_FAILED = new EnchantResult(3, 0, 0);
	/**
	 * Field FAILED_NO_CRYSTALS.
	 */
	public static final EnchantResult FAILED_NO_CRYSTALS = new EnchantResult(4, 0, 0);
	/**
	 * Field ANCIENT_FAILED.
	 */
	public static final EnchantResult ANCIENT_FAILED = new EnchantResult(5, 0, 0);
	
	/**
	 * Constructor for EnchantResult.
	 * @param resultId int
	 * @param crystalId int
	 * @param count long
	 */
	public EnchantResult(int resultId, int crystalId, long count)
	{
		this(resultId, crystalId, count, 0);
	}
	
	/**
	 * Constructor for EnchantResult.
	 * @param resultId int
	 * @param crystalId int
	 * @param count long
	 * @param enchantValue int
	 */
	public EnchantResult(int resultId, int crystalId, long count, int enchantValue)
	{
		_resultId = resultId;
		_crystalId = crystalId;
		_count = count;
		_enchantValue = enchantValue;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x87);
		writeD(_resultId);
		writeD(_crystalId);
		writeQ(_count);
		writeD(_enchantValue);
	}
}
