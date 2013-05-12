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

/**
 */
public class _459_TheVillianOfTheUndergroundMineTeredor extends Quest implements ScriptFile
{
	private static final int NPC_FILAUR = 30535;
	private static final int MOB_HENCHMAN = 25785; // Teredor
	private static final int ITEM_PROOF_OF_FIDELITY = 19450;
	
	public _459_TheVillianOfTheUndergroundMineTeredor()
	{
		super(PARTY_ALL);
		
		addStartNpc(NPC_FILAUR);
		addKillId(MOB_HENCHMAN);
		addLevelCheck(85, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("30535-04.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30535-07.htm"))
		{
			st.giveItems(ITEM_PROOF_OF_FIDELITY, 20);
			st.playSound("ItemSound.quest_finish");
			st.exitCurrentQuest(this);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		
		if (npc.getNpcId() == NPC_FILAUR)
		{
			switch (st.getState())
			{
				default:
					break;
				case COMPLETED:
					htmltext = "completed";
					break;
				case CREATED:
					if (st.getPlayer().getLevel() >= 85)
					{
						if (isAvailableFor(st.getPlayer()))
						{
							htmltext = "30535-00.htm";
						}
						else
						{
							htmltext = "daily";
						}
					}
					else
					{
						htmltext = ""; // low level;
					}
					
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						htmltext = "30535-05.htm";
						break;
					}
					
					if (st.getCond() == 2)
					{
						htmltext = "30535-06.htm";
					}
					
					break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((npc.getNpcId() == MOB_HENCHMAN) && (st.getCond() == 1))
		{
			st.setCond(2);
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
