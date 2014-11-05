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
package handlers.bypass;

import java.util.Collection;

import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.handlers.BypassHandler;
import lineage2.gameserver.handlers.IBypassHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.AcquireSkillDone;
import lineage2.gameserver.network.serverpackets.AcquireSkillList;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 */
public final class CollectionSkillList implements IBypassHandler, ScriptFile
{
	/**
	 * Method getBypasses.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IBypassHandler#getBypasses()
	 */
	@Override
	public String[] getBypasses()
	{
		return new String[]
		{
			"CollectionSkillList"
		};
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param command String
	 * @see lineage2.gameserver.handlers.IBypassHandler#onBypassFeedback(NpcInstance, Player, String)
	 */
	@Override
	public void onBypassFeedback(NpcInstance npc, Player player, String command)
	{
		final Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(player, AcquireType.COLLECTION);
		final AcquireSkillList asl = new AcquireSkillList(AcquireType.COLLECTION, skills.size());
		
		for (SkillLearn s : skills)
		{
			asl.addSkill(s.getId(), s.getLevel(), s.getLevel(), s.getCost(), 0);
		}
		
		if (skills.size() == 0)
		{
			player.sendPacket(AcquireSkillDone.STATIC);
			player.sendPacket(SystemMsg.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
		}
		else
		{
			player.sendPacket(asl);
		}
		
		player.sendActionFailed();
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		BypassHandler.getInstance().registerBypass(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}