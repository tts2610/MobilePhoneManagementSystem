package utility;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Utilities for validating input values.
 * 
 * @author HoangNLM
 * @version 1.1
 */
public class ValUtils {
	//Config features to validate
	public static final int MIN_AGE = 1;
	public static final int MAX_AGE = 100;
	public static final int MIN_YEAR = 1581;	//Cannot valuate leap year before 1581
	public static final int MAX_YEAR = 2500;

	// For testing
	public static void main(String[] args) throws Exception {
		int t = 2;
		valRangedValue(t, 2, true, 5, true, "hehe", true);

	}

	public static void valUserName(String obj) {

	}

	public static void valPassword(String obj) {

	}

	/**
	 * Validate an email string. Not check for empty.
	 * 
	 * @param obj
	 * @param message
	 * @throws Exception
	 */
	public static void valEmail(String obj, String message) throws Exception {
		valFormat(obj, "\\b[\\w.-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b", "Invalid email format!");
	}

	/**
	 * Validate a Unicode name string without special characters.
	 * 
	 * @throws Exception
	 */
	public static void valUnicodeName() throws Exception {

	}

	/**
	 * Validate string phone number.
	 * 
	 * @param obj
	 * @param message
	 * @throws Exception
	 */
	public static void valPhone(String obj, String message) throws Exception {
		valFormat(obj, "\\d{10,12}|\\Q(+84)\\E\\d{9,11}", message!=null?message:"Invalid phone format!");
	}

	/**
	 * Validate a common age with a STRING input.
	 * 
	 * @param obj
	 * @param min
	 * @param max
	 * @param message
	 * @throws Exception
	 */
	public static void valAge(String obj, Integer min, Integer max, String message) throws Exception {
		valDate(obj);
		valRangedValue(min, MIN_AGE, "Min age must be from "+MIN_AGE+"!");
		valRangedValue(max, null, MAX_AGE, "Max age must be under "+MAX_AGE+"!");
		
		Calendar inputCal = Calendar.getInstance();
		Calendar nowCal = Calendar.getInstance();
		Calendar minCal = Calendar.getInstance();
		Calendar maxCal = Calendar.getInstance();
		
		inputCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(obj));
		minCal.set(nowCal.get(Calendar.YEAR)-min, nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DATE), 0, 0, 0);
		maxCal.set(nowCal.get(Calendar.YEAR)-max, nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DATE)-1, 0, 0, 0); //Ngay max phai giam 1 do exclusive
		
//		System.out.println(inputCal.getTime());
//		System.out.println(nowCal.getTime());
//		System.out.println(minCal.getTime());
//		System.out.println(maxCal.getTime());
		
		//inputCal phai truoc hoac bang minCal khi tuoi >= min
		//compareTo tra ve tri >0 khi inputCal nam sau minCal
		if(inputCal.compareTo(minCal)>0)
			throw new IllegalArgumentException(message!=null?message:"Age must be from "+min+"!");
		
		//inputCal phai sau hoac bang maxCal khi tuoi <= max
		//compareTo tra ve tri <0 khi inputCal nam truoc maxCal
		if(inputCal.compareTo(maxCal)<0)
			throw new IllegalArgumentException(message!=null?message:"Age must be under "+max+"!");
		
	}

	/**
	 * Validate date type from a string input.
	 * String format must be "yyyy-mm-dd".
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static void valDate(String obj) throws Exception {
		valFormat(obj, "\\d{4}-\\d{2}-\\d{2}", "Date format is [yyyy-mm-dd]!");
		
		//Chuyen ve format yyyyMMdd de tinh
		int date = Integer.parseInt(obj.replace("-", ""));
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int day = date % 100;

		// leap year calculation not valid before 1581
		if(!(MIN_YEAR<=year && year <= MAX_YEAR))
			throw new IllegalArgumentException("Year must be in "+MIN_YEAR+"-"+MAX_YEAR+"!");
		
		if(!(1<=month && month <= 12))
			throw new IllegalArgumentException("Month must be in 1-12!");
		
		if(!(1<=day && day <= daysInMonth(year, month)))
			throw new IllegalArgumentException("Day must be in 1-"+daysInMonth(year,month)+"!");
	}

	/**
	 * Validate for null/empty. Use for ONLY string and object type.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public static void valEmpty(Object obj) throws Exception {
		valEmpty(obj, null);
	}

	/**
	 * Validate for null/empty. Use for ONLY string and object type.
	 * 
	 * @param obj
	 * @param message
	 * @throws Exception
	 */
	public static void valEmpty(Object obj, String message) throws Exception {
		if (obj == null)
			throw new IllegalArgumentException(message != null ? message : "Object is null!");

		String[] types = { "Byte", "Short", "Integer", "Long", "Float", "Double" };

		if (Arrays.binarySearch(types, checkType(obj)) >= 0)
			throw new IllegalArgumentException("Object must be a string or an object type!");

		if (checkType(obj).equals("String"))
			if (obj.toString().trim().equals(""))
				throw new IllegalArgumentException(
				message != null ? message : "Object is empty!");
	}

	/**
	 * Validate value with a specified format. Use for ONLY string.
	 * 
	 * @param obj
	 * @param format
	 * @throws Exception
	 */
	public static void valFormat(String obj, String format) throws Exception {
		valFormat(obj, format, null, true);
	}

	/**
	 * Validate value with a specified format. Use for ONLY string.
	 * 
	 * @param obj
	 * @param format
	 * @param message
	 * @throws Exception
	 */
	public static void valFormat(String obj, String format, String message) throws Exception {
		valFormat(obj, format, message, true);
	}

	/**
	 * Validate value with a specified format. Use for ONLY string.
	 * 
	 * @param obj
	 * @param format
	 * @param message
	 * @param toMatch
	 * @throws Exception
	 */
	public static void valFormat(String obj, String format, String message, boolean toMatch)
	throws Exception {
		valEmpty(obj);
		valEmpty(format);

		if (toMatch) {
			if (!obj.matches(format))
				throw new IllegalArgumentException(
				message != null ? message : "Object format must be \"" + format + "\"!");
		} else {
			if (obj.matches(format))
				throw new IllegalArgumentException(
				message != null ? message : "Object format must not be \"" + format + "\"!");
		}
	}

	/**
	 * Validate value in a specified range. Use for ONLY number type.
	 * 
	 * @param obj
	 * @param min
	 * @throws Exception
	 */
	public static void valRangedValue(Object obj, Object min) throws Exception {
		valRangedValue(obj, min, true, null, true, null, true);
	}

	/**
	 * Validate value in a specified range. Use for ONLY number type.
	 * 
	 * @param obj
	 * @param min
	 * @param message
	 * @throws Exception
	 */
	public static void valRangedValue(Object obj, Object min, String message)
	throws Exception {
		valRangedValue(obj, min, true, null, true, message, true);
	}

	/**
	 * Validate value in a specified range. Use for ONLY number type.
	 * 
	 * @param obj
	 * @param min
	 * @param max
	 * @throws Exception
	 */
	public static void valRangedValue(Object obj, Object min, Object max) throws Exception {
		valRangedValue(obj, min, true, max, true, null, true);
	}

	/**
	 * Validate value in a specified range. Use for ONLY number type.
	 * 
	 * @param obj
	 * @param min
	 * @param max
	 * @param message
	 * @throws Exception
	 */
	public static void valRangedValue(Object obj, Object min, Object max, String message)
	throws Exception {
		valRangedValue(obj, min, true, max, true, message, true);
	}

	/**
	 * Validate value in a specified range. Use for ONLY number type.
	 * 
	 * @param obj
	 * @param min
	 * @param minInclusive
	 * @param max
	 * @param maxInclusive
	 * @param message
	 * @param inside
	 * @throws Exception
	 */
	public static void valRangedValue(Object obj, Object min, boolean minInclusive, Object max,
	boolean maxInclusive, String message, boolean inside) throws Exception {
		if (obj == null)
			throw new IllegalArgumentException("Object cannot be null!");

		String[] types = { "Byte", "Short", "Integer", "Long", "Float", "Double" };

		if (Arrays.binarySearch(types, checkType(obj)) < 0)
			throw new IllegalArgumentException("Object must be a number!");

		double obj1 = Double.parseDouble(obj.toString());

		if (min != null && max != null) {
			double min1 = Double.parseDouble(min.toString());
			double max1 = Double.parseDouble(max.toString());

			if (inside) {
				if (minInclusive && maxInclusive && !(min1 <= obj1 && obj1 <= max1))
					throw new IllegalArgumentException(message == null
					? "Object must be inside [" + min1 + ", " + max1 + "]!" : message);

				if (!minInclusive && maxInclusive && !(min1 < obj1 && obj1 <= max1))
					throw new IllegalArgumentException(message == null
					? "Object must be inside (" + min1 + ", " + max1 + "]!" : message);

				if (minInclusive && !maxInclusive && !(min1 <= obj1 && obj1 < max1))
					throw new IllegalArgumentException(message == null
					? "Object must be inside [" + min1 + ", " + max1 + ")!" : message);

				if (!minInclusive && !maxInclusive && !(min1 < obj1 && obj1 < max1))
					throw new IllegalArgumentException(message == null
					? "Object must be inside (" + min1 + ", " + max1 + ")!" : message);
			} else {
				if (minInclusive && maxInclusive && min1 <= obj1 && obj1 <= max1)
					throw new IllegalArgumentException(message == null
					? "Object must be outside [" + min1 + ", " + max1 + "]!" : message);

				if (!minInclusive && maxInclusive && min1 < obj1 && obj1 <= max1)
					throw new IllegalArgumentException(message == null
					? "Object must be outside (" + min1 + ", " + max1 + "]!" : message);

				if (minInclusive && !maxInclusive && min1 <= obj1 && obj1 < max1)
					throw new IllegalArgumentException(message == null
					? "Object must be outside [" + min1 + ", " + max1 + ")!" : message);

				if (!minInclusive && !maxInclusive && min1 < obj1 && obj1 < max1)
					throw new IllegalArgumentException(message == null
					? "Object must be outside (" + min1 + ", " + max1 + ")!" : message);
			}
		} else if (max == null) {
			double min1 = Double.parseDouble(min.toString());

			if (minInclusive && obj1 < min1)
				throw new IllegalArgumentException(
				message == null ? "Object must >=" + min1 + "!" : message);

			if (!minInclusive && obj1 <= min1)
				throw new IllegalArgumentException(
				message == null ? "Object must >" + min1 + "!" : message);
		} else if (min == null) {
			double max1 = Double.parseDouble(max.toString());

			if (maxInclusive && obj1 > max1)
				throw new IllegalArgumentException(
				message == null ? "Object must <=" + max1 + "!" : message);

			if (!maxInclusive && obj1 >= max1)
				throw new IllegalArgumentException(
				message == null ? "Object must <" + max1 + "!" : message);
		} else {
			throw new IllegalArgumentException("Min and Max cannot be concurrently null!");
		}
	}

	/**
	 * Validate value in a specified set of values.
	 * 
	 * @param obj
	 * @param values
	 * @throws Exception
	 */
	public static void valSelectedValues(Object obj, Object[] values) throws Exception {
		valSelectedValues(obj, values, null, true);
	}

	/**
	 * Validate value in a specified set of values.
	 * 
	 * @param obj
	 * @param values
	 * @param message
	 * @throws Exception
	 */
	public static void valSelectedValues(Object obj, Object[] values, String message)
	throws Exception {
		valSelectedValues(obj, values, message, true);
	}

	/**
	 * Validate value in a specified set of values.
	 * 
	 * @param obj
	 * @param values
	 * @param message
	 * @param toMatch
	 * @throws Exception
	 */
	public static void valSelectedValues(Object obj, Object[] values, String message,
	boolean toMatch) throws Exception {
		valEmpty(obj);
		valEmpty(values);

		if (values.length == 0)
			throw new IllegalArgumentException("Array argument is empty!");

		if (toMatch && Arrays.binarySearch(values, obj) < 0)
			throw new IllegalArgumentException(
			message != null ? message : "Object must be in " + Arrays.toString(values));

		if (!toMatch && Arrays.binarySearch(values, obj) >= 0)
			throw new IllegalArgumentException(
			message != null ? message : "Object must not be in " + Arrays.toString(values));
	}

	/* ======================NOT FOR VALIDATION===================== */
	public static int daysInMonth(int year, int month) {
		int daysInMonth;
		
		switch (month) {
		case 1: // fall through
		case 3: // fall through
		case 5: // fall through
		case 7: // fall through
		case 8: // fall through
		case 10: // fall through
		case 12:
			daysInMonth = 31;
			break;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
				daysInMonth = 29;
			} else {
				daysInMonth = 28;
			}
			break;
		default:
			// returns 30 even for nonexistant months
			daysInMonth = 30;
		}
		
		return daysInMonth;
	}

	/**
	 * Check class type of argument.
	 * 
	 * @param obj
	 * @return
	 */
	public static String checkType(Object obj) {
		String[] s = obj.getClass().toString().split("[.]");
		return s[s.length - 1];
	}
}
