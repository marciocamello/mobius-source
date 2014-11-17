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

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowUsmVideo;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.Util;

public class Q10360_CertificationOfFate extends Quest implements ScriptFile
{
	private static final int Reins = 30288;
	private static final int Raimon = 30289;
	private static final int Tobias = 30297;
	private static final int Drikus = 30505;
	private static final int Mendius = 30504;
	private static final int Gershfin = 32196;
	private static final int Elinia = 30155;
	private static final int Ershandel = 30158;
	private static final int Renpard = 33524;
	private static final int Joel = 33516;
	private static final int Shachen = 33517;
	private static final int Shelon = 33518;
	private static final int[] classesav =
	{
		1,
		7,
		4,
		11,
		15,
		19,
		22,
		26,
		29,
		32,
		35,
		39,
		42,
		45,
		47,
		50,
		54,
		56,
		125,
		126,
		108,
		109,
		110,
		111,
		112,
		113,
		114,
		115,
		116,
		117,
		118,
		136,
		135,
		134,
		132,
		133
	};
	private static final int Poslov = 27460;
	private static final int Kanilov = 27459;
	private static final int Sakum = 27453;
	private static final int Stone = 17587;
	private int killedkanilov;
	private int killedposlov;
	private int killedsakum;
	
	public Q10360_CertificationOfFate()
	{
		super(false);
		addStartNpc(Reins, Raimon, Tobias, Drikus, Mendius, Gershfin, Elinia, Ershandel);
		addTalkId(Renpard, Joel, Shelon, Shachen);
		addKillId(Poslov, Kanilov, Sakum);
		addQuestItem(Stone);
		addLevelCheck(38, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		if (player.getClassLevel() > 2)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "1-3.htm":
				qs.setState(STARTED);
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "1-3.htm";
				break;
			
			case "telep":
				player.teleToLocation(-24776, 188696, -3993);
				htmltext = "";
				break;
			
			case "master":
				switch (qs.getPlayer().getRace())
				{
					case human:
						if (qs.getPlayer().isMageClass())
						{
							htmltext = "4-5re.htm";
							qs.playSound(SOUND_MIDDLE);
							qs.setCond(9);
						}
						else
						{
							htmltext = "4-5r.htm";
							qs.playSound(SOUND_MIDDLE);
							qs.setCond(8);
						}
						break;
					
					case elf:
						if (qs.getPlayer().isMageClass())
						{
							htmltext = "4-5e.htm";
							qs.playSound(SOUND_MIDDLE);
							qs.setCond(11);
						}
						else
						{
							htmltext = "4-5ew.htm";
							qs.playSound(SOUND_MIDDLE);
							qs.setCond(10);
						}
						break;
					
					case darkelf:
						htmltext = "4-5t.htm";
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(12);
						break;
					
					case orc:
						htmltext = "4-5d.htm";
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(13);
						break;
					
					case dwarf:
						htmltext = "4-5m.htm";
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(14);
						break;
					
					case kamael:
						htmltext = "4-5g.htm";
						qs.playSound(SOUND_MIDDLE);
						qs.setCond(15);
						break;
				}
				break;
			
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				switch (qs.getPlayer().getRace())
				{
					case human:
						if (qs.getPlayer().isMageClass())
						{
							htmltext = "0-3re.htm";
						}
						else
						{
							htmltext = "0-3r.htm";
						}
						break;
					
					case elf:
						if (qs.getPlayer().isMageClass())
						{
							htmltext = "0-3e.htm";
						}
						else
						{
							htmltext = "0-3ew.htm";
						}
						break;
					
					case darkelf:
						htmltext = "0-3t.htm";
						break;
					
					case orc:
						htmltext = "0-3d.htm";
						break;
					
					case dwarf:
						htmltext = "0-3m.htm";
						break;
					
					case kamael:
						htmltext = "0-3g.htm";
						break;
				}
				break;
			
			case "3-3.htm":
				qs.setCond(6);
				qs.playSound(SOUND_MIDDLE);
				player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q003));
				break;
			
			case "2-3.htm":
				htmltext = "2-3.htm";
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(4);
				break;
		}
		
		if (event.startsWith("changeclass"))
		{
			int newClassId = 0;
			
			try
			{
				newClassId = Integer.parseInt(event.substring(12, event.length()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			player.sendPacket(new SystemMessage(SystemMessage.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS));
			player.setClassId(newClassId, false, false);
			player.broadcastCharInfo();
			MultiSellHolder.getInstance().SeparateAndSend(85556, qs.getPlayer(), 0);
			qs.getPlayer().addExpAndSp(2700000, 250000);
			qs.giveItems(17822, 40);
			qs.giveItems(32777, 1);
			qs.giveItems(33800, 1);
			qs.giveItems(ADENA_ID, 110000);
			qs.exitCurrentQuest(false);
			qs.takeAllItems(Stone);
			qs.playSound(SOUND_FINISH);
			
			switch (qs.getPlayer().getRace())
			{
				case human:
					if (qs.getPlayer().isMageClass())
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-6re.htm", qs.getPlayer());
					}
					else
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-6r.htm", qs.getPlayer());
					}
					break;
				
				case elf:
					if (qs.getPlayer().isMageClass())
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-6e.htm", qs.getPlayer());
					}
					else
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-6ew.htm", qs.getPlayer());
					}
					break;
				
				case darkelf:
					htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-6t.htm", qs.getPlayer());
					break;
				
				case orc:
					htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-6d.htm", qs.getPlayer());
					break;
				
				case dwarf:
					htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-6m.htm", qs.getPlayer());
					break;
				
				case kamael:
					htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-6g.htm", qs.getPlayer());
					break;
			}
			
			htmltext = htmltext.replace("%showproof%", HtmlUtils.htmlClassName(newClassId));
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int classid = qs.getPlayer().getClassId().getId();
		
		switch (npc.getId())
		{
			case Raimon:
				if ((qs.getPlayer().getRace() == Race.human) && qs.getPlayer().isMageClass())
				{
					if (qs.isCompleted())
					{
						htmltext = "0re-c.htm";
					}
					else if ((cond == 0) && isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "0-1re.htm";
					}
					else if (cond == 1)
					{
						htmltext = "0-3re.htm";
					}
					else if (cond == 9)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-5re.htm", qs.getPlayer());
						htmltext = htmltext.replace("%classmaster%", makeMessage(qs.getPlayer()));
					}
				}
				break;
			
			case Reins:
				if ((qs.getPlayer().getRace() == Race.human) && !qs.getPlayer().isMageClass())
				{
					if (qs.isCompleted())
					{
						htmltext = "0r-c.htm";
					}
					else if ((cond == 0) && isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "0-1r.htm";
					}
					else if (cond == 1)
					{
						htmltext = "0-3r.htm";
					}
					else if (cond == 8)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-5r.htm", qs.getPlayer());
						htmltext = htmltext.replace("%classmaster%", makeMessage(qs.getPlayer()));
					}
				}
				break;
			
			case Tobias:
				if (qs.getPlayer().getRace() == Race.darkelf)
				{
					if (qs.isCompleted())
					{
						htmltext = "0t-c.htm";
					}
					else if ((cond == 0) && isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "0-1t.htm";
					}
					else if (cond == 1)
					{
						htmltext = "0-3t.htm";
					}
					else if (cond == 12)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-5t.htm", qs.getPlayer());
						htmltext = htmltext.replace("%classmaster%", makeMessage(qs.getPlayer()));
					}
				}
				break;
			
			case Drikus:
				if (qs.getPlayer().getRace() == Race.orc)
				{
					if (qs.isCompleted())
					{
						htmltext = "0d-c.htm";
					}
					else if ((cond == 0) && isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "0-1d.htm";
					}
					else if (cond == 1)
					{
						htmltext = "0-3d.htm";
					}
					else if (cond == 13)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-5d.htm", qs.getPlayer());
						htmltext = htmltext.replace("%classmaster%", makeMessage(qs.getPlayer()));
					}
				}
				break;
			
			case Gershfin:
				if (qs.getPlayer().getRace() == Race.kamael)
				{
					if (qs.isCompleted())
					{
						htmltext = "0g-c.htm";
					}
					else if ((cond == 0) && isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "0-1g.htm";
					}
					else if (cond == 1)
					{
						htmltext = "0-3g.htm";
					}
					else if (cond == 15)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-5g.htm", qs.getPlayer());
						htmltext = htmltext.replace("%classmaster%", makeMessage(qs.getPlayer()));
					}
				}
				break;
			
			case Elinia:
				if ((qs.getPlayer().getRace() == Race.elf) && !qs.getPlayer().isMageClass())
				{
					if (qs.isCompleted())
					{
						htmltext = "0e-c.htm";
					}
					else if ((cond == 0) && isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "0-1e.htm";
					}
					else if (cond == 1)
					{
						htmltext = "0-3e.htm";
					}
					else if (cond == 10)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-5e.htm", qs.getPlayer());
						htmltext = htmltext.replace("%classmaster%", makeMessage(qs.getPlayer()));
					}
				}
				break;
			
			case Ershandel:
				if ((qs.getPlayer().getRace() == Race.elf) && qs.getPlayer().isMageClass())
				{
					if (qs.isCompleted())
					{
						htmltext = "0ew-c.htm";
					}
					else if ((cond == 0) && isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "0-1ew.htm";
					}
					else if (cond == 1)
					{
						htmltext = "0-3ew.htm";
					}
					else if (cond == 11)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-5ew.htm", qs.getPlayer());
						htmltext = htmltext.replace("%classmaster%", makeMessage(qs.getPlayer()));
					}
				}
				break;
			
			case Mendius:
				if (qs.getPlayer().getRace() == Race.dwarf)
				{
					if (qs.isCompleted())
					{
						htmltext = "0m-c.htm";
					}
					else if ((cond == 0) && isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "0-1m.htm";
					}
					else if (cond == 1)
					{
						htmltext = "0-3m.htm";
					}
					else if (cond == 14)
					{
						htmltext = HtmCache.getInstance().getNotNull("quests/Q10360_CertificationOfFate/0-5m.htm", qs.getPlayer());
						htmltext = htmltext.replace("%classmaster%", makeMessage(qs.getPlayer()));
					}
				}
				break;
			
			case Renpard:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 1)
				{
					htmltext = "1-1.htm";
				}
				else if (cond == 2)
				{
					htmltext = "1-4.htm";
				}
				break;
			
			case Joel:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = "2-1.htm";
				}
				else if (cond == 3)
				{
					htmltext = "2-2.htm";
				}
				else if (cond == 4)
				{
					htmltext = "2-5.htm";
				}
				break;
			
			case Shachen:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 5)
				{
					htmltext = "3-1.htm";
				}
				break;
			
			case Shelon:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 6)
				{
					htmltext = "4-1.htm";
				}
				else if (cond == 7)
				{
					htmltext = "4-2.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		switch (npc.getId())
		{
			case Poslov:
				if (qs.getCond() == 4)
				{
					++killedposlov;
					
					if (killedposlov >= 1)
					{
						qs.setCond(5);
						qs.playSound(SOUND_MIDDLE);
						killedposlov = 0;
					}
				}
				break;
			
			case Kanilov:
				if (qs.getCond() == 2)
				{
					++killedkanilov;
					
					if (killedkanilov >= 1)
					{
						qs.setCond(3);
						qs.playSound(SOUND_MIDDLE);
						killedkanilov = 0;
					}
				}
				break;
			
			case Sakum:
				if (qs.getCond() == 6)
				{
					++killedsakum;
					
					if (killedsakum >= 1)
					{
						qs.setCond(7);
						qs.playSound(SOUND_MIDDLE);
						killedsakum = 0;
						qs.giveItems(Stone, 1, false);
					}
				}
				break;
		}
		
		return null;
	}
	
	private String makeMessage(Player player)
	{
		final ClassId classId = player.getClassId();
		StringBuilder html = new StringBuilder();
		
		for (ClassId cid : ClassId.VALUES)
		{
			if (cid == ClassId.INSPECTOR)
			{
				continue;
			}
			
			if (cid.childOf(classId) && (cid.getClassLevel().ordinal() == (classId.getClassLevel().ordinal() + 1)))
			{
				html.append("<a action=\"bypass -h Quest ").append(getClass().getSimpleName()).append(" changeclass ").append(cid.getId()).append(' ').append("\">").append(HtmlUtils.htmlClassName(cid.getId())).append("</a><br>");
			}
		}
		
		return html.toString();
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
