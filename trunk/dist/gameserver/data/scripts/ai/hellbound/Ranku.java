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
package ai.hellbound;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.AggroList.AggroInfo;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

public class Ranku extends Fighter
{
	private static final int TELEPORTATION_CUBIC_ID = 32375;
	private static final Location CUBIC_POSITION = new Location(-19056, 278732, -15000, 0);
	private static final int SCAPEGOAT_ID = 32305;
	private long _massacreTimer = 0;
	private final long _massacreDelay = 30000L;
	
	public Ranku(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		Reflection r = getActor().getReflection();
		if (r != null)
		{
			for (int i = 0; i < 4; i++)
			{
				r.addSpawnWithRespawn(SCAPEGOAT_ID, getActor().getLoc(), 300, 60);
			}
		}
	}
	
	@Override
	protected void thinkAttack()
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return;
		}
		if ((_massacreTimer + _massacreDelay) < System.currentTimeMillis())
		{
			NpcInstance victim = getScapegoat();
			_massacreTimer = System.currentTimeMillis();
			if (victim != null)
			{
				actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, victim, getMaximumHate() + 200000);
			}
		}
		super.thinkAttack();
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		if (actor.getReflection() != null)
		{
			actor.getReflection().setReenterTime(System.currentTimeMillis());
			actor.getReflection().addSpawnWithoutRespawn(TELEPORTATION_CUBIC_ID, CUBIC_POSITION, 0);
		}
		super.onEvtDead(killer);
	}
	
	private NpcInstance getScapegoat()
	{
		for (NpcInstance n : getActor().getReflection().getNpcs())
		{
			if ((n.getNpcId() == SCAPEGOAT_ID) && !n.isDead())
			{
				return n;
			}
		}
		return null;
	}
	
	private int getMaximumHate()
	{
		NpcInstance actor = getActor();
		Creature cha = actor.getAggroList().getMostHated();
		if (cha != null)
		{
			AggroInfo ai = actor.getAggroList().get(cha);
			if (ai != null)
			{
				return ai.hate;
			}
		}
		return 0;
	}
}
