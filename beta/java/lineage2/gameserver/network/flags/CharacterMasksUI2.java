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
package lineage2.gameserver.network.flags;

/**
 * @author Michael
 */
public class CharacterMasksUI2
{
	public static int NONE = 0;
	public static int PLEDGE_INFO = 1;
	public static int ELEMENTAL_OFFENSE_STATS = 2;
	public static int OBJECT_BOUNDARIES = 4;
	public static int ANIMATION_SPEED = 8;
	public static int MOVEMENT_SPEED = 16;
	public static int LOCATION = 32;
	public static int ELEMENTAL_DEFENSE_STATS = 64;
	public static int BASE_STATS = 128;
	
	public static int ALL = PLEDGE_INFO | ELEMENTAL_OFFENSE_STATS | OBJECT_BOUNDARIES | ANIMATION_SPEED | MOVEMENT_SPEED | LOCATION | ELEMENTAL_DEFENSE_STATS | BASE_STATS;
}
