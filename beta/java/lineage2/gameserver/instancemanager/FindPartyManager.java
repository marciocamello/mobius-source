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
package lineage2.gameserver.instancemanager;

import javolution.util.FastList;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FindPartyManager
{
	private FastList<Player> lookingForParty;
	private FastList<Player> wannaToChangeThisPlayer;
	
	/**
	 * Method load.
	 */
	public void load()
	{
		lookingForParty = new FastList<>();
		wannaToChangeThisPlayer = new FastList<>();
	}
	
	/**
	 * Method getLookingForPartyPlayers.
	 * @return FastList<Player>
	 */
	public FastList<Player> getLookingForPartyPlayers()
	{
		return lookingForParty;
	}
	
	/**
	 * Method getWannaToChangeThisPlayers.
	 * @return FastList<Player>
	 */
	public FastList<Player> getWannaToChangeThisPlayers()
	{
		return wannaToChangeThisPlayer;
	}
	
	/**
	 * Method removeLookingForParty.
	 * @param player Player
	 */
	public void removeLookingForParty(Player player)
	{
		lookingForParty.remove(player);
	}
	
	/**
	 * Method removeChangeThisPlayer.
	 * @param player Player
	 */
	public void removeChangeThisPlayer(Player player)
	{
		wannaToChangeThisPlayer.remove(player);
	}
	
	private static final FindPartyManager _instance = new FindPartyManager();
	
	/**
	 * Method getInstance.
	 * @return FindPartyManager
	 */
	public static final FindPartyManager getInstance()
	{
		return _instance;
	}
}
