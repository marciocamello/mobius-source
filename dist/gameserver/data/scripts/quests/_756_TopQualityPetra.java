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
 * @author KilRoy & Mangol
 * @name 756 - Top Quality Petra
 * @category Daily quest. Party
 */
public class _756_TopQualityPetra extends Quest implements ScriptFile
{
	private final int AKU_MARK = 34910;
	private final int TOP_QUALITY_PETRA = 35703;
	private final int AKU = 33671;
	
	public _756_TopQualityPetra()
	{
		super(false);
		addTalkId(AKU);
		addQuestItem(TOP_QUALITY_PETRA);
		addLevelCheck(97, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("sofa_aku_q0756_02.htm"))
		{
			st.takeAllItems(TOP_QUALITY_PETRA);
			st.getPlayer().addExpAndSp(570676680, 261024840);
			st.giveItems(AKU_MARK, 1);
			st.exitCurrentQuest(this);
			st.playSound(SOUND_FINISH);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		
		if (npcId == AKU)
		{
			if (st.isCreated() && (cond == 1))
			{
				htmltext = "sofa_aku_q0756_01.htm";
			}
			else
			{
				htmltext = "sofa_aku_q0756_03.htm";
			}
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