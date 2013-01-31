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
package quests;

import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.network.serverpackets.components.NpcString;

public class _737_DenyBlessings extends Dominion_KillSpecialUnitQuest
{
	public _737_DenyBlessings()
	{
		super();
	}
	
	@Override
	protected NpcString startNpcString()
	{
		return NpcString.DEFEAT_S1_HEALERS_AND_BUFFERS;
	}
	
	@Override
	protected NpcString progressNpcString()
	{
		return NpcString.YOU_HAVE_DEFEATED_S2_OF_S1_HEALERS_AND_BUFFERS;
	}
	
	@Override
	protected NpcString doneNpcString()
	{
		return NpcString.YOU_HAVE_WEAKENED_THE_ENEMYS_SUPPORT;
	}
	
	@Override
	protected int getRandomMin()
	{
		return 3;
	}
	
	@Override
	protected int getRandomMax()
	{
		return 8;
	}
	
	@Override
	protected ClassId[] getTargetClassIds()
	{
		return new ClassId[]
		{
			ClassId.BISHOP,
			ClassId.PROPHET,
			ClassId.ELDER,
			ClassId.SHILLEN_ELDER,
			ClassId.CARDINAL,
			ClassId.HIEROPHANT,
			ClassId.EVAS_SAINT,
			ClassId.SHILLIEN_SAINT,
			ClassId.DOOMCRYER
		};
	}
}
