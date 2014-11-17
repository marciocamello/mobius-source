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
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q10286_ReunionWithSirra extends Quest implements ScriptFile
{
	// Npcs
	private static final int Rafforty = 32020;
	private static final int Jinia = 32760;
	private static final int Jinia2 = 32781;
	private static final int Sirra = 32762;
	
	public Q10286_ReunionWithSirra()
	{
		super(false);
		addStartNpc(Rafforty);
		addTalkId(Jinia, Jinia2, Sirra);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "rafforty_q10286_02.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "enterinstance":
				qs.setCond(2);
				enterInstance(qs.getPlayer(), 141);
				return null;
				
			case "sirraspawn":
				qs.setCond(3);
				NpcInstance sirra = qs.getPlayer().getReflection().addSpawnWithoutRespawn(Sirra, new Location(-23848, -8744, -5413, 49152), 0);
				Functions.npcSay(sirra, "You advanced bravely but got such a tiny result. Hohoho.");
				return null;
				
			case "sirra_q10286_04.htm":
				qs.giveItems(15470, 5);
				qs.setCond(4);
				npc.deleteMe();
				break;
			
			case "leaveinstance":
				qs.setCond(5);
				qs.getPlayer().getReflection().collapse();
				return null;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Rafforty:
				switch (cond)
				{
					case 0:
						final QuestState state = qs.getPlayer().getQuestState(Q10285_MeetingSirra.class);
						if ((qs.getPlayer().getLevel() >= 82) && (state != null) && state.isCompleted())
						{
							htmltext = "rafforty_q10286_01.htm";
						}
						else
						{
							htmltext = "rafforty_q10286_00.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 1:
					case 2:
					case 3:
					case 4:
						htmltext = "rafforty_q10286_03.htm";
						break;
				}
				break;
			
			case Jinia:
				if (cond == 2)
				{
					htmltext = "jinia_q10286_01.htm";
				}
				else if (cond == 3)
				{
					htmltext = "jinia_q10286_01a.htm";
				}
				else if (cond == 4)
				{
					htmltext = "jinia_q10286_05.htm";
				}
				break;
			
			case Sirra:
				if (cond == 3)
				{
					htmltext = "sirra_q10286_01.htm";
				}
				break;
			
			case Jinia2:
				if (cond == 5)
				{
					htmltext = "jinia2_q10286_01.htm";
				}
				else if (cond == 6)
				{
					htmltext = "jinia2_q10286_04.htm";
				}
				else if (cond == 7)
				{
					htmltext = "jinia2_q10286_05.htm";
					qs.addExpAndSp(2152200, 181070);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
				}
				break;
		}
		
		return htmltext;
	}
	
	private void enterInstance(Player player, int izId)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(izId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(izId))
		{
			ReflectionUtils.enterReflection(player, izId);
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
