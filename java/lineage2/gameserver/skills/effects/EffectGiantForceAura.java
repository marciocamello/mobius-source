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
package lineage2.gameserver.skills.effects;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.tables.SkillTable;

public class EffectGiantForceAura extends Effect
{
	private final int forceSkillId;
	private final int auraSkillId;
	private ScheduledFuture<?> startEffectTask;
	
	public EffectGiantForceAura(Env env, EffectTemplate template)
	{
		super(env, template);
		forceSkillId = template.getParam().getInteger("forceSkillId", -1);
		auraSkillId = template.getParam().getInteger("auraSkillId", -1);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		if (forceSkillId > 0)
		{
			startEffectTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					updateAura();
				}
			}, 500 + Rnd.get(4000));
		}
	}
	
	@Override
	public void onExit()
	{
		super.onExit();
		if (startEffectTask != null)
		{
			startEffectTask.cancel(false);
		}
	}
	
	void updateAura()
	{
		Player effector = (Player) getEffector();
		Skill forceSkill = SkillTable.getInstance().getInfo(forceSkillId, 1);
		Skill auraSkill = getSkill();
		if ((effector == null) || (forceSkill == null) || (auraSkill == null))
		{
			return;
		}
		List<Creature> targets = forceSkill.getTargets(effector, getEffected(), false);
		for (Creature target : targets)
		{
			if (target.getEffectList().getEffectsBySkillId(forceSkillId) == null)
			{
				forceSkill.getEffects(effector, target, false, false);
			}
		}
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getEffected().isDead())
		{
			return false;
		}
		if (forceSkillId > 0)
		{
			updateAura();
		}
		else if (auraSkillId > 0)
		{
			Player effector = (Player) getEffector();
			if ((effector == null) || (effector.getEffectList().getEffectsBySkillId(auraSkillId) == null))
			{
				return false;
			}
		}
		return true;
	}
}
