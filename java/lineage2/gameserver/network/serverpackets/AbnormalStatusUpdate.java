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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.IconEffect;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AbnormalStatusUpdate extends L2GameServerPacket implements IconEffectPacket
{
	/**
	 * Field INFINITIVE_EFFECT. (value is -1)
	 */
	public static final int INFINITIVE_EFFECT = -1;
	/**
	 * Field _effects.
	 */
	private final List<IconEffect> _effects;
	
	/**
	 * Constructor for AbnormalStatusUpdate.
	 */
	public AbnormalStatusUpdate()
	{
		_effects = new ArrayList<>();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x85);
		writeH(_effects.size());
		for (IconEffect temp : _effects)
		{
			writeD(temp.getSkillId());
			writeH(temp.getLevel());
			writeD(temp.getDuration());
		}
	}
	
	/**
	 * Method addIconEffect.
	 * @param skillId int
	 * @param level int
	 * @param duration int
	 * @param obj int
	 * @see lineage2.gameserver.network.serverpackets.IconEffectPacket#addIconEffect(int, int, int, int)
	 */
	@Override
	public void addIconEffect(int skillId, int level, int duration, int obj)
	{
		_effects.add(new IconEffect(skillId, level, duration, obj));
	}
}
