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
package lineage2.gameserver.data.htm;

import java.io.File;
import java.io.IOException;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Language;
import lineage2.gameserver.utils.Strings;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HtmCache
{
	public static final int DISABLED = 0;
	public static final int LAZY = 1;
	public static final int ENABLED = 2;
	private static final Logger _log = LoggerFactory.getLogger(HtmCache.class);
	private final static HtmCache _instance = new HtmCache();
	
	/**
	 * Method getInstance.
	 * @return HtmCache
	 */
	public static HtmCache getInstance()
	{
		return _instance;
	}
	
	private final Cache[] _cache = new Cache[Language.VALUES.length];
	
	/**
	 * Constructor for HtmCache.
	 */
	private HtmCache()
	{
		for (int i = 0; i < _cache.length; i++)
		{
			_cache[i] = CacheManager.getInstance().getCache(getClass().getName() + "." + Language.VALUES[i].name());
		}
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		clear();
		
		switch (Config.HTM_CACHE_MODE)
		{
			case ENABLED:
				File root = new File(Config.DATAPACK_ROOT, "data/html");
				if (!root.exists())
				{
					_log.info("HtmCache: Could not find html folder!");
				}
				
				load(root, root.getAbsolutePath() + "/");
				
				for (Cache c : _cache)
				{
					_log.info(String.format("HtmCache: Parsed %d documents.", c.getSize()));
				}
				break;
			
			case LAZY:
				_log.info("HtmCache: Lazy cache mode.");
				break;
			
			case DISABLED:
				_log.info("HtmCache: Disabled.");
				break;
		}
	}
	
	/**
	 * Method load.
	 * @param f File
	 * @param rootPath String
	 */
	private void load(File f, final String rootPath)
	{
		if (!f.exists())
		{
			_log.info("HtmCache: Could not find file: " + f);
			return;
		}
		
		File[] files = f.listFiles();
		
		for (File file : files)
		{
			if (file.isDirectory())
			{
				load(file, rootPath);
			}
			else
			{
				final String fName = file.getName();
				if (fName.endsWith(".htm") || fName.endsWith(".html"))
				{
					try
					{
						putContent(file, rootPath);
					}
					catch (IOException e)
					{
						_log.info("HtmCache: file error" + e, e);
					}
				}
			}
		}
	}
	
	/**
	 * Method putContent.
	 * @param f File
	 * @param rootPath String
	 * @throws IOException
	 */
	private void putContent(File f, final String rootPath) throws IOException
	{
		String content = FileUtils.readFileToString(f, "UTF-8");
		String path = f.getAbsolutePath().substring(rootPath.length()).replace("\\", "/");
		_cache[Language.ENGLISH.ordinal()].put(new Element(path.toLowerCase(), Strings.bbParse(content)));
	}
	
	/**
	 * Method getNotNull.
	 * @param fileName String
	 * @param player Player
	 * @return String
	 */
	public String getNotNull(String fileName, Player player)
	{
		String cache = getCache(fileName);
		
		if (StringUtils.isEmpty(cache))
		{
			cache = "Dialog not found: " + fileName;
		}
		
		return cache;
	}
	
	/**
	 * Method getNullable.
	 * @param fileName String
	 * @param player Player
	 * @return String
	 */
	public String getNullable(String fileName, Player player)
	{
		String cache = getCache(fileName);
		
		if (StringUtils.isEmpty(cache))
		{
			return null;
		}
		
		return cache;
	}
	
	/**
	 * Method getCache.
	 * @param file String
	 * @return String
	 */
	private String getCache(String file)
	{
		if (file == null)
		{
			return null;
		}
		
		String cache = get(file.toLowerCase());
		
		if (cache == null)
		{
			switch (Config.HTM_CACHE_MODE)
			{
				case ENABLED:
					break;
				
				case LAZY:
					cache = loadLazy(file);
					break;
				
				case DISABLED:
					cache = loadDisabled(file);
					break;
			}
		}
		
		return cache;
	}
	
	/**
	 * Method loadDisabled.
	 * @param file String
	 * @return String
	 */
	private String loadDisabled(String file)
	{
		String cache = null;
		File f = new File(Config.DATAPACK_ROOT, "data/html/" + file);
		
		if (f.exists())
		{
			try
			{
				cache = FileUtils.readFileToString(f, "UTF-8");
				cache = Strings.bbParse(cache);
			}
			catch (IOException e)
			{
				_log.info("HtmCache: File error: " + file);
			}
		}
		
		return cache;
	}
	
	/**
	 * Method loadLazy.
	 * @param file String
	 * @return String
	 */
	private String loadLazy(String file)
	{
		String cache = null;
		File f = new File(Config.DATAPACK_ROOT, "data/html/" + file);
		
		if (f.exists())
		{
			try
			{
				cache = FileUtils.readFileToString(f, "UTF-8");
				cache = Strings.bbParse(cache);
				_cache[Language.ENGLISH.ordinal()].put(new Element(file, cache));
			}
			catch (IOException e)
			{
				_log.info("HtmCache: File error: " + file);
			}
		}
		
		return cache;
	}
	
	/**
	 * Method get.
	 * @param f String
	 * @return String
	 */
	private String get(String f)
	{
		final Element element = _cache[Language.ENGLISH.ordinal()].get(f);
		return element == null ? null : (String) element.getObjectValue();
	}
	
	/**
	 * Method clear.
	 */
	public void clear()
	{
		for (Cache element : _cache)
		{
			element.removeAll();
		}
	}
}
