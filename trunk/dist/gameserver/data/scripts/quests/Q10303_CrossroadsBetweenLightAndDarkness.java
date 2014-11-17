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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;

/**
 * @author pchayka
 */
public abstract class Q10303_CrossroadsBetweenLightAndDarkness extends Quest
{
	private static final int YONA = 32909;
	private static final int SECRET_ZHREC = 33343;
	private static final int DARKSTONE = 17747;
	private final int[] locMobs =
	{
		22895,
		22887,
		22879,
		22871,
		22863
	};
	
	public Q10303_CrossroadsBetweenLightAndDarkness()
	{
		super(false);
		addKillId(locMobs);
		addQuestItem(DARKSTONE);
		addTalkId(YONA);
		addTalkId(SECRET_ZHREC);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "32909-5.htm":
				qs.takeItems(57, 465855);
				qs.addExpAndSp(6730155, 2847330);
				qs.takeItems(DARKSTONE, -1);
				qs.giveItems(getRndRewardYona(), 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
			
			case "33343-5.htm":
				qs.takeItems(57, 465855);
				qs.addExpAndSp(6730155, 2847330);
				qs.giveItems(getRndRewardZhrec(), 1);
				qs.playSound(SOUND_FINISH);
				qs.setState(COMPLETED);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if (qs.getPlayer().getLevel() < 90)
		{
			return null;
		}
		
		if (qs.getState() == COMPLETED)
		{
			return null;
		}
		
		if ((qs.getCond() == 0) && (qs.getState() == CREATED) && (Rnd.get(1000) == 3))
		{
			qs.setState(STARTED);
			return null;
		}
		else if ((cond == 1) && Rnd.chance(5))
		{
			if (qs.getQuestItemsCount(DARKSTONE) == 0)
			{
				qs.giveItems(DARKSTONE, 1);
			}
			
			return null;
		}
		
		return null;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case YONA:
				if (qs.getState() == COMPLETED)
				{
					return "32909-comp.htm";
				}
				if (qs.getPlayer().getLevel() < 90)
				{
					return "32909-lvl.htm";
				}
				if ((cond == 1) && (qs.getQuestItemsCount(DARKSTONE) >= 1))
				{
					return "32909.htm";
				}
				break;
			
			case SECRET_ZHREC:
				if (qs.getState() == COMPLETED)
				{
					return "33343-comp.htm";
				}
				if (qs.getPlayer().getLevel() < 90)
				{
					return "33343-lvl.htm";
				}
				if ((cond == 1) && (qs.getQuestItemsCount(DARKSTONE) >= 1))
				{
					return "33343.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		if (player.getLevel() < 90)
		{
			return false;
		}
		
		QuestState questState = player.getQuestState(Q10303_CrossroadsBetweenLightAndDarkness.this.getClass());
		
		if ((questState == null) || (questState.getState() != STARTED))
		{
			return false;
		}
		
		return true;
	}
	
	private static int getRndRewardYona()
	{
		switch (Rnd.get(12))
		{
			case 1:
			case 2:
			case 3:
				return 13505;
				
			case 4:
			case 5:
			case 6:
				return 16108;
				
			case 7:
			case 8:
			case 9:
				return 16102;
				
			case 10:
			case 11:
			case 12:
				return 16105;
		}
		
		return 57;
	}
	
	private static int getRndRewardZhrec()
	{
		switch (Rnd.get(12))
		{
			case 1:
			case 2:
			case 3:
				return 16101;
				
			case 4:
			case 5:
			case 6:
				return 16100;
				
			case 7:
			case 8:
			case 9:
				return 16099;
				
			case 10:
			case 11:
			case 12:
				return 16098;
		}
		
		return 57;
	}
}
