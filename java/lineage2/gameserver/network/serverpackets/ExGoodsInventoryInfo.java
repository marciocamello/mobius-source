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

import lineage2.gameserver.model.Player;

/**
 * @author VISTALL
 * @date 23:37/23.03.2011
 */
public class ExGoodsInventoryInfo extends L2GameServerPacket
{
	
	public ExGoodsInventoryInfo(Player player)
	{
		
	}
	
	@Override
	protected void writeImpl()
	{
		/*
		 * 203DA858 PUSH Engine.205127AC ASCII "QdSSQccSSh" 203DA8D0 PUSH Engine.20506EFC ASCII "dd"
		 */
		writeEx(0x112);
	}
}
