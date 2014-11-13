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

public class Q00050_LanoscosSpecialBait extends Quest implements ScriptFile
{
	// Npc
	private static final int Lanosco = 31570;
	// Monster
	private static final int SingingWind = 21026;
	// Items
	private static final int EssenceofWind = 7621;
	private static final int WindFishingLure = 7610;
	// Skill
	private static final int FishSkill = 1315;
	
	public Q00050_LanoscosSpecialBait()
	{
		super(false);
		addStartNpc(Lanosco);
		addTalkId(Lanosco);
		addKillId(SingingWind);
		addQuestItem(EssenceofWind);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "fisher_lanosco_q0050_0104.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "fisher_lanosco_q0050_0201.htm":
				if (qs.getQuestItemsCount(EssenceofWind) < 100)
				{
					htmltext = "fisher_lanosco_q0050_0202.htm";
				}
				else
				{
					qs.unset("cond");
					qs.takeItems(EssenceofWind, -1);
					qs.giveItems(WindFishingLure, 4);
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
			if (qs.getPlayer().getLevel() < 27)
			{
				htmltext = "fisher_lanosco_q0050_0103.htm";
				qs.exitCurrentQuest(true);
			}
			else if (qs.getPlayer().getSkillLevel(FishSkill) >= 8)
			{
				htmltext = "fisher_lanosco_q0050_0101.htm";
			}
			else
			{
				htmltext = "fisher_lanosco_q0050_0102.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if ((cond == 1) || (cond == 2))
		{
			if (qs.getQuestItemsCount(EssenceofWind) < 100)
			{
				htmltext = "fisher_lanosco_q0050_0106.htm";
				qs.setCond(1);
			}
			else
			{
				htmltext = "fisher_lanosco_q0050_0105.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && (qs.getQuestItemsCount(EssenceofWind) < 100) && Rnd.chance(30))
		{
			qs.giveItems(EssenceofWind, 1);
			
			if (qs.getQuestItemsCount(EssenceofWind) == 100)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
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
