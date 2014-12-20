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
package lineage2.gameserver.instancemanager;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.ReflectionUtils;

public class ArcanManager
{
	private static ArcanManager _instance;
	private static final long _taskDelay = 30 * 60 * 1000L; // 30min
	private static int _Stage = 0;
	private static final int _BLUE = 262001;
	private static final int _RED = 262003;
	
	public static ArcanManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new ArcanManager();
		}
		
		return _instance;
	}
	
	public ArcanManager()
	{
		setStage(_BLUE);
		SpawnManager.getInstance().despawn("magmeld_ritual");
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new ChangeStage(), _taskDelay, _taskDelay);
	}
	
	private class ChangeStage extends RunnableImpl
	{
		public ChangeStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			if (getStage() == _RED)
			{
				setStage(_BLUE);
				broadcastPacket(_BLUE, true, false);
				broadcastPacket(_RED, false, false);
				SpawnManager.getInstance().despawn("magmeld_ritual");
			}
			else
			{
				setStage(_RED);
				broadcastPacket(_RED, true, true);
				broadcastPacket(_BLUE, false, false);
				SpawnManager.getInstance().spawn("magmeld_ritual");
			}
		}
	}
	
	void broadcastPacket(int value, boolean b, boolean message)
	{
		final Zone zone = ReflectionUtils.getZone("[Arcan_0]");
		L2GameServerPacket trigger = new EventTrigger(value, b);
		
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(trigger);
			if (message && zone.getInsidePlayers().contains(player))
			{
				L2GameServerPacket sm = new ExShowScreenMessage(NpcString.DARK_POWER_SEEPS_OUT_FROM_THE_MIDDLE_OF_THE_TOWN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true);
				player.sendPacket(sm);
			}
		}
	}
	
	public static int getStage()
	{
		return _Stage;
	}
	
	public void setStage(int s)
	{
		_Stage = s;
	}
}