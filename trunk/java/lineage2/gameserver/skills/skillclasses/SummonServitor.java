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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.base.SummonType;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.AgathionInstance;
import lineage2.gameserver.model.instances.MerchantInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.SummonInstance;
import lineage2.gameserver.model.instances.TrapInstance;
import lineage2.gameserver.model.instances.TreeInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncAdd;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SummonServitor extends Skill
{
	/**
	 * Field _summonType.
	 */
	private final SummonType _summonType;
	/**
	 * Field _expPenalty.
	 */
	private final double _expPenalty;
	/**
	 * Field _lifeTime.
	 */
	private final int _lifeTime;
	/**
	 * Field _summonPoint.
	 */
	private final int _summonPoint;
	
	/**
	 * Constructor for SummonServitor.
	 * @param set StatsSet
	 */
	public SummonServitor(StatsSet set)
	{
		super(set);
		_summonType = Enum.valueOf(SummonType.class, set.getString("summonType", "SERVITOR").toUpperCase());
		_expPenalty = set.getDouble("expPenalty", 0.f);
		_lifeTime = set.getInteger("lifeTime", 1200) * 1000;
		_summonPoint = set.getInteger("summonPoint", 0);
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		Player player = activeChar.getPlayer();
		if (player == null)
		{
			return false;
		}
		if (player.isProcessingRequest())
		{
			player.sendPacket(Msg.PETS_AND_SERVITORS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}
		switch (_summonType)
		{
			case TRAP:
				if (player.isInZonePeace())
				{
					activeChar.sendPacket(Msg.A_MALICIOUS_SKILL_CANNOT_BE_USED_IN_A_PEACE_ZONE);
					return false;
				}
				break;
			case SERVITOR:
			case MULTI_SERVITOR:
			case SIEGE_SUMMON:
				if (player.isMounted() || !player.getSummonList().canSummon(_summonType, _summonPoint))
				{
					player.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
					return false;
				}
				break;
			case AGATHION:
				if ((player.getAgathionId() > 0) && (_npcId != 0))
				{
					player.sendPacket(SystemMsg.AN_AGATHION_HAS_ALREADY_BEEN_SUMMONED);
					return false;
				}
				break;
			case TREE:
				if (player.isMounted())
				{
					player.sendPacket(SystemMsg.AN_AGATHION_HAS_ALREADY_BEEN_SUMMONED);
					return false;
				}
		}
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param caster Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature caster, List<Creature> targets)
	{
		Player activeChar = caster.getPlayer();
		switch (_summonType)
		{
			case AGATHION:
				activeChar.setAgathion(getNpcId());
				//If have lifeTime this is a servitor agathion...
				if (_lifeTime > 0)
				{
					Skill agatSkill = getFirstAddedSkill();
					NpcTemplate agatTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
					AgathionInstance agat = new AgathionInstance(IdFactory.getInstance().getNextId(), agatTemplate, activeChar, _lifeTime, agatSkill, activeChar.getLoc());
					agat.setReflection(activeChar.getReflection());
					agat.spawnMe(activeChar.getLoc());
					agat.setFollowTarget(activeChar);
					agat.setIsInvul(true);
					agat.setRunning();
					ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(agat), _lifeTime);
				}
				break;
			case TRAP:
				Skill trapSkill = getFirstAddedSkill();
				if (activeChar.getTrapsCount() >= 5)
				{
					activeChar.destroyFirstTrap();
				}
				TrapInstance trap = new TrapInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(getNpcId()), activeChar, trapSkill);
				activeChar.addTrap(trap);
				trap.spawnMe();
				break;
			case SERVITOR:
			case MULTI_SERVITOR:
			case SIEGE_SUMMON:
				Location loc = null;
				if (_targetType == SkillTargetType.TARGET_CORPSE)
				{
					for (Creature target : targets)
					{
						if ((target != null) && target.isDead())
						{
							activeChar.getAI().setAttackTarget(null);
							loc = target.getLoc();
							if (target.isNpc())
							{
								((NpcInstance) target).endDecayTask();
							}
							else if (target.isServitor())
							{
								((SummonInstance) target).endDecayTask();
							}
							else
							{
								return;
							}
						}
					}
				}
				if (activeChar.isMounted() || !activeChar.getSummonList().canSummon(_summonType, _summonPoint))
				{
					return;
				}
				NpcTemplate summonTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				SummonInstance summon = new SummonInstance(IdFactory.getInstance().getNextId(), summonTemplate, activeChar, _lifeTime, _summonPoint, this);
				activeChar.getSummonList().addSummon(summon);
				summon.setExpPenalty(_expPenalty);
				summon.setExp(Experience.LEVEL[Math.min(summon.getLevel(), Experience.LEVEL.length - 1)]);
				summon.setHeading(activeChar.getHeading());
				summon.setReflection(activeChar.getReflection());
				summon.spawnMe(loc == null ? Location.findAroundPosition(activeChar, 50, 70) : loc);
				summon.setRunning();
				summon.setFollowMode(true);
				if (summon.getSkillLevel(4140) > 0)
				{
					summon.altUseSkill(SkillTable.getInstance().getInfo(4140, summon.getSkillLevel(4140)), activeChar);
				}
				if (summon.getName().equalsIgnoreCase("Shadow"))
				{
					summon.addStatFunc(new FuncAdd(Stats.ABSORB_DAMAGE_PERCENT, 0x40, this, 15));
				}
				if (activeChar.isInOlympiadMode())
				{
					summon.getEffectList().stopAllEffects();
				}
				summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp(), false);
				if (_summonType == SummonType.SIEGE_SUMMON)
				{
					SiegeEvent<?, ?> siegeEvent = activeChar.getEvent(SiegeEvent.class);
					siegeEvent.addSiegeSummon(summon);
				}
				break;
			case MERCHANT:
				if ((activeChar.getSummonList().size() > 0) || activeChar.isMounted())
				{
					return;
				}
				NpcTemplate merchantTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				MerchantInstance merchant = new MerchantInstance(IdFactory.getInstance().getNextId(), merchantTemplate);
				merchant.setCurrentHp(merchant.getMaxHp(), false);
				merchant.setCurrentMp(merchant.getMaxMp());
				merchant.setHeading(activeChar.getHeading());
				merchant.setReflection(activeChar.getReflection());
				merchant.spawnMe(activeChar.getLoc());
				ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(merchant), _lifeTime);
				break;
			case TREE:
				if (activeChar.isMounted())
				{
					return;
				}
				NpcTemplate treeTemplate = NpcHolder.getInstance().getTemplate(getNpcId());
				TreeInstance tree = new TreeInstance(IdFactory.getInstance().getNextId(), treeTemplate, activeChar, _lifeTime, SkillTable.getInstance().getInfo(11806, getLevel()), activeChar.getLoc());
				tree.setCurrentHp(tree.getMaxHp(), false);
				tree.setCurrentMp(tree.getMaxMp());
				tree.setHeading(activeChar.getHeading());
				tree.setReflection(activeChar.getReflection());
				tree.spawnMe(activeChar.getLoc());
				ThreadPoolManager.getInstance().schedule(new GameObjectTasks.DeleteTask(tree), _lifeTime);
				break;
		}
		if (isSSPossible())
		{
			caster.unChargeShots(isMagic());
		}
	}
	
	/**
	 * Method getLifeTime.
	 * @return int
	 */
	public final int getLifeTime()
	{
		return _lifeTime;
	}
	
	/**
	 * Method getSummonPoint.
	 * @return int
	 */
	public final int getSummonPoint()
	{
		return _summonPoint;
	}
	
	/**
	 * Method getSummonType.
	 * @return SummonType
	 */
	public final SummonType getSummonType()
	{
		return _summonType;
	}
	
	/**
	 * Method isOffensive.
	 * @return boolean
	 */
	@Override
	public boolean isOffensive()
	{
		return _targetType == SkillTargetType.TARGET_CORPSE;
	}
}
