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
package lineage2.gameserver.network.clientpackets;

import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;
import lineage2.gameserver.model.ActionKey;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExUISetting;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSaveKeyMapping extends L2GameClientPacket
{
	int _keyCount;
	
	Map<Integer, List<ActionKey>> _keyMap = new FastMap<>();
	Map<Integer, List<Integer>> _catMap = new FastMap<>();
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		int category = 0;
		
		/* int _buffSize = */readD();
		/* int _categories = */readD();
		_keyCount = readD();
		for (int i = 0; i < _keyCount; i++)
		{
			int cmd1Size = readC();
			for (int j = 0; j < cmd1Size; j++)
			{
				int cmdId = readC();
				insertCategory(category, cmdId);
			}
			category++;
			
			int cmd2Size = readC();
			for (int j = 0; j < cmd2Size; j++)
			{
				int cmdId = readC();
				insertCategory(category, cmdId);
			}
			category++;
			
			int cmdSize = readD();
			for (int j = 0; j < cmdSize; j++)
			{
				int cmd = readD();
				int key = readD();
				int tgKey1 = readD();
				int tgKey2 = readD();
				int show = readD();
				insertKey(i, cmd, key, tgKey1, tgKey2, show);
			}
		}
		
		/* int _unk16 = */readD();
		/* int _unk17 = */readD();
	}
	
	public void insertCategory(int cat, int cmd)
	{
		if (_catMap.containsKey(cat))
		{
			_catMap.get(cat).add(cmd);
		}
		else
		{
			List<Integer> tmp = new FastList<>();
			tmp.add(cmd);
			_catMap.put(cat, tmp);
		}
	}
	
	public void insertKey(int cat, int cmdId, int key, int tgKey1, int tgKey2, int show)
	{
		ActionKey tmk = new ActionKey(cat, cmdId, key, tgKey1, tgKey2, show);
		if (_keyMap.containsKey(cat))
		{
			_keyMap.get(cat).add(tmk);
		}
		else
		{
			List<ActionKey> tmp = new FastList<>();
			tmp.add(tmk);
			_keyMap.put(cat, tmp);
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		activeChar.getUISettings().storeAll(_catMap, _keyMap);
		activeChar.sendPacket(new ExUISetting(activeChar));
	}
}
