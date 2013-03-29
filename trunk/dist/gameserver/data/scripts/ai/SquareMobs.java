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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.templates.npc.MinionData;

/**@Author Awakeninger
 * Random spawn summon security.
 * Protection proportional to the number of guards.
  	private static final int Mob1 = 23012;
	private static final int Mob2 = 23010;
	private static final int Mob3 = 23011;
 */
public class SquareMobs extends Mystic
{
	private static final int[] Servitors =
	{
		23012,
		23011,
		23010
	};
	
	int _lastMinionCount = 12;
	
	private class FuncMulMinionCount extends Func
	{
		public FuncMulMinionCount(Stats stat, int order, Object owner)
		{
			super(stat, order, owner);
		}
		
		@Override
		public void calc(Env env)
		{
			env.value *= _lastMinionCount;
		}
	}
	
	public SquareMobs(NpcInstance actor)
	{
		super(actor);
		
		actor.addStatFunc(new FuncMulMinionCount(Stats.MAGIC_DEFENCE, 0x30, actor));
		actor.addStatFunc(new FuncMulMinionCount(Stats.POWER_DEFENCE, 0x30, actor));
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		
		NpcInstance actor = getActor();
		actor.getMinionList().addMinion(new MinionData(Servitors[Rnd.get(Servitors.length)], Rnd.get(2)));
		_lastMinionCount = Math.max(actor.getMinionList().getAliveMinions().size(), 1);
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		MonsterInstance actor = (MonsterInstance) getActor();
		if (actor.isDead())
		{
			return;
		}
		_lastMinionCount = Math.max(actor.getMinionList().getAliveMinions().size(), 1);
		super.onEvtAttacked(attacker, damage);
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		actor.getMinionList().deleteMinions();
		super.onEvtDead(killer);
	}
}