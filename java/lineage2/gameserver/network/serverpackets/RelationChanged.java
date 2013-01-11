/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;

public class RelationChanged extends L2GameServerPacket
{
	public static final int RELATION_PARTY1 = 0x00001;
	public static final int RELATION_PARTY2 = 0x00002;
	public static final int RELATION_PARTY3 = 0x00004;
	public static final int RELATION_PARTY4 = 0x00008;
	public static final int RELATION_PARTYLEADER = 0x00010;
	public static final int RELATION_HAS_PARTY = 0x00020;
	public static final int RELATION_CLAN_MEMBER = 0x00040;
	public static final int RELATION_LEADER = 0x00080;
	public static final int RELATION_CLAN_MATE = 0x00100;
	public static final int RELATION_INSIEGE = 0x00200;
	public static final int RELATION_ATTACKER = 0x00400;
	public static final int RELATION_ALLY = 0x00800;
	public static final int RELATION_ENEMY = 0x01000;
	public static final int RELATION_MUTUAL_WAR = 0x04000;
	public static final int RELATION_1SIDED_WAR = 0x08000;
	public static final int RELATION_ALLY_MEMBER = 0x10000;
	public static final int RELATION_ISINTERRITORYWARS = 0x80000;
	protected final List<RelationChangedData> _data;
	
	protected RelationChanged(int s)
	{
		_data = new ArrayList<>(s);
	}
	
	protected void add(RelationChangedData data)
	{
		_data.add(data);
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xCE);
		writeD(_data.size());
		for (RelationChangedData d : _data)
		{
			writeD(d.charObjId);
			writeD(d.relation);
			writeD(d.isAutoAttackable ? 1 : 0);
			writeD(d.karma);
			writeD(d.pvpFlag);
		}
	}
	
	static class RelationChangedData
	{
		public final int charObjId;
		public final boolean isAutoAttackable;
		public final int relation, karma, pvpFlag;
		
		public RelationChangedData(Playable cha, boolean _isAutoAttackable, int _relation)
		{
			isAutoAttackable = _isAutoAttackable;
			relation = _relation;
			charObjId = cha.getObjectId();
			karma = cha.getKarma();
			pvpFlag = cha.getPvpFlag();
		}
	}
	
	public static L2GameServerPacket update(Player sendTo, Playable targetPlayable, Player activeChar)
	{
		if ((sendTo == null) || (targetPlayable == null) || (activeChar == null))
		{
			return null;
		}
		Player targetPlayer = targetPlayable.getPlayer();
		int relation = targetPlayer == null ? 0 : targetPlayer.getRelation(activeChar);
		RelationChanged pkt = new RelationChanged(1);
		pkt.add(new RelationChangedData(targetPlayable, targetPlayable.isAutoAttackable(activeChar), relation));
		return pkt;
	}
}
