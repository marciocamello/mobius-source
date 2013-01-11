/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.utils.MapUtils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Announcements
{
	public class Announce extends RunnableImpl
	{
		private Future<?> _task;
		private final int _time;
		private final String _announce;
		
		public Announce(int t, String announce)
		{
			_time = t;
			_announce = announce;
		}
		
		@Override
		public void runImpl()
		{
			announceToAll(_announce);
		}
		
		public void showAnnounce(Player player)
		{
			Say2 cs = new Say2(0, ChatType.ANNOUNCEMENT, player.getName(), _announce);
			player.sendPacket(cs);
		}
		
		public void start()
		{
			if (_time > 0)
			{
				_task = ThreadPoolManager.getInstance().scheduleAtFixedRate(this, _time * 1000L, _time * 1000L);
			}
		}
		
		public void stop()
		{
			if (_task != null)
			{
				_task.cancel(false);
				_task = null;
			}
		}
		
		public int getTime()
		{
			return _time;
		}
		
		public String getAnnounce()
		{
			return _announce;
		}
	}
	
	private static final Logger _log = LoggerFactory.getLogger(Announcements.class);
	private static final Announcements _instance = new Announcements();
	
	public static final Announcements getInstance()
	{
		return _instance;
	}
	
	private final List<Announce> _announcements = new ArrayList<>();
	
	private Announcements()
	{
		loadAnnouncements();
	}
	
	public List<Announce> getAnnouncements()
	{
		return _announcements;
	}
	
	public void loadAnnouncements()
	{
		_announcements.clear();
		try
		{
			List<String> lines = Arrays.asList(FileUtils.readFileToString(new File("config/announcements.txt"), "UTF-8").split("\n"));
			for (String line : lines)
			{
				StringTokenizer token = new StringTokenizer(line, "\t");
				if (token.countTokens() > 1)
				{
					addAnnouncement(Integer.parseInt(token.nextToken()), token.nextToken(), false);
				}
				else
				{
					addAnnouncement(0, line, false);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error while loading config/announcements.txt!");
		}
	}
	
	public void showAnnouncements(Player activeChar)
	{
		for (Announce announce : _announcements)
		{
			announce.showAnnounce(activeChar);
		}
	}
	
	public void addAnnouncement(int val, String text, boolean save)
	{
		Announce announce = new Announce(val, text);
		announce.start();
		_announcements.add(announce);
		if (save)
		{
			saveToDisk();
		}
	}
	
	public void delAnnouncement(int line)
	{
		Announce announce = _announcements.remove(line);
		if (announce != null)
		{
			announce.stop();
		}
		saveToDisk();
	}
	
	private void saveToDisk()
	{
		try
		{
			File f = new File("config/announcements.txt");
			FileWriter writer = new FileWriter(f, false);
			for (Announce announce : _announcements)
			{
				writer.write(announce.getTime() + "\t" + announce.getAnnounce() + "\n");
			}
			writer.close();
		}
		catch (Exception e)
		{
			_log.error("Error while saving config/announcements.txt!", e);
		}
	}
	
	public void announceToAll(String text)
	{
		announceToAll(text, ChatType.ANNOUNCEMENT);
	}
	
	public static void shout(Creature activeChar, String text, ChatType type)
	{
		Say2 cs = new Say2(activeChar.getObjectId(), type, activeChar.getName(), text);
		int rx = MapUtils.regionX(activeChar);
		int ry = MapUtils.regionY(activeChar);
		int offset = Config.SHOUT_OFFSET;
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if ((player == activeChar) || (activeChar.getReflection() != player.getReflection()))
			{
				continue;
			}
			int tx = MapUtils.regionX(player);
			int ty = MapUtils.regionY(player);
			if (((tx >= (rx - offset)) && (tx <= (rx + offset)) && (ty >= (ry - offset)) && (ty <= (ry + offset))) || activeChar.isInRangeZ(player, Config.CHAT_RANGE))
			{
				player.sendPacket(cs);
			}
		}
		activeChar.sendPacket(cs);
	}
	
	public void announceToAll(String text, ChatType type)
	{
		Say2 cs = new Say2(0, type, "", text);
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(cs);
		}
	}
	
	public void announceByCustomMessage(String address, String[] replacements)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			announceToPlayerByCustomMessage(player, address, replacements);
		}
	}
	
	public void announceByCustomMessage(String address, String[] replacements, ChatType type)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			announceToPlayerByCustomMessage(player, address, replacements, type);
		}
	}
	
	public void announceToPlayerByCustomMessage(Player player, String address, String[] replacements)
	{
		CustomMessage cm = new CustomMessage(address, player);
		if (replacements != null)
		{
			for (String s : replacements)
			{
				cm.addString(s);
			}
		}
		player.sendPacket(new Say2(0, ChatType.ANNOUNCEMENT, "", cm.toString()));
	}
	
	public void announceToPlayerByCustomMessage(Player player, String address, String[] replacements, ChatType type)
	{
		CustomMessage cm = new CustomMessage(address, player);
		if (replacements != null)
		{
			for (String s : replacements)
			{
				cm.addString(s);
			}
		}
		player.sendPacket(new Say2(0, type, "", cm.toString()));
	}
	
	public void announceToAll(SystemMessage sm)
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(sm);
		}
	}
}
