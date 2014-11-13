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
package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author KilRoy
 * @name 468 - Be Lost in the Mysterious Scent
 * @category Daily quest. Party
 */
public class Q00468_BeLostInTheMysteriousScent extends Quest implements ScriptFile
{
	private static final int SELINA = 33032;
	private static final int GARDEN_COMMANDER = 22962;
	private static final int MOON_GARDEN_MANAGER = 22958;
	private static final int MOON_GARDEN = 22960;
	private static final int GARDEN_PROTECTOR = 22959;
	private static final int CERTIFICATE_OF_LIFE = 30385;
	private static final String GARDEN_COMMANDER_KILL = "commander";
	private static final String MOON_GARDEN_MANAGER_KILL = "manager";
	private static final String MOON_GARDEN_KILL = "moon";
	private static final String GARDEN_PROTECTOR_KILL = "protector";
	
	public Q00468_BeLostInTheMysteriousScent()
	{
		super(2);
		addTalkId(SELINA);
		addKillNpcWithLog(1, GARDEN_COMMANDER_KILL, 10, GARDEN_COMMANDER);
		addKillNpcWithLog(1, MOON_GARDEN_MANAGER_KILL, 10, MOON_GARDEN_MANAGER);
		addKillNpcWithLog(1, MOON_GARDEN_KILL, 10, MOON_GARDEN);
		addKillNpcWithLog(1, GARDEN_PROTECTOR_KILL, 10, GARDEN_PROTECTOR);
		addLevelCheck(90, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accpted":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "33032-04.htm";
				break;
			
			case "quest_done":
				qs.giveItems(CERTIFICATE_OF_LIFE, 2);
				qs.exitCurrentQuest(this);
				qs.playSound(SOUND_FINISH);
				htmltext = "33032-07.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (qs.isCreated() && !qs.isNowAvailableByTime())
		{
			htmltext = "33032-08.htm";
		}
		else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
		{
			htmltext = "33032-01.htm";
		}
		else if (cond == 1)
		{
			htmltext = "33032-05.htm";
		}
		else if (cond == 2)
		{
			htmltext = "33032-06.htm";
		}
		else
		{
			htmltext = "33032-02.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (updateKill(npc, qs))
		{
			qs.unset(GARDEN_COMMANDER_KILL);
			qs.unset(MOON_GARDEN_MANAGER_KILL);
			qs.unset(MOON_GARDEN_KILL);
			qs.unset(GARDEN_PROTECTOR_KILL);
			qs.playSound(SOUND_MIDDLE);
			qs.setCond(2);
		}
		
		return null;
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}