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
 * @author Iqman
 */
public class Q10386_MysteriousJourney extends Quest implements ScriptFile
{
	// Npcs
	private static final int TOPOI = 30499;
	private static final int HESET = 33780;
	private static final int BERNA = 33796;
	
	public Q10386_MysteriousJourney()
	{
		super(false);
		addStartNpc(TOPOI);
		addTalkId(TOPOI, HESET, BERNA);
		addLevelCheck(93, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "accepted.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "acceptedHeset.htm":
				qs.setCond(3);
				break;
			
			case "acceptedBerma.htm":
				qs.setCond(4);
				break;
			
			case "endquest.htm":
				qs.getPlayer().addExpAndSp(27244350, 2724435);
				qs.giveItems(57, 58707);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case TOPOI:
				if (cond == 0)
				{
					htmltext = "start.htm";
				}
				break;
			
			case HESET:
				if (cond == 1)
				{
					htmltext = "hesetCond1.htm";
				}
				if (cond == 4)
				{
					htmltext = "collected.htm";
				}
				break;
			
			case BERNA:
				if (cond == 3)
				{
					htmltext = "berna.htm";
				}
				break;
		}
		
		return htmltext;
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