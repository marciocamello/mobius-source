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
import services.SupportMagic;

public class Q10353_CertificationOfValue extends Quest implements ScriptFile
{
	// Npcs
	private static final int LILEJ = 33155;
	private static final int KUORI = 33358;
	// Other
	private static final String A_LIST = "a_list";
	
	public Q10353_CertificationOfValue()
	{
		super(true);
		addStartNpc(LILEJ);
		addTalkId(KUORI);
		addLevelCheck(48, 100);
		addKillNpcWithLog(2, A_LIST, 10, 23044, 23045, 23046, 23047, 23048, 23049, 23050, 23051, 23052, 23053, 23054, 23055, 23056, 23057, 23058, 23059, 23060, 23061, 23062, 23063, 23064, 23065, 23066, 23067, 23068, 23102, 23103, 23104, 23105, 23106, 23107, 23108, 23109, 23110, 23111, 23112);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "SupportPlayer":
				SupportMagic.getSupportMagic(npc, player);
				return "33155-6.htm";
				
			case "SupportPet":
				SupportMagic.getSupportServitorMagic(npc, player);
				return "33155-6.htm";
				
			case "Goto":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				player.teleToLocation(119656, 16072, -5120);
				return null;
				
			case "33358-3.htm":
				qs.setCond(2);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		
		if (player.getLevel() < 48)
		{
			return "33155-lvl.htm";
		}
		
		if (qs.getState() == COMPLETED)
		{
			return "33155-comp.htm";
		}
		
		switch (npc.getId())
		{
			case LILEJ:
				if (cond == 0)
				{
					return "33155.htm";
				}
				else if (cond == 1)
				{
					return "33155-11.htm";
				}
				break;
			
			case KUORI:
				if (cond == 1)
				{
					return "33358.htm";
				}
				else if (cond == 2)
				{
					return "33358-5.htm";
				}
				else if (cond == 3)
				{
					qs.addExpAndSp(3000000, 2500000);
					qs.giveItems(17624, 1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
					return "33358-6.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 2) && updateKill(npc, qs))
		{
			qs.unset(A_LIST);
			qs.setCond(3);
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