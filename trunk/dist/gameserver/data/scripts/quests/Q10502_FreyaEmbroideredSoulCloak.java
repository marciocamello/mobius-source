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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10502_FreyaEmbroideredSoulCloak extends Quest implements ScriptFile
{
	// Npc
	private static final int OLF_ADAMS = 32612;
	// Mpnsters
	private static final int FREYA_NORMAL = 29179;
	private static final int FREYA_HARD = 29180;
	// Items
	private static final int SOUL_FREYA = 21723;
	private static final int CLOAK_FREYA = 21720;
	
	public Q10502_FreyaEmbroideredSoulCloak()
	{
		super(PARTY_ALL);
		addStartNpc(OLF_ADAMS);
		addTalkId(OLF_ADAMS);
		addKillId(FREYA_NORMAL, FREYA_HARD);
		addQuestItem(SOUL_FREYA);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("olf_adams_q10502_02.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 82)
				{
					htmltext = "olf_adams_q10502_01.htm";
				}
				else
				{
					htmltext = "olf_adams_q10502_00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "olf_adams_q10502_03.htm";
				break;
			
			case 2:
				if (qs.getQuestItemsCount(SOUL_FREYA) < 20)
				{
					qs.setCond(1);
					htmltext = "olf_adams_q10502_03.htm";
				}
				else
				{
					qs.takeItems(SOUL_FREYA, -1);
					qs.giveItems(CLOAK_FREYA, 1);
					qs.playSound(SOUND_FINISH);
					htmltext = "olf_adams_q10502_04.htm";
					qs.exitCurrentQuest(false);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			if (qs.getQuestItemsCount(SOUL_FREYA) < 20)
			{
				qs.giveItems(SOUL_FREYA, Rnd.get(1, 3), false);
			}
			
			if (qs.getQuestItemsCount(SOUL_FREYA) >= 20)
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
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
