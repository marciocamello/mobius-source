package instances;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.utils.Location;
import java.util.concurrent.ScheduledFuture;

public class TautiInstance extends Reflection
{
	private ScheduledFuture<?> firstStageGuardSpawn;
	private ZoneListener _epicZoneListener;
	boolean _entryLocked;
	boolean _startLaunched;

	public TautiInstance()
	{
		this._epicZoneListener = new ZoneListener();
		this._entryLocked = false;
		this._startLaunched = false;
	}

	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[tauti_zone_entrace]").addListener(this._epicZoneListener);
	}

	@Override
	protected void onCollapse()
	{
		super.onCollapse();
		doCleanup();
	}

	private void doCleanup()
	{
		if (this.firstStageGuardSpawn != null)
			this.firstStageGuardSpawn.cancel(true);
	}

	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		public ZoneListener()
		{
		}

		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (TautiInstance.this._entryLocked)
			{
				return;
			}
			Player player = cha.getPlayer();
			if ((player == null) || (!cha.isPlayer()))
			{
				return;
			}
			ThreadPoolManager.getInstance().schedule(new TautiInstance.StartTautiInstance(), 30000L);
			TautiInstance.this._startLaunched = true;
		}

		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
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
			TautiInstance.this.addSpawnWithoutRespawn(29233, new Location(-147256, 212888, -10088), 0);
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
			for (Player player : TautiInstance.this.getPlayers())
				player.showQuestMovie(SceneMovie.sc_tauti_opening_b);
			ThreadPoolManager.getInstance().schedule(new TautiInstance.FirstStage(), 500L);
		}
	}

	private class StartTautiInstance extends RunnableImpl
	{
		StartTautiInstance()
		{
		}

		@Override
		public void runImpl()
		{
			TautiInstance.this._entryLocked = true;
			ThreadPoolManager.getInstance().schedule(new TautiInstance.PreStage(), 1000L);
		}
	}
}