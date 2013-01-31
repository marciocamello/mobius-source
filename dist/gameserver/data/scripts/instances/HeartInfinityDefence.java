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
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;
import quests._698_BlocktheLordsEscape;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HeartInfinityDefence extends Reflection
{
	/**
	 * Field DeadTumor. (value is 32535)
	 */
	private static final int DeadTumor = 32535;
	/**
	 * Field AliveTumor. (value is 18708)
	 */
	private static final int AliveTumor = 18708;
	/**
	 * Field RegenerationCoffin. (value is 18709)
	 */
	private static final int RegenerationCoffin = 18709;
	/**
	 * Field SoulWagon. (value is 22523)
	 */
	private static final int SoulWagon = 22523;
	/**
	 * Field EchmusCoffin. (value is 18713)
	 */
	private static final int EchmusCoffin = 18713;
	/**
	 * Field maxCoffins. (value is 20)
	 */
	private static final int maxCoffins = 20;
	/**
	 * Field aliveTumorSpawnTask. Field coffinSpawnTask. Field wagonSpawnTask. Field timerTask.
	 */
	private ScheduledFuture<?> timerTask = null, wagonSpawnTask = null, coffinSpawnTask = null, aliveTumorSpawnTask = null;
	/**
	 * Field conquestEnded.
	 */
	boolean conquestEnded = false;
	/**
	 * Field deathListener.
	 */
	private final DeathListener deathListener = new DeathListener();
	/**
	 * Field startTime.
	 */
	long startTime = 0;
	/**
	 * Field tumorRespawnTime.
	 */
	long tumorRespawnTime = 0;
	/**
	 * Field wagonRespawnTime.
	 */
	long wagonRespawnTime = 0;
	/**
	 * Field coffinsCreated.
	 */
	private int coffinsCreated = 0;
	/**
	 * Field preawakenedEchmus.
	 */
	private NpcInstance preawakenedEchmus = null;
	
	/**
	 * Method onCreate.
	 */
	@Override
	protected void onCreate()
	{
		super.onCreate();
		tumorRespawnTime = 3 * 60 * 1000L;
		wagonRespawnTime = 60 * 1000L;
		coffinsCreated = 0;
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				conquestBegins();
			}
		}, 20000L);
	}
	
	/**
	 * Method conquestBegins.
	 */
	void conquestBegins()
	{
		for (Player p : getPlayers())
		{
			p.sendPacket(new ExShowScreenMessage(NpcString.YOU_CAN_HEAR_THE_UNDEAD_OF_EKIMUS_RUSHING_TOWARD_YOU, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId(), "#" + NpcString.DEFEND.getId()));
		}
		spawnByGroup("soi_hoi_defence_mob_1");
		spawnByGroup("soi_hoi_defence_mob_2");
		spawnByGroup("soi_hoi_defence_mob_3");
		spawnByGroup("soi_hoi_defence_mob_4");
		spawnByGroup("soi_hoi_defence_mob_5");
		spawnByGroup("soi_hoi_defence_mob_6");
		spawnByGroup("soi_hoi_defence_tumors");
		spawnByGroup("soi_hoi_defence_wards");
		getDoor(14240102).openMe();
		preawakenedEchmus = addSpawnWithoutRespawn(29161, new Location(-179534, 208510, -15496, 16342), 0);
		coffinSpawnTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (!conquestEnded)
				{
					for (NpcInstance npc : getAllByNpcId(DeadTumor, true))
					{
						spawnCoffin(npc);
					}
				}
			}
		}, 1000L, 60000L);
		aliveTumorSpawnTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (!conquestEnded)
				{
					despawnByGroup("soi_hoi_defence_tumors");
					spawnByGroup("soi_hoi_defence_alivetumors");
					handleTumorHp(0.5);
					for (Player p : getPlayers())
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_COMPLETELY_REVIVED__, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId()));
					}
					invokeDeathListener();
				}
			}
		}, tumorRespawnTime);
		wagonSpawnTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				addSpawnWithoutRespawn(SoulWagon, new Location(-179544, 207400, -15496), 0);
			}
		}, 1000L, wagonRespawnTime);
		startTime = System.currentTimeMillis();
		timerTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new TimerTask(), 298 * 1000L, 5 * 60 * 1000L);
	}
	
	/**
	 * Method spawnCoffin.
	 * @param tumor NpcInstance
	 */
	void spawnCoffin(NpcInstance tumor)
	{
		addSpawnWithoutRespawn(RegenerationCoffin, new Location(tumor.getLoc().x, tumor.getLoc().y, tumor.getLoc().z, Location.getRandomHeading()), 250);
	}
	
	/**
	 * Method handleTumorHp.
	 * @param percent double
	 */
	void handleTumorHp(double percent)
	{
		for (NpcInstance npc : getAllByNpcId(AliveTumor, true))
		{
			npc.setCurrentHp(npc.getMaxHp() * percent, false);
		}
	}
	
	/**
	 * Method invokeDeathListener.
	 */
	void invokeDeathListener()
	{
		for (NpcInstance npc : getNpcs())
		{
			npc.addListener(deathListener);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class DeathListener implements OnDeathListener
	{
		/**
		 * Constructor for DeathListener.
		 */
		public DeathListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onDeath.
		 * @param self Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (!self.isNpc())
			{
				return;
			}
			if (self.getNpcId() == AliveTumor)
			{
				((NpcInstance) self).dropItem(killer.getPlayer(), 13797, Rnd.get(2, 5));
				final NpcInstance deadTumor = addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
				wagonRespawnTime += 10000L;
				self.deleteMe();
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_BEEN_DESTROYED_NTHE_SPEED_THAT_EKIMUS_CALLS_OUT_HIS_PREY_HAS_SLOWED_DOWN, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId()));
				}
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						deadTumor.deleteMe();
						addSpawnWithoutRespawn(AliveTumor, deadTumor.getLoc(), 0);
						wagonRespawnTime -= 10000L;
						handleTumorHp(0.25);
						invokeDeathListener();
						for (Player p : getPlayers())
						{
							p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_COMPLETELY_REVIVED_, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId()));
						}
					}
				}, tumorRespawnTime);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class TimerTask extends RunnableImpl
	{
		/**
		 * Constructor for TimerTask.
		 */
		public TimerTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			long time = ((startTime + (25 * 60 * 1000L)) - System.currentTimeMillis()) / 60000;
			if (time == 0)
			{
				conquestConclusion(true);
			}
			else
			{
				if (time == 15)
				{
					spawnByGroup("soi_hoi_defence_bosses");
				}
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcString.S1_MINUTES_ARE_REMAINING, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, String.valueOf(((startTime + (25 * 60 * 1000L)) - System.currentTimeMillis()) / 60000)));
				}
			}
		}
	}
	
	/**
	 * Method notifyWagonArrived.
	 */
	public void notifyWagonArrived()
	{
		coffinsCreated++;
		if (coffinsCreated == 20)
		{
			conquestConclusion(false);
		}
		else
		{
			Functions.npcShout(preawakenedEchmus, NpcString.BRING_MORE_MORE_SOULS);
			for (Player p : getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.THE_SOUL_COFFIN_HAS_AWAKENED_EKIMUS, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, String.valueOf(maxCoffins - coffinsCreated)));
			}
			addSpawnWithoutRespawn(EchmusCoffin, getZone("[soi_hoi_attack_echmusroom]").getTerritory().getRandomLoc(getGeoIndex()), 0);
		}
	}
	
	/**
	 * Method conquestConclusion.
	 * @param win boolean
	 */
	void conquestConclusion(boolean win)
	{
		if (conquestEnded)
		{
			return;
		}
		cancelTimers();
		conquestEnded = true;
		clearReflection(15, true);
		if (win)
		{
			setReenterTime(System.currentTimeMillis());
		}
		for (Player p : getPlayers())
		{
			if (win)
			{
				QuestState qs = p.getQuestState(_698_BlocktheLordsEscape.class);
				if ((qs != null) && (qs.getCond() == 1))
				{
					qs.set("defenceDone", 1);
				}
			}
			p.sendPacket(new ExShowScreenMessage(win ? NpcString.CONGRATULATIONS_YOU_HAVE_SUCCEEDED_AT_S1_S2_THE_INSTANCE_WILL_SHORTLY_EXPIRE : NpcString.YOU_HAVE_FAILED_AT_S1_S2, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId(), "#" + NpcString.DEFEND.getId()));
		}
	}
	
	/**
	 * Method notifyCoffinDeath.
	 */
	public void notifyCoffinDeath()
	{
		tumorRespawnTime -= 5 * 1000L;
	}
	
	/**
	 * Method cancelTimers.
	 */
	private void cancelTimers()
	{
		if (timerTask != null)
		{
			timerTask.cancel(false);
		}
		if (coffinSpawnTask != null)
		{
			coffinSpawnTask.cancel(false);
		}
		if (aliveTumorSpawnTask != null)
		{
			aliveTumorSpawnTask.cancel(false);
		}
		if (wagonSpawnTask != null)
		{
			wagonSpawnTask.cancel(false);
		}
	}
	
	/**
	 * Method onCollapse.
	 */
	@Override
	protected void onCollapse()
	{
		cancelTimers();
		super.onCollapse();
	}
}
