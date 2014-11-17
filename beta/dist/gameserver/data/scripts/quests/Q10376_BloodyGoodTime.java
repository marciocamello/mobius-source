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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10376_BloodyGoodTime extends Quest implements ScriptFile
{
	private static final int NPC_ZENYA = 32140;
	private static final int NPC_CASCA = 32139;
	private static final int NPC_AGNES = 31588;
	private static final int NPC_ANDREI = 31292;
	private static final int MOB_BLOODY_VEIN = 27481;
	private static final int REWARD_MAGIC_RUNE_CLIP = 32700;
	private static final String _bloodyVein = "NightmareDeath";
	private static final Map<Integer, Integer> spawns = new HashMap<>();
	
	public Q10376_BloodyGoodTime()
	{
		super(false);
		addStartNpc(NPC_ZENYA);
		addTalkId(NPC_CASCA, NPC_AGNES, NPC_ANDREI);
		addKillNpcWithLog(3, _bloodyVein, 1, MOB_BLOODY_VEIN);
		addLevelCheck(80, 99);
		addQuestCompletedCheck(Q10375_SuccubusDisciples.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "32140-06.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32139-03.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "enterInstance":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				NpcInstance BloodyVein = qs.addSpawn(MOB_BLOODY_VEIN, qs.getPlayer().getX() + 50, qs.getPlayer().getY() + 50, qs.getPlayer().getZ(), 0, 0, 180000);
				spawns.put(qs.getPlayer().getObjectId(), BloodyVein.getObjectId());
				BloodyVein.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, qs.getPlayer(), 100000);
				return "";
				
			case "32139-08.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "teleport_goddard":
				qs.getPlayer().teleToLocation(149597, -57249, -2976);
				return "";
				
			case "31588-03.htm":
				qs.setCond(6);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "31292-03.htm":
				qs.addExpAndSp(121297500, 48433200);
				qs.giveItems(REWARD_MAGIC_RUNE_CLIP, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (npc.getId())
		{
			case NPC_ZENYA:
				switch (qs.getState())
				{
					case COMPLETED:
						htmltext = "32140-05.htm";
						break;
					
					case CREATED:
						if (qs.getPlayer().getLevel() >= 80)
						{
							final QuestState state = qs.getPlayer().getQuestState(Q10375_SuccubusDisciples.class);
							
							if ((qs.getPlayer().getClassId().level() == 4) && (state != null) && state.isCompleted())
							{
								htmltext = "32140-01.htm";
							}
							else
							{
								htmltext = "32140-03.htm";
								qs.exitCurrentQuest(true);
							}
						}
						else
						{
							htmltext = "32140-04.htm";
						}
						break;
					
					case STARTED:
						htmltext = "32140-07.htm";
						break;
				}
				break;
			
			case NPC_CASCA:
				if (qs.isStarted())
				{
					switch (qs.getCond())
					{
						case 1:
							htmltext = "32139-02.htm";
							break;
						
						case 2:
						case 3:
							htmltext = "32139-03.htm";
							Integer obj_id = spawns.get(qs.getPlayer().getObjectId());
							NpcInstance mob = obj_id != null ? GameObjectsStorage.getNpc(obj_id) : null;
							
							if ((mob == null) || mob.isDead())
							{
								htmltext = "32139-03.htm";
							}
							else
							{
								htmltext = "noquest";
							}
							break;
						
						case 4:
							htmltext = "32139-04.htm";
							break;
						
						case 5:
							htmltext = "32139-08.htm";
							break;
					}
				}
				break;
			
			case NPC_AGNES:
				if (qs.isStarted())
				{
					if (qs.getCond() == 5)
					{
						htmltext = "31588-01.htm";
					}
					else if (qs.getCond() == 6)
					{
						htmltext = "31588-03.htm";
					}
				}
				break;
			
			case NPC_ANDREI:
				if (qs.isStarted() && (qs.getCond() == 6))
				{
					htmltext = "31292-01.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 3) && updateKill(npc, qs))
		{
			qs.unset(_bloodyVein);
			qs.setCond(4);
			qs.playSound(SOUND_MIDDLE);
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
