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

public class _736_WeakenTheMagic extends Dominion_KillSpecialUnitQuest
{
	public _736_WeakenTheMagic()
	{
		super();
	}
	
	@Override
	protected NpcString startNpcString()
	{
		return NpcString.DEFEAT_S1_WIZARDS_AND_SUMMONERS;
	}
	
	@Override
	protected NpcString progressNpcString()
	{
		return NpcString.YOU_HAVE_DEFEATED_S2_OF_S1_ENEMIES;
	}
	
	@Override
	protected NpcString doneNpcString()
	{
		return NpcString.YOU_WEAKENED_THE_ENEMYS_MAGIC;
	}
	
	@Override
	protected int getRandomMin()
	{
		return 10;
	}
	
	@Override
	protected int getRandomMax()
	{
		return 15;
	}
	
	@Override
	protected ClassId[] getTargetClassIds()
	{
		return new ClassId[]
		{
			ClassId.SORCERER,
			ClassId.WARLOCK,
			ClassId.SPELLSINGER,
			ClassId.ELEMENTAL_SUMMONER,
			ClassId.SPELLHOWLER,
			ClassId.PHANTOM_SUMMONER,
			ClassId.ARCHMAGE,
			ClassId.ARCANA_LORD,
			ClassId.MYSTIC_MUSE,
			ClassId.ELEMENTAL_MASTER,
			ClassId.STORM_SCREAMER,
			ClassId.SPECTRAL_MASTER
		};
	}
}
