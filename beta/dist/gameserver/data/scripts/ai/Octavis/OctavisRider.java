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
package ai.Octavis;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius and Nache
 */
public final class OctavisRider extends DefaultAI
{
	private static final int OCTAVIS_LIGHT_BEAST = 29192;
	private static final int OCTAVIS_HARD_BEAST = 29210;
	
	private static final int CHAIN_STRIKE = 10015;
	private static final int CHAIN_HYDRA = 10016;
	private final Skill BEAST_HERO_MOVEMENT = SkillTable.getInstance().getInfo(14023, 1);
	private final Skill BEAST_ANCIENT_POWER = SkillTable.getInstance().getInfo(14024, 1);
	
	private final Location[] _points =
	{
		new Location(207992, 120904, -10038, 49151),
		new Location(207544, 121384, -10038),
		new Location(206856, 121384, -10038),
		new Location(206392, 120920, -10038),
		new Location(206392, 120264, -10038),
		new Location(206856, 119768, -10038),
		new Location(207528, 119768, -10038),
		new Location(207992, 120232, -10038)
	};
	
	private int _lastPoint = 0;
	private boolean _firstThought = true;
	
	/**
	 * Constructor for OctavisRider.
	 * @param actor NpcInstance
	 */
	public OctavisRider(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (!_def_think)
		{
			startMoveTask();
		}
		
		return true;
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		startMoveTask();
		super.onEvtArrived();
	}
	
	/**
	 * Method startMoveTask.
	 */
	private void startMoveTask()
	{
		final NpcInstance npc = getActor();
		if (_firstThought)
		{
			_lastPoint = getIndex(Location.findNearest(npc, _points));
			_firstThought = false;
		}
		else
		{
			_lastPoint++;
		}
		
		if (_lastPoint >= _points.length)
		{
			_lastPoint = 0;
		}
		
		addTaskMove(Location.findPointToStay(_points[_lastPoint], 1, npc.getGeoIndex()), true);
		npc.setRunning();
		doTask();
	}
	
	private int getIndex(Location loc)
	{
		for (int i = 0; i < _points.length; i++)
		{
			if (_points[i] == loc)
			{
				return i;
			}
		}
		
		return 0;
	}
	
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		NpcInstance actor = getActor();
		
		if (((actor.getId() == OCTAVIS_LIGHT_BEAST) || (actor.getId() == OCTAVIS_HARD_BEAST)) && ((skill.getId() == CHAIN_STRIKE) || (skill.getId() == CHAIN_HYDRA)))
		{
			if (Rnd.chance(40))
			{
				if (Rnd.chance(50))
				{
					actor.doCast(BEAST_HERO_MOVEMENT, caster, true);
				}
				else
				{
					actor.doCast(BEAST_ANCIENT_POWER, caster, true);
				}
			}
		}
		doTask();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
	}
}
