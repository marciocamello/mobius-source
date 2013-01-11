/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.base;

public enum ClassType
{
	FIGHTER,
	MYSTIC,
	PRIEST;
	public static final ClassType[] VALUES = values();
	public static final ClassType[] MAIN_TYPES = getMainTypes();
	
	public static ClassType[] getMainTypes()
	{
		return new ClassType[]
		{
			FIGHTER,
			MYSTIC
		};
	}
	
	public ClassType getMainType()
	{
		if (this == PRIEST)
		{
			return MYSTIC;
		}
		return this;
	}
	
	public boolean isMagician()
	{
		return this != FIGHTER;
	}
}
