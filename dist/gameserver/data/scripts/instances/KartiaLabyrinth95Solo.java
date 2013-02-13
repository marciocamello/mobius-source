package instances;

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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;
import java.util.concurrent.ScheduledFuture;

public class KartiaLabyrinth95Solo extends Reflection
{
  private ScheduledFuture<?> firstStageGuardSpawn;
  DeathListener _deathListener;
  private ZoneListener _epicZoneListener;
  private ZoneListenerL _landingZoneListener;
  boolean _entryLocked;
  boolean _startLaunched;

  public KartiaLabyrinth95Solo()
  {
    this._deathListener = new DeathListener();
    this._epicZoneListener = new ZoneListener();
    this._landingZoneListener = new ZoneListenerL();
    this._entryLocked = false;
    this._startLaunched = false;
  }

  @Override
protected void onCreate()
  {
    super.onCreate();

    getZone("[kartia_zone1]").addListener(this._epicZoneListener);
    getZone("[kartia_zone2]").addListener(this._landingZoneListener);
  }

  @Override
protected void onCollapse()
  {
    super.onCollapse();
    doCleanup();
  }

  void doCleanup()
  {
    if (this.firstStageGuardSpawn != null)
      this.firstStageGuardSpawn.cancel(true);
  }

  public class ZoneListenerL
    implements OnZoneEnterLeaveListener
  {
    public ZoneListenerL()
    {
    }

    @Override
	public void onZoneEnter(Zone zone, Creature cha)
    {
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.TwoCycleStart(), 17999L);
    }

    @Override
	public void onZoneLeave(Zone zone, Creature cha)
    {
    }
  }

  public class ZoneListener
    implements OnZoneEnterLeaveListener
  {
    public ZoneListener()
    {
    }

    @Override
	public void onZoneEnter(Zone zone, Creature cha)
    {
      if (KartiaLabyrinth95Solo.this._entryLocked) {
        return;
      }
      Player player = cha.getPlayer();
      if ((player == null) || (!cha.isPlayer())) {
        return;
      }
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.StartKartiaSolo85(), 30000L);
      KartiaLabyrinth95Solo.this._startLaunched = true;
    }

    @Override
	public void onZoneLeave(Zone zone, Creature cha)
    {
    }
  }

  private class DeathListener
    implements OnDeathListener
  {
    DeathListener()
    {
    }

    @Override
	public void onDeath(Creature self, Creature killer)
    {
      if ((self.isNpc()) && (self.getNpcId() == 53000))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.FirstStage(), 17000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53001))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SecondStage(), 17000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53002))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.ThirdStage(), 17000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53003))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.ForthStage(), 17000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53004))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.FiveStage(), 17000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53005))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SixStage(), 17000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53006))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.TwoCycle(), 17000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53007))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.TwoCycleStageOne(), 25000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53008))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.TwoCycleStageTwo(), 25000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53009))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.FreeCycleStageTwo(), 25000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53010))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.FourCycleStageTwo(), 25000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53011))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.FiveCycleStageTwo(), 50000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53012))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SixCycleStageTwo(), 25000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 53013))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SevenCycleStageTwo(), 21000L);
        self.deleteMe();
      }
      else if ((self.isNpc()) && (self.getNpcId() == 19253))
      {
        ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.CloseInstance(), 9000L);
        self.deleteMe();
      }
    }
  }

  private class CloseInstance extends RunnableImpl
  {
    CloseInstance()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.startCollapseTimer(300000L);
      KartiaLabyrinth95Solo.this.doCleanup();
      for (Player p : KartiaLabyrinth95Solo.this.getPlayers())
        p.sendPacket(new SystemMessage(2106).addNumber(5));
    }
  }

  private class SevenCycleStageTwo extends RunnableImpl
  {
    SevenCycleStageTwo()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_7, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111848, -15560, -11445), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111656, -15528, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111432, -15496, -11443), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111192, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110968, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110792, -15592, -11444), 0);
      NpcInstance zellakassspawner = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19253, new Location(-111288, -15784, -11428), 0);
      zellakassspawner.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class SixCycleStageTwo extends RunnableImpl
  {
    SixCycleStageTwo()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_6, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111848, -15560, -11445), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111656, -15528, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111432, -15496, -11443), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111192, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110968, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110792, -15592, -11444), 0);
      NpcInstance sevencyclestageonemonster = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53013, new Location(-110792, -15592, -11444), 0);
      sevencyclestageonemonster.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class FiveCycleStageTwo extends RunnableImpl
  {
    FiveCycleStageTwo()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_5, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111848, -15560, -11445), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111656, -15528, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111432, -15496, -11443), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111192, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110968, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110792, -15592, -11444), 0);
      NpcInstance sixcyclestageonemonster = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53012, new Location(-110792, -15592, -11444), 0);
      sixcyclestageonemonster.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class FourCycleStageTwo extends RunnableImpl
  {
    FourCycleStageTwo()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_4, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111848, -15560, -11445), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111656, -15528, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111432, -15496, -11443), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111192, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110968, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110792, -15592, -11444), 0);
      NpcInstance fivecyclestageonemonster = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53011, new Location(-110792, -15592, -11444), 0);
      fivecyclestageonemonster.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class FreeCycleStageTwo extends RunnableImpl
  {
    FreeCycleStageTwo()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_3, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111848, -15560, -11445), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111656, -15528, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111432, -15496, -11443), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111192, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110968, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110792, -15592, -11444), 0);
      NpcInstance fourcyclestageonemonster = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53010, new Location(-110792, -15592, -11444), 0);
      fourcyclestageonemonster.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class TwoCycleStageTwo extends RunnableImpl
  {
    TwoCycleStageTwo()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_2, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.closeDoor(16170003);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111848, -15560, -11445), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111656, -15528, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111432, -15496, -11443), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111192, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110968, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110792, -15592, -11444), 0);
      NpcInstance freecyclestageonemonster = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53009, new Location(-110792, -15592, -11444), 0);
      freecyclestageonemonster.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class TwoCycleStageOne extends RunnableImpl
  {
    TwoCycleStageOne()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_1, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.openDoor(16170003);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111848, -15560, -11445), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111656, -15528, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111432, -15496, -11443), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-111192, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110968, -15512, -11444), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110792, -15592, -11444), 0);
      NpcInstance twocyclestageonemonster = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53008, new Location(-110792, -15592, -11444), 0);
      twocyclestageonemonster.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class TwoCycleStart extends RunnableImpl
  {
    TwoCycleStart()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.closeDoor(16170002);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110904, -12216, -11594), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110888, -12424, -11591), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110856, -12760, -11594), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111304, -13016, -11596), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-111288, -12744, -11596), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111336, -12424, -11596), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-111336, -12232, -11596), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111736, -12232, -11596), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-111720, -12488, -11596), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-111736, -12776, -11596), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-111720, -13032, -11596), 0);
      NpcInstance twocyclemonster = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53007, new Location(-110856, -13080, -11593), 0);
      twocyclemonster.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class TwoCycle extends RunnableImpl
  {
    TwoCycle()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.openDoor(16170002);
    }
  }

  private class ThreeSixStage extends RunnableImpl
  {
    ThreeSixStage()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110664, -10472, -11883), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10360, -11883), 0);
      NpcInstance sixstagestagemobv = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53006, new Location(-110664, -10360, -11883), 0);
      sixstagestagemobv.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class SecondSixStage extends RunnableImpl
  {
    SecondSixStage()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110664, -10472, -11883), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10360, -11883), 0);
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.ThreeSixStage(), 21000L);
    }
  }

  private class SixStage extends RunnableImpl
  {
    SixStage()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_7, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10488, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110600, -10376, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110648, -10424, -11891), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19222, new Location(-110664, -10472, -11883), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10360, -11883), 0);
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SecondSixStage(), 21000L);
    }
  }

  private class SecondFiveStage extends RunnableImpl
  {
    SecondFiveStage()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110648, -10424, -11891), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10472, -11883), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10360, -11883), 0);
      NpcInstance fivestagestagemobv = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53005, new Location(-110664, -10360, -11883), 0);
      fivestagestagemobv.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class FiveStage extends RunnableImpl
  {
    FiveStage()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_6, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10488, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110600, -10376, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110648, -10424, -11891), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10472, -11883), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10360, -11883), 0);
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SecondFiveStage(), 21000L);
    }
  }

  private class SecondForthStage extends RunnableImpl
  {
    SecondForthStage()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110648, -10424, -11891), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10472, -11883), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10360, -11883), 0);
      NpcInstance forthstagestagemobv = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53004, new Location(-110664, -10360, -11883), 0);
      forthstagestagemobv.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class ForthStage extends RunnableImpl
  {
    ForthStage()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_5, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10488, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110600, -10376, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110648, -10424, -11891), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10472, -11883), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10360, -11883), 0);
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SecondForthStage(), 21000L);
    }
  }

  private class SecondThirdStage extends RunnableImpl
  {
    SecondThirdStage()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10488, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110600, -10376, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110648, -10424, -11891), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      NpcInstance thirdstagestagemobv = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53003, new Location(-110664, -10360, -11883), 0);
      thirdstagestagemobv.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class ThirdStage extends RunnableImpl
  {
    ThirdStage()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_4, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10488, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110600, -10376, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110584, -10280, -11917), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110648, -10424, -11891), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19221, new Location(-110664, -10360, -11883), 0);
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SecondThirdStage(), 21000L);
    }
  }

  private class SecondStageSecond extends RunnableImpl
  {
    SecondStageSecond()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10360, -11883), 0);
      NpcInstance secondstagemobv = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53002, new Location(-110664, -10360, -11883), 0);
      secondstagemobv.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class SecondStage extends RunnableImpl
  {
    SecondStage()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_3, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10376, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10360, -11883), 0);
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.SecondStageSecond(), 21000L);
    }
  }

  private class FirstStage extends RunnableImpl
  {
    FirstStage()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_2, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10376, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10360, -11883), 0);
      NpcInstance firststagemobv = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53001, new Location(-110664, -10360, -11883), 0);
      firststagemobv.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class PreStage extends RunnableImpl
  {
    PreStage()
    {
    }

    @Override
	public void runImpl()
    {
      for (Player player : KartiaLabyrinth95Solo.this.getPlayers()) {
        player.sendPacket(new ExShowScreenMessage(NpcString.STAGE_1, 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true, new String[0]));
      }
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10584, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110600, -10376, -11910), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10312, -11889), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10520, -11888), 0);
      KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(19220, new Location(-110664, -10360, -11883), 0);
      NpcInstance prestagemob = KartiaLabyrinth95Solo.this.addSpawnWithoutRespawn(53000, new Location(-110664, -10360, -11883), 0);
      prestagemob.addListener(KartiaLabyrinth95Solo.this._deathListener);
    }
  }

  private class StartKartiaSolo85 extends RunnableImpl
  {
    StartKartiaSolo85()
    {
    }

    @Override
	public void runImpl()
    {
      KartiaLabyrinth95Solo.this._entryLocked = true;
      ThreadPoolManager.getInstance().schedule(new KartiaLabyrinth95Solo.PreStage(), 12000L);
    }
  }
}