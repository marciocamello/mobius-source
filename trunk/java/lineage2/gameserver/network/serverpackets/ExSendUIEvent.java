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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExSendUIEvent extends NpcStringContainer
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _isHide.
	 */
	private final boolean _isHide;
	/**
	 * Field _isIncrease.
	 */
	private final boolean _isIncrease;
	/**
	 * Field _startTime.
	 */
	private final int _startTime;
	/**
	 * Field _endTime.
	 */
	private final int _endTime;
	
	/**
	 * Constructor for ExSendUIEvent.
	 * @param player Player
	 * @param isHide boolean
	 * @param isIncrease boolean
	 * @param startTime int
	 * @param endTime int
	 * @param params String[]
	 */
	public ExSendUIEvent(Player player, boolean isHide, boolean isIncrease, int startTime, int endTime, String... params)
	{
		this(player, isHide, isIncrease, startTime, endTime, NpcString.NONE, params);
	}
	
	/**
	 * Constructor for ExSendUIEvent.
	 * @param player Player
	 * @param isHide boolean
	 * @param isIncrease boolean
	 * @param startTime int
	 * @param endTime int
	 * @param npcString NpcString
	 * @param params String[]
	 */
	public ExSendUIEvent(Player player, boolean isHide, boolean isIncrease, int startTime, int endTime, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objectId = player.getObjectId();
		_isHide = isHide;
		_isIncrease = isIncrease;
		_startTime = startTime;
		_endTime = endTime;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x8E);
		writeD(_objectId);
		writeD(_isHide ? 0x01 : 0x00);
		writeD(0x00);
		writeD(0x00);
		writeS(_isIncrease ? "1" : "0");
		writeS(String.valueOf(_startTime / 60));
		writeS(String.valueOf(_startTime % 60));
		writeS(String.valueOf(_endTime / 60));
		writeS(String.valueOf(_endTime % 60));
		writeElements();
	}
}
