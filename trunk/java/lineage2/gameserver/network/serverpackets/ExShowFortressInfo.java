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
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.pledge.Clan;

import org.apache.commons.lang3.StringUtils;

public class ExShowFortressInfo extends L2GameServerPacket
{
	private List<FortressInfo> _infos = Collections.emptyList();
	
	public ExShowFortressInfo()
	{
		List<Fortress> forts = ResidenceHolder.getInstance().getResidenceList(Fortress.class);
		_infos = new ArrayList<>(forts.size());
		for (Fortress fortress : forts)
		{
			Clan owner = fortress.getOwner();
			_infos.add(new FortressInfo(owner == null ? StringUtils.EMPTY : owner.getName(), fortress.getId(), fortress.getSiegeEvent().isInProgress(), owner == null ? 0 : (int) ((System.currentTimeMillis() - fortress.getOwnDate().getTimeInMillis()) / 1000L)));
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x15);
		writeD(_infos.size());
		for (FortressInfo _info : _infos)
		{
			writeD(_info._id);
			writeS(_info._owner);
			writeD(_info._status);
			writeD(_info._siege);
		}
	}
	
	static class FortressInfo
	{
		public int _id, _siege;
		public String _owner;
		public boolean _status;
		
		public FortressInfo(String owner, int id, boolean status, int siege)
		{
			_owner = owner;
			_id = id;
			_status = status;
			_siege = siege;
		}
	}
}