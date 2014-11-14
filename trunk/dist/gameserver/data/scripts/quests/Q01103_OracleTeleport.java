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
import lineage2.gameserver.scripts.ScriptFile;

public class Q01103_OracleTeleport extends Quest implements ScriptFile
{
	private static final int GLUDIN_DAWN = 31078;
	private static final int GLUDIN_DUSK = 31085;
	private static final int GLUDIO_DAWN = 31079;
	private static final int GLUDIO_DUSK = 31086;
	private static final int DION_DAWN = 31080;
	private static final int DION_DUSK = 31087;
	private static final int GIRAN_DAWN = 31081;
	private static final int GIRAN_DUSK = 31088;
	private static final int OREN_DAWN = 31083;
	private static final int OREN_DUSK = 31090;
	private static final int ADEN_DAWN = 31084;
	private static final int ADEN_DUSK = 31091;
	private static final int HEINE_DAWN = 31082;
	private static final int HEINE_DUSK = 31089;
	private static final int GODDARD_DAWN = 31692;
	private static final int GODDARD_DUSK = 31693;
	private static final int RUNE_DAWN = 31694;
	private static final int RUNE_DUSK = 31695;
	private static final int SCHUTTGART_DAWN = 31997;
	private static final int SCHUTTGART_DUSK = 31998;
	private static final int HV_DAWN = 31168;
	private static final int HV_DUSK = 31169;
	
	public Q01103_OracleTeleport()
	{
		super(false);
		
		for (int i = 31078; i <= 31091; i++)
		{
			addStartNpc(i);
		}
		
		for (int i = 31168; i <= 31170; i++)
		{
			addStartNpc(i);
		}
		
		for (int i = 31692; i <= 31696; i++)
		{
			addStartNpc(i);
		}
		
		for (int i = 31997; i <= 31999; i++)
		{
			addStartNpc(i);
		}
		
		for (int j = 31127; j <= 31142; j++)
		{
			addStartNpc(j);
		}
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		final Player player = st.getPlayer();
		String back = player.getVar("FestivalBackCoords");
		
		if (back == null)
		{
			back = "1";
		}
		
		String htmltext = "Started.htm";
		
		switch (npc.getId())
		{
			case GLUDIN_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "1", -1);
				return htmltext;
				
			case GLUDIO_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "2", -1);
				return htmltext;
				
			case DION_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "3", -1);
				return htmltext;
				
			case GIRAN_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "4", -1);
				return htmltext;
				
			case OREN_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "5", -1);
				return htmltext;
				
			case ADEN_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "6", -1);
				return htmltext;
				
			case HEINE_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "7", -1);
				return htmltext;
				
			case GODDARD_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "8", -1);
				return htmltext;
				
			case RUNE_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "9", -1);
				return htmltext;
				
			case SCHUTTGART_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "10", -1);
				return htmltext;
				
			case HV_DAWN:
				player.teleToLocation(-80157, 111344, -4901);
				player.setVar("id", "11", -1);
				return htmltext;
				
			case GLUDIN_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "1", -1);
				return htmltext;
				
			case GLUDIO_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "2", -1);
				return htmltext;
				
			case DION_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "3", -1);
				return htmltext;
				
			case GIRAN_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "4", -1);
				return htmltext;
				
			case OREN_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "5", -1);
				return htmltext;
				
			case ADEN_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "6", -1);
				return htmltext;
				
			case HEINE_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "7", -1);
				return htmltext;
				
			case GODDARD_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "8", -1);
				return htmltext;
				
			case RUNE_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "9", -1);
				return htmltext;
				
			case SCHUTTGART_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "10", -1);
				return htmltext;
				
			case HV_DUSK:
				player.teleToLocation(-81261, 86531, -5157);
				player.setVar("id", "11", -1);
				return htmltext;
		}
		
		htmltext = "Completed.htm";
		
		switch (back)
		{
			case "1":
				player.teleToLocation(-80826, 149775, -3043);
				return htmltext;
				
			case "2":
				player.teleToLocation(-12672, 122776, -3116);
				return htmltext;
				
			case "3":
				player.teleToLocation(15670, 142983, -2705);
				return htmltext;
				
			case "4":
				player.teleToLocation(83400, 147943, -3404);
				return htmltext;
				
			case "5":
				player.teleToLocation(82956, 53162, -1495);
				return htmltext;
				
			case "6":
				player.teleToLocation(146331, 25762, -2018);
				return htmltext;
				
			case "7":
				player.teleToLocation(111409, 219364, -3545);
				return htmltext;
				
			case "8":
				player.teleToLocation(147928, -55273, -2734);
				return htmltext;
				
			case "9":
				player.teleToLocation(43799, -47727, -798);
				return htmltext;
				
			case "10":
				player.teleToLocation(87386, -143246, -1293);
				return htmltext;
				
			case "11":
				player.teleToLocation(116819, 76994, -2714);
				return htmltext;
		}
		
		return htmltext;
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
