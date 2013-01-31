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

import lineage2.gameserver.model.mail.Mail;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExChangePostState extends L2GameServerPacket
{
	/**
	 * Field _receivedBoard.
	 */
	private final boolean _receivedBoard;
	/**
	 * Field _mails.
	 */
	private final Mail[] _mails;
	/**
	 * Field _changeId.
	 */
	private final int _changeId;
	
	/**
	 * Constructor for ExChangePostState.
	 * @param receivedBoard boolean
	 * @param type int
	 * @param n Mail[]
	 */
	public ExChangePostState(boolean receivedBoard, int type, Mail... n)
	{
		_receivedBoard = receivedBoard;
		_mails = n;
		_changeId = type;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xB3);
		writeD(_receivedBoard ? 1 : 0);
		writeD(_mails.length);
		for (Mail mail : _mails)
		{
			writeD(mail.getMessageId());
			writeD(_changeId);
		}
	}
}
