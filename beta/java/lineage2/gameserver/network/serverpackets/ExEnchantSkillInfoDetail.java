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

import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.tables.SkillTreeTable;

public class ExEnchantSkillInfoDetail extends L2GameServerPacket
{
	private final int _type;
	private final int _skillId;
	private final int _skillLvl;
	private final int _sp;
	private final int _chance;
	private final int _bookId, _adenaCount;
	
	public ExEnchantSkillInfoDetail(int type, int skillId, int skillLvl, int sp, int chance, int bookId, int adenaCount)
	{
		_skillId = skillId;
		_skillLvl = skillLvl;
		_sp = sp;
		_chance = chance;
		_bookId = bookId;
		_adenaCount = adenaCount;
		_type = type;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x5F);
		writeD(_type);
		writeD(_skillId);
		writeD(_skillLvl);
		writeD(_sp);
		writeD(_chance);
		writeD(0x02); // items count?
		writeD(Inventory.ADENA_ID); // Adena
		writeD(_adenaCount); // adena count ?
		if (_bookId > 0)
		{
			writeD(_bookId); // book
			writeD(1); // book count
		}
		else
		{
			writeD(SkillTreeTable.NORMAL_ENCHANT_BOOK); // book
			writeD(0); // book count
		}
	}
}