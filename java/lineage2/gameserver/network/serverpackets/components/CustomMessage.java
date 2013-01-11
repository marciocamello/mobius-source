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
package lineage2.gameserver.network.serverpackets.components;

import lineage2.gameserver.data.StringHolder;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.item.ItemTemplate;

public class CustomMessage
{
	private String _text;
	private int mark = 0;
	
	public CustomMessage(String address, Player player, Object... args)
	{
		_text = StringHolder.getInstance().getNotNull(player, address);
		add(args);
	}
	
	public CustomMessage addNumber(long number)
	{
		_text = _text.replace("{" + mark + "}", String.valueOf(number));
		mark++;
		return this;
	}
	
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
	
	public CustomMessage addString(String str)
	{
		_text = _text.replace("{" + mark + "}", str);
		mark++;
		return this;
	}
	
	public CustomMessage addSkillName(Skill skill)
	{
		_text = _text.replace("{" + mark + "}", skill.getName());
		mark++;
		return this;
	}
	
	public CustomMessage addSkillName(int skillId, int skillLevel)
	{
		return addSkillName(SkillTable.getInstance().getInfo(skillId, skillLevel));
	}
	
	public CustomMessage addItemName(ItemTemplate item)
	{
		_text = _text.replace("{" + mark + "}", item.getName());
		mark++;
		return this;
	}
	
	public CustomMessage addItemName(int itemId)
	{
		return addItemName(ItemHolder.getInstance().getTemplate(itemId));
	}
	
	public CustomMessage addItemName(ItemInstance item)
	{
		return addItemName(item.getTemplate());
	}
	
	public CustomMessage addCharName(Creature cha)
	{
		_text = _text.replace("{" + mark + "}", cha.getName());
		mark++;
		return this;
	}
	
	@Override
	public String toString()
	{
		return _text;
	}
}
