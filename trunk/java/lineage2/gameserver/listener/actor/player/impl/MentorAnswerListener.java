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
package lineage2.gameserver.listener.actor.player.impl;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.base.TeamType;

public class MentorAnswerListener implements OnAnswerListener
{
	private final HardReference<Player> _playerRef;
	private final String _mentee;
	
	public MentorAnswerListener(Player mentor, String mentee)
	{
		_playerRef = mentor.getRef();
		_mentee = mentee;
	}
	
	@Override
	public void sayYes()
	{
		Player player = _playerRef.get();
		if (player == null)
		{
			return;
		}
		if (player.isDead() || !player.getReflection().isDefault() || player.isInOlympiadMode() || player.isInObserverMode() || player.isTeleporting() || (player.getTeam() != TeamType.NONE))
		{
			return;
		}
		player.teleToLocation(World.getPlayer(_mentee).getLoc());
	}
	
	@Override
	public void sayNo()
	{
	}
}
