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
public class ExBR_BroadcastEventState extends L2GameServerPacket
{
	/**
	 * Field _eventId.
	 */
	private final int _eventId;
	/**
	 * Field _eventState.
	 */
	private final int _eventState;
	/**
	 * Field _param0.
	 */
	private int _param0;
	/**
	 * Field _param1.
	 */
	private int _param1;
	/**
	 * Field _param2.
	 */
	private int _param2;
	/**
	 * Field _param3.
	 */
	private int _param3;
	/**
	 * Field _param4.
	 */
	private int _param4;
	/**
	 * Field _param5.
	 */
	private String _param5;
	/**
	 * Field _param6.
	 */
	private String _param6;
	/**
	 * Field APRIL_FOOLS. (value is 20090401)
	 */
	public static final int APRIL_FOOLS = 20090401;
	/**
	 * Field EVAS_INFERNO. (value is 20090801)
	 */
	public static final int EVAS_INFERNO = 20090801;
	/**
	 * Field HALLOWEEN_EVENT. (value is 20091031)
	 */
	public static final int HALLOWEEN_EVENT = 20091031;
	/**
	 * Field RAISING_RUDOLPH. (value is 20091225)
	 */
	public static final int RAISING_RUDOLPH = 20091225;
	/**
	 * Field LOVERS_JUBILEE. (value is 20100214)
	 */
	public static final int LOVERS_JUBILEE = 20100214;
	/**
	 * Field APRIL_FOOLS_10. (value is 20100401)
	 */
	public static final int APRIL_FOOLS_10 = 20100401;
	
	/**
	 * Constructor for ExBR_BroadcastEventState.
	 * @param eventId int
	 * @param eventState int
	 */
	public ExBR_BroadcastEventState(int eventId, int eventState)
	{
		_eventId = eventId;
		_eventState = eventState;
	}
	
	/**
	 * Constructor for ExBR_BroadcastEventState.
	 * @param eventId int
	 * @param eventState int
	 * @param param0 int
	 * @param param1 int
	 * @param param2 int
	 * @param param3 int
	 * @param param4 int
	 * @param param5 String
	 * @param param6 String
	 */
	public ExBR_BroadcastEventState(int eventId, int eventState, int param0, int param1, int param2, int param3, int param4, String param5, String param6)
	{
		_eventId = eventId;
		_eventState = eventState;
		_param0 = param0;
		_param1 = param1;
		_param2 = param2;
		_param3 = param3;
		_param4 = param4;
		_param5 = param5;
		_param6 = param6;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xBC);
		writeD(_eventId);
		writeD(_eventState);
		writeD(_param0);
		writeD(_param1);
		writeD(_param2);
		writeD(_param3);
		writeD(_param4);
		writeS(_param5);
		writeS(_param6);
	}
}
