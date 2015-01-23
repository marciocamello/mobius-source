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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Nache
 */
public class Balok extends Fighter
{
	private static final int BALOK = 29218;
	private static final int ROOM_WARDEN = 23123;
	private static final Skill INVINCIBILITY_ACTIVATION = SkillTable.getInstance().getInfo(14190, 1);
	
	private static final int[][] PRISON_COORDS =
	{
		{
			153584,
			140349,
			-12832,
			16384
		},
		{
			155084,
			141214,
			-12832,
			28672
		},
		{
			155312,
			142080,
			-12832,
			32768
		},
		{
			152704,
			143600,
			-12832,
			53248
		},
		{
			154448,
			143584,
			-12832,
			45056
		},
		{
			154435,
			140601,
			-12832,
			20480
		},
		{
			151805,
			142085,
			-12832,
			0
		},
		{
			152064,
			141190,
			-12832,
			7000
		}
	};
	
	@Override
	protected void onEvtSpawn()
	{
		final NpcInstance npc = getActor();
		if (npc.getId() == ROOM_WARDEN)
		{
			for (NpcInstance warden : npc.getReflection().getAllByNpcId(ROOM_WARDEN, true))
			{
				warden.doCast(INVINCIBILITY_ACTIVATION, npc, true);
				warden.setRandomWalk(true);
				warden.startImmobilized();
			}
		}
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance npc = getActor();
		
		double lastPercentHp = (npc.getCurrentHp() + damage) / npc.getMaxHp();
		double currentPercentHp = npc.getCurrentHp() / npc.getMaxHp();
		
		if ((lastPercentHp > 0.15D) && (currentPercentHp <= 0.15D))
		{
			onPercentHpReached(npc, 15);
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	public void onPercentHpReached(NpcInstance npc, int percent)
	{
		if ((percent <= 0.15D) && (npc.getId() == BALOK))
		{
			if (Rnd.get() <= 0.3D)
			{
				npc.doCast(INVINCIBILITY_ACTIVATION, npc, true);
			}
		}
		else if (Rnd.get() <= 0.1D)
		{
			final int playerInside = npc.getReflection().getPlayers().size();
			final Player teleportPlayers = npc.getReflection().getPlayers().get(Rnd.get(playerInside));
			final int[] coords = PRISON_COORDS[Rnd.get(PRISON_COORDS.length)];
			teleportPlayers.teleToLocation(new Location(coords[0], coords[1], coords[2], coords[3]));
			for (Player player : npc.getReflection().getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcStringId.S1_LOCKED_AWAY_IN_THE_PRISON, 500, ScreenMessageAlign.MIDDLE_CENTER, String.valueOf(player.getName())));
			}
		}
		
	}
	
	public Balok(NpcInstance actor)
	{
		super(actor);
	}
}