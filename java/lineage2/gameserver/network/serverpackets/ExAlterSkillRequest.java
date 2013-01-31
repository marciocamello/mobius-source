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

import lineage2.gameserver.model.Skill;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExAlterSkillRequest extends L2GameServerPacket
{
	/**
	 * Field _skills.
	 */
	private final Skill _skills;
	/**
	 * Field _time.
	 */
	private final int _time;
	
	/**
	 * Constructor for ExAlterSkillRequest.
	 * @param id Skill
	 * @param time int
	 */
	public ExAlterSkillRequest(Skill id, int time)
	{
		_skills = id;
		_time = time;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x113);
		writeD(_skills.getId());
		writeD(0);
		writeD(_time);
	}
}
