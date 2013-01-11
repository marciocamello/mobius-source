/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.instancemanager;

import gnu.trove.map.hash.TIntIntHashMap;

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

public class AwakingManager implements OnPlayerEnterListener
{
	private static final Logger _log = LoggerFactory.getLogger(AwakingManager.class);
	private static AwakingManager _instance;
	private static final int ESSENCE_OF_THE_LESSER_GIANTS = 30306;
	private static TIntIntHashMap _CA = new TIntIntHashMap(36);
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
	
	public void load()
	{
		if (Config.AWAKING_FREE)
		{
			CharListenerList.addGlobal(this);
		}
		_CA.clear();
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
	
	public void SendReqToStartQuest(Player player)
	{
		if (player.getClassId().level() < 3)
		{
			return;
		}
		int newClass = _CA.get(player.getClassId().getId());
		player.sendPacket(new ExCallToChangeClass(newClass, false));
	}
	
	public int childOf(ClassId oldClass)
	{
		int newClass = _CA.get(oldClass.getId());
		return newClass;
	}
	
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
	
	public void onStartQuestAccept(Player player)
	{
		player.teleToLocation(-114708, 243918, -7968);
		player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
		return;
	}
	
	public void SetAwakingId(Player player)
	{
		int _oldId = player.getClassId().getId();
		player.setClassId(_CA.get(_oldId), false, false);
		giveGiantEssences(player, false);
		player.broadcastUserInfo(true);
		player.broadcastPacket(new SocialAction(player.getObjectId(), (_CA.get(_oldId) - 119)));
		giveItems(player);
		getRaceSkill(player);
	}
	
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
	
	public Skill getRaceSkill(Player player)
	{
		int race = player.getRace().ordinal();
		Skill skill = null;
		if (player.getClassId().isOfLevel(ClassLevel.Awaking))
		{
			switch (race)
			{
				case 0:
					skill = SkillTable.getInstance().getInfo(1901, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1902, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1903, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1904, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 1:
					skill = SkillTable.getInstance().getInfo(1905, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1906, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1907, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1908, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 2:
					skill = SkillTable.getInstance().getInfo(1909, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1910, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1911, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1912, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1913, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 3:
					skill = SkillTable.getInstance().getInfo(1914, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1915, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1916, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1917, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
				case 4:
					skill = SkillTable.getInstance().getInfo(1919, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1920, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1921, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1922, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(19088, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(19089, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(19090, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(172, 11);
					player.addSkill(skill);
					break;
				case 5:
					skill = SkillTable.getInstance().getInfo(1923, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1924, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1925, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1926, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(1954, 1);
					player.addSkill(skill);
					skill = SkillTable.getInstance().getInfo(248, 6);
					player.addSkill(skill);
					break;
			}
		}
		else
		{
			player.sendActionFailed();
		}
		player.updateStats();
		return null;
	}
}
