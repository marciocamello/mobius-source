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
package events.HuntForSanta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Hunt for Santa Begins!<br>
 * Info - http://www.lineage2.com/en/news/events/hunt-for-santa.php
 * @author Mobius
 */
public final class HuntForSanta extends Functions implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(HuntForSanta.class);
	private static final String EVENT_NAME = "HuntForSanta";
	private static final int BUFF_REUSE_HOURS = 2;
	private static final int NOELLE = 34008;
	private static final int TREE = 34009;
	private static final int BUFF_STOCKING = 16419;
	private static final int BUFF_TREE = 16420;
	private static final int BUFF_SNOWMAN = 16421;
	private static final List<SimpleSpawner> EVENT_SPAWNS = new ArrayList<>();
	// @formatter:off
	private static final int[][] NOELLE_SPAWN_COORDS =
	{
		{-78920, 248056, -3296, 0}, // Faeron
		{-115016, 256248, -1520, 0}, // Talking Island
		{208328, 87656, -1024, 24575}, // Arcan
		{80968, 148168, -3472, 0}, // Giran
		{146840, 26904, -2208, 11547}, // Aden
		{147464, -56984, -2784, 11547}, // Goddard
		{44584, -48216, -792, 27931}, // Rune
		{82696, 53944, -1488, 40959}, // Oren
		{87592, -141720, -1344, 44315}, // Schuttgart
		{112072, 219960, -3664, 24575}, // Heine
		{16552, 142948, -2720, 24575}, // Dion
		{-14088, 122680, -3120, 32767}, // Gludio
		{-80536, 149960, -3040, 29412} // Gludin
	};
	private static final int[][] TREE_SPAWN_COORDS =
	{
		{-79016, 247912, -3296, 0}, // Faeron
		{-115176, 256280, -1520, 0}, // Talking Island
		{208376, 87480, -1024, 0}, // Arcan
		{80840, 148296, -3472, 0}, // Giran
		{146968, 26728, -2208, 0}, // Aden
		{147432, -57176, -2784, 0}, // Goddard
		{44808, -48168, -800, 0}, // Rune
		{82856, 54088, -1488, 0}, // Oren
		{87608, -141512, -1344, 0}, // Schuttgart
		{112200, 219832, -3664, 0}, // Heine
		{16632, 142824, -2704, 0}, // Dion
		{-13944, 122680, -3120, 0}, // Gludio
		{-80392, 149896, -3040, 0} // Gludin
	};
	// @formatter:on
	
	public void receiveBuffStocking()
	{
		receiveBuff(BUFF_STOCKING);
	}
	
	public void receiveBuffTree()
	{
		receiveBuff(BUFF_TREE);
	}
	
	public void receiveBuffSnowman()
	{
		receiveBuff(BUFF_SNOWMAN);
	}
	
	private void receiveBuff(int buffId)
	{
		if ((getSelf() == null) || (getNpc() == null) || (getSelf().getPlayer() == null))
		{
			return;
		}
		
		String htmltext = null;
		final Player player = getSelf().getPlayer();
		final NpcInstance npc = getNpc();
		final String var = player.getVar(EVENT_NAME);
		
		if (var != null)
		{
			htmltext = "hunt_for_santa_no.htm";
		}
		else
		{
			buff(npc, player, buffId);
			player.setVar(EVENT_NAME, "ExpirationTime", System.currentTimeMillis() + (BUFF_REUSE_HOURS * 60 * 59 * 1000L));
			htmltext = "hunt_for_santa_successfull.htm";
		}
		
		show("scripts/events/HuntForSanta/" + htmltext, player);
	}
	
	public void receiveBuffAll()
	{
		if ((getSelf() == null) || (getNpc() == null) || (getSelf().getPlayer() == null))
		{
			return;
		}
		
		String htmltext = null;
		final Player player = getSelf().getPlayer();
		final Party party = player.getParty();
		final NpcInstance npc = getNpc();
		final String var = player.getVar(EVENT_NAME);
		List<Race> partyRaces = new ArrayList<>();
		
		if (party != null)
		{
			for (Player member : party.getPartyMembers())
			{
				if (!partyRaces.contains(member.getRace()))
				{
					partyRaces.add(member.getRace());
				}
			}
		}
		
		if (var != null)
		{
			htmltext = "hunt_for_santa_all_no.htm";
		}
		else if ((party != null) && ((partyRaces.size() >= 3) || (party.getMemberCount() >= 7)))
		{
			buff(npc, player, BUFF_STOCKING);
			buff(npc, player, BUFF_TREE);
			buff(npc, player, BUFF_SNOWMAN);
			player.setVar(EVENT_NAME, "ExpirationTime", System.currentTimeMillis() + (BUFF_REUSE_HOURS * 60 * 59 * 1000L));
			htmltext = "hunt_for_santa_successfull.htm";
		}
		else
		{
			htmltext = "hunt_for_santa_all_no.htm";
		}
		
		show("scripts/events/HuntForSanta/" + htmltext, player);
	}
	
	private void buff(NpcInstance npc, Player player, int skillId)
	{
		npc.broadcastPacket(new MagicSkillUse(npc, player, skillId, 1, 0, 0));
		player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(skillId, 1));
	}
	
	public void changeBuff()
	{
		if ((getSelf() == null) || (getNpc() == null) || (getSelf().getPlayer() == null))
		{
			return;
		}
		
		final Player player = getSelf().getPlayer();
		
		player.getEffectList().stopEffect(BUFF_STOCKING);
		player.getEffectList().removeEffect(BUFF_STOCKING);
		player.getEffectList().stopEffect(BUFF_TREE);
		player.getEffectList().removeEffect(BUFF_TREE);
		player.getEffectList().stopEffect(BUFF_SNOWMAN);
		player.getEffectList().removeEffect(BUFF_SNOWMAN);
		player.unsetVar(EVENT_NAME);
		
		show("default/34008-1.htm", player);
	}
	
	@Override
	public void onLoad()
	{
		int yearStart = Calendar.getInstance().get(Calendar.YEAR);
		int yearEnd = yearStart;
		if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JANUARY)
		{
			yearStart = yearEnd - 1;
		}
		else
		{
			yearEnd = yearStart + 1;
		}
		
		final GregorianCalendar CALENDAR = new GregorianCalendar();
		GregorianCalendar START_EVENT_CALENDAR = new GregorianCalendar(yearStart, 11, 10); // December 10th
		GregorianCalendar END_EVENT_CALENDAR = new GregorianCalendar(yearEnd, 0, 7); // January 7th
		
		if (CALENDAR.after(START_EVENT_CALENDAR) && CALENDAR.before(END_EVENT_CALENDAR))
		{
			// SetActive(EVENT_NAME, true);
			SpawnNPCs(NOELLE, NOELLE_SPAWN_COORDS, EVENT_SPAWNS);
			SpawnNPCs(TREE, TREE_SPAWN_COORDS, EVENT_SPAWNS);
			_log.info("Loaded Event: The Hunt for Santa Begins!");
		}
		else
		{
			_log.info("Loaded Event: The Hunt for Santa [state: deactivated]");
		}
	}
	
	@Override
	public void onReload()
	{
		deSpawnNPCs(EVENT_SPAWNS);
	}
	
	@Override
	public void onShutdown()
	{
		deSpawnNPCs(EVENT_SPAWNS);
	}
}
