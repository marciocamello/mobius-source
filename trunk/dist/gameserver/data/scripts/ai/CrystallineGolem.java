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

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

public class CrystallineGolem extends Fighter
{
	private static final int CORAL_GARDEN_SECRETGATE = 24220026;
	private static final int Crystal_Fragment = 9693;
	private ItemInstance itemToConsume = null;
	private Location lastPoint = null;
	private static String[] says = new String[]
	{
		"Ням, ням!!!",
		"Дай!!!",
		"Хочу!!!",
		"Моe!!!",
		"Еще!!!",
		"Еда!!!"
	};
	private static String[] says2 = new String[]
	{
		"Отдай!!!",
		"Верни!!!",
		"Жадные вы, уйду я от вас...",
		"Куда оно подевалось?",
		"Наверное показалось..."
	};
	private static TIntObjectHashMap<Info> instanceInfo = new TIntObjectHashMap<>();
	
	private static class Info
	{
		public Info()
		{
			// TODO Auto-generated constructor stub
		}
		
		boolean stage1 = false;
		boolean stage2 = false;
	}
	
	public CrystallineGolem(NpcInstance actor)
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
		if (_def_think)
		{
			doTask();
			return true;
		}
		if (itemToConsume != null)
		{
			if (itemToConsume.isVisible())
			{
				itemToConsume.deleteMe();
				itemToConsume = null;
			}
			else
			{
				itemToConsume = null;
				Functions.npcSay(actor, says2[Rnd.get(says2.length)]);
				actor.setWalking();
				addTaskMove(lastPoint, true);
				lastPoint = null;
				return true;
			}
		}
		Info info = instanceInfo.get(actor.getReflectionId());
		if (info == null)
		{
			info = new Info();
			instanceInfo.put(actor.getReflectionId(), info);
		}
		boolean opened = info.stage1 && info.stage2;
		if (!info.stage1)
		{
			int dx = actor.getX() - 142999;
			int dy = actor.getY() - 151671;
			if (((dx * dx) + (dy * dy)) < 10000)
			{
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 5441, 1, 1, 0));
				info.stage1 = true;
			}
		}
		if (!info.stage2)
		{
			int dx = actor.getX() - 139494;
			int dy = actor.getY() - 151668;
			if (((dx * dx) + (dy * dy)) < 10000)
			{
				actor.broadcastPacket(new MagicSkillUse(actor, actor, 5441, 1, 1, 0));
				info.stage2 = true;
			}
		}
		if (!opened && info.stage1 && info.stage2)
		{
			actor.getReflection().openDoor(CORAL_GARDEN_SECRETGATE);
		}
		if (Rnd.chance(10))
		{
			for (GameObject obj : World.getAroundObjects(actor, 300, 200))
			{
				if (obj.isItem())
				{
					ItemInstance item = (ItemInstance) obj;
					if (item.getItemId() == Crystal_Fragment)
					{
						if (Rnd.chance(50))
						{
							Functions.npcSay(actor, says[Rnd.get(says.length)]);
						}
						itemToConsume = item;
						lastPoint = actor.getLoc();
						actor.setRunning();
						addTaskMove(item.getLoc(), false);
						return true;
					}
				}
			}
		}
		if (randomAnimation())
		{
			return true;
		}
		return false;
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
