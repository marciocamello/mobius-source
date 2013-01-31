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

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.IconEffect;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.utils.EffectsComparator;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PartySpelled extends L2GameServerPacket implements IconEffectPacket
{
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _objId.
	 */
	private final int _objId;
	/**
	 * Field _effects.
	 */
	private final List<IconEffect> _effects;
	
	/**
	 * Constructor for PartySpelled.
	 * @param activeChar Playable
	 * @param full boolean
	 */
	public PartySpelled(Playable activeChar, boolean full)
	{
		_objId = activeChar.getObjectId();
		_type = activeChar.isPet() ? 1 : activeChar.isServitor() ? 2 : 0;
		_effects = new ArrayList<>();
		if (full)
		{
			Effect[] effects = activeChar.getEffectList().getAllFirstEffects();
			Arrays.sort(effects, EffectsComparator.getInstance());
			for (Effect effect : effects)
			{
				if ((effect != null) && effect.isInUse())
				{
					effect.addIcon(this);
				}
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xf4);
		writeD(_type);
		writeD(_objId);
		writeD(_effects.size());
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
