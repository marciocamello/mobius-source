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

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00193_SevenSignsDyingMessage extends Quest implements ScriptFile
{
	// Npcs
	private static final int Hollint = 30191;
	private static final int Cain = 32569;
	private static final int Eric = 32570;
	private static final int SirGustavAthebaldt = 30760;
	// Monster
	private static final int ShilensEvilThoughts = 27343;
	// Items
	private static final int JacobsNecklace = 13814;
	private static final int DeadmansHerb = 13813;
	private static final int SculptureofDoubt = 14352;
	// Other
	private static final Map<Integer, Integer> spawns = new HashMap<>();
	
	public Q00193_SevenSignsDyingMessage()
	{
		super(false);
		addStartNpc(Hollint);
		addTalkId(Cain, Eric, SirGustavAthebaldt);
		addKillId(ShilensEvilThoughts);
		addQuestItem(JacobsNecklace, DeadmansHerb, SculptureofDoubt);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "30191-02.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.giveItems(JacobsNecklace, 1);
				break;
			
			case "32569-05.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32570-02.htm":
				qs.setCond(3);
				qs.giveItems(DeadmansHerb, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30760-02.htm":
				if (player.getBaseClassId() == player.getActiveClassId())
				{
					qs.addExpAndSp(10000000, 2500000);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				else
				{
					return "subclass_forbidden.htm";
				}
				break;
			
			case "close_your_eyes":
				qs.setCond(4);
				qs.takeItems(DeadmansHerb, -1);
				qs.playSound(SOUND_MIDDLE);
				player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ_DYING_MASSAGE);
				return "";
				
			case "32569-09.htm":
				htmltext = "32569-09.htm";
				Functions.npcSay(npc, qs.getPlayer().getName() + "! That stranger must be defeated. Here is the ultimate help!");
				NpcInstance mob = qs.addSpawn(ShilensEvilThoughts, 82425, 47232, -3216, 0, 0, 180000);
				spawns.put(player.getObjectId(), mob.getObjectId());
				mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 100000);
				break;
			
			case "32569-13.htm":
				qs.setCond(6);
				qs.takeItems(SculptureofDoubt, -1);
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final int id = qs.getState();
		final Player player = qs.getPlayer();
		
		switch (npcId)
		{
			case Hollint:
				if (id == CREATED)
				{
					if (player.getLevel() < 79)
					{
						qs.exitCurrentQuest(true);
						return "30191-00.htm";
					}
					
					final QuestState state = player.getQuestState(Q00192_SevenSignsSeriesOfDoubt.class);
					
					if ((state == null) || !state.isCompleted())
					{
						qs.exitCurrentQuest(true);
						return "noquest";
					}
					
					return "30191-01.htm";
				}
				else if (cond == 1)
				{
					return "30191-03.htm";
				}
				break;
			
			case Cain:
				switch (cond)
				{
					case 1:
						return "32569-01.htm";
						
					case 2:
						return "32569-06.htm";
						
					case 3:
						return "32569-07.htm";
						
					case 4:
						Integer obj_id = spawns.get(player.getObjectId());
						NpcInstance mob = obj_id != null ? GameObjectsStorage.getNpc(obj_id) : null;
						if ((mob == null) || mob.isDead())
						{
							return "32569-08.htm";
						}
						return "32569-09.htm";
						
					case 5:
						return "32569-10.htm";
						
					case 6:
						return "32569-13.htm";
				}
				break;
			
			case Eric:
				if (cond == 2)
				{
					return "32570-01.htm";
				}
				else if (cond == 3)
				{
					return "32570-03.htm";
				}
				break;
			
			case SirGustavAthebaldt:
				if (cond == 6)
				{
					return "30760-01.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if (qs.getCond() == 4)
		{
			Integer obj_id = spawns.get(player.getObjectId());
			
			if ((obj_id != null) && (obj_id.intValue() == npc.getObjectId()))
			{
				spawns.remove(player.getObjectId());
				qs.setCond(5);
				qs.playSound(SOUND_ITEMGET);
				qs.giveItems(SculptureofDoubt, 1);
			}
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
