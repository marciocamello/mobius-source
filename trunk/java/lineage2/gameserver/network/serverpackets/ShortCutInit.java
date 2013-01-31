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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.ShortCut;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ShortCutInit extends ShortCutPacket
{
	/**
	 * Field _shortCuts.
	 */
	private List<ShortcutInfo> _shortCuts = Collections.emptyList();
	
	/**
	 * Constructor for ShortCutInit.
	 * @param pl Player
	 */
	public ShortCutInit(Player pl)
	{
		Collection<ShortCut> shortCuts = pl.getAllShortCuts();
		_shortCuts = new ArrayList<>(shortCuts.size());
		for (ShortCut shortCut : shortCuts)
		{
			_shortCuts.add(convert(pl, shortCut));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x45);
		writeD(_shortCuts.size());
		for (final ShortcutInfo sc : _shortCuts)
		{
			sc.write(this);
		}
	}
}
