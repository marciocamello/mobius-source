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
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.utils.Location;

public class PetInfo extends L2GameServerPacket
{
	private final int _runSpd, _walkSpd, MAtkSpd, PAtkSpd, pvp_flag, karma;
	private int rideable;
	private final int _type, obj_id, npc_id, runing, incombat, dead, _sp, level, _abnormalEffect, _abnormalEffect2;
	private final int curFed, maxFed, curHp, maxHp, curMp, maxMp, curLoad, maxLoad;
	private final int PAtk, PDef, MAtk, MDef, Accuracy, Evasion, Crit, sps, ss, type;
	private int _showSpawnAnimation;
	private final int _mAccuracy;
	private final int _mEvasion;
	private final int _mCrit;
	private final Location _loc;
	private final double col_radius, col_height;
	private final long exp, exp_this_lvl, exp_next_lvl;
	private final String _name, title;
	private final TeamType _team;
	private final int _ownerId;
	private final int summonPoint, summonPointsMax;
	
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
		_abnormalEffect = summon.getAbnormalEffect();
		_abnormalEffect2 = summon.getAbnormalEffect2();
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
	}
	
	public PetInfo update()
	{
		_showSpawnAnimation = 1;
		return this;
	}
	
	@Override
	protected final void writeImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (!activeChar.isTautiClient())
		{
			writeC(0xB2);
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
			writeC(1);
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
			writeD(MAtk);
			writeD(MDef);
			writeD(_mEvasion);
			writeD(_mAccuracy);
			writeD(_mCrit);
			writeD(Accuracy);
			writeD(Evasion);
			writeD(Crit);
			writeD(_runSpd);
			writeD(PAtkSpd);
			writeD(MAtkSpd);
			writeD(_abnormalEffect);
			writeD(rideable);
			writeC(0);
			writeC(_team.ordinal());
			writeD(ss);
			writeD(sps);
			writeD(type);
			writeD(_abnormalEffect2);
			writeD(0x00);
			writeD(summonPoint);
			writeD(summonPointsMax);
		}
		else
		{
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
			writeD(rideable);
			writeC(0);
			writeC(_team.ordinal());
			writeD(ss);
			writeD(sps);
			writeD(type);
			writeD(0x00);
			writeD(summonPoint);
			writeD(summonPointsMax);
			writeD(0x00);
		}
	}
}
