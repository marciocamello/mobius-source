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
package lineage2.gameserver.model.quest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.instancemanager.SpawnManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExShowQuestMark;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.QuestList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.TutorialEnableClientEvent;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.TutorialShowQuestionMark;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.spawn.PeriodOfDay;
import lineage2.gameserver.utils.ItemFunctions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QuestState
{
	public class OnDeathListenerImpl implements OnDeathListener
	{
		@Override
		public void onDeath(Creature actor, Creature killer)
		{
			Player player = actor.getPlayer();
			if (player == null)
			{
				return;
			}
			player.removeListener(this);
			_quest.notifyDeath(killer, actor, QuestState.this);
		}
	}
	
	public class PlayerOnKillListenerImpl implements OnKillListener
	{
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			if (!victim.isPlayer())
			{
				return;
			}
			Player actorPlayer = (Player) actor;
			List<Player> players = null;
			switch (_quest.getParty())
			{
				case Quest.PARTY_NONE:
					players = Collections.singletonList(actorPlayer);
					break;
				case Quest.PARTY_ALL:
					if (actorPlayer.getParty() == null)
					{
						players = Collections.singletonList(actorPlayer);
					}
					else
					{
						players = new ArrayList<>(actorPlayer.getParty().getMemberCount());
						for (Player member : actorPlayer.getParty().getPartyMembers())
						{
							if (member.isInRange(actorPlayer, Creature.INTERACTION_DISTANCE))
							{
								players.add(member);
							}
						}
					}
					break;
				default:
					players = Collections.emptyList();
					break;
			}
			for (Player player : players)
			{
				QuestState questState = player.getQuestState(_quest.getClass());
				if ((questState != null) && !questState.isCompleted())
				{
					_quest.notifyKill((Player) victim, questState);
				}
			}
		}
		
		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
	}
	
	private static final Logger _log = LoggerFactory.getLogger(QuestState.class);
	public static final int RESTART_HOUR = 6;
	public static final int RESTART_MINUTES = 30;
	public static final String VAR_COND = "cond";
	public final static QuestState[] EMPTY_ARRAY = new QuestState[0];
	private final Player _player;
	final Quest _quest;
	private int _state;
	private Integer _cond = null;
	private final Map<String, String> _vars = new ConcurrentHashMap<>();
	private final Map<String, QuestTimer> _timers = new ConcurrentHashMap<>();
	private OnKillListener _onKillListener = null;
	
	public QuestState(Quest quest, Player player, int state)
	{
		_quest = quest;
		_player = player;
		player.setQuestState(this);
		_state = state;
		quest.notifyCreate(this);
	}
	
	public void addExpAndSp(long exp, long sp)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (exp > 0)
		{
			player.addExpAndSp((long) (exp * getRateQuestsReward()), 0);
		}
		if (sp > 0)
		{
			player.addExpAndSp(0, (long) (sp * getRateQuestsReward()));
		}
	}
	
	public void addNotifyOfDeath(Player player, boolean withPet)
	{
		OnDeathListenerImpl listener = new OnDeathListenerImpl();
		player.addListener(listener);
		if (withPet)
		{
			for (Summon summon : player.getSummonList())
			{
				summon.addListener(listener);
			}
		}
	}
	
	public void addPlayerOnKillListener()
	{
		if (_onKillListener != null)
		{
			throw new IllegalArgumentException("Cant add twice kill listener to player");
		}
		_onKillListener = new PlayerOnKillListenerImpl();
		_player.addListener(_onKillListener);
	}
	
	public void removePlayerOnKillListener()
	{
		if (_onKillListener != null)
		{
			_player.removeListener(_onKillListener);
		}
	}
	
	public void addRadar(int x, int y, int z)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.addRadar(x, y, z);
		}
	}
	
	public void addRadarWithMap(int x, int y, int z)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.addRadarWithMap(x, y, z);
		}
	}
	
	public void exitCurrentQuest(Quest quest)
	{
		Player player = getPlayer();
		exitCurrentQuest(true);
		quest.newQuestState(player, Quest.DELAYED);
		QuestState qs = player.getQuestState(quest.getClass());
		qs.setRestartTime();
	}
	
	public QuestState exitCurrentQuest(boolean repeatable)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return this;
		}
		removePlayerOnKillListener();
		for (int itemId : _quest.getItems())
		{
			ItemInstance item = player.getInventory().getItemByItemId(itemId);
			if ((item == null) || (itemId == 57))
			{
				continue;
			}
			long count = item.getCount();
			player.getInventory().destroyItemByItemId(itemId, count);
			player.getWarehouse().destroyItemByItemId(itemId, count);
		}
		if (repeatable)
		{
			player.removeQuestState(_quest.getName());
			Quest.deleteQuestInDb(this);
			_vars.clear();
		}
		else
		{
			for (String var : _vars.keySet())
			{
				if (var != null)
				{
					unset(var);
				}
			}
			setState(Quest.COMPLETED);
			Quest.updateQuestInDb(this);
		}
		player.sendPacket(new QuestList(player));
		return this;
	}
	
	public void abortQuest()
	{
		_quest.onAbort(this);
		exitCurrentQuest(true);
	}
	
	public String get(String var)
	{
		return _vars.get(var);
	}
	
	public Map<String, String> getVars()
	{
		return _vars;
	}
	
	public int getInt(String var)
	{
		int varint = 0;
		try
		{
			String val = get(var);
			if (val == null)
			{
				return 0;
			}
			varint = Integer.parseInt(val);
		}
		catch (Exception e)
		{
			_log.error(getPlayer().getName() + ": variable " + var + " isn't an integer: " + varint, e);
		}
		return varint;
	}
	
	public int getItemEquipped(int loc)
	{
		return getPlayer().getInventory().getPaperdollItemId(loc);
	}
	
	public Player getPlayer()
	{
		return _player;
	}
	
	public Quest getQuest()
	{
		return _quest;
	}
	
	public boolean checkQuestItemsCount(int... itemIds)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return false;
		}
		for (int itemId : itemIds)
		{
			if (player.getInventory().getCountOf(itemId) <= 0)
			{
				return false;
			}
		}
		return true;
	}
	
	public long getSumQuestItemsCount(int... itemIds)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return 0;
		}
		long count = 0;
		for (int itemId : itemIds)
		{
			count += player.getInventory().getCountOf(itemId);
		}
		return count;
	}
	
	public long getQuestItemsCount(int itemId)
	{
		Player player = getPlayer();
		return player == null ? 0 : player.getInventory().getCountOf(itemId);
	}
	
	public long getQuestItemsCount(int... itemsIds)
	{
		long result = 0;
		for (int id : itemsIds)
		{
			result += getQuestItemsCount(id);
		}
		return result;
	}
	
	public boolean haveQuestItem(int itemId, int count)
	{
		if (getQuestItemsCount(itemId) >= count)
		{
			return true;
		}
		return false;
	}
	
	public boolean haveQuestItem(int itemId)
	{
		return haveQuestItem(itemId, 1);
	}
	
	public int getState()
	{
		return _state == Quest.DELAYED ? Quest.CREATED : _state;
	}
	
	public String getStateName()
	{
		return Quest.getStateName(_state);
	}
	
	public void giveItems(int itemId, long count)
	{
		if (itemId == ItemTemplate.ITEM_ID_ADENA)
		{
			giveItems(itemId, count, true);
		}
		else
		{
			giveItems(itemId, count, false);
		}
	}
	
	public void giveItems(int itemId, long count, boolean rate)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (count <= 0)
		{
			count = 1;
		}
		if (rate)
		{
			count = (long) (count * getRateQuestsReward());
		}
		ItemFunctions.addItem(player, itemId, count, true);
		player.sendChanges();
	}
	
	public void giveItems(int itemId, long count, Element element, int power)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		if (count <= 0)
		{
			count = 1;
		}
		ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
		if (template == null)
		{
			return;
		}
		for (int i = 0; i < count; i++)
		{
			ItemInstance item = ItemFunctions.createItem(itemId);
			if (element != Element.NONE)
			{
				item.setAttributeElement(element, power);
			}
			player.getInventory().addItem(item);
		}
		player.sendPacket(SystemMessage2.obtainItems(template.getItemId(), count, 0));
		player.sendChanges();
	}
	
	public void dropItem(NpcInstance npc, int itemId, long count)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		ItemInstance item = ItemFunctions.createItem(itemId);
		item.setCount(count);
		item.dropToTheGround(player, npc);
	}
	
	public int rollDrop(int count, double calcChance)
	{
		if ((calcChance <= 0) || (count <= 0))
		{
			return 0;
		}
		return rollDrop(count, count, calcChance);
	}
	
	public int rollDrop(int min, int max, double calcChance)
	{
		if ((calcChance <= 0) || (min <= 0) || (max <= 0))
		{
			return 0;
		}
		int dropmult = 1;
		calcChance *= getRateQuestsDrop();
		if (getQuest().getParty() > Quest.PARTY_NONE)
		{
			Player player = getPlayer();
			if (player.getParty() != null)
			{
				calcChance *= Config.ALT_PARTY_BONUS[player.getParty().getMemberCountInRange(player, Config.ALT_PARTY_DISTRIBUTION_RANGE) - 1];
			}
		}
		if (calcChance > 100)
		{
			if ((int) Math.ceil(calcChance / 100) <= (calcChance / 100))
			{
				calcChance = Math.nextUp(calcChance);
			}
			dropmult = (int) Math.ceil(calcChance / 100);
			calcChance = calcChance / dropmult;
		}
		return Rnd.chance(calcChance) ? Rnd.get(min * dropmult, max * dropmult) : 0;
	}
	
	public double getRateQuestsDrop()
	{
		Player player = getPlayer();
		double Bonus = player == null ? 1. : player.getBonus().getQuestDropRate();
		return Config.RATE_QUESTS_DROP * Bonus;
	}
	
	public double getRateQuestsReward()
	{
		Player player = getPlayer();
		double Bonus = player == null ? 1. : player.getBonus().getQuestRewardRate();
		return Config.RATE_QUESTS_REWARD * Bonus;
	}
	
	public boolean rollAndGive(int itemId, int min, int max, int limit, double calcChance)
	{
		if ((calcChance <= 0) || (min <= 0) || (max <= 0) || (limit <= 0) || (itemId <= 0))
		{
			return false;
		}
		long count = rollDrop(min, max, calcChance);
		if (count > 0)
		{
			long alreadyCount = getQuestItemsCount(itemId);
			if ((alreadyCount + count) > limit)
			{
				count = limit - alreadyCount;
			}
			if (count > 0)
			{
				giveItems(itemId, count, false);
				if ((count + alreadyCount) < limit)
				{
					playSound(Quest.SOUND_ITEMGET);
				}
				else
				{
					playSound(Quest.SOUND_MIDDLE);
					return true;
				}
			}
		}
		return false;
	}
	
	public void rollAndGive(int itemId, int min, int max, double calcChance)
	{
		if ((calcChance <= 0) || (min <= 0) || (max <= 0) || (itemId <= 0))
		{
			return;
		}
		int count = rollDrop(min, max, calcChance);
		if (count > 0)
		{
			giveItems(itemId, count, false);
			playSound(Quest.SOUND_ITEMGET);
		}
	}
	
	public boolean rollAndGive(int itemId, int count, double calcChance)
	{
		if ((calcChance <= 0) || (count <= 0) || (itemId <= 0))
		{
			return false;
		}
		int countToDrop = rollDrop(count, calcChance);
		if (countToDrop > 0)
		{
			giveItems(itemId, countToDrop, false);
			playSound(Quest.SOUND_ITEMGET);
			return true;
		}
		return false;
	}
	
	public boolean isCompleted()
	{
		return getState() == Quest.COMPLETED;
	}
	
	public boolean isStarted()
	{
		return getState() == Quest.STARTED;
	}
	
	public boolean isCreated()
	{
		return getState() == Quest.CREATED;
	}
	
	public void killNpcByObjectId(int _objId)
	{
		NpcInstance npc = GameObjectsStorage.getNpc(_objId);
		if (npc != null)
		{
			npc.doDie(null);
		}
		else
		{
			_log.warn("Attemp to kill object that is not npc in quest " + getQuest().getQuestIntId());
		}
	}
	
	public String set(String var, String val)
	{
		return set(var, val, true);
	}
	
	public String set(String var, int intval)
	{
		return set(var, String.valueOf(intval), true);
	}
	
	public String set(String var, String val, boolean store)
	{
		if (val == null)
		{
			val = StringUtils.EMPTY;
		}
		_vars.put(var, val);
		if (store)
		{
			Quest.updateQuestVarInDb(this, var, val);
		}
		return val;
	}
	
	public Object setState(int state)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return null;
		}
		_state = state;
		if (getQuest().isVisible(player) && isStarted())
		{
			player.sendPacket(new ExShowQuestMark(getQuest().getQuestIntId()));
		}
		Quest.updateQuestInDb(this);
		player.sendPacket(new QuestList(player));
		return state;
	}
	
	public Object setStateAndNotSave(int state)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return null;
		}
		_state = state;
		if (getQuest().isVisible(player) && isStarted())
		{
			player.sendPacket(new ExShowQuestMark(getQuest().getQuestIntId()));
		}
		player.sendPacket(new QuestList(player));
		return state;
	}
	
	public void playSound(String sound)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new PlaySound(sound));
		}
	}
	
	public void playTutorialVoice(String voice)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new PlaySound(PlaySound.Type.VOICE, voice, 0, 0, player.getLoc()));
		}
	}
	
	public void onTutorialClientEvent(int number)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new TutorialEnableClientEvent(number));
		}
	}
	
	public void showQuestionMark(int number)
	{
		Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new TutorialShowQuestionMark(number));
		}
	}
	
	public void showTutorialHTML(String html, int type)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		String text = html;
		if (type == 1)
		{
			text = HtmCache.getInstance().getNotNull("quests/_255_Tutorial/" + html, player);
		}
		player.sendPacket(new TutorialShowHtml(text, type));
	}
	
	public void startQuestTimer(String name, long time)
	{
		startQuestTimer(name, time, null);
	}
	
	public void startQuestTimer(String name, long time, NpcInstance npc)
	{
		QuestTimer timer = new QuestTimer(name, time, npc);
		timer.setQuestState(this);
		QuestTimer oldTimer = getTimers().put(name, timer);
		if (oldTimer != null)
		{
			oldTimer.stop();
		}
		timer.start();
	}
	
	public boolean isRunningQuestTimer(String name)
	{
		return getTimers().get(name) != null;
	}
	
	public boolean cancelQuestTimer(String name)
	{
		QuestTimer timer = removeQuestTimer(name);
		if (timer != null)
		{
			timer.stop();
		}
		return timer != null;
	}
	
	QuestTimer removeQuestTimer(String name)
	{
		QuestTimer timer = getTimers().remove(name);
		if (timer != null)
		{
			timer.setQuestState(null);
		}
		return timer;
	}
	
	public void pauseQuestTimers()
	{
		getQuest().pauseQuestTimers(this);
	}
	
	public void stopQuestTimers()
	{
		for (QuestTimer timer : getTimers().values())
		{
			timer.setQuestState(null);
			timer.stop();
		}
		_timers.clear();
	}
	
	public void resumeQuestTimers()
	{
		getQuest().resumeQuestTimers(this);
	}
	
	Map<String, QuestTimer> getTimers()
	{
		return _timers;
	}
	
	public long takeItems(int itemId, long count)
	{
		Player player = getPlayer();
		if (player == null)
		{
			return 0;
		}
		ItemInstance item = player.getInventory().getItemByItemId(itemId);
		if (item == null)
		{
			return 0;
		}
		if ((count < 0) || (count > item.getCount()))
		{
			count = item.getCount();
		}
		player.getInventory().destroyItemByItemId(itemId, count);
		player.sendPacket(SystemMessage2.removeItems(itemId, count));
		return count;
	}
	
	public long takeAllItems(int itemId)
	{
		return takeItems(itemId, -1);
	}
	
	public long takeAllItems(int... itemsIds)
	{
		long result = 0;
		for (int id : itemsIds)
		{
			result += takeAllItems(id);
		}
		return result;
	}
	
	public long takeAllItems(Collection<Integer> itemsIds)
	{
		long result = 0;
		for (int id : itemsIds)
		{
			result += takeAllItems(id);
		}
		return result;
	}
	
	public String unset(String var)
	{
		if (var == null)
		{
			return null;
		}
		String old = _vars.remove(var);
		if (old != null)
		{
			Quest.deleteQuestVarInDb(this, var);
		}
		return old;
	}
	
	private boolean checkPartyMember(Player member, int state, int maxrange, GameObject rangefrom)
	{
		if (member == null)
		{
			return false;
		}
		if ((rangefrom != null) && (maxrange > 0) && !member.isInRange(rangefrom, maxrange))
		{
			return false;
		}
		QuestState qs = member.getQuestState(getQuest().getName());
		if ((qs == null) || (qs.getState() != state))
		{
			return false;
		}
		return true;
	}
	
	public List<Player> getPartyMembers(int state, int maxrange, GameObject rangefrom)
	{
		List<Player> result = new ArrayList<>();
		Party party = getPlayer().getParty();
		if (party == null)
		{
			if (checkPartyMember(getPlayer(), state, maxrange, rangefrom))
			{
				result.add(getPlayer());
			}
			return result;
		}
		for (Player _member : party.getPartyMembers())
		{
			if (checkPartyMember(_member, state, maxrange, rangefrom))
			{
				result.add(getPlayer());
			}
		}
		return result;
	}
	
	public Player getRandomPartyMember(int state, int maxrangefromplayer)
	{
		return getRandomPartyMember(state, maxrangefromplayer, getPlayer());
	}
	
	public Player getRandomPartyMember(int state, int maxrange, GameObject rangefrom)
	{
		List<Player> list = getPartyMembers(state, maxrange, rangefrom);
		if (list.size() == 0)
		{
			return null;
		}
		return list.get(Rnd.get(list.size()));
	}
	
	public NpcInstance addSpawn(int npcId)
	{
		return addSpawn(npcId, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, 0, 0);
	}
	
	public NpcInstance addSpawn(int npcId, int despawnDelay)
	{
		return addSpawn(npcId, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, 0, despawnDelay);
	}
	
	public NpcInstance addSpawn(int npcId, int x, int y, int z)
	{
		return addSpawn(npcId, x, y, z, 0, 0, 0);
	}
	
	public NpcInstance addSpawn(int npcId, int x, int y, int z, int despawnDelay)
	{
		return addSpawn(npcId, x, y, z, 0, 0, despawnDelay);
	}
	
	public NpcInstance addSpawn(int npcId, int x, int y, int z, int heading, int randomOffset, int despawnDelay)
	{
		return getQuest().addSpawn(npcId, x, y, z, heading, randomOffset, despawnDelay);
	}
	
	public NpcInstance findTemplate(int npcId)
	{
		for (Spawner spawn : SpawnManager.getInstance().getSpawners(PeriodOfDay.NONE.name()))
		{
			if ((spawn != null) && (spawn.getCurrentNpcId() == npcId))
			{
				return spawn.getLastSpawn();
			}
		}
		return null;
	}
	
	public int calculateLevelDiffForDrop(int mobLevel, int player)
	{
		if (!Config.DEEPBLUE_DROP_RULES)
		{
			return 0;
		}
		return Math.max(player - mobLevel - Config.DEEPBLUE_DROP_MAXDIFF, 0);
	}
	
	public int getCond()
	{
		if (_cond == null)
		{
			int val = getInt(VAR_COND);
			if ((val & 0x80000000) != 0)
			{
				val &= 0x7fffffff;
				for (int i = 1; i < 32; i++)
				{
					val = (val >> 1);
					if (val == 0)
					{
						val = i;
						break;
					}
				}
			}
			_cond = val;
		}
		return _cond;
	}
	
	public String setCond(int newCond)
	{
		return setCond(newCond, true);
	}
	
	public String setCond(int newCond, boolean store)
	{
		if (newCond == getCond())
		{
			return String.valueOf(newCond);
		}
		int oldCond = getInt(VAR_COND);
		_cond = newCond;
		if ((oldCond & 0x80000000) != 0)
		{
			if (newCond > 2)
			{
				oldCond &= 0x80000001 | ((1 << newCond) - 1);
				newCond = oldCond | (1 << (newCond - 1));
			}
		}
		else
		{
			if (newCond > 2)
			{
				newCond = 0x80000001 | (1 << (newCond - 1)) | ((1 << oldCond) - 1);
			}
		}
		final String sVal = String.valueOf(newCond);
		final String result = set(VAR_COND, sVal, false);
		if (store)
		{
			Quest.updateQuestVarInDb(this, VAR_COND, sVal);
		}
		final Player player = getPlayer();
		if (player != null)
		{
			player.sendPacket(new QuestList(player));
			if ((newCond != 0) && getQuest().isVisible(player) && isStarted())
			{
				player.sendPacket(new ExShowQuestMark(getQuest().getQuestIntId()));
			}
		}
		return result;
	}
	
	public void setRestartTime()
	{
		Calendar reDo = Calendar.getInstance();
		if (reDo.get(Calendar.HOUR_OF_DAY) >= RESTART_HOUR)
		{
			reDo.add(Calendar.DATE, 1);
		}
		reDo.set(Calendar.HOUR_OF_DAY, RESTART_HOUR);
		reDo.set(Calendar.MINUTE, RESTART_MINUTES);
		set("restartTime", String.valueOf(reDo.getTimeInMillis()));
	}
	
	public boolean isNowAvailableByTime()
	{
		String val = get("restartTime");
		if (val == null)
		{
			return true;
		}
		long restartTime = Long.parseLong(val);
		return restartTime <= System.currentTimeMillis();
	}
}
