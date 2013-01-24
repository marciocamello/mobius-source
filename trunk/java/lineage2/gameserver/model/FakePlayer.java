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
package lineage2.gameserver.model;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.FakePlayerAI;
import lineage2.gameserver.listener.actor.OnAttackListener;
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.CharInfo;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.player.PlayerTemplate;

public class FakePlayer extends Creature
{
	private static final long serialVersionUID = -7275714049223105460L;
	private final Player _owner;
	private final OwnerAttakListener _listener;
	ScheduledFuture<?> _broadcastCharInfoTask;
	
	public FakePlayer(int objectId, PlayerTemplate template, Player owner)
	{
		super(objectId, template);
		_owner = owner;
		_ai = new FakePlayerAI(this);
		_listener = new OwnerAttakListener();
		owner.addListener(_listener);
	}
	
	@Override
	public Player getPlayer()
	{
		return _owner;
	}
	
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, getPlayer(), Integer.valueOf(Config.FOLLOW_RANGE));
	}
	
	@Override
	public FakePlayerAI getAI()
	{
		return (FakePlayerAI) _ai;
	}
	
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return false;
	}
	
	@Override
	public int getLevel()
	{
		return _owner.getLevel();
	}
	
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return _owner.getActiveWeaponInstance();
	}
	
	@Override
	public WeaponTemplate getActiveWeaponItem()
	{
		return _owner.getActiveWeaponItem();
	}
	
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return _owner.getSecondaryWeaponInstance();
	}
	
	@Override
	public WeaponTemplate getSecondaryWeaponItem()
	{
		return _owner.getSecondaryWeaponItem();
	}
	
	@Override
	public void broadcastCharInfo()
	{
		if (_broadcastCharInfoTask != null)
		{
			return;
		}
		_broadcastCharInfoTask = ThreadPoolManager.getInstance().schedule(new BroadcastCharInfoTask(), Config.BROADCAST_CHAR_INFO_INTERVAL);
	}
	
	public void broadcastCharInfoImpl()
	{
		for (Player player : World.getAroundPlayers(this))
		{
			player.sendPacket(new CharInfo(this));
		}
	}
	
	public void notifyOwerStartAttak()
	{
		getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, _owner.getTarget());
	}
	
	private class OwnerAttakListener implements OnAttackListener, OnMagicUseListener
	{
		public OwnerAttakListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
		{
			notifyOwerStartAttak();
		}
		
		@Override
		public void onAttack(Creature actor, Creature target)
		{
			notifyOwerStartAttak();
		}
	}
	
	public class BroadcastCharInfoTask extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			broadcastCharInfoImpl();
			_broadcastCharInfoTask = null;
		}
	}
}
