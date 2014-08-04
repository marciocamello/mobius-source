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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExFriendDetailInfo;

/**
 * @author Smo
 */
public class RequestFriendDetailInfo extends L2GameClientPacket
{
	private static final String _C__D0_97_REQUESTFRIENDDETAILINFO = "[C] D0:97 RequestFriendDetailInfo";
	
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	@Override
	public void runImpl()
	{
		Player player = getClient().getActiveChar();
		int objId = CharacterDAO.getInstance().getObjectIdByName(_name);
		player.sendPacket(new ExFriendDetailInfo(player, objId));
	}
	
	@Override
	public String getType()
	{
		return _C__D0_97_REQUESTFRIENDDETAILINFO;
	}
}
