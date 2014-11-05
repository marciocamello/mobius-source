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

import lineage2.gameserver.model.Creature;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMagicAttackInfo extends L2GameServerPacket
{
	public static final int NORMAL = 0;
	public static final int CRIT = 1;
	public static final int CRIT_ADD = 2;
	public static final int OVERHIT = 3;
	public static final int MISS = 4;
	public static final int BLOCK = 5;
	public static final int RESIST = 6;
	public static final int IMMUNE = 7;
	private final Creature _attacker;
	private final Creature _target;
	private final int _info;
	
	/**
	 * Constructor for ExMagicAttackInfo.
	 * @param attacker Creature
	 * @param target Creature
	 * @param info int
	 */
	public ExMagicAttackInfo(Creature attacker, Creature target, int info)
	{
		_attacker = attacker;
		_target = target;
		_info = info;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xFB);
		writeD(_attacker.getObjectId());
		writeD(_target.getObjectId());
		writeD(_info);
	}
}
