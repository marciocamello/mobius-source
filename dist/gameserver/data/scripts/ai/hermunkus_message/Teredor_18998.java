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
package ai.hermunkus_message;

import instances.MemoryOfDisaster;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

public class Teredor_18998 extends DefaultAI
{
	private static final int SKILL_ID = 16021;
	
	public Teredor_18998(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		addTimer(1, 2500);
	}
	
	@Override
	protected void onEvtTimer(int timer_id, Object arg1, Object arg2)
	{
		super.onEvtTimer(timer_id, arg1, arg2);
		switch (timer_id)
		{
			case 1:
				Skill sk = SkillTable.getInstance().getInfo(SKILL_ID, 1);
				addTaskBuff(getActor(), sk);
				doTask();
				break;
			case 2:
				Reflection r = getActor().getReflection();
				if (r instanceof MemoryOfDisaster)
				{
					((MemoryOfDisaster) r).spawnWyrm();
				}
				getActor().deleteMe();
				break;
		}
	}
	
	@Override
	protected void onEvtFinishCasting(int skill_id, boolean success)
	{
		if (skill_id == SKILL_ID)
		{
			addTimer(2, 2000);
		}
	}
}
