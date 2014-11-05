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
package services.community;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handlers.CommunityBoardManager;
import lineage2.gameserver.handlers.ICommunityBoardHandler;
import lineage2.gameserver.instancemanager.RaidBossSpawnManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.BbsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bosses.AntharasManager;
import bosses.BaiumManager;
import bosses.SailrenManager;
import bosses.ValakasManager;

/**
 * @author Mobius
 */
public final class BossStatus implements ScriptFile, ICommunityBoardHandler
{
	private static final Logger _log = LoggerFactory.getLogger(BossStatus.class);
	
	private static final String[] _commands =
	{
		"_bbsraidstatus"
	};
	
	/**
	 * Method onBypassCommand.
	 * @param player Player
	 * @param bypass String
	 * @see lineage2.gameserver.handlers.ICommunityBoardHandler#onBypassCommand(Player, String)
	 */
	@Override
	public void onBypassCommand(Player player, String bypass)
	{
		if (!player.checkAllowAction())
		{
			return;
		}
		
		if (bypass.equals(_commands[0]))
		{
			String html = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/raidboss_status.htm", player);
			
			html = html.replace("%antharas_status%", AntharasManager._state.getRespawnDate() > 0 ? "<font color=\"D70000\"> Dead</font>" : "<font color=\"00CC00\">Alive</font>");
			html = html.replace("%baium_status%", BaiumManager._state.getRespawnDate() > 0 ? "<font color=\"D70000\">Dead</font>" : "<font color=\"00CC00\">Alive</font>");
			html = html.replace("%valakas_status%", ValakasManager._state.getRespawnDate() > 0 ? "<font color=\"D70000\">Dead</font>" : "<font color=\"00CC00\">Alive</font>");
			html = html.replace("%queen_ant_status%", RaidBossSpawnManager.getInstance().getRespawnDelay(29001) > 0 ? "<font color=\"00CC00\">Alive</font>" : "<font color=\"D70000\">Dead</font>");
			html = html.replace("%sailren_status%", SailrenManager._state.getRespawnDate() > 0 ? "<font color=\"D70000\">Dead</font>" : "<font color=\"00CC00\">Alive</font>");
			// html = html.replace("%zaken_status%", RaidBossSpawnManager.getInstance().getRespawnDelay(29818) > 0 ? "<font color=\"00CC00\">Alive</font>" : "<font color=\"D70000\">Dead</font>");
			// html = html.replace("%baylor_status%", BaylorManager._state.getRespawnDate() > 0 ? "<font color=\"00CC00\">Alive</font>" : "<font color=\"D70000\">Dead</font>");
			ShowBoard.separateAndSend(BbsUtil.htmlAll(html, player), player);
		}
	}
	
	/**
	 * Method onWriteCommand.
	 * @param player Player
	 * @param bypass String
	 * @param arg1 String
	 * @param arg2 String
	 * @param arg3 String
	 * @param arg4 String
	 * @param arg5 String
	 * @see lineage2.gameserver.handlers.ICommunityBoardHandler#onWriteCommand(Player, String, String, String, String, String, String)
	 */
	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}
	
	/**
	 * Method getBypassCommands.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.ICommunityBoardHandler#getBypassCommands()
	 */
	@Override
	public String[] getBypassCommands()
	{
		return _commands;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: Raidboss Status loaded.");
			CommunityBoardManager.getInstance().registerHandler(this);
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			CommunityBoardManager.getInstance().removeHandler(this);
		}
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