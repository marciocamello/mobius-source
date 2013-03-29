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
package lineage2.gameserver.skills.effects;

import gnu.trove.map.hash.TIntIntHashMap;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.ExAlterSkillRequest;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectHellBinding extends Effect
{
	/**
	 * Constructor for EffectParalyze.
	 * @param env Env
	 * @param template EffectTemplate
	 */

	private static TIntIntHashMap _ChainedAirSkills = new TIntIntHashMap(8);
	
	private static TIntIntHashMap _ChainedTemporalReplace = new TIntIntHashMap(8);
	
	public EffectHellBinding(Env env, EffectTemplate template)
	{
		super(env, template);
		_ChainedAirSkills.clear();
		_ChainedTemporalReplace.clear();
		_ChainedAirSkills.put(139, 10249);
		_ChainedAirSkills.put(140, 10499);
		_ChainedAirSkills.put(141, 10749);
		_ChainedAirSkills.put(142, 10999);
		_ChainedAirSkills.put(143, 11247);
		_ChainedAirSkills.put(144, 11749);
		_ChainedAirSkills.put(145, 11499);
		_ChainedAirSkills.put(146, 11999);
		_ChainedTemporalReplace.put(10249, 10009);
		_ChainedTemporalReplace.put(10499, 10258);
		_ChainedTemporalReplace.put(10749, 10508);
		_ChainedTemporalReplace.put(10999, 10760); //Confirmed by lineage forum
		_ChainedTemporalReplace.put(11247, 11011);
		_ChainedTemporalReplace.put(11749, 11510);
		_ChainedTemporalReplace.put(11499, 11273);
		_ChainedTemporalReplace.put(11999, 11766);
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		if (_effected.isParalyzeImmune() || _effected.IsAirBind() || _effected.IsKnockedDown())
		{
			return false;
		}
		return super.checkCondition();
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		for(Player playerNearEffected : World.getAroundPlayers(_effected, 1200, 400))
		{
			if(playerNearEffected.getTarget() == _effected && playerNearEffected.isAwaking())
			{
				int chainSkill = _ChainedAirSkills.get(playerNearEffected.getClassId().getId());
				int temporalReplaceSkill = _ChainedTemporalReplace.get(chainSkill);
				playerNearEffected.sendPacket(new ExAlterSkillRequest(chainSkill,temporalReplaceSkill,5));	
			}
		}
		_effected.startAirBind();
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopAirBind(true);
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
