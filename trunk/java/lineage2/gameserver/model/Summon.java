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

import gnu.trove.iterator.TIntObjectIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.SummonAI;
import lineage2.gameserver.dao.EffectsDAO;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.actor.recorder.SummonStatsChangeRecorder;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.impl.DuelEvent;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PetInventory;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.AutoAttackStart;
import lineage2.gameserver.network.serverpackets.ExPartyPetWindowAdd;
import lineage2.gameserver.network.serverpackets.ExPartyPetWindowDelete;
import lineage2.gameserver.network.serverpackets.ExPartyPetWindowUpdate;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MyTargetSelected;
import lineage2.gameserver.network.serverpackets.NpcInfo;
import lineage2.gameserver.network.serverpackets.PartySpelled;
import lineage2.gameserver.network.serverpackets.PetDelete;
import lineage2.gameserver.network.serverpackets.PetInfo;
import lineage2.gameserver.network.serverpackets.PetItemList;
import lineage2.gameserver.network.serverpackets.PetStatusShow;
import lineage2.gameserver.network.serverpackets.PetStatusUpdate;
import lineage2.gameserver.network.serverpackets.RelationChanged;
import lineage2.gameserver.network.serverpackets.StatusUpdate;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Events;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.taskmanager.DecayTaskManager;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

public abstract class Summon extends Playable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SUMMON_DISAPPEAR_RANGE = 2500;
	private final Player _owner;
	private int _spawnAnimation = 2;
	protected long _exp = 0;
	protected int _sp = 0;
	private int _maxLoad, _spsCharged;
	private boolean _follow = true, _depressed = false, _ssCharged = false;
	private Future<?> _decayTask;
	
	public Summon(int objectId, NpcTemplate template, Player owner)
	{
		super(objectId, template);
		_owner = owner;
		if (template.getSkills().size() > 0)
		{
			for (TIntObjectIterator<Skill> iterator = template.getSkills().iterator(); iterator.hasNext();)
			{
				iterator.advance();
				addSkill(iterator.value());
			}
		}
		setXYZ(owner.getX() + Rnd.get(-100, 100), owner.getY() + Rnd.get(-100, 100), owner.getZ());
	}
	
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_spawnAnimation = 0;
		Player owner = getPlayer();
		Party party = owner.getParty();
		if (party != null)
		{
			party.broadcastToPartyMembers(owner, new ExPartyPetWindowAdd(this));
		}
		getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
	}
	
	@Override
	public SummonAI getAI()
	{
		if (_ai == null)
		{
			synchronized (this)
			{
				if (_ai == null)
				{
					_ai = new SummonAI(this);
				}
			}
		}
		return (SummonAI) _ai;
	}
	
	@Override
	public NpcTemplate getTemplate()
	{
		return (NpcTemplate) _template;
	}
	
	@Override
	public boolean isUndead()
	{
		return getTemplate().isUndead();
	}
	
	public abstract int getSummonType();
	
	public abstract int getSummonSkillId();
	
	public abstract int getSummonSkillLvl();
	
	public boolean isMountable()
	{
		return false;
	}
	
	@Override
	public void onAction(final Player player, boolean shift)
	{
		if (isFrozen())
		{
			player.sendPacket(ActionFail.STATIC);
			return;
		}
		if (Events.onAction(player, this, shift))
		{
			player.sendPacket(ActionFail.STATIC);
			return;
		}
		Player owner = getPlayer();
		if (player.getTarget() != this)
		{
			player.setTarget(this);
			if (player.getTarget() == this)
			{
				player.sendPacket(new MyTargetSelected(getObjectId(), 0), makeStatusUpdate(StatusUpdate.CUR_HP, StatusUpdate.MAX_HP, StatusUpdate.CUR_MP, StatusUpdate.MAX_MP));
			}
			else
			{
				player.sendPacket(ActionFail.STATIC);
			}
		}
		else if (player == owner)
		{
			player.sendPacket(new PetInfo(this).update());
			if (!player.isActionsDisabled())
			{
				player.sendPacket(new PetStatusShow(this));
			}
			player.sendPacket(ActionFail.STATIC);
		}
		else if (isAutoAttackable(player))
		{
			player.getAI().Attack(this, false, shift);
		}
		else
		{
			if (player.getAI().getIntention() != CtrlIntention.AI_INTENTION_FOLLOW)
			{
				if (!shift)
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, this, Config.FOLLOW_RANGE);
				}
				else
				{
					player.sendActionFailed();
				}
			}
			else
			{
				player.sendActionFailed();
			}
		}
	}
	
	public long getExpForThisLevel()
	{
		return 0;
	}
	
	public long getExpForNextLevel()
	{
		return 0;
	}
	
	@Override
	public int getNpcId()
	{
		return getTemplate().npcId;
	}
	
	public final long getExp()
	{
		return _exp;
	}
	
	public final void setExp(final long exp)
	{
		_exp = exp;
	}
	
	public final int getSp()
	{
		return _sp;
	}
	
	public void setSp(final int sp)
	{
		_sp = sp;
	}
	
	@Override
	public int getMaxLoad()
	{
		return _maxLoad;
	}
	
	public void setMaxLoad(final int maxLoad)
	{
		_maxLoad = maxLoad;
	}
	
	@Override
	public int getBuffLimit()
	{
		Player owner = getPlayer();
		return (int) calcStat(Stats.BUFF_LIMIT, owner.getBuffLimit(), null, null);
	}
	
	public abstract int getCurrentFed();
	
	public abstract int getMaxFed();
	
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		startDecay(8500L);
		Player owner = getPlayer();
		if ((killer == null) || (killer == owner) || (killer == this) || isInZoneBattle() || killer.isInZoneBattle())
		{
			return;
		}
		if (killer instanceof Summon)
		{
			killer = killer.getPlayer();
		}
		if (killer == null)
		{
			return;
		}
		if (killer.isPlayer())
		{
			Player pk = (Player) killer;
			if (isInZone(ZoneType.SIEGE))
			{
				return;
			}
			DuelEvent duelEvent = getEvent(DuelEvent.class);
			if ((owner.getPvpFlag() > 0) || owner.atMutualWarWith(pk))
			{
				pk.setPvpKills(pk.getPvpKills() + 1);
			}
			else if (((duelEvent == null) || (duelEvent != pk.getEvent(DuelEvent.class))) && (getKarma() <= 0))
			{
				int pkCountMulti = Math.max(pk.getPkKills() / 2, 1);
				pk.increaseKarma(Config.KARMA_MIN_KARMA * pkCountMulti);
			}
			pk.sendChanges();
		}
	}
	
	protected void startDecay(long delay)
	{
		stopDecay();
		_decayTask = DecayTaskManager.getInstance().addDecayTask(this, delay);
	}
	
	protected void stopDecay()
	{
		if (_decayTask != null)
		{
			_decayTask.cancel(false);
			_decayTask = null;
		}
	}
	
	@Override
	protected void onDecay()
	{
		deleteMe();
	}
	
	public void endDecayTask()
	{
		stopDecay();
		doDecay();
	}
	
	@Override
	public void broadcastStatusUpdate()
	{
		if (!needStatusUpdate())
		{
			return;
		}
		Player owner = getPlayer();
		sendStatusUpdate();
		StatusUpdate su = makeStatusUpdate(StatusUpdate.MAX_HP, StatusUpdate.CUR_HP);
		broadcastToStatusListeners(su);
		Party party = owner.getParty();
		if (party != null)
		{
			party.broadcastToPartyMembers(owner, new ExPartyPetWindowUpdate(this));
		}
	}
	
	public void sendStatusUpdate()
	{
		Player owner = getPlayer();
		owner.sendPacket(new PetStatusUpdate(this));
	}
	
	@Override
	protected void onDelete()
	{
		Player owner = getPlayer();
		Party party = owner.getParty();
		if (party != null)
		{
			party.broadcastToPartyMembers(owner, new ExPartyPetWindowDelete(this));
		}
		owner.sendPacket(new PetDelete(getSummonType(), getObjectId()));
		stopDecay();
		super.onDelete();
	}
	
	public void unSummon()
	{
		deleteMe();
	}
	
	public void saveEffects()
	{
		Player owner = getPlayer();
		if (owner == null)
		{
			return;
		}
		if (owner.isInOlympiadMode())
		{
			getEffectList().stopAllEffects();
		}
		EffectsDAO.getInstance().insert(this);
	}
	
	public void setFollowMode(boolean state)
	{
		Player owner = getPlayer();
		_follow = state;
		if (_follow)
		{
			if (getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
			{
				getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, owner, Config.FOLLOW_RANGE);
			}
		}
		else if (getAI().getIntention() == CtrlIntention.AI_INTENTION_FOLLOW)
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		}
	}
	
	public boolean isFollowMode()
	{
		return _follow;
	}
	
	Future<?> _updateEffectIconsTask;
	
	public abstract int getSummonPoint();
	
	private class UpdateEffectIcons extends RunnableImpl
	{
		public UpdateEffectIcons()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			updateEffectIconsImpl();
			_updateEffectIconsTask = null;
		}
	}
	
	@Override
	public void updateEffectIcons()
	{
		super.updateEffectIcons();
		if (Config.USER_INFO_INTERVAL == 0)
		{
			if (_updateEffectIconsTask != null)
			{
				_updateEffectIconsTask.cancel(false);
				_updateEffectIconsTask = null;
			}
			updateEffectIconsImpl();
			return;
		}
		if (_updateEffectIconsTask != null)
		{
			return;
		}
		_updateEffectIconsTask = ThreadPoolManager.getInstance().schedule(new UpdateEffectIcons(), Config.USER_INFO_INTERVAL);
	}
	
	public void updateEffectIconsImpl()
	{
		Player owner = getPlayer();
		PartySpelled ps = new PartySpelled(this, true);
		Party party = owner.getParty();
		if (party != null)
		{
			party.broadCast(ps);
		}
		else
		{
			owner.sendPacket(ps);
		}
	}
	
	public int getControlItemObjId()
	{
		return 0;
	}
	
	@Override
	public PetInventory getInventory()
	{
		return null;
	}
	
	@Override
	public void doPickupItem(final GameObject object)
	{
	}
	
	@Override
	public void doRevive()
	{
		super.doRevive();
		setRunning();
		getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		setFollowMode(true);
	}
	
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	@Override
	public WeaponTemplate getActiveWeaponItem()
	{
		return null;
	}
	
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	@Override
	public WeaponTemplate getSecondaryWeaponItem()
	{
		return null;
	}
	
	@Override
	public abstract void displayGiveDamageMessage(Creature target, int damage, boolean crit, boolean miss, boolean shld, boolean magic);
	
	@Override
	public abstract void displayReceiveDamageMessage(Creature attacker, int damage);
	
	@Override
	public boolean unChargeShots(final boolean spirit)
	{
		Player owner = getPlayer();
		if (spirit)
		{
			if (_spsCharged != 0)
			{
				_spsCharged = 0;
				owner.autoShot();
				return true;
			}
		}
		else if (_ssCharged)
		{
			_ssCharged = false;
			owner.autoShot();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean getChargedSoulShot()
	{
		return _ssCharged;
	}
	
	@Override
	public int getChargedSpiritShot()
	{
		return _spsCharged;
	}
	
	public void chargeSoulShot()
	{
		_ssCharged = true;
	}
	
	public void chargeSpiritShot(final int state)
	{
		_spsCharged = state;
	}
	
	public int getSoulshotConsumeCount()
	{
		return (getLevel() / 27) + 1;
	}
	
	public int getSpiritshotConsumeCount()
	{
		return (getLevel() / 58) + 1;
	}
	
	public boolean isDepressed()
	{
		return _depressed;
	}
	
	public void setDepressed(final boolean depressed)
	{
		_depressed = depressed;
	}
	
	public boolean isInRange()
	{
		Player owner = getPlayer();
		return getDistance(owner) < SUMMON_DISAPPEAR_RANGE;
	}
	
	public void teleportToOwner()
	{
		Player owner = getPlayer();
		setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
		if (owner.isInOlympiadMode())
		{
			teleToLocation(owner.getLoc(), owner.getReflection());
		}
		else
		{
			teleToLocation(Location.findPointToStay(owner, 50, 150), owner.getReflection());
		}
		if (!isDead() && _follow)
		{
			getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, owner, Config.FOLLOW_RANGE);
		}
	}
	
	ScheduledFuture<?> _broadcastCharInfoTask;
	
	public class BroadcastCharInfoTask extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			broadcastCharInfoImpl();
			_broadcastCharInfoTask = null;
		}
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
		Player owner = getPlayer();
		for (Player player : World.getAroundPlayers(this))
		{
			if (player == owner)
			{
				player.sendPacket(new PetInfo(this).update());
			}
			else
			{
				player.sendPacket(new NpcInfo(this, player).update());
			}
		}
	}
	
	Future<?> _petInfoTask;
	
	private class PetInfoTask extends RunnableImpl
	{
		public PetInfoTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			sendPetInfoImpl();
			_petInfoTask = null;
		}
	}
	
	void sendPetInfoImpl()
	{
		Player owner = getPlayer();
		owner.sendPacket(new PetInfo(this).update());
	}
	
	public void sendPetInfo()
	{
		if (Config.USER_INFO_INTERVAL == 0)
		{
			if (_petInfoTask != null)
			{
				_petInfoTask.cancel(false);
				_petInfoTask = null;
			}
			sendPetInfoImpl();
			return;
		}
		if (_petInfoTask != null)
		{
			return;
		}
		_petInfoTask = ThreadPoolManager.getInstance().schedule(new PetInfoTask(), Config.USER_INFO_INTERVAL);
	}
	
	public int getSpawnAnimation()
	{
		return _spawnAnimation;
	}
	
	@Override
	public void startPvPFlag(Creature target)
	{
		Player owner = getPlayer();
		owner.startPvPFlag(target);
	}
	
	@Override
	public int getPvpFlag()
	{
		Player owner = getPlayer();
		return owner.getPvpFlag();
	}
	
	@Override
	public int getKarma()
	{
		Player owner = getPlayer();
		return owner.getKarma();
	}
	
	@Override
	public TeamType getTeam()
	{
		Player owner = getPlayer();
		return owner.getTeam();
	}
	
	@Override
	public Player getPlayer()
	{
		return _owner;
	}
	
	public abstract double getExpPenalty();
	
	@Override
	public SummonStatsChangeRecorder getStatsRecorder()
	{
		if (_statsRecorder == null)
		{
			synchronized (this)
			{
				if (_statsRecorder == null)
				{
					_statsRecorder = new SummonStatsChangeRecorder(this);
				}
			}
		}
		return (SummonStatsChangeRecorder) _statsRecorder;
	}
	
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		List<L2GameServerPacket> list = new ArrayList<>();
		Player owner = getPlayer();
		if (owner == forPlayer)
		{
			list.add(new PetInfo(this));
			list.add(new PartySpelled(this, true));
			if (isPet())
			{
				list.add(new PetItemList((PetInstance) this));
			}
		}
		else
		{
			Party party = forPlayer.getParty();
			if ((getReflection() == ReflectionManager.GIRAN_HARBOR) && ((owner == null) || (party == null) || (party != owner.getParty())))
			{
				return list;
			}
			list.add(new NpcInfo(this, forPlayer));
			if ((owner != null) && (party != null) && (party == owner.getParty()))
			{
				list.add(new PartySpelled(this, true));
			}
			list.add(RelationChanged.update(forPlayer, this, forPlayer));
		}
		if (isInCombat())
		{
			list.add(new AutoAttackStart(getObjectId()));
		}
		if (isMoving || isFollow)
		{
			list.add(movePacket());
		}
		return list;
	}
	
	@Override
	public void startAttackStanceTask()
	{
		startAttackStanceTask0();
		Player player = getPlayer();
		if (player != null)
		{
			player.startAttackStanceTask0();
		}
	}
	
	@Override
	public <E extends GlobalEvent> E getEvent(Class<E> eventClass)
	{
		Player player = getPlayer();
		if (player != null)
		{
			return player.getEvent(eventClass);
		}
		return super.getEvent(eventClass);
	}
	
	@Override
	public Set<GlobalEvent> getEvents()
	{
		Player player = getPlayer();
		if (player != null)
		{
			return player.getEvents();
		}
		return super.getEvents();
	}
	
	@Override
	public void sendReuseMessage(Skill skill)
	{
		Player player = getPlayer();
		if ((player != null) && isSkillDisabled(skill))
		{
			player.sendPacket(SystemMsg.THAT_PET_SERVITOR_SKILL_CANNOT_BE_USED_BECAUSE_IT_IS_RECHARGING);
		}
	}
}
