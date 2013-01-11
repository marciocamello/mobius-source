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
package lineage2.gameserver.model.actor.instances.player;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.gameserver.model.Player;

public class Friend
{
	private final int _objectId;
	private String _name;
	private int _classId;
	private int _level;
	private HardReference<Player> _playerRef = HardReferences.emptyRef();
	
	public Friend(int objectId, String name, int classId, int level)
	{
		_objectId = objectId;
		_name = name;
		_classId = classId;
		_level = level;
	}
	
	public Friend(Player player)
	{
		_objectId = player.getObjectId();
		update(player, true);
	}
	
	public void update(Player player, boolean set)
	{
		_level = player.getLevel();
		_name = player.getName();
		_classId = player.getActiveClassId();
		_playerRef = set ? player.getRef() : HardReferences.<Player> emptyRef();
	}
	
	public String getName()
	{
		Player player = getPlayer();
		return player == null ? _name : player.getName();
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public int getClassId()
	{
		Player player = getPlayer();
		return player == null ? _classId : player.getActiveClassId();
	}
	
	public int getLevel()
	{
		Player player = getPlayer();
		return player == null ? _level : player.getLevel();
	}
	
	public boolean isOnline()
	{
		Player player = _playerRef.get();
		return (player != null) && !player.isInOfflineMode();
	}
	
	public Player getPlayer()
	{
		Player player = _playerRef.get();
		return (player != null) && !player.isInOfflineMode() ? player : null;
	}
}
