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
package lineage2.gameserver.instancemanager;

import javolution.util.FastList;
import lineage2.gameserver.model.Player;

public class FindPartyManager
{
	FastList<Player> lookingForParty;
	FastList<Player> wannaToChangeThisPlayer;
	
	public void load()
	{
		lookingForParty = new FastList<>();
		wannaToChangeThisPlayer = new FastList<>();
	}
	
	public void addLookingForParty(Player player)
	{
		lookingForParty.add(player);
	}
	
	public void addChangeThisPlayer(Player player)
	{
		wannaToChangeThisPlayer.add(player);
	}
	
	public FastList<Player> getLookingForPartyPlayers()
	{
		return lookingForParty;
	}
	
	public FastList<Player> getWannaToChangeThisPlayers()
	{
		return wannaToChangeThisPlayer;
	}
	
	public void removeLookingForParty(Player player)
	{
		lookingForParty.remove(player);
	}
	
	public void removeChangeThisPlayer(Player player)
	{
		wannaToChangeThisPlayer.remove(player);
	}
	
	public boolean getLookingForParty(int level, int classId)
	{
		for (Player player : lookingForParty)
		{
			if ((player.getLevel() == level) && (player.getClassId().getId() == classId))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean getWannaToChangeThisPlayer(int level, int classId)
	{
		for (Player player : wannaToChangeThisPlayer)
		{
			if ((player.getLevel() == level) && (player.getClassId().getId() == classId))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean getWannaToChangeThisPlayer(int objectID)
	{
		for (Player player : wannaToChangeThisPlayer)
		{
			if (player.getObjectId() == objectID)
			{
				return true;
			}
		}
		return false;
	}
	
	public Player getPlayerFromChange(int level, int classId)
	{
		for (Player player : wannaToChangeThisPlayer)
		{
			if ((player.getLevel() == level) && (player.getClassId().getId() == classId))
			{
				return player;
			}
		}
		return null;
	}
	
	private static final FindPartyManager _instance = new FindPartyManager();
	
	public static final FindPartyManager getInstance()
	{
		return _instance;
	}
}
