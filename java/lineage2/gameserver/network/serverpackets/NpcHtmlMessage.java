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
package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.scripts.Scripts.ScriptClassAndMethod;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NpcHtmlMessage extends L2GameServerPacket
{
	/**
	 * Field _log.
	 */
	protected static final Logger _log = LoggerFactory.getLogger(NpcHtmlMessage.class);
	/**
	 * Field objectId.
	 */
	protected static final Pattern objectId = Pattern.compile("%objectId%");
	/**
	 * Field playername.
	 */
	protected static final Pattern playername = Pattern.compile("%playername%");
	/**
	 * Field _npcObjId.
	 */
	protected int _npcObjId;
	/**
	 * Field _html.
	 */
	protected String _html;
	/**
	 * Field _file.
	 */
	protected String _file = null;
	/**
	 * Field _replaces.
	 */
	protected List<String> _replaces = new ArrayList<>();
	/**
	 * Field have_appends.
	 */
	protected boolean have_appends = false;
	
	/**
	 * Constructor for NpcHtmlMessage.
	 * @param player Player
	 * @param npcId int
	 * @param filename String
	 * @param val int
	 */
	public NpcHtmlMessage(Player player, int npcId, String filename, int val)
	{
		List<ScriptClassAndMethod> appends = Scripts.dialogAppends.get(npcId);
		if ((appends != null) && (appends.size() > 0))
		{
			have_appends = true;
			if ((filename != null) && filename.equalsIgnoreCase("npcdefault.htm"))
			{
				setHtml("");
			}
			else
			{
				setFile(filename);
			}
			String replaces = "";
			Object[] script_args = new Object[]
			{
				val
			};
			for (ScriptClassAndMethod append : appends)
			{
				Object obj = Scripts.getInstance().callScripts(player, append.className, append.methodName, script_args);
				if (obj != null)
				{
					replaces += obj;
				}
			}
			if (!replaces.equals(""))
			{
				replace("</body>", "\n" + Strings.bbParse(replaces) + "</body>");
			}
		}
		else
		{
			setFile(filename);
		}
	}
	
	/**
	 * Constructor for NpcHtmlMessage.
	 * @param player Player
	 * @param npc NpcInstance
	 * @param filename String
	 * @param val int
	 */
	public NpcHtmlMessage(Player player, NpcInstance npc, String filename, int val)
	{
		this(player, npc.getNpcId(), filename, val);
		_npcObjId = npc.getObjectId();
		player.setLastNpc(npc);
		replace("%npcId%", String.valueOf(npc.getNpcId()));
		replace("%npcname%", npc.getName());
	}
	
	/**
	 * Constructor for NpcHtmlMessage.
	 * @param player Player
	 * @param npc NpcInstance
	 */
	public NpcHtmlMessage(Player player, NpcInstance npc)
	{
		if (npc == null)
		{
			_npcObjId = 5;
			player.setLastNpc(null);
		}
		else
		{
			_npcObjId = npc.getObjectId();
			player.setLastNpc(npc);
		}
	}
	
	/**
	 * Constructor for NpcHtmlMessage.
	 * @param npcObjId int
	 */
	public NpcHtmlMessage(int npcObjId)
	{
		_npcObjId = npcObjId;
	}
	
	/**
	 * Method setHtml.
	 * @param text String
	 * @return NpcHtmlMessage
	 */
	public final NpcHtmlMessage setHtml(String text)
	{
		if (!text.contains("<html>"))
		{
			text = "<html><body>" + text + "</body></html>";
		}
		_html = text;
		return this;
	}
	
	/**
	 * Method setFile.
	 * @param file String
	 * @return NpcHtmlMessage
	 */
	public final NpcHtmlMessage setFile(String file)
	{
		_file = file;
		if (_file.startsWith("data/html/"))
		{
			_log.info("NpcHtmlMessage: need fix : " + file, new Exception());
			_file = _file.replace("data/html/", "");
		}
		return this;
	}
	
	/**
	 * Method replace.
	 * @param pattern String
	 * @param value String
	 * @return NpcHtmlMessage
	 */
	public NpcHtmlMessage replace(String pattern, String value)
	{
		if ((pattern == null) || (value == null))
		{
			return this;
		}
		_replaces.add(pattern);
		_replaces.add(value);
		return this;
	}
	
	/**
	 * Method replaceNpcString.
	 * @param pattern String
	 * @param npcString NpcString
	 * @param arg Object[]
	 * @return NpcHtmlMessage
	 */
	public NpcHtmlMessage replaceNpcString(String pattern, NpcString npcString, Object... arg)
	{
		if (pattern == null)
		{
			return this;
		}
		if (npcString.getSize() != arg.length)
		{
			throw new IllegalArgumentException("Not valid size of parameters: " + npcString);
		}
		_replaces.add(pattern);
		_replaces.add(HtmlUtils.htmlNpcString(npcString, arg));
		return this;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if (_file != null)
		{
			if (player.isGM())
			{
				Functions.sendDebugMessage(player, "HTML: " + _file);
			}
			String content = HtmCache.getInstance().getNotNull(_file, player);
			String content2 = HtmCache.getInstance().getNullable(_file, player);
			if (content2 == null)
			{
				setHtml(have_appends && _file.endsWith(".htm") ? "" : content);
			}
			else
			{
				setHtml(content);
			}
		}
		for (int i = 0; i < _replaces.size(); i += 2)
		{
			_html = _html.replace(_replaces.get(i), _replaces.get(i + 1));
		}
		if (_html == null)
		{
			return;
		}
		Matcher m = objectId.matcher(_html);
		if (m != null)
		{
			_html = m.replaceAll(String.valueOf(_npcObjId));
		}
		_html = playername.matcher(_html).replaceAll(player.getName());
		player.cleanBypasses(false);
		_html = player.encodeBypasses(_html, false);
		writeC(0x19);
		writeD(_npcObjId);
		writeS(_html);
		writeD(0x00);
		writeD(0x00);
	}
}
