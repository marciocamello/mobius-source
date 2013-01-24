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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class BlockInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean _isRed;
	
	public BlockInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	public boolean isRed()
	{
		return _isRed;
	}
	
	public void setRed(boolean red)
	{
		_isRed = red;
		broadcastCharInfo();
	}
	
	public void changeColor()
	{
		setRed(!_isRed);
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
	}
	
	@Override
	public boolean isNameAbove()
	{
		return false;
	}
	
	@Override
	public int getFormId()
	{
		return _isRed ? 0x53 : 0;
	}
	
	@Override
	public boolean isInvul()
	{
		return true;
	}
}
