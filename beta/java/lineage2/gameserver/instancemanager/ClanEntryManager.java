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
package lineage2.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.pledge.entry.PledgeApplicantInfo;
import lineage2.gameserver.model.pledge.entry.PledgeRecruitInfo;
import lineage2.gameserver.model.pledge.entry.PledgeWaitingInfo;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClanEntryManager
{
	private static final Logger _log = LoggerFactory.getLogger(ClanEntryManager.class);
	
	private static final Map<Integer, PledgeWaitingInfo> _waitingList = new ConcurrentHashMap<>();
	private static final Map<Integer, PledgeRecruitInfo> _clanList = new ConcurrentHashMap<>();
	private static final Map<Integer, Map<Integer, PledgeApplicantInfo>> _applicantList = new ConcurrentHashMap<>();
	
	private static final Map<Integer, ScheduledFuture<?>> _clanLocked = new ConcurrentHashMap<>();
	private static final Map<Integer, ScheduledFuture<?>> _playerLocked = new ConcurrentHashMap<>();
	
	private static final String INSERT_APPLICANT = "INSERT INTO pledge_applicant VALUES (?, ?, ?, ?)";
	private static final String DELETE_APPLICANT = "DELETE FROM pledge_applicant WHERE charId = ? AND clanId = ?";
	
	private static final String INSERT_WAITING_LIST = "INSERT INTO pledge_waiting_list VALUES (?, ?)";
	private static final String DELETE_WAITING_LIST = "DELETE FROM pledge_waiting_list WHERE char_id = ?";
	
	private static final String INSERT_CLAN_RECRUIT = "INSERT INTO pledge_recruit VALUES (?, ?, ?, ?)";
	private static final String UPDATE_CLAN_RECRUIT = "UPDATE pledge_recruit SET karma = ?, information = ?, detailed_information = ? WHERE clan_id = ?";
	private static final String DELETE_CLAN_RECRUIT = "DELETE FROM pledge_recruit WHERE clan_id = ?";
	
	//@formatter:off
	private static final List<Comparator<PledgeWaitingInfo>> PLAYER_COMPARATOR = Arrays.asList(
		null,
		Comparator.comparing(PledgeWaitingInfo::getPlayerName), 
		Comparator.comparingInt(PledgeWaitingInfo::getKarma), 
		Comparator.comparingInt(PledgeWaitingInfo::getPlayerLvl), 
		Comparator.comparingInt(PledgeWaitingInfo::getPlayerClassId));
	
	private static final List<Comparator<PledgeRecruitInfo>> CLAN_COMPARATOR = Arrays.asList(
		null,
		Comparator.comparing(PledgeRecruitInfo::getClanName),
		Comparator.comparing(PledgeRecruitInfo::getClanLeaderName),
		Comparator.comparingInt(PledgeRecruitInfo::getClanLevel),
		Comparator.comparingInt(PledgeRecruitInfo::getKarma));
	//@formatter:on
	
	private static final long LOCK_TIME = TimeUnit.MINUTES.toMillis(5);
	
	protected ClanEntryManager()
	{
		load();
	}
	
	private final void load()
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM pledge_recruit"))
		{
			while (rs.next())
			{
				_clanList.put(rs.getInt("clan_id"), new PledgeRecruitInfo(rs.getInt("clan_id"), rs.getInt("karma"), rs.getString("information"), rs.getString("detailed_information")));
			}
			_log.info(getClass().getSimpleName() + ": Loaded: " + _clanList.size() + " clan entry");
		}
		catch (Exception e)
		{
			_log.warn("Exception: ClanEntryManager.load(): " + e.getMessage());
		}
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();
			Statement s = con.createStatement();
			Statement s2 = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT a.char_id, a.karma, b.default_class_id, b.level FROM pledge_waiting_list as a LEFT JOIN character_subclasses as b ON a.char_id = b.char_obj_id"))
		{
			while (rs.next())
			{
				final int charId = rs.getInt("char_id");
				String charName = "";
				
				ResultSet rs2 = s2.executeQuery("SELECT char_name FROM characters WHERE obj_Id=" + charId);
				while (rs2.next())
				{
					charName = rs.getString("char_name");
				}
				rs2.close();
				
				_waitingList.put(charId, new PledgeWaitingInfo(charId, rs.getInt("level"), rs.getInt("karma"), rs.getInt("default_class_id"), charName));
			}
			
			_log.info(getClass().getSimpleName() + ": Loaded: " + _waitingList.size() + " player in waiting list");
		}
		catch (Exception e)
		{
			_log.warn(getClass().getSimpleName() + ": Exception: ClanEntryManager.load(): " + e.getMessage());
		}
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();
			Statement s = con.createStatement();
			Statement s2 = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT a.charId, a.clanId, a.karma, a.message, b.default_class_id, b.level FROM pledge_applicant as a LEFT JOIN character_subclasses as b ON a.charId = b.char_obj_id"))
		{
			while (rs.next())
			{
				final int charId = rs.getInt("charId");
				String charName = "";
				
				ResultSet rs2 = s2.executeQuery("SELECT char_name FROM characters WHERE obj_Id=" + charId);
				while (rs2.next())
				{
					charName = rs.getString("char_name");
				}
				rs2.close();
				
				_applicantList.computeIfAbsent(rs.getInt("clanId"), k -> new ConcurrentHashMap<>()).put(charId, new PledgeApplicantInfo(charId, charName, rs.getInt("level"), rs.getInt("karma"), rs.getInt("clanId"), rs.getString("message")));
			}
			
			_log.info(getClass().getSimpleName() + ": Loaded: " + _applicantList.size() + " player application");
		}
		catch (Exception e)
		{
			_log.warn(getClass().getSimpleName() + ": Exception: ClanEntryManager.load(): " + e.getMessage());
		}
	}
	
	public Map<Integer, PledgeWaitingInfo> getWaitingList()
	{
		return _waitingList;
	}
	
	public Map<Integer, PledgeRecruitInfo> getClanList()
	{
		return _clanList;
	}
	
	public Map<Integer, Map<Integer, PledgeApplicantInfo>> getApplicantList()
	{
		return _applicantList;
	}
	
	public Map<Integer, PledgeApplicantInfo> getApplicantListForClan(int clanId)
	{
		return _applicantList.getOrDefault(clanId, Collections.emptyMap());
	}
	
	public PledgeApplicantInfo getPlayerApplication(int clanId, int playerId)
	{
		return _applicantList.getOrDefault(clanId, Collections.emptyMap()).get(playerId);
	}
	
	public boolean removePlayerApplication(int clanId, int playerId)
	{
		final Map<Integer, PledgeApplicantInfo> clanApplicantList = _applicantList.get(clanId);
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(DELETE_APPLICANT))
		{
			statement.setInt(1, playerId);
			statement.setInt(2, clanId);
			statement.executeUpdate();
		}
		catch (Exception e)
		{
			_log.error(e.getMessage(), e);
		}
		
		return (clanApplicantList != null) && (clanApplicantList.remove(playerId) != null);
	}
	
	public boolean addPlayerApplicationToClan(int clanId, PledgeApplicantInfo info)
	{
		if (!_playerLocked.containsKey(info.getPlayerId()))
		{
			_applicantList.computeIfAbsent(clanId, k -> new ConcurrentHashMap<>()).put(info.getPlayerId(), info);
			
			try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(INSERT_APPLICANT))
			{
				statement.setInt(1, info.getPlayerId());
				statement.setInt(2, info.getRequestClanId());
				statement.setInt(3, info.getKarma());
				statement.setString(4, info.getMessage());
				statement.executeUpdate();
			}
			catch (Exception e)
			{
				_log.error(e.getMessage(), e);
			}
			return true;
		}
		return false;
	}
	
	public OptionalInt getClanIdForPlayerApplication(int playerId)
	{
		return _applicantList.entrySet().stream().filter(e -> e.getValue().containsKey(playerId)).mapToInt(e -> e.getKey()).findFirst();
	}
	
	public boolean addToWaitingList(int playerId, PledgeWaitingInfo info)
	{
		if (!_playerLocked.containsKey(playerId))
		{
			try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(INSERT_WAITING_LIST))
			{
				statement.setInt(1, info.getPlayerId());
				statement.setInt(2, info.getKarma());
				statement.executeUpdate();
			}
			catch (Exception e)
			{
				_log.error(e.getMessage(), e);
			}
			
			return _waitingList.put(playerId, info) != null;
		}
		return false;
	}
	
	public boolean removeFromWaitingList(int playerId)
	{
		if (_waitingList.containsKey(playerId))
		{
			try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(DELETE_WAITING_LIST))
			{
				statement.setInt(1, playerId);
				statement.executeUpdate();
			}
			catch (Exception e)
			{
				_log.error(e.getMessage(), e);
			}
			_waitingList.remove(playerId);
			lockPlayer(playerId);
			return true;
		}
		return false;
	}
	
	public boolean addToClanList(int clanId, PledgeRecruitInfo info)
	{
		if (!_clanList.containsKey(clanId) && !_clanLocked.containsKey(clanId))
		{
			try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(INSERT_CLAN_RECRUIT))
			{
				statement.setInt(1, info.getClanId());
				statement.setInt(2, info.getKarma());
				statement.setString(3, info.getInformation());
				statement.setString(4, info.getDetailedInformation());
				statement.executeUpdate();
			}
			catch (Exception e)
			{
				_log.error(e.getMessage(), e);
			}
			return _clanList.put(clanId, info) != null;
		}
		return false;
	}
	
	public boolean updateClanList(int clanId, PledgeRecruitInfo info)
	{
		if (_clanList.containsKey(clanId) && !_clanLocked.containsKey(clanId))
		{
			try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(UPDATE_CLAN_RECRUIT))
			{
				statement.setInt(1, info.getKarma());
				statement.setString(2, info.getInformation());
				statement.setString(3, info.getDetailedInformation());
				statement.setInt(4, info.getClanId());
				statement.executeUpdate();
			}
			catch (Exception e)
			{
				_log.error(e.getMessage(), e);
			}
			return _clanList.replace(clanId, info) != null;
		}
		return false;
	}
	
	public boolean removeFromClanList(int clanId)
	{
		if (_clanList.containsKey(clanId))
		{
			try (Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(DELETE_CLAN_RECRUIT))
			{
				statement.setInt(1, clanId);
				statement.executeUpdate();
			}
			catch (Exception e)
			{
				_log.error(e.getMessage(), e);
			}
			_clanList.remove(clanId);
			lockClan(clanId);
			return true;
		}
		return false;
	}
	
	public List<PledgeWaitingInfo> getSortedWaitingList(int levelMin, int levelMax, int role, int sortBy, boolean descending)
	{
		sortBy = Util.constrain(sortBy, 1, PLAYER_COMPARATOR.size() - 1);
		
		// TODO: Handle Role
		return _waitingList.values().stream().filter(p -> ((p.getPlayerLvl() >= levelMin) && (p.getPlayerLvl() <= levelMax))).sorted(descending ? PLAYER_COMPARATOR.get(sortBy).reversed() : PLAYER_COMPARATOR.get(sortBy)).collect(Collectors.toList());
	}
	
	public List<PledgeWaitingInfo> queryWaitingListByName(String name)
	{
		return _waitingList.values().stream().filter(p -> p.getPlayerName().toLowerCase().contains(name)).collect(Collectors.toList());
	}
	
	public List<PledgeRecruitInfo> getSortedClanListByName(String query, int type)
	{
		return type == 1 ? _clanList.values().stream().filter(p -> p.getClanName().toLowerCase().contains(query)).collect(Collectors.toList()) : _clanList.values().stream().filter(p -> p.getClanLeaderName().toLowerCase().contains(query)).collect(Collectors.toList());
	}
	
	public PledgeRecruitInfo getClanById(int clanId)
	{
		return _clanList.get(clanId);
	}
	
	public boolean isClanRegistred(int clanId)
	{
		return _clanList.get(clanId) != null;
	}
	
	public boolean isPlayerRegistred(int playerId)
	{
		return _waitingList.get(playerId) != null;
	}
	
	public List<PledgeRecruitInfo> getUnSortedClanList()
	{
		return _clanList.values().stream().collect(Collectors.toList());
	}
	
	public List<PledgeRecruitInfo> getSortedClanList(int clanLevel, int karma, int sortBy, boolean descending)
	{
		sortBy = Util.constrain(sortBy, 1, CLAN_COMPARATOR.size() - 1);
		return _clanList.values().stream().filter((p -> (((clanLevel < 0) && (karma >= 0) && (karma != p.getKarma())) || ((clanLevel >= 0) && (karma < 0) && (clanLevel != p.getClanLevel())) || ((clanLevel >= 0) && (karma >= 0) && ((clanLevel != p.getClanLevel()) || (karma != p.getKarma())))))).sorted(descending ? CLAN_COMPARATOR.get(sortBy).reversed() : CLAN_COMPARATOR.get(sortBy)).collect(Collectors.toList());
	}
	
	public long getPlayerLockTime(int playerId)
	{
		return _playerLocked.get(playerId) == null ? 0 : _playerLocked.get(playerId).getDelay(TimeUnit.MINUTES);
	}
	
	public long getClanLockTime(int playerId)
	{
		return _clanLocked.get(playerId) == null ? 0 : _playerLocked.get(playerId).getDelay(TimeUnit.MINUTES);
	}
	
	private static void lockPlayer(int playerId)
	{
		_playerLocked.put(playerId, ThreadPoolManager.getInstance().schedule(() ->
		{
			_playerLocked.remove(playerId);
		}, LOCK_TIME));
	}
	
	private static void lockClan(int clanId)
	{
		_clanLocked.put(clanId, ThreadPoolManager.getInstance().schedule(() ->
		{
			_clanLocked.remove(clanId);
		}, LOCK_TIME));
	}
	
	public static ClanEntryManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final ClanEntryManager _instance = new ClanEntryManager();
	}
}