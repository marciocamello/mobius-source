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
package ai.seedofdestruction;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.instancemanager.SoDManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

public class Tiat extends Fighter
{
	private static final int TIAT_TRANSFORMATION_SKILL_ID = 5974;
	private static final Skill TIAT_TRANSFORMATION_SKILL = SkillTable.getInstance().getInfo(TIAT_TRANSFORMATION_SKILL_ID, 1);
	private boolean _notUsedTransform = true;
	private static final int TRAPS_COUNT = 4;
	private static final Location[] TRAP_LOCS =
	{
		new Location(-252022, 210130, -11995, 16384),
		new Location(-248782, 210130, -11995, 16384),
		new Location(-248782, 206875, -11995, 16384),
		new Location(-252022, 206875, -11995, 16384)
	};
	private static final long COLLAPSE_BY_INACTIVITY_INTERVAL = 10 * 60 * 1000;
	private long _lastAttackTime = 0;
	private static final int TRAP_NPC_ID = 18696;
	private static final int[] TIAT_MINION_IDS =
	{
		29162,
		22538,
		22540,
		22547,
		22542,
		22548
	};
	private static final String[] TIAT_TEXT =
	{
		"You'll regret challenging me!",
		"You shall die in pain!",
		"I will wipe out your entire kind!"
	};
	private long _lastFactionNotifyTime = 0;
	private boolean _immobilized;
	private boolean _failed = false;
	
	public Tiat(NpcInstance actor)
	{
		super(actor);
		_immobilized = true;
		actor.startImmobilized();
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return;
		}
		_lastAttackTime = System.currentTimeMillis();
		if (_notUsedTransform && (actor.getCurrentHpPercents() < 50))
		{
			if (_immobilized)
			{
				_immobilized = false;
				actor.stopImmobilized();
			}
			_notUsedTransform = false;
			clearTasks();
			spawnTraps();
			actor.abortAttack(true, false);
			actor.abortCast(true, false);
			actor.setIsInvul(true);
			actor.doCast(TIAT_TRANSFORMATION_SKILL, actor, true);
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					getActor().setCurrentHpMp(getActor().getMaxHp(), getActor().getMaxMp());
					getActor().setIsInvul(false);
				}
			}, TIAT_TRANSFORMATION_SKILL.getHitTime());
		}
		if ((System.currentTimeMillis() - _lastFactionNotifyTime) > _minFactionNotifyInterval)
		{
			_lastFactionNotifyTime = System.currentTimeMillis();
			for (NpcInstance npc : World.getAroundNpc(actor))
			{
				if (ArrayUtils.contains(TIAT_MINION_IDS, npc.getNpcId()))
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 30000);
				}
			}
			if (Rnd.chance(15) && !_notUsedTransform)
			{
				actor.broadcastPacket(new ExShowScreenMessage(TIAT_TEXT[Rnd.get(TIAT_TEXT.length)], 4000, ScreenMessageAlign.MIDDLE_CENTER, false));
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (!_failed && (_lastAttackTime != 0) && ((_lastAttackTime + COLLAPSE_BY_INACTIVITY_INTERVAL) < System.currentTimeMillis()))
		{
			final Reflection r = actor.getReflection();
			_failed = true;
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					for (Player pl : r.getPlayers())
					{
						pl.showQuestMovie(ExStartScenePlayer.SCENE_TIAT_FAIL);
					}
					r.clearReflection(5, true);
				}
			}, 1000);
			return true;
		}
		return super.thinkActive();
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		_notUsedTransform = true;
		_lastAttackTime = 0;
		_lastFactionNotifyTime = 0;
		NpcInstance actor = getActor();
		SoDManager.addTiatKill();
		final Reflection r = actor.getReflection();
		r.setReenterTime(System.currentTimeMillis());
		for (NpcInstance n : r.getNpcs())
		{
			n.deleteMe();
		}
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				for (Player pl : r.getPlayers())
				{
					if (pl != null)
					{
						pl.showQuestMovie(ExStartScenePlayer.SCENE_TIAT_SUCCESS);
					}
				}
			}
		}, 1000);
	}
	
	private void spawnTraps()
	{
		NpcInstance actor = getActor();
		actor.broadcastPacket(new ExShowScreenMessage("Come out, warriors. Protect Seed of Destruction.", 5000, ScreenMessageAlign.MIDDLE_CENTER, false));
		for (int i = 0; i < TRAPS_COUNT; i++)
		{
			actor.getReflection().addSpawnWithRespawn(TRAP_NPC_ID, TRAP_LOCS[i], 0, 180);
		}
	}
}
