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
package npc.model;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.reflection.OnReflectionCollapseListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.MinionData;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class Kama26BossInstance extends KamalokaBossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ScheduledFuture<?> _spawner;
	private final ReflectionCollapseListener _refCollapseListener = new ReflectionCollapseListener();
	
	public Kama26BossInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		getMinionList().addMinion(new MinionData(18556, 1));
	}
	
	@Override
	public void notifyMinionDied(MinionInstance minion)
	{
		_spawner = ThreadPoolManager.getInstance().scheduleAtFixedRate(new MinionSpawner(), 60000, 60000);
	}
	
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		getReflection().addListener(_refCollapseListener);
	}
	
	@Override
	protected void onDeath(Creature killer)
	{
		if (_spawner != null)
		{
			_spawner.cancel(false);
		}
		_spawner = null;
		super.onDeath(killer);
	}
	
	public class MinionSpawner extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			if (!isDead() && !getMinionList().hasAliveMinions())
			{
				getMinionList().spawnMinions();
				Functions.npcSayCustomMessage(Kama26BossInstance.this, "Kama26Boss.helpme");
			}
		}
	}
	
	public class ReflectionCollapseListener implements OnReflectionCollapseListener
	{
		@Override
		public void onReflectionCollapse(Reflection ref)
		{
			if (_spawner != null)
			{
				_spawner.cancel(true);
			}
		}
	}
}
