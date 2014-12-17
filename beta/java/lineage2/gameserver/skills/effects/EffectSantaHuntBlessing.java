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

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 */
public class EffectSantaHuntBlessing extends Effect
{
	private final static int SANTA_MARK = 40313;
	private static final String EVENT_NAME = "HuntForSanta";
	
	public EffectSantaHuntBlessing(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public void onExit()
	{
		super.onExit();
		
		final Player player = (Player) getEffected();
		final String var = player.getVar(EVENT_NAME);
		
		if (!player.isDead() && (player.getInventoryLimit() > player.getInventory().getSize()) && (var == null))
		{
			ItemFunctions.addItem(player, SANTA_MARK, 1, true);
		}
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
