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

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class KrateisCubeWatcherBlue extends DefaultAI
{
	private static final int RESTORE_CHANCE = 60;
	
	public KrateisCubeWatcherBlue(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 3000;
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
	
	@Override
	protected void onEvtThink()
	{
		NpcInstance actor = getActor();
		List<Creature> around = World.getAroundCharacters(actor, 600, 300);
		if (around.isEmpty())
		{
			return;
		}
		for (Creature cha : around)
		{
			if (cha.isPlayer() && !cha.isDead() && Rnd.chance(RESTORE_CHANCE))
			{
				double valCP = cha.getMaxCp() - cha.getCurrentCp();
				if (valCP > 0)
				{
					cha.setCurrentCp(valCP + cha.getCurrentCp());
					cha.sendPacket(new SystemMessage2(SystemMsg.S1_CP_HAS_BEEN_RESTORED).addInteger(Math.round(valCP)));
				}
				double valHP = cha.getMaxHp() - cha.getCurrentHp();
				if (valHP > 0)
				{
					cha.setCurrentHp(valHP + cha.getCurrentHp(), false);
					cha.sendPacket(new SystemMessage2(SystemMsg.S1_HP_HAS_BEEN_RESTORED).addInteger(Math.round(valHP)));
				}
				double valMP = cha.getMaxMp() - cha.getCurrentMp();
				if (valMP > 0)
				{
					cha.setCurrentMp(valMP + cha.getCurrentMp());
					cha.sendPacket(new SystemMessage2(SystemMsg.S1_MP_HAS_BEEN_RESTORED).addInteger(Math.round(valMP)));
				}
			}
		}
	}
	
	@Override
	public void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		super.onEvtDead(killer);
		actor.deleteMe();
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				NpcTemplate template = NpcHolder.getInstance().getTemplate(18601);
				if (template != null)
				{
					NpcInstance a = template.getNewInstance();
					a.setCurrentHpMp(a.getMaxHp(), a.getMaxMp());
					a.spawnMe(actor.getLoc());
				}
			}
		}, 10000L);
	}
}
