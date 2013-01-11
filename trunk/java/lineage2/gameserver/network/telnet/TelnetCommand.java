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
package lineage2.gameserver.network.telnet;

import org.apache.commons.lang3.ArrayUtils;

public abstract class TelnetCommand implements Comparable<TelnetCommand>
{
	private final String command;
	private final String[] acronyms;
	
	public TelnetCommand(String command)
	{
		this(command, ArrayUtils.EMPTY_STRING_ARRAY);
	}
	
	public TelnetCommand(String command, String... acronyms)
	{
		this.command = command;
		this.acronyms = acronyms;
	}
	
	public String getCommand()
	{
		return command;
	}
	
	public String[] getAcronyms()
	{
		return acronyms;
	}
	
	public abstract String getUsage();
	
	public abstract String handle(String[] args);
	
	public boolean equals(String command)
	{
		for (String acronym : acronyms)
		{
			if (command.equals(acronym))
			{
				return true;
			}
		}
		return this.command.equalsIgnoreCase(command);
	}
	
	@Override
	public String toString()
	{
		return command;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return true;
		}
		if (o instanceof TelnetCommand)
		{
			return command.equals(((TelnetCommand) o).command);
		}
		return false;
	}
	
	@Override
	public int compareTo(TelnetCommand o)
	{
		return command.compareTo(o.command);
	}
}
