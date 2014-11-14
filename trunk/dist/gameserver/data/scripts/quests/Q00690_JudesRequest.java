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
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00690_JudesRequest extends Quest implements ScriptFile
{
	private static final int JUDE = 32356;
	private static final int EVIL_WEAPON = 10327;
	private static final int Evil = 22399;
	private static final int EVIL_WEAPON_CHANCE = 30;
	private static final int ISawsword = 10373;
	private static final int IDisperser = 10374;
	private static final int ISpirit = 10375;
	private static final int IHeavyArms = 10376;
	private static final int ITrident = 10377;
	private static final int IHammer = 10378;
	private static final int IHand = 10379;
	private static final int IHall = 10380;
	private static final int ISpitter = 10381;
	private static final int ISawswordP = 10397;
	private static final int IDisperserP = 10398;
	private static final int ISpiritP = 10399;
	private static final int IHeavyArmsP = 10400;
	private static final int ITridentP = 10401;
	private static final int IHammerP = 10402;
	private static final int IHandP = 10403;
	private static final int IHallP = 10404;
	private static final int ISpitterP = 10405;
	
	public Q00690_JudesRequest()
	{
		super(true);
		addStartNpc(JUDE);
		addTalkId(JUDE);
		addKillId(Evil);
		addQuestItem(EVIL_WEAPON);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("jude_q0690_03.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (cond == 0)
		{
			if (qs.getPlayer().getLevel() >= 78)
			{
				htmltext = "jude_q0690_01.htm";
			}
			else
			{
				htmltext = "jude_q0690_02.htm";
			}
			
			qs.exitCurrentQuest(true);
		}
		else if ((cond == 1) && (qs.getQuestItemsCount(EVIL_WEAPON) >= 5))
		{
			if (qs.getQuestItemsCount(EVIL_WEAPON) >= 100)
			{
				switch (Rnd.get(8))
				{
					case 0:
						qs.giveItems(ISawsword, 1);
						break;
					
					case 1:
						qs.giveItems(IDisperser, 1);
						break;
					
					case 2:
						qs.giveItems(ISpirit, 1);
						break;
					
					case 3:
						qs.giveItems(IHeavyArms, 1);
						break;
					
					case 4:
						qs.giveItems(ITrident, 1);
						break;
					
					case 5:
						qs.giveItems(IHammer, 1);
						break;
					
					case 6:
						qs.giveItems(IHand, 1);
						break;
					
					case 7:
						qs.giveItems(IHall, 1);
						break;
					
					case 8:
						qs.giveItems(ISpitter, 1);
						break;
				}
				
				qs.playSound(SOUND_FINISH);
				qs.takeItems(EVIL_WEAPON, 100);
				htmltext = "jude_q0690_07.htm";
			}
			else if ((qs.getQuestItemsCount(EVIL_WEAPON) > 0) && (qs.getQuestItemsCount(EVIL_WEAPON) < 100))
			{
				switch (Rnd.get(8))
				{
					case 0:
						qs.giveItems(ISawswordP, 1);
						break;
					
					case 1:
						qs.giveItems(IDisperserP, 1);
						break;
					
					case 2:
						qs.giveItems(ISpiritP, 1);
						break;
					
					case 3:
						qs.giveItems(IHeavyArmsP, 1);
						break;
					
					case 4:
						qs.giveItems(ITridentP, 1);
						break;
					
					case 5:
						qs.giveItems(IHammerP, 1);
						break;
					
					case 6:
						qs.giveItems(IHandP, 1);
						break;
					
					case 7:
						qs.giveItems(IHallP, 1);
						break;
					
					case 8:
						qs.giveItems(ISpitterP, 1);
						break;
				}
				
				qs.playSound(SOUND_FINISH);
				qs.takeItems(EVIL_WEAPON, 5);
				htmltext = "jude_q0690_09.htm";
			}
		}
		else
		{
			htmltext = "jude_q0690_10.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getRandomPartyMember(STARTED, Config.ALT_PARTY_DISTRIBUTION_RANGE);
		
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		if (player != null)
		{
			final QuestState state = player.getQuestState(qs.getQuest().getName());
			
			if ((state != null) && Rnd.chance(EVIL_WEAPON_CHANCE))
			{
				qs.giveItems(EVIL_WEAPON, 1);
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
