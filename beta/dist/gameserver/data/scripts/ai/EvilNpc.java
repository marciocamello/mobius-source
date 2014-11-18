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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EvilNpc extends DefaultAI
{
	private long _lastAction;
	private static final String[] _txt =
	{
		"Leave me alone!",
		"Calm down!",
		"I will avenge you, then you will ask for forgiveness!",
		"We'll be in trouble!",
		"I will complain for you, you should be arrested!"
	};
	
	/**
	 * Constructor for EvilNpc.
	 * @param actor NpcInstance
	 */
	public EvilNpc(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		
		if ((attacker == null) || (attacker.getPlayer() == null))
		{
			return;
		}
		
		if ((System.currentTimeMillis() - _lastAction) > 3000)
		{
			final int chance = Rnd.get(0, 100);
			
			if (chance < 2)
			{
				attacker.getPlayer().setKarma(attacker.getPlayer().getKarma() + 5);
			}
			else if (chance < 4)
			{
				actor.doCast(SkillTable.getInstance().getInfo(4578, 1), attacker, true);
			}
			else
			{
				actor.doCast(SkillTable.getInstance().getInfo(4185, 7), attacker, true);
			}
			
			Functions.npcSay(actor, attacker.getName() + ", " + _txt[Rnd.get(_txt.length)]);
			_lastAction = System.currentTimeMillis();
		}
	}
}
