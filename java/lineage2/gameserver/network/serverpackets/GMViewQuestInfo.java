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
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GMViewQuestInfo extends L2GameServerPacket
{
	/**
	 * Field _cha.
	 */
	private final Player _cha;
	
	/**
	 * Constructor for GMViewQuestInfo.
	 * @param cha Player
	 */
	public GMViewQuestInfo(Player cha)
	{
		_cha = cha;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x99);
		writeS(_cha.getName());
		Quest[] quests = _cha.getAllActiveQuests();
		if (quests.length == 0)
		{
			writeH(0);
			writeH(0);
			return;
		}
		writeH(quests.length);
		for (Quest q : quests)
		{
			writeD(q.getQuestIntId());
			QuestState qs = _cha.getQuestState(q.getName());
			writeD(qs == null ? 0 : qs.getInt("cond"));
		}
		writeH(0);
	}
}
