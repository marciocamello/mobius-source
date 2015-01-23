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

import instances.TautiInstance;
import lineage2.gameserver.instancemanager.SoHManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author KilRoy FIXME[K] - Deprecated method getMembers()
 */
public final class SizrakInstance extends NpcInstance
{
	private static final int TAUTI_NORMAL_INSTANCE_ID = 218;
	private static final Location TAUTI_ROOM_TELEPORT = new Location(-147262, 211318, -10040);
	
	public SizrakInstance(int objectId, NpcTemplate template)
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
		
		if (command.equals("request_tauti_normal_battle"))
		{
			if (SoHManager.getCurrentStage() != 2)
			{
				showChatWindow(player, "tauti/sofa_sizraku002h.htm");
				return;
			}
			
			if (player.getParty() == null)
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_A_PARTY_LEADER_CAN_MAKE_THE_REQUEST_TO_ENTER));
				return;
			}
			
			if (player.getParty().getCommandChannel() == null)
			{
				showChatWindow(player, "tauti/sofa_sizraku002e.htm");
				return;
			}
			
			if (!player.getParty().getCommandChannel().isLeaderCommandChannel(player))
			{
				showChatWindow(player, "tauti/sofa_sizraku002d.htm");
				return;
			}
			
			if (player.getParty().getCommandChannel().getMemberCount() > 35)
			{
				showChatWindow(player, "tauti/sofa_sizraku002c.htm");
				return;
			}
			
			for (Player commandChannel : player.getParty().getCommandChannel().getMembers())
			{
				if (commandChannel.getLevel() < 97)
				{
					showChatWindow(player, "tauti/sofa_sizraku002b.htm");
				}
				
				return;
			}
			
			final Reflection reflection = player.getActiveReflection();
			
			if (reflection != null)
			{
				if (player.canReenterInstance(TAUTI_NORMAL_INSTANCE_ID))
				{
					showChatWindow(player, "tauti/sofa_sizraku002g.htm");
				}
			}
			else if (player.canEnterInstance(TAUTI_NORMAL_INSTANCE_ID))
			{
				ReflectionUtils.enterReflection(player, new TautiInstance(), TAUTI_NORMAL_INSTANCE_ID);
				showChatWindow(player, "tauti/sofa_sizraku002a.htm");
			}
		}
		else if (command.equals("reenter_tauti_normal_battle"))
		{
			final Reflection reflection = player.getActiveReflection();
			
			if (reflection != null)
			{
				if (player.canReenterInstance(TAUTI_NORMAL_INSTANCE_ID))
				{
					TautiInstance instance = (TautiInstance) reflection;
					
					if (instance.getInstanceStage() == 2)
					{
						player.teleToLocation(TAUTI_ROOM_TELEPORT, reflection);
					}
					else
					{
						player.teleToLocation(reflection.getTeleportLoc(), reflection);
					}
					
					showChatWindow(player, "tauti/sofa_sizraku002f.htm");
				}
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		player.sendPacket(new NpcHtmlMessage(player, this, "tauti/sofa_sizraku001.htm", val));
		return;
	}
}