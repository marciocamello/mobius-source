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
package lineage2.gameserver.model.entity;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Map;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.HtmlUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HeroDiary
{
	private static final SimpleDateFormat SIMPLE_FORMAT = new SimpleDateFormat("HH:** dd.MM.yyyy");
	public static final int ACTION_RAID_KILLED = 1;
	public static final int ACTION_HERO_GAINED = 2;
	public static final int ACTION_CASTLE_TAKEN = 3;
	private final int _id;
	private final long _time;
	private final int _param;
	
	/**
	 * Constructor for HeroDiary.
	 * @param id int
	 * @param time long
	 * @param param int
	 */
	public HeroDiary(int id, long time, int param)
	{
		_id = id;
		_time = time;
		_param = param;
	}
	
	/**
	 * Method toString.
	 * @param player Player
	 * @return Map.Entry<String,String>
	 */
	public Map.Entry<String, String> toString(Player player)
	{
		String message = "";
		
		switch (_id)
		{
			case ACTION_RAID_KILLED:
				message = HtmlUtils.htmlNpcName(_param) + " was defeated.";
				break;
			
			case ACTION_HERO_GAINED:
				message = "Gained Hero status.";
				break;
			
			case ACTION_CASTLE_TAKEN:
				message = HtmlUtils.htmlResidenceName(_param) + " was successfully taken.";
				break;
			
			default:
				return null;
		}
		
		return new AbstractMap.SimpleEntry<>(SIMPLE_FORMAT.format(_time), message);
	}
}
