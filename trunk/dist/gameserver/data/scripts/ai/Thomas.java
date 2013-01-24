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
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

public class Thomas extends Fighter
{
	private long _lastSay;
	private static final String[] _stay =
	{
		"Ха...Ха... Вы пришли спасти снеговика?",
		"Так просто я вам его не отдам!",
		"Чтобы спасти вашего снеговика, вам придется убить меня!",
		"Ха...Ха... Вы думаете это так просто?"
	};
	private static final String[] _attacked =
	{
		"Вы должны все умереть!",
		"Снеговик мой и не будет у вас Нового Года!",
		"Я вас всех убью!",
		"Что так слабо бьете? Мало каши ели? Ха... Ха...",
		"И это называется герои?",
		"Не видать вам снеговика!",
		"Только древнее оружие способно победить меня!"
	};
	
	public Thomas(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (!actor.isInCombat() && ((System.currentTimeMillis() - _lastSay) > 10000))
		{
			Functions.npcSay(actor, _stay[Rnd.get(_stay.length)]);
			_lastSay = System.currentTimeMillis();
		}
		return super.thinkActive();
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if ((attacker == null) || (attacker.getPlayer() == null))
		{
			return;
		}
		if ((System.currentTimeMillis() - _lastSay) > 5000)
		{
			Functions.npcSay(actor, _attacked[Rnd.get(_attacked.length)]);
			_lastSay = System.currentTimeMillis();
		}
		super.onEvtAttacked(attacker, damage);
	}
}
