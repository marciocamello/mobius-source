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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TutorialShowHtml extends L2GameServerPacket
{
	/**
	 * Field TYPE_HTML. (value is 1)
	 */
	public static final int TYPE_HTML = 1;
	/**
	 * Field TYPE_WINDOW. (value is 2)
	 */
	public static final int TYPE_WINDOW = 2;
	/**
	 * Field QT_001. (value is ""..\\L2Text\\QT_001_Radar_01.htm"")
	 */
	public static final String QT_001 = "..\\L2Text\\QT_001_Radar_01.htm";
	/**
	 * Field QT_002. (value is ""..\\L2Text\\QT_002_Guide_01.htm"")
	 */
	public static final String QT_002 = "..\\L2Text\\QT_002_Guide_01.htm";
	/**
	 * Field QT_003. (value is ""..\\L2Text\\QT_003_bullet_01.htm"")
	 */
	public static final String QT_003 = "..\\L2Text\\QT_003_bullet_01.htm";
	/**
	 * Field QT_004. (value is ""..\\L2Text\\QT_004_skill_01.htm"")
	 */
	public static final String QT_004 = "..\\L2Text\\QT_004_skill_01.htm";
	/**
	 * Field QT_009. (value is ""..\\L2Text\\QT_009_enchant_01.htm"")
	 */
	public static final String QT_009 = "..\\L2Text\\QT_009_enchant_01.htm";
	/**
	 * Field GUIDE. (value is ""..\\L2Text\\Guide_Ad.htm"")
	 */
	public static final String GUIDE = "..\\L2Text\\Guide_Ad.htm";
	/**
	 * Field GUIDE_Aw. (value is ""..\\L2Text\\Guide_Aw.htm"")
	 */
	public static final String GUIDE_Aw = "..\\L2Text\\Guide_Aw.htm";
	/**
	 * Field _html.
	 */
	private final String _html;
	/**
	 * Field _type.
	 */
	private final int _type;
	
	/**
	 * Constructor for TutorialShowHtml.
	 * @param html String
	 * @param type int
	 */
	public TutorialShowHtml(String html, int type)
	{
		_html = html;
		_type = type;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xA6);
		if (_type > 0)
		{
			writeD(_type);
		}
		writeS(_html);
	}
}
