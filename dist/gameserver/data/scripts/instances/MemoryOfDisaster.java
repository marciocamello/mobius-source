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
package instances;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MemoryOfDisaster extends Reflection
{
	/**
	 * Field ROGIN_ID. (value is 19193)
	 */
	private static final int ROGIN_ID = 19193;
	/**
	 * Field TENTACLE_ID. (value is 19171)
	 */
	private static final int TENTACLE_ID = 19171;
	/**
	 * Field TRASNPERENT_TEREDOR_1_ID. (value is 18998)
	 */
	private static final int TRASNPERENT_TEREDOR_1_ID = 18998;
	/**
	 * Field EARTH_WYRM_ID. (value is 19217)
	 */
	private static final int EARTH_WYRM_ID = 19217;
	/**
	 * Field TRANSPARENT. (value is 18919)
	 */
	private static final int TRANSPARENT = 18919;
	/**
	 * Field ELVES.
	 */
	private static final int[] ELVES =
	{
		33536,
		33538,
		33540,
		33542,
		33544,
		33546
	};
	/**
	 * Field introShowed.
	 */
	private boolean introShowed = false;
	
	/**
	 * Constructor for MemoryOfDisaster.
	 * @param player Player
	 */
	public MemoryOfDisaster(Player player)
	{
		setReturnLoc(player.getLoc());
	}
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 */
	@Override
	public void onPlayerEnter(final Player player)
	{
		if (!introShowed)
		{
			introShowed = true;
			for (Player p : getPlayers())
			{
				p.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_OPENING);
				p.sendPacket(new EventTrigger(23120700, true));
			}
			ThreadPoolManager.getInstance().schedule(new TeleportTask(), 29500L);
		}
		super.onPlayerEnter(player);
	}
	
	/**
	 * Method onPlayerExit.
	 * @param player Player
	 */
	@Override
	public void onPlayerExit(Player player)
	{
		player.setVar("GermunkusUSM", "true", -1);
		player.sendPacket(new EventTrigger(23120700, false));
		super.onPlayerExit(player);
	}
	
	/**
	 * Method spawnTentacles.
	 */
	public void spawnTentacles()
	{
		addSpawnWithoutRespawn(TENTACLE_ID, new Location(116596, -183176, -1608, 31175), 0).setParameter("notifyDie", true);
		addSpawnWithoutRespawn(TENTACLE_ID, new Location(116542, -183126, -1600, 52580), 0).setParameter("notifyDie", true);
		addSpawnWithoutRespawn(TENTACLE_ID, new Location(116542, -183189, -1608, 9413), 0).setParameter("notifyDie", true);
	}
	
	/**
	 * Method spawnTransparentTeredor.
	 */
	public void spawnTransparentTeredor()
	{
		addSpawnWithoutRespawn(TRASNPERENT_TEREDOR_1_ID, new Location(116511, -178729, -1176, 43905), 0);
	}
	
	/**
	 * Method spawnWyrm.
	 */
	public void spawnWyrm()
	{
		addSpawnWithoutRespawn(TRANSPARENT, new Location(116511, -178729, -1176, 50366), 0);
		addSpawnWithoutRespawn(EARTH_WYRM_ID, new Location(116511, -178729, -1176, 50366), 0);
	}
	
	/**
	 * Method startFinalScene.
	 */
	public void startFinalScene()
	{
		for (Player player : getPlayers())
		{
			player.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_OPENING_C);
		}
		ThreadPoolManager.getInstance().schedule(new Scene1(), 32900);
	}
	
	/**
	 * Method dieNextElf.
	 */
	public void dieNextElf()
	{
		List<NpcInstance> elves = new ArrayList<>();
		for (int id : ELVES)
		{
			elves.addAll(getAllByNpcId(id, true));
		}
		if (!elves.isEmpty())
		{
			elves.get(Rnd.get(elves.size())).getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "START_DIE");
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class TeleportTask extends RunnableImpl
	{
		/**
		 * Constructor for TeleportTask.
		 */
		public TeleportTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.teleToLocation(new Location(116063, -183167, -1488, 64960));
				player.sendPacket(new EventTrigger(23120700, true));
			}
			ThreadPoolManager.getInstance().schedule(new ScreenMessageTask(NpcString.WATCH_THE_DWARVEN_VILLAGE_LAST_STAND), 2000);
			ThreadPoolManager.getInstance().schedule(new SpawnRoginTask(), 7000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ScreenMessageTask extends RunnableImpl
	{
		/**
		 * Field msg.
		 */
		private final NpcString msg;
		
		/**
		 * Constructor for ScreenMessageTask.
		 * @param msg NpcString
		 */
		public ScreenMessageTask(NpcString msg)
		{
			this.msg = msg;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(msg, 5000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, true, ExShowScreenMessage.STRING_TYPE, -1, false, 0));
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnRoginTask extends RunnableImpl
	{
		/**
		 * Constructor for SpawnRoginTask.
		 */
		public SpawnRoginTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			addSpawnWithRespawn(ROGIN_ID, new Location(115731, -183054, -1472, 3170), 0, 0);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class Scene1 extends RunnableImpl
	{
		/**
		 * Constructor for Scene1.
		 */
		public Scene1()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.teleToLocation(new Location(10400, 17092, -4584, 44839));
				player.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_OPENING_D);
			}
			dieNextElf();
			dieNextElf();
			dieNextElf();
			dieNextElf();
			ThreadPoolManager.getInstance().schedule(new Scene2(), 83000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class Scene2 extends RunnableImpl
	{
		/**
		 * Constructor for Scene2.
		 */
		public Scene2()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_OPENING_E);
			}
			ThreadPoolManager.getInstance().schedule(new Scene3(), 30000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class Scene3 extends RunnableImpl
	{
		/**
		 * Constructor for Scene3.
		 */
		public Scene3()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_AWAKENING_OPENING_F);
			}
			ThreadPoolManager.getInstance().schedule(new EndInstanceTask(), 37500);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class EndInstanceTask extends RunnableImpl
	{
		/**
		 * Constructor for EndInstanceTask.
		 */
		public EndInstanceTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.teleToLocation(getReturnLoc(), ReflectionManager.DEFAULT);
				ThreadPoolManager.getInstance().schedule(new ShowHtmlTask(player), 1500);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ShowHtmlTask extends RunnableImpl
	{
		/**
		 * Field player.
		 */
		private final Player player;
		
		/**
		 * Constructor for ShowHtmlTask.
		 * @param player Player
		 */
		public ShowHtmlTask(Player player)
		{
			this.player = player;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (player == null)
			{
				return;
			}
			QuestState st = player.getQuestState("_255_Tutorial");
			if (st != null)
			{
				st.showTutorialHTML("hermunkus_call.html", TutorialShowHtml.TYPE_HTML);
			}
		}
	}
}
