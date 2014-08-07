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
package lineage2.gameserver.utils;

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.MenteeInfo;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Smo
 */
public class MentorUtil
{
	public static final Map<Integer, Integer> SIGN_OF_TUTOR = new HashMap<Integer, Integer>()
	{
		private static final long serialVersionUID = 1L;
		{
			put(10, 2);
			put(20, 25);
			put(30, 45);
			put(40, 109);
			put(50, 175);
			put(51, 179);
			put(52, 199);
			put(53, 221);
			put(54, 243);
			put(55, 266);
			put(56, 290);
			put(57, 315);
			put(58, 341);
			put(59, 367);
			put(60, 395);
			put(61, 424);
			put(62, 432);
			put(63, 461);
			put(64, 445);
			put(65, 473);
			put(66, 488);
			put(67, 516);
			put(68, 544);
			put(69, 573);
			put(70, 602);
			put(71, 561);
			put(72, 589);
			put(73, 618);
			put(74, 647);
			put(75, 676);
			put(76, 689);
			put(77, 488);
			put(78, 514);
			put(79, 542);
			put(80, 576);
			put(81, 726);
			put(82, 759);
			put(83, 793);
			put(84, 829);
			put(85, 863);
		}
	};
	private static final int[] effectsForMentee =
	{
		9227,
		9228,
		9229,
		9230,
		9231,
		9232,
		9233
	};
	private static final int skillForMentee = 9379;
	private static final int[] skillsForMentor =
	{
		9376,
		9377,
		9378
	};
	private static final int effectForMentor = 9256;
	private static final int[] effectsRemove =
	{
		9233,
		9227,
		9228,
		9229,
		9230,
		9231,
		9232,
		9256
	};
	private static final int[] skillRemove =
	{
		9376,
		9377,
		9378,
		9379
	};
	
	public static void applyMentoringConditions(Player activeChar)
	{
		if (activeChar.isMentor())
		{
			addEffectToPlayer(SkillTable.getInstance().getInfo(effectForMentor, 1), activeChar);
			
			for (MenteeInfo menteeInfo : activeChar.getMentorSystem().getMenteeInfo())
			{
				if (menteeInfo.isOnline())
				{
					Player mentee = World.getPlayer(menteeInfo.getObjectId());
					
					for (int effect : effectsForMentee)
					{
						addEffectToPlayer(SkillTable.getInstance().getInfo(effect, 1), mentee);
					}
				}
			}
		}
		else
		{
			for (int effect : effectsForMentee)
			{
				addEffectToPlayer(SkillTable.getInstance().getInfo(effect, 1), activeChar);
			}
			
			if (World.getPlayer(activeChar.getMentorSystem().getMentor()) == null)
			{
				return;
			}
			
			addEffectToPlayer(SkillTable.getInstance().getInfo(effectForMentor, 1), World.getPlayer(activeChar.getMentorSystem().getMentor()));
		}
	}
	
	private static void addEffectToPlayer(Skill skill, Player target)
	{
		for (EffectTemplate et : skill.getEffectTemplates())
		{
			Env env = new Env(target, target, skill);
			Effect effect = et.getEffect(env);
			target.getEffectList().addEffect(effect);
		}
	}
	
	public static void addSkillsToMentor(Player mentor)
	{
		if (mentor.getMentorSystem().getMentor() != 0)
		{
			return;
		}
		
		for (int skillId : skillsForMentor)
		{
			Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
			mentor.addSkill(skill, true);
			mentor.sendSkillList();
		}
	}
	
	public static void addSkillsToMentee(Player mentee)
	{
		if (mentee.getMentorSystem().getMentor() == 0)
		{
			return;
		}
		
		Skill skill = SkillTable.getInstance().getInfo(skillForMentee, 1);
		mentee.addSkill(skill, true);
		mentee.sendSkillList();
	}
	
	public static void removeConditions(Player player)
	{
		if (player.isMentor())
		{
			removeConditionsFromMentee(player);
			removeEffectsFromPlayer(player);
		}
		else
		{
			if ((player.isMentor()) || (World.getPlayer(player.getMentorSystem().getMentor()) == null))
			{
				return;
			}
			
			removeEffectsFromPlayer(player);
			Player mentor = World.getPlayer(player.getMentorSystem().getMentor());
			int menteeOnline = 0;
			
			for (MenteeInfo mentee : mentor.getMentorSystem().getMenteeInfo())
			{
				if (mentee.isOnline())
				{
					++menteeOnline;
				}
			}
			
			if (menteeOnline >= 1)
			{
				return;
			}
			
			removeEffectsFromPlayer(mentor);
		}
	}
	
	private static void removeConditionsFromMentee(Player player)
	{
		for (MenteeInfo mentee : player.getMentorSystem().getMenteeInfo())
		{
			Player activeChar = World.getPlayer(mentee.getObjectId());
			
			if (activeChar != null)
			{
				removeEffectsFromPlayer(activeChar);
			}
		}
	}
	
	public static void removeEffectsFromPlayer(Player activeChar)
	{
		for (int buff : effectsRemove)
		{
			activeChar.getEffectList().stopEffect(buff);
		}
	}
	
	public static void removeSkills(Player activeChar)
	{
		for (int skillToRemove : skillRemove)
		{
			Skill skill = SkillTable.getInstance().getInfo(skillToRemove, 1);
			activeChar.removeSkill(skill);
		}
	}
	
	public static void setTimePenalty(int mentorId, long timeTo, long expirationTime)
	{
		Player mentor = World.getPlayer(mentorId);
		
		if ((mentor != null) && mentor.isOnline())
		{
			mentor.setVar("mentorPenalty", timeTo, -1);
		}
		else
		{
			mysql.set("REPLACE INTO character_variables (obj_id, type, name, value, expire_time) VALUES (?,'user-var','mentorPenalty',?,?)", mentorId, timeTo, expirationTime);
		}
	}
	
	public static long getTimePenalty(int mentorId)
	{
		Player mentor = World.getPlayer(mentorId);
		
		if ((mentor != null) && mentor.isOnline())
		{
			return mentor.getVarLong("mentorPenalty");
		}
		
		return (long) mysql.get("SELECT value FROM character_variables WHERE obj_id = " + mentorId);
	}
	
	public static void newMenteeMail(Player newMentee)
	{
		if (newMentee == null)
		{
			return;
		}
		
		Mail mail = new Mail();
		mail.setSenderId(1);
		mail.setSenderName("Mentor Guide");
		mail.setReceiverId(newMentee.getObjectId());
		mail.setReceiverName(newMentee.getName());
		mail.setTopic("Congratulations on becoming a mentee.");
		mail.setBody("Greetings. This is the Mentor Guide.\\n\\nYou will experience a world of unlimited adventures with your mentor. Exciting, isn't it?\\n\\nWhen you graduate from mentee status upon awakening at level 85), you will receive a Mentee Certificate. If you bring it to me, I will give you a Diploma that you can exchange for R-grade equipment.\\n\\nHave fun in Lineage II with your mentor!");
		ItemInstance items = ItemFunctions.createItem(34759); // Mentee Headphone
		items.setLocation(ItemInstance.ItemLocation.MAIL);
		items.setCount(1);
		items.save();
		mail.addAttachment(items);
		mail.setUnread(true);
		mail.setType(Mail.SenderType.MENTOR);
		mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
		mail.save();
		newMentee.sendPacket(ExNoticePostArrived.STATIC_TRUE);
		newMentee.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
	}
	
	public static void sendMentorMail(Player receiver, String mentee, Map<Integer, Long> items)
	{
		if (receiver == null)
		{
			return;
		}
		
		if (items.keySet().size() > 8)
		{
			return;
		}
		
		int lvl = World.getPlayer(mentee).getLevel();
		Mail mail = new Mail();
		mail.setSenderId(1);
		mail.setSenderName(new CustomMessage("Mentor.npc", receiver, new Object[0]).toString());
		mail.setReceiverId(receiver.getObjectId());
		mail.setReceiverName(receiver.getName());
		mail.setTopic(new CustomMessage("Mentor.title", receiver, new Object[0]).toString());
		
		if (lvl < 50)
		{
			mail.setBody(new CustomMessage("Mentor.text1", receiver, new Object[0]).addNumber(lvl).addNumber(lvl + 1).addString(mentee).toString());
		}
		else
		{
			mail.setBody(new CustomMessage("Mentor.text2", receiver, new Object[0]).addNumber(lvl).addString(mentee).toString());
		}
		
		for (Map.Entry<Integer, Long> itm : items.entrySet())
		{
			ItemInstance item = ItemFunctions.createItem(itm.getKey());
			item.setLocation(ItemInstance.ItemLocation.MAIL);
			item.setCount(itm.getValue());
			item.save();
			mail.addAttachment(item);
		}
		
		mail.setType(Mail.SenderType.MENTOR);
		mail.setUnread(true);
		mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
		mail.save();
		receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
		receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
	}
	
	public static void graduateMenteeMail(Player newMentee)
	{
		if (newMentee == null)
		{
			return;
		}
		
		Mail mail = new Mail();
		mail.setSenderId(1);
		mail.setSenderName("Mentor Guide");
		mail.setReceiverId(newMentee.getObjectId());
		mail.setReceiverName(newMentee.getName());
		mail.setTopic("Congratulations you graduate.");
		mail.setBody("Greetings. This is the Mentor Guide.\\n\\nTODO: need right text message.");
		ItemInstance items = ItemFunctions.createItem(33800); // Mentee Certificate
		items.setLocation(ItemInstance.ItemLocation.MAIL);
		items.setCount(1);
		items.save();
		mail.addAttachment(items);
		mail.setUnread(true);
		mail.setType(Mail.SenderType.MENTOR);
		mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
		mail.save();
		newMentee.sendPacket(ExNoticePostArrived.STATIC_TRUE);
		newMentee.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
	}
}