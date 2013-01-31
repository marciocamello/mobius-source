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

import lineage2.gameserver.model.Creature;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExFishingStartCombat extends L2GameServerPacket
{
	/**
	 * Field _hp. Field _time.
	 */
	int _time, _hp;
	/**
	 * Field _mode. Field _deceptiveMode. Field _lureType.
	 */
	int _lureType, _deceptiveMode, _mode;
	/**
	 * Field char_obj_id.
	 */
	private final int char_obj_id;
	
	/**
	 * Constructor for ExFishingStartCombat.
	 * @param character Creature
	 * @param time int
	 * @param hp int
	 * @param mode int
	 * @param lureType int
	 * @param deceptiveMode int
	 */
	public ExFishingStartCombat(Creature character, int time, int hp, int mode, int lureType, int deceptiveMode)
	{
		char_obj_id = character.getObjectId();
		_time = time;
		_hp = hp;
		_mode = mode;
		_lureType = lureType;
		_deceptiveMode = deceptiveMode;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x27);
		writeD(char_obj_id);
		writeD(_time);
		writeD(_hp);
		writeC(_mode);
		writeC(_lureType);
		writeC(_deceptiveMode);
	}
}
