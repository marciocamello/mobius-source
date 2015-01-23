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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.cache.CrestCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

public class RequestSetPledgeCrestLarge extends L2GameClientPacket
{
	private int _length;
	private byte[] _data;
	private int _request;
	@SuppressWarnings("unused")
	private int _total;
	
	private byte[] concatenateByteArrays(byte[] a, byte[] b)
	{
		byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_request = readD(); // _data index 0/1/2/3/4
		_total = readD(); // _length-Total 14336*4+8320=65664
		_length = readD();// _length-Split 14336/14336/14336/14336/8320
		
		if ((_length != 0) && (_length == _buf.remaining()))
		{
			_data = new byte[_length];
			readB(_data);
			
			if (_request == 0)
			{
				CrestCache.getInstance().crestLargeTmp = _data;
			}
			else
			{
				CrestCache.getInstance().crestLargeTmp = concatenateByteArrays(CrestCache.getInstance().crestLargeTmp, _data);
			}
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if (_request == 4)
		{
			Player activeChar = getClient().getActiveChar();
			
			if (activeChar == null)
			{
				return;
			}
			
			Clan clan = activeChar.getClan();
			
			if (clan == null)
			{
				return;
			}
			
			if ((activeChar.getClanPrivileges() & Clan.CP_CL_EDIT_CREST) == Clan.CP_CL_EDIT_CREST)
			{
				if ((clan.getCastle() == 0) && (clan.getHasHideout() == 0))
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_CLAN_MARK_WAS_SUCCESSFULLY_REGISTERED_THE_SYMBOL_WILL_APPEAR_ON_THE_CLAN_FLAG_AND_THE_INSIGNIA_IS_ONLY_DISPLAYED_ON_ITEMS_PERTAINING_TO_A_CLAN_THAT_OWNS_A_CLAN_HALL_OR_CASTLE));
					return;
				}
				
				int crestId = 0;
				crestId = CrestCache.getInstance().savePledgeCrestLarge(clan.getClanId(), CrestCache.getInstance().crestLargeTmp);
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_CLAN_MARK_WAS_SUCCESSFULLY_REGISTERED_THE_SYMBOL_WILL_APPEAR_ON_THE_CLAN_FLAG_AND_THE_INSIGNIA_IS_ONLY_DISPLAYED_ON_ITEMS_PERTAINING_TO_A_CLAN_THAT_OWNS_A_CLAN_HALL_OR_CASTLE));
				clan.setCrestLargeId(crestId);
				clan.broadcastClanStatus(false, true, false);
			}
		}
		else if ((_request == 0) && (_data == null))
		{
			Player activeChar = getClient().getActiveChar();
			
			if (activeChar == null)
			{
				return;
			}
			
			Clan clan = activeChar.getClan();
			
			if (clan == null)
			{
				return;
			}
			
			if ((activeChar.getClanPrivileges() & Clan.CP_CL_EDIT_CREST) == Clan.CP_CL_EDIT_CREST)
			{
				if ((clan.getCastle() == 0) && (clan.getHasHideout() == 0))
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_CLAN_MARK_WAS_SUCCESSFULLY_REGISTERED_THE_SYMBOL_WILL_APPEAR_ON_THE_CLAN_FLAG_AND_THE_INSIGNIA_IS_ONLY_DISPLAYED_ON_ITEMS_PERTAINING_TO_A_CLAN_THAT_OWNS_A_CLAN_HALL_OR_CASTLE));
					return;
				}
				
				int crestId = 0;
				
				if (clan.hasCrestLarge())
				{
					CrestCache.getInstance().removePledgeCrestLarge(clan.getClanId());
					activeChar.sendMessage("Large Crest Deleted.");
				}
				
				clan.setCrestLargeId(crestId);
				clan.broadcastClanStatus(false, true, false);
			}
		}
	}
}
