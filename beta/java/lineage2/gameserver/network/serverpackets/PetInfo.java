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
package lineage2.gameserver.network.serverpackets;

import java.util.List;

import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.utils.Location;

public class PetInfo extends L2GameServerPacket
{
	private final int _runSpd;
	private final int _walkSpd;
	private final int _mAtkSpd;
	private final int _pAtkSpd;
	private final int _pvpFlag;
	private final int _karma;
	private final double _AttackSpeedMultiplier;
	private final double _MovementSpeedMultiplier;
	private int _rideable;
	private final int _type;
	private final int _objId;
	private final int _npcId;
	private final int _sp;
	private final int _level;
	private final int _curFed;
	private final int _maxFed;
	private final int _curHp;
	private final int _maxHp;
	private final int _curMp;
	private final int _maxMp;
	private final int _curLoad;
	private final int _maxLoad;
	private final int _pAtk;
	private final int _pDef;
	private final int _mAtk;
	private final int _mDef;
	private final int _accuracy;
	private final int _evasion;
	private final int _critical;
	private final int _sps;
	private final int _ss;
	private final int _formId;
	private int _petStatus;
	private final Location _loc;
	private final double _colRadius;
	private final double _colHeight;
	private final long _exp;
	private final long _expThisLvl;
	private final long _expNextLvl;
	private final String _name;
	private final String _title;
	private final TeamType _team;
	private final int _sumPoint;
	private final int _maxSumPoint;
	private final List<Integer> _aveList;
	private final int _mevasion;
	private final int _maccuracy;
	private final int _mCritRate;
	
	public PetInfo(Summon summon)
	{
		_type = summon.getSummonType();
		_objId = summon.getObjectId();
		_npcId = summon.getTemplate().getId();
		_loc = summon.getLoc();
		_mAtkSpd = summon.getMAtkSpd();
		_pAtkSpd = summon.getPAtkSpd();
		_runSpd = summon.getRunSpeed();
		_walkSpd = summon.getWalkSpeed();
		_colRadius = summon.getColRadius();
		_colHeight = summon.getColHeight();
		_name = summon.getName().equalsIgnoreCase(summon.getTemplate().name) ? "" : summon.getName();
		_title = summon.getTitle();
		_pvpFlag = summon.getPvpFlag();
		_karma = summon.getKarma();
		_curFed = summon.getCurrentFed();
		_maxFed = summon.getMaxFed();
		_curHp = (int) summon.getCurrentHp();
		_maxHp = summon.getMaxHp();
		_curMp = (int) summon.getCurrentMp();
		_maxMp = summon.getMaxMp();
		_sp = summon.getSp();
		_level = summon.getLevel();
		_exp = summon.getExp();
		_expThisLvl = summon.getExpForThisLevel();
		_expNextLvl = summon.getExpForNextLevel();
		_curLoad = summon.isPet() ? summon.getInventory().getTotalWeight() : 0;
		_maxLoad = summon.getMaxLoad();
		_pAtk = summon.getPAtk(null);
		_pDef = summon.getPDef(null);
		_mAtk = summon.getMAtk(null, null);
		_mDef = summon.getMDef(null, null);
		_accuracy = summon.getAccuracy();
		_evasion = summon.getEvasionRate(null);
		_critical = summon.getCriticalHit(null, null);
		
		if (summon.getPlayer().getTransformation() != 0)
		{
			_rideable = 0; // not rideable
		}
		else
		{
			_rideable = PetDataTable.isMountable(_npcId) ? 1 : 0;
		}
		
		_team = summon.getTeam();
		_ss = summon.getSoulshotConsumeCount();
		_sps = summon.getSpiritshotConsumeCount();
		_petStatus = summon.getSpawnAnimation();
		_formId = summon.getFormId();
		_sumPoint = summon.getPlayer().getSummonList().getUsedPoints();
		_maxSumPoint = summon.getPlayer().getSummonPointMax();
		_aveList = summon.getAveList();
		_mevasion = summon.getMEvasionRate(null);
		_maccuracy = summon.getMAccuracy();
		_mCritRate = (int) summon.getMagicCriticalRate(null, null);
		_AttackSpeedMultiplier = summon.getAttackSpeedMultiplier();
		_MovementSpeedMultiplier = summon.getMovementSpeedMultiplier();
	}
	
	public PetInfo update()
	{
		_petStatus = 1;
		return this;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb2);
		writeC(_type);
		writeD(_objId);
		writeD(_npcId + 1000000);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ());
		writeD(_loc.getHeading());
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		
		writeH(_runSpd);
		writeH(_walkSpd);
		writeH(_runSpd/* _swimRunSpd */);
		writeH(_walkSpd/* _swimWalkSpd */);
		writeH(_runSpd/* _flRunSpd */);
		writeH(_walkSpd/* _flWalkSpd */);
		writeH(_runSpd/* _flyRunSpd */);
		writeH(_walkSpd/* _flyWalkSpd */);
		writeF(_MovementSpeedMultiplier);
		writeF(_AttackSpeedMultiplier);
		writeF(_colRadius);
		writeF(_colHeight);
		
		writeD(0); // main weapon
		writeD(0); // armor
		writeD(0); // shield weapon
		writeC(_petStatus); // 0=inactive 1=active 2=summoned (only works if model has a summon animation)
		writeD(-1);
		writeS(_name);
		writeD(-1);
		writeS(_title);
		writeC(_pvpFlag); // 0=white, 1=purple, 2=purpleblink, if its greater then karma = purple
		writeD(_karma); // hmm karma ??
		writeD(_curFed); // how fed it is
		writeD(_maxFed); // max fed it can be
		writeD(_curHp); // current hp
		writeD(_maxHp); // max hp
		writeD(_curMp); // current mp
		writeD(_maxMp); // max mp
		writeQ(_sp); // sp
		writeC(_level);// lvl
		writeQ(_exp);
		writeQ(_expThisLvl); // 0% absolute value
		writeQ(_expNextLvl); // 100% absoulte value
		writeD(_curLoad); // weight
		writeD(_maxLoad); // max weight it can carry
		writeD(_pAtk);// patk
		writeD(_pDef);// pdef
		writeD(_accuracy);// accuracy
		writeD(_evasion);// evasion
		writeD(_critical);// critical
		writeD(_mAtk);// matk
		writeD(_mDef);// mdef
		writeD(_mevasion);
		writeD(_maccuracy);
		writeD(_mCritRate);
		writeD(_runSpd);// speed
		writeD(_pAtkSpd);// atkspeed
		writeD(_mAtkSpd);// casting speed
		writeC(_rideable);
		writeC(_team.ordinal()); // team aura (1 = blue, 2 = red)
		writeC(_ss);
		writeC(_sps);
		writeD(_formId); // evolution type
		writeD(0x00); // evolutionLevel
		writeC(_sumPoint);
		writeC(_maxSumPoint);
		
		if (_aveList != null)
		{
			writeH(_aveList.size());
			
			for (int i : _aveList)
			{
				writeH(i);
			}
		}
		else
		{
			writeH(0x00);
		}
		
		writeC(0);
	}
}