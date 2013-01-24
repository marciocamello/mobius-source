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
import java.util.Calendar;
import java.util.List;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

public class KrateisCubeRunnerEvent extends GlobalEvent
{
	private static final SchedulingPattern DATE_PATTERN = new SchedulingPattern("0,30 * * * *");
	public static final String MANAGER = "manager";
	public static final String REGISTRATION = "registration";
	private boolean _isInProgress;
	private boolean _isRegistrationOver;
	private final List<KrateisCubeEvent> _cubes = new ArrayList<>(3);
	private final Calendar _calendar = Calendar.getInstance();
	
	public KrateisCubeRunnerEvent(MultiValueSet<String> set)
	{
		super(set);
	}
	
	@Override
	public void initEvent()
	{
		super.initEvent();
		_cubes.add(EventHolder.getInstance().<KrateisCubeEvent> getEvent(EventType.PVP_EVENT, 2));
		_cubes.add(EventHolder.getInstance().<KrateisCubeEvent> getEvent(EventType.PVP_EVENT, 3));
		_cubes.add(EventHolder.getInstance().<KrateisCubeEvent> getEvent(EventType.PVP_EVENT, 4));
	}
	
	@Override
	public void startEvent()
	{
		super.startEvent();
		_isInProgress = true;
	}
	
	@Override
	public void stopEvent()
	{
		_isInProgress = false;
		super.stopEvent();
		reCalcNextTime(false);
	}
	
	@Override
	public void announce(int val)
	{
		NpcInstance npc = getNpc();
		switch (val)
		{
			case -600:
			case -300:
				Functions.npcSay(npc, NpcString.THE_MATCH_WILL_BEGIN_IN_S1_MINUTES, String.valueOf(-val / 60));
				break;
			case -540:
			case -330:
			case 60:
			case 360:
			case 660:
			case 960:
				Functions.npcSay(npc, NpcString.REGISTRATION_FOR_THE_NEXT_MATCH_WILL_END_AT_S1_MINUTES_AFTER_HOUR, String.valueOf(_calendar.get(Calendar.MINUTE) == 30 ? 57 : 27));
				break;
			case -480:
				Functions.npcSay(npc, NpcString.THERE_ARE_5_MINUTES_REMAINING_TO_REGISTER_FOR_KRATEIS_CUBE_MATCH);
				break;
			case -360:
				Functions.npcSay(npc, NpcString.THERE_ARE_3_MINUTES_REMAINING_TO_REGISTER_FOR_KRATEIS_CUBE_MATCH);
				break;
			case -240:
				Functions.npcSay(npc, NpcString.THERE_ARE_1_MINUTES_REMAINING_TO_REGISTER_FOR_KRATEIS_CUBE_MATCH);
				break;
			case -180:
			case -120:
			case -60:
				Functions.npcSay(npc, NpcString.THE_MATCH_WILL_BEGIN_SHORTLY);
				break;
			case 600:
				Functions.npcSay(npc, NpcString.THE_MATCH_WILL_BEGIN_IN_S1_MINUTES, String.valueOf(20));
				break;
		}
	}
	
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();
		_calendar.setTimeInMillis(DATE_PATTERN.next(System.currentTimeMillis()));
		registerActions();
	}
	
	public NpcInstance getNpc()
	{
		SpawnExObject obj = getFirstObject(MANAGER);
		return obj.getFirstSpawned();
	}
	
	@Override
	public boolean isInProgress()
	{
		return _isInProgress;
	}
	
	public boolean isRegistrationOver()
	{
		return _isRegistrationOver;
	}
	
	@Override
	protected long startTimeMillis()
	{
		return _calendar.getTimeInMillis();
	}
	
	@Override
	protected void printInfo()
	{
	}
	
	@Override
	public void action(String name, boolean start)
	{
		if (name.equalsIgnoreCase(REGISTRATION))
		{
			_isRegistrationOver = !start;
		}
		else
		{
			super.action(name, start);
		}
	}
	
	public List<KrateisCubeEvent> getCubes()
	{
		return _cubes;
	}
	
	public boolean isRegistered(Player player)
	{
		for (KrateisCubeEvent cubeEvent : _cubes)
		{
			if ((cubeEvent.getRegisteredPlayer(player) != null) && (cubeEvent.getParticlePlayer(player) != null))
			{
				return true;
			}
		}
		return false;
	}
}
