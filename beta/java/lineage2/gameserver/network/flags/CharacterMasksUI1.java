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
public class CharacterMasksUI1
{
	public static int NONE = 0;
	public static int PERSONAL_STORE = 1;
	public static int FACIAL_FEATURES = 2;
	public static int WEAPON_GLOW = 4;
	public static int CURRENT_STATS = 8;
	public static int MAX_STATS = 16;
	public static int BASE_STATS = 32;
	public static int APPEARANCE = 64;
	public static int ROLES = 128;
	
	public static int ALL = PERSONAL_STORE | FACIAL_FEATURES | WEAPON_GLOW | CURRENT_STATS | MAX_STATS | BASE_STATS | APPEARANCE | ROLES;
}