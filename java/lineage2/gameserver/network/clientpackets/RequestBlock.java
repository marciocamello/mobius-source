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
package lineage2.gameserver.network.clientpackets;

import java.util.Collection;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBlock extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestBlock.class);
	private final static int BLOCK = 0;
	private final static int UNBLOCK = 1;
	private final static int BLOCKLIST = 2;
	private final static int ALLBLOCK = 3;
	private final static int ALLUNBLOCK = 4;
	private Integer _type;
	private String targetName = null;
	
	@Override
	protected void readImpl()
	{
		_type = readD();
		if ((_type == BLOCK) || (_type == UNBLOCK))
		{
			targetName = readS(16);
		}
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		switch (_type)
		{
			case BLOCK:
				activeChar.addToBlockList(targetName);
				break;
			case UNBLOCK:
				activeChar.removeFromBlockList(targetName);
				break;
			case BLOCKLIST:
				Collection<String> blockList = activeChar.getBlockList();
				if (blockList != null)
				{
					activeChar.sendPacket(Msg._IGNORE_LIST_);
					for (String name : blockList)
					{
						activeChar.sendMessage(name);
					}
					activeChar.sendPacket(Msg.__EQUALS__);
				}
				break;
			case ALLBLOCK:
				activeChar.setBlockAll(true);
				activeChar.sendPacket(Msg.YOU_ARE_NOW_BLOCKING_EVERYTHING);
				activeChar.sendEtcStatusUpdate();
				break;
			case ALLUNBLOCK:
				activeChar.setBlockAll(false);
				activeChar.sendPacket(Msg.YOU_ARE_NO_LONGER_BLOCKING_EVERYTHING);
				activeChar.sendEtcStatusUpdate();
				break;
			default:
				_log.info("Unknown 0x0a block type: " + _type);
		}
	}
}
