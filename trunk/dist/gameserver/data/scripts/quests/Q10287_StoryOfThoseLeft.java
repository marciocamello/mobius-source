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

import java.util.StringTokenizer;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q10287_StoryOfThoseLeft extends Quest implements ScriptFile
{
	// Npcs
	private static final int Rafforty = 32020;
	private static final int Jinia = 32760;
	private static final int Jinia2 = 32781;
	private static final int Kegor = 32761;
	
	public Q10287_StoryOfThoseLeft()
	{
		super(false);
		addStartNpc(Rafforty);
		addTalkId(Jinia, Jinia2, Kegor);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "rafforty_q10287_02.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "enterinstance":
				qs.setCond(2);
				enterInstance(qs.getPlayer(), 141);
				return null;
				
			case "jinia_q10287_03.htm":
				qs.setCond(3);
				break;
			
			case "kegor_q10287_03.htm":
				qs.setCond(4);
				break;
			
			case "exitinstance":
				qs.setCond(5);
				qs.getPlayer().getReflection().collapse();
				return null;
		}
		
		if (event.startsWith("exgivebook"))
		{
			StringTokenizer str = new StringTokenizer(event);
			str.nextToken();
			int id = Integer.parseInt(str.nextToken());
			htmltext = "rafforty_q10287_05.htm";
			
			switch (id)
			{
				case 1:
					qs.giveItems(10549, 1);
					break;
				
				case 2:
					qs.giveItems(10550, 1);
					break;
				
				case 3:
					qs.giveItems(10551, 1);
					break;
				
				case 4:
					qs.giveItems(10552, 1);
					break;
				
				case 5:
					qs.giveItems(10553, 1);
					break;
				
				case 6:
					qs.giveItems(14219, 1);
					break;
			}
			
			qs.setState(COMPLETED);
			qs.exitCurrentQuest(false);
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
						final QuestState state = qs.getPlayer().getQuestState(Q10286_ReunionWithSirra.class);
						if ((qs.getPlayer().getLevel() >= 82) && (state != null) && state.isCompleted())
						{
							htmltext = "rafforty_q10287_01.htm";
						}
						else
						{
							htmltext = "rafforty_q10287_00.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 1:
					case 2:
					case 3:
					case 4:
						htmltext = "rafforty_q10287_02.htm";
						break;
					
					case 5:
						htmltext = "rafforty_q10287_03.htm";
						break;
					
					default:
						htmltext = "rafforty_q10287_06.htm";
						break;
				}
				break;
			
			case Jinia:
				if (cond == 2)
				{
					htmltext = "jinia_q10287_01.htm";
				}
				else if (cond == 3)
				{
					htmltext = "jinia_q10287_04.htm";
				}
				else if (cond == 4)
				{
					htmltext = "jinia_q10287_05.htm";
				}
				break;
			
			case Kegor:
				if (cond == 3)
				{
					htmltext = "kegor_q10287_01.htm";
				}
				else if ((cond == 2) || (cond == 4))
				{
					htmltext = "kegor_q10287_04.htm";
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
