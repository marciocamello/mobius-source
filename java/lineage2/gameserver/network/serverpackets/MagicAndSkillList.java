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

import lineage2.gameserver.model.Creature;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MagicAndSkillList extends L2GameServerPacket
{
	/**
	 * Field _chaId.
	 */
	private final int _chaId;
	/**
	 * Field _unk1.
	 */
	private final int _unk1;
	/**
	 * Field _unk2.
	 */
	private final int _unk2;
	
	/**
	 * Constructor for MagicAndSkillList.
	 * @param cha Creature
	 * @param unk1 int
	 * @param unk2 int
	 */
	public MagicAndSkillList(Creature cha, int unk1, int unk2)
	{
		_chaId = cha.getObjectId();
		_unk1 = unk1;
		_unk2 = unk2;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x40);
		writeD(_chaId);
		writeD(_unk1);
		writeD(_unk2);
	}
}
