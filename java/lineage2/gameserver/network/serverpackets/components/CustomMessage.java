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
package lineage2.gameserver.network.serverpackets.components;

import lineage2.gameserver.data.StringHolder;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CustomMessage
{
	/**
	 * Field _text.
	 */
	private String _text;
	/**
	 * Field mark.
	 */
	private int mark = 0;
	
	/**
	 * Constructor for CustomMessage.
	 * @param address String
	 * @param player Player
	 * @param args Object[]
	 */
	public CustomMessage(String address, Player player, Object... args)
	{
		_text = StringHolder.getInstance().getNotNull(player, address);
		add(args);
	}
	
	/**
	 * Method addNumber.
	 * @param number long
	 * @return CustomMessage
	 */
	public CustomMessage addNumber(long number)
	{
		_text = _text.replace("{" + mark + "}", String.valueOf(number));
		mark++;
		return this;
	}
	
	/**
	 * Method add.
	 * @param args Object[]
	 * @return CustomMessage
	 */
	public CustomMessage add(Object... args)
	{
		for (Object arg : args)
		{
			if (arg instanceof String)
			{
				addString((String) arg);
			}
			else if (arg instanceof Integer)
			{
				addNumber((Integer) arg);
			}
			else if (arg instanceof Long)
			{
				addNumber((Long) arg);
			}
			else if (arg instanceof ItemTemplate)
			{
				addItemName((ItemTemplate) arg);
			}
			else if (arg instanceof ItemInstance)
			{
				addItemName((ItemInstance) arg);
			}
			else if (arg instanceof Creature)
			{
				addCharName((Creature) arg);
			}
			else if (arg instanceof Skill)
			{
				this.addSkillName((Skill) arg);
			}
			else
			{
				System.out.println("unknown CustomMessage arg type: " + arg);
				Thread.dumpStack();
			}
		}
		return this;
	}
	
	/**
	 * Method addString.
	 * @param str String
	 * @return CustomMessage
	 */
	public CustomMessage addString(String str)
	{
		_text = _text.replace("{" + mark + "}", str);
		mark++;
		return this;
	}
	
	/**
	 * Method addSkillName.
	 * @param skill Skill
	 * @return CustomMessage
	 */
	public CustomMessage addSkillName(Skill skill)
	{
		_text = _text.replace("{" + mark + "}", skill.getName());
		mark++;
		return this;
	}
	
	/**
	 * Method addSkillName.
	 * @param skillId int
	 * @param skillLevel int
	 * @return CustomMessage
	 */
	public CustomMessage addSkillName(int skillId, int skillLevel)
	{
		return addSkillName(SkillTable.getInstance().getInfo(skillId, skillLevel));
	}
	
	/**
	 * Method addItemName.
	 * @param item ItemTemplate
	 * @return CustomMessage
	 */
	public CustomMessage addItemName(ItemTemplate item)
	{
		_text = _text.replace("{" + mark + "}", item.getName());
		mark++;
		return this;
	}
	
	/**
	 * Method addItemName.
	 * @param itemId int
	 * @return CustomMessage
	 */
	public CustomMessage addItemName(int itemId)
	{
		return addItemName(ItemHolder.getInstance().getTemplate(itemId));
	}
	
	/**
	 * Method addItemName.
	 * @param item ItemInstance
	 * @return CustomMessage
	 */
	public CustomMessage addItemName(ItemInstance item)
	{
		return addItemName(item.getTemplate());
	}
	
	/**
	 * Method addCharName.
	 * @param cha Creature
	 * @return CustomMessage
	 */
	public CustomMessage addCharName(Creature cha)
	{
		_text = _text.replace("{" + mark + "}", cha.getName());
		mark++;
		return this;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return _text;
	}
}
