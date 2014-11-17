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

public class Q10304_ForTheForgottenHeroes extends Quest implements ScriptFile
{
	// Npc
	private static final int ISHAEL = 32894;
	
	// Monsters
	private static final int YUI = 25837;
	private static final int KINEN = 25840;
	private static final int KONJAN = 25845;
	private static final int RASINDA = 25841;
	private static final int MAKYSHA = 25838;
	private static final int HORNAPI = 25839;
	private static final int YONTYMAK = 25846;
	private static final int FRON = 25825;
	
	public Q10304_ForTheForgottenHeroes()
	{
		super(PARTY_ALL);
		addStartNpc(ISHAEL);
		addTalkId(ISHAEL);
		addKillId(YUI, KINEN, KONJAN, RASINDA, MAKYSHA, HORNAPI, YONTYMAK, FRON);
		addLevelCheck(90, 99);
		addQuestCompletedCheck(Q10302_UnsettlingShadowAndRumors.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "32894-7.htm":
				qs.setCond(2);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.takeItems(17618, -1);
				break;
			
			case "32894-11.htm":
				qs.addExpAndSp(15197798, 6502166);
				qs.giveItems(57, 47085998);
				qs.giveItems(33467, 1);
				qs.giveItems(33466, 1);
				qs.giveItems(32779, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() == COMPLETED)
		{
			return "32894-comp.htm";
		}
		
		if (qs.getPlayer().getLevel() < 90)
		{
			return "32894-lvl.htm";
		}
		
		final QuestState state = qs.getPlayer().getQuestState(Q10302_UnsettlingShadowAndRumors.class);
		if ((state == null) || !state.isCompleted())
		{
			return "32894-lvl.htm";
		}
		
		switch (qs.getCond())
		{
			case 1:
				return "32894-3.htm";
				
			case 2:
				return "32894-15.htm";
				
			case 9:
				return "32894-10.htm";
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case YUI:
				if (cond == 2)
				{
					qs.setCond(3);
				}
				break;
			
			case KINEN:
				if (cond == 3)
				{
					qs.setCond(4);
				}
				break;
			
			case KONJAN:
				if (cond == 4)
				{
					qs.setCond(5);
				}
				break;
			
			case RASINDA:
				if (cond == 5)
				{
					qs.setCond(6);
				}
				break;
			
			case MAKYSHA:
				if (cond == 6)
				{
					qs.getPlayer().setVar("MarkywaKilled", "true", -1);
					checkVars(qs, qs.getPlayer());
				}
				break;
			
			case HORNAPI:
				if (cond == 6)
				{
					qs.getPlayer().setVar("HornapiKilled", "true", -1);
					checkVars(qs, qs.getPlayer());
				}
				break;
			
			case YONTYMAK:
				if (cond == 7)
				{
					qs.setCond(8);
				}
				break;
			
			case FRON:
				if (cond == 8)
				{
					qs.setCond(9);
				}
				break;
		}
		
		return null;
	}
	
	private static void checkVars(QuestState qs, Player player)
	{
		if (player == null)
		{
			return;
		}
		if ((player.getVar("MarkywaKilled") != null) && (player.getVar("HornapiKilled") != null))
		{
			qs.setCond(7);
		}
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