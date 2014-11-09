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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.components.NpcString;

public class ExSendUIEvent extends NpcStringContainer
{
	private final int _objectId;
	private final int _isHide;
	private final int _isIncrease;
	private final int _startTime;
	private final int _endTime;
	
	public ExSendUIEvent(Player player, int isHide, int isIncrease, int startTime, int endTime, String... params)
	{
		this(player, isHide, isIncrease, startTime, endTime, NpcString.NONE, params);
	}
	
	/*
	 * public ExSendUIEvent(Player player, boolean isHide, boolean isIncrease, int startTime, int endTime, NpcString npcString, String... params) { super(npcString, params); _objectId = player.getObjectId(); _isHide = isHide; _isIncrease = isIncrease; _startTime = startTime; _endTime = endTime; }
	 */
	public ExSendUIEvent(Player player, int isHide, int isIncrease, int startTime, int endTime, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objectId = player.getObjectId();
		_isHide = isHide;
		_isIncrease = isIncrease;
		_startTime = startTime;
		_endTime = endTime;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x8F);
		writeD(_objectId);
		writeD(_isHide); // 0 = show, 1 = hide (there is 2 = pause and 3 = resume also but they don't work well you can only pause count down and you cannot resume it because resume hides the counter).
		writeD(0x00); // unknown
		writeD(0x00); // unknown
		writeS(String.valueOf(_isIncrease)); // 0 = count down, 1 = count up
		// will disappear 10 seconds
		// before it ends)
		writeS(String.valueOf(_startTime / 60)); // timer starting minute(s)
		writeS(String.valueOf(_startTime % 60)); // timer starting second(s)
		writeS(String.valueOf(_endTime / 60)); // timer length minute(s) (timer
		writeS(String.valueOf(_endTime % 60)); // timer length second(s) (timer
		writeElements();
	}
}