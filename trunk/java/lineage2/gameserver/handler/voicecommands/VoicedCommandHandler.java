/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.handler.voicecommands;

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.handler.voicecommands.impl.Debug;
import lineage2.gameserver.handler.voicecommands.impl.Hellbound;
import lineage2.gameserver.handler.voicecommands.impl.Help;
import lineage2.gameserver.handler.voicecommands.impl.Offline;
import lineage2.gameserver.handler.voicecommands.impl.Online;
import lineage2.gameserver.handler.voicecommands.impl.Password;
import lineage2.gameserver.handler.voicecommands.impl.Ping;
import lineage2.gameserver.handler.voicecommands.impl.Repair;
import lineage2.gameserver.handler.voicecommands.impl.ServerInfo;
import lineage2.gameserver.handler.voicecommands.impl.Wedding;
import lineage2.gameserver.handler.voicecommands.impl.WhoAmI;

public class VoicedCommandHandler extends AbstractHolder
{
	private static final VoicedCommandHandler _instance = new VoicedCommandHandler();
	
	public static VoicedCommandHandler getInstance()
	{
		return _instance;
	}
	
	private final Map<String, IVoicedCommandHandler> _datatable = new HashMap<>();
	
	private VoicedCommandHandler()
	{
		registerVoicedCommandHandler(new Help());
		registerVoicedCommandHandler(new Hellbound());
		registerVoicedCommandHandler(new Offline());
		registerVoicedCommandHandler(new Debug());
		registerVoicedCommandHandler(new Repair());
		registerVoicedCommandHandler(new ServerInfo());
		registerVoicedCommandHandler(new Wedding());
		registerVoicedCommandHandler(new WhoAmI());
		registerVoicedCommandHandler(new Online());
		registerVoicedCommandHandler(new Ping());
		registerVoicedCommandHandler(new Password());
	}
	
	public void registerVoicedCommandHandler(IVoicedCommandHandler handler)
	{
		String[] ids = handler.getVoicedCommandList();
		for (String element : ids)
		{
			_datatable.put(element, handler);
		}
	}
	
	public IVoicedCommandHandler getVoicedCommandHandler(String voicedCommand)
	{
		String command = voicedCommand;
		if (voicedCommand.contains(" "))
		{
			command = voicedCommand.substring(0, voicedCommand.indexOf(" "));
		}
		return _datatable.get(command);
	}
	
	@Override
	public int size()
	{
		return _datatable.size();
	}
	
	@Override
	public void clear()
	{
		_datatable.clear();
	}
}
