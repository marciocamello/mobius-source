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

public class Q00642_APowerfulPrimevalCreature extends Quest implements ScriptFile
{
	private static final int Dinn = 32105;
	private static final int Ancient_Egg = 18344;
	private static final int[] Dino =
	{
		22196,
		22197,
		22198,
		22199,
		22200,
		22201,
		22202,
		22203,
		22204,
		22205,
		22218,
		22219,
		22220,
		22223,
		22224,
		22225,
		22226,
		22227,
		22742,
		22743,
		22744,
		22745
	};
	private static final int[] Rewards =
	{
		8690,
		8692,
		8694,
		8696,
		8698,
		8700,
		8702,
		8704,
		8706,
		8708,
		8710
	};
	private static final int Dinosaur_Tissue = 8774;
	private static final int Dinosaur_Egg = 8775;
	private static final int Dinosaur_Tissue_Chance = 33;
	private static final int Dinosaur_Egg_Chance = 1;
	
	public Q00642_APowerfulPrimevalCreature()
	{
		super(true);
		addStartNpc(Dinn);
		addQuestItem(Dinosaur_Tissue, Dinosaur_Egg);
		addKillId(Ancient_Egg);
		for (int dino_id : Dino)
		{
			addKillId(dino_id);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int _state = qs.getState();
		final long Dinosaur_Tissue_Count = qs.getQuestItemsCount(Dinosaur_Tissue);
		
		if (event.equals("dindin_q0642_04.htm") && (_state == CREATED))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("dindin_q0642_12.htm") && (_state == STARTED))
		{
			if (Dinosaur_Tissue_Count == 0)
			{
				return "dindin_q0642_08a.htm";
			}
			
			qs.takeItems(Dinosaur_Tissue, -1);
			qs.giveItems(ADENA_ID, Dinosaur_Tissue_Count * 3000, false);
			qs.playSound(SOUND_MIDDLE);
		}
		else if (event.equals("0"))
		{
			return null;
		}
		else if (_state == STARTED)
		{
			try
			{
				final int rew_id = Integer.valueOf(event);
				
				if ((Dinosaur_Tissue_Count < 150) || (qs.getQuestItemsCount(Dinosaur_Egg) == 0))
				{
					return "dindin_q0642_08a.htm";
				}
				
				for (int reward : Rewards)
				{
					if (reward == rew_id)
					{
						qs.takeItems(Dinosaur_Tissue, 150);
						qs.takeItems(Dinosaur_Egg, 1);
						qs.giveItems(reward, 1, false);
						qs.giveItems(ADENA_ID, 44000, false);
						qs.playSound(SOUND_MIDDLE);
						return "dindin_q0642_12.htm";
					}
				}
				
				return null;
			}
			catch (Exception E)
			{
				// empty catch clause
			}
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		switch (qs.getState())
		{
			case CREATED:
				if (qs.getPlayer().getLevel() < 75)
				{
					qs.exitCurrentQuest(true);
					return "dindin_q0642_01a.htm";
				}
				qs.setCond(0);
				return "dindin_q0642_01.htm";
				
			case STARTED:
				long Dinosaur_Tissue_Count = qs.getQuestItemsCount(Dinosaur_Tissue);
				if (Dinosaur_Tissue_Count == 0)
				{
					return "dindin_q0642_08a.htm";
				}
				if ((Dinosaur_Tissue_Count < 150) || (qs.getQuestItemsCount(Dinosaur_Egg) == 0))
				{
					return "dindin_q0642_07.htm";
				}
				return "dindin_q0642_07a.htm";
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getState() != STARTED) || (qs.getCond() != 1))
		{
			return null;
		}
		
		if (npc.getId() == Ancient_Egg)
		{
			qs.rollAndGive(Dinosaur_Egg, 1, Dinosaur_Egg_Chance);
		}
		else
		{
			qs.rollAndGive(Dinosaur_Tissue, 1, Dinosaur_Tissue_Chance);
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
