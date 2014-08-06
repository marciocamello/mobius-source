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

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

public final class TransparentSkill extends DefaultAI
{
	private static final int SKILL_ID = 14649;
	
	public TransparentSkill(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		addTimer(1, 100);
	}
	
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		if (timerId == 1)
		{
			Skill skill = SkillTable.getInstance().getInfo(SKILL_ID, 1);
			addTaskBuff(getActor(), skill);
			doTask();
		}
	}
	
	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if (skill_id == SKILL_ID)
		{
			getActor().deleteMe();
		}
	}
}
