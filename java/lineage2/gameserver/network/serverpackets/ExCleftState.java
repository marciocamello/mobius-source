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
public class ExCleftState extends L2GameServerPacket
{
	/**
	 * Field CleftState_Total. (value is 0)
	 */
	public static final int CleftState_Total = 0;
	/**
	 * Field CleftState_TowerDestroy. (value is 1)
	 */
	public static final int CleftState_TowerDestroy = 1;
	/**
	 * Field CleftState_CatUpdate. (value is 2)
	 */
	public static final int CleftState_CatUpdate = 2;
	/**
	 * Field CleftState_Result. (value is 3)
	 */
	public static final int CleftState_Result = 3;
	/**
	 * Field CleftState_PvPKill. (value is 4)
	 */
	public static final int CleftState_PvPKill = 4;
	/**
	 * Field CleftState.
	 */
	private final int CleftState = 0;
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x95);
		writeD(CleftState);
		switch (CleftState)
		{
			case CleftState_Total:
				break;
			case CleftState_TowerDestroy:
				break;
			case CleftState_CatUpdate:
				break;
			case CleftState_Result:
				break;
			case CleftState_PvPKill:
				break;
		}
	}
}
