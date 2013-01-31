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

import lineage2.gameserver.model.Effect;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ShortBuffStatusUpdate extends L2GameServerPacket
{
	/**
	 * Field _skillId.
	 */
	int _skillId;
	/**
	 * Field _skillLevel.
	 */
	int _skillLevel;
	/**
	 * Field _skillDuration.
	 */
	int _skillDuration;
	
	/**
	 * Constructor for ShortBuffStatusUpdate.
	 * @param effect Effect
	 */
	public ShortBuffStatusUpdate(Effect effect)
	{
		_skillId = effect.getSkill().getDisplayId();
		_skillLevel = effect.getSkill().getDisplayLevel();
		_skillDuration = effect.getTimeLeft();
	}
	
	/**
	 * Constructor for ShortBuffStatusUpdate.
	 */
	public ShortBuffStatusUpdate()
	{
		_skillId = 0;
		_skillLevel = 0;
		_skillDuration = 0;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xfa);
		writeD(_skillId);
		writeD(_skillLevel);
		writeD(_skillDuration);
	}
}
