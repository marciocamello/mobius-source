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
package ai.talkingisland;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 */
public final class OldManAndBoy extends OldManAndBoySubAI
{
	/**
	 * Constructor for OldManAndBoy.
	 * @param actor NpcInstance
	 */
	public OldManAndBoy(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-116649, 255593, -1424), // start point
			new Location(-116646, 256790, -1496),
			new Location(-116450, 257751, -1542),
			new Location(-114925, 257783, -1136),
			new Location(-114380, 257208, -1136),
			new Location(-113908, 257329, -1136), // mid point
			new Location(-114380, 257208, -1136),
			new Location(-114925, 257783, -1136),
			new Location(-116450, 257751, -1542),
			new Location(-116646, 256790, -1496)
		};
	}
}