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
package ai;

import java.util.List;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.MinionList;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

public class Kama56Boss extends Fighter
{
	private long _nextOrderTime = 0;
	private HardReference<Player> _lastMinionsTargetRef = HardReferences.emptyRef();
	
	public Kama56Boss(NpcInstance actor)
	{
		super(actor);
	}
	
	private void sendOrderToMinions(NpcInstance actor)
	{
		if (!actor.isInCombat())
		{
			_lastMinionsTargetRef = HardReferences.emptyRef();
			return;
		}
		MinionList ml = actor.getMinionList();
		if ((ml == null) || !ml.hasMinions())
		{
			_lastMinionsTargetRef = HardReferences.emptyRef();
			return;
		}
		long now = System.currentTimeMillis();
		if ((_nextOrderTime > now) && (_lastMinionsTargetRef.get() != null))
		{
			Player old_target = _lastMinionsTargetRef.get();
			if ((old_target != null) && !old_target.isAlikeDead())
			{
				for (MinionInstance m : ml.getAliveMinions())
				{
					if (m.getAI().getAttackTarget() != old_target)
					{
						m.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, old_target, 10000000);
					}
				}
				return;
			}
		}
		_nextOrderTime = now + 30000;
		List<Player> pl = World.getAroundPlayers(actor);
		if (pl.isEmpty())
		{
			_lastMinionsTargetRef = HardReferences.emptyRef();
			return;
		}
		List<Player> alive = new LazyArrayList<>();
		for (Player p : pl)
		{
			if (!p.isAlikeDead())
			{
				alive.add(p);
			}
		}
		if (alive.isEmpty())
		{
			_lastMinionsTargetRef = HardReferences.emptyRef();
			return;
		}
		Player target = alive.get(Rnd.get(alive.size()));
		_lastMinionsTargetRef = target.getRef();
		Functions.npcSayCustomMessage(actor, "Kama56Boss.attack", target.getName());
		for (MinionInstance m : ml.getAliveMinions())
		{
			m.getAggroList().clear();
			m.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, target, 10000000);
		}
	}
	
	@Override
	protected void thinkAttack()
	{
		NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		sendOrderToMinions(actor);
		super.thinkAttack();
	}
}
