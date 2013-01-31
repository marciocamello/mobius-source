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
import lineage2.gameserver.model.GameObject;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Attack extends L2GameServerPacket
{
	/**
	 * Field FLAG.
	 */
	public static final int FLAG = 0x00;
	/**
	 * Field FLAG_MISS.
	 */
	public static final int FLAG_MISS = 0x01;
	/**
	 * Field FLAG_BLOCK.
	 */
	public static final int FLAG_BLOCK = 0x02;
	/**
	 * Field FLAG_CRIT.
	 */
	public static final int FLAG_CRIT = 0x04;
	/**
	 * Field FLAG_SOULSHOT.
	 */
	public static final int FLAG_SOULSHOT = 0x08;
	/**
	 * Field SOULSHOT_NG.
	 */
	public static final int SOULSHOT_NG = 0x00;
	/**
	 * Field SOULSHOT_D.
	 */
	public static final int SOULSHOT_D = 0x01;
	/**
	 * Field SOULSHOT_C.
	 */
	public static final int SOULSHOT_C = 0x02;
	/**
	 * Field SOULSHOT_B.
	 */
	public static final int SOULSHOT_B = 0x03;
	/**
	 * Field SOULSHOT_A.
	 */
	public static final int SOULSHOT_A = 0x04;
	/**
	 * Field SOULSHOT_S.
	 */
	public static final int SOULSHOT_S = 0x05;
	/**
	 * Field SOULSHOT_R.
	 */
	public static final int SOULSHOT_R = 0x06;
	
	/**
	 * @author Mobius
	 */
	private class Hit
	{
		/**
		 * Field _flags.
		 */
		/**
		 * Field _damage.
		 */
		/**
		 * Field _targetId.
		 */
		int _targetId, _damage, _flags;
		
		/**
		 * Constructor for Hit.
		 * @param target GameObject
		 * @param damage int
		 * @param miss boolean
		 * @param crit boolean
		 * @param shld boolean
		 */
		Hit(GameObject target, int damage, boolean miss, boolean crit, boolean shld)
		{
			_targetId = target.getObjectId();
			_damage = damage;
			_flags = FLAG;
			if (miss)
			{
				_flags |= FLAG_MISS;
			}
			if (shld)
			{
				_flags |= FLAG_BLOCK;
			}
			if (crit)
			{
				_flags |= FLAG_CRIT;
			}
			if (_soulshot)
			{
				_flags |= FLAG_SOULSHOT;
			}
		}
	}
	
	/**
	 * Field _attackerId.
	 */
	public final int _attackerId;
	/**
	 * Field _soulshot.
	 */
	public final boolean _soulshot;
	/**
	 * Field _grade.
	 */
	private final int _grade;
	/**
	 * Field _tz. Field _ty. Field _tx. Field _z. Field _y. Field _x.
	 */
	private final int _x, _y, _z, _tx, _ty, _tz;
	/**
	 * Field hits.
	 */
	private Hit[] hits;
	
	/**
	 * Constructor for Attack.
	 * @param attacker Creature
	 * @param target Creature
	 * @param ss boolean
	 * @param grade int
	 */
	public Attack(Creature attacker, Creature target, boolean ss, int grade)
	{
		_attackerId = attacker.getObjectId();
		_soulshot = ss;
		_grade = _soulshot ? grade : -1;
		_x = attacker.getX();
		_y = attacker.getY();
		_z = attacker.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
		hits = new Hit[0];
	}
	
	/**
	 * Method addHit.
	 * @param target GameObject
	 * @param damage int
	 * @param miss boolean
	 * @param crit boolean
	 * @param shld boolean
	 */
	public void addHit(GameObject target, int damage, boolean miss, boolean crit, boolean shld)
	{
		int pos = hits.length;
		Hit[] tmp = new Hit[pos + 1];
		System.arraycopy(hits, 0, tmp, 0, hits.length);
		tmp[pos] = new Hit(target, damage, miss, crit, shld);
		hits = tmp;
	}
	
	/**
	 * Method hasHits.
	 * @return boolean
	 */
	public boolean hasHits()
	{
		return hits.length > 0;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x33);
		writeD(_attackerId);
		writeD(hits[0]._targetId);
		writeC(0x00);
		writeD(hits[0]._damage);
		writeD(hits[0]._flags);
		writeD(_grade);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeH(hits.length - 1);
		for (int i = 1; i < hits.length; i++)
		{
			writeD(hits[i]._targetId);
			writeD(hits[i]._damage);
			writeD(hits[i]._flags);
			writeD(_grade);
		}
		writeD(_tx);
		writeD(_ty);
		writeD(_tz);
	}
}
