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
package lineage2.gameserver.model.actor.listener;

import lineage2.commons.listener.Listener;
import lineage2.gameserver.listener.actor.player.OnLevelChangeListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnPlayerPartyInviteListener;
import lineage2.gameserver.listener.actor.player.OnPlayerPartyLeaveListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;

public class PlayerListenerList extends CharListenerList
{
	public PlayerListenerList(Player actor)
	{
		super(actor);
	}
	
	@Override
	public Player getActor()
	{
		return (Player) actor;
	}
	
	public void onEnter()
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnPlayerEnterListener.class.isInstance(listener))
				{
					((OnPlayerEnterListener) listener).onPlayerEnter(getActor());
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnPlayerEnterListener.class.isInstance(listener))
				{
					((OnPlayerEnterListener) listener).onPlayerEnter(getActor());
				}
			}
		}
	}
	
	public void onExit()
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnPlayerExitListener.class.isInstance(listener))
				{
					((OnPlayerExitListener) listener).onPlayerExit(getActor());
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnPlayerExitListener.class.isInstance(listener))
				{
					((OnPlayerExitListener) listener).onPlayerExit(getActor());
				}
			}
		}
	}
	
	public void onTeleport(int x, int y, int z, Reflection reflection)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnTeleportListener.class.isInstance(listener))
				{
					((OnTeleportListener) listener).onTeleport(getActor(), x, y, z, reflection);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnTeleportListener.class.isInstance(listener))
				{
					((OnTeleportListener) listener).onTeleport(getActor(), x, y, z, reflection);
				}
			}
		}
	}
	
	public void onPartyInvite()
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnPlayerPartyInviteListener.class.isInstance(listener))
				{
					((OnPlayerPartyInviteListener) listener).onPartyInvite(getActor());
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnPlayerPartyInviteListener.class.isInstance(listener))
				{
					((OnPlayerPartyInviteListener) listener).onPartyInvite(getActor());
				}
			}
		}
	}
	
	public void onPartyLeave()
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnPlayerPartyLeaveListener.class.isInstance(listener))
				{
					((OnPlayerPartyLeaveListener) listener).onPartyLeave(getActor());
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnPlayerPartyLeaveListener.class.isInstance(listener))
				{
					((OnPlayerPartyLeaveListener) listener).onPartyLeave(getActor());
				}
			}
		}
	}
	
	public void onLevelChange(int oldLvl, int newLvl)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnLevelChangeListener.class.isInstance(listener))
				{
					((OnLevelChangeListener) listener).onLevelChange(getActor(), oldLvl, newLvl);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnLevelChangeListener.class.isInstance(listener))
				{
					((OnLevelChangeListener) listener).onLevelChange(getActor(), oldLvl, newLvl);
				}
			}
		}
	}
}
