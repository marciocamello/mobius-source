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
package lineage2.loginserver.serverpackets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.net.utils.NetUtils;
import lineage2.loginserver.GameServerManager;
import lineage2.loginserver.accounts.Account;
import lineage2.loginserver.gameservercon.GameServer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ServerList extends L2LoginServerPacket
{
	private final List<ServerData> _servers = new ArrayList<>();
	private final int _lastServer;
	private int _paddedBytes;
	
	private static class ServerData
	{
		int _serverId;
		InetAddress _ip;
		int _port;
		int _onlinePlayers;
		int _maxPlayers;
		boolean _status;
		boolean _pvp;
		boolean _brackets;
		int _serverType;
		int _ageLimit;
		int _characterCount;
		int[] _deleteChars;
		
		/**
		 * Constructor for ServerData.
		 * @param serverId int
		 * @param ip InetAddress
		 * @param port int
		 * @param pvp boolean
		 * @param brackets boolean
		 * @param serverType int
		 * @param onlinePlayers int
		 * @param maxPlayers int
		 * @param status boolean
		 * @param characterCount int
		 * @param ageLimit int
		 * @param deleteChars int[]
		 */
		ServerData(int serverId, InetAddress ip, int port, boolean pvp, boolean brackets, int serverType, int onlinePlayers, int maxPlayers, boolean status, int characterCount, int ageLimit, int[] deleteChars)
		{
			_serverId = serverId;
			_ip = ip;
			_port = port;
			_pvp = pvp;
			_brackets = brackets;
			_serverType = serverType;
			_onlinePlayers = onlinePlayers;
			_maxPlayers = maxPlayers;
			_status = status;
			_characterCount = characterCount;
			_ageLimit = ageLimit;
			_deleteChars = deleteChars;
		}
	}
	
	/**
	 * Constructor for ServerList.
	 * @param account Account
	 */
	public ServerList(Account account)
	{
		_lastServer = account.getLastServer();
		_paddedBytes = 1;
		for (GameServer gs : GameServerManager.getInstance().getGameServers())
		{
			InetAddress ip;
			
			try
			{
				ip = NetUtils.isInternalIP(account.getLastIP()) ? gs.getInternalHost() : gs.getExternalHost();
			}
			catch (UnknownHostException e)
			{
				continue;
			}
			
			Pair<Integer, int[]> entry = account.getAccountInfo(gs.getId());
			_paddedBytes += (3 + (4 * (entry == null ? 0 : entry.getValue().length)));
			_servers.add(new ServerData(gs.getId(), ip, gs.getPort(), gs.isPvp(), gs.isShowingBrackets(), gs.getServerType(), gs.getOnline(), gs.getMaxPlayers(), gs.isOnline(), entry == null ? 0 : entry.getKey(), gs.getAgeLimit(), entry == null ? ArrayUtils.EMPTY_INT_ARRAY : entry.getValue()));
		}
	}
	
	@Override
	protected void writeImpl()
	{
		int charsOnServers = 0;
		
		writeC(0x04);
		writeC(_servers.size());
		writeC(_lastServer);
		
		for (ServerData server : _servers)
		{
			writeC(server._serverId);
			
			final InetAddress i4 = server._ip;
			final byte[] raw = i4.getAddress();
			writeC(raw[0] & 0xff);
			writeC(raw[1] & 0xff);
			writeC(raw[2] & 0xff);
			writeC(raw[3] & 0xff);
			
			writeD(server._port);
			writeC(server._ageLimit); // Age Limit 0, 15, 18
			writeC(server._pvp ? 0x01 : 0x00);
			writeH(server._onlinePlayers);
			writeH(server._maxPlayers);
			writeC(server._status ? 0x01 : 0x00);
			writeD(server._serverType); // 1: Normal, 2: Relax, 4: Public Test, 8: No Label, 16: Character Creation Restricted, 32: Event, 64: Free
			writeC(server._brackets ? 0x01 : 0x00);
			
			charsOnServers += server._characterCount;
		}
		
		writeH(_paddedBytes);
		if (charsOnServers > 0)
		{
			writeC(charsOnServers);
			for (ServerData server : _servers)
			{
				writeC(server._serverId);
				writeC(server._characterCount);
				if (server._deleteChars.length == 0)
				{
					writeC(0x00);
				}
				else
				{
					writeC(server._deleteChars.length);
					for (int deleteTime : server._deleteChars)
					{
						writeD((int) (deleteTime - (System.currentTimeMillis() / 1000L)));
					}
				}
			}
		}
		else
		{
			writeC(0x00);
		}
	}
}
