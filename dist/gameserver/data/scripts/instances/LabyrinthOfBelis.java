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
package instances;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import ai.Generator;
import ai.InfiltrationOfficer;
import ai.InfiltrationOfficer.State;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LabyrinthOfBelis extends Reflection
{
	/**
	 * Field OFFICER. (value is 19155)
	 */
	private static final int OFFICER = 19155;
	/**
	 * Field GENERATOR. (value is 33216)
	 */
	private static final int GENERATOR = 33216;
	/**
	 * Field OPERATIVE. (value is 22998)
	 */
	private static final int OPERATIVE = 22998;
	/**
	 * Field HANDYMAN. (value is 22997)
	 */
	private static final int HANDYMAN = 22997;
	/**
	 * Field _marksRequiered.
	 */
	private int _marksRequiered = 3;
	/**
	 * Field _operativesKilled.
	 */
	private int _operativesKilled = 0;
	/**
	 * Field _instanceCondition.
	 */
	private int _instanceCondition = 0;
	/**
	 * Field officerAI.
	 */
	private InfiltrationOfficer officerAI = null;
	/**
	 * Field officer.
	 */
	private NpcInstance officer = null;
	/**
	 * Field generator.
	 */
	private NpcInstance generator = null;
	/**
	 * Field GeneratorAI.
	 */
	@SuppressWarnings("unused")
	private Generator GeneratorAI = null;
	
	/**
	 * Constructor for LabyrinthOfBelis.
	 * @param player Player
	 */
	public LabyrinthOfBelis(Player player)
	{
		setReturnLoc(player.getLoc());
	}
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 */
	@Override
	public void onPlayerEnter(final Player player)
	{
		spawnActiveNPCs(player);
		super.onPlayerEnter(player);
	}
	
	/**
	 * Method spawnActiveNPCs.
	 * @param player Player
	 */
	public void spawnActiveNPCs(Player player)
	{
		officer = getAllByNpcId(OFFICER, true).get(0);
		generator = getAllByNpcId(GENERATOR, true).get(0);
		if ((officer != null) && (generator != null))
		{
			officer.setFollowTarget(player);
			officerAI = ((InfiltrationOfficer) officer.getAI());
			officerAI.setState(State.AI_IDLE);
			GeneratorAI = ((Generator) generator.getAI());
		}
	}
	
	/**
	 * Method reduceMarksRequiered.
	 */
	public void reduceMarksRequiered()
	{
		--_marksRequiered;
	}
	
	/**
	 * Method getMarksRequieredCount.
	 * @return int
	 */
	public int getMarksRequieredCount()
	{
		return _marksRequiered;
	}
	
	/**
	 * Method incOperativesKilled.
	 */
	public void incOperativesKilled()
	{
		++_operativesKilled;
	}
	
	/**
	 * Method getOperativesKilledCount.
	 * @return int
	 */
	public int getOperativesKilledCount()
	{
		return _operativesKilled;
	}
	
	/**
	 * Method makeOnEvent.
	 * @param officerState State
	 * @param openDoorId int
	 */
	public void makeOnEvent(State officerState, int openDoorId)
	{
		++_instanceCondition;
		if (openDoorId != 0)
		{
			openDoor(openDoorId);
		}
		officerAI.setState(officerState);
	}
	
	/**
	 * Method getInstanceCond.
	 * @return int
	 */
	public int getInstanceCond()
	{
		return _instanceCondition;
	}
	
	/**
	 * Method deleteGenerator.
	 */
	public void deleteGenerator()
	{
		generator.deleteMe();
	}
	
	/**
	 * Method activateGenerator.
	 */
	public void activateGenerator()
	{
		generator.setNpcState(1);
	}
	
	/**
	 * Method spawnAttackers.
	 */
	public void spawnAttackers()
	{
		int npcId = ((_instanceCondition % 2) == 0) ? HANDYMAN : OPERATIVE;
		NpcInstance attacker = addSpawnWithoutRespawn(npcId, new Location(-116856, 213320, -8619), Rnd.get(-100, 100));
		attacker.setRunning();
		attacker.getAggroList().addDamageHate(officer, 0, 1000);
		attacker.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, officer);
	}
}
