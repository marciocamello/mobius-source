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
package events.TeamVsTeamArena;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.enums.TeamType;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Revive;
import lineage2.gameserver.network.serverpackets.SkillList;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class TeamVsTeamTemplate extends Functions
{
	private static final int ITEM_ID = 4357;
	private static final String ITEM_NAME = "Silver Shilen";
	private static final int LENGTH_TEAM = 12;
	private static final boolean ALLOW_BUFFS = false;
	private static final boolean ALLOW_CLAN_SKILL = true;
	private static final boolean ALLOW_HERO_SKILL = false;
	protected int _managerId;
	protected String _className;
	private Long _creatorId;
	private NpcInstance _manager;
	protected int _status = 0;
	protected final int _CharacterFound = 0;
	private int _price = 10000;
	private int _team1count = 1;
	private int _team2count = 1;
	private int _team1min = 1;
	private int _team1max = 85;
	private int _team2min = 1;
	private int _team2max = 85;
	private int _timeToStart = 10;
	private boolean _timeOutTask;
	protected List<Location> _team1points;
	protected List<Location> _team2points;
	protected List<Long> _team1list;
	protected List<Long> _team2list;
	protected List<Long> _team1live;
	protected List<Long> _team2live;
	protected Zone _zone;
	protected ZoneListener _zoneListener;
	
	/**
	 * Method onLoad.
	 */
	protected abstract void onLoad();
	
	/**
	 * Method onReload.
	 */
	protected abstract void onReload();
	
	/**
	 * Method template_stop.
	 */
	void template_stop()
	{
		if (_status <= 0)
		{
			return;
		}
		
		sayToAll("Fight interrupted for technical reasons, the rates returned");
		unParalyzeTeams();
		ressurectPlayers();
		returnItemToTeams();
		healPlayers();
		removeBuff();
		teleportPlayersToSavedCoords();
		clearTeams();
		_status = 0;
		_timeOutTask = false;
	}
	
	/**
	 * Method template_create1.
	 * @param player Player
	 */
	void template_create1(Player player)
	{
		if (_status > 0)
		{
			show("Wait until the end of battle", player);
			return;
		}
		
		if (player.getTeam() != TeamType.NONE)
		{
			show("You are already registered", player);
			return;
		}
		
		show("scripts/events/TvTArena/" + _managerId + "-1.htm", player);
	}
	
	/**
	 * Method template_register.
	 * @param player Player
	 */
	void template_register(Player player)
	{
		if (_status == 0)
		{
			show("Fight at the moment is not created", player);
			return;
		}
		
		if (_status > 1)
		{
			show("Wait until the end of battle", player);
			return;
		}
		
		if (player.getTeam() != TeamType.NONE)
		{
			show("You are already registered", player);
			return;
		}
		
		show("scripts/events/TvTArena/" + _managerId + "-3.htm", player);
	}
	
	/**
	 * Method template_check1.
	 * @param player Player
	 * @param manager NpcInstance
	 * @param var String[]
	 */
	void template_check1(Player player, NpcInstance manager, String[] var)
	{
		if (var.length != 8)
		{
			show("invalid data", player);
			return;
		}
		
		if (_status > 0)
		{
			show("Wait until the end of battle", player);
			return;
		}
		
		if ((manager == null) || !manager.isNpc())
		{
			show("Hacker? :) " + manager, player);
			return;
		}
		
		_manager = manager;
		
		try
		{
			_price = Integer.valueOf(var[0]);
			_team1count = Integer.valueOf(var[1]);
			_team2count = Integer.valueOf(var[2]);
			_team1min = Integer.valueOf(var[3]);
			_team1max = Integer.valueOf(var[4]);
			_team2min = Integer.valueOf(var[5]);
			_team2max = Integer.valueOf(var[6]);
			_timeToStart = Integer.valueOf(var[7]);
		}
		catch (Exception e)
		{
			show("invalid data", player);
			return;
		}
		
		if ((_price < 1) || (_price > 500))
		{
			show("incorrect rate", player);
			return;
		}
		
		if ((_team1count < 1) || (_team1count > LENGTH_TEAM) || (_team2count < 1) || (_team2count > LENGTH_TEAM))
		{
			show("Wrong size of the team", player);
			return;
		}
		
		if ((_team1min < 1) || (_team1min > 86) || (_team2min < 1) || (_team2min > 86) || (_team1max < 1) || (_team1max > 86) || (_team2max < 1) || (_team2max > 86) || (_team1min > _team1max) || (_team2min > _team2max))
		{
			show("Wrong level", player);
			return;
		}
		
		if ((player.getLevel() < _team1min) || (player.getLevel() > _team1max))
		{
			show("Wrong level", player);
			return;
		}
		
		if ((_timeToStart < 1) || (_timeToStart > 10))
		{
			show("wrong time", player);
			return;
		}
		
		if (getItemCount(player, ITEM_ID) < _price)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INCORRECT_ITEM_COUNT));
			return;
		}
		
		_creatorId = player.getStoredId();
		removeItem(player, ITEM_ID, _price);
		player.setTeam(TeamType.BLUE);
		_status = 1;
		_team1list.clear();
		_team2list.clear();
		_team1live.clear();
		_team2live.clear();
		_team1list.add(player.getStoredId());
		sayToAll(player.getName() + " created fight " + _team1count + "х" + _team2count + ", " + _team1min + "-" + _team1max + "lv vs " + _team2min + "-" + _team2max + "lv, rate " + _price + " " + ITEM_NAME + ", start over " + _timeToStart + " Min");
		executeTask("events.TvTArena." + _className, "announce", new Object[0], 60000);
	}
	
	/**
	 * Method template_register_check.
	 * @param player Player
	 */
	void template_register_check(Player player)
	{
		if (_status == 0)
		{
			show("Fight at the moment is not created", player);
			return;
		}
		
		if (_status > 1)
		{
			show("Wait until the end of battle", player);
			return;
		}
		
		if (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId()))
		{
			show("You are already registered", player);
			return;
		}
		
		if (player.getTeam() != TeamType.NONE)
		{
			show("You are already registered", player);
			return;
		}
		
		if (getItemCount(player, ITEM_ID) < _price)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INCORRECT_ITEM_COUNT));
			return;
		}
		
		int size1 = _team1list.size(), size2 = _team2list.size();
		
		if (size1 > size2)
		{
			String t = null;
			
			if (tryRegister(2, player) != null)
			{
				t = tryRegister(1, player);
				
				if (t != null)
				{
					show(t, player);
				}
			}
		}
		else if (size1 < size2)
		{
			String t = null;
			
			if (tryRegister(1, player) != null)
			{
				t = tryRegister(2, player);
				
				if (t != null)
				{
					show(t, player);
				}
			}
		}
		else
		{
			int team = Rnd.get(1, 2);
			String t = null;
			
			if (tryRegister(team, player) != null)
			{
				t = tryRegister(team == 1 ? 2 : 1, player);
				
				if (t != null)
				{
					show(t, player);
				}
			}
		}
	}
	
	/**
	 * Method tryRegister.
	 * @param team int
	 * @param player Player
	 * @return String
	 */
	private String tryRegister(int team, Player player)
	{
		if (team == 1)
		{
			if ((player.getLevel() < _team1min) || (player.getLevel() > _team1max))
			{
				return "You do not approach the level of";
			}
			
			if (_team1list.size() >= _team1count)
			{
				return "Team 1 full";
			}
			
			doRegister(1, player);
			return null;
		}
		
		if ((player.getLevel() < _team2min) || (player.getLevel() > _team2max))
		{
			return "You do not approach the level of";
		}
		
		if (_team2list.size() >= _team2count)
		{
			return "Team 2 full";
		}
		
		doRegister(2, player);
		return null;
	}
	
	/**
	 * Method doRegister.
	 * @param team int
	 * @param player Player
	 */
	private void doRegister(int team, Player player)
	{
		removeItem(player, ITEM_ID, _price);
		
		if (team == 1)
		{
			_team1list.add(player.getStoredId());
			player.setTeam(TeamType.BLUE);
			sayToAll(player.getName() + " registered for team 1");
		}
		else
		{
			_team2list.add(player.getStoredId());
			player.setTeam(TeamType.RED);
			sayToAll(player.getName() + " registered for team 2");
		}
		
		if ((_team1list.size() >= _team1count) && (_team2list.size() >= _team2count))
		{
			sayToAll("Teams are ready, start after 1 minute.");
			_timeToStart = 1;
		}
	}
	
	/**
	 * Method template_announce.
	 */
	void template_announce()
	{
		Player creator = GameObjectsStorage.getAsPlayer(_creatorId);
		
		if ((_status != 1) || (creator == null))
		{
			return;
		}
		
		if (_timeToStart > 1)
		{
			_timeToStart--;
			sayToAll(creator.getName() + " created fight " + _team1count + "х" + _team2count + ", " + _team1min + "-" + _team1max + "lv vs " + _team2min + "-" + _team2max + "lv, rate " + _price + " " + ITEM_NAME + ", start over " + _timeToStart + " Min");
			executeTask("events.TvTArena." + _className, "announce", new Object[0], 60000);
		}
		else if (_team2list.size() > 0)
		{
			sayToAll("Prepare for battle");
			executeTask("events.TvTArena." + _className, "prepare", new Object[0], 5000);
		}
		else
		{
			sayToAll("Fight did not take place, no opponents");
			_status = 0;
			returnItemToTeams();
			clearTeams();
		}
	}
	
	/**
	 * Method template_prepare.
	 */
	void template_prepare()
	{
		if (_status != 1)
		{
			return;
		}
		
		_status = 2;
		
		for (Player player : getPlayers(_team1list))
		{
			if (!player.isDead())
			{
				_team1live.add(player.getStoredId());
			}
		}
		
		for (Player player : getPlayers(_team2list))
		{
			if (!player.isDead())
			{
				_team2live.add(player.getStoredId());
			}
		}
		
		if (!checkTeams())
		{
			return;
		}
		
		saveBackCoords();
		clearArena();
		ressurectPlayers();
		removeBuff();
		healPlayers();
		paralyzeTeams();
		teleportTeamsToArena();
		sayToAll("Fight will start in 30 seconds");
		executeTask("events.TvTArena." + _className, "start", new Object[0], 30000);
	}
	
	/**
	 * Method template_start.
	 */
	void template_start()
	{
		if (_status != 2)
		{
			return;
		}
		
		if (!checkTeams())
		{
			return;
		}
		
		sayToAll("Go!!!");
		unParalyzeTeams();
		_status = 3;
		executeTask("events.TvTArena." + _className, "timeOut", new Object[0], 180000);
		_timeOutTask = true;
	}
	
	/**
	 * Method clearArena.
	 */
	private void clearArena()
	{
		for (GameObject obj : _zone.getObjects())
		{
			if ((obj != null) && obj.isPlayable())
			{
				((Playable) obj).teleToLocation(_zone.getSpawn());
			}
		}
	}
	
	/**
	 * Method checkTeams.
	 * @return boolean
	 */
	private boolean checkTeams()
	{
		if (_team1live.isEmpty())
		{
			teamHasLost(1);
			return false;
		}
		else if (_team2live.isEmpty())
		{
			teamHasLost(2);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method saveBackCoords.
	 */
	private void saveBackCoords()
	{
		for (Player player : getPlayers(_team1list))
		{
			player.setVar("TvTArena_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflectionId(), -1);
		}
		
		for (Player player : getPlayers(_team2list))
		{
			player.setVar("TvTArena_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflectionId(), -1);
		}
	}
	
	/**
	 * Method teleportPlayersToSavedCoords.
	 */
	private void teleportPlayersToSavedCoords()
	{
		for (Player player : getPlayers(_team1list))
		{
			try
			{
				String var = player.getVar("TvTArena_backCoords");
				
				if ((var == null) || var.equals(""))
				{
					continue;
				}
				
				String[] coords = var.split(" ");
				
				if (coords.length != 4)
				{
					continue;
				}
				
				player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
				player.unsetVar("TvTArena_backCoords");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		for (Player player : getPlayers(_team2list))
		{
			try
			{
				String var = player.getVar("TvTArena_backCoords");
				
				if ((var == null) || var.equals(""))
				{
					continue;
				}
				
				String[] coords = var.split(" ");
				
				if (coords.length != 4)
				{
					continue;
				}
				
				player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
				player.unsetVar("TvTArena_backCoords");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method healPlayers.
	 */
	private void healPlayers()
	{
		for (Player player : getPlayers(_team1list))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
		
		for (Player player : getPlayers(_team2list))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
	}
	
	/**
	 * Method ressurectPlayers.
	 */
	private void ressurectPlayers()
	{
		for (Player player : getPlayers(_team1list))
		{
			if (player.isDead())
			{
				player.restoreExp();
				player.setCurrentHp(player.getMaxHp(), true);
				player.setCurrentMp(player.getMaxMp());
				player.setCurrentCp(player.getMaxCp());
				player.broadcastPacket(new Revive(player));
			}
		}
		
		for (Player player : getPlayers(_team2list))
		{
			if (player.isDead())
			{
				player.restoreExp();
				player.setCurrentHp(player.getMaxHp(), true);
				player.setCurrentMp(player.getMaxMp());
				player.setCurrentCp(player.getMaxCp());
				player.broadcastPacket(new Revive(player));
			}
		}
	}
	
	/**
	 * Method removeBuff.
	 */
	private void removeBuff()
	{
		for (Player player : getPlayers(_team1list))
		{
			if (player != null)
			{
				try
				{
					if (player.isCastingNow())
					{
						player.abortCast(true, true);
					}
					
					if (!ALLOW_CLAN_SKILL)
					{
						if (player.getClan() != null)
						{
							for (Skill skill : player.getClan().getAllSkills())
							{
								player.removeSkill(skill, false);
							}
						}
					}
					
					if (!ALLOW_HERO_SKILL)
					{
						if (player.isHero())
						{
							Hero.removeSkills(player);
						}
					}
					
					if (!ALLOW_BUFFS)
					{
						player.getEffectList().stopAllEffects();
						
						for (Summon summon : player.getSummonList())
						{
							summon.getEffectList().stopAllEffects();
							
							if (summon.isPet())
							{
								summon.unSummon();
							}
						}
						
						if (player.getAgathionId() > 0)
						{
							player.setAgathion(0);
						}
					}
					
					player.sendSkillList();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		for (Player player : getPlayers(_team2list))
		{
			if (player != null)
			{
				try
				{
					if (player.isCastingNow())
					{
						player.abortCast(true, true);
					}
					
					if (!ALLOW_CLAN_SKILL)
					{
						if (player.getClan() != null)
						{
							for (Skill skill : player.getClan().getAllSkills())
							{
								player.removeSkill(skill, false);
							}
						}
					}
					
					if (!ALLOW_HERO_SKILL)
					{
						if (player.isHero())
						{
							Hero.removeSkills(player);
						}
					}
					
					if (!ALLOW_BUFFS)
					{
						player.getEffectList().stopAllEffects();
						
						for (Summon summon : player.getSummonList())
						{
							summon.getEffectList().stopAllEffects();
							
							if (summon.isPet())
							{
								summon.unSummon();
							}
						}
						
						if (player.getAgathionId() > 0)
						{
							player.setAgathion(0);
						}
					}
					
					player.sendPacket(new SkillList(player));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Method backBuff.
	 */
	private void backBuff()
	{
		for (Player player : getPlayers(_team1list))
		{
			if (player == null)
			{
				continue;
			}
			
			try
			{
				player.getEffectList().stopAllEffects();
				
				if (!ALLOW_CLAN_SKILL)
				{
					if (player.getClan() != null)
					{
						for (Skill skill : player.getClan().getAllSkills())
						{
							if (skill.getMinPledgeClass() <= player.getPledgeClass())
							{
								player.addSkill(skill, false);
							}
						}
					}
				}
				
				if (!ALLOW_HERO_SKILL)
				{
					if (player.isHero())
					{
						Hero.addSkills(player);
					}
				}
				
				player.sendSkillList();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		for (Player player : getPlayers(_team2list))
		{
			if (player == null)
			{
				continue;
			}
			
			try
			{
				player.getEffectList().stopAllEffects();
				
				if (!ALLOW_CLAN_SKILL)
				{
					if (player.getClan() != null)
					{
						for (Skill skill : player.getClan().getAllSkills())
						{
							if (skill.getMinPledgeClass() <= player.getPledgeClass())
							{
								player.addSkill(skill, false);
							}
						}
					}
				}
				
				if (!ALLOW_HERO_SKILL)
				{
					if (player.isHero())
					{
						Hero.addSkills(player);
					}
				}
				
				player.sendPacket(new SkillList(player));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method paralyzeTeams.
	 */
	private void paralyzeTeams()
	{
		Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
		
		for (Player player : getPlayers(_team1list))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
			
			for (Summon summon : player.getSummonList())
			{
				revengeSkill.getEffects(player, summon, false, false);
			}
		}
		
		for (Player player : getPlayers(_team2list))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
			
			for (Summon summon : player.getSummonList())
			{
				revengeSkill.getEffects(player, summon, false, false);
			}
		}
	}
	
	/**
	 * Method unParalyzeTeams.
	 */
	private void unParalyzeTeams()
	{
		for (Player player : getPlayers(_team1list))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			
			for (Summon summon : player.getSummonList())
			{
				summon.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			}
			
			player.leaveParty();
		}
		
		for (Player player : getPlayers(_team2list))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			
			for (Summon summon : player.getSummonList())
			{
				summon.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			}
			
			player.leaveParty();
		}
	}
	
	/**
	 * Method teleportTeamsToArena.
	 */
	private void teleportTeamsToArena()
	{
		Integer n = 0;
		
		for (Player player : getPlayers(_team1live))
		{
			unRide(player);
			unSummonPet(player, true);
			player.teleToLocation(_team1points.get(n), ReflectionManager.DEFAULT);
			n++;
		}
		
		n = 0;
		
		for (Player player : getPlayers(_team2live))
		{
			unRide(player);
			unSummonPet(player, true);
			player.teleToLocation(_team2points.get(n), ReflectionManager.DEFAULT);
			n++;
		}
	}
	
	/**
	 * Method playerHasLost.
	 * @param player Player
	 * @return boolean
	 */
	private boolean playerHasLost(Player player)
	{
		if (player.getTeam() == TeamType.BLUE)
		{
			_team1live.remove(player.getStoredId());
		}
		else
		{
			_team2live.remove(player.getStoredId());
		}
		
		Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
		revengeSkill.getEffects(player, player, false, false);
		return !checkTeams();
	}
	
	/**
	 * Method teamHasLost.
	 * @param team_id Integer
	 */
	private void teamHasLost(Integer team_id)
	{
		if (team_id == 1)
		{
			sayToAll("Team 2 Wins");
			payItemToTeam(2);
		}
		else
		{
			sayToAll("Team 1 Wins");
			payItemToTeam(1);
		}
		
		unParalyzeTeams();
		backBuff();
		teleportPlayersToSavedCoords();
		ressurectPlayers();
		healPlayers();
		clearTeams();
		_status = 0;
		_timeOutTask = false;
	}
	
	/**
	 * Method template_timeOut.
	 */
	void template_timeOut()
	{
		if (_timeOutTask && (_status == 3))
		{
			sayToAll("Time out, draw!");
			returnItemToTeams();
			unParalyzeTeams();
			backBuff();
			teleportPlayersToSavedCoords();
			ressurectPlayers();
			healPlayers();
			clearTeams();
			_status = 0;
			_timeOutTask = false;
		}
	}
	
	/**
	 * Method payItemToTeam.
	 * @param team_id Integer
	 */
	private void payItemToTeam(Integer team_id)
	{
		if (team_id == 1)
		{
			for (Player player : getPlayers(_team1list))
			{
				addItem(player, ITEM_ID, _price + ((_team2list.size() * _price) / _team1list.size()));
			}
		}
		else
		{
			for (Player player : getPlayers(_team2list))
			{
				addItem(player, ITEM_ID, _price + ((_team2list.size() * _price) / _team1list.size()));
			}
		}
	}
	
	/**
	 * Method returnItemToTeams.
	 */
	private void returnItemToTeams()
	{
		for (Player player : getPlayers(_team1list))
		{
			addItem(player, ITEM_ID, _price);
		}
		
		for (Player player : getPlayers(_team2list))
		{
			addItem(player, ITEM_ID, _price);
		}
	}
	
	/**
	 * Method clearTeams.
	 */
	private void clearTeams()
	{
		for (Player player : getPlayers(_team1list))
		{
			player.setTeam(TeamType.NONE);
		}
		
		for (Player player : getPlayers(_team2list))
		{
			player.setTeam(TeamType.NONE);
		}
		
		_team1list.clear();
		_team2list.clear();
		_team1live.clear();
		_team2live.clear();
	}
	
	/**
	 * Method onDeath.
	 * @param self Creature
	 * @param killer Creature
	 */
	public void onDeath(Creature self, Creature killer)
	{
		if ((_status >= 2) && self.isPlayer() && (self.getTeam() != TeamType.NONE) && (_team1list.contains(self.getStoredId()) || _team2list.contains(self.getStoredId())))
		{
			Player player = self.getPlayer();
			Player kplayer = killer.getPlayer();
			
			if (kplayer != null)
			{
				sayToAll(kplayer.getName() + " killed " + player.getName());
				
				if ((player.getTeam() == kplayer.getTeam()) || (!_team1list.contains(kplayer.getStoredId()) && !_team2list.contains(kplayer.getStoredId())))
				{
					sayToAll("Violation of the rules, a player " + kplayer.getName() + " fined " + _price + " " + ITEM_NAME);
					removeItem(kplayer, ITEM_ID, _price);
				}
				
				playerHasLost(player);
			}
			else
			{
				sayToAll(player.getName() + " killed");
				playerHasLost(player);
			}
		}
	}
	
	/**
	 * Method onPlayerExit.
	 * @param player Player
	 */
	public void onPlayerExit(Player player)
	{
		if ((player != null) && (_status > 0) && (player.getTeam() != TeamType.NONE) && (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())))
		{
			switch (_status)
			{
				case 1:
					removePlayer(player);
					sayToAll(player.getName() + " disqualified");
					
					if (player.getStoredId() == _creatorId)
					{
						sayToAll("fight interrupted, rates returned");
						returnItemToTeams();
						backBuff();
						teleportPlayersToSavedCoords();
						unParalyzeTeams();
						ressurectPlayers();
						healPlayers();
						clearTeams();
						unParalyzeTeams();
						clearTeams();
						_status = 0;
						_timeOutTask = false;
					}
					break;
				
				case 2:
					removePlayer(player);
					sayToAll(player.getName() + " disqualified");
					checkTeams();
					break;
				
				case 3:
					removePlayer(player);
					sayToAll(player.getName() + " disqualified");
					checkTeams();
					break;
			}
		}
	}
	
	/**
	 * Method onTeleport.
	 * @param player Player
	 */
	void onTeleport(Player player)
	{
		if ((player != null) && (_status > 1) && (player.getTeam() != TeamType.NONE) && player.isInZone(_zone))
		{
			onPlayerExit(player);
		}
	}
	
	final class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			
			if ((_status >= 2) && (player != null) && !(_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())))
			{
				ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, _zone.getSpawn()), 3000);
			}
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			
			if ((_status >= 2) && (player != null) && (player.getTeam() != TeamType.NONE) && (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())))
			{
				double angle = PositionUtils.convertHeadingToDegree(cha.getHeading());
				double radian = Math.toRadians(angle - 90);
				int x = (int) (cha.getX() + (50 * Math.sin(radian)));
				int y = (int) (cha.getY() - (50 * Math.cos(radian)));
				int z = cha.getZ();
				ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, new Location(x, y, z)), 3000);
			}
		}
	}
	
	private final class TeleportTask extends RunnableImpl
	{
		private final Location loc;
		private final Creature target;
		
		/**
		 * Constructor for TeleportTask.
		 * @param target Creature
		 * @param loc Location
		 */
		TeleportTask(Creature target, Location loc)
		{
			this.target = target;
			this.loc = loc;
			target.block();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			target.unblock();
			target.teleToLocation(loc);
		}
	}
	
	/**
	 * Method removePlayer.
	 * @param player Player
	 */
	private void removePlayer(Player player)
	{
		if (player != null)
		{
			_team1list.remove(player.getStoredId());
			_team2list.remove(player.getStoredId());
			_team1live.remove(player.getStoredId());
			_team2live.remove(player.getStoredId());
			player.setTeam(TeamType.NONE);
		}
	}
	
	/**
	 * Method getPlayers.
	 * @param list List<Long>
	 * @return List<Player>
	 */
	private List<Player> getPlayers(List<Long> list)
	{
		List<Player> result = new ArrayList<>();
		
		for (Long storeId : list)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			
			if (player != null)
			{
				result.add(player);
			}
		}
		
		return result;
	}
	
	/**
	 * Method sayToAll.
	 * @param text String
	 */
	private void sayToAll(String text)
	{
		Announcements.getInstance().announceToAll(_manager.getName() + ": " + text, ChatType.CRITICAL_ANNOUNCE);
	}
}
