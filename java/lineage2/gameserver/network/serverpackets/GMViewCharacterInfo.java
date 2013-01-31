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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GMViewCharacterInfo extends L2GameServerPacket
{
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field _inv.
	 */
	private final int[][] _inv;
	/**
	 * Field mount_type. Field level. Field karma. Field pvp_flag. Field class_id. Field _sex. Field _race. Field obj_id.
	 */
	private final int obj_id, _race, _sex, class_id, pvp_flag, karma, level, mount_type;
	/**
	 * Field _sp. Field _men. Field _wit. Field _int. Field _dex. Field _con. Field _str.
	 */
	private final int _str, _con, _dex, _int, _wit, _men, _sp;
	/**
	 * Field rec_have. Field rec_left. Field maxLoad. Field curLoad. Field maxCp. Field curCp. Field maxMp. Field curMp. Field maxHp. Field curHp.
	 */
	private final int curHp, maxHp, curMp, maxMp, curCp, maxCp, curLoad, maxLoad, rec_left, rec_have;
	/**
	 * Field _matkspd. Field _matk. Field crit. Field accuracy. Field evasion. Field _pdef. Field _patkspd. Field _patk.
	 */
	private final int _patk, _patkspd, _pdef, evasion, accuracy, crit, _matk, _matkspd;
	/**
	 * Field gm_commands. Field face. Field hair_color. Field hair_style. Field _mdef.
	 */
	private final int _mdef, hair_style, hair_color, face, gm_commands;
	/**
	 * Field title_color. Field ally_id. Field clan_crest_id. Field clan_id.
	 */
	private final int clan_id, clan_crest_id, ally_id, title_color;
	/**
	 * Field pvp_kills. Field pk_kills. Field name_color. Field private_store. Field hero. Field noble.
	 */
	private final int noble, hero, private_store, name_color, pk_kills, pvp_kills;
	/**
	 * Field pledge_class. Field running. Field DwarvenCraftLevel. Field _swimSpd. Field _walkSpd. Field _runSpd.
	 */
	private final int _runSpd, _walkSpd, _swimSpd, DwarvenCraftLevel, running, pledge_class;
	/**
	 * Field title. Field _name.
	 */
	private final String _name, title;
	/**
	 * Field _exp.
	 */
	private final long _exp;
	/**
	 * Field col_height. Field col_radius. Field attack_speed. Field move_speed.
	 */
	private final double move_speed, attack_speed, col_radius, col_height;
	/**
	 * Field attackElement.
	 */
	private final Element attackElement;
	/**
	 * Field attackElementValue.
	 */
	private final int attackElementValue;
	/**
	 * Field defenceUnholy. Field defenceHoly. Field defenceEarth. Field defenceWind. Field defenceWater. Field defenceFire.
	 */
	private final int defenceFire, defenceWater, defenceWind, defenceEarth, defenceHoly, defenceUnholy;
	/**
	 * Field vitality. Field fame.
	 */
	private final int fame, vitality;
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
	 * Constructor for GMViewCharacterInfo.
	 * @param cha Player
	 */
	public GMViewCharacterInfo(final Player cha)
	{
		_loc = cha.getLoc();
		obj_id = cha.getObjectId();
		_name = cha.getName();
		_race = cha.getRace().ordinal();
		_sex = cha.getSex();
		class_id = cha.getClassId().getId();
		level = cha.getLevel();
		_exp = cha.getExp();
		_str = cha.getSTR();
		_dex = cha.getDEX();
		_con = cha.getCON();
		_int = cha.getINT();
		_wit = cha.getWIT();
		_men = cha.getMEN();
		curHp = (int) cha.getCurrentHp();
		maxHp = cha.getMaxHp();
		curMp = (int) cha.getCurrentMp();
		maxMp = cha.getMaxMp();
		_sp = cha.getIntSp();
		curLoad = cha.getCurrentLoad();
		maxLoad = cha.getMaxLoad();
		_patk = cha.getPAtk(null);
		_patkspd = cha.getPAtkSpd();
		_pdef = cha.getPDef(null);
		evasion = cha.getEvasionRate(null);
		accuracy = cha.getAccuracy();
		crit = cha.getCriticalHit(null, null);
		_matk = cha.getMAtk(null, null);
		_matkspd = cha.getMAtkSpd();
		_mdef = cha.getMDef(null, null);
		pvp_flag = cha.getPvpFlag();
		karma = cha.getKarma();
		_runSpd = cha.getRunSpeed();
		_walkSpd = cha.getWalkSpeed();
		_swimSpd = cha.getSwimSpeed();
		move_speed = cha.getMovementSpeedMultiplier();
		attack_speed = cha.getAttackSpeedMultiplier();
		mount_type = cha.getMountType();
		col_radius = cha.getColRadius();
		col_height = cha.getColHeight();
		hair_style = cha.getHairStyle();
		hair_color = cha.getHairColor();
		face = cha.getFace();
		gm_commands = cha.isGM() ? 1 : 0;
		title = cha.getTitle();
		_expPercent = Experience.getExpPercent(cha.getLevel(), cha.getExp());
		Clan clan = cha.getClan();
		Alliance alliance = clan == null ? null : clan.getAlliance();
		clan_id = clan == null ? 0 : clan.getClanId();
		clan_crest_id = clan == null ? 0 : clan.getCrestId();
		ally_id = alliance == null ? 0 : alliance.getAllyId();
		private_store = cha.isInObserverMode() ? Player.STORE_OBSERVING_GAMES : cha.getPrivateStoreType();
		DwarvenCraftLevel = Math.max(cha.getSkillLevel(1320), 0);
		pk_kills = cha.getPkKills();
		pvp_kills = cha.getPvpKills();
		rec_left = cha.getRecomLeft();
		rec_have = cha.getRecomHave();
		curCp = (int) cha.getCurrentCp();
		maxCp = cha.getMaxCp();
		running = cha.isRunning() ? 0x01 : 0x00;
		pledge_class = cha.getPledgeClass();
		noble = cha.isNoble() ? 1 : 0;
		hero = cha.isHero() ? 1 : 0;
		name_color = cha.getNameColor();
		title_color = cha.getTitleColor();
		attackElement = cha.getAttackElement();
		attackElementValue = cha.getAttack(attackElement);
		defenceFire = cha.getDefence(Element.FIRE);
		defenceWater = cha.getDefence(Element.WATER);
		defenceWind = cha.getDefence(Element.WIND);
		defenceEarth = cha.getDefence(Element.EARTH);
		defenceHoly = cha.getDefence(Element.HOLY);
		defenceUnholy = cha.getDefence(Element.UNHOLY);
		fame = cha.getFame();
		vitality = cha.getVitality();
		talismans = cha.getTalismanCount();
		openCloak = cha.getOpenCloak();
		_inv = new int[Inventory.PAPERDOLL_MAX][3];
		for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
		{
			_inv[PAPERDOLL_ID][0] = cha.getInventory().getPaperdollObjectId(PAPERDOLL_ID);
			_inv[PAPERDOLL_ID][1] = cha.getInventory().getPaperdollItemId(PAPERDOLL_ID);
			_inv[PAPERDOLL_ID][2] = cha.getInventory().getPaperdollAugmentationId(PAPERDOLL_ID);
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x95);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
		writeD(obj_id);
		writeS(_name);
		writeD(_race);
		writeD(_sex);
		writeD(class_id);
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
		writeD(pk_kills);
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
		writeD(pvp_flag);
		writeD(karma);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_swimSpd);
		writeD(_swimSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
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
		writeC(mount_type);
		writeC(private_store);
		writeC(DwarvenCraftLevel);
		writeD(pk_kills);
		writeD(pvp_kills);
		writeH(rec_left);
		writeH(rec_have);
		writeD(class_id);
		writeD(0x00);
		writeD(maxCp);
		writeD(curCp);
		writeC(running);
		writeC(321);
		writeD(pledge_class);
		writeC(noble);
		writeC(hero);
		writeD(name_color);
		writeD(title_color);
		writeH(attackElement.getId());
		writeH(attackElementValue);
		writeH(defenceFire);
		writeH(defenceWater);
		writeH(defenceWind);
		writeH(defenceEarth);
		writeH(defenceHoly);
		writeH(defenceUnholy);
		writeD(fame);
		writeD(vitality);
	}
}
