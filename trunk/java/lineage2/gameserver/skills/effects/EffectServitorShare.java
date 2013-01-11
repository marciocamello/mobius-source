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
package lineage2.gameserver.skills.effects;

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.stats.funcs.FuncTemplate;

public class EffectServitorShare extends Effect
{
	public class FuncShare extends Func
	{
		public FuncShare(Stats stat, int order, Object owner, double value)
		{
			super(stat, order, owner, value);
		}
		
		@Override
		public void calc(Env env)
		{
			env.value += env.character.getPlayer().calcStat(stat, stat.getInit()) * value;
		}
	}
	
	public EffectServitorShare(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public Func[] getStatFuncs()
	{
		FuncTemplate[] funcTemplates = getTemplate().getAttachedFuncs();
		Func[] funcs = new Func[funcTemplates.length];
		for (int i = 0; i < funcs.length; i++)
		{
			funcs[i] = new FuncShare(funcTemplates[i]._stat, funcTemplates[i]._order, this, funcTemplates[i]._value);
		}
		return funcs;
	}
	
	@Override
	protected boolean onActionTime()
	{
		return false;
	}
}
