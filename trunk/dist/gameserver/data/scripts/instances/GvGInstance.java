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
package instances;

import events.GvG.GvG;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerPartyLeaveListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExCubeGameAddPlayer;
import lineage2.gameserver.network.serverpackets.ExCubeGameChangePoints;
import lineage2.gameserver.network.serverpackets.ExCubeGameCloseUI;
import lineage2.gameserver.network.serverpackets.ExCubeGameEnd;
import lineage2.gameserver.network.serverpackets.ExCubeGameExtendedChangePoints;
import lineage2.gameserver.network.serverpackets.ExCubeGameRemovePlayer;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.Revive;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.mutable.MutableInt;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GvGInstance extends Reflection
{
	/**
	 * Field BOX_ID. (value is 18822)
	 */
	private final static int BOX_ID = 18822;
	/**
	 * Field BOSS_ID. (value is 25655)
	 */
	private final static int BOSS_ID = 25655;
	/**
	 * Field SCORE_BOX. (value is 20)
	 */
	private final static int SCORE_BOX = 20;
	/**
	 * Field SCORE_BOSS. (value is 100)
	 */
	private final static int SCORE_BOSS = 100;
	/**
	 * Field SCORE_KILL. (value is 5)
	 */
	private final static int SCORE_KILL = 5;
	/**
	 * Field SCORE_DEATH. (value is 3)
	 */
	private final static int SCORE_DEATH = 3;
	/**
	 * Field eventTime.
	 */
	private final int eventTime = 1200;
	/**
	 * Field bossSpawnTime.
	 */
	private final long bossSpawnTime = 10 * 60 * 1000L;
	/**
	 * Field active.
	 */
	private boolean active = false;
	/**
	 * Field team1.
	 */
	Party team1;
	/**
	 * Field team2.
	 */
	Party team2;
	/**
	 * Field bothTeams.
	 */
	private final List<HardReference<Player>> bothTeams = new CopyOnWriteArrayList<>();
	/**
	 * Field score.
	 */
	private final TIntObjectHashMap<MutableInt> score = new TIntObjectHashMap<>();
	/**
	 * Field team1Score.
	 */
	private int team1Score = 0;
	/**
	 * Field team2Score.
	 */
	private int team2Score = 0;
	/**
	 * Field startTime.
	 */
	private long startTime;
	/**
	 * Field _bossSpawnTask.
	 */
	private ScheduledFuture<?> _bossSpawnTask;
	/**
	 * Field _countDownTask.
	 */
	private ScheduledFuture<?> _countDownTask;
	/**
	 * Field _battleEndTask.
	 */
	private ScheduledFuture<?> _battleEndTask;
	/**
	 * Field _deathListener.
	 */
	private final DeathListener _deathListener = new DeathListener();
	/**
	 * Field _teleportListener.
	 */
	private final TeleportListener _teleportListener = new TeleportListener();
	/**
	 * Field _playerPartyLeaveListener.
	 */
	private final PlayerPartyLeaveListener _playerPartyLeaveListener = new PlayerPartyLeaveListener();
	/**
	 * Field zonepvp.
	 */
	Zone zonepvp;
	/**
	 * Field peace1.
	 */
	Zone peace1;
	/**
	 * Field peace2.
	 */
	Zone peace2;
	
	/**
	 * Method setTeam1.
	 * @param party1 Party
	 */
	public void setTeam1(Party party1)
	{
		team1 = party1;
	}
	
	/**
	 * Method setTeam2.
	 * @param party2 Party
	 */
	public void setTeam2(Party party2)
	{
		team2 = party2;
	}
	
	/**
	 * Constructor for GvGInstance.
	 */
	public GvGInstance()
	{
		super();
	}
	
	/**
	 * Method start.
	 */
	public void start()
	{
		zonepvp = getZone("[gvg_battle_zone]");
		peace1 = getZone("[gvg_1_peace]");
		peace2 = getZone("[gvg_2_peace]");
		Location boxes[] =
		{
			new Location(142696, 139704, -15264, 0),
			new Location(142696, 145944, -15264, 0),
			new Location(145784, 142824, -15264, 0),
			new Location(145768, 139704, -15264, 0),
			new Location(145768, 145944, -15264, 0),
			new Location(141752, 142760, -15624, 0),
			new Location(145720, 142008, -15880, 0),
			new Location(145720, 143640, -15880, 0),
			new Location(139592, 142824, -15264, 0)
		};
		for (Location boxe : boxes)
		{
			addSpawnWithoutRespawn(BOX_ID, boxe, 0);
		}
		addSpawnWithoutRespawn(35423, new Location(139640, 139736, -15264), 0);
		addSpawnWithoutRespawn(35426, new Location(139672, 145896, -15264), 0);
		_bossSpawnTask = ThreadPoolManager.getInstance().schedule(new BossSpawn(), bossSpawnTime);
		_countDownTask = ThreadPoolManager.getInstance().schedule(new CountingDown(), (eventTime - 1) * 1000L);
		_battleEndTask = ThreadPoolManager.getInstance().schedule(new BattleEnd(), (eventTime - 6) * 1000L);
		for (Player member : team1.getPartyMembers())
		{
			bothTeams.add(member.getRef());
			member.addListener(_deathListener);
			member.addListener(_teleportListener);
			member.addListener(_playerPartyLeaveListener);
		}
		for (Player member : team2.getPartyMembers())
		{
			bothTeams.add(member.getRef());
			member.addListener(_deathListener);
			member.addListener(_teleportListener);
			member.addListener(_playerPartyLeaveListener);
		}
		startTime = System.currentTimeMillis() + (eventTime * 1000L);
		final ExCubeGameChangePoints initialPoints = new ExCubeGameChangePoints(eventTime, team1Score, team2Score);
		final ExCubeGameCloseUI cui = new ExCubeGameCloseUI();
		ExCubeGameExtendedChangePoints clientSetUp;
		for (Player tm : HardReferences.unwrap(bothTeams))
		{
			score.put(tm.getObjectId(), new MutableInt());
			tm.setCurrentCp(tm.getMaxCp());
			tm.setCurrentHp(tm.getMaxHp(), false);
			tm.setCurrentMp(tm.getMaxMp());
			clientSetUp = new ExCubeGameExtendedChangePoints(eventTime, team1Score, team2Score, isRedTeam(tm), tm, 0);
			tm.sendPacket(clientSetUp);
			tm.sendActionFailed();
			tm.sendPacket(initialPoints);
			tm.sendPacket(cui);
			broadCastPacketToBothTeams(new ExCubeGameAddPlayer(tm, isRedTeam(tm)));
		}
		active = true;
	}
	
	/**
	 * Method broadCastPacketToBothTeams.
	 * @param packet L2GameServerPacket
	 */
	void broadCastPacketToBothTeams(L2GameServerPacket packet)
	{
		for (Player tm : HardReferences.unwrap(bothTeams))
		{
			tm.sendPacket(packet);
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	boolean isActive()
	{
		return active;
	}
	
	/**
	 * Method isRedTeam.
	 * @param player Player
	 * @return boolean
	 */
	private boolean isRedTeam(Player player)
	{
		if (team2.containsMember(player))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method end.
	 */
	void end()
	{
		active = false;
		startCollapseTimer(60 * 1000L);
		paralyzePlayers();
		ThreadPoolManager.getInstance().schedule(new Finish(), 55 * 1000L);
		if (_bossSpawnTask != null)
		{
			_bossSpawnTask.cancel(false);
			_bossSpawnTask = null;
		}
		if (_countDownTask != null)
		{
			_countDownTask.cancel(false);
			_countDownTask = null;
		}
		if (_battleEndTask != null)
		{
			_battleEndTask.cancel(false);
			_battleEndTask = null;
		}
		boolean isRedWinner = false;
		isRedWinner = getRedScore() >= getBlueScore();
		final ExCubeGameEnd end = new ExCubeGameEnd(isRedWinner);
		broadCastPacketToBothTeams(end);
		reward(isRedWinner ? team2 : team1);
		GvG.updateWinner(isRedWinner ? team2.getPartyLeader() : team1.getPartyLeader());
		zonepvp.setActive(false);
		peace1.setActive(false);
		peace2.setActive(false);
	}
	
	/**
	 * Method reward.
	 * @param party Party
	 */
	private void reward(Party party)
	{
		for (Player member : party.getPartyMembers())
		{
			member.sendMessage("Ва�?а группа выиграла GvG турнир, лидер группы добавлен в рейтинг победителей.");
			member.setFame(member.getFame() + 500, "GvG");
			Functions.addItem(member, 13067, 30);
		}
	}
	
	/**
	 */
	private class DeathListener implements OnDeathListener
	{
		/**
		 * Constructor for DeathListener.
		 */
		public DeathListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onDeath.
		 * @param self Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (!isActive())
			{
				return;
			}
			if ((self.getReflection() != killer.getReflection()) || (self.getReflection() != GvGInstance.this))
			{
				return;
			}
			if (self.isPlayer() && killer.isPlayable())
			{
				if (team1.containsMember(self.getPlayer()) && team2.containsMember(killer.getPlayer()))
				{
					addPlayerScore(killer.getPlayer());
					changeScore(1, SCORE_KILL, SCORE_DEATH, true, true, killer.getPlayer());
				}
				else if (team2.containsMember(self.getPlayer()) && team1.containsMember(killer.getPlayer()))
				{
					addPlayerScore(killer.getPlayer());
					changeScore(2, SCORE_KILL, SCORE_DEATH, true, true, killer.getPlayer());
				}
				resurrectAtBase(self.getPlayer());
			}
			else if (self.isPlayer() && !killer.isPlayable())
			{
				resurrectAtBase(self.getPlayer());
			}
			else if (self.isNpc() && killer.isPlayable())
			{
				if (self.getNpcId() == BOX_ID)
				{
					if (team1.containsMember(killer.getPlayer()))
					{
						changeScore(1, SCORE_BOX, 0, false, false, killer.getPlayer());
					}
					else if (team2.containsMember(killer.getPlayer()))
					{
						changeScore(2, SCORE_BOX, 0, false, false, killer.getPlayer());
					}
				}
				else if (self.getNpcId() == BOSS_ID)
				{
					if (team1.containsMember(killer.getPlayer()))
					{
						changeScore(1, SCORE_BOSS, 0, false, false, killer.getPlayer());
					}
					else if (team2.containsMember(killer.getPlayer()))
					{
						changeScore(2, SCORE_BOSS, 0, false, false, killer.getPlayer());
					}
					broadCastPacketToBothTeams(new ExShowScreenMessage("�?хранник Сокровищ Герал�?да погиб от руки " + killer.getName(), 5000, ScreenMessageAlign.MIDDLE_CENTER, true));
					end();
				}
			}
		}
	}
	
	/**
	 * Method changeScore.
	 * @param teamId int
	 * @param toAdd int
	 * @param toSub int
	 * @param subbing boolean
	 * @param affectAnotherTeam boolean
	 * @param player Player
	 */
	synchronized void changeScore(int teamId, int toAdd, int toSub, boolean subbing, boolean affectAnotherTeam, Player player)
	{
		int timeLeft = (int) ((startTime - System.currentTimeMillis()) / 1000);
		if (teamId == 1)
		{
			if (subbing)
			{
				team1Score -= toSub;
				if (team1Score < 0)
				{
					team1Score = 0;
				}
				if (affectAnotherTeam)
				{
					team2Score += toAdd;
					broadCastPacketToBothTeams(new ExCubeGameExtendedChangePoints(timeLeft, team1Score, team2Score, true, player, getPlayerScore(player)));
				}
				broadCastPacketToBothTeams(new ExCubeGameExtendedChangePoints(timeLeft, team1Score, team2Score, false, player, getPlayerScore(player)));
			}
			else
			{
				team1Score += toAdd;
				if (affectAnotherTeam)
				{
					team2Score -= toSub;
					if (team2Score < 0)
					{
						team2Score = 0;
					}
					broadCastPacketToBothTeams(new ExCubeGameExtendedChangePoints(timeLeft, team1Score, team2Score, true, player, getPlayerScore(player)));
				}
				broadCastPacketToBothTeams(new ExCubeGameExtendedChangePoints(timeLeft, team1Score, team2Score, false, player, getPlayerScore(player)));
			}
		}
		else if (teamId == 2)
		{
			if (subbing)
			{
				team2Score -= toSub;
				if (team2Score < 0)
				{
					team2Score = 0;
				}
				if (affectAnotherTeam)
				{
					team1Score += toAdd;
					broadCastPacketToBothTeams(new ExCubeGameExtendedChangePoints(timeLeft, team1Score, team2Score, false, player, getPlayerScore(player)));
				}
				broadCastPacketToBothTeams(new ExCubeGameExtendedChangePoints(timeLeft, team1Score, team2Score, true, player, getPlayerScore(player)));
			}
			else
			{
				team2Score += toAdd;
				if (affectAnotherTeam)
				{
					team1Score -= toSub;
					if (team1Score < 0)
					{
						team1Score = 0;
					}
					broadCastPacketToBothTeams(new ExCubeGameExtendedChangePoints(timeLeft, team1Score, team2Score, false, player, getPlayerScore(player)));
				}
				broadCastPacketToBothTeams(new ExCubeGameExtendedChangePoints(timeLeft, team1Score, team2Score, true, player, getPlayerScore(player)));
			}
		}
	}
	
	/**
	 * Method addPlayerScore.
	 * @param player Player
	 */
	void addPlayerScore(Player player)
	{
		MutableInt points = score.get(player.getObjectId());
		points.increment();
	}
	
	/**
	 * Method getPlayerScore.
	 * @param player Player
	 * @return int
	 */
	public int getPlayerScore(Player player)
	{
		MutableInt points = score.get(player.getObjectId());
		return points.intValue();
	}
	
	/**
	 * Method paralyzePlayers.
	 */
	public void paralyzePlayers()
	{
		for (Player tm : HardReferences.unwrap(bothTeams))
		{
			if (tm.isDead())
			{
				tm.setCurrentHp(tm.getMaxHp(), true);
				tm.broadcastPacket(new Revive(tm));
			}
			else
			{
				tm.setCurrentHp(tm.getMaxHp(), false);
			}
			tm.setCurrentMp(tm.getMaxMp());
			tm.setCurrentCp(tm.getMaxCp());
			tm.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			tm.block();
		}
	}
	
	/**
	 * Method unParalyzePlayers.
	 */
	public void unParalyzePlayers()
	{
		for (Player tm : HardReferences.unwrap(bothTeams))
		{
			tm.unblock();
			removePlayer(tm, true);
		}
	}
	
	/**
	 * Method cleanUp.
	 */
	void cleanUp()
	{
		team1 = null;
		team2 = null;
		bothTeams.clear();
		team1Score = 0;
		team2Score = 0;
		score.clear();
	}
	
	/**
	 * Method resurrectAtBase.
	 * @param player Player
	 */
	public void resurrectAtBase(Player player)
	{
		if (player.isDead())
		{
			player.setCurrentHp(0.7 * player.getMaxHp(), true);
			player.broadcastPacket(new Revive(player));
		}
		player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(5660, 2));
		Location pos;
		if (team1.containsMember(player))
		{
			pos = Location.findPointToStay(GvG.TEAM1_LOC, 0, 150, getGeoIndex());
		}
		else
		{
			pos = Location.findPointToStay(GvG.TEAM2_LOC, 0, 150, getGeoIndex());
		}
		player.teleToLocation(pos, this);
	}
	
	/**
	 * Method removePlayer.
	 * @param player Player
	 * @param legalQuit boolean
	 */
	void removePlayer(Player player, boolean legalQuit)
	{
		bothTeams.remove(player.getRef());
		broadCastPacketToBothTeams(new ExCubeGameRemovePlayer(player, isRedTeam(player)));
		player.removeListener(_deathListener);
		player.removeListener(_teleportListener);
		player.removeListener(_playerPartyLeaveListener);
		player.leaveParty();
		if (!legalQuit)
		{
			player.sendPacket(new ExCubeGameEnd(false));
		}
		player.teleToLocation(Location.findPointToStay(GvG.RETURN_LOC, 0, 150, ReflectionManager.DEFAULT.getGeoIndex()), 0);
	}
	
	/**
	 * Method teamWithdraw.
	 * @param party Party
	 */
	void teamWithdraw(Party party)
	{
		if (party == team1)
		{
			for (Player player : team1.getPartyMembers())
			{
				removePlayer(player, false);
			}
			Player player = team2.getPartyLeader();
			changeScore(2, 200, 0, false, false, player);
		}
		else
		{
			for (Player player : team2.getPartyMembers())
			{
				removePlayer(player, false);
			}
			Player player = team1.getPartyLeader();
			changeScore(1, 200, 0, false, false, player);
		}
		broadCastPacketToBothTeams(new ExShowScreenMessage("�?оманда �?оперника покинула поле бо�? в полном �?о�?таве. �?онец �?ражени�?.", 4000, ScreenMessageAlign.MIDDLE_CENTER, true));
		end();
	}
	
	/**
	 * Method getBlueScore.
	 * @return int
	 */
	private int getBlueScore()
	{
		return team1Score;
	}
	
	/**
	 * Method getRedScore.
	 * @return int
	 */
	private int getRedScore()
	{
		return team2Score;
	}
	
	/**
	 */
	public class BossSpawn extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			broadCastPacketToBothTeams(new ExShowScreenMessage("�?о�?вил�?�? �?хранник Сокровищ Герал�?да", 5000, ScreenMessageAlign.MIDDLE_CENTER, true));
			addSpawnWithoutRespawn(BOSS_ID, new Location(147304, 142824, -15864, 32768), 0);
			openDoor(24220042);
		}
	}
	
	/**
	 */
	public class CountingDown extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			broadCastPacketToBothTeams(new ExShowScreenMessage("До конца �?ражени�? о�?тала�?�? 1 минута", 4000, ScreenMessageAlign.MIDDLE_CENTER, true));
		}
	}
	
	/**
	 */
	public class BattleEnd extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			broadCastPacketToBothTeams(new ExShowScreenMessage("Врем�? битвы и�?текл��. Телепортаци�? через 1 минуту.", 4000, ScreenMessageAlign.BOTTOM_RIGHT, true));
			end();
		}
	}
	
	/**
	 */
	public class Finish extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			unParalyzePlayers();
			cleanUp();
		}
	}
	
	/**
	 * Method addSpawnWithoutRespawn.
	 * @param npcId int
	 * @param loc Location
	 * @param randomOffset int
	 * @return NpcInstance
	 */
	@Override
	public NpcInstance addSpawnWithoutRespawn(int npcId, Location loc, int randomOffset)
	{
		NpcInstance npc = super.addSpawnWithoutRespawn(npcId, loc, randomOffset);
		npc.addListener(_deathListener);
		return npc;
	}
	
	/**
	 */
	private class TeleportListener implements OnTeleportListener
	{
		/**
		 * Constructor for TeleportListener.
		 */
		public TeleportListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onTeleport.
		 * @param player Player
		 * @param x int
		 * @param y int
		 * @param z int
		 * @param reflection Reflection
		 * @see lineage2.gameserver.listener.actor.player.OnTeleportListener#onTeleport(Player, int, int, int, Reflection)
		 */
		@Override
		public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
		{
			if (zonepvp.checkIfInZone(x, y, z, reflection) || peace1.checkIfInZone(x, y, z, reflection) || peace2.checkIfInZone(x, y, z, reflection))
			{
				return;
			}
			removePlayer(player, false);
			player.sendMessage("Вы до�?рочно покинули зону битвы и были ди�?квалифицированы.");
		}
	}
	
	/**
	 */
	private class PlayerPartyLeaveListener implements OnPlayerPartyLeaveListener
	{
		/**
		 * Constructor for PlayerPartyLeaveListener.
		 */
		public PlayerPartyLeaveListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onPartyLeave.
		 * @param player Player
		 * @see lineage2.gameserver.listener.actor.player.OnPlayerPartyLeaveListener#onPartyLeave(Player)
		 */
		@Override
		public void onPartyLeave(Player player)
		{
			if (!isActive())
			{
				return;
			}
			Party party = player.getParty();
			if (party.getMemberCount() >= 3)
			{
				removePlayer(player, false);
				return;
			}
			teamWithdraw(party);
		}
	}
}
