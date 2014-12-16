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
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Krash
 */
public class Q10745_TheSecretIngredients extends Quest implements ScriptFile
{
	// Npcs
	private static final int Dolkin = 33954;
	private static final int Dolkin2 = 34002;
	private static final int Karla = 33933;
	// Monsters
	private static final int Karaphon = 23459;
	// Items
	private static final int Secret_Ingredients = 39533;
	private static final int Dolkin_Report = 39534;
	// Rewards
	private static final int Faeron_Support_Box_Warrior = 40262;
	private static final int Faeron_Support_Box_Mage = 40263;
	// Other
	private int killedKaraphon;
	
	public Q10745_TheSecretIngredients()
	{
		super(false);
		addStartNpc(Dolkin);
		addTalkId(Dolkin, Dolkin2, Karla);
		addQuestItem(Secret_Ingredients, Dolkin_Report);
		addKillId(Karaphon);
		addLevelCheck(17, 25);
		addClassCheck(182, 183);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "33954-2.htm";
				break;
			
			case "enter_instance":
				if (qs.getCond() == 1)
				{
					htmltext = "33954-3.htm";
					enterInstance(player, 253);
					return null;
				}
				break;
			
			case "quest_middle":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "33954-5.htm";
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
		
		switch (npc.getId())
		{
			case Dolkin:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							htmltext = "33954-1.htm";
						}
						break;
					
					case 2:
						htmltext = "33954-4.htm";
						qs.takeItems(Secret_Ingredients, 1);
						qs.giveItems(Dolkin_Report, 1);
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
			
			case Dolkin2:
				if (cond == 2)
				{
					htmltext = "34002-1.htm";
				}
				break;
			
			case Karla:
				if (cond == 3)
				{
					htmltext = "33933-1.htm";
					qs.takeItems(Dolkin_Report, 1);
					qs.giveItems(57, 48000);
					qs.getPlayer().addExpAndSp(241076, 5);
					qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.CHECK_YOUR_EQUIPMENT_IN_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
					if (qs.getPlayer().getClassId().getId() == 182) // Ertheia Fighter
					{
						qs.giveItems(Faeron_Support_Box_Warrior, 1);
					}
					else if (qs.getPlayer().getClassId().getId() == 183) // Ertheia Wizard
					{
						qs.giveItems(Faeron_Support_Box_Mage, 1);
					}
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				break;
		}
		
		return htmltext;
	}
	
	private void enterInstance(Player player, int instancedZoneId)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(instancedZoneId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(instancedZoneId))
		{
			ReflectionUtils.enterReflection(player, instancedZoneId);
		}
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			switch (npc.getId())
			{
				case Karaphon:
					qs.playSound(SOUND_ITEMGET);
					++killedKaraphon;
					if (killedKaraphon == 1)
					{
						qs.setCond(2);
						qs.playSound(SOUND_MIDDLE);
						qs.giveItems(Secret_Ingredients, 1);
						qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.TALK_TO_DOLKIN_AND_LEAVE_THE_KARAPHON_HABITAT, 4500, ScreenMessageAlign.TOP_CENTER));
						qs.getPlayer().getReflection().addSpawnWithoutRespawn(Dolkin2, new Location(-82100, 246311, -14152, 0), 0);
					}
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