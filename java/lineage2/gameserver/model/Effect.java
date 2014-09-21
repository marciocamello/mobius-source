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
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.listener.actor.OnAttackListener;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.network.serverpackets.AbnormalStatusUpdate;
import lineage2.gameserver.network.serverpackets.ExOlympiadSpelledInfo;
import lineage2.gameserver.network.serverpackets.IconEffectPacket;
import lineage2.gameserver.network.serverpackets.ShortBuffStatusUpdate;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.stats.funcs.FuncOwner;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.taskmanager.EffectTaskManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Effect extends RunnableImpl implements Comparable<Effect>, FuncOwner
{
	public static final Logger _log = LoggerFactory.getLogger(Effect.class);
	public final static Effect[] EMPTY_L2EFFECT_ARRAY = new Effect[0];
	private static final int SUSPENDED = -1;
	private static final int STARTING = 0;
	private static final int STARTED = 1;
	private static final int ACTING = 2;
	private static final int FINISHING = 3;
	private static final int FINISHED = 4;
	protected final Creature _effector;
	public final Creature _effected;
	protected final Skill _skill;
	protected final int _displayId;
	protected final int _displayLevel;
	private final double _value;
	private final AtomicInteger _state;
	private int _count;
	private long _period;
	private long _startTimeMillis;
	private long _duration;
	private boolean _inUse = false;
	private Effect _next = null;
	private boolean _active = false;
	protected final EffectTemplate _template;
	private Future<?> _effectTask;
	private final int _effector_obj;
	
	/**
	 * Constructor for Effect.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	protected Effect(Env env, EffectTemplate template)
	{
		_skill = env.skill;
		_effector = env.character;
		_effector_obj = _effector.getObjectId();
		_effected = env.target;
		_template = template;
		_value = template._value;
		_count = template.getCount();
		_period = template.getPeriod();
		_duration = _period * _count;
		_displayId = template._displayId != 0 ? template._displayId : _skill.getDisplayId();
		_displayLevel = template._displayLevel != 0 ? template._displayLevel : _skill.getDisplayLevel();
		_state = new AtomicInteger(STARTING);
	}
	
	/**
	 * Method getPeriod.
	 * @return long
	 */
	public long getPeriod()
	{
		return _period;
	}
	
	/**
	 * Method getEffectorObj.
	 * @return int
	 */
	public int getEffectorObj()
	{
		return _effector_obj;
	}
	
	/**
	 * Method setPeriod.
	 * @param time long
	 */
	public void setPeriod(long time)
	{
		_period = time;
		_duration = _period * _count;
	}
	
	/**
	 * Method getCount.
	 * @return int
	 */
	public int getCount()
	{
		return _count;
	}
	
	/**
	 * Method setCount.
	 * @param count int
	 */
	public void setCount(int count)
	{
		_count = count;
		_duration = _period * _count;
	}
	
	/**
	 * Method isOneTime.
	 * @return boolean
	 */
	public boolean isOneTime()
	{
		return _period == 0;
	}
	
	/**
	 * Method getStartTime.
	 * @return long
	 */
	public long getStartTime()
	{
		if (_startTimeMillis == 0L)
		{
			return System.currentTimeMillis();
		}
		
		return _startTimeMillis;
	}
	
	/**
	 * Method getTime.
	 * @return long
	 */
	public long getTime()
	{
		return System.currentTimeMillis() - getStartTime();
	}
	
	/**
	 * Method getDuration.
	 * @return long
	 */
	public long getDuration()
	{
		return _duration;
	}
	
	/**
	 * Method getTimeLeft.
	 * @return int
	 */
	public int getTimeLeft()
	{
		return (int) ((getDuration() - getTime()) / 1000L);
	}
	
	/**
	 * Method isTimeLeft.
	 * @return boolean
	 */
	public boolean isTimeLeft()
	{
		return (getDuration() - getTime()) > 0L;
	}
	
	/**
	 * Method isInUse.
	 * @return boolean
	 */
	public boolean isInUse()
	{
		return _inUse;
	}
	
	/**
	 * Method setInUse.
	 * @param inUse boolean
	 */
	public void setInUse(boolean inUse)
	{
		_inUse = inUse;
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return _active;
	}
	
	/**
	 * Method setActive.
	 * @param set boolean
	 */
	public void setActive(boolean set)
	{
		_active = set;
	}
	
	/**
	 * Method getTemplate.
	 * @return EffectTemplate
	 */
	public EffectTemplate getTemplate()
	{
		return _template;
	}
	
	/**
	 * Method getStackType.
	 * @return List<String>
	 */
	public List<String> getStackType()
	{
		return getTemplate()._stackTypes;
	}
	
	/**
	 * Method checkStackType.
	 * @param param String
	 * @return boolean
	 */
	public boolean checkStackType(String param)
	{
		return getStackType().contains(param);
	}
	
	/**
	 * Method checkStackType.
	 * @param param Effect
	 * @return boolean
	 */
	public boolean checkStackType(Effect param)
	{
		boolean r = false;
		
		for (String arg : param.getStackType())
		{
			r = checkStackType(arg);
		}
		
		return r;
	}
	
	/**
	 * Method getStackOrder.
	 * @return int
	 */
	public int getStackOrder()
	{
		return getTemplate()._stackOrder;
	}
	
	/**
	 * Method getSkill.
	 * @return Skill
	 */
	public Skill getSkill()
	{
		return _skill;
	}
	
	/**
	 * Method getEffector.
	 * @return Creature
	 */
	public Creature getEffector()
	{
		return _effector;
	}
	
	/**
	 * Method getEffected.
	 * @return Creature
	 */
	public Creature getEffected()
	{
		return _effected;
	}
	
	/**
	 * Method calc.
	 * @return double
	 */
	public double calc()
	{
		return _value;
	}
	
	/**
	 * Method isEnded.
	 * @return boolean
	 */
	public boolean isEnded()
	{
		return isFinished() || isFinishing();
	}
	
	/**
	 * Method isFinishing.
	 * @return boolean
	 */
	public boolean isFinishing()
	{
		return getState() == FINISHING;
	}
	
	/**
	 * Method isFinished.
	 * @return boolean
	 */
	public boolean isFinished()
	{
		return getState() == FINISHED;
	}
	
	/**
	 * Method getState.
	 * @return int
	 */
	private int getState()
	{
		return _state.get();
	}
	
	/**
	 * Method setState.
	 * @param oldState int
	 * @param newState int
	 * @return boolean
	 */
	private boolean setState(int oldState, int newState)
	{
		return _state.compareAndSet(oldState, newState);
	}
	
	private ActionDispelListener _actionlistener;
	
	/**
	 * @author Mobius
	 */
	private class ActionDispelListener implements OnAttackListener, OnMagicUseListener
	{
		/**
		 * Constructor for ActionDispelListener.
		 */
		public ActionDispelListener()
		{
		}
		
		/**
		 * Method onMagicUse.
		 * @param actor Creature
		 * @param skill Skill
		 * @param target Creature
		 * @param alt boolean
		 * @see lineage2.gameserver.listener.actor.OnMagicUseListener#onMagicUse(Creature, Skill, Creature, boolean)
		 */
		@Override
		public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
		{
			exit();
		}
		
		/**
		 * Method onAttack.
		 * @param actor Creature
		 * @param target Creature
		 * @see lineage2.gameserver.listener.actor.OnAttackListener#onAttack(Creature, Creature)
		 */
		@Override
		public void onAttack(Creature actor, Creature target)
		{
			exit();
		}
	}
	
	private AttackedDispelListener _attackedlistener;
	
	/**
	 * @author Mobius
	 */
	private class AttackedDispelListener implements OnCurrentHpDamageListener
	{
		/**
		 * Constructor for ActionDispelListener.
		 */
		public AttackedDispelListener()
		{
		}
		
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			exit();
		}
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	public boolean checkCondition()
	{
		return true;
	}
	
	/**
	 * Method onStart.
	 */
	protected void onStart()
	{
		getEffected().addStatFuncs(getStatFuncs());
		getEffected().addTriggers(getTemplate());
		
		if (getTemplate()._abnormalEffect != AbnormalEffect.NULL)
		{
			getEffected().startAbnormalEffect(getTemplate()._abnormalEffect);
		}
		else if (getEffectType().getAbnormal() != null)
		{
			getEffected().startAbnormalEffect(getEffectType().getAbnormal());
		}
		
		if (getTemplate()._abnormalEffect2 != AbnormalEffect.NULL)
		{
			getEffected().startAbnormalEffect(getTemplate()._abnormalEffect2);
		}
		
		if (getTemplate()._abnormalEffect3 != AbnormalEffect.NULL)
		{
			getEffected().startAbnormalEffect(getTemplate()._abnormalEffect3);
		}
		
		if (_template._cancelOnAction)
		{
			getEffected().addListener(_actionlistener = new ActionDispelListener());
		}
		
		if (_template._cancelOnAttacked)
		{
			getEffected().addListener(_attackedlistener = new AttackedDispelListener());
		}
		
		if (getEffected().isPlayer() && !getSkill().canUseTeleport())
		{
			getEffected().getPlayer().getPlayerAccess().UseTeleport = false;
		}
		
		getEffected().broadcastEffectsStatusToListeners();
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	protected abstract boolean onActionTime();
	
	/**
	 * Method onExit.
	 */
	protected void onExit()
	{
		getEffected().removeStatsOwner(this);
		getEffected().removeTriggers(getTemplate());
		
		if (getTemplate()._abnormalEffect != AbnormalEffect.NULL)
		{
			getEffected().stopAbnormalEffect(getTemplate()._abnormalEffect);
		}
		else if (getEffectType().getAbnormal() != null)
		{
			getEffected().stopAbnormalEffect(getEffectType().getAbnormal());
		}
		
		if (getTemplate()._abnormalEffect2 != AbnormalEffect.NULL)
		{
			getEffected().stopAbnormalEffect(getTemplate()._abnormalEffect2);
		}
		
		if (getTemplate()._abnormalEffect3 != AbnormalEffect.NULL)
		{
			getEffected().stopAbnormalEffect(getTemplate()._abnormalEffect3);
		}
		
		if (_template._cancelOnAction)
		{
			getEffected().removeListener(_actionlistener);
		}
		
		if (_template._cancelOnAttacked)
		{
			getEffected().removeListener(_attackedlistener);
		}
		
		if (getEffected().isPlayer() && getStackType().contains(EffectTemplate.HP_RECOVER_CAST))
		{
			getEffected().sendPacket(new ShortBuffStatusUpdate());
		}
		
		if (getEffected().isPlayer() && !getSkill().canUseTeleport() && !getEffected().getPlayer().getPlayerAccess().UseTeleport)
		{
			getEffected().getPlayer().getPlayerAccess().UseTeleport = true;
		}
		
		if (getEffected().isPlayer() && (getEffected().getPlayer().getSummonList() != null))
		{
			for (Summon sm : getEffected().getPlayer().getSummonList())
			{
				if (sm.getEffectList().getEffectByType(EffectType.ServitorShare) != null)
				{
					sm.getEffectList().stopEffect(getSkill());
				}
			}
		}
		
		getEffected().broadcastEffectsStatusToListeners();
	}
	
	/**
	 * Method stopEffectTask.
	 */
	private void stopEffectTask()
	{
		if (_effectTask != null)
		{
			_effectTask.cancel(false);
		}
	}
	
	/**
	 * Method startEffectTask.
	 */
	private void startEffectTask()
	{
		if (_effectTask == null)
		{
			_startTimeMillis = System.currentTimeMillis();
			_effectTask = EffectTaskManager.getInstance().scheduleAtFixedRate(this, _period, _period);
		}
	}
	
	/**
	 * Method schedule.
	 */
	final void schedule()
	{
		Creature effected = getEffected();
		
		if (effected == null)
		{
			return;
		}
		
		if (!checkCondition())
		{
			return;
		}
		
		getEffected().getEffectList().addEffect(this);
	}
	
	/**
	 * Method suspend.
	 */
	private final void suspend()
	{
		if (setState(STARTING, SUSPENDED))
		{
			startEffectTask();
		}
		else if (setState(STARTED, SUSPENDED) || setState(ACTING, SUSPENDED))
		{
			synchronized (this)
			{
				if (isInUse())
				{
					setInUse(false);
					setActive(false);
					onExit();
				}
			}
			getEffected().getEffectList().removeEffect(this);
		}
	}
	
	/**
	 * Method start.
	 */
	final void start()
	{
		if (setState(STARTING, STARTED))
		{
			synchronized (this)
			{
				if (isInUse())
				{
					setActive(true);
					onStart();
					startEffectTask();
				}
			}
		}
		
		run();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public final void runImpl()
	{
		if (setState(STARTED, ACTING))
		{
			if (!getSkill().isHideStartMessage() && (getEffected().getEffectList().getEffectsCountForSkill(getSkill().getId()) == 1))
			{
				getEffected().sendPacket(new SystemMessage(SystemMessage.S1_S2S_EFFECT_CAN_BE_FELT).addSkillName(_displayId, _displayLevel));
			}
			
			return;
		}
		
		if (getState() == SUSPENDED)
		{
			if (isTimeLeft())
			{
				_count--;
				
				if (isTimeLeft())
				{
					return;
				}
			}
			
			exit();
			return;
		}
		
		if (getState() == ACTING)
		{
			if (isTimeLeft())
			{
				_count--;
				
				if ((!isActive() || onActionTime()) && isTimeLeft())
				{
					return;
				}
			}
		}
		
		if (setState(ACTING, FINISHING))
		{
			setInUse(false);
		}
		
		if (setState(FINISHING, FINISHED))
		{
			synchronized (this)
			{
				setActive(false);
				stopEffectTask();
				onExit();
			}
			Effect next = getNext();
			
			if (next != null)
			{
				if (next.setState(SUSPENDED, STARTING))
				{
					next.schedule();
				}
			}
			
			if (getSkill().getDelayedEffect() > 0)
			{
				Skill delayErrects = SkillTable.getInstance().getInfo(getSkill().getDelayedEffect(), 1);
				if (delayErrects != null)
				{
					delayErrects.getEffects(_effector, _effected, false, false);
				}
			}
			
			boolean msg = !isHidden() && (getEffected().getEffectList().getEffectsCountForSkill(getSkill().getId()) == 1);
			getEffected().getEffectList().removeEffect(this);
			
			if (msg)
			{
				getEffected().sendPacket(new SystemMessage(SystemMessage.S1_HAS_WORN_OFF).addSkillName(_displayId, _displayLevel));
			}
		}
	}
	
	/**
	 * Method exit.
	 */
	public final void exit()
	{
		Effect next = getNext();
		
		if (next != null)
		{
			next.exit();
		}
		
		removeNext();
		
		if (setState(STARTING, FINISHED))
		{
			getEffected().getEffectList().removeEffect(this);
		}
		else if (setState(SUSPENDED, FINISHED))
		{
			stopEffectTask();
		}
		else if (setState(STARTED, FINISHED) || setState(ACTING, FINISHED))
		{
			synchronized (this)
			{
				if (isInUse())
				{
					setInUse(false);
					setActive(false);
					stopEffectTask();
					onExit();
				}
			}
			getEffected().getEffectList().removeEffect(this);
		}
	}
	
	/**
	 * Method scheduleNext.
	 * @param e Effect
	 * @return boolean
	 */
	private boolean scheduleNext(Effect e)
	{
		if ((e == null) || e.isEnded())
		{
			return false;
		}
		
		Effect next = getNext();
		
		if ((next != null) && !next.maybeScheduleNext(e))
		{
			return false;
		}
		
		_next = e;
		return true;
	}
	
	/**
	 * Method getNext.
	 * @return Effect
	 */
	public Effect getNext()
	{
		return _next;
	}
	
	/**
	 * Method removeNext.
	 */
	private void removeNext()
	{
		_next = null;
	}
	
	/**
	 * Method maybeScheduleNext.
	 * @param newEffect Effect
	 * @return boolean
	 */
	boolean maybeScheduleNext(Effect newEffect)
	{
		if (newEffect.getStackOrder() < getStackOrder())
		{
			if (newEffect.getTimeLeft() > getTimeLeft())
			{
				newEffect.suspend();
				scheduleNext(newEffect);
			}
			
			return false;
		}
		else if (newEffect.getTimeLeft() >= getTimeLeft())
		{
			if ((getNext() != null) && (getNext().getTimeLeft() > newEffect.getTimeLeft()))
			{
				newEffect.scheduleNext(getNext());
				removeNext();
			}
			
			exit();
		}
		else
		{
			suspend();
			newEffect.scheduleNext(this);
		}
		
		return true;
	}
	
	/**
	 * Method getStatFuncs.
	 * @return Func[]
	 */
	public Func[] getStatFuncs()
	{
		return getTemplate().getStatFuncs(this);
	}
	
	/**
	 * Method addIcon.
	 * @param ps IconEffectPacket
	 */
	public void addIcon(IconEffectPacket ps)
	{
		if (!isActive() || isHidden())
		{
			return;
		}
		
		int duration = _skill.isToggle() ? AbnormalStatusUpdate.INFINITIVE_EFFECT : getTimeLeft();
		ps.addIconEffect(_displayId, _displayLevel, duration, getEffectorObj());
	}
	
	/**
	 * Method addOlympiadSpelledIcon.
	 * @param player Player
	 * @param os ExOlympiadSpelledInfo
	 */
	void addOlympiadSpelledIcon(Player player, ExOlympiadSpelledInfo os)
	{
		if (!isActive() || isHidden())
		{
			return;
		}
		
		int duration = _skill.isToggle() ? AbnormalStatusUpdate.INFINITIVE_EFFECT : getTimeLeft();
		os.addSpellRecivedPlayer(player);
		os.addEffect(_displayId, _displayLevel, duration);
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	protected int getLevel()
	{
		return _skill.getLevel();
	}
	
	/**
	 * Method getEffectType.
	 * @return EffectType
	 */
	public EffectType getEffectType()
	{
		return getTemplate()._effectType;
	}
	
	/**
	 * Method isHidden.
	 * @return boolean
	 */
	public boolean isHidden()
	{
		return _displayId < 0;
	}
	
	/**
	 * Method compareTo.
	 * @param obj Effect
	 * @return int
	 */
	@Override
	public int compareTo(Effect obj)
	{
		if (obj.equals(this))
		{
			return 0;
		}
		
		return 1;
	}
	
	/**
	 * Method isSaveable.
	 * @return boolean
	 */
	public boolean isSaveable()
	{
		return _template.isSaveable(getSkill().isSaveable()) && (getTimeLeft() >= Config.ALT_SAVE_EFFECTS_REMAINING_TIME);
	}
	
	/**
	 * Method getDisplayId.
	 * @return int
	 */
	public int getDisplayId()
	{
		return _displayId;
	}
	
	/**
	 * Method getDisplayLevel.
	 * @return int
	 */
	public int getDisplayLevel()
	{
		return _displayLevel;
	}
	
	/**
	 * Method isCancelable.
	 * @return boolean
	 */
	public boolean isCancelable()
	{
		return _template.isCancelable(getSkill().isCancelable());
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "Skill: " + _skill + ", state: " + getState() + ", inUse: " + _inUse + ", active : " + _active;
	}
	
	/**
	 * Method isFuncEnabled.
	 * @return boolean
	 * @see lineage2.gameserver.stats.funcs.FuncOwner#isFuncEnabled()
	 */
	@Override
	public boolean isFuncEnabled()
	{
		return isInUse();
	}
	
	/**
	 * Method overrideLimits.
	 * @return boolean
	 * @see lineage2.gameserver.stats.funcs.FuncOwner#overrideLimits()
	 */
	@Override
	public boolean overrideLimits()
	{
		return false;
	}
	
	/**
	 * Method isOffensive.
	 * @return boolean
	 */
	public boolean isOffensive()
	{
		return _template.isOffensive(getSkill().isOffensive());
	}
}
