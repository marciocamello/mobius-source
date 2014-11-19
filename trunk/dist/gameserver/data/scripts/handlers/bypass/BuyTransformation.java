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

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.handlers.BypassHandler;
import lineage2.gameserver.handlers.IBypassHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 */
public final class BuyTransformation implements IBypassHandler, ScriptFile
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
			"BuyTransformation"
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
		if (!Config.ALLOW_LEARN_TRANS_SKILLS_WO_QUEST)
		{
			if (!player.isQuestCompleted("Q00136_MoreThanMeetsTheEye"))
			{
				npc.showChatWindow(player, "trainer/" + npc.getId() + "-nobuy.htm");
				return;
			}
		}
		
		Castle castle = npc.getCastle(player);
		MultiSellHolder.getInstance().SeparateAndSend(32323, player, castle != null ? castle.getTaxRate() : 0, npc.getId());
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