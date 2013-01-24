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
package ai;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

public class WatchmanMonster extends Fighter
{
	private long _lastSearch = 0;
	private boolean isSearching = false;
	private HardReference<? extends Creature> _attackerRef = HardReferences.emptyRef();
	static final String[] flood =
	{
		"I'll be back",
		"You are stronger than expected"
	};
	static final String[] flood2 =
	{
		"Help me!",
		"Alarm! We are under attack!"
	};
	
	public WatchmanMonster(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtAttacked(final Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if ((attacker != null) && !actor.getFaction().isNone() && (actor.getCurrentHpPercents() < 50) && (_lastSearch < (System.currentTimeMillis() - 15000)))
		{
			_lastSearch = System.currentTimeMillis();
			_attackerRef = attacker.getRef();
			if (findHelp())
			{
				return;
			}
			Functions.npcSay(actor, "Anyone, help me!");
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	private boolean findHelp()
	{
		isSearching = false;
		final NpcInstance actor = getActor();
		Creature attacker = _attackerRef.get();
		if (attacker == null)
		{
			return false;
		}
		for (final NpcInstance npc : actor.getAroundNpc(1000, 150))
		{
			if (!actor.isDead() && npc.isInFaction(actor) && !npc.isInCombat())
			{
				clearTasks();
				isSearching = true;
				addTaskMove(npc.getLoc(), true);
				Functions.npcSay(actor, flood[Rnd.get(flood.length)]);
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		_lastSearch = 0;
		_attackerRef = HardReferences.emptyRef();
		isSearching = false;
		super.onEvtDead(killer);
	}
	
	@Override
	protected void onEvtArrived()
	{
		NpcInstance actor = getActor();
		if (isSearching)
		{
			Creature attacker = _attackerRef.get();
			if (attacker != null)
			{
				Functions.npcSay(actor, flood2[Rnd.get(flood2.length)]);
				notifyFriends(attacker, 100);
			}
			isSearching = false;
			notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 100);
		}
		else
		{
			super.onEvtArrived();
		}
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		if (!isSearching)
		{
			super.onEvtAggression(target, aggro);
		}
	}
}
