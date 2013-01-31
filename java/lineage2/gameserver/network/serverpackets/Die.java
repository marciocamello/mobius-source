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

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.pledge.Clan;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Die extends L2GameServerPacket
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _fake.
	 */
	private final boolean _fake;
	/**
	 * Field _sweepable.
	 */
	private boolean _sweepable;
	/**
	 * Field _types.
	 */
	private final Map<RestartType, Boolean> _types = new HashMap<>(RestartType.VALUES.length);
	
	/**
	 * Constructor for Die.
	 * @param cha Creature
	 */
	public Die(Creature cha)
	{
		_objectId = cha.getObjectId();
		_fake = !cha.isDead();
		if (cha.isMonster())
		{
			_sweepable = ((MonsterInstance) cha).isSweepActive();
		}
		else if (cha.isPlayer())
		{
			Player player = (Player) cha;
			put(RestartType.FIXED, player.getPlayerAccess().ResurectFixed || (((player.getInventory().getCountOf(10649) > 0) || (player.getInventory().getCountOf(13300) > 0)) && !player.isOnSiegeField()));
			put(RestartType.AGATHION, player.isAgathionResAvailable());
			put(RestartType.TO_VILLAGE, true);
			Clan clan = null;
			if (get(RestartType.TO_VILLAGE))
			{
				clan = player.getClan();
			}
			if (clan != null)
			{
				put(RestartType.TO_CLANHALL, clan.getHasHideout() > 0);
				put(RestartType.TO_CASTLE, clan.getCastle() > 0);
				put(RestartType.TO_FORTRESS, clan.getHasFortress() > 0);
			}
			for (GlobalEvent e : cha.getEvents())
			{
				e.checkRestartLocs(player, _types);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		if (_fake)
		{
			return;
		}
		writeC(0x00);
		writeD(_objectId);
		writeD(get(RestartType.TO_VILLAGE));
		writeD(get(RestartType.TO_CLANHALL));
		writeD(get(RestartType.TO_CASTLE));
		writeD(get(RestartType.TO_FLAG));
		writeD(_sweepable ? 0x01 : 0x00);
		writeD(get(RestartType.FIXED));
		writeD(get(RestartType.TO_FORTRESS));
		writeC(0);
		writeD(get(RestartType.AGATHION));
		writeD(0x00);
	}
	
	/**
	 * Method put.
	 * @param t RestartType
	 * @param b boolean
	 */
	private void put(RestartType t, boolean b)
	{
		_types.put(t, b);
	}
	
	/**
	 * Method get.
	 * @param t RestartType
	 * @return boolean
	 */
	private boolean get(RestartType t)
	{
		Boolean b = _types.get(t);
		return (b != null) && b;
	}
}
