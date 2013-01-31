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
package npc.model;

import java.util.Collection;
import java.util.Set;
import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.actor.instances.player.SubClassInfo;
import lineage2.gameserver.model.actor.instances.player.SubClassList;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.SubClassTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.CertificationFunctions;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SubClassManagerInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is 1)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field CERTIFICATE_ID. (value is 30433)
	 */
	private static final int CERTIFICATE_ID = 30433;
	
	/**
	 * Constructor for SubClassManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SubClassManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		StringTokenizer st = new StringTokenizer(command, "_");
		String cmd = st.nextToken();
		if (cmd.equalsIgnoreCase("subclass"))
		{
			if (player.getSummonList().size() > 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no_servitor.htm");
				return;
			}
			if (player.getTransformation() != 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no_transform.htm");
				return;
			}
			if ((player.getWeightPenalty() >= 3) || ((player.getInventoryLimit() * 0.8) < player.getInventory().getSize()))
			{
				showChatWindow(player, "default/" + getNpcId() + "-no_weight.htm");
				return;
			}
			if (player.getLevel() < 40)
			{
				showChatWindow(player, "default/" + getNpcId() + "-no_level.htm");
				return;
			}
			String cmd2 = st.nextToken();
			if (cmd2.equalsIgnoreCase("add"))
			{
				if (!checkSubClassQuest(player))
				{
					showChatWindow(player, "default/" + getNpcId() + "-no_quest.htm");
					return;
				}
				if (player.getSubClassList().size() >= SubClassList.MAX_SUB_COUNT)
				{
					showChatWindow(player, "default/" + getNpcId() + "-add_no_limit.htm");
					return;
				}
				Collection<SubClass> subClasses = player.getSubClassList().values();
				for (SubClass subClass : subClasses)
				{
					if (subClass.getLevel() < Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS)
					{
						showChatWindow(player, "default/" + getNpcId() + "-add_no_level.htm", "<?LEVEL?>", Config.ALT_GAME_LEVEL_TO_GET_SUBCLASS);
						return;
					}
				}
				if (!st.hasMoreTokens())
				{
					StringBuilder availSubList = new StringBuilder();
					Set<ClassId> availSubClasses = SubClassInfo.getAvailableSubClasses(player, null, null, true);
					for (ClassId subClsId : availSubClasses)
					{
						availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_add_" + subClsId.getId() + "\">" + HtmlUtils.htmlClassName(subClsId.getId()) + "</a><br>");
					}
					showChatWindow(player, "default/" + getNpcId() + "-add_list.htm", "<?ADD_SUB_LIST?>", availSubList.toString());
					return;
				}
				int addSubClassId = Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					String addSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_add_" + addSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(addSubClassId) + "</a>";
					showChatWindow(player, "default/" + getNpcId() + "-add_confirm.htm", "<?ADD_SUB_CONFIRM?>", addSubConfirm);
					return;
				}
				String cmd3 = st.nextToken();
				if (cmd3.equalsIgnoreCase("confirm"))
				{
					if (Config.ENABLE_OLYMPIAD && Olympiad.isRegisteredInComp(player))
					{
						player.sendPacket(SystemMsg.C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_SUBCLASS_CHARACTER_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD);
						return;
					}
					if (player.addSubClass(addSubClassId, true, 0))
					{
						player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
						showChatWindow(player, "default/" + getNpcId() + "-add_success.htm");
						return;
					}
					showChatWindow(player, "default/" + getNpcId() + "-add_error.htm");
					return;
				}
			}
			else if (cmd2.equalsIgnoreCase("change"))
			{
				if (!player.getSubClassList().haveSubClasses())
				{
					showChatWindow(player, "default/" + getNpcId() + "-no_quest.htm");
					return;
				}
				if (ItemFunctions.getItemCount(player, CERTIFICATE_ID) == 0)
				{
					showChatWindow(player, "default/" + getNpcId() + "-no_certificate.htm");
					return;
				}
			}
			else if (cmd2.equalsIgnoreCase("cancel"))
			{
				if (!checkSubClassQuest(player) && !player.getSubClassList().haveSubClasses())
				{
					showChatWindow(player, "default/" + getNpcId() + "-no_quest.htm");
					return;
				}
				if (checkSubClassQuest(player) && !player.getSubClassList().haveSubClasses())
				{
					showChatWindow(player, "default/" + getNpcId() + "-cancel_no_subs.htm");
					return;
				}
				if (!st.hasMoreTokens())
				{
					StringBuilder mySubList = new StringBuilder();
					Collection<SubClass> subClasses = player.getSubClassList().values();
					for (SubClass sub : subClasses)
					{
						if (sub == null)
						{
							continue;
						}
						if (sub.isBase())
						{
							continue;
						}
						if (sub.isDouble())
						{
							continue;
						}
						int classId = sub.getClassId();
						mySubList.append("<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + classId + "\">" + HtmlUtils.htmlClassName(classId) + "</a><br>");
					}
					showChatWindow(player, "default/" + getNpcId() + "-cancel_list.htm", "<?CANCEL_SUB_LIST?>", mySubList.toString());
					return;
				}
				int cancelClassId = Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					StringBuilder availSubList = new StringBuilder();
					int[] availSubClasses = SubClassTable.getInstance().getAvailableSubClasses(player, cancelClassId);
					for (int subClsId : availSubClasses)
					{
						availSubList.append("<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + cancelClassId + "_" + subClsId + "\">" + HtmlUtils.htmlClassName(subClsId) + "</a><br>");
					}
					showChatWindow(player, "default/" + getNpcId() + "-cancel_change_list.htm", "<?CANCEL_CHANGE_SUB_LIST?>", availSubList.toString());
					return;
				}
				int newSubClassId = Integer.parseInt(st.nextToken());
				if (!st.hasMoreTokens())
				{
					String newSubConfirm = "<a action=\"bypass -h npc_%objectId%_subclass_cancel_" + cancelClassId + "_" + newSubClassId + "_confirm\">" + HtmlUtils.htmlClassName(newSubClassId) + "</a>";
					showChatWindow(player, "default/" + getNpcId() + "-cancel_confirm.htm", "<?CANCEL_SUB_CONFIRM?>", newSubConfirm);
					return;
				}
				String cmd3 = st.nextToken();
				if (cmd3.equalsIgnoreCase("confirm"))
				{
					if (player.modifySubClass(cancelClassId, newSubClassId))
					{
						player.sendPacket(SystemMsg.THE_NEW_SUBCLASS_HAS_BEEN_ADDED);
						showChatWindow(player, "default/" + getNpcId() + "-add_success.htm");
						return;
					}
					showChatWindow(player, "default/" + getNpcId() + "-add_error.htm");
					return;
				}
			}
			else if (cmd2.equalsIgnoreCase("CertificationList"))
			{
				CertificationFunctions.showCertificationList(this, player);
			}
			else if (cmd2.equalsIgnoreCase("GetCertification65"))
			{
				CertificationFunctions.getCertification65(this, player);
			}
			else if (cmd2.equalsIgnoreCase("GetCertification70"))
			{
				CertificationFunctions.getCertification70(this, player);
			}
			else if (cmd2.equalsIgnoreCase("GetCertification75"))
			{
				CertificationFunctions.getCertification75(this, player);
			}
			else if (cmd2.equalsIgnoreCase("GetCertification80"))
			{
				CertificationFunctions.getCertification80(this, player);
			}
			else if (cmd2.equalsIgnoreCase("CertificationSkillList"))
			{
				showSertifikationSkillList(player, AcquireType.CERTIFICATION);
			}
			else if (cmd2.equalsIgnoreCase("CertificationCancel"))
			{
				CertificationFunctions.cancelCertification(this, player);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method showSertifikationSkillList.
	 * @param player Player
	 * @param type AcquireType
	 */
	public void showSertifikationSkillList(Player player, AcquireType type)
	{
		if (!Config.ALLOW_LEARN_TRANS_SKILLS_WO_QUEST)
		{
			if (!player.isQuestCompleted("_136_MoreThanMeetsTheEye"))
			{
				showChatWindow(player, "trainer/" + getNpcId() + "-noquest.htm");
				return;
			}
		}
		showAcquireList(type, player);
	}
	
	/**
	 * Method checkSubClassQuest.
	 * @param player Player
	 * @return boolean
	 */
	private static boolean checkSubClassQuest(Player player)
	{
		if (!Config.ALT_GAME_SUBCLASS_WITHOUT_QUESTS)
		{
			if (player.isQuestCompleted("_234_FatesWhisper"))
			{
				if (player.getRace() == Race.kamael)
				{
					return player.isQuestCompleted("_236_SeedsOfChaos");
				}
				return player.isQuestCompleted("_235_MimirsElixir");
			}
			return false;
		}
		return true;
	}
}
