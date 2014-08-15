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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Smo
 */
public class RequestPledgeRecruitBoardAccess extends L2GameClientPacket
{
	public static final Logger _log = LoggerFactory.getLogger(RequestPledgeRecruitBoardAccess.class);
	public int test1, test2;
	public String test3, test4;
	
	@Override
	protected void readImpl()
	{
		test1 = readD();
		test2 = readD(); // Karma
		test3 = readS(); // Short Description
		test4 = readS(); // Description
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		_log.info("______________________________________");
		_log.info("RequestPledgeRecruitBoardAccess");
		_log.info("test1: " + test1);
		_log.info("Karma: " + test2);
		_log.info("Introduction: " + test3);
		_log.info("Detailed Introduction: " + test4);
		_log.info("______________________________________");
	}
}
