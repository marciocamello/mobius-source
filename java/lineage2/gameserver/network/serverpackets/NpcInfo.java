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

import javolution.util.FastList;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Summon;
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
	private int _incombat;
	private int _dead;
	private int _showSpawnAnimation;
	private int _runSpd;
	private int _walkSpd;
	private int _mAtkSpd;
	private int _pAtkSpd;
	private int _rhand;
	private int _lhand;
	private final int _enchantEffect = 0;
	private int _karma;
	private int _pvpFlag;
	private int _clanId;
	private int _clanCrestId;
	private int _allyId;
	private int _allyCrestId;
	private int _formId;
	private int _titleColor;
	private int _HP;
	private int _maxHP;
	private int _MP;
	private int _maxMP;
	private int _CP;
	private int _maxCP;
	private double _colHeight;
	private double _colRadius;
	private double _currentColHeight;
	private double _currentColRadius;
	private boolean _isAttackable;
	private boolean _isNameAbove;
	private boolean _flying;
	private Location _loc;
	private String _name = StringUtils.EMPTY;
	private String _title = StringUtils.EMPTY;
	private boolean _showName;
	private boolean _canTarget;
	private int _state;
	private NpcString _nameNpcString = NpcString.NONE;
	private NpcString _titleNpcString = NpcString.NONE;
	private TeamType _team;
	private int _transformId;
	private FastList<Integer> _aveList;
	
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
		_incombat = cha.isInCombat() ? 1 : 0;
		_dead = cha.isAlikeDead() ? 1 : 0;
		_aveList = cha.getAveList();
		_flying = cha.isFlying();
		_team = cha.getTeam();
		_formId = cha.getFormId();
		_isNameAbove = cha.isNameAbove();
		_titleColor = cha.isSummon() || cha.isPet() ? 1 : 0;
		_canWriteImpl = true;
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
		
		writeC(0x0c);
		writeD(_npcObjId);
		writeD(_npcId + 1000000); // npctype id c4
		writeD(_isAttackable ? 1 : 0);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ() + Config.CLIENT_Z_SHIFT);
		writeD(_loc.getHeading());
		writeD(0x00);
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeF(1.100000023841858);
		writeF(_pAtkSpd / 277.478340719);
		writeF(_colRadius);
		writeF(_colHeight);
		writeD(_rhand); // right hand weapon
		writeD(0x00); // TODO: chest
		writeD(_lhand); // left hand weapon
		writeC(_isNameAbove ? 1 : 0); // 2.2: name above char 1=true ... ?? 2.3: 1 - normal, 2 - dead
		writeC(_running);
		writeC(_incombat);
		writeC(_dead);
		writeC(_showSpawnAnimation); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon animation)
		writeD(_nameNpcString.getId());
		writeS(_name);
		writeD(_titleNpcString.getId());
		writeS(_title);
		writeD(_titleColor);
		writeD(_pvpFlag);
		writeD(_karma);
		writeD(_clanId);
		writeD(_clanCrestId);
		writeD(_allyId);
		writeD(_allyCrestId);
		writeD(0x00);
		writeC(_flying ? 2 : 0); // C2
		writeC(_team.ordinal()); // team aura 1-blue, 2-red
		writeF(_currentColRadius);
		writeF(_currentColHeight);
		writeD(_enchantEffect); // C4
		writeD(_flying ? 1 : 0);
		writeD(0x00);
		writeD(_formId);
		writeC(_canTarget ? 0x01 : 0x00); // show name
		writeC(_showName ? 0x01 : 0x00); // show title
		writeD(_state);
		writeD(_transformId);
		writeD(_HP);
		writeD(_maxHP);
		writeD(_MP);
		writeD(_maxMP);
		writeD(_CP);
		writeD(_maxCP);
		writeD(0);
		writeC(0);
		writeF(0);
		
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