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
package lineage2.commons.data.xml;

import lineage2.commons.logging.LoggerObject;

public abstract class AbstractHolder extends LoggerObject
{
	public void log()
	{
		info(String.format("loaded %d%s(s) count.", size(), formatOut(getClass().getSimpleName().replace("Holder", "")).toLowerCase()));
	}
	
	protected void process()
	{
	}
	
	public abstract int size();
	
	public abstract void clear();
	
	private static String formatOut(String st)
	{
		char[] chars = st.toCharArray();
		StringBuffer buf = new StringBuffer(chars.length);
		for (char ch : chars)
		{
			if (Character.isUpperCase(ch))
			{
				buf.append(" ");
			}
			buf.append(Character.toLowerCase(ch));
		}
		return buf.toString();
	}
}
