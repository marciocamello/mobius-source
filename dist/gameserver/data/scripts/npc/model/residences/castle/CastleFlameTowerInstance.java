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
package npc.model.residences.castle;

import java.util.List;
import java.util.Set;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.events.objects.CastleDamageZoneObject;
import lineage2.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class CastleFlameTowerInstance extends SiegeToggleNpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<String> _zoneList;
	
	public CastleFlameTowerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onDeathImpl(Creature killer)
	{
		CastleSiegeEvent event = getEvent(CastleSiegeEvent.class);
		if ((event == null) || !event.isInProgress())
		{
			return;
		}
		for (String s : _zoneList)
		{
			List<CastleDamageZoneObject> objects = event.getObjects(s);
			for (CastleDamageZoneObject zone : objects)
			{
				zone.getZone().setActive(false);
			}
		}
	}
	
	@Override
	public void setZoneList(Set<String> set)
	{
		_zoneList = set;
	}
}
