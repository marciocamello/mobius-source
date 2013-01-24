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
package lineage2.commons.dao;

public enum JdbcEntityState
{
	CREATED(true, false, false, false),
	STORED(false, true, false, true),
	UPDATED(false, true, true, true),
	DELETED(false, false, false, false);
	private final boolean savable;
	private final boolean deletable;
	private final boolean updatable;
	private final boolean persisted;
	
	JdbcEntityState(boolean savable, boolean deletable, boolean updatable, boolean persisted)
	{
		this.savable = savable;
		this.deletable = deletable;
		this.updatable = updatable;
		this.persisted = persisted;
	}
	
	public boolean isSavable()
	{
		return savable;
	}
	
	public boolean isDeletable()
	{
		return deletable;
	}
	
	public boolean isUpdatable()
	{
		return updatable;
	}
	
	public boolean isPersisted()
	{
		return persisted;
	}
}
