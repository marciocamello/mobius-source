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
package ai.Octavis;

import instances.OctavisInstance;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius and Nache
 */

public class Octavis extends Fighter
{
	public static final Location LAIR_CENTER = new Location(207190, 120574, -10009);
	
	// NPCs
	
	// Octavis Beasts
	private static final int OCTAVIS_LIGHT_BEAST = 29192;
	private static final int OCTAVIS_HARD_BEAST = 29210;
	
	// Npcs Area
	private static final int VOLCANO_NPC = 19161;
	private static final int OCTAVIS_POWER_NPC = 18984;
	
	// Octavis
	private static final int OCTAVIS_LIGHT_SECOND = 29193;
	private static final int OCTAVIS_LIGHT_THIRD = 29194;
	private static final int OCTAVIS_HARD_SECOND = 29211;
	private static final int OCTAVIS_HARD_THIRD = 29212;
	
	// Skills
	public static final Skill VOLCANO_ZONE = SkillTable.getInstance().getInfo(14025, 1);
	public static final Skill OCTAVIS_POWER1 = SkillTable.getInstance().getInfo(14028, 1);
	public static final Skill OCTAVIS_POWER2 = SkillTable.getInstance().getInfo(14029, 1);
	
	public Octavis(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return false;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		final NpcInstance npc = getActor();
		
		switch (npc.getId())
		{
			case OCTAVIS_POWER_NPC:
			{
				npc.setRandomWalk(false);
				npc.setTargetable(false);
				npc.setIsInvul(true);
				return;
			}
			case VOLCANO_NPC:
			{
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						OctavisInstance refl = null;
						if (npc.getReflection() instanceof OctavisInstance)
						{
							refl = (OctavisInstance) npc.getReflection();
						}
						if (refl != null)
						{
							if ((refl.status == 1) && (refl.volcanos != null) && !refl.volcanos.isEmpty())
							{
								for (NpcInstance volcano : refl.volcanos)
								{
									volcano.setRandomWalk(false);
									volcano.setIsInvul(true);
									volcano.setTargetable(false);
									
									for (Player player : refl.getPlayers())
									{
										if (Rnd.chance(50))
										{
											volcano.teleToLocation(player.getLoc(), refl);
											volcano.setTarget(volcano);
											volcano.doCast(VOLCANO_ZONE, volcano, true);
										}
									}
								}
							}
						}
						ThreadPoolManager.getInstance().schedule(this, 5000);
					}
				}, 5000);
				break;
			}
			case OCTAVIS_LIGHT_THIRD:
			case OCTAVIS_HARD_THIRD:
			{
				npc.setIsInvul(false);
				
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						OctavisInstance refl = null;
						if (npc.getReflection() instanceof OctavisInstance)
						{
							refl = (OctavisInstance) npc.getReflection();
						}
						if (refl != null)
						{
							if ((refl.volcanos != null) && (refl.status == 3))
							{
								if (Rnd.chance(50))
								{
									NpcInstance volcano = refl.volcanos.get(0);
									volcano.teleToLocation(LAIR_CENTER, refl);
									Skill skill = Rnd.chance(50) ? OCTAVIS_POWER1 : OCTAVIS_POWER2;
									volcano.doCast(skill, volcano, true);
									
									if (skill == OCTAVIS_POWER1)
									{
										refl.octavisPower.teleToLocation(LAIR_CENTER, refl);
										refl.octavisPower.setNpcState((refl.octavisPower.getNpcState() + 1) % 7);
									}
								}
							}
						}
						ThreadPoolManager.getInstance().schedule(this, 20000);
					}
				}, 20000);
				break;
			}
			default:
				super.onEvtSpawn();
				break;
		}
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance npc = getActor();
		OctavisInstance refl = null;
		if (npc.getReflection() instanceof OctavisInstance)
		{
			refl = (OctavisInstance) npc.getReflection();
		}
		if (refl != null)
		{
			if ((npc.getId() == OCTAVIS_LIGHT_BEAST) || (npc.getId() == OCTAVIS_HARD_BEAST))
			{
				return;
			}
			
			NpcInstance octavis = findOctavis(refl);
			if (octavis == null)
			{
				return;
			}
			if (((npc.getId() == OCTAVIS_LIGHT_SECOND) || (npc.getId() == OCTAVIS_HARD_SECOND)) && ((npc.getCurrentHp() / npc.getMaxHp()) <= 0.01D) && (refl.status == 2))
			{
				refl.nextSpawn();
				npc.deleteMe();
			}
			else if (((npc.getId() == OCTAVIS_LIGHT_THIRD) || (npc.getId() == OCTAVIS_HARD_THIRD)) && ((npc.getCurrentHp() / npc.getMaxHp()) <= 0.01D) && (refl.status == 3))
			{
				npc.doDie(attacker);
				npc.decayMe();
				refl.nextSpawn();
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	public NpcInstance findOctavis(OctavisInstance instance)
	{
		NpcInstance octavis = null;
		for (NpcInstance instanceNpc : instance.getNpcs())
		{
			int npcId = instanceNpc.getId();
			if ((npcId == OCTAVIS_LIGHT_SECOND) || (npcId == OCTAVIS_HARD_SECOND) || (npcId == OCTAVIS_LIGHT_THIRD) || (npcId == OCTAVIS_HARD_THIRD))
			{
				octavis = instanceNpc;
				break;
			}
		}
		return octavis;
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		NpcInstance actor = getActor();
		if ((actor.getId() == OCTAVIS_LIGHT_SECOND) || (actor.getId() == OCTAVIS_HARD_SECOND) || (actor.getId() == OCTAVIS_LIGHT_THIRD) || (actor.getId() == OCTAVIS_HARD_THIRD))
		{
			for (Player p : actor.getReflection().getPlayers())
			{
				actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
			}
			return;
		}
		super.onEvtAggression(target, aggro);
	}
}