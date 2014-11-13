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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00192_SevenSignsSeriesOfDoubt extends Quest implements ScriptFile
{
	// Npcs
	private static final int CROOP = 30676;
	private static final int HECTOR = 30197;
	private static final int STAN = 30200;
	private static final int CORPSE = 32568;
	private static final int HOLLINT = 30191;
	// Items
	private static final int CROOP_INTRO = 13813;
	private static final int JACOB_NECK = 13814;
	private static final int CROOP_LETTER = 13815;
	
	public Q00192_SevenSignsSeriesOfDoubt()
	{
		super(false);
		addStartNpc(CROOP);
		addTalkId(HECTOR, STAN, CORPSE, HOLLINT);
		addQuestItem(CROOP_INTRO, JACOB_NECK, CROOP_LETTER);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		String htmltext = event;
		
		switch (event)
		{
			case "30676-03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "8":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ_SUSPICIOUS_DEATH);
				return "";
				
			case "30197-03.htm":
				qs.setCond(4);
				qs.takeItems(CROOP_INTRO, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30200-04.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32568-02.htm":
				qs.setCond(6);
				qs.giveItems(JACOB_NECK, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30676-12.htm":
				qs.setCond(7);
				qs.takeItems(JACOB_NECK, 1);
				qs.giveItems(CROOP_LETTER, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30191-03.htm":
				if (player.getLevel() < 79)
				{
					htmltext = "<html><body>Only characters who are <font color=\"LEVEL\">level 79</font> or higher may complete this quest.</body></html>";
				}
				else if (player.getBaseClassId() == player.getActiveClassId())
				{
					qs.addExpAndSp(10000000, 2500000);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				else
				{
					return "subclass_forbidden.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		final int id = qs.getState();
		final Player player = qs.getPlayer();
		
		switch (npcId)
		{
			case CROOP:
				if ((id == CREATED) && (player.getLevel() >= 79))
				{
					htmltext = "30676-01.htm";
				}
				else if (cond == 1)
				{
					htmltext = "30676-04.htm";
				}
				else if (cond == 2)
				{
					htmltext = "30676-05.htm";
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
					qs.giveItems(CROOP_INTRO, 1);
				}
				else if ((cond >= 3) && (cond <= 5))
				{
					htmltext = "30676-06.htm";
				}
				else if (cond == 6)
				{
					htmltext = "30676-07.htm";
				}
				else if (id == COMPLETED)
				{
					htmltext = "30676-13.htm";
				}
				else if (player.getLevel() < 79)
				{
					htmltext = "30676-00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case HECTOR:
				if (cond == 3)
				{
					htmltext = "30197-01.htm";
				}
				if ((cond >= 4) && (cond <= 7))
				{
					htmltext = "30197-04.htm";
				}
				break;
			
			case STAN:
				if (cond == 4)
				{
					htmltext = "30200-01.htm";
				}
				if ((cond >= 5) && (cond <= 7))
				{
					htmltext = "30200-05.htm";
				}
				break;
			
			case CORPSE:
				if (cond == 5)
				{
					htmltext = "32568-01.htm";
				}
				break;
			
			case HOLLINT:
				if (cond == 7)
				{
					htmltext = "30191-01.htm";
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
