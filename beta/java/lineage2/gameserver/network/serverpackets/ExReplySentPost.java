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

import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;

public class ExReplySentPost extends AbstractItemPacket
{
	private final Mail _mail;
	
	public ExReplySentPost(Mail mail)
	{
		_mail = mail;
	}
	
	// ddSSS dx[hddQdddhhhhhhhhhh] Qd
	@Override
	protected void writeImpl()
	{
		writeEx(0xAE);
		writeD(_mail.getType().ordinal());
		// Type = Normal
		writeD(_mail.getMessageId());
		writeD(0x00);// unknown1
		writeS(_mail.getSenderName());
		writeS(_mail.getTopic());
		writeS(_mail.getBody());
		writeD(_mail.getAttachments().size());
		
		for (ItemInstance item : _mail.getAttachments())
		{
			writeItem(item);
			writeD(item.getObjectId());
		}
		
		writeQ(_mail.getPrice());
		writeD(0x00); // unk
		writeD(_mail.isReturnable());
		writeD(_mail.getReceiverId());
	}
}