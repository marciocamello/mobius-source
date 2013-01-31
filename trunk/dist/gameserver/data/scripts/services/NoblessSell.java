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
package services;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import quests._234_FatesWhisper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NoblessSell extends Functions
{
	/**
	 * Method get.
	 */
	public void get()
	{
		Player player = getSelf();
		if (player.isNoble())
		{
			return;
		}
		if (player.getSubLevel() < 75)
		{
			player.sendMessage("You must make sub class level 75 first.");
			return;
		}
		if (player.getInventory().destroyItemByItemId(Config.SERVICES_NOBLESS_SELL_ITEM, Config.SERVICES_NOBLESS_SELL_PRICE))
		{
			makeSubQuests();
			becomeNoble();
		}
		else if (Config.SERVICES_NOBLESS_SELL_ITEM == 57)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
	}
	
	/**
	 * Method makeSubQuests.
	 */
	public void makeSubQuests()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		Quest q = QuestManager.getQuest(_234_FatesWhisper.class);
		QuestState qs = player.getQuestState(q.getClass());
		if (qs != null)
		{
			qs.exitCurrentQuest(true);
		}
		q.newQuestState(player, Quest.COMPLETED);
		if (player.getRace() == Race.kamael)
		{
			q = QuestManager.getQuest("_236_SeedsOfChaos");
			qs = player.getQuestState(q.getClass());
			if (qs != null)
			{
				qs.exitCurrentQuest(true);
			}
			q.newQuestState(player, Quest.COMPLETED);
		}
		else
		{
			q = QuestManager.getQuest("_235_MimirsElixir");
			qs = player.getQuestState(q.getClass());
			if (qs != null)
			{
				qs.exitCurrentQuest(true);
			}
			q.newQuestState(player, Quest.COMPLETED);
		}
	}
	
	/**
	 * Method becomeNoble.
	 */
	public void becomeNoble()
	{
		Player player = getSelf();
		if ((player == null) || player.isNoble())
		{
			return;
		}
		Olympiad.addNoble(player);
		player.setNoble(true);
		player.updatePledgeClass();
		player.updateNobleSkills();
		player.sendSkillList();
		player.broadcastUserInfo(true);
	}
}
