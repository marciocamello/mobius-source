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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExDivideAdenaCancel;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

/**
 * @author Erlandys
 */
public class RequestDivideAdenaCancel extends L2GameClientPacket
{
	int _cancel;
	
	@Override
	protected void readImpl()
	{
		_cancel = readC();
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		if (_cancel == 0)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ADENA_DISTRIBUTION_HAS_BEEN_CANCELLED));
			activeChar.sendPacket(new ExDivideAdenaCancel());
		}
	}
}