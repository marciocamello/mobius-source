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

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

public class Q00194_SevenSignsMammonsContract extends Quest implements ScriptFile
{
	// Npcs
	private static final int Colin = 32571;
	private static final int SirGustavAthebaldt = 30760;
	private static final int Frog = 32572;
	private static final int Tess = 32573;
	private static final int Kuta = 32574;
	private static final int ClaudiaAthebaldt = 31001;
	// Items
	private static final int AthebaldtsIntroduction = 13818;
	private static final int FrogKingsBead = 13820;
	private static final int GrandmaTessCandyPouch = 13821;
	private static final int NativesGlove = 13819;
	
	public Q00194_SevenSignsMammonsContract()
	{
		super(false);
		addStartNpc(SirGustavAthebaldt);
		addTalkId(Colin, SirGustavAthebaldt, Frog, Tess, Kuta, ClaudiaAthebaldt);
		addQuestItem(AthebaldtsIntroduction, FrogKingsBead, GrandmaTessCandyPouch, NativesGlove);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		String htmltext = event;
		
		switch (event)
		{
			case "sirgustavathebaldt_q194_2.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "sirgustavathebaldt_q194_2c.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ_CONTRACT_OF_MAMMON);
				return null;
				
			case "sirgustavathebaldt_q194_3.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(AthebaldtsIntroduction, 1);
				break;
			
			case "colin_q194_3.htm":
				qs.takeItems(AthebaldtsIntroduction, -1);
				qs.setCond(4);
				break;
			
			case "colin_q194_3a.htm":
				if ((player.getTransformation() != 0) || player.isMounted())
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				negateSpeedBuffs(player);
				SkillTable.getInstance().getInfo(6201, 1).getEffects(npc, player, false, false);
				break;
			
			case "frog_q194_2.htm":
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(FrogKingsBead, 1);
				break;
			
			case "colin_q194_5.htm":
				qs.setCond(6);
				qs.takeItems(FrogKingsBead, -1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "colin_q194_6.htm":
				if ((player.getTransformation() != 0) || player.isMounted())
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				qs.setCond(7);
				qs.playSound(SOUND_MIDDLE);
				negateSpeedBuffs(player);
				SkillTable.getInstance().getInfo(6202, 1).getEffects(player, player, false, false);
				break;
			
			case "tess_q194_2.htm":
				qs.setCond(8);
				qs.giveItems(GrandmaTessCandyPouch, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "colin_q194_8.htm":
				qs.setCond(9);
				qs.takeItems(GrandmaTessCandyPouch, -1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "colin_q194_9.htm":
				if ((player.getTransformation() != 0) || player.isMounted())
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				qs.setCond(10);
				qs.playSound(SOUND_MIDDLE);
				negateSpeedBuffs(player);
				SkillTable.getInstance().getInfo(6203, 1).getEffects(player, player, false, false);
				break;
			
			case "kuta_q194_2.htm":
				qs.setCond(11);
				qs.giveItems(NativesGlove, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "colin_q194_10a.htm":
				qs.setCond(12);
				qs.takeItems(NativesGlove, -1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "claudiaathebaldt_q194_2.htm":
				if (player.getBaseClassId() == player.getActiveClassId())
				{
					qs.addExpAndSp(10000000, 2500000);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				else
				{
					return "subclass_forbidden.htm";
				}
				break;
			
			case "colin_q194_11a.htm":
				if ((player.getTransformation() != 0) || player.isMounted())
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				negateSpeedBuffs(player);
				SkillTable.getInstance().getInfo(6201, 1).getEffects(player, player, false, false);
				break;
			
			case "colin_q194_12a.htm":
				if ((player.getTransformation() != 0) || player.isMounted())
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				negateSpeedBuffs(player);
				SkillTable.getInstance().getInfo(6202, 1).getEffects(player, player, false, false);
				break;
			
			case "colin_q194_13a.htm":
				if ((player.getTransformation() != 0) || player.isMounted())
				{
					player.sendPacket(new SystemMessage(SystemMessage.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN));
					return null;
				}
				negateSpeedBuffs(player);
				SkillTable.getInstance().getInfo(6203, 1).getEffects(player, player, false, false);
				break;
			
			case "colin_q194_0c.htm":
				negateTransformations(player);
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
		final int npcId = npc.getId();
		final Player player = qs.getPlayer();
		
		if (player.getBaseClassId() != player.getActiveClassId())
		{
			return "subclass_forbidden.htm";
		}
		
		switch (npcId)
		{
			case SirGustavAthebaldt:
				final QuestState state = player.getQuestState(Q00193_SevenSignsDyingMessage.class);
				switch (cond)
				{
					case 0:
						if ((player.getLevel() >= 79) && (state != null) && state.isCompleted())
						{
							htmltext = "sirgustavathebaldt_q194_1.htm";
						}
						break;
					
					case 1:
						htmltext = "sirgustavathebaldt_q194_2b.htm";
						break;
					
					case 2:
						htmltext = "sirgustavathebaldt_q194_2c.htm";
						break;
					
					case 3:
						if (qs.getQuestItemsCount(AthebaldtsIntroduction) < 1)
						{
							qs.giveItems(AthebaldtsIntroduction, 1);
						}
						htmltext = "sirgustavathebaldt_q194_4.htm";
						break;
					
					default:
						htmltext = "sirgustavathebaldt_q194_0.htm";
						qs.exitCurrentQuest(true);
						break;
				}
				break;
			
			case Colin:
				switch (cond)
				{
					case 3:
						if (qs.getQuestItemsCount(AthebaldtsIntroduction) > 0)
						{
							htmltext = "colin_q194_1.htm";
						}
						else
						{
							htmltext = "colin_q194_0b.htm";
						}
						break;
					
					case 5:
						htmltext = "colin_q194_4.htm";
						break;
					
					case 6:
						htmltext = "colin_q194_5.htm";
						break;
					
					case 8:
						htmltext = "colin_q194_7.htm";
						break;
					
					case 9:
						htmltext = "colin_q194_8.htm";
						break;
					
					case 11:
						htmltext = "colin_q194_10.htm";
						break;
					
					case 12:
						htmltext = "colin_q194_14.htm";
						break;
					
					case 4:
						if (player.getTransformation() == 0)
						{
							htmltext = "colin_q194_11.htm";
						}
						else if (player.getTransformation() != 0)
						{
							htmltext = "colin_q194_0a.htm";
						}
						break;
					
					case 7:
						if (player.getTransformation() == 0)
						{
							htmltext = "colin_q194_12.htm";
						}
						else if (player.getTransformation() != 0)
						{
							htmltext = "colin_q194_0a.htm";
						}
						break;
					case 10:
						if (player.getTransformation() == 0)
						{
							htmltext = "colin_q194_13.htm";
						}
						else if (player.getTransformation() != 0)
						{
							htmltext = "colin_q194_0a.htm";
						}
						break;
				}
				break;
			
			case Frog:
				if ((cond == 4) && (player.getTransformation() == 111))
				{
					htmltext = "frog_q194_1.htm";
				}
				else if ((cond == 5) && (player.getTransformation() == 111))
				{
					htmltext = "frog_q194_4.htm";
				}
				else
				{
					htmltext = "frog_q194_3.htm";
				}
				break;
			
			case Tess:
				if ((cond == 7) && (player.getTransformation() == 112))
				{
					htmltext = "tess_q194_1.htm";
				}
				else if ((cond == 8) && (player.getTransformation() == 112))
				{
					htmltext = "tess_q194_3.htm";
				}
				else
				{
					htmltext = "tess_q194_0.htm";
				}
				break;
			
			case Kuta:
				if ((cond == 10) && (player.getTransformation() == 101))
				{
					htmltext = "kuta_q194_1.htm";
				}
				else if ((cond == 11) && (player.getTransformation() == 101))
				{
					htmltext = "kuta_q194_3.htm";
				}
				else
				{
					htmltext = "kuta_q194_0.htm";
				}
				break;
			
			case ClaudiaAthebaldt:
				if (cond == 12)
				{
					htmltext = "claudiaathebaldt_q194_1.htm";
				}
				else
				{
					htmltext = "claudiaathebaldt_q194_0.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	private void negateSpeedBuffs(Player p)
	{
		for (Effect e : p.getEffectList().getAllEffects())
		{
			if (e.getStackType().contains("SpeedUp") && !e.isOffensive())
			{
				e.exit();
			}
		}
	}
	
	private void negateTransformations(Player p)
	{
		p.setTransformation(0);
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
