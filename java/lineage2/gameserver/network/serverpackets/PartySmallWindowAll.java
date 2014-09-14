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
import java.util.Collection;
import java.util.List;

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.party.PartySubstitute;

/**
 * @author ALF
 * @data 09.02.2012
 */
public class PartySmallWindowAll extends L2GameServerPacket
{
	private final int leaderId;
	private final int loot;
	private final List<PartySmallWindowMemberInfo> members = new ArrayList<>();
	
	public PartySmallWindowAll(Party party, Player exclude)
	{
		leaderId = party.getPartyLeader().getObjectId();
		loot = party.getLootDistribution();
		
		for (Player member : party.getPartyMembers())
		{
			if (member != exclude)
			{
				members.add(new PartySmallWindowMemberInfo(member));
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4E);
		writeD(leaderId); // c3 party leader id
		writeD(loot); // c3 party loot type (0,1,2,....)
		writeD(members.size());
		
		for (PartySmallWindowMemberInfo member : members)
		{
			writeD(member._id);
			writeS(member._name);
			writeD(member.curCp);
			writeD(member.maxCp);
			writeD(member.curHp);
			writeD(member.maxHp);
			writeD(member.curMp);
			writeD(member.maxMp);
			writeD(member.vitality);
			writeD(member.level);
			writeD(member.class_id);
			writeD(0x00);// writeD(0x01);
			writeD(member.race_id);
			writeD(0x00); // Hide Name
			writeD(0x00);
			writeD(member.replace);
			writeD(member._pets.size());
			
			for (Summon pet : member._pets)
			{
				writeD(pet.getObjectId());
				writeD(pet.getId() + 1000000);
				writeD(pet.getSummonType());
				writeS(pet.getName());
				writeD((int) pet.getCurrentHp());
				writeD(pet.getMaxHp());
				writeD((int) pet.getCurrentMp());
				writeD(pet.getMaxMp());
				writeD(pet.getLevel());
			}
		}
	}
	
	static class PartySmallWindowMemberInfo
	{
		String _name;
		String pet_Name;
		int _id;
		int curCp;
		int maxCp;
		int curHp;
		int maxHp;
		int curMp;
		int maxMp;
		int level;
		int class_id;
		int race_id;
		int vitality;
		Collection<Summon> _pets;
		int replace;
		
		PartySmallWindowMemberInfo(Player member)
		{
			_name = member.getName();
			_id = member.getObjectId();
			curCp = (int) member.getCurrentCp();
			maxCp = member.getMaxCp();
			vitality = member.getVitality();
			curHp = (int) member.getCurrentHp();
			maxHp = member.getMaxHp();
			curMp = (int) member.getCurrentMp();
			maxMp = member.getMaxMp();
			level = member.getLevel();
			class_id = member.getClassId().getId();
			race_id = member.getRace().ordinal();
			_pets = member.getPets();
			replace = PartySubstitute.getInstance().isPlayerToReplace(member) ? 1 : 0;
		}
	}
}