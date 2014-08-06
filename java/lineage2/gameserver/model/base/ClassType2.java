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
package lineage2.gameserver.model.base;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum ClassType2
{
	None(10280, 10612),
	Warrior(10281, 10289),
	Knight(10282, 10288),
	Rogue(10283, 10290),
	Archer(10283, 10290),
	Healer(10285, 10291),
	Enchanter(10287, 10293),
	Summoner(10286, 10294),
	Wizard(10284, 10292);
	public static final ClassType2[] VALUES = values();
	private final int _certificate;
	private final int _transformation;
	
	/**
	 * Constructor for ClassType2.
	 * @param cer int
	 * @param tra int
	 */
	private ClassType2(int cer, int tra)
	{
		_certificate = cer;
		_transformation = tra;
	}
	
	/**
	 * Method getCertificateId.
	 * @return int
	 */
	public int getCertificateId()
	{
		return _certificate;
	}
	
	/**
	 * Method getTransformationId.
	 * @return int
	 */
	public int getTransformationId()
	{
		return _transformation;
	}
}
