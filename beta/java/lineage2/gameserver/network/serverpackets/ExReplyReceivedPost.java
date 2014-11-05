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

public class ExReplyReceivedPost extends AbstractItemPacket
{
	private final Mail mail;
	
	public ExReplyReceivedPost(Mail mail)
	{
		this.mail = mail;
	}
	
	// dddSSS dx[hddQdddhhhhhhhhhh] Qdd
	@Override
	protected void writeImpl()
	{
		writeEx(0xAC);
		writeD(mail.getType().ordinal());
		
		if (mail.getType() == Mail.SenderType.SYSTEM)
		{
			writeD(0x00);// unknown1
			writeD(0x00);// unknown2
			writeD(0x00);// unknown3
			writeD(0x00);// unknown4
			writeD(0x00);// unknown5
			writeD(0x00);// unknown6
			writeD(0x00);// unknown7
			writeD(0x00);// unknown8
			writeD(mail.getSystemMsg1());
			writeD(mail.getSystemMsg2());
		}
		
		// Type = Normal
		writeD(mail.getMessageId());
		writeD(0x00);// unknown2
		writeD(0x00);// unknown3
		writeS(mail.getSenderName());
		writeS(mail.getTopic());
		writeS(mail.getBody());
		writeD(mail.getAttachments().size());
		
		for (ItemInstance item : mail.getAttachments())
		{
			writeItem(item);
			writeD(item.getObjectId());
		}
		
		writeQ(mail.getPrice());
		writeD(0x00); // unk
		writeD(mail.isReturnable());
		writeD(mail.getReceiverId());
	}
}