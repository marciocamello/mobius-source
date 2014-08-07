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

import lineage2.gameserver.model.Player;

/**
 * const EDGE_GRAY = 15; const EDGE_BLUE = 12; const EDGE_ORANGE = 11; const EDGE_BUFFRED = 9; const EDGE_RED = 8; const EDGE_SSQGRAY = 13; const EDGE_PVPGREEN = 14;
 */
public class ExSetCompassZoneCode extends L2GameServerPacket
{
	public static final int ZONE_ALTERED = 8;
	public static final int ZONE_ALTERED2 = 9;
	public static final int ZONE_REMINDER = 10;
	public static final int ZONE_SIEGE = 11;
	public static final int ZONE_PEACE = 12;
	public static final int ZONE_SSQ = 13;
	public static final int ZONE_PVP = 14;
	public static final int ZONE_GENERAL_FIELD = 15;
	public static final int ZONE_PVP_FLAG = 1 << ExSetCompassZoneCode.ZONE_PVP;
	public static final int ZONE_ALTERED_FLAG = 1 << ExSetCompassZoneCode.ZONE_ALTERED;
	public static final int ZONE_SIEGE_FLAG = 1 << ExSetCompassZoneCode.ZONE_SIEGE;
	public static final int ZONE_PEACE_FLAG = 1 << ExSetCompassZoneCode.ZONE_PEACE;
	public static final int ZONE_SSQ_FLAG = 1 << ExSetCompassZoneCode.ZONE_SSQ;
	private final int _zone;
	
	public ExSetCompassZoneCode(Player player)
	{
		this(player.getZoneMask());
	}
	
	private ExSetCompassZoneCode(int zoneMask)
	{
		if ((zoneMask & ZONE_ALTERED_FLAG) == ZONE_ALTERED_FLAG)
		{
			_zone = ZONE_ALTERED;
		}
		else if ((zoneMask & ZONE_SIEGE_FLAG) == ZONE_SIEGE_FLAG)
		{
			_zone = ZONE_SIEGE;
		}
		else if ((zoneMask & ZONE_PVP_FLAG) == ZONE_PVP_FLAG)
		{
			_zone = ZONE_PVP;
		}
		else if ((zoneMask & ZONE_PEACE_FLAG) == ZONE_PEACE_FLAG)
		{
			_zone = ZONE_PEACE;
		}
		else if ((zoneMask & ZONE_SSQ_FLAG) == ZONE_SSQ_FLAG)
		{
			_zone = ZONE_SSQ;
		}
		else
		{
			_zone = ZONE_GENERAL_FIELD;
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x33);
		writeD(_zone);
	}
}