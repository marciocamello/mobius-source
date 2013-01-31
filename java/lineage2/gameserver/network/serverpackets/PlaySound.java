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

import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlaySound extends L2GameServerPacket
{
	/**
	 * Field SIEGE_VICTORY.
	 */
	public static final L2GameServerPacket SIEGE_VICTORY = new PlaySound("Siege_Victory");
	/**
	 * Field B04_S01.
	 */
	public static final L2GameServerPacket B04_S01 = new PlaySound("B04_S01");
	/**
	 * Field HB01.
	 */
	public static final L2GameServerPacket HB01 = new PlaySound(PlaySound.Type.MUSIC, "HB01", 0, 0, 0, 0, 0);
	
	/**
	 * @author Mobius
	 */
	public enum Type
	{
		/**
		 * Field SOUND.
		 */
		SOUND,
		/**
		 * Field MUSIC.
		 */
		MUSIC,
		/**
		 * Field VOICE.
		 */
		VOICE
	}
	
	/**
	 * Field _type.
	 */
	private final Type _type;
	/**
	 * Field _soundFile.
	 */
	private final String _soundFile;
	/**
	 * Field _hasCenterObject.
	 */
	private final int _hasCenterObject;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _z. Field _y. Field _x.
	 */
	private final int _x, _y, _z;
	
	/**
	 * Constructor for PlaySound.
	 * @param soundFile String
	 */
	public PlaySound(String soundFile)
	{
		this(Type.SOUND, soundFile, 0, 0, 0, 0, 0);
	}
	
	/**
	 * Constructor for PlaySound.
	 * @param type Type
	 * @param soundFile String
	 * @param c int
	 * @param objectId int
	 * @param loc Location
	 */
	public PlaySound(Type type, String soundFile, int c, int objectId, Location loc)
	{
		this(type, soundFile, c, objectId, loc == null ? 0 : loc.x, loc == null ? 0 : loc.y, loc == null ? 0 : loc.z);
	}
	
	/**
	 * Constructor for PlaySound.
	 * @param type Type
	 * @param soundFile String
	 * @param c int
	 * @param objectId int
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public PlaySound(Type type, String soundFile, int c, int objectId, int x, int y, int z)
	{
		_type = type;
		_soundFile = soundFile;
		_hasCenterObject = c;
		_objectId = objectId;
		_x = x;
		_y = y;
		_z = z;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x9e);
		writeD(_type.ordinal());
		writeS(_soundFile);
		writeD(_hasCenterObject);
		writeD(_objectId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}
