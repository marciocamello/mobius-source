/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package npc.model;

import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.RaidBossInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import bosses.FourSepulchersManager;
import bosses.FourSepulchersSpawn;

public class SepulcherRaidInstance extends RaidBossInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int mysteriousBoxId = 0;
	private Future<?> _onDeadEventTask = null;
	
	public SepulcherRaidInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	protected void onDeath(Creature killer)
	{
		super.onDeath(killer);
		Player player = killer.getPlayer();
		if (player != null)
		{
			giveCup(player);
		}
		if (_onDeadEventTask != null)
		{
			_onDeadEventTask.cancel(false);
		}
		_onDeadEventTask = ThreadPoolManager.getInstance().schedule(new OnDeadEvent(this), 8500);
	}
	
	@Override
	protected void onDelete()
	{
		if (_onDeadEventTask != null)
		{
			_onDeadEventTask.cancel(false);
			_onDeadEventTask = null;
		}
		super.onDelete();
	}
	
	private void giveCup(Player player)
	{
		String questId = FourSepulchersManager.QUEST_ID;
		int cupId = 0;
		int oldBrooch = 7262;
		switch (getNpcId())
		{
			case 25339:
				cupId = 7256;
				break;
			case 25342:
				cupId = 7257;
				break;
			case 25346:
				cupId = 7258;
				break;
			case 25349:
				cupId = 7259;
				break;
		}
		if (player.getParty() != null)
		{
			for (Player mem : player.getParty().getPartyMembers())
			{
				QuestState qs = mem.getQuestState(questId);
				if ((qs != null) && (qs.isStarted() || qs.isCompleted()) && (mem.getInventory().getItemByItemId(oldBrooch) == null) && player.isInRange(mem, 700))
				{
					Functions.addItem(mem, cupId, 1);
				}
			}
		}
		else
		{
			QuestState qs = player.getQuestState(questId);
			if ((qs != null) && (qs.isStarted() || qs.isCompleted()) && (player.getInventory().getItemByItemId(oldBrooch) == null))
			{
				Functions.addItem(player, cupId, 1);
			}
		}
	}
	
	private class OnDeadEvent extends RunnableImpl
	{
		SepulcherRaidInstance _activeChar;
		
		public OnDeadEvent(SepulcherRaidInstance activeChar)
		{
			_activeChar = activeChar;
		}
		
		@Override
		public void runImpl()
		{
			FourSepulchersSpawn.spawnEmperorsGraveNpc(_activeChar.mysteriousBoxId);
		}
	}
	
	@Override
	public boolean canChampion()
	{
		return false;
	}
}
