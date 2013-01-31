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
package lineage2.gameserver.model.instances.residences.dominion;

import lineage2.commons.geometry.Circle;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.instances.residences.SiegeFlagInstance;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncMul;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OutpostInstance extends SiegeFlagInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	private class OnZoneEnterLeaveListenerImpl implements OnZoneEnterLeaveListener
	{
		/**
		 * Constructor for OnZoneEnterLeaveListenerImpl.
		 */
		public OnZoneEnterLeaveListenerImpl()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param actor Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature actor)
		{
			DominionSiegeEvent siegeEvent = OutpostInstance.this.getEvent(DominionSiegeEvent.class);
			if (siegeEvent == null)
			{
				return;
			}
			if (actor.getEvent(DominionSiegeEvent.class) != siegeEvent)
			{
				return;
			}
			actor.addStatFunc(new FuncMul(Stats.REGENERATE_HP_RATE, 0x40, OutpostInstance.this, 2.));
			actor.addStatFunc(new FuncMul(Stats.REGENERATE_MP_RATE, 0x40, OutpostInstance.this, 2.));
			actor.addStatFunc(new FuncMul(Stats.REGENERATE_CP_RATE, 0x40, OutpostInstance.this, 2.));
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param actor Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
			actor.removeStatsOwner(OutpostInstance.this);
		}
	}
	
	/**
	 * Field _zone.
	 */
	private Zone _zone = null;
	
	/**
	 * Constructor for OutpostInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public OutpostInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		Circle c = new Circle(getLoc(), 250);
		c.setZmax(World.MAP_MAX_Z);
		c.setZmin(World.MAP_MIN_Z);
		StatsSet set = new StatsSet();
		set.set("name", StringUtils.EMPTY);
		set.set("type", Zone.ZoneType.dummy);
		set.set("territory", new Territory().add(c));
		_zone = new Zone(new ZoneTemplate(set));
		_zone.setReflection(ReflectionManager.DEFAULT);
		_zone.addListener(new OnZoneEnterLeaveListenerImpl());
		_zone.setActive(true);
	}
	
	/**
	 * Method onDelete.
	 */
	@Override
	public void onDelete()
	{
		super.onDelete();
		_zone.setActive(false);
		_zone = null;
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return true;
	}
}
