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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetInfo extends L2GameServerPacket
{
	/**
	 * Field karma. Field pvp_flag. Field PAtkSpd. Field MAtkSpd. Field _walkSpd. Field _runSpd.
	 */
	private final int _runSpd, _walkSpd, MAtkSpd, PAtkSpd, pvp_flag, karma;
	/**
	 * Field rideable.
	 */
	private int rideable;
	/**
	 * Field level. Field _sp. Field dead. Field incombat. Field runing. Field npc_id. Field obj_id. Field _type.
	 */
	private final int _type, obj_id, npc_id, runing, incombat, dead, _sp, level;
	/**
	 * Field maxLoad. Field curLoad. Field maxMp. Field curMp. Field maxHp. Field curHp. Field maxFed. Field curFed.
	 */
	private final int curFed, maxFed, curHp, maxHp, curMp, maxMp, curLoad, maxLoad;
	/**
	 * Field type. Field ss. Field sps. Field Crit. Field Evasion. Field Accuracy. Field MDef. Field MAtk. Field PDef. Field PAtk.
	 */
	private final int PAtk, PDef, MAtk, MDef, Accuracy, Evasion, Crit, sps, ss, type;
	/**
	 * Field _showSpawnAnimation.
	 */
	private int _showSpawnAnimation;
	/**
	 * Field _mAccuracy.
	 */
	private final int _mAccuracy;
	/**
	 * Field _mEvasion.
	 */
	private final int _mEvasion;
	/**
	 * Field _mCrit.
	 */
	private final int _mCrit;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field col_height. Field col_radius.
	 */
	private final double col_radius, col_height;
	/**
	 * Field exp_next_lvl. Field exp_this_lvl. Field exp.
	 */
	private final long exp, exp_this_lvl, exp_next_lvl;
	/**
	 * Field title. Field _name.
	 */
	private final String _name, title;
	/**
	 * Field _team.
	 */
	private final TeamType _team;
	/**
	 * Field _ownerId.
	 */
	private final int _ownerId;
	/**
	 * Field summonPointsMax. Field summonPoint.
	 */
	private final int summonPoint, summonPointsMax;
	private final List<Integer> _aveList;
	
	/**
	 * Constructor for PetInfo.
	 * @param summon Summon
	 */
	public PetInfo(Summon summon)
	{
		_type = summon.getSummonType();
		_ownerId = summon.getPlayer().getObjectId();
		obj_id = summon.getObjectId();
		npc_id = summon.getTemplate().npcId;
		_loc = summon.getLoc();
		MAtkSpd = summon.getMAtkSpd();
		PAtkSpd = summon.getPAtkSpd();
		_runSpd = summon.getRunSpeed();
		_walkSpd = summon.getWalkSpeed();
		col_radius = summon.getColRadius();
		col_height = summon.getColHeight();
		runing = summon.isRunning() ? 1 : 0;
		incombat = summon.isInCombat() ? 1 : 0;
		dead = summon.isAlikeDead() ? 1 : 0;
		_name = summon.getName().equalsIgnoreCase(summon.getTemplate().name) ? "" : summon.getName();
		title = summon.getTitle();
		pvp_flag = summon.getPvpFlag();
		karma = summon.getKarma();
		curFed = summon.getCurrentFed();
		maxFed = summon.getMaxFed();
		curHp = (int) summon.getCurrentHp();
		maxHp = summon.getMaxHp();
		curMp = (int) summon.getCurrentMp();
		maxMp = summon.getMaxMp();
		_sp = summon.getSp();
		level = summon.getLevel();
		exp = summon.getExp();
		exp_this_lvl = summon.getExpForThisLevel();
		exp_next_lvl = summon.getExpForNextLevel();
		curLoad = summon.isPet() ? summon.getInventory().getTotalWeight() : 0;
		maxLoad = summon.getMaxLoad();
		PAtk = summon.getPAtk(null);
		PDef = summon.getPDef(null);
		MAtk = summon.getMAtk(null, null);
		MDef = summon.getMDef(null, null);
		Accuracy = summon.getAccuracy();
		Evasion = summon.getEvasionRate(null);
		Crit = summon.getCriticalHit(null, null);
		summon.getAbnormalEffect();
		if (summon.getPlayer().getTransformation() != 0)
		{
			rideable = 0;
		}
		else
		{
			rideable = PetDataTable.isMountable(npc_id) ? 1 : 0;
		}
		_team = summon.getTeam();
		ss = summon.getSoulshotConsumeCount();
		sps = summon.getSpiritshotConsumeCount();
		_showSpawnAnimation = summon.getSpawnAnimation();
		type = summon.getFormId();
		_mEvasion = summon.getMEvasionRate(null);
		_mAccuracy = summon.getMAccuracy();
		_mCrit = (int) summon.getMagicCriticalRate(null, null);
		summonPoint = summon.getPlayer().getSummonList().getUsedPoints();
		summonPointsMax = summon.getPlayer().getSummonPointMax();
		_aveList = summon.getAveList();
	}
	
	/**
	 * Method update.
	 * @return PetInfo
	 */
	public PetInfo update()
	{
		_showSpawnAnimation = 1;
		return this;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		writeC(0xb2);
		writeD(_type);
		writeD(obj_id);
		writeD(npc_id + 1000000);
		writeD(0);
		
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
		writeD(0);
		writeD(MAtkSpd);
		writeD(PAtkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		
		writeF(1);
		writeF(1);
		writeF(col_radius);
		writeF(col_height);
		writeD(0);
		writeD(0);
		writeD(0);
		writeC(_ownerId);
		writeC(runing);
		writeC(incombat);
		writeC(dead);
		writeC(_showSpawnAnimation);
		writeD(-1);
		writeS(_name);
		writeD(-1);
		writeS(title);
		writeD(1);
		writeD(pvp_flag);
		writeD(karma);
		writeD(curFed);
		writeD(maxFed);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
		writeD(_sp);
		writeD(level);
		writeQ(exp);
		writeQ(exp_this_lvl);
		writeQ(exp_next_lvl);
		writeD(curLoad);
		writeD(maxLoad);
		writeD(PAtk);
		writeD(PDef);
		writeD(Accuracy);
		writeD(Evasion);
		writeD(Crit);
		writeD(MAtk);
		writeD(MDef);
		writeD(_mEvasion);
		writeD(_mAccuracy);
		writeD(_mCrit);
		writeD(_runSpd);
		writeD(PAtkSpd);
		writeD(MAtkSpd);
		
		writeH(rideable);
		
		writeC(0);
		writeH(0);
		
		writeC(_team.ordinal());
		writeD(ss);
		writeD(sps);
		writeD(type);
		writeD(0x00);
		writeD(summonPoint);
		writeD(summonPointsMax);
		
		if (_aveList != null)
		{
			writeD(_aveList.size());
			for (int i : _aveList)
			{
				writeD(i);
			}
		}
		else
		{
			writeD(0x00);
		}
	}
}
