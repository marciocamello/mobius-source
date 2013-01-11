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
package lineage2.gameserver.model;

public class LvlupData
{
	private int _classid;
	private int _classLvl;
	private double _classHpAdd;
	private double _classHpBase;
	private double _classHpModifier;
	private double _classCpAdd;
	private double _classCpBase;
	private double _classCpModifier;
	private double _classMpAdd;
	private double _classMpBase;
	private double _classMpModifier;
	
	public double get_classHpAdd()
	{
		return _classHpAdd;
	}
	
	public void set_classHpAdd(double hpAdd)
	{
		_classHpAdd = hpAdd;
	}
	
	public double get_classHpBase()
	{
		return _classHpBase;
	}
	
	public void set_classHpBase(double hpBase)
	{
		_classHpBase = hpBase;
	}
	
	public double get_classHpModifier()
	{
		return _classHpModifier;
	}
	
	public void set_classHpModifier(double hpModifier)
	{
		_classHpModifier = hpModifier;
	}
	
	public double get_classCpAdd()
	{
		return _classCpAdd;
	}
	
	public void set_classCpAdd(double cpAdd)
	{
		_classCpAdd = cpAdd;
	}
	
	public double get_classCpBase()
	{
		return _classCpBase;
	}
	
	public void set_classCpBase(double cpBase)
	{
		_classCpBase = cpBase;
	}
	
	public double get_classCpModifier()
	{
		return _classCpModifier;
	}
	
	public void set_classCpModifier(double cpModifier)
	{
		_classCpModifier = cpModifier;
	}
	
	public int get_classid()
	{
		return _classid;
	}
	
	public void set_classid(int _classid)
	{
		this._classid = _classid;
	}
	
	public int get_classLvl()
	{
		return _classLvl;
	}
	
	public void set_classLvl(int lvl)
	{
		_classLvl = lvl;
	}
	
	public double get_classMpAdd()
	{
		return _classMpAdd;
	}
	
	public void set_classMpAdd(double mpAdd)
	{
		_classMpAdd = mpAdd;
	}
	
	public double get_classMpBase()
	{
		return _classMpBase;
	}
	
	public void set_classMpBase(double mpBase)
	{
		_classMpBase = mpBase;
	}
	
	public double get_classMpModifier()
	{
		return _classMpModifier;
	}
	
	public void set_classMpModifier(double mpModifier)
	{
		_classMpModifier = mpModifier;
	}
}
