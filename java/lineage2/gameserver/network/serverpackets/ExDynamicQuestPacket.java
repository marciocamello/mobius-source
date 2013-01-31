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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExDynamicQuestPacket extends L2GameServerPacket
{
	/**
	 * @author Mobius
	 */
	public static enum State
	{
		/**
		 * Field NOT_CONFIRMED.
		 */
		NOT_CONFIRMED,
		/**
		 * Field NONE.
		 */
		NONE,
		/**
		 * Field CONFIRMED_IN_PROGRESS.
		 */
		CONFIRMED_IN_PROGRESS;
	}
	
	/**
	 * Constructor for ExDynamicQuestPacket.
	 * @param campaignId int
	 * @param step int
	 */
	public ExDynamicQuestPacket(int campaignId, int step)
	{
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xE8);
		writeC(0x02);
		writeC(2);
		int campaignId = 1;
		writeD(campaignId);
		writeD(1);
		writeC(5);
		writeD(120);
		writeD(2);
		writeD((campaignId * 100) + 1);
		writeD(3);
		writeD(5);
	}
}
