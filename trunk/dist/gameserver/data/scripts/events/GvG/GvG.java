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
package events.GvG;

import instances.GvGInstance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GvG extends Functions implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(GvG.class);
	public static final Location TEAM1_LOC = new Location(139736, 145832, -15264);
	public static final Location TEAM2_LOC = new Location(139736, 139832, -15264);
	public static final Location RETURN_LOC = new Location(43816, -48232, -822);
	public static final int[] everydayStartTime =
	{
		21,
		30,
		00
	};
	private static boolean _active = false;
	private static boolean _isRegistrationActive = false;
	private static int _minLevel = 80;
	private static int _maxLevel = 99;
	private static int _groupsLimit = 100;
	private static int _minPartyMembers = 6;
	private static long regActiveTime = 10 * 60 * 1000L;
	private static ScheduledFuture<?> _globalTask;
	private static ScheduledFuture<?> _regTask;
	private static ScheduledFuture<?> _countdownTask1;
	private static ScheduledFuture<?> _countdownTask2;
	private static ScheduledFuture<?> _countdownTask3;
	private static List<HardReference<Player>> leaderList = new CopyOnWriteArrayList<>();
	
	public static class RegTask extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			prepare();
		}
	}
	
	public static class Countdown extends RunnableImpl
	{
		int _timer;
		
		public Countdown(int timer)
		{
			_timer = timer;
		}
		
		@Override
		public void runImpl()
		{
			Announcements.getInstance().announceToAll("GvG: До конца приема заявок на турнир осталось " + Integer.toString(_timer) + " мин.");
		}
	}
	
	@Override
	public void onLoad()
	{
		_log.info("Loaded Event: GvG");
		initTimer();
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	private static void initTimer()
	{
		long day = 24 * 60 * 60 * 1000L;
		Calendar ci = Calendar.getInstance();
		ci.set(Calendar.HOUR_OF_DAY, everydayStartTime[0]);
		ci.set(Calendar.MINUTE, everydayStartTime[1]);
		ci.set(Calendar.SECOND, everydayStartTime[2]);
		long delay = ci.getTimeInMillis() - System.currentTimeMillis();
		if (delay < 0)
		{
			delay = delay + day;
		}
		if (_globalTask != null)
		{
			_globalTask.cancel(true);
		}
		_globalTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Launch(), delay, day);
	}
	
	public static class Launch extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			activateEvent();
		}
	}
	
	private static boolean canBeStarted()
	{
		for (Castle c : ResidenceHolder.getInstance().getResidenceList(Castle.class))
		{
			if ((c.getSiegeEvent() != null) && c.getSiegeEvent().isInProgress())
			{
				return false;
			}
		}
		return true;
	}
	
	private static boolean isActive()
	{
		return _active;
	}
	
	public static void activateEvent()
	{
		if (!isActive() && canBeStarted())
		{
			_regTask = ThreadPoolManager.getInstance().schedule(new RegTask(), regActiveTime);
			if (regActiveTime > (2 * 60 * 1000L))
			{
				if (regActiveTime > (5 * 60 * 1000L))
				{
					_countdownTask3 = ThreadPoolManager.getInstance().schedule(new Countdown(5), regActiveTime - (300 * 1000));
				}
				_countdownTask1 = ThreadPoolManager.getInstance().schedule(new Countdown(2), regActiveTime - (120 * 1000));
				_countdownTask2 = ThreadPoolManager.getInstance().schedule(new Countdown(1), regActiveTime - (60 * 1000));
			}
			ServerVariables.set("GvG", "on");
			_log.info("Event 'GvG' activated.");
			Announcements.getInstance().announceToAll("Регистрация на GvG турнир началась! Community Board(Alt+B) -> Эвенты -> GvG (регистрация группы, описание)");
			Announcements.getInstance().announceToAll("Заявки принимаются в течение " + (regActiveTime / 60000) + " минут");
			_active = true;
			_isRegistrationActive = true;
		}
	}
	
	public static void deactivateEvent()
	{
		if (isActive())
		{
			stopTimers();
			ServerVariables.unset("GvG");
			_log.info("Event 'GvG' canceled.");
			Announcements.getInstance().announceToAll("GvG: Турнир отменен");
			_active = false;
			_isRegistrationActive = false;
			leaderList.clear();
		}
	}
	
	public void showStats()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (!isActive())
		{
			player.sendMessage("GvG event is not launched");
			return;
		}
		StringBuilder string = new StringBuilder();
		String refresh = "<button value=\"Refresh\" action=\"bypass -h scripts_events.GvG.GvG:showStats\" width=60 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String start = "<button value=\"Start Now\" action=\"bypass -h scripts_events.GvG.GvG:startNow\" width=60 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		int i = 0;
		if (!leaderList.isEmpty())
		{
			for (Player leader : HardReferences.unwrap(leaderList))
			{
				if (!leader.isInParty())
				{
					continue;
				}
				string.append("*").append(leader.getName()).append("*").append(" | group members: ").append(leader.getParty().getMemberCount()).append("\n\n");
				i++;
			}
			show("There are " + i + " group leaders who registered for the event:\n\n" + string + "\n\n" + refresh + "\n\n" + start, player, null);
		}
		else
		{
			show("There are no participants at the time\n\n" + refresh, player, null);
		}
	}
	
	public void startNow()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (!isActive() || !canBeStarted())
		{
			player.sendMessage("GvG event is not launched");
			return;
		}
		prepare();
	}
	
	public void addGroup()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!_isRegistrationActive)
		{
			player.sendMessage("GvG турнир неактивен.");
			return;
		}
		if (leaderList.contains(player.getRef()))
		{
			player.sendMessage("Вы уже зарегистрировались на GvG турнир");
			return;
		}
		if (!player.isInParty())
		{
			player.sendMessage("Вы не состоите в группе и не можете подать заявку");
			return;
		}
		if (!player.getParty().isLeader(player))
		{
			player.sendMessage("Только лидер группы может подать заявку");
			return;
		}
		if (player.getParty().isInCommandChannel())
		{
			player.sendMessage("Чтобы участвовать в турнире вы должны покинуть Командный Канал");
			return;
		}
		if (leaderList.size() >= _groupsLimit)
		{
			player.sendMessage("Достигнут лимит количества групп для участия в турнире. Заявка отклонена");
			return;
		}
		List<Player> party = player.getParty().getPartyMembers();
		String[] abuseReason =
		{
			"не находится в игре",
			"не находится в группе",
			"состоит в неполной группе. Минимальное кол-во членов группы - 6.",
			"не является лидером группы, подававшей заявку",
			"не соответствует требованиям уровней для турнира",
			"использует ездовое животное, что противоречит требованиям турнира",
			"находится в дуэли, что противоречит требованиям турнира",
			"принимает участие в другом эвенте, что противоречит требованиям турнира",
			"находится в списке ожидания Олимпиады или принимает участие в ней",
			"находится в состоянии телепортации, что противоречит требованиям турнира",
			"находится в Dimensional Rift, что противоречит требованиям турнира",
			"обладает Проклятым Оружием, что противоречит требованиям турнира",
			"не находится в мирной зоне",
			"находится в режиме обозревания",
		};
		for (Player eachmember : party)
		{
			int abuseId = checkPlayer(eachmember, false);
			if (abuseId != 0)
			{
				player.sendMessage("Игрок " + eachmember.getName() + " " + abuseReason[abuseId - 1]);
				return;
			}
		}
		leaderList.add(player.getRef());
		player.getParty().broadcastMessageToPartyMembers("Ваша группа внесена в список ожидания. Пожалуйста, не регистрируйтесь в других ивентах и не участвуйте в дуэлях до начала турнира. Полный список требований турнира в Community Board (Alt+B)");
	}
	
	private static void stopTimers()
	{
		if (_regTask != null)
		{
			_regTask.cancel(false);
			_regTask = null;
		}
		if (_countdownTask1 != null)
		{
			_countdownTask1.cancel(false);
			_countdownTask1 = null;
		}
		if (_countdownTask2 != null)
		{
			_countdownTask2.cancel(false);
			_countdownTask2 = null;
		}
		if (_countdownTask3 != null)
		{
			_countdownTask3.cancel(false);
			_countdownTask3 = null;
		}
	}
	
	static void prepare()
	{
		checkPlayers();
		shuffleGroups();
		if (isActive())
		{
			stopTimers();
			ServerVariables.unset("GvG");
			_active = false;
			_isRegistrationActive = false;
		}
		if (leaderList.size() < 2)
		{
			leaderList.clear();
			Announcements.getInstance().announceToAll("GvG: Турнир отменен из-за недостатка участников");
			return;
		}
		Announcements.getInstance().announceToAll("GvG: Прием заявок завершен. Запуск турнира.");
		start();
	}
	
	private static int checkPlayer(Player player, boolean doCheckLeadership)
	{
		if (!player.isOnline())
		{
			return 1;
		}
		if (!player.isInParty())
		{
			return 2;
		}
		if (doCheckLeadership && ((player.getParty() == null) || !player.getParty().isLeader(player)))
		{
			return 4;
		}
		if ((player.getParty() == null) || (player.getParty().getMemberCount() < _minPartyMembers))
		{
			return 3;
		}
		if ((player.getLevel() < _minLevel) || (player.getLevel() > _maxLevel))
		{
			return 5;
		}
		if (player.isMounted())
		{
			return 6;
		}
		if (player.isInDuel())
		{
			return 7;
		}
		if (player.getTeam() != TeamType.NONE)
		{
			return 8;
		}
		if ((player.getOlympiadGame() != null) || Olympiad.isRegistered(player))
		{
			return 9;
		}
		if (player.isTeleporting())
		{
			return 10;
		}
		if (player.isCursedWeaponEquipped())
		{
			return 11;
		}
		if (!player.isInPeaceZone())
		{
			return 12;
		}
		if (player.isInObserverMode())
		{
			return 13;
		}
		return 0;
	}
	
	private static void shuffleGroups()
	{
		if ((leaderList.size() % 2) != 0)
		{
			int rndindex = Rnd.get(leaderList.size());
			Player expelled = leaderList.remove(rndindex).get();
			if (expelled != null)
			{
				expelled.sendMessage("При формировании списка участников турнира ваша группа была отсеяна. Приносим извинения, попробуйте в следующий раз.");
			}
		}
		for (int i = 0; i < leaderList.size(); i++)
		{
			int rndindex = Rnd.get(leaderList.size());
			leaderList.set(i, leaderList.set(rndindex, leaderList.get(i)));
		}
	}
	
	private static void checkPlayers()
	{
		for (Player player : HardReferences.unwrap(leaderList))
		{
			if (checkPlayer(player, true) != 0)
			{
				leaderList.remove(player.getRef());
				continue;
			}
			for (Player partymember : player.getParty().getPartyMembers())
			{
				if (checkPlayer(partymember, false) != 0)
				{
					player.sendMessage("Ваша группа была дисквалифицирована и снята с участия в турнире так как один или более членов группы нарушил условия участия");
					leaderList.remove(player.getRef());
					break;
				}
			}
		}
	}
	
	public static void updateWinner(Player winner)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO event_data(charId, score) VALUES (?,1) ON DUPLICATE KEY UPDATE score=score+1");
			statement.setInt(1, winner.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	private static void start()
	{
		int instancedZoneId = 504;
		InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(instancedZoneId);
		if (iz == null)
		{
			_log.warn("GvG: InstanceZone : " + instancedZoneId + " not found!");
			return;
		}
		for (int i = 0; i < leaderList.size(); i += 2)
		{
			Player team1Leader = leaderList.get(i).get();
			Player team2Leader = leaderList.get(i + 1).get();
			GvGInstance r = new GvGInstance();
			r.setTeam1(team1Leader.getParty());
			r.setTeam2(team2Leader.getParty());
			r.init(iz);
			r.setReturnLoc(GvG.RETURN_LOC);
			for (Player member : team1Leader.getParty().getPartyMembers())
			{
				Functions.unRide(member);
				Functions.unSummonPet(member, true);
				member.setTransformation(0);
				member.setInstanceReuse(instancedZoneId, System.currentTimeMillis());
				member.dispelBuffs();
				member.teleToLocation(Location.findPointToStay(GvG.TEAM1_LOC, 0, 150, r.getGeoIndex()), r);
			}
			for (Player member : team2Leader.getParty().getPartyMembers())
			{
				Functions.unRide(member);
				Functions.unSummonPet(member, true);
				member.setTransformation(0);
				member.setInstanceReuse(instancedZoneId, System.currentTimeMillis());
				member.dispelBuffs();
				member.teleToLocation(Location.findPointToStay(GvG.TEAM2_LOC, 0, 150, r.getGeoIndex()), r);
			}
			r.start();
		}
		leaderList.clear();
		_log.info("GvG: Event started successfuly.");
	}
}
