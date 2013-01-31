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
public class PledgeSkillListAdd extends L2GameServerPacket
{
	/**
	 * Field _skillId.
	 */
	private final int _skillId;
	/**
	 * Field _skillLevel.
	 */
	private final int _skillLevel;
	
	/**
	 * Constructor for PledgeSkillListAdd.
	 * @param skillId int
	 * @param skillLevel int
	 */
	public PledgeSkillListAdd(int skillId, int skillLevel)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x3b);
		writeD(_skillId);
		writeD(_skillLevel);
	}
}
