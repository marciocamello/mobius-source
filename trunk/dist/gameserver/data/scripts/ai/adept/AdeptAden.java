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
package ai.adept;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class AdeptAden extends Adept
{
	/**
	 * Constructor for AdeptAden.
	 * @param actor NpcInstance
	 */
	public AdeptAden(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(146363, 24149, -2008),
			new Location(146360, 25672, -2008),
			new Location(148552, 25688, -2008),
			new Location(146360, 25672, -2008)
		};
	}
}
