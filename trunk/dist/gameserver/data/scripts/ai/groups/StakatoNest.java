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
package ai.groups;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.MinionList;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class StakatoNest extends Fighter
{
	private static final int[] BIZARRE_COCOON =
	{
		18793,
		18794,
		18795,
		18796,
		18797,
		18798
	};
	private static final int CANNIBALISTIC_STAKATO_LEADER = 22625;
	private static final int SPIKE_STAKATO_NURSE = 22630;
	private static final int SPIKE_STAKATO_NURSE_CHANGED = 22631;
	private static final int SPIKED_STAKATO_BABY = 22632;
	private static final int SPIKED_STAKATO_CAPTAIN = 22629;
	private static final int FEMALE_SPIKED_STAKATO = 22620;
	private static final int MALE_SPIKED_STAKATO = 22621;
	private static final int MALE_SPIKED_STAKATO_2 = 22622;
	private static final int SPIKED_STAKATO_GUARD = 22619;
	private static final int SKILL_GROWTH_ACCELERATOR = 2905;
	private static final int CANNIBALISTIC_STAKATO_CHIEF = 25667;
	private static final int QUEEN_SHYEED = 25671;
	private static final int FAIL_COCOON_CHANCE = 8;
	private static final int ABSORB_MINION_CHANCE = 10;
	private static final Zone _zone_mob_buff = ReflectionUtils.getZone("[stakato_mob_buff]");
	private static final Zone _zone_mob_buff_pc_display = ReflectionUtils.getZone("[stakato_mob_buff_display]");
	private static final Zone _zone_pc_buff = ReflectionUtils.getZone("[stakato_pc_buff]");
	private static boolean _debuffed = false;
	
	/**
	 * Constructor for StakatoNest.
	 * @param actor NpcInstance
	 */
	public StakatoNest(NpcInstance actor)
	{
		super(actor);
		
		if (Util.contains(BIZARRE_COCOON, actor.getId()))
		{
			actor.setIsInvul(true);
			actor.startImmobilized();
		}
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		final NpcInstance actor = getActor();
		
		if (actor.getId() != QUEEN_SHYEED)
		{
			super.onEvtSpawn();
			return;
		}
		
		if (!_debuffed)
		{
			_debuffed = true;
			_zone_mob_buff.setActive(true);
			_zone_mob_buff_pc_display.setActive(true);
			_zone_pc_buff.setActive(false);
		}
		
		for (Player player : World.getAroundPlayers(actor))
		{
			if (player != null)
			{
				player.sendPacket(new SystemMessage(SystemMessage.SHYEED_S_ROAR_FILLED_WITH_WRATH_RINGS_THROUGHOUT_THE_STAKATO_NEST));
			}
		}
		
		super.onEvtSpawn();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		final MonsterInstance _mob = (MonsterInstance) actor;
		
		if ((_mob.getId() == CANNIBALISTIC_STAKATO_LEADER) && Rnd.chance(ABSORB_MINION_CHANCE) && (_mob.getCurrentHpPercents() < 30))
		{
			final MonsterInstance _follower = getAliveMinion(actor);
			
			if ((_follower != null) && (_follower.getCurrentHpPercents() > 30))
			{
				_mob.abortAttack(true, false);
				_mob.abortCast(true, false);
				_mob.setHeading(PositionUtils.getHeadingTo(_mob, _follower));
				_mob.doCast(SkillTable.getInstance().getInfo(4485, 1), _follower, false);
				_mob.setCurrentHp(_mob.getCurrentHp() + _follower.getCurrentHp(), false);
				_follower.doDie(_follower);
				_follower.deleteMe();
			}
		}
		
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		final MinionInstance _minion = getAliveMinion(actor);
		MonsterInstance _leader = null;
		
		switch (actor.getId())
		{
			case SPIKE_STAKATO_NURSE:
				if (_minion == null)
				{
					break;
				}
				
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 2046, 1, 1000, 0));
				
				for (int i = 0; i < 3; i++)
				{
					spawnMonster(_minion, killer, SPIKED_STAKATO_CAPTAIN);
				}
				
				break;
			
			case SPIKED_STAKATO_BABY:
				_leader = ((MinionInstance) actor).getLeader();
				
				if ((_leader != null) && !_leader.isDead())
				{
					ThreadPoolManager.getInstance().schedule(new ChangeMonster(SPIKE_STAKATO_NURSE_CHANGED, actor, killer), 3000L);
				}
				
				break;
			
			case MALE_SPIKED_STAKATO:
				if (_minion == null)
				{
					break;
				}
				
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 2046, 1, 1000, 0));
				
				for (int i = 0; i < 3; i++)
				{
					spawnMonster(_minion, killer, SPIKED_STAKATO_GUARD);
				}
				
				break;
			
			case FEMALE_SPIKED_STAKATO:
				_leader = ((MinionInstance) actor).getLeader();
				
				if ((_leader != null) && !_leader.isDead())
				{
					ThreadPoolManager.getInstance().schedule(new ChangeMonster(MALE_SPIKED_STAKATO_2, actor, killer), 3000L);
				}
				
				break;
			
			case QUEEN_SHYEED:
				if (_debuffed)
				{
					_debuffed = false;
					_zone_pc_buff.setActive(true);
					_zone_mob_buff.setActive(false);
					_zone_mob_buff_pc_display.setActive(false);
				}
				
				break;
			
			default:
				break;
		}
		
		super.onEvtDead(killer);
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final NpcInstance actor = getActor();
		
		if ((actor == null) || !Util.contains(BIZARRE_COCOON, actor.getId()) || (caster == null) || (skill.getId() != SKILL_GROWTH_ACCELERATOR))
		{
			super.onEvtSeeSpell(skill, caster);
			return;
		}
		
		if (Rnd.chance(FAIL_COCOON_CHANCE))
		{
			caster.getPlayer().sendPacket(new SystemMessage(SystemMessage.NOTHING_HAPPENED));
			return;
		}
		
		actor.doDie(null);
		actor.endDecayTask();
		
		try
		{
			final NpcInstance mob = NpcHolder.getInstance().getTemplate(CANNIBALISTIC_STAKATO_CHIEF).getNewInstance();
			mob.setSpawnedLoc(actor.getLoc());
			mob.setReflection(actor.getReflection());
			mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp(), true);
			mob.spawnMe(mob.getSpawnedLoc());
			mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, caster.getPlayer(), Rnd.get(1, 100));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		super.onEvtSeeSpell(skill, caster);
	}
	
	/**
	 * @author Mobius
	 */
	private class ChangeMonster extends RunnableImpl
	{
		private final int _monsterId;
		private final Creature _killer;
		private final NpcInstance _npc;
		
		/**
		 * Constructor for ChangeMonster.
		 * @param mobId int
		 * @param npc NpcInstance
		 * @param killer Creature
		 */
		ChangeMonster(int mobId, NpcInstance npc, Creature killer)
		{
			_monsterId = mobId;
			_npc = npc;
			_killer = killer;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			spawnMonster(_npc, _killer, _monsterId);
		}
	}
	
	/**
	 * Method getAliveMinion.
	 * @param npc NpcInstance
	 * @return MinionInstance
	 */
	private MinionInstance getAliveMinion(NpcInstance npc)
	{
		final MinionList ml = npc.getMinionList();
		
		if ((ml != null) && ml.hasAliveMinions())
		{
			for (MinionInstance minion : ml.getAliveMinions())
			{
				return minion;
			}
		}
		
		return null;
	}
	
	/**
	 * Method spawnMonster.
	 * @param actor NpcInstance
	 * @param killer Creature
	 * @param mobId int
	 */
	void spawnMonster(NpcInstance actor, Creature killer, int mobId)
	{
		try
		{
			final NpcInstance npc = NpcHolder.getInstance().getTemplate(mobId).getNewInstance();
			npc.setSpawnedLoc(actor.getSpawnedLoc());
			npc.setReflection(actor.getReflection());
			npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp(), true);
			npc.spawnMe(actor.getSpawnedLoc());
			
			if (killer != null)
			{
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, killer, Rnd.get(1, 100));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return (Util.contains(BIZARRE_COCOON, getActor().getId()) || (getActor().getId() == QUEEN_SHYEED)) ? false : true;
	}
	
	/**
	 * Method randomAnimation.
	 * @return boolean
	 */
	@Override
	protected boolean randomAnimation()
	{
		return Util.contains(BIZARRE_COCOON, getActor().getId()) ? false : true;
	}
}
