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
package lineage2.gameserver.network.clientpackets.PledgeRecruit;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.clientpackets.L2GameClientPacket;
import lineage2.gameserver.network.serverpackets.PledgeRecruit.ExPledgeRecruitBoardSearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Smo
 */
public class RequestPledgeRecruitBoardSearch extends L2GameClientPacket
{
	public static final Logger _log = LoggerFactory.getLogger(RequestPledgeRecruitBoardSearch.class);
	public int t1, t2, t3;
	public String t4;
	public int t5, t6, t7;
	
	@Override
	protected void readImpl()
	{
		t1 = readD();
		t2 = readD();
		t3 = readD();
		t4 = readS();
		t5 = readD();
		t6 = readD();
		t7 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		_log.info("RequestPledgeRecruitBoardSearch--> " + t1 + "|" + t2 + "|" + t3 + "|" + t4);
		activeChar.sendPacket(new ExPledgeRecruitBoardSearch(activeChar));
	}
}
