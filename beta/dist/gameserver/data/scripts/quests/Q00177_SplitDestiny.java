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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.SubClassType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExSubjobInfo;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00177_SplitDestiny extends Quest implements ScriptFile
{
	// Npcs
	private static final int Hadel = 33344;
	private static final int Ishuma = 32615;
	// Items
	private static final int questitem_01 = 17718;
	private static final int questitem_02 = 17719;
	private static final int questitem_03 = 17720;
	private static final int questitem_04 = 17721;
	// Monsters
	private static final List<Integer> _Mobs1 = new ArrayList<>();
	private static final List<Integer> _Mobs2 = new ArrayList<>();
	// Other
	private static final int chanceGetItem = 30;
	
	public Q00177_SplitDestiny()
	{
		super(false);
		_Mobs1.clear();
		_Mobs1.add(21547);
		_Mobs1.add(21548);
		_Mobs1.add(21549);
		_Mobs1.add(21550);
		_Mobs1.add(21587);
		_Mobs2.clear();
		_Mobs2.add(22257);
		_Mobs2.add(22258);
		_Mobs2.add(22259);
		_Mobs2.add(22260);
		addStartNpc(Hadel);
		addTalkId(Hadel, Ishuma);
		addQuestItem(questitem_01, questitem_02, questitem_03, questitem_04);
		addKillId(_Mobs1);
		addKillId(_Mobs2);
		addLevelCheck(80, 99);
		addClassLevelCheck(4);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.contains("SoulCrystal"))
		{
			qs.giveItems(event.contains("Red") ? 10480 : event.contains("Blue") ? 10481 : 10482, 1);
			qs.giveItems(18168, 1);
			qs.addExpAndSp(175739575, 2886300);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(false);
			qs.getPlayer().getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			qs.getPlayer().sendPacket(new ExSubjobInfo(qs.getPlayer(), true));
			htmltext = "33344_16.htm";
		}
		
		switch (event)
		{
			case "33344_03.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.set("subClassId", qs.getPlayer().getActiveClassId());
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "33344_07.htm":
				qs.takeAllItems(questitem_03);
				qs.setCond(4);
				break;
			
			case "33344_10.htm":
				qs.giveItems(questitem_03, 10);
				qs.setCond(7);
				break;
			
			case "32615_03.htm":
				qs.takeAllItems(questitem_03);
				qs.takeAllItems(questitem_04);
				qs.setCond(8);
				break;
			
			case "33344_13.htm":
				qs.takeAllItems(questitem_01);
				qs.takeAllItems(questitem_02);
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
		final Player player = qs.getPlayer();
		
		switch (npcId)
		{
			case Hadel:
				if (!(qs.getState() == Quest.COMPLETED))
				{
					if ((player.getActiveSubClass().isSub() && (cond == 0) && !player.getSubClassList().haveDualClass()) || ((cond > 0) && (player.getActiveSubClass().getClassId() == qs.getInt("subClassId"))))
					{
						switch (cond)
						{
							case 0:
								if ((player.getLevel() >= 80) && ClassId.VALUES[player.getSubClassList().getBaseSubClass().getClassId()].isOfLevel(ClassLevel.Fourth) && ClassId.VALUES[player.getActiveSubClass().getClassId()].isOfLevel(ClassLevel.Third))
								{
									htmltext = "33344_01.htm";
								}
								else
								{
									htmltext = "33344_nosubclass.htm";
								}
								break;
							
							case 1:
								htmltext = "33344_04.htm";
								break;
							
							case 2:
								htmltext = "33344_04.htm";
								break;
							
							case 3:
								if (qs.getQuestItemsCount(questitem_03) >= 10)
								{
									htmltext = "33344_05.htm";
								}
								break;
							
							case 4:
								htmltext = "33344_08.htm";
								break;
							
							case 5:
								htmltext = "33344_08.htm";
								break;
							
							case 6:
								if (qs.getQuestItemsCount(questitem_04) >= 10)
								{
									htmltext = "33344_09.htm";
								}
								break;
							
							case 7:
								htmltext = "33344_11.htm";
								break;
							
							case 9:
								if ((qs.getQuestItemsCount(questitem_01) >= 2) && (qs.getQuestItemsCount(questitem_02) >= 2))
								{
									htmltext = "33344_12.htm";
								}
								else
								{
									htmltext = "33344_14.htm";
								}
								break;
						}
					}
					else
					{
						htmltext = "33344_nosubclass.htm";
					}
				}
				else
				{
					htmltext = "33344_completed.htm";
				}
				break;
			
			case Ishuma:
				if (!(qs.getState() == Quest.COMPLETED))
				{
					if (qs.getInt("subClassId") == player.getClassId().getId())
					{
						if (cond == 7)
						{
							if ((qs.getQuestItemsCount(questitem_03) >= 10) && (qs.getQuestItemsCount(questitem_04) >= 10))
							{
								htmltext = "32615_01.htm";
								qs.setCond(8);
							}
						}
						else if (cond == 8)
						{
							qs.giveItems(questitem_01, 2);
							qs.giveItems(questitem_02, 2);
							htmltext = "32615_04.htm";
							qs.setCond(9);
						}
						else if (cond == 9)
						{
							htmltext = "32615_05.htm";
						}
					}
					else
					{
						htmltext = "no_subclass.htm";
					}
				}
				else
				{
					htmltext = "32615_completed.htm";
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
		final int subclassId = qs.getInt("subClassId");
		
		if ((qs.getPlayer().getActiveSubClass().getClassId() == subclassId) && (qs.getState() == Quest.STARTED))
		{
			switch (cond)
			{
				case 1:
					if (_Mobs1.contains(npcId) && Rnd.chance(chanceGetItem) && (qs.getQuestItemsCount(questitem_03) < 1))
					{
						qs.giveItems(questitem_03, 1);
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(2);
					}
					break;
				
				case 2:
					if (_Mobs1.contains(npcId) && Rnd.chance(chanceGetItem))
					{
						if (qs.getQuestItemsCount(questitem_03) < 10)
						{
							qs.giveItems(questitem_03, 1);
							qs.playSound(SOUND_ITEMGET);
						}
						else if (qs.getQuestItemsCount(questitem_03) >= 10)
						{
							qs.playSound(SOUND_MIDDLE);
							qs.setCond(3);
						}
					}
					break;
				
				case 4:
					if (_Mobs2.contains(npcId) && Rnd.chance(chanceGetItem) && (qs.getQuestItemsCount(questitem_04) < 1))
					{
						qs.giveItems(questitem_04, 1);
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(5);
					}
					break;
				
				case 5:
					if (_Mobs2.contains(npcId) && Rnd.chance(chanceGetItem))
					{
						if (qs.getQuestItemsCount(questitem_04) < 10)
						{
							qs.giveItems(questitem_04, 1);
							qs.playSound(SOUND_ITEMGET);
						}
						else if (qs.getQuestItemsCount(questitem_04) >= 10)
						{
							qs.playSound(SOUND_MIDDLE);
							qs.setCond(6);
						}
					}
					break;
			}
		}
		else
		{
			if (cond > 0)
			{
				qs.getPlayer().sendMessage("You cannot obtain the quest items if you are in different subclass to which started the quest.");
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
