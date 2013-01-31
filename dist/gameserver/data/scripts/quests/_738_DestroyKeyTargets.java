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

public class _738_DestroyKeyTargets extends Dominion_KillSpecialUnitQuest
{
	public _738_DestroyKeyTargets()
	{
		super();
	}
	
	@Override
	protected NpcString startNpcString()
	{
		return NpcString.DEFEAT_S1_WARSMITHS_AND_OVERLORDS;
	}
	
	@Override
	protected NpcString progressNpcString()
	{
		return NpcString.YOU_HAVE_DEFEATED_S2_OF_S1_WARSMITHS_AND_OVERLORDS;
	}
	
	@Override
	protected NpcString doneNpcString()
	{
		return NpcString.YOU_DESTROYED_THE_ENEMYS_PROFESSIONALS;
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
			ClassId.NECROMANCER,
			ClassId.SWORDSINGER,
			ClassId.BLADEDANCER,
			ClassId.OVERLORD,
			ClassId.WARSMITH,
			ClassId.SOULTAKER,
			ClassId.SWORD_MUSE,
			ClassId.SPECTRAL_DANCER,
			ClassId.DOMINATOR,
			ClassId.MAESTRO,
			ClassId.INSPECTOR,
			ClassId.JUDICATOR
		};
	}
}
