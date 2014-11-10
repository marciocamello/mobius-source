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

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.skills.effects.EffectCubic;
import lineage2.gameserver.utils.Location;

public class UserInfo extends L2GameServerPacket
{
	private boolean _canWriteImpl = false;
	private final boolean _partyRoom;
	private final int _runSpd;
	private final int _walkSpd;
	private final int _swimRunSpd;
	private final int _swimWalkSpd;
	private final int _flRunSpd;
	private final int _flWalkSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private int _relation;
	private final double _movementSpeedMultiplier;
	private final double _attackSpeedMultiplier;
	private final double _colRadius;
	private final double _colHeight;
	private final int[][] _inv;
	private final Location _loc;
	private final Location _fishLoc;
	private final int _objectId;
	private final int _vehicleObjId;
	private final int _race;
	private final int _sex;
	private final int _baseClassId;
	private final int _level;
	private final int _curCp;
	private final int _maxCp;
	private int _enchantEffect;
	private final int _weaponFlag;
	private final long _exp;
	private final int _curHp;
	private final int _maxHp;
	private final int _curMp;
	private final int _maxMp;
	private final int _curLoad;
	private final int _maxLoad;
	private final int _recomLeft;
	private final int _recomHave;
	private final int _str;
	private final int _con;
	private final int _dex;
	private final int _int;
	private final int _wit;
	private final int _men;
	private final int _sp;
	private final int _clanPrivileges;
	private final int _inventoryLimit;
	private final int _patk;
	private final int _patkspd;
	private final int _pdef;
	private final int _evasion;
	private final int _accuracy;
	private final int _criticalHit;
	private final int _matk;
	private final int _matkspd;
	private final int _mevasion;
	private final int _maccuracy;
	private final int _mCritRate;
	private final int _mdef;
	private final int _PvpFlag;
	private final int _karma;
	private final int _hairStyle;
	private final int _hairColor;
	private final int _face;
	private final int _gmCommands;
	private final int _fame;
	private final int _vitality;
	private int _clanId;
	private int _clanCrestId;
	private int _allyId;
	private int _allyCrestId;
	private int _largeClanCrestId;
	private final int _privateStoreType;
	private final int _canCrystalize;
	private final int _pkKills;
	private final int _pvpKills;
	private final int _classId;
	private final int _agathionId;
	private final int _partySubstitute;
	private final int _noble;
	private final int _hero;
	private int _mountNpcId;
	private int _cwLevel;
	private final int _nameColor;
	private final int _running;
	private final int _pledgeClass;
	private final int _pledgeType;
	private final int _titleColor;
	private final int _transformation;
	private int _fishing;
	private final int _defenceFire;
	private final int _defenceWater;
	private final int _defenceWind;
	private final int _defenceEarth;
	private final int _defenceHoly;
	private final int _defenceUnholy;
	private int _mountType;
	private String _name;
	private String _title;
	private final EffectCubic[] _cubics;
	private final Element _attackElement;
	private final int _attackElementValue;
	private final boolean _flying;
	private final boolean _allowMap;
	private final int _talismanCount;
	private final boolean _openCloak;
	private final double _expPercent;
	private final TeamType _team;
	private final List<Integer> _aveList;
	private final PcInventory _inventory;
	
	public UserInfo(Player player)
	{
		if (player.getTransformationName() != null)
		{
			_name = player.getTransformationName();
			_title = "";
			_clanCrestId = 0;
			_allyCrestId = 0;
			_largeClanCrestId = 0;
			_cwLevel = CursedWeaponsManager.getInstance().getLevel(player.getCursedWeaponEquippedId());
		}
		else
		{
			_name = player.getName();
			Clan clan = player.getClan();
			Alliance alliance = clan == null ? null : clan.getAlliance();
			
			_clanId = clan == null ? 0 : clan.getClanId();
			_clanCrestId = clan == null ? 0 : clan.getCrestId();
			_largeClanCrestId = clan == null ? 0 : clan.getCrestLargeId();
			
			_allyId = alliance == null ? 0 : alliance.getAllyId();
			_allyCrestId = alliance == null ? 0 : alliance.getAllyCrestId();
			_cwLevel = 0;
			_title = player.getTitle();
		}
		
		if (player.isPolymorphed())
		{
			if (NpcHolder.getInstance().getTemplate(player.getPolyId()) != null)
			{
				_title += " - " + NpcHolder.getInstance().getTemplate(player.getPolyId()).name;
			}
			else
			{
				_title += " - Polymorphed";
			}
		}
		
		if (player.isMounted())
		{
			_enchantEffect = 0;
			_mountNpcId = player.getMountNpcId() + 1000000;
			_mountType = player.getMountType();
		}
		else
		{
			_enchantEffect = player.getEnchantEffect();
			_mountNpcId = 0;
			_mountType = 0;
		}
		
		_weaponFlag = player.getActiveWeaponInstance() == null ? 0x14 : 0x28;
		_movementSpeedMultiplier = player.getMovementSpeedMultiplier();
		_runSpd = (int) (player.getRunSpeed() / _movementSpeedMultiplier);
		_walkSpd = (int) (player.getWalkSpeed() / _movementSpeedMultiplier);
		_flRunSpd = (int) player.getTemplate().getBaseFlyRunSpd();
		_flWalkSpd = (int) player.getTemplate().getBaseFlyWalkSpd();
		
		if (player.isFlying())
		{
			_flyRunSpd = _runSpd;
			_flyWalkSpd = _walkSpd;
		}
		else
		{
			_flyRunSpd = 0;
			_flyWalkSpd = 0;
		}
		
		_swimRunSpd = player.getSwimRunSpeed();
		_swimWalkSpd = player.getSwimWalkSpeed();
		_inv = new int[Inventory.PAPERDOLL_MAX][3];
		
		for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
		{
			_inv[PAPERDOLL_ID][0] = player.getInventory().getPaperdollObjectId(PAPERDOLL_ID);
			_inv[PAPERDOLL_ID][1] = player.getInventory().getPaperdollItemId(PAPERDOLL_ID);
			_inv[PAPERDOLL_ID][2] = player.getInventory().getPaperdollAugmentationId(PAPERDOLL_ID);
		}
		
		_relation = player.isClanLeader() ? 0x40 : 0;
		
		for (GlobalEvent e : player.getEvents())
		{
			_relation = e.getUserRelation(player, _relation);
		}
		
		_loc = player.getLoc();
		_objectId = player.getObjectId();
		_vehicleObjId = player.isInBoat() ? player.getBoat().getObjectId() : 0x00;
		_race = player.getRace().ordinal();
		_sex = player.getSex();
		_baseClassId = player.getBaseClassId();
		_level = player.getLevel();
		_exp = player.getExp();
		_expPercent = Experience.getExpPercent(player.getLevel(), player.getExp());
		_str = player.getSTR();
		_dex = player.getDEX();
		_con = player.getCON();
		_int = player.getINT();
		_wit = player.getWIT();
		_men = player.getMEN();
		_curHp = (int) player.getCurrentHp();
		_maxHp = player.getMaxHp();
		_curMp = (int) player.getCurrentMp();
		_maxMp = player.getMaxMp();
		_curLoad = player.getCurrentLoad();
		_maxLoad = player.getMaxLoad();
		_sp = player.getIntSp();
		_patk = player.getPAtk(null);
		_patkspd = player.getPAtkSpd();
		_pdef = player.getPDef(null);
		_evasion = player.getEvasionRate(null);
		_mevasion = player.getMEvasionRate(null);
		_maccuracy = player.getMAccuracy();
		_mCritRate = (int) player.getMagicCriticalRate(null, null);
		_accuracy = player.getAccuracy();
		_criticalHit = player.getCriticalHit(null, null);
		_matk = player.getMAtk(null, null);
		_matkspd = player.getMAtkSpd();
		_mdef = player.getMDef(null, null);
		_PvpFlag = player.getPvpFlag(); // 0=white, 1=purple, 2=purpleblink
		_karma = player.getKarma();
		_attackSpeedMultiplier = player.getAttackSpeedMultiplier();
		_colRadius = player.getColRadius();
		_colHeight = player.getColHeight();
		_hairStyle = player.getHairStyle();
		_hairColor = player.getHairColor();
		_face = player.getFace();
		_gmCommands = player.isGM() || player.getPlayerAccess().CanUseGMCommand ? 1 : 0;
		_clanId = player.getClanId();
		_allyId = player.getAllyId();
		_privateStoreType = player.getPrivateStoreType();
		_canCrystalize = player.getSkillLevel(Skill.SKILL_CRYSTALLIZE) > 0 ? 1 : 0;
		_pkKills = player.getPkKills();
		_pvpKills = player.getPvpKills();
		_cubics = player.getCubics().toArray(new EffectCubic[player.getCubics().size()]);
		_aveList = player.getAveList();
		_clanPrivileges = player.getClanPrivileges();
		_recomLeft = player.getRecomLeft(); // c2 recommendations remaining
		_recomHave = player.getRecomHave(); // c2 recommendations received
		_inventoryLimit = player.getInventoryLimit();
		_classId = player.getClassId().getId();
		_maxCp = player.getMaxCp();
		_curCp = (int) player.getCurrentCp();
		_team = player.getTeam();
		_noble = player.isNoble() || (player.isGM() && Config.GM_HERO_AURA) ? 1 : 0;
		_hero = player.isHero() || (player.isGM() && Config.GM_HERO_AURA) ? 1 : 0;
		_fishing = player.isFishing() ? 1 : 0; // Fishing Mode
		_fishLoc = player.getFishLoc();
		_nameColor = player.getNameColor();
		_running = player.isRunning() ? 0x01 : 0x00; // changes the Speed display on Status Window
		_pledgeClass = player.getPledgeClass();
		_pledgeType = player.getPledgeType();
		_titleColor = player.getTitleColor();
		_transformation = player.getTransformation();
		_attackElement = player.getAttackElement();
		_attackElementValue = player.getAttack(_attackElement);
		_defenceFire = player.getDefence(Element.FIRE);
		_defenceWater = player.getDefence(Element.WATER);
		_defenceWind = player.getDefence(Element.WIND);
		_defenceEarth = player.getDefence(Element.EARTH);
		_defenceHoly = player.getDefence(Element.HOLY);
		_defenceUnholy = player.getDefence(Element.UNHOLY);
		_agathionId = player.getAgathionId();
		_fame = player.getFame();
		_vitality = player.getVitality();
		_partyRoom = (player.getMatchingRoom() != null) && (player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING) && (player.getMatchingRoom().getLeader() == player);
		_flying = player.isInFlyingTransform();
		_talismanCount = player.getTalismanCount();
		_openCloak = player.getOpenCloak();
		_allowMap = player.isActionBlocked(Zone.BLOCKED_ACTION_MINIMAP);
		_fishing = player.isFishing() ? 1 : 0;
		_partySubstitute = 0;
		_inventory = player.getInventory();
		_canWriteImpl = true;
	}
	
	@Override
	protected final void writeImpl()
	{
		if (!_canWriteImpl)
		{
			return;
		}
		
		writeC(0x32);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ() + Config.CLIENT_Z_SHIFT);
		writeD(_vehicleObjId);
		writeD(_objectId);
		writeS(_name);
		writeD(_race);
		writeD(_sex);
		writeD(_baseClassId);
		writeD(_level);
		writeQ(_exp);
		writeF(_expPercent);
		writeD(_str);
		writeD(_dex);
		writeD(_con);
		writeD(_int);
		writeD(_wit);
		writeD(_men);
		writeD(_maxHp);
		writeD(_curHp);
		writeD(_maxMp);
		writeD(_curMp);
		writeD(_sp);
		writeD(_curLoad);
		writeD(_maxLoad);
		writeD(_weaponFlag);
		
		for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
		{
			writeD(_inv[PAPERDOLL_ID][0]);
		}
		
		for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
		{
			writeD(_inv[PAPERDOLL_ID][1]);
		}
		
		for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
		{
			writeD(_inv[PAPERDOLL_ID][2]);
		}
		
		writeD(_talismanCount);
		writeD(_openCloak ? 0x01 : 0x00);
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_RHAND));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_LHAND));
		writeD(0x00);
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_GLOVES));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_CHEST));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_LEGS));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_FEET));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_HAIR));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_DHAIR));
		writeD(_patk);
		writeD(_patkspd);
		writeD(_pdef);
		writeD(_evasion);
		writeD(_accuracy);
		writeD(_criticalHit);
		writeD(_matk);
		writeD(_matkspd);
		writeD(_patkspd);
		writeD(_mdef);
		writeD(_mevasion);
		writeD(_maccuracy);
		writeD(_mCritRate);
		writeD(_PvpFlag);
		writeD(_karma);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_swimRunSpd);
		writeD(_swimWalkSpd);
		writeD(_flRunSpd);
		writeD(_flWalkSpd);
		writeD(_flyRunSpd);
		writeD(_flyWalkSpd);
		writeF(_movementSpeedMultiplier);
		writeF(_attackSpeedMultiplier);
		writeF(_colRadius);
		writeF(_colHeight);
		writeD(_hairStyle);
		writeD(_hairColor);
		writeD(_face);
		writeD(_gmCommands);
		writeS(_title);
		writeD(_clanId);
		writeD(_clanCrestId);
		writeD(_allyId);
		writeD(_allyCrestId);
		writeD(_relation);
		writeC(_mountType); // mount type
		writeC(_privateStoreType);
		writeC(_canCrystalize);
		writeD(_pkKills);
		writeD(_pvpKills);
		writeH(_cubics.length);
		
		for (EffectCubic cubic : _cubics)
		{
			writeH(cubic == null ? 0 : cubic.getId());
		}
		
		writeC(_partyRoom ? 0x01 : 0x00); // 1-find party members
		writeC(_flying ? 0x02 : 0x00);
		writeD(_clanPrivileges);
		writeH(_recomLeft);
		writeH(_recomHave);
		writeD(_mountNpcId);
		writeH(_inventoryLimit);
		writeD(_classId);
		writeD(0x00); // special effects? circles around player...
		writeD(_maxCp);
		writeD(_curCp);
		writeC(_enchantEffect);
		writeC(_team.ordinal());
		writeD(_largeClanCrestId);
		writeC(_noble);
		writeC(_hero);
		writeC(_fishing);
		writeD(_fishLoc.getX());
		writeD(_fishLoc.getY());
		writeD(_fishLoc.getZ());
		writeD(_nameColor);
		writeC(_running);
		writeD(_pledgeClass);
		writeD(_pledgeType);
		writeD(_titleColor);
		writeD(_cwLevel);
		writeD(_transformation); // Transformation id
		writeH(_attackElement.getId()); // AttackElement (0 - Fire, 1 - Water, 2 - Wind, 3 - Earth, 4 - Holy, 5 - Dark, -2 - None)
		writeH(_attackElementValue); // AttackElementValue
		writeH(_defenceFire); // DefAttrFire
		writeH(_defenceWater); // DefAttrWater
		writeH(_defenceWind); // DefAttrWind
		writeH(_defenceEarth); // DefAttrEarth
		writeH(_defenceHoly); // DefAttrHoly
		writeH(_defenceUnholy); // DefAttrUnholy
		writeD(_agathionId);
		// T2 Starts
		writeD(_fame); // Fame
		writeD(_allowMap ? 1 : 0); // Minimap on Hellbound
		writeD(_vitality); // Vitality Points
		writeD(0x00);// Unknown GOD
		writeD(0x00);// Unknown GOD (1 - Party searching?)
		writeC(_partySubstitute);
		writeD(0x00);// Unknown GOD
		
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
		
		writeC(0x00);
	}
}