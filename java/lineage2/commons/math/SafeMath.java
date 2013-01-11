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
package lineage2.commons.math;

public class SafeMath
{
	public static int addAndCheck(int a, int b) throws ArithmeticException
	{
		return addAndCheck(a, b, "overflow: add", false);
	}
	
	public static int addAndLimit(int a, int b)
	{
		return addAndCheck(a, b, null, true);
	}
	
	private static int addAndCheck(int a, int b, String msg, boolean limit)
	{
		int ret;
		if (a > b)
		{
			ret = addAndCheck(b, a, msg, limit);
		}
		else if (a < 0)
		{
			if (b < 0)
			{
				if ((Integer.MIN_VALUE - b) <= a)
				{
					ret = a + b;
				}
				else if (limit)
				{
					ret = Integer.MIN_VALUE;
				}
				else
				{
					throw new ArithmeticException(msg);
				}
			}
			else
			{
				ret = a + b;
			}
		}
		else if (a <= (Integer.MAX_VALUE - b))
		{
			ret = a + b;
		}
		else if (limit)
		{
			ret = Integer.MAX_VALUE;
		}
		else
		{
			throw new ArithmeticException(msg);
		}
		return ret;
	}
	
	public static long addAndLimit(long a, long b)
	{
		return addAndCheck(a, b, "overflow: add", true);
	}
	
	public static long addAndCheck(long a, long b) throws ArithmeticException
	{
		return addAndCheck(a, b, "overflow: add", false);
	}
	
	private static long addAndCheck(long a, long b, String msg, boolean limit)
	{
		long ret;
		if (a > b)
		{
			ret = addAndCheck(b, a, msg, limit);
		}
		else if (a < 0)
		{
			if (b < 0)
			{
				if ((Long.MIN_VALUE - b) <= a)
				{
					ret = a + b;
				}
				else if (limit)
				{
					ret = Long.MIN_VALUE;
				}
				else
				{
					throw new ArithmeticException(msg);
				}
			}
			else
			{
				ret = a + b;
			}
		}
		else if (a <= (Long.MAX_VALUE - b))
		{
			ret = a + b;
		}
		else if (limit)
		{
			ret = Long.MAX_VALUE;
		}
		else
		{
			throw new ArithmeticException(msg);
		}
		return ret;
	}
	
	public static int mulAndCheck(int a, int b) throws ArithmeticException
	{
		return mulAndCheck(a, b, "overflow: mul", false);
	}
	
	public static int mulAndLimit(int a, int b)
	{
		return mulAndCheck(a, b, "overflow: mul", true);
	}
	
	private static int mulAndCheck(int a, int b, String msg, boolean limit)
	{
		int ret;
		if (a > b)
		{
			ret = mulAndCheck(b, a, msg, limit);
		}
		else if (a < 0)
		{
			if (b < 0)
			{
				if (a >= (Integer.MAX_VALUE / b))
				{
					ret = a * b;
				}
				else if (limit)
				{
					ret = Integer.MAX_VALUE;
				}
				else
				{
					throw new ArithmeticException(msg);
				}
			}
			else if (b > 0)
			{
				if ((Integer.MIN_VALUE / b) <= a)
				{
					ret = a * b;
				}
				else if (limit)
				{
					ret = Integer.MIN_VALUE;
				}
				else
				{
					throw new ArithmeticException(msg);
				}
			}
			else
			{
				ret = 0;
			}
		}
		else if (a > 0)
		{
			if (a <= (Integer.MAX_VALUE / b))
			{
				ret = a * b;
			}
			else if (limit)
			{
				ret = Integer.MAX_VALUE;
			}
			else
			{
				throw new ArithmeticException(msg);
			}
		}
		else
		{
			ret = 0;
		}
		return ret;
	}
	
	public static long mulAndCheck(long a, long b) throws ArithmeticException
	{
		return mulAndCheck(a, b, "overflow: mul", false);
	}
	
	public static long mulAndLimit(long a, long b)
	{
		return mulAndCheck(a, b, "overflow: mul", true);
	}
	
	private static long mulAndCheck(long a, long b, String msg, boolean limit)
	{
		long ret;
		if (a > b)
		{
			ret = mulAndCheck(b, a, msg, limit);
		}
		else if (a < 0)
		{
			if (b < 0)
			{
				if (a >= (Long.MAX_VALUE / b))
				{
					ret = a * b;
				}
				else if (limit)
				{
					ret = Long.MAX_VALUE;
				}
				else
				{
					throw new ArithmeticException(msg);
				}
			}
			else if (b > 0)
			{
				if ((Long.MIN_VALUE / b) <= a)
				{
					ret = a * b;
				}
				else if (limit)
				{
					ret = Long.MIN_VALUE;
				}
				else
				{
					throw new ArithmeticException(msg);
				}
			}
			else
			{
				ret = 0;
			}
		}
		else if (a > 0)
		{
			if (a <= (Long.MAX_VALUE / b))
			{
				ret = a * b;
			}
			else if (limit)
			{
				ret = Long.MAX_VALUE;
			}
			else
			{
				throw new ArithmeticException(msg);
			}
		}
		else
		{
			ret = 0;
		}
		return ret;
	}
}
