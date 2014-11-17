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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q10308_NotToBeForgotten extends Quest implements ScriptFile
{
	private static final int NPC_ADVENTURER_HELPER = 33463;
	private static final int NPC_KURTIZ = 30870;
	private static final int[] MONSTERS =
	{
		20679,
		20680,
		21017,
		21019,
		21020,
		21022,
		21258,
		21259,
		21018,
		21021
	};
	private static final int ITEM_LEGACY_CORE = 19487;
	private static final int DROP_CHANCE = 60;
	
	public Q10308_NotToBeForgotten()
	{
		super(false);
		addStartNpc(NPC_ADVENTURER_HELPER);
		addTalkId(NPC_KURTIZ);
		addKillId(MONSTERS);
		addQuestItem(ITEM_LEGACY_CORE);
		addLevelCheck(55, 59);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (qs == null)
		{
			return "noquest";
		}
		
		if (event.equals("33463-04.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		if (qs == null)
		{
			return htmltext;
		}
		
		final Player player = qs.getPlayer();
		
		if (npc.getId() == NPC_ADVENTURER_HELPER)
		{
			if ((player.getLevel() < 55) || (player.getLevel() > 59))
			{
				htmltext = "33463-00.htm";
			}
			else if (qs.isCreated())
			{
				htmltext = "33463-01.htm";
			}
			else if ((qs.isStarted()) && (qs.getCond() == 1))
			{
				htmltext = "33463-05.htm";
			}
			else if (qs.isCompleted())
			{
				htmltext = "completed";
			}
		}
		else if (npc.getId() == NPC_KURTIZ)
		{
			if (qs.isCompleted())
			{
				htmltext = "30870-03.htm";
			}
			else if ((qs.isStarted()) && (qs.getCond() == 1))
			{
				htmltext = "30870-01.htm";
			}
			else if ((qs.isStarted()) && (qs.getCond() == 2))
			{
				qs.takeItems(ITEM_LEGACY_CORE, -1);
				qs.addExpAndSp(2322445, 1968325);
				qs.giveItems(57, 376704, true);
				qs.unset("cond");
				qs.exitCurrentQuest(false);
				htmltext = "30870-02.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((npc == null) || (qs == null))
		{
			return null;
		}
		
		if (Util.contains(MONSTERS, npc.getId()) && (qs.getCond() == 1))
		{
			if (qs.rollAndGive(ITEM_LEGACY_CORE, 1, 3, 40, DROP_CHANCE))
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
			}
		}
		
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10308_NotToBeForgotten.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
