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
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

public class Q00144_PailakaInjuredDragon extends Quest implements ScriptFile
{
	// Npcs
	private static final int KETRAOSHAMAN = 32499;
	private static final int KOSUPPORTER = 32502;
	private static final int KOIO = 32509;
	private static final int KOSUPPORTER2 = 32512;
	// Monsters
	private static final int VSWARRIOR1 = 18636;
	private static final int VSWARRIOR2 = 18642;
	private static final int VSCOMMAO1 = 18646;
	private static final int VSCOMMAO2 = 18654;
	private static final int VSGMAG1 = 18649;
	private static final int VSGMAG2 = 18650;
	private static final int VSHGAPG1 = 18655;
	private static final int VSHGAPG2 = 18657;
	private static final int[] Pailaka3rd =
	{
		18635,
		VSWARRIOR1,
		18638,
		18639,
		18640,
		18641,
		VSWARRIOR2,
		18644,
		18645,
		VSCOMMAO1,
		18648,
		VSGMAG1,
		VSGMAG2,
		18652,
		18653,
		VSCOMMAO2,
		VSHGAPG1,
		18656,
		VSHGAPG2,
		18658,
		18659
	};
	private static final int[] Antelopes =
	{
		18637,
		18643,
		18647,
		18651
	};
	private static final int LATANA = 18660;
	// Items
	private static final int ScrollOfEscape = 736;
	private static final int SPEAR = 13052;
	private static final int ENCHSPEAR = 13053;
	private static final int LASTSPEAR = 13054;
	private static final int STAGE1 = 13056;
	private static final int STAGE2 = 13057;
	private static final int[] PAILAKA3DROP =
	{
		8600,
		8601,
		8603,
		8604
	};
	private static final int[] ANTELOPDROP =
	{
		13032,
		13033
	};
	private static final int PSHIRT = 13296;
	// Skills
	private static final int[][] BUFFS =
	{
		{
			4357,
			2
		},
		{
			4342,
			2
		},
		{
			4356,
			3
		},
		{
			4355,
			3
		},
		{
			4351,
			6
		},
		{
			4345,
			3
		},
		{
			4358,
			3
		},
		{
			4359,
			3
		},
		{
			4360,
			3
		},
		{
			4352,
			2
		},
		{
			4354,
			4
		},
		{
			4347,
			6
		}
	};
	// Other
	private static final int instanceId = 45;
	
	public Q00144_PailakaInjuredDragon()
	{
		super(false);
		addStartNpc(KETRAOSHAMAN);
		addTalkId(KOSUPPORTER, KOIO, KOSUPPORTER2);
		addAttackId(LATANA, VSWARRIOR1, VSWARRIOR2, VSCOMMAO1, VSCOMMAO2, VSGMAG1, VSGMAG2, VSHGAPG1, VSHGAPG2);
		addKillId(LATANA);
		addKillId(Pailaka3rd);
		addKillId(Antelopes);
		addQuestItem(STAGE1, STAGE2, SPEAR, ENCHSPEAR, LASTSPEAR);
		addQuestItem(ANTELOPDROP);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		String htmltext = event;
		
		if (event.startsWith("buff"))
		{
			int[] skill = BUFFS[Integer.parseInt(event.split("buff")[1])];
			
			if (qs.getInt("spells") < 4)
			{
				makeBuff(npc, player, skill[0], skill[1]);
				qs.set("spells", "" + (qs.getInt("spells") + 1));
				htmltext = "32509-06.htm";
				return htmltext;
			}
			else if (qs.getInt("spells") == 4)
			{
				makeBuff(npc, player, skill[0], skill[1]);
				qs.set("spells", "5");
				htmltext = "32509-05.htm";
				return htmltext;
			}
		}
		
		switch (event)
		{
			case "Enter":
				enterInstance(player);
				return null;
				
			case "Support":
				if (qs.getInt("spells") < 5)
				{
					htmltext = "32509-06.htm";
				}
				else
				{
					htmltext = "32509-04.htm";
				}
				return htmltext;
				
			case "32499-02.htm":
				qs.set("spells", "0");
				qs.set("stage", "1");
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32499-05.htm":
				qs.setCond(2);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32502-05.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(SPEAR, 1);
				break;
			
			case "32512-02.htm":
				qs.takeItems(SPEAR, 1);
				qs.takeItems(ENCHSPEAR, 1);
				qs.takeItems(LASTSPEAR, 1);
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
		final int npcId = npc.getId();
		final int id = qs.getState();
		final Player player = qs.getPlayer();
		
		switch (npcId)
		{
			case KETRAOSHAMAN:
				if (cond == 0)
				{
					if ((player.getLevel() < 73) || (player.getLevel() > 77))
					{
						htmltext = "32499-no.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						return "32499-01.htm";
					}
				}
				else if (id == COMPLETED)
				{
					htmltext = "32499-no.htm";
				}
				else if ((cond == 1) || (cond == 2) || (cond == 3))
				{
					htmltext = "32499-06.htm";
				}
				else
				{
					htmltext = "32499-07.htm";
				}
				break;
			
			case KOSUPPORTER:
				if ((cond == 1) || (cond == 2))
				{
					htmltext = "32502-01.htm";
				}
				else
				{
					htmltext = "32502-05.htm";
				}
				break;
			
			case KOIO:
				if ((qs.getQuestItemsCount(SPEAR) > 0) && (qs.getQuestItemsCount(STAGE1) == 0))
				{
					htmltext = "32509-01.htm";
				}
				if ((qs.getQuestItemsCount(ENCHSPEAR) > 0) && (qs.getQuestItemsCount(STAGE2) == 0))
				{
					htmltext = "32509-01.htm";
				}
				if ((qs.getQuestItemsCount(SPEAR) == 0) && (qs.getQuestItemsCount(STAGE1) > 0))
				{
					htmltext = "32509-07.htm";
				}
				if ((qs.getQuestItemsCount(ENCHSPEAR) == 0) && (qs.getQuestItemsCount(STAGE2) > 0))
				{
					htmltext = "32509-07.htm";
				}
				if ((qs.getQuestItemsCount(SPEAR) == 0) && (qs.getQuestItemsCount(ENCHSPEAR) == 0))
				{
					htmltext = "32509-07.htm";
				}
				if ((qs.getQuestItemsCount(STAGE1) == 0) && (qs.getQuestItemsCount(STAGE2) == 0))
				{
					htmltext = "32509-01.htm";
				}
				if ((qs.getQuestItemsCount(SPEAR) > 0) && (qs.getQuestItemsCount(STAGE1) > 0))
				{
					qs.takeItems(SPEAR, 1);
					qs.takeItems(STAGE1, 1);
					qs.giveItems(ENCHSPEAR, 1);
					htmltext = "32509-02.htm";
				}
				if ((qs.getQuestItemsCount(ENCHSPEAR) > 0) && (qs.getQuestItemsCount(STAGE2) > 0))
				{
					qs.takeItems(ENCHSPEAR, 1);
					qs.takeItems(STAGE2, 1);
					qs.giveItems(LASTSPEAR, 1);
					htmltext = "32509-03.htm";
				}
				if (qs.getQuestItemsCount(LASTSPEAR) > 0)
				{
					htmltext = "32509-03.htm";
				}
				break;
			
			case KOSUPPORTER2:
				if (cond == 4)
				{
					qs.giveItems(ScrollOfEscape, 1);
					qs.giveItems(PSHIRT, 1);
					qs.giveItems(ADENA_ID, 2605000);
					qs.addExpAndSp(24570000, 8850000);
					qs.setCond(5);
					qs.setState(COMPLETED);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
					player.getReflection().startCollapseTimer(60000);
					player.setVitality(Config.MAX_VITALITY);
					htmltext = "32512-01.htm";
				}
				else if (id == COMPLETED)
				{
					htmltext = "32512-03.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int npcId = npc.getId();
		final int refId = player.getReflectionId();
		
		switch (npcId)
		{
			case VSWARRIOR1:
			case VSWARRIOR2:
				if (qs.getInt("stage") == 1)
				{
					qs.set("stage", "2");
				}
				break;
			
			case VSCOMMAO1:
			case VSCOMMAO2:
				if (qs.getInt("stage") == 2)
				{
					qs.set("stage", "3");
				}
				
				if ((qs.getQuestItemsCount(SPEAR) > 0) && (qs.getQuestItemsCount(STAGE1) == 0))
				{
					qs.giveItems(STAGE1, 1);
				}
				break;
			
			case VSGMAG1:
			case VSGMAG2:
				if (qs.getInt("stage") == 3)
				{
					qs.set("stage", "4");
				}
				
				if ((qs.getQuestItemsCount(ENCHSPEAR) > 0) && (qs.getQuestItemsCount(STAGE2) == 0))
				{
					qs.giveItems(STAGE2, 1);
				}
				break;
			
			case VSHGAPG1:
			case VSHGAPG2:
				if (qs.getInt("stage") == 4)
				{
					qs.set("stage", "5");
				}
				break;
			
			case LATANA:
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				addSpawnToInstance(KOSUPPORTER2, npc.getLoc(), 0, refId);
				break;
		}
		
		if (Util.contains(Pailaka3rd, npcId))
		{
			if (Rnd.get(100) < 30)
			{
				qs.dropItem(npc, PAILAKA3DROP[Rnd.get(PAILAKA3DROP.length)], 1);
			}
		}
		else if (Util.contains(Antelopes, npcId))
		{
			qs.dropItem(npc, ANTELOPDROP[Rnd.get(ANTELOPDROP.length)], Rnd.get(1, 10));
		}
		
		return null;
	}
	
	@Override
	public String onAttack(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int npcId = npc.getId();
		final int stage = qs.getInt("stage");
		
		switch (npcId)
		{
			case VSCOMMAO1:
			case VSCOMMAO2:
				if (stage < 2)
				{
					player.teleToLocation(122789, -45692, -3036);
					return null;
				}
				break;
			
			case VSGMAG1:
			case VSGMAG2:
				if (stage == 1)
				{
					player.teleToLocation(122789, -45692, -3036);
					return null;
				}
				else if (stage == 2)
				{
					player.teleToLocation(116948, -46445, -2673);
					return null;
				}
				break;
			
			case VSHGAPG1:
			case VSHGAPG2:
				if (stage == 1)
				{
					player.teleToLocation(122789, -45692, -3036);
					return null;
				}
				else if (stage == 2)
				{
					player.teleToLocation(116948, -46445, -2673);
					return null;
				}
				else if (stage == 3)
				{
					player.teleToLocation(112445, -44118, -2700);
					return null;
				}
				break;
			
			case LATANA:
				if (stage == 1)
				{
					player.teleToLocation(122789, -45692, -3036);
					return null;
				}
				else if (stage == 2)
				{
					player.teleToLocation(116948, -46445, -2673);
					return null;
				}
				else if (stage == 3)
				{
					player.teleToLocation(112445, -44118, -2700);
					return null;
				}
				else if (stage == 4)
				{
					player.teleToLocation(109947, -41433, -2311);
					return null;
				}
				break;
		}
		
		return null;
	}
	
	private void enterInstance(Player player)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(instanceId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(instanceId))
		{
			ReflectionUtils.enterReflection(player, instanceId);
		}
	}
	
	private void makeBuff(NpcInstance npc, Player player, int skillId, int level)
	{
		List<Creature> target = new ArrayList<>();
		target.add(player);
		npc.broadcastPacket(new MagicSkillUse(npc, player, skillId, level, 0, 0));
		npc.callSkill(SkillTable.getInstance().getInfo(skillId, level), target, true);
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
