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
 * Probably the destination coordinates where you should fly your clan's airship.<BR>
 * Exactly at the specified coordinates an airship controller is spawned.<BR>
 * Sent while being in Gracia, when world map is opened, in response to RequestSeedPhase.<BR>
 * FE A1 00 - opcodes<BR>
 * 02 00 00 00 - list size<BR>
 * <BR>
 * B7 3B FC FF - x<BR>
 * 38 D8 03 00 - y<BR>
 * EB 10 00 00 - z<BR>
 * D3 0A 00 00 - sysmsg id<BR>
 * <BR>
 * F6 BC FC FF - x<BR>
 * 48 37 03 00 - y<BR>
 * 30 11 00 00 - z<BR>
 * CE 0A 00 00 - sysmsg id
 * @done by n0nam3
 */
public class ExShowSeedMapInfo extends L2GameServerPacket
{
	private static final Location[] ENTRANCES =
	{
		new Location(-246857, 251960, 4331, 1),
		new Location(-213770, 210760, 4400, 2),
	};
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xA2);
		writeD(ENTRANCES.length);
		for (Location loc : ENTRANCES)
		{
			writeD(loc.x);
			writeD(loc.y);
			writeD(loc.z);
			switch (loc.h)
			{
				case 1: // Seed of Destruction
					if (SoDManager.isAttackStage())
					{
						writeD(2771);
					}
					else
					{
						writeD(2772);
					}
					break;
				case 2: // Seed of Immortality
					writeD(SoIManager.getCurrentStage() + 2765);
					break;
			}
		}
	}
}
