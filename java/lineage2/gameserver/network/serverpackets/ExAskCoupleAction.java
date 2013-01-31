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
public class ExAskCoupleAction extends L2GameServerPacket
{
	/**
	 * Field _socialId. Field _objectId.
	 */
	private final int _objectId, _socialId;
	
	/**
	 * Constructor for ExAskCoupleAction.
	 * @param objectId int
	 * @param socialId int
	 */
	public ExAskCoupleAction(int objectId, int socialId)
	{
		_objectId = objectId;
		_socialId = socialId;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xBB);
		writeD(_socialId);
		writeD(_objectId);
	}
}
