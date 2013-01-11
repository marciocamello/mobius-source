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
package lineage2.gameserver.templates;

import gnu.trove.list.array.TIntArrayList;
import lineage2.gameserver.model.Player;

public class Henna
{
	private final int _symbolId;
	private final int _dyeId;
	private final long _price;
	private final long _drawCount;
	private final int _statINT;
	private final int _statSTR;
	private final int _statCON;
	private final int _statMEN;
	private final int _statDEX;
	private final int _statWIT;
	private final int _skillId;
	private final TIntArrayList _classes;
	
	public Henna(int symbolId, int dyeId, long price, long drawCount, int wit, int intA, int con, int str, int dex, int men, int skillId, TIntArrayList classes)
	{
		_symbolId = symbolId;
		_dyeId = dyeId;
		_price = price;
		_drawCount = drawCount;
		_statINT = intA;
		_statSTR = str;
		_statCON = con;
		_statMEN = men;
		_statDEX = dex;
		_statWIT = wit;
		_classes = classes;
		_skillId = skillId;
	}
	
	public int getSymbolId()
	{
		return _symbolId;
	}
	
	public int getDyeId()
	{
		return _dyeId;
	}
	
	public long getPrice()
	{
		return _price;
	}
	
	public int getStatINT()
	{
		return _statINT;
	}
	
	public int getStatSTR()
	{
		return _statSTR;
	}
	
	public int getStatCON()
	{
		return _statCON;
	}
	
	public int getStatMEN()
	{
		return _statMEN;
	}
	
	public int getStatDEX()
	{
		return _statDEX;
	}
	
	public int getStatWIT()
	{
		return _statWIT;
	}
	
	public boolean isForThisClass(Player player)
	{
		return _classes.contains(player.getActiveClassId());
	}
	
	public long getDrawCount()
	{
		return _drawCount;
	}
	
	public int getSkillId()
	{
		return _skillId;
	}
}
