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

public class EtcStatusUpdate extends L2GameServerPacket
{
	private final int IncreasedForce;
	private final int WeightPenalty;
	private final int DangerArea;
	private final int armorExpertisePenalty;
	private final int weaponExpertisePenalty;
	private final int DeathPenaltyLevel;
	private final int ConsumedSouls;
	
	public EtcStatusUpdate(Player player)
	{
		IncreasedForce = player.getIncreasedForce();
		WeightPenalty = player.getWeightPenalty();
		armorExpertisePenalty = player.getArmorsExpertisePenalty();
		weaponExpertisePenalty = player.getWeaponsExpertisePenalty();
		DeathPenaltyLevel = player.getDeathPenalty() == null ? 0 : player.getDeathPenalty().getLevel(player);
		ConsumedSouls = player.getConsumedSouls();
		
		int messageRefusal = player.getMessageRefusal() || (player.getNoChannel() != 0) || player.isBlockAll() ? 1 : 0;
		int charmOfCourage = player.isCharmOfCourage() ? 2 : 0;
		int dangerZone = player.isInDangerArea() ? 4 : 0;
		DangerArea = messageRefusal + charmOfCourage + dangerZone;
	}
	
	@Override
	protected final void writeImpl()
	{
		// dddddddd
		writeC(0xf9); // Packet type
		// TODO: Momentum
		writeC(IncreasedForce); // skill id 4271, 7 lvl
		writeD(WeightPenalty); // skill id 4270, 4 lvl // 1-4 weight penalty, lvl (1=50%, 2=66.6%, 3=80%, 4=100%)
		writeC(weaponExpertisePenalty); // weapon grade penalty, skill 6209 in epilogue // Weapon Grade Penalty [1-4]
		writeC(armorExpertisePenalty); // armor grade penalty, skill 6213 in epilogue // Armor Grade Penalty [1-4]
		// "Prevents experience value decreasing if killed during a siege war".
		writeC(DeathPenaltyLevel); // Death Penalty max lvl 15,// 1-15 death penalty, lvl (combat ability decreased due to death)
		// "Combat ability is decreased due to death."
		writeC(ConsumedSouls);
		writeC(DangerArea); // 603 4, 0
	}
}