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
package lineage2.gameserver.templates.player;

public class StatAttributes
{
	private int _int = 0;
	private int _str = 0;
	private int _con = 0;
	private int _men = 0;
	private int _dex = 0;
	private int _wit = 0;
	
	public StatAttributes(int _int, int str, int con, int men, int dex, int wit)
	{
		this._int = _int;
		_str = str;
		_con = con;
		_men = men;
		_dex = dex;
		_wit = wit;
	}
	
	public int getINT()
	{
		return _int;
	}
	
	public int getSTR()
	{
		return _str;
	}
	
	public int getCON()
	{
		return _con;
	}
	
	public int getMEN()
	{
		return _men;
	}
	
	public int getDEX()
	{
		return _dex;
	}
	
	public int getWIT()
	{
		return _wit;
	}
}
