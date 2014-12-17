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

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.LevelUpRewardHolder;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelUpRewardParser extends AbstractFileParser<LevelUpRewardHolder>
{
	private static final Logger _log = LoggerFactory.getLogger(LevelUpRewardParser.class);
	
	private static LevelUpRewardParser _instance = new LevelUpRewardParser();
	private static int playerLevel;
	
	protected LevelUpRewardParser()
	{
		super(LevelUpRewardHolder.getInstance());
	}
	
	public static LevelUpRewardParser getInstance()
	{
		return _instance;
	}
	
	@Override
	public String getDTDFileName()
	{
		return "LevelUpRewards.dtd";
	}
	
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		LevelUpRewardHolder.ItemLevel template = null;
		for (Element equipmentElement : rootElement.elements())
		{
			playerLevel = Integer.parseInt(equipmentElement.attributeValue("level"));
			for (Element item : equipmentElement.elements())
			{
				template = new LevelUpRewardHolder.ItemLevel();
				try
				{
					template.id = Integer.parseInt(item.attributeValue("id"));
					template.count = Integer.parseInt(item.attributeValue("count"));
					
					getHolder().addItemLevel(template, playerLevel);
				}
				catch (NumberFormatException e)
				{
					_log.warn("Could not load item: " + e.getMessage());
				}
			}
		}
	}
	
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/chars/LevelUpRewards.xml");
	}
}