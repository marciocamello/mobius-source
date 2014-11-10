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
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.instances.DecoyInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.skills.effects.EffectCubic;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharInfo extends L2GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(CharInfo.class);
	private int[][] _inv;
	private int _mAtkSpd;
	private int _pAtkSpd;
	private int _runSpd;
	private int _walkSpd;
	private int _swimSpd;
	private int _flRunSpd;
	private int _flWalkSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private Location _loc;
	private Location _fishLoc;
	private String _name;
	private String _title;
	private int _objId;
	private int _race;
	private int _sex;
	private int _baseClass;
	private int _PvpFlag;
	private int _karma;
	private int _recomHave;
	private double _movementSpeedMultiplier;
	private double _attackSpeedMultiplier;
	private double _colRadius;
	private double _colHeight;
	private int _hairStyle;
	private int _hairColor;
	private int _face;
	private int _clanId;
	private int _clanCrestId;
	private int _largeClanCrestId;
	private int _allyId;
	private int _allyCrestId;
	private int _classId;
	private int _sit;
	private int _run;
	private int _combat;
	private int _dead;
	private int _privateStore;
	private int _enchant;
	private int _noble;
	private int _hero;
	private int _fishing;
	private int _mountType;
	private int _pledgeClass;
	private int _pledgeType;
	private int _clanRepScore;
	private int _cwLevel;
	private int _mountNpcId;
	private int _nameColor;
	private int _title_color;
	private int _transformation;
	private int _agathionId;
	private int _clanBoatObjectId;
	private EffectCubic[] _cubics;
	private boolean _isPartyRoomLeader;
	private boolean _isFlying;
	private TeamType _team;
	private int _curHP;
	private int _maxHP;
	private int _curMP;
	private int _maxMP;
	private int _curCP;
	private List<Integer> _aveList;
	private PcInventory _inventory;
	private int _talismanCount;
	private boolean _openCloak;
	
	public CharInfo(Player cha)
	{
		this((Creature) cha);
	}
	
	public CharInfo(DecoyInstance cha)
	{
		this((Creature) cha);
	}
	
	public CharInfo(Creature cha)
	{
		if (cha == null)
		{
			System.out.println("CharInfo: cha is null!");
			Thread.dumpStack();
			return;
		}
		
		if (cha.isInvisible())
		{
			return;
		}
		
		if (cha.isDeleted())
		{
			return;
		}
		
		Player player = cha.getPlayer();
		
		if (player == null)
		{
			return;
		}
		
		if (player.isInBoat())
		{
			_loc = player.getInBoatPosition();
			
			if (player.isClanAirShipDriver())
			{
				_clanBoatObjectId = player.getBoat().getBoatId();
			}
		}
		
		if (_loc == null)
		{
			_loc = cha.getLoc();
		}
		
		_objId = cha.getObjectId();
		
		if ((player.getTransformationName() != null) || (((player.getReflection() == ReflectionManager.GIRAN_HARBOR) || (player.getReflection() == ReflectionManager.PARNASSUS)) && (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)))
		{
			_name = player.getTransformationName() != null ? player.getTransformationName() : player.getName();
			_title = "";
			_clanId = 0;
			_clanCrestId = 0;
			_allyId = 0;
			_allyCrestId = 0;
			_largeClanCrestId = 0;
			
			if (player.isCursedWeaponEquipped())
			{
				_cwLevel = CursedWeaponsManager.getInstance().getLevel(player.getCursedWeaponEquippedId());
			}
		}
		else
		{
			_name = player.getName();
			
			if (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
			{
				_title = "";
			}
			else if (!player.isConnected())
			{
				_title = "NO CARRIER";
				_title_color = 255;
			}
			else
			{
				_title = player.getTitle();
				_title_color = player.getTitleColor();
			}
			
			Clan clan = player.getClan();
			Alliance alliance = clan == null ? null : clan.getAlliance();
			//
			_clanId = clan == null ? 0 : clan.getClanId();
			_clanCrestId = clan == null ? 0 : clan.getCrestId();
			_largeClanCrestId = clan == null ? 0 : clan.getCrestLargeId();
			//
			_allyId = alliance == null ? 0 : alliance.getAllyId();
			_allyCrestId = alliance == null ? 0 : alliance.getAllyCrestId();
			_cwLevel = 0;
		}
		
		if (player.isMounted())
		{
			_enchant = 0;
			_mountNpcId = player.getMountNpcId() + 1000000;
			_mountType = player.getMountType();
		}
		else
		{
			_enchant = player.getEnchantEffect();
			_mountNpcId = 0;
			_mountType = 0;
		}
		
		_inv = new int[Inventory.PAPERDOLL_MAX][2];
		
		for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
		{
			_inv[PAPERDOLL_ID][0] = player.getInventory().getPaperdollItemId(PAPERDOLL_ID);
			_inv[PAPERDOLL_ID][1] = player.getInventory().getPaperdollAugmentationId(PAPERDOLL_ID);
		}
		
		_mAtkSpd = player.getMAtkSpd();
		_pAtkSpd = player.getPAtkSpd();
		_movementSpeedMultiplier = player.getMovementSpeedMultiplier();
		_runSpd = (int) (player.getRunSpeed() / _movementSpeedMultiplier);
		_walkSpd = (int) (player.getWalkSpeed() / _movementSpeedMultiplier);
		_flRunSpd = 0; // TODO
		_flWalkSpd = 0; // TODO
		
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
		
		_swimSpd = player.getSwimSpeed();
		_race = player.getRace().ordinal();
		_sex = player.getSex();
		_baseClass = player.getBaseClassId();
		_PvpFlag = player.getPvpFlag();
		_karma = player.getKarma();
		_attackSpeedMultiplier = player.getAttackSpeedMultiplier();
		_colRadius = player.getColRadius();
		_colHeight = player.getColHeight();
		_hairStyle = player.getHairStyle();
		_hairColor = player.getHairColor();
		_face = player.getFace();
		
		if ((_clanId > 0) && (player.getClan() != null))
		{
			_clanRepScore = player.getClan().getReputationScore();
		}
		else
		{
			_clanRepScore = 0;
		}
		
		_sit = player.isSitting() ? 0 : 1; // standing = 1 sitting = 0
		_run = player.isRunning() ? 1 : 0; // running = 1 walking = 0
		_combat = player.isInCombat() ? 1 : 0;
		_dead = player.isAlikeDead() ? 1 : 0;
		_privateStore = player.isInObserverMode() ? Player.STORE_OBSERVING_GAMES : player.getPrivateStoreType();
		_cubics = player.getCubics().toArray(new EffectCubic[player.getCubics().size()]);
		_recomHave = player.isGM() ? 0 : player.getRecomHave();
		_classId = player.getClassId().getId();
		_team = player.getTeam();
		_noble = player.isNoble() ? 1 : 0; // 0x01: symbol on char menu ctrl+I
		_hero = player.isHero() || (player.isGM() && Config.GM_HERO_AURA) ? 1 : 0;
		_fishing = player.isFishing() ? 1 : 0;
		_fishLoc = player.getFishLoc();
		_nameColor = player.getNameColor(); // New C5
		_pledgeClass = player.getPledgeClass();
		_pledgeType = player.getPledgeType();
		_transformation = player.getTransformation();
		_agathionId = player.getAgathionId();
		_isPartyRoomLeader = (player.getMatchingRoom() != null) && (player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING) && (player.getMatchingRoom().getLeader() == player);
		_isFlying = player.isInFlyingTransform();
		_talismanCount = player.getTalismanCount();
		_openCloak = player.getOpenCloak();
		_curCP = (int) player.getCurrentCp();
		_curHP = (int) player.getCurrentHp();
		_maxHP = player.getMaxHp();
		_curMP = (int) player.getCurrentMp();
		_maxMP = player.getMaxMp();
		_aveList = player.getAveList();
		_inventory = player.getInventory();
	}
	
	@Override
	protected final void writeImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		if (_objId == 0)
		{
			return;
		}
		
		if (activeChar.getObjectId() == _objId)
		{
			_log.error("You cant send CharInfo about his character to active user!!!");
			return;
		}
		
		writeC(0x31);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ() + Config.CLIENT_Z_SHIFT);
		writeD(_clanBoatObjectId);
		writeD(_objId);
		writeS(_name);
		writeD(_race);
		writeD(_sex);
		writeD(_baseClass);
		
		for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
		{
			writeD(_inv[PAPERDOLL_ID][0]);
		}
		
		for (int PAPERDOLL_ID : PAPERDOLL_ORDER)
		{
			writeD(_inv[PAPERDOLL_ID][1]);
		}
		
		writeD(_talismanCount);
		writeD((_openCloak) ? 1 : 0);
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_RHAND));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_LHAND));
		writeD(0x00);
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_GLOVES));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_CHEST));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_LEGS));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_FEET));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_HAIR));
		writeD(_inventory.getVisualItemId(Inventory.PAPERDOLL_DHAIR));
		writeD(_PvpFlag);
		writeD(_karma);
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		writeD(0x00);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_swimSpd);
		writeD(_swimSpd);
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
		writeS(_title);
		writeD(_clanId);
		writeD(_clanCrestId);
		writeD(_allyId);
		writeD(_allyCrestId);
		writeC(_sit);
		writeC(_run);
		writeC(_combat);
		writeC(_dead);
		writeC(0x00); // is invisible
		writeC(_mountType); // 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
		writeC(_privateStore);
		writeH(_cubics.length);
		
		for (EffectCubic cubic : _cubics)
		{
			writeH(cubic == null ? 0 : cubic.getId());
		}
		
		writeC(_isPartyRoomLeader ? 0x01 : 0x00); // find party members
		writeC(_isFlying ? 0x02 : 0x00);
		writeH(_recomHave);
		writeD(_mountNpcId);
		writeD(_classId);
		writeD(0x00); // special effects? circles around player...
		writeC(_enchant);
		writeC(_team.ordinal()); // team circle around feet 1 = Blue, 2 = red
		writeD(_largeClanCrestId);
		writeC(_noble);
		writeC(_hero);
		writeC(_fishing);
		writeD(_fishLoc.getX());
		writeD(_fishLoc.getY());
		writeD(_fishLoc.getZ());
		writeD(_nameColor);
		writeD(_loc.getHeading());
		writeD(_pledgeClass);
		writeD(_pledgeType);
		writeD(_title_color);
		writeD(_cwLevel);
		writeD(_clanRepScore);
		writeD(_transformation);
		writeD(_agathionId);
		writeD(0x01); // T2
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(_curCP);
		writeD(_curHP);
		writeD(_maxHP);
		writeD(_curMP);
		writeD(_maxMP);
		writeD(0x00);
		writeD(0x00);
		writeC(0x00);
		
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
	
	private static final int[] PAPERDOLL_ORDER =
	{
		Inventory.PAPERDOLL_UNDER,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_BACK,
		Inventory.PAPERDOLL_LRHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_DHAIR,
		Inventory.PAPERDOLL_RBRACELET,
		Inventory.PAPERDOLL_LBRACELET,
		Inventory.PAPERDOLL_DECO1,
		Inventory.PAPERDOLL_DECO2,
		Inventory.PAPERDOLL_DECO3,
		Inventory.PAPERDOLL_DECO4,
		Inventory.PAPERDOLL_DECO5,
		Inventory.PAPERDOLL_DECO6,
		Inventory.PAPERDOLL_BELT
	};
}