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
package zones;

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;
import quests._10320_LetsGototheCentralSquare;

public class TalkingIslandMovie implements ScriptFile
{
	private static final String ZONE_NAME = "[ti_presentation_video]";
	private static ZoneListener _zoneListener;
	
	@Override
	public void onLoad()
	{
		init();
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	private void init()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(ZONE_NAME);
		if (zone != null)
		{
			zone.addListener(_zoneListener);
		}
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (cha.isPlayer())
			{
				Player player = cha.getPlayer();
				if (!player.getVarB("@ti_present_video"))
				{
					QuestState qs = player.getQuestState(_10320_LetsGototheCentralSquare.class);
					if ((qs != null) && (qs.getCond() == 1))
					{
						player.showQuestMovie(SceneMovie.si_illusion_02_que);
					}
					else
					{
						player.showQuestMovie(SceneMovie.si_illusion_01_que);
					}
					player.setVar("@ti_present_video", "true", -1);
				}
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}
