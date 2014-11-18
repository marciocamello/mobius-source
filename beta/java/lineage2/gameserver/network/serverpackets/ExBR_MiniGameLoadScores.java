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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lineage2.gameserver.instancemanager.games.MiniGameScoreManager;
import lineage2.gameserver.model.Player;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;

/**
 * @author VISTALL
 * @date 0:07:05/10.04.2010
 */
public class ExBR_MiniGameLoadScores extends L2GameServerPacket
{
	private int _place;
	private int _score;
	private int _lastScore;
	private final IntObjectMap<List<Map.Entry<String, Integer>>> _entries = new TreeIntObjectMap<>();
	
	public ExBR_MiniGameLoadScores(Player player)
	{
		int lastBig = 0;
		int i = 1;
		
		for (IntObjectMap.Entry<Set<String>> entry : MiniGameScoreManager.getInstance().getScores().entrySet())
		{
			for (String name : entry.getValue())
			{
				List<Map.Entry<String, Integer>> set = _entries.get(i);
				
				if (set == null)
				{
					_entries.put(i, set = new ArrayList<>());
				}
				
				if (name.equals(player.getName()))
				{
					if (entry.getKey() > lastBig)
					{
						_place = i;
						_score = lastBig = entry.getKey();
					}
				}
				
				set.add(new AbstractMap.SimpleImmutableEntry<>(name, entry.getKey()));
				i++;
				_lastScore = entry.getKey();
				
				if (i > 100)
				{
					break;
				}
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xDE);
		writeD(_place); // place of last big score of player
		writeD(_score); // last big score of player
		writeD(0x00); // ?
		writeD(_lastScore); // last score of list
		
		for (IntObjectMap.Entry<List<Map.Entry<String, Integer>>> entry : _entries.entrySet())
		{
			for (Map.Entry<String, Integer> scoreEntry : entry.getValue())
			{
				writeD(entry.getKey());
				writeS(scoreEntry.getKey());
				writeD(scoreEntry.getValue());
			}
		}
	}
}