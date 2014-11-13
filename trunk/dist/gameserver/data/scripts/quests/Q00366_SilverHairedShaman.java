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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00366_SilverHairedShaman extends Quest implements ScriptFile
{
	// Npc
	private static final int DIETER = 30111;
	// Monsters
	private static final int SAIRON = 20986;
	private static final int SAIRONS_DOLL = 20987;
	private static final int SAIRONS_PUPPET = 20988;
	// Item
	private static final int SAIRONS_SILVER_HAIR = 5874;
	// Others
	private static final int ADENA_PER_ONE = 500;
	private static final int START_ADENA = 12070;
	
	public Q00366_SilverHairedShaman()
	{
		super(false);
		addStartNpc(DIETER);
		addKillId(SAIRON, SAIRONS_DOLL, SAIRONS_PUPPET);
		addQuestItem(SAIRONS_SILVER_HAIR);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30111-02.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30111-quit.htm":
				qs.takeItems(SAIRONS_SILVER_HAIR, -1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		int cond = qs.getCond();
		
		if (qs.getState() == CREATED)
		{
			qs.setCond(0);
		}
		else
		{
			cond = qs.getCond();
		}
		
		if (cond == 0)
		{
			if (qs.getPlayer().getLevel() >= 48)
			{
				htmltext = "30111-01.htm";
			}
			else
			{
				htmltext = "30111-00.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if ((cond == 1) && (qs.getQuestItemsCount(SAIRONS_SILVER_HAIR) == 0))
		{
			htmltext = "30111-03.htm";
		}
		else if ((cond == 1) && (qs.getQuestItemsCount(SAIRONS_SILVER_HAIR) >= 1))
		{
			qs.giveItems(ADENA_ID, ((qs.getQuestItemsCount(SAIRONS_SILVER_HAIR) * ADENA_PER_ONE) + START_ADENA));
			qs.takeItems(SAIRONS_SILVER_HAIR, -1);
			htmltext = "30111-have.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Rnd.chance(66))
		{
			qs.giveItems(SAIRONS_SILVER_HAIR, 1);
			qs.playSound(SOUND_MIDDLE);
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
