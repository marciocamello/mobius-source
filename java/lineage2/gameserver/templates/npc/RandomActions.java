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
package lineage2.gameserver.templates.npc;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import gnu.trove.map.hash.TIntObjectHashMap;

public class RandomActions
{
	private final TIntObjectHashMap<Action> _actions;
	private final boolean _randomOrder;
	
	public RandomActions(boolean randomOrder)
	{
		_actions = new TIntObjectHashMap<>();
		_randomOrder = randomOrder;
	}
	
	public void addAction(Action action)
	{
		_actions.put(action.getId(), action);
	}
	
	public Action getAction(int id)
	{
		if (_randomOrder)
		{
			Action[] actionsArr = _actions.values(new Action[_actions.size()]);
			return actionsArr[Rnd.get(actionsArr.length)];
		}
		return _actions.get(id);
	}
	
	public int getActionsCount()
	{
		return _actions.size();
	}
	
	public static class Action
	{
		private final int _id;
		private final NpcString _phrase;
		private final int _socialActionId;
		private final int _delay;
		
		public Action(int id, NpcString phrase, int socialActionId, int delay)
		{
			_id = id;
			_phrase = phrase;
			_socialActionId = socialActionId;
			_delay = delay;
		}
		
		public int getId()
		{
			return _id;
		}
		
		public NpcString getPhrase()
		{
			return _phrase;
		}
		
		public int getSocialActionId()
		{
			return _socialActionId;
		}
		
		public int getDelay()
		{
			return _delay;
		}
	}
}