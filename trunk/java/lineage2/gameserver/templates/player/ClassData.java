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
package lineage2.gameserver.templates.player;

import gnu.trove.map.hash.TIntObjectHashMap;

public class ClassData
{
	private final int _classId;
	private final TIntObjectHashMap<LvlUpData> _lvlsUpData = new TIntObjectHashMap<>();
	
	public ClassData(int classId)
	{
		_classId = classId;
	}
	
	public void addLvlUpData(int lvl, double hp, double mp, double cp)
	{
		_lvlsUpData.put(lvl, new LvlUpData(hp, mp, cp));
	}
	
	public LvlUpData getLvlUpData(int lvl)
	{
		return _lvlsUpData.get(lvl);
	}
	
	public int getClassId()
	{
		return _classId;
	}
}
