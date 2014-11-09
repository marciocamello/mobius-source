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
public class CharacterMasksUI3
{
	public static int NONE = 0;
	public static int UNDEFINED = 1;
	public static int UNKNOWN_1 = 2;
	public static int INVENTORY_SLOTS = 4;
	public static int NAME_TITLE_COLOR = 8;
	public static int MOVEMENT_TYPES = 16;
	public static int TALISMANS = 32;
	public static int VITALITY = 64;
	public static int STATS = 128;
	
	public static int ALL = /* UNDEFINED | */
	UNKNOWN_1 | INVENTORY_SLOTS | NAME_TITLE_COLOR | MOVEMENT_TYPES | TALISMANS | VITALITY | STATS;
}
