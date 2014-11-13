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

public class Q00310_OnlyWhatRemains extends Quest implements ScriptFile
{
	// Npc
	private static final int KINTAIJIN = 32640;
	// Monsters
	private static final int[] MOBS =
	{
		22617,
		22624,
		22625,
		22626
	};
	// Items
	private static final int DIRTYBEAD = 14880;
	private static final int ACCELERATOR = 14832;
	private static final int JEWEL = 14835;
	
	public Q00310_OnlyWhatRemains()
	{
		super(false);
		addStartNpc(KINTAIJIN);
		addTalkId(KINTAIJIN);
		addKillId(MOBS);
		addQuestItem(DIRTYBEAD);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("32640-3.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int id = qs.getState();
		
		if (id == COMPLETED)
		{
			htmltext = "32640-10.htm";
		}
		else if (id == CREATED)
		{
			QuestState ImTheOnlyOneYouCanTrust = qs.getPlayer().getQuestState(Q00240_ImTheOnlyOneYouCanTrust.class);
			
			if ((qs.getPlayer().getLevel() >= 81) && (ImTheOnlyOneYouCanTrust != null) && ImTheOnlyOneYouCanTrust.isCompleted())
			{
				htmltext = "32640-1.htm";
			}
			else
			{
				htmltext = "32640-0.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			htmltext = "32640-8.htm";
		}
		else if (cond == 2)
		{
			qs.takeItems(DIRTYBEAD, 500);
			qs.giveItems(ACCELERATOR, 1);
			qs.giveItems(JEWEL, 1);
			qs.exitCurrentQuest(true);
			qs.playSound(SOUND_FINISH);
			htmltext = "32640-9.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.giveItems(DIRTYBEAD, 1);
			
			if (qs.getQuestItemsCount(DIRTYBEAD) >= 500)
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
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
