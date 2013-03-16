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

import gnu.trove.map.hash.TIntIntHashMap;

import java.util.ArrayList;
import java.util.Collection;
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
	private static TIntIntHashMap _CA = new TIntIntHashMap(36);
	
	/*
	 * public int[] autoRemoveSkills = {
	 * 2,10,13,15,21,27,33,44,46,58,61,67,69,70,72,78,82,94,97,102,103,112,122,123,129,139,141,143,196,213,222,223,227,230,231,234,254,262,279,287,288,289,291,296,299,302,312,314,315,320,322,323,324,328,329,334,335,336,338,339,340,341,342,346,348,350,353,355,356,357,
	 * 361,368,405,412,416,417,419,420,421
	 * ,424,428,429,430,431,435,438,439,440,445,448,450,454,456,457,458,459,460,461,462,466,467,470,471,482,483,484,485,493,494,495,499,500,502,504,505,510,513,514,515,517,519,520,527,528,531,532,533,534,535,536,537,538,579,620,621,623,624,625,626,755,756,757,758,759
	 * ,760,761,762,763,764,765,766,767,768
	 * ,769,770,771,772,773,774,776,777,778,784,786,788,790,791,792,810,818,819,820,825,826,827,828,829,830,831,832,834,836,912,913,914,915,917,918,919,920,922,923,924,929,930,931,939,945,946,947,948,949,984,985,1018,1020,1044,1049,1071,1072,1074,1078,1083,1095,1102
	 * ,1108,1129,1151,1154,1170,1182,1210,1213,1223,1224,1243,1250,1256,1257,1258,1268,1279,1280,1281,1285,1286,1287,1288,1289,1300,1304,1305,1307,1328,1329,1330,1334,1340,1346,1347,1348,1350,1351,1366,1367,1380,1398,1399,1400, /* 1405,
	 */// TODO: Awakening use divine inspiration or not ???
	/*
	 * 1416,1424,1426,1429,1435,1441,1443,1444,1445,1460,1467,1468,1470,1478,1479,1480,1482,1483,1486,1487,1493,1494,1495,1496,1497,1498,1499,1500,1501,1502,1503,1504,1505,1506,1507,1509,1510,1514,1515,1516,1517,1518,1519,1520,1526,1532,1535,1536,1537,1538,1539,1540,1542,1543,1547,1551,1552,1557,1560
	 * ,1598,1599,1600 };
	 */
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
	 * Field count30.
	 */
	private static final int[] count30 =
	{
		0,
		0,
		0,
		0,
		1,
		1,
		1,
		1,
		2,
		2,
		2,
		3,
		3,
		3,
		4,
		4,
		5,
		6,
		6,
		7,
		8,
		9,
		9,
		10,
		11,
		13,
		14,
		15,
		19,
		21,
		25
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
	 * Field count15.
	 */
	private static final int[] count15 =
	{
		0,
		0,
		0,
		0,
		1,
		1,
		1,
		1,
		2,
		2,
		3,
		3,
		3,
		6,
		8,
		11
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
		/***************************************************************************************************
		 * 139 H_PhoenixKnight, H_HellKnight, E_EvaTemplar, DE_ShillienTemplar 140 H_Duelist, H_Dreadnought, O_Titan, O_GrandKhauatari, D_Maestro, K_Male_Doombringer 141 H_Adventurer, E_WindRider, DE_GhostHunter, D_FortuneSeeker, 142 H_Sagittarius, E_MoonlightSentinel, DE_GhostSentinel,
		 * K_Female_Trickster 143 H_Archmage, H_Soultaker, E_MysticMuse, DE_StormScreamer, K_Male_Soulhound, K_Female_Soulhound 144 H_Hierophant, E_SwordMuse, DE_SpectralDancer, O_Dominator, O_Doomcryer, K_Judicator 145 H_ArcanaLord, E_ElementalMaster, DE_SpectralMaster 146 H_Cardinal, E_EvaSaint,
		 * DE_ShillienSaint
		 ****************************************************************************************************/
		_CA.put(90, 139);
		_CA.put(91, 139);
		_CA.put(99, 139);
		_CA.put(106, 139);
		_CA.put(89, 140);
		_CA.put(88, 140);
		_CA.put(113, 140);
		_CA.put(114, 140);
		_CA.put(118, 140);
		_CA.put(131, 140);
		_CA.put(93, 141);
		_CA.put(101, 141);
		_CA.put(108, 141);
		_CA.put(117, 141);
		_CA.put(92, 142);
		_CA.put(102, 142);
		_CA.put(109, 142);
		_CA.put(134, 142);
		_CA.put(94, 143);
		_CA.put(95, 143);
		_CA.put(103, 143);
		_CA.put(110, 143);
		_CA.put(132, 143);
		_CA.put(133, 143);
		_CA.put(98, 144);
		_CA.put(116, 144);
		_CA.put(115, 144);
		_CA.put(100, 144);
		_CA.put(107, 144);
		_CA.put(136, 144);
		_CA.put(96, 145);
		_CA.put(104, 145);
		_CA.put(111, 145);
		_CA.put(97, 146);
		_CA.put(105, 146);
		_CA.put(112, 146);
		
		_log.info("AwakingManager: Loaded 8 Awaking class for " + _CA.size() + " normal class.");
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
		player.setClassId(_CA.get(_oldId), false, false);
		giveGiantEssences(player, false);
		player.broadcastUserInfo(true);
		player.broadcastPacket(new SocialAction(player.getObjectId(), (_CA.get(_oldId) - 119)));
		AwakingRemoveSkills(player);
		giveItems(player);
	}
	
	/**
	 * Method giveItems.
	 * @param player Player
	 */
	public void giveItems(Player player)
	{
		switch (player.getClassId().getId())
		{
			case 139:
				ItemFunctions.addItem(player, 32264, 1, true);
				ItemFunctions.addItem(player, 33735, 1, true);
				break;
			case 140:
				ItemFunctions.addItem(player, 32265, 1, true);
				ItemFunctions.addItem(player, 33742, 1, true);
				break;
			case 141:
				ItemFunctions.addItem(player, 32266, 1, true);
				ItemFunctions.addItem(player, 33722, 1, true);
				break;
			case 142:
				ItemFunctions.addItem(player, 32267, 1, true);
				ItemFunctions.addItem(player, 33763, 1, true);
				break;
			case 143:
				ItemFunctions.addItem(player, 32268, 1, true);
				ItemFunctions.addItem(player, 33732, 1, true);
				break;
			case 144:
				ItemFunctions.addItem(player, 32270, 1, true);
				ItemFunctions.addItem(player, 33727, 1, true);
				break;
			case 145:
				ItemFunctions.addItem(player, 32269, 1, true);
				ItemFunctions.addItem(player, 33740, 1, true);
				break;
			case 146:
				ItemFunctions.addItem(player, 32271, 1, true);
				ItemFunctions.addItem(player, 33726, 1, true);
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
						if (player.isTautiClient())
						{
							elevel = Math.min(count15T.length, elevel);
							count += count15T[elevel];
						}
						else
						{
							elevel = Math.min(count15.length, elevel);
							count += count15[elevel];
						}
					}
					else
					{
						if (player.isTautiClient())
						{
							elevel = Math.min(count30T.length, elevel);
							count += count30T[elevel];
						}
						else
						{
							elevel = Math.min(count30.length, elevel);
							count += count30[elevel];
						}
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
	
	public void AwakingRemoveSkills(Player player)
	{
		List<Integer> skillsRemove = new ArrayList<>();
		switch (player.getClassId().getId())
		// This Checks if any of the skills of other class transfer are in character skill tree On Relogin
		{
			case 139:// Sigel Knight
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.HELL_KNIGHT.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.PHOENIX_KNIGHT.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.EVAS_TEMPLAR.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.SHILLIEN_TEMPLAR.getId()));
				break;
			case 140:// Tyrr Warrior
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.TITAN.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.GRAND_KHAVATARI.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.DREADNOUGHT.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.DUELIST.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.DOOMBRINGER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.MAESTRO.getId()));
				break;
			case 141:// Othell Rogue
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.WIND_RIDER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.GHOST_HUNTER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.ADVENTURER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.FORTUNE_SEEKER.getId()));
				break;
			case 142:// Yul Archer
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.MOONLIGHT_SENTINEL.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.GHOST_SENTINEL.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.SAGITTARIUS.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.TRICKSTER.getId()));
				break;
			case 143:// Feoh Wizard
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.MYSTIC_MUSE.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.STORM_SCREAMER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.ARCHMAGE.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.SOULTAKER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.F_SOUL_HOUND.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.M_SOUL_HOUND.getId()));
			case 144:// Iss Enchanter
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.DOMINATOR.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.DOOMCRYER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.SPECTRAL_DANCER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.SWORD_MUSE.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.HIEROPHANT.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.JUDICATOR.getId())); // Not Implemented yet, need functional dual class system
				break;
			case 145:// Wynn Summoner
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.ELEMENTAL_MASTER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.SPECTRAL_MASTER.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.ARCANA_LORD.getId()));
			case 146:// Aerore Healer
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.SHILLIEN_SAINT.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.EVAS_SAINT.getId()));
				skillsRemove.addAll(SkillAcquireHolder.getInstance().getSkillRemoveByClass(ClassId.CARDINAL.getId()));
		}
		for (Integer skillId : skillsRemove)
		{
			int level = player.getSkillLevel(skillId);
			if (level > 0)
			{
				Skill skill = SkillTable.getInstance().getInfo(skillId, level);
				_log.info(getClass().getSimpleName() + ":" + player.getName() + ":remove skill:" + skill.getName() + " " + skill.getId());
				// TODO UPDATE TO removeSkill(skill, true) if all is ok
				player.removeSkill(skill);
			}
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
	
	/**
	 * Method getRaceSkill.
	 * @param player Player
	 * @return Skill
	 */
	/*
	 * NO MORE USED !!! public Skill getRaceSkill(Player player) { int race = player.getRace().ordinal(); Skill skill = null; if (player.getClassId().isOfLevel(ClassLevel.Awaking)) { switch (race) { case 0: skill = SkillTable.getInstance().getInfo(248, 6); player.addSkill(skill); break; case 1:
	 * skill = SkillTable.getInstance().getInfo(248, 6); player.addSkill(skill); break; case 2: skill = SkillTable.getInstance().getInfo(248, 6); player.addSkill(skill); break; case 3: skill = SkillTable.getInstance().getInfo(248, 6); player.addSkill(skill); break; case 4: skill =
	 * SkillTable.getInstance().getInfo(248, 6); player.addSkill(skill); break; case 5: skill = SkillTable.getInstance().getInfo(248, 6); player.addSkill(skill); break; } } else { player.sendActionFailed(); } player.updateStats(); return null; }
	 */
}
