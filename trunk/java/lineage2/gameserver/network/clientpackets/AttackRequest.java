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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AttackRequest extends L2GameClientPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	/**
	 * Field _originX.
	 */
	@SuppressWarnings("unused")
	private int _originX;
	/**
	 * Field _originY.
	 */
	@SuppressWarnings("unused")
	private int _originY;
	/**
	 * Field _originZ.
	 */
	@SuppressWarnings("unused")
	private int _originZ;
	/**
	 * Field _attackId.
	 */
	private int _attackId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_originX = readD();
		_originY = readD();
		_originZ = readD();
		_attackId = readC();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		activeChar.setActive();
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (!activeChar.getPlayerAccess().CanAttack)
		{
			activeChar.sendActionFailed();
			return;
		}
		GameObject target = activeChar.getVisibleObject(_objectId);
		if (target == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if ((activeChar.getAggressionTarget() != null) && (activeChar.getAggressionTarget() != target) && !activeChar.getAggressionTarget().isDead())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (target.isPlayer() && (activeChar.isInBoat() || target.isInBoat()))
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.getTarget() != target)
		{
			target.onAction(activeChar, _attackId == 1);
			return;
		}
		if ((target.getObjectId() != activeChar.getObjectId()) && !activeChar.isInStoreMode() && !activeChar.isProcessingRequest())
		{
			target.onForcedAttack(activeChar, _attackId == 1);
		}
	}
}
