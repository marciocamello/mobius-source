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

import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00141_ShadowFoxPart3 extends Quest implements ScriptFile
{
	// Npc
	private final static int NATOOLS = 30894;
	// Item
	private final static int REPORT = 10350;
	// Monsters
	private final static int CrokianWarrior = 20791;
	private final static int Farhite = 20792;
	private final static int Alligator = 20135;
	
	public Q00141_ShadowFoxPart3()
	{
		super(false);
		addFirstTalkId(NATOOLS);
		addTalkId(NATOOLS);
		addQuestItem(REPORT);
		addKillId(CrokianWarrior, Farhite, Alligator);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30894-02.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30894-04.htm":
				qs.setCond(2);
				qs.setState(STARTED);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30894-15.htm":
				qs.setCond(4);
				qs.setState(STARTED);
				qs.unset("talk");
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "30894-18.htm":
				if (qs.getInt("reward") != 1)
				{
					qs.playSound(SOUND_FINISH);
					qs.giveItems(ADENA_ID, 88888);
					qs.set("reward", "1");
					htmltext = "select.htm";
				}
				else
				{
					htmltext = "select.htm";
				}
				break;
			
			case "dawn":
				Quest q1 = QuestManager.getQuest(Q00142_FallenAngelRequestOfDawn.class);
				if (q1 != null)
				{
					qs.exitCurrentQuest(false);
					QuestState qs1 = q1.newQuestState(qs.getPlayer(), STARTED);
					q1.notifyEvent("start", qs1, npc);
					return null;
				}
				break;
			
			case "dusk":
				Quest q2 = QuestManager.getQuest(Q00143_FallenAngelRequestOfDusk.class);
				if (q2 != null)
				{
					qs.exitCurrentQuest(false);
					QuestState qs1 = q2.newQuestState(qs.getPlayer(), STARTED);
					q2.notifyEvent("start", qs1, npc);
					return null;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (cond)
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 37)
				{
					htmltext = "30894-01.htm";
				}
				else
				{
					htmltext = "30894-00.htm";
				}
				break;
			
			case 1:
				htmltext = "30894-02.htm";
				break;
			
			case 2:
				htmltext = "30894-05.htm";
				break;
			
			case 3:
				if (qs.getInt("talk") == 1)
				{
					htmltext = "30894-07.htm";
				}
				else
				{
					htmltext = "30894-06.htm";
					qs.takeItems(REPORT, -1);
					qs.set("talk", "1");
				}
				break;
			
			case 4:
				htmltext = "30894-16.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(Q00140_ShadowFoxPart2.class);
		
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		
		return "";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 2) && qs.rollAndGive(REPORT, 1, 1, 30, 80 * npc.getTemplate().rateHp))
		{
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
