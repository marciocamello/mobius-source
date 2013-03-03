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
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.network.serverpackets.components.SceneMovie;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExStartScenePlayer extends L2GameServerPacket
{
	/**
	 * Field _sceneId.
	 */
	private final int _sceneId;
	/**
	 * Field SCENE_LINDVIOR. (value is 1)
	 */
	public static final int SCENE_LINDVIOR = 1;
	/**
	 * Field SCENE_ECHMUS_OPENING. (value is 2)
	 */
	public static final int SCENE_ECHMUS_OPENING = 2;
	/**
	 * Field SCENE_ECHMUS_SUCCESS. (value is 3)
	 */
	public static final int SCENE_ECHMUS_SUCCESS = 3;
	/**
	 * Field SCENE_ECHMUS_FAIL. (value is 4)
	 */
	public static final int SCENE_ECHMUS_FAIL = 4;
	/**
	 * Field SCENE_TIAT_OPENING. (value is 5)
	 */
	public static final int SCENE_TIAT_OPENING = 5;
	/**
	 * Field SCENE_TIAT_SUCCESS. (value is 6)
	 */
	public static final int SCENE_TIAT_SUCCESS = 6;
	/**
	 * Field SCENE_TIAT_FAIL. (value is 7)
	 */
	public static final int SCENE_TIAT_FAIL = 7;
	/**
	 * Field SCENE_SSQ_SUSPICIOUS_DEATH. (value is 8)
	 */
	public static final int SCENE_SSQ_SUSPICIOUS_DEATH = 8;
	/**
	 * Field SCENE_SSQ_DYING_MASSAGE. (value is 9)
	 */
	public static final int SCENE_SSQ_DYING_MASSAGE = 9;
	/**
	 * Field SCENE_SSQ_CONTRACT_OF_MAMMON. (value is 10)
	 */
	public static final int SCENE_SSQ_CONTRACT_OF_MAMMON = 10;
	/**
	 * Field SCENE_SSQ_RITUAL_OF_PRIEST. (value is 11)
	 */
	public static final int SCENE_SSQ_RITUAL_OF_PRIEST = 11;
	/**
	 * Field SCENE_SSQ_SEALING_EMPEROR_1ST. (value is 12)
	 */
	public static final int SCENE_SSQ_SEALING_EMPEROR_1ST = 12;
	/**
	 * Field SCENE_SSQ_SEALING_EMPEROR_2ND. (value is 13)
	 */
	public static final int SCENE_SSQ_SEALING_EMPEROR_2ND = 13;
	/**
	 * Field SCENE_SSQ_EMBRYO. (value is 14)
	 */
	public static final int SCENE_SSQ_EMBRYO = 14;
	/**
	 * Field SCENE_BOSS_FREYA_OPENING. (value is 15)
	 */
	public static final int SCENE_BOSS_FREYA_OPENING = 15;
	/**
	 * Field SCENE_BOSS_FREYA_PHASE_A. (value is 16)
	 */
	public static final int SCENE_BOSS_FREYA_PHASE_A = 16;
	/**
	 * Field SCENE_BOSS_FREYA_PHASE_B. (value is 17)
	 */
	public static final int SCENE_BOSS_FREYA_PHASE_B = 17;
	/**
	 * Field SCENE_BOSS_KEGOR_INTRUSION. (value is 18)
	 */
	public static final int SCENE_BOSS_KEGOR_INTRUSION = 18;
	/**
	 * Field SCENE_BOSS_FREYA_ENDING_A. (value is 19)
	 */
	public static final int SCENE_BOSS_FREYA_ENDING_A = 19;
	/**
	 * Field SCENE_BOSS_FREYA_ENDING_B. (value is 20)
	 */
	public static final int SCENE_BOSS_FREYA_ENDING_B = 20;
	/**
	 * Field SCENE_BOSS_FREYA_FORCED_DEFEAT. (value is 21)
	 */
	public static final int SCENE_BOSS_FREYA_FORCED_DEFEAT = 21;
	/**
	 * Field SCENE_BOSS_FREYA_DEFEAT. (value is 22)
	 */
	public static final int SCENE_BOSS_FREYA_DEFEAT = 22;
	/**
	 * Field SCENE_ICE_HEAVYKNIGHT_SPAWN. (value is 23)
	 */
	public static final int SCENE_ICE_HEAVYKNIGHT_SPAWN = 23;
	/**
	 * Field SCENE_SSQ2_HOLY_BURIAL_GROUND_OPENING. (value is 24)
	 */
	public static final int SCENE_SSQ2_HOLY_BURIAL_GROUND_OPENING = 24;
	/**
	 * Field SCENE_SSQ2_HOLY_BURIAL_GROUND_CLOSING. (value is 25)
	 */
	public static final int SCENE_SSQ2_HOLY_BURIAL_GROUND_CLOSING = 25;
	/**
	 * Field SCENE_SSQ2_SOLINA_TOMB_OPENING. (value is 26)
	 */
	public static final int SCENE_SSQ2_SOLINA_TOMB_OPENING = 26;
	/**
	 * Field SCENE_SSQ2_SOLINA_TOMB_CLOSING. (value is 27)
	 */
	public static final int SCENE_SSQ2_SOLINA_TOMB_CLOSING = 27;
	/**
	 * Field SCENE_SSQ2_ELYSS_NARRATION. (value is 28)
	 */
	public static final int SCENE_SSQ2_ELYSS_NARRATION = 28;
	/**
	 * Field SCENE_SSQ2_BOSS_OPENING. (value is 29)
	 */
	public static final int SCENE_SSQ2_BOSS_OPENING = 29;
	/**
	 * Field SCENE_SSQ2_BOSS_CLOSING. (value is 30)
	 */
	public static final int SCENE_SSQ2_BOSS_CLOSING = 30;
	/**
	 * Field SCENE_BOSS_ISTHINA_OPENING. (value is 31)
	 */
	public static final int SCENE_BOSS_ISTHINA_OPENING = 31;
	/**
	 * Field SCENE_BOSS_ISTHINA_ENDING_A. (value is 32)
	 */
	public static final int SCENE_BOSS_ISTHINA_ENDING_A = 32;
	/**
	 * Field SCENE_BOSS_ISTHINA_ENDING_B. (value is 33)
	 */
	public static final int SCENE_BOSS_ISTHINA_ENDING_B = 33;
	/**
	 * Field SCENE_BOSS_ISTHINA_BRIDGE. (value is 34)
	 */
	public static final int SCENE_BOSS_ISTHINA_BRIDGE = 34;
	/**
	 * Field SCENE_BOSS_OCTABIS_OPENING. (value is 35)
	 */
	public static final int SCENE_BOSS_OCTABIS_OPENING = 35;
	/**
	 * Field SCENE_BOSS_OCTABIS_PHASECH_A. (value is 36)
	 */
	public static final int SCENE_BOSS_OCTABIS_PHASECH_A = 36;
	/**
	 * Field SCENE_BOSS_OCTABIS_PHASECH_B. (value is 37)
	 */
	public static final int SCENE_BOSS_OCTABIS_PHASECH_B = 37;
	/**
	 * Field SCENE_BOSS_OCTABIS_ENDING. (value is 38)
	 */
	public static final int SCENE_BOSS_OCTABIS_ENDING = 38;
	/**
	 * Field SCENE_TALKING_ISLAND_BOSS_OPENING. (value is 43)
	 */
	public static final int SCENE_TALKING_ISLAND_BOSS_OPENING = 43;
	/**
	 * Field SCENE_TALKING_ISLAND_BOSS_ENDING. (value is 44)
	 */
	public static final int SCENE_TALKING_ISLAND_BOSS_ENDING = 44;
	/**
	 * Field SCENE_AWAKENING_OPENING. (value is 45)
	 */
	public static final int SCENE_AWAKENING_OPENING = 45;
	/**
	 * Field SCENE_AWAKENING_BOSS_OPENING. (value is 46)
	 */
	public static final int SCENE_AWAKENING_BOSS_OPENING = 46;
	/**
	 * Field SCENE_AWAKENING_BOSS_ENDING_A. (value is 47)
	 */
	public static final int SCENE_AWAKENING_BOSS_ENDING_A = 47;
	/**
	 * Field SCENE_AWAKENING_BOSS_ENDING_B. (value is 48)
	 */
	public static final int SCENE_AWAKENING_BOSS_ENDING_B = 48;
	/**
	 * Field SCENE_AWAKENING_VIEW. (value is 55)
	 */
	public static final int SCENE_AWAKENING_VIEW = 55;
	/**
	 * Field SCENE_AWAKENING_OPENING_C. (value is 56)
	 */
	public static final int SCENE_AWAKENING_OPENING_C = 56;
	/**
	 * Field SCENE_AWAKENING_OPENING_D. (value is 57)
	 */
	public static final int SCENE_AWAKENING_OPENING_D = 57;
	/**
	 * Field SCENE_AWAKENING_OPENING_E. (value is 58)
	 */
	public static final int SCENE_AWAKENING_OPENING_E = 58;
	/**
	 * Field SCENE_AWAKENING_OPENING_F. (value is 59)
	 */
	public static final int SCENE_AWAKENING_OPENING_F = 59;
	/**
	 * Field SCENE_ILLUSION_01_QUE. (value is 101)
	 */
	public static final int SCENE_ILLUSION_01_QUE = 101;
	/**
	 * Field SCENE_ILLUSION_02_QUE. (value is 102)
	 */
	public static final int SCENE_ILLUSION_02_QUE = 102;
	/**
	 * Field SCENE_ILLUSION_03_QUE. (value is 103)
	 */
	public static final int SCENE_ILLUSION_03_QUE = 103;
	
	public static final int ARKAN = 104;
	
	/**
	 * Constructor for ExStartScenePlayer.
	 * @param sceneId int
	 */
	public ExStartScenePlayer(int sceneId)
	{
		_sceneId = sceneId;
	}
	
	/**
	 * Constructor for ExStartScenePlayer.
	 * @param scene SceneMovie
	 */
	public ExStartScenePlayer(SceneMovie scene)
	{
		_sceneId = scene.getId();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x99);
		writeD(_sceneId);
	}
}
