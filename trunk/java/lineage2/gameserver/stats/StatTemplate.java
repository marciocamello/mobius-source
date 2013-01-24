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
package lineage2.gameserver.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.stats.triggers.TriggerInfo;

public class StatTemplate
{
	protected FuncTemplate[] _funcTemplates = FuncTemplate.EMPTY_ARRAY;
	protected List<TriggerInfo> _triggerList = Collections.emptyList();
	
	public List<TriggerInfo> getTriggerList()
	{
		return _triggerList;
	}
	
	public void addTrigger(TriggerInfo f)
	{
		if (_triggerList.isEmpty())
		{
			_triggerList = new ArrayList<>(4);
		}
		_triggerList.add(f);
	}
	
	public void attachFunc(FuncTemplate f)
	{
		_funcTemplates = ArrayUtils.add(_funcTemplates, f);
	}
	
	public FuncTemplate[] getAttachedFuncs()
	{
		return _funcTemplates;
	}
	
	public Func[] getStatFuncs(Object owner)
	{
		if (_funcTemplates.length == 0)
		{
			return Func.EMPTY_FUNC_ARRAY;
		}
		Func[] funcs = new Func[_funcTemplates.length];
		for (int i = 0; i < funcs.length; i++)
		{
			funcs[i] = _funcTemplates[i].getFunc(owner);
		}
		return funcs;
	}
}
