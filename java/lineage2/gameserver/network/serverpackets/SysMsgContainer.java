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
package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class SysMsgContainer<T extends SysMsgContainer<T>> extends L2GameServerPacket
{
	/**
	 * @author Mobius
	 */
	public static enum Types
	{
		/**
		 * Field TEXT.
		 */
		TEXT,
		/**
		 * Field NUMBER.
		 */
		NUMBER,
		/**
		 * Field NPC_NAME.
		 */
		NPC_NAME,
		/**
		 * Field ITEM_NAME.
		 */
		ITEM_NAME,
		/**
		 * Field SKILL_NAME.
		 */
		SKILL_NAME,
		/**
		 * Field RESIDENCE_NAME.
		 */
		RESIDENCE_NAME,
		/**
		 * Field LONG.
		 */
		LONG,
		/**
		 * Field ZONE_NAME.
		 */
		ZONE_NAME,
		/**
		 * Field ITEM_NAME_WITH_AUGMENTATION.
		 */
		ITEM_NAME_WITH_AUGMENTATION,
		/**
		 * Field ELEMENT_NAME.
		 */
		ELEMENT_NAME,
		/**
		 * Field INSTANCE_NAME.
		 */
		INSTANCE_NAME,
		/**
		 * Field STATIC_OBJECT_NAME.
		 */
		STATIC_OBJECT_NAME,
		/**
		 * Field PLAYER_NAME.
		 */
		PLAYER_NAME,
		/**
		 * Field SYSTEM_STRING.
		 */
		SYSTEM_STRING
	}
	
	/**
	 * Field _message.
	 */
	protected SystemMsg _message;
	/**
	 * Field _arguments.
	 */
	protected List<IArgument> _arguments;
	
	/**
	 * Constructor for SysMsgContainer.
	 * @param messageId int
	 */
	@Deprecated
	protected SysMsgContainer(int messageId)
	{
		this(SystemMsg.valueOf(messageId));
	}
	
	/**
	 * Constructor for SysMsgContainer.
	 * @param message SystemMsg
	 */
	protected SysMsgContainer(SystemMsg message)
	{
		if (message == null)
		{
			throw new IllegalArgumentException("SystemMsg is null");
		}
		_message = message;
		_arguments = new ArrayList<>(_message.size());
	}
	
	/**
	 * Method writeElements.
	 */
	protected void writeElements()
	{
		if (_message.size() != _arguments.size())
		{
			throw new IllegalArgumentException("Wrong count of arguments: " + _message);
		}
		writeD(_message.getId());
		writeD(_arguments.size());
		for (IArgument argument : _arguments)
		{
			argument.write(this);
		}
	}
	
	/**
	 * Method addName.
	 * @param object GameObject
	 * @return T
	 */
	public T addName(GameObject object)
	{
		if (object == null)
		{
			return add(new StringArgument(null));
		}
		if (object.isNpc())
		{
			return add(new NpcNameArgument(((NpcInstance) object).getNpcId() + 1000000));
		}
		else if (object instanceof Summon)
		{
			return add(new NpcNameArgument(((Summon) object).getNpcId() + 1000000));
		}
		else if (object.isItem())
		{
			return add(new ItemNameArgument(((ItemInstance) object).getItemId()));
		}
		else if (object.isPlayer())
		{
			return add(new PlayerNameArgument((Player) object));
		}
		else if (object.isDoor())
		{
			return add(new StaticObjectNameArgument(((DoorInstance) object).getDoorId()));
		}
		else if (object instanceof StaticObjectInstance)
		{
			return add(new StaticObjectNameArgument(((StaticObjectInstance) object).getUId()));
		}
		return add(new StringArgument(object.getName()));
	}
	
	/**
	 * Method addInstanceName.
	 * @param id int
	 * @return T
	 */
	public T addInstanceName(int id)
	{
		return add(new InstanceNameArgument(id));
	}
	
	/**
	 * Method addSysString.
	 * @param id int
	 * @return T
	 */
	public T addSysString(int id)
	{
		return add(new SysStringArgument(id));
	}
	
	/**
	 * Method addSkillName.
	 * @param skill Skill
	 * @return T
	 */
	public T addSkillName(Skill skill)
	{
		return addSkillName(skill.getDisplayId(), skill.getDisplayLevel());
	}
	
	/**
	 * Method addSkillName.
	 * @param id int
	 * @param level int
	 * @return T
	 */
	public T addSkillName(int id, int level)
	{
		return add(new SkillArgument(id, level));
	}
	
	/**
	 * Method addItemName.
	 * @param item_id int
	 * @return T
	 */
	public T addItemName(int item_id)
	{
		return add(new ItemNameArgument(item_id));
	}
	
	/**
	 * Method addItemNameWithAugmentation.
	 * @param item ItemInstance
	 * @return T
	 */
	public T addItemNameWithAugmentation(ItemInstance item)
	{
		return add(new ItemNameWithAugmentationArgument(item.getItemId(), item.getAugmentationId()));
	}
	
	/**
	 * Method addZoneName.
	 * @param c Creature
	 * @return T
	 */
	public T addZoneName(Creature c)
	{
		return addZoneName(c.getX(), c.getY(), c.getZ());
	}
	
	/**
	 * Method addZoneName.
	 * @param loc Location
	 * @return T
	 */
	public T addZoneName(Location loc)
	{
		return add(new ZoneArgument(loc.x, loc.y, loc.z));
	}
	
	/**
	 * Method addZoneName.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return T
	 */
	public T addZoneName(int x, int y, int z)
	{
		return add(new ZoneArgument(x, y, z));
	}
	
	/**
	 * Method addResidenceName.
	 * @param r Residence
	 * @return T
	 */
	public T addResidenceName(Residence r)
	{
		return add(new ResidenceArgument(r.getId()));
	}
	
	/**
	 * Method addResidenceName.
	 * @param i int
	 * @return T
	 */
	public T addResidenceName(int i)
	{
		return add(new ResidenceArgument(i));
	}
	
	/**
	 * Method addElementName.
	 * @param i int
	 * @return T
	 */
	public T addElementName(int i)
	{
		return add(new ElementNameArgument(i));
	}
	
	/**
	 * Method addElementName.
	 * @param i Element
	 * @return T
	 */
	public T addElementName(Element i)
	{
		return add(new ElementNameArgument(i.getId()));
	}
	
	/**
	 * Method addInteger.
	 * @param i double
	 * @return T
	 */
	public T addInteger(double i)
	{
		return add(new IntegerArgument((int) i));
	}
	
	/**
	 * Method addLong.
	 * @param i long
	 * @return T
	 */
	public T addLong(long i)
	{
		return add(new LongArgument(i));
	}
	
	/**
	 * Method addString.
	 * @param t String
	 * @return T
	 */
	public T addString(String t)
	{
		return add(new StringArgument(t));
	}
	
	/**
	 * Method add.
	 * @param arg IArgument
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	protected T add(IArgument arg)
	{
		_arguments.add(arg);
		return (T) this;
	}
	
	/**
	 * @author Mobius
	 */
	public static abstract class IArgument
	{
		/**
		 * Method write.
		 * @param m SysMsgContainer<?>
		 */
		void write(SysMsgContainer<?> m)
		{
			m.writeD(getType().ordinal());
			writeData(m);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		abstract Types getType();
		
		/**
		 * Method writeData.
		 * @param message SysMsgContainer<?>
		 */
		abstract void writeData(SysMsgContainer<?> message);
	}
	
	/**
	 * @author Mobius
	 */
	public static class IntegerArgument extends IArgument
	{
		/**
		 * Field _data.
		 */
		private final int _data;
		
		/**
		 * Constructor for IntegerArgument.
		 * @param da int
		 */
		public IntegerArgument(int da)
		{
			_data = da;
		}
		
		/**
		 * Method writeData.
		 * @param message SysMsgContainer<?>
		 */
		@Override
		public void writeData(SysMsgContainer<?> message)
		{
			message.writeD(_data);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.NUMBER;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class NpcNameArgument extends IntegerArgument
	{
		/**
		 * Constructor for NpcNameArgument.
		 * @param da int
		 */
		public NpcNameArgument(int da)
		{
			super(da);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.NPC_NAME;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class ItemNameArgument extends IntegerArgument
	{
		/**
		 * Constructor for ItemNameArgument.
		 * @param da int
		 */
		public ItemNameArgument(int da)
		{
			super(da);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.ITEM_NAME;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class ItemNameWithAugmentationArgument extends IArgument
	{
		/**
		 * Field _itemId.
		 */
		private final int _itemId;
		/**
		 * Field _augmentationId.
		 */
		private final int _augmentationId;
		
		/**
		 * Constructor for ItemNameWithAugmentationArgument.
		 * @param itemId int
		 * @param augmentationId int
		 */
		public ItemNameWithAugmentationArgument(int itemId, int augmentationId)
		{
			_itemId = itemId;
			_augmentationId = augmentationId;
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.ITEM_NAME_WITH_AUGMENTATION;
		}
		
		/**
		 * Method writeData.
		 * @param message SysMsgContainer<?>
		 */
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeD(_itemId);
			message.writeD(_augmentationId);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class InstanceNameArgument extends IntegerArgument
	{
		/**
		 * Constructor for InstanceNameArgument.
		 * @param da int
		 */
		public InstanceNameArgument(int da)
		{
			super(da);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.INSTANCE_NAME;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class SysStringArgument extends IntegerArgument
	{
		/**
		 * Constructor for SysStringArgument.
		 * @param da int
		 */
		public SysStringArgument(int da)
		{
			super(da);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.SYSTEM_STRING;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class ResidenceArgument extends IntegerArgument
	{
		/**
		 * Constructor for ResidenceArgument.
		 * @param da int
		 */
		public ResidenceArgument(int da)
		{
			super(da);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.RESIDENCE_NAME;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class StaticObjectNameArgument extends IntegerArgument
	{
		/**
		 * Constructor for StaticObjectNameArgument.
		 * @param da int
		 */
		public StaticObjectNameArgument(int da)
		{
			super(da);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.STATIC_OBJECT_NAME;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class LongArgument extends IArgument
	{
		/**
		 * Field _data.
		 */
		private final long _data;
		
		/**
		 * Constructor for LongArgument.
		 * @param da long
		 */
		public LongArgument(long da)
		{
			_data = da;
		}
		
		/**
		 * Method writeData.
		 * @param message SysMsgContainer<?>
		 */
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeQ(_data);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.LONG;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class StringArgument extends IArgument
	{
		/**
		 * Field _data.
		 */
		private final String _data;
		
		/**
		 * Constructor for StringArgument.
		 * @param da String
		 */
		public StringArgument(String da)
		{
			_data = da == null ? "null" : da;
		}
		
		/**
		 * Method writeData.
		 * @param message SysMsgContainer<?>
		 */
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeS(_data);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.TEXT;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class SkillArgument extends IArgument
	{
		/**
		 * Field _skillId.
		 */
		private final int _skillId;
		/**
		 * Field _skillLevel.
		 */
		private final int _skillLevel;
		
		/**
		 * Constructor for SkillArgument.
		 * @param t1 int
		 * @param t2 int
		 */
		public SkillArgument(int t1, int t2)
		{
			_skillId = t1;
			_skillLevel = t2;
		}
		
		/**
		 * Method writeData.
		 * @param message SysMsgContainer<?>
		 */
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeD(_skillId);
			message.writeD(_skillLevel);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.SKILL_NAME;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class ZoneArgument extends IArgument
	{
		/**
		 * Field _x.
		 */
		private final int _x;
		/**
		 * Field _y.
		 */
		private final int _y;
		/**
		 * Field _z.
		 */
		private final int _z;
		
		/**
		 * Constructor for ZoneArgument.
		 * @param t1 int
		 * @param t2 int
		 * @param t3 int
		 */
		public ZoneArgument(int t1, int t2, int t3)
		{
			_x = t1;
			_y = t2;
			_z = t3;
		}
		
		/**
		 * Method writeData.
		 * @param message SysMsgContainer<?>
		 */
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeD(_x);
			message.writeD(_y);
			message.writeD(_z);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.ZONE_NAME;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class ElementNameArgument extends IntegerArgument
	{
		/**
		 * Constructor for ElementNameArgument.
		 * @param type int
		 */
		public ElementNameArgument(int type)
		{
			super(type);
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.ELEMENT_NAME;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class PlayerNameArgument extends StringArgument
	{
		/**
		 * Constructor for PlayerNameArgument.
		 * @param creature Creature
		 */
		public PlayerNameArgument(Creature creature)
		{
			super(creature.getName());
		}
		
		/**
		 * Method getType.
		 * @return Types
		 */
		@Override
		Types getType()
		{
			return Types.PLAYER_NAME;
		}
	}
}
