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
import java.util.Iterator;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.utils.Location;

public class Attack extends L2GameServerPacket
{
	private final int _attackerObjId;
	private final boolean _soulshot;
	private final int _ssGrade;
	private final Location _attackerLoc;
	private final Location _targetLoc;
	private final List<Hit> _hits = new ArrayList<>();
	
	private static final int FLAG = 0x00; // No message.
	private static final int FLAG_MISS = 0x01; // Dodged attack.
	private static final int FLAG_CRIT = 0x04; // Critical hit.
	private static final int FLAG_SHIELD = 0x06; // Blocked attack.
	private static final int FLAG_SOULSHOT = 0x08; // SoulShot.
	
	private class Hit
	{
		int _targetId, _damage, _ssGrade, _flags;
		
		Hit(GameObject target, int damage, boolean miss, boolean crit, boolean shld, boolean soulshot, int ssGrade)
		{
			_targetId = target.getObjectId();
			_damage = damage;
			_ssGrade = ssGrade;
			_flags = FLAG;
			
			if (miss)
			{
				_flags = FLAG_MISS;
				_ssGrade = -1;
			}
			else if (shld)
			{
				_flags = FLAG_SHIELD;
			}
			else if (crit)
			{
				_flags = FLAG_CRIT;
			}
			
			if (soulshot)
			{
				_flags |= FLAG_SOULSHOT;
			}
		}
		
		public int getTargetId()
		{
			return _targetId;
		}
		
		public int getDamage()
		{
			return _damage;
		}
		
		public int getFlags()
		{
			return _flags;
		}
		
		public int getSSGrade()
		{
			return _ssGrade;
		}
	}
	
	/**
	 * @param attacker
	 * @param target
	 * @param useShots
	 * @param ssGrade
	 */
	public Attack(Creature attacker, Creature target, boolean useShots, int ssGrade)
	{
		_attackerObjId = attacker.getObjectId();
		_soulshot = useShots;
		_ssGrade = ssGrade;
		_attackerLoc = attacker.getLoc();
		_targetLoc = target.getLoc();
	}
	
	/**
	 * Adds hit to the attack (Attacks such as dual dagger/sword/fist has two hits)
	 * @param target
	 * @param damage
	 * @param miss
	 * @param crit
	 * @param shld
	 */
	public void addHit(GameObject target, int damage, boolean miss, boolean crit, boolean shld)
	{
		_hits.add(new Hit(target, damage, miss, crit, shld, _soulshot, _ssGrade));
	}
	
	/**
	 * @return {@code true} if current attack contains at least 1 hit.
	 */
	public boolean hasHits()
	{
		return !_hits.isEmpty();
	}
	
	/**
	 * @return {@code true} if attack has soul shot charged.
	 */
	public boolean hasSoulshot()
	{
		return _soulshot;
	}
	
	/**
	 * Writes current hit
	 * @param hit
	 */
	private void writeHit(Hit hit)
	{
		writeD(hit.getTargetId());
		writeD(hit.getDamage());
		writeD(hit.getFlags());
		writeD(hit.getSSGrade());
	}
	
	private void writeHits(Hit hit)
	{
		writeD(hit.getTargetId());
		writeD(0);
		writeD(hit.getDamage());
		writeD(hit.getFlags());
		writeD(hit.getSSGrade());
	}
	
	@Override
	protected final void writeImpl()
	{
		final Iterator<Hit> it = _hits.iterator();
		writeC(0x33);
		
		writeD(_attackerObjId);
		writeHits(it.next());
		writeLoc(_attackerLoc);
		
		writeH(_hits.size() - 1);
		while (it.hasNext())
		{
			writeHit(it.next());
		}
		
		writeLoc(_targetLoc);
	}
}