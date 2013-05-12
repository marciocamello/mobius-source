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
import lineage2.gameserver.model.party.PartySubstitute;

public class PartySmallWindowAdd extends L2GameServerPacket
{
	private final int leaderId;
	private final int distribution;
	private final PartySmallWindowAll.PartySmallWindowMemberInfo member;
	private final int replace;
	
	public PartySmallWindowAdd(Player player, Player member, int _distribution)
	{
		leaderId = player.getObjectId();
		distribution = _distribution;
		this.member = new PartySmallWindowAll.PartySmallWindowMemberInfo(member);
		replace = PartySubstitute.getInstance().isPlayerToReplace(member) ? 1 : 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4F);
		writeD(leaderId);
		writeD(distribution);
		writeD(member._id);
		writeS(member._name);
		writeD(member.curCp);
		writeD(member.maxCp);
		writeD(member.vitality);
		writeD(member.curHp);
		writeD(member.maxHp);
		writeD(member.curMp);
		writeD(member.maxMp);
		writeD(member.level);
		writeD(member.class_id);
		writeD(0x00);// writeD(0x01); ??
		writeD(member.race_id);
		writeD(0x00);// Hide name
		writeD(0x00);// unknown
		writeD(replace);// Идет ли поиск замены игроку
	}
}