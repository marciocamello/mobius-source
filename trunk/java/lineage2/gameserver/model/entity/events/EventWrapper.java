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
package lineage2.gameserver.model.entity.events;

import lineage2.gameserver.taskmanager.actionrunner.ActionWrapper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
class EventWrapper extends ActionWrapper
{
	private final GlobalEvent _event;
	private final int _time;
	
	/**
	 * Constructor for EventWrapper.
	 * @param name String
	 * @param event GlobalEvent
	 * @param time int
	 */
	EventWrapper(String name, GlobalEvent event, int time)
	{
		super(name);
		_event = event;
		_time = time;
	}
	
	/**
	 * Method runImpl0.
	 */
	@Override
	public void runImpl0()
	{
		_event.timeActions(_time);
	}
}
