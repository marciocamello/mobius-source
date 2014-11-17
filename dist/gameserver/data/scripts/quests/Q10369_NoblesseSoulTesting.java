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
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Util;

/**
 * @author Jocy, vegax
 * @version $Revision: 1.0 $
 */
public class Q10369_NoblesseSoulTesting extends Quest implements ScriptFile, OnMagicUseListener
{
	// Npcs
	private static final int Cerenas = 31281;
	private static final int EvasAltar = 33686;
	private static final int Lanya = 33696;
	private static final int FlameFlower = 27483;
	private static final int Helping = 19293;
	private static final int HelpingRune = 19293;
	private static final int HelpingTree = 27486;
	private static final int SeedWaler = 18678;
	// Monsters
	private static final int OneWho = 27482;
	private static final int[] HotSprings =
	{
		21320,
		21322,
		21323
	};
	private static final int[] IsleOf =
	{
		22262,
		22263,
		22264
	};
	// Skills
	private static final int Trower = 9442;
	private static final int HelpingS = 9444;
	// Items
	private static final int HelpingSeed = 34961;
	private static final int Ashes = 34962;
	private static final int SOEAdneCastle = 34981;
	private static final int SackContaining = 34913;
	private static final int HfCeoW = 34892;
	private static final int SOEIsleofPrayer = 34980;
	private static final int EnergyOfFire = 34891;
	private static final int SOEForgeOfTheGods = 34979;
	private static final int Trowel = 34890;
	private static final int HardLeather = 34889;
	private static final int SOEHotSprings = 34978;
	private static final int SummoningStone = 34912;
	private static final int EmptyHot = 34887;
	private static final int HotFull = 34888;
	private static final int NovellProphecy = 34886;
	private static final int NoblessTiara = 7694;
	private static final int DimensionalDiamond = 7562;
	private static final int NoblessePrivi = 34983;
	
	public Q10369_NoblesseSoulTesting()
	{
		super(false);
		addStartNpc(Cerenas);
		addTalkId(Lanya, EvasAltar, Cerenas, SeedWaler, Helping, HelpingRune);
		addQuestItem(NoblessTiara, DimensionalDiamond, NoblessePrivi, HelpingSeed, Ashes, NovellProphecy, SOEAdneCastle, SackContaining, HfCeoW, SOEIsleofPrayer, EnergyOfFire, SOEForgeOfTheGods, Trowel, HardLeather, SOEHotSprings, SummoningStone, EmptyHot, HotFull);
		addKillId(OneWho);
		addKillId(HotSprings);
		addKillId(IsleOf);
		addLevelCheck(75, 99);
		addSubClassCheck();
		addQuestCompletedCheck(Q10385_RedThreadOfFate.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "showMovie":
				if (cond == 0)
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.getPlayer().showQuestMovie(SceneMovie.sc_noble_opening);
					qs.playSound("ItemSound.quest_accept");
					qs.startQuestTimer("", 25000);
					htmltext = null;
				}
				else if (cond == 3)
				{
					qs.getPlayer().showQuestMovie(SceneMovie.sc_noble_ending);
					qs.playSound("ItemSound.quest_middle");
					qs.startQuestTimer("", 25000);
					htmltext = null;
					qs.takeItems(NovellProphecy, -1);
					qs.setCond(4);
				}
				break;
			
			case "Cerenas-6.htm":
				if (cond == 1)
				{
					qs.setCond(2);
					qs.playSound("ItemSound.quest_middle");
				}
				break;
			
			case "Cerenas-10.htm":
				if (cond == 4)
				{
					qs.playSound("ItemSound.quest_middle");
					player.teleToLocation(new Location(-122136, -116552, -5797));
					qs.setCond(5);
				}
				break;
			
			case "Evas-3.htm":
				if (cond == 5)
				{
					qs.giveItems(EmptyHot, 1);
					qs.giveItems(SummoningStone, 1);
					qs.giveItems(SOEHotSprings, 1);
					qs.playSound("ItemSound.quest_middle");
					qs.setCond(6);
				}
				break;
			
			case "Lanya-2.htm":
				if (cond == 7)
				{
					qs.takeItems(HotFull, -1);
					qs.playSound("ItemSound.quest_middle");
					qs.setCond(8);
				}
				break;
			
			case "Lanya-5.htm":
				if (cond == 9)
				{
					qs.takeItems(HardLeather, -10);
					qs.giveItems(Trowel, 1);
					qs.giveItems(SOEForgeOfTheGods, 1);
					qs.playSound("ItemSound.quest_middle");
					qs.setCond(10);
				}
				break;
			
			case "Lanya-8.htm":
				if (cond == 11)
				{
					qs.takeItems(EnergyOfFire, -5);
					qs.takeItems(Trowel, -1);
					qs.giveItems(SOEIsleofPrayer, 1);
					qs.playSound("ItemSound.quest_middle");
					qs.setCond(12);
				}
				break;
			
			case "Evas-5.htm":
				if (cond == 15)
				{
					qs.giveItems(HelpingSeed, 1);
					qs.giveItems(SOEAdneCastle, 1);
					qs.playSound("ItemSound.quest_middle");
					qs.setCond(16);
				}
				break;
			
			case "Evas-7.htm":
				if (qs.getPlayer().getLevel() >= 75)
				{
					qs.takeItems(Ashes, -1);
					qs.takeItems(SummoningStone, -1);
					qs.giveItems(NoblessTiara, 1);
					qs.giveItems(DimensionalDiamond, 10);
					qs.giveItems(NoblessePrivi, 1);
					qs.addExpAndSp(12625440, 0);
					qs.setState(COMPLETED);
					qs.exitCurrentQuest(false);
					Olympiad.addNoble(qs.getPlayer());
					qs.getPlayer().setNoble(true);
					qs.getPlayer().updatePledgeClass();
					qs.getPlayer().updateNobleSkills();
					qs.getPlayer().sendSkillList();
					qs.getPlayer().broadcastUserInfo();
					qs.playSound(SOUND_FINISH);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Cerenas:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().isBaseClassActive())
						{
							return "Subclass only!";
						}
						if (qs.getPlayer().getSubClassList().size() < 2)
						{
							return "You do not have subclass!";
						}
						if (qs.getPlayer().getLevel() >= 75)
						{
							htmltext = "Cerenas-1.htm";
						}
						else
						{
							htmltext = "Cerenas-no.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 1:
						htmltext = "Cerenas-4.htm";
						break;
					
					case 3:
						htmltext = "Cerenas-7.htm";
						break;
					
					case 4:
						htmltext = "Cerenas-8.htm";
						break;
					
					case 14:
						htmltext = "Cerenas-11.htm";
						qs.playSound("ItemSound.quest_middle");
						player.teleToLocation(new Location(-122136, -116552, -5797));
						qs.setCond(15);
						break;
					
					case 17:
						htmltext = "Cerenas-12.htm";
						qs.playSound("ItemSound.quest_middle");
						player.teleToLocation(new Location(-122136, -116552, -5797));
						qs.setCond(18);
						break;
				}
				
				break;
			
			case Lanya:
				switch (cond)
				{
					case 6:
						if (qs.getQuestItemsCount(HotFull) == 1)
						{
							qs.playSound("ItemSound.quest_middle");
							qs.setCond(7);
							htmltext = "Lanya-1.htm";
						}
						break;
					
					case 7:
						htmltext = "Lanya-1.htm";
						break;
					
					case 9:
						htmltext = "Lanya-4.htm";
						break;
					
					case 10:
						htmltext = "Lanya-6.htm";
						break;
					
					case 11:
						htmltext = "Lanya-7.htm";
						break;
					
					case 13:
						htmltext = "Lanya-9.htm";
						qs.takeItems(HfCeoW, -10);
						qs.playSound("ItemSound.quest_middle");
						qs.setCond(14);
						break;
				}
				
				break;
			
			case EvasAltar:
				if (cond == 5)
				{
					htmltext = "Evas-1.htm";
				}
				else if (cond == 15)
				{
					htmltext = "Evas-4.htm";
				}
				else if (cond == 18)
				{
					htmltext = "Evas-6.htm";
				}
				
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
	{
		if ((actor == null) || !actor.isPlayer() || (target == null) || !target.isNpc())
		{
			return;
		}
		
		final QuestState qs = ((Player) actor).getQuestState(Q10369_NoblesseSoulTesting.class);
		
		if (qs == null)
		{
			return;
		}
		
		final NpcInstance npc = (NpcInstance) target;
		final Player player = qs.getPlayer();
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (skill.getId())
		{
			case HelpingS:
				if ((npcId == Helping) && (cond == 16)) // Aden Castle
				{
					ItemFunctions.removeItem(qs.getPlayer(), HelpingSeed, 1L, true);
					/* NpcInstance mob = */
					qs.addSpawn(HelpingTree, 148216, 14856, -1393);
					qs.giveItems(Ashes, 1);
					qs.playSound("ItemSound.quest_middle");
					qs.setCond(17);
				}
				
				break;
			
			case Trower:
				if ((qs.getCond() == 10) && (npcId == FlameFlower) && !npc.isDead())
				{
					qs.giveItems(EnergyOfFire, 1);
					qs.playSound("ItemSound.quest_itemget");
					npc.doDie(player);
				}
				
				if ((qs.getQuestItemsCount(EnergyOfFire) >= 5))
				{
					qs.playSound(SOUND_MIDDLE);
					qs.setCond(11);
				}
		}
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		if (qs.getPlayer() == null)
		{
			return null;
		}
		
		if ((npcId == OneWho) && (cond == 2))
		{
			qs.giveItems(NovellProphecy, 1);
			qs.playSound("ItemSound.quest_itemget");
			qs.setCond(3);
		}
		
		if ((qs.getCond() == 8) && Util.contains(HotSprings, npcId) && Rnd.chance(40))
		{
			qs.giveItems(HardLeather, 1);
			qs.playSound("ItemSound.quest_itemget");
		}
		
		if ((qs.getQuestItemsCount(HardLeather) >= 10))
		{
			qs.setCond(9);
			qs.playSound(SOUND_MIDDLE);
		}
		
		if ((qs.getCond() == 12) && Util.contains(IsleOf, npcId) && Rnd.chance(40))
		{
			qs.giveItems(HfCeoW, 1);
			qs.playSound("ItemSound.quest_itemget");
		}
		
		if ((qs.getQuestItemsCount(HfCeoW) >= 10))
		{
			qs.setCond(13);
			qs.playSound(SOUND_MIDDLE);
		}
		
		return null;
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
	}
	
	@Override
	public void onReload()
	{
		CharListenerList.removeGlobal(this);
		CharListenerList.addGlobal(this);
	}
}