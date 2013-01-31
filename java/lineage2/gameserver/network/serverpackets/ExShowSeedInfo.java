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

import java.util.List;

import lineage2.gameserver.model.Manor;
import lineage2.gameserver.templates.manor.SeedProduction;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowSeedInfo extends L2GameServerPacket
{
	/**
	 * Field _seeds.
	 */
	private final List<SeedProduction> _seeds;
	/**
	 * Field _manorId.
	 */
	private final int _manorId;
	
	/**
	 * Constructor for ExShowSeedInfo.
	 * @param manorId int
	 * @param seeds List<SeedProduction>
	 */
	public ExShowSeedInfo(int manorId, List<SeedProduction> seeds)
	{
		_manorId = manorId;
		_seeds = seeds;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x23);
		writeC(0);
		writeD(_manorId);
		writeD(0);
		writeD(_seeds.size());
		for (SeedProduction seed : _seeds)
		{
			writeD(seed.getId());
			writeQ(seed.getCanProduce());
			writeQ(seed.getStartProduce());
			writeQ(seed.getPrice());
			writeD(Manor.getInstance().getSeedLevel(seed.getId()));
			writeC(1);
			writeD(Manor.getInstance().getRewardItemBySeed(seed.getId(), 1));
			writeC(1);
			writeD(Manor.getInstance().getRewardItemBySeed(seed.getId(), 2));
		}
	}
}
