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
import java.util.Iterator;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.commons.geometry.Polygon;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.SpawnHolder;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.spawn.PeriodOfDay;
import lineage2.gameserver.templates.spawn.SpawnNpcInfo;
import lineage2.gameserver.templates.spawn.SpawnTemplate;
import lineage2.gameserver.utils.Location;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SpawnsData extends AbstractDirParser<SpawnHolder>
{
	/**
	 * Field _instance.
	 */
	private static final SpawnsData _instance = new SpawnsData();
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SpawnsData.class);
	
	/**
	 * Method getInstance.
	 * @return SpawnsData
	 */
	public static SpawnsData getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for SpawnsData.
	 */
	protected SpawnsData()
	{
		super(SpawnHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/stats/npc/spawnlist/");
	}
	
	/**
	 * Method isIgnored.
	 * @param f File
	 * @return boolean
	 */
	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "spawn.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> spawnIterator = rootElement.elementIterator(); spawnIterator.hasNext();)
		{
			Element spawnElement = spawnIterator.next();
			if (spawnElement.getName().equalsIgnoreCase("spawns"))
			{
				spawnElement.attributeValue("name");
				for (Iterator<Element> subIterator = spawnElement.elementIterator(); subIterator.hasNext();)
				{
					Element subElement = subIterator.next();
					if (subElement.getName().equalsIgnoreCase("spawn"))
					{
						int npcId = Integer.parseInt(subElement.attributeValue("npcId"));
						int count = 1;
						int x = Integer.parseInt(subElement.attributeValue("x"));
						int y = Integer.parseInt(subElement.attributeValue("y"));
						int z = Integer.parseInt(subElement.attributeValue("z"));
						int h = Integer.parseInt(subElement.attributeValue("heading"));
						int respawn = 60;
						if (subElement.attributeValue("delay") != null)
						{
							respawn = Integer.parseInt(subElement.attributeValue("delay"));
						}
						int respawnRandom = 0;
						int periodODay = 0;
						if (subElement.attributeValue("periodOfDay") != null)
						{
							periodODay = Integer.parseInt(subElement.attributeValue("periodOfDay"));
						}
						PeriodOfDay periodOfDay = periodODay == 0 ? PeriodOfDay.NONE : periodODay == 1 ? PeriodOfDay.DAY : PeriodOfDay.NIGHT;
						String group = periodOfDay.name();
						SpawnTemplate template = new SpawnTemplate(periodOfDay, count, respawn, respawnRandom);
						template.addSpawnRange(new Location(x, y, z, h));
						MultiValueSet<String> parameters = StatsSet.EMPTY;
						if (parameters.isEmpty())
						{
							parameters = new MultiValueSet<>();
						}
						template.addNpc(new SpawnNpcInfo(npcId, 0, parameters));
						if (template.getNpcSize() == 0)
						{
							_log.info("Npc id is zero! File: " + getCurrentFileName());
							continue;
						}
						if (template.getSpawnRangeSize() == 0)
						{
							_log.info("No points to spawn! File: " + getCurrentFileName());
							continue;
						}
						getHolder().addSpawn(group, template);
					}
				}
			}
		}
	}
	
	/**
	 * Method parsePolygon0.
	 * @param name String
	 * @param e Element
	 * @return Polygon
	 */
	@SuppressWarnings("unused")
	private Polygon parsePolygon0(String name, Element e)
	{
		Polygon temp = new Polygon();
		for (Iterator<Element> addIterator = e.elementIterator("add"); addIterator.hasNext();)
		{
			Element addElement = addIterator.next();
			int x = Integer.parseInt(addElement.attributeValue("x"));
			int y = Integer.parseInt(addElement.attributeValue("y"));
			int zmin = Integer.parseInt(addElement.attributeValue("zmin"));
			int zmax = Integer.parseInt(addElement.attributeValue("zmax"));
			temp.add(x, y).setZmin(zmin).setZmax(zmax);
		}
		if (!temp.validate())
		{
			error("Invalid polygon: " + name + "{" + temp + "}. File: " + getCurrentFileName());
		}
		return temp;
	}
}
