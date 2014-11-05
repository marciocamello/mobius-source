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
package lineage2.gameserver.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.items.etcitems.AppearanceStone.ShapeTargetType;
import lineage2.gameserver.model.reward.RewardList;
import lineage2.gameserver.templates.item.ExItemType;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Util
{
	private static final String PATTERN = "0.0000000000E00";
	private static final DecimalFormat df;
	private static NumberFormat adenaFormatter;
	static
	{
		adenaFormatter = NumberFormat.getIntegerInstance(Locale.FRANCE);
		df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.applyPattern(PATTERN);
		df.setPositivePrefix("+");
	}
	
	/**
	 * Method isMatchingRegexp.
	 * @param text String
	 * @param template String
	 * @return boolean
	 */
	public static boolean isMatchingRegexp(String text, String template)
	{
		Pattern pattern = null;
		
		try
		{
			pattern = Pattern.compile(template);
		}
		catch (PatternSyntaxException e)
		{
			e.printStackTrace();
		}
		
		if (pattern == null)
		{
			return false;
		}
		
		Matcher regexp = pattern.matcher(text);
		return regexp.matches();
	}
	
	/**
	 * Method formatDouble.
	 * @param x double
	 * @param nanString String
	 * @param forceExponents boolean
	 * @return String
	 */
	public static String formatDouble(double x, String nanString, boolean forceExponents)
	{
		if (Double.isNaN(x))
		{
			return nanString;
		}
		
		if (forceExponents)
		{
			return df.format(x);
		}
		
		if ((long) x == x)
		{
			return String.valueOf((long) x);
		}
		
		return String.valueOf(x);
	}
	
	/**
	 * Method formatAdena.
	 * @param amount long
	 * @return String
	 */
	public static String formatAdena(long amount)
	{
		return adenaFormatter.format(amount);
	}
	
	/**
	 * Method formatTime.
	 * @param time int
	 * @return String
	 */
	public static String formatTime(int time)
	{
		if (time == 0)
		{
			return "now";
		}
		
		time = Math.abs(time);
		String ret = "";
		long numDays = time / 86400;
		time -= numDays * 86400;
		long numHours = time / 3600;
		time -= numHours * 3600;
		long numMins = time / 60;
		time -= numMins * 60;
		long numSeconds = time;
		
		if (numDays > 0)
		{
			ret += numDays + "d ";
		}
		
		if (numHours > 0)
		{
			ret += numHours + "h ";
		}
		
		if (numMins > 0)
		{
			ret += numMins + "m ";
		}
		
		if (numSeconds > 0)
		{
			ret += numSeconds + "s";
		}
		
		return ret.trim();
	}
	
	/**
	 * Method rollDrop.
	 * @param min long
	 * @param max long
	 * @param calcChance double
	 * @param rate boolean
	 * @return long
	 */
	public static long rollDrop(long min, long max, double calcChance, boolean rate)
	{
		if ((calcChance <= 0) || (min <= 0) || (max <= 0))
		{
			return 0;
		}
		
		int dropmult = 1;
		
		if (rate)
		{
			calcChance *= Config.RATE_DROP_ITEMS;
		}
		
		if (calcChance > RewardList.MAX_CHANCE)
		{
			if ((calcChance % RewardList.MAX_CHANCE) == 0)
			{
				dropmult = (int) (calcChance / RewardList.MAX_CHANCE);
			}
			else
			{
				dropmult = (int) Math.ceil(calcChance / RewardList.MAX_CHANCE);
				calcChance = calcChance / dropmult;
			}
		}
		
		return Rnd.chance(calcChance / 10000.) ? Rnd.get(min * dropmult, max * dropmult) : 0;
	}
	
	/**
	 * Method packInt.
	 * @param a int[]
	 * @param bits int
	 * @return int
	 * @throws Exception
	 */
	public static int packInt(int[] a, int bits) throws Exception
	{
		int m = 32 / bits;
		
		if (a.length > m)
		{
			throw new Exception("Overflow");
		}
		
		int result = 0;
		int next;
		int mval = (int) Math.pow(2, bits);
		
		for (int i = 0; i < m; i++)
		{
			result <<= bits;
			
			if (a.length > i)
			{
				next = a[i];
				
				if ((next >= mval) || (next < 0))
				{
					throw new Exception("Overload, value is out of range");
				}
			}
			else
			{
				next = 0;
			}
			
			result += next;
		}
		
		return result;
	}
	
	/**
	 * Method packLong.
	 * @param a int[]
	 * @param bits int
	 * @return long
	 * @throws Exception
	 */
	public static long packLong(int[] a, int bits) throws Exception
	{
		int m = 64 / bits;
		
		if (a.length > m)
		{
			throw new Exception("Overflow");
		}
		
		long result = 0;
		int next;
		int mval = (int) Math.pow(2, bits);
		
		for (int i = 0; i < m; i++)
		{
			result <<= bits;
			
			if (a.length > i)
			{
				next = a[i];
				
				if ((next >= mval) || (next < 0))
				{
					throw new Exception("Overload, value is out of range");
				}
			}
			else
			{
				next = 0;
			}
			
			result += next;
		}
		
		return result;
	}
	
	/**
	 * Method unpackInt.
	 * @param a int
	 * @param bits int
	 * @return int[]
	 */
	public static int[] unpackInt(int a, int bits)
	{
		int m = 32 / bits;
		int mval = (int) Math.pow(2, bits);
		int[] result = new int[m];
		int next;
		
		for (int i = m; i > 0; i--)
		{
			next = a;
			a = a >> bits;
			result[i - 1] = next - (a * mval);
		}
		
		return result;
	}
	
	/**
	 * Method unpackLong.
	 * @param a long
	 * @param bits int
	 * @return int[]
	 */
	public static int[] unpackLong(long a, int bits)
	{
		int m = 64 / bits;
		int mval = (int) Math.pow(2, bits);
		int[] result = new int[m];
		long next;
		
		for (int i = m; i > 0; i--)
		{
			next = a;
			a = a >> bits;
			result[i - 1] = (int) (next - (a * mval));
		}
		
		return result;
	}
	
	/**
	 * Method joinStrings.
	 * @param glueStr String
	 * @param strings String[]
	 * @param startIdx int
	 * @param maxCount int
	 * @return String
	 */
	public static String joinStrings(String glueStr, String[] strings, int startIdx, int maxCount)
	{
		return Strings.joinStrings(glueStr, strings, startIdx, maxCount);
	}
	
	/**
	 * Method joinStrings.
	 * @param glueStr String
	 * @param strings String[]
	 * @param startIdx int
	 * @return String
	 */
	public static String joinStrings(String glueStr, String[] strings, int startIdx)
	{
		return Strings.joinStrings(glueStr, strings, startIdx, -1);
	}
	
	/**
	 * Method isNumber.
	 * @param s String
	 * @return boolean
	 */
	public static boolean isNumber(String s)
	{
		try
		{
			Double.parseDouble(s);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method dumpObject.
	 * @param o Object
	 * @param simpleTypes boolean
	 * @param parentFields boolean
	 * @param ignoreStatics boolean
	 * @return String
	 */
	public static String dumpObject(Object o, boolean simpleTypes, boolean parentFields, boolean ignoreStatics)
	{
		Class<?> cls = o.getClass();
		String val, type, result = "[" + (simpleTypes ? cls.getSimpleName() : cls.getName()) + "\n";
		Object fldObj;
		List<Field> fields = new ArrayList<>();
		
		while (cls != null)
		{
			for (Field fld : cls.getDeclaredFields())
			{
				if (!fields.contains(fld))
				{
					if (ignoreStatics && Modifier.isStatic(fld.getModifiers()))
					{
						continue;
					}
					
					fields.add(fld);
				}
			}
			
			cls = cls.getSuperclass();
			
			if (!parentFields)
			{
				break;
			}
		}
		
		for (Field fld : fields)
		{
			fld.setAccessible(true);
			
			try
			{
				fldObj = fld.get(o);
				
				if (fldObj == null)
				{
					val = "NULL";
				}
				else
				{
					val = fldObj.toString();
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
				val = "<ERROR>";
			}
			
			type = simpleTypes ? fld.getType().getSimpleName() : fld.getType().toString();
			result += String.format("\t%s [%s] = %s;\n", fld.getName(), type, val);
		}
		
		result += "]\n";
		return result;
	}
	
	private static final Pattern _pattern = Pattern.compile("<!--TEMPLET(\\d+)(.*?)TEMPLET-->", Pattern.DOTALL);
	
	/**
	 * Method parseTemplate.
	 * @param html String
	 * @return HashMap<Integer,String>
	 */
	public static HashMap<Integer, String> parseTemplate(String html)
	{
		Matcher m = _pattern.matcher(html);
		HashMap<Integer, String> tpls = new HashMap<>();
		
		while (m.find())
		{
			tpls.put(Integer.parseInt(m.group(1)), m.group(2));
			html = html.replace(m.group(0), "");
		}
		
		tpls.put(0, html);
		return tpls;
	}
	
	/**
	 * Method getThirdClassForId.
	 * @param classId int
	 * @return int
	 */
	public static int getThirdClassForId(int classId)
	{
		int result = -1;
		
		switch (classId)
		{
			case 30:
				result = 150;
				break;
			
			case 20:
				result = 99;
				break;
			
			case 8:
				result = 93;
				break;
			
			case 14:
				result = 96;
				break;
			
			case 12:
				result = 94;
				break;
			
			case 16:
				result = 97;
				break;
			
			case 51:
				result = 115;
				break;
			
			case 127:
				result = 131;
				break;
			
			case 52:
				result = 116;
				break;
			
			case 3:
				result = 89;
				break;
			
			case 2:
				result = 88;
				break;
			
			case 28:
				result = 104;
				break;
			
			case 55:
				result = 117;
				break;
			
			case 36:
				result = 108;
				break;
			
			case 37:
				result = 109;
				break;
			
			case 48:
				result = 114;
				break;
			
			case 6:
				result = 91;
				break;
			
			case 17:
				result = 98;
				break;
			
			case 57:
				result = 118;
				break;
			
			case 24:
				result = 102;
				break;
			
			case 27:
				result = 103;
				break;
			
			case 5:
				result = 90;
				break;
			
			case 9:
				result = 92;
				break;
			
			case 43:
				result = 112;
				break;
			
			case 33:
				result = 106;
				break;
			
			case 128:
				result = 132;
				break;
			
			case 129:
				result = 133;
				break;
			
			case 13:
				result = 95;
				break;
			
			case 34:
				result = 107;
				break;
			
			case 41:
				result = 111;
				break;
			
			case 40:
				result = 110;
				break;
			
			case 21:
				result = 100;
				break;
			
			case 46:
				result = 113;
				break;
			
			case 130:
				result = 134;
				break;
			
			case 23:
				result = 101;
				break;
			
			case 4:
			case 7:
			case 10:
			case 11:
			case 15:
			case 18:
			case 19:
			case 22:
			case 25:
			case 26:
			case 29:
			case 31:
			case 32:
			case 35:
			case 38:
			case 39:
			case 42:
			case 44:
			case 45:
			case 47:
			case 49:
			case 50:
			case 53:
			case 54:
			case 56:
			case 58:
			case 59:
			case 60:
			case 61:
			case 62:
			case 63:
			case 64:
			case 65:
			case 66:
			case 67:
			case 68:
			case 69:
			case 70:
			case 71:
			case 72:
			case 73:
			case 74:
			case 75:
			case 76:
			case 77:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
			case 100:
			case 101:
			case 102:
			case 103:
			case 104:
			case 105:
			case 106:
			case 107:
			case 108:
			case 109:
			case 110:
			case 111:
			case 112:
			case 113:
			case 114:
			case 115:
			case 116:
			case 117:
			case 118:
			case 119:
			case 120:
			case 121:
			case 122:
			case 123:
			case 124:
			case 125:
			case 126:
		}
		
		return result;
	}
	
	public static final double convertHeadingToDegree(int clientHeading)
	{
		double degree = clientHeading / 182.044444444;
		return degree;
	}
	
	/**
	 * @param array - the array to look into
	 * @param obj - the integer to search for
	 * @return {@code true} if the {@code array} contains the {@code obj}, {@code false} otherwise
	 */
	public static boolean contains(int[] array, int obj)
	{
		for (int element : array)
		{
			if (element == obj)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param zones - the zones to look into
	 * @param zone - the zone to search for
	 * @return {@code true} if the {@code zones} contains the {@code zone}, {@code false} otherwise
	 */
	public static boolean contains(Zone[] zones, Zone zone)
	{
		for (Zone element : zones)
		{
			if (element == zone)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param grades - the grades to look into
	 * @param grade - the grade to search for
	 * @return {@code true} if the {@code grades} contains the {@code grade}, {@code false} otherwise
	 */
	public static boolean contains(Grade[] grades, Grade grade)
	{
		for (Grade element : grades)
		{
			if (element == grade)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param classIds - the classIds to look into
	 * @param classId - the classId to search for
	 * @return {@code true} if the {@code classIds} contains the {@code classId}, {@code false} otherwise
	 */
	public static boolean contains(ClassId[] classIds, ClassId classId)
	{
		for (ClassId element : classIds)
		{
			if (element == classId)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param skills - the skills to look into
	 * @param skill - the skill to search for
	 * @return {@code true} if the {@code skills} contains the {@code skill}, {@code false} otherwise
	 */
	public static boolean contains(Skill[] skills, Skill skill)
	{
		for (Skill element : skills)
		{
			if (element == skill)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param shapeTargetTypes - the shapeTargetTypes to look into
	 * @param shapeTargetType - the shapeTargetType to search for
	 * @return {@code true} if the {@code shapeTargetTypes} contains the {@code shapeTargetType}, {@code false} otherwise
	 */
	public static boolean contains(ShapeTargetType[] shapeTargetTypes, ShapeTargetType shapeTargetType)
	{
		for (ShapeTargetType element : shapeTargetTypes)
		{
			if (element == shapeTargetType)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param itemTypes - the itemTypes to look into
	 * @param itemType - the itemType to search for
	 * @return {@code true} if the {@code itemTypes} contains the {@code itemType}, {@code false} otherwise
	 */
	public static boolean contains(ExItemType[] itemTypes, ExItemType itemType)
	{
		for (ExItemType element : itemTypes)
		{
			if (element == itemType)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method to generate the hexadecimal representation of a byte array.<br>
	 * 16 bytes per row, while ascii chars or "." is shown at the end of the line.
	 * @param data the byte array to be represented in hexadecimal representation
	 * @param len the number of bytes to represent in hexadecimal representation
	 * @return byte array represented in hexadecimal format
	 */
	public static String printData(byte[] data, int len)
	{
		return new String(HexUtils.bArr2HexEdChars(data, len));
	}
}
