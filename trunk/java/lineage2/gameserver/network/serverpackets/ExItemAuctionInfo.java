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

import lineage2.gameserver.instancemanager.itemauction.ItemAuction;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionBid;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionState;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExItemAuctionInfo extends L2GameServerPacket
{
	/**
	 * Field _refresh.
	 */
	private final boolean _refresh;
	/**
	 * Field _timeRemaining.
	 */
	private int _timeRemaining;
	/**
	 * Field _currentAuction.
	 */
	private final ItemAuction _currentAuction;
	/**
	 * Field _nextAuction.
	 */
	private final ItemAuction _nextAuction;
	
	/**
	 * Constructor for ExItemAuctionInfo.
	 * @param refresh boolean
	 * @param currentAuction ItemAuction
	 * @param nextAuction ItemAuction
	 */
	public ExItemAuctionInfo(boolean refresh, ItemAuction currentAuction, ItemAuction nextAuction)
	{
		if (currentAuction == null)
		{
			throw new NullPointerException();
		}
		if (currentAuction.getAuctionState() != ItemAuctionState.STARTED)
		{
			_timeRemaining = 0;
		}
		else
		{
			_timeRemaining = (int) (currentAuction.getFinishingTimeRemaining() / 1000);
		}
		_refresh = refresh;
		_currentAuction = currentAuction;
		_nextAuction = nextAuction;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x68);
		writeC(_refresh ? 0x00 : 0x01);
		writeD(_currentAuction.getInstanceId());
		ItemAuctionBid highestBid = _currentAuction.getHighestBid();
		writeQ(highestBid != null ? highestBid.getLastBid() : _currentAuction.getAuctionInitBid());
		writeD(_timeRemaining);
		writeItemInfo(_currentAuction.getAuctionItem());
		if (_nextAuction != null)
		{
			writeQ(_nextAuction.getAuctionInitBid());
			writeD((int) (_nextAuction.getStartingTime() / 1000L));
			writeItemInfo(_nextAuction.getAuctionItem());
		}
	}
}
