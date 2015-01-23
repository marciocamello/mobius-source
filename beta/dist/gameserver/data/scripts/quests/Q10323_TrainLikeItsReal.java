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
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Krash
 */
public class Q10323_TrainLikeItsReal extends Quest implements ScriptFile
{
	// Npcs
	private static final int Evain = 33464;
	private static final int Holden = 33194;
	private static final int Shannon = 32974;
	// Mobs
	private static final int Training_Golem = 27532;
	private int killedGolem;
	// Items
	private static final int Soulshots = 1835;
	private static final int Spiritshots = 2509;
	
	public Q10323_TrainLikeItsReal()
	{
		super(false);
		addStartNpc(Evain);
		addTalkId(Holden, Evain, Shannon);
		addKillId(Training_Golem);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10322_SearchingForTheMysteriousPower.class);
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
				htmltext = "33464-03.htm";
				break;
			
			case "start_golem":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "33194-03.htm";
				break;
			
			case "getshots":
				qs.playSound(SOUND_MIDDLE);
				qs.showTutorialHTML(TutorialShowHtml.QT_003, TutorialShowHtml.TYPE_WINDOW);
				if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
				{
					player.sendPacket(new ExShowScreenMessage(NpcStringId.SOULSHOT_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
					qs.startQuestTimer("soul_timer", 4500);
					qs.showTutorialHTML(TutorialShowHtml.QT_003, TutorialShowHtml.TYPE_WINDOW);
					if (!player.getVarB("Q10323_SS_Rewarded"))
					{
						player.setVar("Q10323_SS_Rewarded", true, -1);
						qs.giveItems(Soulshots, 500);
					}
					qs.setCond(4);
				}
				else
				{
					player.sendPacket(new ExShowScreenMessage(NpcStringId.SPIRITSHOT_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
					qs.startQuestTimer("spirit_timer", 4500);
					qs.showTutorialHTML(TutorialShowHtml.QT_003, TutorialShowHtml.TYPE_WINDOW);
					if (!player.getVarB("Q10323_SS_Rewarded"))
					{
						player.setVar("Q10323_SS_Rewarded", true, -1);
						qs.giveItems(Spiritshots, 500);
					}
					qs.setCond(5);
				}
				return null;
				
			case "soul_timer":
				htmltext = "33194-05.htm";
				player.sendPacket(new ExShowScreenMessage(NpcStringId.AUTOMATE_SOULSHOT_AS_SHOWN_IN_THE_TUTORIAL, 4500, ScreenMessageAlign.TOP_CENTER));
				break;
			
			case "spirit_timer":
				htmltext = "33194-05.htm";
				player.sendPacket(new ExShowScreenMessage(NpcStringId.AUTOMATE_SPIRITSHOT_AS_SHOWN_IN_THE_TUTORIAL, 4500, ScreenMessageAlign.TOP_CENTER));
				break;
			
			case "quest_middle":
				qs.setCond(9);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "33194-08.htm";
				break;
			
			case "32974-02.htm":
				qs.getPlayer().addExpAndSp(1700, 5);
				qs.giveItems(57, 90);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
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
		
		switch (npc.getId())
		{
			case Evain:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							htmltext = "33464-01.htm";
						}
						else if (cond == 1)
						{
							htmltext = "33464-03.htm";
						}
						break;
				}
				
			case Holden:
				switch (cond)
				{
					case 1:
					{
						htmltext = "33194-01.htm";
						break;
					}
					case 3:
					{
						htmltext = "33194-04.htm";
						break;
					}
					case 4:
					{
						htmltext = "33194-06.htm";
						qs.setCond(6);
						break;
					}
					case 5:
					{
						htmltext = "33194-06.htm";
						qs.setCond(7);
						break;
					}
					case 8:
					{
						htmltext = "33194-07.htm";
						break;
					}
				}
				break;
			
			case Shannon:
				switch (cond)
				{
					case 9:
					{
						htmltext = "32974-01.htm";
						break;
					}
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		switch (qs.getCond())
		{
			case 2:
				qs.playSound(SOUND_ITEMGET);
				++killedGolem;
				if (killedGolem >= 4)
				{
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
					killedGolem = 0;
				}
				break;
			
			case 6:
			case 7:
				qs.playSound(SOUND_ITEMGET);
				++killedGolem;
				if (killedGolem >= 4)
				{
					qs.setCond(8);
					qs.playSound(SOUND_MIDDLE);
					killedGolem = 0;
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
