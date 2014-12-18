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

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.TutorialCloseHtml;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00255_Tutorial extends Quest implements ScriptFile, OnPlayerEnterListener
{
	private static final String[][] QMCa =
	{
		{
			"0",
			"tutorial_fighter017.htm",
			"-83165",
			"242711",
			"-3720"
		},
		{
			"10",
			"tutorial_mage017.htm",
			"-85247",
			"244718",
			"-3720"
		},
		{
			"18",
			"tutorial_fighter017.htm",
			"45610",
			"52206",
			"-2792"
		},
		{
			"25",
			"tutorial_mage017.htm",
			"45610",
			"52206",
			"-2792"
		},
		{
			"31",
			"tutorial_fighter017.htm",
			"10344",
			"14445",
			"-4242"
		},
		{
			"38",
			"tutorial_mage017.htm",
			"10344",
			"14445",
			"-4242"
		},
		{
			"44",
			"tutorial_fighter017.htm",
			"-46324",
			"-114384",
			"-200"
		},
		{
			"49",
			"tutorial_fighter017.htm",
			"-46305",
			"-112763",
			"-200"
		},
		{
			"53",
			"tutorial_fighter017.htm",
			"115447",
			"-182672",
			"-1440"
		},
		{
			"123",
			"tutorial_fighter017.htm",
			"-118132",
			"42788",
			"723"
		},
		{
			"124",
			"tutorial_fighter017.htm",
			"-118132",
			"42788",
			"723"
		}
	};
	private static final Map<Integer, String> QMCb = new HashMap<>();
	private static final Map<Integer, String> QMCc = new HashMap<>();
	private static final Map<Integer, String> TCLa = new HashMap<>();
	private static final Map<Integer, String> TCLb = new HashMap<>();
	private static final Map<Integer, String> TCLc = new HashMap<>();
	static TutorialShowListener _tutorialShowListener;
	
	public Q00255_Tutorial()
	{
		super(false);
		CharListenerList.addGlobal(this);
		_tutorialShowListener = new TutorialShowListener();
		QMCb.put(0, "tutorial_human009.htm");
		QMCb.put(10, "tutorial_human009.htm");
		QMCb.put(18, "tutorial_elf009.htm");
		QMCb.put(25, "tutorial_elf009.htm");
		QMCb.put(31, "tutorial_delf009.htm");
		QMCb.put(38, "tutorial_delf009.htm");
		QMCb.put(44, "tutorial_orc009.htm");
		QMCb.put(49, "tutorial_orc009.htm");
		QMCb.put(53, "tutorial_dwarven009.htm");
		QMCb.put(123, "tutorial_kamael009.htm");
		QMCb.put(124, "tutorial_kamael009.htm");
		QMCc.put(0, "tutorial_1st_ct_human.htm");
		QMCc.put(1, "tutorial_1st_ct_elf.htm");
		QMCc.put(2, "tutorial_1st_ct_dark_elf.htm");
		QMCc.put(3, "tutorial_1st_ct_orc.htm");
		QMCc.put(4, "tutorial_1st_ct_dwarf.htm");
		QMCc.put(5, "tutorial_1st_ct_kamael.htm");
		TCLa.put(1, "tutorial_22w.htm");
		TCLa.put(4, "tutorial_22.htm");
		TCLa.put(7, "tutorial_22b.htm");
		TCLa.put(11, "tutorial_22c.htm");
		TCLa.put(15, "tutorial_22d.htm");
		TCLa.put(19, "tutorial_22e.htm");
		TCLa.put(22, "tutorial_22f.htm");
		TCLa.put(26, "tutorial_22g.htm");
		TCLa.put(29, "tutorial_22h.htm");
		TCLa.put(32, "tutorial_22n.htm");
		TCLa.put(35, "tutorial_22o.htm");
		TCLa.put(39, "tutorial_22p.htm");
		TCLa.put(42, "tutorial_22q.htm");
		TCLa.put(45, "tutorial_22i.htm");
		TCLa.put(47, "tutorial_22j.htm");
		TCLa.put(50, "tutorial_22k.htm");
		TCLa.put(54, "tutorial_22l.htm");
		TCLa.put(56, "tutorial_22m.htm");
		TCLb.put(4, "tutorial_22aa.htm");
		TCLb.put(7, "tutorial_22ba.htm");
		TCLb.put(11, "tutorial_22ca.htm");
		TCLb.put(15, "tutorial_22da.htm");
		TCLb.put(19, "tutorial_22ea.htm");
		TCLb.put(22, "tutorial_22fa.htm");
		TCLb.put(26, "tutorial_22ga.htm");
		TCLb.put(32, "tutorial_22na.htm");
		TCLb.put(35, "tutorial_22oa.htm");
		TCLb.put(39, "tutorial_22pa.htm");
		TCLb.put(50, "tutorial_22ka.htm");
		TCLc.put(4, "tutorial_22ab.htm");
		TCLc.put(7, "tutorial_22bb.htm");
		TCLc.put(11, "tutorial_22cb.htm");
		TCLc.put(15, "tutorial_22db.htm");
		TCLc.put(19, "tutorial_22eb.htm");
		TCLc.put(22, "tutorial_22fb.htm");
		TCLc.put(26, "tutorial_22gb.htm");
		TCLc.put(32, "tutorial_22nb.htm");
		TCLc.put(35, "tutorial_22ob.htm");
		TCLc.put(39, "tutorial_22pb.htm");
		TCLc.put(50, "tutorial_22kb.htm");
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		if (player == null)
		{
			return null;
		}
		
		String html = "";
		final int classId = player.getActiveClassId();
		final int Ex = qs.getInt("Ex");
		
		switch (event.substring(0, 2))
		{
			case "UC":
				switch (player.getLevel())
				{
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						if (qs.getInt("onlyone") == 0)
						{
							switch (qs.getInt("ucMemo"))
							{
								case 0:
									qs.set("ucMemo", "0");
									qs.startQuestTimer("QT", 10000);
									qs.set("Ex", "-2");
									break;
								
								case 1:
									qs.showQuestionMark(1);
									qs.playTutorialVoice("tutorial_voice_006");
									qs.playSound(SOUND_TUTORIAL);
									break;
								
								case 2:
									if (Ex == 2)
									{
										qs.showQuestionMark(3);
										qs.playSound(SOUND_TUTORIAL);
									}
									else if (qs.getQuestItemsCount(6353) > 0)
									{
										qs.showQuestionMark(5);
										qs.playSound(SOUND_TUTORIAL);
									}
									break;
								
								case 3:
									qs.showQuestionMark(12);
									qs.playSound(SOUND_TUTORIAL);
									qs.onTutorialClientEvent(0);
									break;
							}
						}
						break;
					
					case 18:
					case 19:
						if (player.getClassLevel() == 0)
						{
							qs.showQuestionMark(35);
							qs.playSound(SOUND_TUTORIAL);
						}
						break;
					
					case 79:
						if (player.getQuestState("Q00192_SevenSignsSeriesOfDoubt") == null)
						{
							qs.showQuestionMark(36);
							qs.playSound(SOUND_TUTORIAL);
						}
						break;
				}
				break;
			
			case "QT":
				if (Ex == -2)
				{
					html = "tutorial_00.htm";
					qs.set("Ex", "-3");
					qs.cancelQuestTimer("QT");
					qs.startQuestTimer("QT", 30000);
				}
				else if (Ex == -3)
				{
					qs.playTutorialVoice("tutorial_voice_002");
					qs.set("Ex", "0");
				}
				else if (Ex == -4)
				{
					qs.playTutorialVoice("tutorial_voice_008");
					qs.set("Ex", "-5");
				}
				break;
			
			case "TE":
				qs.cancelQuestTimer("TE");
				int event_id = 0;
				if (!event.equals("TE"))
				{
					event_id = Integer.valueOf(event.substring(2));
				}
				switch (event_id)
				{
					case 0:
						player.sendPacket(TutorialCloseHtml.STATIC);
						break;
					
					case 1:
						player.sendPacket(TutorialCloseHtml.STATIC);
						qs.playTutorialVoice("tutorial_voice_006");
						qs.showQuestionMark(1);
						qs.playSound(SOUND_TUTORIAL);
						qs.startQuestTimer("QT", 30000);
						qs.set("Ex", "-4");
						break;
					
					case 2:
						qs.playTutorialVoice("tutorial_voice_003");
						html = "tutorial_02.htm";
						qs.onTutorialClientEvent(1);
						qs.set("Ex", "-5");
						break;
					
					case 3:
						html = "tutorial_03.htm";
						qs.onTutorialClientEvent(2);
						break;
					
					case 5:
						html = "tutorial_05.htm";
						qs.onTutorialClientEvent(8);
						break;
					
					case 7:
						html = "tutorial_100.htm";
						qs.onTutorialClientEvent(0);
						break;
					
					case 8:
						html = "tutorial_101.htm";
						qs.onTutorialClientEvent(0);
						break;
					
					case 10:
						html = "tutorial_103.htm";
						qs.onTutorialClientEvent(0);
						break;
					
					case 12:
						player.sendPacket(TutorialCloseHtml.STATIC);
						break;
					
					case 23:
						if (TCLb.containsKey(classId))
						{
							html = TCLb.get(classId);
						}
						break;
					
					case 24:
						if (TCLb.containsKey(classId))
						{
							html = TCLc.get(classId);
						}
						break;
					
					case 25:
						html = "tutorial_22cc.htm";
						break;
					
					case 26:
						if (TCLb.containsKey(classId))
						{
							html = TCLa.get(classId);
						}
						break;
					
					case 27:
						html = "tutorial_29.htm";
						break;
					
					case 28:
						html = "tutorial_28.htm";
						break;
				}
				break;
			
			case "CE":
				switch (Integer.valueOf(event.substring(2)))
				{
					case 1:
						if (player.getLevel() < 6)
						{
							qs.playTutorialVoice("tutorial_voice_004");
							html = "tutorial_03.htm";
							qs.playSound(SOUND_TUTORIAL);
							qs.onTutorialClientEvent(2);
						}
						break;
					
					case 2:
						if (player.getLevel() < 6)
						{
							qs.playTutorialVoice("tutorial_voice_005");
							html = "tutorial_05.htm";
							qs.playSound(SOUND_TUTORIAL);
							qs.onTutorialClientEvent(8);
						}
						break;
					
					case 8:
						if (player.getLevel() < 6)
						{
							html = "tutorial_01.htm";
							qs.playSound(SOUND_TUTORIAL);
							qs.playTutorialVoice("ItemSound.quest_tutorial");
							qs.set("ucMemo", "1");
							qs.set("Ex", "-5");
						}
						break;
					
					case 30:
						if ((player.getLevel() < 10) && (qs.getInt("Die") == 0))
						{
							qs.playTutorialVoice("tutorial_voice_016");
							qs.playSound(SOUND_TUTORIAL);
							qs.set("Die", "1");
							qs.showQuestionMark(8);
							qs.onTutorialClientEvent(0);
						}
						break;
					
					case 800000:
						if ((player.getLevel() < 6) && (qs.getInt("sit") == 0))
						{
							qs.playTutorialVoice("tutorial_voice_018");
							qs.playSound(SOUND_TUTORIAL);
							qs.set("sit", "1");
							qs.onTutorialClientEvent(0);
							html = "tutorial_21z.htm";
						}
						break;
					
					case 40:
						switch (player.getLevel())
						{
							case 5:
								if (((qs.getInt("lvl") < 5) && !player.getClassId().isMage()) || (classId == 49))
								{
									qs.playTutorialVoice("tutorial_voice_014");
									qs.showQuestionMark(9);
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "5");
								}
								break;
							
							case 6:
								if ((qs.getInt("lvl") < 6) && (player.getClassLevel() == 0))
								{
									qs.playTutorialVoice("tutorial_voice_020");
									qs.playSound(SOUND_TUTORIAL);
									qs.showQuestionMark(24);
									qs.set("lvl", "6");
								}
								break;
							
							case 7:
								if ((qs.getInt("lvl") < 7) && player.getClassId().isMage() && (classId != 49) && (player.getClassLevel() == 0))
								{
									qs.playTutorialVoice("tutorial_voice_019");
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "7");
									qs.showQuestionMark(11);
								}
								break;
							
							case 15:
								if (qs.getInt("lvl") < 15)
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "15");
									qs.showQuestionMark(33);
								}
								break;
							
							case 18:
							case 19:
								if ((qs.getInt("lvl") < 18) && (player.getClassLevel() == 0))
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "18");
									qs.showQuestionMark(35);
								}
								break;
							
							case 20:
								if (qs.getInt("lvl") < 20)
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "20");
									qs.showQuestionMark(36);
								}
								break;
							
							case 28:
								if (qs.getInt("lvl") < 28)
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "28");
									qs.showQuestionMark(36);
								}
								break;
							
							case 35:
								if ((qs.getInt("lvl") < 35) && (player.getRace() != Race.kamael) && (player.getClassLevel() == 1))
								{
									switch (classId)
									{
										case 1:
										case 4:
										case 7:
										case 11:
										case 15:
										case 19:
										case 22:
										case 26:
										case 29:
										case 32:
										case 35:
										case 39:
										case 42:
										case 45:
										case 47:
										case 50:
										case 54:
										case 56:
											qs.playSound(SOUND_TUTORIAL);
											qs.set("lvl", "35");
											qs.showQuestionMark(34);
									}
								}
								break;
							
							case 38:
								if (qs.getInt("lvl") < 38)
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "38");
									qs.showQuestionMark(36);
								}
								break;
							
							case 48:
								if (qs.getInt("lvl") < 48)
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "48");
									qs.showQuestionMark(36);
								}
								break;
							
							case 58:
								if (qs.getInt("lvl") < 58)
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "58");
									qs.showQuestionMark(36);
								}
								break;
							
							case 68:
								if (qs.getInt("lvl") < 68)
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "68");
									qs.showQuestionMark(36);
								}
								break;
							
							case 79:
								if (qs.getInt("lvl") < 79)
								{
									qs.playSound(SOUND_TUTORIAL);
									qs.set("lvl", "79");
									qs.showQuestionMark(79);
								}
								break;
						}
						break;
					
					case 45:
						if ((player.getLevel() < 10) && (qs.getInt("HP") == 0))
						{
							qs.playTutorialVoice("tutorial_voice_017");
							qs.playSound(SOUND_TUTORIAL);
							qs.set("HP", "1");
							qs.showQuestionMark(10);
							qs.onTutorialClientEvent(800000);
						}
						break;
					
					case 57:
						if ((player.getLevel() < 6) && (qs.getInt("Adena") == 0))
						{
							qs.playTutorialVoice("tutorial_voice_012");
							qs.playSound(SOUND_TUTORIAL);
							qs.set("Adena", "1");
							qs.showQuestionMark(23);
						}
						break;
					
					case 6353:
						if ((player.getLevel() < 6) && (qs.getInt("Gemstone") == 0))
						{
							qs.playTutorialVoice("tutorial_voice_013");
							qs.playSound(SOUND_TUTORIAL);
							qs.set("Gemstone", "1");
							qs.showQuestionMark(5);
						}
						break;
					
					case 1048576:
						if (player.getLevel() < 6)
						{
							qs.showQuestionMark(5);
							qs.playTutorialVoice("tutorial_voice_013");
							qs.playSound(SOUND_TUTORIAL);
						}
						break;
				}
				break;
			
			case "QM":
				switch (Integer.valueOf(event.substring(2)))
				{
					case 1:
						qs.set("Ex", "-5");
						html = "tutorial_01.htm";
						break;
					
					case 3:
						html = "tutorial_09.htm";
						qs.onTutorialClientEvent(1048576);
						break;
					
					case 5:
						html = "tutorial_11.htm";
						break;
					
					case 7:
						html = "tutorial_15.htm";
						qs.set("ucMemo", "3");
						break;
					
					case 8:
						html = "tutorial_18.htm";
						break;
					
					case 9:
						int x = 0;
						int y = 0;
						int z = 0;
						for (String[] element : QMCa)
						{
							if (classId == Integer.valueOf(element[0]))
							{
								html = element[1];
								x = Integer.valueOf(element[2]);
								y = Integer.valueOf(element[3]);
								z = Integer.valueOf(element[4]);
							}
						}
						if (x != 0)
						{
							qs.addRadar(x, y, z);
						}
						break;
					
					case 10:
						html = "tutorial_19.htm";
						break;
					
					case 11:
						int x2 = 0;
						int y2 = 0;
						int z2 = 0;
						for (String[] element : QMCa)
						{
							if (classId == Integer.valueOf(element[0]))
							{
								html = element[1];
								x2 = Integer.valueOf(element[2]);
								y2 = Integer.valueOf(element[3]);
								z2 = Integer.valueOf(element[4]);
							}
						}
						if (x2 != 0)
						{
							qs.addRadar(x2, y2, z2);
						}
						break;
					
					case 12:
						html = "tutorial_15.htm";
						qs.set("ucMemo", "4");
						break;
					
					// case 12: ?
					// html = "tutorial_30.htm";
					// break;
					
					case 23:
						html = "tutorial_24.htm";
						break;
					
					case 24:
						if (QMCb.containsKey(classId))
						{
							html = QMCb.get(classId);
						}
						break;
					
					case 26:
						if (player.getClassId().isMage() && (classId != 49))
						{
							html = "tutorial_newbie004b.htm";
						}
						else
						{
							html = "tutorial_newbie004a.htm";
						}
						break;
					
					case 33:
						html = "tutorial_27.htm";
						break;
					
					case 34:
						html = "tutorial_28.htm";
						break;
					
					case 35:
						if (QMCc.containsKey(player.getRace().ordinal()))
						{
							html = QMCc.get(player.getRace().ordinal());
						}
						break;
					
					case 36:
						switch (player.getLevel())
						{
							case 20:
								html = "tutorial_kama_20.htm";
								break;
							
							case 28:
								html = "tutorial_kama_28.htm";
								break;
							
							case 38:
								html = "tutorial_kama_38.htm";
								break;
							
							case 48:
								html = "tutorial_kama_48.htm";
								break;
							
							case 58:
								html = "tutorial_kama_58.htm";
								break;
							
							case 68:
								html = "tutorial_kama_68.htm";
								break;
							
							case 79:
								html = "tutorial_epic_quest.htm";
								break;
						}
						break;
				}
				break;
		}
		
		if (html.isEmpty())
		{
			return null;
		}
		
		qs.showTutorialHTML(html, TutorialShowHtml.TYPE_HTML);
		return null;
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		if (player.getLevel() < 6)
		{
			player.addListener(_tutorialShowListener);
		}
	}
	
	public class TutorialShowListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			final Player player = actor.getPlayer();
			
			if (player.getCurrentHpPercents() < 25)
			{
				player.removeListener(_tutorialShowListener);
				Quest q = QuestManager.getQuest(255);
				
				if (q != null)
				{
					player.processQuestEvent(q.getName(), "CE45", null);
				}
			}
			else if (player.getLevel() > 5)
			{
				player.removeListener(_tutorialShowListener);
			}
		}
	}
	
	public boolean isVisible()
	{
		return false;
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
