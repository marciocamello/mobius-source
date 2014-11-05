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
package lineage2.gameserver.network.clientpackets;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExShowUsmVideo;

/**
 * @author Mobius
 */
public class RequestChangeToAwakenedClass extends L2GameClientPacket
{
	private int change;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		change = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if (!Config.AWAKING_FREE)
		{
			final Player player = getClient().getActiveChar();
			
			if (player == null)
			{
				return;
			}
			
			if (player.getLevel() < 85)
			{
				return;
			}
			
			if (player.getClassId().level() < 3)
			{
				return;
			}
			
			if (change != 1)
			{
				return;
			}
			
			AwakingManager.getInstance().SetAwakingId(player);
			
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q010));
				}
			}, 15000);
		}
		else
		{
			Player player = getClient().getActiveChar();
			
			if (player == null)
			{
				return;
			}
			
			if (player.getLevel() < 85)
			{
				return;
			}
			
			if (player.getClassId().level() < 3)
			{
				return;
			}
			
			if (player.isAwaking())
			{
				return;
			}
			
			AwakingManager.getInstance().SetAwakingId(player);
		}
	}
}
