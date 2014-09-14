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
import lineage2.gameserver.model.base.ClassType2;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExChangeToAwakenedClass;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

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
			htmlMessage.setFile("awaken/" + player.getActiveSubClass().getClassId() + ".htm");
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken2"))
		{
			calculateNextClass(player);
			
			player.setVar("AwakenPrepared", "true", -1);
			player.setVar("AwakenedID", NextClassId, -1);
			player.sendPacket(new ExChangeToAwakenedClass(NextClassId));
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		final int NpcID = getId();
		final ClassType2 currentClassType = player.getClassId().getType2();
		final ClassId[] classType = getClassIdsByNpc(NpcID);
		
		boolean correctNPC = false;
		
		for (final ClassId classID : classType)
		{
			if (classID.getType2() == currentClassType)
			{
				correctNPC = true;
				break;
			}
		}
		
		if (!correctNPC)
		{
			showChatWindow(player, getHtmlPath(NpcID, 2, player), replace);
			return;
		}
		
		String htmltext;
		
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
					htmltext = getHtmlPath(getId(), 1, player);
				}
				else
				{
					if (player.getVar("AwakenedOldIDClass") == null)
					{
						player.setVar("AwakenedOldIDClass", player.getClassId().getId(), -1);
					}
					htmltext = getHtmlPath(getId(), 3, player);
				}
				if (player.getVarB("AwakenPrepared", false))
				{
					player.sendPacket(new ExChangeToAwakenedClass(NextClassId));
					return;
				}
			}
			else
			{
				htmltext = getHtmlPath(getId(), val, player);
			}
		}
		else
		{
			htmltext = getHtmlPath(getId(), val, player);
		}
		
		showChatWindow(player, htmltext, replace);
	}
	
	private void calculateNextClass(Player player)
	{
		switch (player.getActiveSubClass().getClassId())
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
	
	ClassId[] getClassIdsByNpc(final int NpcId)
	{
		switch (NpcId)
		{
			case 33404:
				return new ClassId[]
				{
					ClassId.AEORE_CARDINAL,
					ClassId.AEORE_EVA_SAINT,
					ClassId.AEORE_SHILLEN_SAINT
				};
			case 33397:
				return new ClassId[]
				{
					ClassId.SIGEL_PHOENIX_KNIGHT,
					ClassId.SIGEL_HELL_KNIGHT,
					ClassId.SIGEL_EVAS_TEMPLAR,
					ClassId.SIGEL_SHILLIEN_TEMPLAR
				};
			case 33400:
				return new ClassId[]
				{
					ClassId.YUL_SAGITTARIUS,
					ClassId.YUL_MOONLIGHT_SENTINEL,
					ClassId.YUL_GHOST_SENTINEL,
					ClassId.YUL_TRICKSTER
				};
			case 33402:
				return new ClassId[]
				{
					ClassId.ISS_HIEROPHANT,
					ClassId.ISS_SWORD_MUSE,
					ClassId.ISS_SPECTRAL_DANCER,
					ClassId.ISS_DOMINATOR,
					ClassId.ISS_DOOMCRYER
				};
			case 33399:
				return new ClassId[]
				{
					ClassId.OTHELL_ADVENTURER,
					ClassId.OTHELL_WIND_RIDER,
					ClassId.OTHELL_GHOST_HUNTER,
					ClassId.OTHELL_FORTUNE_SEEKER
				};
			case 33398:
				return new ClassId[]
				{
					ClassId.TYRR_DUELIST,
					ClassId.TYRR_DREADNOUGHT,
					ClassId.TYRR_TITAN,
					ClassId.TYRR_MAESTRO,
					ClassId.TYRR_GRAND_KHAVATARI,
					ClassId.TYRR_DOOMBRINGER
				};
			case 33401:
				return new ClassId[]
				{
					ClassId.FEOH_ARCHMAGE,
					ClassId.FEOH_SOULTAKER,
					ClassId.FEOH_MYSTIC_MUSE,
					ClassId.FEOH_STORM_SCREAMER,
					ClassId.FEOH_SOUL_HOUND
				};
			case 33403:
				return new ClassId[]
				{
					ClassId.WYNN_ARCANA_LORD,
					ClassId.WYNN_ELEMENTAL_MASTER,
					ClassId.WYNN_SPECTRAL_MASTER
				};
		}
		return null;
	}
}