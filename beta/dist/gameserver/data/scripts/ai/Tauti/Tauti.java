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
package ai.Tauti;

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Nache
 */
public class Tauti extends Fighter
{
	// Npcs
	private static final int TAUTI_NORMAL = 29233;
	private static final int TAUTI_NORMAL_AXE = 29236;
	private static final int TAUTI_EXTREME = 29234;
	private static final int TAUTI_EXTREME_AXE = 29237;
	// Skills
	private static final Skill TAUTI_ULTIMATE_CHAIN_STRIKE = SkillTable.getInstance().getInfo(15168, 1);
	// Normal Mode
	private static final Skill TAUTI_PUSH_EASY = SkillTable.getInstance().getInfo(15042, 1);
	private static final Skill TAUTI_WHIRLWIND_EASY = SkillTable.getInstance().getInfo(15044, 1);
	private static final Skill TAUTI_SHOCK_WAVE_EASY = SkillTable.getInstance().getInfo(15046, 1);
	private static final Skill TAUTI_TYPHOON_EASY = SkillTable.getInstance().getInfo(15163, 1);
	private static final Skill TAUTI_ULTRA_WHIRLWIND_EASY = SkillTable.getInstance().getInfo(15200, 1);
	private static final Skill TAUTI_ULTRA_TYPHOON_EASY = SkillTable.getInstance().getInfo(15202, 1);
	// Hard Mode
	private static final Skill TAUTI_PUSH_HARD = SkillTable.getInstance().getInfo(15043, 1);
	private static final Skill TAUTI_WHIRLWIND_HARD = SkillTable.getInstance().getInfo(15045, 1);
	// private static final Skill TAUTI_LEAP_ATTACK = SkillTable.getInstance().getInfo(15048, 1); FIXME: Need to fix skill
	private static final Skill TAUTI_TYPHOON_HARD = SkillTable.getInstance().getInfo(15164, 1);
	private static final Skill TAUTI_SHOCK_WAVE_HARD = SkillTable.getInstance().getInfo(15165, 1);
	private static final Skill TAUTI_ULTRA_WHIRLWIND_HARD = SkillTable.getInstance().getInfo(15201, 1);
	private static final Skill TAUTI_ULTRA_TYPHOON_HARD = SkillTable.getInstance().getInfo(15203, 1);
	// Other
	private final double[] hpPercentTriggers =
	{
		0.9D,
		0.8D,
		0.7D,
		0.6D,
		0.5D,
		0.45D,
		0.4D,
		0.35D,
		0.3D,
		0.25D,
		0.2D,
		0.15D,
		0.1D
	};
	
	public Tauti(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return false;
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance npc = getActor();
		
		final double lastPercentHp = (npc.getCurrentHp() + damage) / npc.getMaxHp();
		final double currentPercentHp = npc.getCurrentHp() / npc.getMaxHp();
		
		for (double trigger : hpPercentTriggers)
		{
			if ((lastPercentHp > trigger) && (currentPercentHp <= trigger))
			{
				onPercentHpReached(npc, (int) (trigger * 100));
			}
		}
		
		super.onEvtAttacked(attacker, damage);
	}
	
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		Creature target = prepareTarget();
		
		if (target == null)
		{
			return false;
		}
		
		NpcInstance npc = getActor();
		
		if (npc.isDead())
		{
			return false;
		}
		
		final double distance = npc.getDistance(target);
		
		switch (npc.getId())
		{
			case TAUTI_NORMAL:
			case TAUTI_NORMAL_AXE:
				if ((Rnd.get() <= 0.2D) && !npc.isCastingNow())
				{
					final Map<Skill, Integer> skills = new HashMap<>();
					addDesiredSkill(skills, target, distance, TAUTI_PUSH_EASY);
					addDesiredSkill(skills, target, distance, TAUTI_WHIRLWIND_EASY);
					addDesiredSkill(skills, target, distance, TAUTI_SHOCK_WAVE_EASY);
					addDesiredSkill(skills, target, distance, TAUTI_TYPHOON_EASY);
					// addDesiredSkill(skills, target, distance, TAUTI_LEAP_ATTACK); FIXME: need fix
					
					final Skill skill = selectTopSkill(skills);
					if ((skill != null) && !skill.isOffensive())
					{
						target = npc;
					}
					
					return chooseTaskAndTargets(skill, target, distance);
				}
				break;
			
			case TAUTI_EXTREME:
			case TAUTI_EXTREME_AXE:
				if ((Rnd.get() <= 0.2D) && !npc.isCastingNow())
				{
					final Map<Skill, Integer> skills = new HashMap<>();
					addDesiredSkill(skills, target, distance, TAUTI_PUSH_HARD);
					addDesiredSkill(skills, target, distance, TAUTI_WHIRLWIND_HARD);
					addDesiredSkill(skills, target, distance, TAUTI_SHOCK_WAVE_HARD);
					addDesiredSkill(skills, target, distance, TAUTI_TYPHOON_HARD);
					// addDesiredSkill(skills, target, distance, TAUTI_LEAP_ATTACK); FIXME need fix
					
					final Skill skill = selectTopSkill(skills);
					if ((skill != null) && !skill.isOffensive())
					{
						target = npc;
					}
					
					return chooseTaskAndTargets(skill, target, distance);
				}
				break;
		}
		return chooseTaskAndTargets(null, target, distance);
	}
	
	public void onPercentHpReached(NpcInstance npc, int percent)
	{
		final Creature player = prepareTarget();
		
		switch (npc.getId())
		{
			case TAUTI_NORMAL:
				if ((percent <= 50) && (percent >= 15))
				{
					if ((Rnd.get() <= 0.2D) && !npc.isCastingNow())
					{
						Skill skill = Rnd.chance(50) ? TAUTI_ULTRA_WHIRLWIND_EASY : TAUTI_ULTRA_TYPHOON_EASY;
						npc.doCast(skill, player, true);
						
						if (skill == TAUTI_ULTRA_TYPHOON_EASY)
						{
							npc.doCast(TAUTI_ULTIMATE_CHAIN_STRIKE, player, true);
						}
					}
				}
				break;
			
			case TAUTI_EXTREME:
				if ((percent <= 50) && (percent >= 15))
				{
					if ((Rnd.get() <= 0.2D) && !npc.isCastingNow())
					{
						Skill skill = Rnd.chance(50) ? TAUTI_ULTRA_WHIRLWIND_HARD : TAUTI_ULTRA_TYPHOON_HARD;
						npc.doCast(skill, player, true);
						
						if (skill == TAUTI_ULTRA_TYPHOON_HARD)
						{
							npc.doCast(TAUTI_ULTIMATE_CHAIN_STRIKE, player, true);
						}
					}
				}
				break;
		}
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
	}
}