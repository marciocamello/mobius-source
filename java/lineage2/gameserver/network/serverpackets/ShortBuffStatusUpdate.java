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

public class ShortBuffStatusUpdate extends L2GameServerPacket
{
	/**
	 * This is client's row 2 buff packet.
	 * <p/>
	 * Example (C4): F4 CD 04 00 00 07 00 00 00 0F 00 00 00 - overlord's healing, panel2
	 * <p/>
	 * structure cddd
	 * <p/>
	 * NOTES: 1). hex converting: Skill 1229 is in hex 4CD, but in packet it is CD 04 00 00. So i think that we must read the skill's hex id form behind ^^ 2). multipe skills on row 2: i don't know what more skills can go at row2 @ offie. please contact me to test it. Currently packet is working for
	 * one skill. 3). Removing buff icon must be sended empty packet F4 00 00 00 00 00 00 00 00 00 00 00 00 to remove buff icon. Or it will be lasted forever.
	 */
	private final int _skillId;
	private final int _skillLevel;
	private final int _skillDuration;
	
	public ShortBuffStatusUpdate(Effect effect)
	{
		_skillId = effect.getSkill().getDisplayId();
		_skillLevel = effect.getSkill().getDisplayLevel();
		_skillDuration = effect.getTimeLeft();
	}
	
	/**
	 * Zero packet to delete skill icon.
	 */
	public ShortBuffStatusUpdate()
	{
		_skillId = 0;
		_skillLevel = 0;
		_skillDuration = 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfa); // Packet type
		writeD(_skillId); // skill id??? CD 04 00 00 = skill 1229, hex 4CD
		writeD(_skillLevel); // Skill Level??? 07 00 00 00 = casted by heal 7 lvl.
		writeD(_skillDuration); // DURATION???? 0F 00 00 00 = 15 sec = overlord's heal
	}
}