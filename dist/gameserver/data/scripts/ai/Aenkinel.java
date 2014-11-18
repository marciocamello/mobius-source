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
package ai;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Aenkinel extends Fighter
{
	private static final int Aenkinel1 = 25694;
	private static final int Aenkinel2 = 25695;
	private static final int NihilInvaderTreasureChest = 18820;
	private static final int MutantTreasureChest = 18823;
	
	/**
	 * Constructor for Aenkinel.
	 * @param actor NpcInstance
	 */
	public Aenkinel(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		
		if ((actor.getId() == Aenkinel1) || (actor.getId() == Aenkinel2))
		{
			final Reflection ref = actor.getReflection();
			ref.setReenterTime(System.currentTimeMillis());
		}
		
		if (actor.getId() == Aenkinel1)
		{
			for (int i = 0; i < 4; i++)
			{
				actor.getReflection().addSpawnWithoutRespawn(NihilInvaderTreasureChest, actor.getLoc(), 250);
			}
		}
		else if (actor.getId() == Aenkinel2)
		{
			for (int i = 0; i < 4; i++)
			{
				actor.getReflection().addSpawnWithoutRespawn(MutantTreasureChest, actor.getLoc(), 250);
			}
		}
		
		super.onEvtDead(killer);
	}
}
