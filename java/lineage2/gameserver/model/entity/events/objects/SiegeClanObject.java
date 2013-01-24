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
package lineage2.gameserver.model.entity.events.objects;

import java.io.Serializable;
import java.util.Comparator;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;

public class SiegeClanObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static class SiegeClanComparatorImpl implements Comparator<SiegeClanObject>
	{
		private static final SiegeClanComparatorImpl _instance = new SiegeClanComparatorImpl();
		
		public static SiegeClanComparatorImpl getInstance()
		{
			return _instance;
		}
		
		@Override
		public int compare(SiegeClanObject o1, SiegeClanObject o2)
		{
			return (o2.getParam() < o1.getParam()) ? -1 : ((o2.getParam() == o1.getParam()) ? 0 : 1);
		}
	}
	
	private String _type;
	private final Clan _clan;
	private NpcInstance _flag;
	private final long _date;
	
	public SiegeClanObject(String type, Clan clan, long param)
	{
		this(type, clan, 0, System.currentTimeMillis());
	}
	
	public SiegeClanObject(String type, Clan clan, long param, long date)
	{
		_type = type;
		_clan = clan;
		_date = date;
	}
	
	public int getObjectId()
	{
		return _clan.getClanId();
	}
	
	public Clan getClan()
	{
		return _clan;
	}
	
	public NpcInstance getFlag()
	{
		return _flag;
	}
	
	public void deleteFlag()
	{
		if (_flag != null)
		{
			_flag.deleteMe();
			_flag = null;
		}
	}
	
	public void setFlag(NpcInstance npc)
	{
		_flag = npc;
	}
	
	public void setType(String type)
	{
		_type = type;
	}
	
	public String getType()
	{
		return _type;
	}
	
	public void broadcast(IStaticPacket... packet)
	{
		getClan().broadcastToOnlineMembers(packet);
	}
	
	public void broadcast(L2GameServerPacket... packet)
	{
		getClan().broadcastToOnlineMembers(packet);
	}
	
	public void setEvent(boolean start, SiegeEvent<?, ?> event)
	{
		if (start)
		{
			for (Player player : _clan.getOnlineMembers(0))
			{
				player.addEvent(event);
				player.broadcastCharInfo();
			}
		}
		else
		{
			for (Player player : _clan.getOnlineMembers(0))
			{
				player.removeEvent(event);
				player.getEffectList().stopEffect(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME);
				player.broadcastCharInfo();
			}
		}
	}
	
	public boolean isParticle(Player player)
	{
		return true;
	}
	
	public long getParam()
	{
		return 0;
	}
	
	public long getDate()
	{
		return _date;
	}
}
