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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.interfaces.IBypassHandler;

/**
 * @author Mobius
 */
public class BypassHandler extends AbstractHolder
{
	private static final BypassHandler _instance = new BypassHandler();
	
	/**
	 * Method getInstance.
	 * @return BypassHandler
	 */
	public static BypassHandler getInstance()
	{
		return _instance;
	}
	
	private final Map<String, IBypassHandler> _datatable = new HashMap<>();
	
	/**
	 * Constructor for BypassHandler.
	 */
	private BypassHandler()
	{
	}
	
	/**
	 * Method registerBypass.
	 * @param bypass IBypassHandler
	 */
	public void registerBypass(IBypassHandler bypass)
	{
		String[] ids = bypass.getBypasses();
		
		for (String element : ids)
		{
			_datatable.put(element, bypass);
		}
	}
	
	/**
	 * Method getBypasses.
	 * @param bypass String
	 * @return IBypassHandler
	 */
	public IBypassHandler getBypass(String bypass)
	{
		return _datatable.get(bypass);
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