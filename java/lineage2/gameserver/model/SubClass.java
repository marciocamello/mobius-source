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
package lineage2.gameserver.model;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.SubClassType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SubClass
{
	public static final int CERTIFICATION_65 = 1 << 0;
	public static final int CERTIFICATION_70 = 1 << 1;
	public static final int CERTIFICATION_75 = 1 << 2;
	public static final int CERTIFICATION_80 = 1 << 3;
	public static final int DUALCERTIFICATION_85 = 1 << 0;
	public static final int DUALCERTIFICATION_90 = 1 << 1;
	public static final int DUALCERTIFICATION_95 = 1 << 2;
	public static final int DUALCERTIFICATION_99 = 1 << 3;
	private int _classId = 0;
	private int _defaultClassId = 0;
	private int _index = 1;
	private boolean _active = false;
	private SubClassType _type = SubClassType.BASE_CLASS;
	private DeathPenalty _dp;
	private int _level = 1;
	private long _exp = 0;
	private int _sp = 0;
	private int _maxLvl = Experience.getMaxLevel();
	private long _minExp = Experience.LEVEL[Config.STARTING_LEVEL];
	private long _maxExp = Experience.LEVEL[_maxLvl + 1] - 1;
	private int _certification;
	private int _dualCertification;
	private double _hp = 1;
	private double _mp = 1;
	private double _cp = 1;
	private double _logOnhp = 1;
	private double _logOnMp = 1;
	private double _logOnCp = 1;
	private int awakingId;
	
	/**
	 * Constructor for SubClass.
	 */
	public SubClass()
	{
	}
	
	/**
	 * Method getClassId.
	 * @return int
	 */
	public int getClassId()
	{
		return _classId;
	}
	
	/**
	 * Method getDefaultClassId.
	 * @return int
	 */
	public int getDefaultClassId()
	{
		return _defaultClassId;
	}
	
	/**
	 * Method getExp.
	 * @return long
	 */
	public long getExp()
	{
		return _exp;
	}
	
	/**
	 * Method getMaxExp.
	 * @return long
	 */
	public long getMaxExp()
	{
		return _maxExp;
	}
	
	/**
	 * Method addExp.
	 * @param val long
	 * @param delevel boolean
	 */
	public void addExp(long val, boolean delevel)
	{
		setExp(_exp + val, delevel);
	}
	
	/**
	 * Method getSp.
	 * @return int
	 */
	public int getSp()
	{
		return _sp;
	}
	
	/**
	 * Method addSp.
	 * @param val long
	 */
	void addSp(long val)
	{
		setSp(_sp + val);
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method setClassId.
	 * @param id int
	 */
	public void setClassId(int id)
	{
		if (_classId == id)
		{
			return;
		}
		
		_classId = id;
		refreshInfo();
	}
	
	/**
	 * Method setDefaultClassId.
	 * @param id int
	 */
	public void setDefaultClassId(int id)
	{
		_defaultClassId = id;
	}
	
	/**
	 * Method setExp.
	 * @param val long
	 * @param delevel
	 */
	public void setExp(long val, boolean delevel)
	{
		if (delevel)
		{
			_exp = Math.min(Math.max(_minExp, val), _maxExp);
		}
		else
		{
			_exp = Math.min(Math.max(Experience.LEVEL[_level], val), _maxExp);
		}
		_level = Experience.getLevel(_exp);
	}
	
	/**
	 * Method setSp.
	 * @param spValue long
	 */
	public void setSp(long spValue)
	{
		_sp = (int) Math.min(Math.max(0, spValue), Integer.MAX_VALUE);
	}
	
	/**
	 * Method setlogOnHp.
	 * @param hpValue double This function are in for controls HP on logon of characters
	 */
	public void setLogonHp(double hpValue)
	{
		_logOnhp = hpValue;
	}
	
	/**
	 * Method getHp.
	 * @return double When the character skills effects are loaded, this value is taken without modification of the table, an applied to the character when all effect are loaded
	 */
	public double getlogOnHp()
	{
		return _logOnhp;
	}
	
	/**
	 * Method setlogOnMp.
	 * @param mpValue
	 */
	public void setLogonMp(double mpValue)
	{
		_logOnMp = mpValue;
	}
	
	/**
	 * Method getLogonMp.
	 * @return double When the character skills effects are loaded, this value is taken without modification of the table, an applied to the character when all effect are loaded
	 */
	public double getlogOnMp()
	{
		return _logOnMp;
	}
	
	/**
	 * Method setlogOnCp.
	 * @param cpValue
	 */
	public void setLogonCp(double cpValue)
	{
		_logOnCp = cpValue;
	}
	
	/**
	 * Method getLogOnCp.
	 * @return double When the character skills effects are loaded, this value is taken without modification of the table, an applied to the character when all effect are loaded
	 */
	public double getlogOnCp()
	{
		return _logOnCp;
	}
	
	/**
	 * Method setHp.
	 * @param hpValue double
	 */
	public void setHp(double hpValue)
	{
		_hp = hpValue;
	}
	
	/**
	 * Method getHp.
	 * @return double
	 */
	public double getHp()
	{
		return _hp;
	}
	
	/**
	 * Method setMp.
	 * @param mpValue double
	 */
	public void setMp(final double mpValue)
	{
		_mp = mpValue;
	}
	
	/**
	 * Method getMp.
	 * @return double
	 */
	public double getMp()
	{
		return _mp;
	}
	
	/**
	 * Method setCp.
	 * @param cpValue double
	 */
	public void setCp(final double cpValue)
	{
		_cp = cpValue;
	}
	
	/**
	 * Method getCp.
	 * @return double
	 */
	public double getCp()
	{
		return _cp;
	}
	
	/**
	 * Method setActive.
	 * @param active boolean
	 */
	public void setActive(final boolean active)
	{
		_active = active;
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	public boolean isActive()
	{
		return _active;
	}
	
	/**
	 * Method setType.
	 * @param type SubClassType
	 */
	public void setType(final SubClassType type)
	{
		if (_type == type)
		{
			return;
		}
		
		_type = type;
		refreshInfo();
	}
	
	/**
	 * Method getType.
	 * @return SubClassType
	 */
	public SubClassType getType()
	{
		return _type;
	}
	
	/**
	 * Method isBase.
	 * @return boolean
	 */
	public boolean isBase()
	{
		return _type == SubClassType.BASE_CLASS;
	}
	
	/**
	 * Method isDouble.
	 * @return boolean
	 */
	public boolean isDual()
	{
		return _type == SubClassType.DOUBLE_SUBCLASS;
	}
	
	/**
	 * Method isSub.
	 * @return boolean
	 */
	public boolean isSub()
	{
		return _type == SubClassType.SUBCLASS;
	}
	
	/**
	 * Method getDeathPenalty.
	 * @param player Player
	 * @return DeathPenalty
	 */
	public DeathPenalty getDeathPenalty(Player player)
	{
		if (_dp == null)
		{
			_dp = new DeathPenalty(player, 0);
		}
		
		return _dp;
	}
	
	/**
	 * Method setDeathPenalty.
	 * @param dp DeathPenalty
	 */
	public void setDeathPenalty(DeathPenalty dp)
	{
		_dp = dp;
	}
	
	/**
	 * Method getCertification.
	 * @return int
	 */
	public int getCertification()
	{
		return _certification;
	}
	
	/**
	 * Method setCertification.
	 * @param certification int
	 */
	public void setCertification(int certification)
	{
		_certification = certification;
	}
	
	/**
	 * Method addCertification.
	 * @param c int
	 */
	public void addCertification(int c)
	{
		_certification |= c;
	}
	
	/**
	 * Method isCertificationGet.
	 * @param v int
	 * @return boolean
	 */
	public boolean isCertificationGet(int v)
	{
		return (_certification & v) == v;
	}
	
	/**
	 * Method getCertification.
	 * @return int
	 */
	public int getDualCertification()
	{
		return _dualCertification;
	}
	
	/**
	 * Method setDualCertification.
	 * @param dualcertification
	 */
	public void setDualCertification(int dualcertification)
	{
		_dualCertification = dualcertification;
	}
	
	/**
	 * Method addDualCertification.
	 * @param c int
	 */
	public void addDualCertification(int c)
	{
		_dualCertification |= c;
	}
	
	/**
	 * Method isDualCertificationGet.
	 * @param v int
	 * @return boolean
	 */
	public boolean isDualCertificationGet(int v)
	{
		return (_dualCertification & v) == v;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return ClassId.VALUES[_classId].toString() + " " + _level;
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public int getMaxLevel()
	{
		return _maxLvl;
	}
	
	/**
	 * Method setIndex.
	 * @param i int
	 */
	public void setIndex(int i)
	{
		_index = i;
	}
	
	/**
	 * Method getIndex.
	 * @return int
	 */
	public int getIndex()
	{
		return _index;
	}
	
	/**
	 * Method refreshInfo.
	 */
	private void refreshInfo()
	{
		if (_type == SubClassType.SUBCLASS)
		{
			_maxLvl = Experience.getMaxSubLevel();
			_minExp = Experience.LEVEL[Config.SUB_START_LEVEL];
			_maxExp = Experience.LEVEL[_maxLvl + 1] - 1;
			_level = Math.min(Math.max(Config.SUB_START_LEVEL, _level), _maxLvl);
		}
		else
		{
			if (ClassId.VALUES[_classId].isOfLevel(ClassLevel.Fourth))
			{
				_maxLvl = Experience.getMaxDualLevel();
			}
			else
			{
				_maxLvl = Experience.getMaxLevel();
			}
			
			_minExp = Experience.LEVEL[Config.STARTING_LEVEL];
			_maxExp = Experience.LEVEL[_maxLvl + 1] - 1;
			_level = Math.min(Math.max(1, _level), _maxLvl);
		}
		
		_exp = Math.min(Math.max(Experience.LEVEL[_level], _exp), _maxExp);
	}
	
	/**
	 * Method getAwakingId.
	 * @return int
	 */
	public int getAwakingId()
	{
		return awakingId;
	}
	
	/**
	 * Method setAwakingId.
	 * @param _Id int
	 */
	public void setAwakingId(int _Id)
	{
		awakingId = _Id;
	}
}
