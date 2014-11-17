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

public class Q10310_TwistedCreationTree extends Quest implements ScriptFile
{
	// Npcs
	private static final int SELINA = 33032;
	private static final int HORPINA = 33031;
	// Monsters
	private static final int GARDEN_SENTRY = 22947;
	private static final int GARDEN_SCOUT = 22948;
	private static final int GARDEN_COMMANDER = 22949;
	private static final int OUTDOOR_GARDENER = 22950;
	private static final int GARDEN_DESTROYER = 22951;
	
	public Q10310_TwistedCreationTree()
	{
		super(PARTY_ONE);
		addStartNpc(SELINA);
		addTalkId(HORPINA);
		addKillNpcWithLog(2, "Sentry", 10, GARDEN_SENTRY);
		addKillNpcWithLog(2, "Scount", 10, GARDEN_SCOUT);
		addKillNpcWithLog(2, "Commander", 10, GARDEN_COMMANDER);
		addKillNpcWithLog(2, "Gardener", 10, OUTDOOR_GARDENER);
		addKillNpcWithLog(2, "Destroyer", 10, GARDEN_DESTROYER);
		addLevelCheck(90, 99);
		addQuestCompletedCheck(Q10302_UnsettlingShadowAndRumors.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmlText = "noquest";
		if (qs == null)
		{
			return htmlText;
		}
		
		switch (event)
		{
			case "33032-06.htm":
			{
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			}
			case "33031-03.htm":
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			}
			case "33031-05.htm":
			{
				qs.addExpAndSp(50178765, 21980595);
				qs.giveItems(57, 3424540, true);
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
		String htmlText = "noquest";
		if (qs == null)
		{
			return htmlText;
		}
		
		final Player player = qs.getPlayer();
		final QuestState previous = player.getQuestState(Q10302_UnsettlingShadowAndRumors.class);
		
		switch (npc.getId())
		{
			case SELINA:
				if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 90))
				{
					qs.exitCurrentQuest(true);
					return "33032-03.htm";
				}
				switch (qs.getState())
				{
					case COMPLETED:
						htmlText = "33032-02.htm";
						break;
					
					case CREATED:
						htmlText = "33032-01.htm";
						break;
					
					case STARTED:
						htmlText = "33032-07.htm";
				}
				break;
			
			case HORPINA:
				switch (qs.getState())
				{
					case COMPLETED:
						htmlText = "completed";
						break;
					
					case STARTED:
						if (qs.getCond() == 1)
						{
							htmlText = "33031-01.htm";
						}
						else if (qs.getCond() == 2)
						{
							htmlText = "33031-03.htm";
						}
						else
						{
							if (qs.getCond() != 3)
							{
								break;
							}
							
							htmlText = "33031-04.htm";
						}
				}
				break;
		}
		
		return htmlText;
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
			qs.setCond(3);
			qs.playSound(SOUND_MIDDLE);
			qs.unset("Sentry");
			qs.unset("Scount");
			qs.unset("Commander");
			qs.unset("Gardener");
			qs.unset("Destroyer");
		}
		
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10310_TwistedCreationTree.class);
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
