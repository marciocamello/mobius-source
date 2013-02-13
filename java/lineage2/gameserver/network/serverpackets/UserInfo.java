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
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.skills.effects.EffectCubic;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class UserInfo extends L2GameServerPacket
{
	/**
	 * Field can_writeImpl.
	 */
	private boolean can_writeImpl = false;
	/**
	 * Field partyRoom.
	 */
	private final boolean partyRoom;
	/**
	 * Field _swimWalkSpd. Field _swimRunSpd. Field _walkSpd. Field _runSpd.
	 */
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	/**
	 * Field _flRunSpd.
	 */
	private int _flRunSpd;
	/**
	 * Field _flWalkSpd.
	 */
	private int _flWalkSpd;
	/**
	 * Field _flyRunSpd.
	 */
	private int _flyRunSpd;
	/**
	 * Field _flyWalkSpd.
	 */
	private int _flyWalkSpd;
	/**
	 * Field _relation.
	 */
	private int _relation;
	/**
	 * Field col_height. Field col_radius. Field attack_speed. Field move_speed.
	 */
	private final double move_speed, attack_speed, col_radius, col_height;
	/**
	 * Field _inv.
	 */
	private final int[][] _inv;
	/**
	 * Field _fishLoc. Field _loc.
	 */
	private final Location _loc, _fishLoc;
	/**
	 * Field maxCp. Field curCp. Field level. Field base_class. Field sex. Field _race. Field vehicle_obj_id. Field obj_id.
	 */
	private final int obj_id, vehicle_obj_id, _race, sex, base_class, level, curCp, maxCp;
	/**
	 * Field _enchant.
	 */
	private int _enchant;
	/**
	 * Field _weaponFlag.
	 */
	private final int _weaponFlag;
	/**
	 * Field _exp.
	 */
	private final long _exp;
	/**
	 * Field rec_have. Field rec_left. Field maxLoad. Field curLoad. Field maxMp. Field curMp. Field maxHp. Field curHp.
	 */
	private final int curHp, maxHp, curMp, maxMp, curLoad, maxLoad, rec_left, rec_have;
	/**
	 * Field InventoryLimit. Field ClanPrivs. Field _sp. Field _men. Field _wit. Field _int. Field _dex. Field _con. Field _str.
	 */
	private final int _str, _con, _dex, _int, _wit, _men, _sp, ClanPrivs, InventoryLimit;
	/**
	 * Field mCritRate. Field maccuracy. Field mevasion. Field _matkspd. Field _matk. Field crit. Field accuracy. Field evasion. Field _pdef. Field _patkspd. Field _patk.
	 */
	private final int _patk, _patkspd, _pdef, evasion, accuracy, crit, _matk, _matkspd, mevasion, maccuracy, mCritRate;
	/**
	 * Field vitality. Field fame. Field gm_commands. Field face. Field hair_color. Field hair_style. Field karma. Field pvp_flag. Field _mdef.
	 */
	private final int _mdef, pvp_flag, karma, hair_style, hair_color, face, gm_commands, fame, vitality;
	/**
	 * Field large_clan_crest_id. Field ally_crest_id. Field ally_id. Field clan_crest_id. Field clan_id.
	 */
	private int clan_id, clan_crest_id, ally_id, ally_crest_id, large_clan_crest_id;
	/**
	 * Field agathion. Field class_id. Field pvp_kills. Field pk_kills. Field can_crystalize. Field private_store.
	 */
	private final int private_store, can_crystalize, pk_kills, pvp_kills, class_id, agathion;
	/**
	 * Field hero. Field noble.
	 */
	private final int noble, hero;
	/**
	 * Field mount_id.
	 */
	private int mount_id;
	/**
	 * Field cw_level.
	 */
	private int cw_level;
	/**
	 * Field fishing. Field transformation. Field title_color. Field pledge_type. Field pledge_class. Field running. Field name_color.
	 */
	private final int name_color, running, pledge_class, pledge_type, title_color, transformation, fishing;
	/**
	 * Field defenceUnholy. Field defenceHoly. Field defenceEarth. Field defenceWind. Field defenceWater. Field defenceFire.
	 */
	private final int defenceFire, defenceWater, defenceWind, defenceEarth, defenceHoly, defenceUnholy;
	/**
	 * Field mount_type.
	 */
	private int mount_type;
	/**
	 * Field title. Field _name.
	 */
	private String _name, title;
	/**
	 * Field cubics.
	 */
	private final EffectCubic[] cubics;
	/**
	 * Field attackElement.
	 */
	private final Element attackElement;
	/**
	 * Field attackElementValue.
	 */
	private final int attackElementValue;
	/**
	 * Field _allowMap. Field isFlying.
	 */
	private final boolean isFlying, _allowMap;
	/**
	 * Field talismans.
	 */
	private final int talismans;
	/**
	 * Field openCloak.
	 */
	private final boolean openCloak;
	/**
	 * Field _expPercent.
	 */
	private final double _expPercent;
	/**
	 * Field _team.
	 */
	private final TeamType _team;
	private final FastList<Integer> _aveList;
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(UserInfo.class);
	
	/**
	 * Constructor for UserInfo.
	 * @param player Player
	 */
	public UserInfo(Player player)
	{
		if (player.getTransformationName() != null)
		{
			_name = player.getTransformationName();
			title = "";
			clan_crest_id = 0;
			ally_crest_id = 0;
			large_clan_crest_id = 0;
			cw_level = CursedWeaponsManager.getInstance().getLevel(player.getCursedWeaponEquippedId());
		}
		else
		{
			_name = player.getName();
			Clan clan = player.getClan();
			Alliance alliance = clan == null ? null : clan.getAlliance();
			clan_id = clan == null ? 0 : clan.getClanId();
			clan_crest_id = clan == null ? 0 : clan.getCrestId();
			large_clan_crest_id = clan == null ? 0 : clan.getCrestLargeId();
			ally_id = alliance == null ? 0 : alliance.getAllyId();
			ally_crest_id = alliance == null ? 0 : alliance.getAllyCrestId();
			cw_level = 0;
			title = player.getTitle();
		}
		if (player.getPlayerAccess().GodMode && player.isInvisible())
		{
			title += "[I]";
		}
		if (player.isPolymorphed())
		{
			if (NpcHolder.getInstance().getTemplate(player.getPolyId()) != null)
			{
				title += " - " + NpcHolder.getInstance().getTemplate(player.getPolyId()).name;
			}
			else
			{
				title += " - Polymorphed";
			}
		}
		if (player.isMounted())
		{
			_enchant = 0;
			mount_id = player.getMountNpcId() + 1000000;
			mount_type = player.getMountType();
		}
		else
		{
			_enchant = player.getEnchantEffect();
			mount_id = 0;
			mount_type = 0;
		}
		_weaponFlag = player.getActiveWeaponInstance() == null ? 0x14 : 0x28;
		move_speed = player.getMovementSpeedMultiplier();
		_runSpd = (int) (player.getRunSpeed() / move_speed);
		_walkSpd = (int) (player.getWalkSpeed() / move_speed);
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
		_swimRunSpd = player.getSwimSpeed();
		_swimWalkSpd = player.getSwimSpeed();
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
		obj_id = player.getObjectId();
		vehicle_obj_id = player.isInBoat() ? player.getBoat().getObjectId() : 0x00;
		_race = player.getRace().ordinal();
		sex = player.getSex();
		base_class = player.getBaseClassId();
		level = player.getLevel();
		_exp = player.getExp();
		_expPercent = Experience.getExpPercent(player.getLevel(), player.getExp());
		_str = player.getSTR();
		_dex = player.getDEX();
		_con = player.getCON();
		_int = player.getINT();
		_wit = player.getWIT();
		_men = player.getMEN();
		curHp = (int) player.getCurrentHp();
		maxHp = player.getMaxHp();
		curMp = (int) player.getCurrentMp();
		maxMp = player.getMaxMp();
		curLoad = player.getCurrentLoad();
		maxLoad = player.getMaxLoad();
		_sp = player.getIntSp();
		_patk = player.getPAtk(null);
		_patkspd = player.getPAtkSpd();
		_pdef = player.getPDef(null);
		evasion = player.getEvasionRate(null);
		mevasion = player.getMEvasionRate(null);
		maccuracy = player.getMAccuracy();
		mCritRate = (int) player.getMagicCriticalRate(null, null);
		accuracy = player.getAccuracy();
		crit = player.getCriticalHit(null, null);
		_matk = player.getMAtk(null, null);
		_matkspd = player.getMAtkSpd();
		_mdef = player.getMDef(null, null);
		pvp_flag = player.getPvpFlag();
		karma = player.getKarma();
		attack_speed = player.getAttackSpeedMultiplier();
		col_radius = player.getColRadius();
		col_height = player.getColHeight();
		hair_style = player.getHairStyle();
		hair_color = player.getHairColor();
		face = player.getFace();
		gm_commands = player.isGM() || player.getPlayerAccess().CanUseGMCommand ? 1 : 0;
		clan_id = player.getClanId();
		ally_id = player.getAllyId();
		private_store = player.getPrivateStoreType();
		can_crystalize = player.getSkillLevel(Skill.SKILL_CRYSTALLIZE) > 0 ? 1 : 0;
		pk_kills = player.getPkKills();
		pvp_kills = player.getPvpKills();
		cubics = player.getCubics().toArray(new EffectCubic[player.getCubics().size()]);
		player.getAbnormalEffect();
		ClanPrivs = player.getClanPrivileges();
		rec_left = player.getRecomLeft();
		rec_have = player.getRecomHave();
		InventoryLimit = player.getInventoryLimit();
		class_id = player.getClassId().getId();
		maxCp = player.getMaxCp();
		curCp = (int) player.getCurrentCp();
		_team = player.getTeam();
		noble = player.isNoble() || (player.isGM() && Config.GM_HERO_AURA) ? 1 : 0;
		hero = player.isHero() || (player.isGM() && Config.GM_HERO_AURA) ? 1 : 0;
		_fishLoc = player.getFishLoc();
		name_color = player.getNameColor();
		running = player.isRunning() ? 0x01 : 0x00;
		pledge_class = player.getPledgeClass();
		pledge_type = player.getPledgeType();
		title_color = player.getTitleColor();
		transformation = player.getTransformation();
		attackElement = player.getAttackElement();
		attackElementValue = player.getAttack(attackElement);
		defenceFire = player.getDefence(Element.FIRE);
		defenceWater = player.getDefence(Element.WATER);
		defenceWind = player.getDefence(Element.WIND);
		defenceEarth = player.getDefence(Element.EARTH);
		defenceHoly = player.getDefence(Element.HOLY);
		defenceUnholy = player.getDefence(Element.UNHOLY);
		agathion = player.getAgathionId();
		fame = player.getFame();
		vitality = player.getVitality();
		partyRoom = (player.getMatchingRoom() != null) && (player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING) && (player.getMatchingRoom().getLeader() == player);
		isFlying = player.isInFlyingTransform();
		talismans = player.getTalismanCount();
		openCloak = player.getOpenCloak();
		_allowMap = player.isActionBlocked(Zone.BLOCKED_ACTION_MINIMAP);
		fishing = player.isFishing() ? 1 : 0;
		can_writeImpl = true;
		_aveList = player.getAveList();
	}
	
	/**
	 * Method writeImpl.
	 */
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
		writeC(0x32);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z + Config.CLIENT_Z_SHIFT);
		writeD(vehicle_obj_id);
		writeD(obj_id);
		writeS(_name);
		writeD(_race);
		writeD(sex);
		writeD(base_class);
		writeD(level);
		writeQ(_exp);
		writeF(_expPercent);
		writeD(_str);
		writeD(_dex);
		writeD(_con);
		writeD(_int);
		writeD(_wit);
		writeD(_men);
		writeD(maxHp);
		writeD(curHp);
		writeD(maxMp);
		writeD(curMp);
		writeD(_sp);
		writeD(curLoad);
		writeD(maxLoad);
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
		writeD(talismans);
		writeD(openCloak ? 0x01 : 0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		if (getClient().getRevision() == 448)
		{
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
		}
		writeD(_patk);
		writeD(_patkspd);
		writeD(_pdef);
		writeD(evasion);
		writeD(accuracy);
		writeD(crit);
		writeD(_matk);
		writeD(_matkspd);
		writeD(_patkspd);
		writeD(_mdef);
		writeD(mevasion);
		writeD(maccuracy);
		writeD(mCritRate);
		writeD(pvp_flag);
		writeD(karma);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_swimRunSpd);
		writeD(_swimWalkSpd);
		writeD(_flRunSpd);
		writeD(_flWalkSpd);
		writeD(_flyRunSpd);
		writeD(_flyWalkSpd);
		writeF(move_speed);
		writeF(attack_speed);
		writeF(col_radius);
		writeF(col_height);
		writeD(hair_style);
		writeD(hair_color);
		writeD(face);
		writeD(gm_commands);
		writeS(title);
		writeD(clan_id);
		writeD(clan_crest_id);
		writeD(ally_id);
		writeD(ally_crest_id);
		writeD(_relation);
		writeC(mount_type);
		writeC(private_store);
		writeC(can_crystalize);
		writeD(pk_kills);
		writeD(pvp_kills);
		writeH(cubics.length);
		for (EffectCubic cubic : cubics)
		{
			writeH(cubic == null ? 0 : cubic.getId());
		}
		writeC(partyRoom ? 0x01 : 0x00);
		writeC(isFlying ? 0x02 : 0x00);
		writeD(ClanPrivs);
		writeH(rec_left);
		writeH(rec_have);
		writeD(mount_id);
		writeH(InventoryLimit);
		writeD(class_id);
		writeD(0x00);
		writeD(maxCp);
		writeD(curCp);
		writeC(_enchant);
		writeC(_team.ordinal());
		writeD(large_clan_crest_id);
		writeC(noble);
		writeC(hero);
		writeC(fishing);
		writeD(_fishLoc.x);
		writeD(_fishLoc.y);
		writeD(_fishLoc.z);
		writeD(name_color);
		writeC(running);
		writeD(pledge_class);
		writeD(pledge_type);
		writeD(title_color);
		writeD(cw_level);
		writeD(transformation);
		writeH(attackElement.getId());
		writeH(attackElementValue);
		writeH(defenceFire);
		writeH(defenceWater);
		writeH(defenceWind);
		writeH(defenceEarth);
		writeH(defenceHoly);
		writeH(defenceUnholy);
		writeD(agathion);
		writeD(fame);
		writeD(_allowMap ? 1 : 0);
		writeD(vitality);
		writeD(0x00);
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
}
