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
import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ClassDataHolder;
import lineage2.gameserver.templates.player.ClassData;
import org.dom4j.Element;

/**
 * @author Smo
 */
public class ClassDataParser extends AbstractDirParser<ClassDataHolder>
{
	private static final ClassDataParser _instance = new ClassDataParser();
	
	public static ClassDataParser getInstance()
	{
		return _instance;
	}
	
	private ClassDataParser()
	{
		super(ClassDataHolder.getInstance());
	}
	
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/pc_parameters/class_data/");
	}
	
	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}
	
	@Override
	public String getDTDFileName()
	{
		return "class_data.dtd";
	}
	
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();
			int classId = Integer.parseInt(element.attributeValue("class_id"));
			ClassData template = new ClassData(classId);
			
			for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();
				
				if ("lvl_up_data".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						int lvl = Integer.parseInt(e.attributeValue("lvl"));
						double hp = Double.parseDouble(e.attributeValue("hp"));
						double mp = Double.parseDouble(e.attributeValue("mp"));
						double cp = Double.parseDouble(e.attributeValue("cp"));
						template.addLvlUpData(lvl, hp, mp, cp);
					}
				}
			}
			
			getHolder().addClassData(template);
		}
	}
}
