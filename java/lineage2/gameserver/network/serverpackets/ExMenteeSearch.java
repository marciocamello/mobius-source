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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMenteeSearch extends L2GameServerPacket
{
	/**
	 * Field mentees.
	 */
	ArrayList<Player> mentees;
	/**
	 * Field playersInPage. Field page.
	 */
	int page, playersInPage;
	
	/**
	 * Constructor for ExMenteeSearch.
	 * @param activeChar Player
	 * @param _page int
	 * @param minLevel int
	 * @param maxLevel int
	 */
	public ExMenteeSearch(Player activeChar, int _page, int minLevel, int maxLevel)
	{
		mentees = new ArrayList<>();
		page = _page;
		playersInPage = 64;
		for (Player player : World.getAroundPlayers(activeChar))
		{
			if ((player.getLevel() >= minLevel) && (player.getLevel() <= maxLevel))
			{
				mentees.add(player);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x122);
		writeD(page);
		if (!mentees.isEmpty())
		{
			writeD(mentees.size());
			writeD(mentees.size() % playersInPage);
			int i = 1;
			for (Player player : mentees)
			{
				if ((i <= (playersInPage * page)) && (i > (playersInPage * (page - 1))))
				{
					writeS(player.getName());
					writeD(player.getClassId().getId());
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
	
	/**
	 * Method getType.
	 * @return String
	 */
	@Override
	public String getType()
	{
		return null;
	}
}
