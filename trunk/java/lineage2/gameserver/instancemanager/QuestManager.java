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
package lineage2.gameserver.instancemanager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.gameserver.model.quest.Quest;

public class QuestManager
{
	private static Map<String, Quest> _questsByName = new ConcurrentHashMap<>();
	private static Map<Integer, Quest> _questsById = new ConcurrentHashMap<>();
	
	public static Quest getQuest(String name)
	{
		return _questsByName.get(name);
	}
	
	public static Quest getQuest(Class<?> quest)
	{
		return getQuest(quest.getSimpleName());
	}
	
	public static Quest getQuest(int questId)
	{
		return _questsById.get(questId);
	}
	
	public static Quest getQuest2(String nameOrId)
	{
		if (_questsByName.containsKey(nameOrId))
		{
			return _questsByName.get(nameOrId);
		}
		try
		{
			int questId = Integer.valueOf(nameOrId);
			return _questsById.get(questId);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static void addQuest(Quest newQuest)
	{
		_questsByName.put(newQuest.getName(), newQuest);
		_questsById.put(newQuest.getQuestIntId(), newQuest);
	}
	
	public static Collection<Quest> getQuests()
	{
		return _questsByName.values();
	}
}
