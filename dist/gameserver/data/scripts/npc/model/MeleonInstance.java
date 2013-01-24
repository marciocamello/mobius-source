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
package npc.model;

import lineage2.commons.lang.reference.HardReference;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.SpecialMonsterInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class MeleonInstance extends SpecialMonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int Young_Watermelon = 13271;
	public final static int Rain_Watermelon = 13273;
	public final static int Defective_Watermelon = 13272;
	public final static int Young_Honey_Watermelon = 13275;
	public final static int Rain_Honey_Watermelon = 13277;
	public final static int Defective_Honey_Watermelon = 13276;
	public final static int Large_Rain_Watermelon = 13274;
	public final static int Large_Rain_Honey_Watermelon = 13278;
	private HardReference<Player> _spawnerRef;
	
	public MeleonInstance(int objectId, NpcTemplate template)
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
		if ((getNpcId() == Defective_Honey_Watermelon) || (getNpcId() == Rain_Honey_Watermelon) || (getNpcId() == Large_Rain_Honey_Watermelon))
		{
			if ((weaponId != 4202) && (weaponId != 5133) && (weaponId != 5817) && (weaponId != 7058) && (weaponId != 8350))
			{
				return;
			}
			i = 1;
		}
		else if ((getNpcId() == Rain_Watermelon) || (getNpcId() == Defective_Watermelon) || (getNpcId() == Large_Rain_Watermelon))
		{
			i = 5;
		}
		else
		{
			return;
		}
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
