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
package lineage2.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.ClassType;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.base.Sex;
import lineage2.gameserver.templates.player.PlayerTemplate;

public final class PlayerTemplateHolder extends AbstractHolder
{
	private static final PlayerTemplateHolder _instance = new PlayerTemplateHolder();
	private static final TIntObjectHashMap<TIntObjectHashMap<TIntObjectHashMap<PlayerTemplate>>> _templates;
	static
	{
		_templates = new TIntObjectHashMap<>();
	}
	private static int _templates_count = 0;
	
	public static PlayerTemplateHolder getInstance()
	{
		return _instance;
	}
	
	public void addPlayerTemplate(Race race, ClassType type, Sex sex, PlayerTemplate template)
	{
		if (!_templates.containsKey(race.ordinal()))
		{
			_templates.put(race.ordinal(), new TIntObjectHashMap<TIntObjectHashMap<PlayerTemplate>>());
		}
		if (!_templates.get(race.ordinal()).containsKey(type.ordinal()))
		{
			_templates.get(race.ordinal()).put(type.ordinal(), new TIntObjectHashMap<PlayerTemplate>());
		}
		_templates.get(race.ordinal()).get(type.ordinal()).put(sex.ordinal(), template);
		_templates_count++;
	}
	
	public PlayerTemplate getPlayerTemplate(Race race, ClassId classId, Sex sex)
	{
		ClassType type = classId.getType();
		if (!classId.isOfLevel(ClassLevel.Awaking))
		{
			race = classId.getRace();
		}
		if (_templates.containsKey(race.ordinal()))
		{
			if (_templates.get(race.ordinal()).containsKey(type.ordinal()))
			{
				return _templates.get(race.ordinal()).get(type.ordinal()).get(sex.ordinal());
			}
		}
		return null;
	}
	
	@Override
	public int size()
	{
		return _templates_count;
	}
	
	@Override
	public void clear()
	{
		_templates.clear();
	}
}
