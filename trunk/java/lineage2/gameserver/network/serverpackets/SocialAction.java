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
public class SocialAction extends L2GameServerPacket
{
	/**
	 * Field _playerId.
	 */
	private final int _playerId;
	/**
	 * Field _actionId.
	 */
	private final int _actionId;
	/**
	 * Field GREETING. (value is 2)
	 */
	public static final int GREETING = 2;
	/**
	 * Field VICTORY. (value is 3)
	 */
	public static final int VICTORY = 3;
	/**
	 * Field ADVANCE. (value is 4)
	 */
	public static final int ADVANCE = 4;
	/**
	 * Field NO. (value is 5)
	 */
	public static final int NO = 5;
	/**
	 * Field YES. (value is 6)
	 */
	public static final int YES = 6;
	/**
	 * Field BOW. (value is 7)
	 */
	public static final int BOW = 7;
	/**
	 * Field UNAWARE. (value is 8)
	 */
	public static final int UNAWARE = 8;
	/**
	 * Field WAITING. (value is 9)
	 */
	public static final int WAITING = 9;
	/**
	 * Field LAUGH. (value is 10)
	 */
	public static final int LAUGH = 10;
	/**
	 * Field APPLAUD. (value is 11)
	 */
	public static final int APPLAUD = 11;
	/**
	 * Field DANCE. (value is 12)
	 */
	public static final int DANCE = 12;
	/**
	 * Field SORROW. (value is 13)
	 */
	public static final int SORROW = 13;
	/**
	 * Field CHARM. (value is 14)
	 */
	public static final int CHARM = 14;
	/**
	 * Field SHYNESS. (value is 15)
	 */
	public static final int SHYNESS = 15;
	/**
	 * Field COUPLE_BOW. (value is 16)
	 */
	public static final int COUPLE_BOW = 16;
	/**
	 * Field COUPLE_HIGH_FIVE. (value is 17)
	 */
	public static final int COUPLE_HIGH_FIVE = 17;
	/**
	 * Field COUPLE_DANCE. (value is 18)
	 */
	public static final int COUPLE_DANCE = 18;
	/**
	 * Field AWAKENING. (value is 26)
	 */
	public static final int AWAKENING = 26;
	/**
	 * Field Tauty1. (value is 28)
	 */
	public static final int PROPOSE = 28;
	/**
	 * Field Tauty2. (value is 29)
	 */
	public static final int PROVOKE = 29;
	/**
	 * Field LEVEL_UP. (value is 2122)
	 */
	public static final int LEVEL_UP = 2122;
	/**
	 * Field GIVE_HERO. (value is 20016)
	 */
	public static final int GIVE_HERO = 20016;
	
	/**
	 * Constructor for SocialAction.
	 * @param playerId int
	 * @param actionId int
	 */
	public SocialAction(int playerId, int actionId)
	{
		_playerId = playerId;
		_actionId = actionId;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x27);
		writeD(_playerId);
		writeD(_actionId);
	}
}
