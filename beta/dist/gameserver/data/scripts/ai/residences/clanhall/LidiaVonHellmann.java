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
package ai.residences.clanhall;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.PositionUtils;
import ai.residences.SiegeGuardFighter;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class LidiaVonHellmann extends SiegeGuardFighter
{
	private static final Skill DRAIN_SKILL = SkillTable.getInstance().getInfo(4999, 1);
	private static final Skill DAMAGE_SKILL = SkillTable.getInstance().getInfo(4998, 1);
	
	/**
	 * Constructor for LidiaVonHellmann.
	 * @param actor NpcInstance
	 */
	public LidiaVonHellmann(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		Functions.npcShout(getActor(), NpcStringId.HMM_THOSE_WHO_ARE_NOT_OF_THE_BLOODLINE_ARE_COMING_THIS_WAY_TO_TAKE_OVER_THE_CASTLE_HUMPH_THE_BITTER_GRUDGES_OF_THE_DEAD_YOU_MUST_NOT_MAKE_LIGHT_OF_THEIR_POWER);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	public void onEvtDead(Creature killer)
	{
		super.onEvtDead(killer);
		Functions.npcShout(getActor(), NpcStringId.GRARR_FOR_THE_NEXT_2_MINUTES_OR_SO_THE_GAME_ARENA_ARE_WILL_BE_CLEANED_THROW_ANY_ITEMS_YOU_DON_T_NEED_TO_THE_FLOOR_NOW);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	public void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		super.onEvtAttacked(attacker, damage);
		
		if (Rnd.chance(0.22))
		{
			addTaskCast(attacker, DRAIN_SKILL);
		}
		else if ((actor.getCurrentHpPercents() < 20) && Rnd.chance(0.22))
		{
			addTaskCast(attacker, DRAIN_SKILL);
		}
		
		if ((PositionUtils.calculateDistance(actor, attacker, false) > 300) && Rnd.chance(0.13))
		{
			addTaskCast(attacker, DAMAGE_SKILL);
		}
	}
}
