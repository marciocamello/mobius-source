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
package lineage2.loginserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.interfaces.RSAPrivateKey;

import lineage2.commons.net.nio.impl.MMOClient;
import lineage2.commons.net.nio.impl.MMOConnection;
import lineage2.loginserver.accounts.Account;
import lineage2.loginserver.crypt.LoginCrypt;
import lineage2.loginserver.crypt.ScrambledKeyPair;
import lineage2.loginserver.serverpackets.AccountKicked;
import lineage2.loginserver.serverpackets.AccountKicked.AccountKickedReason;
import lineage2.loginserver.serverpackets.L2LoginServerPacket;
import lineage2.loginserver.serverpackets.LoginFail;
import lineage2.loginserver.serverpackets.LoginFail.LoginFailReason;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class L2LoginClient extends MMOClient<MMOConnection<L2LoginClient>>
{
	private final static Logger _log = LoggerFactory.getLogger(L2LoginClient.class);
	
	public static enum LoginClientState
	{
		CONNECTED,
		AUTHED_GG,
		AUTHED,
		DISCONNECTED
	}
	
	private LoginClientState _state;
	private LoginCrypt _loginCrypt;
	private ScrambledKeyPair _scrambledPair;
	private byte[] _blowfishKey;
	private String _login;
	private SessionKey _skey;
	private Account _account;
	private final String _ipAddr;
	private int _sessionId;
	
	public L2LoginClient(MMOConnection<L2LoginClient> con)
	{
		super(con);
		_state = LoginClientState.CONNECTED;
		_scrambledPair = Config.getScrambledRSAKeyPair();
		_blowfishKey = Config.getBlowfishKey();
		_loginCrypt = new LoginCrypt();
		_loginCrypt.setKey(_blowfishKey);
		_sessionId = con.hashCode();
		_ipAddr = getConnection().getSocket().getInetAddress().getHostAddress();
	}
	
	@Override
	public boolean decrypt(ByteBuffer buf, int size)
	{
		boolean ret;
		try
		{
			ret = _loginCrypt.decrypt(buf.array(), buf.position(), size);
		}
		catch (IOException e)
		{
			_log.error("", e);
			closeNow(true);
			return false;
		}
		if (!ret)
		{
			closeNow(true);
		}
		return ret;
	}
	
	@Override
	public boolean encrypt(ByteBuffer buf, int size)
	{
		final int offset = buf.position();
		try
		{
			size = _loginCrypt.encrypt(buf.array(), offset, size);
		}
		catch (IOException e)
		{
			_log.error("", e);
			return false;
		}
		buf.position(offset + size);
		return true;
	}
	
	public LoginClientState getState()
	{
		return _state;
	}
	
	public void setState(LoginClientState state)
	{
		_state = state;
	}
	
	public byte[] getBlowfishKey()
	{
		return _blowfishKey;
	}
	
	public byte[] getScrambledModulus()
	{
		return _scrambledPair.getScrambledModulus();
	}
	
	public RSAPrivateKey getRSAPrivateKey()
	{
		return (RSAPrivateKey) _scrambledPair.getKeyPair().getPrivate();
	}
	
	public String getLogin()
	{
		return _login;
	}
	
	public void setLogin(String login)
	{
		_login = login;
	}
	
	public Account getAccount()
	{
		return _account;
	}
	
	public void setAccount(Account account)
	{
		_account = account;
	}
	
	public SessionKey getSessionKey()
	{
		return _skey;
	}
	
	public void setSessionKey(SessionKey skey)
	{
		_skey = skey;
	}
	
	public void setSessionId(int val)
	{
		_sessionId = val;
	}
	
	public int getSessionId()
	{
		return _sessionId;
	}
	
	public void sendPacket(L2LoginServerPacket lsp)
	{
		if (isConnected())
		{
			getConnection().sendPacket(lsp);
		}
	}
	
	public void close(LoginFailReason reason)
	{
		if (isConnected())
		{
			getConnection().close(new LoginFail(reason));
		}
	}
	
	public void close(AccountKickedReason reason)
	{
		if (isConnected())
		{
			getConnection().close(new AccountKicked(reason));
		}
	}
	
	public void close(L2LoginServerPacket lsp)
	{
		if (isConnected())
		{
			getConnection().close(lsp);
		}
	}
	
	@Override
	public void onDisconnection()
	{
		_state = LoginClientState.DISCONNECTED;
		_skey = null;
		_loginCrypt = null;
		_scrambledPair = null;
		_blowfishKey = null;
	}
	
	@Override
	public String toString()
	{
		switch (_state)
		{
			case AUTHED:
				return "[ Account : " + getLogin() + " IP: " + getIpAddress() + "]";
			default:
				return "[ State : " + getState() + " IP: " + getIpAddress() + "]";
		}
	}
	
	public String getIpAddress()
	{
		return _ipAddr;
	}
	
	@Override
	protected void onForcedDisconnection()
	{
	}
}
