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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00140_ShadowFoxPart2 extends Quest implements ScriptFile
{
	// Npcs
	private final static int KLUCK = 30895;
	private final static int XENOVIA = 30912;
	// Items
	private final static int CRYSTAL = 10347;
	private final static int OXYDE = 10348;
	private final static int CRYPT = 10349;
	// Monsters
	private final static int Crokian = 20789;
	private final static int Dailaon = 20790;
	private final static int CrokianWarrior = 20791;
	private final static int Farhite = 20792;
	
	public Q00140_ShadowFoxPart2()
	{
		super(false);
		addFirstTalkId(KLUCK);
		addTalkId(KLUCK, XENOVIA);
		addQuestItem(CRYSTAL, OXYDE, CRYPT);
		addKillId(Crokian, Dailaon, CrokianWarrior, Farhite);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30895-02.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30895-05.htm":
				qs.setCond(2);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30895-09.htm":
				qs.playSound(SOUND_FINISH);
				qs.giveItems(ADENA_ID, 18775);
				Quest q = QuestManager.getQuest(Q00141_ShadowFoxPart3.class);
				if (q != null)
				{
					q.newQuestState(qs.getPlayer(), STARTED);
				}
				qs.exitCurrentQuest(false);
				break;
			
			case "30912-07.htm":
				qs.setCond(3);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30912-09.htm":
				qs.takeItems(CRYSTAL, 5);
				if (Rnd.chance(60))
				{
					qs.giveItems(OXYDE, 1);
					
					if (qs.getQuestItemsCount(OXYDE) >= 3)
					{
						htmltext = "30912-09b.htm";
						qs.setCond(4);
						qs.setState(STARTED);
						qs.playSound(SOUND_MIDDLE);
						qs.takeItems(CRYSTAL, -1);
						qs.takeItems(OXYDE, -1);
						qs.giveItems(CRYPT, 1);
					}
				}
				else
				{
					htmltext = "30912-09a.htm";
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
			case KLUCK:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() >= 37)
						{
							htmltext = "30895-01.htm";
						}
						else
						{
							htmltext = "30895-00.htm";
						}
						break;
					
					case 1:
						htmltext = "30895-02.htm";
						break;
					
					case 2:
					case 3:
						htmltext = "30895-06.htm";
						break;
					
					case 4:
						if (qs.getInt("talk") == 1)
						{
							htmltext = "30895-08.htm";
						}
						else
						{
							htmltext = "30895-07.htm";
							qs.takeItems(CRYPT, -1);
							qs.set("talk", "1");
						}
						break;
				}
				break;
			
			case XENOVIA:
				switch (cond)
				{
					case 2:
						htmltext = "30912-01.htm";
						break;
					
					case 3:
						if (qs.getQuestItemsCount(CRYSTAL) >= 5)
						{
							htmltext = "30912-08.htm";
						}
						else
						{
							htmltext = "30912-07.htm";
						}
						break;
					
					case 4:
						htmltext = "30912-10.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(Q00139_ShadowFoxPart1.class);
		
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		
		return "";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 3)
		{
			qs.rollAndGive(CRYSTAL, 1, 80 * npc.getTemplate().rateHp);
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
