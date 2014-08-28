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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.pledge.Clan;

public class PledgeReceiveWarList extends L2GameServerPacket
{
	private final List<WarInfo> infos = new ArrayList<>();
	private final int _updateType;
	
	public PledgeReceiveWarList(Clan clan, int type, int page)
	{
		_updateType = type;
		List<Clan> clans = _updateType == 1 ? clan.getAttackerClans() : clan.getEnemyClans();
		
		for (Clan _clan : clans)
		{
			if (_clan == null)
			{
				continue;
			}
			
			infos.add(new WarInfo(_clan.getName(), _updateType, 0));
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x3f);
		writeD(_updateType); // which type of war list sould be revamped by this packet
		writeD(0x00); // page number goes here(_page ), made it static cuz not sure how many war to add to one page so TODO here
		writeD(infos.size());
		
		for (WarInfo _info : infos)
		{
			writeS(_info.clan_name);
			writeD(_info.unk1);
			writeD(_info.unk2); // filler ??
		}
	}
	
	private static class WarInfo
	{
		final String clan_name;
		final int unk1;
		final int unk2;
		
		WarInfo(String _clan_name, int _unk1, int _unk2)
		{
			clan_name = _clan_name;
			unk1 = _unk1;
			unk2 = _unk2;
		}
	}
}