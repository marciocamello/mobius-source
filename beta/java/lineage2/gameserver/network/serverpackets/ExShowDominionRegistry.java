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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Smo
 */
public class ExShowDominionRegistry extends L2GameServerPacket
{
	private final int _dominionId;
	private final String _ownerClanName;
	private final String _ownerLeaderName;
	private final String _ownerAllyName;
	private final int _clanReq;
	private final int _mercReq;
	private final int _warTime;
	private final int _currentTime;
	private final boolean _registeredAsPlayer;
	private final boolean _registeredAsClan;
	private List<TerritoryFlagsInfo> _flags = Collections.emptyList();
	
	public ExShowDominionRegistry(Player activeChar, Dominion dominion)
	{
		_dominionId = dominion.getId();
		
		Clan owner = dominion.getOwner();
		Alliance alliance = owner.getAlliance();
		
		DominionSiegeEvent siegeEvent = dominion.getSiegeEvent();
		_ownerClanName = owner.getName();
		_ownerLeaderName = owner.getLeaderName();
		_ownerAllyName = alliance == null ? StringUtils.EMPTY : alliance.getAllyName();
		_warTime = (int) (dominion.getSiegeDate().getTimeInMillis() / 1000L);
		_currentTime = (int) (System.currentTimeMillis() / 1000L);
		_mercReq = siegeEvent.getObjects(DominionSiegeEvent.DEFENDER_PLAYERS).size();
		_clanReq = siegeEvent.getObjects(SiegeEvent.DEFENDERS).size() + 1;
		_registeredAsPlayer = siegeEvent.getObjects(DominionSiegeEvent.DEFENDER_PLAYERS).contains(activeChar.getObjectId());
		_registeredAsClan = siegeEvent.getSiegeClan(SiegeEvent.DEFENDERS, activeChar.getClan()) != null;
		
		List<Dominion> dominions = ResidenceHolder.getInstance().getResidenceList(Dominion.class);
		_flags = new ArrayList<>(dominions.size());
		for (Dominion d : dominions)
		{
			_flags.add(new TerritoryFlagsInfo(d.getId(), d.getFlags()));
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x91);
		
		writeD(_dominionId);
		writeS(_ownerClanName);
		writeS(_ownerLeaderName);
		writeS(_ownerAllyName);
		writeD(_clanReq); // Clan Request
		writeD(_mercReq); // Merc Request
		writeD(_warTime); // War Time
		writeD(_currentTime); // Current Time
		writeD(_registeredAsClan);
		writeD(_registeredAsPlayer);
		writeD(0x01);
		writeD(_flags.size()); // Territory Count
		for (TerritoryFlagsInfo cf : _flags)
		{
			writeD(cf.id); // Territory Id
			writeD(cf.flags.length); // Emblem Count
			for (int flag : cf.flags)
			{
				writeD(flag); // Emblem ID - should be in for loop for emblem count
			}
		}
	}
	
	private class TerritoryFlagsInfo
	{
		public int id;
		public int[] flags;
		
		public TerritoryFlagsInfo(int id_, int[] flags_)
		{
			id = id_;
			flags = flags_;
		}
	}
}
