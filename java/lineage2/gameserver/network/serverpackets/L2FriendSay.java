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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class L2FriendSay extends L2GameServerPacket
{
	/**
	 * Field _message. Field _receiver. Field _sender.
	 */
	private final String _sender, _receiver, _message;
	
	/**
	 * Constructor for L2FriendSay.
	 * @param sender String
	 * @param reciever String
	 * @param message String
	 */
	public L2FriendSay(String sender, String reciever, String message)
	{
		_sender = sender;
		_receiver = reciever;
		_message = message;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x78);
		writeD(0);
		writeS(_receiver);
		writeS(_sender);
		writeS(_message);
	}
}
