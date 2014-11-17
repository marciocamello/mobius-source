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

public class Q10271_TheEnvelopingDarkness extends Quest implements ScriptFile
{
	// Npcs
	private static final int Orbyu = 32560;
	private static final int El = 32556;
	private static final int MedibalsCorpse = 32528;
	// Item
	private static final int InspectorMedibalsDocument = 13852;
	// Other
	private static final int CC_MINIMUM = 36;
	
	public Q10271_TheEnvelopingDarkness()
	{
		super(false);
		addStartNpc(Orbyu);
		addTalkId(Orbyu, El, MedibalsCorpse);
		addQuestItem(InspectorMedibalsDocument);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "orbyu_q10271_3.htm":
				if (qs.getCond() == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "el_q10271_2.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "medibalscorpse_q10271_2.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(InspectorMedibalsDocument, 1);
				break;
			
			case "el_q10271_4.htm":
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				qs.takeItems(InspectorMedibalsDocument, -1);
				break;
			
			case "orbyu_q10271_5.htm":
				qs.giveItems(ADENA_ID, 236510);
				qs.addExpAndSp(1109665, 1229015);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		switch (npc.getId())
		{
			case Orbyu:
				if (cond == 0)
				{
					final QuestState ToTheSeedOfDestruction = player.getQuestState(Q10269_ToTheSeedOfDestruction.class);
					
					if ((player.getLevel() >= 75) && (ToTheSeedOfDestruction != null) && ToTheSeedOfDestruction.isCompleted() && (player.getParty() != null) && (player.getParty().getCommandChannel() != null) && (player.getParty().getCommandChannel().getMemberCount() >= CC_MINIMUM))
					{
						htmltext = "orbyu_q10271_1.htm";
					}
					else
					{
						htmltext = "orbyu_q10271_0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 4)
				{
					htmltext = "orbyu_q10271_4.htm";
				}
				break;
			
			case El:
				if (cond == 1)
				{
					htmltext = "el_q10271_1.htm";
				}
				else if ((cond == 3) && (qs.getQuestItemsCount(InspectorMedibalsDocument) >= 1))
				{
					htmltext = "el_q10271_3.htm";
				}
				else if ((cond == 3) && (qs.getQuestItemsCount(InspectorMedibalsDocument) < 1))
				{
					htmltext = "el_q10271_0.htm";
				}
				break;
			
			case MedibalsCorpse:
				if (cond == 2)
				{
					htmltext = "medibalscorpse_q10271_1.htm";
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
