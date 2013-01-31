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

import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExOlympiadSpelledInfo extends L2GameServerPacket
{
	/**
	 * Field char_obj_id.
	 */
	private int char_obj_id = 0;
	/**
	 * Field _effects.
	 */
	private final List<Effect> _effects;
	
	/**
	 * @author Mobius
	 */
	class Effect
	{
		/**
		 * Field skillId.
		 */
		int skillId;
		/**
		 * Field level.
		 */
		int level;
		/**
		 * Field duration.
		 */
		int duration;
		
		/**
		 * Constructor for Effect.
		 * @param skillId int
		 * @param level int
		 * @param duration int
		 */
		public Effect(int skillId, int level, int duration)
		{
			this.skillId = skillId;
			this.level = level;
			this.duration = duration;
		}
	}
	
	/**
	 * Constructor for ExOlympiadSpelledInfo.
	 */
	public ExOlympiadSpelledInfo()
	{
		_effects = new ArrayList<>();
	}
	
	/**
	 * Method addEffect.
	 * @param skillId int
	 * @param level int
	 * @param duration int
	 */
	public void addEffect(int skillId, int level, int duration)
	{
		_effects.add(new Effect(skillId, level, duration));
	}
	
	/**
	 * Method addSpellRecivedPlayer.
	 * @param cha Player
	 */
	public void addSpellRecivedPlayer(Player cha)
	{
		if (cha != null)
		{
			char_obj_id = cha.getObjectId();
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x7b);
		writeD(char_obj_id);
		writeD(_effects.size());
		for (Effect temp : _effects)
		{
			writeD(temp.skillId);
			writeH(temp.level);
			writeD(temp.duration);
		}
	}
}
