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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.npc.NpcTemplate;

public final class MammonsInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;
	private static final int ANCIENT_ADENA_ID = 5575;
	private static final int ADENA = 57;
	public static final String MAMMONS_HTML_PATH = "ssmammons/";
	
	public MammonsInstance(int objectId, NpcTemplate template)
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
		
		super.onBypassFeedback(player, command);
		
		if (command.startsWith("bmarket"))
		{
			ItemInstance ancientAdena = player.getInventory().getItemByItemId(ANCIENT_ADENA_ID);
			long ancientAdenaAmount = ancientAdena != null ? ancientAdena.getCount() : 0L;
			int val = Integer.parseInt(command.substring(11, 12).trim());
			
			if (command.length() > 12)
			{
				val = Integer.parseInt(command.substring(11, 13).trim());
			}
			
			switch (val)
			{
				case 1:
					long ancientAdenaConvert = 0L;
					
					try
					{
						ancientAdenaConvert = Long.parseLong(command.substring(13).trim());
					}
					catch (NumberFormatException e)
					{
						player.sendMessage(new CustomMessage("common.IntegerAmount", player, new Object[0]));
						return;
					}
					catch (StringIndexOutOfBoundsException e)
					{
						player.sendMessage(new CustomMessage("common.IntegerAmount", player, new Object[0]));
						return;
					}
					
					if ((ancientAdenaAmount < ancientAdenaConvert) || (ancientAdenaConvert < 1L))
					{
						player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
						return;
					}
					
					if (player.getInventory().destroyItemByItemId(ANCIENT_ADENA_ID, ancientAdenaConvert))
					{
						player.addAdena(ancientAdenaConvert);
						player.sendPacket(SystemMessage2.removeItems(ANCIENT_ADENA_ID, ancientAdenaConvert));
						player.sendPacket(SystemMessage2.obtainItems(ADENA, ancientAdenaConvert, 0));
					}
					
					break;
				
				default:
					showChatWindow(player, "blkmrkt.htm", new Object[0]);
					break;
			}
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		int npcId = getTemplate().npcId;
		String filename = MAMMONS_HTML_PATH;
		
		switch (npcId)
		{
			case 31092:
				filename = new StringBuilder().append(filename).append("blkmrkt.htm").toString();
				break;
			
			case 31113:
				filename = new StringBuilder().append(filename).append("merchmamm.htm").toString();
				break;
			
			case 31126:
				filename = new StringBuilder().append(filename).append("blksmithmam.htm").toString();
				break;
			
			case 33511:
				filename = new StringBuilder().append(filename).append("priestmam.htm").toString();
				break;
			
			default:
				filename = getHtmlPath(npcId, val, player);
				break;
		}
		
		player.sendPacket(new NpcHtmlMessage(player, this, filename, val));
	}
}
