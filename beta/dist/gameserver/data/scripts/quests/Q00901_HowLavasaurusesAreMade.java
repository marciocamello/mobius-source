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
import lineage2.gameserver.utils.Util;

public class Q00901_HowLavasaurusesAreMade extends Quest implements ScriptFile
{
	private static final int ROONEY = 32049;
	private static final int TOTEM_OF_BODY = 21899;
	private static final int TOTEM_OF_SPIRIT = 21900;
	private static final int TOTEM_OF_COURAGE = 21901;
	private static final int TOTEM_OF_FORTITUDE = 21902;
	private static final int LAVASAURUS_STONE_FRAGMENT = 21909;
	private static final int LAVASAURUS_HEAD_FRAGMENT = 21910;
	private static final int LAVASAURUS_BODY_FRAGMENT = 21911;
	private static final int LAVASAURUS_HORN_FRAGMENT = 21912;
	private static final int[] KILLING_MONSTERS = new int[]
	{
		18799,
		18800,
		18801,
		18802,
		18803
	};
	private static final int DROP_CHANCE = 5;
	
	public Q00901_HowLavasaurusesAreMade()
	{
		super(PARTY_ONE);
		addStartNpc(ROONEY);
		addTalkId(ROONEY);
		addQuestItem(LAVASAURUS_STONE_FRAGMENT, LAVASAURUS_HEAD_FRAGMENT, LAVASAURUS_BODY_FRAGMENT, LAVASAURUS_HORN_FRAGMENT);
		addKillId(KILLING_MONSTERS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "blacksmith_rooney_q901_03.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "blacksmith_rooney_q901_12a.htm":
				qs.giveItems(TOTEM_OF_BODY, 1);
				qs.playSound(SOUND_FINISH);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(this);
				break;
			
			case "blacksmith_rooney_q901_12b.htm":
				qs.giveItems(TOTEM_OF_SPIRIT, 1);
				qs.playSound(SOUND_FINISH);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(this);
				break;
			
			case "blacksmith_rooney_q901_12c.htm":
				qs.giveItems(TOTEM_OF_FORTITUDE, 1);
				qs.playSound(SOUND_FINISH);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(this);
				break;
			
			case "blacksmith_rooney_q901_12d.htm":
				qs.giveItems(TOTEM_OF_COURAGE, 1);
				qs.playSound(SOUND_FINISH);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(this);
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
				if (qs.getPlayer().getLevel() >= 76)
				{
					if (qs.isNowAvailableByTime())
					{
						htmltext = "blacksmith_rooney_q901_01.htm";
					}
					else
					{
						htmltext = "blacksmith_rooney_q901_01n.htm";
					}
				}
				else
				{
					htmltext = "blacksmith_rooney_q901_00.htm";
				}
				break;
			
			case 1:
				htmltext = "blacksmith_rooney_q901_04.htm";
				break;
			
			case 2:
				if (qs.getInt("collect") == 1)
				{
					htmltext = "blacksmith_rooney_q901_07.htm";
				}
				else
				{
					if (qs.haveQuestItem(LAVASAURUS_STONE_FRAGMENT, 10) && qs.haveQuestItem(LAVASAURUS_HEAD_FRAGMENT, 10) && qs.haveQuestItem(LAVASAURUS_BODY_FRAGMENT, 10) && qs.haveQuestItem(LAVASAURUS_HORN_FRAGMENT, 10))
					{
						htmltext = "blacksmith_rooney_q901_05.htm";
						qs.takeAllItems(LAVASAURUS_STONE_FRAGMENT, LAVASAURUS_HEAD_FRAGMENT, LAVASAURUS_BODY_FRAGMENT, LAVASAURUS_HORN_FRAGMENT);
						qs.set("collect", 1);
					}
					else
					{
						htmltext = "blacksmith_rooney_q901_06.htm";
					}
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
			if (!Util.contains(KILLING_MONSTERS, npc.getId()))
			{
				return null;
			}
			
			if (!qs.haveQuestItem(LAVASAURUS_STONE_FRAGMENT, 10))
			{
				qs.rollAndGive(LAVASAURUS_STONE_FRAGMENT, 1, DROP_CHANCE);
			}
			
			if (!qs.haveQuestItem(LAVASAURUS_HEAD_FRAGMENT, 10))
			{
				qs.rollAndGive(LAVASAURUS_HEAD_FRAGMENT, 1, DROP_CHANCE);
			}
			
			if (!qs.haveQuestItem(LAVASAURUS_BODY_FRAGMENT, 10))
			{
				qs.rollAndGive(LAVASAURUS_BODY_FRAGMENT, 1, DROP_CHANCE);
			}
			
			if (!qs.haveQuestItem(LAVASAURUS_HORN_FRAGMENT, 10))
			{
				qs.rollAndGive(LAVASAURUS_HORN_FRAGMENT, 1, DROP_CHANCE);
			}
			
			if (qs.haveQuestItem(LAVASAURUS_STONE_FRAGMENT, 10) && qs.haveQuestItem(LAVASAURUS_HEAD_FRAGMENT, 10) && qs.haveQuestItem(LAVASAURUS_BODY_FRAGMENT, 10) && qs.haveQuestItem(LAVASAURUS_HORN_FRAGMENT, 10))
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
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
