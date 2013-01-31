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

import java.util.Collection;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.base.ClassType2;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CertificationFunctions
{
	/**
	 * Field PATH. (value is ""villagemaster/certification/"")
	 */
	public static final String PATH = "villagemaster/certification/";
	
	/**
	 * Method showCertificationList.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void showCertificationList(NpcInstance npc, Player player)
	{
		if (!checkConditions(65, npc, player, true))
		{
			return;
		}
		Functions.show(PATH + "certificatelist.htm", player, npc);
	}
	
	/**
	 * Method getCertification65.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void getCertification65(NpcInstance npc, Player player)
	{
		if (!checkConditions(65, npc, player, Config.ALT_GAME_SUB_BOOK))
		{
			return;
		}
		SubClass clzz = player.getActiveSubClass();
		if (clzz.isCertificationGet(SubClass.CERTIFICATION_65))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}
		Functions.addItem(player, 10280, 1);
		clzz.addCertification(SubClass.CERTIFICATION_65);
		player.store(true);
	}
	
	/**
	 * Method getCertification70.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void getCertification70(NpcInstance npc, Player player)
	{
		if (!checkConditions(70, npc, player, Config.ALT_GAME_SUB_BOOK))
		{
			return;
		}
		SubClass clzz = player.getActiveSubClass();
		if (!clzz.isCertificationGet(SubClass.CERTIFICATION_65))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}
		if (clzz.isCertificationGet(SubClass.CERTIFICATION_70))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}
		Functions.addItem(player, 10280, 1);
		clzz.addCertification(SubClass.CERTIFICATION_70);
		player.store(true);
	}
	
	/**
	 * Method getCertification75.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void getCertification75(NpcInstance npc, Player player)
	{
		if (!checkConditions(75, npc, player, Config.ALT_GAME_SUB_BOOK))
		{
			return;
		}
		SubClass clzz = player.getActiveSubClass();
		if (!clzz.isCertificationGet(SubClass.CERTIFICATION_65))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}
		if (clzz.isCertificationGet(SubClass.CERTIFICATION_75))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}
		Functions.addItem(player, 10280, 1);
		clzz.addCertification(SubClass.CERTIFICATION_75);
		player.store(true);
	}
	
	/**
	 * Method getCertification80.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void getCertification80(NpcInstance npc, Player player)
	{
		if (!checkConditions(80, npc, player, Config.ALT_GAME_SUB_BOOK))
		{
			return;
		}
		SubClass clzz = player.getActiveSubClass();
		if (!clzz.isCertificationGet(SubClass.CERTIFICATION_65))
		{
			Functions.show(PATH + "certificate-fail.htm", player, npc);
			return;
		}
		if (clzz.isCertificationGet(SubClass.CERTIFICATION_80))
		{
			Functions.show(PATH + "certificate-already.htm", player, npc);
			return;
		}
		Functions.addItem(player, 10280, 1);
		clzz.addCertification(SubClass.CERTIFICATION_80);
		player.store(true);
	}
	
	/**
	 * Method cancelCertification.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static void cancelCertification(NpcInstance npc, Player player)
	{
		if (player.getInventory().getAdena() < 10000000)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		if (!player.getActiveSubClass().isBase())
		{
			return;
		}
		player.getInventory().reduceAdena(10000000);
		for (ClassType2 classType2 : ClassType2.VALUES)
		{
			player.getInventory().destroyItemByItemId(classType2.getCertificateId(), player.getInventory().getCountOf(classType2.getCertificateId()));
			player.getInventory().destroyItemByItemId(classType2.getTransformationId(), player.getInventory().getCountOf(classType2.getTransformationId()));
		}
		Collection<SkillLearn> skillLearnList = SkillAcquireHolder.getInstance().getAvailableSkills(null, AcquireType.CERTIFICATION);
		for (SkillLearn learn : skillLearnList)
		{
			Skill skill = player.getKnownSkill(learn.getId());
			if (skill != null)
			{
				player.removeSkill(skill, true);
			}
		}
		for (SubClass subClass : player.getSubClassList().values())
		{
			if (!subClass.isBase())
			{
				subClass.setCertification(0);
			}
		}
		player.sendSkillList();
		Functions.show(new CustomMessage("scripts.services.SubclassSkills.SkillsDeleted", player), player);
	}
	
	/**
	 * Method checkConditions.
	 * @param level int
	 * @param npc NpcInstance
	 * @param player Player
	 * @param first boolean
	 * @return boolean
	 */
	public static boolean checkConditions(int level, NpcInstance npc, Player player, boolean first)
	{
		if (player.getLevel() < level)
		{
			Functions.show(PATH + "certificate-nolevel.htm", player, npc, "%level%", level);
			return false;
		}
		if (player.getActiveSubClass().isBase())
		{
			Functions.show(PATH + "certificate-nosub.htm", player, npc);
			return false;
		}
		if (first)
		{
			return true;
		}
		for (ClassType2 type : ClassType2.VALUES)
		{
			if ((player.getInventory().getCountOf(type.getCertificateId()) > 0) || (player.getInventory().getCountOf(type.getTransformationId()) > 0))
			{
				Functions.show(PATH + "certificate-already.htm", player, npc);
				return false;
			}
		}
		return true;
	}
}
