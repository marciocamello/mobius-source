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
public class ExNoticePostArrived extends L2GameServerPacket
{
	/**
	 * Field STATIC_TRUE.
	 */
	public static final L2GameServerPacket STATIC_TRUE = new ExNoticePostArrived(1);
	/**
	 * Field STATIC_FALSE.
	 */
	public static final L2GameServerPacket STATIC_FALSE = new ExNoticePostArrived(0);
	/**
	 * Field _anim.
	 */
	private final int _anim;
	
	/**
	 * Constructor for ExNoticePostArrived.
	 * @param useAnim int
	 */
	public ExNoticePostArrived(int useAnim)
	{
		_anim = useAnim;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xA9);
		writeD(_anim);
	}
}
