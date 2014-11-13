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

public class Q00052_WilliesSpecialBait extends Quest implements ScriptFile
{
	// Npc
	private final static int Willie = 31574;
	// Monsters
	private final static int TarlkBasilisk1 = 20573;
	private final static int TarlkBasilisk2 = 20574;
	// Items
	private final static int EyeOfTarlkBasilisk = 7623;
	private final static int EarthFishingLure = 7612;
	// Skill
	private final static int FishSkill = 1315;
	
	public Q00052_WilliesSpecialBait()
	{
		super(false);
		addStartNpc(Willie);
		addKillId(TarlkBasilisk1, TarlkBasilisk2);
		addQuestItem(EyeOfTarlkBasilisk);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "fisher_willeri_q0052_0104.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "fisher_willeri_q0052_0201.htm":
				if (qs.getQuestItemsCount(EyeOfTarlkBasilisk) < 100)
				{
					htmltext = "fisher_willeri_q0052_0202.htm";
				}
				else
				{
					qs.unset("cond");
					qs.takeItems(EyeOfTarlkBasilisk, -1);
					qs.giveItems(EarthFishingLure, 4);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
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
		
		if (qs.getState() == CREATED)
		{
			if (qs.getPlayer().getLevel() < 48)
			{
				htmltext = "fisher_willeri_q0052_0103.htm";
				qs.exitCurrentQuest(true);
			}
			else if (qs.getPlayer().getSkillLevel(FishSkill) >= 16)
			{
				htmltext = "fisher_willeri_q0052_0101.htm";
			}
			else
			{
				htmltext = "fisher_willeri_q0052_0102.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if ((cond == 1) || (cond == 2))
		{
			if (qs.getQuestItemsCount(EyeOfTarlkBasilisk) < 100)
			{
				htmltext = "fisher_willeri_q0052_0106.htm";
				qs.setCond(1);
			}
			else
			{
				htmltext = "fisher_willeri_q0052_0105.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getCond() == 1) && (st.getQuestItemsCount(EyeOfTarlkBasilisk) < 100) && Rnd.chance(30))
		{
			st.giveItems(EyeOfTarlkBasilisk, 1);
			
			if (st.getQuestItemsCount(EyeOfTarlkBasilisk) == 100)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
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
