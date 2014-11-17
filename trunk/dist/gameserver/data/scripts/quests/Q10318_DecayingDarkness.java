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

public class Q10318_DecayingDarkness extends Quest implements ScriptFile
{
	// Npc
	private static final int LYDIA = 32892;
	// Item
	private static final int ITEM_CURSE_RESIDUE = 17733;
	// Monsters
	private static final int[] MOB_ANCIENT_HEROES =
	{
		18978,
		18979,
		18980,
		18981,
		18982,
		18983
	};
	
	public Q10318_DecayingDarkness()
	{
		super(PARTY_ONE);
		addStartNpc(LYDIA);
		addKillId(MOB_ANCIENT_HEROES);
		addQuestItem(ITEM_CURSE_RESIDUE);
		addLevelCheck(95, 99);
		addQuestCompletedCheck(Q10317_OrbisWitch.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (qs == null)
		{
			return "noquest";
		}
		
		if (event.equals("32892-07.htm"))
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
		final QuestState previous = player.getQuestState(Q10317_OrbisWitch.class);
		
		if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 95))
		{
			qs.exitCurrentQuest(true);
			return "32892-02.htm";
		}
		
		switch (qs.getState())
		{
			case COMPLETED:
				htmltext = "32892-03.htm";
				break;
			
			case CREATED:
				htmltext = "32892-01.htm";
				break;
			
			case STARTED:
				if (qs.getCond() == 1)
				{
					if (qs.getQuestItemsCount(ITEM_CURSE_RESIDUE) != 0)
					{
						htmltext = "32892-08.htm";
					}
					else
					{
						htmltext = "32892-09.htm";
					}
				}
				else
				{
					if ((qs.getCond() != 2) || (qs.getQuestItemsCount(ITEM_CURSE_RESIDUE) < 8))
					{
						break;
					}
					
					htmltext = "32892-10.htm";
					qs.addExpAndSp(79260650, 36253450);
					qs.giveItems(ADENA_ID, 5427900, true);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && (Util.contains(MOB_ANCIENT_HEROES, npc.getId())))
		{
			if (qs.rollAndGive(ITEM_CURSE_RESIDUE, 1, 1, 8, 100))
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
			}
		}
		
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10318_DecayingDarkness.class);
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
