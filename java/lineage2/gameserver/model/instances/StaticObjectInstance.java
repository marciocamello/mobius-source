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
package lineage2.gameserver.model.instances;

import java.util.Collections;
import java.util.List;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.reference.L2Reference;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MyTargetSelected;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.ShowTownMap;
import lineage2.gameserver.network.serverpackets.StaticObject;
import lineage2.gameserver.scripts.Events;
import lineage2.gameserver.templates.StaticObjectTemplate;
import lineage2.gameserver.utils.Location;

public class StaticObjectInstance extends GameObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final HardReference<StaticObjectInstance> reference;
	private final StaticObjectTemplate _template;
	private int _meshIndex;
	
	public StaticObjectInstance(int objectId, StaticObjectTemplate template)
	{
		super(objectId);
		_template = template;
		reference = new L2Reference<>(this);
	}
	
	@Override
	public HardReference<StaticObjectInstance> getRef()
	{
		return reference;
	}
	
	public int getUId()
	{
		return _template.getUId();
	}
	
	public int getType()
	{
		return _template.getType();
	}
	
	@Override
	public void onAction(Player player, boolean shift)
	{
		if (Events.onAction(player, this, shift))
		{
			return;
		}
		if (player.getTarget() != this)
		{
			player.setTarget(this);
			player.sendPacket(new MyTargetSelected(getObjectId(), 0));
			return;
		}
		MyTargetSelected my = new MyTargetSelected(getObjectId(), 0);
		player.sendPacket(my);
		if (!isInRange(player, 150))
		{
			if (player.getAI().getIntention() != CtrlIntention.AI_INTENTION_INTERACT)
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this, null);
			}
			return;
		}
		if (_template.getType() == 0)
		{
			player.sendPacket(new NpcHtmlMessage(player, getUId(), "newspaper/arena.htm", 0));
		}
		else if (_template.getType() == 2)
		{
			player.sendPacket(new ShowTownMap(_template.getFilePath(), _template.getMapX(), _template.getMapY()));
			player.sendActionFailed();
		}
	}
	
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		return Collections.<L2GameServerPacket> singletonList(new StaticObject(this));
	}
	
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return false;
	}
	
	public void broadcastInfo(boolean force)
	{
		StaticObject p = new StaticObject(this);
		for (Player player : World.getAroundPlayers(this))
		{
			player.sendPacket(p);
		}
	}
	
	@Override
	public int getGeoZ(Location loc)
	{
		return loc.z;
	}
	
	public int getMeshIndex()
	{
		return _meshIndex;
	}
	
	public void setMeshIndex(int meshIndex)
	{
		_meshIndex = meshIndex;
	}
}
