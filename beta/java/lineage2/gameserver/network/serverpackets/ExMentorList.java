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

import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.MenteeInfo;
import lineage2.gameserver.model.Player;

public class ExMentorList extends L2GameServerPacket
{
	private List<MenteeInfo> _list = Collections.emptyList();
	private final int _mentor;
	private final Player _activeChar;
	
	/**
	 * Constructor for ExMentorList.
	 * @param player Player
	 */
	public ExMentorList(Player player)
	{
		_mentor = player.getMentorSystem().getMentor();
		_list = player.getMentorSystem().getMenteeInfo();
		_activeChar = player;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x11B);
		writeD((_mentor == 0) && _activeChar.isMentor() ? 0x01 : 0x02);
		writeD(0);
		writeD(_list.size());
		
		for (MenteeInfo entry : _list)
		{
			writeD(entry.getObjectId());
			writeS(entry.getName());
			writeD(entry.getClassId());
			writeD(entry.getLevel());
			writeD(entry.isOnline());
		}
	}
}
