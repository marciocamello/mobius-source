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
package ai.TalkingIsland;

import java.util.concurrent.Future;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 */
public class Theodore extends DefaultAI
{
	final static NpcStringId _say = NpcStringId.AND_NOW_YOUR_JOURNEY_BEGINS;
	private Future<?> _talkTask = null;
	
	public Theodore(NpcInstance actor)
	{
		super(actor);
		getActor().setRandomWalk(false);
	}
	
	@Override
	protected void onEvtSpawn()
	{
		if (_talkTask != null)
		{
			_talkTask.cancel(true);
		}
		_talkTask = ThreadPoolManager.getInstance().scheduleAtFixedDelay(new doTalk(getActor()), 10000, 10000);
	}
	
	private static class doTalk implements Runnable
	{
		final NpcInstance _npc;
		
		public doTalk(NpcInstance npcInstance)
		{
			_npc = npcInstance;
		}
		
		@Override
		public void run()
		{
			Functions.npcSay(_npc, _say);
		}
	}
}
