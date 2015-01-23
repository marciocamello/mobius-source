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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.residence.ResidenceSide;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TakeCastle extends Skill
{
	/**
	 * Constructor for TakeCastle.
	 */
	private final ResidenceSide _side;
	
	public TakeCastle(StatsSet set)
	{
		super(set);
		_side = set.getEnum("castle_side", ResidenceSide.class, ResidenceSide.NEUTRAL);
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
		
		if ((player.getClan() == null) || !player.isClanLeader())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}
		
		CastleSiegeEvent siegeEvent = player.getEvent(CastleSiegeEvent.class);
		
		if (siegeEvent == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}
		
		if (siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan()) == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}
		
		if (player.isMounted())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
			return false;
		}
		
		if (!player.isInRangeZ(target, 185))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_TARGET_IS_OUT_OF_RANGE));
			return false;
		}
		
		if (first)
		{
			siegeEvent.broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.THE_OPPOSING_CLAN_HAS_STARTED_S1), SiegeEvent.DEFENDERS);
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
				if (!target.isArtefact())
				{
					continue;
				}
				
				Player player = (Player) activeChar;
				CastleSiegeEvent siegeEvent = player.getEvent(CastleSiegeEvent.class);
				
				if (siegeEvent != null)
				{
					siegeEvent.broadcastTo(SystemMessage.getSystemMessage(SystemMessageId.CLAN_S1_HAS_SUCCEEDED_IN_S2).addString(player.getClan().getName()), SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
					siegeEvent.takeCastle(player.getClan(), _side); // processStep(player.getClan());
				}
			}
		}
	}
}
