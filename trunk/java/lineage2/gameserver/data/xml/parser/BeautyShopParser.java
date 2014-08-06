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
import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.BeautyShopHolder;
import lineage2.gameserver.model.beautyshop.BeautyShopFace;
import lineage2.gameserver.model.beautyshop.BeautyShopHairColor;
import lineage2.gameserver.model.beautyshop.BeautyShopHairStyle;
import lineage2.gameserver.model.beautyshop.BeautyShopSet;
import org.dom4j.Element;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Smo
 */
public final class BeautyShopParser extends AbstractFileParser<BeautyShopHolder>
{
	private static final BeautyShopParser _instance = new BeautyShopParser();
	
	public static BeautyShopParser getInstance()
	{
		return _instance;
	}
	
	private BeautyShopParser()
	{
		super(BeautyShopHolder.getInstance());
	}
	
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/beauty_shop.xml");
	}
	
	@Override
	public String getDTDFileName()
	{
		return "beauty_shop.dtd";
	}
	
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<?> iterator = rootElement.elementIterator("config"); iterator.hasNext();)
		{
			Element element = (Element) iterator.next();
			Config.BEAUTY_SHOP_COIN_ITEM_ID = Integer.parseInt(element.attributeValue("coin_item_id"));
		}
		
		for (Iterator<?> iterator = rootElement.elementIterator("set"); iterator.hasNext();)
		{
			Element element = (Element) iterator.next();
			int setId = Integer.parseInt(element.attributeValue("id"));
			TIntObjectHashMap<BeautyShopHairStyle> hairStyles = new TIntObjectHashMap<>();
			TIntObjectHashMap<BeautyShopFace> faces = new TIntObjectHashMap<>();
			
			for (Iterator<?> subIterator = element.elementIterator("hair"); subIterator.hasNext();)
			{
				Element subElement = (Element) subIterator.next();
				int id = Integer.parseInt(subElement.attributeValue("id"));
				long adena = Long.parseLong(subElement.attributeValue("adena"));
				long coins = Long.parseLong(subElement.attributeValue("coins"));
				long resetPrice = Long.parseLong(subElement.attributeValue("reset_price"));
				TIntObjectHashMap<BeautyShopHairColor> colors = new TIntObjectHashMap<>();
				
				for (Iterator<?> colorIterator = subElement.elementIterator("color"); colorIterator.hasNext();)
				{
					Element colorElement = (Element) colorIterator.next();
					int cId = Integer.parseInt(colorElement.attributeValue("id"));
					long cAdena = Long.parseLong(colorElement.attributeValue("adena"));
					long cCoins = Long.parseLong(colorElement.attributeValue("coins"));
					colors.put(cId, new BeautyShopHairColor(cId, cAdena, cCoins));
				}
				
				if (colors.isEmpty())
				{
					colors.put(101, new BeautyShopHairColor(101, 0L, 0L));
				}
				
				hairStyles.put(id, new BeautyShopHairStyle(id, adena, coins, resetPrice, colors));
			}
			
			for (Iterator<?> subIterator = element.elementIterator("face"); subIterator.hasNext();)
			{
				Element subElement = (Element) subIterator.next();
				int id = Integer.parseInt(subElement.attributeValue("id"));
				long adena = Long.parseLong(subElement.attributeValue("adena"));
				long coins = Long.parseLong(subElement.attributeValue("coins"));
				long resetPrice = Long.parseLong(subElement.attributeValue("reset_price"));
				faces.put(id, new BeautyShopFace(id, adena, coins, resetPrice));
			}
			
			getHolder().addSet(new BeautyShopSet(setId, hairStyles, faces));
		}
	}
}