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

public class Q00700_CursedLife extends Quest implements ScriptFile
{
	// Npc
	private static final int Orbyu = 32560;
	// Items
	private static final int SwallowedSkull = 13872;
	private static final int SwallowedSternum = 13873;
	private static final int SwallowedBones = 13874;
	// Monsters
	private static final int MutantBird1 = 22602;
	private static final int MutantBird2 = 22603;
	private static final int DraHawk1 = 22604;
	private static final int DraHawk2 = 22605;
	private static final int Rok = 25624;
	// Others
	private static final int _skullprice = 50000;
	private static final int _sternumprice = 5000;
	private static final int _bonesprice = 500;
	
	public Q00700_CursedLife()
	{
		super(false);
		addStartNpc(Orbyu);
		addTalkId(Orbyu);
		addKillId(MutantBird1, MutantBird2, DraHawk1, DraHawk2, Rok);
		addQuestItem(SwallowedSkull, SwallowedSternum, SwallowedBones);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "orbyu_q700_2.htm":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "ex_bones":
				if (cond == 1)
				{
					if ((qs.getQuestItemsCount(SwallowedSkull) >= 1) || (qs.getQuestItemsCount(SwallowedSternum) >= 1) || (qs.getQuestItemsCount(SwallowedBones) >= 1))
					{
						long _adenatogive = (qs.getQuestItemsCount(SwallowedSkull) * _skullprice) + (qs.getQuestItemsCount(SwallowedSternum) * _sternumprice) + (qs.getQuestItemsCount(SwallowedBones) * _bonesprice);
						qs.giveItems(ADENA_ID, _adenatogive);
						
						if (qs.getQuestItemsCount(SwallowedSkull) >= 1)
						{
							qs.takeItems(SwallowedSkull, -1);
						}
						
						if (qs.getQuestItemsCount(SwallowedSternum) >= 1)
						{
							qs.takeItems(SwallowedSternum, -1);
						}
						
						if (qs.getQuestItemsCount(SwallowedBones) >= 1)
						{
							qs.takeItems(SwallowedBones, -1);
						}
						
						htmltext = "orbyu_q700_4.htm";
					}
					else
					{
						htmltext = "orbyu_q700_3a.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				final QuestState GoodDayToFly = qs.getPlayer().getQuestState(Q10273_GoodDayToFly.class);
				if ((qs.getPlayer().getLevel() >= 75) && (GoodDayToFly != null) && GoodDayToFly.isCompleted())
				{
					htmltext = "orbyu_q700_1.htm";
				}
				else
				{
					htmltext = "orbyu_q700_0.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if ((qs.getQuestItemsCount(SwallowedSkull) >= 1) || (qs.getQuestItemsCount(SwallowedSternum) >= 1) || (qs.getQuestItemsCount(SwallowedBones) >= 1))
				{
					htmltext = "orbyu_q700_3.htm";
				}
				else
				{
					htmltext = "orbyu_q700_3a.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			switch (npc.getId())
			{
				case MutantBird1:
				case MutantBird2:
				case DraHawk1:
				case DraHawk2:
					qs.giveItems(SwallowedBones, 1);
					qs.playSound(SOUND_ITEMGET);
					if (Rnd.chance(20))
					{
						qs.giveItems(SwallowedSkull, 1);
					}
					else if (Rnd.chance(20))
					{
						qs.giveItems(SwallowedSternum, 1);
					}
					break;
				
				case Rok:
					qs.giveItems(SwallowedSternum, 50);
					qs.giveItems(SwallowedSkull, 30);
					qs.giveItems(SwallowedBones, 100);
					qs.playSound(SOUND_ITEMGET);
					break;
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
