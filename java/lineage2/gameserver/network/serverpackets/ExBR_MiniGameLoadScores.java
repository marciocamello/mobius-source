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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExBR_MiniGameLoadScores extends L2GameServerPacket
{
	/**
	 * Field _place.
	 */
	private int _place;
	/**
	 * Field _score.
	 */
	private int _score;
	/**
	 * Field _lastScore.
	 */
	private int _lastScore;
	/**
	 * Field _entries.
	 */
	private final IntObjectMap<List<Map.Entry<String, Integer>>> _entries = new TreeIntObjectMap<>();
	
	/**
	 * Constructor for ExBR_MiniGameLoadScores.
	 * @param player Player
	 */
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
					_entries.put(i, (set = new ArrayList<>()));
				}
				if (name.equalsIgnoreCase(player.getName()))
				{
					if (entry.getKey() > lastBig)
					{
						_place = i;
						_score = (lastBig = entry.getKey());
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
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xDD);
		writeD(_place);
		writeD(_score);
		writeD(0x00);
		writeD(_lastScore);
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
