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
public class SpecialCamera extends L2GameServerPacket
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _dist.
	 */
	private final int _dist;
	/**
	 * Field _yaw.
	 */
	private final int _yaw;
	/**
	 * Field _pitch.
	 */
	private final int _pitch;
	/**
	 * Field _time.
	 */
	private final int _time;
	/**
	 * Field _duration.
	 */
	private final int _duration;
	/**
	 * Field _turn.
	 */
	private final int _turn;
	/**
	 * Field _rise.
	 */
	private final int _rise;
	/**
	 * Field _widescreen.
	 */
	private final int _widescreen;
	/**
	 * Field _unknown.
	 */
	private final int _unknown;
	
	/**
	 * Constructor for SpecialCamera.
	 * @param id int
	 * @param dist int
	 * @param yaw int
	 * @param pitch int
	 * @param time int
	 * @param duration int
	 */
	public SpecialCamera(int id, int dist, int yaw, int pitch, int time, int duration)
	{
		_id = id;
		_dist = dist;
		_yaw = yaw;
		_pitch = pitch;
		_time = time;
		_duration = duration;
		_turn = 0;
		_rise = 0;
		_widescreen = 0;
		_unknown = 0;
	}
	
	/**
	 * Constructor for SpecialCamera.
	 * @param id int
	 * @param dist int
	 * @param yaw int
	 * @param pitch int
	 * @param time int
	 * @param duration int
	 * @param turn int
	 * @param rise int
	 * @param widescreen int
	 * @param unk int
	 */
	public SpecialCamera(int id, int dist, int yaw, int pitch, int time, int duration, int turn, int rise, int widescreen, int unk)
	{
		_id = id;
		_dist = dist;
		_yaw = yaw;
		_pitch = pitch;
		_time = time;
		_duration = duration;
		_turn = turn;
		_rise = rise;
		_widescreen = widescreen;
		_unknown = unk;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xd6);
		writeD(_id);
		writeD(_dist);
		writeD(_yaw);
		writeD(_pitch);
		writeD(_time);
		writeD(_duration);
		writeD(_turn);
		writeD(_rise);
		writeD(_widescreen);
		writeD(_unknown);
	}
}
