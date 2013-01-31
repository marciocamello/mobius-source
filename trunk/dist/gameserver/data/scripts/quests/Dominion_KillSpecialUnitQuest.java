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
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeRunnerEvent;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

public abstract class Dominion_KillSpecialUnitQuest extends Quest implements ScriptFile
{
	private final ClassId[] _classIds;
	
	public Dominion_KillSpecialUnitQuest()
	{
		super(PARTY_ALL);
		_classIds = getTargetClassIds();
		DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
		for (ClassId c : _classIds)
		{
			runnerEvent.addClassQuest(c, this);
		}
	}
	
	protected abstract NpcString startNpcString();
	
	protected abstract NpcString progressNpcString();
	
	protected abstract NpcString doneNpcString();
	
	protected abstract int getRandomMin();
	
	protected abstract int getRandomMax();
	
	protected abstract ClassId[] getTargetClassIds();
	
	@Override
	public String onKill(Player killed, QuestState qs)
	{
		Player player = qs.getPlayer();
		if (player == null)
		{
			return null;
		}
		DominionSiegeEvent event1 = player.getEvent(DominionSiegeEvent.class);
		if (event1 == null)
		{
			return null;
		}
		DominionSiegeEvent event2 = killed.getEvent(DominionSiegeEvent.class);
		if ((event2 == null) || (event2 == event1))
		{
			return null;
		}
		if (!ArrayUtils.contains(_classIds, killed.getClassId()))
		{
			return null;
		}
		int max_kills = qs.getInt("max_kills");
		if (max_kills == 0)
		{
			qs.setState(STARTED);
			qs.setCond(1);
			max_kills = Rnd.get(getRandomMin(), getRandomMax());
			qs.set("max_kills", max_kills);
			qs.set("current_kills", 1);
			player.sendPacket(new ExShowScreenMessage(startNpcString(), 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false, String.valueOf(max_kills)));
		}
		else
		{
			int current_kills = qs.getInt("current_kills") + 1;
			if (current_kills >= max_kills)
			{
				event1.addReward(player, DominionSiegeEvent.STATIC_BADGES, 10);
				qs.setState(COMPLETED);
				qs.addExpAndSp(534000, 51000);
				qs.exitCurrentQuest(true);
				player.sendPacket(new ExShowScreenMessage(doneNpcString(), 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false));
			}
			else
			{
				qs.set("current_kills", current_kills);
				player.sendPacket(new ExShowScreenMessage(progressNpcString(), 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, false, String.valueOf(max_kills), String.valueOf(current_kills)));
			}
		}
		return null;
	}
	
	@Override
	public boolean canAbortByPacket()
	{
		return false;
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
