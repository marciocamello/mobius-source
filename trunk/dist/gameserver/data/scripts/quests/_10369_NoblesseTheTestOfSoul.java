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
package quests;

import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.scripts.ScriptFile;

public class _10369_NoblesseTheTestOfSoul extends Quest implements ScriptFile
{
	private static final int CON1 = 31281;
	
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
	
	public _10369_NoblesseTheTestOfSoul()
	{
		super(false);
		addStartNpc(CON1);
		addTalkId(CON1);
		addLevelCheck(75, 99);
		addSubClassCheck();
	}
}
