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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;

public final class PowerfulDeviceInstance extends NpcInstance
{
	private static final int SCROLL_OF_AFTERLIFE = 17600;
	private final int sp = Rnd.get(10000000);
	private int NextClassId = 0;
	
	public PowerfulDeviceInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.equalsIgnoreCase("Awaken"))
		{
			int essencesCount = AwakingManager.getInstance().giveGiantEssences(player, true);
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			htmlMessage.replace("%SP%", String.valueOf(sp));
			htmlMessage.replace("%ESSENCES%", String.valueOf(essencesCount));
			htmlMessage.setFile("default/" + getId() + "-4.htm");
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken1"))
		{
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			htmlMessage.setFile("awaken/" + player.getClassId().getId() + ".htm");
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken2"))
		{
			calculateNextClass(player);
			player.setVar("AwakenPrepared", "true", -1);
			player.setVar("AwakenedID", NextClassId, -1);
			player.sendPacket(new ExChangeToAwakenedClass(NextClassId));
			player.addExpAndSp(0L, sp);
			player.getInventory().removeItemByItemId(SCROLL_OF_AFTERLIFE, 1);
			AwakingManager.getInstance().giveGiantEssences(player, false);
			ItemFunctions.addItem(player, 32778, 1, true);
			AwakingManager.getInstance().getRaceSkill(player);
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
				calculateNextClass(player);
				
				for (final ClassId classId1 : ClassId.VALUES)
				{
					if ((player.getClassId().getClassLevel() == ClassLevel.Third) && classId1.childOf(player.getClassId()))
					{
						break;
					}
				}
				
				if (player.getPets().size() > 0)
				{
					htmlpath = getHtmlPath(getId(), 1, player);
				}
				else if (!classSynk(player))
				{
					htmlpath = getHtmlPath(getId(), 2, player);
				}
				else if (player.getLevel() < 85)
				{
					htmlpath = getHtmlPath(getId(), val, player);
				}
				else
				{
					if (player.getVar("AwakenedOldIDClass") == null)
					{
						player.setVar("AwakenedOldIDClass", player.getClassId().getId(), -1);
					}
					htmlpath = getHtmlPath(getId(), 3, player);
				}
				if (player.getVarB("AwakenPrepared", false))
				{
					player.sendPacket(new ExChangeToAwakenedClass(NextClassId));
					return;
				}
			}
			else
			{
				htmlpath = getHtmlPath(getId(), val, player);
			}
		}
		else
		{
			htmlpath = getHtmlPath(getId(), val, player);
		}
		
		showChatWindow(player, htmlpath, replace);
	}
	
	private void calculateNextClass(Player player)
	{
		switch (player.getClassId().getId())
		{
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
			case 115:
				NextClassId = 174;
				break;
			case 116:
				NextClassId = 175;
				break;
			case 107:
				NextClassId = 173;
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
	
	private boolean classSynk(Player player)
	{
		int oldId = player.getClassId().getId();
		switch (getId())
		{
		
			case 33397:
				if ((oldId == 90) || (oldId == 91) || (oldId == 99) || (oldId == 106))
				{
					return true;
				}
				
				break;
			case 33398:
				if ((oldId == 88) || (oldId == 89) || (oldId == 113) || (oldId == 114) || (oldId == 118) || (oldId == 131))
				{
					return true;
				}
				
				break;
			case 33399:
				if ((oldId == 93) || (oldId == 101) || (oldId == 108) || (oldId == 117))
				{
					return true;
				}
				
				break;
			case 33400:
				if ((oldId == 92) || (oldId == 102) || (oldId == 109) || (oldId == 134))
				{
					return true;
				}
				
				break;
			case 33401:
				if ((oldId == 94) || (oldId == 95) || (oldId == 103) || (oldId == 110) || (oldId == 132) || (oldId == 133))
				{
					return true;
				}
				
				break;
			case 33402:
				if ((oldId == 98) || (oldId == 116) || (oldId == 115) || (oldId == 100) || (oldId == 107) || (oldId == 136))
				{
					return true;
				}
				
				break;
			case 33403:
				if ((oldId == 96) || (oldId == 104) || (oldId == 111))
				{
					return true;
				}
				
				break;
			case 33404:
				if ((oldId == 97) || (oldId == 105) || (oldId == 112))
				{
					return true;
				}
				break;
		}
		
		return false;
	}
}