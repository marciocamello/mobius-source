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
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.scripts.ScriptFile;

public class Q01102_Nottingale extends Quest implements ScriptFile
{
	private final static int Nottingale = 32627;
	
	public Q01102_Nottingale()
	{
		super(false);
		addFirstTalkId(Nottingale);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		final QuestState state = player.getQuestState(Q10273_GoodDayToFly.class);
		
		if ((state == null) || (state.getState() != COMPLETED))
		{
			player.sendPacket(new RadarControl(2, 2, 0, 0, 0));
			player.sendPacket(new RadarControl(0, 2, -184545, 243120, 1581));
			htmltext = "32627.htm";
		}
		
		switch (event)
		{
			case "32627-3.htm":
				player.sendPacket(new RadarControl(2, 2, 0, 0, 0));
				player.sendPacket(new RadarControl(0, 2, -192361, 254528, 3598));
				break;
			
			case "32627-4.htm":
				player.sendPacket(new RadarControl(2, 2, 0, 0, 0));
				player.sendPacket(new RadarControl(0, 2, -174600, 219711, 4424));
				break;
			
			case "32627-5.htm":
				player.sendPacket(new RadarControl(2, 2, 0, 0, 0));
				player.sendPacket(new RadarControl(0, 2, -181989, 208968, 4424));
				break;
			
			case "32627-6.htm":
				player.sendPacket(new RadarControl(2, 2, 0, 0, 0));
				player.sendPacket(new RadarControl(0, 2, -252898, 235845, 5343));
				break;
			
			case "32627-8.htm":
				player.sendPacket(new RadarControl(2, 2, 0, 0, 0));
				player.sendPacket(new RadarControl(0, 2, -212819, 209813, 4288));
				break;
			
			case "32627-9.htm":
				player.sendPacket(new RadarControl(2, 2, 0, 0, 0));
				player.sendPacket(new RadarControl(0, 2, -246899, 251918, 4352));
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(getClass());
		
		if (qs == null)
		{
			newQuestState(player, STARTED);
		}
		
		return "";
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
