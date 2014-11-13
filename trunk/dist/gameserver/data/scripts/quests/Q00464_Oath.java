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

public class Q00464_Oath extends Quest implements ScriptFile
{
	// Npcs
	private static final int Sophia = 32596;
	private static final int Seresin = 30657;
	private static final int Holly = 30839;
	private static final int Flauen = 30899;
	private static final int Dominic = 31350;
	private static final int Chichirin = 30539;
	private static final int Tobias = 30297;
	private static final int Blacksmith = 31960;
	private static final int Agnes = 31588;
	// Items
	private static final int BookofSilence1 = 15538;
	private static final int BookofSilence2 = 15539;
	
	public Q00464_Oath()
	{
		super(false);
		addTalkId(Sophia, Seresin, Holly, Flauen, Dominic, Chichirin, Tobias, Blacksmith, Agnes);
		addQuestItem(BookofSilence1, BookofSilence2);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "bookowner":
				switch (Rnd.get(2, 8))
				{
					case 2:
						qs.setCond(2);
						htmltext = "sophia_q464_04a.htm";
						break;
					
					case 3:
						qs.setCond(3);
						htmltext = "sophia_q464_04b.htm";
						break;
					
					case 4:
						qs.setCond(4);
						htmltext = "sophia_q464_04c.htm";
						break;
					
					case 5:
						qs.setCond(5);
						htmltext = "sophia_q464_04d.htm";
						break;
					
					case 6:
						qs.setCond(6);
						htmltext = "sophia_q464_04e.htm";
						break;
					
					case 7:
						qs.setCond(7);
						htmltext = "sophia_q464_04f.htm";
						break;
					
					case 8:
						qs.setCond(8);
						htmltext = "sophia_q464_04g.htm";
						break;
					
					case 9:
						qs.setCond(9);
						htmltext = "sophia_q464_04h.htm";
						break;
				}
				qs.takeAllItems(BookofSilence1);
				qs.giveItems(BookofSilence2, 1);
				break;
			
			case "request_reward":
				switch (npc.getId())
				{
					case Seresin:
						htmltext = "seresin_q464_02.htm";
						break;
					
					case Holly:
						htmltext = "holly_q464_02.htm";
						break;
					
					case Flauen:
						htmltext = "flauen_q464_02.htm";
						break;
					
					case Dominic:
						htmltext = "dominic_q464_02.htm";
						break;
					
					case Chichirin:
						htmltext = "chichirin_q464_02.htm";
						break;
					
					case Tobias:
						htmltext = "tobias_q464_02.htm";
						break;
					
					case Blacksmith:
						htmltext = "blacksmith_q464_02.htm";
						break;
					
					case Agnes:
						htmltext = "agnes_q464_02.htm";
						break;
				}
				qs.giveItems(ADENA_ID, Rnd.get(45000, 90000));
				qs.addExpAndSp(Rnd.get(15450, 1200000), Rnd.get(15000, 200000));
				qs.takeAllItems(BookofSilence2);
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
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Sophia:
				switch (cond)
				{
					case 1:
						htmltext = "sophia_q464_01.htm";
						break;
					
					case 2:
						htmltext = "sophia_q464_05a.htm";
						break;
					
					case 3:
						htmltext = "sophia_q464_05b.htm";
						break;
					
					case 4:
						htmltext = "sophia_q464_05c.htm";
						break;
					
					case 5:
						htmltext = "sophia_q464_05d.htm";
						break;
					
					case 6:
						htmltext = "sophia_q464_05e.htm";
						break;
					
					case 7:
						htmltext = "sophia_q464_05f.htm";
						break;
					
					case 8:
						htmltext = "sophia_q464_05g.htm";
						break;
					
					case 9:
						htmltext = "sophia_q464_05h.htm";
						break;
				}
				break;
			
			case Seresin:
				if (cond == 2)
				{
					htmltext = "seresin_q464_01.htm";
				}
				break;
			
			case Holly:
				if (cond == 3)
				{
					htmltext = "holly_q464_01.htm";
				}
				break;
			
			case Flauen:
				if (cond == 4)
				{
					htmltext = "flauen_q464_01.htm";
				}
				break;
			
			case Dominic:
				if (cond == 5)
				{
					htmltext = "dominic_q464_01.htm";
				}
				break;
			
			case Chichirin:
				if (cond == 6)
				{
					htmltext = "chichirin_q464_01.htm";
				}
				break;
			
			case Tobias:
				if (cond == 7)
				{
					htmltext = "tobias_q464_01.htm";
				}
				break;
			
			case Blacksmith:
				if (cond == 8)
				{
					htmltext = "blacksmith_q464_01.htm";
				}
				break;
			
			case Agnes:
				if (cond == 9)
				{
					htmltext = "agnes_q464_01.htm";
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
