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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

public class GuardofDawn extends DefaultAI
{
	private static final int _aggrorange = 150;
	private static final Skill _skill = SkillTable.getInstance().getInfo(5978, 1);
	private Location _locStart = null;
	private Location _locEnd = null;
	private Location _locTele = null;
	private boolean moveToEnd = true;
	boolean noCheckPlayers = false;
	
	public GuardofDawn(NpcInstance actor, Location locationEnd, Location telePoint)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 200;
		setStartPoint(actor.getSpawnedLoc());
		setEndPoint(locationEnd);
		setTelePoint(telePoint);
	}
	
	public class Teleportation extends RunnableImpl
	{
		Location _telePoint = null;
		Playable _target = null;
		
		public Teleportation(Location telePoint, Playable target)
		{
			_telePoint = telePoint;
			_target = target;
		}
		
		@Override
		public void runImpl()
		{
			_target.teleToLocation(_telePoint);
			noCheckPlayers = false;
		}
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (!noCheckPlayers)
		{
			checkAroundPlayers(actor);
		}
		if (_def_think)
		{
			doTask();
			return true;
		}
		moveToEnd = !moveToEnd;
		if (!moveToEnd)
		{
			addTaskMove(getEndPoint(), true);
		}
		else
		{
			addTaskMove(getStartPoint(), true);
		}
		doTask();
		return true;
	}
	
	private boolean checkAroundPlayers(NpcInstance actor)
	{
		for (Playable target : World.getAroundPlayables(actor, _aggrorange, _aggrorange))
		{
			if ((target != null) && target.isPlayer() && !target.isInvul() && GeoEngine.canSeeTarget(actor, target, false))
			{
				actor.doCast(_skill, target, true);
				Functions.npcSay(actor, "Intruder! Protect the Priests of Dawn!");
				noCheckPlayers = true;
				ThreadPoolManager.getInstance().schedule(new Teleportation(getTelePoint(), target), 3000);
				return true;
			}
		}
		return false;
	}
	
	private void setStartPoint(Location loc)
	{
		_locStart = loc;
	}
	
	private void setEndPoint(Location loc)
	{
		_locEnd = loc;
	}
	
	private void setTelePoint(Location loc)
	{
		_locTele = loc;
	}
	
	private Location getStartPoint()
	{
		return _locStart;
	}
	
	private Location getEndPoint()
	{
		return _locEnd;
	}
	
	private Location getTelePoint()
	{
		return _locTele;
	}
	
	@Override
	protected void thinkAttack()
	{
	}
	
	@Override
	protected void onIntentionAttack(Creature target)
	{
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
	
	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
	}
	
	@Override
	protected void onEvtClanAttacked(Creature attacked_member, Creature attacker, int damage)
	{
	}
}
