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
package lineage2.gameserver.listener.actor.player.impl;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.listener.actor.player.OnAnswerListener;
import lineage2.gameserver.model.Player;

public class ReviveAnswerListener implements OnAnswerListener
{
	private final HardReference<Player> _playerRef;
	private final double _power;
	private final boolean _forPet;
	
	public ReviveAnswerListener(Player player, double power, boolean forPet)
	{
		_playerRef = player.getRef();
		_forPet = forPet;
		_power = power;
	}
	
	@Override
	public void sayYes()
	{
		Player player = _playerRef.get();
		if (player == null)
		{
			return;
		}
		if ((!player.isDead() && !_forPet) || (_forPet && (player.getSummonList().getPet() != null) && !player.getSummonList().getPet().isDead()))
		{
			return;
		}
		if (!_forPet)
		{
			player.doRevive(_power);
		}
		else if (player.getSummonList().getPet() != null)
		{
			player.getSummonList().getPet().doRevive(_power);
		}
	}
	
	@Override
	public void sayNo()
	{
	}
	
	public double getPower()
	{
		return _power;
	}
	
	public boolean isForPet()
	{
		return _forPet;
	}
}
