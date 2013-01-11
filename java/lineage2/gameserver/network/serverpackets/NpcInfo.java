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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.StringUtils;

public class NpcInfo extends L2GameServerPacket
{
	private boolean can_writeImpl = false;
	private int _npcObjId, _npcId, running, incombat, dead, _showSpawnAnimation;
	private int _runSpd, _walkSpd, _mAtkSpd, _pAtkSpd, _rhand, _lhand, _enchantEffect;
	private int karma, pvp_flag, clan_id, clan_crest_id, ally_id, ally_crest_id, _formId, _titleColor;
	private int _HP, _maxHP, _MP, _maxMP, _CP, _maxCP;
	private double colHeight, colRadius, currentColHeight, currentColRadius;
	private boolean _isAttackable, _isNameAbove, isFlying;
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
	private int _abnormalEffect, _abnormalEffect2;
	
	public NpcInfo(NpcInstance cha, Creature attacker)
	{
		_npcId = cha.getDisplayId() != 0 ? cha.getDisplayId() : cha.getTemplate().npcId;
		_isAttackable = (attacker != null) && cha.isAutoAttackable(attacker);
		_rhand = cha.getRightHandItem();
		_lhand = cha.getLeftHandItem();
		_enchantEffect = 0;
		if (Config.SERVER_SIDE_NPC_NAME || (cha.getTemplate().displayId != 0) || (cha.getName() != cha.getTemplate().name))
		{
			_name = cha.getName();
		}
		if (Config.SERVER_SIDE_NPC_TITLE || (cha.getTemplate().displayId != 0) || (cha.getTitle() != cha.getTemplate().title))
		{
			_title = cha.getTitle();
			if (Config.SERVER_SIDE_NPC_TITLE_ETC)
			{
				if (cha.isMonster())
				{
					if (_title.isEmpty())
					{
						_title = "Lv " + cha.getLevel();
					}
					else
					{
						_title = "Lv " + cha.getLevel() + "|" + _title;
					}
				}
			}
		}
		_HP = (int) cha.getCurrentHp();
		_MP = (int) cha.getCurrentMp();
		_maxHP = cha.getMaxHp();
		_maxMP = cha.getMaxMp();
		_CP = (int) cha.getCurrentCp();
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
		_npcId = cha.getTemplate().npcId;
		_isAttackable = cha.isAutoAttackable(attacker);
		_rhand = 0;
		_lhand = 0;
		_enchantEffect = 0;
		_showName = true;
		_name = cha.getName();
		_title = cha.getTitle();
		_showSpawnAnimation = cha.getSpawnAnimation();
		common(cha);
	}
	
	private void common(Creature cha)
	{
		colHeight = cha.getTemplate().getCollisionHeight();
		colRadius = cha.getTemplate().getCollisionRadius();
		currentColHeight = cha.getColHeight();
		currentColRadius = cha.getColRadius();
		_npcObjId = cha.getObjectId();
		_loc = cha.getLoc();
		_mAtkSpd = cha.getMAtkSpd();
		Clan clan = cha.getClan();
		Alliance alliance = clan == null ? null : clan.getAlliance();
		clan_id = clan == null ? 0 : clan.getClanId();
		clan_crest_id = clan == null ? 0 : clan.getCrestId();
		ally_id = alliance == null ? 0 : alliance.getAllyId();
		ally_crest_id = alliance == null ? 0 : alliance.getAllyCrestId();
		_abnormalEffect = cha.getAbnormalEffect();
		_abnormalEffect2 = cha.getAbnormalEffect2();
		_runSpd = cha.getRunSpeed();
		_walkSpd = cha.getWalkSpeed();
		karma = cha.getKarma();
		pvp_flag = cha.getPvpFlag();
		_pAtkSpd = cha.getPAtkSpd();
		running = cha.isRunning() ? 1 : 0;
		incombat = cha.isInCombat() ? 1 : 0;
		dead = cha.isAlikeDead() ? 1 : 0;
		isFlying = cha.isFlying();
		_team = cha.getTeam();
		_formId = cha.getFormId();
		_isNameAbove = cha.isNameAbove();
		_titleColor = cha.isServitor() || cha.isPet() ? 1 : 0;
		can_writeImpl = true;
	}
	
	public NpcInfo update()
	{
		_showSpawnAnimation = 1;
		return this;
	}
	
	@Override
	protected final void writeImpl()
	{
		if (!can_writeImpl)
		{
			return;
		}
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (!activeChar.isTautiClient())
		{
			writeC(0x0c);
			writeD(_npcObjId);
			writeD(_npcId + 1000000);
			writeD(_isAttackable ? 1 : 0);
			writeD(_loc.x);
			writeD(_loc.y);
			writeD(_loc.z + Config.CLIENT_Z_SHIFT);
			writeD(_loc.h);
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
			writeF(colRadius);
			writeF(colHeight);
			writeD(_rhand);
			writeD(0);
			writeD(_lhand);
			writeC(_isNameAbove ? 1 : 0);
			writeC(running);
			writeC(incombat);
			writeC(dead);
			writeC(_showSpawnAnimation);
			writeD(_nameNpcString.getId());
			writeS(_name);
			writeD(_titleNpcString.getId());
			writeS(_title);
			writeD(_titleColor);
			writeD(pvp_flag);
			writeD(karma);
			writeD(_abnormalEffect);
			writeD(clan_id);
			writeD(clan_crest_id);
			writeD(ally_id);
			writeD(ally_crest_id);
			writeC(isFlying ? 2 : 0);
			writeC(_team.ordinal());
			writeF(currentColRadius);
			writeF(currentColHeight);
			writeD(_enchantEffect);
			writeD(0x00);
			writeD(0x00);
			writeD(_formId);
			writeC(_canTarget ? 0x01 : 0x00);
			writeC(_showName ? 0x01 : 0x00);
			writeD(_abnormalEffect2);
			writeD(_state);
			writeD(0x00);
			writeD(_HP);
			writeD(_maxHP);
			writeD(_MP);
			writeD(_maxMP);
			writeC(0);
			writeD(0);
			writeD(0);
			writeD(0);
		}
		else
		{
			writeC(0x0c);
			writeD(_npcObjId);
			writeD(_npcId + 1000000);
			writeD(_isAttackable ? 1 : 0);
			writeD(_loc.x);
			writeD(_loc.y);
			writeD(_loc.z + Config.CLIENT_Z_SHIFT);
			writeD(_loc.h);
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
			writeF(colRadius);
			writeF(colHeight);
			writeD(_rhand);
			writeD(0);
			writeD(_lhand);
			writeC(_isNameAbove ? 1 : 0);
			writeC(running);
			writeC(incombat);
			writeC(dead);
			writeC(_showSpawnAnimation);
			writeD(_nameNpcString.getId());
			writeS(_name);
			writeD(_titleNpcString.getId());
			writeS(_title);
			writeD(_titleColor);
			writeD(pvp_flag);
			writeD(karma);
			writeD(clan_id);
			writeD(clan_crest_id);
			writeD(ally_id);
			writeD(ally_crest_id);
			writeD(0x00);
			writeC(isFlying ? 2 : 0);
			writeC(_team.ordinal());
			writeF(currentColRadius);
			writeF(currentColHeight);
			writeD(_enchantEffect);
			writeD(isFlying ? 1 : 0);
			writeD(0x00);
			writeD(_formId);
			writeC(_canTarget ? 0x01 : 0x00);
			writeC(_showName ? 0x01 : 0x00);
			writeD(_state);
			writeD(_transformId);
			writeD(_HP);
			writeD(_maxHP);
			writeD(_MP);
			writeD(_maxMP);
			writeD(_CP);
			writeD(_maxCP);
			writeH(0x00);
			writeH(0x00);
			writeC(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
		}
	}
}
