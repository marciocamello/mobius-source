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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.dao.SiegePlayerDAO;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.model.pledge.Privilege;
import lineage2.gameserver.network.serverpackets.ExReplyRegisterDominion;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExJoinDominionWar extends L2GameClientPacket
{
	/**
	 * Field _dominionId.
	 */
	private int _dominionId;
	/**
	 * Field _clanRegistration.
	 */
	private boolean _clanRegistration;
	/**
	 * Field _isRegistration.
	 */
	private boolean _isRegistration;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_dominionId = readD();
		_clanRegistration = readD() == 1;
		_isRegistration = readD() == 1;
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		Dominion dominion = ResidenceHolder.getInstance().getResidence(Dominion.class, _dominionId);
		if (dominion == null)
		{
			return;
		}
		DominionSiegeEvent siegeEvent = dominion.getSiegeEvent();
		if (siegeEvent.isRegistrationOver())
		{
			player.sendPacket(SystemMsg.IT_IS_NOT_A_TERRITORY_WAR_REGISTRATION_PERIOD_SO_A_REQUEST_CANNOT_BE_MADE_AT_THIS_TIME);
			return;
		}
		if ((player.getClan() != null) && (player.getClan().getCastle() > 0))
		{
			player.sendPacket(SystemMsg.THE_CLAN_WHO_OWNS_THE_TERRITORY_CANNOT_PARTICIPATE_IN_THE_TERRITORY_WAR_AS_MERCENARIES);
			return;
		}
		if ((player.getLevel() < 40) || (player.getClassLevel() <= 2))
		{
			player.sendPacket(SystemMsg.ONLY_CHARACTERS_WHO_ARE_LEVEL_40_OR_ABOVE_WHO_HAVE_COMPLETED_THEIR_SECOND_CLASS_TRANSFER_CAN_REGISTER_IN_A_TERRITORY_WAR);
			return;
		}
		int playerReg = 0;
		int clanReg = 0;
		for (Dominion d : ResidenceHolder.getInstance().getResidenceList(Dominion.class))
		{
			DominionSiegeEvent dominionSiegeEvent = d.getSiegeEvent();
			if (dominionSiegeEvent.getObjects(DominionSiegeEvent.DEFENDER_PLAYERS).contains(player.getObjectId()))
			{
				playerReg = d.getId();
			}
			else if (dominionSiegeEvent.getSiegeClan(SiegeEvent.DEFENDERS, player.getClan()) != null)
			{
				clanReg = d.getId();
			}
		}
		if (_isRegistration)
		{
			if (clanReg > 0)
			{
				player.sendPacket(SystemMsg.YOUVE_ALREADY_REQUESTED_A_TERRITORY_WAR_IN_ANOTHER_TERRITORY_ELSEWHERE);
				return;
			}
			if (!_clanRegistration && ((clanReg > 0) || (playerReg > 0)))
			{
				player.sendPacket(SystemMsg.YOUVE_ALREADY_REQUESTED_A_TERRITORY_WAR_IN_ANOTHER_TERRITORY_ELSEWHERE);
				return;
			}
			if (_clanRegistration)
			{
				if (!player.hasPrivilege(Privilege.CS_FS_SIEGE_WAR))
				{
					player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
					return;
				}
				SiegeClanObject object = new SiegeClanObject(SiegeEvent.DEFENDERS, player.getClan(), 0);
				siegeEvent.addObject(SiegeEvent.DEFENDERS, object);
				SiegeClanDAO.getInstance().insert(dominion, object);
				player.sendPacket(new SystemMessage2(SystemMsg.CLAN_PARTICIPATION_IS_REQUESTED_IN_S1_TERRITORY).addResidenceName(dominion));
			}
			else
			{
				siegeEvent.addObject(DominionSiegeEvent.DEFENDER_PLAYERS, player.getObjectId());
				SiegePlayerDAO.getInstance().insert(dominion, 0, player.getObjectId());
				player.sendPacket(new SystemMessage2(SystemMsg.MERCENARY_PARTICIPATION_IS_REQUESTED_IN_S1_TERRITORY).addResidenceName(dominion));
			}
		}
		else
		{
			if (_clanRegistration && (clanReg != dominion.getId()))
			{
				return;
			}
			if (!_clanRegistration && (playerReg != dominion.getId()))
			{
				return;
			}
			if (_clanRegistration)
			{
				if (!player.hasPrivilege(Privilege.CS_FS_SIEGE_WAR))
				{
					player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
					return;
				}
				SiegeClanObject clanObject = siegeEvent.getSiegeClan(SiegeEvent.DEFENDERS, player.getClan());
				siegeEvent.removeObject(SiegeEvent.DEFENDERS, clanObject);
				SiegeClanDAO.getInstance().delete(dominion, clanObject);
				player.sendPacket(new SystemMessage2(SystemMsg.CLAN_PARTICIPATION_REQUEST_IS_CANCELLED_IN_S1_TERRITORY).addResidenceName(dominion));
			}
			else
			{
				siegeEvent.removeObject(DominionSiegeEvent.DEFENDER_PLAYERS, player.getObjectId());
				SiegePlayerDAO.getInstance().delete(dominion, 0, player.getObjectId());
				player.sendPacket(new SystemMessage2(SystemMsg.MERCENARY_PARTICIPATION_REQUEST_IS_CANCELLED_IN_S1_TERRITORY).addResidenceName(dominion));
			}
		}
		player.sendPacket(new ExReplyRegisterDominion(dominion, true, _isRegistration, _clanRegistration));
	}
}
