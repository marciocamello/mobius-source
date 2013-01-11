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
package lineage2.gameserver.model.actor.instances.player;

public class Macro
{
	public final static int CMD_TYPE_SKILL = 1;
	public final static int CMD_TYPE_ACTION = 3;
	public final static int CMD_TYPE_SHORTCUT = 4;
	public int id;
	public final int icon;
	public final String name;
	public final String descr;
	public final String acronym;
	public final L2MacroCmd[] commands;
	
	public static class L2MacroCmd
	{
		public final int entry;
		public final int type;
		public final int d1;
		public final int d2;
		public final String cmd;
		
		public L2MacroCmd(int entry, int type, int d1, int d2, String cmd)
		{
			this.entry = entry;
			this.type = type;
			this.d1 = d1;
			this.d2 = d2;
			this.cmd = cmd;
		}
	}
	
	public Macro(int id, int icon, String name, String descr, String acronym, L2MacroCmd[] commands)
	{
		this.id = id;
		this.icon = icon;
		this.name = name;
		this.descr = descr;
		this.acronym = acronym.length() > 4 ? acronym.substring(0, 4) : acronym;
		this.commands = commands;
	}
	
	@Override
	public String toString()
	{
		return "macro id=" + id + " icon=" + icon + "name=" + name + " descr=" + descr + " acronym=" + acronym + " commands=" + commands;
	}
}
