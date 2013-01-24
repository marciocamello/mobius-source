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
package lineage2.gameserver.model.base;

public class PlayerAccess
{
	public int PlayerID;
	public boolean IsGM = false;
	public boolean CanUseGMCommand = false;
	public boolean CanAnnounce = false;
	public boolean CanBanChat = false;
	public boolean CanUnBanChat = false;
	public boolean CanChatPenalty = false;
	public int BanChatDelay = -1;
	public int BanChatMaxValue = -1;
	public int BanChatCountPerDay = -1;
	public int BanChatBonusId = -1;
	public int BanChatBonusCount = -1;
	public boolean CanSetCarma = false;
	public boolean CanCharBan = false;
	public boolean CanCharUnBan = false;
	public boolean CanBan = false;
	public boolean CanUnBan = false;
	public boolean CanTradeBanUnban = false;
	public boolean CanUseBanPanel = false;
	public boolean UseGMShop = false;
	public boolean CanDelete = false;
	public boolean CanKick = false;
	public boolean Menu = false;
	public boolean GodMode = false;
	public boolean CanEditChar = false;
	public boolean CanEditCharAll = false;
	public boolean CanEditPledge = false;
	public boolean CanViewChar = false;
	public boolean CanEditNPC = false;
	public boolean CanViewNPC = false;
	public boolean CanTeleport = false;
	public boolean CanRestart = false;
	public boolean MonsterRace = false;
	public boolean Rider = false;
	public boolean FastUnstuck = false;
	public boolean ResurectFixed = false;
	public boolean Door = false;
	public boolean Res = false;
	public boolean PeaceAttack = false;
	public boolean Heal = false;
	public boolean Unblock = false;
	public boolean UseInventory = true;
	public boolean UseTrade = true;
	public boolean CanAttack = true;
	public boolean CanEvaluate = true;
	public boolean CanJoinParty = true;
	public boolean CanJoinClan = true;
	public boolean UseWarehouse = true;
	public boolean UseShop = true;
	public boolean UseTeleport = true;
	public boolean BlockInventory = false;
	public boolean CanChangeClass = false;
	public boolean CanGmEdit = false;
	public boolean IsEventGm = false;
	public boolean CanReload = false;
	public boolean CanRename = false;
	public boolean CanJail = false;
	public boolean CanPolymorph = false;
	
	public PlayerAccess()
	{
	}
}
