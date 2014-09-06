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
package npc.model;

import java.util.HashMap;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import gnu.trove.map.hash.TIntIntHashMap;

public final class PowerfulDeviceInstance extends NpcInstance
{
	private static final TIntIntHashMap _NPC = new TIntIntHashMap(34);
	private static final TIntIntHashMap _DESTINYCHANGECLASSES = new TIntIntHashMap(35);
	private static final HashMap<Integer, String> _NAMECLASSES = new HashMap<>();
	private static final int SCROLL_OF_AFTERLIFE = 17600;
	private final int sp = Rnd.get(10000000);
	private int NextClassId = 0;
	
	public PowerfulDeviceInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_NPC.clear();
		// sigelKnights
		_NPC.put(33397, 148);
		_NPC.put(33397, 149);
		_NPC.put(33397, 150);
		_NPC.put(33397, 151);
		// tyrrWarrior
		_NPC.put(33398, 152);
		_NPC.put(33398, 153);
		_NPC.put(33398, 154);
		_NPC.put(33398, 155);
		_NPC.put(33398, 156);
		_NPC.put(33398, 157);
		// othellRogue
		_NPC.put(33399, 158);
		_NPC.put(33399, 159);
		_NPC.put(33399, 160);
		_NPC.put(33399, 161);
		// yulArcher
		_NPC.put(33400, 162);
		_NPC.put(33400, 163);
		_NPC.put(33400, 164);
		_NPC.put(33400, 165);
		// feohWizard
		_NPC.put(33401, 166);
		_NPC.put(33401, 167);
		_NPC.put(33401, 168);
		_NPC.put(33401, 169);
		_NPC.put(33401, 170);
		// issEnchanter
		_NPC.put(33402, 171);
		_NPC.put(33402, 172);
		_NPC.put(33402, 173);
		_NPC.put(33402, 174);
		_NPC.put(33402, 175);
		// wynnSummoner
		_NPC.put(33403, 176);
		_NPC.put(33403, 177);
		_NPC.put(33403, 178);
		// aeoreHealer
		_NPC.put(33404, 179);
		_NPC.put(33404, 180);
		_NPC.put(33404, 181);
		// Knights
		_DESTINYCHANGECLASSES.put(148, 90);
		_DESTINYCHANGECLASSES.put(149, 91);
		_DESTINYCHANGECLASSES.put(150, 99);
		_DESTINYCHANGECLASSES.put(151, 106);
		// Warriors
		_DESTINYCHANGECLASSES.put(152, 88);
		_DESTINYCHANGECLASSES.put(153, 89);
		_DESTINYCHANGECLASSES.put(154, 113);
		_DESTINYCHANGECLASSES.put(155, 114);
		_DESTINYCHANGECLASSES.put(156, 118);
		_DESTINYCHANGECLASSES.put(157, 131);
		// Daggers
		_DESTINYCHANGECLASSES.put(158, 93);
		_DESTINYCHANGECLASSES.put(159, 101);
		_DESTINYCHANGECLASSES.put(160, 108);
		_DESTINYCHANGECLASSES.put(161, 117);
		// Archers
		_DESTINYCHANGECLASSES.put(162, 92);
		_DESTINYCHANGECLASSES.put(163, 102);
		_DESTINYCHANGECLASSES.put(164, 109);
		_DESTINYCHANGECLASSES.put(165, 134);
		// Wizards
		_DESTINYCHANGECLASSES.put(166, 94);
		_DESTINYCHANGECLASSES.put(167, 95);
		_DESTINYCHANGECLASSES.put(168, 103);
		_DESTINYCHANGECLASSES.put(169, 110);
		_DESTINYCHANGECLASSES.put(170, 132);
		_DESTINYCHANGECLASSES.put(170, 133);
		// Dancers
		_DESTINYCHANGECLASSES.put(171, 98);
		_DESTINYCHANGECLASSES.put(172, 100);
		_DESTINYCHANGECLASSES.put(173, 107);
		_DESTINYCHANGECLASSES.put(174, 115);
		_DESTINYCHANGECLASSES.put(175, 116);
		// Summoners
		_DESTINYCHANGECLASSES.put(176, 96);
		_DESTINYCHANGECLASSES.put(177, 104);
		_DESTINYCHANGECLASSES.put(178, 111);
		// Healers
		_DESTINYCHANGECLASSES.put(179, 97);
		_DESTINYCHANGECLASSES.put(180, 105);
		_DESTINYCHANGECLASSES.put(181, 112);
		_NAMECLASSES.put(90, "Phoenix Knight");
		_NAMECLASSES.put(91, "Hell Knight");
		_NAMECLASSES.put(99, "Eva Templar");
		_NAMECLASSES.put(106, "Shillien Templar");
		_NAMECLASSES.put(88, "Duelist");
		_NAMECLASSES.put(89, "Dreadnought");
		_NAMECLASSES.put(113, "Titan");
		_NAMECLASSES.put(114, "Grand Khavatari");
		_NAMECLASSES.put(118, "Maestro");
		_NAMECLASSES.put(131, "Doombringer");
		_NAMECLASSES.put(93, "Adventurer");
		_NAMECLASSES.put(101, "Wind Rider");
		_NAMECLASSES.put(108, "Ghost Hunter");
		_NAMECLASSES.put(117, "Fortune Seeker");
		_NAMECLASSES.put(92, "Sagittarius");
		_NAMECLASSES.put(102, "Moonlight Sentinel");
		_NAMECLASSES.put(109, "Ghost Sentinel");
		_NAMECLASSES.put(134, "Trickster");
		_NAMECLASSES.put(94, "Archmage");
		_NAMECLASSES.put(95, "SoulTaker");
		_NAMECLASSES.put(103, "Mystic Muse");
		_NAMECLASSES.put(110, "Storm Screamer");
		_NAMECLASSES.put(132, "M Soul Hound");
		_NAMECLASSES.put(133, "F Soul Hound");
		_NAMECLASSES.put(98, "Hierophant");
		_NAMECLASSES.put(100, "Sword Muse");
		_NAMECLASSES.put(107, "Spectral Dancer");
		_NAMECLASSES.put(115, "Dominator");
		_NAMECLASSES.put(116, "Doomcryer");
		_NAMECLASSES.put(96, "Arcana Lord");
		_NAMECLASSES.put(104, "Elemental Master");
		_NAMECLASSES.put(111, "Spectral Master");
		_NAMECLASSES.put(97, "Cardinal");
		_NAMECLASSES.put(105, "Eva Saint");
		_NAMECLASSES.put(112, "Shillien Saint");
		_NAMECLASSES.put(148, "Sigel Phoenix Knight");
		_NAMECLASSES.put(149, "Sigel Hell Knight");
		_NAMECLASSES.put(150, "Sigel Eva Templar");
		_NAMECLASSES.put(151, "Sigel Shillien Templar");
		_NAMECLASSES.put(152, "Tyrr Duelist");
		_NAMECLASSES.put(153, "Tyrr Dreadnought");
		_NAMECLASSES.put(154, "Tyrr Titan");
		_NAMECLASSES.put(155, "Tyrr Grand Khavatari");
		_NAMECLASSES.put(156, "Tyrr Maestro");
		_NAMECLASSES.put(157, "Tyrr Doombringer");
		_NAMECLASSES.put(158, "Othell Adventure");
		_NAMECLASSES.put(159, "Othell Wind Rider");
		_NAMECLASSES.put(160, "Othell Ghost Hunter");
		_NAMECLASSES.put(161, "Othell Fortune Seeker");
		_NAMECLASSES.put(162, "Yul Saggitarius");
		_NAMECLASSES.put(163, "Yul Moonlight Sentinel");
		_NAMECLASSES.put(164, "Yul Ghost Sentinel");
		_NAMECLASSES.put(165, "Yul Trickster");
		_NAMECLASSES.put(166, "Feoh Archmage");
		_NAMECLASSES.put(167, "Feoh Soultaker");
		_NAMECLASSES.put(168, "Feoh Mystic Muse");
		_NAMECLASSES.put(169, "Feoh Storm Screamer");
		_NAMECLASSES.put(170, "Feoh Soul Hound");
		_NAMECLASSES.put(171, "Iss Hierophant");
		_NAMECLASSES.put(172, "Iss Sword Muse");
		_NAMECLASSES.put(173, "Iss Spectral Dancer");
		_NAMECLASSES.put(174, "Iss Dominator");
		_NAMECLASSES.put(175, "Iss Doomcryer");
		_NAMECLASSES.put(176, "Wynn Arcana Lord");
		_NAMECLASSES.put(177, "Wynn Elemental Master");
		_NAMECLASSES.put(178, "Wynn Spectral Master");
		_NAMECLASSES.put(179, "Aeore Cardinal");
		_NAMECLASSES.put(180, "Aeore Eva Saint");
		_NAMECLASSES.put(181, "Aeore Shillen Saint");
	}
	
	private String obtainIcon(int skillId)
	{
		String format = " ";
		String prefix = "icon.skill";
		
		if ((skillId > 0) && (skillId < 10))
		{
			format = "000" + skillId;
		}
		else if ((skillId > 9) && (skillId < 100))
		{
			format = "00" + skillId;
		}
		else if (skillId == 995)
		{
			format = "0793";
		}
		else if (skillId == 331)
		{
			format = "0330";
		}
		else if (skillId == 626)
		{
			format = "0312";
		}
		else if ((skillId == 926) || (skillId == 934) || (skillId == 935))
		{
			format = "0925";
		}
		else if ((skillId > 778) && (skillId < 784))
		{
			format = "0779";
		}
		else if (skillId == 933)
		{
			format = "0470";
		}
		else if ((skillId > 99) && (skillId < 1000))
		{
			format = "0" + skillId;
		}
		else if (skillId == 1565)
		{
			format = "0213";
		}
		else if (skillId == 1517)
		{
			format = "1536";
		}
		else if (skillId == 1518)
		{
			format = "1537";
		}
		else if (skillId == 1547)
		{
			format = "0065";
		}
		else if ((skillId > 999) && (skillId < 2000))
		{
			format = String.valueOf(skillId);
		}
		else if ((skillId > 4550) && (skillId < 4555))
		{
			format = "5739";
		}
		else if ((skillId < 4698) && (skillId < 4701))
		{
			format = "1331";
		}
		else if ((skillId > 4701) && (skillId < 4704))
		{
			format = "1332";
		}
		else if (skillId == 6049)
		{
			format = "0094";
		}
		else if (skillId == 20006)
		{
			prefix = "BranchSys2.icon.skill";
			format = "20006";
		}
		else
		{
			format = String.valueOf(skillId);
		}
		
		String finalCompose = prefix + format;
		return finalCompose;
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		calculateNextClass(player);
		
		int oldClassId = 0;
		int newClassId = AwakingManager.getInstance().childOf(player.getClassId());
		
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		if (command.equalsIgnoreCase("Awaken"))
		{
			int essencesCount = AwakingManager.getInstance().giveGiantEssences(player, true);
			String transferData = new String();
			
			if (_NPC.get(getNpcId()) != newClassId)
			{
				oldClassId = _DESTINYCHANGECLASSES.get(NextClassId);
				player.unsetVar("awakenByStoneOfDestiny");
				player.unsetVar("classTarget");
				player.unsetVar("classKeepSkills");
				transferData = "I will ask again... do you wish to Awaken?<br><font color=af9878>(The " + _NAMECLASSES.get(oldClassId) + "'s skills must be present before awakening as an " + _NAMECLASSES.get(NextClassId) + ").</font>";
			}
			else
			{
				oldClassId = player.getClassId().getId();
				transferData = "You are not strong enough to receive the giant's power. You need to choose between the giant's power and the god's power.<br>In other words, you should be in the best shape to obtain all the power from the giant. Come back when you are ready.";
			}
			
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			htmlMessage.setFile("default/" + getNpcId() + "-4.htm");
			htmlMessage.replace("%SP%", String.valueOf(sp));
			htmlMessage.replace("%ESSENCES%", String.valueOf(essencesCount));
			htmlMessage.replace("%TRANSFERDATA%", transferData);
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken1"))
		{
			String skillList = new String();
			skillList = skillList + "<table border=0 cellpading=8 cellspacing=4>";
			
			if (_NPC.get(getNpcId()) != newClassId)
			{
				oldClassId = _DESTINYCHANGECLASSES.get(NextClassId);
				player.setVar("awakenByStoneOfDestiny", "true", 120000);
				player.setVar("classTarget", String.valueOf(NextClassId), 120000);
				player.setVar("classKeepSkills", String.valueOf(oldClassId), 120000);
			}
			else
			{
				oldClassId = player.getClassId().getId();
			}
			
			List<Integer> skillListId = SkillAcquireHolder.getInstance().getMaintainSkillOnAwake(oldClassId, newClassId);
			
			for (int sId : skillListId)
			{
				String iconData = obtainIcon(sId);
				String name = new String();
				Skill skl = SkillTable.getInstance().getInfo(sId, SkillTable.getInstance().getBaseLevel(sId));
				
				if (skl != null)
				{
					name = skl.getName();
				}
				else
				{
					continue;
				}
				
				skillList = skillList + "<tr><td width=34 height=34><img src=" + iconData + " width=32 height=32></td><td width=200> " + name + " </td></tr><tr><td colspan=2><br></td></tr>";
			}
			
			skillList = skillList + "</table>";
			
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			htmlMessage.replace("%SKILLIST%", skillList);
			htmlMessage.setFile("default/" + getNpcId() + "-5.htm");
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken2"))
		{
			player.setVar("AwakenPrepared", "true", -1);
			player.setVar("AwakenedID", NextClassId, -1);
			player.sendPacket(new ExChangeToAwakenedClass(NextClassId));
			player.addExpAndSp(0, sp);
			AwakingManager.getInstance().giveGiantEssences(player, false);
			ItemFunctions.addItem(player, 32778, 1, true);
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		String htmlpath;
		
		if (val == 0)
		{
			if ((player.getClassId().getClassLevel() == ClassLevel.Third) && (player.getInventory().getCountOf(SCROLL_OF_AFTERLIFE) > 0))
			{
				for (ClassId classId1 : ClassId.VALUES)
				{
					if (classId1.childOf(player.getClassId()))
					{
						classId1.getId();
						break;
					}
				}
				
				if (player.getPets().size() > 0)
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else if (!classSynk(player))
				{
					htmlpath = getHtmlPath(getNpcId(), 2, player);
				}
				else if (player.getLevel() < 85)
				{
					htmlpath = getHtmlPath(getNpcId(), val, player);
				}
				else
				{
					if (player.getVar("AwakenedOldIDClass") == null)
					{
						player.setVar("AwakenedOldIDClass", player.getClassId().getId(), -1);
					}
					
					htmlpath = getHtmlPath(getNpcId(), 3, player);
				}
				
				if (player.getVarB("AwakenPrepared", false))
				{
					player.sendPacket(new ExChangeToAwakenedClass(NextClassId));
					return;
				}
			}
			else
			{
				htmlpath = getHtmlPath(getNpcId(), val, player);
			}
		}
		else
		{
			htmlpath = getHtmlPath(getNpcId(), val, player);
		}
		
		showChatWindow(player, htmlpath, replace);
	}
	
	private boolean classSynk(Player player)
	{
		int oldId = player.getClassId().getId();
		
		switch (getNpcId())
		{
			case 33397:
			{
				if ((oldId == 90) || (oldId == 91) || (oldId == 99) || (oldId == 106))
				{
					return true;
				}
				
				break;
			}
			
			case 33398:
			{
				if ((oldId == 88) || (oldId == 89) || (oldId == 113) || (oldId == 114) || (oldId == 118) || (oldId == 131))
				{
					return true;
				}
				
				break;
			}
			
			case 33399:
			{
				if ((oldId == 93) || (oldId == 101) || (oldId == 108) || (oldId == 117))
				{
					return true;
				}
				
				break;
			}
			
			case 33400:
			{
				if ((oldId == 92) || (oldId == 102) || (oldId == 109) || (oldId == 134))
				{
					return true;
				}
				
				break;
			}
			
			case 33401:
			{
				if ((oldId == 94) || (oldId == 95) || (oldId == 103) || (oldId == 110) || (oldId == 132) || (oldId == 133))
				{
					return true;
				}
				
				break;
			}
			
			case 33402:
			{
				if ((oldId == 98) || (oldId == 116) || (oldId == 115) || (oldId == 100) || (oldId == 107) || (oldId == 136))
				{
					return true;
				}
				
				break;
			}
			
			case 33403:
			{
				if ((oldId == 96) || (oldId == 104) || (oldId == 111))
				{
					return true;
				}
				
				break;
			}
			
			case 33404:
			{
				if ((oldId == 97) || (oldId == 105) || (oldId == 112))
				{
					return true;
				}
				
				break;
			}
		}
		
		return false;
	}
	
	private void calculateNextClass(Player player)
	{
		switch (player.getClassId().getId())
		{
			case 90:
				NextClassId = 148;
				break;
			
			case 91:
				NextClassId = 149;
				break;
			
			case 99:
				NextClassId = 150;
				break;
			
			case 106:
				NextClassId = 151;
				break;
			
			case 88:
				NextClassId = 152;
				break;
			
			case 89:
				NextClassId = 153;
				break;
			
			case 113:
				NextClassId = 154;
				break;
			
			case 114:
				NextClassId = 155;
				break;
			
			case 118:
				NextClassId = 156;
				break;
			
			case 131:
				NextClassId = 157;
				break;
			
			case 93:
				NextClassId = 158;
				break;
			
			case 101:
				NextClassId = 159;
				break;
			
			case 108:
				NextClassId = 160;
				break;
			
			case 117:
				NextClassId = 161;
				break;
			
			case 92:
				NextClassId = 162;
				break;
			
			case 102:
				NextClassId = 163;
				break;
			
			case 109:
				NextClassId = 164;
				break;
			
			case 134:
				NextClassId = 165;
				break;
			
			case 94:
				NextClassId = 166;
				break;
			
			case 95:
				NextClassId = 167;
				break;
			
			case 103:
				NextClassId = 168;
				break;
			
			case 110:
				NextClassId = 169;
				break;
			
			case 132:
				NextClassId = 170;
				break;
			
			case 133:
				NextClassId = 170;
				break;
			
			case 98:
				NextClassId = 171;
				break;
			
			case 100:
				NextClassId = 172;
				break;
			
			case 107:
				NextClassId = 173;
				break;
			
			case 115:
				NextClassId = 174;
				break;
			
			case 116:
				NextClassId = 175;
				break;
			
			case 96:
				NextClassId = 176;
				break;
			
			case 104:
				NextClassId = 177;
				break;
			
			case 111:
				NextClassId = 178;
				break;
			
			case 97:
				NextClassId = 179;
				break;
			
			case 105:
				NextClassId = 180;
				break;
			
			case 112:
				NextClassId = 181;
				break;
		}
	}
}