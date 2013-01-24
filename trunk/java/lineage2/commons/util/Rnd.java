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
package lineage2.commons.util;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.math.random.MersenneTwister;
import org.apache.commons.math.random.RandomGenerator;

public class Rnd
{
	private Rnd()
	{
	}
	
	private static final Random random = new Random();
	private static final ThreadLocal<RandomGenerator> rnd = new ThreadLocalGeneratorHolder();
	static AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);
	
	static final class ThreadLocalGeneratorHolder extends ThreadLocal<RandomGenerator>
	{
		@Override
		public RandomGenerator initialValue()
		{
			return new MersenneTwister(seedUniquifier.getAndIncrement() + System.nanoTime());
		}
	}
	
	private static RandomGenerator rnd()
	{
		return rnd.get();
	}
	
	public static double get()
	{
		return rnd().nextDouble();
	}
	
	public static int get(int n)
	{
		return rnd().nextInt(n);
	}
	
	public static long get(long n)
	{
		return (long) (rnd().nextDouble() * n);
	}
	
	public static int get(int min, int max)
	{
		return min + get((max - min) + 1);
	}
	
	public static long get(long min, long max)
	{
		return min + get((max - min) + 1);
	}
	
	public static int nextInt()
	{
		return rnd().nextInt();
	}
	
	public static double nextDouble()
	{
		return rnd().nextDouble();
	}
	
	public static double nextGaussian()
	{
		return rnd().nextGaussian();
	}
	
	public static boolean nextBoolean()
	{
		return rnd().nextBoolean();
	}
	
	public static boolean chance(int chance)
	{
		return (chance >= 1) && ((chance > 99) || ((rnd().nextInt(99) + 1) <= chance));
	}
	
	public static boolean chance(double chance)
	{
		return rnd().nextDouble() <= (chance / 100.);
	}
	
	public static <E> E get(E[] list)
	{
		return list[get(list.length)];
	}
	
	public static int get(int[] list)
	{
		return list[get(list.length)];
	}
	
	public static <E> E get(List<E> list)
	{
		return list.get(get(list.size()));
	}
	
	public static byte[] nextBytes(byte[] array)
	{
		random.nextBytes(array);
		return array;
	}
}
