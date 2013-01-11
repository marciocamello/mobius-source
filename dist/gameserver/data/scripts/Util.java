/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

public class Util extends Functions
{
	public void Gatekeeper(String[] param)
	{
		if (param.length < 4)
		{
			throw new IllegalArgumentException();
		}
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		long price = Long.parseLong(param[param.length - 1]);
		if (!NpcInstance.canBypassCheck(player, player.getLastNpc()))
		{
			return;
		}
		if ((price > 0) && (player.getAdena() < price))
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		if (player.getMountType() == 2)
		{
			player.sendMessage("Телепортация верхом на виверне невозможна.");
			return;
		}
		if (player.getLastNpc() != null)
		{
			int npcId = player.getLastNpc().getNpcId();
			switch (npcId)
			{
				case 30483:
					if (player.getLevel() >= Config.CRUMA_GATEKEEPER_LVL)
					{
						show("teleporter/30483-no.htm", player);
						return;
					}
					break;
				case 32864:
				case 32865:
				case 32866:
				case 32867:
				case 32868:
				case 32869:
				case 32870:
					if (player.getLevel() < 80)
					{
						show("teleporter/" + npcId + "-no.htm", player);
						return;
					}
					break;
			}
		}
		int x = Integer.parseInt(param[0]);
		int y = Integer.parseInt(param[1]);
		int z = Integer.parseInt(param[2]);
		int castleId = param.length > 4 ? Integer.parseInt(param[3]) : 0;
		if (player.getReflection().isDefault())
		{
			Castle castle = castleId > 0 ? ResidenceHolder.getInstance().getResidence(Castle.class, castleId) : null;
			if ((castle != null) && castle.getSiegeEvent().isInProgress())
			{
				player.sendPacket(Msg.YOU_CANNOT_TELEPORT_TO_A_VILLAGE_THAT_IS_IN_A_SIEGE);
				return;
			}
		}
		Location pos = Location.findPointToStay(x, y, z, 50, 100, player.getGeoIndex());
		if (price > 0)
		{
			player.reduceAdena(price, true);
		}
		player.teleToLocation(pos);
	}
	
	public void QuestGatekeeper(String[] param)
	{
		if (param.length < 5)
		{
			throw new IllegalArgumentException();
		}
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		long count = Long.parseLong(param[3]);
		int item = Integer.parseInt(param[4]);
		if (!NpcInstance.canBypassCheck(player, player.getLastNpc()))
		{
			return;
		}
		if (count > 0)
		{
			if (!player.getInventory().destroyItemByItemId(item, count))
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
				return;
			}
			player.sendPacket(SystemMessage2.removeItems(item, count));
		}
		int x = Integer.parseInt(param[0]);
		int y = Integer.parseInt(param[1]);
		int z = Integer.parseInt(param[2]);
		Location pos = Location.findPointToStay(x, y, z, 20, 70, player.getGeoIndex());
		player.teleToLocation(pos);
	}
	
	public void ReflectionGatekeeper(String[] param)
	{
		if (param.length < 5)
		{
			throw new IllegalArgumentException();
		}
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		player.setReflection(Integer.parseInt(param[4]));
		Gatekeeper(param);
	}
	
	public void TokenJump(String[] param)
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (player.getLevel() <= 19)
		{
			QuestGatekeeper(param);
		}
		else
		{
			show("Only for newbies", player);
		}
	}
	
	public void NoblessTeleport()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (player.isNoble() || Config.ALLOW_NOBLE_TP_TO_ALL)
		{
			show("scripts/noble.htm", player);
		}
		else
		{
			show("scripts/nobleteleporter-no.htm", player);
		}
	}
	
	public void PayPage(String[] param)
	{
		if (param.length < 2)
		{
			throw new IllegalArgumentException();
		}
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		String page = param[0];
		int item = Integer.parseInt(param[1]);
		long price = Long.parseLong(param[2]);
		if (getItemCount(player, item) < price)
		{
			player.sendPacket(item == 57 ? Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA : SystemMsg.INCORRECT_ITEM_COUNT);
			return;
		}
		removeItem(player, item, price);
		show(page, player);
	}
	
	public void TakeNewbieWeaponCoupon()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.ALT_ALLOW_SHADOW_WEAPONS)
		{
			show(new CustomMessage("common.Disabled", player), player);
			return;
		}
		if ((player.getLevel() > 19) || (player.getClassLevel() > 1))
		{
			show("Your level is too high!", player);
			return;
		}
		if (player.getLevel() < 6)
		{
			show("Your level is too low!", player);
			return;
		}
		if (player.getVarB("newbieweapon"))
		{
			show("Your already got your newbie weapon!", player);
			return;
		}
		addItem(player, 7832, 5);
		player.setVar("newbieweapon", "true", -1);
	}
	
	public void TakeAdventurersArmorCoupon()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.ALT_ALLOW_SHADOW_WEAPONS)
		{
			show(new CustomMessage("common.Disabled", player), player);
			return;
		}
		if ((player.getLevel() > 39) || (player.getClassLevel() > 2))
		{
			show("Your level is too high!", player);
			return;
		}
		if ((player.getLevel() < 20) || (player.getClassLevel() < 2))
		{
			show("Your level is too low!", player);
			return;
		}
		if (player.getVarB("newbiearmor"))
		{
			show("Your already got your newbie weapon!", player);
			return;
		}
		addItem(player, 7833, 1);
		player.setVar("newbiearmor", "true", -1);
	}
	
	public void enter_dc()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		player.setVar("DCBackCoords", player.getLoc().toXYZString(), -1);
		player.teleToLocation(-114582, -152635, -6742);
	}
	
	public void exit_dc()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		String var = player.getVar("DCBackCoords");
		if ((var == null) || var.isEmpty())
		{
			player.teleToLocation(new Location(43768, -48232, -800), 0);
			return;
		}
		player.teleToLocation(Location.parseLoc(var), 0);
		player.unsetVar("DCBackCoords");
	}
}
