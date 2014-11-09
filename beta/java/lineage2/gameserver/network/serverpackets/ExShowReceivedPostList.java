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

import java.util.List;

import lineage2.commons.collections.CollectionUtils;
import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.mail.Mail;

public class ExShowReceivedPostList extends L2GameServerPacket
{
	private final List<Mail> mails;
	
	public ExShowReceivedPostList(Player cha)
	{
		mails = MailDAO.getInstance().getReceivedMailByOwnerId(cha.getObjectId());
		CollectionUtils.eqSort(mails);
	}
	
	// d dx[dSSddddddd]
	@Override
	protected void writeImpl()
	{
		writeEx(0xAB);
		writeD((int) (System.currentTimeMillis() / 1000L));
		writeD(mails.size());
		
		for (Mail mail : mails)
		{
			writeD(mail.getType().ordinal());
			// writeD(mail.getType() == Mail.SenderType.SYSTEM ? mail.getSystemMsg1() : 0x00); TODO: Test this !!!
			writeD(mail.getMessageId());
			writeS(mail.getTopic());
			writeS(mail.getSenderName());
			writeD(mail.isPayOnDelivery() ? 1 : 0);
			writeD(mail.getExpireTime());
			writeD(mail.isUnread() ? 1 : 0);
			writeD(mail.isReturnable()); // returnable
			writeD(mail.getAttachments().isEmpty() ? 0 : 1);
			writeD(0x00); // unknown
			writeD(mail.getReceiverId());
		}
		
		writeD(100);
		writeD(1000);
	}
}