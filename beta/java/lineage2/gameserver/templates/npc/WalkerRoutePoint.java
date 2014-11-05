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

import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

public class WalkerRoutePoint
{
	private final Location _loc;
	private final NpcString _phrase;
	private final int _socialActionId;
	private final int _delay;
	private final boolean _running;
	
	public WalkerRoutePoint(Location loc, NpcString phrase, int socialActionId, int delay, boolean running)
	{
		_loc = loc;
		_phrase = phrase;
		_socialActionId = socialActionId;
		_delay = delay;
		_running = running;
	}
	
	public Location getLocation()
	{
		return _loc;
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
	
	public boolean isRunning()
	{
		return _running;
	}
}