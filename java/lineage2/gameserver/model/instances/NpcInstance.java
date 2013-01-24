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

import gnu.trove.iterator.TIntObjectIterator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.DelusionChamberManager;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.NpcListener;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks.NotifyAITask;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.MinionList;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.TeleportLocation;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.actor.listener.NpcListenerList;
import lineage2.gameserver.model.actor.recorder.NpcStatsChangeRecorder;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.entity.DelusionChamber;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.objects.TerritoryWardObject;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.AcquireSkillDone;
import lineage2.gameserver.network.serverpackets.AcquireSkillList;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.AutoAttackStart;
import lineage2.gameserver.network.serverpackets.ExChangeNpcState;
import lineage2.gameserver.network.serverpackets.ExShowBaseAttributeCancelWindow;
import lineage2.gameserver.network.serverpackets.ExShowVariationCancelWindow;
import lineage2.gameserver.network.serverpackets.ExShowVariationMakeWindow;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.MyTargetSelected;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.NpcInfo;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.StatusUpdate;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.ValidateLocation;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Events;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.taskmanager.DecayTaskManager;
import lineage2.gameserver.taskmanager.LazyPrecisionTaskManager;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate;
import lineage2.gameserver.templates.npc.Faction;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.utils.CertificationFunctions;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NpcInstance extends Creature
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NO_CHAT_WINDOW = "noChatWindow";
	public static final String NO_RANDOM_WALK = "noRandomWalk";
	public static final String NO_RANDOM_ANIMATION = "noRandomAnimation";
	public static final String TARGETABLE = "TargetEnabled";
	public static final String ATTACKABLE = "attackable";
	public static final String SHOW_NAME = "showName";
	public static final String SHOW_TITLE = "showTitle";
	private static final Logger _log = LoggerFactory.getLogger(NpcInstance.class);
	private int _personalAggroRange = -1;
	private int _level = 0;
	private long _dieTime = 0L;
	protected int _spawnAnimation = 2;
	private int _currentLHandId;
	private int _currentRHandId;
	private double _currentCollisionRadius;
	private double _currentCollisionHeight;
	private int npcState = 0;
	protected boolean _hasRandomAnimation;
	protected boolean _hasRandomWalk;
	protected boolean _hasChatWindow;
	private Future<?> _decayTask;
	private Future<?> _animationTask;
	private final AggroList _aggroList;
	private boolean _isTargetable;
	private boolean _isAttackable;
	private boolean _showName;
	private boolean _showTitle;
	private Castle _nearestCastle;
	private Fortress _nearestFortress;
	private ClanHall _nearestClanHall;
	private Dominion _nearestDominion;
	private NpcString _nameNpcString = NpcString.NONE;
	private NpcString _titleNpcString = NpcString.NONE;
	private Spawner _spawn;
	private Location _spawnedLoc = new Location();
	private SpawnRange _spawnRange;
	private MultiValueSet<String> _parameters = StatsSet.EMPTY;
	
	public NpcInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		if (template == null)
		{
			throw new NullPointerException("No template for Npc. Please check your datapack is setup correctly.");
		}
		setParameters(template.getAIParams());
		_hasRandomAnimation = !getParameter(NO_RANDOM_ANIMATION, false) && (Config.MAX_NPC_ANIMATION > 0);
		_hasRandomWalk = !getParameter(NO_RANDOM_WALK, false);
		setHasChatWindow(!getParameter(NO_CHAT_WINDOW, false));
		setTargetable(getParameter(TARGETABLE, true));
		setAttackable(getParameter(ATTACKABLE, false));
		setShowName(getParameter(SHOW_NAME, true));
		setShowTitle(getParameter(SHOW_TITLE, true));
		if (template.getSkills().size() > 0)
		{
			for (TIntObjectIterator<Skill> iterator = template.getSkills().iterator(); iterator.hasNext();)
			{
				iterator.advance();
				addSkill(iterator.value());
			}
		}
		setName(template.name);
		setTitle(template.title);
		setLHandId(getTemplate().lhand);
		setRHandId(getTemplate().rhand);
		setCollisionHeight(getTemplate().getCollisionHeight());
		setCollisionRadius(getTemplate().getCollisionRadius());
		_aggroList = new AggroList(this);
		setFlying(getParameter("isFlying", false));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HardReference<NpcInstance> getRef()
	{
		return (HardReference<NpcInstance>) super.getRef();
	}
	
	@Override
	public CharacterAI getAI()
	{
		if (_ai == null)
		{
			synchronized (this)
			{
				if (_ai == null)
				{
					_ai = getTemplate().getNewAI(this);
				}
			}
		}
		return _ai;
	}
	
	public Location getSpawnedLoc()
	{
		return _spawnedLoc;
	}
	
	public void setSpawnedLoc(Location loc)
	{
		_spawnedLoc = loc;
	}
	
	public int getRightHandItem()
	{
		return _currentRHandId;
	}
	
	public int getLeftHandItem()
	{
		return _currentLHandId;
	}
	
	public void setLHandId(int newWeaponId)
	{
		_currentLHandId = newWeaponId;
	}
	
	public void setRHandId(int newWeaponId)
	{
		_currentRHandId = newWeaponId;
	}
	
	public double getCollisionHeight()
	{
		return _currentCollisionHeight;
	}
	
	public void setCollisionHeight(double offset)
	{
		_currentCollisionHeight = offset;
	}
	
	public double getCollisionRadius()
	{
		return _currentCollisionRadius;
	}
	
	public void setCollisionRadius(double collisionRadius)
	{
		_currentCollisionRadius = collisionRadius;
	}
	
	@Override
	protected void onReduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp)
	{
		if (attacker.isPlayable())
		{
			getAggroList().addDamageHate(attacker, (int) damage, 0);
		}
		super.onReduceCurrentHp(damage, attacker, skill, awake, standUp, directHp);
	}
	
	@Override
	protected void onDeath(Creature killer)
	{
		_dieTime = System.currentTimeMillis();
		if (isMonster() && (((MonsterInstance) this).isSeeded() || ((MonsterInstance) this).isSpoiled()))
		{
			startDecay(20000L);
		}
		else if (isBoss())
		{
			startDecay(20000L);
		}
		else if (isFlying())
		{
			startDecay(4500L);
		}
		else
		{
			startDecay(8500L);
		}
		setLHandId(getTemplate().lhand);
		setRHandId(getTemplate().rhand);
		setCollisionHeight(getTemplate().getCollisionHeight());
		setCollisionRadius(getTemplate().getCollisionRadius());
		getAI().stopAITask();
		stopRandomAnimation();
		super.onDeath(killer);
	}
	
	public long getDeadTime()
	{
		if (_dieTime <= 0L)
		{
			return 0L;
		}
		return System.currentTimeMillis() - _dieTime;
	}
	
	public AggroList getAggroList()
	{
		return _aggroList;
	}
	
	public MinionList getMinionList()
	{
		return null;
	}
	
	public boolean hasMinions()
	{
		return false;
	}
	
	public void dropItem(Player lastAttacker, int itemId, long itemCount)
	{
		if ((itemCount == 0) || (lastAttacker == null))
		{
			return;
		}
		ItemInstance item;
		for (long i = 0; i < itemCount; i++)
		{
			item = ItemFunctions.createItem(itemId);
			for (GlobalEvent e : getEvents())
			{
				item.addEvent(e);
			}
			if (item.isStackable())
			{
				i = itemCount;
				item.setCount(itemCount);
			}
			if (isRaid() || (this instanceof ReflectionBossInstance))
			{
				SystemMessage2 sm;
				if (itemId == 57)
				{
					sm = new SystemMessage2(SystemMsg.C1_HAS_DIED_AND_DROPPED_S2_ADENA);
					sm.addName(this);
					sm.addLong(item.getCount());
				}
				else
				{
					sm = new SystemMessage2(SystemMsg.C1_DIED_AND_DROPPED_S3_S2);
					sm.addName(this);
					sm.addItemName(itemId);
					sm.addLong(item.getCount());
				}
				broadcastPacket(sm);
			}
			lastAttacker.doAutoLootOrDrop(item, this);
		}
	}
	
	public void dropItem(Player lastAttacker, ItemInstance item)
	{
		if (item.getCount() == 0)
		{
			return;
		}
		if (isRaid() || (this instanceof ReflectionBossInstance))
		{
			SystemMessage2 sm;
			if (item.getItemId() == 57)
			{
				sm = new SystemMessage2(SystemMsg.C1_HAS_DIED_AND_DROPPED_S2_ADENA);
				sm.addName(this);
				sm.addLong(item.getCount());
			}
			else
			{
				sm = new SystemMessage2(SystemMsg.C1_DIED_AND_DROPPED_S3_S2);
				sm.addName(this);
				sm.addItemName(item.getItemId());
				sm.addLong(item.getCount());
			}
			broadcastPacket(sm);
		}
		lastAttacker.doAutoLootOrDrop(item, this);
	}
	
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return true;
	}
	
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return _isAttackable;
	}
	
	@Override
	protected void onSpawn()
	{
		super.onSpawn();
		_dieTime = 0L;
		_spawnAnimation = 0;
		if (getAI().isGlobalAI() || ((getCurrentRegion() != null) && getCurrentRegion().isActive()))
		{
			getAI().startAITask();
			startRandomAnimation();
		}
		ThreadPoolManager.getInstance().execute(new NotifyAITask(this, CtrlEvent.EVT_SPAWN));
		getListeners().onSpawn();
	}
	
	@Override
	protected void onDespawn()
	{
		getAggroList().clear();
		getAI().onEvtDeSpawn();
		getAI().stopAITask();
		getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		stopRandomAnimation();
		super.onDespawn();
	}
	
	@Override
	public NpcTemplate getTemplate()
	{
		return (NpcTemplate) _template;
	}
	
	@Override
	public int getNpcId()
	{
		return getTemplate().npcId;
	}
	
	protected boolean _unAggred = false;
	
	public void setUnAggred(boolean state)
	{
		_unAggred = state;
	}
	
	public boolean isAggressive()
	{
		return getAggroRange() > 0;
	}
	
	public int getAggroRange()
	{
		if (_unAggred)
		{
			return 0;
		}
		if (_personalAggroRange >= 0)
		{
			return _personalAggroRange;
		}
		return getTemplate().aggroRange;
	}
	
	public void setAggroRange(int aggroRange)
	{
		_personalAggroRange = aggroRange;
	}
	
	public Faction getFaction()
	{
		return getTemplate().getFaction();
	}
	
	public boolean isInFaction(NpcInstance npc)
	{
		return getFaction().equals(npc.getFaction()) && !getFaction().isIgnoreNpcId(npc.getNpcId());
	}
	
	@Override
	public int getMAtk(Creature target, Skill skill)
	{
		return (int) (super.getMAtk(target, skill) * Config.ALT_NPC_MATK_MODIFIER);
	}
	
	@Override
	public int getPAtk(Creature target)
	{
		return (int) (super.getPAtk(target) * Config.ALT_NPC_PATK_MODIFIER);
	}
	
	@Override
	public int getMaxHp()
	{
		return (int) (super.getMaxHp() * Config.ALT_NPC_MAXHP_MODIFIER);
	}
	
	@Override
	public int getMaxMp()
	{
		return (int) (super.getMaxMp() * Config.ALT_NPC_MAXMP_MODIFIER);
	}
	
	public long getExpReward()
	{
		return (long) calcStat(Stats.EXP, getTemplate().rewardExp, null, null);
	}
	
	public long getSpReward()
	{
		return (long) calcStat(Stats.SP, getTemplate().rewardSp, null, null);
	}
	
	@Override
	protected void onDelete()
	{
		stopDecay();
		if (_spawn != null)
		{
			_spawn.stopRespawn();
		}
		setSpawn(null);
		super.onDelete();
	}
	
	public Spawner getSpawn()
	{
		return _spawn;
	}
	
	public void setSpawn(Spawner spawn)
	{
		_spawn = spawn;
	}
	
	@Override
	protected void onDecay()
	{
		super.onDecay();
		_spawnAnimation = 2;
		if (_spawn != null)
		{
			_spawn.decreaseCount(this);
		}
		else
		{
			deleteMe();
		}
	}
	
	protected void startDecay(long delay)
	{
		stopDecay();
		_decayTask = DecayTaskManager.getInstance().addDecayTask(this, delay);
	}
	
	public void stopDecay()
	{
		if (_decayTask != null)
		{
			_decayTask.cancel(false);
			_decayTask = null;
		}
	}
	
	public void endDecayTask()
	{
		if (_decayTask != null)
		{
			_decayTask.cancel(false);
			_decayTask = null;
		}
		doDecay();
	}
	
	@Override
	public boolean isUndead()
	{
		return getTemplate().isUndead();
	}
	
	public void setLevel(int level)
	{
		_level = level;
	}
	
	@Override
	public int getLevel()
	{
		return _level == 0 ? getTemplate().level : _level;
	}
	
	private int _displayId = 0;
	
	public void setDisplayId(int displayId)
	{
		_displayId = displayId;
	}
	
	public int getDisplayId()
	{
		return _displayId > 0 ? _displayId : getTemplate().displayId;
	}
	
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	@Override
	public WeaponTemplate getActiveWeaponItem()
	{
		int weaponId = getTemplate().rhand;
		if (weaponId < 1)
		{
			return null;
		}
		ItemTemplate item = ItemHolder.getInstance().getTemplate(getTemplate().rhand);
		if (!(item instanceof WeaponTemplate))
		{
			return null;
		}
		return (WeaponTemplate) item;
	}
	
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	@Override
	public WeaponTemplate getSecondaryWeaponItem()
	{
		int weaponId = getTemplate().lhand;
		if (weaponId < 1)
		{
			return null;
		}
		ItemTemplate item = ItemHolder.getInstance().getTemplate(getTemplate().lhand);
		if (!(item instanceof WeaponTemplate))
		{
			return null;
		}
		return (WeaponTemplate) item;
	}
	
	@Override
	public void sendChanges()
	{
		if (isFlying())
		{
			return;
		}
		super.sendChanges();
	}
	
	ScheduledFuture<?> _broadcastCharInfoTask;
	
	public void onMenuSelect(Player player, int ask, int reply)
	{
		if (getAI() != null)
		{
			getAI().notifyEvent(CtrlEvent.EVT_MENU_SELECTED, player, ask, reply);
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
	
	@Override
	public void broadcastCharInfo()
	{
		if (!isVisible())
		{
			return;
		}
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
			player.sendPacket(new NpcInfo(this, player).update());
		}
	}
	
	public void onRandomAnimation()
	{
		if ((System.currentTimeMillis() - _lastSocialAction) > 10000L)
		{
			broadcastPacket(new SocialAction(getObjectId(), 2));
			_lastSocialAction = System.currentTimeMillis();
		}
	}
	
	public void startRandomAnimation()
	{
		if (!hasRandomAnimation())
		{
			return;
		}
		_animationTask = LazyPrecisionTaskManager.getInstance().addNpcAnimationTask(this);
	}
	
	public void stopRandomAnimation()
	{
		if (_animationTask != null)
		{
			_animationTask.cancel(false);
			_animationTask = null;
		}
	}
	
	public boolean hasRandomAnimation()
	{
		return _hasRandomAnimation;
	}
	
	public boolean hasRandomWalk()
	{
		return _hasRandomWalk;
	}
	
	public Castle getCastle()
	{
		if ((getReflection() == ReflectionManager.PARNASSUS) && Config.SERVICES_PARNASSUS_NOTAX)
		{
			return null;
		}
		if (Config.SERVICES_OFFSHORE_NO_CASTLE_TAX && (getReflection() == ReflectionManager.GIRAN_HARBOR))
		{
			return null;
		}
		if (Config.SERVICES_OFFSHORE_NO_CASTLE_TAX && (getReflection() == ReflectionManager.PARNASSUS))
		{
			return null;
		}
		if (Config.SERVICES_OFFSHORE_NO_CASTLE_TAX && isInZone(ZoneType.offshore))
		{
			return null;
		}
		if (_nearestCastle == null)
		{
			_nearestCastle = ResidenceHolder.getInstance().getResidence(getTemplate().getCastleId());
		}
		return _nearestCastle;
	}
	
	public Castle getCastle(Player player)
	{
		return getCastle();
	}
	
	public Fortress getFortress()
	{
		if (_nearestFortress == null)
		{
			_nearestFortress = ResidenceHolder.getInstance().findNearestResidence(Fortress.class, getX(), getY(), getZ(), getReflection(), 32768);
		}
		return _nearestFortress;
	}
	
	public ClanHall getClanHall()
	{
		if (_nearestClanHall == null)
		{
			_nearestClanHall = ResidenceHolder.getInstance().findNearestResidence(ClanHall.class, getX(), getY(), getZ(), getReflection(), 32768);
		}
		return _nearestClanHall;
	}
	
	public Dominion getDominion()
	{
		if (getReflection() != ReflectionManager.DEFAULT)
		{
			return null;
		}
		if (_nearestDominion == null)
		{
			if (getTemplate().getCastleId() == 0)
			{
				return null;
			}
			Castle castle = ResidenceHolder.getInstance().getResidence(getTemplate().getCastleId());
			_nearestDominion = castle.getDominion();
		}
		return _nearestDominion;
	}
	
	protected long _lastSocialAction;
	
	@Override
	public void onAction(Player player, boolean shift)
	{
		if (!isTargetable())
		{
			player.sendActionFailed();
			return;
		}
		if (player.getTarget() != this)
		{
			player.setTarget(this);
			if (player.getTarget() == this)
			{
				player.sendPacket(new MyTargetSelected(getObjectId(), player.getLevel() - getLevel()), makeStatusUpdate(StatusUpdate.CUR_HP, StatusUpdate.MAX_HP));
			}
			player.sendPacket(new ValidateLocation(this), ActionFail.STATIC);
			return;
		}
		if (Events.onAction(player, this, shift))
		{
			player.sendActionFailed();
			return;
		}
		if (isAutoAttackable(player))
		{
			player.getAI().Attack(this, false, shift);
			return;
		}
		if (!isInRange(player, INTERACTION_DISTANCE))
		{
			if (player.getAI().getIntention() != CtrlIntention.AI_INTENTION_INTERACT)
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this, null);
			}
			return;
		}
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (player.getKarma() < 0) && !player.isGM() && !(this instanceof WarehouseInstance))
		{
			player.sendActionFailed();
			return;
		}
		if ((!Config.ALLOW_TALK_WHILE_SITTING && player.isSitting()) || player.isAlikeDead())
		{
			return;
		}
		if (hasRandomAnimation())
		{
			onRandomAnimation();
		}
		player.sendActionFailed();
		player.stopMove(false);
		if (_isBusy)
		{
			showBusyWindow(player);
		}
		else if (isHasChatWindow())
		{
			boolean flag = false;
			Quest[] qlst = getTemplate().getEventQuests(QuestEventType.NPC_FIRST_TALK);
			if ((qlst != null) && (qlst.length > 0))
			{
				for (Quest element : qlst)
				{
					QuestState qs = player.getQuestState(element.getName());
					if (((qs == null) || !qs.isCompleted()) && element.notifyFirstTalk(this, player))
					{
						flag = true;
					}
				}
			}
			if (!flag)
			{
				showChatWindow(player, 0);
			}
		}
	}
	
	public void showQuestWindow(Player player, String questId)
	{
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		int count = 0;
		for (QuestState quest : player.getAllQuestsStates())
		{
			if ((quest != null) && quest.getQuest().isVisible(player) && quest.isStarted() && (quest.getCond() > 0))
			{
				count++;
			}
		}
		if (count > 40)
		{
			showChatWindow(player, "quest-limit.htm");
			return;
		}
		try
		{
			QuestState qs = player.getQuestState(questId);
			if (qs != null)
			{
				if (qs.getQuest().notifyTalk(this, qs))
				{
					return;
				}
			}
			else
			{
				Quest q = QuestManager.getQuest(questId);
				if (q != null)
				{
					Quest[] qlst = getTemplate().getEventQuests(QuestEventType.QUEST_START);
					if ((qlst != null) && (qlst.length > 0))
					{
						for (Quest element : qlst)
						{
							if (element == q)
							{
								qs = q.newQuestState(player, Quest.CREATED);
								if (qs.getQuest().notifyTalk(this, qs))
								{
									return;
								}
								break;
							}
						}
					}
				}
			}
			showChatWindow(player, "no-quest.htm");
		}
		catch (Exception e)
		{
			_log.warn("problem with npc text(questId: " + questId + ") " + e);
			_log.error("", e);
		}
		player.sendActionFailed();
	}
	
	public static boolean canBypassCheck(Player player, NpcInstance npc)
	{
		if ((npc == null) || player.isActionsDisabled() || (!Config.ALLOW_TALK_WHILE_SITTING && player.isSitting()) || !npc.isInRange(player, INTERACTION_DISTANCE))
		{
			player.sendActionFailed();
			return false;
		}
		return true;
	}
	
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if ((getTemplate().getTeleportList().size() > 0) && checkForDominionWard(player))
		{
			return;
		}
		try
		{
			if (command.equalsIgnoreCase("TerritoryStatus"))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("merchant/territorystatus.htm");
				html.replace("%npcname%", getName());
				Castle castle = getCastle(player);
				if ((castle != null) && (castle.getId() > 0))
				{
					html.replace("%castlename%", HtmlUtils.htmlResidenceName(castle.getId()));
					html.replace("%taxpercent%", String.valueOf(castle.getTaxPercent()));
					if (castle.getOwnerId() > 0)
					{
						Clan clan = ClanTable.getInstance().getClan(castle.getOwnerId());
						if (clan != null)
						{
							html.replace("%clanname%", clan.getName());
							html.replace("%clanleadername%", clan.getLeaderName());
						}
						else
						{
							html.replace("%clanname%", "unexistant clan");
							html.replace("%clanleadername%", "None");
						}
					}
					else
					{
						html.replace("%clanname%", "NPC");
						html.replace("%clanleadername%", "None");
					}
				}
				else
				{
					html.replace("%castlename%", "Open");
					html.replace("%taxpercent%", "0");
					html.replace("%clanname%", "No");
					html.replace("%clanleadername%", getName());
				}
				player.sendPacket(html);
			}
			else if (command.startsWith("Quest"))
			{
				String quest = command.substring(5).trim();
				if (quest.length() == 0)
				{
					showQuestWindow(player);
				}
				else
				{
					showQuestWindow(player, quest);
				}
			}
			else if (command.startsWith("Chat"))
			{
				try
				{
					int val = Integer.parseInt(command.substring(5));
					showChatWindow(player, val);
				}
				catch (NumberFormatException nfe)
				{
					String filename = command.substring(5).trim();
					if (filename.length() == 0)
					{
						showChatWindow(player, "npcdefault.htm");
					}
					else
					{
						showChatWindow(player, filename);
					}
				}
			}
			else if (command.startsWith("AttributeCancel"))
			{
				player.sendPacket(new ExShowBaseAttributeCancelWindow(player));
			}
			else if (command.startsWith("NpcLocationInfo"))
			{
				int val = Integer.parseInt(command.substring(16));
				NpcInstance npc = GameObjectsStorage.getByNpcId(val);
				if (npc != null)
				{
					player.sendPacket(new RadarControl(2, 2, npc.getLoc()));
					player.sendPacket(new RadarControl(0, 1, npc.getLoc()));
				}
			}
			else if (command.startsWith("Multisell") || command.startsWith("multisell"))
			{
				String listId = command.substring(9).trim();
				Castle castle = getCastle(player);
				MultiSellHolder.getInstance().SeparateAndSend(Integer.parseInt(listId), player, castle != null ? castle.getTaxRate() : 0);
			}
			else if (command.startsWith("ChangeDCRoom"))
			{
				if (player.isInParty() && player.getParty().isInReflection() && (player.getParty().getReflection() instanceof DelusionChamber))
				{
					((DelusionChamber) player.getParty().getReflection()).manualTeleport(player, this);
				}
				else
				{
					DelusionChamberManager.getInstance().teleportToWaitingRoom(player);
				}
			}
			else if (command.startsWith("ExitDCWaitingRoom"))
			{
				if (player.isInParty() && player.getParty().isInReflection() && (player.getParty().getReflection() instanceof DelusionChamber))
				{
					((DelusionChamber) player.getParty().getReflection()).manualExitChamber(player, this);
				}
			}
			else if (command.equalsIgnoreCase("SkillList"))
			{
			}
			else if (command.equalsIgnoreCase("ClanSkillList"))
			{
				showClanSkillList(player);
			}
			else if (command.startsWith("SubUnitSkillList"))
			{
				showSubUnitSkillList(player);
			}
			else if (command.equalsIgnoreCase("TransformationSkillList"))
			{
				showTransformationSkillList(player, AcquireType.TRANSFORMATION);
			}
			else if (command.equalsIgnoreCase("CertificationSkillList"))
			{
				showTransformationSkillList(player, AcquireType.CERTIFICATION);
			}
			else if (command.equalsIgnoreCase("CollectionSkillList"))
			{
				showCollectionSkillList(player);
			}
			else if (command.equalsIgnoreCase("BuyTransformation"))
			{
				showTransformationMultisell(player);
			}
			else if (command.startsWith("Augment"))
			{
				int cmdChoice = Integer.parseInt(command.substring(8, 9).trim());
				if (cmdChoice == 1)
				{
					player.sendPacket(Msg.SELECT_THE_ITEM_TO_BE_AUGMENTED, ExShowVariationMakeWindow.STATIC);
				}
				else if (cmdChoice == 2)
				{
					player.sendPacket(Msg.SELECT_THE_ITEM_FROM_WHICH_YOU_WISH_TO_REMOVE_AUGMENTATION, ExShowVariationCancelWindow.STATIC);
				}
			}
			else if (command.startsWith("Link"))
			{
				showChatWindow(player, command.substring(5));
			}
			else if (command.startsWith("Teleport"))
			{
				int cmdChoice = Integer.parseInt(command.substring(9, 10).trim());
				TeleportLocation[] list = getTemplate().getTeleportList(cmdChoice);
				if (list != null)
				{
					showTeleportList(player, list);
				}
				else
				{
					player.sendMessage("Link is faulty, contact an administrator.");
				}
			}
			else if (command.startsWith("Tele20Lvl"))
			{
				int cmdChoice = Integer.parseInt(command.substring(10, 11).trim());
				TeleportLocation[] list = getTemplate().getTeleportList(cmdChoice);
				if (player.getLevel() > 20)
				{
					showChatWindow(player, "teleporter/" + getNpcId() + "-no.htm");
				}
				else if (list != null)
				{
					showTeleportList(player, list);
				}
				else
				{
					player.sendMessage("Link is faulty, contact an administrator.");
				}
			}
			else if (command.startsWith("open_gate"))
			{
				int val = Integer.parseInt(command.substring(10));
				ReflectionUtils.getDoor(val).openMe();
				player.sendActionFailed();
			}
			else if (command.equalsIgnoreCase("TransferSkillList"))
			{
				showTransferSkillList(player);
			}
			else if (command.equalsIgnoreCase("CertificationCancel"))
			{
				CertificationFunctions.cancelCertification(this, player);
			}
			else if (command.startsWith("RemoveTransferSkill"))
			{
				AcquireType type = AcquireType.transferType(player.getActiveClassId());
				if (type == null)
				{
					return;
				}
				Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(null, type);
				if (skills.isEmpty())
				{
					player.sendActionFailed();
					return;
				}
				boolean reset = false;
				for (SkillLearn skill : skills)
				{
					if (player.getKnownSkill(skill.getId()) != null)
					{
						reset = true;
						break;
					}
				}
				if (!reset)
				{
					player.sendActionFailed();
					return;
				}
				if (!player.reduceAdena(10000000L, true))
				{
					showChatWindow(player, "common/skill_share_healer_no_adena.htm");
					return;
				}
				for (SkillLearn skill : skills)
				{
					if (player.removeSkill(skill.getId(), true) != null)
					{
						for (int itemId : skill.getRequiredItems().keySet())
						{
							ItemFunctions.addItem(player, itemId, skill.getRequiredItems().get(itemId), true);
						}
					}
				}
			}
			else if (command.startsWith("ExitFromQuestInstance"))
			{
				Reflection r = player.getReflection();
				r.startCollapseTimer(60000);
				player.teleToLocation(r.getReturnLoc(), 0);
				if (command.length() > 22)
				{
					try
					{
						int val = Integer.parseInt(command.substring(22));
						showChatWindow(player, val);
					}
					catch (NumberFormatException nfe)
					{
						String filename = command.substring(22).trim();
						if (filename.length() > 0)
						{
							showChatWindow(player, filename);
						}
					}
				}
			}
		}
		catch (StringIndexOutOfBoundsException sioobe)
		{
			_log.info("Incorrect htm bypass! npcId=" + getTemplate().npcId + " command=[" + command + "]");
		}
		catch (NumberFormatException nfe)
		{
			_log.info("Invalid bypass to Server command parameter! npcId=" + getTemplate().npcId + " command=[" + command + "]");
		}
	}
	
	public void showTeleportList(Player player, TeleportLocation[] list)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("&$556;").append("<br><br>");
		if ((list != null) && player.getPlayerAccess().UseTeleport)
		{
			for (TeleportLocation tl : list)
			{
				if (tl.getItem().getItemId() == ItemTemplate.ITEM_ID_ADENA)
				{
					double pricemod = player.getLevel() <= Config.GATEKEEPER_FREE ? 0. : Config.GATEKEEPER_MODIFIER;
					if ((tl.getPrice() > 0) && (pricemod > 0))
					{
						Calendar calendar = Calendar.getInstance();
						int day = calendar.get(Calendar.DAY_OF_WEEK);
						int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
						if (((day == Calendar.SUNDAY) || (day == Calendar.SATURDAY)) && ((hour >= 20) && (hour <= 12)))
						{
							pricemod /= 2;
						}
					}
					sb.append("[scripts_Util:Gatekeeper ").append(tl.getX()).append(" ").append(tl.getY()).append(" ").append(tl.getZ());
					if (tl.getCastleId() != 0)
					{
						sb.append(" ").append(tl.getCastleId());
					}
					sb.append(" ").append((long) (tl.getPrice() * pricemod)).append(" @811;F;").append(tl.getName()).append("|").append(HtmlUtils.htmlNpcString(tl.getName()));
					if ((tl.getPrice() * pricemod) > 0)
					{
						sb.append(" - ").append((long) (tl.getPrice() * pricemod)).append(" ").append(HtmlUtils.htmlItemName(ItemTemplate.ITEM_ID_ADENA));
					}
					sb.append("]<br1>\n");
				}
				else
				{
					sb.append("[scripts_Util:QuestGatekeeper ").append(tl.getX()).append(" ").append(tl.getY()).append(" ").append(tl.getZ()).append(" ").append(tl.getPrice()).append(" ").append(tl.getItem().getItemId()).append(" @811;F;").append("|").append(HtmlUtils.htmlNpcString(tl.getName())).append(" - ").append(tl.getPrice()).append(" ").append(HtmlUtils.htmlItemName(tl.getItem().getItemId())).append("]<br1>\n");
				}
			}
		}
		else
		{
			sb.append("No teleports available for you.");
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setHtml(Strings.bbParse(sb.toString()));
		player.sendPacket(html);
	}
	
	private class QuestInfo implements Comparable<QuestInfo>
	{
		private final Quest quest;
		private final Player player;
		private final boolean isStart;
		
		public QuestInfo(Quest quest, Player player, boolean isStart)
		{
			this.quest = quest;
			this.player = player;
			this.isStart = isStart;
		}
		
		public final Quest getQuest()
		{
			return quest;
		}
		
		public final boolean isStart()
		{
			return isStart;
		}
		
		@Override
		public int compareTo(QuestInfo info)
		{
			int quest1 = quest.getDescrState(player, isStart);
			int quest2 = info.getQuest().getDescrState(player, isStart);
			int questId1 = quest.getQuestIntId();
			int questId2 = info.getQuest().getQuestIntId();
			if ((quest1 == 1) && (quest2 == 2))
			{
				return 1;
			}
			else if ((quest1 == 2) && (quest2 == 1))
			{
				return -1;
			}
			else if ((quest1 == 3) && (quest2 == 4))
			{
				return 1;
			}
			else if ((quest1 == 4) && (quest2 == 3))
			{
				return -1;
			}
			else if (quest1 > quest2)
			{
				return 1;
			}
			else if (quest1 < quest2)
			{
				return -1;
			}
			else
			{
				if (questId1 > questId2)
				{
					return 1;
				}
				else if (questId1 < questId2)
				{
					return -1;
				}
				else
				{
					return 0;
				}
			}
		}
	}
	
	public void showQuestWindow(Player player)
	{
		Map<Integer, QuestInfo> options = new HashMap<>();
		Quest[] starts = getTemplate().getEventQuests(QuestEventType.QUEST_START);
		List<QuestState> awaits = player.getQuestsForEvent(this, QuestEventType.QUEST_TALK, true);
		if (starts != null)
		{
			for (Quest x : starts)
			{
				if ((x.getQuestIntId() > 0) && !options.containsKey(x.getQuestIntId()))
				{
					options.put(x.getQuestIntId(), new QuestInfo(x, player, true));
				}
			}
		}
		if (awaits != null)
		{
			for (QuestState x : awaits)
			{
				if ((x.getQuest().getQuestIntId() > 0) && !options.containsKey(x.getQuest().getQuestIntId()))
				{
					options.put(x.getQuest().getQuestIntId(), new QuestInfo(x.getQuest(), player, false));
				}
			}
		}
		if (options.size() > 1)
		{
			List<QuestInfo> l = new ArrayList<>();
			l.addAll(options.values());
			Collections.sort(l);
			showQuestChooseWindow(player, l);
		}
		else if (options.size() == 1)
		{
			showQuestWindow(player, options.values().toArray(new QuestInfo[1])[0].getQuest().getName());
		}
		else
		{
			showQuestWindow(player, "");
		}
	}
	
	public void showQuestChooseWindow(Player player, List<QuestInfo> quests)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		for (QuestInfo info : quests)
		{
			Quest q = info.getQuest();
			if (!q.isVisible(player))
			{
				continue;
			}
			sb.append("<a action=\"bypass -h npc_").append(getObjectId()).append("_Quest ").append(q.getName()).append("\">").append(q.getDescr(player, info.isStart())).append("</a><br>");
		}
		sb.append("</body></html>");
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setHtml(sb.toString());
		player.sendPacket(html);
	}
	
	public void showChatWindow(Player player, int val, Object... replace)
	{
		if ((getTemplate().getTeleportList().size() > 0) && checkForDominionWard(player))
		{
			return;
		}
		String filename;
		int npcId = getNpcId();
		switch (npcId)
		{
			case 30298:
				if (player.getPledgeType() == Clan.SUBUNIT_ACADEMY)
				{
					filename = getHtmlPath(npcId, 1, player);
				}
				else
				{
					filename = getHtmlPath(npcId, 0, player);
				}
				break;
			default:
				filename = getHtmlPath(npcId, val, player);
				break;
		}
		NpcHtmlMessage packet = new NpcHtmlMessage(player, this, filename, val);
		if ((replace.length % 2) == 0)
		{
			for (int i = 0; i < replace.length; i += 2)
			{
				packet.replace(String.valueOf(replace[i]), String.valueOf(replace[i + 1]));
			}
		}
		player.sendPacket(packet);
	}
	
	public void showChatWindow(Player player, String filename, Object... replace)
	{
		NpcHtmlMessage packet = new NpcHtmlMessage(player, this, filename, 0);
		if ((replace.length % 2) == 0)
		{
			for (int i = 0; i < replace.length; i += 2)
			{
				packet.replace(String.valueOf(replace[i]), String.valueOf(replace[i + 1]));
			}
		}
		player.sendPacket(packet);
	}
	
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String pom;
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		if (getTemplate().getHtmRoot() != null)
		{
			return getTemplate().getHtmRoot() + pom + ".htm";
		}
		String temp = "default/" + pom + ".htm";
		if (HtmCache.getInstance().getNullable(temp, player) != null)
		{
			return temp;
		}
		temp = "trainer/" + pom + ".htm";
		if (HtmCache.getInstance().getNullable(temp, player) != null)
		{
			return temp;
		}
		return "npcdefault.htm";
	}
	
	private boolean _isBusy;
	private String _busyMessage = "";
	
	public final boolean isBusy()
	{
		return _isBusy;
	}
	
	public void setBusy(boolean isBusy)
	{
		_isBusy = isBusy;
	}
	
	public final String getBusyMessage()
	{
		return _busyMessage;
	}
	
	public void setBusyMessage(String message)
	{
		_busyMessage = message;
	}
	
	public void showBusyWindow(Player player)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile("npcbusy.htm");
		html.replace("%npcname%", getName());
		html.replace("%playername%", player.getName());
		html.replace("%busymessage%", _busyMessage);
		player.sendPacket(html);
	}
	
	public void showSkillList(Player player)
	{
		ClassId classId = player.getClassId();
		if (classId == null)
		{
			return;
		}
		int npcId = getTemplate().npcId;
		if (getTemplate().getTeachInfo().isEmpty())
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			StringBuilder sb = new StringBuilder();
			sb.append("<html><head><body>");
			if (player.getVar("lang@").equalsIgnoreCase("en"))
			{
				sb.append("I cannot teach you. My class list is empty.<br> Ask admin to fix it. <br>NpcId:" + npcId + ", Your classId:" + player.getClassId().getId() + "<br>");
			}
			else
			{
				sb.append("Я не могу обучить тебя. Для твоего класса мой список пуст.<br> Свяжись с админом для фикса этого. <br>NpcId:" + npcId + ", твой classId:" + player.getClassId().getId() + "<br>");
			}
			sb.append("</body></html>");
			html.setHtml(sb.toString());
			player.sendPacket(html);
			return;
		}
		if (!(getTemplate().canTeach(classId) || getTemplate().canTeach(classId.getParent(player.getSex()))))
		{
			if (this instanceof WarehouseInstance)
			{
				showChatWindow(player, "warehouse/" + getNpcId() + "-noteach.htm");
			}
			else if (this instanceof TrainerInstance)
			{
				showChatWindow(player, "trainer/" + getNpcId() + "-noteach.htm");
			}
			else
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				StringBuilder sb = new StringBuilder();
				sb.append("<html><head><body>");
				sb.append(new CustomMessage("lineage2.gameserver.model.instances.L2NpcInstance.WrongTeacherClass", player));
				sb.append("</body></html>");
				html.setHtml(sb.toString());
				player.sendPacket(html);
			}
			return;
		}
		final Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(player, AcquireType.NORMAL);
		final AcquireSkillList asl = new AcquireSkillList(AcquireType.NORMAL, skills.size());
		int counts = 0;
		for (SkillLearn s : skills)
		{
			Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
			if ((sk == null) || !sk.getCanLearn(player.getClassId()) || !sk.canTeachBy(npcId))
			{
				continue;
			}
			counts++;
			asl.addSkill(s.getId(), s.getLevel(), s.getLevel(), s.getCost(), 0);
		}
		if (counts == 0)
		{
			int minlevel = SkillAcquireHolder.getInstance().getMinLevelForNewSkill(player, AcquireType.NORMAL);
			if (minlevel > 0)
			{
				SystemMessage2 sm = new SystemMessage2(SystemMsg.YOU_DO_NOT_HAVE_ANY_FURTHER_SKILLS_TO_LEARN__COME_BACK_WHEN_YOU_HAVE_REACHED_LEVEL_S1);
				sm.addInteger(minlevel);
				player.sendPacket(sm);
			}
			else
			{
				player.sendPacket(SystemMsg.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
			}
			player.sendPacket(AcquireSkillDone.STATIC);
		}
		else
		{
			player.sendPacket(asl);
		}
		player.sendActionFailed();
	}
	
	public void showTransferSkillList(Player player)
	{
		ClassId classId = player.getClassId();
		if (classId == null)
		{
			return;
		}
		if ((player.getLevel() < 76) || (classId.getClassLevel().ordinal() < 4))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			StringBuilder sb = new StringBuilder();
			sb.append("<html><head><body>");
			sb.append("You must have 3rd class change quest completed.");
			sb.append("</body></html>");
			html.setHtml(sb.toString());
			player.sendPacket(html);
			return;
		}
		AcquireType type = AcquireType.transferType(player.getActiveClassId());
		if (type == null)
		{
			return;
		}
		showAcquireList(type, player);
	}
	
	public static void showCollectionSkillList(Player player)
	{
		showAcquireList(AcquireType.COLLECTION, player);
	}
	
	public void showTransformationMultisell(Player player)
	{
		if (!Config.ALLOW_LEARN_TRANS_SKILLS_WO_QUEST)
		{
			if (!player.isQuestCompleted("_136_MoreThanMeetsTheEye"))
			{
				showChatWindow(player, "trainer/" + getNpcId() + "-nobuy.htm");
				return;
			}
		}
		Castle castle = getCastle(player);
		MultiSellHolder.getInstance().SeparateAndSend(32323, player, castle != null ? castle.getTaxRate() : 0);
		player.sendActionFailed();
	}
	
	public void showTransformationSkillList(Player player, AcquireType type)
	{
		if (!Config.ALLOW_LEARN_TRANS_SKILLS_WO_QUEST)
		{
			if (!player.isQuestCompleted("_136_MoreThanMeetsTheEye"))
			{
				showChatWindow(player, "trainer/" + getNpcId() + "-noquest.htm");
				return;
			}
		}
		showAcquireList(type, player);
	}
	
	public static void showFishingSkillList(Player player)
	{
		showAcquireList(AcquireType.FISHING, player);
	}
	
	public static void showClanSkillList(Player player)
	{
		if ((player.getClan() == null) || !player.isClanLeader())
		{
			player.sendPacket(SystemMsg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
			player.sendActionFailed();
			return;
		}
		showAcquireList(AcquireType.CLAN, player);
	}
	
	public static void showAcquireList(AcquireType t, Player player)
	{
		final Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(player, t);
		final AcquireSkillList asl = new AcquireSkillList(t, skills.size());
		for (SkillLearn s : skills)
		{
			asl.addSkill(s.getId(), s.getLevel(), s.getLevel(), s.getCost(), 0);
		}
		if (skills.size() == 0)
		{
			player.sendPacket(AcquireSkillDone.STATIC);
			player.sendPacket(SystemMsg.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
		}
		else
		{
			player.sendPacket(asl);
		}
		player.sendActionFailed();
	}
	
	public static void showSubUnitSkillList(Player player)
	{
		Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		if ((player.getClanPrivileges() & Clan.CP_CL_TROOPS_FAME) != Clan.CP_CL_TROOPS_FAME)
		{
			player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		Set<SkillLearn> learns = new TreeSet<>();
		for (SubUnit sub : player.getClan().getAllSubUnits())
		{
			learns.addAll(SkillAcquireHolder.getInstance().getAvailableSkills(player, AcquireType.SUB_UNIT, sub));
		}
		final AcquireSkillList asl = new AcquireSkillList(AcquireType.SUB_UNIT, learns.size());
		for (SkillLearn s : learns)
		{
			asl.addSkill(s.getId(), s.getLevel(), s.getLevel(), s.getCost(), 1, Clan.SUBUNIT_KNIGHT4);
		}
		if (learns.size() == 0)
		{
			player.sendPacket(AcquireSkillDone.STATIC);
			player.sendPacket(SystemMsg.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
		}
		else
		{
			player.sendPacket(asl);
		}
		player.sendActionFailed();
	}
	
	public int getSpawnAnimation()
	{
		return _spawnAnimation;
	}
	
	@Override
	public double getColRadius()
	{
		return getCollisionRadius();
	}
	
	@Override
	public double getColHeight()
	{
		return getCollisionHeight();
	}
	
	public int calculateLevelDiffForDrop(int charLevel)
	{
		if (!Config.DEEPBLUE_DROP_RULES)
		{
			return 0;
		}
		int mobLevel = getLevel();
		int deepblue_maxdiff = this instanceof RaidBossInstance ? Config.DEEPBLUE_DROP_RAID_MAXDIFF : Config.DEEPBLUE_DROP_MAXDIFF;
		return Math.max(charLevel - mobLevel - deepblue_maxdiff, 0);
	}
	
	@Override
	public String toString()
	{
		return getNpcId() + " " + getName();
	}
	
	public void refreshID()
	{
		objectId = IdFactory.getInstance().getNextId();
		_storedId = GameObjectsStorage.refreshId(this);
	}
	
	private boolean _isUnderground = false;
	
	public void setUnderground(boolean b)
	{
		_isUnderground = b;
	}
	
	public boolean isUnderground()
	{
		return _isUnderground;
	}
	
	public boolean isTargetable()
	{
		return _isTargetable;
	}
	
	public void setTargetable(boolean value)
	{
		_isTargetable = value;
	}
	
	public void setAttackable(boolean value)
	{
		_isAttackable = value;
	}
	
	public boolean isShowName()
	{
		return _showName;
	}
	
	public boolean isShowTitle()
	{
		return _showTitle;
	}
	
	public void setShowName(boolean value)
	{
		_showName = value;
	}
	
	public void setShowTitle(boolean value)
	{
		_showTitle = value;
	}
	
	@Override
	public NpcListenerList getListeners()
	{
		if (listeners == null)
		{
			synchronized (this)
			{
				if (listeners == null)
				{
					listeners = new NpcListenerList(this);
				}
			}
		}
		return (NpcListenerList) listeners;
	}
	
	public <T extends NpcListener> boolean addListener(T listener)
	{
		return getListeners().add(listener);
	}
	
	public <T extends NpcListener> boolean removeListener(T listener)
	{
		return getListeners().remove(listener);
	}
	
	@Override
	public NpcStatsChangeRecorder getStatsRecorder()
	{
		if (_statsRecorder == null)
		{
			synchronized (this)
			{
				if (_statsRecorder == null)
				{
					_statsRecorder = new NpcStatsChangeRecorder(this);
				}
			}
		}
		return (NpcStatsChangeRecorder) _statsRecorder;
	}
	
	public void setNpcState(int stateId)
	{
		broadcastPacket(new ExChangeNpcState(getObjectId(), stateId));
		npcState = stateId;
	}
	
	public int getNpcState()
	{
		return npcState;
	}
	
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		List<L2GameServerPacket> list = new ArrayList<>(3);
		list.add(new NpcInfo(this, forPlayer));
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
	public boolean isNpc()
	{
		return true;
	}
	
	@Override
	public int getGeoZ(Location loc)
	{
		if (isFlying() || isInWater() || isInBoat() || isBoat() || isDoor())
		{
			return loc.z;
		}
		if (isNpc())
		{
			if (_spawnRange instanceof Territory)
			{
				return GeoEngine.getHeight(loc, getGeoIndex());
			}
			return loc.z;
		}
		return super.getGeoZ(loc);
	}
	
	@Override
	public Clan getClan()
	{
		Dominion dominion = getDominion();
		if (dominion == null)
		{
			return null;
		}
		int lordObjectId = dominion.getLordObjectId();
		return lordObjectId == 0 ? null : dominion.getOwner();
	}
	
	public NpcString getNameNpcString()
	{
		return _nameNpcString;
	}
	
	public NpcString getTitleNpcString()
	{
		return _titleNpcString;
	}
	
	public void setNameNpcString(NpcString nameNpcString)
	{
		_nameNpcString = nameNpcString;
	}
	
	public void setTitleNpcString(NpcString titleNpcString)
	{
		_titleNpcString = titleNpcString;
	}
	
	public boolean isMerchantNpc()
	{
		return false;
	}
	
	public SpawnRange getSpawnRange()
	{
		return _spawnRange;
	}
	
	public void setSpawnRange(SpawnRange spawnRange)
	{
		_spawnRange = spawnRange;
	}
	
	public boolean checkForDominionWard(Player player)
	{
		ItemInstance item = getActiveWeaponInstance();
		if ((item != null) && (item.getAttachment() instanceof TerritoryWardObject))
		{
			showChatWindow(player, "flagman.htm");
			return true;
		}
		return false;
	}
	
	public void setParameter(String str, Object val)
	{
		if (_parameters == StatsSet.EMPTY)
		{
			_parameters = new StatsSet();
		}
		_parameters.set(str, val);
	}
	
	public void setParameters(MultiValueSet<String> set)
	{
		if (set.isEmpty())
		{
			return;
		}
		if (_parameters == StatsSet.EMPTY)
		{
			_parameters = new MultiValueSet<>(set.size());
		}
		_parameters.putAll(set);
	}
	
	public int getParameter(String str, int val)
	{
		return _parameters.getInteger(str, val);
	}
	
	public long getParameter(String str, long val)
	{
		return _parameters.getLong(str, val);
	}
	
	public boolean getParameter(String str, boolean val)
	{
		return _parameters.getBool(str, val);
	}
	
	public String getParameter(String str, String val)
	{
		return _parameters.getString(str, val);
	}
	
	public MultiValueSet<String> getParameters()
	{
		return _parameters;
	}
	
	@Override
	public boolean isInvul()
	{
		return true;
	}
	
	public boolean isHasChatWindow()
	{
		return _hasChatWindow;
	}
	
	public void setHasChatWindow(boolean hasChatWindow)
	{
		_hasChatWindow = hasChatWindow;
	}
}
