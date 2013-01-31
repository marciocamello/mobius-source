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
package lineage2.gameserver.model.actor.instances.player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Macro
{
	/**
	 * Field CMD_TYPE_SKILL. (value is 1)
	 */
	public final static int CMD_TYPE_SKILL = 1;
	/**
	 * Field CMD_TYPE_ACTION. (value is 3)
	 */
	public final static int CMD_TYPE_ACTION = 3;
	/**
	 * Field CMD_TYPE_SHORTCUT. (value is 4)
	 */
	public final static int CMD_TYPE_SHORTCUT = 4;
	/**
	 * Field id.
	 */
	public int id;
	/**
	 * Field icon.
	 */
	public final int icon;
	/**
	 * Field name.
	 */
	public final String name;
	/**
	 * Field descr.
	 */
	public final String descr;
	/**
	 * Field acronym.
	 */
	public final String acronym;
	/**
	 * Field commands.
	 */
	public final L2MacroCmd[] commands;
	
	/**
	 * @author Mobius
	 */
	public static class L2MacroCmd
	{
		/**
		 * Field entry.
		 */
		public final int entry;
		/**
		 * Field type.
		 */
		public final int type;
		/**
		 * Field d1.
		 */
		public final int d1;
		/**
		 * Field d2.
		 */
		public final int d2;
		/**
		 * Field cmd.
		 */
		public final String cmd;
		
		/**
		 * Constructor for L2MacroCmd.
		 * @param entry int
		 * @param type int
		 * @param d1 int
		 * @param d2 int
		 * @param cmd String
		 */
		public L2MacroCmd(int entry, int type, int d1, int d2, String cmd)
		{
			this.entry = entry;
			this.type = type;
			this.d1 = d1;
			this.d2 = d2;
			this.cmd = cmd;
		}
	}
	
	/**
	 * Constructor for Macro.
	 * @param id int
	 * @param icon int
	 * @param name String
	 * @param descr String
	 * @param acronym String
	 * @param commands L2MacroCmd[]
	 */
	public Macro(int id, int icon, String name, String descr, String acronym, L2MacroCmd[] commands)
	{
		this.id = id;
		this.icon = icon;
		this.name = name;
		this.descr = descr;
		this.acronym = acronym.length() > 4 ? acronym.substring(0, 4) : acronym;
		this.commands = commands;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "macro id=" + id + " icon=" + icon + "name=" + name + " descr=" + descr + " acronym=" + acronym + " commands=" + commands;
	}
}
