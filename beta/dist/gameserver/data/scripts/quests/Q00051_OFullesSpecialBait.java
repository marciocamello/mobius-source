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

public class Q00051_OFullesSpecialBait extends Quest implements ScriptFile
{
	// Npc
	private static final int OFulle = 31572;
	// Monster
	private static final int FetteredSoul = 20552;
	// Items
	private static final int LostBaitIngredient = 7622;
	private static final int IcyAirFishingLure = 7611;
	// Skill
	private static final int FishSkill = 1315;
	
	public Q00051_OFullesSpecialBait()
	{
		super(false);
		addStartNpc(OFulle);
		addTalkId(OFulle);
		addKillId(FetteredSoul);
		addQuestItem(LostBaitIngredient);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "fisher_ofulle_q0051_0104.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "fisher_ofulle_q0051_0201.htm":
				if (qs.getQuestItemsCount(LostBaitIngredient) < 100)
				{
					htmltext = "fisher_ofulle_q0051_0202.htm";
				}
				else
				{
					qs.unset("cond");
					qs.takeItems(LostBaitIngredient, -1);
					qs.giveItems(IcyAirFishingLure, 4);
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
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		
		if (qs.getState() == CREATED)
		{
			if (qs.getPlayer().getLevel() < 36)
			{
				htmltext = "fisher_ofulle_q0051_0103.htm";
				qs.exitCurrentQuest(true);
			}
			else if (qs.getPlayer().getSkillLevel(FishSkill) >= 11)
			{
				htmltext = "fisher_ofulle_q0051_0101.htm";
			}
			else
			{
				htmltext = "fisher_ofulle_q0051_0102.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if ((cond == 1) || (cond == 2))
		{
			if (qs.getQuestItemsCount(LostBaitIngredient) < 100)
			{
				htmltext = "fisher_ofulle_q0051_0106.htm";
				qs.setCond(1);
			}
			else
			{
				htmltext = "fisher_ofulle_q0051_0105.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && (qs.getQuestItemsCount(LostBaitIngredient) < 100) && Rnd.chance(30))
		{
			qs.giveItems(LostBaitIngredient, 1);
			
			if (qs.getQuestItemsCount(LostBaitIngredient) == 100)
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
