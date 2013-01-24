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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.EffectList;
import lineage2.gameserver.skills.AbnormalEffect;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.StatTemplate;
import lineage2.gameserver.stats.conditions.Condition;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EffectTemplate extends StatTemplate
{
	private static final Logger _log = LoggerFactory.getLogger(EffectTemplate.class);
	public static final EffectTemplate[] EMPTY_ARRAY = new EffectTemplate[0];
	public static final String NO_STACK = "none".intern();
	public static final String HP_RECOVER_CAST = "HpRecoverCast".intern();
	public Condition _attachCond;
	public final double _value;
	public final int _count;
	public final long _period;
	public AbnormalEffect _abnormalEffect;
	public AbnormalEffect _abnormalEffect2;
	public AbnormalEffect _abnormalEffect3;
	public final EffectType _effectType;
	public List<String> _stackTypes = new ArrayList<>();
	public final int _stackOrder;
	public final int _displayId;
	public final int _displayLevel;
	public final boolean _applyOnCaster;
	public final boolean _applyOnSummon;
	public final boolean _cancelOnAction;
	public final boolean _isReflectable;
	private final Boolean _isSaveable;
	private final Boolean _isCancelable;
	private final Boolean _isOffensive;
	protected final int _skillToCast;
	protected final int _skillToCastLevel;
	private final StatsSet _paramSet;
	private final int _chance;
	
	public EffectTemplate(StatsSet set)
	{
		_value = set.getDouble("value");
		_count = set.getInteger("count", 1) < 0 ? Integer.MAX_VALUE : set.getInteger("count", 1);
		_period = Math.min(Integer.MAX_VALUE, 1000 * (set.getInteger("time", 1) < 0 ? Integer.MAX_VALUE : set.getInteger("time", 1)));
		_abnormalEffect = set.getEnum("abnormal", AbnormalEffect.class);
		_abnormalEffect2 = set.getEnum("abnormal2", AbnormalEffect.class);
		_abnormalEffect3 = set.getEnum("abnormal3", AbnormalEffect.class);
		String args[] = set.getString("stackType", NO_STACK).split(";", -1);
		for (String arg : args)
		{
			_stackTypes.add(arg);
		}
		_stackOrder = set.getInteger("stackOrder", _stackTypes.contains(NO_STACK) ? 1 : 0);
		_applyOnCaster = set.getBool("applyOnCaster", false);
		_applyOnSummon = set.getBool("applyOnSummon", true);
		_cancelOnAction = set.getBool("cancelOnAction", false);
		_isReflectable = set.getBool("isReflectable", true);
		_isSaveable = set.isSet("isSaveable") ? set.getBool("isSaveable") : null;
		_isCancelable = set.isSet("isCancelable") ? set.getBool("isCancelable") : null;
		_isOffensive = set.isSet("isOffensive") ? set.getBool("isOffensive") : null;
		_displayId = set.getInteger("displayId", 0);
		_displayLevel = set.getInteger("displayLevel", 0);
		_effectType = set.getEnum("name", EffectType.class);
		_chance = set.getInteger("chance", Integer.MAX_VALUE);
		_skillToCast = set.getInteger("skillToCast", 0);
		_skillToCastLevel = set.getInteger("skillToCastLevel", 0);
		_paramSet = set;
	}
	
	public Effect getEffect(Env env)
	{
		if ((_attachCond != null) && !_attachCond.test(env))
		{
			return null;
		}
		try
		{
			return _effectType.makeEffect(env, this);
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		return null;
	}
	
	public void attachCond(Condition c)
	{
		_attachCond = c;
	}
	
	public int getCount()
	{
		return _count;
	}
	
	public long getPeriod()
	{
		return _period;
	}
	
	public EffectType getEffectType()
	{
		return _effectType;
	}
	
	public Effect getSameByStackType(List<Effect> list)
	{
		for (Effect ef : list)
		{
			if ((ef != null) && EffectList.checkStackType(ef.getTemplate(), this))
			{
				return ef;
			}
		}
		return null;
	}
	
	public Effect getSameByStackType(EffectList list)
	{
		return getSameByStackType(list.getAllEffects());
	}
	
	public Effect getSameByStackType(Creature actor)
	{
		return getSameByStackType(actor.getEffectList().getAllEffects());
	}
	
	public StatsSet getParam()
	{
		return _paramSet;
	}
	
	public int chance(int val)
	{
		return _chance == Integer.MAX_VALUE ? val : _chance;
	}
	
	public boolean isSaveable(boolean def)
	{
		return _isSaveable != null ? _isSaveable.booleanValue() : def;
	}
	
	public boolean isCancelable(boolean def)
	{
		return _isCancelable != null ? _isCancelable.booleanValue() : def;
	}
	
	public boolean isOffensive(boolean def)
	{
		return _isOffensive != null ? _isOffensive.booleanValue() : def;
	}
}
