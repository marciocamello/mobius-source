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

/**
 * @author vegax
 */
public final class MentoringInstance extends NpcInstance
{
	private final int MENTEE_CERTIFICATE = 33800;
	private final int DIPLOMA = 33805;
	
	public MentoringInstance(int objectId, NpcTemplate template)
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
		
		if (command.startsWith("changediploma"))
		{
			if (player.isAwaking() && (player.getLevel() >= 85))
			{
				if (ItemFunctions.getItemCount(player, MENTEE_CERTIFICATE) == 1)
				{
					ItemFunctions.removeItem(player, MENTEE_CERTIFICATE, 1, true);
					ItemFunctions.addItem(player, DIPLOMA, 40, true);
				}
				else
				{
					showChatWindow(player, "mentoring/menthelper-no-diploma.htm");
				}
			}
			else
			{
				showChatWindow(player, "mentoring/menthelper-no-diploma.htm");
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}