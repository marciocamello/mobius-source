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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowUsmVideo extends L2GameServerPacket
{
	/**
	 * Field _usmVideo.
	 */
	private final int _usmVideo;
	/**
	 * Field Q001.
	 */
	public static int Q001 = 1;
	/**
	 * Field GD1_INTRO.
	 */
	public static int GD1_INTRO = 2;
	/**
	 * Field Q002.
	 */
	public static int Q002 = 3;
	/**
	 * Field Q003.
	 */
	public static int Q003 = 4;
	/**
	 * Field Q004.
	 */
	public static int Q004 = 5;
	/**
	 * Field Q005.
	 */
	public static int Q005 = 6;
	/**
	 * Field Q006.
	 */
	public static int Q006 = 7;
	/**
	 * Field Q007.
	 */
	public static int Q007 = 8;
	/**
	 * Field Q009.
	 */
	public static int Q009 = 9;
	/**
	 * Field Q010.
	 */
	public static int Q010 = 10;
	/**
	 * Field AWAKE_1.
	 */
	public static int AWAKE_1 = 139;
	/**
	 * Field AWAKE_2.
	 */
	public static int AWAKE_2 = 140;
	/**
	 * Field AWAKE_3.
	 */
	public static int AWAKE_3 = 141;
	/**
	 * Field AWAKE_4.
	 */
	public static int AWAKE_4 = 142;
	/**
	 * Field AWAKE_5.
	 */
	public static int AWAKE_5 = 143;
	/**
	 * Field AWAKE_6.
	 */
	public static int AWAKE_6 = 144;
	/**
	 * Field AWAKE_7.
	 */
	public static int AWAKE_7 = 145;
	/**
	 * Field AWAKE_8.
	 */
	public static int AWAKE_8 = 146;
	
	/**
	 * Constructor for ExShowUsmVideo.
	 * @param usmVideo int
	 */
	public ExShowUsmVideo(int usmVideo)
	{
		_usmVideo = usmVideo;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x10D);
		writeD(_usmVideo);
	}
}
