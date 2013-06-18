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
package npc.model;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Awakeninger
 */
public final class ShilenAltarInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final int DoorEnter1 = 25180001;
	@SuppressWarnings("unused")
	private static final int DoorEnter2 = 25180002;
	@SuppressWarnings("unused")
	private static final int DoorEnter3 = 25180003;
	@SuppressWarnings("unused")
	private static final int DoorEnter4 = 25180004;
	@SuppressWarnings("unused")
	private static final int DoorEnter5 = 25180005;
	@SuppressWarnings("unused")
	private static final int DoorEnter6 = 25180006;
	@SuppressWarnings("unused")
	private static final int DoorEnter7 = 25180007;
	private static final Location FLOOR2 = new Location(179336, 13608, -9852);
	private static final Location FLOOR3 = new Location(179304, 12824, -12796);
	private static final Location OFF = new Location(193353, 22608, -3616);
	@SuppressWarnings("unused")
	private long _savedTime;
	
	// DoorInstance _door1 = getReflection().getDoor(DoorEnter1);
	
	public ShilenAltarInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		if (command.startsWith("start1"))
		{
		}
		else if (command.startsWith("start2"))
		{
			
			for (Player party : GameObjectsStorage.getAllPlayersForIterate())
			{
				party.unsetVar("Altar1");
				party.setVar("Altar2", "true", -1);
				party.teleToLocation(FLOOR2, player.getReflection());
			}
		}
		else if (command.startsWith("start3"))
		{
			for (Player party : GameObjectsStorage.getAllPlayersForIterate())
			{
				party.unsetVar("Altar2");
				party.setVar("Altar3", "true", -1);
				party.teleToLocation(FLOOR3, player.getReflection());
			}
			
		}
		else if (command.startsWith("exit"))
		{
			for (Player party : GameObjectsStorage.getAllPlayersForIterate())
			{
				party.unsetVar("Altar3");
				party.teleToLocation(OFF, 0);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
		htmlMessage.setFile("default/33785.htm");
		if (player.getVar("Altar1") != null)
		{
			htmlMessage.setFile("default/33785-2.htm");
		}
		if (player.getVar("Altar2") != null)
		{
			htmlMessage.setFile("default/33785-3.htm");
		}
		if (player.getVar("Altar3") != null)
		{
			htmlMessage.setFile("default/33785-e.htm");
		}
		
		player.sendPacket(htmlMessage);
	}
}