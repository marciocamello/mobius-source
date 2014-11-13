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

public class Q00137_TempleChampionPart1 extends Quest implements ScriptFile
{
	// Npc
	private static final int SYLVAIN = 30070;
	// Item
	private static final int FRAGMENT = 10340;
	// Monsters
	private static final int BadgeTempleExecutor = 10334;
	private static final int BadgeTempleMissionary = 10339;
	private final static int GraniteGolem = 20083;
	private final static int HangmanTree = 20144;
	private final static int AmberBasilisk = 20199;
	private final static int Strain = 20200;
	private final static int Ghoul = 20201;
	private final static int DeadSeeker = 20202;
	
	public Q00137_TempleChampionPart1()
	{
		super(false);
		addStartNpc(SYLVAIN);
		addKillId(GraniteGolem, HangmanTree, AmberBasilisk, Strain, Ghoul, DeadSeeker);
		addQuestItem(FRAGMENT);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "sylvain_q0137_04.htm":
				qs.takeItems(BadgeTempleExecutor, -1);
				qs.takeItems(BadgeTempleMissionary, -1);
				qs.setCond(1);
				qs.setState(STARTED);
				qs.set("talk", "0");
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "sylvain_q0137_08.htm":
				qs.set("talk", "1");
				break;
			
			case "sylvain_q0137_10.htm":
				qs.set("talk", "2");
				break;
			
			case "sylvain_q0137_13.htm":
				qs.unset("talk");
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "sylvain_q0137_24.htm":
				qs.giveItems(ADENA_ID, 69146);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		
		switch (cond)
		{
			case 0:
				if ((qs.getPlayer().getLevel() >= 35) && (qs.getQuestItemsCount(BadgeTempleExecutor) > 0) && (qs.getQuestItemsCount(BadgeTempleMissionary) > 0))
				{
					htmltext = "sylvain_q0137_01.htm";
				}
				else
				{
					htmltext = "sylvain_q0137_03.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if (qs.getInt("talk") == 0)
				{
					htmltext = "sylvain_q0137_05.htm";
				}
				else if (qs.getInt("talk") == 1)
				{
					htmltext = "sylvain_q0137_08.htm";
				}
				else if (qs.getInt("talk") == 2)
				{
					htmltext = "sylvain_q0137_10.htm";
				}
				break;
			
			case 2:
				htmltext = "sylvain_q0137_13.htm";
				break;
			
			case 3:
				if (qs.getQuestItemsCount(FRAGMENT) >= 30)
				{
					htmltext = "sylvain_q0137_15.htm";
					qs.set("talk", "1");
					qs.takeItems(FRAGMENT, -1);
				}
				else if (qs.getInt("talk") == 1)
				{
					htmltext = "sylvain_q0137_16.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 2)
		{
			if (qs.getQuestItemsCount(FRAGMENT) < 30)
			{
				qs.giveItems(FRAGMENT, 1);
				
				if (qs.getQuestItemsCount(FRAGMENT) >= 30)
				{
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
				}
				else
				{
					qs.playSound(SOUND_ITEMGET);
				}
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
