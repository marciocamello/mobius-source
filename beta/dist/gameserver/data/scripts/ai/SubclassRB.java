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
package ai;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

public final class SubclassRB extends Fighter
{
	private final static int SHILLEN_MESSAGER = 27470; // GoD Update
	private final static int DEATH_LORD = 27477; // GoD Update
	private final static int KERNON = 27473; // GOD Update
	private final static int LONGHORN = 27476; // GoD Update
	private final static int CABRIOCOFFER = 31027;
	private final static int CHEST_KERNON = 31028;
	private final static int CHEST_GOLKONDA = 31029;
	private final static int CHEST_HALLATE = 31030;
	
	public SubclassRB(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance actor = getActor();
		
		switch (actor.getId())
		{
			case SHILLEN_MESSAGER:
				chestSelect(actor, CABRIOCOFFER);
				break;
			
			case DEATH_LORD:
				chestSelect(actor, CHEST_HALLATE);
				break;
			
			case KERNON:
				chestSelect(actor, CHEST_KERNON);
				break;
			
			case LONGHORN:
				chestSelect(actor, CHEST_GOLKONDA);
				break;
		}
		
		super.onEvtDead(killer);
	}
	
	private void chestSelect(NpcInstance actor, int npcId)
	{
		NpcInstance chest = NpcHolder.getInstance().getTemplate(npcId).getNewInstance();
		chest.spawnMe(actor.getLoc());
		ThreadPoolManager.getInstance().schedule(new ChestDespawnTask(chest), 120 * 1000);
	}
	
	private class ChestDespawnTask extends RunnableImpl
	{
		private final NpcInstance _chest;
		
		ChestDespawnTask(NpcInstance chest)
		{
			_chest = chest;
		}
		
		@Override
		public void runImpl()
		{
			if (_chest != null)
			{
				_chest.deleteMe();
			}
		}
	}
}
