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
 * sample
 * <p/>
 * 0000: 85 02 00 10 04 00 00 01 00 4b 02 00 00 2c 04 00 .........K...,.. 0010: 00 01 00 58 02 00 00 ...X...
 * <p/>
 * <p/>
 * format h (dhd)
 * @version $Revision: 1.3.2.1.2.6 $ $Date: 2005/04/05 19:41:08 $
 */
public class AbnormalStatusUpdate extends L2GameServerPacket implements IconEffectPacket
{
	public static final int INFINITIVE_EFFECT = -1;
	private final List<IconEffect> _effects;
	
	public AbnormalStatusUpdate()
	{
		_effects = new ArrayList<>();
	}
	
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
	
	@Override
	public void addIconEffect(int skillId, int level, int duration, int obj)
	{
		_effects.add(new IconEffect(skillId, level, duration, obj));
	}
}