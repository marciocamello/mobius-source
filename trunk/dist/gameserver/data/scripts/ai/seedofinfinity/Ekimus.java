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
package ai.seedofinfinity;

import instances.HeartInfinityAttack;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

public class Ekimus extends Mystic
{
	private long delayTimer = 0;
	
	public Ekimus(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected boolean randomAnimation()
	{
		return false;
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		for (NpcInstance npc : actor.getReflection().getAllByNpcId(29151, true))
		{
			npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, damage);
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	@Override
	protected void thinkAttack()
	{
		if ((delayTimer + 5000) < System.currentTimeMillis())
		{
			delayTimer = System.currentTimeMillis();
			if (getActor().getReflection().getInstancedZoneId() == 121)
			{
				((HeartInfinityAttack) getActor().getReflection()).notifyEkimusAttack();
			}
		}
		super.thinkAttack();
	}
	
	@Override
	protected boolean thinkActive()
	{
		if ((delayTimer + 5000) < System.currentTimeMillis())
		{
			delayTimer = System.currentTimeMillis();
			if (getActor().getReflection().getInstancedZoneId() == 121)
			{
				((HeartInfinityAttack) getActor().getReflection()).notifyEkimusIdle();
			}
		}
		return super.thinkActive();
	}
}
