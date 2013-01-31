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

import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleSiegeAttackerList extends L2GameServerPacket
{
	/**
	 * Field _registrationValid. Field _id.
	 */
	private final int _id, _registrationValid;
	/**
	 * Field _clans.
	 */
	private List<SiegeClanObject> _clans = Collections.emptyList();
	
	/**
	 * Constructor for CastleSiegeAttackerList.
	 * @param residence Residence
	 */
	public CastleSiegeAttackerList(Residence residence)
	{
		_id = residence.getId();
		_registrationValid = !residence.getSiegeEvent().isRegistrationOver() ? 1 : 0;
		_clans = residence.getSiegeEvent().getObjects(SiegeEvent.ATTACKERS);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xCA);
		writeD(_id);
		writeD(0x00);
		writeD(_registrationValid);
		writeD(0x00);
		writeD(_clans.size());
		writeD(_clans.size());
		for (SiegeClanObject siegeClan : _clans)
		{
			Clan clan = siegeClan.getClan();
			writeD(clan.getClanId());
			writeS(clan.getName());
			writeS(clan.getLeaderName());
			writeD(clan.getCrestId());
			writeD((int) (siegeClan.getDate() / 1000L));
			Alliance alliance = clan.getAlliance();
			writeD(clan.getAllyId());
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
				writeD(0);
			}
		}
	}
}
