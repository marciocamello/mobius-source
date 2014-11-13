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
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author blacksmoke
 */
public class Q10740_NeverForget extends Quest implements ScriptFile
{
	private static final int Sivanthe = 33951;
	private static final int RemembranceTower = 33989;
	
	private static final int UnnamedRelics = 39526;
	
	private static final int KeenFloato = 23449;
	private static final int Ratel = 23450;
	private static final int RobustRatel = 23451;
	private int Relics;
	
	public Q10740_NeverForget()
	{
		super(false);
		addStartNpc(Sivanthe);
		addTalkId(Sivanthe, RemembranceTower);
		addQuestItem(UnnamedRelics);
		addKillId(KeenFloato, Ratel, RobustRatel);
		addLevelCheck(8, 20);
		addClassCheck(182, 183);
		// addQuestCompletedCheck(Q10739_SupplyAndDemand.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "33951-3.htm";
				break;
			case "quest_cont":
				qs.takeItems(UnnamedRelics, 20);
				qs.setCond(3);
				htmltext = "33989-2.htm";
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		
		switch (npcId)
		{
			case Sivanthe:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							htmltext = "33951-1.htm";
						}
						break;
					case 1:
						htmltext = "33951-4.htm";
						break;
					case 2:
						htmltext = "33951-5.htm";
						break;
					case 3:
						htmltext = "33951-6.htm";
						qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.CHECK_YOUR_EQUIPMENT_IN_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.giveItems(57, 1600);
						qs.giveItems(875, 1); // Ring of Knowledge
						qs.giveItems(875, 1); // Ring of Knowledge
						qs.giveItems(1060, 100); // 100x Healing Potion
						qs.getPlayer().addExpAndSp(16851, 0);
						qs.exitCurrentQuest(false);
						qs.playSound(SOUND_FINISH);
						break;
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
			case RemembranceTower:
				switch (cond)
				{
					case 1:
						htmltext = "FIND HTML";
						break;
					case 2:
						htmltext = "33989-1.htm";
						break;
					case 3:
						htmltext = "33989-3.htm";
						break;
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		if (cond == 1)
		{
			if ((npcId == KeenFloato) || (npcId == Ratel) || (npcId == RobustRatel))
			{
				qs.giveItems(UnnamedRelics, 1);
				qs.playSound(SOUND_ITEMGET);
				Relics++;
				if (Relics >= 20)
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
					Relics = 0;
				}
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
