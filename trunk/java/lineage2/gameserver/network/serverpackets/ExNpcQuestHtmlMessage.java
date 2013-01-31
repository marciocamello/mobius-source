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

import java.util.regex.Matcher;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExNpcQuestHtmlMessage extends NpcHtmlMessage
{
	/**
	 * Field _questId.
	 */
	private final int _questId;
	
	/**
	 * Constructor for ExNpcQuestHtmlMessage.
	 * @param npcObjId int
	 * @param questId int
	 */
	public ExNpcQuestHtmlMessage(int npcObjId, int questId)
	{
		super(npcObjId);
		_questId = questId;
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
			_html = _html.replaceAll(_replaces.get(i), _replaces.get(i + 1));
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
		writeEx(0x8d);
		writeD(_npcObjId);
		writeS(_html);
		writeD(_questId);
	}
}
