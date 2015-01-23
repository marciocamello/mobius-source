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

import lineage2.gameserver.listener.actor.player.impl.MentorAnswerListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.network.serverpackets.ConfirmDlg;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SummonMentor extends Skill
{
	/**
	 * Constructor for SummonMentor.
	 * @param set StatsSet
	 */
	public SummonMentor(StatsSet set)
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
		if (!activeChar.isPlayer())
		{
			return false;
		}
		
		if (activeChar.isPlayer())
		{
			Player p = (Player) activeChar;
			
			if (p.getActiveWeaponFlagAttachment() != null)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD));
				return false;
			}
			
			if (p.isInDuel() || (p.getTeam() != TeamType.NONE))
			{
				activeChar.sendMessage("You cannot use escape skills during a duel or during event participation.");
				return false;
			}
			
			if (p.isInOlympiadMode())
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_USE_THAT_SKILL_IN_A_OLYMPIAD_MATCH));
				return false;
			}
		}
		
		if (activeChar.isInZone(Zone.ZoneType.NoEscape) && (activeChar.getReflection() != null) && (activeChar.getReflection().getCoreLoc() != null))
		{
			if (activeChar.isPlayer())
			{
				activeChar.sendMessage("You may not use an escape skill here.");
			}
			
			return false;
		}
		
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		Player player = (Player) activeChar;
		int mentorId = player.getMentorSystem().getMentor();
		
		if (mentorId != 0)
		{
			Player mentor = World.getPlayer(mentorId);
			
			if (mentor == null)
			{
				return;
			}
			
			mentor.ask(new ConfirmDlg(SystemMessageId.YOU_HAVE_REQUESTED_A_TELEPORT_TO_S1_DO_YOU_WISH_TO_CONTINUE).addString(activeChar.getName()), new MentorAnswerListener(mentor, activeChar.getName()));
		}
		else
		{
			return;
		}
	}
}
