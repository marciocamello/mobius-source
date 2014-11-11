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

import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00061_LawEnforcement extends Quest implements ScriptFile
{
	// Npcs
	private static final int Liane = 32222;
	private static final int Kekropus = 32138;
	private static final int Eindburgh = 32469;
	
	public Q00061_LawEnforcement()
	{
		super(false);
		addStartNpc(Liane);
		addTalkId(Kekropus, Eindburgh);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "ask":
				if (qs.getPlayer().getRace() != Race.kamael)
				{
					htmltext = "grandmaste_piane_q0061_03.htm";
					qs.exitCurrentQuest(true);
				}
				else if ((qs.getPlayer().getClassId() != ClassId.INSPECTOR) || (qs.getPlayer().getLevel() < 76))
				{
					htmltext = "grandmaste_piane_q0061_02.htm";
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "grandmaste_piane_q0061_04.htm";
				}
				break;
			
			case "accept":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "grandmaste_piane_q0061_05.htm";
				break;
			
			case "kekrops_q0061_09.htm":
				qs.setCond(2);
				break;
			
			case "subelder_aientburg_q0061_08.htm":
			case "subelder_aientburg_q0061_09.htm":
				qs.giveItems(ADENA_ID, 26000);
				qs.getPlayer().setClassId(ClassId.JUDICATOR.ordinal(), false, true);
				qs.getPlayer().broadcastCharInfo();
				qs.getPlayer().broadcastPacket(new MagicSkillUse(qs.getPlayer(), 4339, 1, 6000, 1));
				qs.getPlayer().broadcastPacket(new MagicSkillUse(npc, 4339, 1, 6000, 1));
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Liane:
				if (qs.getState() == CREATED)
				{
					htmltext = "grandmaste_piane_q0061_01.htm";
				}
				else
				{
					htmltext = "grandmaste_piane_q0061_06.htm";
				}
				break;
			
			case Kekropus:
				if (cond == 1)
				{
					htmltext = "kekrops_q0061_01.htm";
				}
				else if (cond == 2)
				{
					htmltext = "kekrops_q0061_10.htm";
				}
				break;
			
			case Eindburgh:
				if (cond == 2)
				{
					htmltext = "subelder_aientburg_q0061_01.htm";
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
