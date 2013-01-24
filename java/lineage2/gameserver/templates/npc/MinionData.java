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

public class MinionData
{
	private final int _minionId;
	private final int _minionAmount;
	
	public MinionData(int minionId, int minionAmount)
	{
		_minionId = minionId;
		_minionAmount = minionAmount;
	}
	
	public int getMinionId()
	{
		return _minionId;
	}
	
	public int getAmount()
	{
		return _minionAmount;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != this.getClass())
		{
			return false;
		}
		return ((MinionData) o).getMinionId() == getMinionId();
	}
}
