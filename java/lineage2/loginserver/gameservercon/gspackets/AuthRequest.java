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
package lineage2.loginserver.gameservercon.gspackets;

import lineage2.loginserver.GameServerManager;
import lineage2.loginserver.gameservercon.GameServer;
import lineage2.loginserver.gameservercon.ReceivablePacket;
import lineage2.loginserver.gameservercon.lspackets.AuthResponse;
import lineage2.loginserver.gameservercon.lspackets.LoginServerFail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthRequest extends ReceivablePacket
{
	private final static Logger _log = LoggerFactory.getLogger(AuthRequest.class);
	private int _protocolVersion;
	private int requestId;
	private boolean acceptAlternateID;
	private String externalIp;
	private String internalIp;
	private int maxOnline;
	private int _serverType;
	private int _ageLimit;
	private boolean _gmOnly;
	private boolean _brackets;
	private boolean _pvp;
	private int[] ports;
	
	@Override
	protected void readImpl()
	{
		_protocolVersion = readD();
		requestId = readC();
		acceptAlternateID = readC() == 1;
		_serverType = readD();
		_ageLimit = readD();
		_gmOnly = readC() == 1;
		_brackets = readC() == 1;
		_pvp = readC() == 1;
		externalIp = readS();
		internalIp = readS();
		ports = new int[readH()];
		for (int i = 0; i < ports.length; i++)
		{
			ports[i] = readH();
		}
		maxOnline = readD();
	}
	
	@Override
	protected void runImpl()
	{
		_log.info("Trying to register gameserver: " + requestId + " [" + getGameServer().getConnection().getIpAddress() + "]");
		int failReason = 0;
		GameServer gs = getGameServer();
		if (GameServerManager.getInstance().registerGameServer(requestId, gs))
		{
			gs.setPorts(ports);
			gs.setExternalHost(externalIp);
			gs.setInternalHost(internalIp);
			gs.setMaxPlayers(maxOnline);
			gs.setPvp(_pvp);
			gs.setServerType(_serverType);
			gs.setShowingBrackets(_brackets);
			gs.setGmOnly(_gmOnly);
			gs.setAgeLimit(_ageLimit);
			gs.setProtocol(_protocolVersion);
			gs.setAuthed(true);
			gs.getConnection().startPingTask();
		}
		else if (acceptAlternateID)
		{
			if (GameServerManager.getInstance().registerGameServer(gs = getGameServer()))
			{
				gs.setPorts(ports);
				gs.setExternalHost(externalIp);
				gs.setInternalHost(internalIp);
				gs.setMaxPlayers(maxOnline);
				gs.setPvp(_pvp);
				gs.setServerType(_serverType);
				gs.setShowingBrackets(_brackets);
				gs.setGmOnly(_gmOnly);
				gs.setAgeLimit(_ageLimit);
				gs.setProtocol(_protocolVersion);
				gs.setAuthed(true);
				gs.getConnection().startPingTask();
			}
			else
			{
				failReason = LoginServerFail.REASON_NO_FREE_ID;
			}
		}
		else
		{
			failReason = LoginServerFail.REASON_ID_RESERVED;
		}
		if (failReason != 0)
		{
			_log.info("Gameserver registration failed.");
			sendPacket(new LoginServerFail(failReason));
			return;
		}
		_log.info("Gameserver registration successful.");
		sendPacket(new AuthResponse(gs));
	}
}
