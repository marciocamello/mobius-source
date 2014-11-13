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

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00143_FallenAngelRequestOfDusk extends Quest implements ScriptFile
{
	// Npcs
	private final static int NATOOLS = 30894;
	private final static int TOBIAS = 30297;
	private final static int CASIAN = 30612;
	private final static int ROCK = 32368;
	private final static int ANGEL = 32369;
	// Monster
	private final static int MonsterAngel = 27338;
	// Items
	private final static int SEALED_PATH = 10354;
	private final static int PATH = 10355;
	private final static int EMPTY_CRYSTAL = 10356;
	private final static int MEDICINE = 10357;
	private final static int MESSAGE = 10358;
	
	public Q00143_FallenAngelRequestOfDusk()
	{
		super(false);
		addTalkId(NATOOLS, TOBIAS, CASIAN, ROCK, ANGEL);
		addQuestItem(SEALED_PATH, PATH, EMPTY_CRYSTAL, MEDICINE, MESSAGE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "start":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "warehouse_chief_natools_q0143_01.htm";
				break;
			
			case "warehouse_chief_natools_q0143_04.htm":
				qs.setCond(2);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(SEALED_PATH, 1);
				break;
			
			case "master_tobias_q0143_05.htm":
				qs.setCond(3);
				qs.setState(STARTED);
				qs.unset("talk");
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(PATH, 1);
				qs.giveItems(EMPTY_CRYSTAL, 1);
				break;
			
			case "sage_kasian_q0143_09.htm":
				qs.setCond(4);
				qs.setState(STARTED);
				qs.unset("talk");
				qs.giveItems(MEDICINE, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "stained_rock_q0143_05.htm":
				if (GameObjectsStorage.getByNpcId(MonsterAngel) != null)
				{
					htmltext = "stained_rock_q0143_03.htm";
				}
				else if (GameObjectsStorage.getByNpcId(ANGEL) != null)
				{
					htmltext = "stained_rock_q0143_04.htm";
				}
				else
				{
					qs.addSpawn(ANGEL, 180000);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "q_fallen_angel_npc_q0143_14.htm":
				qs.setCond(5);
				qs.setState(STARTED);
				qs.unset("talk");
				qs.takeItems(EMPTY_CRYSTAL, -1);
				qs.giveItems(MESSAGE, 1);
				qs.playSound(SOUND_MIDDLE);
				NpcInstance n = GameObjectsStorage.getByNpcId(ANGEL);
				if (n != null)
				{
					n.deleteMe();
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case NATOOLS:
				if ((cond == 1) || (qs.isStarted() && (cond == 0)))
				{
					htmltext = "warehouse_chief_natools_q0143_01.htm";
				}
				else if (cond == 2)
				{
					htmltext = "warehouse_chief_natools_q0143_05.htm";
				}
				break;
			
			case TOBIAS:
				if (cond == 2)
				{
					if (qs.getInt("talk") == 1)
					{
						htmltext = "master_tobias_q0143_03.htm";
					}
					else
					{
						htmltext = "master_tobias_q0143_02.htm";
						qs.takeItems(SEALED_PATH, -1);
						qs.set("talk", "1");
					}
				}
				else if (cond == 3)
				{
					htmltext = "master_tobias_q0143_06.htm";
				}
				else if (cond == 5)
				{
					htmltext = "master_tobias_q0143_07.htm";
					qs.playSound(SOUND_FINISH);
					qs.giveItems(ADENA_ID, 89046);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case CASIAN:
				if (cond == 3)
				{
					if (qs.getInt("talk") == 1)
					{
						htmltext = "sage_kasian_q0143_03.htm";
					}
					else
					{
						htmltext = "sage_kasian_q0143_02.htm";
						qs.takeItems(PATH, -1);
						qs.set("talk", "1");
					}
				}
				else if (cond == 4)
				{
					htmltext = "sage_kasian_q0143_09.htm";
				}
				break;
			
			case ROCK:
				if (cond <= 3)
				{
					htmltext = "stained_rock_q0143_01.htm";
				}
				else if (cond == 4)
				{
					htmltext = "stained_rock_q0143_02.htm";
				}
				else
				{
					htmltext = "stained_rock_q0143_06.htm";
				}
				break;
			
			case ANGEL:
				if (cond == 4)
				{
					if (qs.getInt("talk") == 1)
					{
						htmltext = "q_fallen_angel_npc_q0143_04.htm";
					}
					else
					{
						htmltext = "q_fallen_angel_npc_q0143_03.htm";
						qs.takeItems(MEDICINE, -1);
						qs.set("talk", "1");
					}
				}
				else if (cond == 5)
				{
					htmltext = "q_fallen_angel_npc_q0143_14.htm";
				}
				break;
		}
		
		return htmltext;
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
