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

public class Q00460_PreciousResearchMaterial extends Quest implements ScriptFile
{
	// Npcs
	private static final int AMER = 33092;
	private static final int FILAR = 30535;
	// Items
	private static final int TEREDOR_EGG_FRAGMENT = 17735;
	private static final int[] MOB_EGGS =
	{
		18997,
		19023
	};
	private static final int REWARD_PROOF_OF_FIDELITY = 19450;
	
	public Q00460_PreciousResearchMaterial()
	{
		super(PARTY_ALL);
		addStartNpc(AMER);
		addTalkId(FILAR);
		addKillId(MOB_EGGS);
		addQuestItem(TEREDOR_EGG_FRAGMENT);
		addLevelCheck(85, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("30535-01.htm"))
		{
			qs.playSound(SOUND_FINISH);
			qs.takeAllItems(TEREDOR_EGG_FRAGMENT);
			qs.giveItems(REWARD_PROOF_OF_FIDELITY, 2);
			qs.exitCurrentQuest(this);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (npc.getId())
		{
			case AMER:
				if (qs.getPlayer().getLevel() < 85)
				{
					return htmltext = "You level is not correct!";
				}
				switch (qs.getState())
				{
					case COMPLETED:
						htmltext = "completed";
						break;
					
					case CREATED:
						if (isAvailableFor(qs.getPlayer()))
						{
							htmltext = "33092-00.htm";
							qs.setState(STARTED);
							qs.playSound(SOUND_ACCEPT);
							qs.setCond(1);
						}
						else
						{
							htmltext = "daily";
						}
						break;
					
					case STARTED:
						if (qs.getCond() == 1)
						{
							htmltext = "33092-00.htm";
						}
						break;
				}
				break;
			
			case FILAR:
				if (qs.isStarted() && (qs.getCond() == 2))
				{
					htmltext = "30535-00.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Rnd.chance(50))
		{
			qs.giveItems(TEREDOR_EGG_FRAGMENT, 1);
			qs.playSound(SOUND_ITEMGET);
			if (qs.getQuestItemsCount(TEREDOR_EGG_FRAGMENT) >= 20)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
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