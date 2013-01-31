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

import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMentorAdd extends L2GameServerPacket
{
	/**
	 * Field _newMentorName.
	 */
	private final String _newMentorName;
	/**
	 * Field _newMentorLvl. Field _newMentorClassId.
	 */
	private final int _newMentorClassId, _newMentorLvl;
	
	/**
	 * Constructor for ExMentorAdd.
	 * @param newMentor Player
	 */
	public ExMentorAdd(Player newMentor)
	{
		_newMentorName = newMentor.getName();
		_newMentorClassId = newMentor.getClassId().getId();
		_newMentorLvl = newMentor.getLevel();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x121);
		writeS(_newMentorName);
		writeD(_newMentorClassId);
		writeD(_newMentorLvl);
	}
}
