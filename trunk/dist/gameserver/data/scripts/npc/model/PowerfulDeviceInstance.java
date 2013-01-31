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

import gnu.trove.map.hash.TIntIntHashMap;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PowerfulDeviceInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is -1632474353838420887)
	 */
	private static final long serialVersionUID = -1632474353838420887L;
	/**
	 * Field _NPC.
	 */
	private static TIntIntHashMap _NPC = new TIntIntHashMap(8);
	
	/**
	 * Constructor for PowerfulDeviceInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public PowerfulDeviceInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_NPC.clear();
		_NPC.put(33397, 139);
		_NPC.put(33398, 140);
		_NPC.put(33399, 141);
		_NPC.put(33400, 142);
		_NPC.put(33401, 143);
		_NPC.put(33402, 144);
		_NPC.put(33403, 145);
		_NPC.put(33404, 146);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
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
			htmlMessage.replace("%SP%", String.valueOf(Rnd.get(10000000)));
			htmlMessage.replace("%ESSENCES%", String.valueOf(essencesCount));
			htmlMessage.setFile("default/" + getNpcId() + "-4.htm");
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken1"))
		{
			NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
			htmlMessage.replace("%SKILLIST%", "");
			htmlMessage.setFile("default/" + getNpcId() + "-5.htm");
			player.sendPacket(htmlMessage);
		}
		else if (command.equalsIgnoreCase("Awaken2"))
		{
			player.setVar("AwakenPrepared", "true", -1);
			AwakingManager.getInstance().SendReqToAwaking(player);
		}
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param replace Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... replace)
	{
		String htmlpath;
		int newClassId = 0;
		if (val == 0)
		{
			if ((player.getClassLevel() == 4) && (player.getInventory().getCountOf(17600) > 0))
			{
				newClassId = AwakingManager.getInstance().childOf(player.getClassId());
				if (player.getSummonList().size() > 0)
				{
					htmlpath = getHtmlPath(getNpcId(), 1, player);
				}
				else if (_NPC.get(getNpcId()) != newClassId)
				{
					htmlpath = getHtmlPath(getNpcId(), 2, player);
				}
				else
				{
					htmlpath = getHtmlPath(getNpcId(), 3, player);
				}
				if (player.getVarB("AwakenPrepared", false))
				{
					AwakingManager.getInstance().SendReqToAwaking(player);
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
}
