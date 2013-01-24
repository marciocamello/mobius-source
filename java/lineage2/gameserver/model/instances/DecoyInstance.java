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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.AutoAttackStart;
import lineage2.gameserver.network.serverpackets.CharInfo;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MyTargetSelected;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecoyInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(DecoyInstance.class);
	private final HardReference<Player> _playerRef;
	private int _lifeTime, _timeRemaining;
	private ScheduledFuture<?> _decoyLifeTask, _hateSpam;
	
	public DecoyInstance(int objectId, NpcTemplate template, Player owner, int lifeTime)
	{
		super(objectId, template);
		_playerRef = owner.getRef();
		_lifeTime = lifeTime;
		_timeRemaining = _lifeTime;
		int skilllevel = getNpcId() < 13257 ? getNpcId() - 13070 : getNpcId() - 13250;
		_decoyLifeTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new DecoyLifetime(), 1000, 1000);
		_hateSpam = ThreadPoolManager.getInstance().scheduleAtFixedRate(new HateSpam(SkillTable.getInstance().getInfo(5272, skilllevel)), 1000, 3000);
	}
	
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		if (_hateSpam != null)
		{
			_hateSpam.cancel(false);
			_hateSpam = null;
		}
		_lifeTime = 0;
	}
	
	class DecoyLifetime extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			try
			{
				double newTimeRemaining;
				decTimeRemaining(1000);
				newTimeRemaining = getTimeRemaining();
				if (newTimeRemaining < 0)
				{
					unSummon();
				}
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
		}
	}
	
	class HateSpam extends RunnableImpl
	{
		private final Skill _skill;
		
		HateSpam(Skill skill)
		{
			_skill = skill;
		}
		
		@Override
		public void runImpl()
		{
			try
			{
				setTarget(DecoyInstance.this);
				doCast(_skill, DecoyInstance.this, true);
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
		}
	}
	
	public void unSummon()
	{
		if (_decoyLifeTask != null)
		{
			_decoyLifeTask.cancel(false);
			_decoyLifeTask = null;
		}
		if (_hateSpam != null)
		{
			_hateSpam.cancel(false);
			_hateSpam = null;
		}
		deleteMe();
	}
	
	public void decTimeRemaining(int value)
	{
		_timeRemaining -= value;
	}
	
	public int getTimeRemaining()
	{
		return _timeRemaining;
	}
	
	public int getLifeTime()
	{
		return _lifeTime;
	}
	
	@Override
	public Player getPlayer()
	{
		return _playerRef.get();
	}
	
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		Player owner = getPlayer();
		return (owner != null) && owner.isAutoAttackable(attacker);
	}
	
	@Override
	public boolean isAttackable(Creature attacker)
	{
		Player owner = getPlayer();
		return (owner != null) && owner.isAttackable(attacker);
	}
	
	@Override
	protected void onDelete()
	{
		Player owner = getPlayer();
		if (owner != null)
		{
			owner.setDecoy(null);
		}
		super.onDelete();
	}
	
	@Override
	public void onAction(Player player, boolean shift)
	{
		if (player.getTarget() != this)
		{
			player.setTarget(this);
			player.sendPacket(new MyTargetSelected(getObjectId(), 0));
		}
		else if (isAutoAttackable(player))
		{
			player.getAI().Attack(this, false, shift);
		}
	}
	
	@Override
	public double getColRadius()
	{
		Player player = getPlayer();
		if (player == null)
		{
			return 0;
		}
		if ((player.getTransformation() != 0) && (player.getTransformationTemplate() != 0))
		{
			return NpcHolder.getInstance().getTemplate(player.getTransformationTemplate()).getCollisionRadius();
		}
		return player.getTemplate().getCollisionRadius();
	}
	
	@Override
	public double getColHeight()
	{
		Player player = getPlayer();
		if (player == null)
		{
			return 0;
		}
		if ((player.getTransformation() != 0) && (player.getTransformationTemplate() != 0))
		{
			return NpcHolder.getInstance().getTemplate(player.getTransformationTemplate()).getCollisionHeight();
		}
		return player.getTemplate().getCollisionHeight();
	}
	
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		if (!isInCombat())
		{
			return Collections.<L2GameServerPacket> singletonList(new CharInfo(this));
		}
		List<L2GameServerPacket> list = new ArrayList<>(2);
		list.add(new CharInfo(this));
		list.add(new AutoAttackStart(objectId));
		return list;
	}
	
	@Override
	public boolean isInvul()
	{
		return _isInvul;
	}
}
