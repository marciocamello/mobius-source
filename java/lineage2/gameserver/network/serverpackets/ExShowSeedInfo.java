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

import lineage2.gameserver.data.xml.holder.ManorDataHolder;
import lineage2.gameserver.templates.manor.SeedProduction;

/**
 * format cddd[dddddc[d]c[d]] cddd[dQQQdc[d]c[d]] - Gracia Final
 */
public class ExShowSeedInfo extends L2GameServerPacket
{
	private final List<SeedProduction> _seeds;
	private final int _manorId;
	
	public ExShowSeedInfo(int manorId, List<SeedProduction> seeds)
	{
		_manorId = manorId;
		_seeds = seeds;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x23); // SubId
		writeC(0);
		writeD(_manorId); // Manor ID
		writeD(0);
		writeD(_seeds.size());
		
		for (SeedProduction seed : _seeds)
		{
			writeD(seed.getId()); // Seed id
			writeQ(seed.getCanProduce()); // Left to buy
			writeQ(seed.getStartProduce()); // Started amount
			writeQ(seed.getPrice()); // Sell Price
			writeD(ManorDataHolder.getInstance().getSeedLevel(seed.getId())); // Seed Level
			writeC(1); // reward 1 Type
			writeD(ManorDataHolder.getInstance().getRewardItemBySeed(seed.getId(), 1)); // Reward
			writeC(1); // reward 2 Type
			writeD(ManorDataHolder.getInstance().getRewardItemBySeed(seed.getId(), 2)); // Reward
		}
	}
}