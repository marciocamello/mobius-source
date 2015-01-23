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
package lineage2.gameserver.model;

import java.util.List;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.network.serverpackets.ExVoteSystemInfo;
import lineage2.gameserver.network.serverpackets.MagicSkillLaunched;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameObjectTasks
{
	/**
	 * @author Mobius
	 */
	public static class DeleteTask extends RunnableImpl
	{
		private final HardReference<? extends Creature> _ref;
		
		/**
		 * Constructor for DeleteTask.
		 * @param c Creature
		 */
		public DeleteTask(Creature c)
		{
			_ref = c.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature c = _ref.get();
			
			if (c != null)
			{
				c.deleteMe();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class SoulConsumeTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for SoulConsumeTask.
		 * @param player Player
		 */
		SoulConsumeTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			player.setConsumedSouls(player.getConsumedSouls() + 1, null);
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class PvPFlagTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for PvPFlagTask.
		 * @param player Player
		 */
		PvPFlagTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			long diff = Math.abs(System.currentTimeMillis() - player.getlastPvpAttack());
			
			if (diff > Config.PVP_TIME)
			{
				player.stopPvPFlag();
			}
			else if (diff > (Config.PVP_TIME - 20000))
			{
				player.updatePvPFlag(2);
			}
			else
			{
				player.updatePvPFlag(1);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class HourlyTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for HourlyTask.
		 * @param player Player
		 */
		HourlyTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			int hoursInGame = player.getHoursInGame();
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_PLAYED_FOR_S1_HOUR_S_PLEASE_TAKE_A_BREAK).addInt(hoursInGame));
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_OBTAINED_S1_RECOMMENDATION_S).addInt(player.addRecomLeft()));
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class RecomBonusTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for RecomBonusTask.
		 * @param player Player
		 */
		RecomBonusTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			player.setRecomBonusTime(0);
			player.sendPacket(new ExVoteSystemInfo(player));
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class WaterTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for WaterTask.
		 * @param player Player
		 */
		WaterTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			if (player.isDead() || !player.isInWater())
			{
				player.stopWaterTask();
				return;
			}
			
			double reduceHp = player.getMaxHp() < 100 ? 1 : player.getMaxHp() / 100;
			player.reduceCurrentHp(reduceHp, 0, player, null, false, false, true, false, false, false, false);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_TAKEN_S1_DAMAGE_BECAUSE_YOU_WERE_UNABLE_TO_BREATHE).addLong((long) reduceHp));
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class KickTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for KickTask.
		 * @param player Player
		 */
		KickTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			player.setOfflineMode(false);
			player.kick();
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class UnJailTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for UnJailTask.
		 * @param player Player
		 */
		UnJailTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			player.unblock();
			player.standUp();
			player.teleToLocation(17817, 170079, -3530, ReflectionManager.DEFAULT);
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class EndSitDownTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for EndSitDownTask.
		 * @param player Player
		 */
		EndSitDownTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			player.sittingTaskLaunched = false;
			player.getAI().clearNextAction();
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class EndStandUpTask extends RunnableImpl
	{
		private final HardReference<Player> _playerRef;
		
		/**
		 * Constructor for EndStandUpTask.
		 * @param player Player
		 */
		EndStandUpTask(Player player)
		{
			_playerRef = player.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = _playerRef.get();
			
			if (player == null)
			{
				return;
			}
			
			player.sittingTaskLaunched = false;
			player.setSitting(false);
			
			if (!player.getAI().setNextIntention())
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class AltMagicUseTask extends RunnableImpl
	{
		private final Skill _skill;
		private final HardReference<? extends Creature> _charRef, _targetRef;
		
		/**
		 * Constructor for AltMagicUseTask.
		 * @param character Creature
		 * @param target Creature
		 * @param skill Skill
		 */
		AltMagicUseTask(Creature character, Creature target, Skill skill)
		{
			_charRef = character.getRef();
			_targetRef = target.getRef();
			_skill = skill;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature cha, target;
			
			if (((cha = _charRef.get()) == null) || ((target = _targetRef.get()) == null))
			{
				return;
			}
			
			cha.altOnMagicUseTimer(target, _skill);
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class CastEndTimeTask extends RunnableImpl
	{
		private final HardReference<? extends Creature> _charRef;
		
		/**
		 * Constructor for CastEndTimeTask.
		 * @param character Creature
		 */
		CastEndTimeTask(Creature character)
		{
			_charRef = character.getRef();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character = _charRef.get();
			
			if (character == null)
			{
				return;
			}
			
			character.onCastEndTime(true);
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class HitTask extends RunnableImpl
	{
		private final boolean _crit, _miss, _shld, _soulshot, _unchargeSS, _notify;
		private final int _damage, _reflectableDamage;
		private final HardReference<? extends Creature> _charRef, _targetRef;
		
		/**
		 * Constructor for HitTask.
		 * @param cha Creature
		 * @param target Creature
		 * @param damage int
		 * @param reflectableDamage int
		 * @param crit boolean
		 * @param miss boolean
		 * @param soulshot boolean
		 * @param shld boolean
		 * @param unchargeSS boolean
		 * @param notify boolean
		 */
		HitTask(Creature cha, Creature target, int damage, int reflectableDamage, boolean crit, boolean miss, boolean soulshot, boolean shld, boolean unchargeSS, boolean notify)
		{
			_charRef = cha.getRef();
			_targetRef = target.getRef();
			_damage = damage;
			_reflectableDamage = reflectableDamage;
			_crit = crit;
			_shld = shld;
			_miss = miss;
			_soulshot = soulshot;
			_unchargeSS = unchargeSS;
			_notify = notify;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character, target;
			
			if (((character = _charRef.get()) == null) || ((target = _targetRef.get()) == null))
			{
				return;
			}
			
			if (character.isAttackAborted())
			{
				return;
			}
			
			character.onHitTimer(target, _damage, _reflectableDamage, _crit, _miss, _soulshot, _shld, _unchargeSS);
			
			if (_notify)
			{
				character.getAI().notifyEvent(CtrlEvent.EVT_READY_TO_ACT);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class MagicUseTask extends RunnableImpl
	{
		private final boolean _forceUse;
		private final HardReference<? extends Creature> _charRef;
		
		/**
		 * Constructor for MagicUseTask.
		 * @param cha Creature
		 * @param forceUse boolean
		 */
		MagicUseTask(Creature cha, boolean forceUse)
		{
			_charRef = cha.getRef();
			_forceUse = forceUse;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character = _charRef.get();
			
			if (character == null)
			{
				return;
			}
			
			Skill castingSkill = character.getCastingSkill();
			Creature castingTarget = character.getCastingTarget();
			
			if ((castingSkill == null) || (castingTarget == null))
			{
				character.clearCastVars();
				return;
			}
			
			character.onMagicUseTimer(castingTarget, castingSkill, _forceUse);
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class MagicLaunchedTask extends RunnableImpl
	{
		private final boolean _forceUse;
		private final HardReference<? extends Creature> _charRef;
		
		/**
		 * Constructor for MagicLaunchedTask.
		 * @param cha Creature
		 * @param forceUse boolean
		 */
		MagicLaunchedTask(Creature cha, boolean forceUse)
		{
			_charRef = cha.getRef();
			_forceUse = forceUse;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character = _charRef.get();
			
			if (character == null)
			{
				return;
			}
			
			Skill castingSkill = character.getCastingSkill();
			Creature castingTarget = character.getCastingTarget();
			
			if ((castingSkill == null) || (castingTarget == null))
			{
				character.clearCastVars();
				return;
			}
			
			List<Creature> targets = castingSkill.getTargets(character, castingTarget, _forceUse);
			character.broadcastPacket(new MagicSkillLaunched(character.getObjectId(), castingSkill.getDisplayId(), castingSkill.getDisplayLevel(), targets));
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class NotifyAITask extends RunnableImpl
	{
		private final CtrlEvent _evt;
		private final Object _agr0;
		private final Object _agr1;
		private final HardReference<? extends Creature> _charRef;
		
		/**
		 * Constructor for NotifyAITask.
		 * @param cha Creature
		 * @param evt CtrlEvent
		 * @param agr0 Object
		 * @param agr1 Object
		 */
		public NotifyAITask(Creature cha, CtrlEvent evt, Object agr0, Object agr1)
		{
			_charRef = cha.getRef();
			_evt = evt;
			_agr0 = agr0;
			_agr1 = agr1;
		}
		
		/**
		 * Constructor for NotifyAITask.
		 * @param cha Creature
		 * @param evt CtrlEvent
		 */
		public NotifyAITask(Creature cha, CtrlEvent evt)
		{
			this(cha, evt, null, null);
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Creature character = _charRef.get();
			
			if ((character == null) || !character.hasAI())
			{
				return;
			}
			
			character.getAI().notifyEvent(_evt, _agr0, _agr1);
		}
	}
}
