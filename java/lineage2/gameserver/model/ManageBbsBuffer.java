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
package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageBbsBuffer
{
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(ManageBbsBuffer.class);
	private static final ManageBbsBuffer _instance = new ManageBbsBuffer();
	private final List<SBufferScheme> listScheme;
	
	public ManageBbsBuffer()
	{
		listScheme = new ArrayList<>();
	}
	
	public static ManageBbsBuffer getInstance()
	{
		return _instance;
	}
	
	public static SBufferScheme getScheme(int id, int obj_id)
	{
		for (SBufferScheme scheme : getInstance().listScheme)
		{
			if ((scheme.id == id) && (scheme.obj_id == obj_id))
			{
				return scheme;
			}
		}
		return null;
	}
	
	public static int getAutoIncrement(int ain)
	{
		int count = 0;
		for (SBufferScheme scheme : getInstance().listScheme)
		{
			if (ain == scheme.id)
			{
				count++;
			}
		}
		if (count == 0)
		{
			return ain;
		}
		return getAutoIncrement(ain + 1);
	}
	
	public static List<Integer> StringToInt(String list)
	{
		List<Integer> skills_id = new ArrayList<>();
		String[] s_id = list.split(";");
		for (String element : s_id)
		{
			skills_id.add(Integer.valueOf(Integer.parseInt(element)));
		}
		return skills_id;
	}
	
	public static String IntToString(List<Integer> id)
	{
		String buff_list = "";
		for (int i = 0; i < id.size(); i++)
		{
			buff_list = buff_list + new StringBuilder().append(id.get(i)).append(";").toString();
		}
		return buff_list;
	}
	
	public static List<SBufferScheme> getSchemeList()
	{
		return getInstance().listScheme;
	}
	
	public static int getCountOnePlayer(int obj_id)
	{
		int count = 0;
		for (SBufferScheme scheme : getInstance().listScheme)
		{
			if (obj_id == scheme.obj_id)
			{
				count++;
			}
		}
		return count;
	}
	
	public static boolean existName(int obj_id, String name)
	{
		for (SBufferScheme scheme : getInstance().listScheme)
		{
			if ((obj_id == scheme.obj_id) && (name == scheme.name))
			{
				return true;
			}
		}
		return false;
	}
	
	public static List<SBufferScheme> getSchemePlayer(int obj_id)
	{
		List<SBufferScheme> list = new ArrayList<>();
		for (SBufferScheme sm : getInstance().listScheme)
		{
			if (sm.obj_id == obj_id)
			{
				list.add(sm);
			}
		}
		return list;
	}
	
	public static class SBufferScheme
	{
		public int id;
		public int obj_id;
		public String name;
		public List<Integer> skills_id;
		
		public SBufferScheme()
		{
			id = 0;
			obj_id = 0;
			name = "";
			skills_id = new ArrayList<>();
		}
	}
}
