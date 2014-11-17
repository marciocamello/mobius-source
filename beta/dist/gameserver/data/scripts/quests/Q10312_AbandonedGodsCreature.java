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

public class Q10312_AbandonedGodsCreature extends Quest implements ScriptFile
{
	// Npc
	private static final int HORPINA = 33031;
	// Items
	private static final int GIANTS_WARSMITH_HOLDER = 19305;
	private static final int GIANTS_REORIN_MOLD = 19306;
	private static final int GIANTS_ARCSMITH_ANVIL = 19307;
	private static final int GIANTS_WARSMITH_MOLD = 19308;
	private static final int ENCHANT_ARMOR_AR = 17527;
	private static final int HARDENER_POUNCH_R = 34861;
	// Monster
	private static final int APHROS = 25866;
	
	public Q10312_AbandonedGodsCreature()
	{
		super(PARTY_ALL);
		addStartNpc(HORPINA);
		addKillId(APHROS);
		addKillNpcWithLog(1, "Arphos", 1, APHROS);
		addQuestCompletedCheck(Q10310_TwistedCreationTree.class);
		addLevelCheck(90, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (qs == null)
		{
			return "noquest";
		}
		
		switch (event)
		{
			case "33031-06.htm":
			{
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			}
			case "33031-10.htm":
			{
				qs.addExpAndSp(46847289, 20739487);
				qs.giveItems(GIANTS_WARSMITH_HOLDER, 1);
				qs.giveItems(GIANTS_REORIN_MOLD, 1);
				qs.giveItems(GIANTS_ARCSMITH_ANVIL, 1);
				qs.giveItems(GIANTS_WARSMITH_MOLD, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
			}
			case "33031-11.htm":
			{
				qs.addExpAndSp(46847289, 20739487);
				qs.giveItems(ENCHANT_ARMOR_AR, 2);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
			}
			case "33031-12.htm":
			{
				qs.addExpAndSp(46847289, 20739487);
				qs.giveItems(HARDENER_POUNCH_R, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
			}
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
		final QuestState previous = player.getQuestState(Q10310_TwistedCreationTree.class);
		
		if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 90))
		{
			qs.exitCurrentQuest(true);
			return "33031-03.htm";
		}
		
		switch (qs.getState())
		{
			case COMPLETED:
				htmltext = "33031-02.htm";
				break;
			
			case CREATED:
				htmltext = "33031-01.htm";
				break;
			
			case STARTED:
				if (qs.getCond() == 1)
				{
					htmltext = "33031-07.htm";
				}
				else
				{
					if (qs.getCond() != 2)
					{
						break;
					}
					
					htmltext = "33031-09.htm";
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
		
		if (updateKill(npc, qs))
		{
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
			qs.unset("Arphos");
		}
		
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10312_AbandonedGodsCreature.class);
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
