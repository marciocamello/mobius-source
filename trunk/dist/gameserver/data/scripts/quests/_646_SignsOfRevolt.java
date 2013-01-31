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

public class _646_SignsOfRevolt extends Quest implements ScriptFile
{
	private static int TORRANT = 32016;
	private static int Ragna_Orc = 22029;
	private static int Ragna_Orc_Sorcerer = 22044;
	private static int Guardian_of_the_Ghost_Town = 22047;
	private static int Varangkas_Succubus = 22049;
	private static int Steel = 1880;
	private static int Coarse_Bone_Powder = 1881;
	private static int Leather = 1882;
	private static int CURSED_DOLL = 8087;
	private static int CURSED_DOLL_Chance = 75;
	
	public _646_SignsOfRevolt()
	{
		super(false);
		addStartNpc(TORRANT);
		for (int Ragna_Orc_id = Ragna_Orc; Ragna_Orc_id <= Ragna_Orc_Sorcerer; Ragna_Orc_id++)
		{
			addKillId(Ragna_Orc_id);
		}
		addKillId(Guardian_of_the_Ghost_Town);
		addKillId(Varangkas_Succubus);
		addQuestItem(CURSED_DOLL);
	}
	
	private static String doReward(QuestState st, int reward_id, int _count)
	{
		if (st.getQuestItemsCount(CURSED_DOLL) < 180)
		{
			return null;
		}
		st.takeItems(CURSED_DOLL, -1);
		st.giveItems(reward_id, _count, true);
		st.playSound(SOUND_FINISH);
		st.exitCurrentQuest(true);
		return "torant_q0646_0202.htm";
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		int _state = st.getState();
		if (event.equalsIgnoreCase("torant_q0646_0103.htm") && (_state == CREATED))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("reward_adena") && (_state == STARTED))
		{
			return doReward(st, ADENA_ID, 21600);
		}
		else if (event.equalsIgnoreCase("reward_cbp") && (_state == STARTED))
		{
			return doReward(st, Coarse_Bone_Powder, 12);
		}
		else if (event.equalsIgnoreCase("reward_steel") && (_state == STARTED))
		{
			return doReward(st, Steel, 9);
		}
		else if (event.equalsIgnoreCase("reward_leather") && (_state == STARTED))
		{
			return doReward(st, Leather, 20);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if (npc.getNpcId() != TORRANT)
		{
			return htmltext;
		}
		int _state = st.getState();
		if (_state == CREATED)
		{
			if (st.getPlayer().getLevel() < 40)
			{
				htmltext = "torant_q0646_0102.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "torant_q0646_0101.htm";
				st.setCond(0);
			}
		}
		else if (_state == STARTED)
		{
			htmltext = st.getQuestItemsCount(CURSED_DOLL) >= 180 ? "torant_q0646_0105.htm" : "torant_q0646_0106.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		Player player = qs.getRandomPartyMember(STARTED, Config.ALT_PARTY_DISTRIBUTION_RANGE);
		if (player == null)
		{
			return null;
		}
		QuestState st = player.getQuestState(qs.getQuest().getName());
		long CURSED_DOLL_COUNT = st.getQuestItemsCount(CURSED_DOLL);
		if ((CURSED_DOLL_COUNT < 180) && Rnd.chance(CURSED_DOLL_Chance))
		{
			st.giveItems(CURSED_DOLL, 1);
			if (CURSED_DOLL_COUNT == 179)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
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
