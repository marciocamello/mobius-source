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
import lineage2.gameserver.utils.Util;

public class Q00477_BloodFromTheWall extends Quest implements ScriptFile
{
	// Npcs
	public static final int GUIDE = 33463;
	public static final int JASTIN = 31282;
	// Item
	public static final int BLOOD_TEARS = 19496;
	// Monsters
	private final int[] MONSTERS =
	{
		21294,
		21295,
		21296,
		21297,
		21298,
		21299,
		21300,
		21301,
		21302,
		21303,
		21304,
		21305,
		21307,
		21312,
		21313
	};
	
	public Q00477_BloodFromTheWall()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(JASTIN);
		addKillId(MONSTERS);
		addQuestItem(BLOOD_TEARS);
		addLevelCheck(70, 74);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("33463-3.htm"))
		{
			qs.setCond(1);
			qs.setState(2);
			qs.playSound("ItemSound.quest_accept");
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int state = qs.getState();
		final int cond = qs.getCond();
		
		if (npcId == GUIDE)
		{
			if (state == 1)
			{
				if (!qs.isNowAvailableByTime())
				{
					return "33463-comp.htm";
				}
				
				return "33463.htm";
			}
			else if (state == 2)
			{
				if (cond == 1)
				{
					return "33463-4.htm";
				}
				else if (cond == 2)
				{
					return "33463-5.htm";
				}
			}
		}
		else if ((npcId == JASTIN) && (state == 2))
		{
			if (cond == 1)
			{
				return "31282-1.htm";
			}
			else if (cond == 2)
			{
				qs.giveItems(57, 334560L);
				qs.addExpAndSp(8534700L, 8523390L);
				qs.takeItems(BLOOD_TEARS, -1L);
				qs.unset("cond");
				qs.playSound("ItemSound.quest_finish");
				qs.exitCurrentQuest(this);
				return "31282.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() != 1) || (npc == null))
		{
			return null;
		}
		
		if ((Util.contains(MONSTERS, npc.getId())) && (Rnd.chance(50)))
		{
			qs.giveItems(BLOOD_TEARS, 1L);
		}
		
		if (qs.getQuestItemsCount(BLOOD_TEARS) >= 45L)
		{
			qs.setCond(2);
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