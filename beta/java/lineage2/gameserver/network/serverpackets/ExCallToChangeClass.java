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

/**
 * @author Darvin
 * @data 08.06.2012
 */
public class ExCallToChangeClass extends L2GameServerPacket
{
	private final int _classId;
	private final int _showAnimation;
	private final int _canChange;
	
	public ExCallToChangeClass(int classId, boolean showAnimation, boolean canChange)
	{
		_classId = classId;
		_showAnimation = showAnimation ? 1 : 0;
		_canChange = canChange ? 1 : 0;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xFE);
		writeD(_classId); // New Class Id
		writeD(_showAnimation); // 0 icon, window-the request to change the Saba 1, 2 and TD-0
		writeD(_canChange); // Show Message
	}
}
