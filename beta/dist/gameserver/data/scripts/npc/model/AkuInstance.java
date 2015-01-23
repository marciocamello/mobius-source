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
 * @author KilRoy & Bonux & Nache
 */
public class AkuInstance extends NpcInstance
{
	private static final int TAUTI_EXTREME_INSTANCE_ID = 219;
	private static final Location TAUTI_ROOM_TELEPORT = new Location(-147262, 211318, -10040);
	
	public AkuInstance(int objectId, NpcTemplate template)
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
		
		if (command.equals("request_tauti_extreme_battle"))
		{
			
			if (SoHManager.getCurrentStage() != 2)
			{
				showChatWindow(player, "tauti/sofa_aku002h.htm");
				return;
			}
			if (player.getParty() == null)
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_A_PARTY_LEADER_CAN_MAKE_THE_REQUEST_TO_ENTER));
				return;
			}
			if (player.getParty().getCommandChannel() == null)
			{
				showChatWindow(player, "tauti/sofa_aku002e.htm");
				return;
			}
			if (!player.getParty().getCommandChannel().isLeaderCommandChannel(player))
			{
				showChatWindow(player, "tauti/sofa_aku002d.htm");
				return;
			}
			int channelMemberCount = player.getParty().getCommandChannel().getMemberCount();
			if ((channelMemberCount > 35) || (channelMemberCount < 21))
			{
				showChatWindow(player, "tauti/sofa_aku002c.htm");
				return;
			}
			for (Player commandChannel : player.getParty().getCommandChannel().getMembers())
			{
				if (commandChannel.getLevel() < 97)
				{
					showChatWindow(player, "tauti/sofa_aku002b.htm");
				}
				return;
			}
			
			final Reflection reflection = player.getActiveReflection();
			if (reflection != null)
			{
				if (player.canReenterInstance(TAUTI_EXTREME_INSTANCE_ID))
				{
					showChatWindow(player, "tauti/sofa_aku002g.htm");
				}
			}
			else if (player.canEnterInstance(TAUTI_EXTREME_INSTANCE_ID))
			{
				ReflectionUtils.enterReflection(player, new TautiInstance(), TAUTI_EXTREME_INSTANCE_ID);
				showChatWindow(player, "tauti/sofa_aku002a.htm");
			}
		}
		else if (command.equals("reenter_tauti_extreme_battle"))
		{
			final Reflection reflection = player.getActiveReflection();
			if (reflection != null)
			{
				if (player.canReenterInstance(TAUTI_EXTREME_INSTANCE_ID))
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
					showChatWindow(player, "tauti/sofa_aku002f.htm");
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
		player.sendPacket(new NpcHtmlMessage(player, this, "tauti/sofa_aku001.htm", val));
		return;
	}
}