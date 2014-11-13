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

import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

public class Q00307_ControlDeviceOfTheGiants extends Quest implements ScriptFile
{
	// Npc
	private static final int Droph = 32711;
	// Monster
	private static final int HekatonPrime = 25687;
	// Items
	private static final int DrophsSupportItems = 14850;
	private static final int CaveExplorationText1Sheet = 14851;
	private static final int CaveExplorationText2Sheet = 14852;
	private static final int CaveExplorationText3Sheet = 14853;
	// Others
	private static final long HekatonPrimeRespawn = 12 * 3600 * 1000L;
	private static final Location GorgolosLoc = new Location(186096, 61501, -4075, 0);
	private static final Location LastTitanUtenusLoc = new Location(186730, 56456, -4555, 0);
	private static final Location GiantMarpanakLoc = new Location(194057, 53722, -4259, 0);
	private static final Location HekatonPrimeLoc = new Location(192328, 56120, -7651, 0);
	
	public Q00307_ControlDeviceOfTheGiants()
	{
		super(true);
		addStartNpc(Droph);
		addTalkId(Droph);
		addKillId(HekatonPrime);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "droph_q307_2.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				break;
			
			case "loc1":
				htmltext = "droph_q307_2a_1.htm";
				qs.getPlayer().sendPacket(new RadarControl(0, 1, GorgolosLoc));
				break;
			
			case "loc2":
				htmltext = "droph_q307_2a_2.htm";
				qs.getPlayer().sendPacket(new RadarControl(0, 1, LastTitanUtenusLoc));
				break;
			
			case "loc3":
				htmltext = "droph_q307_2a_3.htm";
				qs.getPlayer().sendPacket(new RadarControl(0, 1, GiantMarpanakLoc));
				break;
			
			case "summon_rb":
				if ((ServerVariables.getLong("HekatonPrimeRespawn", 0) < System.currentTimeMillis()) && (qs.getQuestItemsCount(CaveExplorationText1Sheet) >= 1) && (qs.getQuestItemsCount(CaveExplorationText2Sheet) >= 1) && (qs.getQuestItemsCount(CaveExplorationText3Sheet) >= 1))
				{
					qs.takeItems(CaveExplorationText1Sheet, 1);
					qs.takeItems(CaveExplorationText2Sheet, 1);
					qs.takeItems(CaveExplorationText3Sheet, 1);
					ServerVariables.set("HekatonPrimeRespawn", System.currentTimeMillis() + HekatonPrimeRespawn);
					NpcInstance boss = qs.addSpawn(HekatonPrime, HekatonPrimeLoc.getX(), HekatonPrimeLoc.getY(), HekatonPrimeLoc.getZ(), HekatonPrimeLoc.getHeading(), 0, 0);
					boss.getMinionList().spawnMinions();
					htmltext = "droph_q307_3a.htm";
				}
				else
				{
					htmltext = "droph_q307_2b.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 79)
				{
					htmltext = "droph_q307_1.htm";
				}
				else
				{
					htmltext = "droph_q307_0.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if ((qs.getQuestItemsCount(CaveExplorationText1Sheet) >= 1) && (qs.getQuestItemsCount(CaveExplorationText2Sheet) >= 1) && (qs.getQuestItemsCount(CaveExplorationText3Sheet) >= 1))
				{
					if (ServerVariables.getLong("HekatonPrimeRespawn", 0) < System.currentTimeMillis())
					{
						htmltext = "droph_q307_3.htm";
					}
					else
					{
						htmltext = "droph_q307_4.htm";
					}
				}
				else
				{
					htmltext = "droph_q307_2a.htm";
				}
				break;
			
			case 2:
				htmltext = "droph_q307_5.htm";
				qs.giveItems(DrophsSupportItems, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.setCond(2);
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
