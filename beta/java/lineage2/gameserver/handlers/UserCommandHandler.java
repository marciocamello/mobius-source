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
package lineage2.gameserver.handlers;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.interfaces.IUserCommandHandler;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class UserCommandHandler extends AbstractHolder
{
	private static final UserCommandHandler _instance = new UserCommandHandler();
	
	/**
	 * Method getInstance.
	 * @return UserCommandHandler
	 */
	public static UserCommandHandler getInstance()
	{
		return _instance;
	}
	
	private final TIntObjectHashMap<IUserCommandHandler> _datatable = new TIntObjectHashMap<>();
	
	/**
	 * Constructor for UserCommandHandler.
	 */
	private UserCommandHandler()
	{
	}
	
	/**
	 * Method registerUserCommandHandler.
	 * @param handler IUserCommandHandler
	 */
	public void registerUserCommandHandler(IUserCommandHandler handler)
	{
		int[] ids = handler.getUserCommandList();
		
		for (int element : ids)
		{
			_datatable.put(element, handler);
		}
	}
	
	/**
	 * Method getUserCommandHandler.
	 * @param userCommand int
	 * @return IUserCommandHandler
	 */
	public IUserCommandHandler getUserCommandHandler(int userCommand)
	{
		return _datatable.get(userCommand);
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _datatable.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_datatable.clear();
	}
}
