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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lineage2.gameserver.Config;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.manor.CropProcure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Service class for manor
 * @author Janiko
 */
public class ManorDataHolder
{
	private static final Logger _log = LoggerFactory.getLogger(ManorDataHolder.class);
	private static ManorDataHolder _instance;
	
	private static Map<Integer, SeedData> _seeds;
	
	public static ManorDataHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new ManorDataHolder();
		}
		return _instance;
	}
	
	public static void reload()
	{
		_instance = new ManorDataHolder();
	}
	
	private ManorDataHolder()
	{
		_seeds = new ConcurrentHashMap<>();
		try
		{
			
			File filelists = new File(Config.DATAPACK_ROOT, "data/xml/other/seeds.xml");
			DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
			factory1.setValidating(false);
			factory1.setIgnoringComments(true);
			Document doc1 = factory1.newDocumentBuilder().parse(filelists);
			
			StatsSet set;
			NamedNodeMap attrs;
			Node att;
			int castleId;
			for (Node n = doc1.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if ("castle".equalsIgnoreCase(d.getNodeName()))
						{
							castleId = Integer.parseInt(d.getAttributes().getNamedItem("id").getNodeValue());
							for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
							{
								if ("crop".equalsIgnoreCase(c.getNodeName()))
								{
									set = new StatsSet();
									set.set("castleId", castleId);
									
									attrs = c.getAttributes();
									for (int i = 0; i < attrs.getLength(); i++)
									{
										att = attrs.item(i);
										set.set(att.getNodeName(), att.getNodeValue());
									}
									
									SeedData seed = new SeedData(set);
									_seeds.put(seed.getSeedId(), seed);
								}
							}
						}
					}
				}
			}
			_log.info(getClass().getSimpleName() + ": Loaded: " + _seeds.size() + " seeds.");
		}
		catch (DOMException | IOException | NumberFormatException | ParserConfigurationException | SAXException e)
		{
			_log.warn("TradeController: Buy lists could not be initialized.", e);
		}
	}
	
	public List<Integer> getAllCrops()
	{
		List<Integer> crops = new ArrayList<>();
		
		for (SeedData seed : _seeds.values())
		{
			if (!crops.contains(seed.getCropId()) && (seed.getCropId() != 0) && !crops.contains(seed.getCropId()))
			{
				crops.add(seed.getCropId());
			}
		}
		
		return crops;
	}
	
	public int getSeedBasicPrice(int seedId)
	{
		final ItemTemplate seedItem = ItemHolder.getInstance().getTemplate(seedId);
		if (seedItem != null)
		{
			return seedItem.getReferencePrice();
		}
		return 0;
	}
	
	public int getSeedBasicPriceByCrop(int cropId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCropId() == cropId)
			{
				return getSeedBasicPrice(seed.getSeedId());
			}
		}
		return 0;
	}
	
	public int getCropBasicPrice(int cropId)
	{
		final ItemTemplate cropItem = ItemHolder.getInstance().getTemplate(cropId);
		if (cropItem != null)
		{
			return cropItem.getReferencePrice();
		}
		return 0;
	}
	
	public int getMatureCrop(int cropId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCropId() == cropId)
			{
				return seed.getMatureId();
			}
		}
		return 0;
	}
	
	/**
	 * Returns price which lord pays to buy one seed
	 * @param seedId
	 * @return seed price
	 */
	public long getSeedBuyPrice(int seedId)
	{
		long buyPrice = getSeedBasicPrice(seedId);
		return (buyPrice > 0 ? buyPrice : 1);
	}
	
	public int getSeedMinLevel(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getLevel() - 5;
		}
		return -1;
	}
	
	public int getSeedMaxLevel(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getLevel() + 5;
		}
		return -1;
	}
	
	public int getSeedLevelByCrop(int cropId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCropId() == cropId)
			{
				return seed.getLevel();
			}
		}
		return 0;
	}
	
	public int getSeedLevel(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getLevel();
		}
		return -1;
	}
	
	public boolean isAlternative(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.isAlternative();
		}
		return false;
	}
	
	public int getCropType(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getCropId();
		}
		return -1;
	}
	
	public int getRewardItem(int cropId, int type)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCropId() == cropId)
			{
				return seed.getReward(type); // there can be several seeds with same crop, but reward should be the same for all.
			}
		}
		return -1;
	}
	
	/**
	 * Method getRewardAmountPerCrop.
	 * @param castle int
	 * @param cropId int
	 * @param type int
	 * @return long
	 */
	public synchronized long getRewardAmountPerCrop(int castle, int cropId, int type)
	{
		final CropProcure cs = ResidenceHolder.getInstance().getResidence(Castle.class, castle).getCropProcure(CastleManorManager.PERIOD_CURRENT).get(cropId);
		
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCropId() == cropId)
			{
				return cs.getPrice() / getCropBasicPrice(seed.getReward(type));
			}
		}
		
		return -1;
	}
	
	public int getRewardItemBySeed(int seedId, int type)
	{
		SeedData seed = _seeds.get(seedId);
		if (seed != null)
		{
			return seed.getReward(type);
		}
		return 0;
	}
	
	/**
	 * Return all crops which can be purchased by given castle
	 * @param castleId
	 * @return
	 */
	public List<Integer> getCropsForCastle(int castleId)
	{
		List<Integer> crops = new ArrayList<>();
		
		for (SeedData seed : _seeds.values())
		{
			if ((seed.getCastleId() == castleId) && !crops.contains(seed.getCropId()))
			{
				crops.add(seed.getCropId());
			}
		}
		
		return crops;
	}
	
	/**
	 * Return list of seed ids, which belongs to castle with given id
	 * @param castleId - id of the castle
	 * @return seedIds - list of seed ids
	 */
	public List<Integer> getSeedsForCastle(int castleId)
	{
		List<Integer> seedsID = new ArrayList<>();
		
		for (SeedData seed : _seeds.values())
		{
			if ((seed.getCastleId() == castleId) && !seedsID.contains(seed.getSeedId()))
			{
				seedsID.add(seed.getSeedId());
			}
		}
		
		return seedsID;
	}
	
	/**
	 * Returns castle id where seed can be sowned<br>
	 * @param seedId
	 * @return castleId
	 */
	public int getCastleIdForSeed(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		
		if (seed != null)
		{
			return seed.getCastleId();
		}
		return 0;
	}
	
	public int getSeedSaleLimit(int seedId)
	{
		SeedData seed = _seeds.get(seedId);
		
		if (seed != null)
		{
			return seed.getSeedLimit();
		}
		return 0;
	}
	
	public int getCropPuchaseLimit(int cropId)
	{
		for (SeedData seed : _seeds.values())
		{
			if (seed.getCropId() == cropId)
			{
				return seed.getCropLimit();
			}
		}
		return 0;
	}
	
	/**
	 * Method getAllSeeds.
	 * @return Map<Integer,SeedData>
	 */
	public Map<Integer, SeedData> getAllSeeds()
	{
		return _seeds;
	}
	
	public class SeedData
	{
		private final int _seedId;
		private final int _cropId; // crop type
		private final int _level; // seed level
		private final int _matureId; // mature crop type
		private final int _reward1;
		private final int _reward2;
		private final int _castleId; // id of manor (castle id) where seed can be farmed
		private final boolean _isAlternative;
		private final int _limitSeeds;
		private final int _limitCrops;
		
		public SeedData(StatsSet set)
		{
			_cropId = set.getInteger("id");
			_seedId = set.getInteger("seedId");
			_level = set.getInteger("level");
			_matureId = set.getInteger("mature_Id");
			_reward1 = set.getInteger("reward1");
			_reward2 = set.getInteger("reward2");
			_castleId = set.getInteger("castleId");
			_isAlternative = set.getBool("alternative");
			_limitCrops = set.getInteger("limit_crops");
			_limitSeeds = set.getInteger("limit_seed");
		}
		
		public int getCastleId()
		{
			return _castleId;
		}
		
		public int getSeedId()
		{
			return _seedId;
		}
		
		public int getCropId()
		{
			return _cropId;
		}
		
		public int getMatureId()
		{
			return _matureId;
		}
		
		public int getReward(int type)
		{
			return (type == 1 ? _reward1 : _reward2);
		}
		
		public int getLevel()
		{
			return _level;
		}
		
		public boolean isAlternative()
		{
			return _isAlternative;
		}
		
		public int getSeedLimit()
		{
			return _limitSeeds;
		}
		
		public int getCropLimit()
		{
			return _limitCrops;
		}
		
		@Override
		public String toString()
		{
			return "SeedData [_id=" + _seedId + ", _level=" + _level + ", _crop=" + _cropId + ", _mature=" + _matureId + ", _type1=" + _reward1 + ", _type2=" + _reward2 + ", _manorId=" + _castleId + ", _isAlternative=" + _isAlternative + ", _limitSeeds=" + _limitSeeds + ", _limitCrops=" + _limitCrops + "]";
		}
	}
}