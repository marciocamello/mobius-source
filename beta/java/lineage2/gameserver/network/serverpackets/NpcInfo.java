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

import java.util.ArrayList;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.StringUtils;

/**
 * ddddddddddddddddddffffdddcccccdSdSddd dddddccffddddccdddddddcddd
 */
public class NpcInfo extends L2GameServerPacket
{
	private boolean _canWriteImpl = false;
	private int _npcObjId;
	private int _npcId;
	private int _running;
	private boolean _incombat;
	private boolean _dead;
	int _showSpawnAnimation;
	int _runSpd;
	int _walkSpd;
	private int _mAtkSpd;
	private int _pAtkSpd;
	private int _rhand;
	private int _lhand;
	int _karma;
	int _pvpFlag;
	int _clanId;
	int _clanCrestId;
	int _allyId;
	int _allyCrestId;
	int _formId;
	int _titleColor;
	private int _HP;
	private int _maxHP;
	int _MP;
	int _maxMP;
	int _CP;
	int _maxCP;
	double _colHeight;
	double _colRadius;
	double _currentColHeight;
	double _currentColRadius;
	private boolean _isAttackable;
	private boolean _isNameAbove;
	private boolean _flying;
	private Location _loc;
	String _name = StringUtils.EMPTY;
	private String _title = StringUtils.EMPTY;
	private boolean _showName;
	private boolean _canTarget;
	int _state;
	NpcString _nameNpcString = NpcString.NONE;
	NpcString _titleNpcString = NpcString.NONE;
	TeamType _team;
	int _transformId;
	private ArrayList<Integer> _aveList;
	boolean _waterZone;
	protected double _moveMultiplier;
	protected double _spdMultiplier;
	
	public NpcInfo(NpcInstance cha, Creature attacker)
	{
		_npcId = cha.getDisplayId() != 0 ? cha.getDisplayId() : cha.getTemplate().getId();
		_isAttackable = (attacker != null) && cha.isAutoAttackable(attacker);
		_rhand = cha.getRightHandItem();
		_lhand = cha.getLeftHandItem();
		
		if (Config.SERVER_SIDE_NPC_NAME || (cha.getTemplate().displayId != 0) || (!cha.getName().equals(cha.getTemplate().name)))
		{
			_name = cha.getName();
		}
		
		if (Config.SERVER_SIDE_NPC_TITLE || (cha.getTemplate().displayId != 0) || (!cha.getTitle().equals(cha.getTemplate().title)))
		{
			_title = cha.getTitle();
		}
		
		if (Config.SHOW_NPC_LVL && cha.isMonster())
		{
			String t = "Lv " + cha.getLevel() + (cha.isAggressive() ? "*" : "");
			if (_title != null)
			{
				t += " " + _title;
			}
			
			_title = t;
		}
		
		_HP = (int) cha.getCurrentHp();
		_MP = (int) cha.getCurrentMp();
		_CP = (int) cha.getCurrentCp();
		_maxHP = cha.getMaxHp();
		_maxMP = cha.getMaxMp();
		_maxCP = cha.getMaxCp();
		_showSpawnAnimation = cha.getSpawnAnimation();
		_showName = cha.isShowName();
		_canTarget = cha.isTargetable();
		_state = cha.getNpcState();
		_nameNpcString = cha.getNameNpcString();
		_titleNpcString = cha.getTitleNpcString();
		_transformId = cha.getTransformation();
		common(cha);
	}
	
	public NpcInfo(Summon cha, Creature attacker)
	{
		if ((cha.getPlayer() != null) && cha.getPlayer().isInvisible())
		{
			return;
		}
		_npcId = cha.getTemplate().getId();
		_isAttackable = cha.isAutoAttackable(attacker);
		_rhand = 0;
		_lhand = 0;
		_showName = true;
		_name = cha.getName();
		_title = cha.getTitle();
		_showSpawnAnimation = cha.getSpawnAnimation();
		common(cha);
	}
	
	private void common(Creature cha)
	{
		_colHeight = cha.getTemplate().getCollisionHeight();
		_colRadius = cha.getTemplate().getCollisionRadius();
		_currentColHeight = cha.getColHeight();
		_currentColRadius = cha.getColRadius();
		_npcObjId = cha.getObjectId();
		_loc = cha.getLoc();
		_mAtkSpd = cha.getMAtkSpd();
		
		Clan clan = cha.getClan();
		Alliance alliance = clan == null ? null : clan.getAlliance();
		
		_clanId = clan == null ? 0 : clan.getClanId();
		_clanCrestId = clan == null ? 0 : clan.getCrestId();
		
		_allyId = alliance == null ? 0 : alliance.getAllyId();
		_allyCrestId = alliance == null ? 0 : alliance.getAllyCrestId();
		
		_runSpd = cha.getRunSpeed();
		_walkSpd = cha.getWalkSpeed();
		_karma = cha.getKarma();
		_pvpFlag = cha.getPvpFlag();
		_pAtkSpd = cha.getPAtkSpd();
		_running = cha.isRunning() ? 1 : 0;
		_incombat = cha.isInCombat();
		_dead = cha.isAlikeDead();
		_aveList = cha.getAveList();
		_flying = cha.isFlying();
		_team = cha.getTeam();
		_formId = cha.getFormId();
		_isNameAbove = cha.isNameAbove();
		_titleColor = cha.isSummon() || cha.isPet() ? 1 : 0;
		_canWriteImpl = true;
		_waterZone = cha.isInZone(ZoneType.Water);
		_moveMultiplier = cha.getMovementSpeedMultiplier();
		_spdMultiplier = cha.getAttackSpeedMultiplier();
	}
	
	public NpcInfo update()
	{
		_showSpawnAnimation = 1;
		return this;
	}
	
	@Override
	protected final void writeImpl()
	{
		if (!_canWriteImpl)
		{
			return;
		}
		
		writeC(0x0C);
		writeD(_npcObjId);
		writeC(0x00);
		writeC(0x25);
		writeC(0x00);
		writeC(0xED);
		if ((_rhand > 0) || (_lhand > 0))
		{
			writeC(0xFE);
		}
		else
		{
			writeC(0xBE);
		}
		writeC(0x4E);
		writeC(0xA2);
		writeC(0x0C);
		writeC(7 + (_title.length() * 2));
		writeC(_isAttackable ? 1 : 0);
		writeH(0);
		writeH(0);
		writeS(_title);
		if ((_rhand > 0) || (_lhand > 0))
		{
			writeH(68);
		}
		else
		{
			writeH(56);
		}
		writeD(_npcId + 1000000); // npctype id
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ() + Config.CLIENT_Z_SHIFT);
		writeD(_loc.getHeading());
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		
		putFloat(1.100000023841858f);
		putFloat(_pAtkSpd / 277.478340719f);
		
		if ((_rhand > 0) || (_lhand > 0))
		{
			writeD(_rhand);
			writeD(0); // chest
			writeD(_lhand);
			
		}
		writeC(_isNameAbove ? 1 : 0);
		writeC(_running);
		writeC(_flying ? 2 : 0);
		writeD(_flying ? 1 : 0);
		writeC(0);
		writeC(0);
		writeH(0);
		writeD(_HP);
		writeD(_maxHP);
		writeC((_incombat ? 1 : 0) + (_dead ? 2 : 0) + (_canTarget ? 4 : 0) + (_showName ? 8 : 0));
		
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
	}
}