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

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q10327_IntruderWhoWantsTheBookOfGiants extends Quest implements ScriptFile
{
	private static final int Panteleon = 32972;
	private static final int Table = 33126;
	private static final int Assasin = 23121;
	private static final int Tairen = 33004;
	private static final int Book = 17575;
	private NpcInstance _tairen = null;
	private int killedassasin = 0;
	private static final int INSTANCE_ID = 182;
	private int bookDeskObjectId = 0;
	private boolean bookTaken = false;
	
	public Q10327_IntruderWhoWantsTheBookOfGiants()
	{
		super(false);
		addStartNpc(Panteleon);
		addTalkId(Panteleon);
		addFirstTalkId(Table);
		addQuestItem(Book);
		addSkillUseId(Assasin);
		addFirstTalkId(Tairen);
		addKillId(Assasin);
		addAttackId(Assasin);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10326_RespectYourElders.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-3.htm";
				break;
			
			case "enter_museum":
				player.teleToLocation(-114360, 260184, -1224);
				return null;
				
			case "enter_instance":
				enterInstance(qs.getPlayer());
				qs.playSound(SOUND_MIDDLE);
				bookTaken = false;
				_tairen = qs.getPlayer().getActiveReflection().getAllByNpcId(Tairen, true).get(0);
				if (_tairen != null)
				{
					_tairen.setRunning();
				}
				return null;
				
			case "qet_rev":
				player.sendPacket(new ExShowScreenMessage(NpcString.ACCESSORIES_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
				htmltext = "0-5.htm";
				qs.getPlayer().addExpAndSp(7800, 3500);
				qs.giveItems(57, 16000);
				qs.giveItems(112, 2);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "attak":
				htmltext = "";
				qs.startQuestTimer("attak", 5000);
				if (_tairen != null)
				{
					_tairen.moveToLocation(qs.getPlayer().getLoc(), Rnd.get(0, 100), true);
				}
				{
					if (Rnd.chance(33))
					{
						Functions.npcSayToPlayer(_tairen, qs.getPlayer(), NpcString.LOOKS_LIKE_ONLY_SKILL_BASED_ATTACKS_DAMAGE_THEM, ChatType.NPC_SAY);
					}
					
					if (Rnd.chance(33))
					{
						Functions.npcSayToPlayer(_tairen, qs.getPlayer(), NpcString.YOUR_NORMAL_ATTACKS_ARENT_WORKING, ChatType.NPC_SAY);
					}
					
					if (Rnd.chance(33))
					{
						Functions.npcSayToPlayer(_tairen, qs.getPlayer(), NpcString.USE_YOUR_SKILL_ATTACKS_AGAINST_THEM, ChatType.NPC_SAY);
					}
				}
				break;
			
			case "spawnas":
				htmltext = "";
				NpcInstance asa = NpcUtils.spawnSingle(Assasin, new Location(-114815, 244966, -7976, 0), player.getActiveReflection());
				NpcInstance ass = NpcUtils.spawnSingle(Assasin, new Location(-114554, 244954, -7976, 0), player.getActiveReflection());
				Functions.npcSayToPlayer(ass, qs.getPlayer(), NpcString.FINALLY_I_THOUGHT_I_WAS_GOING_TO_DIE_WAITING, ChatType.NPC_SAY);
				asa.getAggroList().addDamageHate(qs.getPlayer(), 0, 10000);
				asa.setAggressionTarget(player);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (npc.getId() == Panteleon)
		{
			if (qs.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3.htm";
			}
			else if ((cond == 3) && (qs.getQuestItemsCount(Book) >= 1))
			{
				htmltext = "0-4.htm";
			}
			else if (cond == 2)
			{
				htmltext = "0-3.htm";
				qs.setCond(1);
				qs.takeAllItems(Book);
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		String htmltext = "3-4.htm";
		final QuestState st = player.getQuestState(getClass());
		if (st == null)
		{
			return htmltext;
		}
		
		switch (npc.getId())
		{
			case Table:
				if ((npc.getObjectId() == bookDeskObjectId) && !bookTaken)
				{
					bookTaken = true;
					player.sendPacket(new ExShowScreenMessage(NpcString.WATCH_OUT_YOU_ARE_BEING_ATTACKED, 4500, ScreenMessageAlign.TOP_CENTER));
					htmltext = "2-2.htm";
					st.takeAllItems(Book);
					st.giveItems(Book, 1, false);
					st.setCond(2);
					st.startQuestTimer("attak", 5000);
					st.startQuestTimer("spawnas", 50);
				}
				else
				{
					htmltext = "2-1.htm";
				}
				break;
			
			case Tairen:
				htmltext = "3-4.htm";
				if (st.getCond() == 1)
				{
					htmltext = "3-1.htm";
				}
				else if (st.getCond() == 2)
				{
					htmltext = "3-2.htm";
				}
				else if (st.getCond() == 3)
				{
					htmltext = "3-3.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onAttack(NpcInstance npc, QuestState qs)
	{
		Functions.npcSayToPlayer(_tairen, qs.getPlayer(), NpcString.ENOUGH_OF_THIS_COME_AT_ME, ChatType.NPC_SAY);
		
		if (npc.getId() == Assasin)
		{
			if (_tairen != null)
			{
				_tairen.getAggroList().addDamageHate(npc, 0, 5000);
			}
			
			if (killedassasin >= 1)
			{
				qs.setCond(3);
				qs.cancelQuestTimer("attak");
				qs.playSound(SOUND_MIDDLE);
				killedassasin = 0;
			}
			else
			{
				killedassasin++;
			}
		}
		
		return null;
	}
	
	private void enterInstance(Player player)
	{
		final Reflection reflection = player.getActiveReflection();
		
		if (reflection != null)
		{
			if (player.canReenterInstance(INSTANCE_ID))
			{
				player.teleToLocation(reflection.getTeleportLoc(), reflection);
			}
		}
		else if (player.canEnterInstance(INSTANCE_ID))
		{
			ReflectionUtils.enterReflection(player, INSTANCE_ID);
		}
		
		List<NpcInstance> desks = player.getActiveReflection().getAllByNpcId(Table, true);
		double seed = Math.random();
		int counter = 0;
		
		for (NpcInstance desk : desks)
		{
			if (((seed <= 0.25) && (counter == 0)) || ((seed > 0.25) && (seed <= 0.5) && (counter == 1)) || ((seed > 0.5) && (seed <= 0.75) && (counter == 2)) || ((seed > 0.75) && (counter == 3)))
			{
				bookDeskObjectId = desk.getObjectId();
			}
			
			++counter;
		}
		
		if ((bookDeskObjectId == 0) && (desks.size() > 0))
		{
			bookDeskObjectId = desks.get(0).getObjectId();
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
