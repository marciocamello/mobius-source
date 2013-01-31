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

import java.util.Collection;
import java.util.Collections;

import lineage2.gameserver.model.Creature;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MagicSkillLaunched extends L2GameServerPacket
{
	/**
	 * Field _casterId.
	 */
	private final int _casterId;
	/**
	 * Field _skillId.
	 */
	private final int _skillId;
	/**
	 * Field _skillLevel.
	 */
	private final int _skillLevel;
	/**
	 * Field _targets.
	 */
	private final Collection<Creature> _targets;
	
	/**
	 * Constructor for MagicSkillLaunched.
	 * @param casterId int
	 * @param skillId int
	 * @param skillLevel int
	 * @param target Creature
	 */
	public MagicSkillLaunched(int casterId, int skillId, int skillLevel, Creature target)
	{
		_casterId = casterId;
		_skillId = skillId;
		_skillLevel = skillLevel;
		_targets = Collections.singletonList(target);
	}
	
	/**
	 * Constructor for MagicSkillLaunched.
	 * @param casterId int
	 * @param skillId int
	 * @param skillLevel int
	 * @param targets Collection<Creature>
	 */
	public MagicSkillLaunched(int casterId, int skillId, int skillLevel, Collection<Creature> targets)
	{
		_casterId = casterId;
		_skillId = skillId;
		_skillLevel = skillLevel;
		_targets = targets;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x54);
		writeD(0x00);
		writeD(_casterId);
		writeD(_skillId);
		writeD(_skillLevel);
		writeD(_targets.size());
		for (Creature target : _targets)
		{
			if (target != null)
			{
				writeD(target.getObjectId());
			}
		}
	}
}
