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

import java.util.concurrent.ScheduledFuture;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00457_LostAndFound extends Quest implements ScriptFile
{
	// Npcs
	private static final int Gumiel = 32759;
	private static final int Follow = 32764;
	// Other
	private ScheduledFuture<?> _followTask;
	
	public Q00457_LostAndFound()
	{
		super(true);
		addStartNpc(Gumiel);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		
		if (event.equals("lost_villager_q0457_06.htm"))
		{
			qs.setCond(1);
			qs.setState(2);
			qs.playSound("ItemSound.quest_accept");
			npc.setFollowTarget(qs.getPlayer());
			
			if (_followTask != null)
			{
				_followTask.cancel(false);
				_followTask = null;
			}
			
			_followTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Follow(npc, player, qs), 0L, 1000L);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int state = qs.getState();
		final int cond = qs.getCond();
		
		switch (state)
		{
			case 1:
				if ((npc.getFollowTarget() != null) && (npc.getFollowTarget() != player))
				{
					return "lost_villager_q0457_01a.htm";
				}
				else if (qs.getPlayer().getLevel() >= 82)
				{
					if (qs.isNowAvailableByTime())
					{
						return "lost_villager_q0457_01.htm";
					}
					
					return "lost_villager_q0457_02.htm";
				}
				return "lost_villager_q0457_03.htm";
				
			case 2:
				if ((npc.getFollowTarget() != null) && (npc.getFollowTarget() != player))
				{
					return "lost_villager_q0457_01a.htm";
				}
				else if (cond == 1)
				{
					return "lost_villager_q0457_08.htm";
				}
				else if (cond == 2)
				{
					npc.deleteMe();
					qs.giveItems(15716, 1L);
					qs.unset("cond");
					qs.playSound("ItemSound.quest_finish");
					qs.exitCurrentQuest(this);
					return "lost_villager_q0457_09.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	void checkInRadius(int id, QuestState qs, NpcInstance npc)
	{
		final NpcInstance quest0457 = GameObjectsStorage.getByNpcId(id);
		
		if (npc.getRealDistance3D(quest0457) <= 150.0D)
		{
			qs.setCond(2);
			
			if (_followTask != null)
			{
				_followTask.cancel(false);
			}
			
			_followTask = null;
			npc.stopMove();
		}
	}
	
	private class Follow implements Runnable
	{
		private final NpcInstance _npc;
		private final Player _player;
		private final QuestState _qs;
		
		Follow(NpcInstance npc, Player player, QuestState qs)
		{
			_npc = npc;
			_player = player;
			_qs = qs;
		}
		
		@Override
		public void run()
		{
			_npc.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, _player, Integer.valueOf(150));
			checkInRadius(Follow, _qs, _npc);
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