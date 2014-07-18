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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javolution.util.FastList;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
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
 * @version $Revision: 1.0 $
 */
public class AwakingManager implements OnPlayerEnterListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AwakingManager.class);
	/**
	 * Field _instance.
	 */
	private static AwakingManager _instance;
	/**
	 * Field ESSENCE_OF_THE_LESSER_GIANTS. (value is 30306)
	 */
	private static final int ESSENCE_OF_THE_LESSER_GIANTS = 30306;
	/**
	 * Field _CA.
	 */
	private static TIntIntHashMap _CA = new TIntIntHashMap(69);
	/**
	 * Field _LegacyWeapon.
	 */
	private static TIntIntHashMap _LegacyWeapon = new TIntIntHashMap(36);
	/**
	 * Field _AwakenPower.
	 */
	private static TIntIntHashMap _AwakenPower = new TIntIntHashMap(8);
	/**
	 * Field _AwakenPower.
	 */
	private static TIntIntHashMap _CloakDualClass = new TIntIntHashMap(8);

	/**
	 * Field _AlterSigel.
	 */
	private static final Integer[] _AlterSigel =
	{
		10250,
		10249
	};

	/**
	 * Field _AlterTyrr.
	 */
	private static final Integer[] _AlterTyrr =
	{
		10500,
		10499
	};

	/**
	 * Field _AlterOthell.
	 */
	private static final Integer[] _AlterOthell =
	{
		10750,
		10749
	};

	/**
	 * Field _AlterYul.
	 */
	private static final Integer[] _AlterYul =
	{
		11000,
		10999
	};

	/**
	 * Field _AlterFeoh.
	 */
	private static final Integer[] _AlterFeoh =
	{
		11249,
		11247
	};

	/**
	 * Field _AlterIss.
	 */
	private static final Integer[] _AlterIss =
	{
		11750,
		11749
	};

	/**
	 * Field _AlterWynn.
	 */
	private static final Integer[] _AlterWynn =
	{
		11500,
		11499
	};

	/**
	 * Field _AlterAerore.
	 */
	private static final Integer[] _AlterAerore =
	{
		12000,
		11999
	};

	private static final HashMap<Integer, Integer[]> _AlterSkills = new HashMap<>();
	/**
	 * Field count30T.
	 */
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
	/**
	 * Field count15T.
	 */
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
	public void load()
	{
		if (Config.AWAKING_FREE)
		{
			CharListenerList.addGlobal(this);
		}
		_CA.clear();
		_AlterSkills.clear();
		_LegacyWeapon.clear();
		_AwakenPower.clear();
		_CloakDualClass.clear();
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
		_AwakenPower.put(139, 32264);
		_AwakenPower.put(148, 32264);
		_AwakenPower.put(149, 32264);
		_AwakenPower.put(150, 32264);
		_AwakenPower.put(151, 32264);

		_AwakenPower.put(140, 32265);
		_AwakenPower.put(152, 32265);
		_AwakenPower.put(153, 32265);
		_AwakenPower.put(154, 32265);
		_AwakenPower.put(155, 32265);
		_AwakenPower.put(156, 32265);
		_AwakenPower.put(157, 32265);

		_AwakenPower.put(141, 32266);
		_AwakenPower.put(158, 32266);
		_AwakenPower.put(159, 32266);
		_AwakenPower.put(160, 32266);
		_AwakenPower.put(161, 32266);

		_AwakenPower.put(142, 32267);
		_AwakenPower.put(162, 32267);
		_AwakenPower.put(163, 32267);
		_AwakenPower.put(164, 32267);
		_AwakenPower.put(165, 32267);

		_AwakenPower.put(143, 32268);
		_AwakenPower.put(166, 32268);
		_AwakenPower.put(167, 32268);
		_AwakenPower.put(168, 32268);
		_AwakenPower.put(169, 32268);
		_AwakenPower.put(170, 32268);

		_AwakenPower.put(144, 32269);
		_AwakenPower.put(171, 32269);
		_AwakenPower.put(172, 32269);
		_AwakenPower.put(173, 32269);
		_AwakenPower.put(174, 32269);
		_AwakenPower.put(175, 32269);

		_AwakenPower.put(145, 32270);
		_AwakenPower.put(176, 32270);
		_AwakenPower.put(177, 32270);
		_AwakenPower.put(178, 32270);

		_AwakenPower.put(146, 32271);
		_AwakenPower.put(179, 32271);
		_AwakenPower.put(180, 32271);
		_AwakenPower.put(181, 32271);

		_CloakDualClass.put(139, 30310);
		_CloakDualClass.put(148, 30310);
		_CloakDualClass.put(149, 30310);
		_CloakDualClass.put(150, 30310);
		_CloakDualClass.put(151, 30310);

		_CloakDualClass.put(140, 30311);
		_CloakDualClass.put(152, 30311);
		_CloakDualClass.put(153, 30311);
		_CloakDualClass.put(154, 30311);
		_CloakDualClass.put(155, 30311);
		_CloakDualClass.put(156, 30311);
		_CloakDualClass.put(157, 30311);

		_CloakDualClass.put(141, 30312);
		_CloakDualClass.put(158, 30312);
		_CloakDualClass.put(159, 30312);
		_CloakDualClass.put(160, 30312);
		_CloakDualClass.put(161, 30312);

		_CloakDualClass.put(142, 30313);
		_CloakDualClass.put(162, 30313);
		_CloakDualClass.put(163, 30313);
		_CloakDualClass.put(164, 30313);
		_CloakDualClass.put(165, 30313);

		_CloakDualClass.put(143, 30314);
		_CloakDualClass.put(166, 30314);
		_CloakDualClass.put(167, 30314);
		_CloakDualClass.put(168, 30314);
		_CloakDualClass.put(169, 30314);
		_CloakDualClass.put(170, 30314);

		_CloakDualClass.put(144, 30315);
		_CloakDualClass.put(171, 30315);
		_CloakDualClass.put(172, 30315);
		_CloakDualClass.put(173, 30315);
		_CloakDualClass.put(174, 30315);
		_CloakDualClass.put(175, 30315);

		_CloakDualClass.put(145, 30316);
		_CloakDualClass.put(176, 30316);
		_CloakDualClass.put(177, 30316);
		_CloakDualClass.put(178, 30316);

		_CloakDualClass.put(146, 30317);
		_CloakDualClass.put(179, 30317);
		_CloakDualClass.put(180, 30317);
		_CloakDualClass.put(181, 30317);

		_LegacyWeapon.put(88, 33717);
		_LegacyWeapon.put(89, 33718);
		_LegacyWeapon.put(90, 33719);
		_LegacyWeapon.put(91, 33720);
		_LegacyWeapon.put(92, 33721);
		_LegacyWeapon.put(93, 33722);
		_LegacyWeapon.put(94, 33723);
		_LegacyWeapon.put(95, 33724);
		_LegacyWeapon.put(96, 33725);
		_LegacyWeapon.put(97, 33726);
		_LegacyWeapon.put(98, 33727);
		_LegacyWeapon.put(99, 33728);
		_LegacyWeapon.put(100, 33729);
		_LegacyWeapon.put(101, 33730);
		_LegacyWeapon.put(102, 33731);
		_LegacyWeapon.put(103, 33732);
		_LegacyWeapon.put(104, 33733);
		_LegacyWeapon.put(105, 33734);
		_LegacyWeapon.put(106, 33735);
		_LegacyWeapon.put(107, 33736);
		_LegacyWeapon.put(108, 33737);
		_LegacyWeapon.put(109, 33738);
		_LegacyWeapon.put(110, 33739);
		_LegacyWeapon.put(111, 33740);
		_LegacyWeapon.put(112, 33741);
		_LegacyWeapon.put(113, 33742);
		_LegacyWeapon.put(114, 33743);
		_LegacyWeapon.put(115, 33744);
		_LegacyWeapon.put(116, 33745);
		_LegacyWeapon.put(117, 33746);
		_LegacyWeapon.put(118, 33747);
		_LegacyWeapon.put(131, 33761);
		_LegacyWeapon.put(132, 33762);
		_LegacyWeapon.put(133, 33763);
		_LegacyWeapon.put(134, 33763);
		_LegacyWeapon.put(136, 33765);

		_AlterSkills.put(Integer.valueOf(139), _AlterSigel);
		_AlterSkills.put(Integer.valueOf(148), _AlterSigel);
		_AlterSkills.put(Integer.valueOf(149), _AlterSigel);
		_AlterSkills.put(Integer.valueOf(150), _AlterSigel);
		_AlterSkills.put(Integer.valueOf(151), _AlterSigel);

		_AlterSkills.put(Integer.valueOf(140), _AlterTyrr);
		_AlterSkills.put(Integer.valueOf(152), _AlterTyrr);
		_AlterSkills.put(Integer.valueOf(153), _AlterTyrr);
		_AlterSkills.put(Integer.valueOf(154), _AlterTyrr);
		_AlterSkills.put(Integer.valueOf(155), _AlterTyrr);
		_AlterSkills.put(Integer.valueOf(156), _AlterTyrr);
		_AlterSkills.put(Integer.valueOf(157), _AlterTyrr);

		_AlterSkills.put(Integer.valueOf(141), _AlterOthell);
		_AlterSkills.put(Integer.valueOf(158), _AlterOthell);
		_AlterSkills.put(Integer.valueOf(159), _AlterOthell);
		_AlterSkills.put(Integer.valueOf(160), _AlterOthell);
		_AlterSkills.put(Integer.valueOf(161), _AlterOthell);

		_AlterSkills.put(Integer.valueOf(142), _AlterYul);
		_AlterSkills.put(Integer.valueOf(162), _AlterYul);
		_AlterSkills.put(Integer.valueOf(163), _AlterYul);
		_AlterSkills.put(Integer.valueOf(164), _AlterYul);
		_AlterSkills.put(Integer.valueOf(165), _AlterYul);

		_AlterSkills.put(Integer.valueOf(143), _AlterFeoh);
		_AlterSkills.put(Integer.valueOf(166), _AlterFeoh);
		_AlterSkills.put(Integer.valueOf(167), _AlterFeoh);
		_AlterSkills.put(Integer.valueOf(168), _AlterFeoh);
		_AlterSkills.put(Integer.valueOf(169), _AlterFeoh);
		_AlterSkills.put(Integer.valueOf(170), _AlterFeoh);

		_AlterSkills.put(Integer.valueOf(144), _AlterIss);
		_AlterSkills.put(Integer.valueOf(171), _AlterIss);
		_AlterSkills.put(Integer.valueOf(172), _AlterIss);
		_AlterSkills.put(Integer.valueOf(173), _AlterIss);
		_AlterSkills.put(Integer.valueOf(174), _AlterIss);
		_AlterSkills.put(Integer.valueOf(175), _AlterIss);

		_AlterSkills.put(Integer.valueOf(145), _AlterWynn);
		_AlterSkills.put(Integer.valueOf(176), _AlterWynn);
		_AlterSkills.put(Integer.valueOf(177), _AlterWynn);
		_AlterSkills.put(Integer.valueOf(178), _AlterWynn);

		_AlterSkills.put(Integer.valueOf(146), _AlterAerore);
		_AlterSkills.put(Integer.valueOf(179), _AlterAerore);
		_AlterSkills.put(Integer.valueOf(180), _AlterAerore);
		_AlterSkills.put(Integer.valueOf(181), _AlterAerore);
		_log.info("AwakingManager: Loaded 8 Awaking class for " + _CA.size() + " normal class. Loaded " + _LegacyWeapon.size() + " Legacy Weapons.");
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
	 * Method SendReqToAwaking.
	 * @param player Player, int toClassId
	 * @param toClassId
	 */
	public void SendReqToAwaking(Player player, int toClassId)
	{
		if (player.getClassId().level() < 3)
		{
			return;
		}
		player.sendPacket(new ExChangeToAwakenedClass(toClassId));
		return;
	}

	/**
	 * Method onStartQuestAccept.
	 * @param player Player
	 */
	public void onStartQuestAccept(Player player)
	{
		player.teleToLocation(-114708, 243918, -7968);
		player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
		return;
	}

	/**
	 * Method SetAwakingId.
	 * @param player Player
	 */
	public void SetAwakingId(Player player)
	{
		int _oldId = player.getClassId().getId();
		giveGiantEssences(player, false);
		if (Config.ALT_DELETE_SKILL_PROF) // its important part of correct skill assignment this If sentence, removed from player.java
		{
			onTransferOnlyRemoveSkills(player);
		}
		player.setClassId(_CA.get(_oldId), false, false);
		player.broadcastUserInfo();
		player.broadcastPacket(new SocialAction(player.getObjectId(), 20));
		giveItems(player, _oldId, _CA.get(_oldId));
		getRaceSkill(player);
	}

	/**
	 * Method SetAwakingId.
	 * @param player Player, int toClass, Int classIdSkills
	 * @param toClass
	 * @param classIdSkills
	 */
	public void SetAwakingId(Player player, int toClass, int classIdSkills)
	{
		giveGiantEssences(player, false);
		giveItems(player, player.getActiveClassId(), toClass);
		if (Config.ALT_DELETE_SKILL_PROF) // its important part of correct skill assignment this If sentence, removed from player.java
		{
			onTransferOnlyRemoveSkills(player, toClass, classIdSkills);
		}
		player.setClassId(toClass, false, false);
		player.broadcastUserInfo();
		player.broadcastPacket(new SocialAction(player.getObjectId(), 20));
		getRaceSkill(player);
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
		if (player.getClassId().isOfLevel(ClassLevel.Awaking))
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
		if (!player.getSubClassList().isDoubleClassActive())
		{
			ItemFunctions.addItem(player, _AwakenPower.get(newClassId), 1, true);
			ItemFunctions.addItem(player, _LegacyWeapon.get(previousClassId), 1, true);
		}
		else
		{
			if (previousClassId >= 139)
			{
				player.getInventory().destroyItemByItemId(_CloakDualClass.get(previousClassId), 1);
				player.getWarehouse().destroyItemByItemId(_CloakDualClass.get(previousClassId), 1);
			}
			ItemFunctions.addItem(player, _CloakDualClass.get(newClassId), 1, true);
		}
	}

	/**
	 * Method giveDeletedSkillList.
	 * @param player Player
	 * @return String
	 */
	public String giveDeletedSkillList(Player player)
	{
		int newClassId = _CA.get(player.getClassId().getId());
		Collection<SkillLearn> skills = SkillAcquireHolder.getInstance().getAvailableAllSkillsForDellet(player, newClassId);
		StringBuilder tmp = new StringBuilder();
		for (SkillLearn s : skills)
		{
			Skill sk = SkillTable.getInstance().getInfo(s.getId(), s.getLevel());
			if (sk.isRelationSkill())
			{
				final int[] _ss = sk.getRelationSkills();
				if (_ss != null)
				{
					for (int _k : _ss)
					{
						SkillTable.getInstance().getInfo(_k, SkillTable.getInstance().getBaseLevel(_k));
					}
				}
			}
		}
		return tmp.toString();
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
		for (int alterSkill : _AlterSkills.get(newClassId))
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
		for (int alterSkill : _AlterSkills.get(toFinalClass))
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
		if (player.getTransformation() == 0)// if the character log on with a transformation, do not remove any skill
		{
			for (Skill skl : player.getAllSkills())
			{
				if (!SkillsCheck.contains(skl.getId()) && allSkillsId.contains(skl.getId()))
				{
					// player.removeSkill(skl,delete);
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
					// player.removeSkill(skl,delete);
					SkillsToRemove.add(skl.getId());
				}
			}
		}
		player.removeSkills(SkillsToRemove, delete);
		for (int alterSkill : _AlterSkills.get(classId))
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
		if (player.getActiveSubClass().isBase() || player.getActiveSubClass().isDouble())
		{
			player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
			player.sendPacket(new ExCallToChangeClass(_CA.get(player.getClassId().getId()), true));
		}
	}
}
