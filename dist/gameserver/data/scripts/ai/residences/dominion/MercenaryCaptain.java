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
package ai.residences.dominion;

import java.util.Calendar;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeRunnerEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.taskmanager.AiTaskManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MercenaryCaptain extends DefaultAI
{
	/**
	 * Field MESSAGES.
	 */
	private static final NpcString[] MESSAGES = new NpcString[]
	{
		NpcString.COURAGE_AMBITION_PASSION_MERCENARIES_WHO_WANT_TO_REALIZE_THEIR_DREAM_OF_FIGHTING_IN_THE_TERRITORY_WAR_COME_TO_ME_FORTUNE_AND_GLORY_ARE_WAITING_FOR_YOU,
		NpcString.DO_YOU_WISH_TO_FIGHT_ARE_YOU_AFRAID_NO_MATTER_HOW_HARD_YOU_TRY_YOU_HAVE_NOWHERE_TO_RUN
	};
	
	/**
	 * Constructor for MercenaryCaptain.
	 * @param actor NpcInstance
	 */
	public MercenaryCaptain(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 1000L;
		AI_TASK_ATTACK_DELAY = 1000L;
	}
	
	/**
	 * Method startAITask.
	 */
	@Override
	public synchronized void startAITask()
	{
		if (_aiTask == null)
		{
			_aiTask = AiTaskManager.getInstance().scheduleAtFixedRate(this, calcDelay(), 3600000L);
		}
	}
	
	/**
	 * Method switchAITask.
	 * @param NEW_DELAY long
	 */
	@Override
	protected synchronized void switchAITask(long NEW_DELAY)
	{
		// empty method
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		NpcString shout;
		final DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
		if (runnerEvent.isInProgress())
		{
			shout = NpcString.CHARGE_CHARGE_CHARGE;
		}
		else
		{
			shout = MESSAGES[Rnd.get(MESSAGES.length)];
		}
		Functions.npcShout(actor, shout);
		return false;
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	/**
	 * Method calcDelay.
	 * @return long
	 */
	private static long calcDelay()
	{
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 55);
		cal.set(Calendar.SECOND, 0);
		final long t = System.currentTimeMillis();
		while (cal.getTimeInMillis() < t)
		{
			cal.add(Calendar.HOUR_OF_DAY, 1);
		}
		return cal.getTimeInMillis() - t;
	}
}
