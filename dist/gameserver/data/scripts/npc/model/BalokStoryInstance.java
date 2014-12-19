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
package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Awakeninger + Nache
 */
public final class BalokStoryInstance extends NpcInstance
{
	
	public BalokStoryInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		switch (command)
		{
			case "request_video":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SI_BARLOG_STORY);
				break;
			
			case "request_video2":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SC_SOULISLAND_QUEST);
				break;
			
			case "request_video3":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SC_METUCELLAR_OPENING);
				break;
			
			case "request_video4":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SC_SUB_QUEST);
				break;
			
			case "request_video5":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SC_NOBLE_ENDING);
				break;
			
			case "request_video6":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SI_ILLUSION_01_QUE);
				break;
			
			case "request_video7":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SI_ILLUSION_02_QUE);
				break;
			
			case "request_video8":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SI_ILLUSION_03_QUE);
				break;
			
			case "request_video9":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SI_ILLUSION_04_QUE);
				break;
			
			case "request_video10":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SI_ILLUSION_05_QUE);
				break;
			
			case "request_video11":
				player.showQuestMovie(ExStartScenePlayer.SCENE_SC_BLOODVEIN_OPENING);
				break;
			
			default:
				super.onBypassFeedback(player, command);
				break;
		}
	}
}