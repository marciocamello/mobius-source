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

import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExEventMatchTeamInfo extends L2GameServerPacket
{
	/**
	 * Field loot. Field leader_id.
	 */
	@SuppressWarnings("unused")
	private final int leader_id, loot;
	/**
	 * Field members.
	 */
	private final List<EventMatchTeamInfo> members = new ArrayList<>();
	
	/**
	 * Constructor for ExEventMatchTeamInfo.
	 * @param party List<Player>
	 * @param exclude Player
	 */
	public ExEventMatchTeamInfo(List<Player> party, Player exclude)
	{
		leader_id = party.get(0).getObjectId();
		loot = party.get(0).getParty().getLootDistribution();
		for (Player member : party)
		{
			if (!member.equals(exclude))
			{
				members.add(new EventMatchTeamInfo(member));
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x1C);
	}
	
	/**
	 * @author Mobius
	 */
	public static class EventMatchTeamInfo
	{
		/**
		 * Field pet_Name.
		 */
		/**
		 * Field _name.
		 */
		public String _name, pet_Name;
		/**
		 * Field race_id.
		 */
		/**
		 * Field class_id.
		 */
		/**
		 * Field level.
		 */
		/**
		 * Field maxMp.
		 */
		/**
		 * Field curMp.
		 */
		/**
		 * Field maxHp.
		 */
		/**
		 * Field curHp.
		 */
		/**
		 * Field maxCp.
		 */
		/**
		 * Field curCp.
		 */
		/**
		 * Field _id.
		 */
		public int _id, curCp, maxCp, curHp, maxHp, curMp, maxMp, level, class_id, race_id;
		/**
		 * Field pet_level.
		 */
		/**
		 * Field pet_maxMp.
		 */
		/**
		 * Field pet_curMp.
		 */
		/**
		 * Field pet_maxHp.
		 */
		/**
		 * Field pet_curHp.
		 */
		/**
		 * Field pet_NpcId.
		 */
		/**
		 * Field pet_id.
		 */
		public int pet_id, pet_NpcId, pet_curHp, pet_maxHp, pet_curMp, pet_maxMp, pet_level;
		
		/**
		 * Constructor for EventMatchTeamInfo.
		 * @param member Player
		 */
		public EventMatchTeamInfo(Player member)
		{
			_name = member.getName();
			_id = member.getObjectId();
			curCp = (int) member.getCurrentCp();
			maxCp = member.getMaxCp();
			curHp = (int) member.getCurrentHp();
			maxHp = member.getMaxHp();
			curMp = (int) member.getCurrentMp();
			maxMp = member.getMaxMp();
			level = member.getLevel();
			class_id = member.getClassId().getId();
			race_id = member.getRace().ordinal();
		}
	}
}
