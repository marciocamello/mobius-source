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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowDominionRegistry extends L2GameServerPacket
{
	/**
	 * Field _dominionId.
	 */
	private final int _dominionId;
	/**
	 * Field _ownerClanName.
	 */
	private final String _ownerClanName;
	/**
	 * Field _ownerLeaderName.
	 */
	private final String _ownerLeaderName;
	/**
	 * Field _ownerAllyName.
	 */
	private final String _ownerAllyName;
	/**
	 * Field _clanReq.
	 */
	private final int _clanReq;
	/**
	 * Field _mercReq.
	 */
	private final int _mercReq;
	/**
	 * Field _warTime.
	 */
	private final int _warTime;
	/**
	 * Field _currentTime.
	 */
	private final int _currentTime;
	/**
	 * Field _registeredAsPlayer.
	 */
	private final boolean _registeredAsPlayer;
	/**
	 * Field _registeredAsClan.
	 */
	private final boolean _registeredAsClan;
	/**
	 * Field _flags.
	 */
	private List<TerritoryFlagsInfo> _flags = Collections.emptyList();
	
	/**
	 * Constructor for ExShowDominionRegistry.
	 * @param activeChar Player
	 * @param dominion Dominion
	 */
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
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x90);
		writeD(_dominionId);
		writeS(_ownerClanName);
		writeS(_ownerLeaderName);
		writeS(_ownerAllyName);
		writeD(_clanReq);
		writeD(_mercReq);
		writeD(_warTime);
		writeD(_currentTime);
		writeD(_registeredAsClan);
		writeD(_registeredAsPlayer);
		writeD(0x01);
		writeD(_flags.size());
		for (TerritoryFlagsInfo cf : _flags)
		{
			writeD(cf.id);
			writeD(cf.flags.length);
			for (int flag : cf.flags)
			{
				writeD(flag);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class TerritoryFlagsInfo
	{
		/**
		 * Field id.
		 */
		public int id;
		/**
		 * Field flags.
		 */
		public int[] flags;
		
		/**
		 * Constructor for TerritoryFlagsInfo.
		 * @param id_ int
		 * @param flags_ int[]
		 */
		public TerritoryFlagsInfo(int id_, int[] flags_)
		{
			id = id_;
			flags = flags_;
		}
	}
}
