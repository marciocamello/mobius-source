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
package ai.octavis;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

public final class OctavisRider extends Pointer
{
	public OctavisRider(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 250;
		_points = new Location[]
		{
			// new Location(181911, 114835, -7678),
			// new Location(182824, 114808, -7906),
			// new Location(182536, 115224, -7836),
			// new Location(182104, 115160, -7735),
			// new Location(181480, 114936, -7702)
			new Location(207704, 120792, -10038),
			new Location(207416, 121080, -10038),
			new Location(207016, 121080, -10038),
			new Location(206696, 120776, -10038),
			new Location(206696, 120360, -10038),
			new Location(206984, 120088, -10038),
			new Location(207400, 120072, -10038),
			new Location(207672, 120360, -10038)
		};
	}
}