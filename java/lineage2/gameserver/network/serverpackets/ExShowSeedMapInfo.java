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

import lineage2.gameserver.instancemanager.SoDManager;
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowSeedMapInfo extends L2GameServerPacket
{
	/**
	 * Field ENTRANCES.
	 */
	private static final Location[] ENTRANCES =
	{
		new Location(-246857, 251960, 4331, 1),
		new Location(-213770, 210760, 4400, 2),
	};
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xA1);
		writeD(ENTRANCES.length);
		for (Location loc : ENTRANCES)
		{
			writeD(loc.x);
			writeD(loc.y);
			writeD(loc.z);
			switch (loc.h)
			{
				case 1:
					if (SoDManager.isAttackStage())
					{
						writeD(2771);
					}
					else
					{
						writeD(2772);
					}
					break;
				case 2:
					writeD(SoIManager.getCurrentStage() + 2765);
					break;
			}
		}
	}
}
