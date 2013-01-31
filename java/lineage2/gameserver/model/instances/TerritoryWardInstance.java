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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.objects.TerritoryWardObject;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TerritoryWardInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _territoryWard.
	 */
	private final TerritoryWardObject _territoryWard;
	
	/**
	 * Constructor for TerritoryWardInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 * @param territoryWardObject TerritoryWardObject
	 */
	public TerritoryWardInstance(int objectId, NpcTemplate template, TerritoryWardObject territoryWardObject)
	{
		super(objectId, template);
		setHasChatWindow(false);
		_territoryWard = territoryWardObject;
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	public void onDeath(Creature killer)
	{
		super.onDeath(killer);
		Player player = killer.getPlayer();
		if (player == null)
		{
			return;
		}
		if (_territoryWard.canPickUp(player))
		{
			_territoryWard.pickUp(player);
			decayMe();
		}
	}
	
	/**
	 * Method onDecay.
	 */
	@Override
	protected void onDecay()
	{
		decayMe();
		_spawnAnimation = 2;
	}
	
	/**
	 * Method isAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature attacker)
	{
		return isAutoAttackable(attacker);
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param attacker Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		DominionSiegeEvent siegeEvent = getEvent(DominionSiegeEvent.class);
		if (siegeEvent == null)
		{
			return false;
		}
		DominionSiegeEvent siegeEvent2 = attacker.getEvent(DominionSiegeEvent.class);
		if (siegeEvent2 == null)
		{
			return false;
		}
		if (siegeEvent == siegeEvent2)
		{
			return false;
		}
		if (siegeEvent2.getResidence().getOwner() != attacker.getClan())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return false;
	}
	
	/**
	 * Method getClan.
	 * @return Clan
	 */
	@Override
	public Clan getClan()
	{
		return null;
	}
}
