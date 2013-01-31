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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.Henna;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HennaUnequipList extends L2GameServerPacket
{
	/**
	 * Field _emptySlots.
	 */
	private final int _emptySlots;
	/**
	 * Field _adena.
	 */
	private final long _adena;
	/**
	 * Field availHenna.
	 */
	private final List<Henna> availHenna = new ArrayList<>(3);
	
	/**
	 * Constructor for HennaUnequipList.
	 * @param player Player
	 */
	public HennaUnequipList(Player player)
	{
		_adena = player.getAdena();
		_emptySlots = player.getHennaEmptySlots();
		for (int i = 1; i <= 3; i++)
		{
			if (player.getHenna(i) != null)
			{
				availHenna.add(player.getHenna(i));
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xE6);
		writeQ(_adena);
		writeD(_emptySlots);
		writeD(availHenna.size());
		for (Henna henna : availHenna)
		{
			writeD(henna.getSymbolId());
			writeD(henna.getDyeId());
			writeQ(henna.getDrawCount());
			writeQ(henna.getPrice());
			writeD(1);
		}
	}
}
