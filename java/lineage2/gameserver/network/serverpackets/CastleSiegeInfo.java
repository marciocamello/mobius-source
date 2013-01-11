/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import java.util.Calendar;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class CastleSiegeInfo extends L2GameServerPacket
{
	private long _startTime;
	private final int _id, _ownerObjectId;
	private int _allyId;
	private boolean _isLeader;
	private String _ownerName = "NPC";
	private String _leaderName = StringUtils.EMPTY;
	private String _allyName = StringUtils.EMPTY;
	private int[] _nextTimeMillis = ArrayUtils.EMPTY_INT_ARRAY;
	
	public CastleSiegeInfo(Castle castle, Player player)
	{
		this((Residence) castle, player);
		CastleSiegeEvent siegeEvent = castle.getSiegeEvent();
		long siegeTimeMillis = castle.getSiegeDate().getTimeInMillis();
		if (siegeTimeMillis == 0)
		{
			_nextTimeMillis = siegeEvent.getNextSiegeTimes();
		}
		else
		{
			_startTime = (int) (siegeTimeMillis / 1000);
		}
	}
	
	public CastleSiegeInfo(ClanHall ch, Player player)
	{
		this((Residence) ch, player);
		_startTime = (int) (ch.getSiegeDate().getTimeInMillis() / 1000);
	}
	
	protected CastleSiegeInfo(Residence residence, Player player)
	{
		_id = residence.getId();
		_ownerObjectId = residence.getOwnerId();
		Clan owner = residence.getOwner();
		if (owner != null)
		{
			_isLeader = player.isGM() || (owner.getLeaderId(Clan.SUBUNIT_MAIN_CLAN) == player.getObjectId());
			_ownerName = owner.getName();
			_leaderName = owner.getLeaderName(Clan.SUBUNIT_MAIN_CLAN);
			Alliance ally = owner.getAlliance();
			if (ally != null)
			{
				_allyId = ally.getAllyId();
				_allyName = ally.getAllyName();
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xC9);
		writeD(_id);
		writeD(_isLeader ? 0x01 : 0x00);
		writeD(_ownerObjectId);
		writeS(_ownerName);
		writeS(_leaderName);
		writeD(_allyId);
		writeS(_allyName);
		writeD((int) (Calendar.getInstance().getTimeInMillis() / 1000));
		writeD((int) _startTime);
		if (_startTime == 0)
		{
			writeDD(_nextTimeMillis, true);
		}
	}
}
