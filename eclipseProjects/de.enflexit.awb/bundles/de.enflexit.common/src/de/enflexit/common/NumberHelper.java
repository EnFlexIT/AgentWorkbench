package de.enflexit.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;

/**
 * The Class NumberHelper provides static help functions to handle number and corresponding string formats.
 * Thus, methods like round or parse are provided.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @author Nils Loose - SOFTEC - ICB - University of Duisburg-Essen
 */
public class NumberHelper {

	// Use this precision for rounding decimal numbers if nothing else was specified. 
	private static final int DEFAULT_ROUNDING_PRECISION = 2;
	
	private static String decimalSeparator;

	/**
	 * Returns the local decimal separator.
	 * @return the decimal separator local
	 */
	public static String getLocalDecimalSeparator() {
		if (decimalSeparator==null) {
			DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
			DecimalFormatSymbols symbols=format.getDecimalFormatSymbols();
			char sep=symbols.getDecimalSeparator();
			decimalSeparator = Character.toString(sep);	
		}
		return decimalSeparator;
	}
	
	/**
	 * Determines the decimal separator from a number string.
	 * @param numberString the number string
	 * @return the decimal separator
	 */
	public static String getDecimalSeparator(String numberString) {
		
		if (numberString==null || numberString.isBlank()==true) return getLocalDecimalSeparator();
		
		int lastIndexOfPoint = numberString.lastIndexOf('.');
		int lasIndexOfComma =  numberString.lastIndexOf(',');
		
		if (lasIndexOfComma==-1 && lastIndexOfPoint==-1) return getLocalDecimalSeparator();

		// --- Check if comma or point occurs later -----------------
		if (lastIndexOfPoint>lasIndexOfComma) {
			return ".";
		} else {
			return ",";
		}
	}
	
	/**
	 * Parses the specified doubleString into a float. If the system specific 
	 * decimal separator is a comma and used in the String, it will be considered 
	 * and adjusted accordingly.  
	 *
	 * @param floatString the float string
	 * @return the float value or Null
	 */
	public static Float parseFloat(String floatString) {
		Float dblValue = null;
		if (floatString!=null && floatString.trim().isEmpty()==false) {
			String deciSep = NumberHelper.getDecimalSeparator(floatString);
			if (deciSep.equals(",") && floatString.contains(deciSep)) {
				floatString = floatString.replace(deciSep, ".");
			}
			try {
				dblValue = Float.parseFloat(floatString);
			} catch (Exception ex) {
			}
		}
		return dblValue;
	}
	/**
	 * Parses the specified doubleString into a double. If the system specific 
	 * decimal separator is a comma and used in the String, it will be considered 
	 * and adjusted accordingly.  
	 *
	 * @param doubleString the double string
	 * @return the double value or Null
	 */
	public static Double parseDouble(String doubleString) {
		Double dblValue = null;
		if (doubleString!=null && doubleString.trim().isEmpty()==false) {
			String deciSep = NumberHelper.getDecimalSeparator(doubleString);
			if (deciSep.equals(",") && doubleString.contains(deciSep)) {
				doubleString = doubleString.replace(deciSep, ".");
			}
			try {
				dblValue = Double.parseDouble(doubleString);
			} catch (Exception ex) {
			}
		}
		return dblValue;
	}
	/**
	 * Parses the specified integer string into an integer.
	 *
	 * @param integerString the integer string
	 * @return the int value 
	 */
	public static int parseInteger(String integerString) {
		int intValue = 0;
		if (integerString!=null && integerString.trim().isEmpty()==false) {
			try {
				intValue = Integer.parseInt(integerString);
			} catch (Exception ex) {
			}
		}
		return intValue;
	}
	/**
	 * Parses the specified integer string into an integer.
	 *
	 * @param integerString the integer string
	 * @return the long value; may be <code>null</code> 
	 */
	public static Long parseLong(String doubleString) {
		Long lngValue = null;
		if (doubleString!=null && doubleString.trim().isEmpty()==false) {
			try {
				lngValue = Long.parseLong(doubleString);
			} catch (Exception ex) {
			}
		}
		return lngValue;
	}
	
	/**
	 * Rounds a double value to the given precision.
	 * For example: <br> 
	 * <code> round(3.1415926535, 2)</code> will return 3.14
	 *
	 * @param doubleValue the double value
	 * @param precision the precision
	 * @return the double
	 */
	public static double round(double doubleValue, double precision) {
		return Math.round(doubleValue * Math.pow(10.0, precision)) / Math.pow(10.0, precision);
	}
	
	/**
	 * Rounds a double value to the precision that is specified in DEFAULT_ROUNDING_PRECISION.
	 * For example: <br> 
	 * With DEFAULT_ROUNDING_PRECISION=2, <code> round(3.1415926535)</code> will return 3.14
	 *
	 * @param doubleValue the double value
	 * @return the double
	 */
	public static double round(double doubleValue) {
		return Math.round(doubleValue * Math.pow(10.0, DEFAULT_ROUNDING_PRECISION)) / Math.pow(10.0, DEFAULT_ROUNDING_PRECISION);
	}
	
	/**
	 * Gets the number of decimal places behind comma of the specified double value.
	 * @param doubleValue the double value
	 * @return the number of decimal places
	 */
	public static int getNumberOfDecimalPlaces(double doubleValue) {
		DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(15);
        String toSplit = df.format(doubleValue);
		String[] split = toSplit.split(NumberHelper.getLocalDecimalSeparator());
		int decimalPlaces = 0;
		if (split.length==1) {
			decimalPlaces = 0; 
		} else {
			decimalPlaces = split[1].length();
		}
		return decimalPlaces;
	}
	
	/**
	 * Gets the exponential scale of a double value to the base of 10.<br><br> 
	 * For example:<br>
	 * 40000 will return 4,<br>
	 * 5000 will return 3,<br>
	 * 823 will return 2,<br>
	 * 10 will return 1,<br>
	 * 4 will return 0,<br>
	 * 0.1 will return -1,<br>
	 * 0.023 will return -2 and so on
	 * 
	 * @param doubleValue the double value to evaluate
	 * @return the exponential scale of double to the base of 10
	 */
	public static double getExponentialScaleOfDouble(double doubleValue) {
		
		double scale = 0;
		double doubleValueTmp = Math.abs(doubleValue);
		double doubleScaler = 0;
		
		if (doubleValueTmp==0 || (doubleValueTmp>=1 && doubleValueTmp<10) ) {
			return 0;
		} else if (doubleValueTmp>1) { 
			doubleScaler = 0.1;
		} else {
			doubleScaler = 10;
		}

		while (doubleValueTmp<1 || doubleValueTmp>10) {
			doubleValueTmp = doubleValueTmp * doubleScaler;
			scale++;
		}
		
		if (Math.abs(doubleValue) < 1) {
			scale = scale * (-1);
		}
		return scale;
	}
	
	/**
	 * Rounds the specified doubleValue to the nearest value defined by precision step.
	 * If the precision step value is 0, this method returns the original value
	 *
	 * @param doubleValue the double value
	 * @param precisionStepValue the precision step value
	 * @return the double
	 */
	public static double roundToPrecisionStep(double doubleValue, double precisionStepValue) {
		
		if (precisionStepValue==0) return doubleValue;
		
		double result = 0;
		double step = Math.abs(precisionStepValue);
		int stepScale = NumberHelper.getNumberOfDecimalPlaces(precisionStepValue);
		
		double comp1 = NumberHelper.round(step * Math.round(doubleValue / step), stepScale) ;
		double comp2 = 0;
		if (comp1 == doubleValue) {
			return comp1;
		} else if (comp1 < doubleValue) {
			comp2 = comp1 + step;
		} else {
			comp2 = comp1 - step;
		}
		if (Math.abs(doubleValue-comp1) == Math.abs(doubleValue-comp2)) {
			if (Math.signum(doubleValue)>0) {
				result = Math.max(comp1, comp2);
			} else {
				result = Math.min(comp1, comp2);
			}
		} else if (Math.abs(doubleValue-comp1) < Math.abs(doubleValue-comp2)) {
			result = comp1;
		} else {
			result = comp2;
		}
		return result;
	}
	
	/**
	 * Returns a random integer.
	 *
	 * @param fromBound the from bound
	 * @param toBound the to bound
	 * @return the random integer
	 */
	public static int getRandomInteger(int fromBound, int toBound) {   
		toBound++;   
		return (int) (Math.random() * (toBound - fromBound) + fromBound);   
	}
	/**
	 * Gets the random float.
	 *
	 * @param fromBound the from bound
	 * @param toBound the to bound
	 * @return the random float
	 */
	public static float getRandomFloat(float fromBound, float toBound) {   
		Random rand = new Random();
		return (rand.nextFloat() * (toBound - fromBound) + fromBound);   
	}
	/**
	 * Gets the random boolean.
	 * 
	 * @return the random boolean
	 */
	public static boolean getRandomBoolean() {
		Random rand  = new Random();
		return rand.nextBoolean();
	}
	
	/**
	 * Interpolate linear between to given value pairs of x and y points.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @param xSearch the x search
	 * @return the linear interpolated double
	 */
	public static double interpolateLinear(double x1, double y1, double x2, double y2, double xSearch) {
		double a = (y2-y1) / (x2-x1);
		double b = y1 - (a*x1);
		return (a*xSearch) + b;
	}
	
	/**
	 * Interpolate linear between to given value pairs of time and y points.
	 *
	 * @param time1 the time1
	 * @param y1 the y1
	 * @param time2 the time2
	 * @param y2 the y2
	 * @param timeSearch the time to which it should be interpolated
	 * @return the linear interpolated double
	 */
	public static double interpolateLinearOverTime(long time1, double y1, long time2, double y2, long timeSearch) {
		double t1 = (double) time1;
		double t2 = (double) time2;
		double a = (y2-y1) / (t2-t1);
		double b = y1 - (a*t1);
		return (a*timeSearch) + b;
	}
}
