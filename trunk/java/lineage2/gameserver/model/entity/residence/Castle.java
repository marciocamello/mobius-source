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
package lineage2.gameserver.model.entity.residence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.math.SafeMath;
import lineage2.gameserver.dao.CastleDAO;
import lineage2.gameserver.dao.CastleHiredGuardDAO;
import lineage2.gameserver.dao.ClanDataDAO;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Manor;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.Warehouse;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.support.MerchantGuard;
import lineage2.gameserver.templates.manor.CropProcure;
import lineage2.gameserver.templates.manor.SeedProduction;
import lineage2.gameserver.utils.GameStats;
import lineage2.gameserver.utils.Log;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CTreeIntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unchecked")
public class Castle extends Residence
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Castle.class);
	/**
	 * Field CASTLE_MANOR_DELETE_PRODUCTION. (value is ""DELETE FROM castle_manor_production WHERE castle_id=?;"")
	 */
	private static final String CASTLE_MANOR_DELETE_PRODUCTION = "DELETE FROM castle_manor_production WHERE castle_id=?;";
	/**
	 * Field CASTLE_MANOR_DELETE_PRODUCTION_PERIOD. (value is ""DELETE FROM castle_manor_production WHERE castle_id=? AND period=?;"")
	 */
	private static final String CASTLE_MANOR_DELETE_PRODUCTION_PERIOD = "DELETE FROM castle_manor_production WHERE castle_id=? AND period=?;";
	/**
	 * Field CASTLE_MANOR_DELETE_PROCURE. (value is ""DELETE FROM castle_manor_procure WHERE castle_id=?;"")
	 */
	private static final String CASTLE_MANOR_DELETE_PROCURE = "DELETE FROM castle_manor_procure WHERE castle_id=?;";
	/**
	 * Field CASTLE_MANOR_DELETE_PROCURE_PERIOD. (value is ""DELETE FROM castle_manor_procure WHERE castle_id=? AND period=?;"")
	 */
	private static final String CASTLE_MANOR_DELETE_PROCURE_PERIOD = "DELETE FROM castle_manor_procure WHERE castle_id=? AND period=?;";
	/**
	 * Field CASTLE_UPDATE_CROP. (value is ""UPDATE castle_manor_procure SET can_buy=? WHERE crop_id=? AND castle_id=? AND period=?"")
	 */
	private static final String CASTLE_UPDATE_CROP = "UPDATE castle_manor_procure SET can_buy=? WHERE crop_id=? AND castle_id=? AND period=?";
	/**
	 * Field CASTLE_UPDATE_SEED. (value is ""UPDATE castle_manor_production SET can_produce=? WHERE seed_id=? AND castle_id=? AND period=?"")
	 */
	private static final String CASTLE_UPDATE_SEED = "UPDATE castle_manor_production SET can_produce=? WHERE seed_id=? AND castle_id=? AND period=?";
	/**
	 * Field _merchantGuards.
	 */
	private final IntObjectMap<MerchantGuard> _merchantGuards = new HashIntObjectMap<>();
	/**
	 * Field _relatedFortresses.
	 */
	private final IntObjectMap<List<?>> _relatedFortresses = new CTreeIntObjectMap<>();
	/**
	 * Field _dominion.
	 */
	private Dominion _dominion;
	/**
	 * Field _procure.
	 */
	private List<CropProcure> _procure;
	/**
	 * Field _production.
	 */
	private List<SeedProduction> _production;
	/**
	 * Field _procureNext.
	 */
	private List<CropProcure> _procureNext;
	/**
	 * Field _productionNext.
	 */
	private List<SeedProduction> _productionNext;
	/**
	 * Field _isNextPeriodApproved.
	 */
	private boolean _isNextPeriodApproved;
	/**
	 * Field _TaxPercent.
	 */
	private int _TaxPercent;
	/**
	 * Field _TaxRate.
	 */
	private double _TaxRate;
	/**
	 * Field _treasury.
	 */
	private long _treasury;
	/**
	 * Field _collectedShops.
	 */
	private long _collectedShops;
	/**
	 * Field _collectedSeed.
	 */
	private long _collectedSeed;
	/**
	 * Field _npcStringName.
	 */
	private final NpcString _npcStringName;
	/**
	 * Field _spawnMerchantTickets.
	 */
	private final Set<ItemInstance> _spawnMerchantTickets = new CopyOnWriteArraySet<>();
	
	/**
	 * Constructor for Castle.
	 * @param set StatsSet
	 */
	public Castle(StatsSet set)
	{
		super(set);
		_npcStringName = NpcString.valueOf(1001000 + _id);
	}
	
	/**
	 * Method init.
	 */
	@Override
	public void init()
	{
		super.init();
		for (IntObjectMap.Entry<List<?>> entry : _relatedFortresses.entrySet())
		{
			_relatedFortresses.remove(entry.getKey());
			List<Integer> list = (List<Integer>) entry.getValue();
			List<Fortress> list2 = new ArrayList<>(list.size());
			for (int i : list)
			{
				Fortress fortress = ResidenceHolder.getInstance().getResidence(Fortress.class, i);
				if (fortress == null)
				{
					continue;
				}
				list2.add(fortress);
				fortress.addRelatedCastle(this);
			}
			_relatedFortresses.put(entry.getKey(), list2);
		}
	}
	
	/**
	 * Method getType.
	 * @return ResidenceType
	 */
	@Override
	public ResidenceType getType()
	{
		return ResidenceType.Castle;
	}
	
	/**
	 * Method changeOwner.
	 * @param newOwner Clan
	 */
	@Override
	public void changeOwner(Clan newOwner)
	{
		if (newOwner != null)
		{
			if (newOwner.getHasFortress() != 0)
			{
				Fortress oldFortress = ResidenceHolder.getInstance().getResidence(Fortress.class, newOwner.getHasFortress());
				if (oldFortress != null)
				{
					oldFortress.changeOwner(null);
				}
			}
			if (newOwner.getCastle() != 0)
			{
				Castle oldCastle = ResidenceHolder.getInstance().getResidence(Castle.class, newOwner.getCastle());
				if (oldCastle != null)
				{
					oldCastle.changeOwner(null);
				}
			}
		}
		Clan oldOwner = null;
		if ((getOwnerId() > 0) && ((newOwner == null) || (newOwner.getClanId() != getOwnerId())))
		{
			removeSkills();
			getDominion().changeOwner(null);
			getDominion().removeSkills();
			setTaxPercent(null, 0);
			cancelCycleTask();
			oldOwner = getOwner();
			if (oldOwner != null)
			{
				long amount = getTreasury();
				if (amount > 0)
				{
					Warehouse warehouse = oldOwner.getWarehouse();
					if (warehouse != null)
					{
						warehouse.addItem(ItemTemplate.ITEM_ID_ADENA, amount);
						addToTreasuryNoTax(-amount, false, false);
						Log.add(getName() + "|" + -amount + "|Castle:changeOwner", "treasury");
					}
				}
				for (Player clanMember : oldOwner.getOnlineMembers(0))
				{
					if ((clanMember != null) && (clanMember.getInventory() != null))
					{
						clanMember.getInventory().validateItems();
					}
				}
				oldOwner.setHasCastle(0);
			}
		}
		if (newOwner != null)
		{
			newOwner.setHasCastle(getId());
		}
		updateOwnerInDB(newOwner);
		rewardSkills();
		update();
	}
	
	/**
	 * Method loadData.
	 */
	@Override
	protected void loadData()
	{
		_TaxPercent = 0;
		_TaxRate = 0;
		_treasury = 0;
		_procure = new ArrayList<>();
		_production = new ArrayList<>();
		_procureNext = new ArrayList<>();
		_productionNext = new ArrayList<>();
		_isNextPeriodApproved = false;
		_owner = ClanDataDAO.getInstance().getOwner(this);
		CastleDAO.getInstance().select(this);
		CastleHiredGuardDAO.getInstance().load(this);
	}
	
	/**
	 * Method setTaxPercent.
	 * @param p int
	 */
	public void setTaxPercent(int p)
	{
		_TaxPercent = Math.min(Math.max(0, p), 100);
		_TaxRate = _TaxPercent / 100.0;
	}
	
	/**
	 * Method setTreasury.
	 * @param t long
	 */
	public void setTreasury(long t)
	{
		_treasury = t;
	}
	
	/**
	 * Method updateOwnerInDB.
	 * @param clan Clan
	 */
	private void updateOwnerInDB(Clan clan)
	{
		_owner = clan;
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET hasCastle=0 WHERE hasCastle=? LIMIT 1");
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			if (clan != null)
			{
				statement = con.prepareStatement("UPDATE clan_data SET hasCastle=? WHERE clan_id=? LIMIT 1");
				statement.setInt(1, getId());
				statement.setInt(2, getOwnerId());
				statement.execute();
				clan.broadcastClanStatus(true, false, false);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getTaxPercent.
	 * @return int
	 */
	public int getTaxPercent()
	{
		if (_TaxPercent > 15)
		{
			_TaxPercent = 15;
		}
		return _TaxPercent;
	}
	
	/**
	 * Method getTaxPercent0.
	 * @return int
	 */
	public int getTaxPercent0()
	{
		return _TaxPercent;
	}
	
	/**
	 * Method getCollectedShops.
	 * @return long
	 */
	public long getCollectedShops()
	{
		return _collectedShops;
	}
	
	/**
	 * Method getCollectedSeed.
	 * @return long
	 */
	public long getCollectedSeed()
	{
		return _collectedSeed;
	}
	
	/**
	 * Method setCollectedShops.
	 * @param value long
	 */
	public void setCollectedShops(long value)
	{
		_collectedShops = value;
	}
	
	/**
	 * Method setCollectedSeed.
	 * @param value long
	 */
	public void setCollectedSeed(long value)
	{
		_collectedSeed = value;
	}
	
	/**
	 * Method addToTreasury.
	 * @param amount long
	 * @param shop boolean
	 * @param seed boolean
	 */
	public void addToTreasury(long amount, boolean shop, boolean seed)
	{
		if (getOwnerId() <= 0)
		{
			return;
		}
		if (amount == 0)
		{
			return;
		}
		if ((amount > 1) && (_id != 5) && (_id != 8))
		{
			Castle royal = ResidenceHolder.getInstance().getResidence(Castle.class, _id >= 7 ? 8 : 5);
			if (royal != null)
			{
				long royalTax = (long) (amount * royal.getTaxRate());
				if (royal.getOwnerId() > 0)
				{
					royal.addToTreasury(royalTax, shop, seed);
					if (_id == 5)
					{
						Log.add("Aden|" + royalTax + "|Castle:adenTax", "treasury");
					}
					else if (_id == 8)
					{
						Log.add("Rune|" + royalTax + "|Castle:runeTax", "treasury");
					}
				}
				amount -= royalTax;
			}
		}
		addToTreasuryNoTax(amount, shop, seed);
	}
	
	/**
	 * Method addToTreasuryNoTax.
	 * @param amount long
	 * @param shop boolean
	 * @param seed boolean
	 */
	public void addToTreasuryNoTax(long amount, boolean shop, boolean seed)
	{
		if (getOwnerId() <= 0)
		{
			return;
		}
		if (amount == 0)
		{
			return;
		}
		GameStats.addAdena(amount);
		_treasury = SafeMath.addAndLimit(_treasury, amount);
		if (shop)
		{
			_collectedShops += amount;
		}
		if (seed)
		{
			_collectedSeed += amount;
		}
		setJdbcState(JdbcEntityState.UPDATED);
		update();
	}
	
	/**
	 * Method getCropRewardType.
	 * @param crop int
	 * @return int
	 */
	public int getCropRewardType(int crop)
	{
		int rw = 0;
		for (CropProcure cp : _procure)
		{
			if (cp.getId() == crop)
			{
				rw = cp.getReward();
			}
		}
		return rw;
	}
	
	/**
	 * Method setTaxPercent.
	 * @param activeChar Player
	 * @param taxPercent int
	 */
	public void setTaxPercent(Player activeChar, int taxPercent)
	{
		setTaxPercent(taxPercent);
		setJdbcState(JdbcEntityState.UPDATED);
		update();
		if (activeChar != null)
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.model.entity.Castle.OutOfControl.CastleTaxChangetTo", activeChar).addString(getName()).addNumber(taxPercent));
		}
	}
	
	/**
	 * Method getTaxRate.
	 * @return double
	 */
	public double getTaxRate()
	{
		if (_TaxRate > 0.15)
		{
			_TaxRate = 0.15;
		}
		return _TaxRate;
	}
	
	/**
	 * Method getTreasury.
	 * @return long
	 */
	public long getTreasury()
	{
		return _treasury;
	}
	
	/**
	 * Method getSeedProduction.
	 * @param period int
	 * @return List<SeedProduction>
	 */
	public List<SeedProduction> getSeedProduction(int period)
	{
		return period == CastleManorManager.PERIOD_CURRENT ? _production : _productionNext;
	}
	
	/**
	 * Method getCropProcure.
	 * @param period int
	 * @return List<CropProcure>
	 */
	public List<CropProcure> getCropProcure(int period)
	{
		return period == CastleManorManager.PERIOD_CURRENT ? _procure : _procureNext;
	}
	
	/**
	 * Method setSeedProduction.
	 * @param seed List<SeedProduction>
	 * @param period int
	 */
	public void setSeedProduction(List<SeedProduction> seed, int period)
	{
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			_production = seed;
		}
		else
		{
			_productionNext = seed;
		}
	}
	
	/**
	 * Method setCropProcure.
	 * @param crop List<CropProcure>
	 * @param period int
	 */
	public void setCropProcure(List<CropProcure> crop, int period)
	{
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			_procure = crop;
		}
		else
		{
			_procureNext = crop;
		}
	}
	
	/**
	 * Method getSeed.
	 * @param seedId int
	 * @param period int
	 * @return SeedProduction
	 */
	public synchronized SeedProduction getSeed(int seedId, int period)
	{
		for (SeedProduction seed : getSeedProduction(period))
		{
			if (seed.getId() == seedId)
			{
				return seed;
			}
		}
		return null;
	}
	
	/**
	 * Method getCrop.
	 * @param cropId int
	 * @param period int
	 * @return CropProcure
	 */
	public synchronized CropProcure getCrop(int cropId, int period)
	{
		for (CropProcure crop : getCropProcure(period))
		{
			if (crop.getId() == cropId)
			{
				return crop;
			}
		}
		return null;
	}
	
	/**
	 * Method getManorCost.
	 * @param period int
	 * @return long
	 */
	public long getManorCost(int period)
	{
		List<CropProcure> procure;
		List<SeedProduction> production;
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			procure = _procure;
			production = _production;
		}
		else
		{
			procure = _procureNext;
			production = _productionNext;
		}
		long total = 0;
		if (production != null)
		{
			for (SeedProduction seed : production)
			{
				total += Manor.getInstance().getSeedBuyPrice(seed.getId()) * seed.getStartProduce();
			}
		}
		if (procure != null)
		{
			for (CropProcure crop : procure)
			{
				total += crop.getPrice() * crop.getStartAmount();
			}
		}
		return total;
	}
	
	/**
	 * Method saveSeedData.
	 */
	public void saveSeedData()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_MANOR_DELETE_PRODUCTION);
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			if (_production != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[_production.size()];
				for (SeedProduction s : _production)
				{
					values[count] = "(" + getId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + CastleManorManager.PERIOD_CURRENT + ")";
					count++;
				}
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
			if (_productionNext != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[_productionNext.size()];
				for (SeedProduction s : _productionNext)
				{
					values[count] = "(" + getId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + CastleManorManager.PERIOD_NEXT + ")";
					count++;
				}
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error adding seed production data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method saveSeedData.
	 * @param period int
	 */
	public void saveSeedData(int period)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_MANOR_DELETE_PRODUCTION_PERIOD);
			statement.setInt(1, getId());
			statement.setInt(2, period);
			statement.execute();
			DbUtils.close(statement);
			List<SeedProduction> prod = null;
			prod = getSeedProduction(period);
			if (prod != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[prod.size()];
				for (SeedProduction s : prod)
				{
					values[count] = "(" + getId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + period + ")";
					count++;
				}
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error adding seed production data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method saveCropData.
	 */
	public void saveCropData()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_MANOR_DELETE_PROCURE);
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			if (_procure != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[_procure.size()];
				for (CropProcure cp : _procure)
				{
					values[count] = "(" + getId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + CastleManorManager.PERIOD_CURRENT + ")";
					count++;
				}
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
			if (_procureNext != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[_procureNext.size()];
				for (CropProcure cp : _procureNext)
				{
					values[count] = "(" + getId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + CastleManorManager.PERIOD_NEXT + ")";
					count++;
				}
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error adding crop data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method saveCropData.
	 * @param period int
	 */
	public void saveCropData(int period)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_MANOR_DELETE_PROCURE_PERIOD);
			statement.setInt(1, getId());
			statement.setInt(2, period);
			statement.execute();
			DbUtils.close(statement);
			List<CropProcure> proc = null;
			proc = getCropProcure(period);
			if (proc != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[proc.size()];
				for (CropProcure cp : proc)
				{
					values[count] = "(" + getId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + period + ")";
					count++;
				}
				if (values.length > 0)
				{
					query += values[0];
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error adding crop data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method updateCrop.
	 * @param cropId int
	 * @param amount long
	 * @param period int
	 */
	public void updateCrop(int cropId, long amount, int period)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_UPDATE_CROP);
			statement.setLong(1, amount);
			statement.setInt(2, cropId);
			statement.setInt(3, getId());
			statement.setInt(4, period);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("Error adding crop data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method updateSeed.
	 * @param seedId int
	 * @param amount long
	 * @param period int
	 */
	public void updateSeed(int seedId, long amount, int period)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_UPDATE_SEED);
			statement.setLong(1, amount);
			statement.setInt(2, seedId);
			statement.setInt(3, getId());
			statement.setInt(4, period);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("Error adding seed production data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method isNextPeriodApproved.
	 * @return boolean
	 */
	public boolean isNextPeriodApproved()
	{
		return _isNextPeriodApproved;
	}
	
	/**
	 * Method setNextPeriodApproved.
	 * @param val boolean
	 */
	public void setNextPeriodApproved(boolean val)
	{
		_isNextPeriodApproved = val;
	}
	
	/**
	 * Method getDominion.
	 * @return Dominion
	 */
	public Dominion getDominion()
	{
		return _dominion;
	}
	
	/**
	 * Method setDominion.
	 * @param dominion Dominion
	 */
	public void setDominion(Dominion dominion)
	{
		_dominion = dominion;
	}
	
	/**
	 * Method addRelatedFortress.
	 * @param type int
	 * @param fortress int
	 */
	public void addRelatedFortress(int type, int fortress)
	{
		List<Integer> fortresses = (List<Integer>) _relatedFortresses.get(type);
		if (fortresses == null)
		{
			_relatedFortresses.put(type, fortresses = new ArrayList<>());
		}
		fortresses.add(fortress);
	}
	
	/**
	 * Method getDomainFortressContract.
	 * @return int
	 */
	public int getDomainFortressContract()
	{
		List<Fortress> list = (List<Fortress>) _relatedFortresses.get(Fortress.DOMAIN);
		if (list == null)
		{
			return 0;
		}
		for (Fortress f : list)
		{
			if ((f.getContractState() == Fortress.CONTRACT_WITH_CASTLE) && (f.getCastleId() == getId()))
			{
				return f.getId();
			}
		}
		return 0;
	}
	
	/**
	 * Method update.
	 * @see lineage2.commons.dao.JdbcEntity#update()
	 */
	@Override
	public void update()
	{
		CastleDAO.getInstance().update(this);
	}
	
	/**
	 * Method getNpcStringName.
	 * @return NpcString
	 */
	public NpcString getNpcStringName()
	{
		return _npcStringName;
	}
	
	/**
	 * Method getRelatedFortresses.
	 * @return IntObjectMap<List>
	 */
	public IntObjectMap<List<?>> getRelatedFortresses()
	{
		return _relatedFortresses;
	}
	
	/**
	 * Method addMerchantGuard.
	 * @param merchantGuard MerchantGuard
	 */
	public void addMerchantGuard(MerchantGuard merchantGuard)
	{
		_merchantGuards.put(merchantGuard.getItemId(), merchantGuard);
	}
	
	/**
	 * Method getMerchantGuard.
	 * @param itemId int
	 * @return MerchantGuard
	 */
	public MerchantGuard getMerchantGuard(int itemId)
	{
		return _merchantGuards.get(itemId);
	}
	
	/**
	 * Method getMerchantGuards.
	 * @return IntObjectMap<MerchantGuard>
	 */
	public IntObjectMap<MerchantGuard> getMerchantGuards()
	{
		return _merchantGuards;
	}
	
	/**
	 * Method getSpawnMerchantTickets.
	 * @return Set<ItemInstance>
	 */
	public Set<ItemInstance> getSpawnMerchantTickets()
	{
		return _spawnMerchantTickets;
	}
	
	/**
	 * Method startCycleTask.
	 */
	@Override
	public void startCycleTask()
	{
	}
}
