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
import lineage2.gameserver.model.entity.residence.Dominion;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExReplyDominionInfo extends L2GameServerPacket
{
	/**
	 * Field _dominionList.
	 */
	private List<TerritoryInfo> _dominionList = Collections.emptyList();
	
	/**
	 * Constructor for ExReplyDominionInfo.
	 */
	public ExReplyDominionInfo()
	{
		List<Dominion> dominions = ResidenceHolder.getInstance().getResidenceList(Dominion.class);
		_dominionList = new ArrayList<>(dominions.size());
		for (Dominion dominion : dominions)
		{
			if (dominion.getSiegeDate().getTimeInMillis() == 0)
			{
				continue;
			}
			_dominionList.add(new TerritoryInfo(dominion.getId(), dominion.getName(), dominion.getOwner().getName(), dominion.getFlags(), (int) (dominion.getSiegeDate().getTimeInMillis() / 1000L)));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x92);
		writeD(_dominionList.size());
		for (TerritoryInfo cf : _dominionList)
		{
			writeD(cf.id);
			writeS(cf.terr);
			writeS(cf.clan);
			writeD(cf.flags.length);
			for (int f : cf.flags)
			{
				writeD(f);
			}
			writeD(cf.startTime);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class TerritoryInfo
	{
		/**
		 * Field id.
		 */
		public int id;
		/**
		 * Field terr.
		 */
		public String terr;
		/**
		 * Field clan.
		 */
		public String clan;
		/**
		 * Field flags.
		 */
		public int[] flags;
		/**
		 * Field startTime.
		 */
		public int startTime;
		
		/**
		 * Constructor for TerritoryInfo.
		 * @param id int
		 * @param terr String
		 * @param clan String
		 * @param flags int[]
		 * @param startTime int
		 */
		public TerritoryInfo(int id, String terr, String clan, int[] flags, int startTime)
		{
			this.id = id;
			this.terr = terr;
			this.clan = clan;
			this.flags = flags;
			this.startTime = startTime;
		}
	}
}
