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

import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeRunnerEvent;
import lineage2.gameserver.model.entity.events.objects.TerritoryWardObject;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.model.instances.residences.SiegeFlagInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TakeFlag extends Skill
{
	/**
	 * Constructor for TakeFlag.
	 * @param set StatsSet
	 */
	public TakeFlag(StatsSet set)
	{
		super(set);
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
		if (!super.checkCondition(activeChar, target, forceUse, dontMove, first))
		{
			return false;
		}
		if ((activeChar == null) || !activeChar.isPlayer())
		{
			return false;
		}
		Player player = (Player) activeChar;
		if (player.getClan() == null)
		{
			return false;
		}
		DominionSiegeEvent siegeEvent1 = player.getEvent(DominionSiegeEvent.class);
		if (siegeEvent1 == null)
		{
			return false;
		}
		if (!(player.getActiveWeaponFlagAttachment() instanceof TerritoryWardObject))
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}
		if (player.isMounted())
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}
		if (!(target instanceof SiegeFlagInstance) || (target.getNpcId() != 36590) || (target.getClan() != player.getClan()))
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}
		DominionSiegeEvent siegeEvent2 = target.getEvent(DominionSiegeEvent.class);
		if ((siegeEvent2 == null) || (siegeEvent1 != siegeEvent2))
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}
		return true;
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		for (Creature target : targets)
		{
			if (target != null)
			{
				Player player = (Player) activeChar;
				DominionSiegeEvent siegeEvent1 = player.getEvent(DominionSiegeEvent.class);
				if (siegeEvent1 == null)
				{
					continue;
				}
				if (!(target instanceof SiegeFlagInstance) || (target.getNpcId() != 36590) || (target.getClan() != player.getClan()))
				{
					continue;
				}
				if (!(player.getActiveWeaponFlagAttachment() instanceof TerritoryWardObject))
				{
					continue;
				}
				DominionSiegeEvent siegeEvent2 = target.getEvent(DominionSiegeEvent.class);
				if ((siegeEvent2 == null) || (siegeEvent1 != siegeEvent2))
				{
					continue;
				}
				Dominion dominion = siegeEvent1.getResidence();
				TerritoryWardObject wardObject = (TerritoryWardObject) player.getActiveWeaponFlagAttachment();
				DominionSiegeEvent siegeEvent3 = wardObject.getEvent();
				Dominion dominion3 = siegeEvent3.getResidence();
				int wardDominionId = wardObject.getDominionId();
				wardObject.despawnObject(siegeEvent3);
				dominion3.removeFlag(wardDominionId);
				dominion.addFlag(wardDominionId);
				siegeEvent1.spawnAction("ward_" + wardDominionId, true);
				DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
				runnerEvent.broadcastTo(new SystemMessage2(SystemMsg.CLAN_S1_HAS_SUCCEEDED_IN_CAPTURING_S2S_TERRITORY_WARD).addString(dominion.getOwner().getName()).addResidenceName(wardDominionId));
			}
		}
	}
}
