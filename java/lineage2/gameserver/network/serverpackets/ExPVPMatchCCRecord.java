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

import lineage2.gameserver.model.entity.events.impl.KrateisCubeEvent;
import lineage2.gameserver.model.entity.events.objects.KrateisCubePlayerObject;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExPVPMatchCCRecord extends L2GameServerPacket
{
	/**
	 * Field _players.
	 */
	private final KrateisCubePlayerObject[] _players;
	
	/**
	 * Constructor for ExPVPMatchCCRecord.
	 * @param cube KrateisCubeEvent
	 */
	public ExPVPMatchCCRecord(KrateisCubeEvent cube)
	{
		_players = cube.getSortedPlayers();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	public void writeImpl()
	{
		writeEx(0x89);
		writeD(0x00);
		writeD(_players.length);
		for (KrateisCubePlayerObject p : _players)
		{
			writeS(p.getName());
			writeD(p.getPoints());
		}
	}
}
