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
 * @modified KilRoy
 * @date 29.07.2012
 */
public class ExTacticalSign extends L2GameServerPacket
{
	private final int _targetId;
	private final int _signId;
	public static final int SIGN_NONE = 0;
	public static final int SIGN_STAR = 1;
	public static final int SIGN_HEART = 2;
	public static final int SIGN_MOON = 3;
	public static final int SIGN_CROSS = 4;
	
	/**
	 * Constructor for ExTacticalSign.
	 * @param target int
	 * @param sign int
	 */
	public ExTacticalSign(int target, int sign)
	{
		_targetId = target;
		_signId = sign;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x100);
		writeD(_targetId);
		writeD(_signId);
	}
}