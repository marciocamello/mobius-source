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

public class Q00624_TheFinestIngredientsPart1 extends Quest implements ScriptFile
{
	// Npc
	private static final int JEREMY = 31521;
	// Monsters
	private static final int HOT_SPRINGS_ATROX = 21321;
	private static final int HOT_SPRINGS_NEPENTHES = 21319;
	private static final int HOT_SPRINGS_ATROXSPAWN = 21317;
	private static final int HOT_SPRINGS_BANDERSNATCHLING = 21314;
	// Items
	private static final int SECRET_SPICE = 7204;
	private static final int TRUNK_OF_NEPENTHES = 7202;
	private static final int FOOT_OF_BANDERSNATCHLING = 7203;
	private static final int CRYOLITE = 7080;
	private static final int SAUCE = 7205;
	
	public Q00624_TheFinestIngredientsPart1()
	{
		super(true);
		addStartNpc(JEREMY);
		addKillId(HOT_SPRINGS_ATROX, HOT_SPRINGS_NEPENTHES, HOT_SPRINGS_ATROXSPAWN, HOT_SPRINGS_BANDERSNATCHLING);
		addQuestItem(TRUNK_OF_NEPENTHES, FOOT_OF_BANDERSNATCHLING, SECRET_SPICE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "jeremy_q0624_0104.htm":
				if (qs.getPlayer().getLevel() >= 73)
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.playSound(SOUND_ACCEPT);
				}
				else
				{
					htmltext = "jeremy_q0624_0103.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "jeremy_q0624_0201.htm":
				if ((qs.getQuestItemsCount(TRUNK_OF_NEPENTHES) == 50) && (qs.getQuestItemsCount(FOOT_OF_BANDERSNATCHLING) == 50) && (qs.getQuestItemsCount(SECRET_SPICE) == 50))
				{
					qs.takeItems(TRUNK_OF_NEPENTHES, -1);
					qs.takeItems(FOOT_OF_BANDERSNATCHLING, -1);
					qs.takeItems(SECRET_SPICE, -1);
					qs.playSound(SOUND_FINISH);
					qs.giveItems(SAUCE, 1);
					qs.giveItems(CRYOLITE, 1);
					htmltext = "jeremy_q0624_0201.htm";
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "jeremy_q0624_0202.htm";
					qs.setCond(1);
				}
				break;
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
			htmltext = "jeremy_q0624_0101.htm";
		}
		else if (cond != 3)
		{
			htmltext = "jeremy_q0624_0106.htm";
		}
		else
		{
			htmltext = "jeremy_q0624_0105.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		final int npcId = npc.getId();
		
		if (qs.getCond() == 1)
		{
			switch (npcId)
			{
				case HOT_SPRINGS_NEPENTHES:
					if (qs.getQuestItemsCount(TRUNK_OF_NEPENTHES) < 50)
					{
						qs.rollAndGive(TRUNK_OF_NEPENTHES, 1, 1, 50, 100);
					}
					break;
				
				case HOT_SPRINGS_BANDERSNATCHLING:
					if (qs.getQuestItemsCount(FOOT_OF_BANDERSNATCHLING) < 50)
					{
						qs.rollAndGive(FOOT_OF_BANDERSNATCHLING, 1, 1, 50, 100);
					}
					break;
				
				case HOT_SPRINGS_ATROX:
				case HOT_SPRINGS_ATROXSPAWN:
					if (qs.getQuestItemsCount(SECRET_SPICE) < 50)
					{
						qs.rollAndGive(SECRET_SPICE, 1, 1, 50, 100);
					}
					break;
			}
			
			onKillCheck(qs);
		}
		
		return null;
	}
	
	private void onKillCheck(QuestState qs)
	{
		if ((qs.getQuestItemsCount(TRUNK_OF_NEPENTHES) == 50) && (qs.getQuestItemsCount(FOOT_OF_BANDERSNATCHLING) == 50) && (qs.getQuestItemsCount(SECRET_SPICE) == 50))
		{
			qs.playSound(SOUND_MIDDLE);
			qs.setCond(3);
		}
		else
		{
			qs.playSound(SOUND_ITEMGET);
		}
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
