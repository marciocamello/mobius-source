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
package handlers.bypass;

import lineage2.gameserver.enums.InvisibleType;
import lineage2.gameserver.handlers.BypassHandler;
import lineage2.gameserver.handlers.IBypassHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExResponseBeautyListPacket;
import lineage2.gameserver.network.serverpackets.ExResponseResetList;
import lineage2.gameserver.network.serverpackets.ExShowBeautyMenuPacket;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.skills.AbnormalEffect;

/**
 * @author Mobius
 */
public final class BeautyShop implements IBypassHandler, ScriptFile
{
	private static final int LA_VIE_EN_ROSE = 33825;
	
	/**
	 * Method getBypasses.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IBypassHandler#getBypasses()
	 */
	@Override
	public String[] getBypasses()
	{
		return new String[]
		{
			"Beautyshop",
			"ResetBeautyshop"
		};
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param command String
	 * @see lineage2.gameserver.handlers.IBypassHandler#onBypassFeedback(NpcInstance, Player, String)
	 */
	@Override
	public void onBypassFeedback(NpcInstance npc, Player player, String command)
	{
		if (npc.getId() != LA_VIE_EN_ROSE)
		{
			return;
		}
		
		if (command.equals("Beautyshop"))
		{
			if (player.isInvisible())
			{
				player.setInvisibleType(InvisibleType.NONE);
				player.stopAbnormalEffect(AbnormalEffect.STEALTH);
			}
			player.sendPacket(new ExShowBeautyMenuPacket(0));
			player.sendPacket(new ExResponseBeautyListPacket(player, 1));
		}
		else if (command.equals("ResetBeautyshop"))
		{
			if (player.isInvisible())
			{
				player.setInvisibleType(InvisibleType.NONE);
				player.stopAbnormalEffect(AbnormalEffect.STEALTH);
			}
			player.sendPacket(new ExShowBeautyMenuPacket(1));
			player.sendPacket(new ExResponseResetList(player));
		}
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		BypassHandler.getInstance().registerBypass(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}