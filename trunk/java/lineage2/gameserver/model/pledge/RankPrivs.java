/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.pledge;

public class RankPrivs
{
	private final int _rank;
	private int _party;
	private int _privs;
	
	public RankPrivs(int rank, int party, int privs)
	{
		_rank = rank;
		_party = party;
		_privs = privs;
	}
	
	public int getRank()
	{
		return _rank;
	}
	
	public int getParty()
	{
		return _party;
	}
	
	public void setParty(int party)
	{
		_party = party;
	}
	
	public int getPrivs()
	{
		return _privs;
	}
	
	public void setPrivs(int privs)
	{
		_privs = privs;
	}
}
