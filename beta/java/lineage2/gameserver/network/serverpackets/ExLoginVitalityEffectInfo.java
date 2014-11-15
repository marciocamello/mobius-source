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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.CharSelectionInfo;

/**
 * @author : Ragnarok
 * @date : 22.01.12 11:44
 *       <p/>
 *       dddd dd
 */
public class ExLoginVitalityEffectInfo extends L2GameServerPacket
{
	private final CharSelectionInfo charInfo;
	
	public ExLoginVitalityEffectInfo(CharSelectionInfo charInfo)
	{
		this.charInfo = charInfo;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x119);
		writeD(charInfo.getVitalityPoints() == 0 ? 0 : (int) (Config.ALT_VITALITY_RATE * 100));
		// bonus
		writeD(5); // TODO: Remaining items count
		writeD(0x00); // TODO: Max vitality items
		writeD(0x00); // TODO: Max vitality items allowed
	}
}
