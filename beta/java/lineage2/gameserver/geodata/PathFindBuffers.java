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
package lineage2.gameserver.geodata;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.gameserver.Config;
import lineage2.gameserver.utils.Location;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
class PathFindBuffers
{
	private final static int MIN_MAP_SIZE = 1 << 6;
	private final static int STEP_MAP_SIZE = 1 << 5;
	private final static int MAX_MAP_SIZE = 1 << 9;
	private static final TIntObjectHashMap<PathFindBuffer[]> buffers = new TIntObjectHashMap<>();
	private static int[] sizes = new int[0];
	private static final Lock lock = new ReentrantLock();
	static
	{
		TIntIntHashMap config = new TIntIntHashMap();
		String[] k;
		
		for (String e : Config.PATHFIND_BUFFERS.split(";"))
		{
			if (!e.isEmpty() && ((k = e.split("x")).length == 2))
			{
				config.put(Integer.valueOf(k[1]), Integer.valueOf(k[0]));
			}
		}
		
		TIntIntIterator itr = config.iterator();
		
		while (itr.hasNext())
		{
			itr.advance();
			int size = itr.key();
			int count = itr.value();
			PathFindBuffer[] buff = new PathFindBuffer[count];
			
			for (int i = 0; i < count; i++)
			{
				buff[i] = new PathFindBuffer(size);
			}
			
			buffers.put(size, buff);
		}
		
		sizes = config.keys();
		Arrays.sort(sizes);
	}
	
	/**
	 * Method create.
	 * @param mapSize int
	 * @return PathFindBuffer
	 */
	private static PathFindBuffer create(int mapSize)
	{
		lock.lock();
		
		try
		{
			PathFindBuffer buffer;
			PathFindBuffer[] buff = buffers.get(mapSize);
			
			if (buff != null)
			{
				buff = lineage2.commons.lang.ArrayUtils.add(buff, buffer = new PathFindBuffer(mapSize));
			}
			else
			{
				buff = new PathFindBuffer[]
				{
					buffer = new PathFindBuffer(mapSize)
				};
				sizes = org.apache.commons.lang3.ArrayUtils.add(sizes, mapSize);
				Arrays.sort(sizes);
			}
			
			buffers.put(mapSize, buff);
			buffer.inUse = true;
			return buffer;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method get.
	 * @param mapSize int
	 * @return PathFindBuffer
	 */
	private static PathFindBuffer get(int mapSize)
	{
		lock.lock();
		
		try
		{
			PathFindBuffer[] buff = buffers.get(mapSize);
			
			for (PathFindBuffer buffer : buff)
			{
				if (!buffer.inUse)
				{
					buffer.inUse = true;
					return buffer;
				}
			}
			
			return null;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * Method alloc.
	 * @param mapSize int
	 * @return PathFindBuffer
	 */
	static PathFindBuffer alloc(int mapSize)
	{
		if (mapSize > MAX_MAP_SIZE)
		{
			return null;
		}
		
		mapSize += STEP_MAP_SIZE;
		
		if (mapSize < MIN_MAP_SIZE)
		{
			mapSize = MIN_MAP_SIZE;
		}
		
		PathFindBuffer buffer = null;
		
		for (int size : sizes)
		{
			if (size >= mapSize)
			{
				mapSize = size;
				buffer = get(mapSize);
				break;
			}
		}
		
		if (buffer == null)
		{
			for (int size = MIN_MAP_SIZE; size < MAX_MAP_SIZE; size += STEP_MAP_SIZE)
			{
				if (size >= mapSize)
				{
					mapSize = size;
					buffer = create(mapSize);
					break;
				}
			}
		}
		
		return buffer;
	}
	
	/**
	 * Method recycle.
	 * @param buffer PathFindBuffer
	 */
	static void recycle(PathFindBuffer buffer)
	{
		lock.lock();
		
		try
		{
			buffer.inUse = false;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class PathFindBuffer
	{
		final int mapSize;
		final GeoNode[][] nodes;
		final Queue<GeoNode> open;
		int offsetX, offsetY;
		boolean inUse;
		long totalUses;
		long successUses;
		long overtimeUses;
		long playableUses;
		long totalTime;
		long totalItr;
		
		/**
		 * Constructor for PathFindBuffer.
		 * @param mapSize int
		 */
		PathFindBuffer(int mapSize)
		{
			open = new PriorityQueue<>(mapSize);
			this.mapSize = mapSize;
			nodes = new GeoNode[mapSize][mapSize];
			
			for (int i = 0; i < nodes.length; i++)
			{
				for (int j = 0; j < nodes[i].length; j++)
				{
					nodes[i][j] = new GeoNode();
				}
			}
		}
		
		/**
		 * Method free.
		 */
		void free()
		{
			open.clear();
			
			for (GeoNode[] node : nodes)
			{
				for (GeoNode element : node)
				{
					element.free();
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class GeoNode implements Comparable<GeoNode>
	{
		final static int NONE = 0;
		final static int OPENED = 1;
		final static int CLOSED = -1;
		int x, y;
		short z, nswe;
		float totalCost, costFromStart, costToEnd;
		int state;
		GeoNode parent;
		
		/**
		 * Constructor for GeoNode.
		 */
		public GeoNode()
		{
			nswe = -1;
		}
		
		/**
		 * Method set.
		 * @param x int
		 * @param y int
		 * @param z short
		 * @return GeoNode
		 */
		GeoNode set(int x, int y, short z)
		{
			setX(x);
			setY(y);
			setZ(z);
			return this;
		}
		
		/**
		 * Method getX.
		 * @return x int
		 */
		int getX()
		{
			return x;
		}
		
		/**
		 * Method getY.
		 * @return y int
		 */
		int getY()
		{
			return y;
		}
		
		/**
		 * Method getZ.
		 * @return z short
		 */
		short getZ()
		{
			return z;
		}
		
		/**
		 * Method setX.
		 * @param newX int
		 */
		void setX(int newX)
		{
			x = newX;
		}
		
		/**
		 * Method setY.
		 * @param newY int
		 */
		void setY(int newY)
		{
			y = newY;
		}
		
		/**
		 * Method setZ.
		 * @param newZ short
		 */
		void setZ(short newZ)
		{
			z = newZ;
		}
		
		/**
		 * Method isSet.
		 * @return boolean
		 */
		public boolean isSet()
		{
			return nswe != -1;
		}
		
		/**
		 * Method free.
		 */
		void free()
		{
			nswe = -1;
			costFromStart = 0f;
			totalCost = 0f;
			costToEnd = 0f;
			parent = null;
			state = NONE;
		}
		
		/**
		 * Method getLoc.
		 * @return Location
		 */
		public Location getLoc()
		{
			return new Location(x, y, z);
		}
		
		/**
		 * Method toString.
		 * @return String
		 */
		@Override
		public String toString()
		{
			return "[" + x + "," + y + "," + z + "] f: " + totalCost;
		}
		
		/**
		 * Method compareTo.
		 * @param o GeoNode
		 * @return int
		 */
		@Override
		public int compareTo(GeoNode o)
		{
			if (totalCost > o.totalCost)
			{
				return 1;
			}
			
			if (totalCost < o.totalCost)
			{
				return -1;
			}
			
			return 0;
		}
	}
}
