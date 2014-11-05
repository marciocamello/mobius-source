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
package ai.DwarvenVillageAttack;

import instances.MemoryOfDisaster;

import java.util.List;

import lineage2.commons.collections.CollectionUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SiegeGolem extends DefaultAI
{
	private static final int SKILL_ID = 16024;
	private static final int[] ATTACK_IDS =
	{
		19172,
		19217
	};
	private static final Location[] MOVE_LOC =
	{
		new Location(116560, -179440, -1144),
		new Location(116608, -179205, -1176)
	};
	private long lastCastTime = 0;
	private int diedTeredor = 0;
	private int currentPoint = -1;
	private Location loc;
	
	/**
	 * Constructor for SiegeGolem.
	 * @param actor NpcInstance
	 */
	public SiegeGolem(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 50;
		AI_TASK_ACTIVE_DELAY = 250;
	}
	
	/**
	 * Method onEvtThink.
	 */
	@Override
	protected void onEvtThink()
	{
		super.onEvtThink();
		
		if (!getActor().getAggroList().isEmpty())
		{
			final List<Creature> chars = World.getAroundCharacters(getActor());
			CollectionUtils.eqSort(chars, _nearestTargetComparator);
			
			for (Creature cha : chars)
			{
				if ((getActor().getAggroList().get(cha) != null) && checkAggression(cha))
				{
					Skill sk = SkillTable.getInstance().getInfo(SKILL_ID, 1);
					
					if ((lastCastTime + sk.getHitTime() + sk.getReuseDelay()) <= System.currentTimeMillis())
					{
						lastCastTime = System.currentTimeMillis();
						addTaskCast(cha, sk);
					}
				}
			}
		}
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		
		if ((actor == null) || actor.isDead())
		{
			return true;
		}
		
		if (_def_think)
		{
			doTask();
			return true;
		}
		
		if ((diedTeredor < 3) || (currentPoint >= (MOVE_LOC.length - 1)))
		{
			final List<Creature> list = World.getAroundCharacters(getActor(), getActor().getAggroRange(), getActor().getAggroRange());
			
			for (Creature target : list)
			{
				if ((target != null) && !target.isDead() && Util.contains(ATTACK_IDS, target.getId()))
				{
					Skill sk = SkillTable.getInstance().getInfo(SKILL_ID, 1);
					
					if ((lastCastTime + sk.getHitTime() + sk.getReuseDelay()) <= System.currentTimeMillis())
					{
						lastCastTime = System.currentTimeMillis();
						clearTasks();
						addTaskCast(target, sk);
						return true;
					}
					
					return false;
				}
			}
		}
		else if ((diedTeredor >= 3) && (currentPoint < (MOVE_LOC.length - 1)))
		{
			if ((loc == null) || (getActor().getDistance(loc) <= 100))
			{
				currentPoint++;
				loc = new Location((MOVE_LOC[currentPoint].getX() + Rnd.get(50)) - Rnd.get(50), (MOVE_LOC[currentPoint].getY() + Rnd.get(50)) - Rnd.get(50), (MOVE_LOC[currentPoint].getZ() + Rnd.get(50)) - Rnd.get(50));
				
				if (currentPoint == 0)
				{
					final Reflection r = getActor().getReflection();
					
					if (r instanceof MemoryOfDisaster)
					{
						((MemoryOfDisaster) r).spawnTransparentTeredor();
					}
				}
			}
			
			actor.setWalking();
			clearTasks();
			addTaskMove(loc, true);
			doTask();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Method onEvtFinishCasting.
	 * @param skill_id int
	 * @param success boolean
	 */
	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if (success && (skill_id == SKILL_ID))
		{
			getActor().broadcastPacket(new Earthquake(getActor().getLoc(), 50, 4));
		}
	}
	
	/**
	 * Method onEvtScriptEvent.
	 * @param event String
	 * @param arg1 Object
	 * @param arg2 Object
	 */
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		super.onEvtScriptEvent(event, arg1, arg2);
		
		if (event.equalsIgnoreCase("TEREDOR_DIE"))
		{
			diedTeredor++;
		}
	}
	
	/**
	 * Method canAttackCharacter.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean canAttackCharacter(Creature target)
	{
		return Util.contains(ATTACK_IDS, target.getId());
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		return Util.contains(ATTACK_IDS, target.getId());
	}
	
	/**
	 * Method returnHome.
	 * @param clearAggro boolean
	 * @param teleport boolean
	 */
	@Override
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
	}
	
	/**
	 * Method maybeMoveToHome.
	 * @return boolean
	 */
	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}
	
	/**
	 * Method getMaxAttackTimeout.
	 * @return int
	 */
	@Override
	public int getMaxAttackTimeout()
	{
		return 0;
	}
}
