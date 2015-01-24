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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;

public final class PartySmallWindowAdd extends L2GameServerPacket
{
	private final Player _member;
	private final Party _party;
	
	public PartySmallWindowAdd(Player member, Party party)
	{
		_member = member;
		_party = party;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4F);
		writeD(_party.getPartyLeader().getObjectId()); // c3
		writeD(_party.getLootDistribution());// writeD(0x04); ?? //c3
		writeD(_member.getObjectId());
		writeS(_member.getName());
		
		writeD((int) _member.getCurrentCp()); // c4
		writeD(_member.getMaxCp()); // c4
		writeD((int) _member.getCurrentHp());
		writeD(_member.getMaxHp());
		writeD((int) _member.getCurrentMp());
		writeD(_member.getMaxMp());
		writeD(_member.getVitality());
		writeC(_member.getLevel());
		writeH(_member.getClassId().getId());
		writeC(0x00);
		writeH(_member.getRace().ordinal());
	}
}
