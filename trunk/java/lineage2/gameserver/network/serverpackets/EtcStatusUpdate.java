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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EtcStatusUpdate extends L2GameServerPacket
{
	/**
	 * Field DangerArea. Field MessageRefusal. Field WeightPenalty. Field IncreasedForce.
	 */
	private final int IncreasedForce, WeightPenalty, MessageRefusal, DangerArea;
	/**
	 * Field CharmOfCourage. Field weaponExpertisePenalty. Field armorExpertisePenalty.
	 */
	private final int armorExpertisePenalty, weaponExpertisePenalty, CharmOfCourage;
	/**
	 * Field ConsumedSouls.
	 */
	private final int ConsumedSouls;
	
	/**
	 * Constructor for EtcStatusUpdate.
	 * @param player Player
	 */
	public EtcStatusUpdate(Player player)
	{
		IncreasedForce = player.getIncreasedForce();
		WeightPenalty = player.getWeightPenalty();
		MessageRefusal = player.getMessageRefusal() || (player.getNoChannel() != 0) || player.isBlockAll() ? 1 : 0;
		DangerArea = player.isInDangerArea() ? 1 : 0;
		armorExpertisePenalty = player.getArmorsExpertisePenalty();
		weaponExpertisePenalty = player.getWeaponsExpertisePenalty();
		CharmOfCourage = player.isCharmOfCourage() ? 1 : 0;
		ConsumedSouls = player.getConsumedSouls();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xF9);
		writeD(IncreasedForce);
		writeD(WeightPenalty);
		writeD(MessageRefusal);
		writeD(DangerArea);
		writeD(weaponExpertisePenalty);
		writeD(armorExpertisePenalty);
		writeD(CharmOfCourage);
		writeD(0);
		writeD(ConsumedSouls);
	}
}
