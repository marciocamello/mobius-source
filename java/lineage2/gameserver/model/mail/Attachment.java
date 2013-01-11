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
package lineage2.gameserver.model.mail;

import lineage2.gameserver.model.items.ItemInstance;

public class Attachment
{
	private int messageId;
	private ItemInstance item;
	private Mail mail;
	
	public int getMessageId()
	{
		return messageId;
	}
	
	public void setMessageId(int messageId)
	{
		this.messageId = messageId;
	}
	
	public ItemInstance getItem()
	{
		return item;
	}
	
	public void setItem(ItemInstance item)
	{
		this.item = item;
	}
	
	public Mail getMail()
	{
		return mail;
	}
	
	public void setMail(Mail mail)
	{
		this.mail = mail;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != this.getClass())
		{
			return false;
		}
		return ((Attachment) o).getItem() == getItem();
	}
}
