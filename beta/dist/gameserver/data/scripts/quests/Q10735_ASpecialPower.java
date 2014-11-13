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
package quests;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author blacksmoke
 */
public class Q10735_ASpecialPower extends Quest implements ScriptFile
{
	private static final int Ayanthe = 33942;
	private static final int Ayanthe_Instance = 33944;
	private static final int Floato = 27526;
	private static final int Ratel = 27527;
	private int killedFloato;
	private int killedRatel;
	
	public Q10735_ASpecialPower()
	{
		super(false);
		addStartNpc(Ayanthe, Ayanthe_Instance);
		addTalkId(Ayanthe, Ayanthe_Instance);
		addKillId(Floato, Ratel);
		addLevelCheck(4, 20);
		addQuestCompletedCheck(Q10734_DoOrDie.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		List<Creature> target = new ArrayList<>();
		target.add(player);
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "33942-2.htm";
				break;
			
			case "enter_instance":
				if (qs.getCond() == 1)
				{
					enterInstance(player, 251);
					return null;
				}
				break;
			
			case "more_monsters":
				htmltext = "33944-3.htm";
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ATTACK_THE_MONSTER, 4500, ScreenMessageAlign.TOP_CENTER));
				break;
			
			case "skill_fight":
				qs.setCond(6);
				htmltext = "33944-7.htm";
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.FIGHT_USING_SKILLS, 4500, ScreenMessageAlign.TOP_CENTER));
				qs.getPlayer().getReflection().addSpawnWithoutRespawn(Ratel, new Location(-75112, 240760, -3615, 0), 0);
				qs.getPlayer().getReflection().addSpawnWithoutRespawn(Ratel, new Location(-75016, 240456, -3628, 0), 0);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Ayanthe:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()) && (qs.getPlayer().getClassId().getId() == 183))
						{
							htmltext = "33942-1.htm";
						}
						break;
					
					case 1:
						htmltext = "33942-2.htm";
						break;
					
					case 7:
						htmltext = "33942-3.htm";
						qs.giveItems(57, 900);
						qs.giveItems(2509, 500);
						qs.getPlayer().addExpAndSp(3154, 0);
						qs.exitCurrentQuest(false);
						qs.playSound(SOUND_FINISH);
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
			
			case Ayanthe_Instance:
				switch (cond)
				{
					case 1:
						htmltext = "33944-1.htm";
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(2);
						
						qs.getPlayer().getReflection().addSpawnWithoutRespawn(Floato, new Location(-75112, 240760, -3615, 0), 0);
						qs.getPlayer().getReflection().addSpawnWithoutRespawn(Floato, new Location(-75016, 240456, -3628, 0), 0);
						qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ATTACK_THE_MONSTER, 4500, ScreenMessageAlign.TOP_CENTER));
						
						break;
					
					case 2:
						htmltext = "33944-2.htm";
						break;
					
					case 3:
						if (killedFloato == 2)
						{
							// Automate Spiritshot as shown in the Tutorial.
							qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_AUTOMATE_SOULSHOTS, 4500, ScreenMessageAlign.TOP_CENTER));
							qs.showTutorialHTML(TutorialShowHtml.QT_003, TutorialShowHtml.TYPE_WINDOW);
							htmltext = "33944-4.htm";
							qs.giveItems(2509, 150); // 150 Spiritshot
							killedFloato = 0;
						}
						else
						{
							htmltext = "33944-5.htm";
							qs.getPlayer().getReflection().addSpawnWithoutRespawn(Floato, new Location(-75112, 240760, -3615, 0), 0);
							qs.getPlayer().getReflection().addSpawnWithoutRespawn(Floato, new Location(-75016, 240456, -3628, 0), 0);
							qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.ATTACK_THE_MONSTER, 4500, ScreenMessageAlign.TOP_CENTER));
							qs.playSound(SOUND_MIDDLE);
							qs.setCond(4);
						}
						break;
					
					case 4:
						htmltext = "33944-2.htm";
						break;
					
					case 5:
						killedFloato = 0;
						htmltext = "33944-6.htm";
						qs.showTutorialHTML(TutorialShowHtml.QT_004, TutorialShowHtml.TYPE_WINDOW);
						break;
					
					case 6:
						htmltext = "33944-2.htm";
						break;
					
					case 7:
						htmltext = "33944-8.htm";
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	private void enterInstance(Player player, int instancedZoneId)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(instancedZoneId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(instancedZoneId))
		{
			ReflectionUtils.enterReflection(player, instancedZoneId);
		}
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		switch (npcId)
		{
			case Floato:
				if ((cond == 2) || (cond == 4))
				{
					qs.playSound(SOUND_ITEMGET);
					++killedFloato;
					if (killedFloato >= 2)
					{
						qs.setCond(cond + 1);
						qs.playSound(SOUND_MIDDLE);
					}
				}
				break;
			
			case Ratel:
				if (cond == 6)
				{
					qs.playSound(SOUND_ITEMGET);
					++killedRatel;
					if (killedRatel >= 2)
					{
						qs.setCond(cond + 1);
						qs.playSound(SOUND_MIDDLE);
						qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.TALK_TO_AYANTHE_TO_LEAVE_THE_TRAINING_GROUNDS, 4500, ScreenMessageAlign.TOP_CENTER));
						killedRatel = 0;
					}
				}
				break;
		}
		
		return null;
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}