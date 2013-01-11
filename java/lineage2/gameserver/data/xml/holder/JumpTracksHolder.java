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
package lineage2.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.jump.JumpTrack;

public final class JumpTracksHolder extends AbstractHolder
{
	private static final JumpTracksHolder _instance = new JumpTracksHolder();
	private final TIntObjectHashMap<JumpTrack> _jumpingTracks = new TIntObjectHashMap<>();
	
	public static JumpTracksHolder getInstance()
	{
		return _instance;
	}
	
	public void addTrack(JumpTrack track)
	{
		_jumpingTracks.put(track.getId(), track);
	}
	
	public JumpTrack getTrack(int id)
	{
		return _jumpingTracks.get(id);
	}
	
	@Override
	public int size()
	{
		return _jumpingTracks.size();
	}
	
	@Override
	public void clear()
	{
		_jumpingTracks.clear();
	}
}
