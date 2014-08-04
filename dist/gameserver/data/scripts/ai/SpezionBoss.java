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

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author cruel
 */
public class SpezionBoss extends Fighter
{
	private ScheduledFuture<?> DeadTask;
	
	public SpezionBoss(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		NpcInstance actor = getActor();
		DeadTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnMinion(), 1000, 30000);
		Reflection r = actor.getReflection();
		
		for (Player p : r.getPlayers())
		{
			notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
		}
		
		super.onEvtSpawn();
		Skill fp = SkillTable.getInstance().getInfo(14190, 1);
		fp.getEffects(actor, actor, false, false);
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		if (DeadTask != null)
		{
			DeadTask.cancel(true);
		}
		
		super.onEvtDead(killer);
	}
	
	public class SpawnMinion extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			NpcInstance actor = getActor();
			NpcInstance minion = actor.getReflection().addSpawnWithoutRespawn(25780, actor.getLoc(), 250);
			
			for (Player p : actor.getReflection().getPlayers())
			{
				minion.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
			}
		}
	}
}
