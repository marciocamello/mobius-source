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

import lineage2.gameserver.model.Player;

public class RequestDynamicQuestScoreBoard extends L2GameClientPacket
{
	private int _unk1, _unk2, _unk0;
	
	@Override
	protected void readImpl()
	{
		_unk0 = readC();
		_unk1 = readD();
		_unk2 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		activeChar.sendMessage("RequestDynamicQuestScoreBoard: С[" + _unk0 + "] D[" + _unk1 + "] D[" + _unk2 + "]");
	}
}
