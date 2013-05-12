package instances;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExChangeNpcState;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

import ai.Generator;
import ai.InfiltrationOfficer;
import ai.InfiltrationOfficer.State;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 30.06.12
 * Time: 7:50
 */
public class LabyrinthOfBelis extends Reflection
{
	private static final int OFFICER = 19155;
	private static final int GENERATOR = 33216;

	private static final int OPERATIVE = 22998;
	private static final int HANDYMAN = 22997;
	private static final int DOOR = 16240001;

	/**
	 * 3 marks are required to open 4th door.
	 */
	private int _marksRequiered = 3;
	private int _operativesKilled = 0;
	/**
	 * This is internal instance zone condition status
	 */
	private int _instanceCondition = 0;

	private InfiltrationOfficer officerAI = null;
	private NpcInstance officer = null;
	private NpcInstance generator = null;
	private Generator GeneratorAI = null;

	public LabyrinthOfBelis(Player player)
	{
		setReturnLoc(player.getLoc());
	}

	@Override
	public void onPlayerEnter(final Player player)
	{
		spawnActiveNPCs(player);
		openDoor(DOOR);
		super.onPlayerEnter(player);
	}

	public void spawnActiveNPCs(Player player)
	{
		//officer = addSpawnWithoutRespawn(OFFICER, new Location(-118973, 211197, -8592, 8546), 0);
		officer = getAllByNpcId(OFFICER, true).get(0);
		generator = getAllByNpcId(GENERATOR, true).get(0);
		if(officer != null && generator != null)
		{
			officer.setFollowTarget(player);
			officerAI = (InfiltrationOfficer) officer.getAI();
			officerAI.setState(State.AI_IDLE);
			setGeneratorAI((Generator) generator.getAI());
		}
	}

	public void reduceMarksRequiered()
	{
		--_marksRequiered;
	}

	public int getMarksRequieredCount()
	{
		return _marksRequiered;
	}

	public void incOperativesKilled()
	{
		++_operativesKilled;
	}

	public int getOperativesKilledCount()
	{
		return _operativesKilled;
	}

	public void makeOnEvent(State officerState, int openDoorId)
	{
		++_instanceCondition;
		if(openDoorId != 0)
			getDoor(openDoorId).openMe();
		officerAI.setState(officerState);
	}

	public int getInstanceCond()
	{
		return _instanceCondition;
	}

	public void deleteGenerator()
	{
		generator.deleteMe();
	}

	public void activateGenerator(Player player)
	{
		generator.setNpcState(1);
		if(player.isInRange(generator, NpcInstance.INTERACTION_DISTANCE))
			generator.doCast(SkillTable.getInstance().getInfo(14698, 1), player, false);
		player.sendPacket(new ExChangeNpcState(generator.getObjectId(), 1));
	}

	public void spawnAttackers()
	{
		// Handymans and Operatives spawned each after another
		int npcId = _instanceCondition % 2 == 0 ? HANDYMAN : OPERATIVE;
		NpcInstance attacker = addSpawnWithoutRespawn(npcId, new Location(-116856, 213320, -8619), Rnd.get(-100, 100));

		attacker.setRunning();
		attacker.getAggroList().addDamageHate(officer, 0, 1000);
		attacker.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, officer);
	}

	public Generator getGeneratorAI()
	{
		return GeneratorAI;
	}

	public void setGeneratorAI(Generator generatorAI)
	{
		GeneratorAI = generatorAI;
	}

}
