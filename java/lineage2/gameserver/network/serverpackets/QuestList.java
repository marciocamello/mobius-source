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
import lineage2.gameserver.model.quest.QuestState;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class QuestList extends L2GameServerPacket
{
	/**
	 * Field questlist.
	 */
	private final List<int[]> questlist;
	/**
	 * Field unk.
	 */
	private static byte[] unk = new byte[128];
	
	/**
	 * Constructor for QuestList.
	 * @param player Player
	 */
	public QuestList(Player player)
	{
		QuestState[] allQuestStates = player.getAllQuestsStates();
		questlist = new ArrayList<>(allQuestStates.length);
		for (QuestState quest : allQuestStates)
		{
			if (quest.getQuest().isVisible(player) && quest.isStarted())
			{
				questlist.add(new int[]
				{
					quest.getQuest().getQuestIntId(),
					quest.getInt(QuestState.VAR_COND)
				});
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x86);
		writeH(questlist.size());
		for (int[] q : questlist)
		{
			writeD(q[0]);
			writeD(q[1]);
		}
		writeB(unk);
	}
}
