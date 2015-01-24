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
import lineage2.gameserver.enums.AcquireType;
import lineage2.gameserver.enums.ClassId;
import lineage2.gameserver.handlers.BypassHandler;
import lineage2.gameserver.handlers.IBypassHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.AcquireSkillDone;
import lineage2.gameserver.network.serverpackets.AcquireSkillList;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 */
public final class TransferSkill implements IBypassHandler, ScriptFile
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
			"TransferSkillList",
			"RemoveTransferSkill"
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
		if (command.equals("TransferSkillList"))
		{
			ClassId classId = player.getClassId();
			
			if (classId == null)
			{
				return;
			}
			
			if ((player.getLevel() < 76) || (classId.getClassLevel().ordinal() < 4))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, npc);
				StringBuilder sb = new StringBuilder();
				sb.append("<html><head><body>");
				sb.append("You must have 3rd class change quest completed.");
				sb.append("</body></html>");
				html.setHtml(sb.toString());
				player.sendPacket(html);
				return;
			}
			
			AcquireType type = AcquireType.transferType(player.getActiveClassId());
			
			if (type == null)
			{
				return;
			}
			
			final Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(player, type);
			final AcquireSkillList asl = new AcquireSkillList(type, skills.size());
			
			for (SkillLearn s : skills)
			{
				asl.addSkill(s.getId(), s.getLevel(), s.getLevel(), s.getCost(), 0);
			}
			
			if (skills.size() == 0)
			{
				player.sendPacket(AcquireSkillDone.STATIC);
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN));
			}
			else
			{
				player.sendPacket(asl);
			}
			
			player.sendActionFailed();
		}
		else if (command.startsWith("RemoveTransferSkill"))
		{
			AcquireType type = AcquireType.transferType(player.getActiveClassId());
			
			if (type == null)
			{
				return;
			}
			
			Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(null, type);
			
			if (skills.isEmpty())
			{
				player.sendActionFailed();
				return;
			}
			
			boolean reset = false;
			
			for (SkillLearn skill : skills)
			{
				if (player.getKnownSkill(skill.getId()) != null)
				{
					reset = true;
					break;
				}
			}
			
			if (!reset)
			{
				player.sendActionFailed();
				return;
			}
			
			if (!player.reduceAdena(10000000L, true))
			{
				npc.showChatWindow(player, "common/skill_share_healer_no_adena.htm");
				return;
			}
			
			for (SkillLearn skill : skills)
			{
				if (player.removeSkill(skill.getId(), true) != null)
				{
					for (int itemId : skill.getRequiredItems().keySet())
					{
						ItemFunctions.addItem(player, itemId, skill.getRequiredItems().get(itemId), true);
					}
				}
			}
		}
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