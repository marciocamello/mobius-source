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
package lineage2.gameserver.network.serverpackets.components;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum SceneMovie implements IStaticPacket
{
	/**
	 * Field LINDVIOR_SPAWN.
	 */
	LINDVIOR_SPAWN(1, 45500),
	/**
	 * Field ECHMUS_OPENING.
	 */
	ECHMUS_OPENING(2, 62000),
	/**
	 * Field ECHMUS_SUCCESS.
	 */
	ECHMUS_SUCCESS(3, 18000),
	/**
	 * Field ECHMUS_FAIL.
	 */
	ECHMUS_FAIL(4, 17000),
	/**
	 * Field TIAT_OPENING.
	 */
	TIAT_OPENING(5, 54200),
	/**
	 * Field TIAT_SUCCESS.
	 */
	TIAT_SUCCESS(6, 26100),
	/**
	 * Field TIAT_FAIL.
	 */
	TIAT_FAIL(7, 24800),
	/**
	 * Field SSQ_SERIES_OF_DOUBT.
	 */
	SSQ_SERIES_OF_DOUBT(8, 26000),
	/**
	 * Field SSQ_DYING_MESSAGE.
	 */
	SSQ_DYING_MESSAGE(9, 27000),
	/**
	 * Field SSQ_MAMMONS_CONTRACT.
	 */
	SSQ_MAMMONS_CONTRACT(10, 98000),
	/**
	 * Field SSQ_SECRET_RITUAL_PRIEST.
	 */
	SSQ_SECRET_RITUAL_PRIEST(11, 30000),
	/**
	 * Field SSQ_SEAL_EMPEROR_1.
	 */
	SSQ_SEAL_EMPEROR_1(12, 18000),
	/**
	 * Field SSQ_SEAL_EMPEROR_2.
	 */
	SSQ_SEAL_EMPEROR_2(13, 26000),
	/**
	 * Field SSQ_EMBRYO.
	 */
	SSQ_EMBRYO(14, 28000),
	/**
	 * Field FREYA_OPENING.
	 */
	FREYA_OPENING(15, 53500),
	/**
	 * Field FREYA_PHASE_CHANGE_A.
	 */
	FREYA_PHASE_CHANGE_A(16, 21100),
	/**
	 * Field FREYA_PHASE_CHANGE_B.
	 */
	FREYA_PHASE_CHANGE_B(17, 21500),
	/**
	 * Field KEGOR_INTRUSION.
	 */
	KEGOR_INTRUSION(18, 27000),
	/**
	 * Field FREYA_ENDING_A.
	 */
	FREYA_ENDING_A(19, 16000),
	/**
	 * Field FREYA_ENDING_B.
	 */
	FREYA_ENDING_B(20, 56000),
	/**
	 * Field FREYA_FORCED_DEFEAT.
	 */
	FREYA_FORCED_DEFEAT(21, 21000),
	/**
	 * Field FREYA_DEFEAT.
	 */
	FREYA_DEFEAT(22, 20500),
	/**
	 * Field ICE_HEAVY_KNIGHT_SPAWN.
	 */
	ICE_HEAVY_KNIGHT_SPAWN(23, 7000),
	/**
	 * Field SSQ2_HOLY_BURIAL_GROUND_OPENING.
	 */
	SSQ2_HOLY_BURIAL_GROUND_OPENING(24, 23000),
	/**
	 * Field SSQ2_HOLY_BURIAL_GROUND_CLOSING.
	 */
	SSQ2_HOLY_BURIAL_GROUND_CLOSING(25, 22000),
	/**
	 * Field SSQ2_SOLINA_TOMB_OPENING.
	 */
	SSQ2_SOLINA_TOMB_OPENING(26, 25000),
	/**
	 * Field SSQ2_SOLINA_TOMB_CLOSING.
	 */
	SSQ2_SOLINA_TOMB_CLOSING(27, 15000),
	/**
	 * Field SSQ2_ELYSS_NARRATION.
	 */
	SSQ2_ELYSS_NARRATION(28, 59000),
	/**
	 * Field SSQ2_BOSS_OPENING.
	 */
	SSQ2_BOSS_OPENING(29, 60000),
	/**
	 * Field SSQ2_BOSS_CLOSING.
	 */
	SSQ2_BOSS_CLOSING(30, 60000),
	/**
	 * Field sc_istina_opening.
	 */
	sc_istina_opening(31, 36700),
	/**
	 * Field sc_istina_ending_a.
	 */
	sc_istina_ending_a(32, 23300),
	/**
	 * Field sc_istina_ending_b.
	 */
	sc_istina_ending_b(33, 22200),
	/**
	 * Field sc_istina_bridge.
	 */
	sc_istina_bridge(34, 7200),
	/**
	 * Field sc_octabis_opening.
	 */
	sc_octabis_opening(35, 26600),
	/**
	 * Field sc_octabis_phasech_a.
	 */
	sc_octabis_phasech_a(36, 10000),
	/**
	 * Field sc_octabis_phasech_b.
	 */
	sc_octabis_phasech_b(37, 14000),
	/**
	 * Field sc_octabis_ending.
	 */
	sc_octabis_ending(38, 38000),
	/**
	 * Field sc_gd1_prologue.
	 */
	sc_gd1_prologue(42, 64000),
	/**
	 * Field sc_talking_island_boss_opening.
	 */
	sc_talking_island_boss_opening(43, 47430),
	/**
	 * Field sc_talking_island_boss_ending.
	 */
	sc_talking_island_boss_ending(44, 32040),
	/**
	 * Field sc_awakening_opening.
	 */
	sc_awakening_opening(45, 27000),
	/**
	 * Field sc_awakening_boss_opening.
	 */
	sc_awakening_boss_opening(46, 29950),
	/**
	 * Field sc_awakening_boss_ending_a.
	 */
	sc_awakening_boss_ending_a(47, 25050),
	/**
	 * Field sc_awakening_boss_ending_b.
	 */
	sc_awakening_boss_ending_b(48, 13100),
	/**
	 * Field sc_earthworm_ending.
	 */
	sc_earthworm_ending(49, 32600),
	/**
	 * Field sc_spacia_opening.
	 */
	sc_spacia_opening(50, 38600),
	/**
	 * Field sc_spacia_a.
	 */
	sc_spacia_a(51, 29500),
	/**
	 * Field sc_spacia_b.
	 */
	sc_spacia_b(52, 45000),
	/**
	 * Field sc_spacia_c.
	 */
	sc_spacia_c(53, 36000),
	/**
	 * Field sc_spacia_ending.
	 */
	sc_spacia_ending(54, 23000),
	/**
	 * Field sc_awakening_view.
	 */
	sc_awakening_view(55, 34000),
	/**
	 * Field sc_awakening_opening_c.
	 */
	sc_awakening_opening_c(56, 28500),
	/**
	 * Field sc_awakening_opening_d.
	 */
	sc_awakening_opening_d(57, 20000),
	/**
	 * Field sc_awakening_opening_e.
	 */
	sc_awakening_opening_e(58, 24000),
	/**
	 * Field sc_awakening_opening_f.
	 */
	sc_awakening_opening_f(59, 38100),
	
	sc_tauti_opening_b(69, 15000),
	sc_tauti_opening(70, 15000),
	sc_tauti_phase(71, 15000),
	sc_tauti_ending(72, 15000),
	sc_noble_opening(99, 10000),
	sc_noble_ending(100, 10000),
	/**
	 * Field si_illusion_01_que.
	 */
	si_illusion_01_que(101, 29200),
	/**
	 * Field si_illusion_02_que.
	 */
	si_illusion_02_que(102, 27150),
	/**
	 * Field si_illusion_03_que.
	 */
	si_illusion_03_que(103, 16100),
	/**
	 * Field si_arkan_enter.
	 */
	si_arkan_enter(104, 30300),
	/**
	 * Field si_barlog_opening.
	 */
	si_barlog_opening(105, 19300),
	/**
	 * Field si_barlog_story.
	 */
	si_barlog_story(106, 67500),
	/**
	 * Field si_illusion_04_que.
	 */
	si_illusion_04_que(107, 10100),
	/**
	 * Field si_illusion_05_que.
	 */
	si_illusion_05_que(108, 10100),
	/**
	 * Field LANDING_KSERTH_LEFT.
	 */
	LANDING_KSERTH_LEFT(1000, 10000),
	/**
	 * Field LANDING_KSERTH_RIGHT.
	 */
	LANDING_KSERTH_RIGHT(1001, 10000),
	/**
	 * Field LANDING_INFINITY.
	 */
	LANDING_INFINITY(1002, 10000),
	/**
	 * Field LANDING_DESTRUCTION.
	 */
	LANDING_DESTRUCTION(1003, 10000),
	/**
	 * Field LANDING_ANNIHILATION.
	 */
	LANDING_ANNIHILATION(1004, 15000);
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _duration.
	 */
	private final int _duration;
	/**
	 * Field _static.
	 */
	private final L2GameServerPacket _static;
	
	/**
	 * Constructor for SceneMovie.
	 * @param id int
	 * @param duration int
	 */
	SceneMovie(int id, int duration)
	{
		_id = id;
		_duration = duration;
		_static = new ExStartScenePlayer(this);
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getDuration.
	 * @return int
	 */
	public int getDuration()
	{
		return _duration;
	}
	
	/**
	 * Method packet.
	 * @param player Player
	 * @return L2GameServerPacket * @see lineage2.gameserver.network.serverpackets.components.IStaticPacket#packet(Player)
	 */
	@Override
	public L2GameServerPacket packet(Player player)
	{
		return _static;
	}
}
