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
 * @author ALF
 * @data 04.02.2012
 */
public class ExShowUsmVideo extends L2GameServerPacket
{
	public static final int Q001 = 0x01; // What is the red gate
	public static final int GD1_INTRO = 0x02; // Start the video
	public static final int Q002 = 0x03; // What is the blue gate
	public static final int Q003 = 0x04; // What is the type of Cerberus
	public static final int Q004 = 0x05; // Nitsche shows
	public static final int Q005 = 0x06; // Goddess of destruction offers glory
	public static final int Q006 = 0x07; // Goddess of destruction... revenge ...
	public static final int Q007 = 0x08; // Goddess of destruction... Bring darkness
	public static final int Q009 = 0x09; // Does not have a niche
	public static final int Q010 = 0x0A; // Awakening, beginning
	public static final int Q011 = 0x0B; //
	public static final int Q012 = 0x0C; //
	private final int _id;
	
	public ExShowUsmVideo(int id)
	{
		_id = id;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x10E);
		writeD(_id);
	}
}
