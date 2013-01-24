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
package lineage2.commons.time.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class SchedulingPattern
{
	private static final int MINUTE_MIN_VALUE = 0;
	private static final int MINUTE_MAX_VALUE = 59;
	private static final int HOUR_MIN_VALUE = 0;
	private static final int HOUR_MAX_VALUE = 23;
	private static final int DAY_OF_MONTH_MIN_VALUE = 1;
	private static final int DAY_OF_MONTH_MAX_VALUE = 31;
	private static final int MONTH_MIN_VALUE = 1;
	private static final int MONTH_MAX_VALUE = 12;
	private static final int DAY_OF_WEEK_MIN_VALUE = 0;
	private static final int DAY_OF_WEEK_MAX_VALUE = 7;
	private static final ValueParser MINUTE_VALUE_PARSER = new MinuteValueParser();
	private static final ValueParser HOUR_VALUE_PARSER = new HourValueParser();
	private static final ValueParser DAY_OF_MONTH_VALUE_PARSER = new DayOfMonthValueParser();
	private static final ValueParser MONTH_VALUE_PARSER = new MonthValueParser();
	private static final ValueParser DAY_OF_WEEK_VALUE_PARSER = new DayOfWeekValueParser();
	
	public static boolean validate(String schedulingPattern)
	{
		try
		{
			new SchedulingPattern(schedulingPattern);
		}
		catch (InvalidPatternException e)
		{
			return false;
		}
		return true;
	}
	
	private final String asString;
	protected List<ValueMatcher> minuteMatchers = new ArrayList<>();
	protected List<ValueMatcher> hourMatchers = new ArrayList<>();
	protected List<ValueMatcher> dayOfMonthMatchers = new ArrayList<>();
	protected List<ValueMatcher> monthMatchers = new ArrayList<>();
	protected List<ValueMatcher> dayOfWeekMatchers = new ArrayList<>();
	protected int matcherSize = 0;
	
	public SchedulingPattern(String pattern) throws InvalidPatternException
	{
		asString = pattern;
		StringTokenizer st1 = new StringTokenizer(pattern, "|");
		if (st1.countTokens() < 1)
		{
			throw new InvalidPatternException("invalid pattern: \"" + pattern + "\"");
		}
		while (st1.hasMoreTokens())
		{
			String localPattern = st1.nextToken();
			StringTokenizer st2 = new StringTokenizer(localPattern, " \t");
			if (st2.countTokens() != 5)
			{
				throw new InvalidPatternException("invalid pattern: \"" + localPattern + "\"");
			}
			try
			{
				minuteMatchers.add(buildValueMatcher(st2.nextToken(), MINUTE_VALUE_PARSER));
			}
			catch (Exception e)
			{
				throw new InvalidPatternException("invalid pattern \"" + localPattern + "\". Error parsing minutes field: " + e.getMessage() + ".");
			}
			try
			{
				hourMatchers.add(buildValueMatcher(st2.nextToken(), HOUR_VALUE_PARSER));
			}
			catch (Exception e)
			{
				throw new InvalidPatternException("invalid pattern \"" + localPattern + "\". Error parsing hours field: " + e.getMessage() + ".");
			}
			try
			{
				dayOfMonthMatchers.add(buildValueMatcher(st2.nextToken(), DAY_OF_MONTH_VALUE_PARSER));
			}
			catch (Exception e)
			{
				throw new InvalidPatternException("invalid pattern \"" + localPattern + "\". Error parsing days of month field: " + e.getMessage() + ".");
			}
			try
			{
				monthMatchers.add(buildValueMatcher(st2.nextToken(), MONTH_VALUE_PARSER));
			}
			catch (Exception e)
			{
				throw new InvalidPatternException("invalid pattern \"" + localPattern + "\". Error parsing months field: " + e.getMessage() + ".");
			}
			try
			{
				dayOfWeekMatchers.add(buildValueMatcher(st2.nextToken(), DAY_OF_WEEK_VALUE_PARSER));
			}
			catch (Exception e)
			{
				throw new InvalidPatternException("invalid pattern \"" + localPattern + "\". Error parsing days of week field: " + e.getMessage() + ".");
			}
			matcherSize++;
		}
	}
	
	private ValueMatcher buildValueMatcher(String str, ValueParser parser) throws Exception
	{
		if ((str.length() == 1) && str.equals("*"))
		{
			return new AlwaysTrueValueMatcher();
		}
		List<Integer> values = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens())
		{
			String element = st.nextToken();
			List<Integer> local;
			try
			{
				local = parseListElement(element, parser);
			}
			catch (Exception e)
			{
				throw new Exception("invalid field \"" + str + "\", invalid element \"" + element + "\", " + e.getMessage());
			}
			for (Integer value : local)
			{
				if (!values.contains(value))
				{
					values.add(value);
				}
			}
		}
		if (values.size() == 0)
		{
			throw new Exception("invalid field \"" + str + "\"");
		}
		if (parser == DAY_OF_MONTH_VALUE_PARSER)
		{
			return new DayOfMonthValueMatcher(values);
		}
		return new IntArrayValueMatcher(values);
	}
	
	private List<Integer> parseListElement(String str, ValueParser parser) throws Exception
	{
		StringTokenizer st = new StringTokenizer(str, "/");
		int size = st.countTokens();
		if ((size < 1) || (size > 2))
		{
			throw new Exception("syntax error");
		}
		List<Integer> values;
		try
		{
			values = parseRange(st.nextToken(), parser);
		}
		catch (Exception e)
		{
			throw new Exception("invalid range, " + e.getMessage());
		}
		if (size == 2)
		{
			String dStr = st.nextToken();
			int div;
			try
			{
				div = Integer.parseInt(dStr);
			}
			catch (NumberFormatException e)
			{
				throw new Exception("invalid divisor \"" + dStr + "\"");
			}
			if (div < 1)
			{
				throw new Exception("non positive divisor \"" + div + "\"");
			}
			List<Integer> values2 = new ArrayList<>();
			for (int i = 0; i < values.size(); i += div)
			{
				values2.add(values.get(i));
			}
			return values2;
		}
		return values;
	}
	
	private List<Integer> parseRange(String str, ValueParser parser) throws Exception
	{
		if (str.equals("*"))
		{
			int min = parser.getMinValue();
			int max = parser.getMaxValue();
			List<Integer> values = new ArrayList<>();
			for (int i = min; i <= max; i++)
			{
				values.add(new Integer(i));
			}
			return values;
		}
		StringTokenizer st = new StringTokenizer(str, "-");
		int size = st.countTokens();
		if ((size < 1) || (size > 2))
		{
			throw new Exception("syntax error");
		}
		String v1Str = st.nextToken();
		int v1;
		try
		{
			v1 = parser.parse(v1Str);
		}
		catch (Exception e)
		{
			throw new Exception("invalid value \"" + v1Str + "\", " + e.getMessage());
		}
		if (size == 1)
		{
			List<Integer> values = new ArrayList<>();
			values.add(new Integer(v1));
			return values;
		}
		String v2Str = st.nextToken();
		int v2;
		try
		{
			v2 = parser.parse(v2Str);
		}
		catch (Exception e)
		{
			throw new Exception("invalid value \"" + v2Str + "\", " + e.getMessage());
		}
		List<Integer> values = new ArrayList<>();
		if (v1 < v2)
		{
			for (int i = v1; i <= v2; i++)
			{
				values.add(new Integer(i));
			}
		}
		else if (v1 > v2)
		{
			int min = parser.getMinValue();
			int max = parser.getMaxValue();
			for (int i = v1; i <= max; i++)
			{
				values.add(new Integer(i));
			}
			for (int i = min; i <= v2; i++)
			{
				values.add(new Integer(i));
			}
		}
		else
		{
			values.add(new Integer(v1));
		}
		return values;
	}
	
	public boolean match(TimeZone timezone, long millis)
	{
		GregorianCalendar gc = new GregorianCalendar(timezone);
		gc.setTimeInMillis(millis);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		int minute = gc.get(Calendar.MINUTE);
		int hour = gc.get(Calendar.HOUR_OF_DAY);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		int month = gc.get(Calendar.MONTH) + 1;
		int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK) - 1;
		int year = gc.get(Calendar.YEAR);
		for (int i = 0; i < matcherSize; i++)
		{
			ValueMatcher minuteMatcher = minuteMatchers.get(i);
			ValueMatcher hourMatcher = hourMatchers.get(i);
			ValueMatcher dayOfMonthMatcher = dayOfMonthMatchers.get(i);
			ValueMatcher monthMatcher = monthMatchers.get(i);
			ValueMatcher dayOfWeekMatcher = dayOfWeekMatchers.get(i);
			boolean eval = minuteMatcher.match(minute) && hourMatcher.match(hour) && ((dayOfMonthMatcher instanceof DayOfMonthValueMatcher) ? ((DayOfMonthValueMatcher) dayOfMonthMatcher).match(dayOfMonth, month, gc.isLeapYear(year)) : dayOfMonthMatcher.match(dayOfMonth)) && monthMatcher.match(month) && dayOfWeekMatcher.match(dayOfWeek);
			if (eval)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean match(long millis)
	{
		return match(TimeZone.getDefault(), millis);
	}
	
	public long next(TimeZone timezone, long millis)
	{
		long next = -1L;
		for (int i = 0; i < matcherSize; i++)
		{
			GregorianCalendar gc = new GregorianCalendar(timezone);
			gc.setTimeInMillis(millis);
			gc.set(Calendar.SECOND, 0);
			gc.set(Calendar.MILLISECOND, 0);
			ValueMatcher minuteMatcher = minuteMatchers.get(i);
			ValueMatcher hourMatcher = hourMatchers.get(i);
			ValueMatcher dayOfMonthMatcher = dayOfMonthMatchers.get(i);
			ValueMatcher monthMatcher = monthMatchers.get(i);
			ValueMatcher dayOfWeekMatcher = dayOfWeekMatchers.get(i);
			loop:
			for (;;)
			{
				int year = gc.get(Calendar.YEAR);
				boolean isLeapYear = gc.isLeapYear(year);
				for (int month = gc.get(Calendar.MONTH) + 1; month <= MONTH_MAX_VALUE; month++)
				{
					if (monthMatcher.match(month))
					{
						gc.set(Calendar.MONTH, month - 1);
						int maxDayOfMonth = DayOfMonthValueMatcher.getLastDayOfMonth(month, isLeapYear);
						for (int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH); dayOfMonth <= maxDayOfMonth; dayOfMonth++)
						{
							if ((dayOfMonthMatcher instanceof DayOfMonthValueMatcher) ? ((DayOfMonthValueMatcher) dayOfMonthMatcher).match(dayOfMonth, month, isLeapYear) : dayOfMonthMatcher.match(dayOfMonth))
							{
								gc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
								int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK) - 1;
								if (dayOfWeekMatcher.match(dayOfWeek))
								{
									for (int hour = gc.get(Calendar.HOUR_OF_DAY); hour <= HOUR_MAX_VALUE; hour++)
									{
										if (hourMatcher.match(hour))
										{
											gc.set(Calendar.HOUR_OF_DAY, hour);
											for (int minute = gc.get(Calendar.MINUTE); minute <= MINUTE_MAX_VALUE; minute++)
											{
												if (minuteMatcher.match(minute))
												{
													gc.set(Calendar.MINUTE, minute);
													long next0 = gc.getTimeInMillis();
													if ((next == -1L) || (next0 < next))
													{
														next = next0;
													}
													break loop;
												}
											}
										}
										gc.set(Calendar.MINUTE, MINUTE_MIN_VALUE);
									}
								}
							}
							gc.set(Calendar.HOUR_OF_DAY, HOUR_MIN_VALUE);
							gc.set(Calendar.MINUTE, MINUTE_MIN_VALUE);
						}
					}
					gc.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH_MIN_VALUE);
					gc.set(Calendar.HOUR_OF_DAY, HOUR_MIN_VALUE);
					gc.set(Calendar.MINUTE, MINUTE_MIN_VALUE);
				}
				gc.set(Calendar.MONTH, MONTH_MIN_VALUE - 1);
				gc.set(Calendar.HOUR_OF_DAY, HOUR_MIN_VALUE);
				gc.set(Calendar.MINUTE, MINUTE_MIN_VALUE);
				gc.roll(Calendar.YEAR, true);
			}
		}
		return next;
	}
	
	public long next(long millis)
	{
		return next(TimeZone.getDefault(), millis);
	}
	
	@Override
	public String toString()
	{
		return asString;
	}
	
	static int parseAlias(String value, String[] aliases, int offset) throws Exception
	{
		for (int i = 0; i < aliases.length; i++)
		{
			if (aliases[i].equalsIgnoreCase(value))
			{
				return offset + i;
			}
		}
		throw new Exception("invalid alias \"" + value + "\"");
	}
	
	public class InvalidPatternException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
		
		InvalidPatternException()
		{
		}
		
		InvalidPatternException(String message)
		{
			super(message);
		}
	}
	
	private interface ValueParser
	{
		public int parse(String value) throws Exception;
		
		public int getMinValue();
		
		public int getMaxValue();
	}
	
	private static class SimpleValueParser implements ValueParser
	{
		protected int minValue;
		protected int maxValue;
		
		public SimpleValueParser(int minValue, int maxValue)
		{
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
		
		@Override
		public int parse(String value) throws Exception
		{
			int i;
			try
			{
				i = Integer.parseInt(value);
			}
			catch (NumberFormatException e)
			{
				throw new Exception("invalid integer value");
			}
			if ((i < minValue) || (i > maxValue))
			{
				throw new Exception("value out of range");
			}
			return i;
		}
		
		@Override
		public int getMinValue()
		{
			return minValue;
		}
		
		@Override
		public int getMaxValue()
		{
			return maxValue;
		}
	}
	
	private static class MinuteValueParser extends SimpleValueParser
	{
		public MinuteValueParser()
		{
			super(MINUTE_MIN_VALUE, MINUTE_MAX_VALUE);
		}
	}
	
	private static class HourValueParser extends SimpleValueParser
	{
		public HourValueParser()
		{
			super(HOUR_MIN_VALUE, HOUR_MAX_VALUE);
		}
	}
	
	private static class DayOfMonthValueParser extends SimpleValueParser
	{
		public DayOfMonthValueParser()
		{
			super(DAY_OF_MONTH_MIN_VALUE, DAY_OF_MONTH_MAX_VALUE);
		}
		
		@Override
		public int parse(String value) throws Exception
		{
			if (value.equalsIgnoreCase("L"))
			{
				return 32;
			}
			return super.parse(value);
		}
	}
	
	private static class MonthValueParser extends SimpleValueParser
	{
		private static String[] ALIASES =
		{
			"jan",
			"feb",
			"mar",
			"apr",
			"may",
			"jun",
			"jul",
			"aug",
			"sep",
			"oct",
			"nov",
			"dec"
		};
		
		public MonthValueParser()
		{
			super(MONTH_MIN_VALUE, MONTH_MAX_VALUE);
		}
		
		@Override
		public int parse(String value) throws Exception
		{
			try
			{
				return super.parse(value);
			}
			catch (Exception e)
			{
				return parseAlias(value, ALIASES, 1);
			}
		}
	}
	
	private static class DayOfWeekValueParser extends SimpleValueParser
	{
		private static String[] ALIASES =
		{
			"sun",
			"mon",
			"tue",
			"wed",
			"thu",
			"fri",
			"sat"
		};
		
		public DayOfWeekValueParser()
		{
			super(DAY_OF_WEEK_MIN_VALUE, DAY_OF_WEEK_MAX_VALUE);
		}
		
		@Override
		public int parse(String value) throws Exception
		{
			try
			{
				return super.parse(value) % 7;
			}
			catch (Exception e)
			{
				return parseAlias(value, ALIASES, 0);
			}
		}
	}
	
	private interface ValueMatcher
	{
		public boolean match(int value);
	}
	
	private static class AlwaysTrueValueMatcher implements ValueMatcher
	{
		public AlwaysTrueValueMatcher()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean match(int value)
		{
			return true;
		}
	}
	
	private static class IntArrayValueMatcher implements ValueMatcher
	{
		private final int[] values;
		
		public IntArrayValueMatcher(List<Integer> integers)
		{
			int size = integers.size();
			values = new int[size];
			for (int i = 0; i < size; i++)
			{
				try
				{
					values[i] = integers.get(i).intValue();
				}
				catch (Exception e)
				{
					throw new IllegalArgumentException(e.getMessage());
				}
			}
		}
		
		@Override
		public boolean match(int value)
		{
			for (int value2 : values)
			{
				if (value2 == value)
				{
					return true;
				}
			}
			return false;
		}
	}
	
	private static class DayOfMonthValueMatcher extends IntArrayValueMatcher
	{
		private static final int[] lastDays =
		{
			31,
			28,
			31,
			30,
			31,
			30,
			31,
			31,
			30,
			31,
			30,
			31
		};
		
		public DayOfMonthValueMatcher(List<Integer> integers)
		{
			super(integers);
		}
		
		public boolean match(int value, int month, boolean isLeapYear)
		{
			return (super.match(value) || ((value > 27) && match(32) && isLastDayOfMonth(value, month, isLeapYear)));
		}
		
		public static int getLastDayOfMonth(int month, boolean isLeapYear)
		{
			if (isLeapYear && (month == 2))
			{
				return 29;
			}
			return lastDays[month - 1];
		}
		
		public static boolean isLastDayOfMonth(int value, int month, boolean isLeapYear)
		{
			return value == getLastDayOfMonth(month, isLeapYear);
		}
	}
}
