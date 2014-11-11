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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00139_ShadowFoxPart1 extends Quest implements ScriptFile
{
	// Npc
	private final static int MIA = 30896;
	// Items
	private final static int FRAGMENT = 10345;
	private final static int CHEST = 10346;
	// Monsters
	private final static int TasabaLizardman1 = 20784;
	private final static int TasabaLizardman2 = 21639;
	private final static int TasabaLizardmanShaman1 = 20785;
	private final static int TasabaLizardmanShaman2 = 21640;
	
	public Q00139_ShadowFoxPart1()
	{
		super(false);
		addFirstTalkId(MIA);
		addTalkId(MIA);
		addQuestItem(FRAGMENT, CHEST);
		addKillId(TasabaLizardman1, TasabaLizardman2, TasabaLizardmanShaman1, TasabaLizardmanShaman2);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "30896-03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30896-11.htm":
				qs.setCond(2);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30896-14.htm":
				qs.takeItems(FRAGMENT, -1);
				qs.takeItems(CHEST, -1);
				qs.set("talk", "1");
				break;
			
			case "30896-16.htm":
				qs.playSound(SOUND_FINISH);
				qs.giveItems(ADENA_ID, 14050);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		
		switch (cond)
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 37)
				{
					htmltext = "30896-01.htm";
				}
				else
				{
					htmltext = "30896-00.htm";
				}
				break;
			
			case 1:
				htmltext = "30896-03.htm";
				break;
			
			case 2:
				if ((qs.getQuestItemsCount(FRAGMENT) >= 10) && (qs.getQuestItemsCount(CHEST) >= 1))
				{
					htmltext = "30896-13.htm";
				}
				else if (qs.getInt("talk") == 1)
				{
					htmltext = "30896-14.htm";
				}
				else
				{
					htmltext = "30896-12.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(Q00138_TempleChampionPart2.class);
		
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		
		return "";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 2)
		{
			qs.giveItems(FRAGMENT, 1);
			qs.playSound(SOUND_ITEMGET);
			
			if (Rnd.chance(10))
			{
				qs.giveItems(CHEST, 1);
			}
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
