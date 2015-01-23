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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.MonsterRace;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.DeleteObject;
import lineage2.gameserver.network.serverpackets.MonRaceInfo;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class AdminMonsterRace implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_mons"
	};
	
	static int state = -1;
	
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
		if (fullString.equals("admin_mons"))
		{
			if (!activeChar.getPlayerAccess().MonsterRace)
			{
				return false;
			}
			
			handleSendPacket(activeChar);
		}
		
		return true;
	}
	
	/**
	 * Method handleSendPacket.
	 * @param activeChar Player
	 */
	private void handleSendPacket(Player activeChar)
	{
		int[][] codes =
		{
			{
				-1,
				0
			},
			{
				0,
				15322
			},
			{
				13765,
				-1
			},
			{
				-1,
				0
			}
		};
		MonsterRace race = MonsterRace.getInstance();
		
		if (state == -1)
		{
			state++;
			race.newRace();
			race.newSpeeds();
			activeChar.broadcastPacket(new MonRaceInfo(codes[state][0], codes[state][1], race.getMonsters(), race.getSpeeds()));
		}
		else if (state == 0)
		{
			state++;
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THEY_RE_OFF));
			activeChar.broadcastPacket(new PlaySound("S_Race"));
			activeChar.broadcastPacket(new PlaySound(PlaySound.Type.SOUND, "ItemSound2.race_start", 1, 121209259, new Location(12125, 182487, -3559)));
			activeChar.broadcastPacket(new MonRaceInfo(codes[state][0], codes[state][1], race.getMonsters(), race.getSpeeds()));
			ThreadPoolManager.getInstance().schedule(new RunRace(codes, activeChar), 5000);
		}
	}
	
	private class RunRace extends RunnableImpl
	{
		private final int[][] codes;
		private final Player activeChar;
		
		/**
		 * Constructor for RunRace.
		 * @param codes int[][]
		 * @param activeChar Player
		 */
		RunRace(int[][] codes, Player activeChar)
		{
			this.codes = codes;
			this.activeChar = activeChar;
		}
		
		/**
		 * Method runImpl.
		 * @throws Exception
		 */
		@Override
		public void runImpl() throws Exception
		{
			activeChar.broadcastPacket(new MonRaceInfo(codes[2][0], codes[2][1], MonsterRace.getInstance().getMonsters(), MonsterRace.getInstance().getSpeeds()));
			ThreadPoolManager.getInstance().schedule(new RunEnd(activeChar), 30000);
		}
	}
	
	private class RunEnd extends RunnableImpl
	{
		private final Player activeChar;
		
		/**
		 * Constructor for RunEnd.
		 * @param activeChar Player
		 */
		RunEnd(Player activeChar)
		{
			this.activeChar = activeChar;
		}
		
		/**
		 * Method runImpl.
		 * @throws Exception
		 */
		@Override
		public void runImpl() throws Exception
		{
			NpcInstance obj;
			
			for (int i = 0; i < 8; i++)
			{
				obj = MonsterRace.getInstance().getMonsters()[i];
				activeChar.broadcastPacket(new DeleteObject(obj));
			}
			
			state = -1;
		}
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
