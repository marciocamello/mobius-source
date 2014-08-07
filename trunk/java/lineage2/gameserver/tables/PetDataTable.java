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
package lineage2.gameserver.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.PetData;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.items.ItemInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetDataTable
{
	private static final Logger _log = LoggerFactory.getLogger(PetDataTable.class);
	private static final PetDataTable _instance = new PetDataTable();
	
	/**
	 * Method getInstance.
	 * @return PetDataTable
	 */
	public static final PetDataTable getInstance()
	{
		return _instance;
	}
	
	public final static int PET_WOLF_ID = 12077;
	public final static int HATCHLING_WIND_ID = 12311;
	public final static int HATCHLING_STAR_ID = 12312;
	public final static int HATCHLING_TWILIGHT_ID = 12313;
	public final static int STRIDER_WIND_ID = 12526;
	public final static int STRIDER_STAR_ID = 12527;
	public final static int STRIDER_TWILIGHT_ID = 12528;
	public final static int RED_STRIDER_WIND_ID = 16038;
	public final static int RED_STRIDER_STAR_ID = 16039;
	public final static int RED_STRIDER_TWILIGHT_ID = 16040;
	public final static int WYVERN_ID = 12621;
	public final static int BABY_BUFFALO_ID = 12780;
	public final static int BABY_KOOKABURRA_ID = 12781;
	public final static int BABY_COUGAR_ID = 12782;
	public final static int IMPROVED_BABY_BUFFALO_ID = 16034;
	public final static int IMPROVED_BABY_KOOKABURRA_ID = 16035;
	public final static int IMPROVED_BABY_COUGAR_ID = 16036;
	public final static int SIN_EATER_ID = 12564;
	public final static int GREAT_WOLF_ID = 16025;
	public final static int WGREAT_WOLF_ID = 16037;
	public final static int FENRIR_WOLF_ID = 16041;
	public final static int WFENRIR_WOLF_ID = 16042;
	public final static int FOX_SHAMAN_ID = 16043;
	public final static int WILD_BEAST_FIGHTER_ID = 16044;
	public final static int WHITE_WEASEL_ID = 16045;
	public final static int FAIRY_PRINCESS_ID = 16046;
	public final static int OWL_MONK_ID = 16050;
	public final static int SPIRIT_SHAMAN_ID = 16051;
	public final static int TOY_KNIGHT_ID = 16052;
	public final static int TURTLE_ASCETIC_ID = 16053;
	public final static int DEINONYCHUS_ID = 16067;
	public final static int GUARDIANS_STRIDER_ID = 16068;
	private final TIntObjectHashMap<PetData> _pets = new TIntObjectHashMap<>();
	
	/**
	 * Constructor for PetDataTable.
	 */
	private PetDataTable()
	{
		load();
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		load();
	}
	
	/**
	 * Method getInfo.
	 * @param petNpcId int
	 * @param level int
	 * @return PetData
	 */
	public PetData getInfo(int petNpcId, int level)
	{
		PetData result = null;
		result = _pets.get((petNpcId * 100) + level);
		
		if (result != null)
		{
			return result;
		}
		
		_log.error("Missing PetData for NpcId:" + petNpcId + " at level:" + level);
		return null;
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		PetData petData;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT id, level, exp, hp, mp, patk, pdef, matk, mdef, acc, evasion, crit, speed, atk_speed, cast_speed, max_meal, battle_meal, normal_meal, loadMax, hpregen, mpregen FROM pet_data");
			rset = statement.executeQuery();
			
			while (rset.next())
			{
				petData = new PetData();
				petData.setID(rset.getInt("id"));
				petData.setLevel(rset.getInt("level"));
				petData.setExp(rset.getLong("exp"));
				petData.setHP(rset.getInt("hp"));
				petData.setMP(rset.getInt("mp"));
				petData.setPAtk(rset.getInt("patk"));
				petData.setPDef(rset.getInt("pdef"));
				petData.setMAtk(rset.getInt("matk"));
				petData.setMDef(rset.getInt("mdef"));
				petData.setAccuracy(rset.getInt("acc"));
				petData.setEvasion(rset.getInt("evasion"));
				petData.setCritical(rset.getInt("crit"));
				petData.setSpeed(rset.getInt("speed"));
				petData.setAtkSpeed(rset.getInt("atk_speed"));
				petData.setCastSpeed(rset.getInt("cast_speed"));
				petData.setFeedMax(rset.getInt("max_meal"));
				petData.setFeedBattle(rset.getInt("battle_meal"));
				petData.setFeedNormal(rset.getInt("normal_meal"));
				petData.setMaxLoad(rset.getInt("loadMax"));
				petData.setHpRegen(rset.getInt("hpregen"));
				petData.setMpRegen(rset.getInt("mpregen"));
				petData.setControlItemId(getControlItemId(petData.getID()));
				petData.setFoodId(getFoodId(petData.getID()));
				petData.setMountable(isMountable(petData.getID()));
				petData.setMinLevel(getMinLevel(petData.getID()));
				petData.setAddFed(getAddFed(petData.getID()));
				_pets.put((petData.getID() * 100) + petData.getLevel(), petData);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		_log.info("PetDataTable: Loaded " + _pets.size() + " pets.");
	}
	
	/**
	 * Method deletePet.
	 * @param item ItemInstance
	 * @param owner Creature
	 */
	public static void deletePet(ItemInstance item, Creature owner)
	{
		int petObjectId = 0;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT objId FROM pets WHERE item_obj_id=?");
			statement.setInt(1, item.getObjectId());
			rset = statement.executeQuery();
			
			while (rset.next())
			{
				petObjectId = rset.getInt("objId");
			}
			
			DbUtils.close(statement, rset);
			Summon summon = owner.getPlayer().getSummonList().getPet();
			
			if ((summon != null) && (summon.getObjectId() == petObjectId))
			{
				owner.getPlayer().getSummonList().unsummonPet(false);
			}
			
			Player player = owner.getPlayer();
			
			if ((player != null) && player.isMounted() && (player.getMountObjId() == petObjectId))
			{
				player.setMount(0, 0, 0);
			}
			
			statement = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?");
			statement.setInt(1, item.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("could not restore pet objectid:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method unSummonPet.
	 * @param oldItem ItemInstance
	 * @param owner Creature
	 */
	public static void unSummonPet(ItemInstance oldItem, Creature owner)
	{
		int petObjectId = 0;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT objId FROM pets WHERE item_obj_id=?");
			statement.setInt(1, oldItem.getObjectId());
			rset = statement.executeQuery();
			
			while (rset.next())
			{
				petObjectId = rset.getInt("objId");
			}
			
			if (owner == null)
			{
				return;
			}
			
			Summon summon = owner.getPlayer().getSummonList().getPet();
			
			if ((summon != null) && (summon.getObjectId() == petObjectId))
			{
				owner.getPlayer().getSummonList().unsummonPet(false);
			}
			
			Player player = owner.getPlayer();
			
			if ((player != null) && player.isMounted() && (player.getMountObjId() == petObjectId))
			{
				player.setMount(0, 0, 0);
			}
		}
		catch (Exception e)
		{
			_log.error("could not restore pet objectid:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static enum L2Pet
	{
		WOLF(PET_WOLF_ID, 2375, 2515, false, 1, 12, .30, 2, 2),
		HATCHLING_WIND(HATCHLING_WIND_ID, 3500, 4038, false, 1, 12, .30, 2, 2),
		HATCHLING_STAR(HATCHLING_STAR_ID, 3501, 4038, false, 1, 12, .30, 2, 2),
		HATCHLING_TWILIGHT(HATCHLING_TWILIGHT_ID, 3502, 4038, false, 1, 100, .30, 2, 2),
		STRIDER_WIND(STRIDER_WIND_ID, 4422, 5168, true, 1, 12, .30, 2, 2),
		STRIDER_STAR(STRIDER_STAR_ID, 4423, 5168, true, 1, 12, .30, 2, 2),
		STRIDER_TWILIGHT(STRIDER_TWILIGHT_ID, 4424, 5168, true, 1, 100, .30, 2, 2),
		RED_STRIDER_WIND(RED_STRIDER_WIND_ID, 10308, 5168, true, 1, 12, .30, 2, 2),
		RED_STRIDER_STAR(RED_STRIDER_STAR_ID, 10309, 5168, true, 1, 12, .30, 2, 2),
		RED_STRIDER_TWILIGHT(RED_STRIDER_TWILIGHT_ID, 10310, 5168, true, 1, 100, .30, 2, 2),
		WYVERN(WYVERN_ID, 5249, 6316, true, 1, 12, .0, 2, 2),
		GREAT_WOLF(GREAT_WOLF_ID, 9882, 9668, false, 55, 10, .30, 2, 2),
		WGREAT_WOLF(WGREAT_WOLF_ID, 10307, 9668, true, 55, 12, .30, 2, 2),
		FENRIR_WOLF(FENRIR_WOLF_ID, 10426, 9668, true, 70, 12, .30, 2, 2),
		WFENRIR_WOLF(WFENRIR_WOLF_ID, 10611, 9668, true, 70, 12, .30, 2, 2),
		BABY_BUFFALO(BABY_BUFFALO_ID, 6648, 7582, false, 1, 12, .05, 2, 2),
		BABY_KOOKABURRA(BABY_KOOKABURRA_ID, 6650, 7582, false, 1, 12, .05, 2, 2),
		BABY_COUGAR(BABY_COUGAR_ID, 6649, 7582, false, 1, 12, .05, 2, 2),
		IMPROVED_BABY_BUFFALO(IMPROVED_BABY_BUFFALO_ID, 10311, 10425, false, 55, 12, .30, 2, 2),
		IMPROVED_BABY_KOOKABURRA(IMPROVED_BABY_KOOKABURRA_ID, 10313, 10425, false, 55, 12, .30, 2, 2),
		IMPROVED_BABY_COUGAR(IMPROVED_BABY_COUGAR_ID, 10312, 10425, false, 55, 12, .30, 2, 2),
		SIN_EATER(SIN_EATER_ID, 4425, 2515, false, 1, 12, .0, 2, 2),
		FOX_SHAMAN(FOX_SHAMAN_ID, 13020, -1, false, 25, 12, .30, 2, 2),
		WILD_BEAST_FIGHTER(WILD_BEAST_FIGHTER_ID, 13019, -1, false, 25, 12, .30, 2, 2),
		WHITE_WEASEL(WHITE_WEASEL_ID, 13017, -1, false, 25, 12, .30, 2, 2),
		FAIRY_PRINCESS(FAIRY_PRINCESS_ID, 13018, -1, false, 25, 12, .30, 2, 2),
		OWL_MONK(OWL_MONK_ID, 14063, -1, false, 25, 12, .30, 2, 2),
		SPIRIT_SHAMAN(SPIRIT_SHAMAN_ID, 14062, -1, false, 25, 12, .30, 2, 2),
		TOY_KNIGHT(TOY_KNIGHT_ID, 14061, -1, false, 25, 12, .30, 2, 2),
		TURTLE_ASCETIC(TURTLE_ASCETIC_ID, 14064, -1, false, 25, 12, .30, 2, 2),
		DEINONYCHUS(DEINONYCHUS_ID, 14828, 2515, false, 55, 12, .30, 2, 2),
		GUARDIANS_STRIDER(GUARDIANS_STRIDER_ID, 14819, 5168, true, 55, 12, .30, 2, 2);
		private final int _npcId;
		private final int _controlItemId;
		private final int _foodId;
		private final boolean _isMountable;
		private final int _minLevel;
		private final int _addFed;
		private final double _expPenalty;
		private final int _soulshots;
		private final int _spiritshots;
		
		/**
		 * Constructor for L2Pet.
		 * @param npcId int
		 * @param controlItemId int
		 * @param foodId int
		 * @param isMountabe boolean
		 * @param minLevel int
		 * @param addFed int
		 * @param expPenalty double
		 * @param soulshots int
		 * @param spiritshots int
		 */
		private L2Pet(int npcId, int controlItemId, int foodId, boolean isMountabe, int minLevel, int addFed, double expPenalty, int soulshots, int spiritshots)
		{
			_npcId = npcId;
			_controlItemId = controlItemId;
			_foodId = foodId;
			_isMountable = isMountabe;
			_minLevel = minLevel;
			_addFed = addFed;
			_expPenalty = expPenalty;
			_soulshots = soulshots;
			_spiritshots = spiritshots;
		}
		
		/**
		 * Method getNpcId.
		 * @return int
		 */
		public int getNpcId()
		{
			return _npcId;
		}
		
		/**
		 * Method getControlItemId.
		 * @return int
		 */
		public int getControlItemId()
		{
			return _controlItemId;
		}
		
		/**
		 * Method getFoodId.
		 * @return int
		 */
		public int getFoodId()
		{
			return _foodId;
		}
		
		/**
		 * Method isMountable.
		 * @return boolean
		 */
		public boolean isMountable()
		{
			return _isMountable;
		}
		
		/**
		 * Method getMinLevel.
		 * @return int
		 */
		public int getMinLevel()
		{
			return _minLevel;
		}
		
		/**
		 * Method getAddFed.
		 * @return int
		 */
		public int getAddFed()
		{
			return _addFed;
		}
		
		/**
		 * Method getExpPenalty.
		 * @return double
		 */
		public double getExpPenalty()
		{
			return _expPenalty;
		}
		
		/**
		 * Method getSoulshots.
		 * @return int
		 */
		public int getSoulshots()
		{
			return _soulshots;
		}
		
		/**
		 * Method getSpiritshots.
		 * @return int
		 */
		public int getSpiritshots()
		{
			return _spiritshots;
		}
	}
	
	/**
	 * Method getControlItemId.
	 * @param npcId int
	 * @return int
	 */
	public static int getControlItemId(int npcId)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getNpcId() == npcId)
			{
				return pet.getControlItemId();
			}
		}
		
		return 1;
	}
	
	/**
	 * Method getFoodId.
	 * @param npcId int
	 * @return int
	 */
	private static int getFoodId(int npcId)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getNpcId() == npcId)
			{
				return pet.getFoodId();
			}
		}
		
		return 1;
	}
	
	/**
	 * Method isMountable.
	 * @param npcId int
	 * @return boolean
	 */
	public static boolean isMountable(int npcId)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getNpcId() == npcId)
			{
				return pet.isMountable();
			}
		}
		
		return false;
	}
	
	/**
	 * Method getMinLevel.
	 * @param npcId int
	 * @return int
	 */
	public static int getMinLevel(int npcId)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getNpcId() == npcId)
			{
				return pet.getMinLevel();
			}
		}
		
		return 1;
	}
	
	/**
	 * Method getAddFed.
	 * @param npcId int
	 * @return int
	 */
	private static int getAddFed(int npcId)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getNpcId() == npcId)
			{
				return pet.getAddFed();
			}
		}
		
		return 1;
	}
	
	/**
	 * Method getExpPenalty.
	 * @param npcId int
	 * @return double
	 */
	public static double getExpPenalty(int npcId)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getNpcId() == npcId)
			{
				return pet.getExpPenalty();
			}
		}
		
		return 0f;
	}
	
	/**
	 * Method getSoulshots.
	 * @param npcId int
	 * @return int
	 */
	public static int getSoulshots(int npcId)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getNpcId() == npcId)
			{
				return pet.getSoulshots();
			}
		}
		
		return 2;
	}
	
	/**
	 * Method getSpiritshots.
	 * @param npcId int
	 * @return int
	 */
	public static int getSpiritshots(int npcId)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getNpcId() == npcId)
			{
				return pet.getSpiritshots();
			}
		}
		
		return 2;
	}
	
	/**
	 * Method getSummonId.
	 * @param item ItemInstance
	 * @return int
	 */
	public static int getSummonId(ItemInstance item)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getControlItemId() == item.getItemId())
			{
				return pet.getNpcId();
			}
		}
		
		return 0;
	}
	
	/**
	 * Method getPetControlItems.
	 * @return int[]
	 */
	public static int[] getPetControlItems()
	{
		int[] items = new int[L2Pet.values().length];
		int i = 0;
		
		for (L2Pet pet : L2Pet.values())
		{
			items[i++] = pet.getControlItemId();
		}
		
		return items;
	}
	
	/**
	 * Method isPetControlItem.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public static boolean isPetControlItem(ItemInstance item)
	{
		for (L2Pet pet : L2Pet.values())
		{
			if (pet.getControlItemId() == item.getItemId())
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Method isBabyPet.
	 * @param id int
	 * @return boolean
	 */
	public static boolean isBabyPet(int id)
	{
		switch (id)
		{
			case BABY_BUFFALO_ID:
			case BABY_KOOKABURRA_ID:
			case BABY_COUGAR_ID:
				return true;
				
			default:
				return false;
		}
	}
	
	/**
	 * Method isImprovedBabyPet.
	 * @param id int
	 * @return boolean
	 */
	public static boolean isImprovedBabyPet(int id)
	{
		switch (id)
		{
			case IMPROVED_BABY_BUFFALO_ID:
			case IMPROVED_BABY_KOOKABURRA_ID:
			case IMPROVED_BABY_COUGAR_ID:
			case FAIRY_PRINCESS_ID:
				return true;
				
			default:
				return false;
		}
	}
	
	/**
	 * Method isWolf.
	 * @param id int
	 * @return boolean
	 */
	public static boolean isWolf(int id)
	{
		return id == PET_WOLF_ID;
	}
	
	/**
	 * Method isHatchling.
	 * @param id int
	 * @return boolean
	 */
	public static boolean isHatchling(int id)
	{
		switch (id)
		{
			case HATCHLING_WIND_ID:
			case HATCHLING_STAR_ID:
			case HATCHLING_TWILIGHT_ID:
				return true;
				
			default:
				return false;
		}
	}
	
	/**
	 * Method isStrider.
	 * @param id int
	 * @return boolean
	 */
	public static boolean isStrider(int id)
	{
		switch (id)
		{
			case STRIDER_WIND_ID:
			case STRIDER_STAR_ID:
			case STRIDER_TWILIGHT_ID:
			case RED_STRIDER_WIND_ID:
			case RED_STRIDER_STAR_ID:
			case RED_STRIDER_TWILIGHT_ID:
			case GUARDIANS_STRIDER_ID:
				return true;
				
			default:
				return false;
		}
	}
	
	/**
	 * Method isGWolf.
	 * @param id int
	 * @return boolean
	 */
	public static boolean isGWolf(int id)
	{
		switch (id)
		{
			case GREAT_WOLF_ID:
			case WGREAT_WOLF_ID:
			case FENRIR_WOLF_ID:
			case WFENRIR_WOLF_ID:
				return true;
				
			default:
				return false;
		}
	}
	
	/**
	 * Method isVitaminPet.
	 * @param id int
	 * @return boolean
	 */
	public static boolean isVitaminPet(int id)
	{
		switch (id)
		{
			case FOX_SHAMAN_ID:
			case WILD_BEAST_FIGHTER_ID:
			case WHITE_WEASEL_ID:
			case FAIRY_PRINCESS_ID:
			case OWL_MONK_ID:
			case SPIRIT_SHAMAN_ID:
			case TOY_KNIGHT_ID:
			case TURTLE_ASCETIC_ID:
				return true;
				
			default:
				return false;
		}
	}
}
