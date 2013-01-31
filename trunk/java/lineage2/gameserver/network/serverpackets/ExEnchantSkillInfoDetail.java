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

import lineage2.gameserver.tables.SkillTreeTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExEnchantSkillInfoDetail extends L2GameServerPacket
{
	/**
	 * Field _unk.
	 */
	private final int _unk = 0;
	/**
	 * Field _skillId.
	 */
	private final int _skillId;
	/**
	 * Field _skillLvl.
	 */
	private final int _skillLvl;
	/**
	 * Field _sp.
	 */
	private final int _sp;
	/**
	 * Field _chance.
	 */
	private final int _chance;
	/**
	 * Field _adenaCount. Field _bookId.
	 */
	private final int _bookId, _adenaCount;
	
	/**
	 * Constructor for ExEnchantSkillInfoDetail.
	 * @param skillId int
	 * @param skillLvl int
	 * @param sp int
	 * @param chance int
	 * @param bookId int
	 * @param adenaCount int
	 */
	public ExEnchantSkillInfoDetail(int skillId, int skillLvl, int sp, int chance, int bookId, int adenaCount)
	{
		_skillId = skillId;
		_skillLvl = skillLvl;
		_sp = sp;
		_chance = chance;
		_bookId = bookId;
		_adenaCount = adenaCount;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x5E);
		writeD(_unk);
		writeD(_skillId);
		writeD(_skillLvl);
		writeD(_sp);
		writeD(_chance);
		writeD(2);
		writeD(57);
		writeD(_adenaCount);
		if (_bookId > 0)
		{
			writeD(_bookId);
			writeD(1);
		}
		else
		{
			writeD(SkillTreeTable.NORMAL_ENCHANT_BOOK);
			writeD(0);
		}
	}
}
