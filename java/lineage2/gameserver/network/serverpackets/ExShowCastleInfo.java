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
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.tables.ClanTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowCastleInfo extends L2GameServerPacket
{
	/**
	 * Field _infos.
	 */
	private List<CastleInfo> _infos = Collections.emptyList();
	
	/**
	 * Constructor for ExShowCastleInfo.
	 */
	public ExShowCastleInfo()
	{
		String ownerName;
		int id, tax, nextSiege;
		List<Castle> castles = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		_infos = new ArrayList<>(castles.size());
		for (Castle castle : castles)
		{
			ownerName = ClanTable.getInstance().getClanName(castle.getOwnerId());
			id = castle.getId();
			tax = castle.getTaxPercent();
			nextSiege = (int) (castle.getSiegeDate().getTimeInMillis() / 1000);
			_infos.add(new CastleInfo(ownerName, id, tax, nextSiege));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x14);
		writeD(_infos.size());
		for (CastleInfo info : _infos)
		{
			writeD(info._id);
			writeS(info._ownerName);
			writeD(info._tax);
			writeD(info._nextSiege);
		}
		_infos.clear();
	}
	
	/**
	 * @author Mobius
	 */
	private static class CastleInfo
	{
		/**
		 * Field _ownerName.
		 */
		public String _ownerName;
		/**
		 * Field _nextSiege.
		 */
		/**
		 * Field _tax.
		 */
		/**
		 * Field _id.
		 */
		public int _id, _tax, _nextSiege;
		
		/**
		 * Constructor for CastleInfo.
		 * @param ownerName String
		 * @param id int
		 * @param tax int
		 * @param nextSiege int
		 */
		public CastleInfo(String ownerName, int id, int tax, int nextSiege)
		{
			_ownerName = ownerName;
			_id = id;
			_tax = tax;
			_nextSiege = nextSiege;
		}
	}
}
