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

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.flags.CharacterMasksUI1;
import lineage2.gameserver.network.flags.CharacterMasksUI2;
import lineage2.gameserver.network.flags.CharacterMasksUI3;
import lineage2.gameserver.utils.Location;

public class UserInfo extends L2GameServerPacket
{
	// private static final Logger _log = LoggerFactory.getLogger(UserInfo.class);
	
	private static final int _blockSize = 2;
	
	private final boolean _partyRoom;
	private final int _runSpd;
	private final int _walkSpd;
	private final int _swimRunSpd;
	private final int _swimWalkSpd;
	private final double _movementSpeedMultiplier;
	private final double _attackSpeedMultiplier;
	private final double _colRadius;
	private final double _colHeight;
	private final int[][] _inv;
	private final Location _loc;
	private final int _objectId;
	private final int _vehicleObjId;
	private final int _race;
	private final int _sex;
	private final int _baseClassId;
	private final int _level;
	private final int _curCp;
	private final int _maxCp;
	private final long _exp;
	private final int _curHp;
	private final int _maxHp;
	private final int _curMp;
	private final int _maxMp;
	private final int _str;
	private final int _con;
	private final int _dex;
	private final int _int;
	private final int _wit;
	private final int _men;
	private final int _luc;
	private final int _cha;
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
	private final int _hairStyle;
	private final int _hairColor;
	private final int _face;
	private final int _fame;
	private final int _vitality;
	private final int _privateStoreType;
	private final int _canCrystalize;
	private final int _pkKills;
	private final int _pvpKills;
	private final int _classId;
	private final int _nameColor;
	private final int _pledgeClass;
	private final int _pledgeType;
	private final int _titleColor;
	private final int _defenceFire;
	private final int _defenceWater;
	private final int _defenceWind;
	private final int _defenceEarth;
	private final int _defenceHoly;
	private final int _defenceUnholy;
	private final int _attackElementId;
	private final int _attackElementValue;
	private final int _talismanSlots;
	private final double _expPercent;
	
	private final int _uiv1;
	private final int _uiv2;
	private final int _uiv3;
	private final int _structType;
	private final int _canUseAdminCmd;
	private final int _weaponEnchantGlow;
	private final int _armorEnchantGlow;
	private final int _showHairAccessory;
	private final int _canUseAlchemy;
	private final int _boatObjectId;
	private final int _isClanLeader;
	private final int _lookingParty;
	private final int _reputation;
	private final int _isNoble;
	private final int _isHero;
	private final int _recomendationsHave;
	private final int _evaluationScore;
	private final int _raidPoints;
	private final int _cloakSlots;
	private final int _duelTeam;
	private final int _teamMask;
	private final int _swim;
	private final int _run;
	private final int _mount;
	private final int _hideTitle;
	
	private int _flyRunSpd = 0;
	private int _flyWalkSpd = 0;
	private int _relation = 0;
	private int _clanId = 0;
	private int _clanCrestId = 0;
	private int _allyId = 0;
	private int _allyCrestId = 0;
	private int _largeClanCrestId = 0;
	private int _mountType = 0;
	
	private int _appearanceBlockSize = 0;
	private int _fullBlockSize = 0;
	private int _baseStatsBlockSize = 0;
	private int _maxStatsBlockSize = 0;
	private int _currStatsBlockSize = 0;
	private int _weaponGlowBlockSize = 0;
	private int _facialFeaturesBlockSize = 0;
	private int _personalStoreBlockSize = 0;
	private int _baseStatsv2BlockSize = 0;
	private final int _weaponStatus = 0;
	private int _elemDefBlockSize = 0;
	private int _locationBlockSize = 0;
	private int _moveSpeedBlockSize = 0;
	private int _mountRunSpd = 0;
	private int _mountWalkpd = 0;
	private int _animSpeedBlockSize = 0;
	private int _objBounBlockSize = 0;
	private int _elemOffBlockSize = 0;
	private int _pledgeInfoBlockSize = 0;
	private int _statsv3BlockSize = 0;
	private int _vitalityBlockSize = 0;
	private int _talismansBlockSize = 0;
	private int _moveTypeBlockSize = 0;
	private int _nameBlockSize = 0;
	private int _invBlockSize = 0;
	private int _unk1BlockSize = 0;
	
	private String _name;
	private String _title;
	
	/**
	 * Constructor. This constructor should be use only for first request from client about user data.
	 * @param player
	 */
	public UserInfo(Player player)
	{
		this(player, CharacterMasksUI1.ALL, CharacterMasksUI2.ALL, CharacterMasksUI3.ALL);
	}
	
	/**
	 * Constructor. TODO: Need to be cleaned soon after tests.
	 * @param player
	 * @param uiv1
	 * @param uiv2
	 * @param uiv3
	 */
	public UserInfo(Player player, int uiv1, int uiv2, int uiv3)
	{
		_uiv1 = uiv1;
		_uiv2 = uiv2;
		_uiv3 = uiv3;
		_structType = 23;
		_canUseAdminCmd = player.isGM() ? 1 : 0; // fix for using //admin command
		
		if (player.getTransformationName() != null)
		{
			_name = player.getTransformationName();
			_clanCrestId = 0;
			_allyCrestId = 0;
			_largeClanCrestId = 0;
		}
		else
		{
			_name = player.getName();
			Clan clan = player.getClan();
			if (clan == null)
			{
				_clanId = 0;
				_clanCrestId = 0;
				_largeClanCrestId = 0;
				
				_allyId = 0;
				_allyCrestId = 0;
			}
			else
			{
				_clanId = clan.getClanId();
				_clanCrestId = clan.getCrestId();
				_largeClanCrestId = clan.getCrestLargeId();
				
				Alliance alliance = clan.getAlliance();
				if (alliance == null)
				{
					_allyId = 0;
					_allyCrestId = 0;
				}
				else
				{
					_allyId = alliance.getAllyId();
					_allyCrestId = alliance.getAllyCrestId();
				}
			}
		}
		
		_title = player.getTitle();
		// if (_activeChar.isInvisible())
		// {
		// title = "Invisible";
		// }
		
		if (player.isPolymorphed())
		{
			if (NpcHolder.getInstance().getTemplate(player.getPolyId()) != null)
			{
				_title += " (" + NpcHolder.getInstance().getTemplate(player.getPolyId()).name + ")";
			}
		}
		
		// _log.info("_name = " + _name + ", length = " + _name.length());
		generateBlockSizes();
		
		_movementSpeedMultiplier = player.getMovementSpeedMultiplier();
		_runSpd = (int) (player.getRunSpeed() / _movementSpeedMultiplier);
		_walkSpd = (int) (player.getWalkSpeed() / _movementSpeedMultiplier);
		_flyRunSpd = (int) player.getTemplate().getBaseFlyRunSpd();
		_flyWalkSpd = (int) player.getTemplate().getBaseFlyWalkSpd();
		
		// if (player.isFlying())
		// {
		// _flyRunSpd = _runSpd;
		// _flyWalkSpd = _walkSpd;
		// }
		// else
		// {
		// _flyRunSpd = 0;
		// _flyWalkSpd = 0;
		// }
		
		_mountRunSpd = _flyRunSpd;
		_mountWalkpd = _flyWalkSpd;
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
		_luc = player.getLUC();
		_cha = player.getCHA();
		_curHp = (int) player.getCurrentHp();
		_maxHp = player.getMaxHp();
		_curMp = (int) player.getCurrentMp();
		_maxMp = player.getMaxMp();
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
		_attackSpeedMultiplier = player.getAttackSpeedMultiplier();
		_colRadius = player.getColRadius();
		_colHeight = player.getColHeight();
		_hairStyle = player.getHairStyle();
		_hairColor = player.getHairColor();
		_face = player.getFace();
		_showHairAccessory = 1;
		_clanId = player.getClanId();
		_isClanLeader = player.isClanLeader() ? 1 : 0;
		
		_allyId = player.getAllyId();
		_privateStoreType = player.getPrivateStoreType();
		_canCrystalize = player.getSkillLevel(Skill.SKILL_CRYSTALLIZE) > 0 ? 1 : 0;
		_canUseAlchemy = 0;
		_pkKills = player.getPkKills();
		_pvpKills = player.getPvpKills();
		_clanPrivileges = player.getClanPrivileges();
		_inventoryLimit = player.getInventoryLimit();
		_classId = player.getClassId().getId();
		_maxCp = player.getMaxCp();
		_curCp = (int) player.getCurrentCp();
		_duelTeam = player.getTeam().ordinal();
		_teamMask = 0;
		_nameColor = player.getNameColor();
		_pledgeClass = player.getPledgeClass();
		_pledgeType = player.getPledgeType();
		_titleColor = player.getTitleColor();
		Element _attackElement = player.getAttackElement();
		if (_attackElement != null)
		{
			_attackElementId = _attackElement.getId();
			_attackElementValue = player.getAttack(_attackElement);
		}
		else
		{
			_attackElementId = 0;
			_attackElementValue = 0;
		}
		_defenceFire = player.getDefence(Element.FIRE);
		_defenceWater = player.getDefence(Element.WATER);
		_defenceWind = player.getDefence(Element.WIND);
		_defenceEarth = player.getDefence(Element.EARTH);
		_defenceHoly = player.getDefence(Element.HOLY);
		_defenceUnholy = player.getDefence(Element.UNHOLY);
		_fame = player.getFame();
		_vitality = player.getVitality();
		_partyRoom = (player.getMatchingRoom() != null) && (player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING) && (player.getMatchingRoom().getLeader() == player);
		_talismanSlots = player.getTalismanCount();
		_cloakSlots = /* player.getOpenCloak() ? 1 : */0; // TODO: Test this !!!
		_boatObjectId = player.getBoat() != null ? player.getBoat().getObjectId() : 0;
		_lookingParty = _partyRoom ? 0x01 : 0x00;
		_raidPoints = 0; // TODO: Implement this !!!
		_reputation = 0 - player.getKarma();
		_isNoble = player.isNoble() || (player.isGM() && Config.GM_HERO_AURA) ? 1 : 0;
		_isHero = player.isHero() || (player.isGM() && Config.GM_HERO_AURA) ? 1 : 0;
		_recomendationsHave = player.getRecomLeft();
		_evaluationScore = player.getRecomHave();
		
		_swim = player.isInZone(ZoneType.Water) ? 1 : player.isFlying() ? 2 : 0;
		_run = player.isRunning() ? 0x01 : 0x00;
		
		_hideTitle = player.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(player.getCursedWeaponEquippedId()) : 0;
		
		if (player.isMounted())
		{
			_weaponEnchantGlow = (player.isMounted() || (_vehicleObjId != 0)) ? 0 : player.getEnchantEffect();
			_armorEnchantGlow = /* (player.isMounted() || (_vehicleObjId != 0)) ? 0 : player.getEnchantEffect() */0;
			_mount = /* player.getMountNpcId() + 1000000 */0;
			_mountType = /* player.getMountType() */0;
		}
		else
		{
			_weaponEnchantGlow = (player.isMounted() || (_vehicleObjId != 0)) ? 0 : player.getEnchantEffect();
			_armorEnchantGlow = /* (player.isMounted() || (_vehicleObjId != 0)) ? 0 : player.getEnchantEffect() */0;
			_mount = 0;
			_mountType = 0;
		}
		
		player.sendPacket(new ExUserInfoEquipSlot(player));
		player.sendPacket(new ExUserInfoCubic(player));
		player.sendPacket(new ExUserInfoAbnormalVisualEffect(player));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x32);
		writeD(_objectId);
		writeD(_fullBlockSize); // Size of all packets
		writeH(_structType);
		writeC(_uiv1);
		writeC(_uiv2);
		writeC(_uiv3);
		
		if ((_uiv1 & CharacterMasksUI1.ROLES) == CharacterMasksUI1.ROLES)
		{
			writeD(_relation);
		}
		
		if ((_uiv1 & CharacterMasksUI1.APPEARANCE) == CharacterMasksUI1.APPEARANCE)
		{
			writeH(_appearanceBlockSize); // blockSize
			writeH(_name.length());
			writeS2(_name);
			writeC(_canUseAdminCmd);
			writeC(_race);
			writeC(_sex);
			writeD(_baseClassId);
			writeD(_classId);
			writeC(_level);
		}
		
		if ((_uiv1 & CharacterMasksUI1.BASE_STATS) == CharacterMasksUI1.BASE_STATS)
		{
			writeH(_baseStatsBlockSize); // blockSize
			writeH(_str);
			writeH(_dex);
			writeH(_con);
			writeH(_int);
			writeH(_wit);
			writeH(_men);
			writeH(_luc);
			writeH(_cha);
		}
		
		if ((_uiv1 & CharacterMasksUI1.MAX_STATS) == CharacterMasksUI1.MAX_STATS)
		{
			writeH(_maxStatsBlockSize); // blockSize
			writeD(_maxHp);
			writeD(_maxMp);
			writeD(_maxCp);
		}
		
		if ((_uiv1 & CharacterMasksUI1.CURRENT_STATS) == CharacterMasksUI1.CURRENT_STATS)
		{
			writeH(_currStatsBlockSize); // blockSize
			writeD(_curHp);
			writeD(_curMp);
			writeD(_curCp);
			writeQ(_sp);
			writeQ(_exp);
			writeF(_expPercent);
		}
		
		if ((_uiv1 & CharacterMasksUI1.WEAPON_GLOW) == CharacterMasksUI1.WEAPON_GLOW)
		{
			writeH(_weaponGlowBlockSize); // blockSize
			writeC(_weaponEnchantGlow);
			writeC(_armorEnchantGlow);
		}
		
		if ((_uiv1 & CharacterMasksUI1.FACIAL_FEATURES) == CharacterMasksUI1.FACIAL_FEATURES)
		{
			writeH(_facialFeaturesBlockSize); // blockSize
			writeD(_hairStyle);
			writeD(_hairColor);
			writeD(_face);
			writeC(_showHairAccessory);
		}
		
		if ((_uiv1 & CharacterMasksUI1.PERSONAL_STORE) == CharacterMasksUI1.PERSONAL_STORE)
		{
			writeH(_personalStoreBlockSize); // blockSize
			writeC(_mountType);
			writeC(_privateStoreType);
			writeC(_canCrystalize);
			writeC(_canUseAlchemy);
		}
		
		if ((_uiv2 & CharacterMasksUI2.BASE_STATS) == CharacterMasksUI2.BASE_STATS)
		{
			writeH(_baseStatsv2BlockSize); // blockSize
			writeH(_weaponStatus);
			writeD(_patk);
			writeD(_patkspd);
			writeD(_pdef);
			writeD(_evasion);
			writeD(_accuracy);
			writeD(_criticalHit);
			writeD(_matk);
			writeD(_matkspd);
			writeD(_patkspd);
			writeD(_mevasion);
			writeD(_mdef);
			writeD(_maccuracy);
			writeD(_mCritRate);
		}
		
		if ((_uiv2 & CharacterMasksUI2.ELEMENTAL_DEFENSE_STATS) == CharacterMasksUI2.ELEMENTAL_DEFENSE_STATS)
		{
			writeH(_elemDefBlockSize); // blockSize
			writeH(_defenceFire);
			writeH(_defenceWater);
			writeH(_defenceWind);
			writeH(_defenceEarth);
			writeH(_defenceHoly);
			writeH(_defenceUnholy);
		}
		
		if ((_uiv2 & CharacterMasksUI2.LOCATION) == CharacterMasksUI2.LOCATION)
		{
			writeH(_locationBlockSize); // blockSize
			writeD(_loc.getX());
			writeD(_loc.getY());
			writeD(_loc.getZ());
			writeD(_boatObjectId);
		}
		
		if ((_uiv2 & CharacterMasksUI2.MOVEMENT_SPEED) == CharacterMasksUI2.MOVEMENT_SPEED)
		{
			writeH(_moveSpeedBlockSize); // blockSize
			writeH(_runSpd);
			writeH(_walkSpd);
			writeH(_swimRunSpd);
			writeH(_swimWalkSpd);
			writeH(_mountRunSpd);
			writeH(_mountWalkpd);
			writeH(_flyRunSpd);
			writeH(_flyWalkSpd);
		}
		
		if ((_uiv2 & CharacterMasksUI2.ANIMATION_SPEED) == CharacterMasksUI2.ANIMATION_SPEED)
		{
			writeH(_animSpeedBlockSize); // blockSize
			writeF(_movementSpeedMultiplier);
			writeF(_attackSpeedMultiplier);
		}
		
		if ((_uiv2 & CharacterMasksUI2.OBJECT_BOUNDARIES) == CharacterMasksUI2.OBJECT_BOUNDARIES)
		{
			writeH(_objBounBlockSize); // blockSize
			writeF(_colRadius);
			writeF(_colHeight);
		}
		
		if ((_uiv2 & CharacterMasksUI2.ELEMENTAL_OFFENSE_STATS) == CharacterMasksUI2.ELEMENTAL_OFFENSE_STATS)
		{
			writeH(_elemOffBlockSize); // blockSize
			writeC(_attackElementId);
			writeH(_attackElementValue);
		}
		
		if ((_uiv2 & CharacterMasksUI2.PLEDGE_INFO) == CharacterMasksUI2.PLEDGE_INFO)
		{
			writeH(_pledgeInfoBlockSize); // blockSize
			writeH(_title.length());
			writeS2(_title);
			writeH(_pledgeType);
			writeD(_clanId);
			writeD(_largeClanCrestId);
			writeD(_clanCrestId);
			writeD(_clanPrivileges);
			writeC(_isClanLeader);
			writeD(_allyId);
			writeD(_allyCrestId);
			writeC(_lookingParty);
		}
		
		if ((_uiv3 & CharacterMasksUI3.STATS) == CharacterMasksUI3.STATS)
		{
			writeH(_statsv3BlockSize); // blockSize
			writeC(_PvpFlag);
			writeD(_reputation);
			writeC(_isNoble);
			writeC(_isHero);
			writeC(_pledgeClass);
			writeD(_pkKills);
			writeD(_pvpKills);
			writeH(_recomendationsHave);
			writeH(_evaluationScore);
		}
		
		if ((_uiv3 & CharacterMasksUI3.VITALITY) == CharacterMasksUI3.VITALITY)
		{
			writeH(_vitalityBlockSize); // blockSize
			writeD(_vitality);
			writeC(0);
			writeD(_fame);
			writeD(_raidPoints);
		}
		
		if ((_uiv3 & CharacterMasksUI3.TALISMANS) == CharacterMasksUI3.TALISMANS)
		{
			writeH(_talismansBlockSize); // blockSize
			writeC(_talismanSlots);
			writeC(_cloakSlots);
			writeC(_duelTeam);
			writeD(_teamMask);
		}
		
		if ((_uiv3 & CharacterMasksUI3.MOVEMENT_TYPES) == CharacterMasksUI3.MOVEMENT_TYPES)
		{
			writeH(_moveTypeBlockSize); // blockSize
			writeC(_swim);
			writeC(_run);
		}
		
		if ((_uiv3 & CharacterMasksUI3.NAME_TITLE_COLOR) == CharacterMasksUI3.NAME_TITLE_COLOR)
		{
			writeH(_nameBlockSize); // blockSize
			writeD(_nameColor);
			writeD(_titleColor);
		}
		
		if ((_uiv3 & CharacterMasksUI3.INVENTORY_SLOTS) == CharacterMasksUI3.INVENTORY_SLOTS)
		{
			writeH(_invBlockSize); // blockSize
			writeD(_mount);
			writeH(_inventoryLimit);
			writeC(_hideTitle);
		}
		
		if ((_uiv3 & CharacterMasksUI3.UNKNOWN_1) == CharacterMasksUI3.UNKNOWN_1)
		{
			writeH(_unk1BlockSize); // blockSize
			writeD(1);
			writeH(0);
			writeC(0);
		}
		
		// For now this is not used
		// if ((_uiv3 & CharacterMasksUI3.UNDEFINED) == CharacterMasksUI3.UNDEFINED)
		// {
		// writeH(_undBlockSize); // blockSize
		// }
	}
	
	/**
	 * Method for counting block sizes.
	 */
	private void generateBlockSizes()
	{
		_fullBlockSize = 2 + 1 + 1 + 1;
		
		if ((_uiv1 & CharacterMasksUI1.ROLES) == CharacterMasksUI1.ROLES)
		{
			_fullBlockSize += 4;
		}
		
		if ((_uiv1 & CharacterMasksUI1.APPEARANCE) == CharacterMasksUI1.APPEARANCE)
		{
			_appearanceBlockSize = (getLengthS2(_name)) + 14 + _blockSize;
			_fullBlockSize += _appearanceBlockSize - 2;
		}
		
		if ((_uiv1 & CharacterMasksUI1.BASE_STATS) == CharacterMasksUI1.BASE_STATS)
		{
			_baseStatsBlockSize = 16 + _blockSize;
			_fullBlockSize += _baseStatsBlockSize;
		}
		
		if ((_uiv1 & CharacterMasksUI1.MAX_STATS) == CharacterMasksUI1.MAX_STATS)
		{
			_maxStatsBlockSize = 12 + _blockSize;
			_fullBlockSize += _maxStatsBlockSize;
		}
		
		if ((_uiv1 & CharacterMasksUI1.CURRENT_STATS) == CharacterMasksUI1.CURRENT_STATS)
		{
			_currStatsBlockSize = 36 + _blockSize;
			_fullBlockSize += _currStatsBlockSize;
		}
		
		if ((_uiv1 & CharacterMasksUI1.WEAPON_GLOW) == CharacterMasksUI1.WEAPON_GLOW)
		{
			_weaponGlowBlockSize = 2 + _blockSize;
			_fullBlockSize += _weaponGlowBlockSize;
		}
		
		if ((_uiv1 & CharacterMasksUI1.FACIAL_FEATURES) == CharacterMasksUI1.FACIAL_FEATURES)
		{
			_facialFeaturesBlockSize = 13 + _blockSize;
			_fullBlockSize += _facialFeaturesBlockSize;
		}
		
		if ((_uiv1 & CharacterMasksUI1.PERSONAL_STORE) == CharacterMasksUI1.PERSONAL_STORE)
		{
			_personalStoreBlockSize = 4 + _blockSize;
			_fullBlockSize += _personalStoreBlockSize;
		}
		
		if ((_uiv2 & CharacterMasksUI2.BASE_STATS) == CharacterMasksUI2.BASE_STATS)
		{
			_baseStatsv2BlockSize = 54 + _blockSize;
			_fullBlockSize += _baseStatsv2BlockSize;
		}
		
		if ((_uiv2 & CharacterMasksUI2.ELEMENTAL_DEFENSE_STATS) == CharacterMasksUI2.ELEMENTAL_DEFENSE_STATS)
		{
			_elemDefBlockSize = 12 + _blockSize;
			_fullBlockSize += _elemDefBlockSize;
		}
		
		if ((_uiv2 & CharacterMasksUI2.LOCATION) == CharacterMasksUI2.LOCATION)
		{
			_locationBlockSize = 16 + _blockSize;
			_fullBlockSize += _locationBlockSize;
		}
		
		if ((_uiv2 & CharacterMasksUI2.MOVEMENT_SPEED) == CharacterMasksUI2.MOVEMENT_SPEED)
		{
			_moveSpeedBlockSize = 16 + _blockSize;
			_fullBlockSize += _moveSpeedBlockSize;
		}
		
		if ((_uiv2 & CharacterMasksUI2.ANIMATION_SPEED) == CharacterMasksUI2.ANIMATION_SPEED)
		{
			_animSpeedBlockSize = 16 + _blockSize;
			_fullBlockSize += _animSpeedBlockSize;
		}
		
		if ((_uiv2 & CharacterMasksUI2.OBJECT_BOUNDARIES) == CharacterMasksUI2.OBJECT_BOUNDARIES)
		{
			_objBounBlockSize = 16 + _blockSize;
			_fullBlockSize += _objBounBlockSize;
		}
		
		if ((_uiv2 & CharacterMasksUI2.ELEMENTAL_OFFENSE_STATS) == CharacterMasksUI2.ELEMENTAL_OFFENSE_STATS)
		{
			_elemOffBlockSize = 3 + _blockSize;
			_fullBlockSize += _elemOffBlockSize;
		}
		
		if ((_uiv2 & CharacterMasksUI2.PLEDGE_INFO) == CharacterMasksUI2.PLEDGE_INFO)
		{
			_pledgeInfoBlockSize = (getLengthS2(_title)) + 30 + _blockSize;
			_fullBlockSize += _pledgeInfoBlockSize - 2;
		}
		
		if ((_uiv3 & CharacterMasksUI3.STATS) == CharacterMasksUI3.STATS)
		{
			_statsv3BlockSize = 20 + _blockSize;
			_fullBlockSize += _statsv3BlockSize;
		}
		
		if ((_uiv3 & CharacterMasksUI3.VITALITY) == CharacterMasksUI3.VITALITY)
		{
			_vitalityBlockSize = 13 + _blockSize;
			_fullBlockSize += _vitalityBlockSize;
		}
		
		if ((_uiv3 & CharacterMasksUI3.TALISMANS) == CharacterMasksUI3.TALISMANS)
		{
			_talismansBlockSize = 7 + _blockSize;
			_fullBlockSize += _talismansBlockSize;
		}
		
		if ((_uiv3 & CharacterMasksUI3.MOVEMENT_TYPES) == CharacterMasksUI3.MOVEMENT_TYPES)
		{
			_moveTypeBlockSize = 2 + _blockSize;
			_fullBlockSize += _moveTypeBlockSize;
		}
		
		if ((_uiv3 & CharacterMasksUI3.NAME_TITLE_COLOR) == CharacterMasksUI3.NAME_TITLE_COLOR)
		{
			_nameBlockSize = 8 + _blockSize;
			_fullBlockSize += _nameBlockSize;
		}
		
		if ((_uiv3 & CharacterMasksUI3.INVENTORY_SLOTS) == CharacterMasksUI3.INVENTORY_SLOTS)
		{
			_invBlockSize = 7 + _blockSize;
			_fullBlockSize += _invBlockSize;
		}
		
		if ((_uiv3 & CharacterMasksUI3.UNKNOWN_1) == CharacterMasksUI3.UNKNOWN_1)
		{
			_unk1BlockSize = 7 + _blockSize;
			_fullBlockSize += _unk1BlockSize;
		}
		
		// When will be needed
		// if ((_uiv3 & CharacterMasksUI3.UNDEFINED) == CharacterMasksUI3.UNDEFINED)
		// {
		// _undBlockSize = 0 + 2;
		// _fullBlockSize += _undBlockSize;
		// }
	}
}