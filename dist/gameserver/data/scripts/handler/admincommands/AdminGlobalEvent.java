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
package handler.admincommands;

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

public class AdminGlobalEvent extends ScriptAdminCommand
{
	enum Commands
	{
		admin_list_events
	}
	
	@Override
	public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands c = (Commands) comm;
		switch (c)
		{
			case admin_list_events:
				GameObject object = activeChar.getTarget();
				if (object == null)
				{
					activeChar.sendPacket(SystemMsg.INVALID_TARGET);
				}
				else
				{
					for (GlobalEvent e : object.getEvents())
					{
						activeChar.sendMessage("- " + e.toString());
					}
				}
				break;
		}
		return false;
	}
	
	@Override
	public Enum[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
