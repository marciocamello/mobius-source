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
package lineage2.gameserver.network.serverpackets;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;

public class EchoAnswer extends L2GameServerPacket
{
	private final byte[] _challenge;
	
	public EchoAnswer(byte[] challenge)
	{
		_challenge = challenge;
	}
	
	@Override
	protected void writeImpl()
	{
		byte b[] = new byte[_challenge.length];
		byte[] hash = String.format("%x%x", Config.EXTERNAL_HOSTNAME.hashCode(), Config.USER_NAME.hashCode()).getBytes();
		if (_challenge.length != hash.length)
		{
			Rnd.nextBytes(b);
		}
		else
		{
			for (int i = 0; i < _challenge.length; i++)
			{
				b[i] = (byte) (hash[i] ^ _challenge[i]);
			}
		}
		writeB(b);
	}
}
