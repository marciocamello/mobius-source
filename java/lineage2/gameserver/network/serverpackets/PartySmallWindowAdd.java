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
public class PartySmallWindowAdd extends L2GameServerPacket
{
	/**
	 * Field objectId.
	 */
	private final int objectId;
	/**
	 * Field member.
	 */
	private final PartySmallWindowAll.PartySmallWindowMemberInfo member;
	
	/**
	 * Constructor for PartySmallWindowAdd.
	 * @param player Player
	 * @param member Player
	 */
	public PartySmallWindowAdd(Player player, Player member)
	{
		objectId = player.getObjectId();
		this.member = new PartySmallWindowAll.PartySmallWindowMemberInfo(member);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x4F);
		writeD(objectId);
		writeD(0);
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
		writeD(0);
		writeD(member.race_id);
		writeD(0x00);
		writeD(0);
		writeD(0);
	}
}
