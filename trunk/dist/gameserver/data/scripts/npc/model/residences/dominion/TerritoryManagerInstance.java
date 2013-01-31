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
package npc.model.residences.dominion;

import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.ItemFunctions;
import quests._234_FatesWhisper;
import quests._235_MimirsElixir;
import quests._236_SeedsOfChaos;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TerritoryManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for TerritoryManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public TerritoryManagerInstance(int objectId, NpcTemplate template)
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
		Dominion dominion = getDominion();
		DominionSiegeEvent siegeEvent = dominion.getSiegeEvent();
		int npcId = getNpcId();
		int badgeId = 13676 + dominion.getId();
		if (command.equalsIgnoreCase("buyspecial"))
		{
			if (Functions.getItemCount(player, badgeId) < 1)
			{
				showChatWindow(player, 1);
			}
			else
			{
				MultiSellHolder.getInstance().SeparateAndSend(npcId, player, 0);
			}
		}
		else if (command.equalsIgnoreCase("buyNobless"))
		{
			if (player.isNoble())
			{
				return;
			}
			if (player.consumeItem(badgeId, 100L))
			{
				Quest q = QuestManager.getQuest(_234_FatesWhisper.class);
				QuestState qs = player.getQuestState(q.getClass());
				if (qs != null)
				{
					qs.exitCurrentQuest(true);
				}
				q.newQuestState(player, Quest.COMPLETED);
				if (player.getRace() == Race.kamael)
				{
					qs = player.getQuestState(_236_SeedsOfChaos.class);
					if (qs != null)
					{
						qs.exitCurrentQuest(true);
					}
					q.newQuestState(player, Quest.COMPLETED);
				}
				else
				{
					q = QuestManager.getQuest(_235_MimirsElixir.class);
					qs = player.getQuestState(q.getClass());
					if (qs != null)
					{
						qs.exitCurrentQuest(true);
					}
					q.newQuestState(player, Quest.COMPLETED);
				}
				Olympiad.addNoble(player);
				player.setNoble(true);
				player.updatePledgeClass();
				player.updateNobleSkills();
				player.sendSkillList();
				player.broadcastUserInfo(true);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
		}
		else if (command.equalsIgnoreCase("calculate"))
		{
			if (!player.isQuestContinuationPossible(true))
			{
				return;
			}
			int[] rewards = siegeEvent.calculateReward(player);
			if ((rewards == null) || (rewards[0] == 0))
			{
				showChatWindow(player, 4);
				return;
			}
			NpcHtmlMessage html = new NpcHtmlMessage(player, this, getHtmlPath(npcId, 5, player), 5);
			html.replace("%territory%", HtmlUtils.htmlResidenceName(dominion.getId()));
			html.replace("%badges%", String.valueOf(rewards[0]));
			html.replace("%adena%", String.valueOf(rewards[1]));
			html.replace("%fame%", String.valueOf(rewards[2]));
			player.sendPacket(html);
		}
		else if (command.equalsIgnoreCase("recivelater"))
		{
			showChatWindow(player, getHtmlPath(npcId, 6, player));
		}
		else if (command.equalsIgnoreCase("recive"))
		{
			int[] rewards = siegeEvent.calculateReward(player);
			if ((rewards == null) || (rewards[0] == 0))
			{
				showChatWindow(player, 4);
				return;
			}
			ItemFunctions.addItem(player, badgeId, rewards[0], true);
			ItemFunctions.addItem(player, ItemTemplate.ITEM_ID_ADENA, rewards[1], true);
			if (rewards[2] > 0)
			{
				player.setFame(player.getFame() + rewards[2], "CalcBadges:" + dominion.getId());
			}
			siegeEvent.clearReward(player.getObjectId());
			showChatWindow(player, 7);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		if ((player.getLevel() < 40) || (player.getClassLevel() <= 2))
		{
			val = 8;
		}
		return val == 0 ? "residence2/dominion/TerritoryManager.htm" : "residence2/dominion/TerritoryManager-" + val + ".htm";
	}
}
