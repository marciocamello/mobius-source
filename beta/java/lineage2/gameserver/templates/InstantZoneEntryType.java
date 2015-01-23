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
package lineage2.gameserver.templates;

import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum InstantZoneEntryType
{
	SOLO
	{
		@Override
		public boolean canEnter(Player player, InstantZone instancedZone)
		{
			if (player.isInParty())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.A_PARTY_CANNOT_BE_FORMED_IN_THIS_AREA));
				return false;
			}
			
			SystemMessageId msg = checkPlayer(player, instancedZone);
			
			if (msg != null)
			{
				if (msg.getParamCount() > 0)
				{
					player.sendPacket(SystemMessage.getSystemMessage(msg).addPcName(player));
				}
				else
				{
					player.sendPacket(SystemMessage.getSystemMessage(msg));
				}
				
				return false;
			}
			
			return true;
		}
		
		@Override
		public boolean canReEnter(Player player, InstantZone instancedZone)
		{
			if (player.isCursedWeaponEquipped() || player.isInFlyingTransform())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS));
				return false;
			}
			
			return true;
		}
	},
	PARTY
	{
		@Override
		public boolean canEnter(Player player, InstantZone instancedZone)
		{
			Party party = player.getParty();
			
			if (party == null)
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER));
				return false;
			}
			
			if (!party.isLeader(player))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_A_PARTY_LEADER_CAN_MAKE_THE_REQUEST_TO_ENTER));
				return false;
			}
			
			if (party.getMemberCount() < instancedZone.getMinParty())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_MUST_HAVE_A_MINIMUM_OF_S1_PEOPLE_TO_ENTER_THIS_INSTANCED_ZONE).addInt(instancedZone.getMinParty()));
				return false;
			}
			
			if (party.getMemberCount() > instancedZone.getMaxParty())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_DUE_TO_THE_PARTY_HAVING_EXCEEDED_THE_LIMIT));
				return false;
			}
			
			for (Player member : party.getPartyMembers())
			{
				if (!player.isInRange(member, 500))
				{
					party.broadCast(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_IN_A_LOCATION_WHICH_CANNOT_BE_ENTERED_THEREFORE_IT_CANNOT_BE_PROCESSED).addPcName(member));
					return false;
				}
				
				SystemMessageId msg = checkPlayer(member, instancedZone);
				
				if (msg != null)
				{
					if (msg.getParamCount() > 0)
					{
						party.broadCast(SystemMessage.getSystemMessage(msg).addPcName(member));
					}
					else
					{
						member.sendPacket(SystemMessage.getSystemMessage(msg));
					}
					
					return false;
				}
			}
			
			return true;
		}
		
		@Override
		public boolean canReEnter(Player player, InstantZone instanceZone)
		{
			Party party = player.getParty();
			
			if (party == null)
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER));
				return false;
			}
			
			if (party.getMemberCount() > instanceZone.getMaxParty())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_DUE_TO_THE_PARTY_HAVING_EXCEEDED_THE_LIMIT));
				return false;
			}
			
			if (player.isCursedWeaponEquipped() || player.isInFlyingTransform())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS));
				return false;
			}
			
			return true;
		}
	},
	COMMAND_CHANNEL
	{
		@Override
		public boolean canEnter(Player player, InstantZone instancedZone)
		{
			Party party = player.getParty();
			
			if ((party == null) || (party.getCommandChannel() == null))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_ASSOCIATED_WITH_THE_CURRENT_COMMAND_CHANNEL));
				return false;
			}
			
			CommandChannel cc = party.getCommandChannel();
			
			if (cc.getMemberCount() < instancedZone.getMinParty())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_MUST_HAVE_A_MINIMUM_OF_S1_PEOPLE_TO_ENTER_THIS_INSTANCED_ZONE).addInt(instancedZone.getMinParty()));
				return false;
			}
			
			if (cc.getMemberCount() > instancedZone.getMaxParty())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_DUE_TO_THE_PARTY_HAVING_EXCEEDED_THE_LIMIT));
				return false;
			}
			
			for (Player member : cc)
			{
				if (!player.isInRange(member, 500))
				{
					cc.broadCast(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_IN_A_LOCATION_WHICH_CANNOT_BE_ENTERED_THEREFORE_IT_CANNOT_BE_PROCESSED).addPcName(member));
					return false;
				}
				
				SystemMessageId msg = checkPlayer(member, instancedZone);
				
				if (msg != null)
				{
					if (msg.getParamCount() > 0)
					{
						cc.broadCast(SystemMessage.getSystemMessage(msg).addPcName(member));
					}
					else
					{
						member.sendPacket(SystemMessage.getSystemMessage(msg));
					}
					
					return false;
				}
			}
			
			return true;
		}
		
		@Override
		public boolean canReEnter(Player player, InstantZone instanceZone)
		{
			Party commparty = player.getParty();
			
			if ((commparty == null) || (commparty.getCommandChannel() == null))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_ASSOCIATED_WITH_THE_CURRENT_COMMAND_CHANNEL));
				return false;
			}
			
			CommandChannel cc = commparty.getCommandChannel();
			
			if (cc.getMemberCount() > instanceZone.getMaxParty())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_DUE_TO_THE_PARTY_HAVING_EXCEEDED_THE_LIMIT));
				return false;
			}
			
			if (player.isCursedWeaponEquipped() || player.isInFlyingTransform())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ENTER_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS));
				return false;
			}
			
			return true;
		}
	};
	/**
	 * Method canEnter.
	 * @param player Player
	 * @param instancedZone InstantZone
	 * @return boolean
	 */
	public abstract boolean canEnter(Player player, InstantZone instancedZone);
	
	/**
	 * Method canReEnter.
	 * @param player Player
	 * @param instancedZone InstantZone
	 * @return boolean
	 */
	public abstract boolean canReEnter(Player player, InstantZone instancedZone);
	
	/**
	 * Method checkPlayer.
	 * @param player Player
	 * @param instancedZone InstantZone
	 * @return SystemMessageId
	 */
	static SystemMessageId checkPlayer(Player player, InstantZone instancedZone)
	{
		if (player.getActiveReflection() != null)
		{
			return SystemMessageId.YOU_HAVE_ENTERED_ANOTHER_INSTANT_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON;
		}
		
		if ((player.getLevel() < instancedZone.getMinLevel()) || (player.getLevel() > instancedZone.getMaxLevel()))
		{
			return SystemMessageId.C1_S_LEVEL_DOES_NOT_CORRESPOND_TO_THE_REQUIREMENTS_FOR_ENTRY;
		}
		
		if (player.isCursedWeaponEquipped() || player.isInFlyingTransform())
		{
			return SystemMessageId.YOU_CANNOT_ENTER_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS;
		}
		
		if (InstantZoneHolder.getInstance().getMinutesToNextEntrance(instancedZone.getId(), player) > 0)
		{
			return SystemMessageId.C1_MAY_NOT_RE_ENTER_YET;
		}
		
		if ((instancedZone.getRemovedItemId() > 0) && instancedZone.getRemovedItemNecessity() && (ItemFunctions.getItemCount(player, instancedZone.getRemovedItemId()) < 1))
		{
			return SystemMessageId.C1_S_ITEM_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED;
		}
		
		if (instancedZone.getRequiredQuestId() > 0)
		{
			Quest q = QuestManager.getQuest(instancedZone.getRequiredQuestId());
			QuestState qs = player.getQuestState(q.getClass());
			
			if ((qs == null) || (qs.getState() != Quest.STARTED))
			{
				return SystemMessageId.C1_S_QUEST_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED;
			}
		}
		
		return null;
	}
}
