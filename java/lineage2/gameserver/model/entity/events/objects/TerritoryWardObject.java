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
package lineage2.gameserver.model.entity.events.objects;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeRunnerEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.TerritoryWardInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.attachment.FlagItemAttachment;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TerritoryWardObject implements SpawnableObject, FlagItemAttachment
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _template.
	 */
	private final NpcTemplate _template;
	/**
	 * Field _location.
	 */
	private final Location _location;
	/**
	 * Field _wardNpcInstance.
	 */
	private NpcInstance _wardNpcInstance;
	/**
	 * Field _wardItemInstance.
	 */
	private ItemInstance _wardItemInstance;
	
	/**
	 * Constructor for TerritoryWardObject.
	 * @param itemId int
	 * @param npcId int
	 * @param location Location
	 */
	public TerritoryWardObject(int itemId, int npcId, Location location)
	{
		_itemId = itemId;
		_template = NpcHolder.getInstance().getTemplate(npcId);
		_location = location;
	}
	
	/**
	 * Method spawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#spawnObject(GlobalEvent)
	 */
	@Override
	public void spawnObject(GlobalEvent event)
	{
		_wardItemInstance = ItemFunctions.createItem(_itemId);
		_wardItemInstance.setAttachment(this);
		_wardNpcInstance = new TerritoryWardInstance(IdFactory.getInstance().getNextId(), _template, this);
		_wardNpcInstance.addEvent(event);
		_wardNpcInstance.setCurrentHpMp(_wardNpcInstance.getMaxHp(), _wardNpcInstance.getMaxMp());
		_wardNpcInstance.spawnMe(_location);
	}
	
	/**
	 * Method despawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#despawnObject(GlobalEvent)
	 */
	@Override
	public void despawnObject(GlobalEvent event)
	{
		if ((_wardItemInstance == null) || (_wardNpcInstance == null))
		{
			return;
		}
		Player owner = GameObjectsStorage.getPlayer(_wardItemInstance.getOwnerId());
		if (owner != null)
		{
			owner.getInventory().destroyItem(_wardItemInstance);
			owner.sendDisarmMessage(_wardItemInstance);
		}
		_wardItemInstance.setAttachment(null);
		_wardItemInstance.setJdbcState(JdbcEntityState.UPDATED);
		_wardItemInstance.delete();
		_wardItemInstance.deleteMe();
		_wardItemInstance = null;
		_wardNpcInstance.deleteMe();
		_wardNpcInstance = null;
	}
	
	/**
	 * Method refreshObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#refreshObject(GlobalEvent)
	 */
	@Override
	public void refreshObject(GlobalEvent event)
	{
	}
	
	/**
	 * Method onLogout.
	 * @param player Player
	 * @see lineage2.gameserver.model.items.attachment.FlagItemAttachment#onLogout(Player)
	 */
	@Override
	public void onLogout(Player player)
	{
		player.getInventory().removeItem(_wardItemInstance);
		_wardItemInstance.setOwnerId(0);
		_wardItemInstance.setJdbcState(JdbcEntityState.UPDATED);
		_wardItemInstance.update();
		_wardNpcInstance.setCurrentHpMp(_wardNpcInstance.getMaxHp(), _wardNpcInstance.getMaxMp(), true);
		_wardNpcInstance.spawnMe(_location);
	}
	
	/**
	 * Method onDeath.
	 * @param owner Player
	 * @param killer Creature
	 * @see lineage2.gameserver.model.items.attachment.FlagItemAttachment#onDeath(Player, Creature)
	 */
	@Override
	public void onDeath(Player owner, Creature killer)
	{
		Location loc = owner.getLoc();
		owner.getInventory().removeItem(_wardItemInstance);
		owner.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_DROPPED_S1).addName(_wardItemInstance));
		_wardItemInstance.setOwnerId(0);
		_wardItemInstance.setJdbcState(JdbcEntityState.UPDATED);
		_wardItemInstance.update();
		_wardNpcInstance.setCurrentHpMp(_wardNpcInstance.getMaxHp(), _wardNpcInstance.getMaxMp(), true);
		_wardNpcInstance.spawnMe(loc);
		DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
		runnerEvent.broadcastTo(new SystemMessage2(SystemMsg.THE_CHARACTER_THAT_ACQUIRED_S1S_WARD_HAS_BEEN_KILLED).addResidenceName(getDominionId()));
	}
	
	/**
	 * Method canPickUp.
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.model.items.attachment.PickableAttachment#canPickUp(Player)
	 */
	@Override
	public boolean canPickUp(Player player)
	{
		return true;
	}
	
	/**
	 * Method pickUp.
	 * @param player Player
	 * @see lineage2.gameserver.model.items.attachment.PickableAttachment#pickUp(Player)
	 */
	@Override
	public void pickUp(Player player)
	{
		player.getInventory().addItem(_wardItemInstance);
		player.getInventory().equipItem(_wardItemInstance);
		player.sendPacket(SystemMsg.YOUVE_ACQUIRED_THE_WARD);
		DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
		runnerEvent.broadcastTo(new SystemMessage2(SystemMsg.THE_S1_WARD_HAS_BEEN_DESTROYED_C2_NOW_HAS_THE_TERRITORY_WARD).addResidenceName(getDominionId()).addName(player));
	}
	
	/**
	 * Method canAttack.
	 * @param player Player
	 * @return boolean * @see lineage2.gameserver.model.items.attachment.FlagItemAttachment#canAttack(Player)
	 */
	@Override
	public boolean canAttack(Player player)
	{
		player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_PERFORM_ANY_ATTACKS);
		return false;
	}
	
	/**
	 * Method canCast.
	 * @param player Player
	 * @param skill Skill
	 * @return boolean * @see lineage2.gameserver.model.items.attachment.FlagItemAttachment#canCast(Player, Skill)
	 */
	@Override
	public boolean canCast(Player player, Skill skill)
	{
		Skill[] skills = player.getActiveWeaponItem().getAttachedSkills();
		if (!ArrayUtils.contains(skills, skill))
		{
			player.sendPacket(SystemMsg.THAT_WEAPON_CANNOT_USE_ANY_OTHER_SKILL_EXCEPT_THE_WEAPONS_SKILL);
			return false;
		}
		return true;
	}
	
	/**
	 * Method setItem.
	 * @param item ItemInstance
	 * @see lineage2.gameserver.model.items.attachment.ItemAttachment#setItem(ItemInstance)
	 */
	@Override
	public void setItem(ItemInstance item)
	{
	}
	
	/**
	 * Method getWardLocation.
	 * @return Location
	 */
	public Location getWardLocation()
	{
		if ((_wardItemInstance == null) || (_wardNpcInstance == null))
		{
			return null;
		}
		if (_wardItemInstance.getOwnerId() > 0)
		{
			Player player = GameObjectsStorage.getPlayer(_wardItemInstance.getOwnerId());
			if (player != null)
			{
				return player.getLoc();
			}
		}
		return _wardNpcInstance.getLoc();
	}
	
	/**
	 * Method getWardNpcInstance.
	 * @return NpcInstance
	 */
	public NpcInstance getWardNpcInstance()
	{
		return _wardNpcInstance;
	}
	
	/**
	 * Method getWardItemInstance.
	 * @return ItemInstance
	 */
	public ItemInstance getWardItemInstance()
	{
		return _wardItemInstance;
	}
	
	/**
	 * Method getDominionId.
	 * @return int
	 */
	public int getDominionId()
	{
		return _itemId - 13479;
	}
	
	/**
	 * Method getEvent.
	 * @return DominionSiegeEvent
	 */
	public DominionSiegeEvent getEvent()
	{
		return _wardNpcInstance.getEvent(DominionSiegeEvent.class);
	}
}
