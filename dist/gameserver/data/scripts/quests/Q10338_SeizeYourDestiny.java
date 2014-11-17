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

import instances.HarnakUndergroundRuins;
import instances.MemoryOfDisaster;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q10338_SeizeYourDestiny extends Quest implements ScriptFile
{
	private static final int CELLPHINE = 33477;
	private static final int HADEL = 33344;
	private static final int HERMUNKUS = 33340;
	private static final int HARNAK = 27445;
	private static final int SCROLL_OF_AFTERLIFE = 17600;
	private static final int INSTANCE_ID = 195;
	private static final int MEMORY_OF_DISASTER_ID = 200;
	
	public Q10338_SeizeYourDestiny()
	{
		super(false);
		addStartNpc(CELLPHINE);
		addTalkId(HADEL, HERMUNKUS);
		addFirstTalkId(HERMUNKUS);
		addKillId(HARNAK);
		addLevelCheck(85, 99);
		addClassLevelCheck(4);
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs == null)
		{
			return null;
		}
		
		final Player player = qs.getPlayer();
		
		if (player == null)
		{
			return null;
		}
		
		String htmltext = null;
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case CELLPHINE:
				if (!isAvailableFor(player))
				{
					htmltext = "noqu.htm";
				}
				else
				{
					// If a DualClass Not Awakened Yet try to do the quest reset it.
					// TODO: Check if better put this quest as repeatable by simply add more start condition or leave as this
					if (player.getSubClassList().isDualClassActive() && (player.getClassLevel() == 4))
					{
						qs.exitCurrentQuest(true);
					}
					
					switch (cond)
					{
						case 0:
							htmltext = "start.htm";
							break;
						
						case 1:
							htmltext = "0-3.htm";
					}
				}
				break;
			
			case HADEL:
				htmltext = "noqu.htm";
				switch (cond)
				{
					case 1:
						htmltext = "1-1.htm";
						break;
					
					case 2:
					case 3:
						htmltext = "1-5.htm";
						break;
				}
				break;
			
			case HERMUNKUS:
				htmltext = "noqu.htm";
				switch (cond)
				{
					case 3:
						htmltext = "2-2.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (qs == null)
		{
			return htmltext;
		}
		
		final Player player = qs.getPlayer();
		
		if (player == null)
		{
			return htmltext;
		}
		
		if (event.equals("MemoryOfDisaster"))
		{
			final Reflection r = player.getActiveReflection();
			
			if (r != null)
			{
				if (player.canReenterInstance(MEMORY_OF_DISASTER_ID))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(MEMORY_OF_DISASTER_ID))
			{
				ReflectionUtils.enterReflection(player, new MemoryOfDisaster(player), MEMORY_OF_DISASTER_ID);
			}
			
			htmltext = null;
		}
		
		if (npc == null)
		{
			return htmltext;
		}
		
		switch (npc.getId())
		{
			case CELLPHINE:
				if (event.equals("quest_ac"))
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.playSound(SOUND_ACCEPT);
					htmltext = "0-2.htm";
				}
				break;
			
			case HADEL:
				if (event.equals("1-5.htm"))
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				else if (event.equals("EnterInstance"))
				{
					final Reflection r = player.getActiveReflection();
					
					if (r != null)
					{
						if (player.canReenterInstance(INSTANCE_ID))
						{
							player.teleToLocation(r.getTeleportLoc(), r);
						}
					}
					else if (player.canEnterInstance(INSTANCE_ID))
					{
						if (qs.getCond() < 3)
						{
							ReflectionUtils.enterReflection(player, new HarnakUndergroundRuins(1), INSTANCE_ID);
						}
						else
						{
							ReflectionUtils.enterReflection(player, new HarnakUndergroundRuins(2), INSTANCE_ID);
						}
					}
					
					htmltext = null;
				}
				break;
			
			case HERMUNKUS:
				if (event.equals("accept_scroll"))
				{
					player.sendPacket(new ExShowScreenMessage(NpcString.YOU_MAY_USE_SCROLL_OF_AFTERLIFE_FROM_HERMUNCUS_TO_AWAKEN, 10000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, false, 0));
					qs.playSound(SOUND_FINISH);
					qs.giveItems(SCROLL_OF_AFTERLIFE, 1);
					qs.exitCurrentQuest(false);
					htmltext = "2-3.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState st = player.getQuestState(getClass());
		
		if (st == null)
		{
			return null;
		}
		
		if (npc.getNpcState() == 1)
		{
			return null;
		}
		else if (st.getCond() == 3)
		{
			return "2-1.htm";
		}
		else
		{
			return "2-3.htm";
		}
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		qs.setCond(3);
		qs.playSound(SOUND_MIDDLE);
		
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