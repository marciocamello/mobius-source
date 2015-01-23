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
package lineage2.gameserver.model.entity.events.impl;

import java.util.List;

import lineage2.commons.collections.CollectionUtils;
import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.dao.SiegePlayerDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.events.objects.CTBSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.CTBTeamObject;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanHallTeamBattleEvent extends SiegeEvent<ClanHall, CTBSiegeClanObject>
{
	public static final String TRYOUT_PART = "tryout_part";
	private static final String CHALLENGER_RESTART_POINTS = "challenger_restart_points";
	private static final String NEXT_STEP = "next_step";
	public static final String FIRST_DOORS = "first_doors";
	public static final String SECOND_DOORS = "second_doors";
	
	/**
	 * Constructor for ClanHallTeamBattleEvent.
	 * @param set MultiValueSet<String>
	 */
	public ClanHallTeamBattleEvent(MultiValueSet<String> set)
	{
		super(set);
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		_oldOwner = getResidence().getOwner();
		List<CTBSiegeClanObject> attackers = getObjects(ATTACKERS);
		
		if (attackers.isEmpty())
		{
			if (_oldOwner == null)
			{
				broadcastInZone2(SystemMessage.getSystemMessage(SystemMessageId.THE_SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST).addCastleId(getResidence().getId()));
			}
			else
			{
				broadcastInZone2(SystemMessage.getSystemMessage(SystemMessageId.S1_S_SIEGE_WAS_CANCELED_BECAUSE_THERE_WERE_NO_CLANS_THAT_PARTICIPATED).addCastleId(getResidence().getId()));
			}
			
			reCalcNextTime(false);
			return;
		}
		
		if (_oldOwner != null)
		{
			addObject(DEFENDERS, new SiegeClanObject(DEFENDERS, _oldOwner, 0));
		}
		
		SiegeClanDAO.getInstance().delete(getResidence());
		SiegePlayerDAO.getInstance().delete(getResidence());
		List<CTBTeamObject> teams = getObjects(TRYOUT_PART);
		
		for (int i = 0; i < 5; i++)
		{
			CTBTeamObject team = teams.get(i);
			team.setSiegeClan(CollectionUtils.safeGet(attackers, i));
		}
		
		broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.THE_SIEGE_TO_CONQUER_S1_HAS_BEGUN).addCastleId(getResidence().getId()), ATTACKERS, DEFENDERS);
		broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.THE_TRYOUTS_ARE_ABOUT_TO_BEGIN_LINE_UP), ATTACKERS);
		super.startEvent();
	}
	
	/**
	 * Method nextStep.
	 */
	public void nextStep()
	{
		broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.THE_TRYOUTS_HAVE_BEGUN), ATTACKERS, DEFENDERS);
		updateParticles(true, ATTACKERS, DEFENDERS);
	}
	
	/**
	 * Method processStep.
	 * @param team CTBTeamObject
	 */
	public void processStep(CTBTeamObject team)
	{
		if (team.getSiegeClan() != null)
		{
			CTBSiegeClanObject object = team.getSiegeClan();
			object.setEvent(false, this);
			teleportPlayers(SPECTATORS);
		}
		
		team.despawnObject(this);
		List<CTBTeamObject> teams = getObjects(TRYOUT_PART);
		boolean hasWinner = false;
		CTBTeamObject winnerTeam = null;
		
		for (CTBTeamObject t : teams)
		{
			if (t.isParticle())
			{
				hasWinner = winnerTeam == null;
				winnerTeam = t;
			}
		}
		
		if (!hasWinner || (winnerTeam == null))
		{
			return;
		}
		
		SiegeClanObject clan = winnerTeam.getSiegeClan();
		
		if (clan != null)
		{
			getResidence().changeOwner(clan.getClan());
		}
		
		stopEvent(true);
	}
	
	/**
	 * Method announce.
	 * @param val int
	 */
	@Override
	public void announce(int val)
	{
		int minute = val / 60;
		
		if (minute > 0)
		{
			broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.THE_CONTEST_WILL_BEGIN_IN_S1_MINUTE_S).addInt(minute), ATTACKERS, DEFENDERS);
		}
		else
		{
			broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.THE_PRELIMINARY_MATCH_WILL_BEGIN_IN_S1_SECOND_S_PREPARE_YOURSELF).addInt(val), ATTACKERS, DEFENDERS);
		}
	}
	
	/**
	 * Method stopEvent.
	 * @param step boolean
	 */
	@Override
	public void stopEvent(boolean step)
	{
		Clan newOwner = getResidence().getOwner();
		
		if (newOwner != null)
		{
			if (_oldOwner != newOwner)
			{
				newOwner.broadcastToOnlineMembers(PlaySound.SIEGE_VICTORY);
				newOwner.incReputation(1700, false, toString());
			}
			
			broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.S1_CLAN_HAS_DEFEATED_S2).addString(newOwner.getName()).addCastleId(getResidence().getId()), ATTACKERS, DEFENDERS);
			broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.THE_SIEGE_OF_S1_IS_FINISHED).addCastleId(getResidence().getId()), ATTACKERS, DEFENDERS);
		}
		else
		{
			broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.THE_PRELIMINARY_MATCH_OF_S1_HAS_ENDED_IN_A_DRAW).addCastleId(getResidence().getId()), ATTACKERS);
		}
		
		updateParticles(false, ATTACKERS, DEFENDERS);
		removeObjects(DEFENDERS);
		removeObjects(ATTACKERS);
		super.stopEvent(step);
		_oldOwner = null;
	}
	
	/**
	 * Method loadSiegeClans.
	 */
	@Override
	public void loadSiegeClans()
	{
		List<SiegeClanObject> siegeClanObjectList = SiegeClanDAO.getInstance().load(getResidence(), ATTACKERS);
		addObjects(ATTACKERS, siegeClanObjectList);
		List<CTBSiegeClanObject> objects = getObjects(ATTACKERS);
		
		for (CTBSiegeClanObject clan : objects)
		{
			clan.select(getResidence());
		}
	}
	
	/**
	 * Method newSiegeClan.
	 * @param type String
	 * @param clanId int
	 * @param i long
	 * @param date long
	 * @return CTBSiegeClanObject
	 */
	@Override
	public CTBSiegeClanObject newSiegeClan(String type, int clanId, long i, long date)
	{
		Clan clan = ClanTable.getInstance().getClan(clanId);
		return clan == null ? null : new CTBSiegeClanObject(type, clan, i, date);
	}
	
	/**
	 * Method isParticle.
	 * @param player Player
	 * @return boolean
	 */
	@Override
	public boolean isParticle(Player player)
	{
		if (!isInProgress() || (player.getClan() == null))
		{
			return false;
		}
		
		CTBSiegeClanObject object = getSiegeClan(ATTACKERS, player.getClan());
		return (object != null) && object.getPlayers().contains(player.getObjectId());
	}
	
	/**
	 * Method getRestartLoc.
	 * @param player Player
	 * @param type RestartType
	 * @return Location
	 */
	@Override
	public Location getRestartLoc(Player player, RestartType type)
	{
		if (!checkIfInZone(player))
		{
			return null;
		}
		
		SiegeClanObject attackerClan = getSiegeClan(ATTACKERS, player.getClan());
		Location loc = null;
		
		switch (type)
		{
			case TO_VILLAGE:
				if ((attackerClan != null) && checkIfInZone(player))
				{
					List<SiegeClanObject> objectList = getObjects(ATTACKERS);
					List<Location> teleportList = getObjects(CHALLENGER_RESTART_POINTS);
					int index = objectList.indexOf(attackerClan);
					loc = teleportList.get(index);
				}
				break;
			
			default:
				break;
		}
		
		return loc;
	}
	
	/**
	 * Method action.
	 * @param name String
	 * @param start boolean
	 */
	@Override
	public void action(String name, boolean start)
	{
		if (name.equals(NEXT_STEP))
		{
			nextStep();
		}
		else
		{
			super.action(name, start);
		}
	}
	
	/**
	 * Method getUserRelation.
	 * @param thisPlayer Player
	 * @param result int
	 * @return int
	 */
	@Override
	public int getUserRelation(Player thisPlayer, int result)
	{
		return result;
	}
	
	/**
	 * Method getRelation.
	 * @param thisPlayer Player
	 * @param targetPlayer Player
	 * @param result int
	 * @return int
	 */
	@Override
	public int getRelation(Player thisPlayer, Player targetPlayer, int result)
	{
		return result;
	}
}
