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
package lineage2.gameserver.data.xml.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.StatuesHolder;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.utils.Location;

import org.dom4j.Element;

/**
 */
public class StatuesSpawnParser extends AbstractFileParser<StatuesHolder>
{
	private static StatuesSpawnParser ourInstance = new StatuesSpawnParser();
	
	private StatuesSpawnParser()
	{
		super(StatuesHolder.getInstance());
	}
	
	public static StatuesSpawnParser getInstance()
	{
		return ourInstance;
	}
	
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/StatuesSpawnData.xml");
	}
	
	@Override
	public String getDTDFileName()
	{
		return "StatuesSpawnData.dtd";
	}
	
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		
		for (Element statuesElement : rootElement.elements())
		{
			int type = Integer.parseInt(statuesElement.attributeValue("type"));
			CategoryType categoryType = CategoryType.getCategoryById(type, 0);
			
			List<Location> locations = new ArrayList<>();
			for (Element spawnElement : statuesElement.elements())
			{
				String[] loc = spawnElement.attributeValue("loc").split(",");
				locations.add(new Location(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3])));
			}
			StatuesHolder.getInstance().addSpawnInfo(categoryType, locations);
		}
	}
}
