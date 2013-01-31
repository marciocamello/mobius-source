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
package lineage2.gameserver.network.serverpackets.components;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum ChatType
{
	/**
	 * Field ALL.
	 */
	ALL,
	/**
	 * Field SHOUT.
	 */
	SHOUT,
	/**
	 * Field TELL.
	 */
	TELL,
	/**
	 * Field PARTY.
	 */
	PARTY,
	/**
	 * Field CLAN.
	 */
	CLAN,
	/**
	 * Field GM.
	 */
	GM,
	/**
	 * Field PETITION_PLAYER.
	 */
	PETITION_PLAYER,
	/**
	 * Field PETITION_GM.
	 */
	PETITION_GM,
	/**
	 * Field TRADE.
	 */
	TRADE,
	/**
	 * Field ALLIANCE.
	 */
	ALLIANCE,
	/**
	 * Field ANNOUNCEMENT.
	 */
	ANNOUNCEMENT,
	/**
	 * Field SYSTEM_MESSAGE.
	 */
	SYSTEM_MESSAGE,
	/**
	 * Field L2FRIEND.
	 */
	L2FRIEND,
	/**
	 * Field MSNCHAT.
	 */
	MSNCHAT,
	/**
	 * Field PARTY_ROOM.
	 */
	PARTY_ROOM,
	/**
	 * Field COMMANDCHANNEL_ALL.
	 */
	COMMANDCHANNEL_ALL,
	/**
	 * Field COMMANDCHANNEL_COMMANDER.
	 */
	COMMANDCHANNEL_COMMANDER,
	/**
	 * Field HERO_VOICE.
	 */
	HERO_VOICE,
	/**
	 * Field CRITICAL_ANNOUNCE.
	 */
	CRITICAL_ANNOUNCE,
	/**
	 * Field SCREEN_ANNOUNCE.
	 */
	SCREEN_ANNOUNCE,
	/**
	 * Field BATTLEFIELD.
	 */
	BATTLEFIELD,
	/**
	 * Field MPCC_ROOM.
	 */
	MPCC_ROOM,
	/**
	 * Field NPC_SAY.
	 */
	NPC_SAY;
	/**
	 * Field VALUES.
	 */
	public static final ChatType[] VALUES = values();
}
