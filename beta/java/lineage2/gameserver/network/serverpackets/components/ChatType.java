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
 * @author VISTALL
 * @date 12:48/29.12.2010
 */
public enum ChatType
{
	ALL, // 0
	SHOUT, // 1 !
	TELL, // 2 "
	PARTY, // 3 #
	CLAN, // 4 @
	GM, // 5
	PETITION_PLAYER, // 6 used for petition
	PETITION_GM, // 7 * used for petition
	TRADE, // 8 +
	ALLIANCE, // 9 $
	ANNOUNCEMENT, // 10
	SYSTEM_MESSAGE, // 11
	L2FRIEND,
	MSNCHAT,
	PARTY_ROOM, // 14
	COMMANDCHANNEL_ALL, // 15 ``
	COMMANDCHANNEL_COMMANDER, // 16 `
	HERO_VOICE, // 17 %
	CRITICAL_ANNOUNCE, // 18
	SCREEN_ANNOUNCE,
	BATTLEFIELD, // 20 ^
	MPCC_ROOM, // 21 added with epilogue, similar to PARTY_ROOM current for SS
	NPC_ALL, // 22
	NPC_SHOUT, // 23
	NPC_TELL, // 24 NEW_TELL
	GLOBAL; // 25
	public static final ChatType[] VALUES = values();
}
