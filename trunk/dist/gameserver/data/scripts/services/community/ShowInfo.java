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
package services.community;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class ShowInfo extends Functions implements ScriptFile
{
	public void show(String[] param)
	{
		Player player = getSelf();
		String info_folder = "";
		String info_page = "";
		if (player == null)
		{
			return;
		}
		if (param.length != 2)
		{
			String html = HtmCache.getInstance().getNotNull("scripts/services/wiki/error_page.htm", player);
			show(html, player);
			return;
		}
		info_folder = String.valueOf(param[0]);
		info_page = String.valueOf(param[1]);
		String html = HtmCache.getInstance().getNotNull("scripts/services/wiki/" + info_folder + "/" + info_page + ".htm", player);
		show(html, player);
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}
