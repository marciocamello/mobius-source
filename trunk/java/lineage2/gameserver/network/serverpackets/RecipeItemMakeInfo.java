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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.item.RecipeTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RecipeItemMakeInfo extends L2GameServerPacket
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _isDwarvenRecipe.
	 */
	private final boolean _isDwarvenRecipe;
	/**
	 * Field _status.
	 */
	private final int _status;
	/**
	 * Field _curMP.
	 */
	private final int _curMP;
	/**
	 * Field _maxMP.
	 */
	private final int _maxMP;
	
	/**
	 * Constructor for RecipeItemMakeInfo.
	 * @param player Player
	 * @param recipeList RecipeTemplate
	 * @param status int
	 */
	public RecipeItemMakeInfo(Player player, RecipeTemplate recipeList, int status)
	{
		_id = recipeList.getId();
		_isDwarvenRecipe = recipeList.isDwarven();
		_status = status;
		_curMP = (int) player.getCurrentMp();
		_maxMP = player.getMaxMp();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xdd);
		writeD(_id);
		writeD(_isDwarvenRecipe ? 0x00 : 0x01);
		writeD(_curMP);
		writeD(_maxMP);
		writeD(_status);
	}
}
