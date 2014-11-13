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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q00111_ElrokianHuntersProof extends Quest implements ScriptFile
{
	// Npcs
	private static final int Marquez = 32113;
	private static final int Asamah = 32115;
	private static final int Kirikachin = 32116;
	// Monsters
	private static final int[] Velociraptor =
	{
		22196,
		22197,
		22198,
		22218,
		22223
	};
	private static final int[] Ornithomimus =
	{
		22200,
		22201,
		22202,
		22219,
		22224,
		22744,
		22742
	};
	private static final int[] Deinonychus =
	{
		22203,
		22204,
		22205,
		22220,
		22225,
		22745,
		22743
	};
	private static final int[] Pachycephalosaurus =
	{
		22208,
		22209,
		22210,
		22221,
		22226
	};
	// Items
	private static final int DiaryFragment = 8768;
	private static final int OrnithomimusClaw = 8770;
	private static final int DeinonychusBone = 8771;
	private static final int PachycephalosaurusSkin = 8772;
	private static final int ElrokianTrap = 8763;
	private static final int TrapStone = 8764;
	
	public Q00111_ElrokianHuntersProof()
	{
		super(true);
		addStartNpc(Marquez);
		addTalkId(Asamah, Kirikachin);
		addKillId(Velociraptor);
		addKillId(Ornithomimus);
		addKillId(Deinonychus);
		addKillId(Pachycephalosaurus);
		addQuestItem(DiaryFragment, OrnithomimusClaw, DeinonychusBone, PachycephalosaurusSkin);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "marquez_q111_2.htm":
				if (qs.getCond() == 0)
				{
					qs.setCond(2);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "asamah_q111_2.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "marquez_q111_4.htm":
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "marquez_q111_6.htm":
				qs.setCond(6);
				qs.takeItems(DiaryFragment, -1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "kirikachin_q111_2.htm":
				qs.setCond(7);
				player.sendPacket(new PlaySound("EtcSound.elcroki_song_full"));
				break;
			
			case "kirikachin_q111_3.htm":
				qs.setCond(8);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "asamah_q111_4.htm":
				qs.setCond(9);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "asamah_q111_5.htm":
				qs.setCond(10);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "asamah_q111_7.htm":
				qs.takeItems(OrnithomimusClaw, -1);
				qs.takeItems(DeinonychusBone, -1);
				qs.takeItems(PachycephalosaurusSkin, -1);
				qs.setCond(12);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "asamah_q111_8.htm":
				qs.giveItems(ADENA_ID, 4257180);
				qs.addExpAndSp(19973970, 22122270);
				qs.giveItems(ElrokianTrap, 1);
				qs.giveItems(TrapStone, 100);
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
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case Marquez:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() >= 75)
						{
							htmltext = "marquez_q111_1.htm";
						}
						else if (qs.getPlayer().getLevel() < 75)
						{
							htmltext = "marquez_q111_0.htm";
						}
						break;
					
					case 3:
						htmltext = "marquez_q111_3.htm";
						break;
					
					case 5:
						htmltext = "marquez_q111_5.htm";
						break;
				}
				break;
			
			case Asamah:
				switch (cond)
				{
					case 2:
						htmltext = "asamah_q111_1.htm";
						break;
					
					case 8:
						htmltext = "asamah_q111_3.htm";
						break;
					
					case 11:
						htmltext = "asamah_q111_6.htm";
						break;
				}
				break;
			
			case Kirikachin:
				if (cond == 6)
				{
					htmltext = "kirikachin_q111_1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		if (cond == 4)
		{
			if (Util.contains(Velociraptor, npcId) && (qs.getQuestItemsCount(DiaryFragment) < 50))
			{
				qs.giveItems(DiaryFragment, 1, false);
				
				if (qs.getQuestItemsCount(DiaryFragment) == 50)
				{
					qs.playSound(SOUND_MIDDLE);
					qs.setCond(5);
					return null;
				}
				
				qs.playSound(SOUND_ITEMGET);
			}
		}
		else if (cond == 10)
		{
			if (Util.contains(Ornithomimus, npcId) && (qs.getQuestItemsCount(OrnithomimusClaw) < 10))
			{
				qs.giveItems(OrnithomimusClaw, 1, false);
				return null;
			}
			else if (Util.contains(Deinonychus, npcId) && (qs.getQuestItemsCount(DeinonychusBone) < 10))
			{
				qs.giveItems(DeinonychusBone, 1, false);
				return null;
			}
			else if (Util.contains(Pachycephalosaurus, npcId) && (qs.getQuestItemsCount(PachycephalosaurusSkin) < 10))
			{
				qs.giveItems(PachycephalosaurusSkin, 1, false);
				return null;
			}
			
			if ((qs.getQuestItemsCount(OrnithomimusClaw) >= 10) && (qs.getQuestItemsCount(DeinonychusBone) >= 10) && (qs.getQuestItemsCount(PachycephalosaurusSkin) >= 10))
			{
				qs.setCond(11);
				qs.playSound(SOUND_MIDDLE);
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
