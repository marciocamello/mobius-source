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
package lineage2.gameserver.network.serverpackets;

public class ExShowUsmVideo extends L2GameServerPacket
{
	private final int _usmVideo;
	public static int Q001 = 1;
	public static int GD1_INTRO = 2;
	public static int Q002 = 3;
	public static int Q003 = 4;
	public static int Q004 = 5;
	public static int Q005 = 6;
	public static int Q006 = 7;
	public static int Q007 = 8;
	public static int Q009 = 9;
	public static int Q010 = 10;
	public static int AWAKE_1 = 139;
	public static int AWAKE_2 = 140;
	public static int AWAKE_3 = 141;
	public static int AWAKE_4 = 142;
	public static int AWAKE_5 = 143;
	public static int AWAKE_6 = 144;
	public static int AWAKE_7 = 145;
	public static int AWAKE_8 = 146;
	
	public ExShowUsmVideo(int usmVideo)
	{
		_usmVideo = usmVideo;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x10D);
		writeD(_usmVideo);
	}
}
