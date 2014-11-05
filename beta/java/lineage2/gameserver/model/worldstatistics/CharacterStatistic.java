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
package lineage2.gameserver.model.worldstatistics;

/**
 * @author Дмитрий
 * @date 10.10.12 20:51
 */
public class CharacterStatistic
{
	private final int objId;
	private final String name;
	private final CharacterStatisticElement statisticElement;
	private final int clanObjId = 0;
	private boolean clanCrestId;
	
	public CharacterStatistic(int objId, String name, CharacterStatisticElement statisticElement)
	{
		this.objId = objId;
		this.name = name;
		this.statisticElement = statisticElement;
	}
	
	public int getObjId()
	{
		return objId;
	}
	
	public String getName()
	{
		return name;
	}
	
	public long getValue()
	{
		return statisticElement.getValue();
	}
	
	public int getClanObjId()
	{
		return clanObjId;
	}
	
	public boolean getClanCrestId()
	{
		return clanCrestId;
	}
}
