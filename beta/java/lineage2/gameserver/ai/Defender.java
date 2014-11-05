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
package lineage2.gameserver.ai;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

public class Defender extends Fighter
{
	public Defender(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (attacker.isPlayable())
		{
			return;
		}
		
		super.onEvtAttacked(attacker, damage);
	}
	
	@Override
	public boolean canAttackCharacter(Creature target)
	{
		return target.isMonster();
	}
	
	@Override
	public int getMaxAttackTimeout()
	{
		return 0;
	}
}