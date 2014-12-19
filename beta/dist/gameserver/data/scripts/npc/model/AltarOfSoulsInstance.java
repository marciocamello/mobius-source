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
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 */
public final class AltarOfSoulsInstance extends NpcInstance
{
	// Npcs
	private static final int LADAR = 25942;
	private static final int CASSIUS = 25943;
	private static final int TERAKAN = 25944;
	// Items
	private static final int APPARITION_STONE_88 = 38572;
	private static final int APPARITION_STONE_93 = 38573;
	private static final int APPARITION_STONE_98 = 38574;
	// Others
	private NpcInstance BOSS_88;
	private NpcInstance BOSS_93;
	private NpcInstance BOSS_98;
	
	public AltarOfSoulsInstance(int objectId, NpcTemplate template)
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
		
		switch (command)
		{
			case "request_boss_88":
				if ((BOSS_88 != null) && !BOSS_88.isDead())
				{
					showChatWindow(player, "default/33920-4.htm");
					return;
				}
				if (ItemFunctions.getItemCount(player, APPARITION_STONE_88) > 0)
				{
					ItemFunctions.removeItem(player, APPARITION_STONE_88, 1, true);
					BOSS_88 = NpcUtils.spawnSingle(TERAKAN, Location.coordsRandomize(getLoc(), 100, 300), getReflection());
					showChatWindow(player, "default/33920-1.htm");
				}
				else
				{
					showChatWindow(player, "default/33920-7.htm");
				}
				break;
			
			case "request_boss_93":
				if ((BOSS_93 != null) && !BOSS_93.isDead())
				{
					showChatWindow(player, "default/33920-5.htm");
					return;
				}
				if (ItemFunctions.getItemCount(player, APPARITION_STONE_93) > 0)
				{
					ItemFunctions.removeItem(player, APPARITION_STONE_93, 1, true);
					BOSS_93 = NpcUtils.spawnSingle(CASSIUS, Location.coordsRandomize(getLoc(), 100, 300), getReflection());
					showChatWindow(player, "default/33920-2.htm");
				}
				else
				{
					showChatWindow(player, "default/33920-8.htm");
				}
				break;
			
			case "request_boss_98":
				if ((BOSS_98 != null) && !BOSS_98.isDead())
				{
					showChatWindow(player, "default/33920-6.htm");
					return;
				}
				if (ItemFunctions.getItemCount(player, APPARITION_STONE_98) > 0)
				{
					ItemFunctions.removeItem(player, APPARITION_STONE_98, 1, true);
					BOSS_98 = NpcUtils.spawnSingle(LADAR, Location.coordsRandomize(getLoc(), 100, 300), getReflection());
					showChatWindow(player, "default/33920-3.htm");
				}
				else
				{
					showChatWindow(player, "default/33920-9.htm");
				}
				break;
			
			default:
				super.onBypassFeedback(player, command);
				break;
		}
	}
}
