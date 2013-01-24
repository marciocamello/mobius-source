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
	@SuppressWarnings("unused")
	private final int _page;
	
	public PledgeReceiveWarList(Clan clan, int type, int page)
	{
		_updateType = type;
		_page = page;
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
		writeD(_updateType);
		writeD(0x00);
		writeD(infos.size());
		for (WarInfo _info : infos)
		{
			writeS(_info.clan_name);
			writeD(_info.unk1);
			writeD(_info.unk2);
		}
	}
	
	static class WarInfo
	{
		public String clan_name;
		public int unk1, unk2;
		
		public WarInfo(String _clan_name, int _unk1, int _unk2)
		{
			clan_name = _clan_name;
			unk1 = _unk1;
			unk2 = _unk2;
		}
	}
}
