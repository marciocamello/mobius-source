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
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

public class UserInfo extends L2GameServerPacket
{
	private final Player _activeChar;
	private final boolean _partyRoom;
	private final int _relation;
	private final double _movementSpeedMultiplier;
	private final int _vehicleObjId;
	private final int _clanCrestId;
	private final int _allyCrestId;
	private final int _largeClanCrestId;
	private int data_name_Err = 0;
	private byte[] data_name;
	private int data_title_Err = 0;
	private byte[] data_title;
	
	public UserInfo(Player player)
	{
		_activeChar = player;
		_relation = _activeChar.isClanLeader() ? 0x40 : 0;
		_vehicleObjId = player.isInBoat() ? player.getBoat().getObjectId() : 0x00;
		_movementSpeedMultiplier = player.getMovementSpeedMultiplier();
		_partyRoom = (player.getMatchingRoom() != null) && (player.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING) && (player.getMatchingRoom().getLeader() == player);
		Clan clan = player.getClan();
		Alliance alliance = clan == null ? null : clan.getAlliance();
		
		_clanCrestId = clan == null ? 0 : clan.getCrestId();
		_largeClanCrestId = clan == null ? 0 : clan.getCrestLargeId();
		
		_allyCrestId = alliance == null ? 0 : alliance.getAllyCrestId();
		_activeChar.sendPacket(new ExUserInfoEquipSlot(_activeChar));
		_activeChar.sendPacket(new ExUserInfoCubic(_activeChar));
		_activeChar.sendPacket(new ExUserInfoAbnormalVisualEffect(_activeChar));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x32);
		writeD(_activeChar.getObjectId());
		
		String user_name = _activeChar.getName();
		int len_user_name = _activeChar.getName().length();
		if (len_user_name > 0)
		{
			try
			{
				data_name = user_name.getBytes("UTF-16LE");
			}
			catch (Exception e)
			{
				data_name_Err = 2;
			}
		}
		
		String title = _activeChar.getTitle();
		// if (_activeChar.isInvisible())
		// {
		// title = "Invisible";
		// }
		
		if (_activeChar.isPolymorphed())
		{
			if (NpcHolder.getInstance().getTemplate(_activeChar.getPolyId()) != null)
			{
				title += " (" + NpcHolder.getInstance().getTemplate(_activeChar.getPolyId()).name + ")";
			}
		}
		
		int len_user_title = title.length();
		if (len_user_title > 0)
		{
			try
			{
				data_title = title.getBytes("UTF-16LE");
			}
			catch (Exception e)
			{
				data_title_Err = 2;
			}
		}
		
		writeD(377 + (len_user_name * 2) + data_name_Err + (len_user_title * 2) + data_title_Err);
		writeH(23);
		writeH(65535);
		writeC(254);
		writeH(_relation);
		writeH(0);
		
		writeH(16 + (len_user_name * 2) + data_name_Err);
		writeH(len_user_name + (data_name_Err / 2));
		if (len_user_name > 0)
		{
			if (data_name_Err == 0)
			{
				writeB(data_name);
			}
			else
			{
				writeS(_activeChar.getName());
			}
		}
		writeC(_activeChar.isGM() ? 1 : 0);
		writeC(_activeChar.getRace().ordinal());
		writeC(_activeChar.getSex());
		writeD(_activeChar.getBaseClassId());
		writeD(_activeChar.getClassId().getId());
		writeC(_activeChar.getLevel());
		
		writeH(18);
		writeH(_activeChar.getSTR());
		writeH(_activeChar.getDEX());
		writeH(_activeChar.getCON());
		writeH(_activeChar.getINT());
		writeH(_activeChar.getWIT());
		writeH(_activeChar.getMEN());
		writeH(_activeChar.getLUC());
		writeH(_activeChar.getCHA());
		
		writeH(14);
		writeD(_activeChar.getMaxHp());
		writeD(_activeChar.getMaxMp());
		writeD(_activeChar.getMaxCp());
		
		writeH(38);
		writeD((int) Math.round(_activeChar.getCurrentHp()));
		writeD((int) Math.round(_activeChar.getCurrentMp()));
		writeD((int) _activeChar.getCurrentCp());
		writeQ(_activeChar.getSp());
		writeQ(_activeChar.getExp());
		writeF(Experience.getExpPercent(_activeChar.getLevel(), _activeChar.getExp()));
		
		writeH(4);
		writeC(_activeChar.isMounted() || (_vehicleObjId != 0) ? 0 : _activeChar.getEnchantEffect());
		writeC(0);
		
		writeH(15);
		writeD(_activeChar.getHairStyle());
		writeD(_activeChar.getHairColor());
		writeD(_activeChar.getFace());
		writeC(1);
		
		writeH(6);
		writeC(0);
		writeC(_activeChar.getPrivateStoreType());
		writeC((_activeChar.getSkillLevel(Skill.SKILL_CRYSTALLIZE) > 0) || _activeChar.isAwaking() ? 1 : 0);
		writeC(0);
		
		writeH(56);
		if (_activeChar.getActiveWeaponItem() != null)
		{
			WeaponTemplate weaponItem = _activeChar.getActiveWeaponItem();
			if (weaponItem.getItemType() == WeaponType.POLE)
			{
				writeH(80);
			}
			else if (weaponItem.getItemType() == WeaponType.BOW)
			{
				writeH(500);
			}
			else if (weaponItem.getItemType() == WeaponType.CROSSBOW)
			{
				writeH(400);
			}
			else
			{
				writeH(40);
			}
		}
		else
		{
			writeH(0);
		}
		writeD(_activeChar.getPAtk(null));
		writeD(_activeChar.getPAtkSpd());
		writeD(_activeChar.getPDef(null));
		writeD(_activeChar.getEvasionRate(null));
		writeD(_activeChar.getAccuracy());
		writeD(_activeChar.getCriticalHit(null, null));
		writeD(_activeChar.getMAtk(null, null));
		writeD(_activeChar.getMAtkSpd());
		writeD(_activeChar.getPAtkSpd());
		writeD(_activeChar.getEvasionRate(null)); // Magic
		writeD(_activeChar.getMDef(null, null));
		writeD(_activeChar.getAccuracy()); // Magic
		writeD((int) _activeChar.getMagicCriticalRate(null, null)); // Magic
		
		writeH(14);
		writeH(_activeChar.getDefence(Element.FIRE));
		writeH(_activeChar.getDefence(Element.WATER));
		writeH(_activeChar.getDefence(Element.WIND));
		writeH(_activeChar.getDefence(Element.EARTH));
		writeH(_activeChar.getDefence(Element.HOLY));
		writeH(_activeChar.getDefence(Element.UNHOLY));
		
		writeH(18);
		writeD(_activeChar.getX());
		writeD(_activeChar.getY());
		writeD(_activeChar.getZ());
		writeD(_activeChar.getBoat() != null ? _activeChar.getBoat().getObjectId() : 0);
		
		writeH(18);
		writeH((int) (_activeChar.getRunSpeed() / _movementSpeedMultiplier));
		writeH((int) (_activeChar.getWalkSpeed() / _movementSpeedMultiplier));
		writeH(_activeChar.getSwimRunSpeed());
		writeH(_activeChar.getSwimWalkSpeed());
		writeH((int) _activeChar.getTemplate().getBaseFlyRunSpd());
		writeH((int) _activeChar.getTemplate().getBaseFlyWalkSpd());
		writeH((int) _activeChar.getTemplate().getBaseFlyRunSpd());
		writeH((int) _activeChar.getTemplate().getBaseFlyWalkSpd());
		
		writeH(18);
		writeF(_movementSpeedMultiplier);
		writeF(_activeChar.getAttackSpeedMultiplier());
		
		writeH(18);
		writeF(_activeChar.getCollisionRadius());
		writeF(_activeChar.getCollisionHeight());
		
		writeH(5);
		Element attackAttribute = _activeChar.getAttackElement();
		writeC(attackAttribute.getId());
		writeH(_activeChar.getAttack(attackAttribute));
		
		writeH(32 + (len_user_title * 2) + data_title_Err);
		writeH(len_user_title + (data_title_Err / 2));
		if (len_user_title > 0)
		{
			if (data_title_Err == 0)
			{
				writeB(data_title);
			}
			else
			{
				writeS(title);
			}
		}
		writeH(_activeChar.getPledgeType());
		writeD(_activeChar.getClanId());
		writeD(_largeClanCrestId);
		writeD(_clanCrestId);
		writeC(_activeChar.getClanPrivileges());
		writeH(0);
		writeC(0);
		writeC(_activeChar.isClanLeader() ? 1 : 0);
		writeD(_activeChar.getAllyId());
		writeD(_allyCrestId);
		writeC(_partyRoom ? 0x01 : 0x00);
		
		writeH(22);
		writeC(_activeChar.getPvpFlag());
		int Karma = 0 - _activeChar.getKarma();
		writeD(Karma);
		writeC(_activeChar.isNoble() ? 1 : 0);
		writeC(_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) ? 1 : 0);
		writeC(_activeChar.getPledgeClass());
		writeD(_activeChar.getPkKills());
		writeD(_activeChar.getPvpKills());
		writeH(_activeChar.getRecomLeft());
		writeH(_activeChar.getRecomHave());
		
		writeH(15);
		writeD(_activeChar.getVitality());
		writeC(0);
		writeD(_activeChar.getFame());
		writeD(0);
		
		writeH(9);
		writeC(_activeChar.getTalismanCount());
		writeC(0);
		writeC(_activeChar.getTeam().ordinal());
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		
		writeH(4);
		writeC(_activeChar.isInZone(ZoneType.Water) ? 1 : _activeChar.isFlying() ? 2 : 0);
		writeC(_activeChar.isRunning() ? 0x01 : 0x00);
		
		writeH(10);
		writeD(_activeChar.getNameColor());
		writeD(_activeChar.getTitleColor());
		
		writeH(9);
		writeD(0);
		writeH(_activeChar.getInventoryLimit());
		writeC(_activeChar.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquippedId()) : 0);
		
		writeH(9);
		writeD(1);
		writeH(0);
		writeC(0);
	}
}