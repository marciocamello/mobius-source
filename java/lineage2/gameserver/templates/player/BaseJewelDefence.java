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
package lineage2.gameserver.templates.player;

public final class BaseJewelDefence
{
	private final int _r_earring;
	private final int _l_earring;
	private final int _r_ring;
	private final int _l_ring;
	private final int _necklace;
	
	public BaseJewelDefence(int r_earring, int l_earring, int r_ring, int l_ring, int necklace)
	{
		_r_earring = r_earring;
		_l_earring = l_earring;
		_r_ring = r_ring;
		_l_ring = l_ring;
		_necklace = necklace;
	}
	
	public int getREaaringDef()
	{
		return _r_earring;
	}
	
	public int getLEaaringDef()
	{
		return _l_earring;
	}
	
	public int getRRingDef()
	{
		return _r_ring;
	}
	
	public int getLRingDef()
	{
		return _l_ring;
	}
	
	public int getNecklaceDef()
	{
		return _necklace;
	}
}
