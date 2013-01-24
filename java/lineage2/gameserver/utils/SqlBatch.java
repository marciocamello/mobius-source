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
package lineage2.gameserver.utils;

public class SqlBatch
{
	private final String _header;
	private final String _tail;
	private StringBuilder _sb;
	private final StringBuilder _result;
	private long _limit = Long.MAX_VALUE;
	private boolean isEmpty = true;
	
	public SqlBatch(String header, String tail)
	{
		_header = header + "\n";
		_tail = (tail != null) && (tail.length() > 0) ? " " + tail + ";\n" : ";\n";
		_sb = new StringBuilder(_header);
		_result = new StringBuilder();
	}
	
	public SqlBatch(String header)
	{
		this(header, null);
	}
	
	public void writeStructure(String str)
	{
		_result.append(str);
	}
	
	public void write(String str)
	{
		isEmpty = false;
		if ((_sb.length() + str.length()) < (_limit - _tail.length()))
		{
			_sb.append(str + ",\n");
		}
		else
		{
			_sb.append(str + _tail);
			_result.append(_sb.toString());
			_sb = new StringBuilder(_header);
		}
	}
	
	public void writeBuffer()
	{
		String last = _sb.toString();
		if (last.length() > 0)
		{
			_result.append(last.substring(0, last.length() - 2) + _tail);
		}
		_sb = new StringBuilder(_header);
	}
	
	public String close()
	{
		if (_sb.length() > _header.length())
		{
			writeBuffer();
		}
		return _result.toString();
	}
	
	public void setLimit(long l)
	{
		_limit = l;
	}
	
	public boolean isEmpty()
	{
		return isEmpty;
	}
}
