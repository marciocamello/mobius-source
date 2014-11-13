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

public class Q00473_InTheCoralGarden extends Quest implements ScriptFile
{
	// Npc
	public static final int FIOREN = 33044;
	// Monster
	public static final int MICHAEL = 25799;
	// Item
	public static final int PROOF = 30387;
	
	public Q00473_InTheCoralGarden()
	{
		super(true);
		addStartNpc(FIOREN);
		addKillId(MICHAEL);
		addLevelCheck(97, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "33044-3.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "33044-6.htm":
				qs.giveItems(PROOF, 10);
				qs.unset("cond");
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		
		switch (qs.getState())
		{
			case 1:
				if (player.getLevel() < 97)
				{
					return "33044-lvl.htm";
				}
				else if (!qs.isNowAvailable())
				{
					return "33044-comp.htm";
				}
				else if (player.getLevel() < 97)
				{
					return "33044-lvl.htm";
				}
				return "33044.htm";
				
			case 2:
				if (cond == 1)
				{
					return "33044-4.htm";
				}
				else if (cond == 2)
				{
					return "33044-5.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
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