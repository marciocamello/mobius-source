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
package lineage2.loginserver.gameservercon;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lineage2.loginserver.Config;

import org.apache.log4j.Logger;

public class GameServer
{
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(GameServer.class);
	private int _id;
	private String _internalHost, _externalHost;
	private int[] _ports = new int[]
	{
		7777
	};
	private int _serverType;
	private int _ageLimit;
	private int _protocol;
	private boolean _isOnline;
	private boolean _isPvp;
	private boolean _isShowingBrackets;
	private boolean _isGmOnly;
	private int _maxPlayers;
	private GameServerConnection _conn;
	private boolean _isAuthed;
	private int _port;
	private final Set<String> _accounts = new CopyOnWriteArraySet<>();
	
	public GameServer(GameServerConnection conn)
	{
		_conn = conn;
	}
	
	public GameServer(int id)
	{
		_id = id;
	}
	
	public void setId(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public void setAuthed(boolean isAuthed)
	{
		_isAuthed = isAuthed;
	}
	
	public boolean isAuthed()
	{
		return _isAuthed;
	}
	
	public void setConnection(GameServerConnection conn)
	{
		_conn = conn;
	}
	
	public GameServerConnection getConnection()
	{
		return _conn;
	}
	
	public InetAddress getInternalHost() throws UnknownHostException
	{
		return InetAddress.getByName(_internalHost);
	}
	
	public void setInternalHost(String internalHost)
	{
		if (internalHost.equals("*"))
		{
			internalHost = getConnection().getIpAddress();
		}
		_internalHost = internalHost;
	}
	
	public void setExternalHost(String externalHost)
	{
		if (externalHost.equals("*"))
		{
			externalHost = getConnection().getIpAddress();
		}
		_externalHost = externalHost;
	}
	
	public InetAddress getExternalHost() throws UnknownHostException
	{
		return InetAddress.getByName(_externalHost);
	}
	
	public int getPort()
	{
		return _ports[_port++ & (_ports.length - 1)];
	}
	
	public void setPorts(int[] ports)
	{
		_ports = ports;
	}
	
	public void setMaxPlayers(int maxPlayers)
	{
		_maxPlayers = maxPlayers;
	}
	
	public int getMaxPlayers()
	{
		return _maxPlayers;
	}
	
	public int getOnline()
	{
		return _accounts.size();
	}
	
	public Set<String> getAccounts()
	{
		return _accounts;
	}
	
	public void addAccount(String account)
	{
		_accounts.add(account);
	}
	
	public void removeAccount(String account)
	{
		_accounts.remove(account);
	}
	
	public void setDown()
	{
		setAuthed(false);
		setConnection(null);
		setOnline(false);
		_accounts.clear();
	}
	
	public String getName()
	{
		return Config.SERVER_NAMES.get(getId());
	}
	
	public void sendPacket(SendablePacket packet)
	{
		GameServerConnection conn = getConnection();
		if (conn != null)
		{
			conn.sendPacket(packet);
		}
	}
	
	public int getServerType()
	{
		return _serverType;
	}
	
	public boolean isOnline()
	{
		return _isOnline;
	}
	
	public void setOnline(boolean online)
	{
		_isOnline = online;
	}
	
	public void setServerType(int serverType)
	{
		_serverType = serverType;
	}
	
	public boolean isPvp()
	{
		return _isPvp;
	}
	
	public void setPvp(boolean pvp)
	{
		_isPvp = pvp;
	}
	
	public boolean isShowingBrackets()
	{
		return _isShowingBrackets;
	}
	
	public void setShowingBrackets(boolean showingBrackets)
	{
		_isShowingBrackets = showingBrackets;
	}
	
	public boolean isGmOnly()
	{
		return _isGmOnly;
	}
	
	public void setGmOnly(boolean gmOnly)
	{
		_isGmOnly = gmOnly;
	}
	
	public int getAgeLimit()
	{
		return _ageLimit;
	}
	
	public void setAgeLimit(int ageLimit)
	{
		_ageLimit = ageLimit;
	}
	
	public int getProtocol()
	{
		return _protocol;
	}
	
	public void setProtocol(int protocol)
	{
		_protocol = protocol;
	}
}
