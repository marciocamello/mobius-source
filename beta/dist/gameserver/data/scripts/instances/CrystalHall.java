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

import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.utils.Location;

/**
 * @author Awakeninger + Nache
 */
public final class CrystalHall extends Reflection
{
	// Npcs
	private static final int RB1 = 25881;
	private static final int RB2 = 25881;
	private static final int Cannon1 = 19008;
	private static final int Cannon2 = 19008;
	private static final int Cannon3 = 19008;
	private static final int Cannon4 = 19008;
	private static final int Cannon5 = 19008;
	private static final int Cannon6 = 19008;
	private static final int Cannon7 = 19008;
	private static final int Cannon8 = 19009;
	private static final int Exchanger = 33388;
	// Doors
	private static final int DoorOutside = 24220005;
	private static final int DoorInside = 24220006;
	// Locations
	private final Location Cannon1Loc = new Location(143144, 145832, -12061);
	private final Location Cannon2Loc = new Location(141912, 144200, -11949);
	private final Location Cannon3Loc = new Location(143368, 143768, -11976);
	private final Location Cannon4Loc = new Location(145544, 143746, -11841);
	private final Location Cannon5Loc = new Location(147544, 144872, -12251);
	private final Location Cannon6Loc = new Location(148952, 145224, -12326);
	private final Location Cannon7Loc = new Location(148152, 146136, -12305);
	private final Location Cannon8Loc = new Location(149096, 146872, -12369);
	private final Location RB1Loc = new Location(152984, 145960, -12609, 15640);
	public final Location RB2Loc = new Location(152536, 145960, -12609, 15640);
	// Other
	private long _savedTime;
	NpcInstance can8 = null;
	NpcInstance exchange;
	private final DeathListener _deathListener = new DeathListener();
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		_savedTime = System.currentTimeMillis();
		
		can8 = addSpawnWithoutRespawn(Cannon8, Cannon8Loc, 0);
		can8.addListener(_deathListener);
		can8.setIsInvul(true);
		NpcInstance can1 = addSpawnWithoutRespawn(Cannon1, Cannon1Loc, 0);
		can1.addListener(_deathListener);
		NpcInstance can2 = addSpawnWithoutRespawn(Cannon2, Cannon2Loc, 0);
		can2.addListener(_deathListener);
		NpcInstance can3 = addSpawnWithoutRespawn(Cannon3, Cannon3Loc, 0);
		can3.addListener(_deathListener);
		NpcInstance can4 = addSpawnWithoutRespawn(Cannon4, Cannon4Loc, 0);
		can4.addListener(_deathListener);
		NpcInstance can5 = addSpawnWithoutRespawn(Cannon5, Cannon5Loc, 0);
		can5.addListener(_deathListener);
		NpcInstance can6 = addSpawnWithoutRespawn(Cannon6, Cannon6Loc, 0);
		can6.addListener(_deathListener);
		NpcInstance can7 = addSpawnWithoutRespawn(Cannon7, Cannon7Loc, 0);
		can7.addListener(_deathListener);
		NpcInstance RB1N = addSpawnWithoutRespawn(RB1, RB1Loc, 0);
		RB1N.addListener(_deathListener);
		NpcInstance RB2N = addSpawnWithoutRespawn(RB2, RB2Loc, 0);
		RB2N.addListener(_deathListener);
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		player.sendPacket(new ExSendUIEvent(player, 0, 1, (int) (System.currentTimeMillis() - _savedTime) / 1000, 0, NpcStringId.ELAPSED_TIME));
		player.setVar("Cannon0", "true", -1);
		player.setVar("ED1", "true", -1);
	}
	
	@Override
	public void onPlayerExit(Player player)
	{
		super.onPlayerExit(player);
		player.sendPacket(new ExSendUIEvent(player, 1, 1, 0, 0));
	}
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc() && (self.getId() == Cannon1))
			{
				for (Player p : getPlayers())
				{
					if (p.getVar("Cannon0") != null)
					{
						p.sendPacket(new ExShowScreenMessage(NpcStringId.SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_S1, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "1"));
						p.setVar("Cannon1", "true", -1);
						p.unsetVar("Cannon0");
					}
					else if (p.getVar("Cannon1") != null)
					{
						p.sendPacket(new ExShowScreenMessage(NpcStringId.SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_S1, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "2"));
						p.setVar("Cannon2", "true", -1);
						p.unsetVar("Cannon1");
					}
					else if (p.getVar("Cannon2") != null)
					{
						p.sendPacket(new ExShowScreenMessage(NpcStringId.SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_S1, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "3"));
						p.setVar("Cannon3", "true", -1);
						p.unsetVar("Cannon2");
					}
					else if (p.getVar("Cannon3") != null)
					{
						p.sendPacket(new ExShowScreenMessage(NpcStringId.SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_S1, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "4"));
						p.setVar("Cannon4", "true", -1);
						p.unsetVar("Cannon3");
					}
					else if (p.getVar("Cannon4") != null)
					{
						p.sendPacket(new ExShowScreenMessage(NpcStringId.SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_S1, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "5"));
						p.setVar("Cannon5", "true", -1);
						p.unsetVar("Cannon4");
					}
					else if (p.getVar("Cannon5") != null)
					{
						p.sendPacket(new ExShowScreenMessage(NpcStringId.SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_S1, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "6"));
						p.setVar("Cannon6", "true", -1);
						p.unsetVar("Cannon5");
					}
					else if (p.getVar("Cannon6") != null)
					{
						p.sendPacket(new ExShowScreenMessage(NpcStringId.SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_S1, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true, "7"));
						p.setVar("Cannon7", "true", -1);
						p.unsetVar("Cannon6");
						if (can8 != null)
						{
							can8.setIsInvul(false);
						}
					}
				}
			}
			else if (self.isNpc() && (self.getId() == Cannon8))
			{
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcStringId.SUCCESSFUL_DESTRUCTION_OF_STRONGHOLD_ENTRY_ACCESSED, 12000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_CENTER, true, 1, -1, true));
					p.unsetVar("Cannon7");
				}
				getDoor(DoorOutside).openMe();
				getDoor(DoorInside).openMe();
			}
			else if (self.isNpc() && (self.getId() == RB1))
			{
				for (Player p : getPlayers())
				{
					if ((p.getVar("ED1") != null) && (p.getVar("Emam Dead") == null))
					{
						p.setVar("Emam Dead", "true", -1);
						p.unsetVar("ED1");
					}
					else if ((p.getVar("Emam Dead") != null) && (p.getVar("ED1") == null))
					{
						p.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTE_S_YOU_WILL_BE_FORCED_OUT_OF_THE_DUNGEON_WHEN_THE_TIME_EXPIRES).addInt(5));
						startCollapseTimer(5 * 60 * 1000L);
						exchange = addSpawnWithoutRespawn(Exchanger, RB2Loc, 0);
						p.unsetVar("Emam Dead");
						
					}
				}
			}
		}
	}
}