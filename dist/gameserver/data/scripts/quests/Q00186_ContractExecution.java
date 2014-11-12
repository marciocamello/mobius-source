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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00186_ContractExecution extends Quest implements ScriptFile
{
	// Npcs
	private static final int Luka = 31437;
	private static final int Lorain = 30673;
	private static final int Nikola = 30621;
	// Items
	private static final int Certificate = 10362;
	private static final int MetalReport = 10366;
	private static final int Accessory = 10367;
	// Monsters
	private static final int LetoLizardman = 20577;
	private static final int LetoLizardmanArcher = 20578;
	private static final int LetoLizardmanSoldier = 20579;
	private static final int LetoLizardmanWarrior = 20580;
	private static final int LetoLizardmanShaman = 20581;
	private static final int LetoLizardmanOverlord = 20582;
	private static final int TimakOrc = 20583;
	
	public Q00186_ContractExecution()
	{
		super(false);
		addTalkId(Luka, Nikola, Lorain);
		addFirstTalkId(Lorain);
		addKillId(LetoLizardman, LetoLizardmanArcher, LetoLizardmanSoldier, LetoLizardmanWarrior, LetoLizardmanShaman, LetoLizardmanOverlord, TimakOrc);
		addQuestItem(Certificate, MetalReport, Accessory);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "researcher_lorain_q0186_03.htm":
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				qs.takeItems(Certificate, -1);
				qs.giveItems(MetalReport, 1);
				break;
			
			case "maestro_nikola_q0186_03.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "blueprint_seller_luka_q0186_06.htm":
				qs.giveItems(ADENA_ID, 137920);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
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
		
		if (qs.getState() == STARTED)
		{
			switch (npcId)
			{
				case Lorain:
					if (cond == 0)
					{
						if (qs.getPlayer().getLevel() < 41)
						{
							htmltext = "researcher_lorain_q0186_02.htm";
						}
						else
						{
							htmltext = "researcher_lorain_q0186_01.htm";
						}
					}
					else if (cond == 1)
					{
						htmltext = "researcher_lorain_q0186_04.htm";
					}
					break;
				
				case Nikola:
					if (cond == 1)
					{
						htmltext = "maestro_nikola_q0186_01.htm";
					}
					else if (cond == 2)
					{
						htmltext = "maestro_nikola_q0186_04.htm";
					}
					break;
				
				case Luka:
					if (qs.getQuestItemsCount(Accessory) <= 0)
					{
						htmltext = "blueprint_seller_luka_q0186_01.htm";
					}
					else
					{
						htmltext = "blueprint_seller_luka_q0186_02.htm";
					}
					break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getState() == STARTED) && (qs.getQuestItemsCount(Accessory) <= 0) && (qs.getCond() == 2) && (Rnd.get(5) == 0))
		{
			qs.playSound(SOUND_MIDDLE);
			qs.giveItems(Accessory, 1);
		}
		
		return null;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(Q00184_ArtOfPersuasion.class);
		
		if ((qs != null) && qs.isCompleted() && (player.getQuestState(getClass()) == null))
		{
			newQuestState(player, STARTED);
		}
		
		return "";
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
