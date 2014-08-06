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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Awakeninger
 */
public final class AltarGatekeeperInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int DoorEnter1 = 25180001;
	private static final int DoorEnter2 = 25180002;
	private static final int DoorEnter3 = 25180003;
	private static final int DoorEnter4 = 25180004;
	private static final int DoorEnter5 = 25180005;
	private static final int DoorEnter6 = 25180006;
	private static final int DoorEnter7 = 25180007;
	private long _savedTime;
	final DoorInstance _door1 = ReflectionUtils.getDoor(DoorEnter1);
	final DoorInstance _door2 = ReflectionUtils.getDoor(DoorEnter2);
	final DoorInstance _door3 = ReflectionUtils.getDoor(DoorEnter3);
	final DoorInstance _door4 = ReflectionUtils.getDoor(DoorEnter4);
	final DoorInstance _door5 = ReflectionUtils.getDoor(DoorEnter5);
	final DoorInstance _door6 = ReflectionUtils.getDoor(DoorEnter6);
	final DoorInstance _door7 = ReflectionUtils.getDoor(DoorEnter7);
	
	public AltarGatekeeperInstance(int objectId, NpcTemplate template)
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
		
		if (command.startsWith("start"))
		{
			_savedTime = System.currentTimeMillis();
			player.sendPacket(new ExSendUIEvent(player, 0, 1, (int) (System.currentTimeMillis() - _savedTime) / 1000, 0, NpcString.ELAPSED_TIME));
			_door1.openMe();
			_door2.openMe();
			_door3.openMe();
			_door4.openMe();
			_door5.openMe();
			_door6.openMe();
			_door7.openMe();
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}