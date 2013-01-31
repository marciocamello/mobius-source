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
import java.util.Arrays;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.IconEffect;
import lineage2.gameserver.utils.EffectsComparator;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExAbnormalStatusUpdateFromTargetPacket extends L2GameServerPacket implements IconEffectPacket
{
	/**
	 * Field objId.
	 */
	private final int objId;
	/**
	 * Field _effects.
	 */
	private final List<IconEffect> _effects;
	
	/**
	 * Constructor for ExAbnormalStatusUpdateFromTargetPacket.
	 * @param target Creature
	 */
	public ExAbnormalStatusUpdateFromTargetPacket(Creature target)
	{
		_effects = new ArrayList<>();
		objId = target.getObjectId();
		Effect[] effects = target.getEffectList().getAllFirstEffects();
		Arrays.sort(effects, EffectsComparator.getInstance());
		for (Effect effect : effects)
		{
			if ((effect != null) && effect.isInUse())
			{
				effect.addIcon(this);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xE5);
		writeD(objId);
		writeH(_effects.size());
		for (IconEffect e : _effects)
		{
			writeD(e.getSkillId());
			writeH(e.getLevel());
			writeD(0x00);
			writeD(e.getDuration());
			writeD(e.getObj());
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
