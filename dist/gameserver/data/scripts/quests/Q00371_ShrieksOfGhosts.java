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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00371_ShrieksOfGhosts extends Quest implements ScriptFile
{
	// Npcs
	private static final int REVA = 30867;
	private static final int PATRIN = 30929;
	// Monsters
	private static final int Hallates_Warrior = 20818;
	private static final int Hallates_Knight = 20820;
	private static final int Hallates_Commander = 20824;
	// Items
	private static final int Ancient_Porcelain__Excellent = 6003;
	private static final int Ancient_Porcelain__High_Quality = 6004;
	private static final int Ancient_Porcelain__Low_Quality = 6005;
	private static final int Ancient_Porcelain__Lowest_Quality = 6006;
	private static final int Ancient_Ash_Urn = 5903;
	private static final int Ancient_Porcelain = 6002;
	// Others
	private static final int Urn_Chance = 43;
	private static final int Ancient_Porcelain__Excellent_Chance = 1;
	private static final int Ancient_Porcelain__High_Quality_Chance = 14;
	private static final int Ancient_Porcelain__Low_Quality_Chance = 46;
	private static final int Ancient_Porcelain__Lowest_Quality_Chance = 84;
	private final Map<Integer, Integer> common_chances = new HashMap<>();
	
	public Q00371_ShrieksOfGhosts()
	{
		super(true);
		addStartNpc(REVA);
		addTalkId(PATRIN);
		addKillId(Hallates_Warrior);
		addKillId(Hallates_Knight);
		addKillId(Hallates_Commander);
		addQuestItem(Ancient_Ash_Urn);
		common_chances.put(Hallates_Warrior, 71);
		common_chances.put(Hallates_Knight, 74);
		common_chances.put(Hallates_Commander, 82);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		final int _state = st.getState();
		
		switch (event)
		{
			case "30867-03.htm":
				if (_state == CREATED)
				{
					st.setState(STARTED);
					st.setCond(1);
					st.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "30867-10.htm":
				if (_state == STARTED)
				{
					final long Ancient_Ash_Urn_count = st.getQuestItemsCount(Ancient_Ash_Urn);
					
					if (Ancient_Ash_Urn_count > 0)
					{
						st.takeItems(Ancient_Ash_Urn, -1);
						st.giveItems(ADENA_ID, Ancient_Ash_Urn_count * 1000L);
					}
					
					st.exitCurrentQuest(true);
				}
				break;
			
			case "30867-TRADE":
				if (_state == STARTED)
				{
					final long Ancient_Ash_Urn_count = st.getQuestItemsCount(Ancient_Ash_Urn);
					
					if (Ancient_Ash_Urn_count > 0)
					{
						htmltext = Ancient_Ash_Urn_count > 100 ? "30867-08.htm" : "30867-07.htm";
						final int bonus = Ancient_Ash_Urn_count > 100 ? 17000 : 3000;
						st.takeItems(Ancient_Ash_Urn, -1);
						st.giveItems(ADENA_ID, bonus + (Ancient_Ash_Urn_count * 1000L));
					}
					else
					{
						htmltext = "30867-06.htm";
					}
				}
				break;
			
			case "30929-TRADE":
				if (_state == STARTED)
				{
					final long Ancient_Porcelain_count = st.getQuestItemsCount(Ancient_Porcelain);
					
					if (Ancient_Porcelain_count > 0)
					{
						st.takeItems(Ancient_Porcelain, 1);
						int chance = Rnd.get(100);
						
						if (chance < Ancient_Porcelain__Excellent_Chance)
						{
							st.giveItems(Ancient_Porcelain__Excellent, 1);
							htmltext = "30929-03.htm";
						}
						else if (chance < Ancient_Porcelain__High_Quality_Chance)
						{
							st.giveItems(Ancient_Porcelain__High_Quality, 1);
							htmltext = "30929-04.htm";
						}
						else if (chance < Ancient_Porcelain__Low_Quality_Chance)
						{
							st.giveItems(Ancient_Porcelain__Low_Quality, 1);
							htmltext = "30929-05.htm";
						}
						else if (chance < Ancient_Porcelain__Lowest_Quality_Chance)
						{
							st.giveItems(Ancient_Porcelain__Lowest_Quality, 1);
							htmltext = "30929-06.htm";
						}
						else
						{
							htmltext = "30929-07.htm";
						}
					}
					else
					{
						htmltext = "30929-02.htm";
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
		final int _state = qs.getState();
		final int npcId = npc.getId();
		
		if (_state == CREATED)
		{
			if (npcId != REVA)
			{
				return htmltext;
			}
			else if (qs.getPlayer().getLevel() >= 59)
			{
				htmltext = "30867-02.htm";
				qs.setCond(0);
			}
			else
			{
				htmltext = "30867-01.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if ((_state == STARTED) && (npcId == REVA))
		{
			htmltext = qs.getQuestItemsCount(Ancient_Porcelain) > 0 ? "30867-05.htm" : "30867-04.htm";
		}
		else if ((_state == STARTED) && (npcId == PATRIN))
		{
			htmltext = "30929-01.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getRandomPartyMember(STARTED, Config.ALT_PARTY_DISTRIBUTION_RANGE);
		final QuestState st = player.getQuestState(qs.getQuest().getName());
		final Integer _chance = common_chances.get(npc.getId());
		
		if (_chance == null)
		{
			return null;
		}
		
		if (Rnd.chance(_chance))
		{
			st.giveItems(Rnd.chance(Urn_Chance) ? Ancient_Ash_Urn : Ancient_Porcelain, 1);
			st.playSound(SOUND_ITEMGET);
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
