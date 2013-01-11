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
package zones;

import lineage2.commons.math.random.RndSelector;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;

public class MonsterTrap implements ScriptFile
{
	private static ZoneListener _zoneListener;
	private static String[] zones =
	{
		"[hellbound_trap1]",
		"[hellbound_trap2]",
		"[hellbound_trap3]",
		"[hellbound_trap4]",
		"[hellbound_trap5]",
		"[SoD_trap_center]",
		"[SoD_trap_left]",
		"[SoD_trap_right]",
		"[SoD_trap_left_back]",
		"[SoD_trap_right_back]"
	};
	
	@Override
	public void onLoad()
	{
		_zoneListener = new ZoneListener();
		for (String s : zones)
		{
			Zone zone = ReflectionUtils.getZone(s);
			zone.addListener(_zoneListener);
		}
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	private class ZoneListener implements OnZoneEnterLeaveListener
	{
		public ZoneListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			if ((player == null) || (zone.getParams() == null))
			{
				return;
			}
			String[] params;
			int reuse = zone.getParams().getInteger("reuse");
			int despawn = zone.getParams().getInteger("despawn", 5 * 60);
			boolean attackOnSpawn = zone.getParams().getBool("attackOnSpawn", true);
			long currentMillis = System.currentTimeMillis();
			long nextReuse = zone.getParams().getLong("nextReuse", currentMillis);
			if (nextReuse > currentMillis)
			{
				return;
			}
			zone.getParams().set("nextReuse", currentMillis + (reuse * 1000L));
			String[] groups = zone.getParams().getString("monsters").split(";");
			RndSelector<int[]> rnd = new RndSelector<>();
			for (String group : groups)
			{
				params = group.split(":");
				int chance = Integer.parseInt(params[0]);
				params = params[1].split(",");
				int[] mobs = new int[params.length];
				for (int j = 0; j < params.length; j++)
				{
					mobs[j] = Integer.parseInt(params[j]);
				}
				rnd.add(mobs, chance);
			}
			int[] mobs = rnd.chance();
			for (int npcId : mobs)
			{
				try
				{
					SimpleSpawner spawn = new SimpleSpawner(npcId);
					spawn.setTerritory(zone.getTerritory());
					spawn.setAmount(1);
					spawn.setReflection(player.getReflection());
					spawn.stopRespawn();
					NpcInstance mob = spawn.doSpawn(true);
					if (mob != null)
					{
						ThreadPoolManager.getInstance().schedule(new UnSpawnTask(spawn), despawn * 1000L);
						if (mob.isAggressive() && attackOnSpawn)
						{
							mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 100);
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	public class UnSpawnTask extends RunnableImpl
	{
		private final SimpleSpawner spawn;
		
		public UnSpawnTask(SimpleSpawner spawn)
		{
			this.spawn = spawn;
		}
		
		@Override
		public void runImpl()
		{
			spawn.deleteAll();
		}
	}
}
