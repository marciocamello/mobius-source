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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author KilRoy
 * @name 10381 - To The Seed Of HellFire
 * @category One quest. Single
 */
public class Q10381_ToTheSeedOfHellfire extends Quest implements ScriptFile
{
	// Npcs
	private final static int KEUCEREUS = 32548;
	private final static int SIZRAK = 33669;
	private final static int KBALDIR = 32733;
	
	public Q10381_ToTheSeedOfHellfire()
	{
		super(false);
		addStartNpc(KEUCEREUS);
		addTalkId(KBALDIR, SIZRAK);
		addLevelCheck(97, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accpted":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "kserth_q10381_03.htm";
				break;
			
			case "quest_next":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "kbarldire_q10381_03.htm";
				break;
			
			case "quest_done":
				qs.giveItems(ADENA_ID, 3256740);
				qs.addExpAndSp(951127800, 435041400);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				htmltext = "sofa_sizraku_q10381_03.htm";
				break;
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
			case KEUCEREUS:
				if (qs.isCompleted())
				{
					htmltext = "kserth_q10381_05.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "kserth_q10381_01.htm";
				}
				else if (cond > 0)
				{
					htmltext = "kserth_q10381_06.htm";
				}
				else
				{
					htmltext = "kserth_q10381_04.htm";
				}
				break;
			
			case KBALDIR:
				if (cond == 2)
				{
					htmltext = "kbarldire_q10381_04.htm";
				}
				else if (cond > 0)
				{
					htmltext = "kbarldire_q10381_01.htm";
				}
				break;
			
			case SIZRAK:
				if (cond == 2)
				{
					htmltext = "sofa_sizraku_q10381_01.htm";
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