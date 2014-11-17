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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

public class Q10272_LightFragment extends Quest implements ScriptFile
{
	// Npcs
	private static final int Orbyu = 32560;
	private static final int Artius = 32559;
	private static final int Lelikia = 32567;
	private static final int Ginby = 32566;
	private static final int Lekon = 32557;
	// Items
	private static final int DestroyedDarknessFragmentPowder = 13853;
	private static final int DestroyedLightFragmentPowder = 13854;
	private static final int SacredLightFragment = 13855;
	// Other
	private static final Location LELIKIA_POSITION = new Location(-170936, 247768, 1102);
	private static final Location BASE_POSITION = new Location(-185032, 242824, 1553);
	
	public Q10272_LightFragment()
	{
		super(true);
		addStartNpc(Orbyu);
		addTalkId(Orbyu, Artius, Lelikia, Ginby, Lekon);
		addKillId(22552, 22541, 22550, 22551, 22596, 22544, 22540, 22547, 22542, 22543, 22539, 22546, 22548, 22536, 22538, 22537);
		addQuestItem(DestroyedDarknessFragmentPowder, DestroyedLightFragmentPowder, SacredLightFragment);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "orbyu_q10272_2.htm":
				if (qs.getCond() == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "artius_q10272_2.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "artius_q10272_4.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "tele_to_lelikia":
				if (qs.getQuestItemsCount(ADENA_ID) >= 10000)
				{
					qs.takeItems(ADENA_ID, 10000);
					qs.getPlayer().teleToLocation(LELIKIA_POSITION);
					return null;
				}
				qs.getPlayer().sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
				return null;
				
			case "lelikia_q10272_2.htm":
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "tele_to_base":
				qs.getPlayer().teleToLocation(BASE_POSITION);
				return null;
				
			case "artius_q10272_7.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "artius_q10272_9.htm":
				qs.setCond(6);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "artius_q10272_11.htm":
				qs.setCond(7);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "lekon_q10272_2.htm":
				if (qs.getQuestItemsCount(DestroyedLightFragmentPowder) >= 100)
				{
					qs.takeItems(DestroyedLightFragmentPowder, -1);
					qs.giveItems(SacredLightFragment, 1);
					qs.setCond(8);
					qs.playSound(SOUND_MIDDLE);
				}
				else
				{
					htmltext = "lekon_q10272_1a.htm";
				}
				break;
			
			case "artius_q10272_12.htm":
				qs.giveItems(ADENA_ID, 2219330);
				qs.addExpAndSp(2458030, 600000);
				qs.setState(COMPLETED);
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
			case Orbyu:
				if (cond == 0)
				{
					final QuestState TheEnvelopingDarkness = qs.getPlayer().getQuestState(Q10271_TheEnvelopingDarkness.class);
					
					if ((qs.getPlayer().getLevel() >= 75) && (TheEnvelopingDarkness != null) && TheEnvelopingDarkness.isCompleted())
					{
						htmltext = "orbyu_q10272_1.htm";
					}
					else
					{
						htmltext = "orbyu_q10272_0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 4)
				{
					htmltext = "orbyu_q10271_4.htm";
				}
				break;
			
			case Artius:
				switch (cond)
				{
					case 1:
						htmltext = "artius_q10272_1.htm";
						break;
					
					case 2:
						htmltext = "artius_q10272_3.htm";
						break;
					
					case 4:
						htmltext = "artius_q10272_5.htm";
						break;
					
					case 5:
						if (qs.getQuestItemsCount(DestroyedDarknessFragmentPowder) >= 100)
						{
							htmltext = "artius_q10272_8.htm";
						}
						else
						{
							htmltext = "artius_q10272_8a.htm";
						}
						break;
					
					case 6:
						if (qs.getQuestItemsCount(DestroyedLightFragmentPowder) >= 100)
						{
							htmltext = "artius_q10272_10.htm";
						}
						else
						{
							htmltext = "artius_q10272_10a.htm";
						}
						break;
					
					case 8:
						htmltext = "artius_q10272_12.htm";
						break;
				}
				break;
			
			case Ginby:
				if (cond == 3)
				{
					htmltext = "ginby_q10272_1.htm";
				}
				break;
			
			case Lelikia:
				if (cond == 3)
				{
					htmltext = "lelikia_q10272_1.htm";
				}
				break;
			
			case Lekon:
				if ((cond == 7) && (qs.getQuestItemsCount(DestroyedLightFragmentPowder) >= 100))
				{
					htmltext = "lekon_q10272_1.htm";
				}
				else
				{
					htmltext = "lekon_q10272_1a.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 5)
		{
			if (qs.getQuestItemsCount(DestroyedDarknessFragmentPowder) <= 100)
			{
				qs.giveItems(DestroyedDarknessFragmentPowder, 1);
				qs.playSound(SOUND_ITEMGET);
			}
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
