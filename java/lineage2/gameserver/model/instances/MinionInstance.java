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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

public class MinionInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MonsterInstance _master;
	
	public MinionInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	public void setLeader(MonsterInstance leader)
	{
		_master = leader;
	}
	
	public MonsterInstance getLeader()
	{
		return _master;
	}
	
	public boolean isRaidFighter()
	{
		return (getLeader() != null) && getLeader().isRaid();
	}
	
	@Override
	protected void onDeath(Creature killer)
	{
		if (getLeader() != null)
		{
			getLeader().notifyMinionDied(this);
		}
		super.onDeath(killer);
	}
	
	@Override
	protected void onDecay()
	{
		decayMe();
		_spawnAnimation = 2;
	}
	
	@Override
	public boolean isFearImmune()
	{
		return isRaidFighter();
	}
	
	@Override
	public Location getSpawnedLoc()
	{
		return getLeader() != null ? getLeader().getLoc() : getLoc();
	}
	
	@Override
	public boolean canChampion()
	{
		return false;
	}
	
	@Override
	public boolean isMinion()
	{
		return true;
	}
}
