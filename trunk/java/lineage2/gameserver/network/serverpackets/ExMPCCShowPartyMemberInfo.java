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
import java.util.List;

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMPCCShowPartyMemberInfo extends L2GameServerPacket
{
	/**
	 * Field members.
	 */
	private final List<PartyMemberInfo> members;
	
	/**
	 * Constructor for ExMPCCShowPartyMemberInfo.
	 * @param party Party
	 */
	public ExMPCCShowPartyMemberInfo(Party party)
	{
		members = new ArrayList<>();
		for (Player _member : party.getPartyMembers())
		{
			members.add(new PartyMemberInfo(_member.getName(), _member.getObjectId(), _member.getClassId().getId()));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x4b);
		writeD(members.size());
		for (PartyMemberInfo member : members)
		{
			writeS(member.name);
			writeD(member.object_id);
			writeD(member.class_id);
		}
		members.clear();
	}
	
	/**
	 * @author Mobius
	 */
	static class PartyMemberInfo
	{
		/**
		 * Field name.
		 */
		public String name;
		/**
		 * Field class_id.
		 */
		/**
		 * Field object_id.
		 */
		public int object_id, class_id;
		
		/**
		 * Constructor for PartyMemberInfo.
		 * @param _name String
		 * @param _object_id int
		 * @param _class_id int
		 */
		public PartyMemberInfo(String _name, int _object_id, int _class_id)
		{
			name = _name;
			object_id = _object_id;
			class_id = _class_id;
		}
	}
}
