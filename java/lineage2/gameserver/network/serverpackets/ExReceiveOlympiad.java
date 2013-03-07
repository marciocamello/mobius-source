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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.olympiad.OlympiadGame;
import lineage2.gameserver.model.entity.olympiad.OlympiadManager;
import lineage2.gameserver.model.entity.olympiad.TeamMember;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class ExReceiveOlympiad extends L2GameServerPacket
{
	/**
	 * @author Mobius
	 */
	public static class MatchList extends ExReceiveOlympiad
	{
		/**
		 * Field _arenaList.
		 */
		private List<ArenaInfo> _arenaList = Collections.emptyList();
		
		/**
		 * Constructor for MatchList.
		 */
		public MatchList()
		{
			super(0);
			OlympiadManager manager = Olympiad._manager;
			if (manager != null)
			{
				_arenaList = new ArrayList<>();
				for (int i = 0; i < Olympiad.STADIUMS.length; i++)
				{
					OlympiadGame game = manager.getOlympiadInstance(i);
					if ((game != null) && (game.getState() > 0))
					{
						_arenaList.add(new ArenaInfo(i, game.getState(), game.getType().ordinal(), game.getTeamName1(), game.getTeamName2()));
					}
				}
			}
		}
		
		/**
		 * Method writeImpl.
		 */
		@Override
		protected void writeImpl()
		{
			super.writeImpl();
			writeD(_arenaList.size());
			writeD(0x00);
			for (ArenaInfo arena : _arenaList)
			{
				writeD(arena._id);
				writeD(arena._matchType);
				writeD(arena._status);
				writeS(arena._name1);
				writeS(arena._name2);
			}
		}
		
		/**
		 * @author Mobius
		 */
		private static class ArenaInfo
		{
			/**
			 * Field _id.
			 */
			final int _id;
			/**
			 * Field _status.
			 */
			final int _status;
			/**
			 * Field _matchType.
			 */
			final int _matchType;
			/**
			 * Field _name1.
			 */
			final String _name1;
			/**
			 * Field _name2.
			 */
			final String _name2;
			
			/**
			 * Constructor for ArenaInfo.
			 * @param id int
			 * @param status int
			 * @param match_type int
			 * @param name1 String
			 * @param name2 String
			 */
			public ArenaInfo(int id, int status, int match_type, String name1, String name2)
			{
				_id = id;
				_status = status;
				_matchType = match_type;
				_name1 = name1;
				_name2 = name2;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class MatchResult extends ExReceiveOlympiad
	{
		/**
		 * Field _players.
		 */
		@SuppressWarnings("unchecked")
		private final List<PlayerInfo>[] _players = new ArrayList[2];
		/**
		 * Field _tie.
		 */
		private final boolean _tie;
		/**
		 * Field _name.
		 */
		private final String _name;
		
		/**
		 * Constructor for MatchResult.
		 * @param tie boolean
		 * @param name String
		 */
		public MatchResult(boolean tie, String name)
		{
			super(1);
			_tie = tie;
			_name = name;
		}
		
		/**
		 * Method addPlayer.
		 * @param team int
		 * @param member TeamMember
		 * @param gameResultPoints int
		 */
		public void addPlayer(int team, TeamMember member, int gameResultPoints)
		{
			int points = Config.OLYMPIAD_OLDSTYLE_STAT ? 0 : member.getStat().getInteger(Olympiad.POINTS, 0);
			PlayerInfo playerInfo = new PlayerInfo(member.getName(), member.getClanName(), member.getClassId(), points, gameResultPoints, (int) member.getDamage());
			if (_players[team] == null)
			{
				_players[team] = new ArrayList<>(2);
			}
			_players[team].add(playerInfo);
		}
		
		/**
		 * Method writeImpl.
		 */
		@Override
		protected void writeImpl()
		{
			super.writeImpl();
			writeD(_tie);
			writeS(_name);
			for (int i = 0; i < _players.length; i++)
			{
				writeD(i + 1);
				List<PlayerInfo> players = _players[i] == null ? Collections.<PlayerInfo> emptyList() : _players[i];
				writeD(players.size());
				for (PlayerInfo playerInfo : players)
				{
					writeS(playerInfo._name);
					writeS(playerInfo._clanName);
					writeD(0x00);
					writeD(playerInfo._classId);
					writeD(playerInfo._damage);
					writeD(playerInfo._currentPoints);
					writeD(playerInfo._gamePoints);
				}
			}
		}
		
		/**
		 * @author Mobius
		 */
		private static class PlayerInfo
		{
			/**
			 * Field _name.
			 */
			final String _name;
			/**
			 * Field _clanName.
			 */
			final String _clanName;
			/**
			 * Field _classId.
			 */
			final int _classId;
			/**
			 * Field _currentPoints.
			 */
			final int _currentPoints;
			/**
			 * Field _gamePoints.
			 */
			final int _gamePoints;
			/**
			 * Field _damage.
			 */
			final int _damage;
			
			/**
			 * Constructor for PlayerInfo.
			 * @param name String
			 * @param clanName String
			 * @param classId int
			 * @param currentPoints int
			 * @param gamePoints int
			 * @param damage int
			 */
			public PlayerInfo(String name, String clanName, int classId, int currentPoints, int gamePoints, int damage)
			{
				_name = name;
				_clanName = clanName;
				_classId = classId;
				_currentPoints = currentPoints;
				_gamePoints = gamePoints;
				_damage = damage;
			}
		}
	}
	
	/**
	 * Field _type.
	 */
	private final int _type;
	
	/**
	 * Constructor for ExReceiveOlympiad.
	 * @param type int
	 */
	public ExReceiveOlympiad(int type)
	{
		_type = type;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xD4);
		writeD(_type);
	}
}
