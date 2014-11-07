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
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.instances.DecoyInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.skills.effects.EffectCubic;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

public class CharInfo extends L2GameServerPacket
{
	private Player _activeChar;
	private Location _loc;
	private String _name;
	private String _title;
	private int _title_color;
	private int _objId;
	private int _runSpd;
	private int _walkSpd;
	private int _swimSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private double _movementSpeedMultiplier;
	private double _attackSpeedMultiplier;
	private int _clanId;
	private int _clanCrestId;
	private int _largeClanCrestId;
	private int _allyId;
	private int _allyCrestId;
	private int _clanBoatObjectId;
	private EffectCubic[] _cubics;
	private ArrayList<Integer> _aveList;
	
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
		if (cha.isInvisible())
		{
			return;
		}
		
		if (cha.isDeleted())
		{
			return;
		}
		
		_activeChar = cha.getPlayer();
		if (_activeChar == null)
		{
			return;
		}
		
		if (_activeChar.isInBoat())
		{
			_loc = _activeChar.getInBoatPosition();
			
			if (_activeChar.isClanAirShipDriver())
			{
				_clanBoatObjectId = _activeChar.getBoat().getBoatId();
			}
		}
		
		if (_loc == null)
		{
			_loc = cha.getLoc();
		}
		
		_objId = cha.getObjectId();
		
		if ((_activeChar.getTransformationName() != null) || (((_activeChar.getReflection() == ReflectionManager.GIRAN_HARBOR) || (_activeChar.getReflection() == ReflectionManager.PARNASSUS)) && (_activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)))
		{
			_name = _activeChar.getTransformationName() != null ? _activeChar.getTransformationName() : _activeChar.getName();
			_title = "";
			_clanId = 0;
			_clanCrestId = 0;
			_allyId = 0;
			_allyCrestId = 0;
			_largeClanCrestId = 0;
		}
		else
		{
			_name = _activeChar.getName();
			
			if (_activeChar.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
			{
				_title = "";
			}
			else if (!_activeChar.isConnected())
			{
				_title = "NO CARRIER";
				_title_color = 255;
			}
			else
			{
				_title = _activeChar.getTitle();
				_title_color = _activeChar.getTitleColor();
			}
			
			Clan clan = _activeChar.getClan();
			Alliance alliance = clan == null ? null : clan.getAlliance();
			
			_clanId = clan == null ? 0 : clan.getClanId();
			_clanCrestId = clan == null ? 0 : clan.getCrestId();
			_largeClanCrestId = clan == null ? 0 : clan.getCrestLargeId();
			
			_allyId = alliance == null ? 0 : alliance.getAllyId();
			_allyCrestId = alliance == null ? 0 : alliance.getAllyCrestId();
		}
		
		_attackSpeedMultiplier = _activeChar.getAttackSpeedMultiplier();
		_movementSpeedMultiplier = _activeChar.getMovementSpeedMultiplier();
		_runSpd = (int) (_activeChar.getRunSpeed() / _movementSpeedMultiplier);
		_walkSpd = (int) (_activeChar.getWalkSpeed() / _movementSpeedMultiplier);
		_swimSpd = _activeChar.getSwimSpeed();
		
		if (_activeChar.isFlying())
		{
			_flyRunSpd = _runSpd;
			_flyWalkSpd = _walkSpd;
		}
		else
		{
			_flyRunSpd = 0;
			_flyWalkSpd = 0;
		}
		
		_cubics = _activeChar.getCubics().toArray(new EffectCubic[_activeChar.getCubics().size()]);
		_aveList = _activeChar.getAveList();
	}
	
	@Override
	protected final void writeImpl()
	{
		if (_activeChar == null)
		{
			return;
		}
		
		final NpcTemplate template = _activeChar.isPolymorphed() ? NpcHolder.getInstance().getTemplate(_activeChar.getPolyId()) : null;
		if (template != null)
		{
			writeC(0x0C);
			writeD(_objId);
			writeC(0x00);
			writeC(0x25);
			writeC(0x00);
			writeC(0xED);
			if ((template.getLHandId() > 0) || (template.getRHandId() > 0) || (template.getChestId() > 0))
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
			int len_poly_title = 0;
			if (_activeChar.getTitle() != null)
			{
				len_poly_title = _activeChar.getTitle().length();
			}
			writeC(7 + (len_poly_title * 2));
			writeC(_activeChar.getKarma() < 0 ? 1 : 0);
			writeH(0);
			writeH(0);
			writeS(_activeChar.getTitle());
			if ((template.getLHandId() > 0) || (template.getRHandId() > 0) || (template.getChestId() > 0))
			{
				writeH(68);
			}
			else
			{
				writeH(56);
			}
			writeD(template.getId() + 1000000); // npctype id
			writeD(_loc.getX());
			writeD(_loc.getY());
			writeD(_loc.getZ());
			writeD(_loc.getHeading());
			writeD(_activeChar.getMAtkSpd());
			writeD(_activeChar.getPAtkSpd());
			putFloat((float) _activeChar.getMovementSpeedMultiplier());
			putFloat((float) _activeChar.getAttackSpeedMultiplier());
			if ((template.getRHandId() > 0) || (template.getChestId() > 0) || (template.getLHandId() > 0))
			{
				writeD(template.getRHandId()); // right hand weapon
				writeD(template.getChestId()); // chest
				writeD(template.getLHandId()); // left hand weapon
			}
			writeC(1);
			writeC(_activeChar.isRunning() ? 1 : 0);
			writeC(_activeChar.isInZone(ZoneType.Water) ? 1 : _activeChar.isFlying() ? 2 : 0);
			writeD(_activeChar.isFlying() ? 1 : 0);
			writeC(0);
			writeC(0);
			writeH(0);
			writeD((int) _activeChar.getCurrentHp());
			writeD(_activeChar.getMaxHp());
			
			writeC((_activeChar.isInCombat() ? 1 : 0) + (_activeChar.isAlikeDead() ? 2 : 0) + (/* template.isTargetable() ? 4 : 0 */4) + (template.isShowName() ? 8 : 0));
			
			if (_aveList != null)
			{
				writeD(_aveList.size());
				
				for (int i : _aveList)
				{
					writeD(i);
				}
			}
		}
		else
		{
			writeC(0x31);
			writeD(_activeChar.getX());
			writeD(_activeChar.getY());
			writeD(_activeChar.getZ());
			writeD(_clanBoatObjectId);
			writeD(_objId);
			writeS(_name);
			writeH(_activeChar.getRace().ordinal());
			writeC(_activeChar.getSex());
			writeD(_activeChar.getBaseClassId());
			
			for (int slot : PAPERDOLL_ORDER)
			{
				writeD(_activeChar.getInventory().getPaperdollItemId(slot));
			}
			
			writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
			writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LHAND));
			writeD(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
			
			writeC(_activeChar.getTalismanCount());
			
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			
			writeC(_activeChar.getPvpFlag());
			int Karma = 0 - _activeChar.getKarma();
			writeD(Karma);
			
			writeD(_activeChar.getMAtkSpd());
			writeD(_activeChar.getPAtkSpd());
			
			writeH(_runSpd);
			writeH(_walkSpd);
			writeH(_swimSpd);
			writeH(_swimSpd);
			writeH(_flyRunSpd);
			writeH(_flyWalkSpd);
			writeH(_flyRunSpd);
			writeH(_flyWalkSpd);
			writeF(_movementSpeedMultiplier);
			writeF(_attackSpeedMultiplier);
			
			writeF(_activeChar.getCollisionRadius());
			writeF(_activeChar.getCollisionHeight());
			
			writeD(_activeChar.getHairStyle());
			writeD(_activeChar.getHairColor());
			writeD(_activeChar.getFace());
			writeS(_title);
			
			if (!_activeChar.isCursedWeaponEquipped())
			{
				writeD(_clanId);
				writeD(_clanCrestId);
				writeD(_allyId);
				writeD(_allyCrestId);
			}
			else
			{
				writeD(0x00);
				writeD(0x00);
				writeD(0x00);
				writeD(0x00);
			}
			
			writeC(_activeChar.isSitting() ? 0 : 1); // standing = 1 sitting = 0
			writeC(_activeChar.isRunning() ? 1 : 0); // running = 1 walking = 0
			writeC(_activeChar.isInCombat() ? 1 : 0);
			
			writeC(!_activeChar.isInOlympiadMode() && _activeChar.isAlikeDead() ? 1 : 0);
			
			writeC(0x00); // FIXME: GM??? invisible = 1 visible =0
			
			writeC(_activeChar.isMounted() ? _activeChar.getMountType() : 0); // 1-on Strider, 2-on Wyvern, 3-on Great Wolf, 0-no mount
			writeC(_activeChar.isInObserverMode() ? Player.STORE_OBSERVING_GAMES : _activeChar.getPrivateStoreType());
			
			writeH(_cubics.length);
			for (EffectCubic cubic : _cubics)
			{
				writeH(cubic == null ? 0 : cubic.getId());
			}
			
			writeC((_activeChar.getMatchingRoom() != null) && (_activeChar.getMatchingRoom().getType() == MatchingRoom.PARTY_MATCHING) && (_activeChar.getMatchingRoom().getLeader() == _activeChar) ? 1 : 0);
			
			writeC(_activeChar.isInZone(ZoneType.Water) ? 1 : _activeChar.isFlying() ? 2 : 0);
			
			writeH(_activeChar.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
			writeD(_activeChar.getMountNpcId() > 0 ? _activeChar.getMountNpcId() + 1000000 : 0);
			writeD(_activeChar.getClassId().getId());
			writeD(0x00);
			writeC(_activeChar.isMounted() ? 0 : _activeChar.getEnchantEffect());
			
			writeC(_activeChar.getTeam().ordinal());
			
			writeD(_largeClanCrestId);
			writeC(_activeChar.isNoble() ? 1 : 0); // Symbol on char menu ctrl+I
			writeC(_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) ? 1 : 0); // Hero Aura
			
			writeC(_activeChar.isFishing() ? 1 : 0); // 0x01: Fishing Mode (Cant be undone by setting back to 0)
			writeD(_activeChar.getFishLoc().getX());
			writeD(_activeChar.getFishLoc().getY());
			writeD(_activeChar.getFishLoc().getZ());
			
			writeD(_activeChar.getNameColor());
			
			writeD(_loc.getHeading());
			
			writeC(_activeChar.getPledgeClass());
			writeH(_activeChar.getPledgeType());
			
			writeD(_title_color);
			
			writeC(_activeChar.isCursedWeaponEquipped() ? CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquippedId()) : 0);
			
			writeD(_activeChar.getClanId() > 0 ? _activeChar.getClan().getReputationScore() : 0);
			
			// T1
			writeD(_activeChar.getTransformation());
			writeD(_activeChar.getAgathionId());
			
			// T2
			writeC(0x01);
			
			// T2.3
			writeD((int) _activeChar.getCurrentCp());
			writeD(_activeChar.getMaxHp());
			writeD((int) _activeChar.getCurrentHp());
			writeD(_activeChar.getMaxMp());
			writeD((int) _activeChar.getCurrentMp());
			writeC(0);
			
			// java.util.List<Integer> el = _activeChar.getEffectIdList();
			// if (gmSeeInvis && !el.contains(21))
			// {
			// el.add(21);
			// }
			if (_aveList != null)
			{
				writeD(_aveList.size());
				
				for (int i : _aveList)
				{
					writeD(i);
				}
			}
			
			writeC(0);
			writeC(1);
			writeC(0);
		}
	}
	
	private static final int[] PAPERDOLL_ORDER = new int[]
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
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_DHAIR
	};
}