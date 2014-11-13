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

public class Q00142_FallenAngelRequestOfDawn extends Quest implements ScriptFile
{
	// Npcs
	private final static int NATOOLS = 30894;
	private final static int RAYMOND = 30289;
	private final static int CASIAN = 30612;
	private final static int ROCK = 32368;
	private final static int NpcAngel = 32369;
	// Items
	private final static int CRYPT = 10351;
	private final static int FRAGMENT = 10352;
	private final static int BLOOD = 10353;
	// Monsters
	private final static int Ant = 20079;
	private final static int AntCaptain = 20080;
	private final static int AntOverseer = 20081;
	private final static int AntRecruit = 20082;
	private final static int AntPatrol = 20084;
	private final static int AntGuard = 20086;
	private final static int AntSoldier = 20087;
	private final static int AntWarriorCaptain = 20088;
	private final static int NobleAnt = 20089;
	private final static int NobleAntLeader = 20090;
	private final static int FallenAngel = 27338;
	
	public Q00142_FallenAngelRequestOfDawn()
	{
		super(false);
		addTalkId(NATOOLS, RAYMOND, CASIAN, ROCK);
		addQuestItem(CRYPT, FRAGMENT, BLOOD);
		addKillId(Ant, AntCaptain, AntOverseer, AntRecruit, AntPatrol, AntGuard, AntSoldier, AntWarriorCaptain, NobleAnt, NobleAntLeader, FallenAngel);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "start":
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				qs.setState(STARTED);
				htmltext = "stained_rock_q0142_03.htm";
				break;
			
			case "warehouse_chief_natools_q0142_10.htm":
				qs.setCond(2);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(CRYPT, 1);
				break;
			
			case "bishop_raimund_q0142_05.htm":
				qs.setCond(3);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "sage_kasian_q0142_10.htm":
				qs.setCond(4);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "stained_rock_q0142_05.htm":
				if (GameObjectsStorage.getByNpcId(NpcAngel) != null)
				{
					htmltext = "stained_rock_q0142_03.htm";
				}
				else if (GameObjectsStorage.getByNpcId(FallenAngel) != null)
				{
					htmltext = "stained_rock_q0142_04.htm";
				}
				else
				{
					qs.addSpawn(FallenAngel, 180000);
					qs.playSound(SOUND_MIDDLE);
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
					htmltext = "warehouse_chief_natools_q0142_07.htm";
				}
				else if (cond == 2)
				{
					htmltext = "warehouse_chief_natools_q0142_11.htm";
				}
				break;
			
			case RAYMOND:
				if (cond == 2)
				{
					if (qs.getInt("talk") == 1)
					{
						htmltext = "bishop_raimund_q0142_02a.htm";
					}
					else
					{
						htmltext = "bishop_raimund_q0142_02.htm";
						qs.takeItems(CRYPT, -1);
						qs.set("talk", "1");
					}
				}
				else if (cond == 3)
				{
					htmltext = "bishop_raimund_q0142_06.htm";
				}
				else if (cond == 6)
				{
					htmltext = "bishop_raimund_q0142_07.htm";
					qs.playSound(SOUND_FINISH);
					qs.giveItems(ADENA_ID, 92676);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case CASIAN:
				if (cond == 3)
				{
					htmltext = "sage_kasian_q0142_02.htm";
				}
				else if (cond == 4)
				{
					htmltext = "sage_kasian_q0142_10.htm";
				}
				break;
			
			case ROCK:
				if (cond <= 4)
				{
					htmltext = "stained_rock_q0142_01.htm";
				}
				else if (cond == 5)
				{
					htmltext = "stained_rock_q0142_02.htm";
					
					if (qs.getInt("talk") != 1)
					{
						qs.takeItems(BLOOD, -1);
						qs.set("talk", "1");
					}
				}
				else
				{
					htmltext = "stained_rock_q0142_06.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if (npc.getId() == FallenAngel)
		{
			if (cond == 5)
			{
				qs.setCond(6);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(BLOOD, 1);
			}
		}
		else if ((cond == 4) && (qs.getQuestItemsCount(FRAGMENT) < 30))
		{
			qs.rollAndGive(FRAGMENT, 1, 1, 30, 20);
			
			if (qs.getQuestItemsCount(FRAGMENT) >= 30)
			{
				qs.setCond(5);
				qs.setState(STARTED);
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
