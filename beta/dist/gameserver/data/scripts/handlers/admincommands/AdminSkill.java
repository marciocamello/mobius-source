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
package handlers.admincommands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.ExAcquirableSkillListByClass;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.PledgeSkillList;
import lineage2.gameserver.network.serverpackets.SkillCoolTime;
import lineage2.gameserver.network.serverpackets.SkillList;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.stats.Calculator;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminSkill implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_show_skills",
		"admin_remove_skills",
		"admin_skill_list",
		"admin_skill_index",
		"admin_add_skill",
		"admin_remove_skill",
		"admin_remove_all_skills",
		"admin_get_skills",
		"admin_reset_skills",
		"admin_give_all_skills",
		"admin_give_clan_skills",
		"admin_give_all_clan_skills",
		"admin_show_effects",
		"admin_debug_stats",
		"admin_remove_cooldown",
		"admin_buff",
		"admin_call_skill"
	};
	
	private static Skill[] adminSkills;
	
	/**
	 * Method useAdminCommand.
	 * @param command String
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IAdminCommandHandler#useAdminCommand(String, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(String command, String[] wordList, String fullString, Player activeChar)
	{
		if (!activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}
		
		switch (command)
		{
			case "admin_show_skills":
				showSkillsPage(activeChar);
				break;
			
			case "admin_show_effects":
				showEffects(activeChar);
				break;
			
			case "admin_remove_skills":
				removeSkillsPage(activeChar);
				break;
			
			case "admin_remove_all_skills":
				removeAllSkills(activeChar);
				break;
			
			case "admin_skill_list":
				activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/skills.htm"));
				break;
			
			case "admin_skill_index":
				if (wordList.length > 1)
				{
					activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/skills/" + wordList[1] + ".htm"));
				}
				
				break;
			
			case "admin_add_skill":
				adminAddSkill(activeChar, wordList);
				break;
			
			case "admin_remove_skill":
				adminRemoveSkill(activeChar, wordList);
				break;
			
			case "admin_get_skills":
				adminGetSkills(activeChar);
				break;
			
			case "admin_reset_skills":
				adminResetSkills(activeChar);
				break;
			
			case "admin_give_all_skills":
				adminGiveAllSkills(activeChar);
				break;
			
			case "admin_give_clan_skills":
				adminGiveClanSkills(activeChar, false);
				break;
			
			case "admin_give_all_clan_skills":
				adminGiveClanSkills(activeChar, true);
				break;
			
			case "admin_debug_stats":
				debug_stats(activeChar);
				break;
			
			case "admin_remove_cooldown":
				activeChar.resetReuse();
				activeChar.sendPacket(new SkillCoolTime(activeChar));
				break;
			
			case "admin_buff":
				for (int i = 7041; i <= 7064; i++)
				{
					activeChar.addSkill(SkillTable.getInstance().getInfo(i, 1));
				}
				
				activeChar.sendSkillList();
				break;
			
			case "admin_call_skill":
				List<Creature> target = new ArrayList<>();
				// Always use self as target. Can be changed with code bellow.
				// target.add(activeChar.getTarget() != null ? (Creature) activeChar.getTarget() : activeChar);
				target.add(activeChar);
				activeChar.callSkill(SkillTable.getInstance().getInfo(Integer.valueOf(wordList[1]), Integer.valueOf(wordList[2]) > 1 ? Integer.valueOf(wordList[2]) : 1), target, false);
				// Use method bellow if you want to cast skill / spend mana.
				// activeChar.doCast(SkillTable.getInstance().getInfo(Integer.valueOf(wordList[1]), Integer.valueOf(wordList[2]) > 1 ? Integer.valueOf(wordList[2]) : 1), activeChar.getTarget() != null ? (Creature) activeChar.getTarget() : activeChar, false);
				break;
		}
		
		return true;
	}
	
	/**
	 * Method debug_stats.
	 * @param activeChar Player
	 */
	private void debug_stats(Player activeChar)
	{
		GameObject target_obj = activeChar.getTarget();
		
		if (!target_obj.isCreature())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		Creature target = (Creature) target_obj;
		Calculator[] calculators = target.getCalculators();
		String log_str = "--- Debug for " + target.getName() + " ---\r\n";
		
		for (Calculator calculator : calculators)
		{
			if (calculator == null)
			{
				continue;
			}
			
			Env env = new Env(target, activeChar, null);
			env.value = calculator.getBase();
			log_str += "Stat: " + calculator._stat.getValue() + ", prevValue: " + calculator.getLast() + "\r\n";
			Func[] funcs = calculator.getFunctions();
			
			for (int i = 0; i < funcs.length; i++)
			{
				String order = Integer.toHexString(funcs[i].order).toUpperCase();
				
				if (order.length() == 1)
				{
					order = "0" + order;
				}
				
				log_str += "\tFunc #" + i + "@ [0x" + order + "]" + funcs[i].getClass().getSimpleName() + "\t" + env.value;
				
				if ((funcs[i].getCondition() == null) || funcs[i].getCondition().test(env))
				{
					funcs[i].calc(env);
				}
				
				log_str += " -> " + env.value + (funcs[i].owner != null ? "; owner: " + funcs[i].owner.toString() : "; no owner") + "\r\n";
			}
		}
		
		Log.add(log_str, "debug_stats");
	}
	
	/**
	 * Method adminGiveAllSkills.
	 * @param activeChar Player
	 */
	private void adminGiveAllSkills(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		Player player = null;
		
		if ((target != null) && target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		int unLearnable = 0;
		int skillCounter = 0;
		Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(player, AcquireType.NORMAL);
		
		while (skills.size() > unLearnable)
		{
			unLearnable = 0;
			
			for (SkillLearn s : skills)
			{
				Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
				
				if ((sk == null) || !sk.getCanLearn(player.getClassId()))
				{
					unLearnable++;
					continue;
				}
				
				if (player.getSkillLevel(sk.getId()) == -1)
				{
					skillCounter++;
				}
				
				player.addSkill(sk, true);
			}
			
			skills = SkillAcquireHolder.getInstance().getAvailableSkills(player, AcquireType.NORMAL);
		}
		
		player.sendMessage("Admin gave you " + skillCounter + " skills.");
		player.sendSkillList();
		activeChar.sendMessage("You gave " + skillCounter + " skills to " + player.getName());
	}
	
	/**
	 * This function will give all the skills that the target's clan can learn at it's level.<br>
	 * If the target is not the clan leader, a system message will be sent to the Game Master.
	 * @param activeChar the active char, probably a Game Master.
	 * @param includeSquad if Squad skills is included
	 */
	private void adminGiveClanSkills(Player activeChar, boolean includeSquad)
	{
		final GameObject target = activeChar.getTarget();
		if ((target == null) || !target.isPlayer())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		final Player player = target.getPlayer();
		final Clan clan = player.getClan();
		
		if (clan == null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.THE_TARGET_MUST_BE_A_CLAN_MEMBER));
			return;
		}
		
		if (!player.isClanLeader())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addName(player));
		}
		
		Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableSkills(player, AcquireType.CLAN);
		if ((includeSquad) && (clan.getSubUnit(0) != null))
		{
			Collection<SkillLearn> skillsSub;
			skillsSub = SkillAcquireHolder.getInstance().getAvailableSkills(player, AcquireType.SUB_UNIT, clan.getSubUnit(0));
			skills.addAll(skillsSub);
		}
		
		for (SkillLearn s : skills)
		{
			clan.addSkill(SkillTable.getInstance().getInfo(s.getId(), s.getLevel()), true);
		}
		
		// Notify target and active char
		clan.broadcastToOnlineMembers(new PledgeSkillList(clan));
		for (Player member : clan.getOnlineMembers(0))
		{
			member.sendSkillList();
		}
		
		activeChar.sendMessage("You gave " + skills.size() + " skills to " + player.getName() + "'s clan " + clan.getName() + ".");
		player.sendMessage("Your clan received " + skills.size() + " skills.");
	}
	
	/**
	 * Method removeSkillsPage.
	 * @param activeChar Player
	 */
	private void removeSkillsPage(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		Player player;
		
		if (target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		Collection<Skill> skills = player.getAllSkills();
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_show_skills\" width=45 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		replyMSG.append("<center>Editing character: " + player.getName() + "</center>");
		replyMSG.append("<br><table width=270><tr><td>Lv: " + player.getLevel() + " " + HtmlUtils.htmlClassName(player.getClassId().getId()) + "</td></tr></table>");
		replyMSG.append("<br><center>Click on the skill you wish to remove:</center>");
		replyMSG.append("<br><table width=270>");
		replyMSG.append("<tr><td width=80>Name:</td><td width=60>Level:</td><td width=40>Id:</td></tr>");
		
		for (Skill element : skills)
		{
			replyMSG.append("<tr><td width=80><a action=\"bypass -h admin_remove_skill " + element.getId() + "\">" + element.getName() + "</a></td><td width=60>" + element.getLevel() + "</td><td width=40>" + element.getId() + "</td></tr>");
		}
		
		replyMSG.append("</table>");
		replyMSG.append("<br><center><table>");
		replyMSG.append("Remove custom skill:");
		replyMSG.append("<tr><td>Id: </td>");
		replyMSG.append("<td><edit var=\"id_to_remove\" width=110></td></tr>");
		replyMSG.append("</table></center>");
		replyMSG.append("<center><button value=\"Remove skill\" action=\"bypass -h admin_remove_skill $id_to_remove\" width=110 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
		replyMSG.append("<br><center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=45 height=21></center>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method removeAllSkills.
	 * @param activeChar Player
	 */
	private void removeAllSkills(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		
		if (!(target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll)))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		Player player = (Player) target;
		Collection<Skill> skills = player.getAllSkills();
		
		for (Skill skill : skills)
		{
			if (skill == null)
			{
				continue;
			}
			
			player.removeSkill(skill, true);
			player.sendPacket(new SkillList(player), new ExAcquirableSkillListByClass(player));
		}
		
		activeChar.sendMessage("You removed all skills from " + player.getName() + ".");
		showSkillsPage(activeChar);
	}
	
	/**
	 * Method showSkillsPage.
	 * @param activeChar Player
	 */
	private void showSkillsPage(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		Player player;
		
		if ((target != null) && target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Player/Clan Skills Editor</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=45 height=21 back=\")L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table><br><br>");
		replyMSG.append("<center>Editing <font color=\"LEVEL\">" + player.getName() + "</font><br>");
		replyMSG.append("<table width=270><tr>");
		replyMSG.append("<td width=50>Class:</td>");
		replyMSG.append("<td width=160><font color=\"LEVEL\">" + HtmlUtils.htmlClassName(player.getClassId().getId()) + "</font></td>");
		replyMSG.append("<td width=20>Lv:</td>");
		replyMSG.append("<td width=40><font color=\"LEVEL\">" + player.getLevel() + "</font></td>");
		replyMSG.append("</tr></table><br>");
		replyMSG.append("============ Player ============<br>");
		replyMSG.append("<table width=270><tr>");
		replyMSG.append("<td><button value=\"Add skills\" action=\"bypass -h admin_skill_list\" width=130 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Take skills\" action=\"bypass -h admin_get_skills\" width=130 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr><tr>");
		replyMSG.append("<td><button value=\"Delete skills\" action=\"bypass -h admin_remove_skills 0\" width=130 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Give taken skills\" action=\"bypass -h admin_reset_skills\" width=130 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr><tr>");
		replyMSG.append("<td><button value=\"Give All Skills\" action=\"bypass -h admin_give_all_skills\" width=130 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Remove All Skills\" action=\"bypass -h admin_remove_all_skills\" width=130 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table><br>");
		replyMSG.append("============ Clan ============<br>");
		replyMSG.append("<table width=270><tr>");
		replyMSG.append("<td><button value=\"Give Clan Skills\" action=\"bypass -h admin_give_clan_skills\" width=130 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Give All Clan Skills\" action=\"bypass -h admin_give_all_clan_skills\" width=130 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table></center></body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method showEffects.
	 * @param activeChar Player
	 */
	private void showEffects(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		Player player;
		
		if ((target != null) && target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=45 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		replyMSG.append("<center>Editing character: " + player.getName() + "</center>");
		replyMSG.append("<br><center><button value=\"");
		replyMSG.append("Refresh");
		replyMSG.append("\" action=\"bypass -h admin_show_effects\" width=100 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" /></center>");
		replyMSG.append("<br>");
		List<Effect> list = player.getEffectList().getAllEffects();
		
		if ((list != null) && !list.isEmpty())
		{
			for (Effect e : list)
			{
				replyMSG.append(e.getSkill().getName()).append(' ').append(e.getSkill().getLevel()).append(" - ").append(e.getSkill().isToggle() ? "Infinity" : (e.getTimeLeft() + " seconds")).append("<br1>");
			}
		}
		
		replyMSG.append("<br></body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method adminGetSkills.
	 * @param activeChar Player
	 */
	private void adminGetSkills(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		Player player;
		
		if (target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		if (player.getName().equals(activeChar.getName()))
		{
			player.sendMessage("There is no point in doing it on your character.");
		}
		else
		{
			Collection<Skill> skills = player.getAllSkills();
			adminSkills = activeChar.getAllSkillsArray();
			
			for (Skill element : adminSkills)
			{
				activeChar.removeSkill(element, true);
			}
			
			for (Skill element : skills)
			{
				activeChar.addSkill(element, true);
			}
			
			activeChar.sendMessage("You now have all the skills of  " + player.getName() + ".");
		}
		
		showSkillsPage(activeChar);
	}
	
	/**
	 * Method adminResetSkills.
	 * @param activeChar Player
	 */
	private void adminResetSkills(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		Player player = null;
		
		if (target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		player.getAllSkillsArray();
		int counter = 0;
		player.checkSkills();
		player.sendSkillList();
		player.sendMessage("[GM]" + activeChar.getName() + " has updated your skills.");
		activeChar.sendMessage(counter + " skills removed.");
		showSkillsPage(activeChar);
	}
	
	/**
	 * Method adminAddSkill.
	 * @param activeChar Player
	 * @param wordList String[]
	 */
	private void adminAddSkill(Player activeChar, String[] wordList)
	{
		GameObject target = activeChar.getTarget();
		Player player;
		
		if ((target != null) && target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		if (wordList.length == 3)
		{
			int id = Integer.parseInt(wordList[1]);
			int level = Integer.parseInt(wordList[2]);
			Skill skill = SkillTable.getInstance().getInfo(id, level);
			
			if (skill != null)
			{
				player.sendMessage("Admin gave you the skill " + skill.getName() + ".");
				player.addSkill(skill, true);
				player.sendSkillList();
				activeChar.sendMessage("You gave the skill " + skill.getName() + " to " + player.getName() + ".");
			}
			else
			{
				activeChar.sendMessage("Error: there is no such skill.");
			}
		}
		
		showSkillsPage(activeChar);
	}
	
	/**
	 * Method adminRemoveSkill.
	 * @param activeChar Player
	 * @param wordList String[]
	 */
	private void adminRemoveSkill(Player activeChar, String[] wordList)
	{
		GameObject target = activeChar.getTarget();
		Player player = null;
		
		if (target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		if (wordList.length == 2)
		{
			int id = Integer.parseInt(wordList[1]);
			int level = player.getSkillLevel(id);
			Skill skill = SkillTable.getInstance().getInfo(id, level);
			
			if (skill != null)
			{
				player.sendMessage("Admin removed the skill " + skill.getName() + ".");
				player.removeSkill(skill, true);
				player.sendSkillList();
				activeChar.sendMessage("You removed the skill " + skill.getName() + " from " + player.getName() + ".");
			}
			else
			{
				activeChar.sendMessage("Error: there is no such skill.");
			}
		}
		
		removeSkillsPage(activeChar);
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IAdminCommandHandler#getAdminCommandList()
	 */
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
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