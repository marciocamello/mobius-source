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
package ai.teredor;

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Teredor extends Fighter
{
	static final int teredor = 19160;
	private static final int eliteMillipede = 19015;
	static final int teredorLairEgg = 19023;
	final Location[] coordsToSpawnEggs =
	{
		new Location(176360, -185096, -3826),
		new Location(175896, -185576, -3826)
	};
	static final int timeFromPassiveToActive = 10;
	private static final int delayEggTask = 90;
	boolean _teredorActive = true;
	boolean _eliteSpawned = false;
	boolean _battleActive = false;
	boolean _jumpAttacked = false;
	boolean _canUsePoison = false;
	boolean _poisonCasted = false;
	List<NpcInstance> teredorEggs;
	private final NpcInstance actor = getActor();
	private final CurrentHpListener _currentHpListener = new CurrentHpListener();
	
	/**
	 * Constructor for Teredor.
	 * @param actor NpcInstance
	 */
	public Teredor(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 7000;
		actor.addListener(_currentHpListener);
	}
	
	/**
	 * Method onEvtAggression.
	 * @param attacker Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
		super.onEvtAggression(attacker, aggro);
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		if (!_battleActive)
		{
			_battleActive = true;
			final Reflection r = actor.getReflection();
			teredorEggs = r.getAllByNpcId(teredorLairEgg, true);
			ThreadPoolManager.getInstance().scheduleAtFixedDelay(new EggSpawnTask(r), 1000, delayEggTask * 1000);
		}
		
		if (!_eliteSpawned)
		{
			final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(eliteMillipede));
			sp.setLoc(Location.findPointToStay(actor, 100, 120));
			
			for (int i = 0; i == 2; i++)
			{
				NpcInstance npc = sp.doSpawn(true);
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, actor.getAggroList().getMostHated(), Rnd.get(1, 100));
			}
			
			_eliteSpawned = true;
		}
		
		super.thinkAttack();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_battleActive = false;
		actor.removeListener(_currentHpListener);
		super.onEvtDead(killer);
	}
	
	/**
	 * Method teleportHome.
	 */
	@Override
	protected void teleportHome()
	{
	}
	
	/**
	 * @author Mobius
	 */
	private final class CurrentHpListener implements OnCurrentHpDamageListener
	{
		public CurrentHpListener()
		{
		}
		
		/**
		 * Method onCurrentHpDamage.
		 * @param actor Creature
		 * @param damage double
		 * @param attacker Creature
		 * @param skill Skill
		 * @see lineage2.gameserver.listener.actor.OnCurrentHpDamageListener#onCurrentHpDamage(Creature, double, Creature, Skill)
		 */
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if ((actor == null) || actor.isDead() || (actor.getNpcId() != teredor))
			{
				return;
			}
			
			final double newHp = actor.getCurrentHp() - damage;
			final double maxHp = actor.getMaxHp();
			
			if (_teredorActive && ((newHp == (0.8 * maxHp)) || (newHp == (0.6 * maxHp)) || (newHp == (0.4 * maxHp)) || (newHp == (0.2 * maxHp))))
			{
				_teredorActive = false;
				_eliteSpawned = false;
				ThreadPoolManager.getInstance().execute(new TeredorPassiveTask((NpcInstance) actor));
				
				if ((newHp <= (0.8 * maxHp)) && !_canUsePoison)
				{
					_canUsePoison = true;
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private final class TeredorPassiveTask extends RunnableImpl
	{
		private final NpcInstance _npc;
		
		/**
		 * Constructor for TeredorPassiveTask.
		 * @param npc NpcInstance
		 */
		TeredorPassiveTask(NpcInstance npc)
		{
			_npc = npc;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if ((_npc != null) && (!_npc.isDead()))
			{
				_npc.getAI().notifyEvent(CtrlEvent.EVT_FORGET_OBJECT);
				_npc.getAggroList().clear();
				_npc.setTargetable(false);
				_npc.setIsInvul(true);
				ThreadPoolManager.getInstance().schedule(new TeredorActiveTask(_npc), timeFromPassiveToActive * 10);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private final class TeredorActiveTask extends RunnableImpl
	{
		NpcInstance _npc;
		
		/**
		 * Constructor for TeredorActiveTask.
		 * @param npc NpcInstance
		 */
		TeredorActiveTask(NpcInstance npc)
		{
			_npc = npc;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if ((_npc != null) && (!_npc.isDead()))
			{
				_npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				_npc.setTargetable(true);
				_npc.setUnAggred(false);
				_npc.setIsInvul(false);
				_eliteSpawned = false;
				_teredorActive = true;
				_jumpAttacked = false;
				_poisonCasted = false;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private final class EggSpawnTask extends RunnableImpl
	{
		private final Reflection _r;
		
		/**
		 * Constructor for EggSpawnTask.
		 * @param r Reflection
		 */
		EggSpawnTask(Reflection r)
		{
			_r = r;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_battleActive)
			{
				final Location _coords = Location.findPointToStay(coordsToSpawnEggs[Rnd.get(1)], 50, 100);
				_r.addSpawnWithoutRespawn(teredorLairEgg, _coords, 0);
			}
		}
	}
}
