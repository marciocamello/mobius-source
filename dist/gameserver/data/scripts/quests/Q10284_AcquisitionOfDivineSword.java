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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q10284_AcquisitionOfDivineSword extends Quest implements ScriptFile
{
	// Npcs
	private static final int Rafforty = 32020;
	private static final int Jinia = 32760;
	private static final int Krun = 32653;
	private static final int InjKegor = 18846;
	// Item
	private static final int ColdResistancePotion = 15514;
	// Monster
	private static final int MithrilMillipede = 22766;
	// Other
	int _count = 0;
	
	public Q10284_AcquisitionOfDivineSword()
	{
		super(false);
		addStartNpc(Rafforty);
		addTalkId(Jinia, Krun, InjKegor);
		addKillId(MithrilMillipede);
		addQuestItem(ColdResistancePotion);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "rafforty_q10284_02.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "enterinstance":
				qs.setCond(2);
				enterInstance(qs.getPlayer(), 140);
				return null;
				
			case "jinia_q10284_03.htm":
				if (!qs.getPlayer().getReflection().isDefault())
				{
					qs.getPlayer().getReflection().startCollapseTimer(60 * 1000L);
					qs.getPlayer().sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(1));
				}
				qs.setCond(3);
				break;
			
			case "leaveinstance":
				qs.getPlayer().getReflection().collapse();
				return null;
				
			case "entermines":
				qs.setCond(4);
				if (qs.getQuestItemsCount(ColdResistancePotion) < 1)
				{
					qs.giveItems(ColdResistancePotion, 1);
				}
				enterInstance(qs.getPlayer(), 138);
				return null;
				
			case "leavemines":
				qs.giveItems(ADENA_ID, 296425);
				qs.addExpAndSp(921805, 82230);
				qs.playSound(SOUND_FINISH);
				qs.setState(COMPLETED);
				qs.exitCurrentQuest(false);
				qs.getPlayer().getReflection().collapse();
				return null;
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
			case Rafforty:
				if (cond == 0)
				{
					final QuestState state = qs.getPlayer().getQuestState(Q10283_RequestOfIceMerchant.class);
					
					if ((qs.getPlayer().getLevel() >= 82) && (state != null) && state.isCompleted())
					{
						htmltext = "rafforty_q10284_01.htm";
					}
					else
					{
						htmltext = "rafforty_q10284_00.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if ((cond == 1) || (cond == 2))
				{
					htmltext = "rafforty_q10284_02.htm";
				}
				break;
			
			case Jinia:
				if (cond == 2)
				{
					htmltext = "jinia_q10284_01.htm";
				}
				else if (cond == 3)
				{
					htmltext = "jinia_q10284_02.htm";
				}
				break;
			
			case Krun:
				if ((cond == 3) || (cond == 4) || (cond == 5))
				{
					htmltext = "krun_q10284_01.htm";
				}
				break;
			
			case InjKegor:
				if (cond == 4)
				{
					qs.takeAllItems(ColdResistancePotion);
					qs.setCond(5);
					htmltext = "kegor_q10284_01.htm";
					
					for (int i = 0; i < 4; i++)
					{
						NpcInstance mob = qs.getPlayer().getReflection().addSpawnWithoutRespawn(MithrilMillipede, Location.findPointToStay(qs.getPlayer(), 50, 100), qs.getPlayer().getGeoIndex());
						mob.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, qs.getPlayer(), 300);
					}
				}
				else if (cond == 5)
				{
					htmltext = "kegor_q10284_02.htm";
				}
				else if (cond == 6)
				{
					htmltext = "kegor_q10284_03.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 5)
		{
			if (_count < 3)
			{
				_count++;
			}
			else
			{
				qs.setCond(6);
				qs.getPlayer().getReflection().startCollapseTimer(3 * 60 * 1000L);
				qs.getPlayer().sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(3));
			}
		}
		
		return null;
	}
	
	private void enterInstance(Player player, int izId)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(izId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(izId))
		{
			ReflectionUtils.enterReflection(player, izId);
		}
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
