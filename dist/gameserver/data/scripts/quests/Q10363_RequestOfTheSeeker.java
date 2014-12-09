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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public class Q10363_RequestOfTheSeeker extends Quest implements ScriptFile
{
	private static final int Nagel = 33450;
	private static final int Celin = 33451;
	private static final int soul1 = 19157;
	private static final int soul2 = 19158;
	private static final int corps1 = 32961;
	private static final int corps2 = 32962;
	private static final int corps3 = 32963;
	private static final int corps4 = 32964;
	
	public Q10363_RequestOfTheSeeker()
	{
		super(false);
		addStartNpc(Nagel);
		addTalkId(Celin);
		addTalkId(Nagel);
		addLevelCheck(12, 20);
		addQuestCompletedCheck(Q10362_CertificationOfTheSeeker.class);
	}
	
	@Override
	public void onSocialActionUse(QuestState qs, int actionId)
	{
		final Player player = qs.getPlayer();
		final GameObject npc1 = player.getTarget();
		
		if ((player.getTarget() == null) || !player.getTarget().isNpc() || ((NpcInstance) npc1).isDead())
		{
			return;
		}
		
		final int target = ((NpcInstance) npc1).getId();
		final double dist = player.getDistance(npc1);
		final int cond = qs.getCond();
		
		if (actionId == SocialAction.SORROW)
		{
			if ((dist < 70) && ((target == corps1) || (target == corps2) || (target == corps3) || (target == corps4)))
			{
				switch (cond)
				{
					case 1:
						player.sendPacket(new ExShowScreenMessage(NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_ONE_CORPSE, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.setCond(2);
						qs.playSound(SOUND_MIDDLE);
						((NpcInstance) npc1).doDie(player);
						break;
					
					case 2:
						player.sendPacket(new ExShowScreenMessage(NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_SECOND_CORPSE, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.setCond(3);
						qs.playSound(SOUND_MIDDLE);
						((NpcInstance) npc1).doDie(player);
						break;
					
					case 3:
						player.sendPacket(new ExShowScreenMessage(NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_THIRD_CORPSE, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.setCond(4);
						qs.playSound(SOUND_MIDDLE);
						((NpcInstance) npc1).doDie(player);
						break;
					
					case 4:
						player.sendPacket(new ExShowScreenMessage(NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_FOURTH_CORPSE, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.setCond(5);
						qs.playSound(SOUND_MIDDLE);
						((NpcInstance) npc1).doDie(player);
						break;
					
					case 5:
						player.sendPacket(new ExShowScreenMessage(NpcString.YOU_SHOWN_YOUR_CONDOLENCES_TO_FIFTH_CORPSE, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.setCond(6);
						qs.playSound(SOUND_MIDDLE);
						((NpcInstance) npc1).doDie(player);
						break;
				}
				
				if (cond == 6)
				{
					player.sendPacket(new ExShowScreenMessage(NpcString.GRUDGE_OF_YE_SAGIRA_VICTIMS_HAVE_BEEN_RELIEVED_WITH_YOUR_TEARS, 4500, ScreenMessageAlign.TOP_CENTER));
					npc1.deleteMe();
				}
			}
			else if ((dist >= 70) && ((target == corps1) || (target == corps2) || (target == corps3) || (target == corps4)))
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.YOU_ARE_TOO_FAR_FROM_CORPSE_TO_SHOW_YOUR_CONDOLENCES, 4500, ScreenMessageAlign.TOP_CENTER));
			}
		}
		else if ((actionId == SocialAction.LAUGH) || (actionId == SocialAction.DANCE))
		{
			if ((dist < 70) && ((target == corps1) || (target == corps2) || (target == corps3) || (target == corps4)))
			{
				if ((cond == 1) || (cond == 2) || (cond == 3) || (cond == 4) || (cond == 5) || (cond == 6))
				{
					player.sendPacket(new ExShowScreenMessage(NpcString.DONT_TOY_WITH_DEAD, 4500, ScreenMessageAlign.TOP_CENTER));
					NpcInstance asa = NpcUtils.spawnSingle(soul1, new Location(player.getX() - Rnd.get(100), player.getY() - Rnd.get(100), player.getZ(), 0));
					asa.getAggroList().addDamageHate(qs.getPlayer(), 0, 10000);
					asa.setAggressionTarget(player);
					NpcInstance ass = NpcUtils.spawnSingle(soul2, new Location(player.getX() - Rnd.get(100), player.getY() - Rnd.get(100), player.getZ(), 0));
					ass.getAggroList().addDamageHate(qs.getPlayer(), 0, 10000);
					ass.setAggressionTarget(player);
					((NpcInstance) npc1).doDie(player);
				}
			}
			else if ((dist >= 70) && ((target == corps1) || (target == corps2) || (target == corps3) || (target == corps4)))
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.YOU_ARE_TOO_FAR_FROM_THE_CORPSE_TO_DO_THAT, 4500, ScreenMessageAlign.TOP_CENTER));
			}
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-3.htm";
				break;
			
			case "qet_rev":
				htmltext = "1-3.htm";
				qs.getPlayer().addExpAndSp(70200, 810);
				qs.giveItems(57, 479);
				qs.giveItems(1060, 100);
				qs.giveItems(43, 1);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Nagel:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-4.htm";
				}
				else if (cond == 6)
				{
					htmltext = "0-5.htm";
					qs.setCond(7);
				}
				else if (cond == 7)
				{
					htmltext = "0-6.htm";
				}
				else
				{
					htmltext = TODO_FIND_HTML;
				}
				break;
			
			case Celin:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 7)
				{
					htmltext = "1-1.htm";
				}
				break;
		}
		
		return htmltext;
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
