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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Location;

public abstract class AbstractTargetAction extends L2GameClientPacket
{
	private final Location _actorPosition = new Location();
	private boolean _forced;
	private int _targetObjectId;
	
	@Override
	protected void readImpl()
	{
		_targetObjectId = readD();
		_actorPosition.setX(readD());
		_actorPosition.setY(readD());
		_actorPosition.setZ(readD());
		_forced = readC() == 1;
	}
	
	@Override
	protected void runImpl()
	{
		final Player actor = getActiveChar();
		
		if (actor == null)
		{
			return;
		}
		
		final GameObject obj = actor.getVisibleObject(_targetObjectId);
		
		if (obj == null)
		{
			return;
		}
		
		if (_actorPosition.isNull())
		{
			return;
		}
		
		if (obj.isTargetable(actor))
		{
			if (isAttackRequest())
			{
				if (actor.getPlayerAccess().CanAttack)
				{
					obj.onForcedAttack(actor, _forced);
				}
			}
			else
			{
				obj.onActionSelect(actor, _forced);
			}
			
			actor.setActive();
		}
		
		actor.sendActionFailed();
	}
	
	protected abstract boolean isAttackRequest();
}
