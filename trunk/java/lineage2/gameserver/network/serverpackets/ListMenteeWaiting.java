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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListMenteeWaiting extends L2GameServerPacket
{
	public static final Logger _log = LoggerFactory.getLogger(ListMenteeWaiting.class);
	List<Player> mentees;
	int page;
	int playersInPage;
	
	public ListMenteeWaiting(Player activeChar, int _page, int minLevel, int maxLevel)
	{
		mentees = new ArrayList<>();
		page = _page;
		playersInPage = 64;
		
		for (Player player : World.getAroundPlayers(activeChar))
		{
			int mentorId = player.getMentorSystem().getMentor();
			// _log.info("players: " + player + " mentee: [" + mentorId + "]");
			if ((player.getLevel() >= minLevel) && (player.getLevel() <= maxLevel) && (mentorId == 0) && !player.isAwaking())
			{
				mentees.add(player);
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x123);
		
		writeD(page);
		int i;
		if (!mentees.isEmpty())
		{
			writeD(mentees.size());
			writeD(mentees.size() % playersInPage);
			i = 1;
			for (Player player : mentees)
			{
				if ((i <= (playersInPage * page)) && (i > (playersInPage * (page - 1))))
				{
					writeS(player.getName());
					writeD(player.getClassId().ordinal());
					writeD(player.getLevel());
				}
			}
		}
		else
		{
			writeD(0x00);
			writeD(0x00);
		}
	}
}
