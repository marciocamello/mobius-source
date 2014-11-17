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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10374_ThatPlaceSuccubus extends Quest implements ScriptFile
{
	private static final int NPC_ANDREI = 31292;
	private static final int NPC_AGNES = 31588;
	private static final int NPC_ZENYA = 32140;
	private static final int MirageFighter = 23186;
	private static final int WarriorMirage = 23187;
	private static final int ShooterMirage = 23188;
	private static final int ShamanMirage = 23189;
	private static final int MartyrMirage = 23190;
	private static final String Mirage_Fighter = "MirageFighter";
	private static final String Warrior_Mirage = "WarriorMirage";
	private static final String Shooter_Mirage = "ShooterMirage";
	private static final String Shaman_Mirage = "ShamanMirage";
	private static final String Martyr_Mirage = "MartyrMirage";
	
	public Q10374_ThatPlaceSuccubus()
	{
		super(false);
		addStartNpc(NPC_ANDREI);
		addTalkId(NPC_AGNES, NPC_ZENYA);
		addKillNpcWithLog(3, Mirage_Fighter, 15, MirageFighter);
		addKillNpcWithLog(3, Warrior_Mirage, 10, WarriorMirage);
		addKillNpcWithLog(3, Shooter_Mirage, 5, ShooterMirage);
		addKillNpcWithLog(3, Shaman_Mirage, 5, ShamanMirage);
		addKillNpcWithLog(3, Martyr_Mirage, 5, MartyrMirage);
		addLevelCheck(80, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "31292-07.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "31588-02.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "32140-02.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (npc.getId())
		{
			case NPC_ANDREI:
				switch (qs.getState())
				{
					case COMPLETED:
						htmltext = "31292-06.htm";
						break;
					
					case CREATED:
						if (qs.getPlayer().getLevel() >= 80)
						{
							if (qs.getPlayer().getClassId().level() >= 4)
							{
								htmltext = "31292-01.htm";
							}
							else
							{
								htmltext = "31292-04.htm";
								qs.exitCurrentQuest(true);
							}
						}
						else
						{
							htmltext = "31292-05.htm";
						}
						break;
					
					case STARTED:
						if (qs.getCond() == 1)
						{
							htmltext = "31292-08.htm";
						}
				}
				break;
			
			case NPC_AGNES:
				if (qs.isStarted())
				{
					if (qs.getCond() == 1)
					{
						htmltext = "31588-01.htm";
					}
					else if (qs.getCond() == 2)
					{
						htmltext = "31588-03.htm";
					}
				}
				break;
			
			case NPC_ZENYA:
				if (qs.isStarted())
				{
					if (qs.getCond() == 2)
					{
						htmltext = "32140-01.htm";
					}
					else if (qs.getCond() == 3)
					{
						htmltext = "32140-03.htm";
					}
					else if (qs.getCond() == 4)
					{
						htmltext = "32140-04.htm";
						qs.addExpAndSp(23747100, 27618200);
						qs.giveItems(57, 500560);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(false);
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 3) && updateKill(npc, qs))
		{
			qs.unset(Mirage_Fighter);
			qs.unset(Warrior_Mirage);
			qs.unset(Shooter_Mirage);
			qs.unset(Shaman_Mirage);
			qs.unset(Martyr_Mirage);
			qs.setCond(4);
			qs.playSound(SOUND_MIDDLE);
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
