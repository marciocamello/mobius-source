/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package npc.model;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.SpecialMonsterInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class SquashInstance extends SpecialMonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int Young_Squash = 12774;
	public final static int High_Quality_Squash = 12775;
	public final static int Low_Quality_Squash = 12776;
	public final static int Large_Young_Squash = 12777;
	public final static int High_Quality_Large_Squash = 12778;
	public final static int Low_Quality_Large_Squash = 12779;
	public final static int King_Squash = 13016;
	public final static int Emperor_Squash = 13017;
	private HardReference<Player> _spawnerRef;
	
	public SquashInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	public void setSpawner(Player spawner)
	{
		_spawnerRef = spawner.getRef();
	}
	
	public Player getSpawner()
	{
		return _spawnerRef.get();
	}
	
	@Override
	public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if (attacker.getActiveWeaponInstance() == null)
		{
			return;
		}
		int weaponId = attacker.getActiveWeaponInstance().getItemId();
		if ((getNpcId() == Low_Quality_Large_Squash) || (getNpcId() == High_Quality_Large_Squash) || (getNpcId() == Emperor_Squash))
		{
			if ((weaponId != 4202) && (weaponId != 5133) && (weaponId != 5817) && (weaponId != 7058) && (weaponId != 8350))
			{
				return;
			}
		}
		i = 1;
		super.reduceCurrentHp(i, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
	
	@Override
	public long getRegenTick()
	{
		return 0L;
	}
	
	@Override
	public boolean canChampion()
	{
		return false;
	}
}
