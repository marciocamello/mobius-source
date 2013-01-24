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

public class Teredor extends Fighter
{
	static int teredor = 19160;
	private static int eliteMillipede = 19015;
	static int teredorLairEgg = 19023;
	final Location[] coordsToSpawnEggs =
	{
		new Location(176360, -185096, -3826),
		new Location(175896, -185576, -3826)
	};
	static int timeFromPassiveToActive = 10;
	private static int delayEggTask = 90;
	boolean _teredorActive = true;
	boolean _eliteSpawned = false;
	boolean _battleActive = false;
	boolean _jumpAttacked = false;
	boolean _canUsePoison = false;
	boolean _poisonCasted = false;
	List<NpcInstance> teredorEggs;
	private final NpcInstance actor = getActor();
	private final CurrentHpListener _currentHpListener = new CurrentHpListener();
	
	public Teredor(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 7000;
		actor.addListener(_currentHpListener);
	}
	
	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
		super.onEvtAggression(attacker, aggro);
	}
	
	@Override
	protected void thinkAttack()
	{
		if (!_battleActive)
		{
			_battleActive = true;
			Reflection r = actor.getReflection();
			teredorEggs = r.getAllByNpcId(teredorLairEgg, true);
			ThreadPoolManager.getInstance().scheduleAtFixedDelay(new EggSpawnTask(r), 1000, delayEggTask * 1000);
		}
		if (!_eliteSpawned)
		{
			SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(eliteMillipede));
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
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		_battleActive = false;
		actor.removeListener(_currentHpListener);
		super.onEvtDead(killer);
	}
	
	@Override
	protected void teleportHome()
	{
		return;
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if ((actor == null) || actor.isDead() || (actor.getNpcId() != teredor))
			{
				return;
			}
			double newHp = actor.getCurrentHp() - damage;
			double maxHp = actor.getMaxHp();
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
	
	public class TeredorPassiveTask extends RunnableImpl
	{
		NpcInstance _npc;
		
		public TeredorPassiveTask(NpcInstance npc)
		{
			_npc = npc;
		}
		
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
	
	public class TeredorActiveTask extends RunnableImpl
	{
		NpcInstance _npc;
		
		public TeredorActiveTask(NpcInstance npc)
		{
			_npc = npc;
		}
		
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
	
	public class EggSpawnTask extends RunnableImpl
	{
		Reflection _r;
		
		public EggSpawnTask(Reflection r)
		{
			_r = r;
		}
		
		@Override
		public void runImpl()
		{
			if (_battleActive)
			{
				Location _coords = Location.findPointToStay(coordsToSpawnEggs[Rnd.get(1)], 50, 100);
				_r.addSpawnWithoutRespawn(teredorLairEgg, _coords, 0);
			}
		}
	}
}
