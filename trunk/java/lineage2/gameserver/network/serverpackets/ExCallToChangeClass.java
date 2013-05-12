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
	private final int classId;
	private final boolean showMsg;
	
	public ExCallToChangeClass(int classId, boolean showMsg)
	{
		this.classId = classId;
		this.showMsg = showMsg;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xFE);
		writeD(classId); // New Class Id
		writeD(showMsg); // Show Message
	}
}
