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
package lineage2.gameserver.model.entity.events.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.dao.DominionRewardDAO;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.dao.SiegePlayerDAO;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.objects.DoorObject;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.ZoneObject;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExDominionWarEnd;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import lineage2.gameserver.network.serverpackets.RelationChanged;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.utils.Location;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DominionSiegeEvent extends SiegeEvent<Dominion, SiegeClanObject>
{
	/**
	 * @author Mobius
	 */
	public class DoorDeathListener implements OnDeathListener
	{
		/**
		 * Method onDeath.
		 * @param actor Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature actor, Creature killer)
		{
			if (!isInProgress())
			{
				return;
			}
			DoorInstance door = (DoorInstance) actor;
			if (door.getDoorType() == DoorTemplate.DoorType.WALL)
			{
				return;
			}
			Player player = killer.getPlayer();
			if (player != null)
			{
				player.sendPacket(SystemMsg.THE_CASTLE_GATE_HAS_BEEN_DESTROYED);
			}
			Clan owner = getResidence().getOwner();
			if ((owner != null) && owner.getLeader().isOnline())
			{
				owner.getLeader().getPlayer().sendPacket(SystemMsg.THE_CASTLE_GATE_HAS_BEEN_DESTROYED);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class KillListener implements OnKillListener
	{
		/**
		 * Method onKill.
		 * @param actor Creature
		 * @param victim Creature
		 * @see lineage2.gameserver.listener.actor.OnKillListener#onKill(Creature, Creature)
		 */
		@Override
		public void onKill(Creature actor, Creature victim)
		{
			Player winner = actor.getPlayer();
			if ((winner == null) || !victim.isPlayer() || (winner.getLevel() < 40) || (winner == victim) || (victim.getEvent(DominionSiegeEvent.class) == DominionSiegeEvent.this) || !actor.isInZone(Zone.ZoneType.SIEGE) || !victim.isInZone(Zone.ZoneType.SIEGE))
			{
				return;
			}
			winner.setFame(winner.getFame() + Rnd.get(10, 20), DominionSiegeEvent.this.toString());
			addReward(winner, KILL_REWARD, 1);
			if (victim.getLevel() >= 61)
			{
				Quest q = _runnerEvent.getClassQuest(((Player) victim).getClassId());
				if (q == null)
				{
					return;
				}
				QuestState questState = winner.getQuestState(q.getClass());
				if (questState == null)
				{
					questState = q.newQuestState(winner, Quest.CREATED);
					q.notifyKill(((Player) victim), questState);
				}
			}
		}
		
		/**
		 * Method ignorePetOrSummon.
		 * @return boolean * @see lineage2.gameserver.listener.actor.OnKillListener#ignorePetOrSummon()
		 */
		@Override
		public boolean ignorePetOrSummon()
		{
			return true;
		}
	}
	
	/**
	 * Field KILL_REWARD. (value is 0)
	 */
	public static final int KILL_REWARD = 0;
	/**
	 * Field ONLINE_REWARD. (value is 1)
	 */
	public static final int ONLINE_REWARD = 1;
	/**
	 * Field STATIC_BADGES. (value is 2)
	 */
	public static final int STATIC_BADGES = 2;
	/**
	 * Field REWARD_MAX. (value is 3)
	 */
	public static final int REWARD_MAX = 3;
	/**
	 * Field ATTACKER_PLAYERS. (value is ""attacker_players"")
	 */
	public static final String ATTACKER_PLAYERS = "attacker_players";
	/**
	 * Field DEFENDER_PLAYERS. (value is ""defender_players"")
	 */
	public static final String DEFENDER_PLAYERS = "defender_players";
	/**
	 * Field DISGUISE_PLAYERS. (value is ""disguise_players"")
	 */
	public static final String DISGUISE_PLAYERS = "disguise_players";
	/**
	 * Field TERRITORY_NPC. (value is ""territory_npc"")
	 */
	public static final String TERRITORY_NPC = "territory_npc";
	/**
	 * Field CATAPULT. (value is ""catapult"")
	 */
	public static final String CATAPULT = "catapult";
	/**
	 * Field CATAPULT_DOORS. (value is ""catapult_doors"")
	 */
	public static final String CATAPULT_DOORS = "catapult_doors";
	/**
	 * Field _runnerEvent.
	 */
	DominionSiegeRunnerEvent _runnerEvent;
	/**
	 * Field _forSakeQuest.
	 */
	private Quest _forSakeQuest;
	/**
	 * Field _playersRewards.
	 */
	private final IntObjectMap<int[]> _playersRewards = new CHashIntObjectMap<>();
	
	/**
	 * Constructor for DominionSiegeEvent.
	 * @param set MultiValueSet<String>
	 */
	public DominionSiegeEvent(MultiValueSet<String> set)
	{
		super(set);
		_killListener = new KillListener();
		_doorDeathListener = new DoorDeathListener();
	}
	
	/**
	 * Method initEvent.
	 */
	@Override
	public void initEvent()
	{
		_runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
		super.initEvent();
		SiegeEvent<?, ?> castleSiegeEvent = getResidence().getCastle().getSiegeEvent();
		addObjects("mass_gatekeeper", castleSiegeEvent.getObjects("mass_gatekeeper"));
		addObjects(CastleSiegeEvent.CONTROL_TOWERS, castleSiegeEvent.getObjects(CastleSiegeEvent.CONTROL_TOWERS));
		List<DoorObject> doorObjects = getObjects(DOORS);
		for (DoorObject doorObject : doorObjects)
		{
			doorObject.getDoor().addListener(_doorDeathListener);
		}
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		List<Dominion> registeredDominions = _runnerEvent.getRegisteredDominions();
		List<DominionSiegeEvent> dominions = new ArrayList<>(9);
		for (Dominion d : registeredDominions)
		{
			if ((d.getSiegeDate().getTimeInMillis() != 0) && (d != getResidence()))
			{
				dominions.add(d.<DominionSiegeEvent> getSiegeEvent());
			}
		}
		SiegeClanObject ownerClan = new SiegeClanObject(DEFENDERS, getResidence().getOwner(), 0);
		addObject(DEFENDERS, ownerClan);
		for (DominionSiegeEvent d : dominions)
		{
			d.addObject(ATTACKERS, ownerClan);
			List<Integer> defenderPlayers = d.getObjects(DEFENDER_PLAYERS);
			for (int i : defenderPlayers)
			{
				addObject(ATTACKER_PLAYERS, i);
			}
			List<SiegeClanObject> otherDefenders = d.getObjects(DEFENDERS);
			for (SiegeClanObject siegeClan : otherDefenders)
			{
				if (siegeClan.getClan() != d.getResidence().getOwner())
				{
					addObject(ATTACKERS, siegeClan);
				}
			}
		}
		int[] flags = getResidence().getFlags();
		if (flags.length > 0)
		{
			getResidence().removeSkills();
			getResidence().getOwner().broadcastToOnlineMembers(SystemMsg.THE_EFFECT_OF_TERRITORY_WARD_IS_DISAPPEARING);
		}
		SiegeClanDAO.getInstance().delete(getResidence());
		SiegePlayerDAO.getInstance().delete(getResidence());
		for (int i : flags)
		{
			spawnAction("ward_" + i, true);
		}
		updateParticles(true);
		super.startEvent();
	}
	
	/**
	 * Method stopEvent.
	 * @param t boolean
	 */
	@Override
	public void stopEvent(boolean t)
	{
		getObjects(DISGUISE_PLAYERS).clear();
		int[] flags = getResidence().getFlags();
		for (int i : flags)
		{
			spawnAction("ward_" + i, false);
		}
		getResidence().rewardSkills();
		getResidence().setJdbcState(JdbcEntityState.UPDATED);
		getResidence().update();
		updateParticles(false);
		List<SiegeClanObject> defenders = getObjects(DEFENDERS);
		for (SiegeClanObject clan : defenders)
		{
			clan.deleteFlag();
		}
		super.stopEvent(t);
		DominionRewardDAO.getInstance().insert(getResidence());
	}
	
	/**
	 * Method loadSiegeClans.
	 */
	@Override
	public void loadSiegeClans()
	{
		addObjects(DEFENDERS, SiegeClanDAO.getInstance().load(getResidence(), DEFENDERS));
		addObjects(DEFENDER_PLAYERS, SiegePlayerDAO.getInstance().select(getResidence(), 0));
		DominionRewardDAO.getInstance().select(getResidence());
	}
	
	/**
	 * Method updateParticles.
	 * @param start boolean
	 * @param arg String[]
	 */
	@Override
	public void updateParticles(boolean start, String... arg)
	{
		boolean battlefieldChat = _runnerEvent.isBattlefieldChatActive();
		List<SiegeClanObject> siegeClans = getObjects(DEFENDERS);
		for (SiegeClanObject s : siegeClans)
		{
			if (battlefieldChat)
			{
				s.getClan().setWarDominion(start ? getId() : 0);
				PledgeShowInfoUpdate packet = new PledgeShowInfoUpdate(s.getClan());
				for (Player player : s.getClan().getOnlineMembers(0))
				{
					player.sendPacket(packet);
					updatePlayer(player, start);
				}
			}
			else
			{
				for (Player player : s.getClan().getOnlineMembers(0))
				{
					updatePlayer(player, start);
				}
			}
		}
		List<Integer> players = getObjects(DEFENDER_PLAYERS);
		for (int i : players)
		{
			Player player = GameObjectsStorage.getPlayer(i);
			if (player != null)
			{
				updatePlayer(player, start);
			}
		}
	}
	
	/**
	 * Method updatePlayer.
	 * @param player Player
	 * @param start boolean
	 */
	public void updatePlayer(Player player, boolean start)
	{
		player.setBattlefieldChatId(_runnerEvent.isBattlefieldChatActive() ? getId() : 0);
		if (_runnerEvent.isBattlefieldChatActive())
		{
			if (start)
			{
				player.addEvent(this);
				addReward(player, STATIC_BADGES, 5);
			}
			else
			{
				player.removeEvent(this);
				addReward(player, STATIC_BADGES, 5);
				player.getEffectList().stopEffect(Skill.SKILL_BATTLEFIELD_DEATH_SYNDROME);
				player.addExpAndSp(270000, 27000);
			}
			player.broadcastCharInfo();
			if (!start)
			{
				player.sendPacket(ExDominionWarEnd.STATIC);
			}
			questUpdate(player, start);
		}
	}
	
	/**
	 * Method questUpdate.
	 * @param player Player
	 * @param start boolean
	 */
	public void questUpdate(Player player, boolean start)
	{
		if (start)
		{
			QuestState sakeQuestState = _forSakeQuest.newQuestState(player, Quest.CREATED);
			sakeQuestState.setState(Quest.STARTED);
			sakeQuestState.setCond(1);
			Quest protectCatapultQuest = QuestManager.getQuest("_729_ProtectTheTerritoryCatapult");
			if (protectCatapultQuest == null)
			{
				return;
			}
			QuestState questState = protectCatapultQuest.newQuestStateAndNotSave(player, Quest.CREATED);
			questState.setCond(1, false);
			questState.setStateAndNotSave(Quest.STARTED);
		}
		else
		{
			for (Quest q : _runnerEvent.getBreakQuests())
			{
				QuestState questState = player.getQuestState(q.getClass());
				if (questState != null)
				{
					questState.abortQuest();
				}
			}
		}
	}
	
	/**
	 * Method isParticle.
	 * @param player Player
	 * @return boolean
	 */
	@Override
	public boolean isParticle(Player player)
	{
		if (isInProgress() || _runnerEvent.isBattlefieldChatActive())
		{
			boolean registered = getObjects(DEFENDER_PLAYERS).contains(player.getObjectId()) || (getSiegeClan(DEFENDERS, player.getClan()) != null);
			if (!registered)
			{
				return false;
			}
			if (isInProgress())
			{
				return true;
			}
			player.setBattlefieldChatId(getId());
			return false;
		}
		return false;
	}
	
	/**
	 * Method getRelation.
	 * @param thisPlayer Player
	 * @param targetPlayer Player
	 * @param result int
	 * @return int
	 */
	@Override
	public int getRelation(Player thisPlayer, Player targetPlayer, int result)
	{
		DominionSiegeEvent event2 = targetPlayer.getEvent(DominionSiegeEvent.class);
		if (event2 == null)
		{
			return result;
		}
		result |= RelationChanged.RELATION_ISINTERRITORYWARS;
		return result;
	}
	
	/**
	 * Method getUserRelation.
	 * @param thisPlayer Player
	 * @param oldRelation int
	 * @return int
	 */
	@Override
	public int getUserRelation(Player thisPlayer, int oldRelation)
	{
		oldRelation |= 0x1000;
		return oldRelation;
	}
	
	/**
	 * Method checkForAttack.
	 * @param target Creature
	 * @param attacker Creature
	 * @param skill Skill
	 * @param force boolean
	 * @return SystemMsg
	 */
	@Override
	public SystemMsg checkForAttack(Creature target, Creature attacker, Skill skill, boolean force)
	{
		DominionSiegeEvent dominionSiegeEvent = target.getEvent(DominionSiegeEvent.class);
		if (this == dominionSiegeEvent)
		{
			return SystemMsg.YOU_CANNOT_FORCE_ATTACK_A_MEMBER_OF_THE_SAME_TERRITORY;
		}
		return null;
	}
	
	/**
	 * Method broadcastTo.
	 * @param packet IStaticPacket
	 * @param types String[]
	 */
	@Override
	public void broadcastTo(IStaticPacket packet, String... types)
	{
		List<SiegeClanObject> siegeClans = getObjects(DEFENDERS);
		for (SiegeClanObject siegeClan : siegeClans)
		{
			siegeClan.broadcast(packet);
		}
		List<Integer> players = getObjects(DEFENDER_PLAYERS);
		for (int i : players)
		{
			Player player = GameObjectsStorage.getPlayer(i);
			if (player != null)
			{
				player.sendPacket(packet);
			}
		}
	}
	
	/**
	 * Method broadcastTo.
	 * @param packet L2GameServerPacket
	 * @param types String[]
	 */
	@Override
	public void broadcastTo(L2GameServerPacket packet, String... types)
	{
		List<SiegeClanObject> siegeClans = getObjects(DEFENDERS);
		for (SiegeClanObject siegeClan : siegeClans)
		{
			siegeClan.broadcast(packet);
		}
		List<Integer> players = getObjects(DEFENDER_PLAYERS);
		for (int i : players)
		{
			Player player = GameObjectsStorage.getPlayer(i);
			if (player != null)
			{
				player.sendPacket(packet);
			}
		}
	}
	
	/**
	 * Method giveItem.
	 * @param player Player
	 * @param itemId int
	 * @param count long
	 */
	@Override
	public void giveItem(Player player, int itemId, long count)
	{
		Zone zone = player.getZone(Zone.ZoneType.SIEGE);
		if (zone == null)
		{
			count = 0;
		}
		else
		{
			int id = zone.getParams().getInteger("residence");
			if (id < 100)
			{
				count = 125;
			}
			else
			{
				count = 31;
			}
		}
		addReward(player, ONLINE_REWARD, 1);
		super.giveItem(player, itemId, count);
	}
	
	/**
	 * Method itemObtainPlayers.
	 * @return List<Player>
	 */
	@Override
	public List<Player> itemObtainPlayers()
	{
		List<Player> playersInZone = getPlayersInZone();
		List<Player> list = new LazyArrayList<>(playersInZone.size());
		for (Player player : getPlayersInZone())
		{
			if (player.getEvent(DominionSiegeEvent.class) != null)
			{
				list.add(player);
			}
		}
		return list;
	}
	
	/**
	 * Method checkRestartLocs.
	 * @param player Player
	 * @param r Map<RestartType,Boolean>
	 */
	@Override
	public void checkRestartLocs(Player player, Map<RestartType, Boolean> r)
	{
		if (getObjects(FLAG_ZONES).isEmpty())
		{
			return;
		}
		SiegeClanObject clan = getSiegeClan(DEFENDERS, player.getClan());
		if ((clan != null) && (clan.getFlag() != null))
		{
			r.put(RestartType.TO_FLAG, Boolean.TRUE);
		}
	}
	
	/**
	 * Method getRestartLoc.
	 * @param player Player
	 * @param type RestartType
	 * @return Location
	 */
	@Override
	public Location getRestartLoc(Player player, RestartType type)
	{
		if (type == RestartType.TO_FLAG)
		{
			SiegeClanObject defenderClan = getSiegeClan(DEFENDERS, player.getClan());
			if ((defenderClan != null) && (defenderClan.getFlag() != null))
			{
				return Location.findPointToStay(defenderClan.getFlag(), 50, 75);
			}
			player.sendPacket(SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE);
			return null;
		}
		return super.getRestartLoc(player, type);
	}
	
	/**
	 * Method getEnterLoc.
	 * @param player Player
	 * @return Location
	 */
	@Override
	public Location getEnterLoc(Player player)
	{
		Zone zone = player.getZone(Zone.ZoneType.SIEGE);
		if (zone == null)
		{
			return player.getLoc();
		}
		SiegeClanObject siegeClan = getSiegeClan(DEFENDERS, player.getClan());
		if (siegeClan != null)
		{
			if (siegeClan.getFlag() != null)
			{
				return Location.findAroundPosition(siegeClan.getFlag(), 50, 75);
			}
		}
		Residence r = ResidenceHolder.getInstance().getResidence(zone.getParams().getInteger("residence"));
		if (r == null)
		{
			error(toString(), new Exception("Not find residence: " + zone.getParams().getInteger("residence")));
			return player.getLoc();
		}
		return r.getNotOwnerRestartPoint(player);
	}
	
	/**
	 * Method teleportPlayers.
	 * @param t String
	 */
	@Override
	public void teleportPlayers(String t)
	{
		List<ZoneObject> zones = getObjects(SIEGE_ZONES);
		for (ZoneObject zone : zones)
		{
			Residence r = ResidenceHolder.getInstance().getResidence(zone.getZone().getParams().getInteger("residence"));
			r.banishForeigner();
		}
	}
	
	/**
	 * Method canRessurect.
	 * @param resurrectPlayer Player
	 * @param target Creature
	 * @param force boolean
	 * @return boolean
	 */
	@Override
	public boolean canRessurect(Player resurrectPlayer, Creature target, boolean force)
	{
		boolean playerInZone = resurrectPlayer.isInZone(Zone.ZoneType.SIEGE);
		boolean targetInZone = target.isInZone(Zone.ZoneType.SIEGE);
		if (!playerInZone && !targetInZone)
		{
			return true;
		}
		if (!targetInZone)
		{
			return false;
		}
		Player targetPlayer = target.getPlayer();
		DominionSiegeEvent siegeEvent = target.getEvent(DominionSiegeEvent.class);
		if (siegeEvent == null)
		{
			if (force)
			{
				targetPlayer.sendPacket(SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE);
			}
			resurrectPlayer.sendPacket(force ? SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE : SystemMsg.INVALID_TARGET);
			return false;
		}
		SiegeClanObject targetSiegeClan = siegeEvent.getSiegeClan(DEFENDERS, targetPlayer.getClan());
		if (targetSiegeClan.getFlag() == null)
		{
			if (force)
			{
				targetPlayer.sendPacket(SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE);
			}
			resurrectPlayer.sendPacket(force ? SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE : SystemMsg.INVALID_TARGET);
			return false;
		}
		if (force)
		{
			return true;
		}
		resurrectPlayer.sendPacket(SystemMsg.INVALID_TARGET);
		return false;
	}
	
	/**
	 * Method setReward.
	 * @param objectId int
	 * @param type int
	 * @param v int
	 */
	public void setReward(int objectId, int type, int v)
	{
		int val[] = _playersRewards.get(objectId);
		if (val == null)
		{
			_playersRewards.put(objectId, val = new int[REWARD_MAX]);
		}
		val[type] = v;
	}
	
	/**
	 * Method addReward.
	 * @param player Player
	 * @param type int
	 * @param v int
	 */
	public void addReward(Player player, int type, int v)
	{
		int val[] = _playersRewards.get(player.getObjectId());
		if (val == null)
		{
			_playersRewards.put(player.getObjectId(), val = new int[REWARD_MAX]);
		}
		val[type] += v;
	}
	
	/**
	 * Method getReward.
	 * @param player Player
	 * @param type int
	 * @return int
	 */
	public int getReward(Player player, int type)
	{
		int val[] = _playersRewards.get(player.getObjectId());
		if (val == null)
		{
			return 0;
		}
		return val[type];
	}
	
	/**
	 * Method clearReward.
	 * @param objectId int
	 */
	public void clearReward(int objectId)
	{
		if (_playersRewards.containsKey(objectId))
		{
			_playersRewards.remove(objectId);
			DominionRewardDAO.getInstance().delete(getResidence(), objectId);
		}
	}
	
	/**
	 * Method getRewards.
	 * @return Collection<IntObjectMap.Entry<int[]>>
	 */
	public Collection<IntObjectMap.Entry<int[]>> getRewards()
	{
		return _playersRewards.entrySet();
	}
	
	/**
	 * Method calculateReward.
	 * @param player Player
	 * @return int[]
	 */
	public int[] calculateReward(Player player)
	{
		int rewards[] = _playersRewards.get(player.getObjectId());
		if (rewards == null)
		{
			return null;
		}
		int[] out = new int[3];
		out[0] += rewards[STATIC_BADGES];
		out[0] += rewards[ONLINE_REWARD] >= 14 ? 7 : rewards[ONLINE_REWARD] / 2;
		if (rewards[KILL_REWARD] < 50)
		{
			out[0] += rewards[KILL_REWARD] * 0.1;
		}
		else if (rewards[KILL_REWARD] < 120)
		{
			out[0] += (5 + ((rewards[KILL_REWARD] - 50) / 14));
		}
		else
		{
			out[0] += 10;
		}
		if (out[0] > 90)
		{
			out[0] = 90;
			out[1] = 0;
			out[2] = 450;
		}
		return out;
	}
	
	/**
	 * Method setForSakeQuest.
	 * @param forSakeQuest Quest
	 */
	public void setForSakeQuest(Quest forSakeQuest)
	{
		_forSakeQuest = forSakeQuest;
	}
}
