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
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleSiegeDefenderList extends L2GameServerPacket
{
	/**
	 * Field OWNER.
	 */
	public static int OWNER = 1;
	/**
	 * Field WAITING.
	 */
	public static int WAITING = 2;
	/**
	 * Field ACCEPTED.
	 */
	public static int ACCEPTED = 3;
	/**
	 * Field REFUSE.
	 */
	public static int REFUSE = 4;
	/**
	 * Field _registrationValid. Field _id.
	 */
	private final int _id, _registrationValid;
	/**
	 * Field _defenderClans.
	 */
	private List<DefenderClan> _defenderClans = Collections.emptyList();
	
	/**
	 * Constructor for CastleSiegeDefenderList.
	 * @param castle Castle
	 */
	public CastleSiegeDefenderList(Castle castle)
	{
		_id = castle.getId();
		_registrationValid = !castle.getSiegeEvent().isRegistrationOver() && (castle.getOwner() != null) ? 1 : 0;
		List<SiegeClanObject> defenders = castle.getSiegeEvent().getObjects(SiegeEvent.DEFENDERS);
		List<SiegeClanObject> defendersWaiting = castle.getSiegeEvent().getObjects(CastleSiegeEvent.DEFENDERS_WAITING);
		List<SiegeClanObject> defendersRefused = castle.getSiegeEvent().getObjects(CastleSiegeEvent.DEFENDERS_REFUSED);
		_defenderClans = new ArrayList<>(defenders.size() + defendersWaiting.size() + defendersRefused.size());
		if (castle.getOwner() != null)
		{
			_defenderClans.add(new DefenderClan(castle.getOwner(), OWNER, 0));
		}
		for (SiegeClanObject siegeClan : defenders)
		{
			_defenderClans.add(new DefenderClan(siegeClan.getClan(), ACCEPTED, (int) (siegeClan.getDate() / 1000L)));
		}
		for (SiegeClanObject siegeClan : defendersWaiting)
		{
			_defenderClans.add(new DefenderClan(siegeClan.getClan(), WAITING, (int) (siegeClan.getDate() / 1000L)));
		}
		for (SiegeClanObject siegeClan : defendersRefused)
		{
			_defenderClans.add(new DefenderClan(siegeClan.getClan(), REFUSE, (int) (siegeClan.getDate() / 1000L)));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xCB);
		writeD(_id);
		writeD(0x00);
		writeD(_registrationValid);
		writeD(0x00);
		writeD(_defenderClans.size());
		writeD(_defenderClans.size());
		for (DefenderClan defenderClan : _defenderClans)
		{
			Clan clan = defenderClan._clan;
			writeD(clan.getClanId());
			writeS(clan.getName());
			writeS(clan.getLeaderName());
			writeD(clan.getCrestId());
			writeD(defenderClan._time);
			writeD(defenderClan._type);
			writeD(clan.getAllyId());
			Alliance alliance = clan.getAlliance();
			if (alliance != null)
			{
				writeS(alliance.getAllyName());
				writeS(alliance.getAllyLeaderName());
				writeD(alliance.getAllyCrestId());
			}
			else
			{
				writeS(StringUtils.EMPTY);
				writeS(StringUtils.EMPTY);
				writeD(0x00);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class DefenderClan
	{
		/**
		 * Field _clan.
		 */
		final Clan _clan;
		/**
		 * Field _type.
		 */
		final int _type;
		/**
		 * Field _time.
		 */
		final int _time;
		
		/**
		 * Constructor for DefenderClan.
		 * @param clan Clan
		 * @param type int
		 * @param time int
		 */
		public DefenderClan(Clan clan, int type, int time)
		{
			_clan = clan;
			_type = type;
			_time = time;
		}
	}
}
