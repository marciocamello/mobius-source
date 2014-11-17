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

public class Q10501_ZakenEmbroideredSoulCloak extends Quest implements ScriptFile
{
	// Npc
	private static final int OLF_ADAMS = 32612;
	// Monster
	private static final int ZAKEN_HIGH = 29181;
	// Items
	private static final int SOUL_ZAKEN = 21722;
	private static final int CLOAK_OF_ZAKEN = 21719;
	
	public Q10501_ZakenEmbroideredSoulCloak()
	{
		super(PARTY_ALL);
		addStartNpc(OLF_ADAMS);
		addTalkId(OLF_ADAMS);
		addKillId(ZAKEN_HIGH);
		addQuestItem(SOUL_ZAKEN);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("olf_adams_q10501_02.htm"))
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
				if (qs.getPlayer().getLevel() >= 78)
				{
					htmltext = "olf_adams_q10501_01.htm";
				}
				else
				{
					htmltext = "olf_adams_q10501_00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "olf_adams_q10501_03.htm";
				break;
			
			case 2:
				if (qs.getQuestItemsCount(SOUL_ZAKEN) < 20)
				{
					qs.setCond(1);
					htmltext = "olf_adams_q10501_03.htm";
				}
				else
				{
					qs.takeItems(SOUL_ZAKEN, -1);
					qs.giveItems(CLOAK_OF_ZAKEN, 1);
					qs.playSound(SOUND_FINISH);
					htmltext = "olf_adams_q10501_04.htm";
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
			if (qs.getQuestItemsCount(SOUL_ZAKEN) < 20)
			{
				qs.giveItems(SOUL_ZAKEN, Rnd.get(1, 3), false);
			}
			
			if (qs.getQuestItemsCount(SOUL_ZAKEN) >= 20)
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
