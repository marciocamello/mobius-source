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
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.actor.instances.player.SummonList;

public final class PartySmallWindowAll extends L2GameServerPacket
{
	private final Party _party;
	private final Player _exclude;
	
	public PartySmallWindowAll(Player exclude, Party party)
	{
		_exclude = exclude;
		_party = party;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4E);
		writeD(_party.getPartyLeader().getObjectId());
		writeC(_party.getLootDistribution());
		writeC(_party.getMemberCount() - 1);
		
		for (Player member : _party.getPartyMembers())
		{
			if ((member != null) && (member != _exclude))
			{
				writeD(member.getObjectId());
				writeS(member.getName());
				
				writeD((int) member.getCurrentCp()); // c4
				writeD(member.getMaxCp()); // c4
				
				writeD((int) member.getCurrentHp());
				writeD(member.getMaxHp());
				writeD((int) member.getCurrentMp());
				writeD(member.getMaxMp());
				writeD(member.getVitality());
				writeC(member.getLevel());
				writeH(member.getClassId().getId());
				writeC(0x01); // Unk
				writeH(member.getRace().ordinal());
				final SummonList pets = member.getSummonList();
				writeD(member.getSummonList().size());
				if (pets.size() > 0)
				{
					for (Summon pet : member.getSummonList())
					{
						writeD(pet.getObjectId());
						writeD(pet.getId() + 1000000);
						writeC(pet.getSummonType());
						writeS(pet.getName());
						writeD((int) pet.getCurrentHp());
						writeD(pet.getMaxHp());
						writeD((int) pet.getCurrentMp());
						writeD(pet.getMaxMp());
						writeC(pet.getLevel());
					}
				}
			}
		}
	}
}
