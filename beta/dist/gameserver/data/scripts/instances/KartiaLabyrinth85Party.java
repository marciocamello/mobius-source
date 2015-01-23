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
package instances;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.utils.Location;

public final class KartiaLabyrinth85Party extends Reflection
{
	private ScheduledFuture<?> firstStageGuardSpawn;
	DeathListener _deathListener;
	private final ZoneListener _epicZoneListener;
	private final ZoneListenerL _landingZoneListener;
	boolean _startLaunched;
	boolean _landingentered;
	private static final int DOOR1_ID = 16170002;
	private static final int DOOR2_ID = 16170003;
	final int KartiaGuard = 19229;
	final int KartiaWatchman = 19230;
	final int DimensionalWatchman = 19231;
	final int LordOfKartia = 25882;
	
	public KartiaLabyrinth85Party()
	{
		_deathListener = new DeathListener();
		_epicZoneListener = new ZoneListener();
		_landingZoneListener = new ZoneListenerL();
		_landingentered = false;
		_startLaunched = false;
	}
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[kartia_zone1]").addListener(_epicZoneListener);
		getZone("[kartia_zone2]").addListener(_landingZoneListener);
	}
	
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
		doCleanup();
	}
	
	void doCleanup()
	{
		if (firstStageGuardSpawn != null)
		{
			firstStageGuardSpawn.cancel(true);
		}
	}
	
	class StartKartiaParty85 extends RunnableImpl
	{
		StartKartiaParty85()
		{
		}
		
		@Override
		public void runImpl()
		{
			ThreadPoolManager.getInstance().schedule(new FirstStage(), 12000);
		}
	}
	
	class FirstStage extends RunnableImpl
	{
		FirstStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_1, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SecondStage(), 60000);
		}
	}
	
	class SecondStage extends RunnableImpl
	{
		SecondStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_2, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SecondStagePart2(), 21000);
		}
	}
	
	class SecondStagePart2 extends RunnableImpl
	{
		SecondStagePart2()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ThirdStage(), 60000);
		}
	}
	
	class ThirdStage extends RunnableImpl
	{
		ThirdStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_3, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			ThreadPoolManager.getInstance().schedule(new ThirdStagePart2(), 21000);
		}
	}
	
	class ThirdStagePart2 extends RunnableImpl
	{
		ThirdStagePart2()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			ThreadPoolManager.getInstance().schedule(new ThirdStagePart3(), 21000);
		}
	}
	
	class ThirdStagePart3 extends RunnableImpl
	{
		ThirdStagePart3()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ForthStage(), 60000);
		}
	}
	
	class ForthStage extends RunnableImpl
	{
		ForthStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_4, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			ThreadPoolManager.getInstance().schedule(new ForthStagePart2(), 21000);
		}
	}
	
	class ForthStagePart2 extends RunnableImpl
	{
		ForthStagePart2()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new ForthStagePart3(), 21000);
		}
	}
	
	class ForthStagePart3 extends RunnableImpl
	{
		ForthStagePart3()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new FifthStage(), 60000);
		}
	}
	
	class FifthStage extends RunnableImpl
	{
		FifthStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_5, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new FifthStagePart2(), 21000);
		}
	}
	
	class FifthStagePart2 extends RunnableImpl
	{
		FifthStagePart2()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new FifthStagePart3(), 21000);
		}
	}
	
	class FifthStagePart3 extends RunnableImpl
	{
		FifthStagePart3()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SixthStage(), 60000);
		}
	}
	
	class SixthStage extends RunnableImpl
	{
		SixthStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_6, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SixthStagePart2(), 21000);
		}
	}
	
	class SixthStagePart2 extends RunnableImpl
	{
		SixthStagePart2()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SixthStagePart3(), 21000);
		}
	}
	
	class SixthStagePart3 extends RunnableImpl
	{
		SixthStagePart3()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SeventhStage(), 60000);
		}
	}
	
	class SeventhStage extends RunnableImpl
	{
		SeventhStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_7, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SeventhStagePart2(), 21000);
		}
	}
	
	class SeventhStagePart2 extends RunnableImpl
	{
		SeventhStagePart2()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10584, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10488, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110600, -10376, -11910), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110584, -10280, -11917), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110664, -10312, -11889), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			ThreadPoolManager.getInstance().schedule(new SeventhStagePart3(), 21000);
		}
	}
	
	class SeventhStagePart3 extends RunnableImpl
	{
		SeventhStagePart3()
		{
		}
		
		@Override
		public void runImpl()
		{
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110648, -10424, -11891), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10520, -11888), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10472, -11883), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110664, -10360, -11883), 0);
			NpcInstance sixstagestagemobv = addSpawnWithoutRespawn(DimensionalWatchman, new Location(-110664, -10360, -11883), 0);
			sixstagestagemobv.addListener(_deathListener);
		}
	}
	
	class SecondCycle extends RunnableImpl
	{
		SecondCycle()
		{
		}
		
		@Override
		public void runImpl()
		{
			openDoor(DOOR1_ID);
			ThreadPoolManager.getInstance().schedule(new SecondCycleStart(), 47999);
			
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage("Proceed to the upper right room before the door closes!", 8000, ScreenMessageAlign.TOP_CENTER, true));
			}
		}
	}
	
	class SecondCycleStart extends RunnableImpl
	{
		SecondCycleStart()
		{
		}
		
		@Override
		public void runImpl()
		{
			closeDoor(DOOR1_ID);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110904, -12216, -11594), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110888, -12424, -11591), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110856, -12760, -11594), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111304, -13016, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111288, -12744, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111336, -12424, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111336, -12232, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111736, -12232, -11596), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111720, -12488, -11596), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111736, -12776, -11596), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111736, -12776, -11596), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111736, -12776, -11596), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111736, -12776, -11596), 0);
			addSpawnWithoutRespawn(DimensionalWatchman, new Location(-110856, -13080, -11593), 0);
			ThreadPoolManager.getInstance().schedule(new SecondCycleStageOne(), 180000);
		}
	}
	
	class SecondCycleStageOne extends RunnableImpl
	{
		SecondCycleStageOne()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_1, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			for (NpcInstance n : getNpcs())
			{
				n.deleteMe();
			}
			
			openDoor(DOOR2_ID);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new SecondCycleStageTwo(), 60000);
		}
	}
	
	class SecondCycleStageTwo extends RunnableImpl
	{
		SecondCycleStageTwo()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_2, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			closeDoor(DOOR2_ID);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new SecondCycleStageThree(), 60000);
		}
	}
	
	class SecondCycleStageThree extends RunnableImpl
	{
		SecondCycleStageThree()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_3, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new SecondCycleStageFour(), 60000);
		}
	}
	
	class SecondCycleStageFour extends RunnableImpl
	{
		SecondCycleStageFour()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_4, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(DimensionalWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new SecondCycleStageFive(), 60000);
		}
	}
	
	class SecondCycleStageFive extends RunnableImpl
	{
		SecondCycleStageFive()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_5, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new SecondCycleStageSix(), 60000);
		}
	}
	
	class SecondCycleStageSix extends RunnableImpl
	{
		SecondCycleStageSix()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_6, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110792, -15592, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110792, -15592, -11444), 0);
			ThreadPoolManager.getInstance().schedule(new SecondCycleStageSeven(), 90000);
		}
	}
	
	class SecondCycleStageSeven extends RunnableImpl
	{
		SecondCycleStageSeven()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.STAGE_7, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
			}
			
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111848, -15560, -11445), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111656, -15528, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-111432, -15496, -11443), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-111192, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaWatchman, new Location(-110968, -15512, -11444), 0);
			addSpawnWithoutRespawn(KartiaGuard, new Location(-110792, -15592, -11444), 0);
			NpcInstance kaliospawner = addSpawnWithoutRespawn(LordOfKartia, new Location(-111288, -15784, -11428), 0);
			kaliospawner.addListener(_deathListener);
		}
	}
	
	class CloseInstance extends RunnableImpl
	{
		CloseInstance()
		{
		}
		
		@Override
		public void runImpl()
		{
			startCollapseTimer(300000);
			doCleanup();
			
			for (Player p : getPlayers())
			{
				p.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTE_S_YOU_WILL_BE_FORCED_OUT_OF_THE_DUNGEON_WHEN_THE_TIME_EXPIRES).addInt(5));
			}
		}
	}
	
	class DeathListener implements OnDeathListener
	{
		DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc() && (self.getId() == DimensionalWatchman))
			{
				ThreadPoolManager.getInstance().schedule(new SecondCycle(), 17000);
				self.deleteMe();
			}
			else if (self.isNpc() && (self.getId() == LordOfKartia))
			{
				ThreadPoolManager.getInstance().schedule(new CloseInstance(), 9000);
				self.deleteMe();
			}
		}
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (_startLaunched)
			{
				return;
			}
			
			if (!(((cha.getPlayer()) != null) && cha.isPlayer()))
			{
				return;
			}
			
			ThreadPoolManager.getInstance().schedule(new StartKartiaParty85(), 30000);
			_startLaunched = true;
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	public class ZoneListenerL implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (!_landingentered)
			{
				_landingentered = true;
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}