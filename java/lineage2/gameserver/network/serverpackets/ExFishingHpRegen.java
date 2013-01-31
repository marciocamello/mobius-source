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
public class ExFishingHpRegen extends L2GameServerPacket
{
	/**
	 * Field _hpBarColor. Field _Penalty. Field _GoodUse. Field _Anim. Field _HPmode. Field _fishHP. Field _time.
	 */
	private final int _time, _fishHP, _HPmode, _Anim, _GoodUse, _Penalty, _hpBarColor;
	/**
	 * Field char_obj_id.
	 */
	private final int char_obj_id;
	
	/**
	 * Constructor for ExFishingHpRegen.
	 * @param character Creature
	 * @param time int
	 * @param fishHP int
	 * @param HPmode int
	 * @param GoodUse int
	 * @param anim int
	 * @param penalty int
	 * @param hpBarColor int
	 */
	public ExFishingHpRegen(Creature character, int time, int fishHP, int HPmode, int GoodUse, int anim, int penalty, int hpBarColor)
	{
		char_obj_id = character.getObjectId();
		_time = time;
		_fishHP = fishHP;
		_HPmode = HPmode;
		_GoodUse = GoodUse;
		_Anim = anim;
		_Penalty = penalty;
		_hpBarColor = hpBarColor;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x28);
		writeD(char_obj_id);
		writeD(_time);
		writeD(_fishHP);
		writeC(_HPmode);
		writeC(_GoodUse);
		writeC(_Anim);
		writeD(_Penalty);
		writeC(_hpBarColor);
	}
}
