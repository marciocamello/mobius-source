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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javolution.util.FastList;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.EnchantSkillLearn;
import lineage2.gameserver.network.serverpackets.ExCallToChangeClass;
import lineage2.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import lineage2.gameserver.network.serverpackets.ExShowUsmVideo;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.tables.SkillTreeTable;
import lineage2.gameserver.utils.ItemFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.map.hash.TIntIntHashMap;

/**
 * @author Mobius
 */
public class AwakingManager implements OnPlayerEnterListener
{
	private static final Logger _log = LoggerFactory.getLogger(AwakingManager.class);
	
	private static AwakingManager _instance;
	
	private static final int ESSENCE_OF_THE_LESSER_GIANTS = 30306;
	
	public static final int CHAOS_ESSENCE = 36949;
	public static final int CHAOS_ESSENCE_DUAL_CLASS = 37494;
	public static final int CHAOS_POMANDER = 37374;
	public static final int CHAOS_POMANDER_DUAL_CLASS = 37375;
	
	private static final TIntIntHashMap _CA = new TIntIntHashMap(69);
	private static final TIntIntHashMap LEGACY_WEAPONS = new TIntIntHashMap(36);
	private static final TIntIntHashMap AWAKEN_POWER = new TIntIntHashMap(8);
	private static final TIntIntHashMap CLOAK_DUAL_CLASS = new TIntIntHashMap(8);
	
	private static final Integer[] _AlterSigel =
	{
		10250,
		10249
	};
	private static final Integer[] _AlterTyrr =
	{
		10500,
		10499
	};
	private static final Integer[] _AlterOthell =
	{
		10750,
		10749
	};
	private static final Integer[] _AlterYul =
	{
		11000,
		10999
	};
	private static final Integer[] _AlterFeoh =
	{
		11249,
		11247
	};
	private static final Integer[] _AlterIss =
	{
		11750,
		11749
	};
	private static final Integer[] _AlterWynn =
	{
		11500,
		11499
	};
	private static final Integer[] _AlterAerore =
	{
		12000,
		11999
	};
	private static final HashMap<Integer, Integer[]> ALTER_SKILLS = new HashMap<>();
	private static final int[] count30T =
	{
		0,
		0,
		0,
		0,
		1,
		1,
		2,
		3,
		4,
		5,
		6,
		7,
		9,
		10,
		12,
		13,
		15,
		17,
		19,
		22,
		24,
		27,
		29,
		32,
		35,
		42,
		45,
		48,
		63,
		70,
		83
	};
	private static final int[] count15T =
	{
		0,
		0,
		0,
		0,
		1,
		1,
		2,
		3,
		4,
		5,
		7,
		9,
		10,
		19,
		24,
		35
	};
	
	/**
	 * Method load.
	 */
	private void load()
	{
		if (Config.AWAKING_FREE)
		{
			CharListenerList.addGlobal(this);
		}
		
		_CA.clear();
		ALTER_SKILLS.clear();
		LEGACY_WEAPONS.clear();
		AWAKEN_POWER.clear();
		CLOAK_DUAL_CLASS.clear();
		/***************************************************************************************************
		 * 139 H_PhoenixKnight, H_HellKnight, E_EvaTemplar, DE_ShillienTemplar 140 H_Duelist, H_Dreadnought, O_Titan, O_GrandKhauatari, D_Maestro, K_Male_Doombringer 141 H_Adventurer, E_WindRider, DE_GhostHunter, D_FortuneSeeker, 142 H_Sagittarius, E_MoonlightSentinel, DE_GhostSentinel,
		 * K_Female_Trickster 143 H_Archmage, H_Soultaker, E_MysticMuse, DE_StormScreamer, K_Male_Soulhound, K_Female_Soulhound 144 H_Hierophant, E_SwordMuse, DE_SpectralDancer, O_Dominator, O_Doomcryer, K_Judicator 145 H_ArcanaLord, E_ElementalMaster, DE_SpectralMaster 146 H_Cardinal, E_EvaSaint,
		 * DE_ShillienSaint
		 ****************************************************************************************************/
		_CA.put(90, 148);
		_CA.put(91, 149);
		_CA.put(99, 150);
		_CA.put(106, 151);
		_CA.put(139, 148);
		_CA.put(139, 149);
		_CA.put(139, 150);
		_CA.put(139, 151);
		_CA.put(89, 153);
		_CA.put(88, 152);
		_CA.put(113, 154);
		_CA.put(114, 155);
		_CA.put(118, 156);
		_CA.put(131, 157);
		_CA.put(140, 152);
		_CA.put(140, 153);
		_CA.put(140, 154);
		_CA.put(140, 155);
		_CA.put(140, 157);
		_CA.put(93, 158);
		_CA.put(101, 159);
		_CA.put(108, 160);
		_CA.put(117, 161);
		_CA.put(141, 158);
		_CA.put(141, 159);
		_CA.put(141, 160);
		_CA.put(141, 161);
		_CA.put(92, 162);
		_CA.put(102, 163);
		_CA.put(109, 164);
		_CA.put(134, 165);
		_CA.put(142, 162);
		_CA.put(142, 163);
		_CA.put(142, 164);
		_CA.put(142, 165);
		_CA.put(94, 166);
		_CA.put(95, 167);
		_CA.put(103, 168);
		_CA.put(110, 169);
		_CA.put(132, 170);
		_CA.put(133, 170);
		_CA.put(143, 166);
		_CA.put(143, 167);
		_CA.put(143, 168);
		_CA.put(143, 169);
		_CA.put(143, 170);
		_CA.put(98, 171);
		_CA.put(100, 172);
		_CA.put(115, 174);
		_CA.put(116, 175);
		_CA.put(107, 173);
		_CA.put(144, 171);
		_CA.put(144, 172);
		_CA.put(144, 173);
		_CA.put(144, 175);
		_CA.put(96, 176);
		_CA.put(104, 177);
		_CA.put(111, 178);
		_CA.put(145, 176);
		_CA.put(145, 177);
		_CA.put(145, 178);
		_CA.put(97, 179);
		_CA.put(146, 179);
		_CA.put(105, 180);
		_CA.put(146, 180);
		_CA.put(112, 181);
		_CA.put(146, 181);
		
		AWAKEN_POWER.put(139, 32264);
		AWAKEN_POWER.put(148, 32264);
		AWAKEN_POWER.put(149, 32264);
		AWAKEN_POWER.put(150, 32264);
		AWAKEN_POWER.put(151, 32264);
		AWAKEN_POWER.put(140, 32265);
		AWAKEN_POWER.put(152, 32265);
		AWAKEN_POWER.put(153, 32265);
		AWAKEN_POWER.put(154, 32265);
		AWAKEN_POWER.put(155, 32265);
		AWAKEN_POWER.put(156, 32265);
		AWAKEN_POWER.put(157, 32265);
		AWAKEN_POWER.put(141, 32266);
		AWAKEN_POWER.put(158, 32266);
		AWAKEN_POWER.put(159, 32266);
		AWAKEN_POWER.put(160, 32266);
		AWAKEN_POWER.put(161, 32266);
		AWAKEN_POWER.put(142, 32267);
		AWAKEN_POWER.put(162, 32267);
		AWAKEN_POWER.put(163, 32267);
		AWAKEN_POWER.put(164, 32267);
		AWAKEN_POWER.put(165, 32267);
		AWAKEN_POWER.put(143, 32268);
		AWAKEN_POWER.put(166, 32268);
		AWAKEN_POWER.put(167, 32268);
		AWAKEN_POWER.put(168, 32268);
		AWAKEN_POWER.put(169, 32268);
		AWAKEN_POWER.put(170, 32268);
		AWAKEN_POWER.put(144, 32269);
		AWAKEN_POWER.put(171, 32269);
		AWAKEN_POWER.put(172, 32269);
		AWAKEN_POWER.put(173, 32269);
		AWAKEN_POWER.put(174, 32269);
		AWAKEN_POWER.put(175, 32269);
		AWAKEN_POWER.put(145, 32270);
		AWAKEN_POWER.put(176, 32270);
		AWAKEN_POWER.put(177, 32270);
		AWAKEN_POWER.put(178, 32270);
		AWAKEN_POWER.put(146, 32271);
		AWAKEN_POWER.put(179, 32271);
		AWAKEN_POWER.put(180, 32271);
		AWAKEN_POWER.put(181, 32271);
		
		CLOAK_DUAL_CLASS.put(139, 30310);
		CLOAK_DUAL_CLASS.put(148, 30310);
		CLOAK_DUAL_CLASS.put(149, 30310);
		CLOAK_DUAL_CLASS.put(150, 30310);
		CLOAK_DUAL_CLASS.put(151, 30310);
		CLOAK_DUAL_CLASS.put(140, 30311);
		CLOAK_DUAL_CLASS.put(152, 30311);
		CLOAK_DUAL_CLASS.put(153, 30311);
		CLOAK_DUAL_CLASS.put(154, 30311);
		CLOAK_DUAL_CLASS.put(155, 30311);
		CLOAK_DUAL_CLASS.put(156, 30311);
		CLOAK_DUAL_CLASS.put(157, 30311);
		CLOAK_DUAL_CLASS.put(141, 30312);
		CLOAK_DUAL_CLASS.put(158, 30312);
		CLOAK_DUAL_CLASS.put(159, 30312);
		CLOAK_DUAL_CLASS.put(160, 30312);
		CLOAK_DUAL_CLASS.put(161, 30312);
		CLOAK_DUAL_CLASS.put(142, 30313);
		CLOAK_DUAL_CLASS.put(162, 30313);
		CLOAK_DUAL_CLASS.put(163, 30313);
		CLOAK_DUAL_CLASS.put(164, 30313);
		CLOAK_DUAL_CLASS.put(165, 30313);
		CLOAK_DUAL_CLASS.put(143, 30314);
		CLOAK_DUAL_CLASS.put(166, 30314);
		CLOAK_DUAL_CLASS.put(167, 30314);
		CLOAK_DUAL_CLASS.put(168, 30314);
		CLOAK_DUAL_CLASS.put(169, 30314);
		CLOAK_DUAL_CLASS.put(170, 30314);
		CLOAK_DUAL_CLASS.put(144, 30315);
		CLOAK_DUAL_CLASS.put(171, 30315);
		CLOAK_DUAL_CLASS.put(172, 30315);
		CLOAK_DUAL_CLASS.put(173, 30315);
		CLOAK_DUAL_CLASS.put(174, 30315);
		CLOAK_DUAL_CLASS.put(175, 30315);
		CLOAK_DUAL_CLASS.put(145, 30316);
		CLOAK_DUAL_CLASS.put(176, 30316);
		CLOAK_DUAL_CLASS.put(177, 30316);
		CLOAK_DUAL_CLASS.put(178, 30316);
		CLOAK_DUAL_CLASS.put(146, 30317);
		CLOAK_DUAL_CLASS.put(179, 30317);
		CLOAK_DUAL_CLASS.put(180, 30317);
		CLOAK_DUAL_CLASS.put(181, 30317);
		
		LEGACY_WEAPONS.put(88, 33717);
		LEGACY_WEAPONS.put(89, 33718);
		LEGACY_WEAPONS.put(90, 33719);
		LEGACY_WEAPONS.put(91, 33720);
		LEGACY_WEAPONS.put(92, 33721);
		LEGACY_WEAPONS.put(93, 33722);
		LEGACY_WEAPONS.put(94, 33723);
		LEGACY_WEAPONS.put(95, 33724);
		LEGACY_WEAPONS.put(96, 33725);
		LEGACY_WEAPONS.put(97, 33726);
		LEGACY_WEAPONS.put(98, 33727);
		LEGACY_WEAPONS.put(99, 33728);
		LEGACY_WEAPONS.put(100, 33729);
		LEGACY_WEAPONS.put(101, 33730);
		LEGACY_WEAPONS.put(102, 33731);
		LEGACY_WEAPONS.put(103, 33732);
		LEGACY_WEAPONS.put(104, 33733);
		LEGACY_WEAPONS.put(105, 33734);
		LEGACY_WEAPONS.put(106, 33735);
		LEGACY_WEAPONS.put(107, 33736);
		LEGACY_WEAPONS.put(108, 33737);
		LEGACY_WEAPONS.put(109, 33738);
		LEGACY_WEAPONS.put(110, 33739);
		LEGACY_WEAPONS.put(111, 33740);
		LEGACY_WEAPONS.put(112, 33741);
		LEGACY_WEAPONS.put(113, 33742);
		LEGACY_WEAPONS.put(114, 33743);
		LEGACY_WEAPONS.put(115, 33744);
		LEGACY_WEAPONS.put(116, 33745);
		LEGACY_WEAPONS.put(117, 33746);
		LEGACY_WEAPONS.put(118, 33747);
		LEGACY_WEAPONS.put(131, 33761);
		LEGACY_WEAPONS.put(132, 33762);
		LEGACY_WEAPONS.put(133, 33763);
		LEGACY_WEAPONS.put(134, 33763);
		LEGACY_WEAPONS.put(136, 33765);
		
		ALTER_SKILLS.put(Integer.valueOf(139), _AlterSigel);
		ALTER_SKILLS.put(Integer.valueOf(148), _AlterSigel);
		ALTER_SKILLS.put(Integer.valueOf(149), _AlterSigel);
		ALTER_SKILLS.put(Integer.valueOf(150), _AlterSigel);
		ALTER_SKILLS.put(Integer.valueOf(151), _AlterSigel);
		ALTER_SKILLS.put(Integer.valueOf(140), _AlterTyrr);
		ALTER_SKILLS.put(Integer.valueOf(152), _AlterTyrr);
		ALTER_SKILLS.put(Integer.valueOf(153), _AlterTyrr);
		ALTER_SKILLS.put(Integer.valueOf(154), _AlterTyrr);
		ALTER_SKILLS.put(Integer.valueOf(155), _AlterTyrr);
		ALTER_SKILLS.put(Integer.valueOf(156), _AlterTyrr);
		ALTER_SKILLS.put(Integer.valueOf(157), _AlterTyrr);
		ALTER_SKILLS.put(Integer.valueOf(141), _AlterOthell);
		ALTER_SKILLS.put(Integer.valueOf(158), _AlterOthell);
		ALTER_SKILLS.put(Integer.valueOf(159), _AlterOthell);
		ALTER_SKILLS.put(Integer.valueOf(160), _AlterOthell);
		ALTER_SKILLS.put(Integer.valueOf(161), _AlterOthell);
		ALTER_SKILLS.put(Integer.valueOf(142), _AlterYul);
		ALTER_SKILLS.put(Integer.valueOf(162), _AlterYul);
		ALTER_SKILLS.put(Integer.valueOf(163), _AlterYul);
		ALTER_SKILLS.put(Integer.valueOf(164), _AlterYul);
		ALTER_SKILLS.put(Integer.valueOf(165), _AlterYul);
		ALTER_SKILLS.put(Integer.valueOf(143), _AlterFeoh);
		ALTER_SKILLS.put(Integer.valueOf(166), _AlterFeoh);
		ALTER_SKILLS.put(Integer.valueOf(167), _AlterFeoh);
		ALTER_SKILLS.put(Integer.valueOf(168), _AlterFeoh);
		ALTER_SKILLS.put(Integer.valueOf(169), _AlterFeoh);
		ALTER_SKILLS.put(Integer.valueOf(170), _AlterFeoh);
		ALTER_SKILLS.put(Integer.valueOf(144), _AlterIss);
		ALTER_SKILLS.put(Integer.valueOf(171), _AlterIss);
		ALTER_SKILLS.put(Integer.valueOf(172), _AlterIss);
		ALTER_SKILLS.put(Integer.valueOf(173), _AlterIss);
		ALTER_SKILLS.put(Integer.valueOf(174), _AlterIss);
		ALTER_SKILLS.put(Integer.valueOf(175), _AlterIss);
		ALTER_SKILLS.put(Integer.valueOf(145), _AlterWynn);
		ALTER_SKILLS.put(Integer.valueOf(176), _AlterWynn);
		ALTER_SKILLS.put(Integer.valueOf(177), _AlterWynn);
		ALTER_SKILLS.put(Integer.valueOf(178), _AlterWynn);
		ALTER_SKILLS.put(Integer.valueOf(146), _AlterAerore);
		ALTER_SKILLS.put(Integer.valueOf(179), _AlterAerore);
		ALTER_SKILLS.put(Integer.valueOf(180), _AlterAerore);
		ALTER_SKILLS.put(Integer.valueOf(181), _AlterAerore);
		
		_log.info("AwakingManager: Loaded 34 Awaking class for " + _CA.size() + " normal class. Loaded " + LEGACY_WEAPONS.size() + " Legacy Weapons.");
	}
	
	/**
	 * Method getInstance.
	 * @return AwakingManager
	 */
	public static AwakingManager getInstance()
	{
		if (_instance == null)
		{
			_log.info("Initializing: AwakingManager");
			_instance = new AwakingManager();
			_instance.load();
		}
		
		return _instance;
	}
	
	/**
	 * Method SendReqToStartQuest.
	 * @param player Player
	 */
	public void SendReqToStartQuest(Player player)
	{
		if (player.getClassId().level() < 3)
		{
			return;
		}
		
		int newClass = _CA.get(player.getClassId().getId());
		player.sendPacket(new ExCallToChangeClass(newClass, false));
	}
	
	/**
	 * Method childOf.
	 * @param oldClass ClassId
	 * @return int
	 */
	public int childOf(ClassId oldClass)
	{
		int newClass = _CA.get(oldClass.getId());
		return newClass;
	}
	
	/**
	 * Method SendReqToAwaking.
	 * @param player Player
	 */
	public void SendReqToAwaking(Player player)
	{
		if (player.getClassId().level() < 3)
		{
			return;
		}
		
		int newClass = _CA.get(player.getClassId().getId());
		player.sendPacket(new ExChangeToAwakenedClass(newClass));
		return;
	}
	
	/**
	 * Method SetAwakingId.
	 * @param player Player
	 */
	public void SetAwakingId(Player player)
	{
		int _oldId = player.getClassId().getId();
		player.broadcastPacket(new SocialAction(player.getObjectId(), 20));
		
		if (Config.ALT_DELETE_SKILL_PROF) // its important part of correct skill assignment this If sentence, removed from player.java
		{
			onTransferOnlyRemoveSkills(player);
		}
		
		getRaceSkill(player);
		player.setClassId(_CA.get(_oldId), false, false);
		player.broadcastUserInfo();
		giveItems(player, _oldId, _CA.get(_oldId));
		giveItemsChaosPomander(player);
		giveItemsChaosEssence(player);
		giveGiantEssences(player, false);
	}
	
	public void giveItemsChaosPomander(Player player)
	{
		ItemFunctions.addItem(player, player.isBaseClassActive() ? CHAOS_POMANDER : CHAOS_POMANDER_DUAL_CLASS, 2, true);
	}
	
	public void giveItemsChaosEssence(Player player)
	{
		ItemFunctions.addItem(player, player.isBaseClassActive() ? CHAOS_ESSENCE : CHAOS_ESSENCE_DUAL_CLASS, 1, true);
	}
	
	/**
	 * Method getRaceSkill.
	 * @param player Player
	 * @return null
	 */
	public Skill getRaceSkill(Player player)
	{
		int race = player.getRace().ordinal();
		Skill skill = null;
		
		if (player.getClassId().isOfLevel(ClassLevel.Fourth))
		{
			switch (race)
			{
				case 0:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				
				case 1:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				
				case 2:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				
				case 3:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				
				case 4:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				
				case 5:
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
			}
		}
		else
		{
			player.sendActionFailed();
		}
		
		player.updateStats();
		return null;
	}
	
	/**
	 * Method giveItems.
	 * @param player Player
	 * @param previousClassId
	 * @param newClassId
	 */
	public void giveItems(Player player, Integer previousClassId, Integer newClassId)
	{
		if (!player.getSubClassList().isDualClassActive())
		{
			ItemFunctions.addItem(player, AWAKEN_POWER.get(newClassId), 1, true);
			ItemFunctions.addItem(player, LEGACY_WEAPONS.get(previousClassId), 1, true);
		}
		else
		{
			if (previousClassId >= 139)
			{
				player.getInventory().destroyItemByItemId(CLOAK_DUAL_CLASS.get(previousClassId), 1);
				player.getWarehouse().destroyItemByItemId(CLOAK_DUAL_CLASS.get(previousClassId), 1);
			}
			
			ItemFunctions.addItem(player, CLOAK_DUAL_CLASS.get(newClassId), 1, true);
		}
	}
	
	/**
	 * Method giveGiantEssences.
	 * @param player Player
	 * @param onlyCalculateCount boolean
	 * @return int
	 */
	public int giveGiantEssences(Player player, boolean onlyCalculateCount)
	{
		List<Integer> enchantedSkills = new FastList<>();
		int count = 0;
		
		for (Skill skill : player.getAllSkills())
		{
			if ((SkillTreeTable.isEnchantable(skill) != 0) && (player.getSkillDisplayLevel(skill.getId()) > 99))
			{
				int skillLvl = skill.getDisplayLevel();
				int elevel = skillLvl % 100;
				EnchantSkillLearn sl = SkillTreeTable.getSkillEnchant(skill.getId(), skillLvl);
				
				if (sl != null)
				{
					if (sl.getMaxLevel() == 15)
					{
						elevel = Math.min(count15T.length, elevel);
						count += count15T[elevel];
					}
					else
					{
						elevel = Math.min(count30T.length, elevel);
						count += count30T[elevel];
					}
				}
			}
			
			enchantedSkills.add(Integer.valueOf(skill.getId()));
		}
		
		if (!onlyCalculateCount)
		{
			if (count > 0)
			{
				for (int i = 0; i < enchantedSkills.size(); i++)
				{
					player.removeSkillById(enchantedSkills.get(i));
					player.addSkill(SkillTable.getInstance().getInfo(enchantedSkills.get(i), SkillTable.getInstance().getBaseLevel(enchantedSkills.get(i))), true);
				}
				
				ItemFunctions.addItem(player, ESSENCE_OF_THE_LESSER_GIANTS, count, true);
			}
		}
		
		return count;
	}
	
	private void onTransferOnlyRemoveSkills(Player player)
	{
		int previousClassId = player.getClassId().getId();
		int newClassId = _CA.get(previousClassId);
		boolean delete = false;
		
		if (Config.ALT_DELETE_AWAKEN_SKILL_FROM_DB)
		{
			delete = true;
		}
		
		List<Integer> skillsToMantain = SkillAcquireHolder.getInstance().getMaintainSkillOnAwake(previousClassId, newClassId);
		List<Integer> allSkillsId = SkillAcquireHolder.getInstance().getAllClassSkillId();
		
		for (Skill skl : player.getAllSkills())
		{
			if (allSkillsId.contains(skl.getId()))
			{
				player.removeSkill(skl, delete);
			}
		}
		
		for (int skillId : skillsToMantain)
		{
			int skillLv = SkillTable.getInstance().getBaseLevel(skillId);
			Skill newSkill = SkillTable.getInstance().getInfo(skillId, skillLv);
			player.addSkill(newSkill, true);
		}
		
		for (int alterSkill : ALTER_SKILLS.get(newClassId))
		{
			int skillLv = SkillTable.getInstance().getBaseLevel(alterSkill);
			Skill newSkillAlter = SkillTable.getInstance().getInfo(alterSkill, skillLv);
			player.addSkill(newSkillAlter, true);
		}
		
		player.sendSkillList();
	}
	
	public void onTransferOnlyRemoveSkills(Player player, int toFinalClass, int baseKeepSkills)
	{
		boolean delete = false;
		
		if (Config.ALT_DELETE_AWAKEN_SKILL_FROM_DB)
		{
			delete = true;
		}
		
		List<Integer> allSkillsId = SkillAcquireHolder.getInstance().getAllClassSkillId();
		List<Integer> skillsToMantain = SkillAcquireHolder.getInstance().getMaintainSkillOnAwake(baseKeepSkills, toFinalClass);
		
		for (Skill skl : player.getAllSkills())
		{
			if (allSkillsId.contains(skl.getId()))
			{
				player.removeSkill(skl, delete);
			}
		}
		
		for (int skillId : skillsToMantain)
		{
			int skillLv = SkillTable.getInstance().getBaseLevel(skillId);
			Skill newSkill = SkillTable.getInstance().getInfo(skillId, skillLv);
			player.addSkill(newSkill, true);
		}
		
		for (int alterSkill : ALTER_SKILLS.get(toFinalClass))
		{
			int skillLv = SkillTable.getInstance().getBaseLevel(alterSkill);
			Skill newSkillAlter = SkillTable.getInstance().getInfo(alterSkill, skillLv);
			player.addSkill(newSkillAlter, true);
		}
		
		player.sendSkillList();
	}
	
	public void checkAwakenPlayerSkills(Player player) // For check on subclass change and logon
	{
		int classId = player.getActiveClassId();
		boolean delete = false;
		
		if (Config.ALT_DELETE_AWAKEN_SKILL_FROM_DB)
		{
			delete = true;
		}
		
		List<Integer> SkillsCheck = new ArrayList<>();
		List<Integer> SkillsToRemove = new ArrayList<>();
		List<Integer> allSkillsId = SkillAcquireHolder.getInstance().getAllClassSkillId();
		SkillsCheck.addAll(SkillAcquireHolder.getInstance().getAwakenGeneralKeepSkillList());
		SkillsCheck.addAll(SkillAcquireHolder.getInstance().getAwakenClassSkillForCheck(classId));
		SkillsCheck.addAll(SkillAcquireHolder.getInstance().getAllAwakenSkillsByClass(classId));
		
		if (player.getTransformation() == 0) // if the character log on with a transformation, do not remove any skill
		{
			for (Skill skl : player.getAllSkills())
			{
				if (!SkillsCheck.contains(skl.getId()) && allSkillsId.contains(skl.getId()))
				{
					SkillsToRemove.add(skl.getId());
				}
			}
		}
		else
		{
			for (Skill skl : player.getAllSkills())
			{
				if (!SkillsCheck.contains(skl.getId()) && SkillsCheck.contains(skl.getId()))
				{
					SkillsToRemove.add(skl.getId());
				}
			}
		}
		
		player.removeSkills(SkillsToRemove, delete);
		
		for (int alterSkill : ALTER_SKILLS.get(classId))
		{
			int skillLv = SkillTable.getInstance().getBaseLevel(alterSkill);
			Skill newSkillAlter = SkillTable.getInstance().getInfo(alterSkill, skillLv);
			player.addSkill(newSkillAlter, true);
		}
		
		player.sendSkillList();
	}
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 * @see lineage2.gameserver.listener.actor.player.OnPlayerEnterListener#onPlayerEnter(Player)
	 */
	@Override
	public void onPlayerEnter(Player player)
	{
		if (player.getClassId().level() < 3)
		{
			return;
		}
		
		if (player.getLevel() < 85)
		{
			return;
		}
		
		if (player.isAwaking())
		{
			return;
		}
		
		if (player.getActiveSubClass().isBase() || player.getActiveSubClass().isDual())
		{
			player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
			player.sendPacket(new ExCallToChangeClass(_CA.get(player.getClassId().getId()), true));
		}
	}
	
	public static String getAwakeningRequestVar(ClassId classId)
	{
		return "awakening_request_" + classId.getId();
	}
}
