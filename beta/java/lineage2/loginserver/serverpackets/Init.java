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

import lineage2.loginserver.L2LoginClient;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Init extends L2LoginServerPacket
{
	private final int _sessionId;
	private final byte[] _publicKey;
	private final byte[] _blowfishKey;
	private final int _protocol;
	
	/**
	 * Constructor for Init.
	 * @param client L2LoginClient
	 */
	public Init(L2LoginClient client)
	{
		this(client.getScrambledModulus(), client.getBlowfishKey(), client.getSessionId(), client.getProtocol());
	}
	
	/**
	 * Constructor for Init.
	 * @param publickey byte[]
	 * @param blowfishkey byte[]
	 * @param sessionId int
	 * @param protocol int
	 */
	public Init(byte[] publickey, byte[] blowfishkey, int sessionId, int protocol)
	{
		_sessionId = sessionId;
		_publicKey = publickey;
		_blowfishKey = blowfishkey;
		_protocol = protocol;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x00);
		writeD(_sessionId);
		writeD(_protocol); // Protocol version
		writeB(_publicKey);
		writeB(new byte[16]);
		writeB(_blowfishKey);
		writeD(0x00);
	}
}
