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
package ai.harnak_4pf;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;

public class Hermuncus extends DefaultAI
{
	private final boolean LAST_SPAWN;
	
	public Hermuncus(NpcInstance actor)
	{
		super(actor);
		LAST_SPAWN = actor.getParameter("lastSpawn", false);
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if (!LAST_SPAWN)
		{
			getActor().setNpcState(1);
		}
	}
	
	@Override
	protected void onEvtMenuSelected(Player player, int ask, int reply)
	{
		if ((ask == 10338) && (reply == 2))
		{
			player.teleToLocation(-114962, 226564, -2864, ReflectionManager.DEFAULT);
			player.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_VIEW);
		}
	}
}
